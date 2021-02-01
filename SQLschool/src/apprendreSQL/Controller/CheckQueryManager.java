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

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import apprendreSQL.Model.Table;

public class CheckQueryManager {

	private String output = "";
	private Font font = new Font("Arial", Font.PLAIN, 10);
	private ArrayList<String> tableColumns = new ArrayList<String>();

	/**
	 * Checking if the first word of the query is "select".
	 * 
	 * @param query
	 * @return true if it's select, false otherwise
	 */
	public boolean ifSelectQuery(String query) {
		if (query.toLowerCase().contains("select") && (query.toLowerCase().trim().indexOf("select") == 0)) {
			return true;
		}
		return false;
	}

	/**
	 * Method that executes queries that start with anything but "select" It returns
	 * nothing except if there is an error.
	 * 
	 * @param query
	 * @param connection
	 * @return
	 */
	public String callOtherQuery(String query, ConnectionSQLite connection) {
		output = "";
		try {
			connection.getStatement().execute(query);
			return output = "La rêquete a été effectuée avec succés";
		} catch (SQLException e) {
			return output = "Erreur:" + e.getMessage();

		}

	}

	/**
	 * Method that executes a select query and prints it on the output panel It
	 * colors the columns found in the correct answer It returns either an html
	 * table or an error message.
	 * 
	 * @param query
	 * @param connection
	 * @param eventManager
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String callSelect(String query, ConnectionSQLite connection, EventManager eventManager) {
		ArrayList<String> highlightedColumns = new ArrayList<String>();
		ArrayList<Integer> columnsNumbers = new ArrayList<Integer>();
		ArrayList<String> common = new ArrayList<>(tableColumns);
		String color = "";
		output = "";

		if (eventManager != null)
			highlightedColumns = eventManager.getAnswerColumns();

		try {

			ResultSet resultSet = connection.queryExecution(query);
			if (resultSet == null) {
				output = "Erreur:" + connection.getErrorMessage();
				return output;
			}

			ResultSetMetaData meta = resultSet.getMetaData();

			int colCount = meta.getColumnCount();
			common.retainAll(highlightedColumns);
			common = (ArrayList<String>) common.stream().distinct().collect(toList());
			// draw the tables :Line
			output = output + "<table style= \" padding: 0px 0px 0px 0px; font-family: " + font.getFamily()
					+ "; font-size: 14; font-weight:normal\">\r\n" + "  <colgroup span=\"4\"></colgroup>\r\n";

			output = output + "<tr>";
			for (int column0 = 1; column0 <= colCount; column0++) {

				String columName = meta.getColumnName(column0).toLowerCase();
				color = "";

				for (int i = 0; i < common.size(); i++) {
					if (columName.equals(common.get(i))) {
						columnsNumbers.add(column0);
						color = " style= \"background-color:Red;\"";
					}
				}
				output += "<th " + color + " >" + columName + "</th>\r\n";
			}
			output = output + "</tr>\r\n";

			columnsNumbers = (ArrayList<Integer>) columnsNumbers.stream().distinct().collect(toList());

			while (resultSet.next()) {
				output = output + "<tr>\r\n";
				// draw the tables :Line columns
				for (int column = 1; column <= colCount; column++) {
					Object value = resultSet.getObject(column);
					String tempoColor = "";

					if (columnsNumbers.contains(column))
						tempoColor = color;

					output = output + "<td" + tempoColor + ">" + String.valueOf(value) + "</td>\r\n";
				}
				output = output + "</tr>\r\n";
			}
			output = output + "</table> <br> <br>\r\n";
			return output;

		} catch (SQLException e) {
			output = "Erreur:" + e.getMessage();
		}

		return output;
	}

	@SuppressWarnings("rawtypes")
	private Collector toList() {
		return Collectors.toList();

	}

	/**
	 * Shows the contents of the database in html.
	 * 
	 * @param table
	 * @param connection
	 * @param manager
	 * @return
	 */
	public String getFormattedTable(String table, ConnectionSQLite connection, EventManager manager) {
		output = "";
		String query = "select * from " + table + ";";
		getColumns(query, connection);
		return callSelect(query, connection, manager);

	}

	/**
	 * Listens for a '.table' query and retrieves the tables from the given database.
	 *  
	 * @param query '.tables' query
	 * @param connection SQL connection
	 * @param database database name
	 * @return
	 */
	public ArrayList<Table> getTables(String query, ConnectionSQLite connection, String database) {
		ArrayList<Table> tables = new ArrayList<Table>();
		ResultSet resultSet = connection.queryExecution(query);

		if (resultSet == null) {
			System.out.println(connection.getErrorMessage());
		}

		try {
			while (resultSet.next()) {
				String tableName = resultSet.getString("name");
				tables.add(new Table(tableName, database));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tables;

	}

	/**
	 * This function is responsible for getting the columns of a database table.
	 * 
	 * @param query
	 * @param connectionSQLite
	 * @return
	 */
	public void getColumns(String query, ConnectionSQLite connectionSQLite) {
		try {

			ResultSet resultSet = connectionSQLite.queryExecution(query);
			if (resultSet == null) {
				output = connectionSQLite.getErrorMessage();
				System.out.println("Output: " + output);
			}
			ResultSetMetaData meta = resultSet.getMetaData();
			int colCount = meta.getColumnCount();
			for (int i = 1; i <= colCount; i++) {
				tableColumns.add(meta.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getTableColumns() {
		return tableColumns;
	}

	public void setTableColumns(ArrayList<String> tableColumns) {
		this.tableColumns = tableColumns;
	}

}
