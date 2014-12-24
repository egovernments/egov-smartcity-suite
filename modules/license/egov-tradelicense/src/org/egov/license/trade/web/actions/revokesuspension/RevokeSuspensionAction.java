/*
 * @(#)RevokeSuspensionAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.revokesuspension;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseStatusValues;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.SubCategory;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;
import org.egov.license.web.actions.common.BaseLicenseAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

public class RevokeSuspensionAction extends BaseLicenseAction {

	private static final Logger LOGGER = Logger.getLogger(RevokeSuspensionAction.class);

	protected LicenseUtils licenseUtils;
	private Date revokeDate;
	private String revokeRemarks;
	private static final long serialVersionUID = 1L;
	private TradeService ts;
	private Long licenseId;
	private License license = new TradeLicense();

	@Override
	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	public Long getLicenseId() {
		return this.licenseId;
	}

	public void setLicenseId(final Long licenseId) {
		this.licenseId = licenseId;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	@Override
	@SkipValidation
	public String newForm() {
		this.license = (License) this.persistenceService.find("from License where id=?", this.licenseId);
		return Constants.NEW;
	}

	public RevokeSuspensionAction() {
		this.addRelatedEntity("boundary", Boundary.class);
		this.addRelatedEntity("address", Address.class);
		this.addRelatedEntity("licensee", Licensee.class);
		this.addRelatedEntity("licensee.address", Address.class);
		this.addRelatedEntity("tradeName", SubCategory.class);

	}

	@Override
	public void prepare() {
		super.prepare();

	}

	@Override
	public Object getModel() {
		return this.license;
	}

	public void setModel(final License license) {
		this.license = license;
	}

	@SkipValidation
	@SuppressWarnings("unchecked")
	@ValidationErrorPage("revokesuspension")
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "revokeDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "remarks", message = "", key = Constants.REQUIRED) })
	public String confirmRevokeSuspension() {
		LOGGER.debug("Revoke Suspension Action Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + this.toString());
		this.license = (License) this.persistenceService.find("from License where id=?", this.licenseId);
		final LicenseStatusValues licenseStatusValues = this.licenseUtils.getCurrentStatus(this.license);
		if (licenseStatusValues != null) {
			licenseStatusValues.setActive(false);
		}
		final LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
		newLicenseStatusValues.setReferenceDate(this.revokeDate);
		newLicenseStatusValues.setRemarks(this.revokeRemarks);
		newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);
		this.ts.revokeSuspendedLicense(this.license, this.licenseUtils, newLicenseStatusValues);
		this.addActionMessage(this.getText("license.revoke.succesful"));
		LOGGER.debug("Revoke Suspension Action License Number:<<<<<<<<<<>>>>>>>>>>>>>:" + this.license.getLicenseNumber());
		return "message";
	}

	public Date getRevokeDate() {
		return this.revokeDate;
	}

	public void setRevokeDate(final Date revokeDate) {
		this.revokeDate = revokeDate;
	}

	public String getRevokeRemarks() {
		return this.revokeRemarks;
	}

	public void setRevokeRemarks(final String revokeRemarks) {
		this.revokeRemarks = revokeRemarks;
	}

	@Override
	protected License license() {
		return this.license;
	}

	@Override
	protected BaseLicenseService service() {
		return null;
	}

	@SkipValidation
	public String getObjectionReason(final int reasonId) {
		return this.licenseUtils.getObjectionReasons().get(reasonId);
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("RevokeSuspensionAction={");
		str.append("  licenseUtils=").append(this.licenseUtils == null ? "null" : this.licenseUtils.toString());
		str.append("  revokeDate=").append(this.revokeDate == null ? "null" : this.revokeDate.toString());
		str.append("  revokeRemarks=").append(this.revokeRemarks == null ? "null" : this.revokeRemarks.toString());
		str.append("  ts=").append(this.ts == null ? "null" : this.ts.toString());
		str.append("  licenseId=").append(this.licenseId == null ? "null" : this.licenseId.toString());
		str.append("  license=").append(this.license == null ? "null" : this.license.toString());
		return str.toString();
	}
}
