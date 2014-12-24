/*
 * @(#)CancelLicenseAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.cancellation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatusValues;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.SubCategory;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Result(name = Action.SUCCESS, type = "redirect", location = "CancelLicense.action")
@ParentPackage("egov")
public class CancelLicenseAction extends BaseFormAction {
	private static final Logger LOGGER = Logger.getLogger(CancelLicenseAction.class);

	protected LicenseUtils licenseUtils;
	private Integer reasonForCancellation;
	private String refernceno;
	private Date commdateApp;
	private String cancelInforemarks;
	private Map reasonMap;
	private AuditEventService auditEventService;
	protected WorkflowBean workflowBean = new WorkflowBean();

	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	/**
	 * @param ts the ts to set
	 */
	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	/**
	 * @return the licenseId
	 */
	public Integer getLicenseId() {
		return this.licenseId;
	}

	/**
	 * @param licenseId the licenseId to set
	 */
	public void setLicenseId(final Integer licenseId) {
		this.licenseId = licenseId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return CancelLicenseAction.serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
	private TradeService ts;
	private Integer licenseId;
	private License license = new TradeLicense();

	// searchForm

	/**
	 * @return the license
	 */
	public License getLicense() {
		return this.license;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(final License license) {
		this.license = license;
	}

	@SkipValidation
	public String newForm() {
		this.license = this.ts.getTps().findById(this.licenseId.longValue(), false);
		return Constants.NEW;
	}

	public CancelLicenseAction() {
		this.addRelatedEntity("boundary", Boundary.class);
		this.addRelatedEntity("address", Address.class);
		this.addRelatedEntity("licensee", Licensee.class);
		this.addRelatedEntity("licensee.address", Address.class);
		this.addRelatedEntity("tradeName", SubCategory.class);

	}

	@Override
	public void prepare() {
		LOGGER.debug("Entering in the prepare method:<<<<<<<<<<>>>>>>>>>>>>>:");
		super.prepare();
		final List<Boundary> areaList = new ArrayList<Boundary>();
		this.addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, areaList);
		// Fetch Ward Dropdown List
		final List<Boundary> divisionList = new ArrayList<Boundary>();
		this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, divisionList);
		// Fetch Zone Dropdown List
		this.addDropdownData(Constants.DROPDOWN_ZONE_LIST, this.licenseUtils.getAllZone());
		this.addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, this.licenseUtils.getAllTradeNames("TradeLicense"));
		this.license = this.ts.getTps().findById(this.licenseId.longValue(), false);
		// find(query,licenseId);
		LOGGER.debug("Exiting from the prepare method:<<<<<<<<<<>>>>>>>>>>>>>:");
	}

	@ValidationErrorPage(Constants.NEW)
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "reasonForCancellation", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "refernceno", message = "", key = Constants.REQUIRED) })
	public String confirmCancellation() {
		LOGGER.debug("Cancel Trade License Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		this.license.setActive(false);
		this.license.setStatus(this.licenseUtils.getLicenseStatusbyCode("CAN"));
		final LicenseStatusValues licenseStatusValues = this.licenseUtils.getCurrentStatus(this.license);
		if (licenseStatusValues != null) {
			licenseStatusValues.setActive(false);

		}
		final LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
		newLicenseStatusValues.setLicense(this.license);
		newLicenseStatusValues.setLicenseStatus(this.licenseUtils.getLicenseStatusbyCode("CAN"));
		newLicenseStatusValues.setReason(Integer.valueOf(this.reasonForCancellation));

		newLicenseStatusValues.setReferenceNo(this.refernceno);
		newLicenseStatusValues.setReferenceDate(this.commdateApp);
		newLicenseStatusValues.setRemarks(this.cancelInforemarks);
		newLicenseStatusValues.setActive(true);
		newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);
		this.license.addLicenseStatusValuesSet(newLicenseStatusValues);
		this.ts.getTps().update((TradeLicense) this.license);
		LOGGER.debug("Cancel Trade License Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:" + this.license.getNameOfEstablishment());
		doAuditing("Cancel License", this.getCancellationDetails());
		return Constants.CANCEL_Result_MSG_PAGE;
	}

	/**
	 * @return the reasonForCancellation
	 */
	public Integer getReasonForCancellation() {
		return this.reasonForCancellation;
	}

	/**
	 * @param reasonForCancellation the reasonForCancellation to set
	 */
	public void setReasonForCancellation(final Integer reasonForCancellation) {
		this.reasonForCancellation = reasonForCancellation;
	}

	/**
	 * @return the refernceno
	 */
	public String getRefernceno() {
		return this.refernceno;
	}

	/**
	 * @param refernceno the refernceno to set
	 */
	public void setRefernceno(final String refernceno) {
		this.refernceno = refernceno;
	}

	/**
	 * @return the commdateApp
	 */
	public Date getCommdateApp() {
		return this.commdateApp;
	}

	/**
	 * @param commdateApp the commdateApp to set
	 */
	public void setCommdateApp(final Date commdateApp) {
		this.commdateApp = commdateApp;
	}

	/**
	 * @return the cancelInforemarks
	 */
	public String getCancelInforemarks() {
		return this.cancelInforemarks;
	}

	/**
	 * @param cancelInforemarks the cancelInforemarks to set
	 */
	public void setCancelInforemarks(final String cancelInforemarks) {
		this.cancelInforemarks = cancelInforemarks;
	}

	@Override
	public Object getModel() {
		return this.license;
	}

	public void setModel(final License license) {
		this.license = license;
	}

	/**
	 * @return the reasonMap
	 */
	public Map getReasonMap() {

		this.reasonMap = this.licenseUtils.getCancellationReasonMap();
		return this.reasonMap;
	}

	/**
	 * @param reasonMap the reasonMap to set
	 */
	public void setReasonMap(final Map reasonMap) {
		this.reasonMap = this.licenseUtils.getCancellationReasonMap();
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("CancelLicenseAction={");
		str.append("  licenseUtils=").append(this.licenseUtils == null ? "null" : this.licenseUtils.toString());
		str.append("  reasonForCancellation=").append(this.reasonForCancellation == null ? "null" : this.reasonForCancellation.toString());
		str.append("  refernceno=").append(this.refernceno == null ? "null" : this.refernceno.toString());
		str.append("  commdateApp=").append(this.commdateApp == null ? "null" : this.commdateApp.toString());
		str.append("  cancelInforemarks=").append(this.cancelInforemarks == null ? "null" : this.cancelInforemarks.toString());
		str.append("  reasonMap=").append(this.reasonMap == null ? "null" : this.reasonMap.toString());
		str.append("  licenseId=").append(this.licenseId == null ? "null" : this.licenseId.toString());
		str.append("  license=").append(this.license == null ? "null" : this.license.toString());
		str.append("  ts=").append(this.ts == null ? "null" : this.ts.toString());
		return str.toString();
	}

	public void setAuditEventService(final AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	protected void doAuditing(final String action, final String details) {
		final License license = (License) this.getModel();
		final AuditEvent auditEvent = new AuditEvent(AuditModule.HPL, AuditEntity.HPL_LIC, action, this.license.getLicenseNumber(), details);
		auditEvent.setPkId(license.getId());
		auditEvent.setDetails2(this.workflowBean.getActionName() == null ? "" : this.workflowBean.getActionName());
		this.auditEventService.createAuditEvent(auditEvent, this.license.getClass());
	}

	@SkipValidation
	public String auditReport() {
		return "auditReport";
	}

	public String getCancellationDetails() {
		return new StringBuffer("[Reason for Cancellation : ").append(this.getReasonForCancellation()).append(", Reference number : ").append(this.getRefernceno()).append(", Reference date : ").append(this.getCommdateApp()).append(" ]").toString();

	}

}
