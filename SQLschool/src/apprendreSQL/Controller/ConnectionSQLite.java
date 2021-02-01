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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionSQLite {

	private String dBPathModif;
	private String dBPathOrigin = "";

	private Connection connection = null;
	private Statement statement = null;
	private String errorMessage = "";

	public ConnectionSQLite(String dBPath) {
		dBPathOrigin = dBPath;
		resetDatabase();
		dBPathModif = dBPathOrigin.replace(".db", "") + "_versionReset.db";
	}

	public ConnectionSQLite(String dBPath, boolean action) {
		dBPathOrigin = dBPath;
		resetDatabase();
		dBPathModif = dBPathOrigin.replace(".db", "") + "_versionReset.db";
	}

	/**
	 * Connects to the database.
	 * 
	 * @return
	 */
	public Boolean connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + dBPathModif);
			this.statement = connection.createStatement();
			System.out.println("Connexion à " + dBPathModif + " avec succès");
			return true;
		} catch (ClassNotFoundException notFoundException) {
			notFoundException.printStackTrace();
			System.out.println("Erreur de connection0");
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.out.println("Erreur de connection1");
		}
		return false;
	}

	/**
	 * Closes the connection.
	 */
	public void close() {
		try {
			connection.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes a query and returns the result as a ResultSet object.
	 * 
	 * @param query
	 * @return
	 */
	public ResultSet queryExecution(String query) {
		ResultSet result = null;
		errorMessage = "";
		try {
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			errorMessage = e.getMessage();
		}
		return result;

	}

	/**
	 * Creates a backup file of the database.
	 */
	public void resetDatabase() {

		FileInputStream sourceDirectory = null;
		FileOutputStream targetDirectory = null;

		try {
			File source = new File(dBPathOrigin);
			File destination = new File(dBPathOrigin.replace(".db", "") + "_versionReset.db");

			sourceDirectory = new FileInputStream(source);
			targetDirectory = new FileOutputStream(destination);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = sourceDirectory.read(buffer)) > 0) {
				targetDirectory.write(buffer, 0, length);
			}

			sourceDirectory.close();
			targetDirectory.close();

			System.out.println("File copied successfully.");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Statement getStatement() {
		return statement;
	}
	
	public String getdBPathOrigin() {
		return dBPathOrigin;
	}

}
