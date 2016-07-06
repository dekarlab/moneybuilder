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

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.dekarlab.moneybuilder.App;
import de.dekarlab.moneybuilder.model.Account;
import de.dekarlab.moneybuilder.model.Book;
import de.dekarlab.moneybuilder.model.Folder;
import de.dekarlab.moneybuilder.model.Period;
import de.dekarlab.moneybuilder.model.util.Formatter;

/**
 * Analytics view.
 * 
 * @author dk
 * 
 */
public class AnalyticsView extends Composite {
	/**
	 * Colors. http://www.mulinblog.com/a-color-palette-optimized-for-data-
	 * visualization/
	 * 
	 */
	private Color[] COLORS = new Color[] { new Color(93, 165, 218), // 5DA5DA
																	// (blue)
			new Color(241, 88, 84), // F15854 (red)
			new Color(222, 207, 63), // DECF3F (yellow)
			new Color(250, 164, 58), // FAA43A (orange)
			new Color(96, 189, 104), // 60BD68 (green)
			new Color(77, 77, 77), // 4D4D4D
			// (gray)
			new Color(241, 124, 176), // F17CB0 (pink)
			new Color(178, 145, 47), // B2912F (brown)
			new Color(178, 118, 178) // B276B2 (purple)
	};
	/**
	 * Reports.
	 */
	private Combo cmbReports;
	/**
	 * Chart composite.
	 */
	private ChartComposite chartComp;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param style
	 */
	public AnalyticsView(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Create GUI method.
	 */
	protected void initView() throws Exception {
		// init layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		Label lblReport = new Label(this, SWT.NONE);
		lblReport.setText(App.getGuiProp("analytics.reports"));

		cmbReports = new Combo(this, SWT.READ_ONLY);
		cmbReports.add(App.getGuiProp("report.allperiods.capital"));
		cmbReports.add(App.getGuiProp("report.allperiods.pl"));
		cmbReports.add(App.getGuiProp("report.income"));
		cmbReports.add(App.getGuiProp("report.expenses"));
		cmbReports.add(App.getGuiProp("report.assets"));
		cmbReports.add(App.getGuiProp("report.liability"));
		cmbReports.select(0);
		createChart();
		// select by default
		cmbReports.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				createChart();
			}
		});
	}

	/**
	 * Create pie chart.
	 * 
	 * @param dataset
	 * @param title
	 * @return
	 */
	protected JFreeChart createPieChart(final PieDataset dataset, final String title) {
		final JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setNoDataMessage(App.getGuiProp("report.nodata.msg"));
		plot.setCircular(true);
		// plot.set
		plot.setLabelGap(0.02);
		plot.setBackgroundPaint(Color.white);
		chart.removeLegend();
		plot.setBackgroundPaint(Color.white);
		Iterator<?> it = dataset.getKeys().iterator();
		int color = 0;
		while (it.hasNext()) {
			plot.setSectionPaint((String) it.next(), COLORS[color]);
			color++;
			if (COLORS.length == color) {
				color = 0;
			}
		}
		plot.setLabelBackgroundPaint(Color.white);
		StandardPieSectionLabelGenerator slbl = new StandardPieSectionLabelGenerator("{0} {2}",
				new DecimalFormat("#,##0"), new DecimalFormat("0%"));
		plot.setLabelGenerator(slbl);
		plot.setLabelFont(new Font("Helvetica", Font.PLAIN, 14));
		plot.setLabelOutlinePaint(Color.white);
		plot.setLabelShadowPaint(Color.white);
		plot.setShadowPaint(Color.white);
		plot.setIgnoreZeroValues(true);
		return chart;
	}

	/**
	 * Create pie chart.
	 * 
	 * @param dataset
	 * @param title
	 * @return
	 */
	protected JFreeChart createLineChart(final CategoryDataset dataset, final String title) {
		final JFreeChart chart = ChartFactory.createLineChart("", // chart title
				App.getGuiProp("report.period.lbl"), // domain axis label
				App.getGuiProp("report.value.lbl"), // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);
		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setNoDataMessage(App.getGuiProp("report.nodata.msg"));
		plot.setBackgroundPaint(Color.white);
		plot.setBackgroundPaint(Color.white);
		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		int color = 0;
		CategoryItemRenderer renderer = plot.getRenderer();
		for (int ser = 0; ser < dataset.getColumnCount(); ser++) {
			renderer.setSeriesPaint(ser, COLORS[color]);
			color++;
			if (COLORS.length == color) {
				color = 0;
			}
		}
		return chart;
	}

	/**
	 * Create chart.
	 */
	protected void createChart() {
		if (chartComp != null) {
			chartComp.dispose();
		}
		JFreeChart chart = null;
		String reportSel = cmbReports.getItem(cmbReports.getSelectionIndex());
		Book book = App.getApp().getBookFile().getBook();
		Period period = book.getCurrPeriod();
		if (reportSel.equals(App.getGuiProp("report.expenses"))) {
			chart = createPieChart(createDatasetSnapshot(Account.TYPE_EXPENSE),
					// cmbReports.getItem(cmbReports.getSelectionIndex())
					Formatter.formatValue(book.getExpenseList().getValue(period.getId()).getCredit()));
		} else if (reportSel.equals(App.getGuiProp("report.income"))) {
			chart = createPieChart(createDatasetSnapshot(Account.TYPE_INCOME),
					// cmbReports.getItem(cmbReports.getSelectionIndex())
					Formatter.formatValue(book.getIncomeList().getValue(period.getId()).getDebit()));
		} else if (reportSel.equals(App.getGuiProp("report.assets"))) {
			chart = createPieChart(createDatasetSnapshot(Account.TYPE_ASSET),
					// cmbReports.getItem(cmbReports.getSelectionIndex())
					Formatter.formatValue(book.getAssetList().getValue(period.getId()).getEndOfPeriod()));
		} else if (reportSel.equals(App.getGuiProp("report.liability"))) {
			chart = createPieChart(createDatasetSnapshot(Account.TYPE_LIABILITY),
					// cmbReports.getItem(cmbReports.getSelectionIndex())
					Formatter.formatValue(book.getLiabilityList().getValue(period.getId()).getEndOfPeriod()));
		} else if (reportSel.equals(App.getGuiProp("report.allperiods.capital"))) {
			chart = createLineChart(createDatasetPeriod(true), cmbReports.getItem(cmbReports.getSelectionIndex()));
		} else if (reportSel.equals(App.getGuiProp("report.allperiods.pl"))) {
			chart = createLineChart(createDatasetPeriod(false), cmbReports.getItem(cmbReports.getSelectionIndex()));
		}
		if (chart == null) {
			return;
		}
		GridData chartGd = new GridData(GridData.FILL_BOTH);
		chartGd.horizontalSpan = 2;
		chartComp = new ChartComposite(this, SWT.NONE, chart, false);
		chartComp.setLayoutData(chartGd);
		// layout of parent works
		getParent().layout(true, true);
		// marks the composite's screen are as invalidates, which will force a
		getParent().redraw();
		// tells the application to do all outstanding paint requests
		// immediately
		getParent().update();
	}

	/**
	 * Load book from model.
	 * 
	 * @param book
	 */
	protected void loadGuiFromModel() {
		createChart();
	}

	/**
	 * Dataset expenses.
	 * 
	 * @return
	 */
	protected PieDataset createDatasetSnapshot(int type) {
		Book book = App.getApp().getBookFile().getBook();
		Period period = book.getCurrPeriod();
		Folder folder = book.getExpenseList();
		if (type == Account.TYPE_ASSET) {
			folder = book.getAssetList();
		} else if (type == Account.TYPE_EXPENSE) {
			folder = book.getExpenseList();
		} else if (type == Account.TYPE_LIABILITY) {
			folder = book.getLiabilityList();
		} else if (type == Account.TYPE_INCOME) {
			folder = book.getIncomeList();
		}
		final DefaultPieDataset result = new DefaultPieDataset();
		for (Account acnt : folder.getList()) {
			double value = 0.0;
			if (type == Account.TYPE_ASSET || type == Account.TYPE_LIABILITY) {
				value = acnt.getValue(period.getId()).getEndOfPeriod();
			} else if (type == Account.TYPE_EXPENSE) {
				value = acnt.getValue(period.getId()).getCredit();

			} else if (type == Account.TYPE_INCOME) {
				value = acnt.getValue(period.getId()).getDebit();
			}
			result.setValue(acnt.getName(), value);
		}
		return result;
	}

	/**
	 * Dataset expenses.
	 * 
	 * @return
	 */
	protected CategoryDataset createDatasetPeriod(boolean capital) {
		Book book = App.getApp().getBookFile().getBook();
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Period> periodList = book.getPeriodList();
		for (Period period : periodList) {
			Folder assets = book.getAssetList();
			double assetValue = assets.getValue(period.getId()).getEndOfPeriod();
			Folder liability = book.getLiabilityList();
			double liabilityValue = liability.getValue(period.getId()).getEndOfPeriod();
			Folder income = book.getIncomeList();
			double incomeValue = income.getValue(period.getId()).getDebit()
					- income.getValue(period.getId()).getCredit();
			Folder expense = book.getExpenseList();
			double expenseValue = expense.getValue(period.getId()).getCredit()
					- expense.getValue(period.getId()).getDebit();
			if (capital) {
				if (assetValue != 0.0 || liabilityValue != 0.0) {
					dataset.addValue(assetValue - liabilityValue, assets.getName() + " - " + liability.getName(),
							Formatter.formatDateMonth(period.getDate()));
				}
			} else {
				if (incomeValue != 0.0 || expenseValue != 0.0) {
					dataset.addValue(incomeValue, income.getName(), Formatter.formatDateMonth(period.getDate()));
					dataset.addValue(expenseValue, expense.getName(), Formatter.formatDateMonth(period.getDate()));
				}
			}
		}
		return dataset;
	}

}
