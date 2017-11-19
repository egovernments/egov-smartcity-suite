/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.assets.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.ObjectMapper;
import org.egov.assets.model.bean.AssetCustomPropertyBean;
import org.egov.assets.persistence.StringJsonUserType;
import org.egov.assets.util.AssetConstants;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.validator.constraints.Length;

@TypeDefs({ @TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class) })

@Entity
@Table(name = "EGASSET_ASSET")
@SequenceGenerator(name = Asset.SEQ, sequenceName = Asset.SEQ, allocationSize = 1)
@Unique(fields = { "code" }, id = "id", tableName = "EGASSET_ASSET", columnName = { "CODE" }, message = "asset.code.isunique")
public class Asset extends AbstractAuditable {

	// Constructors

	private static final long serialVersionUID = 730236511745178022L;

	public enum ModeOfAcquisition {

		ACQUIRED, CONSTRUCTION, PURCHASE, TENDER;
	}

	public static final String WORKFLOWTYPES_QRY = "ParentChildCategories";

	public static final String SEQ = "seq_egasset_asset";

	@Id
	@GeneratedValue(generator = Asset.SEQ, strategy = GenerationType.SEQUENCE)
	private Long id;

	// Fields
	//	@Required(message = "asset.code.null")
	@Length(max = 50, message = "asset.code.length")
	//	@NotNull
	@OptionalPattern(regex = AssetConstants.alphaNumericwithspecialchar, message = "asset.code.alphaNumericwithspecialchar")
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Required(message = "asset.name.null")
	@Length(max = 256, message = "asset.name.length")
	@OptionalPattern(regex = AssetConstants.alphaNumericwithspecialchar, message = "asset.name.alphaNumericwithspecialchar")
	@NotNull
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSETCATEGORY_ID")
	@Required(message = "asset.category.null")
	private AssetCategory assetCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENTID")
	private Department department;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STREET_ID")
	private Boundary street;

	@Column(name="asset_details")
	private String assetDetails;

	@Enumerated(EnumType.STRING)
	@Required(message = "asset.modeofacqui.null")
	@NotNull
	private ModeOfAcquisition modeOfAcquisition;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUSID")
	@Required(message = "asset.status.null")
	private EgwStatus status;

	@Length(max = 256)
	private String description;

	@Column(name="date_of_creation")
	private Date dateOfCreation;

	@Length(max = 1024)
	private String remarks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PREPAREDBY")
	private PersonalInformation preparedBy;

	private BigDecimal grossValue;
	private BigDecimal accDepreciation;
	private BigDecimal length;
	private BigDecimal width;

	@Column(name="total_area")
	private BigDecimal totalArea;

	@Length(max = 150)
	private String sourcePath;

	@Type(type="StringJsonObject")
	private String properties;

	@Transient
	private List<AssetCustomPropertyBean> categoryProperties;

	@Transient
	private List<CategoryPropertyType> categoryPropertyTypeList;

	@Transient
	private List<Long> searchStatus;

	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "locationDetails_Id", nullable = false)
	private LocationDetails locationDetails;


	public List<ValidationError> validate() {
		final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if (dateOfCreation != null
				&& !DateUtils.compareDates(new Date(), dateOfCreation))
			validationErrors.add(new ValidationError("dateOfCreation",
					"asset.dateOfCreation.invalid"));

		return validationErrors;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public ModeOfAcquisition getModeOfAcquisition() {
		return modeOfAcquisition;
	}

	public void setModeOfAcquisition(final ModeOfAcquisition modeOfAcquisition) {
		this.modeOfAcquisition = modeOfAcquisition;
	}

	public String getAssetDetails() {
		return assetDetails;
	}

	public void setAssetDetails(final String assetDetails) {
		this.assetDetails = assetDetails;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}

	public AssetCategory getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(final AssetCategory assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Boundary getStreet() {
		return street;
	}

	public void setStreet(final Boundary street) {
		this.street = street;
	}

	public EgwStatus getStatus() {
		return status;
	}

	public void setStatus(final EgwStatus status) {
		this.status = status;
	}

	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(final Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public PersonalInformation getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(final PersonalInformation preparedBy) {
		this.preparedBy = preparedBy;
	}

	public BigDecimal getGrossValue() {
		return grossValue;
	}

	public void setGrossValue(final BigDecimal grossValue) {
		this.grossValue = grossValue;
	}

	public BigDecimal getAccDepreciation() {
		return accDepreciation;
	}

	public void setAccDepreciation(final BigDecimal accDepreciation) {
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

	public void setLength(final BigDecimal length) {
		this.length = length;
	}

	public void setWidth(final BigDecimal width) {
		this.width = width;
	}

	public void setTotalArea(final BigDecimal totalArea) {
		this.totalArea = totalArea;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(final String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public List<CategoryPropertyType> getCategoryPropertyTypeList() {
		//		List<CategoryPropertyType> categoryList = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			if(this.properties != null)
			{
				List<CategoryPropertyType> categoryList = objectMapper.readValue(this.properties,
						objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryPropertyType.class));
				return categoryList;
			}
			
		} catch (IOException e) {


		}
		return null;
	}

	public void setCategoryPropertyTypeList(
			List<CategoryPropertyType> categoryPropertyTypeList) {
		this.categoryPropertyTypeList = categoryPropertyTypeList;
	}

	public List<AssetCustomPropertyBean> getCategoryProperties() {
		return categoryProperties;
	}

	public void setCategoryProperties(
			List<AssetCustomPropertyBean> categoryProperties) {
		this.categoryProperties = categoryProperties;
	}

	public LocationDetails getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}	

	public List<Long> getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(List<Long> searchStatus) {
		this.searchStatus = searchStatus;
	}
}