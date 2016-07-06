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

package de.dekarlab.moneybuilder.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Period.
 * 
 * @author dk
 *
 */
public class Period {
	private String id;
	private Date date;
	private List<Transaction> transactions;
	private Value value;

	/**
	 * Constructor.
	 */
	public Period(Date date) {
		this.date = date;
		this.transactions = new ArrayList<Transaction>();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public Date getDate() {
		return date;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String index) {
		this.id = index;
	}

}
