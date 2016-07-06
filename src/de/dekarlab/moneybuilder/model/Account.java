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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Constructor.
 * 
 * @author dk
 *
 */
public class Account {
	public static final int TYPE_ASSET = 1;
	public static final int TYPE_LIABILITY = 2;
	public static final int TYPE_INCOME = 3;
	public static final int TYPE_EXPENSE = 4;
	/**
	 * Value.
	 */
	private Map<String, Value> periodValue;
	/**
	 * Budget.
	 */
	private Map<String, Double> periodBudget;
	/**
	 * Name of account.
	 */
	private String name;
	/**
	 * Parent folder.
	 */
	private Folder parent;
	/**
	 * Type.
	 */
	private int type;
	/**
	 * Is account hidden?
	 */
	private boolean hidden = false;
	/**
	 * Is account hidden from date?
	 */
	private Date hiddenDate;
	/**
	 * Expanded attribute for view.
	 */
	private boolean viewExpanded;

	/**
	 * Constructor.
	 */
	public Account(int type) {
		this.type = type;
		this.periodValue = new HashMap<String, Value>();
		this.periodBudget = new HashMap<String, Double>();
	}

	/**
	 * Get naem.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set naem.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get parent.
	 * 
	 * @return
	 */
	public Folder getParent() {
		return parent;
	}

	/**
	 * Set parent.
	 * 
	 * @param parent
	 */
	public void setParent(Folder parent) {
		this.parent = parent;
	}

	/**
	 * Get type.
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set type.
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Get value.
	 * 
	 * @param periodId
	 * @return
	 */
	public Value getValue(String periodId) {
		Value value = periodValue.get(periodId);
		if (value == null) {
			value = new Value();
			periodValue.put(periodId, value);
		}
		return value;
	}

	/**
	 * Set value.
	 * 
	 * @param periodId
	 * @param value
	 */
	public void setValue(String periodId, Value value) {
		periodValue.put(periodId, value);
	}

	/**
	 * Get budget.
	 * 
	 * @param periodId
	 * @return
	 */
	public Double getBudget(String periodId) {
		Double val = periodBudget.get(periodId);
		if (val == null) {
			val = 0.0;
			periodBudget.put(periodId, val);
		}
		return val;
	}

	/**
	 * Set budget.
	 * 
	 * @param periodId
	 * @param value
	 */
	public void setBudget(String periodId, Double value) {
		periodBudget.put(periodId, value);
	}

	/**
	 * Clear value.
	 */
	public void clearValue() {
		Iterator<String> it = periodValue.keySet().iterator();
		while (it.hasNext()) {
			Value val = periodValue.get(it.next());
			val.setStartOfPeriod(0.0);
			val.setCredit(0.0);
			val.setDebit(0.0);
			if (!val.isManual()) {
				val.setEndOfPeriod(0.0);
			}
			val.setControl(0.0);
		}
	}

	/**
	 * Clear budget.
	 */
	public void clearBudget() {
		Iterator<String> it = periodBudget.keySet().iterator();
		while (it.hasNext()) {
			periodBudget.put(it.next(), 0.0);
		}
	}

	/**
	 * Get absolute name.
	 * 
	 * @return
	 */
	public String getAbsName() {
		if (getParent() == null) {
			return getName();
		} else {
			return getParent().getAbsName() + "/" + getName();
		}
	}

	/**
	 * Is view expanded.
	 * 
	 * @return
	 */
	public boolean isViewExpanded() {
		return viewExpanded;
	}

	/**
	 * Set view expanded.
	 * 
	 * @param viewExpanded
	 */
	public void setViewExpanded(boolean viewExpanded) {
		this.viewExpanded = viewExpanded;
	}

	/**
	 * Get period value.
	 * 
	 * @return
	 */
	public Map<String, Value> getPeriodValue() {
		return periodValue;
	}

	/**
	 * Get period budget.
	 * 
	 * @return
	 */
	public Map<String, Double> getPeriodBudget() {
		return periodBudget;
	}

	/**
	 * Is hidden?
	 * 
	 * @return
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Set hidden.
	 * 
	 * @param hidden
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Get hidden date.
	 * 
	 * @return
	 */
	public Date getHiddenDate() {
		return hiddenDate;
	}

	/**
	 * Set hidden date.
	 * 
	 * @param hiddenDate
	 */
	public void setHiddenDate(Date hiddenDate) {
		this.hiddenDate = hiddenDate;
	}
}
