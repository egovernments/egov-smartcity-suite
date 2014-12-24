/*
 * @(#)SubCategory.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "code" }, id = "id", tableName = "EGL_MSTR_SUB_CATEGORY", columnName = { "code" }, message = "masters.code.isunique")
public class SubCategory extends BaseModel {
	private static final long serialVersionUID = 1L;
	private boolean approvalrequired;
	private LicenseCategory category;
	@Required(message = "masters.master.trade.code")
	@Length(max = 32, message = "masters.master.code.length")
	private String code;
	@Required(message = "masters.master.trade.feebasedon")
	@Length(max = 40, message = "masters.master.feebasedon.length")
	private String feeBasedOn;
	private LicenseType licenseType;
	@Required(message = "masters.master.trade.tradename")
	@Length(max = 256, message = "masters.master.tradename.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.tradename.text")
	private String name;
	private NatureOfBusiness natureOfBusiness;
	private boolean pfaApplicable;
	private Schedule scheduleMaster;
	private String sectionApplicable;
	private LicenseSubType licenseSubType;
	private Boolean nocApplicable;

	public SubCategory() {
	}

	public LicenseCategory getCategory() {
		return this.category;
	}

	public String getCode() {
		return this.code;
	}

	public String getFeeBasedOn() {
		return this.feeBasedOn;
	}

	public LicenseType getLicenseType() {
		return this.licenseType;
	}

	public String getName() {
		return this.name;
	}

	public NatureOfBusiness getNatureOfBusiness() {
		return this.natureOfBusiness;
	}

	public Schedule getScheduleMaster() {
		return this.scheduleMaster;
	}

	public String getSectionApplicable() {
		return this.sectionApplicable;
	}

	public boolean isApprovalrequired() {
		return this.approvalrequired;
	}

	public boolean isPfaApplicable() {
		return this.pfaApplicable;
	}

	public void setApprovalrequired(final boolean approvalrequired) {
		this.approvalrequired = approvalrequired;
	}

	public void setCategory(final LicenseCategory category) {
		this.category = category;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public void setFeeBasedOn(final String feeBasedOn) {
		this.feeBasedOn = feeBasedOn;
	}

	public void setLicenseType(final LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNatureOfBusiness(final NatureOfBusiness natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	public void setPfaApplicable(final boolean pfaApplicable) {
		this.pfaApplicable = pfaApplicable;
	}

	public void setScheduleMaster(final Schedule scheduleMaster) {
		this.scheduleMaster = scheduleMaster;
	}

	public void setSectionApplicable(final String sectionApplicable) {
		this.sectionApplicable = sectionApplicable;
	}

	public LicenseSubType getLicenseSubType() {
		return this.licenseSubType;
	}

	public void setLicenseSubType(final LicenseSubType licenseSubType) {
		this.licenseSubType = licenseSubType;
	}

	public Boolean isNocApplicable() {
		return this.nocApplicable;
	}

	public void setNocApplicable(final Boolean nocApplicable) {
		this.nocApplicable = nocApplicable;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("SubCategory={");
		str.append("  name=").append(this.name == null ? "null" : this.name.toString());
		str.append("  code=").append(this.code == null ? "null" : this.code.toString());
		str.append("  scheduleMaster=").append(this.scheduleMaster == null ? "null" : this.scheduleMaster.toString());
		str.append("  natureOfBusiness=").append(this.natureOfBusiness == null ? "null" : this.natureOfBusiness.toString());
		str.append("  category=").append(this.category == null ? "null" : this.category.toString());
		str.append("  pfaApplicable=").append(this.pfaApplicable);
		str.append("  approvalrequired=").append(this.approvalrequired);
		str.append("  licenseType=").append(this.licenseType == null ? "null" : this.licenseType.toString());
		str.append("  feeBasedOn=").append(this.feeBasedOn == null ? "null" : this.feeBasedOn.toString());
		str.append("  sectionApplicable=").append(this.sectionApplicable == null ? "null" : this.sectionApplicable.toString());
		str.append("  licenseSubType=").append(this.licenseSubType == null ? "null" : this.licenseSubType.toString());
		str.append("  nocApplicable=").append(this.nocApplicable);
		str.append("}");
		return str.toString();
	}

}