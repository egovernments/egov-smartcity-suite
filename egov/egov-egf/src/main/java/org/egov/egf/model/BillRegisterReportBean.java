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
/**
 *
 */
package org.egov.egf.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author manoranjan
 *
 */
public class BillRegisterReportBean {

    private String billNumber;
    private String voucherNumber;
    private String paymentVoucherNumber;
    private String partyName;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    private BigDecimal deductionAmount;
    private BigDecimal paidAmount;
    private String status;
    private String billDate;
    private String chequeNumAndDate;
    private String remittanceVoucherNumber;
    private String remittanceChequeNumberAndDate;
    private Date ChequeDate;

    public String getBillNumber() {
        return billNumber;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public String getPaymentVoucherNumber() {
        return paymentVoucherNumber;
    }

    public String getPartyName() {
        return partyName;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public void setPaymentVoucherNumber(final String paymentVoucherNumber) {
        this.paymentVoucherNumber = paymentVoucherNumber;
    }

    public String getChequeNumAndDate() {
        return chequeNumAndDate;
    }

    public void setChequeNumAndDate(final String chequeNumAndDate) {
        this.chequeNumAndDate = chequeNumAndDate;
    }

    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }

    public void setGrossAmount(final BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setNetAmount(final BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public void setDeductionAmount(final BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public void setPaidAmount(final BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(final String billDate) {
        this.billDate = billDate;
    }

    public String getRemittanceVoucherNumber() {
        return remittanceVoucherNumber;
    }

    public void setRemittanceVoucherNumber(final String remittanceVoucherNumber) {
        this.remittanceVoucherNumber = remittanceVoucherNumber;
    }

    public Date getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(final Date chequeDate) {
        ChequeDate = chequeDate;
    }

    public String getRemittanceChequeNumberAndDate() {
        return remittanceChequeNumberAndDate;
    }

    public void setRemittanceChequeNumberAndDate(
            final String remittanceChequeNumberAndDate) {
        this.remittanceChequeNumberAndDate = remittanceChequeNumberAndDate;
    }

}
