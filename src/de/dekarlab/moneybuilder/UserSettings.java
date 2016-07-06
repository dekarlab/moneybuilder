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
package de.dekarlab.moneybuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User settings class.
 * 
 * @author dk
 *
 */
public class UserSettings {
	/**
	 * User settings.
	 */
	private static UserSettings props;
	/**
	 * Properties.
	 */
	private Properties internalProps;

	/**
	 * Singleton constructor.
	 */
	private UserSettings() {

	}

	/**
	 * Get props.
	 * 
	 * @return
	 */
	public static UserSettings getProps() {
		if (props == null) {
			props = new UserSettings();
			props.load();
		}
		return props;
	}

	/**
	 * Load props.
	 */
	public void load() {
		String path = getFilePath();
		File file = new File(path);
		internalProps = new Properties();
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			initDefault();
			save();
		} else {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				internalProps.load(fis);
			} catch (Exception e) {
				Logger.writeToLog(e);
				initDefault();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						// nothing to do
					}
				}
			}
		}
	}

	/**
	 * Get file path.
	 * 
	 * @return
	 */
	public static String getFilePath() {
		String path = System.getProperty("user.home") + "/" + App.getGuiProp("default.props.folder") + "/"
				+ App.getGuiProp("default.props.name");
		return path;
	}

	/**
	 * Save props.
	 */
	public void save() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(getFilePath());
			internalProps.store(fos, "Properties for MoneyBuilder");
			fos.close();
		} catch (Exception e) {
			Logger.writeToLog(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// nothing to do
				}
			}
		}
	}

	/**
	 * Init default.
	 */
	protected void initDefault() {
		internalProps.setProperty("topX", "250");
		internalProps.setProperty("topY", "50");
		internalProps.setProperty("height", "800");
		internalProps.setProperty("width", "800");
		internalProps.setProperty("lastFile",
				new File(App.getGuiProp("default.file.path") + "/" + App.getGuiProp("default.file.name"))
						.getAbsolutePath());
	}

	/**
	 * Get integer.
	 * 
	 * @param key
	 * @return
	 */
	public static int getInteger(String key) {
		return Integer.parseInt(getProps().internalProps.getProperty(key));
	}

	/**
	 * Set integer.
	 * 
	 * @param key
	 * @return
	 */
	public static void setInteger(String key, int value) {
		getProps().internalProps.setProperty(key, Integer.toString(value));
	}

	/**
	 * Get string.
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getProps().internalProps.getProperty(key);
	}

	/**
	 * Set string.
	 * 
	 * @param key
	 * @return
	 */
	public static void setString(String key, String value) {
		getProps().internalProps.setProperty(key, value);
	}

}
