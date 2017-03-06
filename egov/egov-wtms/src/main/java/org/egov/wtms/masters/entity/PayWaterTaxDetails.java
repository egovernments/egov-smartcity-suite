/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.wtms.masters.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayWaterTaxDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7083364740584612941L;
    private String consumerNo;
    private String ulbCode;
    @JsonIgnore
    private String applicaionNumber;
    private String paymentMode;
    private BigDecimal paymentAmount;
    private String paidBy;
    private String transactionId;
    private String chqddNo;
    private String chqddDate;
    private String bankName;
    private String branchName;
    @JsonIgnore
    private String source;

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(final String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getTotalAmount() {
        return paymentAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        paymentAmount = totalAmount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(final String paidBy) {
        this.paidBy = paidBy;
    }

    @Override
    public String toString() {
        return "PayWaterTaxDetails [consumerNo=" + consumerNo + ", applicaionNumber=" + applicaionNumber
                + ", paymentMode=" + paymentMode + ", paymentAmount=" + paymentAmount + ", paidBy=" + paidBy
                + ", transactionId=" + transactionId + "]";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public String getApplicaionNumber() {
        return applicaionNumber;
    }

    public void setApplicaionNumber(final String applicaionNumber) {
        this.applicaionNumber = applicaionNumber;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(final BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getChqddNo() {
        return chqddNo;
    }

    public void setChqddNo(final String chqddNo) {
        this.chqddNo = chqddNo;
    }

    public String getChqddDate() {
        return chqddDate;
    }

    public void setChqddDate(final String chqddDate) {
        this.chqddDate = chqddDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(final String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
