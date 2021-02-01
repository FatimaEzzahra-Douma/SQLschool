/*******************************************************************************
 * 	Java tool with a GUI to help learn SQL
 * 	
 *     Copyright (C) 2020  Bayad Nasr-eddine, Bayol Thibaud, Benazzi Naima, 
 *     Douma Fatima Ezzahra, Chaouche Sonia, Kanyamibwa Blandine
 *     (thesqlschool@hotmail.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package apprendreSQL.View;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import apprendreSQL.Model.*;
import apprendreSQL.Controller.DiagramUtils;
import apprendreSQL.Controller.EventManager;

/**
 * 
 * This is the panel that lists the available exercises and their respective
 * databases.
 * 
 */

@SuppressWarnings("serial")
public class ExercisesPanel extends JPanel implements TreeSelectionListener {

	private static final String os = System.getProperty("os.name");
	private final String treeRootName = "Bases de données";
	private String etat;
	private JTree hierarchyView;
	private ArrayList<DataBase> database;
	private ArrayList<TreePath> treePaths;
	private EventManager manager;
	private DefaultMutableTreeNode top;
	
	public ExercisesPanel(EventManager manager) {
		init(manager);
	}

	/**
	 * Initialize our swing components.
	 * 
	 * @param manager object that coordinates, edits and transfers information
	 *                between the different swing objects of our project
	 */
	private void init(EventManager manager) {
		this.manager = manager;
		setLayout(new BorderLayout());
		Border border = BorderFactory.createTitledBorder("Choix de l'exercice");
		setBorder(border);

		database = new ArrayList<DataBase>();
		for (String name : manager.getDbFiles())
			this.database.add(new DataBase(name, manager));

		treePaths = new ArrayList<TreePath>();

		top = new DefaultMutableTreeNode(treeRootName);
		try {
			createNodes(top);
		} catch (IOException e) {
			e.printStackTrace();
		}
		hierarchyView = new JTree(top);
		hierarchyView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		hierarchyView.addTreeSelectionListener(this);
		JScrollPane treeScrollPane = new JScrollPane(hierarchyView);
		treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		hierarchyView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				resetDatabase(me);
			}
		});
		add(hierarchyView);

	}

	/**
	 * Creates a tree-like exercises explorer and populates it with questions
	 * extracted from a json file with the help of manager object.
	 * 
	 * @param root the root of the tree view
	 * @throws IOException
	 */
	private void createNodes(DefaultMutableTreeNode root) throws IOException {

		root.removeAllChildren();

		DefaultMutableTreeNode databaseName = null;
		DefaultMutableTreeNode exerciceName = null;
		DefaultMutableTreeNode subjectName = null;
		ArrayList<Question> questionsList = manager.getQuestionsList();
		Object[] subjectDB;

		for (DataBase db : database) {
			databaseName = new DefaultMutableTreeNode(db.getNameDatabase());
			root.add(databaseName);

			subjectDB = questionsList.stream().filter((q) -> {
				return q.getDatabase().contentEquals(db.getNameDatabase());
			}).map((q) -> q.getSubject()).distinct().toArray();
			
			for (Object subject : subjectDB) {
				subjectName = new DefaultMutableTreeNode(subject.toString());
				databaseName.add(subjectName);

				for (Question question : questionsList) {
					exerciceName = new DefaultMutableTreeNode(question.getTitleQuestion());
					if (db.getNameDatabase().equals(question.getDatabase())) {
						if (subject.toString().equals(question.getSubject())) {
							subjectName.add(exerciceName);
						}
					}
				}
			}

		}
	}

	/**
	 * Listens for user actions on the tree view and either starts an exercise or
	 * calls the updateViews method.
	 * 
	 * @param e user action
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) hierarchyView.getLastSelectedPathComponent();

		try {
			createNodes(top);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String dbName = "";
		if (node == null)
			return;

		if (node.isLeaf()) {

			if (node.getParent() != null && node.getParent().getParent() != null) {
				dbName = node.getParent().getParent().toString();
				manager.callQuestion(node.getParent().getParent().toString(), node.getParent().toString(),
						node.toString());
				manager.showEditor();
				manager.clearInput();

			} else {
				dbName = node.toString();
			}

		} else if (node.isRoot()) {
			dbName = node.toString();

		} else {
			if (node.getParent() != null)
				dbName = node.getParent().toString();
		}

		if (node != null && node.toString() != treeRootName && node.getLevel() == 0) {
			updateViews(dbName);
		}

	}

	/**
	 * Updates both tables and diagram's view ports.
	 * 
	 * @param dbName selected database name
	 */
	private void updateViews(String dbName) {
		manager.populateTablesView(new DataBase(dbName, manager));

		if (os.toLowerCase().contains("windows")) {

			if (new DiagramUtils().checkInstallation()) {
				if (new DiagramUtils().checkDirectory()) {

					new DiagramUtils().cleanDirectory();
					new DiagramUtils().safeDatabaseTransfer(dbName);
					final String database = dbName;
					new Thread(() -> {
						try {
							manager.showProgress();
							new DiagramUtils().generateDiagram(database);
							manager.hideProgress();
						} catch (IOException e) {
							e.printStackTrace();
						}
						manager.updateDiagram(true);
					}).start();

				} else {
					manager.notifyUnavailableDirectory();
				}
			} else {
				manager.updateDiagram(false);
			}
		} else {
			manager.notifyIncompatible();
		}
	}

	/**
	 * Updates the tree view whenever the Watcher class sends a signal.
	 * 
	 * @throws IOException File exceptions handling
	 */
	void updateExercicesView() throws IOException {
		DefaultTreeModel model = (DefaultTreeModel) hierarchyView.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		createNodes(root);
		model.reload(root);
	}

	public String getEtat() {
		return etat;
	}

	/**
	 * Method that resets the database file
	 * 
	 * @param mouseEvent
	 */
	void resetDatabase(MouseEvent mouseEvent) {
		TreePath treePath = hierarchyView.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
		treePaths.add(treePath);
		if (treePath != null) {
			if (treePaths.size() > 1 && !treePath.equals(treePaths.get(treePaths.size() - 2))) {
				if (manager.getSelectedConnection() != null) {
					manager.getSelectedConnection().resetDatabase();
				}
			}
		}
	}

}
