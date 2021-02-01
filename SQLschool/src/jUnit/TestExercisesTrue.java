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
package jUnit;

import static org.junit.Assert.*;

import org.junit.Test;

import apprendreSQL.Model.Question;
import apprendreSQL.Controller.ConnectionSQLite;
import apprendreSQL.Controller.Corrector;
import apprendreSQL.Controller.JsonManager;

public class TestExercisesTrue {

	Corrector c;
	private static ConnectionSQLite moviesConnection;

	@Test
	public void setUP() {
		c = new Corrector();
		moviesConnection = new ConnectionSQLite("resource/films.db");
		moviesConnection.connect();
	}

	@Test
	public void testExercisesALL() {
		setUP();
		boolean bb = true;
		JsonManager jsonManager = new JsonManager();

		jsonManager.readFileQuestion("resource/filmExercises.json");

		for (Question q : jsonManager.getListQuestion()) {

			Boolean b = c.correction(q.getAnswer(), q.getAnswer(), moviesConnection);
			if (!b) {
				System.out.println("==================================================>" + q.getTitleQuestion());
				System.out.println(c.getCommentaire().replaceAll("<br>", "\n"));
				bb = false;
				try {
					throw new Exception("LA BONNE REQUÊTE NE FONCTIONNE PAS EXO titre=" + q.getTitleQuestion()
							+ " reponce:" + q.getAnswer());
				} catch (Exception e) {
					System.out.println("A corriger");
					e.printStackTrace();

				}
			} else {
				System.out.println("Pas d'erreur trouver");
			}

		}
		assertTrue(bb);

	}

}
