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
package apprendreSQL.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import apprendreSQL.Model.Table;
import apprendreSQL.Controller.EventManager;

/**
 * This is the panel that prints out a preview of the tables for the selected
 * database.
 * 
 */

@SuppressWarnings("serial")
public class TablesView extends JPanel {

	private JScrollPane tablesViewScrollPane;
	private JLabel tableLabel;
	private JPanel view, menu;
	private JComboBox<String> tablesList;
	private JTable table;
	private String[] elements = { "" };
	private String[] columnNames = { "" };
	private ArrayList<Table> tableObjects;
	private Object[][] data = {};
	private EventManager manager;

	public TablesView(EventManager manager) {
		init(manager);
	}

	/**
	 * Initialize our swing components.
	 * 
	 * @param manager object that coordinates, edits and transfers information
	 *                between the different swing objects of our project
	 */
	private void init(EventManager manager) {
		this.manager = manager;
		this.view = new JPanel();
		this.menu = new JPanel();
		this.tableLabel = new JLabel("Table: ");
		this.tablesList = new JComboBox<String>(elements);
		menu.setVisible(false);
		this.table = new JTable(data, columnNames);
		this.tableObjects = new ArrayList<Table>();
		view.setLayout(new BorderLayout());
		view.setBackground(Color.black);

		tablesViewScrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		tablesViewScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		tablesList.addActionListener(e -> {
			updateTableModel();
		});

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		view.add(tablesViewScrollPane);
		menu.add(tableLabel);
		menu.add(tablesList);

		c.gridheight = 1;
		c.gridx = c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.LINE_START;
		add(menu, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.;
		c.weighty = 1.;
		c.fill = GridBagConstraints.BOTH;
		add(view, c);

	}

	/**
	 * Draws databases tables on the output.
	 * 
	 * @param tables a list of tables extracted from the selected database
	 */
	public void updateTablesView(ArrayList<Table> tables) {
		tableObjects = tables;
		menu.setVisible(true);
		tablesList.removeAllItems();
		for (Table table : tables) {
			tablesList.addItem(table.getName());
		}

	}

	/**
	 * Returns a JTable model filled with data from the database.
	 * 
	 * @param rs ResultSet from which data is extracted
	 * @return a JTable model
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static DefaultTableModel getTableModel(ResultSet rs) {
		try {
			ResultSetMetaData data = rs.getMetaData();
			int numberOfColumns = data.getColumnCount();

			Vector columnNames = new Vector();
			for (int column = 0; column < numberOfColumns; column++) {
				columnNames.addElement(data.getColumnLabel(column + 1));
			}

			Vector rows = new Vector();
			while (rs.next()) {
				Vector newRow = new Vector();
				for (int i = 1; i <= numberOfColumns; i++) {
					newRow.addElement(rs.getObject(i));
				}
				rows.addElement(newRow);
			}

			return new DefaultTableModel(rows, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Updates the data on the table.
	 */
	public void updateTableModel() {
		if (tablesList.getSelectedItem() != null) {
			System.out.println("Table: " + tablesList.getSelectedItem().toString());
			if (!tableObjects.isEmpty()) {
			table.setModel(getTableModel(
					manager.getTable(
							tableObjects.get(0).getDatabase(),
							tablesList.getSelectedItem().toString())));
			}
		}
	}
}
