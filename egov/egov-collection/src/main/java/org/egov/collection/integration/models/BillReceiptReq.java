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

package org.egov.collection.integration.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class BillReceiptReq {

    @JsonProperty("billReferenceNum")
    private String billReferenceNum;
    private String event;
    private String receiptNum;
    private Date receiptDate;
    private String payeeName;
    private String payeeAddress;
    private Set<ReceiptAccountInfo> accountDetails;
    private String serviceName;
    private String paidBy;
    private String description;
    private BigDecimal totalAmount;
    private String receiptURL;
    private String collectionType;
    private Boolean legacy;
    private String additionalInfo;
    private String source;
    private String receiptInstrumentType;
    private String tenantId = null;
    private String receiptStatus;
    
    

    public BillReceiptReq(BillReceiptInfo billReceipt) {
        super();
        this.billReferenceNum = billReceipt.getBillReferenceNum();
        this.event = billReceipt.getEvent();
        this.receiptNum = billReceipt.getReceiptNum();
        this.receiptDate = billReceipt.getReceiptDate();
        this.payeeName = billReceipt.getPayeeName();
        this.payeeAddress = billReceipt.getPayeeAddress();
        this.accountDetails = billReceipt.getAccountDetails();
        this.serviceName = billReceipt.getServiceName();
        this.paidBy = billReceipt.getPaidBy();
        this.description = billReceipt.getDescription();
        this.totalAmount = billReceipt.getTotalAmount();
        this.receiptURL = billReceipt.getReceiptURL();
        this.collectionType = billReceipt.getCollectionType();
        this.legacy = billReceipt.getLegacy();
        this.additionalInfo = billReceipt.getAdditionalInfo();
        this.source = billReceipt.getSource();
        this.receiptInstrumentType = billReceipt.getReceiptInstrumentType();
        this.tenantId = tenantId;
        this.receiptStatus = billReceipt.getReceiptStatus().getCode();
    }

    public String getBillReferenceNum() {
        return billReferenceNum;
    }

    public void setBillReferenceNum(final String billReferenceNum) {
        this.billReferenceNum = billReferenceNum;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(final String event) {
        this.event = event;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(final String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(final String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(final String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public Set<ReceiptAccountInfo> getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(final Set<ReceiptAccountInfo> accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(final String paidBy) {
        this.paidBy = paidBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiptURL() {
        return receiptURL;
    }

    public void setReceiptURL(final String receiptURL) {
        this.receiptURL = receiptURL;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(final String collectionType) {
        this.collectionType = collectionType;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(final Boolean legacy) {
        this.legacy = legacy;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(final String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getReceiptInstrumentType() {
        return receiptInstrumentType;
    }

    public void setReceiptInstrumentType(final String receiptInstrumentType) {
        this.receiptInstrumentType = receiptInstrumentType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(final String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

}