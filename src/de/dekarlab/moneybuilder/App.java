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

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.dekarlab.moneybuilder.model.BookFile;
import de.dekarlab.moneybuilder.view.AppView;

/**
 * Main class for application.
 * 
 * @author dk
 * 
 */
public class App {
	/**
	 * Display.
	 */
	private Display display;

	/**
	 * Application.
	 */
	private static App app;
	/**
	 * Resource bundle.
	 */
	private static ResourceBundle guiProps = ResourceBundle.getBundle("de.dekarlab.moneybuilder.gui");
	/**
	 * GUI
	 */
	private AppView appView;
	/**
	 * File name.
	 */
	private BookFile bookFile;

	/**
	 * Private constructor.
	 */
	private App() {

	}

	/**
	 * Get application.
	 * 
	 * @return
	 */
	public static App getApp() {
		if (app == null) {
			app = new App();
		}
		return app;
	}

	/**
	 * Main class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			App app = App.getApp();
			app.startGui();
			// save properties
			saveCurrentSettings();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Save current settings.
	 */
	public static void saveCurrentSettings() {
		UserSettings.getProps().save();
	}

	/**
	 * Start GUI.
	 */
	public void startGui() throws Exception {
		// initialize and load book
		bookFile = new BookFile(UserSettings.getString("lastFile"));
		bookFile.load();
		display = new Display();
		reload();

	}

	/**
	 * Get GUI properties.
	 * 
	 * @return
	 */
	public static String getGuiProp(String key) {
		try {
			return guiProps.getString(key);
		} catch (Exception ex) {
			return key;
		}
	}

	/**
	 * Application view.
	 * 
	 * @return
	 */
	public AppView getAppView() {
		return appView;
	}

	/**
	 * Get display.
	 * 
	 * @return
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Get book file.
	 * 
	 * @return
	 */
	public BookFile getBookFile() {
		return bookFile;
	}

	/**
	 * Set book file.
	 * 
	 * @param bookFile
	 */
	public void setBookFile(BookFile bookFile) {
		this.bookFile = bookFile;
	}

	/**
	 * Reload.
	 * 
	 * @throws Exception
	 */
	public void reload() throws Exception {
		if (display.getActiveShell() != null) {
			display.getActiveShell().close();
		}
		Shell shell = new Shell(display);
		shell.setText(getAppHeadetText(bookFile));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		shell.setLayout(layout);
		appView = new AppView(shell, SWT.NONE);
		appView.initView();
		// shell.setSize(new Point(UserSettings.getInteger("height"),
		// UserSettings.getInteger("width")));
		shell.setBounds(UserSettings.getInteger("topX"), UserSettings.getInteger("topY"),
				UserSettings.getInteger("width"), UserSettings.getInteger("height"));
		// shell.pack();
		shell.open();
		shell.addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent arg0) {
				Rectangle rect = ((Shell) arg0.widget).getBounds();
				UserSettings.setInteger("topX", rect.x);
				UserSettings.setInteger("topY", rect.y);
				UserSettings.setInteger("width", rect.width);
				UserSettings.setInteger("height", rect.height);
			}

			@Override
			public void controlMoved(ControlEvent arg0) {
				Rectangle rect = ((Shell) arg0.widget).getBounds();
				UserSettings.setInteger("topX", rect.x);
				UserSettings.setInteger("topY", rect.y);
				UserSettings.setInteger("width", rect.width);
				UserSettings.setInteger("height", rect.height);
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public static String getAppHeadetText(BookFile bookFile) {
		return guiProps.getString("app.name") + " - " + bookFile.getFilePath() + " - "
				+ guiProps.getString("app.version");
	}
}
