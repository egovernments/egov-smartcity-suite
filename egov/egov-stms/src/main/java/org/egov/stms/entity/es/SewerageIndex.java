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

package org.egov.stms.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

@Document(indexName = "sewerage", type = "sewerage")
public class SewerageIndex {

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date applicationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date disposalDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationStatus;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String estimationNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date estimationDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String workOrderNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date workOrderDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String closureNoticeNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date closureNoticeDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String applicationCreatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String shscNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyIdentifier;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String propertyType;

    @Field(type = FieldType.Integer)
    private Integer noOfClosets_residential;

    @Field(type = FieldType.Integer)
    private Integer noOfClosets_nonResidential;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String connectionStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date executionDate;

    @Field(type = FieldType.Boolean)
    private Boolean islegacy;

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

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String mobileNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String ward;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String address;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String doorNo;
    // isActive used for setting application status is active or not
    // on connection execution application is marked as active
    @Field(type = FieldType.Boolean)
    private boolean active;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueBlock;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String locationName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String period;

    @Field(type = FieldType.Double)
    private BigDecimal arrearAmount;

    @Field(type = FieldType.Double)
    private BigDecimal collectedArrearAmount;

    @Field(type = FieldType.Double)
    private BigDecimal totalAmount;

    @Field(type = FieldType.Double)
    private BigDecimal demandAmount;

    @Field(type = FieldType.Double)
    private BigDecimal collectedDemandAmount;

    @Field(type = FieldType.Double)
    private BigDecimal extraAdvanceAmount;

    @Field(type = FieldType.Double)
    private BigDecimal donationAmount;

    @Field(type = FieldType.Double)
    private BigDecimal inspectionCharge;

    @Field(type = FieldType.Double)
    private BigDecimal estimationCharge;

    @Field(type = FieldType.Double)
    private BigDecimal sewerageTax;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String source;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String rejectionNoticeNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date rejectionNoticeDate;

    public String getRevenueBlock() {
        return revenueBlock;
    }

    public void setRevenueBlock(final String revenueBlock) {
        this.revenueBlock = revenueBlock;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(final String locationName) {
        this.locationName = locationName;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(final String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(final String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getEstimationNumber() {
        return estimationNumber;
    }

    public void setEstimationNumber(final String estimationNumber) {
        this.estimationNumber = estimationNumber;
    }

    public Date getEstimationDate() {
        return estimationDate;
    }

    public void setEstimationDate(final Date estimationDate) {
        this.estimationDate = estimationDate;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(final Date workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public String getClosureNoticeNumber() {
        return closureNoticeNumber;
    }

    public void setClosureNoticeNumber(final String closureNoticeNumber) {
        this.closureNoticeNumber = closureNoticeNumber;
    }

    public Date getClosureNoticeDate() {
        return closureNoticeDate;
    }

    public void setClosureNoticeDate(final Date closureNoticeDate) {
        this.closureNoticeDate = closureNoticeDate;
    }

    public String getApplicationCreatedBy() {
        return applicationCreatedBy;
    }

    public void setApplicationCreatedBy(final String applicationCreatedBy) {
        this.applicationCreatedBy = applicationCreatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getShscNumber() {
        return shscNumber;
    }

    public void setShscNumber(final String shscNumber) {
        this.shscNumber = shscNumber;
    }

    public String getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void setPropertyIdentifier(final String propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getNoOfClosets_residential() {
        return noOfClosets_residential;
    }

    public void setNoOfClosets_residential(final Integer noOfClosets_residential) {
        this.noOfClosets_residential = noOfClosets_residential;
    }

    public Integer getNoOfClosets_nonResidential() {
        return noOfClosets_nonResidential;
    }

    public void setNoOfClosets_nonResidential(final Integer noOfClosets_nonResidential) {
        this.noOfClosets_nonResidential = noOfClosets_nonResidential;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(final String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(final Date executionDate) {
        this.executionDate = executionDate;
    }

    public Boolean getIslegacy() {
        return islegacy;
    }

    public void setIslegacy(final Boolean islegacy) {
        this.islegacy = islegacy;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getUlbGrade() {
        return ulbGrade;
    }

    public void setUlbGrade(final String ulbGrade) {
        this.ulbGrade = ulbGrade;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public BigDecimal getArrearAmount() {
        return arrearAmount;
    }

    public void setArrearAmount(final BigDecimal arrearAmount) {
        this.arrearAmount = arrearAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public BigDecimal getCollectedArrearAmount() {
        return collectedArrearAmount;
    }

    public void setCollectedArrearAmount(final BigDecimal collectedArrearAmount) {
        this.collectedArrearAmount = collectedArrearAmount;
    }

    public BigDecimal getDemandAmount() {
        return demandAmount;
    }

    public void setDemandAmount(final BigDecimal demandAmount) {
        this.demandAmount = demandAmount;
    }

    public BigDecimal getCollectedDemandAmount() {
        return collectedDemandAmount;
    }

    public void setCollectedDemandAmount(final BigDecimal collectedDemandAmount) {
        this.collectedDemandAmount = collectedDemandAmount;
    }

    public BigDecimal getExtraAdvanceAmount() {
        return extraAdvanceAmount;
    }

    public void setExtraAdvanceAmount(final BigDecimal extraAdvanceAmount) {
        this.extraAdvanceAmount = extraAdvanceAmount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public BigDecimal getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(final BigDecimal donationAmount) {
        this.donationAmount = donationAmount;
    }

    public BigDecimal getEstimationCharge() {
        return estimationCharge;
    }

    public void setEstimationCharge(final BigDecimal estimationCharge) {
        this.estimationCharge = estimationCharge;
    }

    public BigDecimal getInspectionCharge() {
        return inspectionCharge;
    }

    public void setInspectionCharge(final BigDecimal inspectionCharge) {
        this.inspectionCharge = inspectionCharge;
    }

    public String getRejectionNoticeNumber() {
        return rejectionNoticeNumber;
    }

    public void setRejectionNoticeNumber(final String rejectionNoticeNumber) {
        this.rejectionNoticeNumber = rejectionNoticeNumber;
    }

    public Date getRejectionNoticeDate() {
        return rejectionNoticeDate;
    }

    public void setRejectionNoticeDate(final Date rejectionNoticeDate) {
        this.rejectionNoticeDate = rejectionNoticeDate;
    }

    public BigDecimal getSewerageTax() {
        return sewerageTax;
    }

    public void setSewerageTax(final BigDecimal sewerageTax) {
        this.sewerageTax = sewerageTax;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
