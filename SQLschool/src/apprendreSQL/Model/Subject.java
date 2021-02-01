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
package apprendreSQL.Model;

/**
 * The Subject object that represents the different subjects of the application.
 *
 */
public class Subject {

	private long id_Subject;
	private String titleSubject;
	private DataBase database;

	public Subject(String name, DataBase database) {
		this.titleSubject = name;
		this.setDatabase(database);
	}

	public long getId_Subject() {
		return id_Subject;
	}

	public void setId_Subject(long id_Subject) {
		this.id_Subject = id_Subject;
	}

	public String getTitleSubject() {
		return titleSubject;
	}

	public void setTitleSubject(String title_Subject) {
		this.titleSubject = title_Subject;
	}

	public DataBase getDatabase() {
		return database;
	}

	public void setDatabase(DataBase database) {
		this.database = database;
	}

}
