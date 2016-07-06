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

package de.dekarlab.moneybuilder.calc;

import java.util.List;

import de.dekarlab.moneybuilder.model.Account;
import de.dekarlab.moneybuilder.model.Book;
import de.dekarlab.moneybuilder.model.Folder;
import de.dekarlab.moneybuilder.model.Period;
import de.dekarlab.moneybuilder.model.Transaction;
import de.dekarlab.moneybuilder.model.Value;

/**
 * Calculator.
 * 
 * @author dk
 *
 */
public class Calculator {
	/**
	 * Recalculate period.
	 */
	public static void recalc(Book book, Period period, boolean calcTrans) {
		if (calcTrans) {
			List<Transaction> list = period.getTransactions();
			Transaction trans = null;
			for (int i = 0; i < list.size(); i++) {
				trans = list.get(i);
				recalc(book, period, trans, false, null, null);
			}
		}
		// recalculate all leaf accounts
		recalcFolderDown(book, period, book.getAssetList());
		recalcFolderDown(book, period, book.getLiabilityList());
		recalcFolderDown(book, period, book.getIncomeList());
		recalcFolderDown(book, period, book.getExpenseList());

	}

	/**
	 * Calculate accounts.
	 * 
	 * @param book
	 * @param period
	 * @param acnt
	 */
	public static void recalcFolderDown(Book book, Period period, Folder folder) {
		for (Account acnt : folder.getList()) {
			if (acnt instanceof Folder) {
				recalcFolderDown(book, period, (Folder) acnt);
			} else {
				recalcAccount(book, period, acnt);
			}
		}
	}

	/**
	 * Recalculate transaction.
	 * 
	 * @param book
	 * @param period
	 * @param trans
	 */
	public static void recalc(Book book, Period period, Transaction trans, boolean calcParentAccnt, Account oldFrom,
			Account oldTo) {
		Account from = null;
		Account to = null;
		Value valueFrom = null;
		Value valueTo = null;
		Value oldValueFrom = null;
		Value oldValueTo = null;

		from = trans.getFrom();
		to = trans.getTo();
		valueFrom = from.getValue(period.getId());
		valueTo = to.getValue(period.getId());
		if (oldFrom != null && oldTo != null) {
			oldValueFrom = oldFrom.getValue(period.getId());
			oldValueTo = oldTo.getValue(period.getId());
		}

		List<Transaction> transList = period.getTransactions();
		valueFrom.setDebit(0.0);
		valueTo.setCredit(0.0);
		if (oldFrom != null && oldTo != null) {
			oldValueFrom.setDebit(0.0);
			oldValueTo.setCredit(0.0);
		}
		for (Transaction tr : transList) {
			if (tr.getFrom().equals(from)) {
				valueFrom.addDebit(tr.getValue());
			}
			if (tr.getTo().equals(to)) {
				valueTo.addCredit(tr.getValue());
			}
			if (oldFrom != null && oldTo != null) {
				// do not calculate twice
				if (tr.getFrom().equals(oldFrom) && !oldFrom.equals(from) && !oldFrom.equals(to)) {
					oldValueFrom.addDebit(tr.getValue());
				}
				//do not calculate twice
				if (tr.getTo().equals(oldTo) && !oldFrom.equals(from) && !oldFrom.equals(to)) {
					oldValueTo.addCredit(tr.getValue());
				}
			}
		}
		if (calcParentAccnt) {
			recalcAccount(book, period, from);
			recalcAccount(book, period, to);
			if (oldFrom != null && oldTo != null) {
				recalcAccount(book, period, oldFrom);
				recalcAccount(book, period, oldTo);
			}
		}
	}

	/**
	 * Recalculate account, if values are changed.
	 * 
	 * @param acnt
	 */
	public static void recalcAccount(Book book, Period curr, Account acnt) {
		Value value = acnt.getValue(curr.getId());
		List<Period> periodList = book.getPeriodList();
		Value valuePrev = null;
		Period prev = null;
		int index = periodList.indexOf(curr);
		if (index > 0) {
			prev = periodList.get(index - 1);
			valuePrev = acnt.getValue(prev.getId());
		}
		if (acnt.getType() == Account.TYPE_ASSET) {
			if (valuePrev != null) {
				value.setStartOfPeriod(value != null ? valuePrev.getEndOfPeriod() : 0.0);
			} else {
				value.setStartOfPeriod(0.0);
			}
			if (!value.isManual()) {
				value.setEndOfPeriod(value.getStartOfPeriod() + (value.getCredit() - value.getDebit()));
			}
			value.setControl(
					(value.getEndOfPeriod() - value.getStartOfPeriod()) - (value.getCredit() - value.getDebit()));
		} else if (acnt.getType() == Account.TYPE_LIABILITY) {
			if (valuePrev != null) {
				value.setStartOfPeriod(value != null ? valuePrev.getEndOfPeriod() : 0.0);
			} else {
				value.setStartOfPeriod(0.0);
			}
			if (!value.isManual()) {
				value.setEndOfPeriod(value.getStartOfPeriod() - (value.getCredit() - value.getDebit()));
			}
			value.setControl(
					(value.getEndOfPeriod() - value.getStartOfPeriod()) + (value.getCredit() - value.getDebit()));
		} else if (acnt.getType() == Account.TYPE_INCOME) {
			value.setStartOfPeriod(0.0);
			if (!value.isManual()) {
				value.setEndOfPeriod(0.0);
			}
			// value.setControl((value.getDebit() - value.getCredit()));
		} else if (acnt.getType() == Account.TYPE_EXPENSE) {
			value.setStartOfPeriod(0.0);
			if (!value.isManual()) {
				value.setEndOfPeriod(0.0);
			}
			// value.setControl((value.getCredit() - value.getDebit()));
		}
		recalcFolderUp(book, curr, acnt.getParent());
		// Recalculate next period
		Period next = null;
		if (periodList.size() > (index + 1)) {
			next = periodList.get(index + 1);
		}
		if (next != null) {
			recalcAccount(book, next, acnt);
		}
	}

	/**
	 * Recalculate folder.
	 * 
	 * @param book
	 * @param curr
	 * @param folder
	 */
	public static void recalcFolderUp(Book book, Period curr, Folder folder) {
		List<Account> acntList = folder.getList();
		Value valueFolder = folder.getValue(curr.getId());
		Value valueAcnt = null;
		// clear
		valueFolder.setEndOfPeriod(0.0);
		valueFolder.setStartOfPeriod(0.0);
		valueFolder.setCredit(0.0);
		valueFolder.setDebit(0.0);
		// control
		valueFolder.setControl(0.0);
		// budget
		folder.setBudget(curr.getId(), 0.0);
		// recalculate full
		for (Account acnt : acntList) {
			valueAcnt = acnt.getValue(curr.getId());
			valueFolder.setEndOfPeriod(valueFolder.getEndOfPeriod() + valueAcnt.getEndOfPeriod());
			valueFolder.setStartOfPeriod(valueFolder.getStartOfPeriod() + valueAcnt.getStartOfPeriod());
			valueFolder.setCredit(valueFolder.getCredit() + valueAcnt.getCredit());
			valueFolder.setDebit(valueFolder.getDebit() + valueAcnt.getDebit());
			// control
			valueFolder.setControl(valueFolder.getControl() + valueAcnt.getControl());
			// budget
			folder.setBudget(curr.getId(), folder.getBudget(curr.getId()) + acnt.getBudget(curr.getId()));
		}
		if (folder.getParent() != null) {
			recalcFolderUp(book, curr, folder.getParent());
		} else {
			Value valueBook = book.getValue(curr.getId());
			Value valueAsset = book.getAssetList().getValue(curr.getId());
			Value valueLiability = book.getLiabilityList().getValue(curr.getId());
			Value valueIncome = book.getIncomeList().getValue(curr.getId());
			Value valueExpense = book.getExpenseList().getValue(curr.getId());

			valueBook.setStartOfPeriod(valueAsset.getStartOfPeriod() - valueLiability.getStartOfPeriod());
			if (!valueBook.isManual()) {
				valueBook.setEndOfPeriod(valueAsset.getEndOfPeriod() - valueLiability.getEndOfPeriod());
			}
			valueBook.setCredit(valueAsset.getCredit() + valueLiability.getCredit() + valueIncome.getCredit()
					+ valueExpense.getCredit());
			valueBook.setDebit(valueAsset.getDebit() + valueLiability.getDebit() + valueIncome.getDebit()
					+ valueExpense.getDebit());
			valueBook.setControl(valueAsset.getControl() + valueLiability.getControl() + valueIncome.getControl()
					+ valueExpense.getControl());
		}
	}

	/**
	 * Clear all periods.
	 * 
	 * @param book
	 */
	public static void clearAll(Book book) {
		book.clearValue();
		clearAll(book.getAssetList());
		book.getAssetList().clearValue();
		clearAll(book.getLiabilityList());
		book.getLiabilityList().clearValue();
		clearAll(book.getIncomeList());
		book.getIncomeList().clearValue();
		clearAll(book.getExpenseList());
		book.getExpenseList().clearValue();
	}

	/**
	 * Clear all.
	 * 
	 * @param lst
	 */
	public static void clearAll(Folder folder) {
		for (Account acnt : folder.getList()) {
			acnt.clearValue();
			acnt.clearBudget();
		}
	}
}