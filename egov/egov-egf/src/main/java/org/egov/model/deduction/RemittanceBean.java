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
package org.egov.model.deduction;

import org.apache.commons.collections.Predicate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author manoranjan
 *
 */
public class RemittanceBean implements Predicate {

    private Long recoveryId;
    private String voucherNumber;
    private String voucherName;
    private String voucherDate;
    private BigDecimal amount;
    private BigDecimal deductionAmount;
    private BigDecimal earlierPayment;
    private String partyName;
    private String partyCode;
    private String panNo;
    private Integer remittanceId;
    private String selectedrRemit;
    private BigDecimal totalAmount;
    private boolean chkremit;
    private Integer bank;
    private Integer bankBranchId;
    private Long bankAccountId;
    private Integer detailTypeId;
    private Integer detailKeyid;
    private BigDecimal partialAmount;
    private Integer remittance_gl_dtlId;
    private String fromDate;
    private Date fromVhDate;
    private Long departmentId;
    private Long functionId;

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Long functionId) {
        this.functionId = functionId;
    }

    public Integer getRemittance_gl_dtlId() {
        return remittance_gl_dtlId;
    }

    public void setRemittance_gl_dtlId(final Integer remittance_gl_dtlId) {
        this.remittance_gl_dtlId = remittance_gl_dtlId;
    }

    public BigDecimal getPartialAmount() {
        return partialAmount;
    }

    public void setPartialAmount(final BigDecimal partialAmount) {
        this.partialAmount = partialAmount;
    }

    private Integer accountNumber;

    public boolean getChkremit() {
        return chkremit;
    }

    public void setChkremit(final boolean chkremit) {
        this.chkremit = chkremit;
    }

    public Long getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(final Long recoveryId) {
        this.recoveryId = recoveryId;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(final String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(final String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(final String panNo) {
        this.panNo = panNo;
    }

    public Integer getRemittanceId() {
        return remittanceId;
    }

    public void setRemittanceId(final Integer remittanceId) {
        this.remittanceId = remittanceId;
    }

    public String getSelectedrRemit() {
        return selectedrRemit;
    }

    public void setSelectedrRemit(final String selectedrRemit) {
        this.selectedrRemit = selectedrRemit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean evaluate(final Object arg0) {
        final RemittanceBean remittanceBean = (RemittanceBean) arg0;
        return remittanceBean.getChkremit();
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(final Integer bank) {
        this.bank = bank;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(final Integer detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    public Integer getDetailKeyid() {
        return detailKeyid;
    }

    public void setDetailKeyid(final Integer detailKeyid) {
        this.detailKeyid = detailKeyid;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(final BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public BigDecimal getEarlierPayment() {
        return earlierPayment;
    }

    public void setEarlierPayment(final BigDecimal earlierPayment) {
        this.earlierPayment = earlierPayment;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromVhDate() {
        return fromVhDate;
    }

    public void setFromVhDate(final Date fromVhDate) {
        this.fromVhDate = fromVhDate;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final Integer bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(final Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

}
