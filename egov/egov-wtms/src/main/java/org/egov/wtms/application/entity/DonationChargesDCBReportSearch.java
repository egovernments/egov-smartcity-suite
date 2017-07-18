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

package org.egov.wtms.application.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DonationChargesDCBReportSearch {
    private Date fromDate;
    private Date toDate;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private Boolean pendingForPaymentOnly;
    private String consumerCode;
    private String assessmentNumber;
    private String ownerName;
    private String mobileNumber;
    private String propertyAddress;
    private BigDecimal totalDonationAmount;
    private BigDecimal paidDonationAmount;
    private BigDecimal balanceDonationAmount;
    private String propertyIdentifier;

    public String getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void setPropertyIdentifier(final String propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Boolean getPendingForPaymentOnly() {
        return pendingForPaymentOnly;
    }

    public void setPendingForPaymentOnly(final Boolean pendingForPaymentOnly) {
        this.pendingForPaymentOnly = pendingForPaymentOnly;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(final BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(final BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(final String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(final String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public BigDecimal getTotalDonationAmount() {
        return totalDonationAmount;
    }

    public void setTotalDonationAmount(final BigDecimal totalDonationAmount) {
        this.totalDonationAmount = totalDonationAmount;
    }

    public BigDecimal getPaidDonationAmount() {
        return paidDonationAmount;
    }

    public void setPaidDonationAmount(final BigDecimal paidDonationAmount) {
        this.paidDonationAmount = paidDonationAmount;
    }

    public BigDecimal getBalanceDonationAmount() {
        return balanceDonationAmount;
    }

    public void setBalanceDonationAmount(final BigDecimal balanceDonationAmount) {
        this.balanceDonationAmount = balanceDonationAmount;
    }
}
