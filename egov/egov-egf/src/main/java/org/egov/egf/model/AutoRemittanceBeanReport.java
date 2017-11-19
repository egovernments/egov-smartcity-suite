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
package org.egov.egf.model;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional(readOnly = true)
public class AutoRemittanceBeanReport {

    private String voucherNumber;
    private String billNumber;
    private BigDecimal billAmount;
    private String remittancePaymentNo;
    private BigDecimal remittedAmount;
    private String rtgsNoDate;
    private BigDecimal rtgsAmount;
    private String partyName;
    private String fundName;
    private String bankbranchAccount;
    private BigDecimal remittanceDTId;
    private String remittanceCOA;
    private BigDecimal detailKeyTypeId;
    private BigDecimal detailKeyId;
    private BigDecimal voucherId;
    private BigDecimal billId;
    private BigDecimal paymentVoucherId;
    private BigDecimal remittedAmountSubtotal = new BigDecimal("0");
    private String department;
    private String drawingOfficer;
    private String panNumber;

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(final BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getRemittancePaymentNo() {
        return remittancePaymentNo;
    }

    public void setRemittancePaymentNo(final String remittancePaymentNo) {
        this.remittancePaymentNo = remittancePaymentNo;
    }

    public BigDecimal getRemittedAmount() {
        return remittedAmount;
    }

    public void setRemittedAmount(final BigDecimal remittedAmount) {
        this.remittedAmount = remittedAmount;
    }

    public String getRtgsNoDate() {
        return rtgsNoDate;
    }

    public void setRtgsNoDate(final String rtgsNoDate) {
        this.rtgsNoDate = rtgsNoDate;
    }

    public BigDecimal getRtgsAmount() {
        return rtgsAmount;
    }

    public void setRtgsAmount(final BigDecimal rtgsAmount) {
        this.rtgsAmount = rtgsAmount;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    public String getBankbranchAccount() {
        return bankbranchAccount;
    }

    public void setBankbranchAccount(final String bankbranchAccount) {
        this.bankbranchAccount = bankbranchAccount;
    }

    public String getRemittanceCOA() {
        return remittanceCOA;
    }

    public void setRemittanceCOA(final String remittanceCOA) {
        this.remittanceCOA = remittanceCOA;
    }

    public BigDecimal getRemittanceDTId() {
        return remittanceDTId;
    }

    public void setRemittanceDTId(final BigDecimal remittanceDTId) {
        this.remittanceDTId = remittanceDTId;
    }

    public BigDecimal getDetailKeyTypeId() {
        return detailKeyTypeId;
    }

    public void setDetailKeyTypeId(final BigDecimal detailKeyTypeId) {
        this.detailKeyTypeId = detailKeyTypeId;
    }

    public BigDecimal getDetailKeyId() {
        return detailKeyId;
    }

    public void setDetailKeyId(final BigDecimal detailKeyId) {
        this.detailKeyId = detailKeyId;
    }

    public BigDecimal getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(final BigDecimal voucherId) {
        this.voucherId = voucherId;
    }

    public BigDecimal getBillId() {
        return billId;
    }

    public void setBillId(final BigDecimal billId) {
        this.billId = billId;
    }

    public BigDecimal getPaymentVoucherId() {
        return paymentVoucherId;
    }

    public void setPaymentVoucherId(final BigDecimal paymentVoucherId) {
        this.paymentVoucherId = paymentVoucherId;
    }

    public BigDecimal getRemittedAmountSubtotal() {
        return remittedAmountSubtotal;
    }

    public void setRemittedAmountSubtotal(final BigDecimal remittedAmountSubtotal) {
        this.remittedAmountSubtotal = remittedAmountSubtotal;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getDrawingOfficer() {
        return drawingOfficer;
    }

    public void setDrawingOfficer(final String drawingOfficer) {
        this.drawingOfficer = drawingOfficer;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(final String panNumber) {
        this.panNumber = panNumber;
    }

}
