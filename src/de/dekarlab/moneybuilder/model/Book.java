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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model root.
 * 
 * @author dk
 *
 */
public class Book {
	/**
	 * Name.
	 */
	private String name;
	/**
	 * Asset list.
	 */
	private Folder assetList;
	/**
	 * Liability list.
	 */
	private Folder liabilityList;
	/**
	 * Income list.
	 */
	private Folder incomeList;
	/**
	 * Expense list.
	 */
	private Folder expenseList;

	/**
	 * List of periods.
	 */
	private List<Period> periodList;
	/**
	 * Current period id.
	 */
	private String currPeriodId;
	/**
	 * Current period.
	 */
	private Period currPeriod;
	/**
	 * Value.
	 */
	private Map<String, Value> periodValue;
	/**
	 * Budget.
	 */
	private Map<String, Double> periodBudget;

	/**
	 * Root.
	 */
	public Book() {
		this.periodList = new ArrayList<Period>();
		this.assetList = new Folder(Account.TYPE_ASSET);
		this.liabilityList = new Folder(Account.TYPE_LIABILITY);
		this.incomeList = new Folder(Account.TYPE_INCOME);
		this.expenseList = new Folder(Account.TYPE_EXPENSE);
		this.periodValue = new HashMap<String, Value>();
		this.periodBudget = new HashMap<String, Double>();
	}

	/**
	 * Get period.
	 * 
	 * @return
	 */
	public List<Period> getPeriodList() {
		return periodList;
	}

	/**
	 * Get current period.
	 * 
	 * @return
	 */
	public Period getCurrPeriod() {
		if (currPeriod == null) {
			for (int i = 0; i < periodList.size(); i++) {
				if (periodList.get(i).getId().equals(currPeriodId)) {
					currPeriod = periodList.get(i);
				}
			}
		}

		return currPeriod;
	}

	/**
	 * Set current period.
	 * 
	 * @param currPeriod
	 */
	public void setCurrPeriod(Period currPeriod) {
		this.currPeriod = currPeriod;
		this.currPeriodId = currPeriod.getId();
	}

	/**
	 * Get current period id.
	 */
	public String getCurrPeriodId() {
		return currPeriodId;
	}

	/**
	 * Set current period.
	 * 
	 * @param currPeriod
	 */
	public void setCurrPeriodId(String currPeriodId) {
		this.currPeriodId = currPeriodId;
	}

	/**
	 * Get assets.
	 * 
	 * @return
	 */
	public Folder getAssetList() {
		return assetList;
	}

	/**
	 * Get liabilities.
	 * 
	 * @return
	 */
	public Folder getLiabilityList() {
		return liabilityList;
	}

	/**
	 * Get incomes.
	 * 
	 * @return
	 */
	public Folder getIncomeList() {
		return incomeList;
	}

	/**
	 * Get expenses.
	 * 
	 * @return
	 */
	public Folder getExpenseList() {
		return expenseList;
	}

	public Value getValue(String periodId) {
		Value value = periodValue.get(periodId);
		if (value == null) {
			value = new Value();
			periodValue.put(periodId, value);
		}
		return value;
	}

	public void setValue(String periodId, Value value) {
		periodValue.put(periodId, value);
	}

	public Double getBudget(String periodId) {
		return periodBudget.get(periodId);
	}

	public void setBudget(Period period, Double value) {
		periodBudget.put(period.getId(), value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAssetList(Folder assetList) {
		this.assetList = assetList;
	}

	public void setLiabilityList(Folder liabilityList) {
		this.liabilityList = liabilityList;
	}

	public void setIncomeList(Folder incomeList) {
		this.incomeList = incomeList;
	}

	public void setExpenseList(Folder expenseList) {
		this.expenseList = expenseList;
	}

	/**
	 * Get account by name.
	 * 
	 * @param absId
	 * @return
	 */
	public Account getAccountByName(String absName) {
		Account res = getAccountByName(getAssetList(), absName);
		if (res != null) {
			return res;
		}
		res = getAccountByName(getLiabilityList(), absName);
		if (res != null) {
			return res;
		}
		res = getAccountByName(getIncomeList(), absName);
		if (res != null) {
			return res;
		}
		res = getAccountByName(getExpenseList(), absName);
		if (res != null) {
			return res;
		}
		return null;
	}

	/**
	 * Get account from folder by name.
	 * 
	 * @param list
	 * @param absId
	 * @return
	 */
	public Account getAccountByName(Folder list, String absName) {
		for (int i = 0; i < list.getList().size(); i++) {
			Account acn = list.getList().get(i);
			if (acn.getAbsName().equals(absName)) {
				return acn;
			}
			if (acn instanceof Folder) {
				Account acn2 = getAccountByName((Folder) acn, absName);
				if (acn2 != null) {
					return acn2;
				}
			}
		}
		return null;
	}

	/**
	 * Clear values.
	 */
	public void clearValue() {
		this.periodValue = new HashMap<String, Value>();
	}

	/**
	 * Get account list (only leafs)
	 * 
	 * @return
	 */
	public List<Account> getAccountList() {
		List<Account> list = new ArrayList<Account>();
		getAccountList(getAssetList(), list);
		getAccountList(getLiabilityList(), list);
		getAccountList(getIncomeList(), list);
		getAccountList(getExpenseList(), list);
		return list;
	}

	/**
	 * Get account list.
	 * 
	 * @param folder
	 * @param list
	 */
	protected void getAccountList(Folder folder, List<Account> list) {
		for (Account acnt : folder.getList()) {
			if (acnt instanceof Folder) {
				getAccountList((Folder) acnt, list);
			} else {
				list.add(acnt);
			}
		}
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

}
