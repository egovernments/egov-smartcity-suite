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
import java.util.List;

@Transactional(readOnly = true)
public class BankBookViewEntry {
    String receiptVoucherDate;
    String receiptVoucherNumber;
    String receiptParticulars;
    BigDecimal receiptAmount;
    String receiptChequeDetail;
    String paymentVoucherDate;
    String paymentVoucherNumber;
    String paymentParticulars;
    BigDecimal paymentAmount;
    String paymentChequeDetail;
    String instrumentStatus;
    String glCode;
    private Long voucherId;
    // string of cheque number and dates
    private String chequeNumber;

    private List<InstrumentVoucher> instrumentVouchers;

    public List<InstrumentVoucher> getInstrumentVouchers() {
        return instrumentVouchers;
    }

    public void setInstrumentVouchers(final List<InstrumentVoucher> instrumentVouchers) {
        this.instrumentVouchers = instrumentVouchers;
    }

    public BankBookViewEntry() {
    };

    public BankBookViewEntry(final String voucherNumber, final String voucherDate, final String particulars,
            final BigDecimal amount,
            final String chequeDetail, final String type) {
        super();
        if ("Payment".equalsIgnoreCase(type)) {
            paymentVoucherDate = voucherDate;
            paymentVoucherNumber = voucherNumber;
            paymentParticulars = particulars;
            paymentAmount = amount;
            paymentChequeDetail = chequeDetail;
        } else {
            receiptVoucherDate = voucherDate;
            receiptVoucherNumber = voucherNumber;
            receiptParticulars = particulars;
            receiptAmount = amount;
            receiptChequeDetail = chequeDetail;
        }
    }

    public BankBookViewEntry(final String voucherNumber, final String voucherDate, final String particulars,
            final BigDecimal amount,
            final String chequeDetail, final String type, final String chequeNumber) {
        super();
        if ("Payment".equalsIgnoreCase(type)) {
            paymentVoucherDate = voucherDate;
            paymentVoucherNumber = voucherNumber;
            paymentParticulars = particulars;
            paymentAmount = amount;
            paymentChequeDetail = chequeDetail;
            instrumentVouchers = instrumentVouchers;
            this.chequeNumber = chequeNumber;
        } else {
            receiptVoucherDate = voucherDate;
            receiptVoucherNumber = voucherNumber;
            receiptParticulars = particulars;
            receiptAmount = amount;
            receiptChequeDetail = chequeDetail;
            instrumentVouchers = instrumentVouchers;
            this.chequeNumber = chequeNumber;
        }
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getReceiptVoucherDate() {
        return receiptVoucherDate;
    }

    public void setReceiptVoucherDate(final String receiptVoucherDate) {
        this.receiptVoucherDate = receiptVoucherDate;
    }

    public String getReceiptVoucherNumber() {
        return receiptVoucherNumber;
    }

    public void setReceiptVoucherNumber(final String receiptVoucherNumber) {
        this.receiptVoucherNumber = receiptVoucherNumber;
    }

    public String getReceiptParticulars() {
        return receiptParticulars;
    }

    public void setReceiptParticulars(final String receiptParticulars) {
        this.receiptParticulars = receiptParticulars;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(final BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getReceiptChequeDetail() {
        return receiptChequeDetail;
    }

    public void setReceiptChequeDetail(final String receiptChequeDetail) {
        this.receiptChequeDetail = receiptChequeDetail;
    }

    public String getPaymentVoucherDate() {
        return paymentVoucherDate;
    }

    public void setPaymentVoucherDate(final String paymentVoucherDate) {
        this.paymentVoucherDate = paymentVoucherDate;
    }

    public String getPaymentVoucherNumber() {
        return paymentVoucherNumber;
    }

    public void setPaymentVoucherNumber(final String paymentVoucherNumber) {
        this.paymentVoucherNumber = paymentVoucherNumber;
    }

    public String getPaymentParticulars() {
        return paymentParticulars;
    }

    public void setPaymentParticulars(final String paymentParticulars) {
        this.paymentParticulars = paymentParticulars;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(final BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentChequeDetail() {
        return paymentChequeDetail;
    }

    public void setPaymentChequeDetail(final String paymentChequeDetail) {
        this.paymentChequeDetail = paymentChequeDetail;
    }

    public String getInstrumentStatus() {
        return instrumentStatus;
    }

    public void setInstrumentStatus(final String instrumentStatus) {
        this.instrumentStatus = instrumentStatus;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public void setVoucherId(final Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() {
        return voucherId;
    }
}
