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
package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.Length;
import org.egov.tradelicense.domain.entity.NatureOfBusiness;


/**
 * The Class TradeSubCategory.
 */
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
		return category;
	}

	public String getCode() {
		return code;
	}

	public String getFeeBasedOn() {
		return feeBasedOn;
	}

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public String getName() {
		return name;
	}

	public NatureOfBusiness getNatureOfBusiness() {
		return natureOfBusiness;
	}

	public Schedule getScheduleMaster() {
		return scheduleMaster;
	}

	public String getSectionApplicable() {
		return this.sectionApplicable;
	}

	public boolean isApprovalrequired() {
		return approvalrequired;
	}

	public boolean isPfaApplicable() {
		return pfaApplicable;
	}

	public void setApprovalrequired(boolean approvalrequired) {
		this.approvalrequired = approvalrequired;
	}

	public void setCategory(LicenseCategory category) {
		this.category = category;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setFeeBasedOn(String feeBasedOn) {
		this.feeBasedOn = feeBasedOn;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNatureOfBusiness(NatureOfBusiness natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	public void setPfaApplicable(boolean pfaApplicable) {
		this.pfaApplicable = pfaApplicable;
	}

	public void setScheduleMaster(Schedule scheduleMaster) {
		this.scheduleMaster = scheduleMaster;
	}

	public void setSectionApplicable(String sectionApplicable) {
		this.sectionApplicable = sectionApplicable;
	}

	public LicenseSubType getLicenseSubType() {
		return licenseSubType;
	}

	public void setLicenseSubType(LicenseSubType licenseSubType) {
		this.licenseSubType = licenseSubType;
	}
	
	public Boolean isNocApplicable() {
		return nocApplicable;
	}
	
	public void setNocApplicable(Boolean nocApplicable) {
		this.nocApplicable = nocApplicable;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("SubCategory={");
		str.append("  name=").append(name == null ? "null" : name.toString());
		str.append("  code=").append(code == null ? "null" : code.toString());
		str.append("  scheduleMaster=").append(scheduleMaster == null ? "null" : scheduleMaster.toString());
		str.append("  natureOfBusiness=").append(natureOfBusiness == null ? "null" : natureOfBusiness.toString());
		str.append("  category=").append(category == null ? "null" : category.toString());
		str.append("  pfaApplicable=").append(pfaApplicable);
		str.append("  approvalrequired=").append(approvalrequired);
		str.append("  licenseType=").append(licenseType == null ? "null" : licenseType.toString());
		str.append("  feeBasedOn=").append(feeBasedOn == null ? "null" : feeBasedOn.toString());
		str.append("  sectionApplicable=").append(sectionApplicable == null ? "null" : sectionApplicable.toString());
		str.append("  licenseSubType=").append(licenseSubType == null ? "null" : licenseSubType.toString());
		str.append("  nocApplicable=").append(nocApplicable);
		str.append("}");
		return str.toString();
	}

}
