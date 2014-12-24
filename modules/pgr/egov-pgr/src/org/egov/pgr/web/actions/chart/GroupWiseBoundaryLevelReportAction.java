/*
 * @(#)GroupWiseBoundaryLevelReportAction.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.ejb.api.BoundaryService;
import org.egov.pgr.constant.PGRConstants;
import org.egov.web.actions.BaseFormAction;

import ChartDirector.Chart;
import ChartDirector.PieChart;

@ParentPackage("egov")
public class GroupWiseBoundaryLevelReportAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(GroupWiseBoundaryLevelReportAction.class);
	private String[] complaintGroups;
	private double[] groupWiseComplains;
	private String[] complatedComplaints;
	private String chartURL;
	private String imageMap;

	private BoundaryService boundaryService;

	@Override
	public Object getModel() {

		return null;
	}

	public String newform() {

		LOGGER.debug("GroupWiseBoundaryLevelReportAction | newform | Start ");
		setNoOfCompByGroups();
		setNoOfCompletedCompByGroups();
		createPieChart();
		return NEW;
	}

	@SuppressWarnings("unchecked")
	private void setNoOfCompByGroups() {
		final String topLevelBndry = ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString();
		final StringBuffer query = new StringBuffer(100);
		query.append(" select cg.complaintgroupname , COUNT(cd.complaintid) from eggr_complaintdetails cd , eggr_complaintgroup cg ,")
				.append("  eggr_complainttypes ct where cd.complainttype = ct.complainttypeid and ct.complaintgroup_id = cg.id_complaintgroup").append(" and cd.toplevelbndry =").append(topLevelBndry)
				.append(" group by cg.complaintgroupname ORDER BY cg.complaintgroupname");

		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		if (!list.isEmpty()) {
			this.complaintGroups = new String[list.size()];
			this.groupWiseComplains = new double[list.size()];
			int index = 0;
			for (final Object[] object : list) {

				this.complaintGroups[index] = object[0].toString();
				this.groupWiseComplains[index] = Double.parseDouble(object[1].toString());
				index++;
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void setNoOfCompletedCompByGroups() {
		final String topLevelBndry = ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString();
		final StringBuffer query = new StringBuffer(100);
		query.append(" select cg.complaintgroupname , COUNT(cd.complaintid) from eggr_complaintdetails cd , eggr_complaintgroup cg").append("  ,eggr_complainttypes ct , eggr_redressaldetails rd, eggr_complaintstatus cs where cd.complainttype ")
				.append(" = ct.complainttypeid  and ct.complaintgroup_id = cg.id_complaintgroup and cd.complaintid=rd.complaintid").append(" and rd.complaintstatusid =cs.complaintstatusid and  upper(cs.statusname)='COMPLETED' and cd.toplevelbndry=")
				.append(topLevelBndry).append(" group by cg.complaintgroupname ORDER BY cg.complaintgroupname");
		final List<Object[]> list = HibernateUtil.getCurrentSession().createSQLQuery(query.toString()).list();
		final Map<String, Integer> groupCompMap = new HashMap<String, Integer>();
		if (!list.isEmpty()) {
			for (final Object[] object : list) {
				groupCompMap.put(object[0].toString(), Integer.valueOf(object[1].toString()));

			}

		}
		this.complatedComplaints = new String[this.complaintGroups.length];
		for (int i = 0; i < this.complaintGroups.length; i++) {
			if (null != groupCompMap.get(this.complaintGroups[i])) {

				this.complatedComplaints[i] = "Completed:" + groupCompMap.get(this.complaintGroups[i]);
			} else {
				this.complatedComplaints[i] = "Completed: 0";
			}
		}

	}

	private void createPieChart() {
		final String topLevelBndry = ServletActionContext.getRequest().getSession().getAttribute(PGRConstants.SESSION_ATTRIBUTE_TOP_LEVEL_BOUNDARY).toString();
		final Boundary topLvlBoundary = this.boundaryService.getBoundary(Integer.valueOf(topLevelBndry));
		final PieChart pieChart = new PieChart(1000, 850);
		pieChart.setPieSize(500, 200, 150);
		pieChart.setStartAngle(90);

		pieChart.addExtraField(this.complatedComplaints);

		pieChart.addTitle(topLvlBoundary.getBndryNameLocal().toUpperCase() + " : " + getText("groupwise.comp.breakdown"), "Times New Roman", 20).setBackground(0xefefef, Chart.Transparent, 1);

		pieChart.addLegend(400, 360, true, "verdana;Mangal", 10);

		pieChart.set3D(-1, 60);

		pieChart.setLabelFormat("{label}: {value} ({percent}%)\n{field0}");
		pieChart.setLabelStyle("verdana;Mangal", 8);
		pieChart.setLabelLayout(Chart.SideLayout, -1, 60, 300);
		pieChart.setColors(Chart.transparentPalette);

		for (int i = 0; i < this.complaintGroups.length; i++) {
			pieChart.setExplode(i);
		}

		pieChart.setData(this.groupWiseComplains, this.complaintGroups);

		// output the chart
		this.chartURL = pieChart.makeSession(ServletActionContext.getRequest(), "chart1");

		// include tool tip for the chart
		this.imageMap = pieChart.getHTMLImageMap("", "", "title='{label}: {value} ({percent}%)'");

	}

	/**
	 * @return the complaintGroups
	 */
	public String[] getComplaintGroups() {
		return this.complaintGroups;
	}

	/**
	 * @param complaintGroups the complaintGroups to set
	 */
	public void setComplaintGroups(final String[] complaintGroups) {
		this.complaintGroups = complaintGroups;
	}

	/**
	 * @return the groupWiseComplains
	 */
	public double[] getGroupWiseComplains() {
		return this.groupWiseComplains;
	}

	/**
	 * @param groupWiseComplains the groupWiseComplains to set
	 */
	public void setGroupWiseComplains(final double[] groupWiseComplains) {
		this.groupWiseComplains = groupWiseComplains;
	}

	/**
	 * @return the complatedComplaints
	 */
	public String[] getComplatedComplaints() {
		return this.complatedComplaints;
	}

	/**
	 * @param complatedComplaints the complatedComplaints to set
	 */
	public void setComplatedComplaints(final String[] complatedComplaints) {
		this.complatedComplaints = complatedComplaints;
	}

	/**
	 * @param boundaryService the boundaryService to set
	 */
	public void setBoundaryService(final BoundaryService boundaryService) {
		this.boundaryService = boundaryService;
	}

	/**
	 * @return the chartURL
	 */
	public String getChartURL() {
		return this.chartURL;
	}

	/**
	 * @param chartURL the chartURL to set
	 */
	public void setChartURL(final String chartURL) {
		this.chartURL = chartURL;
	}

	/**
	 * @return the imageMap
	 */
	public String getImageMap() {
		return this.imageMap;
	}

	/**
	 * @param imageMap the imageMap to set
	 */
	public void setImageMap(final String imageMap) {
		this.imageMap = imageMap;
	}

}
