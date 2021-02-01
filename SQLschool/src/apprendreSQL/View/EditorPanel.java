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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import apprendreSQL.Controller.EventManager;

/**
 * 
 * This is the panel that holds the components for user input / output and
 * prints out a preview of exercises subjects.
 * 
 */
@SuppressWarnings("serial")
public class EditorPanel extends JPanel {

	private static final int gap_1 = 250, gap_2 = 300;
	private static final Font font = new Font("Arial", Font.PLAIN, 14);
	private String etat;
	private JTextArea input;
	private JTextArea lines;
	private JTextPane output;
	private JButton submit;
	private JButton hint;
	private JTextArea exerciseOutput;
	private Dimension prefferedDimension;
	private JScrollPane outputScrollpane;
	private JScrollPane inputScrollPane;

	public EditorPanel() {
		init();
	}

	/**
	 * Initialize our swing components.
	 */
	private void init() {
		setLayout(new GridBagLayout());
		setBackground(new Color(Integer.valueOf("#f8f8f8".substring(1, 3), 16),
				Integer.valueOf("#f8f8f8".substring(3, 5), 16), Integer.valueOf("#f8f8f8".substring(5, 7), 16)));

		GridBagConstraints theGrid = new GridBagConstraints();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		Border border = BorderFactory.createLoweredBevelBorder();// ("Entrée(Input)");

		prefferedDimension = new Dimension(screenSize.width / 2 - gap_2, screenSize.height / 2 - gap_1);

		lines = new JTextArea("1 ");
		lines.setBackground(new Color(Integer.valueOf("#f8f8f8".substring(1, 3), 16),
				Integer.valueOf("#f8f8f8".substring(3, 5), 16), Integer.valueOf("#f8f8f8".substring(5, 7), 16)));
		lines.setEditable(false);

		input = new JTextArea();
		input.setLineWrap(true);
		input.setFont(font);

		output = new JTextPane();
		output.setFont(font);
		output.setEditable(false);

		exerciseOutput = new JTextArea();
		exerciseOutput.setLineWrap(true);
		exerciseOutput.setEditable(false);
		exerciseOutput.setBackground(new Color(Integer.valueOf("#f8f8f8".substring(1, 3), 16),
				Integer.valueOf("#f8f8f8".substring(3, 5), 16), Integer.valueOf("#f8f8f8".substring(5, 7), 16)));

		border = BorderFactory.createTitledBorder("Output");

		outputScrollpane = new JScrollPane(output);
		outputScrollpane.setBackground(Color.white);
		outputScrollpane.setPreferredSize(prefferedDimension);

		border = BorderFactory.createTitledBorder("Input");

		inputScrollPane = new JScrollPane(input);
		inputScrollPane.setBackground(Color.white);
		inputScrollPane.setPreferredSize(prefferedDimension);

		submit = new JButton("Exécuter");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EventManager.callExecute();
			}
		});
		hint = new JButton("Aide");
		hint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(EditorPanel.this, EventManager.callHint(), "Aide",
						JOptionPane.PLAIN_MESSAGE);

			}
		});

		input.getDocument().addDocumentListener(new DocumentListener() {
			public String getText() {
				int caretPosition = input.getDocument().getLength();
				Element root = input.getDocument().getDefaultRootElement();
				String text = "1 " + System.getProperty("line.separator");
				for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
					text += i + System.getProperty("line.separator");
				}
				return text;
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				lines.setText(getText());

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				lines.setText(getText());

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lines.setText(getText());

			}
		});

		inputScrollPane.setRowHeaderView(lines);

		border = BorderFactory.createTitledBorder("Exercice");
		setBorder(border);

		theGrid.fill = GridBagConstraints.HORIZONTAL;
		theGrid.insets = new Insets(10, 10, 10, 40);

		theGrid.gridwidth = 2;
		theGrid.gridx = 0;
		theGrid.gridy = 0;
		add(exerciseOutput, theGrid);

		theGrid.gridwidth = 1;
		theGrid.gridx = 0;
		theGrid.gridy = 1;
		add(inputScrollPane, theGrid);

		theGrid.gridx = 0;
		theGrid.gridy = 2;
		add(submit, theGrid);

		theGrid.gridx = 1;
		theGrid.gridy = 1;
		add(outputScrollpane, theGrid);

		theGrid.gridx = 1;
		theGrid.gridy = 2;
		add(hint, theGrid);

		setEditorToDefault();

	}

	public String getInput() {
		return input.getText();
	}

	public void setInput(String string) {
		this.input.setText(string);
	}

	public void setOutput(String output) {
		this.output.setContentType("text/html");
		this.output.setText(output);
	}

	/**
	 * This method is triggered whenever a question is selected from the exercises
	 * menu. It updates both the title and the description of the exercise in the
	 * EditorPanel.
	 * 
	 * @param description a string that represents the description of the
	 *                    question/exercise, extracted from json file
	 * @param title       the title of the question/exercise, extracted from json
	 *                    file
	 */
	public void updateDescription(String description, String title) {
		this.setBorder(BorderFactory.createTitledBorder(title));
		this.exerciseOutput.setText(description);
	}

	public String getEtat() {
		return etat;
	}

	/**
	 * Clears the editor and show an empty panel at application startup.
	 */
	private void setEditorToDefault() {
		inputScrollPane.setVisible(false);
		outputScrollpane.setVisible(false);
		submit.setVisible(false);
		hint.setVisible(false);
		exerciseOutput.setVisible(false);
	}

	/**
	 * Shows the input, output and corresponding description text when we click on a
	 * question from the exercises menu.
	 */
	public void showEditor() {
		inputScrollPane.setVisible(true);
		outputScrollpane.setVisible(true);
		submit.setVisible(true);
		hint.setVisible(true);

		exerciseOutput.setVisible(true);
	}

}
