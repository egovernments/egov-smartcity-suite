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
package org.egov.tradelicense.domain.service.integration;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.LatePayPenaltyCalculator;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseDemand;
import org.egov.tradelicense.utils.LicenseUtils;

public class LicenseBill extends AbstractBillable implements LatePayPenaltyCalculator {

	private LicenseUtils licenseUtils;
	private License license;
	private String moduleName;
	private String serviceCode;
	private EgBillDao billDao;
	
	public void setBillDao(EgBillDao billDao) {
		this.billDao = billDao;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setLicenseUtils(LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	public Module getModule() {
		return this.licenseUtils.getModule(this.moduleName);
	}

	public String getBillPayee() {
		return this.license.getLicensee().getApplicantName();
	}

	public String getBillAddress() {
		return this.license.getLicensee().getAddress().toString()+"\nPh : "+defaultString(this.license.getLicensee().getPhoneNumber());
	}

	public EgDemand getCurrentDemand() {
		final Set<LicenseDemand> demands = license.getDemandSet();
		for (EgDemand demand : demands) {
			if (demand.getIsHistory().equals("N")) {
				return demand;
			}
		}
		return null;
	}

	public List<EgDemand> getAllDemands() {
		return new ArrayList<EgDemand>(license.getDemandSet());

	}

	public EgBillType getBillType() {
		return billDao.getBillTypeByCode("AUTO");

	}

	public Date getBillLastDueDate() {
		Date dueDate = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dueDate);
		cal.get(Calendar.MONTH + 1);
		dueDate = cal.getTime();
		return dueDate;

	}

	public Integer getBoundaryNum() {
		return this.license.getBoundary().getId();
	}

	public String getBoundaryType() {
		return this.license.getBoundary().getBoundaryType().getName();
	}

	public String getDepartmentCode() {
		return "CAF";//TODO
	}

	public BigDecimal getFunctionaryCode() {
		return BigDecimal.ZERO;
	}

	public String getFundCode() {
		return "45061";//TODO Insert
	}

	public String getFundSourceCode() {
		return "BOM 637";//TODO
	}

	public Date getIssueDate() {
		return new Date();
	}

	public Date getLastDate() {
		return this.getBillLastDueDate();
	}

	public Boolean getOverrideAccountHeadsAllowed() {
		return false;
	}

	public Boolean getPartPaymentAllowed() {
		return false;
	}


	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getServiceCode() {
		return this.serviceCode;
	}

	public BigDecimal getTotalAmount() {		
		return getCurrentDemand().getBaseDemand();
	}

	public Long getUserId() {
		return Long.valueOf(EGOVThreadLocals.getUserId());
	}

	public String getDescription() {
		return  isBlank(this.license.getLicenseNumber()) ? "Application No : "+this.license.getApplicationNumber() : "License No : "+this.license.getLicenseNumber();
	}

	public String getDisplayMessage() {
		return this.moduleName+" Collection";
	}

	public String getCollModesNotAllowed() {
		return "";
	}

	public String getPropertyId() {
		return defaultString(this.license.getLicenseNumber(),this.license.getApplicationNumber());
	}

	public Boolean isCallbackForApportion() {
		return false;
	}

	public void setCallbackForApportion(Boolean b) {
		
	}

	public BigDecimal getLPPPercentage() {
		return BigDecimal.ZERO;
	}

	public LPPenaltyCalcType getLPPenaltyCalcType() {
		return null;
	}

	public void setPenaltyCalcType(LPPenaltyCalcType penaltyType) {

	}

	public BigDecimal calcLPPenaltyForPeriod(Date fromDate, Date toDate, BigDecimal amount) {
		return null;
	}

	public BigDecimal calcPanalty(Date latestCollectedRcptDate, Date fromDate, BigDecimal amount) {
		return null;
	}
	
}
