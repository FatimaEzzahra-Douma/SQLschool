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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

/**
 * Functions that don't really fit anywhere specific are put in this class.
 *
 */
public class UtilitiesFactory {
	/**
	 * Opens a URL on the web browser.
	 * 
	 * @param uri
	 */
	public static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A method that creates the questions in filmExercises.json
	 */
	protected static void createJsonTemp() {
		JsonManager jsonManager = new JsonManager();

		jsonManager.addQuestion("films.db", "SELECT", "Question 1 ", "Afficher tous les films existants.",
				"SELECT * FROM FILM;");
		jsonManager.addQuestion("films.db", "SELECT", "Question 2", "Afficher les films triés par année de sortie .",
				"SELECT * from Film ORDER BY annee; ");
		jsonManager.addQuestion("films.db", "SELECT", "Question 3",
				"Afficher le titre de tous les films dont la date de sortie est 1937 .",
				"SELECT titre FROM Film WHERE annee=1937; ");
		jsonManager.addQuestion("films.db", "SELECT", "Question 4", "Afficher l'année de sortie du film 'Star Wars' .",
				"SELECT annee From Film WHERE titre='Star Wars';");
		jsonManager.addQuestion("films.db", "SELECT", "Question 5",
				"Afficher le nom du réalisateur du film 'Titanic' .",
				"Select realisateur  FROM film WHERE titre='Titanic';");
		jsonManager.addQuestion("films.db", "SELECT", "Question 6",
				"Afficher les(s) film(s) dont le titre est 'Alice In Wonderland'?",
				"Select * FROM film WHERE titre='Alice In Wonderland';");
		jsonManager.addQuestion("films.db", "SELECT", "Question 7",
				"Afficher tous les titres des films par ordre alphabétique", "select titre from film order by titre;");
		jsonManager.addQuestion("films.db", "SELECT", "Question 8",
				"Afficher tous les noms des critiques par ordre alphabétique",
				"select nom from critique order by nom;");
		jsonManager.addQuestion("films.db", "SELECT", "Question 9",
				"Afficher le nom et le rID de tous les critiques par ordre croissant de riD",
				"select nom,rID from critique order by rID;");
		jsonManager.addQuestion("films.db", "SELECT", "Question 10",
				"Afficher toutes les évaluations dans l'ordre chronologique",
				"select * from evaluation order by dateEvaluation;");
		
		
		jsonManager.addQuestion("films.db", "INSERT", "Question 11",
				"Ajouter le film 'The Magnificent Seven' de '2016' qui est réalisé par 'Antoine Fuqua' avec mID=111",
				"INSERT INTO FILM VALUES(111,'The Magnificent Seven',2016,'Antoine Fuqua');");
		jsonManager.addQuestion("films.db", "INSERT", "Question 12",
				"Ajouter le film réaliser par 'Jacques Audiard' qui porte le titre 'the sisters brothers' , réalisé en '2018' avec mID=112",
				"INSERT INTO FILM VALUES (112,'the sisters brothers' , 2018 , 'Jacques Audiard');");
		jsonManager.addQuestion("films.db", "INSERT", "Question 13",
				"Ajouter les films '111,Joker,2019,Todd Phillips' et '110,Avengers Endgame,2019,Anthony et Joe Russo' en utilisant une seule requête.",
				"INSERT INTO FILM VALUES(111,'Joker',2019,'Todd Phillips'),(110,'Avengers Endgame',2019,'Anthony et Joe Russo');");
		jsonManager.addQuestion("films.db", "INSERT", "Question 14",
				"Ajouter le critique 'Leonard Maltin' avec rID=209.",
				"INSERT INTO critique values(209,'Leonard Maltin');");
		jsonManager.addQuestion("films.db", "INSERT", "Question 15",
				"Faites en sorte que le film 'Titanic'(mid 105) reçoive une critique de (205,'Chris Jackson') avec la note de 5 le '2012-01-01' .",
				"INSERT INTO evaluation values(205,105,5,'2012-01-01');");
		
		
		jsonManager.addQuestion("films.db", "DELETE", "Question 16", "Supprimer le film 'Star Wars'.",
				"DELETE FROM Film Where titre = 'Star Wars';");
		jsonManager.addQuestion("films.db", "DELETE", "Question 17",
				"Supprimer tous les films réalisés par James Cameron.",
				"DELETE FROM film where realisateur = 'James Cameron';");
		jsonManager.addQuestion("films.db", "DELETE", "Question 18",
				"Supprimer tous les films qui ont été réalisés entre 1937 et 1977.",
				"DELETE FROM Film where annee < 1977 and annee > 1937;");
		jsonManager.addQuestion("films.db", "DELETE", "Question 19",
				"Supprimer toutes les évaluations faites à la date du '2011-01-12'.",
				"DELETE FROM evaluation where DateEvaluation='2011-01-12';");
		jsonManager.addQuestion("films.db", "DELETE", "Question 20",
				"Supprimer le critique dont le nom est 'Daniel Lewis'.",
				"DELETE FROM critique where nom='Daniel Lewis';");
		jsonManager.addQuestion("films.db", "DELETE", "Question 21",
				"Supprimer toutes les évalutations faites le '2011-01-27' et où la note était 4.",
				"DELETE FROM evaluation where DateEvaluation='2011-01-27' and note=4;");
		
		
		jsonManager.addQuestion("films.db", "JOIN", "Question 22 ",
				"Donner le titre des films ayant obtenu une note supérieure à 2",
				"select titre from film inner join evaluation ON (film.mID=evaluation.mId) where EVALUATION.NOTE>2;");
		jsonManager.addQuestion("films.db", "JOIN", "Question 23 ",
				"Afficher les films qui ont été notés par Sarah Martinez",
				"select * from ( (film f inner join evaluation e ON (e.mID=f.mID) ) as fe inner join critique c ON (c.rID=fe.rID))  where c.nom='Sarah Martinez';");
		jsonManager.addQuestion("films.db", "JOIN", "Question 24 ",
				"Donner tous les films qui ont été evalués entre le 01-01-2011 et le 15-01-2011 ",
				"select * from film f inner join evaluation e ON (f.mID = e.mID) where dateEvaluation >'01-01-2011' and dateEvaluation<'15-01-2011';");
		jsonManager.addQuestion("films.db", "JOIN", "Question 25 ",
				"Donner les notes des films critiqués par 'James Cameron'",
				"select note from evaluation e , critique c where e.rID=c.rID and c.nom='James Cameron';");
		jsonManager.addQuestion("films.db", "JOIN", "Question 26 ", "Quelle est la note du film 'Avatar'?",
				"select note from Evaluation e inner join film f ON (e.mID=f.mID) where titre='Avatar';");
		
		
		jsonManager.addQuestion("films.db", "COUNT&SUM", "Question 27",
				"Donner la somme de toutes les notes obtenues par tous les films.",
				" select Sum(note) from evaluation;");
		jsonManager.addQuestion("films.db", "COUNT&SUM", "Question 28",
				"Afficher tous les réalisateurs (sans doublon).", "select distinct (realisateur) from film;");
		jsonManager.addQuestion("films.db", "COUNT&SUM", "Question 29",
				"Combien de films ont été réalisés par James Cameron ?",
				" select count(titre) from film where realisateur='James Cameron';");
		jsonManager.addQuestion("films.db", "COUNT&SUM", "Question 30",
				"Combien de films ont obtenu une note supérieure à 4 ?",
				" select count(mID) from evaluation where note>4;");
		jsonManager.addQuestion("films.db", "COUNT&SUM", "Question 31",
				"Combien de films realisés par James Cameron sont sortis en 1997 ?",
				"select count(titre) from film where realisateur='James Cameron'and annee=1997;");
		
		
		jsonManager.addQuestion("films.db", "UPDATE", "Question 32",
				"Changer la date de sortie du film 'Titanic' en 1993.",
				"update film set annee=1993 where titre ='Titanic';");
		jsonManager.addQuestion("films.db", "UPDATE", "Question 33",
				"Changer le titre du film de Victor Fleming en 'GWTW'.",
				"update film set titre='GWTW' where realisateur='Victor Fleming';");
		jsonManager.addQuestion("films.db", "UPDATE", "Question 34",
				"Remplacer le nom du réalisateur du film Avatar par celui de David Crew.",
				"update film set realisateur='David Crew' where titre='Avatar';");
		jsonManager.addQuestion("films.db", "UPDATE", "Question 35",
				"Donner la valeur 1944 à l'année de sortie de tous les films réalisés par 'James Cameron'",
				"update film set annee=1944 where realisateur='James Cameron';");
		jsonManager.addQuestion("films.db", "UPDATE", "Question 36",
				"Pour toutes les évaluations du '2011-01-22' avec la note 4, passer la note à 2.",
				"update evaluation set note=4 where dateEvaluation='2011-01-22'and note=2;");
		jsonManager.addQuestion("films.db", "UPDATE", "Question 37",
				"Remplacer le nom de critique dont le rID est 203 par 'BH'.",
				"Update critique set nom='BH' where rId=203;");
		
		
		jsonManager.addQuestion("films.db", "SELECT dans SELECT", "Question 38",
				"Indiquer le titre des films dont l'année de sortie est supérieure à celle du film Titanic",
				"select titre from film where annee > (select annee from film where titre='Titanic');");
		jsonManager.addQuestion("films.db", "SELECT dans SELECT", "Question 39 ",
				"Afficher tous les film dont le réalisateur est celui du film 'Snow White'",
				"select * from film where realisateur=(select realisateur from film where titre='Snow White');");
		jsonManager.addQuestion("films.db", "SELECT dans SELECT", "Question 40 ",
				"Donner le titre ainsi que l'année des films dont l'année de sortie est inferieure à celle du film Avatar",
				"select titre,annee from film where annee<(select annee from film where titre='Avatar');");
		jsonManager.addQuestion("films.db", "SELECT dans SELECT", "Question 41 ",
				"Afficher les evaluations ayant obtenu la meilleure note.",
				"select * from evaluation where note=(select max(note) from evaluation);");

		jsonManager.createJSON("resource/filmExercises.json");

	}

}
