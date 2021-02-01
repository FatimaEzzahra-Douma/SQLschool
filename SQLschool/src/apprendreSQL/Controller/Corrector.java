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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import apprendreSQL.Controller.ConnectionSQLite;

public class Corrector {

	ArrayList<String> listType = new ArrayList<>();

	String hint = "";

	String type = "none";
	String table = "none";

	String comment = "";
	String commentO = "";

	public Corrector() {
		listType.add("select");
		listType.add("insert into");
		listType.add("update");
		listType.add("delete");

	}

	public String getHint() {
		return hint;
	}

	public String getCommentaire() {
		return comment;
	}

	/**
	 * This method verifies the presence of a semicolon at the end of a query along
	 * with other syntactic verifications
	 * 
	 * @param inputUser
	 * @param right_Answer
	 * @param connection
	 * @return
	 */
	public boolean correction(String inputUser, String right_Answer, ConnectionSQLite connection) {
		type = "none";
		comment = "";
		long counter;

		inputUser = inputUser.replaceAll("\n", " ");
		inputUser = inputUser.toString().trim();
		if (!inputUser.isEmpty()) {

			// checking the number of semicolons
			counter = inputUser.chars().filter(ch -> ch == ';').count();
			// checking to see if the semicolon is at the end
			if (!inputUser.equals(";")) {
				if (counter == 1 && inputUser.charAt(inputUser.length() - 2) != ';'
						&& inputUser.charAt(inputUser.length() - 1) == ';') {

					if (correction_simple(inputUser, right_Answer)) {
						if (correction_table(inputUser, right_Answer, connection)) {
							if (correction_result(inputUser, right_Answer, connection)) {
								return true;

							}
						}
					}
				} else if (counter == 0) {
					comment = comment + "Erreur: Pas de point virgule à la fin de votre requête." + "<br>";
					return false;

				} else {
					comment = comment + "Erreur: Plusieurs points virgules ou des caractères non valides." + "<br>";
					return false;
				}
			} else {
				comment = "Rien à exécuter<br>";
				return false;
			}

		} else {
			comment = "Rien à exécuter<br>";
			return false;
		}
		return false;
	}

	/**
	 * determined the type (select, insert ...) verify if "inputUser" uses the same
	 * key words as for "right_Answer"
	 * 
	 * @param inputUser
	 * @param right_Answer
	 * @return if the response is correct
	 */
	private boolean correction_simple(String inputUser, String right_Answer) {
		// determined the type (select, insert ...)
		type = findType(right_Answer);
		commentO = "Erreur de syntaxe:";
		comment = commentO;
		if (type.equals("none")) {
			comment = "ERR dans réponse correction_simple";
			return false;
		}
		if (!compareContent(inputUser, type + " ")) {
			return false;
		}

		switch (type) {

		case "select":
		case "delete":
			return (compareContent(inputUser, " from ") && verifyAifExist(inputUser, right_Answer, " where "));

		case "insert into":
			return compareContent(inputUser, " values");

		case "update":
			return compareContent(inputUser, " set ") && verifyAifExist(inputUser, right_Answer, " where ");

		case "none":
			comment = "L'exercice fonctionne mal";
			break;

		default:
			comment = "ERROR Corrector.java correction_simple";
			System.out.println(comment);
			break;
		}

		return false;
	}

	/***
	 * verify if the name of the table used by "inputUser" is the same for
	 * "right_Answer"
	 * 
	 * @param inputUser
	 * @param right_Answer
	 * @return true if the table for "inputUser" and "right_Answer" are the same
	 */
	private boolean correction_table(String inputUser, String right_Answer, ConnectionSQLite connection) {
		commentO = "Erreur de nom:<br>";
		comment = commentO;
		// verify the name of the table used

		switch (type) {

		case "select":
		case "delete":
			return compareTableName(inputUser, right_Answer);

		case "insert into":
			return compareTableName_Between_A_B(inputUser, right_Answer, "into", "values");

		case "update":
			return compareTableName_Between_A_B(inputUser, right_Answer, "update", "set");

		case "none":
			comment = "L'exercice fonctionne mal<br>";
			break;

		default:
			comment = "ERROR correction_table<br>";
			System.out.println(comment);
			break;
		}

		return false;
	}

	/**
	 * Function finds difference in the result
	 * 
	 * @param inputUser
	 * @param right_Answer
	 * @param conn         (connection to the database )
	 * @return if the result of inputUser is the same as the result of right_Answer
	 */
	private boolean correction_result(String inputUser, String right_Answer, ConnectionSQLite conn) {
		// find difference in the result
		if (table.contains("none")) {
			System.out.println("ERRRRR correction_result table");
			return false;
		}
		commentO = "Erreur de contenu:<br> ";
		comment = commentO;

		switch (type) {

		case "select":
		case "delete":
		case "insert into":
		case "update":
			return findDiff(inputUser, right_Answer, conn);

		case "none":
			comment = "L'exercice fonctionne mal <br>";
			break;

		default:
			comment = "ERROR  correction_result<br>";
			System.out.println(comment);
			break;
		}

		return false;
	}

	/// Function used by "correction_resultat"

	// find difference in the result
	/**
	 * Function finds difference in the result
	 * 
	 * @param inputUser
	 * @param right_Answer
	 * @param connection
	 * @return if no find difference
	 */
	private boolean findDiff(String inputUser, String right_Answer, ConnectionSQLite connection) {

		ArrayList<String> arrayColumnsUser;
		ArrayList<String> arrayColumnsAnswer;

		ArrayList<String> arrayListUser;
		ArrayList<String> arrayListAnswer;

		if (!type.equals(listType.get(0))) { // not Select
			connection.resetDatabase();
			connection.connect();

			try {
				connection.getStatement().execute(inputUser);
			} catch (SQLException e) {
				comment = "Il y a une erreur dans votre requête<br>";
				return false;
			}
			inputUser = "select * from " + table + ";";
		}
		ResultSet userResult = connection.queryExecution(inputUser); // USER
		System.out.println("uu:" + inputUser);
		if (userResult == null) {
			comment = commentO + "Il y a une erreur dans votre requête.<br>";
			return false;
		}
		arrayColumnsUser = toArryColumn(userResult);
		arrayListUser = toArryList(userResult, arrayColumnsUser.size());
		// System.out.println("User\n"+arrayListUser+"\nfinUSER");
		if (!type.equals(listType.get(0))) {// not Select
			connection.resetDatabase();
			connection.connect();

			try {
				connection.getStatement().execute(right_Answer);
			} catch (SQLException e) {
				comment = "Il y a une erreur dans requête PROF<br>";
				return false;
			}
			right_Answer = "select * from " + table + ";";
		}
		ResultSet answerResult = connection.queryExecution(right_Answer); // ANSWER
		if (answerResult == null) {
			comment = commentO + "Il y a une erreur dans requête PROF (null)<br>" + right_Answer;
			return false;
		}
		arrayColumnsAnswer = toArryColumn(answerResult);
		arrayListAnswer = toArryList(answerResult, arrayColumnsAnswer.size());
		// System.out.println("Answer\n"+arrayListAnswer+"\nfinAnswer");

		/// VERIFY COLUNNS
		if (type.equals(listType.get(0))) {// Select

			if (!findDiffColumnName(arrayColumnsUser, arrayColumnsAnswer)) {
				System.out.println("findDiff Column false");
				return false;
			}
		} else {
			connection.resetDatabase();
			connection.connect();
		}
		// VERIFY LINE

		if (!findDiffLine(arrayListUser, arrayListAnswer)) {
			System.out.println("findDiff Line false");
			return false;
		}

		return true;
	}

	/**
	 * Creates ArrayList<String> of column name in "resultSet"
	 * 
	 * @param resultSet
	 * @return ArrayList<String> of column name
	 */
	private ArrayList<String> toArryColumn(ResultSet resultSet) {

		try {
			ResultSetMetaData metaResultSet = resultSet.getMetaData();

			int colCount = metaResultSet.getColumnCount();

			ArrayList<String> arrayList = new ArrayList<>();

			for (int i = 1; i <= colCount; i++) {
				arrayList.add(metaResultSet.getColumnName(i).toLowerCase().replace(" ", ""));
			}

			return arrayList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates ArrayList<String> of line content in "resultSet"
	 * 
	 * @param resultSet
	 * @param numberOfColumns
	 * @return ArrayList<String> of line content in "resultSet"
	 */
	private ArrayList<String> toArryList(ResultSet resultSet, int numberOfColumns) {
		ArrayList<String> arrayList = new ArrayList<>();
		String s;
		try {
			while (resultSet.next()) {
				s = "";
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1)
						s += ",";
					s += (resultSet.getString(i));
				}
				arrayList.add(s);
			}

			return arrayList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Function finds difference in name of the Columns in the result
	 * 
	 * @param arrayColumnsUser
	 * @param arrayColumnsAnwser
	 * @return if no find difference
	 */
	private boolean findDiffColumnName(ArrayList<String> arrayColumnsUser, ArrayList<String> arrayColumnsAnwser) {

		Boolean bb = true;

		for (String s : arrayColumnsUser) {
			if (!arrayColumnsAnwser.contains(s)) {
				comment += "la colonne \"" + s + "\" n'est pas à utiliser.<br>";
				bb = false;
			}
		}

		for (String s : arrayColumnsAnwser) {
			if (!arrayColumnsUser.contains(s)) {
				comment += "la colonne \"" + s + "\" est à utiliser.<br>";
				bb = false;
			}
		}
		int max = Math.max(arrayColumnsAnwser.size(), arrayColumnsUser.size());
		for (int i = 0; i < max; i++) {

			if (i < arrayColumnsUser.size() && i < arrayColumnsAnwser.size()) {
				if (!arrayColumnsUser.get(i).equals(arrayColumnsAnwser.get(i))) {
					comment += "la " + (i + 1) + "eme colonne attendu est \"" + arrayColumnsAnwser.get(i)
							+ "\" alors que vous utiliser \"" + arrayColumnsUser.get(i) + "\".<br>";
					bb = false;
				}

			} else if (i >= arrayColumnsUser.size()) {
				comment += "la " + (i + 1) + "eme colonne attendue est \"" + arrayColumnsAnwser.get(i)
						+ "\" mais elle est manquante.<br>";
				bb = false;
			} else {
				comment += "la " + (i + 1) + "eme colonne \"" + arrayColumnsUser.get(i) + "\" est en trop.<br>";
				bb = false;
			}
		}

		return bb;
	}

	/**
	 * Finds difference in line content in the result
	 * 
	 * @param arrayListUser
	 * @param arrayListAnwser
	 * @return if no find difference
	 */
	private boolean findDiffLine(ArrayList<String> arrayListUser, ArrayList<String> arrayListAnwser) {
		Boolean temp = true;
		Boolean bb = true;
		String ligneUser = "";
		String ligneAwser = "";
		String ligne;

		if (listType.get(2).equals(type) || (listType.get(0).equals(type))) {// UPDATE AND SELECT

			for (int i = 0; i < arrayListAnwser.size(); i++) {
				String q = (i < arrayListUser.size()) ? arrayListUser.get(i) : "est absente ce qui est ";
				String a = arrayListAnwser.get(i);
				if (!a.equals(q)) {
					ligneUser = "";
					ligneAwser = "";
					ligne = "";

					if (i < arrayListUser.size()) {

						String sq[] = q.split(",");
						String sa[] = a.split(",");

						for (int j = 0; j < sa.length; j++) {

							q = sq[j];
							a = sa[j];

							if (!q.equals(a)) {
								q = q.toUpperCase(); // UPDATE
								a = a.toUpperCase();
								temp = false;
							}

							ligneUser += q;
							ligneAwser += a;

							if (j < sa.length - 1) {
								ligneUser += ",";
								ligneAwser += ",";
							}

						}
					} else {
						ligneUser = q;
						ligneAwser = a;
					}
					ligne = "ligne(n°" + (i + 1) + ") (" + ligneUser + ") different de la correction: (" + ligneAwser
							+ ") <br>";

					if (!temp) {
						comment += ligne;
						bb = false;
						temp = true;
					}

				}

			}

		}

		if (!listType.get(2).equals(type)) { // other than UPDATE

			for (String s : arrayListUser) {
				if (!arrayListAnwser.contains(s)) {
					comment += " la ligne (" + s + ") est en trop.<br> ";
					bb = false;
				}
			}

			for (String s : arrayListAnwser) {
				if (!arrayListUser.contains(s)) {
					comment += " la ligne (" + s + ") est manquante.<br>";
					bb = false;
				}
			}

		}

		return bb;
	}

	// Function used by "correction_simple"

	// find the Type of the query
	/**
	 * Finds the Type of the query
	 * 
	 * @param query
	 * @return Type of SQL query find in listType
	 */
	private String findType(String query) {
		query = query.toLowerCase();
		String myType;
		for (int i = 0; i < listType.size(); i++) {
			myType = listType.get(i);
			if (compareContent(query, myType)) {
				return myType;
			}

		}
		return "none";
	}

	// Function used by "correction_table"
	/**
	 * use betweenFromAndWhere for find the table on SQL query verify if the table
	 * of the query of "query" and "corr" are the same for type: select or delete
	 * 
	 * @param query
	 * @param corr
	 * @return table of "query" is the same as "corr"
	 */
	private boolean compareTableName(String query, String corr) {

		String q = betweenFromAndWhere(query);
		String r = betweenFromAndWhere(corr);

		if (q.equals(r) || (r.contains("inner join") && q.contains("inner join"))) {
			table = r;
			return true;
		}

		comment = commentO + " le nom de la table n'est pas le bon ou il est mal écrit.(" + q + "!=" + r + ").<br>";
		return false;
	}

	/**
	 * 
	 * @param s
	 * @return the string between from and (where if exist,order by if exist or ;)
	 */
	private String betweenFromAndWhere(String s) {

		String[] s2 = s.toLowerCase().split("from");
		if (s2.length == 2) {

			String q = s2[1];

			if (compareContent(q, "where")) {
				s2 = q.split("where");
			} else if (compareContent(q, "order by")) {
				s2 = q.split("order by");
			} else {
				comment = "";
				s2 = q.split(";");
			}

			if (s2.length >= 1) {
				String sq = s2[0];

				if (!sq.contains("inner join")) {
					sq = sq.replaceAll(" ", "");
				}

				return sq;

			} else {
				System.out.println("ERROR Correcteur.java betweenFromAndWhere(String s)<br>");
			}

		}
		return "";
	}

	/**
	 * use betweenA_And_B for find the table on SQL query
	 * 
	 * @param query
	 * @param corr
	 * @param a
	 * @param b
	 * @return if the table for "query" and "corr" are the same
	 */
	private boolean compareTableName_Between_A_B(String query, String corr, String a, String b) {

		String q = betweenA_And_B(query, a, b);
		String r = betweenA_And_B(corr, a, b);

		String s[];
		q = q.replace("(", "@");
		s = q.split("@");
		q = s[0];
		r = r.replace("(", "@");
		s = r.split("@");
		r = s[0];

		if (q.equals(r)) {
			table = r;
			return true;
		}
		comment = commentO + " le nom de la table n'est pas le bon ou il est mal écrit.(" + q + "!=" + r + ")<br>";
		return false;
	}

	/**
	 * @param s
	 * @param a
	 * @param b
	 * @return the string between "a" and "b" in s
	 */
	private String betweenA_And_B(String s, String a, String b) {
		String[] s2 = s.toLowerCase().split(a);
		if (s2.length == 2) {
			s2 = s2[1].split(b);
			if (s2.length >= 1) {
				return s2[0].replaceAll(" ", "");
			} else {
				System.out.println("ERROR Correcteur.java betweenFromAndWhere(String s)<br>");
			}
		}
		return "";
	}

	/// USE by all

	// find if query.contains(myType) and edit "comment"
	/**
	 * 
	 * @param query
	 * @param myType
	 * @return if "query" contain "myType"
	 */
	Boolean compareContent(String query, String myType) {
		if (query.toLowerCase().contains(myType)) {
			return true;
		}
		comment = "Vous avez oublié ou mal écrit \"" + myType + "\" dans votre requête. (ou oublié un espace)) <br>";

		return false;
	}

	// find if query.contains(myType) ONLY if query2.contains(myType) and edit
	// "comment"
	/**
	 * 
	 * @param query
	 * @param query2
	 * @param mytype
	 * @return if "query2" contain "myType" return (if "query2" contain "myType" )
	 *         else true
	 */
	private Boolean verifyAifExist(String query, String query2, String mytype) {
		if (compareContent(query2, mytype)) {
			return compareContent(query, mytype);
		}
		comment = "";
		return true;
	}

	/**
	 * This method helps the user to write their query correctly
	 * 
	 * @param type type of he query"select,update,delete,insert into"
	 * @return the hint
	 */
	public String definehint(String goodanswer) {
		type = findType(goodanswer);
		String select = "SELECT [* (tout les champs) ou  DISTINCT ] [(column1, column2, column3,...columnN)]\n[FROM table-list]"
				+ "\n" + "[WHERE expr]" + "\n" + "[ORDER BY expr]" + "\n";
		String insert_into = "INSERT INTO TABLE_NAME [(column1, column2, column3,...columnN)]\n"
				+ "VALUES (value1, value2, value3,...valueN);\n";
		String update = "UPDATE table_name\n " + "SET column1 = value1, column2 = value2...., columnN = valueN"
				+ "\n WHERE [condition];";
		String delete = "DELETE FROM table_name \n" + "WHERE [condition];";

		if (listType.get(0).equals(type)) {
			hint = "Le format de Select est : \n" + select;
		} else if (listType.get(1).equals(type))

		{
			hint = "Le format de insert into  est : \n " + insert_into;
		} else if (listType.get(2).equals(type)) {
			hint = "Le format de update est : \n" + update;
		} else if (listType.get(3).equals(type)) {
			hint = "Le format de delete est : \n" + delete;
		}

		return hint;

	}

	public void setHint(String hint) {
		this.hint = hint;
	}

}
