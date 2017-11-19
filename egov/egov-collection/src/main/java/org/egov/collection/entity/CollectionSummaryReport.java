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
package org.egov.collection.entity;

import java.math.BigDecimal;


public class CollectionSummaryReport {

    private String source;
    private String serviceName;
    private String cashCount;
    private BigDecimal cashAmount;
    private String chequeddCount;
    private BigDecimal chequeddAmount;
    private String onlineCount;
    private String bankCount;
    private BigDecimal onlineAmount;
    private BigDecimal bankAmount;
    private String totalReceiptCount;
    private BigDecimal totalAmount;
    private String employeeName;
    private String counterName;
    private BigDecimal cardAmount;
    private String cardCount;

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCashCount() {
        return cashCount;
    }

    public void setCashCount(final String cashCount) {
        this.cashCount = cashCount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(final BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getChequeddCount() {
        return chequeddCount;
    }

    public void setChequeddCount(final String chequeddCount) {
        this.chequeddCount = chequeddCount;
    }

    public BigDecimal getChequeddAmount() {
        return chequeddAmount;
    }

    public void setChequeddAmount(final BigDecimal chequeddAmount) {
        this.chequeddAmount = chequeddAmount;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(final String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getBankCount() {
        return bankCount;
    }

    public void setBankCount(String bankCount) {
        this.bankCount = bankCount;
    }

    public BigDecimal getOnlineAmount() {
        return onlineAmount;
    }

    public void setOnlineAmount(final BigDecimal onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    public BigDecimal getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    public String getTotalReceiptCount() {
        return totalReceiptCount;
    }

    public void setTotalReceiptCount(final String totalReceiptCount) {
        this.totalReceiptCount = totalReceiptCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(final String counterName) {
        this.counterName = counterName;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getCardCount() {
        return cardCount;
    }

    public void setCardCount(String cardCount) {
        this.cardCount = cardCount;
    }
}