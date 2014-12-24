/*
 * @(#)LicenseBill.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.service.integration;

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
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseDemand;
import org.egov.license.utils.LicenseUtils;

public class LicenseBill extends AbstractBillable implements LatePayPenaltyCalculator {

	private LicenseUtils licenseUtils;
	private License license;
	private String moduleName;
	private String serviceCode;
	private EgBillDao billDao;

	public void setBillDao(final EgBillDao billDao) {
		this.billDao = billDao;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	@Override
	public Module getModule() {
		return this.licenseUtils.getModule(this.moduleName);
	}

	@Override
	public String getBillPayee() {
		return this.license.getLicensee().getApplicantName();
	}

	@Override
	public String getBillAddress() {
		return this.license.getLicensee().getAddress().toString() + "\nPh : " + defaultString(this.license.getLicensee().getPhoneNumber());
	}

	@Override
	public EgDemand getCurrentDemand() {
		final Set<LicenseDemand> demands = this.license.getDemandSet();
		for (final EgDemand demand : demands) {
			if (demand.getIsHistory().equals("N")) {
				return demand;
			}
		}
		return null;
	}

	@Override
	public List<EgDemand> getAllDemands() {
		return new ArrayList<EgDemand>(this.license.getDemandSet());

	}

	@Override
	public EgBillType getBillType() {
		return this.billDao.getBillTypeByCode("AUTO");

	}

	@Override
	public Date getBillLastDueDate() {
		Date dueDate = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dueDate);
		cal.get(Calendar.MONTH + 1);
		dueDate = cal.getTime();
		return dueDate;

	}

	@Override
	public Integer getBoundaryNum() {
		return this.license.getBoundary().getId();
	}

	@Override
	public String getBoundaryType() {
		return this.license.getBoundary().getBoundaryType().getName();
	}

	@Override
	public String getDepartmentCode() {
		return "CAF";// TODO
	}

	@Override
	public BigDecimal getFunctionaryCode() {
		return BigDecimal.ZERO;
	}

	@Override
	public String getFundCode() {
		return "45061";// TODO Insert
	}

	@Override
	public String getFundSourceCode() {
		return "BOM 637";// TODO
	}

	@Override
	public Date getIssueDate() {
		return new Date();
	}

	@Override
	public Date getLastDate() {
		return this.getBillLastDueDate();
	}

	@Override
	public Boolean getOverrideAccountHeadsAllowed() {
		return false;
	}

	@Override
	public Boolean getPartPaymentAllowed() {
		return false;
	}

	public void setServiceCode(final String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Override
	public String getServiceCode() {
		return this.serviceCode;
	}

	@Override
	public BigDecimal getTotalAmount() {
		return getCurrentDemand().getBaseDemand();
	}

	@Override
	public Long getUserId() {
		return Long.valueOf(EGOVThreadLocals.getUserId());
	}

	@Override
	public String getDescription() {
		return isBlank(this.license.getLicenseNumber()) ? "Application No : " + this.license.getApplicationNumber() : "License No : " + this.license.getLicenseNumber();
	}

	@Override
	public String getDisplayMessage() {
		return this.moduleName + " Collection";
	}

	@Override
	public String getCollModesNotAllowed() {
		return "";
	}

	@Override
	public String getPropertyId() {
		return defaultString(this.license.getLicenseNumber(), this.license.getApplicationNumber());
	}

	@Override
	public Boolean isCallbackForApportion() {
		return false;
	}

	@Override
	public void setCallbackForApportion(final Boolean b) {

	}

	@Override
	public BigDecimal getLPPPercentage() {
		return BigDecimal.ZERO;
	}

	@Override
	public LPPenaltyCalcType getLPPenaltyCalcType() {
		return null;
	}

	@Override
	public void setPenaltyCalcType(final LPPenaltyCalcType penaltyType) {

	}

	@Override
	public BigDecimal calcLPPenaltyForPeriod(final Date fromDate, final Date toDate, final BigDecimal amount) {
		return null;
	}

	@Override
	public BigDecimal calcPanalty(final Date fromDate, final BigDecimal amount) {
		return null;
	}

}
