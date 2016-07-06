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

import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONWriter;

import de.dekarlab.moneybuilder.model.Account;
import de.dekarlab.moneybuilder.model.Book;
import de.dekarlab.moneybuilder.model.BookFile;
import de.dekarlab.moneybuilder.model.Folder;
import de.dekarlab.moneybuilder.model.Period;
import de.dekarlab.moneybuilder.model.Transaction;
import de.dekarlab.moneybuilder.model.Value;
import de.dekarlab.moneybuilder.model.util.Formatter;

/**
 * Class.
 * 
 * @author dk
 *
 */
public class JsonBookSaver {
	/**
	 * Save book to file.
	 * 
	 * @param file
	 * @param book
	 * @throws Exception
	 */
	public static void saveBook(BookFile bookFile) throws Exception {
		FileWriter writer = null;
		JSONWriter jsonWriter = null;
		try {
			Book book = bookFile.getBook();
			writer = new FileWriter(bookFile.getFilePath());
			jsonWriter = new JSONWriter(writer);
			jsonWriter.object().key("book");
			jsonWriter.object();
			jsonWriter.key("name").value(book.getName());
			jsonWriter.key("currPeriodId").value(book.getCurrPeriodId());
			savePeriodBudget(book.getPeriodBudget(), jsonWriter);
			savePeriodValue(book.getPeriodValue(), jsonWriter);
			jsonWriter.key("assets");
			saveAccount(book.getAssetList(), jsonWriter);
			jsonWriter.key("liability");
			saveAccount(book.getLiabilityList(), jsonWriter);
			jsonWriter.key("income");
			saveAccount(book.getIncomeList(), jsonWriter);
			jsonWriter.key("expenses");
			saveAccount(book.getExpenseList(), jsonWriter);
			jsonWriter.key("periodList");
			savePeriodList(book.getPeriodList(), jsonWriter);
			jsonWriter.endObject();
			jsonWriter.endObject();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * Save folder.
	 * 
	 * @param account
	 * @param jsonWriter
	 */
	protected static void saveAccount(Account account, JSONWriter jsonWriter) {
		jsonWriter.object();
		jsonWriter.key("name").value(account.getName());
		jsonWriter.key("type").value(account.getType());
		if (account instanceof Folder) {
			jsonWriter.key("folder").value("y");
		}
		if (account.isHidden()) {
			jsonWriter.key("hidden").value("y");
			if (account.getHiddenDate() != null) {
				jsonWriter.key("hiddenDate").value(Formatter.formatDate(account.getHiddenDate()));
			}
		}
		savePeriodBudget(account.getPeriodBudget(), jsonWriter);
		savePeriodValue(account.getPeriodValue(), jsonWriter);
		if (account instanceof Folder) {
			jsonWriter.key("list");
			jsonWriter.array();
			for (Account child : ((Folder) account).getList()) {
				saveAccount(child, jsonWriter);
			}
			jsonWriter.endArray();
		}
		jsonWriter.endObject();
	}

	/**
	 * Save period list.
	 * 
	 * @param periodList
	 * @param jsonWriter
	 */
	protected static void savePeriodList(List<Period> periodList, JSONWriter jsonWriter) {
		jsonWriter.array();
		for (Period period : periodList) {
			jsonWriter.object();
			jsonWriter.key("id").value(period.getId());
			jsonWriter.key("date").value(Formatter.formatDate(period.getDate()));
			saveValue(period.getValue(), jsonWriter);
			saveTransactions(period.getTransactions(), jsonWriter);
			jsonWriter.endObject();
		}
		jsonWriter.endArray();
	}

	/**
	 * Save transactions.
	 * 
	 * @param trans
	 * @param jsonWriter
	 */
	protected static void saveTransactions(List<Transaction> trans, JSONWriter jsonWriter) {
		if (trans.size() > 0) {
			jsonWriter.key("transactions");
			jsonWriter.array();
			for (Transaction tr : trans) {
				jsonWriter.object();
				jsonWriter.key("from").value(tr.getFrom().getAbsName());
				jsonWriter.key("to").value(tr.getTo().getAbsName());
				jsonWriter.key("description").value(tr.getDescription());
				jsonWriter.key("value").value(tr.getValue());
				jsonWriter.endObject();
			}
			jsonWriter.endArray();
		}
	}

	/**
	 * Save period budget.
	 * 
	 * @param periodBudget
	 * @param jsonWriter
	 */
	protected static void savePeriodBudget(Map<String, Double> periodBudget, JSONWriter jsonWriter) {
		if (!isEmptyBudget(periodBudget)) {
			jsonWriter.key("periodBudget");
			jsonWriter.array();
			Iterator<String> it = periodBudget.keySet().iterator();
			while (it.hasNext()) {
				String id = it.next();
				Double value = periodBudget.get(id);
				if (value.doubleValue() != 0.0) {
					jsonWriter.object();
					jsonWriter.key("periodId").value(id);
					jsonWriter.key("budget").value(value);
					jsonWriter.endObject();
				}
			}
			jsonWriter.endArray();
		}
	}

	/**
	 * Is period value empty.
	 * 
	 * @return
	 */
	protected static boolean isEmptyBudget(Map<String, Double> periodBudget) {
		Iterator<String> it = periodBudget.keySet().iterator();
		while (it.hasNext()) {
			String id = it.next();
			Double value = periodBudget.get(id);
			if (value.doubleValue() != 0.0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Save period value.
	 * 
	 * @param periodBudget
	 * @param jsonWriter
	 */
	protected static void savePeriodValue(Map<String, Value> periodValue, JSONWriter jsonWriter) {
		if (!isEmpty(periodValue)) {
			jsonWriter.key("periodValue");
			jsonWriter.array();
			Iterator<String> it = periodValue.keySet().iterator();
			while (it.hasNext()) {
				String id = it.next();
				Value value = periodValue.get(id);
				if (!value.isEmpty()) {
					jsonWriter.object();
					jsonWriter.key("periodId").value(id);
					saveValue(value, jsonWriter);
					jsonWriter.endObject();
				}
			}
			jsonWriter.endArray();
		}
	}

	/**
	 * Is period value empty.
	 * 
	 * @return
	 */
	protected static boolean isEmpty(Map<String, Value> periodValue) {
		Iterator<String> it = periodValue.keySet().iterator();
		while (it.hasNext()) {
			String id = it.next();
			Value value = periodValue.get(id);
			if (!value.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Value object.
	 * 
	 * @param value
	 * @param jsonWriter
	 */
	protected static void saveValue(Value value, JSONWriter jsonWriter) {
		if (value != null && !value.isEmpty()) {
			jsonWriter.key("value");
			jsonWriter.object();
			if (value.getStartOfPeriod() != 0.0) {
				jsonWriter.key("startOfPeriod").value(value.getStartOfPeriod());
			}
			if (value.getDebit() != 0.0) {
				jsonWriter.key("debit").value(value.getDebit());
			}
			if (value.getCredit() != 0.0) {
				jsonWriter.key("credit").value(value.getCredit());
			}
			if (value.getEndOfPeriod() != 0.0) {
				jsonWriter.key("endOfPeriod").value(value.getEndOfPeriod());
			}
			if (value.getControl() != 0.0) {
				jsonWriter.key("control").value(value.getControl());
			}
			if (value.isManual()) {
				jsonWriter.key("manual").value("y");
			}
			jsonWriter.endObject();
		}
	}
}
