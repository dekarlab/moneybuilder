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

package de.dekarlab.moneybuilder.model.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.dekarlab.moneybuilder.Logger;

/**
 * Formatter.
 * 
 * @author dk
 * 
 */
public class Formatter {
	/**
	 * Format date.
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		return sd.format(date);
	}

	/**
	 * Format date.
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateMonth(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sd = new SimpleDateFormat("MMMM, yyyy");
		return sd.format(date);
	}

	/**
	 * Format date.
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateId(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");
		return sd.format(date);
	}

	/**
	 * Parse date.
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) {
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return sd.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Format value.
	 * 
	 * @param value
	 * @return
	 */
	public static String formatValue(double value) {
		if (Math.round(value*100) == 0.0) {
			return "";
		}
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(true);
		return nf.format(value);
	}

	/**
	 * Format value.
	 * 
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public static Double parseValue(String value) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(true);
		if (!value.equals("")) {
			try {
				return nf.parse(value).doubleValue();
			} catch (ParseException e) {
				Logger.writeToLog(e);
			}
		}
		return 0.0;
	}

	/**
	 * Get period name.
	 * 
	 * @param date
	 * @return
	 */
	public static String getPeriodName(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String monthYear = ".";
		monthYear += String.format("%02d", cal.get(Calendar.MONTH) + 1);
		monthYear += "." + String.format("%04d", cal.get(Calendar.YEAR));
		monthYear = "01" + monthYear + " - " + cal.getActualMaximum(Calendar.DAY_OF_MONTH) + monthYear;
		return monthYear;
	}

}
