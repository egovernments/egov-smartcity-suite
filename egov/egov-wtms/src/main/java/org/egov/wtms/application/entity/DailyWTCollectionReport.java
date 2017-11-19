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
package org.egov.wtms.application.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DailyWTCollectionReport {

    private String receiptNumber;
    private Date receiptDate;
    private String consumerCode;
    private String consumerName;
    private String wardName;
    private String doorNumber;
    private String connectionType;
    private String paymentMode;
    private String paidAt;
    private BigDecimal currentTotal;
    private BigDecimal arrearTotal;
    private BigDecimal total;
    private String cancellationDetails;
    private String fromInstallment;
    private String toInstallment;
    private String status;

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(final String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(final String paidAt) {
        this.paidAt = paidAt;
    }

    public BigDecimal getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(final BigDecimal currentTotal) {
        this.currentTotal = currentTotal;
    }

    public BigDecimal getArrearTotal() {
        return arrearTotal;
    }

    public void setArrearTotal(final BigDecimal arrearTotal) {
        this.arrearTotal = arrearTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    public String getCancellationDetails() {
        return cancellationDetails;
    }

    public void setCancellationDetails(final String cancellationDetails) {
        this.cancellationDetails = cancellationDetails;
    }

    public String getFromInstallment() {
        return fromInstallment;
    }

    public void setFromInstallment(final String fromInstallment) {
        this.fromInstallment = fromInstallment;
    }

    public String getToInstallment() {
        return toInstallment;
    }

    public void setToInstallment(final String toInstallment) {
        this.toInstallment = toInstallment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

}
