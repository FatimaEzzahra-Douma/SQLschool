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
package apprendreSQL.Controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import apprendreSQL.Model.Question;

public class JsonManager {

	private ArrayList<Question> listQuestions = new ArrayList<>();
	private QuestionJSON questionJSON = new QuestionJSON();
	final int MAX = 2000;
	private Boolean overCapacity = false;

	/**
	 * This method create a JSON file with the liste of Question
	 * 
	 * @param nomFichier name of the JSON file create
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void createJSON(String nomFichier) {

		if (listQuestions == null)
			return;

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("listeQuestion", questionJSON.createQListJSON(listQuestions));

		try (FileWriter file = new FileWriter(nomFichier)) {
			file.write(jsonObject.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Le fichier spécifié est inutilisable.", "Attention",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	/**
	 * This method use readFileQuestion(nomFichier,false);
	 * 
	 * @param nomFichier name of the JSON file who read
	 * @return void
	 */
	public void readFileQuestion(String nomFichier) {
		readFileQuestion(nomFichier, false);
	}

	/**
	 * This method read a JSON file and push the contended in a list of Question
	 * 
	 * @param nomFichier name of the JSON file who read
	 * @param add        If true we add the Question contened on the JSON file in
	 *                   the exiting list of Question. If false we overwrite the
	 *                   content of the Question list by the contents of the file.
	 */
	public void readFileQuestion(String nomFichier, Boolean add) {

		try (FileReader reader = new FileReader(new File(nomFichier))) {
			// read the json file
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONArray a = (JSONArray) jsonObject.get("listeQuestion");
			ArrayList<Question> listQuestionsTempo = questionJSON.readQListJSON(a);

			if (add) {
				if (listQuestions.size() + listQuestionsTempo.size() > MAX) {
					if (!overCapacity) {
						JOptionPane.showMessageDialog(null,
								"Il y a trop de question (MAX=" + MAX + ") dans le dossier resource.", "Attention",
								JOptionPane.WARNING_MESSAGE);
					}
					overCapacity = true;
					return;
				}
				listQuestions.addAll(listQuestionsTempo);

				int max = listQuestions.size();
				int bis;
				Question q;
				for (int i = 1; i < max; i++) {
					q = listQuestions.get(i);
					bis = 0;
					while (listQuestions.subList(0, i - 1).contains(q)) {
						if (bis == 0) {
							q.setTitleQuestion(q.getTitleQuestion() + " bis");
						} else {
							q.setTitleQuestion(
									q.getTitleQuestion().replace(bis == 1 ? " bis" : " bis" + (bis - 1), " bis" + bis));
						}
						bis++;
					}
				}

			} else {
				if (listQuestionsTempo.size() < MAX)
					listQuestions = listQuestionsTempo;
			}

		} catch (Exception ex) {
			System.out.println("Le fichier spécifié est introuvable.j ");
			JOptionPane.showMessageDialog(null, "Le fichier spécifié est introuvable.", "Attention",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	/**
	 * Add a new Question in the Question list If the question doesn't have the the
	 * same (databae,subject,titre) as the one that already exists in the Question
	 * list.
	 * 
	 * @param database
	 * @param subject
	 * @param titre
	 * @param contenu
	 * @param bonne_reponse
	 * @return
	 */
	public boolean addQuestion(String database, String subject, String titre, String contenu, String bonne_reponse) {
		Question q = new Question(database, subject, titre, contenu, bonne_reponse);
		if (listQuestions.size() + 1 > MAX) {
			JOptionPane.showMessageDialog(null, "Il y a trop de question (MAX=" + MAX + ") dans ce fichier JSON.",
					"Attention", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (listQuestions.contains(q)) {
			System.out.println("Cette question existe déja.");
			return false;
		}
		listQuestions.add(q);
		return true;
	}

	/**
	 * return the Question List.
	 * 
	 * @return ArrayList<Question>
	 */
	public ArrayList<Question> getListQuestion() {
		return listQuestions;
	}

	/**
	 * clear the Question List .
	 * 
	 */
	public void clear() {
		overCapacity = false;
		listQuestions.clear();
	}

}
