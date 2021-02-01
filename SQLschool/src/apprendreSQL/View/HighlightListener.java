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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * 
 * This class observes if a specific component is empty and if it's the case it
 * highlights its border red
 *
 */
public class HighlightListener implements DocumentListener {

	JTextComponent component;
	Border defaultBorder;
	Border highlightBorder;

	public HighlightListener(JTextComponent jtc) {
		highlightBorder = BorderFactory.createLineBorder(Color.red);
		component = jtc;
		defaultBorder = component.getBorder();
		component.getDocument().addDocumentListener(this);
		this.maybeHighlight();
	}

	public void insertUpdate(DocumentEvent e) {
		maybeHighlight();
	}

	public void removeUpdate(DocumentEvent e) {
		maybeHighlight();
	}

	public void changedUpdate(DocumentEvent e) {
		maybeHighlight();
	}

	/**
	 * The function responsible for highlighting the border red
	 */
	private void maybeHighlight() {
		if (!component.getText().isEmpty())
			component.setBorder(defaultBorder);
		else
			component.setBorder(highlightBorder);
	}

}