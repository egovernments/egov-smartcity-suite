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

package org.egov.collection.entity.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_TIMEZONE;
import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;

@Document(indexName = "receipts", type = "receipts_bifurcation")
public class CollectionDocument {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String receiptNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billingService;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String paymentMode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String channel;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String paymentGateway;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String billNumber;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String districtName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String regionName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityGrade;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String receiptCreator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date receiptDate;

    @Field(type = FieldType.Double)
    private Double arrearAmount;

    @Field(type = FieldType.Double)
    private Double penaltyAmount;

    @Field(type = FieldType.Double)
    private Double currentAmount;

    @Field(type = FieldType.Double)
    private Double totalAmount;

    @Field(type = FieldType.Double)
    private Double advanceAmount;

    @Field(type = FieldType.Double)
    private Double latePaymentCharges;

    @Field(type = FieldType.Double)
    private Double arrearCess;

    @Field(type = FieldType.Double)
    private Double currentCess;

    @Field(type = FieldType.String)
    private String installmentFrom;

    @Field(type = FieldType.String)
    private String installmentTo;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerName;

    @Field(type = FieldType.Double)
    private Double reductionAmount;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String revenueWard;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ES_DATE_FORMAT, timezone = DEFAULT_TIMEZONE)
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time, pattern = ES_DATE_FORMAT)
    private Date createdDate;
    
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String consumerType;
    
    @Field(type = FieldType.Integer)
    private Integer conflict;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(final String cityCode) {
        this.cityCode = cityCode;
    }

    public String getBillingService() {
        return billingService;
    }

    public void setBillingService(final String billingService) {
        this.billingService = billingService;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(final String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(final String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getReceiptCreator() {
        return receiptCreator;
    }

    public void setReceiptCreator(final String receiptCreator) {
        this.receiptCreator = receiptCreator;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Double getArrearAmount() {
        return arrearAmount;
    }

    public void setArrearAmount(final Double arrearAmount) {
        this.arrearAmount = arrearAmount;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(final Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(final Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(final Double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public Double getLatePaymentCharges() {
        return latePaymentCharges;
    }

    public void setLatePaymentCharges(final Double latePaymentCharges) {
        this.latePaymentCharges = latePaymentCharges;
    }

    public Double getArrearCess() {
        return arrearCess;
    }

    public void setArrearCess(final Double arrearCess) {
        this.arrearCess = arrearCess;
    }

    public Double getCurrentCess() {
        return currentCess;
    }

    public void setCurrentCess(final Double currentCess) {
        this.currentCess = currentCess;
    }

    public String getInstallmentFrom() {
        return installmentFrom;
    }

    public void setInstallmentFrom(final String installmentFrom) {
        this.installmentFrom = installmentFrom;
    }

    public String getInstallmentTo() {
        return installmentTo;
    }

    public void setInstallmentTo(final String installmentTo) {
        this.installmentTo = installmentTo;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    public Double getReductionAmount() {
        return reductionAmount;
    }

    public void setReductionAmount(final Double reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public Integer getConflict() {
        return conflict;
    }

    public void setConflict(Integer conflict) {
        this.conflict = conflict;
    }
}
