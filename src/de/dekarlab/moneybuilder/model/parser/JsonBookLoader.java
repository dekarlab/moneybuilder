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

package de.dekarlab.moneybuilder.model.parser;

import java.io.File;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.dekarlab.moneybuilder.App;
import de.dekarlab.moneybuilder.Logger;
import de.dekarlab.moneybuilder.model.Account;
import de.dekarlab.moneybuilder.model.Book;
import de.dekarlab.moneybuilder.model.Folder;
import de.dekarlab.moneybuilder.model.Period;
import de.dekarlab.moneybuilder.model.Transaction;
import de.dekarlab.moneybuilder.model.Value;
import de.dekarlab.moneybuilder.model.util.Formatter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Class for loading Book from JSON file.
 * 
 * @author dk
 *
 */
public class JsonBookLoader {
	/**
	 * Load book from JSON file.
	 * 
	 * @return
	 */
	public static Book loadBook(File file) throws Exception {
		Book book = new Book();
		if (!file.exists()) {
			book.setName("Book");
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			// create period
			Period newPeriod = new Period(cal.getTime());
			newPeriod.setId(Formatter.formatDateId(cal.getTime()));
			book.getPeriodList().add(newPeriod);
			book.setCurrPeriod(newPeriod);
			book.getAssetList().setName(App.getGuiProp("default.assets"));
			book.getLiabilityList().setName(App.getGuiProp("default.liability"));
			book.getExpenseList().setName(App.getGuiProp("default.expenses"));
			book.getIncomeList().setName(App.getGuiProp("default.income"));
			return book;
		}
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			JSONTokener jsonTokener = new JSONTokener(fr);
			JSONObject root = new JSONObject(jsonTokener);
			JSONObject jsonBook = root.getJSONObject("book");
			book.setName(jsonBook.getString("name"));
			book.setCurrPeriodId(jsonBook.getString("currPeriodId"));
			parseAccounts(jsonBook.getJSONObject("assets"), book.getAssetList());
			parseAccounts(jsonBook.getJSONObject("liability"), book.getLiabilityList());
			parseAccounts(jsonBook.getJSONObject("income"), book.getIncomeList());
			parseAccounts(jsonBook.getJSONObject("expenses"), book.getExpenseList());
			parsePeriods(jsonBook.getJSONArray("periodList"), book.getPeriodList(), book);
		} finally {
			if (fr != null) {
				fr.close();
			}
		}
		return book;
	}

	/**
	 * Parse accounts.
	 * 
	 * @param jsonFolder
	 * @param folder
	 */
	protected static void parseAccounts(JSONObject jsonFolder, Folder folder) {
		// add attributes to current folder.
		folder.setName(jsonFolder.getString("name"));
		parseAccountValues(jsonFolder, folder);
		// ----------------
		JSONArray list = jsonFolder.getJSONArray("list");
		for (int i = 0; i < list.length(); i++) {
			JSONObject jsonNewAccount = list.getJSONObject(i);
			if (jsonNewAccount.optString("folder").equals("y")) {
				Folder newFolder = new Folder(jsonNewAccount.getInt("type"));
				parseAccountValues(jsonNewAccount, newFolder);
				parseAccounts(jsonNewAccount, newFolder);
				folder.addAccount(newFolder);
			} else {
				Account newAccount = new Account(jsonNewAccount.getInt("type"));
				parseAccountValues(jsonNewAccount, newAccount);
				newAccount.setName(jsonNewAccount.getString("name"));
				if (jsonNewAccount.optString("hidden").equals("y")) {
					newAccount.setHidden(true);
				}
				String hiddenDate = jsonNewAccount.optString("hiddenDate");
				if (hiddenDate != null) {
					newAccount.setHiddenDate(Formatter.parseDate(hiddenDate));
				}
				folder.addAccount(newAccount);
			}
		}
	}

	/**
	 * Parse account value.
	 * 
	 * @param jsonNewAccount
	 * @param acnt
	 */
	protected static void parseAccountValues(JSONObject jsonNewAccount, Account acnt) {
		JSONArray jsonPeriodBudgetList = jsonNewAccount.optJSONArray("periodBudget");
		JSONArray jsonPeriodValueList = jsonNewAccount.optJSONArray("periodValue");
		if (jsonPeriodBudgetList != null) {
			for (int j = 0; j < jsonPeriodBudgetList.length(); j++) {
				JSONObject jsonPeriodBudget = jsonPeriodBudgetList.getJSONObject(j);
				acnt.setBudget(jsonPeriodBudget.getString("periodId"), jsonPeriodBudget.getDouble("budget"));
			}
		}
		if (jsonPeriodValueList != null) {
			for (int j = 0; j < jsonPeriodValueList.length(); j++) {
				JSONObject jsonPeriodValue = jsonPeriodValueList.getJSONObject(j);
				acnt.setValue(jsonPeriodValue.getString("periodId"),
						parseValue(jsonPeriodValue.optJSONObject("value")));

			}
		}
	}

	/**
	 * Parse periods.
	 * 
	 * @param jsonList
	 * @param periodList
	 * @param book
	 */
	protected static void parsePeriods(JSONArray jsonList, List<Period> periodList, Book book) {
		for (int i = 0; i < jsonList.length(); i++) {
			JSONObject jsonPeriod = jsonList.getJSONObject(i);
			Period newPeriod = new Period(Formatter.parseDate(jsonPeriod.getString("date")));
			newPeriod.setId(jsonPeriod.getString("id"));
			newPeriod.setValue(parseValue(jsonPeriod.optJSONObject("value")));
			parseTransactions(jsonPeriod.optJSONArray("transactions"), newPeriod.getTransactions(), book);
			periodList.add(newPeriod);
		}
	}

	/**
	 * Parse value.
	 * 
	 * @param jsonFolder
	 * @return
	 */
	protected static Value parseValue(JSONObject jsonValue) {
		Value res = new Value();
		if (jsonValue != null) {
			double val = jsonValue.optDouble("startOfPeriod");
			if (Double.isNaN(val)) {
				val = 0.0;
			}
			res.setStartOfPeriod(val);
			val = jsonValue.optDouble("credit");
			if (Double.isNaN(val)) {
				val = 0.0;
			}
			res.setCredit(val);
			val = jsonValue.optDouble("debit");
			if (Double.isNaN(val)) {
				val = 0.0;
			}
			res.setDebit(val);
			val = jsonValue.optDouble("endOfPeriod");
			if (Double.isNaN(val)) {
				val = 0.0;
			}
			res.setEndOfPeriod(val);
			val = jsonValue.optDouble("control");
			if (Double.isNaN(val)) {
				val = 0.0;
			}
			res.setControl(val);
			if (jsonValue.optString("manual").equals("y")) {
				res.setManual(true);
			}
		}
		return res;
	}

	/**
	 * Transactions.
	 * 
	 * @param jsonList
	 * @param transactions
	 */
	protected static void parseTransactions(JSONArray jsonList, List<Transaction> transactions, Book book) {
		boolean transOk;
		if (jsonList != null) {
			for (int i = 0; i < jsonList.length(); i++) {
				transOk = true;
				JSONObject jsonTrans = jsonList.getJSONObject(i);
				Transaction newTrans = new Transaction();
				String from = jsonTrans.getString("from");
				newTrans.setFrom(book.getAccountByName(from));
				if (newTrans.getFrom() == null) {
					Logger.writeToLog("Account is not found: " + from);
					transOk = false;
				}
				String to = jsonTrans.getString("to");
				newTrans.setTo(book.getAccountByName(to));
				if (newTrans.getTo() == null) {
					Logger.writeToLog("Account is not found: " + to);
					transOk = false;
				}

				newTrans.setDescription(jsonTrans.getString("description"));
				newTrans.setValue(jsonTrans.getDouble("value"));
				if (transOk) {
					transactions.add(newTrans);
				}
			}
		}
	}
}
