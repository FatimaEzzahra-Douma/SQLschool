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

import java.util.ArrayList;

import apprendreSQL.Controller.EventManager;

/**
 * This class represents a Database object.
 *
 */
public class DataBase {

	private long id_database;
	private String nameDatabase;
	private ArrayList<Table> tables;

	public DataBase(Long id_database, String name_Database) {
		this.id_database = id_database;
		this.nameDatabase = name_Database;
	}

	public DataBase(String name, EventManager manager) {
		this.nameDatabase = name;
		this.tables = new ArrayList<Table>();
		this.tables = manager.getTables(name);
	}

	public Long getId_database() {
		return id_database;
	}

	public void setId_database(long id_database) {
		this.id_database = id_database;
	}

	public String getNameDatabase() {
		return nameDatabase;
	}

	public void setNameDatabase(String name_Database) {
		this.nameDatabase = name_Database;
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}

}
