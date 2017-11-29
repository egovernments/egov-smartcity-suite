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

import org.egov.model.instrument.InstrumentVoucher;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class BankBookEntry {
    public String voucherNumber;
    public Date voucherDate;
    public String particulars;
    public  BigDecimal amount;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;
    public String chequeNumber;
    public  String chequeDate;
    public   String type;
    private String chequeDetail;
    private String glCode;
    private BigDecimal receiptAmount;
    private BigDecimal paymentAmount;
    private String instrumentStatus;
    private BigDecimal voucherId;
    private List<InstrumentVoucher> instrumentVouchers;

    public BankBookEntry() {
    };

    public BankBookEntry(final String particulars, final BigDecimal amount, final String type,
            final BigDecimal receiptAmount, final BigDecimal paymentAmount) {
        super();
        this.particulars = particulars;
        this.amount = amount;
        this.type = type;
        this.receiptAmount = receiptAmount;
        this.paymentAmount = paymentAmount;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(final String particulars) {
        this.particulars = particulars;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setChequeDetail(final String chequeDetail) {
        this.chequeDetail = chequeDetail;
    }

    public String getChequeDetail() {
        return chequeDetail;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setDebitAmount(final BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setReceiptAmount(final BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setPaymentAmount(final BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setInstrumentStatus(final String instrumentStatus) {
        this.instrumentStatus = instrumentStatus;
    }

    public String getInstrumentStatus() {
        return instrumentStatus;
    }

    public void setVoucherId(final BigDecimal voucherId) {
        this.voucherId = voucherId;
    }

    public BigDecimal getVoucherId() {
        return voucherId;
    }

    public void setInstrumentVouchers(final List<InstrumentVoucher> chequeDetails) {
        instrumentVouchers = chequeDetails;

    }

    public List<InstrumentVoucher> getInstrumentVouchers() {
        return instrumentVouchers;
    }
}
