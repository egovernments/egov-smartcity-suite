package org.egov.assets.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.assets.util.AssetConstants;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.validator.constraints.Length;

/**
 * Asset entity.
 *
 * @author Nilesh
 */
@Unique(fields={"code"},id="id",tableName="EG_ASSET",columnName={"CODE"},message="asset.code.isunique")
public class Asset extends BaseModel {

	// Constructors

	/** default constructor */
	public Asset() {
	}

	public Asset(String code) {
		this.code = code;
	}

	/** minimal constructor */
	public Asset(String code, String name) {
		this.code = code;
		this.name = name;
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
	private String address;
	private String landmark;

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
	private List<AssetSale> assetSales = new LinkedList<AssetSale>();
	private List<Depreciation> depreciations = new LinkedList<Depreciation>();
	private List<AssetValueChange> assetValueChanges = new LinkedList<AssetValueChange>();

	public List<ValidationError> validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(dateOfCreation!=null && !DateUtils.compareDates(new Date(),dateOfCreation))
			validationErrors.add(new ValidationError("dateOfCreation", "asset.dateOfCreation.invalid"));

		return validationErrors;
	}
	public Boundary getWard() {
		return ward;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
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

	public List<AssetSale> getAssetSales() {
		return assetSales;
	}

	public void setAssetSales(List<AssetSale> assetSales) {
		this.assetSales = assetSales;
	}

	public List<Depreciation> getDepreciations() {
		return depreciations;
	}

	public void setDepreciations(List<Depreciation> depreciations) {
		this.depreciations = depreciations;
	}

	public List<AssetValueChange> getAssetValueChanges() {
		return assetValueChanges;
	}

	public void setAssetValueChanges(List<AssetValueChange> assetValueChanges) {
		this.assetValueChanges = assetValueChanges;
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

}

