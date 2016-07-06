/**
* Copyright (C) 2016,  Denis Karlow, DekarLab.de/BehindTheStrategy.com
*  
* This file is part of Dekar Lab Money Builder Software.
* 
* Dekar Lab Money Builder Software is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* Dekar Lab Money Builder Software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with DEKAR Lab CapBuilder Software.  If not, see <http://www.gnu.org/licenses/>.
*
* Diese Datei ist Teil von Dekar Lab Money Builder Software.
* 
* Dekar Lab Money Builder ist Freie Software: Sie können es unter den Bedingungen
* der GNU General Public License, wie von der Free Software Foundation,
* Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
* veröffentlichten Version, weiterverbreiten und/oder modifizieren.
* 
* Dekar Lab Money Builder Software wird in der Hoffnung, dass es nützlich sein wird, aber
* OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
* Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
* Siehe die GNU General Public License für weitere Details.

* Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
* Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
*/

package de.dekarlab.moneybuilder.fix;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;

public class DekarlabGrid extends Grid {

	public DekarlabGrid(Composite parent, int style) {
		super(parent, style);
	}

	public GridItem getNextVisibleItem(GridItem item) {
		if (item.getParent() == null) {
			return null;
		}
		if (item.getParent().getItems().length == 0) {
			return null;
		} else {
			GridItem[] items = item.getParent().getItems();
			for (GridItem itemA : items) {
				if (itemA.equals(item)) {
					return super.getNextVisibleItem(item);
				}
			}
			return items[items.length - 1];
		}
	}

	public GridItem getPreviousVisibleItem(GridItem item) {
		if (item.getParent() == null) {
			return null;
		}
		if (item.getParent().getItems().length == 0) {
			return null;
		} else {
			GridItem[] items = item.getParent().getItems();
			for (GridItem itemA : items) {
				if (itemA.equals(item)) {
					return super.getPreviousVisibleItem(item);
				}
			}
			return items[items.length - 1];
		}
	}

}
