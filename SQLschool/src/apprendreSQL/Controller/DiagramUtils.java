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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * This class provides the necessary functions to generate and manipulate
 * relational diagrams off a database, with the help of SchemaSpy and Graphviz,
 * it also checks the availability of the needed resources and for compatibility
 * issues.
 * 
 * http://schemaspy.org/ 
 * https://www.graphviz.org/
 *
 */
public class DiagramUtils {
	private static String os = System.getProperty("os.name");
	private static final String dir = System.getProperty("user.dir");
	public static final String temp = dir + "\\graphs\\temp";
	private String graphvizInstallation;

	/**
	 * Uses system's command line tool to run SchemaSpy on a database file. This
	 * generates html, .png and .dot diagram files in .../graphs/temp.
	 * 
	 * @param database input database
	 * @throws IOException File reader exception
	 */
	public void generateDiagram(String database) throws IOException {
		System.out.println("Graph log - Current dir: " + dir);
		System.out.println("Graph log - Temp dir: " + temp);

		File graphsDirectory = new File(temp);

		graphsDirectory.mkdir();

		if (checkInstallation()) {
			ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c",
					"java -jar schemaSpy.jar" + " -t sqlite" + " -db " + database + " -u admin" + " -dp sqlite-jdbc.jar"
							+ " -o \"" + temp + "\" -gv \"" + graphvizInstallation + "\"");
			processBuilder.directory(new File(dir + "\\graphs"));
			processBuilder.redirectErrorStream(true);
			String message;
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			System.out.println("--------------------------- LOG ---------------------------");

			while (true) {
				message = reader.readLine();
				if (message == null) {
					break;
				}
				System.out.println(message);
			}

			System.out.println("--------------------------- LOG ---------------------------");

			safeDatabaseDeletion(database);
		} else {
			System.out.println("Graph log - Missing Graphviz installation.");
		}
	}

	/**
	 * Cycles through the temp directory and runs the deleteDirectory(File) function
	 * on each one of the files everytime the program runs.
	 */
	public void cleanDirectory() {
		if (checkDirectory()) {
			File directory = new File(temp);
			directory.mkdir();
			if (directory != null) {
				for (File file : directory.listFiles()) {
					if (file != null) {
						deleteDirectory(file);
					}
				}
			}
		}
	}

	/**
	 * Deletes the File passed as parameter.
	 * 
	 * @param directory the File to be deleted
	 */

	public void deleteDirectory(File directory) {
		File[] subFolders = directory.listFiles();
		if (subFolders != null) {
			for (File file : subFolders) {
				if (file != null) {
					deleteDirectory(file);
				}
			}
		}
		directory.delete();
	}

	/**
	 * Checks whether graphs directory is present.
	 * 
	 * @return true if present, false otherwise
	 */

	public boolean checkDirectory() {
		boolean isAvailable = false;
		File graphsDir = new File(dir + "\\graphs");
		if (graphsDir != null && graphsDir.isDirectory()) {
			isAvailable = true;
		}
		return isAvailable;
	}

	/**
	 * Copies the desired database file from .../resource to .../graphs.
	 * 
	 * @param databaseName the name of the database
	 */
	public void safeDatabaseTransfer(String databaseName) {
		final String databaseDir = dir + "\\resource\\" + databaseName;
		final String destinationDir = dir + "\\graphs\\" + databaseName;
		System.out.println("Graph log - Copying from " + databaseDir + " to " + destinationDir + "...");

		try {
			Files.copy(Paths.get(databaseDir), Paths.get(destinationDir), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the desired database file from .../graphs.
	 * 
	 * @param databaseName the name of the database
	 */
	public void safeDatabaseDeletion(String databaseName) {
		final String database = dir + "\\graphs\\" + databaseName;
		System.out.println("Graph log - Deleting " + database + "...");
		try {
			Files.deleteIfExists(Paths.get(database));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether Graphviz is installed on the system.
	 * 
	 * @return true if Graphviz is installed, false otherwise
	 */
	public boolean checkInstallation() {
		File programsDir = new File("C:\\Program Files (x86)");
		boolean isInstalled = false;

		for (File file : programsDir.listFiles()) {
			if (file.isDirectory()) {
				if (file.getName().contains("Graphviz")) {
					this.graphvizInstallation = file.getAbsolutePath();
					System.out.println("Graph log - Located Graphviz intallation: " + graphvizInstallation);
					isInstalled = true;
				}
			}
		}
		return isInstalled;
	}

	/**
	 * Checks if the program is running on a Windows machine.
	 * 
	 * @return true if the OS is Windows, false otherwise
	 */
	public boolean checkOS() {
		return os.toLowerCase().contains("windows");
	}
}