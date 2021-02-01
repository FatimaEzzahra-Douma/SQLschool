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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import apprendreSQL.Controller.DiagramUtils;
import apprendreSQL.Controller.EventManager;
import apprendreSQL.Controller.UtilitiesFactory;
import apprendreSQL.Model.Table;

/**
 * This is the main frame that contains all the swing components of the
 * application.
 * 
 */

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener {

	private static final String URL = "https://github.com/bayad-ne/SQLschool.git";
	private static final String title = "SQLschool";
	private static final int gap = 50;
	private WindowedUpperPanel windowedPanel;
	private TablesView tablesView;
	private DiagramsView diagramsView;
	private JSplitPane panelSpliter;
	private JTabbedPane tabs;
	private EventManager manager;

	public MainWindow(EventManager manager) {
		super(title);
		init(manager);
		
		this.setIconImage(new ImageIcon("resource//logo.png").getImage().getScaledInstance(400, 400,  java.awt.Image.SCALE_SMOOTH));
		
	}

	/**
	 * Initialize our swing components.
	 * 
	 * @param manager object that coordinates, edits and transfers information
	 *                between the different swing objects of our project
	 */
	private void init(EventManager manager) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.manager = manager;
		windowedPanel = new WindowedUpperPanel(manager);
		tablesView = new TablesView(manager);
		diagramsView = new DiagramsView();
		tabs = new JTabbedPane();

		tabs.add("Tables", tablesView);
		tabs.add("Schéma relationnel", diagramsView);

		panelSpliter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, windowedPanel, tabs);
		panelSpliter.setDividerLocation((screenSize.height - gap * 2) / 2);
		panelSpliter.setOneTouchExpandable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(gap, gap, screenSize.width - gap * 2, screenSize.height - gap * 2);
		setContentPane(panelSpliter);
		setJMenuBar(createMenuBar());
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Post close clean up.......");
				new DiagramUtils().cleanDirectory();
			}
		});

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates the menu on the top and fills it with options.
	 * 
	 * @return our JMenuBar object populated with menus
	 */
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Fichier");
		JMenu helpMenu = new JMenu("Aide");
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		JMenuItem fileMenuItem = new JMenuItem("Nouveau Fichier");
		fileMenuItem.addActionListener(new NewFileWindow(manager));
		fileMenu.add(fileMenuItem);

		fileMenu.addSeparator();

		JMenuItem fileMenuItem2 = new JMenuItem("Modification de Fichier");
		fileMenuItem2.addActionListener(new FileModificationWindow(manager));
		fileMenu.add(fileMenuItem2);

		fileMenu.addSeparator();

		fileMenuItem = new JMenuItem("Quitter");
		fileMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});
		fileMenu.add(fileMenuItem);

		JMenuItem helpMenuItem = new JMenuItem("À propos de SQLschool");
		JPanel viewPort = new JPanel();
		viewPort.setLayout(new GridLayout(0, 1));
		JButton link = new JButton();
		link.setText("<html><font color=\"#8c00a1\"><U>Page GitHub du logiciel.</U></font></html>");
		link.setHorizontalAlignment(SwingConstants.CENTER);
		link.setOpaque(true);
		link.setBorderPainted(false);

		URI uri;
		try {
			uri = new URI(URL);
			link.setToolTipText(uri.toString());
			link.addActionListener(e -> UtilitiesFactory.open(uri));
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		viewPort.add(link);
		helpMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this, viewPort, "À propos de SQLschool",
						JOptionPane.PLAIN_MESSAGE);

			}
		});
		helpMenu.add(helpMenuItem);

		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public void setOutPut(String s) {
		windowedPanel.getEditorPanel().setOutput(s);
	}

	public EditorPanel getEditorPanel() {
		return windowedPanel.getEditorPanel();
	}

	public String getInput() {
		return windowedPanel.getEditorPanel().getInput();

	}
	
	public void setInput(String string) {
		windowedPanel.getEditorPanel().setInput(string);;
	}

	/**
	 * Updates exercise's description and title on the EditorPanel.
	 * 
	 * @param description exercise's description
	 * @param title       exercise's title
	 */
	public void setDescription(String description, String title) {
		windowedPanel.getEditorPanel().updateDescription(description, title);
	}

	/**
	 * Forwards the list of tables to be printed to the TablesView panel.
	 * 
	 * @param tables a list of tables extracted from the selected database
	 */
	public void populateTablesView(ArrayList<Table> tables) {
		tablesView.updateTablesView(tables);
	}

	/**
	 * Calls the EditorPanel's showEditor method.
	 */
	public void showEditor() {
		windowedPanel.getEditorPanel().showEditor();

	}

	/**
	 * Closes any open connections to the database with the help of manager and
	 * terminates the application.
	 */
	void close() {
		new DiagramUtils().cleanDirectory();
		manager.close();
		System.exit(1);
	}

	public void updateExercisesView() throws IOException {
		windowedPanel.updateExercisesView();

	}

	public void updateDiagram(boolean isInstalled) {
		diagramsView.loadRelationalDiagram(isInstalled);

	}

	public void notifyIncompatible() {
		diagramsView.notifyIncompatibleOS();

	}

	public void notifyUnavailableDirectory() {
		diagramsView.notifyUnavailableDirectory();

	}

	public void showProgress() {
		diagramsView.showProgress();

	}

	public void hideProgress() {
		diagramsView.hideProgress();

	}

	public void updateTableModel() {
		tablesView.updateTableModel();
		
	}

}
