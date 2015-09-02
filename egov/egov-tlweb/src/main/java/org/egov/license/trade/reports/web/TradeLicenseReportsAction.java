package org.egov.license.trade.reports.web;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.domain.service.LicenseReportService;
import org.egov.license.domain.web.BaseLicenseAction;
import org.egov.license.utils.Constants;
import org.egov.web.utils.EgovPaginatedList;

public class TradeLicenseReportsAction extends BaseLicenseAction{

	private LicenseReportService licenseReportService;
	private EgovPaginatedList paginateList;
	private Integer zoneId;
	private List<Map<String, Object>> totalList;

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	public String showZoneWiseReport(){
		
		LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		String pno = licenseReportService.getParameterValue(Constants.PARAM_PAGE,
				parameters);
		paginateList=licenseReportService.getZoneWiseReportList(pno,Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE);
		totalList=licenseReportService.getTotalsForWardWiseReport(zoneId,Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE);
		LOGGER.debug("Exiting From the showZoneWiseReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.ZONE_WISE_REPORT;
	}
	
	public String showWardWiseReport(){
		LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		String pno = licenseReportService.getParameterValue(Constants.PARAM_PAGE,
				parameters);
		paginateList=licenseReportService.getWardWiseReportList(zoneId,pno,Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE);
		totalList=licenseReportService.getTotalsForWardWiseReport(zoneId,Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE);
		LOGGER.debug("Exiting from the showWardWiseReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.WARD_WISE_REPORT;
	}
	public String showTradeWiseReport(){
		LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		String pno = licenseReportService.getParameterValue(Constants.PARAM_PAGE,
				parameters);
		paginateList=licenseReportService.getTradeWiseReportList(pno,Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE,Constants.TRADELICENSE);
		totalList=licenseReportService.getTotalForTradeWiseReport(Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE,Constants.TRADELICENSE);
		LOGGER.debug("Exiting from the showTradeWiseReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.TRADE_WISE_REPORT;
	}
	
	public String showLateRenewalsReport(){
		LOGGER.debug("Trade License Report Elements:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		String pno = licenseReportService.getParameterValue(Constants.PARAM_PAGE,
				parameters);
		paginateList=licenseReportService.getLateRenewalsListReport(pno,Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE);
		totalList=licenseReportService.getTotalForLateRenewalsReport(Constants.TRADELICENSE_MODULENAME,Constants.TRADELICENSE_LICENSETYPE);
		LOGGER.debug("Exiting from the showLateRenewalsReport Method:<<<<<<<<<<>>>>>>>>>>>>>:");
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
	
	public void setLicenseReportService(LicenseReportService licenseReportService) {
		this.licenseReportService = licenseReportService;
	}
	
	public EgovPaginatedList getPaginateList() {
		return paginateList;
	}


	public void setPaginateList(EgovPaginatedList paginateList) {
		this.paginateList = paginateList;
	}
	
	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public List<Map<String, Object>> getTotalList() {
		return totalList;
	}

	public void setTotalList(List<Map<String, Object>> totalList) {
		this.totalList = totalList;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		str.append(" TradeLicenseReportsAction={");
		str.append(" licenseReportService=").append(licenseReportService==null ? "null": licenseReportService.toString());
		str.append(" paginateList=").append(paginateList ==null ? "null" : paginateList.toString());
		str.append(" zoneId=").append(zoneId ==null ? "null" : zoneId.toString());
		str.append(" totalList=").append(totalList ==null ? "null" : totalList.toString());
		return str.toString();
	}

}
