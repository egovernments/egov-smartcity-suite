/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.reports.web;

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
