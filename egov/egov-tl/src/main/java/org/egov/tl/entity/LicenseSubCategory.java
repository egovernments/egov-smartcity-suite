/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class TradeSubCategory.
 */
@Unique(fields = { "code" }, id = "id", tableName = "EGTL_MSTR_SUB_CATEGORY", columnName = { "code" }, message = "masters.code.isunique")
public class LicenseSubCategory extends AbstractAuditable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private LicenseCategory category;
    @Required(message = "tradelic.master.tradesubcategorycode.null")
    @Length(max = 32, message = "tradelic.master.tradesubcategorycode.length")  
    private String code;
    
    @Required(message = "tradelic.master.tradesubcategoryname.null")
    @Length(max = 256, message = "tradelic.master.tradesubcategoryname.length")
    private String name;
    
    private boolean approvalrequired;
    
    private String feeBasedOn;
    private LicenseType licenseType;
    private NatureOfBusiness natureOfBusiness;
    private boolean pfaApplicable;
    private Schedule scheduleMaster;
    private String sectionApplicable;
    private LicenseSubType licenseSubType;
    private Boolean nocApplicable;
    
    private List<LicenseSubCategoryDetails> licenseSubCategoryDetails = new ArrayList<LicenseSubCategoryDetails>();

    public LicenseCategory getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

 
    public String getName() {
        return name;
    }


    public void setCategory(final LicenseCategory category) {
        this.category = category;
    }

    public void setCode(final String code) {
        this.code = code;
    }


    public void setName(final String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("LicenseSubCategory={");
        str.append("  name=").append(name == null ? "null" : name.toString());
        str.append("  code=").append(code == null ? "null" : code.toString());
        str.append("  category=").append(category == null ? "null" : category.toString());
        str.append("}");
        return str.toString();
    }

    public boolean isApprovalrequired() {
        return approvalrequired;
    }

    public void setApprovalrequired(boolean approvalrequired) {
        this.approvalrequired = approvalrequired;
    }

    public String getFeeBasedOn() {
        return feeBasedOn;
    }

    public void setFeeBasedOn(String feeBasedOn) {
        this.feeBasedOn = feeBasedOn;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public NatureOfBusiness getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(NatureOfBusiness natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public boolean isPfaApplicable() {
        return pfaApplicable;
    }

    public void setPfaApplicable(boolean pfaApplicable) {
        this.pfaApplicable = pfaApplicable;
    }

    public Schedule getScheduleMaster() {
        return scheduleMaster;
    }

    public void setScheduleMaster(Schedule scheduleMaster) {
        this.scheduleMaster = scheduleMaster;
    }

    public String getSectionApplicable() {
        return sectionApplicable;
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

   
    public void addLicenseSubCategoryDetails(LicenseSubCategoryDetails licenseSubCategoryDetail)
    {                       
        getLicenseSubCategoryDetails().add(licenseSubCategoryDetail); 
    }

    public List<LicenseSubCategoryDetails> getLicenseSubCategoryDetails() {
        return licenseSubCategoryDetails;
    }

    public void setLicenseSubCategoryDetails(List<LicenseSubCategoryDetails> licenseSubCategoryDetails) {
        this.licenseSubCategoryDetails = licenseSubCategoryDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
