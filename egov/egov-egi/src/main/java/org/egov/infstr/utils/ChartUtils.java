/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
