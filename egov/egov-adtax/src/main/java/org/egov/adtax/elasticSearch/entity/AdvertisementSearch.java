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
package org.egov.adtax.elasticSearch.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infra.search.elastic.Indexable;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.search.domain.Searchable;
import org.elasticsearch.common.geo.GeoPoint;

public class AdvertisementSearch implements Indexable {

	@Override
	public String getIndexId() {
		return EgovThreadLocals.getCityCode() + "-" + applicationNumber;
	}

	@Searchable(name = "zone", group = Searchable.Group.CLAUSES)
	private String zone;

	@Searchable(name = "ward", group = Searchable.Group.CLAUSES)
	private String ward;

	@Searchable(name = "locality", group = Searchable.Group.SEARCHABLE)
	private String locality;

	@Searchable(name = "block", group = Searchable.Group.CLAUSES)
	private String block;

	@Searchable(name = "electionward", group = Searchable.Group.CLAUSES)
	private String electionWard;

	@Searchable(name = "street", group = Searchable.Group.CLAUSES)
	private String street;

	@Searchable(name = "createdDate", group = Searchable.Group.COMMON)
	private Date createdDate;

	@Searchable(name = "advertisementnumber", group = Searchable.Group.SEARCHABLE)
	private String advertisementNumber;

	@Searchable(name = "assessmentnumber", group = Searchable.Group.CLAUSES)
	private String assessmentNumber;

	@Searchable(name = "propertytype", group = Searchable.Group.CLAUSES)
	private String propertyType;

	@Searchable(name = "category", group = Searchable.Group.CLAUSES)
	private String category;

	@Searchable(name = "subcategory", group = Searchable.Group.CLAUSES)
	private String subCategory;

	@Searchable(name = "class", group = Searchable.Group.CLAUSES)
	private String advertisementClass;

	@Searchable(name = "revenueinspector", group = Searchable.Group.CLAUSES)
	private String revenueInspector;

	@Searchable(name = "address", group = Searchable.Group.SEARCHABLE)
	private String address;

	@Searchable(name = "advertisementcreatedby", group = Searchable.Group.CLAUSES)
	private String advertisementCreatedBy;

	@Searchable(name = "status", group = Searchable.Group.CLAUSES)
	private String advertisement_status;

	@Searchable(name = "type", group = Searchable.Group.CLAUSES)
	private String type;

	@Searchable(name = "islegacy", group = Searchable.Group.CLAUSES)
	private Boolean islegacy;

	@Searchable(name = "electricityservicenumber", group = Searchable.Group.CLAUSES)
	private String electricityServiceNumber;

	@Searchable(name = "applicationdate", group = Searchable.Group.CLAUSES)
	private Date applicationDate;

	@Searchable(name = "applicationnumber", group = Searchable.Group.SEARCHABLE)
	private String applicationNumber;

	@Searchable(name = "permissionnumber", group = Searchable.Group.SEARCHABLE)
	private String permissionNumber;

	@Searchable(name = "permitstatus", group = Searchable.Group.CLAUSES)
	private String permitStatus;

	@Searchable(name = "advertisementduration", group = Searchable.Group.CLAUSES)
	private String advertisement_duration;

	@Searchable(name = "taxamount", group = Searchable.Group.CLAUSES)
	private BigDecimal taxAmount;

	@Searchable(name = "encroachmentfee", group = Searchable.Group.CLAUSES)
	private BigDecimal encroachmentFee;

	@Searchable(name = "permissionstartdate", group = Searchable.Group.CLAUSES)
	private Date permissionStartDate;

	@Searchable(name = "permissionenddate", group = Searchable.Group.CLAUSES)
	private Date permissionEndDate;

	@Searchable(name = "mobilenumber", group = Searchable.Group.CLAUSES)
	private String mobileNumber;

	@Searchable(name = "agencyname", group = Searchable.Group.SEARCHABLE)
	private String agencyName;

	@Searchable(name = "advertiser", group = Searchable.Group.CLAUSES)
	private String advertiser;

	@Searchable(name = "advertiserparticular", group = Searchable.Group.CLAUSES)
	private String advertiserParticular;

	@Searchable(name = "ownerdetail", group = Searchable.Group.SEARCHABLE)
	private String ownerDetail;

	@Searchable(name = "measurement", group = Searchable.Group.CLAUSES)
	private Double measurement;

	@Searchable(name = "unitofmeasure", group = Searchable.Group.CLAUSES)
	private String uom;

	@Searchable(name = "length", group = Searchable.Group.CLAUSES)
	private Double length;

	@Searchable(name = "width", group = Searchable.Group.CLAUSES)
	private Double width;

	@Searchable(name = "breadth", group = Searchable.Group.CLAUSES)
	private Double breadth;

	@Searchable(name = "totalheight", group = Searchable.Group.CLAUSES)
	private Double totalHeight;

	@Searchable(name = "deactivationremarks", group = Searchable.Group.CLAUSES)
	private String deactivationRemarks;

	@Searchable(name = " deactivationdate", group = Searchable.Group.CLAUSES)
	private Date deactivationDate;

	@Searchable(name = "ulbname", group = Searchable.Group.CLAUSES)
	private String ulbName;

	@Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
	private String districtName;

	@Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
	private String regionName;

	@Searchable(name = "ulbgrade", group = Searchable.Group.CLAUSES)
	private String ulbGrade;

	@Searchable(name = "ulbcode", group = Searchable.Group.CLAUSES)
	private String ulbCode;

	@Searchable(name = "tax_demand", group = Searchable.Group.CLAUSES)
	private BigDecimal tax_demand;

	@Searchable(name = "tax_collected", group = Searchable.Group.CLAUSES)
	private BigDecimal tax_collected;

	@Searchable(name = "encroachmentfee_demand", group = Searchable.Group.CLAUSES)
	private BigDecimal encroachmentfee_demand;

	@Searchable(name = "encroachmentfee_collected", group = Searchable.Group.CLAUSES)
	private BigDecimal encroachmentfee_collected;

	@Searchable(name = "arrears_demand", group = Searchable.Group.CLAUSES)
	private BigDecimal arrears_demand;

	@Searchable(name = "arrears_collected", group = Searchable.Group.CLAUSES)
	private BigDecimal arrears_collected;

	@Searchable(name = "penalty_demand", group = Searchable.Group.CLAUSES)
	private BigDecimal penalty_demand;

	@Searchable(name = "penalty_collected", group = Searchable.Group.CLAUSES)
	private BigDecimal penalty_collected;

	@Searchable(name = "totalamount", group = Searchable.Group.CLAUSES)
	private BigDecimal totalamount;

	@Searchable(name = "totalamountcollected", group = Searchable.Group.CLAUSES)
	private BigDecimal totalamountcollected;

	@Searchable(name = "advertisementlocation", group = Searchable.Group.CLAUSES)
	private GeoPoint advertisementLocation;

	public AdvertisementSearch(final String advertisementNumber,
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

	public AdvertisementSearch() {
		// TODO Auto-generated constructor stub
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getIslegacy() {
		return islegacy;
	}

	public void setIslegacy(Boolean islegacy) {
		this.islegacy = islegacy;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getAdvertisementNumber() {
		return advertisementNumber;
	}

	public void setAdvertisementNumber(String advertisementNumber) {
		this.advertisementNumber = advertisementNumber;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
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

	public String getAdvertisementCreatedBy() {
		return advertisementCreatedBy;
	}

	public void setAdvertisementCreatedBy(String advertisementCreatedBy) {
		this.advertisementCreatedBy = advertisementCreatedBy;
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

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
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

	public GeoPoint getAdvertisementLocation() {
		return advertisementLocation;
	}

	public void setAdvertisementLocation(GeoPoint advertisementLocation) {
		this.advertisementLocation = advertisementLocation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
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

	public void setEncroachmentfee_collected(
			BigDecimal encroachmentfee_collected) {
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

	public String getAdvertisement_status() {
		return advertisement_status;
	}

	public void setAdvertisement_status(String advertisement_status) {
		this.advertisement_status = advertisement_status;
	}

	public String getAdvertisement_duration() {
		return advertisement_duration;
	}

	public void setAdvertisement_duration(String advertisement_duration) {
		this.advertisement_duration = advertisement_duration;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getAssessmentNumber() {
		return assessmentNumber;
	}

	public void setAssessmentNumber(String assessmentNumber) {
		this.assessmentNumber = assessmentNumber;
	}
}
