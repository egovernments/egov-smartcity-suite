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

package org.egov.adtax.entity.es;

import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

import java.math.BigDecimal;
import java.util.Date;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import com.fasterxml.jackson.annotation.JsonFormat;
@Document(indexName = "adtax", type = "adtax")
public class AdvertisementIndex {
	@Id
	private String id;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String ward;
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String locality;
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String block;
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String electionWard;
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String street;
	@JsonFormat (shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT)
	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String advertisementNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String assessmentNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String propertyType;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String category;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String subCategory;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String advertisementClass;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String revenueInspector;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String address;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String advertisementCreatedBy;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String advertisement_status;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String type;
    @Field(type = FieldType.Boolean)
    private Boolean islegacy;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String electricityServiceNumber;
 
    @JsonFormat (shape = JsonFormat.Shape.STRING,  pattern = ES_DATE_FORMAT)
	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time,  pattern = ES_DATE_FORMAT)
    private Date applicationDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String consumerNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String permissionNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String permitStatus;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String advertisement_duration;
    
    @Field(type = FieldType.Double)
    private BigDecimal taxAmount;
    @Field(type = FieldType.Double)
    private BigDecimal encroachmentFee;
    
    
    @JsonFormat (shape = JsonFormat.Shape.STRING,  pattern = ES_DATE_FORMAT)
  	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time,  pattern = ES_DATE_FORMAT)
    private Date permissionStartDate;
    @JsonFormat (shape = JsonFormat.Shape.STRING,  pattern = ES_DATE_FORMAT)
  	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time,  pattern = ES_DATE_FORMAT)
    private Date permissionEndDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String mobileNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String agencyName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String advertiser;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String advertiserParticular;
    @Field(type = FieldType.String, index = FieldIndex.analyzed)
    private String ownerDetail;
    @Field(type = FieldType.Double)
    private Double measurement;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String uom;
    @Field(type = FieldType.Double)
     private Double length;
    @Field(type = FieldType.Double)
    private Double width;
    @Field(type = FieldType.Double)
    private Double breadth;
    @Field(type = FieldType.Double)
    private Double totalHeight;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String deactivationRemarks;
    @JsonFormat (shape = JsonFormat.Shape.STRING,  pattern = ES_DATE_FORMAT)
  	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time,  pattern = ES_DATE_FORMAT)
    private Date deactivationDate;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbGrade;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ulbCode;
    
    @Field(type = FieldType.Double)
    private BigDecimal tax_demand;
    @Field(type = FieldType.Double)
     private BigDecimal tax_collected;
    @Field(type = FieldType.Double)
     private BigDecimal encroachmentfee_demand;
    @Field(type = FieldType.Double)
     private BigDecimal encroachmentfee_collected;
    @Field(type = FieldType.Double)
    private BigDecimal arrears_demand;
    @Field(type = FieldType.Double)
     private BigDecimal arrears_collected;
    @Field(type = FieldType.Double)
     private BigDecimal penalty_demand;
    @Field(type = FieldType.Double)
     private BigDecimal penalty_collected;
    @Field(type = FieldType.Double)
     private BigDecimal totalamount;
    @Field(type = FieldType.Double)
     private BigDecimal totalamountcollected;
    @GeoPointField
    private GeoPoint advertisementLocation;
    
    private BigDecimal totalbalance;
    
    @Field(type = FieldType.String, index = FieldIndex.analyzed)
     private String consumerName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerName_Clauses;
    @Field(type = FieldType.String, index = FieldIndex.analyzed)
    private String agencyName_Clauses;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationNumber;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String permissionNumber_Clauses;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String advertisementNumber_Clauses;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String source;
	
	public AdvertisementIndex(final String advertisementNumber,
			final String ulbName, final String ulbCode, final Date createdDate,
			final String districtName, final String regionName,
			final String ulbGrade) {
		this.advertisementNumber = advertisementNumber;
		this.ulbName = ulbName;
		this.createdDate = createdDate;
		this.districtName = districtName;
		this.regionName = regionName;
		this.ulbGrade = ulbGrade;
		this.ulbCode = ulbCode;
}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getElectionWard() {
		return electionWard;
	}
	public void setElectionWard(String electionWard) {
		this.electionWard = electionWard;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getAdvertisementNumber() {
		return advertisementNumber;
	}
	public void setAdvertisementNumber(String advertisementNumber) {
		this.advertisementNumber = advertisementNumber;
	}
	public String getAssessmentNumber() {
		return assessmentNumber;
	}
	public void setAssessmentNumber(String assessmentNumber) {
		this.assessmentNumber = assessmentNumber;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getAdvertisementClass() {
		return advertisementClass;
	}
	public void setAdvertisementClass(String advertisementClass) {
		this.advertisementClass = advertisementClass;
	}
	public String getRevenueInspector() {
		return revenueInspector;
	}
	public void setRevenueInspector(String revenueInspector) {
		this.revenueInspector = revenueInspector;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAdvertisementCreatedBy() {
		return advertisementCreatedBy;
	}
	public void setAdvertisementCreatedBy(String advertisementCreatedBy) {
		this.advertisementCreatedBy = advertisementCreatedBy;
	}
	public String getAdvertisement_status() {
		return advertisement_status;
	}
	public void setAdvertisement_status(String advertisement_status) {
		this.advertisement_status = advertisement_status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIslegacy() {
		return islegacy;
	}
	public void setIslegacy(Boolean islegacy) {
		this.islegacy = islegacy;
	}
	public String getElectricityServiceNumber() {
		return electricityServiceNumber;
	}
	public void setElectricityServiceNumber(String electricityServiceNumber) {
		this.electricityServiceNumber = electricityServiceNumber;
	}
	public Date getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getConsumerNumber() {
		return consumerNumber;
	}
	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}
	public String getPermissionNumber() {
		return permissionNumber;
	}
	public void setPermissionNumber(String permissionNumber) {
		this.permissionNumber = permissionNumber;
	}
	public String getPermitStatus() {
		return permitStatus;
	}
	public void setPermitStatus(String permitStatus) {
		this.permitStatus = permitStatus;
	}
	public String getAdvertisement_duration() {
		return advertisement_duration;
	}
	public void setAdvertisement_duration(String advertisement_duration) {
		this.advertisement_duration = advertisement_duration;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getEncroachmentFee() {
		return encroachmentFee;
	}
	public void setEncroachmentFee(BigDecimal encroachmentFee) {
		this.encroachmentFee = encroachmentFee;
	}
	public Date getPermissionStartDate() {
		return permissionStartDate;
	}
	public void setPermissionStartDate(Date permissionStartDate) {
		this.permissionStartDate = permissionStartDate;
	}
	public Date getPermissionEndDate() {
		return permissionEndDate;
	}
	public void setPermissionEndDate(Date permissionEndDate) {
		this.permissionEndDate = permissionEndDate;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getAdvertiser() {
		return advertiser;
	}
	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}
	public String getAdvertiserParticular() {
		return advertiserParticular;
	}
	public void setAdvertiserParticular(String advertiserParticular) {
		this.advertiserParticular = advertiserParticular;
	}
	public String getOwnerDetail() {
		return ownerDetail;
	}
	public void setOwnerDetail(String ownerDetail) {
		this.ownerDetail = ownerDetail;
	}
	public Double getMeasurement() {
		return measurement;
	}
	public void setMeasurement(Double measurement) {
		this.measurement = measurement;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getBreadth() {
		return breadth;
	}
	public void setBreadth(Double breadth) {
		this.breadth = breadth;
	}
	public Double getTotalHeight() {
		return totalHeight;
	}
	public void setTotalHeight(Double totalHeight) {
		this.totalHeight = totalHeight;
	}
	public String getDeactivationRemarks() {
		return deactivationRemarks;
	}
	public void setDeactivationRemarks(String deactivationRemarks) {
		this.deactivationRemarks = deactivationRemarks;
	}
	public Date getDeactivationDate() {
		return deactivationDate;
	}
	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}
	public String getUlbName() {
		return ulbName;
	}
	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getUlbGrade() {
		return ulbGrade;
	}
	public void setUlbGrade(String ulbGrade) {
		this.ulbGrade = ulbGrade;
	}
	public String getUlbCode() {
		return ulbCode;
	}
	public void setUlbCode(String ulbCode) {
		this.ulbCode = ulbCode;
	}
	public BigDecimal getTax_demand() {
		return tax_demand;
	}
	public void setTax_demand(BigDecimal tax_demand) {
		this.tax_demand = tax_demand;
	}
	public BigDecimal getTax_collected() {
		return tax_collected;
	}
	public void setTax_collected(BigDecimal tax_collected) {
		this.tax_collected = tax_collected;
	}
	public BigDecimal getEncroachmentfee_demand() {
		return encroachmentfee_demand;
	}
	public void setEncroachmentfee_demand(BigDecimal encroachmentfee_demand) {
		this.encroachmentfee_demand = encroachmentfee_demand;
	}
	public BigDecimal getEncroachmentfee_collected() {
		return encroachmentfee_collected;
	}
	public void setEncroachmentfee_collected(BigDecimal encroachmentfee_collected) {
		this.encroachmentfee_collected = encroachmentfee_collected;
	}
	public BigDecimal getArrears_demand() {
		return arrears_demand;
	}
	public void setArrears_demand(BigDecimal arrears_demand) {
		this.arrears_demand = arrears_demand;
	}
	public BigDecimal getArrears_collected() {
		return arrears_collected;
	}
	public void setArrears_collected(BigDecimal arrears_collected) {
		this.arrears_collected = arrears_collected;
	}
	public BigDecimal getPenalty_demand() {
		return penalty_demand;
	}
	public void setPenalty_demand(BigDecimal penalty_demand) {
		this.penalty_demand = penalty_demand;
	}
	public BigDecimal getPenalty_collected() {
		return penalty_collected;
	}
	public void setPenalty_collected(BigDecimal penalty_collected) {
		this.penalty_collected = penalty_collected;
	}
	public BigDecimal getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}
	public BigDecimal getTotalamountcollected() {
		return totalamountcollected;
	}
	public void setTotalamountcollected(BigDecimal totalamountcollected) {
		this.totalamountcollected = totalamountcollected;
	}
	public GeoPoint getAdvertisementLocation() {
		return advertisementLocation;
	}
	public void setAdvertisementLocation(GeoPoint advertisementLocation) {
		this.advertisementLocation = advertisementLocation;
	}
	public BigDecimal getTotalbalance() {
		return totalbalance;
	}
	public void setTotalbalance(BigDecimal totalbalance) {
		this.totalbalance = totalbalance;
	}
	public String getConsumerName() {
		return consumerName;
	}
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
	public String getConsumerName_Clauses() {
		return consumerName_Clauses;
	}
	public void setConsumerName_Clauses(String consumerName_Clauses) {
		this.consumerName_Clauses = consumerName_Clauses;
	}
	public String getAgencyName_Clauses() {
		return agencyName_Clauses;
	}
	public void setAgencyName_Clauses(String agencyName_Clauses) {
		this.agencyName_Clauses = agencyName_Clauses;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getPermissionNumber_Clauses() {
		return permissionNumber_Clauses;
	}
	public void setPermissionNumber_Clauses(String permissionNumber_Clauses) {
		this.permissionNumber_Clauses = permissionNumber_Clauses;
	}
	public String getAdvertisementNumber_Clauses() {
		return advertisementNumber_Clauses;
	}
	public void setAdvertisementNumber_Clauses(String advertisementNumber_Clauses) {
		this.advertisementNumber_Clauses = advertisementNumber_Clauses;
	}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
