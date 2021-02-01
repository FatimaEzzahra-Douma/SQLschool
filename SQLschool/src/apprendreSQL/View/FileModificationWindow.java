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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import apprendreSQL.Model.Question;
import apprendreSQL.Controller.EventManager;
import apprendreSQL.Controller.JsonManager;

public class FileModificationWindow implements ActionListener, GetInformation, SimilarFunctions {

	private List<String> questionInfo;
	private JFrame frmModificationDexercice;
	private JLabel lblBd, lblSujet, lblEnoncé, lblRponse, lblQuestion, lblTitre;
	private JComboBox<String> comboBoxJsonFile, comboBoxSubject, comboBoxDB;
	private JComboBox<Integer> comboBoxIdQuestion;
	private JTextArea textAreaQuestion, textAreaAnswer;
	private JButton btnSupprimer, btnModifierQuestion, btnAjouterNouvelleQuestion, buttonNvSujet;
	private static JsonManager jsonManager;
	private ArrayList<String> subjects;
	protected JTextField textFieldSubject, txtFldTitre;
	private EventManager eventManager;
	
	
	public FileModificationWindow(EventManager manager) {
		eventManager = manager;
	}

	/**
	 * A function that creates the 'Modification d'exercice' window
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			System.out.println("Nimbus is not available, you can set the GUI to another look and feel.");
		}

		subjects = new ArrayList<String>();
		jsonManager = new JsonManager();
		frmModificationDexercice = new JFrame();
		frmModificationDexercice.setBounds(100, 100, 877, 664);
		frmModificationDexercice.getContentPane().setLayout(null);

		lblBd = new JLabel("Fichier :");
		lblBd.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBd.setBounds(104, 59, 69, 28);
		frmModificationDexercice.getContentPane().add(lblBd);

		lblSujet = new JLabel("Sujet :");
		lblSujet.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSujet.setBounds(104, 257, 69, 28);
		frmModificationDexercice.getContentPane().add(lblSujet);

		lblEnoncé = new JLabel("Enonc\u00E9 :");
		lblEnoncé.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEnoncé.setBounds(104, 357, 69, 28);
		frmModificationDexercice.getContentPane().add(lblEnoncé);

		lblRponse = new JLabel("R\u00E9ponse :");
		lblRponse.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRponse.setBounds(104, 446, 69, 28);
		frmModificationDexercice.getContentPane().add(lblRponse);

		comboBoxJsonFile = new JComboBox<String>();
		comboBoxJsonFile.setBounds(262, 60, 350, 26);
		frmModificationDexercice.getContentPane().add(comboBoxJsonFile);

		comboBoxSubject = new JComboBox<String>();
		comboBoxSubject.setBounds(262, 258, 350, 26);
		frmModificationDexercice.getContentPane().add(comboBoxSubject);

		textFieldSubject = new JTextField();
		textFieldSubject.setBounds(262, 258, 350, 26);
		frmModificationDexercice.getContentPane().add(textFieldSubject);
		textFieldSubject.setVisible(false);

		lblQuestion = new JLabel("Question :");
		lblQuestion.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblQuestion.setBounds(104, 159, 69, 28);
		frmModificationDexercice.getContentPane().add(lblQuestion);

		comboBoxIdQuestion = new JComboBox<Integer>();
		comboBoxIdQuestion.setBounds(262, 160, 350, 26);
		frmModificationDexercice.getContentPane().add(comboBoxIdQuestion);

		comboBoxDB = new JComboBox<String>();
		comboBoxDB.setBounds(262, 112, 350, 26);
		frmModificationDexercice.getContentPane().add(comboBoxDB);

		for (String name : getJSONFiles())
			comboBoxJsonFile.addItem(name);

		for (String database : getDbFiles())
			comboBoxDB.addItem(database);

		textAreaQuestion = new JTextArea();
		textAreaQuestion.setBounds(262, 363, 350, 60);
		frmModificationDexercice.getContentPane().add(textAreaQuestion);

		textAreaAnswer = new JTextArea();
		textAreaAnswer.setBounds(262, 452, 350, 73);
		frmModificationDexercice.getContentPane().add(textAreaAnswer);

		txtFldTitre = new JTextField();
		txtFldTitre.setBounds(262, 206, 350, 26);
		frmModificationDexercice.getContentPane().add(txtFldTitre);

		refreshJcomponents(comboBoxSubject, comboBoxJsonFile, comboBoxIdQuestion, textAreaQuestion, textAreaAnswer,
				txtFldTitre);

		btnSupprimer = new JButton("Supprimer");
		btnSupprimer.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSupprimer.setBounds(489, 571, 123, 36);
		frmModificationDexercice.getContentPane().add(btnSupprimer);

		btnModifierQuestion = new JButton("Modifier");
		btnModifierQuestion.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnModifierQuestion.setBounds(262, 571, 150, 36);
		frmModificationDexercice.getContentPane().add(btnModifierQuestion);


		btnAjouterNouvelleQuestion = new JButton("Ajouter nouvelle question");
		btnAjouterNouvelleQuestion.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAjouterNouvelleQuestion.setBounds(652, 571, 196, 36);
		frmModificationDexercice.getContentPane().add(btnAjouterNouvelleQuestion);

		buttonNvSujet = new JButton("Nouveau sujet");
		buttonNvSujet.setFont(new Font("Tahoma", Font.BOLD, 12));
		buttonNvSujet.setBounds(262, 308, 139, 26);
		frmModificationDexercice.getContentPane().add(buttonNvSujet);

		lblTitre = new JLabel("Titre :");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTitre.setBounds(104, 205, 69, 28);
		frmModificationDexercice.getContentPane().add(lblTitre);

		JLabel lblBd_1 = new JLabel("BD :");
		lblBd_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBd_1.setBounds(104, 110, 69, 28);
		frmModificationDexercice.getContentPane().add(lblBd_1);

		
		buttonListeners();
		
		frmModificationDexercice.setVisible(true);
		frmModificationDexercice.setResizable(false);
		frmModificationDexercice.setLocationRelativeTo(null);
		frmModificationDexercice.setTitle("Modification de Fichier");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		initialize();

	}
	
	/**
	 * This function is responsible for adding the various
	 * listeners on the modification window
	 */
	public void  buttonListeners() {
		//Delete button listener
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteQuestion(comboBoxJsonFile.getSelectedItem().toString(),
						(int) comboBoxIdQuestion.getSelectedItem());
				refreshJcomponents(comboBoxSubject, comboBoxJsonFile, comboBoxIdQuestion, textAreaQuestion,
						textAreaAnswer, txtFldTitre);

			}

		});
		
		// Modify button listener
		btnModifierQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkFields(frmModificationDexercice, txtFldTitre, textAreaQuestion, textAreaAnswer)) {
					modifyQuestion(comboBoxJsonFile.getSelectedItem().toString(),
							(int) comboBoxIdQuestion.getSelectedItem(), comboBoxSubject.getSelectedItem().toString(),
							txtFldTitre.getText(),comboBoxDB.getSelectedItem().toString());
					refreshJcomponents(comboBoxSubject, comboBoxJsonFile, comboBoxIdQuestion, textAreaQuestion,
							textAreaAnswer, txtFldTitre);
				}
			}

		});
		
		//ID Question ComboBox listener
		comboBoxIdQuestion.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					questionInfo = displayQuestion(comboBoxJsonFile.getSelectedItem().toString(),
							(int) comboBoxIdQuestion.getSelectedItem());
					textAreaQuestion.setText(questionInfo.get(0));
					textAreaAnswer.setText(questionInfo.get(1));
					comboBoxSubject.setSelectedItem(questionInfo.get(2));
					txtFldTitre.setText(questionInfo.get(3));
					comboBoxDB.setSelectedItem(questionInfo.get(4));

				}

			}
		});
		
		// Json File ComboBox listener
		comboBoxJsonFile.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				refreshJcomponents(comboBoxSubject, comboBoxJsonFile, comboBoxIdQuestion, textAreaQuestion,
						textAreaAnswer, txtFldTitre);
			}
		});
		
		//New Subject button listener
		buttonNvSujet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newSubject(buttonNvSujet, comboBoxSubject, textFieldSubject, subjects);
			}
		});

		//Add new question button listener
		btnAjouterNouvelleQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (checkFields(frmModificationDexercice, txtFldTitre, textAreaQuestion, textAreaAnswer)) {
					newQuestion(comboBoxJsonFile.getSelectedItem().toString(), comboBoxSubject);
					refreshJcomponents(comboBoxSubject, comboBoxJsonFile, comboBoxIdQuestion, textAreaQuestion,
							textAreaAnswer, txtFldTitre);
				}
			}
		});
	}

	/**
	 * A function that adds a Question
	 * 
	 * @param fileName
	 * @param comboSujet
	 */
	private void newQuestion(String fileName, JComboBox<String> comboSujet) {
		if(!verifyExecution(eventManager, textAreaAnswer.getText(),comboBoxDB.getSelectedItem().toString())) return;

		jsonManager.readFileQuestion("resource/" + fileName);
		if (jsonManager.addQuestion(comboBoxDB.getSelectedItem().toString(), comboSujet.getSelectedItem().toString(),
				txtFldTitre.getText(), textAreaQuestion.getText(), textAreaAnswer.getText())) {
			jsonManager.createJSON("resource/" + fileName);
			System.out.println("Ajouter Question");
		} else {
			JOptionPane.showMessageDialog(null,
					"l'ajout de cette question rentre en confie avec une autre question.\n(changer de titre)",
					"Attention", JOptionPane.WARNING_MESSAGE);
			System.out.println("Ajouter Question Annuler");
		}
	}

	/**
	 * A function that delete the Question of a specific ID
	 * 
	 * @param fileName
	 * @param id
	 */
	private void deleteQuestion(String fileName, int id) {
		if (comboBoxIdQuestion.getSelectedItem() != null && 0 != (int) comboBoxIdQuestion.getSelectedItem()) {
			jsonManager.readFileQuestion("resource/" + fileName);
			jsonManager.getListQuestion().remove(id - 1);
			jsonManager.createJSON("resource/" + fileName);
			System.out.println("Suprimer id:" + id);

		}
	}

	/**
	 * A function that modify the parameter Question of a specific ID
	 * 
	 * @param fileName
	 * @param id
	 * @param sujet
	 * @param titre
	 * @param dbname
	 */
	private void modifyQuestion(String fileName, int id, String sujet, String titre,String dbname) {

		if (!textAreaQuestion.getText().contentEquals("") && textAreaAnswer.getText().contentEquals(""))
			return;
		
		if(!verifyExecution(eventManager, textAreaAnswer.getText(),dbname)) return;
		
		
		jsonManager.readFileQuestion("resource/" + fileName);

		Question question = jsonManager.getListQuestion().get(id - 1);

		if ((!question.getSubject().contentEquals(sujet) || !question.getTitleQuestion().contentEquals(titre))
				&& jsonManager.getListQuestion().contains(new Question(dbname,
						sujet, titre, null, null))) {
			JOptionPane.showMessageDialog(null,
					"la modification rentre en confie avec une autre question.\n(changer de titre)", "Attention",
					JOptionPane.WARNING_MESSAGE);
			System.out.println("Modifier  id:" + id + "annuler");
			return;
		}

		question.setDatabase(dbname);
		question.setSubject(sujet);
		question.setTitleQuestion(titre);

		question.setContentQuestion(textAreaQuestion.getText());
		question.setAnswer(textAreaAnswer.getText());

		jsonManager.createJSON("resource/" + fileName);
		System.out.println("Modifier id:" + id);

	}

	/**
	 * A function that refreshes the jComponents of the window
	 * 
	 * @param comboSubject
	 * @param comboFileName
	 * @param comboID
	 * @param textAreaQuestion
	 * @param textAreaAnswer
	 */
	private void refreshJcomponents(JComboBox<String> comboSubject, JComboBox<String> comboFileName,
			JComboBox<Integer> comboID, JTextArea textAreaQuestion, JTextArea textAreaAnswer,
			JTextField txtFldTitre) {

		comboSubject.removeAllItems();
		subjects.clear();
		subjects = getSubjects(comboFileName.getSelectedItem().toString());
		for (String name : subjects)
			comboSubject.addItem(name);

		if(subjects.isEmpty())
			comboSubject.addItem("Sujet");
		
		comboID.removeAllItems();
		for (Integer id : getIDQuestion(comboFileName.getSelectedItem().toString(),jsonManager))
			comboID.addItem(id);
		int idSelected=0;
		if(comboID.getSelectedItem()!=null) {
			idSelected = (int) comboID.getSelectedItem();
		}
		questionInfo = displayQuestion(comboFileName.getSelectedItem().toString(), idSelected);
		textAreaQuestion.setText(questionInfo.get(0));
		textAreaAnswer.setText(questionInfo.get(1));
		comboSubject.setSelectedItem(questionInfo.get(2));
		txtFldTitre.setText(questionInfo.get(3));
		comboBoxDB.setSelectedItem(questionInfo.get(4));

	}

	

	/**
	 * A function that gets the question of a specific ID
	 * 
	 * @param nameFile
	 * @return List<String>
	 */
	public List<String> displayQuestion(String nameFile, int id) {
		questionInfo = new ArrayList<>();
		jsonManager.readFileQuestion("resource/" + nameFile);

		if (jsonManager.getListQuestion().size() > id - 1 && id>0) {
			Question question = jsonManager.getListQuestion().get(id - 1);
			questionInfo.add(question.getContentQuestion());
			questionInfo.add(question.getAnswer());
			questionInfo.add(question.getSubject());
			questionInfo.add(question.getTitleQuestion());
			questionInfo.add(question.getDatabase());
		} else {
			questionInfo.addAll(Arrays.asList("","","","",""));
		}

		return questionInfo;
	}
	

	
	
}
