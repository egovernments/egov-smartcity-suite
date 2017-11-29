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

public class CollectionSummaryHeadWiseReport {

    private String source;
    private String glCode;
    private String cashCount;
    private Double cashAmount;
    private String chequeddCount;
    private Double chequeddAmount;
    private String onlineCount;
    private Double onlineAmount;
    private String totalReceiptCount;
    private Double totalAmount;
    private String employeeName;
    private String counterName;
    private Double cardAmount;
    private String cardCount;

    private Double totalCashRebateAmount = new Double(0.0);
    private Double totalChequeddRebateAmount = new Double(0.0);
    private Double totalOnlineRebateAmount = new Double(0.0);
    private Double totalCardRebateAmount = new Double(0.0);
    private Double totalRebateAmount = new Double(0.0);

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public String getCashCount() {
        return cashCount;
    }

    public void setCashCount(final String cashCount) {
        this.cashCount = cashCount;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(final Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getChequeddCount() {
        return chequeddCount;
    }

    public void setChequeddCount(final String chequeddCount) {
        this.chequeddCount = chequeddCount;
    }

    public Double getChequeddAmount() {
        return chequeddAmount;
    }

    public void setChequeddAmount(final Double chequeddAmount) {
        this.chequeddAmount = chequeddAmount;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(final String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Double getOnlineAmount() {
        return onlineAmount;
    }

    public void setOnlineAmount(final Double onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    public String getTotalReceiptCount() {
        return totalReceiptCount;
    }

    public void setTotalReceiptCount(final String totalReceiptCount) {
        this.totalReceiptCount = totalReceiptCount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Double totalAmount) {
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

    public String getCardCount() {
        return cardCount;
    }

    public void setCardCount(final String cardCount) {
        this.cardCount = cardCount;
    }

    public Double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(final Double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public Double getTotalCashRebateAmount() {
        return totalCashRebateAmount;
    }

    public void setTotalCashRebateAmount(Double totalCashRebateAmount) {
        this.totalCashRebateAmount = totalCashRebateAmount;
    }

    public Double getTotalChequeddRebateAmount() {
        return totalChequeddRebateAmount;
    }

    public void setTotalChequeddRebateAmount(Double totalChequeddRebateAmount) {
        this.totalChequeddRebateAmount = totalChequeddRebateAmount;
    }

    public Double getTotalOnlineRebateAmount() {
        return totalOnlineRebateAmount;
    }

    public void setTotalOnlineRebateAmount(Double totalOnlineRebateAmount) {
        this.totalOnlineRebateAmount = totalOnlineRebateAmount;
    }

    public Double getTotalCardRebateAmount() {
        return totalCardRebateAmount;
    }

    public void setTotalCardRebateAmount(Double totalCardRebateAmount) {
        this.totalCardRebateAmount = totalCardRebateAmount;
    }

    public Double getTotalRebateAmount() {
        return totalRebateAmount;
    }

    public void setTotalRebateAmount(Double totalRebateAmount) {
        this.totalRebateAmount = totalRebateAmount;
    }
}