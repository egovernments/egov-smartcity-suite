/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.asset.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.util.AssetConstants;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.validator.constraints.Length;

@Unique(fields={"code"},id="id",tableName="EGASSET_ASSET",columnName={"CODE"},message="asset.code.isunique")
public class Asset extends BaseModel {

	// Constructors

	/** default constructor */
	public Asset() {
	}

	// Fields
	@Required(message="asset.code.null")
	@Length(max=50,message="asset.code.length")
	@OptionalPattern(regex=AssetConstants.alphaNumericwithspecialchar,message="asset.code.alphaNumericwithspecialchar")
	private String code;
	
	@Required(message="asset.name.null")
	@Length(max=100,message="asset.name.length")
	@OptionalPattern(regex=AssetConstants.alphaNumericwithspecialchar,message="asset.name.alphaNumericwithspecialchar")
	private String name;
	
	@Required(message="asset.category.null")
	private AssetCategory assetCategory;
	
	private Boundary area;
	private Boundary location;
	
	private Boundary street;
	private Boundary ward;
	private String assetDetails;
	
	@Required(message="asset.modeofacqui.null")
	private String modeOfAcquisition;
	@Required(message="asset.status.null")
	private EgwStatus status;
	private String description;
	private Department department;
	// @Required(message="asset.creationdate.null")
	private Date dateOfCreation;

	private String remark;
	private PersonalInformation preparedBy;
	private BigDecimal grossValue;
	private BigDecimal accDepreciation;
	private BigDecimal length;
	private BigDecimal width;
	private BigDecimal totalArea;
	private String sourcePath;
	
	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(dateOfCreation!=null && !DateUtils.compareDates(new Date(),dateOfCreation))
			validationErrors.add(new ValidationError("dateOfCreation", "asset.dateOfCreation.invalid"));
		
		return validationErrors;
	}
	public Boundary getWard() {
		return ward;
	}
	
        public Asset(String code) {
                this.code = code;
        }
        
        /** minimal constructor */
        public Asset(String code, String name) {
                this.code = code;
                this.name = name;
        }

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModeOfAcquisition() {
		return modeOfAcquisition;
	}

	public void setModeOfAcquisition(String modeOfAcquisition) {
		this.modeOfAcquisition = modeOfAcquisition;
	}

	public String getAssetDetails() {
		return assetDetails;
	}

	public void setAssetDetails(String assetDetails) {
		this.assetDetails = assetDetails;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Boundary getArea() {
		return area;
	}

	public void setArea(Boundary area) {
		this.area = area;
	}

	public Boundary getLocation() {
		return location;
	}

	public void setLocation(Boundary location) {
		this.location = location;
	}

	public Boundary getStreet() {
		return street;
	}

	public void setStreet(Boundary street) {
		this.street = street;
	}

	public EgwStatus getStatus() {
		return status;
	}

	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PersonalInformation getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(PersonalInformation preparedBy) {
		this.preparedBy = preparedBy;
	}

	public BigDecimal getGrossValue() {
		return grossValue;
	}

	public void setGrossValue(BigDecimal grossValue) {
		this.grossValue = grossValue;
	}

	public BigDecimal getAccDepreciation() {
		return accDepreciation;
	}

	public void setAccDepreciation(BigDecimal accDepreciation) {
		this.accDepreciation = accDepreciation;
	}

	public BigDecimal getLength() {
		return length;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public BigDecimal getTotalArea() {
		return totalArea;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public void setTotalArea(BigDecimal totalArea) {
		this.totalArea = totalArea;
	}
	
	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
}
