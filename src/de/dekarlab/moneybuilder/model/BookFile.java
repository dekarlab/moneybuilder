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

import java.io.File;

import de.dekarlab.moneybuilder.UserSettings;
import de.dekarlab.moneybuilder.calc.Calculator;
import de.dekarlab.moneybuilder.model.parser.JsonBookLoader;
import de.dekarlab.moneybuilder.model.parser.JsonBookSaver;

/**
 * File represents the file for book.
 * 
 * @author dk
 *
 */
public class BookFile {
	private Book book;
	private String filePath;

	/**
	 * Constructor.
	 */
	public BookFile(String filePath) {
		this.filePath = filePath;

	}

	/**
	 * Get book.
	 * 
	 * @return
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Set book.
	 * 
	 * @param book
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 * Get file path.
	 * 
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Set file path.
	 * 
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		UserSettings.setString("lastFile", filePath);
		this.filePath = filePath;
	}

	/**
	 * Load book file.
	 */
	public void load() throws Exception {
		File file = new File(getFilePath());
		book = JsonBookLoader.loadBook(file);
		// clear all saved values
		Calculator.clearAll(book);
		for (int i = 0; i < book.getPeriodList().size(); i++) {
			Calculator.recalc(book, book.getPeriodList().get(i), true);
		}
		// make backup
		backup();
	}

	/**
	 * Backup.
	 * 
	 * @throws Exception
	 */
	public void backup() throws Exception {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_");
		new File(new File(getFilePath()).getParent() + "/backup/").mkdirs();
		BookFile bf = new BookFile(
				new File(getFilePath()).getParent() + "/backup/" + new File(getFilePath()).getName());
		bf.setBook(book);
		JsonBookSaver.saveBook(bf);
	}
}
