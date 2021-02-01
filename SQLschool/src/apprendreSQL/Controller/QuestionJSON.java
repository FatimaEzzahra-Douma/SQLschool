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

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import apprendreSQL.Model.*;

public class QuestionJSON {

	/**
	 * Creates a question in a JSON
	 * 
	 * @param question
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject createQinJSON(Question question) {

		JSONObject objQuestion = new JSONObject();
		objQuestion.put("bd", question.getDatabase());
		objQuestion.put("sujet", question.getSubject());
		objQuestion.put("titre", question.getTitleQuestion());
		objQuestion.put("contenu", question.getContentQuestion());
		objQuestion.put("bonne_reponse", question.getAnswer());

		return objQuestion;
	}

	/**
	 * Reads a question from a JSON
	 * 
	 * @param jsonObjectA
	 * @return
	 */
	public Question readQinJSON(JSONObject jsonObjectA) {
		String bd = (String) jsonObjectA.get("bd");
		String sujet = (String) jsonObjectA.get("sujet");
		String titre = (String) jsonObjectA.get("titre");
		String contenu = (String) jsonObjectA.get("contenu");
		String bonn_reponse = (String) jsonObjectA.get("bonne_reponse");

		return new Question(bd, sujet, titre, contenu, bonn_reponse);

	}

	/**
	 * Transforms a list of question to a JSONArray
	 * 
	 * @param listeE
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray createQListJSON(ArrayList<Question> listeE) {
		Iterator<Question> iteratorListeE = listeE.iterator();
		JSONArray listEJson = new JSONArray();
		while (iteratorListeE.hasNext()) {
			Question ex = iteratorListeE.next();
			listEJson.add(createQinJSON(ex));
		}
		return listEJson;
	}

	/**
	 * Transforms a JSONArray to LIST of Question
	 * 
	 * @param a
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Question> readQListJSON(JSONArray a) {
		ArrayList<Question> listeA = new ArrayList<>();
		Iterator<JSONObject> iterator = a.iterator();
		while (iterator.hasNext()) {
			JSONObject e = iterator.next();
			listeA.add(readQinJSON(e));
		}
		return listeA;
	}

}
