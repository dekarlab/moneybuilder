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

/**
 * Value Handler.
 * 
 * @author dk
 *
 */
public class Value {
	/**
	 * Start of period.
	 */
	private double startOfPeriod = 0.0;
	/**
	 * Debit.
	 */
	private double debit = 0.0;
	/**
	 * Credit.
	 */
	private double credit = 0.0;
	/**
	 * End of period.
	 */
	private double endOfPeriod = 0.0;
	/**
	 * Control.
	 */
	private double control = 0.0;
	/**
	 * Manual.
	 */
	private boolean manual;

	/**
	 * Constructor.
	 */
	public Value() {

	}

	public double getStartOfPeriod() {
		return startOfPeriod;
	}

	public void setStartOfPeriod(double startOfPeriod) {
		this.startOfPeriod = startOfPeriod;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getEndOfPeriod() {
		return endOfPeriod;
	}

	public void setEndOfPeriod(double endOfPeriod) {
		this.endOfPeriod = endOfPeriod;
	}

	public double getControl() {
		return control;
	}

	public void setControl(double control) {
		this.control = control;
	}

	public void addDebit(double value) {
		this.debit += value;
	}

	public void addCredit(double value) {
		this.credit += value;
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public boolean isEmpty() {
		// if (debit == 0.0 && credit == 0.0 && startOfPeriod == 0.0 &&
		// endOfPeriod == 0.0) {
		// return true;
		// }
		if (isManual()) {
			return false;
		}
		return true;
	}

}
