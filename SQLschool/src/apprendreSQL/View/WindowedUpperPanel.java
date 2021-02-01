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

import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import apprendreSQL.Controller.EventManager;

/**
 * This is the panel that holds both ExercisesPanel and EditorPanel.
 * 
 */

@SuppressWarnings("serial")
public class WindowedUpperPanel extends JPanel {
	
	private JSplitPane panelSpliter;
	private ExercisesPanel exercisePanel;
	private EditorPanel editorPanel;
	private JScrollPane exercisesScrollPane, editorScrollPane;

	public WindowedUpperPanel(EventManager manager) {
		init(manager);
	}

	/**
	 * Initialize our swing components.
	 * 
	 * @param manager object that coordinates, edits and transfers information
	 *                between the different swing objects of our project
	 */
	private void init(EventManager manager) {
		setLayout(new CardLayout());
		editorPanel = new EditorPanel();
		exercisePanel = new ExercisesPanel(manager);
		editorScrollPane = new JScrollPane(editorPanel);
		exercisesScrollPane = new JScrollPane(exercisePanel);

		editorScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		exercisesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		exercisesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panelSpliter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, exercisesScrollPane, editorScrollPane);
		panelSpliter.setOneTouchExpandable(true);
		add(panelSpliter);

	}

	public ExercisesPanel getExercisesPanel() {
		return exercisePanel;
	}

	public void setExercicesPanel(ExercisesPanel exercicesPanel) {
		this.exercisePanel = exercicesPanel;
	}

	public EditorPanel getEditorPanel() {
		return editorPanel;
	}

	public void setEditorPanel(EditorPanel editorPanel) {
		this.editorPanel = editorPanel;
	}

	public void updateExercisesView() throws IOException {
		exercisePanel.updateExercicesView();
		
	}

}
