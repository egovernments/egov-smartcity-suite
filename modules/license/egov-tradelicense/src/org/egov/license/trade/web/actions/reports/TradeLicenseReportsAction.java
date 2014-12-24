/*
 * @(#)TradeLicenseReportsAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.reports;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.domain.service.LicenseReportService;
import org.egov.license.utils.Constants;
import org.egov.license.web.actions.common.BaseLicenseAction;
import org.egov.web.utils.EgovPaginatedList;

public class TradeLicenseReportsAction extends BaseLicenseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LicenseReportService licenseReportService;
	private EgovPaginatedList paginateList;
	private Integer zoneId;
	private List<Map<String, Object>> totalList;

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	public String showZoneWiseReport() {

		this.LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		final String pno = this.licenseReportService.getParameterValue(Constants.PARAM_PAGE, this.parameters);
		this.paginateList = this.licenseReportService.getZoneWiseReportList(pno, Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE);
		this.totalList = this.licenseReportService.getTotalsForWardWiseReport(this.zoneId, Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE);
		this.LOGGER.debug("Exiting From the showZoneWiseReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.ZONE_WISE_REPORT;
	}

	public String showWardWiseReport() {
		this.LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		final String pno = this.licenseReportService.getParameterValue(Constants.PARAM_PAGE, this.parameters);
		this.paginateList = this.licenseReportService.getWardWiseReportList(this.zoneId, pno, Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE);
		this.totalList = this.licenseReportService.getTotalsForWardWiseReport(this.zoneId, Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE);
		this.LOGGER.debug("Exiting from the showWardWiseReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.WARD_WISE_REPORT;
	}

	public String showTradeWiseReport() {
		this.LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		final String pno = this.licenseReportService.getParameterValue(Constants.PARAM_PAGE, this.parameters);
		this.paginateList = this.licenseReportService.getTradeWiseReportList(pno, Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE, Constants.TRADELICENSE);
		this.totalList = this.licenseReportService.getTotalForTradeWiseReport(Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE, Constants.TRADELICENSE);
		this.LOGGER.debug("Exiting from the showTradeWiseReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.TRADE_WISE_REPORT;
	}

	public String showLateRenewalsReport() {
		this.LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		final String pno = this.licenseReportService.getParameterValue(Constants.PARAM_PAGE, this.parameters);
		this.paginateList = this.licenseReportService.getLateRenewalsListReport(pno, Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE);
		this.totalList = this.licenseReportService.getTotalForLateRenewalsReport(Constants.TRADELICENSE_MODULENAME, Constants.TRADELICENSE_LICENSETYPE);
		this.LOGGER.debug("Exiting from the showLateRenewalsReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.LATE_RENEWALS_REPORT;
	}

	@Override
	protected License license() {
		return null;
	}

	@Override
	protected BaseLicenseService service() {
		return null;
	}

	public void setLicenseReportService(final LicenseReportService licenseReportService) {
		this.licenseReportService = licenseReportService;
	}

	public EgovPaginatedList getPaginateList() {
		return this.paginateList;
	}

	public void setPaginateList(final EgovPaginatedList paginateList) {
		this.paginateList = paginateList;
	}

	public Integer getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(final Integer zoneId) {
		this.zoneId = zoneId;
	}

	public List<Map<String, Object>> getTotalList() {
		return this.totalList;
	}

	public void setTotalList(final List<Map<String, Object>> totalList) {
		this.totalList = totalList;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append(" TradeLicenseReportsAction={");
		str.append(" licenseReportService=").append(this.licenseReportService == null ? "null" : this.licenseReportService.toString());
		str.append(" paginateList=").append(this.paginateList == null ? "null" : this.paginateList.toString());
		str.append(" zoneId=").append(this.zoneId == null ? "null" : this.zoneId.toString());
		str.append(" totalList=").append(this.totalList == null ? "null" : this.totalList.toString());
		return str.toString();
	}

}
