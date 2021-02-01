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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import apprendreSQL.Controller.ConnectionSQLite;
import apprendreSQL.Controller.Corrector;
import apprendreSQL.Controller.JsonManager;

public class JUnitCorrector {
	Corrector c;
	private static ConnectionSQLite moviesConnection;

	@Test
	public void setUP() {
		c = new Corrector();
		moviesConnection = new ConnectionSQLite("resource/films.db");
		moviesConnection.connect();
	}

	@Test
	public void testSelectJSON2() {
		System.out.println("JSON2");

		JsonManager jm = new JsonManager();

		jm.readFileQuestion("resource/filmExercises.json");

		int i = jm.getListQuestion().size();
		System.out.println("nombre d'exo:" + i);

		assertTrue(i > 0);
	}

	public Boolean test(String inputUser, String bonne_Reponse) {
		setUP();
		Boolean b = c.correction(inputUser, bonne_Reponse, moviesConnection);
		if (!b) {
			System.out.println(c.getCommentaire().replaceAll("<br>", "\n"));
		} else {
			System.out.println("Pas d'erreur trouver");
		}
		return b;
	}

	@Test
	public void testSelect1() {
		System.out.println("test1");
		Boolean b = test("", "SELECT * FROM film;");
		assertFalse(b);
	}

	@Test
	public void testSelect2() {
		System.out.println("test2");

		String inputUser = "select ";
		String bonne_Reponse = "SELECT * FROM film;";
		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect3() {
		System.out.println("test3");

		String inputUser = "SElect from ;";
		String bonne_Reponse = "SELECT * FROM film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect4() {
		System.out.println("test4");

		String inputUser = "SElect  from film;";
		String bonne_Reponse = "SELECT * FROM film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect5() {
		System.out.println("test5");

		String inputUser = "select * from film;";
		String bonne_Reponse = "SELECT * FROM film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);
	}

	@Test
	public void testSelect7() {
		System.out.println("test7");

		String inputUser = "select * from film ;";
		String bonne_Reponse = "SELECT * FROM film where mID = 101;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect8() {
		System.out.println("test8");

		String inputUser = "select * from film where ;";
		String bonne_Reponse = "SELECT * FROM film where mID = 101;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect9() {
		System.out.println("test9");
		String inputUser = "select titre from film where mID= 102;";
		String bonne_Reponse = "SELECT * FROM film ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect10() {
		System.out.println("test10");
		String inputUser = "select * from film where mID= 102;";
		String bonne_Reponse = "SELECT * FROM film ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect11() {
		System.out.println("test11");
		String inputUser = "select  gtrsdg from film where mID= 102;";
		String bonne_Reponse = "SELECT titre FROM film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect12() {
		System.out.println("test12");
		String inputUser = "select  mid, titre, annee, realisateur from film ;";
		String bonne_Reponse = "SELECT * FROM film ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);
	}

	@Test
	public void testSelect13() {
		System.out.println("test13");
		String inputUser = "select  * from film ;";
		String bonne_Reponse = "SELECT mid, titre, annee, realisateur FROM film ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);
	}

	@Test
	public void testSelect14() {
		System.out.println("test14");
		String inputUser = "select  count( titre from film ;";
		String bonne_Reponse = "SELECT COUNT(titre) FROM film ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect15() {
		System.out.println("test15");
		String inputUser = "select  count( titre ) from film ;";
		String bonne_Reponse = "SELECT COUNT(titre) FROM film ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);
	}

	@Test
	public void testSelect16() {
		System.out.println("test16");
		String inputUser = "SELECT titre, Count(note)  FROM film inner join Evaluation on film.mID=Evaluation.mID where film.mid = 101;";
		String bonne_Reponse = "SELECT titre, Count(note)  FROM film inner join Evaluation on film.mID=Evaluation.mID where film.mid = 101;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);
	}

	@Test
	public void testSelect17() {
		System.out.println("test17");
		String inputUser = "SELECT titre, Count(note)  FROM film inner join Evaluation on mID=Evaluation.mID where film.mid = 101;";
		String bonne_Reponse = "SELECT titre, Count(note)  FROM film inner join Evaluation on film.mID=Evaluation.mID where film.mid = 101;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect18() {
		System.out.println("testINSERT18");
		String inputUser = "INSERT INTO film VALUES (222,\"Le film a lui\",2018,\"inconnu\");";
		String bonne_Reponse = "INSERT INTO film VALUES (222,\"Mon film a moi\",2018,\"thibaud\");";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);
	}

	@Test
	public void testSelect19() {
		System.out.println("testINSERT19");
		String inputUser = "INSERT INTO film VALUES (101,\"Le film a lui\",2018,\"inconnu\");";
		String bonne_Reponse = "INSERT INTO film VALUES (222,\"Mon film a moi\",2018,\"thibaud\");";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect20() {
		System.out.println("testINSERT20");
		String inputUser = "INSERT INTO film VALUES (222,\"Mon film a moi\",2018,\"thibaud\");";
		String bonne_Reponse = "INSERT INTO film VALUES (222,\"Mon film a moi\",2018,\"thibaud\");";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect21() {
		System.out.println("testUPDATE21");
		String inputUser = "UPDATE film SET titre = \"TOTO2\" WHERE mid = 101 ;";
		String bonne_Reponse = "UPDATE film SET titre = \"TOTO\" WHERE mid = 101 ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect22() {
		System.out.println("testUPDATE22");
		String inputUser = "UPDATE film SET titre = \"TOTO\" WHERE mid = 101 ;";
		String bonne_Reponse = "UPDATE film SET titre = \"TOTO\" WHERE mid = 101 ;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect23() {
		System.out.println("testDELETE23");
		String inputUser = "delete from film where mid=102;";
		String bonne_Reponse = "delete from film where mid=101;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect24() {
		System.out.println("testDELETE24");
		String inputUser = "delete from film where mid=101;";
		String bonne_Reponse = "delete from film where mid=101;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect25() {
		System.out.println("test25");
		String inputUser = "select DISTINCT realisateur from film where mid= 101";
		String bonne_Reponse = "select DISTINCT realisateur from film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect26() {
		System.out.println("test26");
		String inputUser = "select DISTINCT realisateur from film;";
		String bonne_Reponse = "select DISTINCT realisateur from film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect27() {
		System.out.println("test27");
		String inputUser = "select AVG(annee) from film;";
		String bonne_Reponse = "select AVG(annee) from film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect28() {
		System.out.println("test28");
		String inputUser = "select MIN(annee) from film;";
		String bonne_Reponse = "select MIN(annee) from film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect29() {
		System.out.println("test29");
		String inputUser = "select MAX(annee) from film;";
		String bonne_Reponse = "select MAX(annee) from film;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect30() {
		System.out.println("test30");
		String inputUser = "select SUM(note) from Evaluation;";
		String bonne_Reponse = "select SUM(note) from Evaluation;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

	@Test
	public void testSelect31() {
		System.out.println("test31");
		String inputUser = "select * from film;";
		String bonne_Reponse = "select * from film order by annee;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertFalse(b);

	}

	@Test
	public void testSelect32() {
		System.out.println("test32");
		String inputUser = "select * from film  order by annee;";
		String bonne_Reponse = "select * from film  order by annee;";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}





	@Test
	public void testINSERT2() {
		System.out.println("testINSERT2");
		String inputUser = "insert into film(mid,titre,annee,realisateur) values (222,\"Moi2\",2019,\"moi\");";
		String bonne_Reponse = "insert into film(mid,titre,annee,realisateur) values (222,\"Moi2\",2019,\"moi\");";

		Boolean b = test(inputUser, bonne_Reponse);

		assertTrue(b);

	}

}
