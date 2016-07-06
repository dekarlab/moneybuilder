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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.nebula.widgets.grid.GridItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import de.dekarlab.moneybuilder.App;
import de.dekarlab.moneybuilder.ImageUtil;
import de.dekarlab.moneybuilder.calc.Calculator;
import de.dekarlab.moneybuilder.fix.DekarlabGrid;
import de.dekarlab.moneybuilder.model.Account;
import de.dekarlab.moneybuilder.model.Book;
import de.dekarlab.moneybuilder.model.Folder;
import de.dekarlab.moneybuilder.model.Period;
import de.dekarlab.moneybuilder.model.Transaction;
import de.dekarlab.moneybuilder.model.Value;
import de.dekarlab.moneybuilder.model.util.Formatter;

/**
 * The main view in application.
 * 
 * @author dk
 * 
 */
public class BookView extends Composite {
	/**
	 * Grid for Snapshot.
	 * 
	 */
	private Grid gridSnapshot;
	/**
	 * Lable transactions.
	 */
	private Grid gridTrans;
	/**
	 * Selected account from.
	 */
	private Combo cbAccountFrom;
	/**
	 * Selected account to.
	 */
	private Combo cbAccountTo;
	/**
	 * Sash form.
	 */
	private SashForm sashForm;
	/**
	 * Constants for snapshot.
	 */
	protected static final int SNPSH_COL_ACNT = 0;
	protected static final int SNPSH_COL_SOP = 1;
	protected static final int SNPSH_COL_DEBIT = 2;
	protected static final int SNPSH_COL_CREDIT = 3;
	protected static final int SNPSH_COL_EOP = 4;
	protected static final int SNPSH_COL_CONTROL = 5;
	protected static final int SNPSH_COL_BUDGET = 6;
	/**
	 * Constant for transactions.
	 */
	protected static final int TRANS_COL_FROM = 0;
	protected static final int TRANS_COL_TO = 1;
	protected static final int TRANS_COL_VALUE = 2;
	protected static final int TRANS_COL_DESCR = 3;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param style
	 */
	public BookView(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Create GUI method.
	 */
	protected void initView() throws Exception {
		// init layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		setLayout(layout);
		sashForm = new SashForm(this, SWT.VERTICAL);
		sashForm.setSashWidth(5);
		sashForm.setBackground(sashForm.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		sashForm.setLayout(new GridLayout());
		GridData panelGD = new GridData(GridData.FILL_BOTH);
		panelGD.horizontalSpan = 4;
		sashForm.setLayoutData(panelGD);
		// Initialize snapshot
		initGridSanpshot();
		// Grid transactions
		initGridTrans();
		// load GUI
		loadGuiFromModel();
		// Link to site
		Link lblBts = new Link(this, SWT.NONE);
		GridData panelGD2 = new GridData(GridData.FILL_HORIZONTAL);
		panelGD2.horizontalSpan = 4;
		panelGD2.horizontalAlignment = SWT.RIGHT;
		lblBts.setLayoutData(panelGD2);
		FontDescriptor advFondDescr = FontDescriptor.createFrom(lblBts.getFont()).setStyle(SWT.ITALIC).setHeight(8);
		Font advFont = advFondDescr.createFont(lblBts.getDisplay());
		lblBts.setFont(advFont);
		String url = "http://BehindTheStrategy.com";
		lblBts.setText("<a>" + url + "</a> - Find Your Investment Strategy");
		lblBts.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Program.launch(url);
			}
		});

	}

	/**
	 * Initialize grid snapshot.
	 */
	protected void initGridSanpshot() {
		// work around to make arrow up/down working
		Composite panel = new Composite(sashForm, SWT.NONE);
		GridData panelGD = new GridData(GridData.FILL_BOTH);
		// panelGD.horizontalSpan = 4;
		// panelGD.verticalSpan = 1;
		// panelGD.widthHint = 400;
		panel.setLayoutData(panelGD);

		panel.setLayout(new GridLayout());
		GridData gridGD = new GridData(GridData.FILL_BOTH);

		Label lblSnapshot = new Label(panel, SWT.NONE);
		lblSnapshot.setText(App.getGuiProp("book.tree.name"));

		gridSnapshot = new DekarlabGrid(panel, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gridSnapshot.setLayoutData(gridGD);
		gridSnapshot.setHeaderVisible(true);
		gridSnapshot.setCellSelectionEnabled(true);
		gridSnapshot.setFooterVisible(true);

		GridColumn tcAccounts = new GridColumn(gridSnapshot, SWT.LEFT);
		tcAccounts.setTree(true);
		tcAccounts.setText(App.getGuiProp("book.tree.header.accnt"));
		tcAccounts.setMinimumWidth(200);
		tcAccounts.setWidth(200);

		GridColumn tcPrevPeriod = new GridColumn(gridSnapshot, SWT.RIGHT);
		tcPrevPeriod.setText(App.getGuiProp("book.tree.header.prev.period"));
		tcPrevPeriod.setMinimumWidth(100);
		tcPrevPeriod.setWidth(100);

		GridColumn tcDebit = new GridColumn(gridSnapshot, SWT.RIGHT);
		tcDebit.setText(App.getGuiProp("book.tree.header.debit"));
		tcDebit.setMinimumWidth(100);
		tcDebit.setWidth(100);

		GridColumn tcCredit = new GridColumn(gridSnapshot, SWT.RIGHT);
		tcCredit.setText(App.getGuiProp("book.tree.header.crebit"));
		tcCredit.setMinimumWidth(100);
		tcCredit.setWidth(100);

		GridColumn tcEop = new GridColumn(gridSnapshot, SWT.RIGHT);
		tcEop.setText(App.getGuiProp("book.tree.header.end.of.period"));
		tcEop.setMinimumWidth(100);
		tcEop.setWidth(100);

		GridColumn tcControl = new GridColumn(gridSnapshot, SWT.RIGHT);
		tcControl.setText(App.getGuiProp("book.tree.header.control"));
		tcControl.setMinimumWidth(100);
		tcControl.setWidth(100);

		GridColumn tcBudget = new GridColumn(gridSnapshot, SWT.RIGHT);
		tcBudget.setText(App.getGuiProp("book.tree.header.budget"));
		tcBudget.setMinimumWidth(100);
		tcBudget.setWidth(100);

		// Auto resize tree.
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				final GridItem treeItem = (GridItem) e.item;
				App.getApp().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						((Account) treeItem.getData()).setViewExpanded(treeItem.isExpanded());
						for (GridColumn tc : treeItem.getParent().getColumns()) {
							tc.pack();
						}
						updateBackgroundColor(gridSnapshot);
					}
				});
			}
		};
		gridSnapshot.addListener(SWT.Collapse, listener);
		gridSnapshot.addListener(SWT.Expand, listener);
		Listener editListener = getEditSnapshotListener();

		gridSnapshot.addListener(SWT.MouseDoubleClick, editListener);
		gridSnapshot.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent event) {
				// enter
				if (event.detail == SWT.TRAVERSE_RETURN) {
					editListener.handleEvent(null);
				}
			}
		});
		gridSnapshot.addKeyListener(getDeleteInsertSnapshotListener());
		// popup menu
		Menu popupMenu = new Menu(gridSnapshot);
		gridSnapshot.setMenu(popupMenu);
		gridSnapshot.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent arg0) {
				MenuItem[] items = popupMenu.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}

				MenuItem newFolderMenu = new MenuItem(popupMenu, SWT.NONE);
				newFolderMenu.setImage(ImageUtil.IMG_FOLDER_ADD);
				newFolderMenu.setText(App.getGuiProp("book.tree.new.folder"));
				newFolderMenu.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event arg0) {
						Account currAcnt = (Account) gridSnapshot.getSelection()[0].getData();
						Account newAcnt = new Folder(currAcnt.getType());
						newAcnt.setName("New Folder");
						if (currAcnt instanceof Folder) {
							((Folder) currAcnt).addAccount(newAcnt);
						} else {
							currAcnt.getParent().addAccount(newAcnt);
						}
						setSnapshotValues(gridSnapshot);
					}
				});
				MenuItem newAcntMenu = new MenuItem(popupMenu, SWT.NONE);
				newAcntMenu.setText(App.getGuiProp("book.tree.new.acnt"));
				newAcntMenu.setImage(ImageUtil.IMG_ACCOUNT);
				newAcntMenu.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event arg0) {
						Account currAcnt = (Account) gridSnapshot.getSelection()[0].getData();
						Account newAcnt = new Account(currAcnt.getType());
						newAcnt.setName("New Account");
						if (currAcnt instanceof Folder) {
							((Folder) currAcnt).addAccount(newAcnt);
						} else {
							currAcnt.getParent().addAccount(newAcnt);
						}
						setSnapshotValues(gridSnapshot);
					}
				});
			}
		});
	}

	/**
	 * Grid transactions.
	 */
	protected void initGridTrans() {
		Composite panel = new Composite(sashForm, SWT.NONE);
		GridData panelGD = new GridData(GridData.FILL_BOTH);
		// panelGD.horizontalSpan = 4;
		// panelGD.verticalSpan = 1;
		// panelGD.widthHint = 400;
		panel.setLayoutData(panelGD);

		panel.setLayout(new GridLayout());
		GridData gridGD = new GridData(GridData.FILL_BOTH);

		Label lblTrans = new Label(panel, SWT.NONE);
		lblTrans.setText(App.getGuiProp("book.trans.name"));

		gridTrans = new DekarlabGrid(panel, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gridTrans.setLayoutData(gridGD);
		gridTrans.setHeaderVisible(true);
		gridTrans.setFooterVisible(true);
		gridTrans.setCellSelectionEnabled(true);

		Listener editListener = getEditTransactionListener();

		gridTrans.addListener(SWT.MouseDoubleClick, editListener);
		gridTrans.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent event) {
				// enter
				if (event.detail == SWT.TRAVERSE_RETURN) {
					editListener.handleEvent(null);
				}
			}
		});
		// Add Delete/Insert Listener
		gridTrans.addKeyListener(getDeleteInsertListener());

		GridColumn tcAccountFrom = new GridColumn(gridTrans, SWT.LEFT);
		tcAccountFrom.setText(App.getGuiProp("book.trans.header.account.from"));
		tcAccountFrom.setMinimumWidth(300);
		tcAccountFrom.setWidth(300);
		cbAccountFrom = new Combo(gridTrans, SWT.READ_ONLY | SWT.BORDER);
		tcAccountFrom.setHeaderControl(cbAccountFrom);
		tcAccountFrom.addSelectionListener(getSortListener(tcAccountFrom));

		GridColumn tcAccountTo = new GridColumn(gridTrans, SWT.LEFT);
		tcAccountTo.setText(App.getGuiProp("book.trans.header.account.to"));
		tcAccountTo.setMinimumWidth(300);
		tcAccountTo.setWidth(300);
		cbAccountTo = new Combo(gridTrans, SWT.READ_ONLY | SWT.BORDER);
		tcAccountTo.setHeaderControl(cbAccountTo);
		tcAccountTo.addSelectionListener(getSortListener(tcAccountTo));

		Listener cmbFilterListener = new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				setTransactionValues(gridTrans);
			}
		};
		cbAccountFrom.addListener(SWT.Selection, cmbFilterListener);
		cbAccountTo.addListener(SWT.Selection, cmbFilterListener);

		GridColumn tcValue = new GridColumn(gridTrans, SWT.RIGHT);
		tcValue.setText(App.getGuiProp("book.trans.header.value"));
		tcValue.setMinimumWidth(100);
		tcValue.setWidth(100);
		tcValue.addSelectionListener(getSortListener(tcValue));

		GridColumn tcDescr = new GridColumn(gridTrans, SWT.LEFT);
		tcDescr.setText(App.getGuiProp("book.trans.header.descr"));
		tcDescr.setMinimumWidth(200);
		tcDescr.setWidth(200);
		tcDescr.addSelectionListener(getSortListener(tcDescr));

		Label lblTransHelp = new Label(panel, SWT.NONE);
		lblTransHelp.setText(App.getGuiProp("book.trans.help"));

	}

	/**
	 * Get delete insert listener.
	 * 
	 * @return
	 */
	protected KeyAdapter getDeleteInsertListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Book book = App.getApp().getBookFile().getBook();
				// System.out.println("Mask " + e.stateMask + " Code " +
				// e.keyCode);
				if (e.stateMask == SWT.CTRL && e.keyCode == SWT.ARROW_DOWN) {
					// if (gridTrans.getItemCount() ==
					// (gridTrans.getSelectionIndex() + 1)) {
					Account to = null;
					Account from = null;
					if (cbAccountFrom.getText().length() > 0 && cbAccountTo.getText().length() > 0) {
						to = book.getAccountByName(cbAccountTo.getText());
						from = book.getAccountByName(cbAccountFrom.getText());
					} else {
						if (gridTrans.getItemCount() > 0) {
							Transaction tr = (Transaction) gridTrans.getItem(gridTrans.getItemCount() - 1).getData();
							to = tr.getTo();
							from = tr.getFrom();
						} else {
							List<Account> lstAcnt = book.getAccountList();
							to = lstAcnt.get(0);
							from = lstAcnt.get(0);
						}
					}
					if (to != null && from != null) {
						newTransaction(book, to, from);
					}
				} else if (e.keyCode == SWT.DEL && gridTrans.getSelectionIndex() != -1) {
					List<Transaction> trans = book.getCurrPeriod().getTransactions();
					Transaction tran = (Transaction) gridTrans.getSelection()[0].getData();
					int sel = trans.indexOf(tran);
					if (sel != -1) {
						trans.remove(sel);
						setTransactionValues(gridTrans);
						if (gridTrans.getItemCount() > 0) {
							gridTrans.forceFocus();
							gridTrans.select(gridTrans.getItemCount() - 1);
						}
						Calculator.recalc(book, book.getCurrPeriod(), tran, true, null, null);
						setSnapshotValues(gridSnapshot);
					}
				}
			}

		};
	}

	/**
	 * Get delete insert listener.
	 * 
	 * @return
	 */
	protected KeyAdapter getDeleteInsertSnapshotListener() {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Book book = App.getApp().getBookFile().getBook();
				if (e.stateMask == SWT.CTRL && e.keyCode == SWT.ARROW_DOWN) {
					if (gridSnapshot.isFocusControl()) {
						if (gridSnapshot.getSelectionCount() == 1) {
							Account currAcnt = (Account) gridSnapshot.getSelection()[0].getData();
							Account newAcnt = new Account(currAcnt.getType());
							newAcnt.setName("New Account");
							if (currAcnt instanceof Folder) {
								((Folder) currAcnt).addAccount(newAcnt);
							} else {
								currAcnt.getParent().addAccount(newAcnt);
							}
							Calculator.recalc(book, book.getCurrPeriod(), false);
							setSnapshotValues(gridSnapshot);
						}
					}
				} else if (e.keyCode == SWT.DEL && gridSnapshot.getSelectionIndex() != -1) {
					if (gridSnapshot.isFocusControl()) {
						if (gridSnapshot.getSelectionCount() == 1) {
							Account currAcnt = (Account) gridSnapshot.getSelection()[0].getData();
							if (currAcnt instanceof Folder) {
								if (((Folder) currAcnt).getList().size() == 0 && currAcnt.getParent() != null) {
									currAcnt.getParent().getList().remove(currAcnt);
								}
							} else {
								if (isAccountEmpty(currAcnt)) {
									currAcnt.getParent().getList().remove(currAcnt);
								}
							}
							Calculator.recalc(book, book.getCurrPeriod(), false);
							setSnapshotValues(gridSnapshot);
						}
					}
				}
			}
		};
	}

	/**
	 * Check if there is no references to account.
	 * 
	 * @param currAcnt
	 * @return
	 */
	protected static boolean isAccountEmpty(Account currAcnt) {
		Book book = App.getApp().getBookFile().getBook();
		for (Period period : book.getPeriodList()) {
			Value val = currAcnt.getValue(period.getId());
			if (val.isManual()) {
				return false;
			}
			for (Transaction trans : period.getTransactions()) {
				if (trans.getFrom().getAbsName().equals(currAcnt.getAbsName())
						|| trans.getTo().getAbsName().equals(currAcnt.getAbsName())) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * New transaction.
	 * 
	 * @param book
	 * @param to
	 * @param from
	 */
	protected void newTransaction(Book book, Account to, Account from) {
		Transaction trans = new Transaction();
		trans.setTo(to);
		trans.setFrom(from);
		trans.setValue(0.0);
		trans.setDescription("New Transaction");
		book.getCurrPeriod().getTransactions().add(trans);
		setTransactionValues(gridTrans);
		if (gridTrans.getItemCount() > 0) {
			gridTrans.forceFocus();
			gridTrans.select(gridTrans.getItemCount() - 1);
		}
		Calculator.recalc(book, book.getCurrPeriod(), trans, true, null, null);
		setSnapshotValues(gridSnapshot);
	}

	/**
	 * Get edit transaction listener. Double Click + Enter
	 * 
	 * @return
	 */
	protected Listener getEditTransactionListener() {
		// edit listener.
		Listener editListener = new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				Book book = App.getApp().getBookFile().getBook();
				// New transaction if the field is empty
				if (gridTrans.getItemCount() == 0) {
					List<Account> lstAcnt = book.getAccountList();
					Account to = lstAcnt.get(0);
					Account from = lstAcnt.get(0);
					if (to != null && from != null) {
						newTransaction(book, to, from);
					}
				}
				// Editing of transaction
				if (gridTrans.getCellSelectionCount() == 1) {
					Point point = gridTrans.getCellSelection()[0];
					if (point.x == TRANS_COL_FROM || point.x == TRANS_COL_TO) {
						Transaction tran = (Transaction) gridTrans.getItem(point.y).getData();
						GridEditor editor1 = new GridEditor(gridTrans);
						Combo combo = new Combo(gridTrans, SWT.READ_ONLY);

						List<Account> listAcn = book.getAccountList();
						int sel = 0;
						int col = 0;
						for (Account acn : listAcn) {
							combo.add(acn.getAbsName());
							if (point.x == TRANS_COL_FROM) {
								if (tran.getFrom().getAbsName().equals(acn.getAbsName())) {
									sel = col;
								}
							}
							if (point.x == TRANS_COL_TO) {
								if (tran.getTo().getAbsName().equals(acn.getAbsName())) {
									sel = col;
								}
							}
							col++;
						}
						combo.select(sel);
						editor1.grabHorizontal = true;
						editor1.setEditor(combo, gridTrans.getItem(point.y), point.x);
						combo.addTraverseListener(new TraverseListener() {
							@Override
							public void keyTraversed(TraverseEvent event) {
								// enter
								if (event.detail == SWT.TRAVERSE_RETURN) {
									Transaction tran = (Transaction) gridTrans.getItem(point.y).getData();
									Account oldFrom = tran.getFrom();
									Account oldTo = tran.getTo();
									// set new
									if (point.x == TRANS_COL_FROM) {
										tran.setFrom(book.getAccountByName(combo.getText()));
										gridTrans.getItem(point.y).setText(point.x, tran.getFrom().getAbsName());
									} else if (point.x == TRANS_COL_TO) {
										tran.setTo(book.getAccountByName(combo.getText()));
										gridTrans.getItem(point.y).setText(point.x, tran.getTo().getAbsName());
									}
									updateTransCombo();
									combo.dispose();
									Calculator.recalc(book, book.getCurrPeriod(), tran, true, oldFrom, oldTo);
									setSnapshotValues(gridSnapshot);
								}
							}
						});
						combo.addFocusListener(new FocusAdapter() {
							public void focusLost(FocusEvent event) {
								Transaction tran = (Transaction) gridTrans.getItem(point.y).getData();
								Account oldFrom = tran.getFrom();
								Account oldTo = tran.getTo();
								// set new
								if (point.x == TRANS_COL_FROM) {
									tran.setFrom(book.getAccountByName(combo.getText()));
									gridTrans.getItem(point.y).setText(point.x, tran.getFrom().getAbsName());
								} else if (point.x == TRANS_COL_TO) {
									tran.setTo(book.getAccountByName(combo.getText()));
									gridTrans.getItem(point.y).setText(point.x, tran.getTo().getAbsName());
								}
								updateTransCombo();
								combo.dispose();
								Calculator.recalc(book, book.getCurrPeriod(), tran, true, oldFrom, oldTo);
								setSnapshotValues(gridSnapshot);
							}
						});
						combo.addKeyListener(new KeyAdapter() {
							public void keyPressed(KeyEvent e) {
								if (e.character == SWT.ESC) {
									combo.dispose();
								}
							}
						});
					}

					if (point.x == TRANS_COL_VALUE || point.x == TRANS_COL_DESCR) {
						GridEditor editor1 = new GridEditor(gridTrans);
						Text text1 = new Text(gridTrans, point.x == TRANS_COL_DESCR ? SWT.LEFT : SWT.RIGHT);
						text1.setText(gridTrans.getItem(point.y).getText(point.x));
						editor1.grabHorizontal = true;
						editor1.setEditor(text1, gridTrans.getItem(point.y), point.x);
						text1.forceFocus();
						text1.selectAll();
						text1.addTraverseListener(new TraverseListener() {
							@Override
							public void keyTraversed(TraverseEvent event) {
								// enter
								if (event.detail == SWT.TRAVERSE_RETURN) {
									Transaction tran = (Transaction) gridTrans.getItem(point.y).getData();
									if (point.x == TRANS_COL_DESCR) {
										tran.setDescription(text1.getText());
										gridTrans.getItem(point.y).setText(point.x, tran.getDescription());
									} else if (point.x == TRANS_COL_VALUE) {
										tran.setValue(Formatter.parseValue(text1.getText()));
										gridTrans.getItem(point.y).setText(point.x,
												Formatter.formatValue(tran.getValue()));
									}
									updateTransSum();
									Calculator.recalc(book, book.getCurrPeriod(), tran, true, null, null);
									setSnapshotValues(gridSnapshot);
									text1.dispose();
								}
							}
						});
						text1.addFocusListener(new FocusAdapter() {
							public void focusLost(FocusEvent event) {
								Transaction tran = (Transaction) gridTrans.getItem(point.y).getData();
								if (point.x == TRANS_COL_DESCR) {
									tran.setDescription(text1.getText());
									gridTrans.getItem(point.y).setText(point.x, tran.getDescription());
								} else if (point.x == TRANS_COL_VALUE) {
									tran.setValue(Formatter.parseValue(text1.getText()));
									gridTrans.getItem(point.y).setText(point.x, Formatter.formatValue(tran.getValue()));
								}
								updateTransSum();
								Calculator.recalc(book, book.getCurrPeriod(), tran, true, null, null);
								setSnapshotValues(gridSnapshot);
								text1.dispose();
							}
						});
						text1.addKeyListener(new KeyAdapter() {
							public void keyPressed(KeyEvent e) {
								if (e.character == SWT.ESC) {
									text1.dispose();
								}
							}
						});
					}

				}
			}
		};
		return editListener;
	}

	/**
	 * Get edit snapshot listener. Double Click + Enter
	 * 
	 * @return
	 */
	protected Listener getEditSnapshotListener() {
		// edit listener.
		Listener editListener = new Listener() {
			/**
			 * Handle event.
			 */
			@Override
			public void handleEvent(Event arg0) {
				Book book = App.getApp().getBookFile().getBook();
				// Editing of snapshot
				if (gridSnapshot.getCellSelectionCount() == 1) {
					Point point = gridSnapshot.getCellSelection()[0];
					if (point.x == SNPSH_COL_ACNT || point.x == SNPSH_COL_BUDGET || point.x == SNPSH_COL_EOP) {
						Account acnt = (Account) gridSnapshot.getItem(point.y).getData();
						if (!(acnt instanceof Folder) || (point.x == SNPSH_COL_ACNT && acnt.getParent() != null)) {
							if (point.x == SNPSH_COL_BUDGET && (acnt.getType() == Account.TYPE_ASSET
									|| acnt.getType() == Account.TYPE_LIABILITY)) {
								return;
							}
							if (point.x == SNPSH_COL_EOP && (acnt.getType() == Account.TYPE_INCOME
									|| acnt.getType() == Account.TYPE_EXPENSE)) {
								return;
							}
							GridEditor editor1 = new GridEditor(gridSnapshot);
							Text text1 = new Text(gridSnapshot, SWT.RIGHT);
							text1.setText(gridSnapshot.getItem(point.y).getText(point.x));
							editor1.grabHorizontal = true;
							editor1.setEditor(text1, gridSnapshot.getItem(point.y), point.x);
							text1.forceFocus();
							text1.selectAll();
							text1.addTraverseListener(new TraverseListener() {
								@Override
								public void keyTraversed(TraverseEvent event) {
									// enter
									if (event.detail == SWT.TRAVERSE_RETURN) {
										Account acnt = (Account) gridSnapshot.getItem(point.y).getData();
										if (point.x == SNPSH_COL_EOP) {
											Value val = acnt.getValue(book.getCurrPeriodId());
											val.setEndOfPeriod(Formatter.parseValue(text1.getText()));
											if (val.getEndOfPeriod() != 0.0) {
												val.setManual(true);
											} else {
												val.setManual(false);// clear
											}
											gridSnapshot.getItem(point.y).setText(point.x,
													Formatter.formatValue(val.getEndOfPeriod()));
											Calculator.recalcAccount(book, book.getCurrPeriod(), acnt);
											setSnapshotValues(gridSnapshot);
										} else if (point.x == SNPSH_COL_BUDGET) {
											acnt.setBudget(book.getCurrPeriodId(),
													Formatter.parseValue(text1.getText()));
											gridSnapshot.getItem(point.y).setText(point.x,
													Formatter.formatValue(acnt.getBudget(book.getCurrPeriodId())));
											Calculator.recalcAccount(book, book.getCurrPeriod(), acnt);
											setSnapshotValues(gridSnapshot);
										} else if (point.x == SNPSH_COL_ACNT) {
											acnt.setName(text1.getText());
											gridSnapshot.getItem(point.y).setText(text1.getText());
											setTransactionValues(gridTrans);
										}
										text1.dispose();
									}
								}
							});
							text1.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent event) {
									Account acnt = (Account) gridSnapshot.getItem(point.y).getData();
									if (point.x == SNPSH_COL_EOP) {
										Value val = acnt.getValue(book.getCurrPeriodId());
										val.setEndOfPeriod(Formatter.parseValue(text1.getText()));
										if (val.getEndOfPeriod() != 0.0) {
											val.setManual(true);
										} else {
											val.setManual(false); // clear value
										}
										gridSnapshot.getItem(point.y).setText(point.x,
												Formatter.formatValue(val.getEndOfPeriod()));
										Calculator.recalcAccount(book, book.getCurrPeriod(), acnt);
										setSnapshotValues(gridSnapshot);
									} else if (point.x == SNPSH_COL_BUDGET) {
										acnt.setBudget(book.getCurrPeriodId(), Formatter.parseValue(text1.getText()));
										gridSnapshot.getItem(point.y).setText(point.x,
												Formatter.formatValue(acnt.getBudget(book.getCurrPeriodId())));
										Calculator.recalcAccount(book, book.getCurrPeriod(), acnt);
										setSnapshotValues(gridSnapshot);
									} else if (point.x == SNPSH_COL_ACNT) {
										acnt.setName(text1.getText());
										gridSnapshot.getItem(point.y).setText(text1.getText());
										setTransactionValues(gridTrans);
									}
									text1.dispose();
								}
							});
							text1.addKeyListener(new KeyAdapter() {
								public void keyPressed(KeyEvent e) {
									if (e.character == SWT.ESC) {
										text1.dispose();
									}
								}
							});
						}
					}
				}
			}
		};
		return editListener;
	}

	/**
	 * Load book from model.
	 * 
	 * @param book
	 */
	protected void loadGuiFromModel() {
		setSnapshotValues(gridSnapshot);
		setTransactionValues(gridTrans);
	}

	/**
	 * Update combo in transactions.
	 */
	protected void updateTransCombo() {
		String currSelectionFrom = cbAccountFrom.getText();
		String currSelectionTo = cbAccountTo.getText();

		Book book = App.getApp().getBookFile().getBook();
		cbAccountFrom.removeAll();
		cbAccountTo.removeAll();
		cbAccountFrom.add("");
		cbAccountTo.add("");
		List<Transaction> trans = book.getCurrPeriod().getTransactions();
		Map<String, String> accountFrom = new HashMap<String, String>();
		Map<String, String> accountTo = new HashMap<String, String>();
		for (Transaction tr : trans) {
			accountFrom.put(tr.getFrom().getAbsName(), "");
			accountTo.put(tr.getTo().getAbsName(), "");
		}
		String[] accountToA = accountTo.keySet().toArray(new String[accountTo.size()]);
		String[] accountFromA = accountFrom.keySet().toArray(new String[accountFrom.size()]);
		Arrays.sort(accountToA);
		Arrays.sort(accountFromA);
		for (String acntName : accountFromA) {
			cbAccountFrom.add(acntName);
		}
		for (String acntName : accountToA) {
			cbAccountTo.add(acntName);
		}
		cbAccountFrom.setText(currSelectionFrom);
		cbAccountTo.setText(currSelectionTo);
	}

	/**
	 * Set transaction values.
	 * 
	 * @param gridTrans
	 * @param trans
	 */
	protected void setTransactionValues(Grid gridTrans) {
		Book book = App.getApp().getBookFile().getBook();
		String fromFilter = null;
		String toFilter = null;
		if (cbAccountFrom.getSelectionIndex() != -1 && !cbAccountFrom.getText().equals("")) {
			fromFilter = cbAccountFrom.getText();
		}
		if (cbAccountTo.getSelectionIndex() != -1 && !cbAccountTo.getText().equals("")) {
			toFilter = cbAccountTo.getText();
		}
		// sorted columns
		int sortCol = TRANS_COL_FROM;
		int sortDir = SWT.UP;
		int cnt = gridTrans.getColumnCount();
		for (int i = 0; i < cnt; i++) {
			int tmpSort = gridTrans.getColumn(i).getSort();
			if (tmpSort != SWT.NONE) {
				sortCol = i;
				sortDir = tmpSort;
				break;
			}
		}
		final int sortColFin = sortCol;
		final int sortDirFin = sortDir;
		List<Transaction> trans = book.getCurrPeriod().getTransactions();
		Collections.sort(trans, new Comparator<Transaction>() {
			@Override
			public int compare(Transaction o1, Transaction o2) {
				int test = -1;
				if (sortDirFin == SWT.UP) {
					test = 1;
				}
				if (sortColFin == TRANS_COL_DESCR) {
					return test * o1.getDescription().compareTo(o2.getDescription());
				}
				if (sortColFin == TRANS_COL_FROM) {
					return test * o1.getFrom().getAbsName().compareTo(o2.getFrom().getAbsName());
				}
				if (sortColFin == TRANS_COL_TO) {
					return test * o1.getTo().getAbsName().compareTo(o2.getTo().getAbsName());
				}
				if (sortColFin == TRANS_COL_VALUE) {
					if (o1.getValue() > o2.getValue()) {
						return -1 * test;
					} else {
						return test;
					}
				}
				return 0;
			}
		});

		// clear transactions
		gridTrans.clearItems();
		for (int i = 0; i < trans.size(); i++) {
			if (fromFilter != null && !trans.get(i).getFrom().getAbsName().equals(fromFilter)) {
				continue;
			}
			if (toFilter != null && !trans.get(i).getTo().getAbsName().equals(toFilter)) {
				continue;
			}
			GridItem rowTrans = new GridItem(gridTrans, SWT.NONE);
			rowTrans.setData(trans.get(i));
			rowTrans.setText(TRANS_COL_FROM, trans.get(i).getFrom().getAbsName());
			rowTrans.setText(TRANS_COL_TO, trans.get(i).getTo().getAbsName());
			rowTrans.setText(TRANS_COL_VALUE, Formatter.formatValue(trans.get(i).getValue()));
			rowTrans.setText(TRANS_COL_DESCR, trans.get(i).getDescription());
		}
		updateBackgroundColor(gridTrans);
		updateTransSum();
		updateTransCombo();
	}

	/**
	 * Set snapshot values.
	 * 
	 * @param gridSnapshot
	 */
	protected static void setSnapshotValues(Grid gridSnapshot) {
		//
		Book book = App.getApp().getBookFile().getBook();
		// clear snapshot
		gridSnapshot.clearItems();
		// reload
		GridItem assets = new GridItem(gridSnapshot, SWT.NONE);
		assets.setText(book.getAssetList().getName());
		setAccountValues(assets, book.getAssetList());
		initAccountTree(assets, book.getAssetList());
		assets.setImage(ImageUtil.IMG_FOLDER);

		GridItem liability = new GridItem(gridSnapshot, SWT.NONE);
		liability.setText(book.getLiabilityList().getName());
		setAccountValues(liability, book.getLiabilityList());
		initAccountTree(liability, book.getLiabilityList());
		liability.setImage(ImageUtil.IMG_FOLDER);

		GridItem income = new GridItem(gridSnapshot, SWT.NONE);
		income.setText(book.getIncomeList().getName());
		setAccountValues(income, book.getIncomeList());
		initAccountTree(income, book.getIncomeList());
		income.setImage(ImageUtil.IMG_FOLDER);

		GridItem expenses = new GridItem(gridSnapshot, SWT.NONE);
		expenses.setText(book.getExpenseList().getName());
		setAccountValues(expenses, book.getExpenseList());
		initAccountTree(expenses, book.getExpenseList());
		expenses.setImage(ImageUtil.IMG_FOLDER);

		// add footer values
		Value bookValue = book.getValue(book.getCurrPeriodId());
		gridSnapshot.getColumn(SNPSH_COL_SOP).setFooterText(Formatter.formatValue(bookValue.getStartOfPeriod()));
		gridSnapshot.getColumn(SNPSH_COL_DEBIT).setFooterText(Formatter.formatValue(bookValue.getDebit()));
		gridSnapshot.getColumn(SNPSH_COL_CREDIT).setFooterText(Formatter.formatValue(bookValue.getCredit()));
		gridSnapshot.getColumn(SNPSH_COL_EOP).setFooterText(Formatter.formatValue(bookValue.getEndOfPeriod()));
		gridSnapshot.getColumn(SNPSH_COL_CONTROL).setFooterText(Formatter.formatValue(bookValue.getControl()));

		// auto resize column
		for (GridColumn tc : gridSnapshot.getColumns()) {
			tc.pack();
		}
		updateBackgroundColor(gridSnapshot);
	}

	/**
	 * Update
	 * 
	 * @param grid
	 */
	public static void updateBackgroundColor(Grid grid) {
		int k = 0;
		for (int i = 0; i < grid.getItemCount(); i++) {
			grid.getItem(i).setBackground(new Color(grid.getDisplay(), 255, 255, 255));
			if (grid.getItem(i).isVisible()) {
				if (k % 2 == 0) {
					grid.getItem(i).setBackground(new Color(grid.getDisplay(), 245, 248, 255));
				}
				k++;
			}
			// Value
			Object account = grid.getItem(i).getData();
			if (account != null && account instanceof Account) {
				Book book = App.getApp().getBookFile().getBook();
				Value value = ((Account) account).getValue(book.getCurrPeriodId());
				if (value != null && ((Value) value).isManual()) {
					grid.getItem(i).setBackground(SNPSH_COL_EOP, new Color(grid.getDisplay(), 255, 211, 182));
					grid.getItem(i).setToolTipText(SNPSH_COL_EOP, App.getGuiProp("book.tooltip.manual"));
				}
			}
		}
	}

	/**
	 * Build tree.
	 * 
	 * @param parentAccount
	 * @param parentAccountFld
	 */
	protected static void initAccountTree(GridItem parentAccount, Folder parentAccountFld) {
		for (int i = 0; i < parentAccountFld.getList().size(); i++) {
			Account acnt = parentAccountFld.getList().get(i);
			GridItem item = new GridItem(parentAccount, SWT.NONE);

			item.setText(acnt.getName());
			setAccountValues(item, acnt);
			if (acnt instanceof Folder) {
				item.setImage(ImageUtil.IMG_FOLDER);
				initAccountTree(item, (Folder) acnt);
			} else {
				item.setImage(ImageUtil.IMG_ACCOUNT);
			}
		}
	}

	/**
	 * Set account values.
	 * 
	 * @param item
	 * @param acnt
	 */
	protected static void setAccountValues(GridItem item, Account acnt) {
		Book book = App.getApp().getBookFile().getBook();
		// Columns
		Value value = acnt.getValue(book.getCurrPeriod().getId());
		if (value == null) {
			value = new Value();
			acnt.setValue(book.getCurrPeriod().getId(), value);
		}
		if (acnt.getBudget(book.getCurrPeriod().getId()) == null) {
			acnt.setBudget(book.getCurrPeriod().getId(), 0.0);
		}
		item.setText(SNPSH_COL_SOP, Formatter.formatValue(value.getStartOfPeriod()));
		item.setText(SNPSH_COL_DEBIT, Formatter.formatValue(value.getDebit()));
		item.setText(SNPSH_COL_CREDIT, Formatter.formatValue(value.getCredit()));
		item.setText(SNPSH_COL_EOP, Formatter.formatValue(value.getEndOfPeriod()));
		item.setText(SNPSH_COL_CONTROL, Formatter.formatValue(value.getControl()));
		item.setText(SNPSH_COL_BUDGET, Formatter.formatValue(acnt.getBudget(book.getCurrPeriod().getId())));
		item.setExpanded(acnt.isViewExpanded());
		item.setData(acnt);
	}

	/**
	 * Update sum for transactions.
	 */
	protected void updateTransSum() {
		Book book = App.getApp().getBookFile().getBook();
		String fromFilter = null;
		String toFilter = null;
		if (cbAccountFrom.getSelectionIndex() != -1 && !cbAccountFrom.getText().equals("")) {
			fromFilter = cbAccountFrom.getText();
		}
		if (cbAccountTo.getSelectionIndex() != -1 && !cbAccountTo.getText().equals("")) {
			toFilter = cbAccountTo.getText();
		}
		List<Transaction> trans = book.getCurrPeriod().getTransactions();
		double sum = 0.0;
		for (int i = 0; i < trans.size(); i++) {
			if (fromFilter != null && !trans.get(i).getFrom().getAbsName().equals(fromFilter)) {
				continue;
			}
			if (toFilter != null && !trans.get(i).getTo().getAbsName().equals(toFilter)) {
				continue;
			}
			sum += trans.get(i).getValue();
		}
		gridTrans.getColumn(TRANS_COL_VALUE).setFooterText(Formatter.formatValue(sum));
	}

	/**
	 * Get selection listener.
	 * 
	 * @param column
	 * @return
	 */
	public SelectionListener getSortListener(GridColumn column) {
		SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// clear all
				int cnt = gridTrans.getColumnCount();
				for (int i = 0; i < cnt; i++) {
					if (!gridTrans.getColumn(i).equals(column)) {
						gridTrans.getColumn(i).setSort(SWT.NONE);
					}
				}
				if (column.getSort() == SWT.UP) {
					column.setSort(SWT.DOWN);
					setTransactionValues(gridTrans);
				} else {
					column.setSort(SWT.UP);
					setTransactionValues(gridTrans);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		};
		return listener;
	}
}
