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

package de.dekarlab.moneybuilder.view;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.dekarlab.moneybuilder.App;
import de.dekarlab.moneybuilder.ImageUtil;
import de.dekarlab.moneybuilder.Logger;
import de.dekarlab.moneybuilder.calc.Calculator;
import de.dekarlab.moneybuilder.model.Book;
import de.dekarlab.moneybuilder.model.BookFile;
import de.dekarlab.moneybuilder.model.Period;
import de.dekarlab.moneybuilder.model.parser.JsonBookSaver;
import de.dekarlab.moneybuilder.model.util.Formatter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Main view for appliction.
 * 
 * @author dk
 *
 */
public class AppView extends Composite {
	/**
	 * Label period.
	 */
	private Label lblPeriod;
	/**
	 * Next period button.
	 */
	private Button btNextPeriod;
	/**
	 * Previous period.
	 */
	private Button btPrevPeriod;

	/**
	 * Open book.
	 */
	private Button btOpen;
	/**
	 * Save book.
	 */
	private Button btSave;
	/**
	 * Save as book.
	 */
	private Button btSaveAs;

	/**
	 * Tab folder.
	 */
	private TabFolder tbfFolder;
	/**
	 * Balance view.
	 */
	private BookView bookView;
	/**
	 * Analytics view.
	 */
	private AnalyticsView analyticsView;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param style
	 */
	public AppView(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Initialize view.
	 * 
	 * @throws Exception
	 */
	public void initView() throws Exception {
		setLayoutData(new GridData(GridData.FILL_BOTH));
		// init layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		setLayout(layout);

		btOpen = new Button(this, SWT.PUSH);
		btOpen.setImage(ImageUtil.IMG_OPEN);
		btOpen.setText(App.getGuiProp("book.bt.open"));
		final Shell shell = getShell();
		btOpen.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterNames(new String[] { "JSON Files", "All Files (*.*)" });
				dialog.setFilterExtensions(new String[] { "*.json", "*.*" });
				BookFile bookFile = App.getApp().getBookFile();
				dialog.setFilterPath(bookFile.getFilePath());
				// dialog.setFileName(bookFile.getFileName());
				String newFileName = dialog.open();
				if (newFileName != null) {
					File file = new File(newFileName);
					// bookFile.setFileName(file.getName());
					bookFile.setFilePath(file.getAbsolutePath());
					try {
						bookFile.load();
						App.getApp().reload();
					} catch (Exception ex) {
						Logger.writeToLog(ex);
					}
				}
			}
		});

		//
		btSave = new Button(this, SWT.PUSH);
		btSave.setText(App.getGuiProp("book.bt.save"));
		btSave.setImage(ImageUtil.IMG_SAVE);
		btSave.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					JsonBookSaver.saveBook(App.getApp().getBookFile());
				} catch (Exception ex) {
					Logger.writeToLog(ex);
				}
			}
		});
		//
		btSaveAs = new Button(this, SWT.PUSH);
		btSaveAs.setText(App.getGuiProp("book.bt.save.as"));
		// btSaveAs.setImage(ImageUtil.IMG_SAVE);
		btSaveAs.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				dialog.setFilterNames(new String[] { "JSON Files", "All Files (*.*)" });
				dialog.setFilterExtensions(new String[] { "*.json", "*.*" });
				BookFile bookFile = App.getApp().getBookFile();
				dialog.setFilterPath(bookFile.getFilePath());
				// dialog.setFileName(bookFile.getFileName());
				String newFileName = dialog.open();
				if (newFileName != null) {
					File file = new File(newFileName);
					// bookFile.setFileName(file.getName());
					bookFile.setFilePath(file.getAbsolutePath());
					try {
						JsonBookSaver.saveBook(bookFile);
						shell.setText(App.getAppHeadetText(bookFile));
					} catch (Exception ex) {
						Logger.writeToLog(ex);
					}
				}
			}
		});
		//
		Label lblBookName = new Label(this, SWT.LEFT);
		lblBookName.setText(App.getApp().getBookFile().getBook().getName());
		// add period panel
		addPeriodPanel();
		//
		// Tabbed folder.
		tbfFolder = new TabFolder(this, SWT.NONE);
		GridData grdTabFolder = new GridData(GridData.FILL_BOTH);
		grdTabFolder.horizontalSpan = 4;
		tbfFolder.setLayoutData(grdTabFolder);
		// Balance
		TabItem balanceTab = new TabItem(tbfFolder, SWT.NONE);
		balanceTab.setText(App.getGuiProp("app.tab.balance"));
		bookView = new BookView(tbfFolder, SWT.NONE);
		bookView.initView();
		balanceTab.setControl(bookView);
		// Analytics
		TabItem analyticsTab = new TabItem(tbfFolder, SWT.NONE);
		analyticsTab.setText(App.getGuiProp("app.tab.analytics"));
		analyticsView = new AnalyticsView(tbfFolder, SWT.NONE);
		analyticsView.initView();
		analyticsTab.setControl(analyticsView);

	}

	/**
	 * Button next period.
	 * 
	 * @return
	 */
	public Button getBtNextPeriod() {
		return btNextPeriod;
	}

	/**
	 * Button prev. period.
	 * 
	 * @return
	 */
	public Button getBtPrevPeriod() {
		return btPrevPeriod;
	}

	/**
	 * Next period.
	 */
	protected void nextPeriod() {
		Book book = App.getApp().getBookFile().getBook();
		List<Period> periodList = book.getPeriodList();
		int i = periodList.indexOf(book.getCurrPeriod());
		if (i + 1 >= periodList.size()) {
			Date datePrev = book.getCurrPeriod().getDate();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(datePrev);
			cal.add(Calendar.MONTH, 1);
			// create period
			Period newPeriod = new Period(cal.getTime());
			newPeriod.setId(Formatter.formatDateId(cal.getTime()));
			periodList.add(newPeriod);
		}
		// recalculate next period
		Calculator.recalc(book, periodList.get(i + 1), false);
		book.setCurrPeriod(periodList.get(i + 1));
		bookView.loadGuiFromModel();
		analyticsView.loadGuiFromModel();
		lblPeriod.setText(Formatter.formatDateMonth(book.getCurrPeriod().getDate()));
	}

	/**
	 * Prev. period.
	 */
	protected void prevPeriod() {
		Book book = App.getApp().getBookFile().getBook();
		List<Period> periodList = book.getPeriodList();
		int i = periodList.indexOf(book.getCurrPeriod());
		if (i == 0) {
			Date datePrev = book.getCurrPeriod().getDate();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(datePrev);
			cal.add(Calendar.MONTH, -1);
			// create period
			Period newPeriod = new Period(cal.getTime());
			newPeriod.setId(Formatter.formatDateId(cal.getTime()));
			periodList.add(0, newPeriod);
			i = 1;
		}
		book.setCurrPeriod(periodList.get(i - 1));
		bookView.loadGuiFromModel();
		analyticsView.loadGuiFromModel();
		lblPeriod.setText(Formatter.formatDateMonth(book.getCurrPeriod().getDate()));
	}

	/**
	 * Add period buttons panel.
	 */
	protected void addPeriodPanel() {
		Composite composite = new Composite(this, SWT.NONE);
		GridData grdData = new GridData(GridData.FILL_HORIZONTAL);
		grdData.horizontalSpan = 4;
		composite.setLayoutData(grdData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		composite.setLayout(layout);
		composite.setBackground(new Color(getDisplay(), 255, 255, 255));

		// GridData grdBookName = new GridData(GridData.FILL_HORIZONTAL);
		// lblBookName.setLayoutData(grdBookName);
		// Get model
		// dao = App.getApp().getDao();
		// Get current balance period.
		// balancePeriod = dao.getCurrBalancePeriod();
		// Periods
		Label lblCurrDate = new Label(composite, SWT.NONE);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(lblCurrDate.getFont()).setStyle(SWT.BOLD);
		Font boldFont = boldDescriptor.createFont(lblCurrDate.getDisplay());
		lblCurrDate.setFont(boldFont);
		lblCurrDate.setText(Formatter.formatDate(new Date()));
		lblCurrDate.setBackground(new Color(getDisplay(), 255, 255, 255));
		// Peirods
		btPrevPeriod = new Button(composite, SWT.PUSH);
		btPrevPeriod.setText("<<");
		lblPeriod = new Label(composite, SWT.NONE);
		FontDescriptor boldDescriptor2 = FontDescriptor.createFrom(lblCurrDate.getFont()).setStyle(SWT.BOLD)
				.setHeight(14);
		Font boldFont2 = boldDescriptor2.createFont(lblPeriod.getDisplay());
		lblPeriod.setFont(boldFont2);
		lblPeriod.setAlignment(SWT.CENTER);
		lblPeriod.setText(Formatter.formatDateMonth(App.getApp().getBookFile().getBook().getCurrPeriod().getDate()));
		lblPeriod.setBackground(new Color(getDisplay(), 255, 255, 255));
		GridData lbPeriodGD = new GridData(GridData.FILL_HORIZONTAL);
		lblPeriod.setLayoutData(lbPeriodGD);
		btPrevPeriod.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				prevPeriod();
			}
		});

		btNextPeriod = new Button(composite, SWT.PUSH);
		btNextPeriod.setText(">>");
		btNextPeriod.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				nextPeriod();
			}
		});

	}

}
