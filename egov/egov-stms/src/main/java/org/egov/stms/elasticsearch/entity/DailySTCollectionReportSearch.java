/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.elasticsearch.entity;

import java.util.ArrayList;
import java.util.List;

public class DailySTCollectionReportSearch {
    private String fromDate;
    private String toDate;
    private List<String> consumerCode = new ArrayList<>();
    private String collectionMode;
    private String collectionOperator;
    private String revenueWard;
    private String searchText;
    private String ulbName;
    private String consumerNumber;
    private String shscNumber;
    private String applicantName;
    private String mobileNumber;
    private String doorNumber;
    private String applicationDate;
    private String receiptDate;
    private String paidAt;
    private String paymentMode;
    private Double arrearAmount;
    private Double currentAmount;
    private Double totalAmount;
    private String doorNo;
    private String ownerName;
    private String receiptNumber;
    private String status;

    public List<String> getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final List<String> consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getCollectionMode() {
        return collectionMode;
    }

    public void setCollectionMode(final String collectionMode) {
        this.collectionMode = collectionMode;
    }

    public String getCollectionOperator() {
        return collectionOperator;
    }

    public void setCollectionOperator(final String collectionOperator) {
        this.collectionOperator = collectionOperator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public String searchQuery() {
        return searchText;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(final String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getShscNumber() {
        return shscNumber;
    }

    public void setShscNumber(final String shscNumber) {
        this.shscNumber = shscNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(final String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(final String paidAt) {
        this.paidAt = paidAt;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getArrearAmount() {
        return arrearAmount;
    }

    public void setArrearAmount(final Double arrearAmount) {
        this.arrearAmount = arrearAmount;
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

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

}