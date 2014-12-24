/*
 * @(#)ChartUtils.java 3.0, 10 Jun, 2013 11:26:05 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.awt.Color;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartUtils {

	/**
	 * This method is to frame the Bar Chart
	 * @param map map-key-X-axis-Values map-value-Y-axis-Values
	 * @param xaxis
	 * @param yaxis
	 * @param title
	 */
	public static void frameBarChart(final Map<String, Long> map, final String xaxis, final String yaxis, final String title) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (final Map.Entry<String, Long> entry : map.entrySet()) {
			dataset.setValue(entry.getValue(), entry.getKey(), "");
		}
		final JFreeChart chart = ChartFactory.createBarChart(title, xaxis, yaxis, dataset, PlotOrientation.VERTICAL, true, true, true);
		chart.setBackgroundPaint(Color.white);
		chart.getTitle().setPaint(Color.gray);
		final CategoryPlot p = chart.getCategoryPlot();
		p.setRangeGridlinePaint(Color.blue);
		final ChartFrame frame = new ChartFrame(title, chart);
		frame.setVisible(true);
		frame.setSize(800, 700);
	}

}
