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
package org.egov.egf.revenue;

import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FixedDeposit extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 5997964263687954751L;
    private String fileNo;
    private BigDecimal amount;
    private Date date;
    private Bankbranch bankBranch;
    private Bankaccount bankAccount;
    private BigDecimal interestRate;
    private String period;
    private String serialNumber;
    private CVoucherHeader outFlowVoucher; // GJV
    private BigDecimal gjvAmount;
    private BigDecimal receiptAmount;
    private BigDecimal maturityAmount;
    private Date maturityDate;
    private Date withdrawalDate;
    private CVoucherHeader inFlowVoucher;
    private CVoucherHeader challanReceiptVoucher;
    private InstrumentHeader instrumentHeader;
    private String remarks;
    private String referenceNumber;
    private FixedDeposit parentId;
    private boolean extend;
    private boolean extendTemp;
    private Long parentTemp;
    private List<Bankaccount> bankAccountList = new ArrayList<Bankaccount>();

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(final String fileNo) {
        this.fileNo = fileNo;
    }

    public Bankbranch getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(final Bankbranch bankBranch) {
        this.bankBranch = bankBranch;
    }

    public BigDecimal getGjvAmount() {
        return gjvAmount;
    }

    public void setGjvAmount(final BigDecimal gjvAmount) {
        this.gjvAmount = gjvAmount;
    }

    public Long getParentTemp() {
        return parentTemp;
    }

    public void setParentTemp(final Long parentTemp) {
        this.parentTemp = parentTemp;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(final BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public boolean isExtendTemp() {
        return extendTemp;
    }

    public void setExtendTemp(final boolean extendTemp) {
        this.extendTemp = extendTemp;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public InstrumentHeader getInstrumentHeader() {
        return instrumentHeader;
    }

    public void setInstrumentHeader(final InstrumentHeader instrumentHeader) {
        this.instrumentHeader = instrumentHeader;
    }

    public List<Bankaccount> getBankAccountList() {
        return bankAccountList;
    }

    public void setBankAccountList(final List<Bankaccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
    }

    public BigDecimal getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(final BigDecimal maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(final Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Date getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(final Date withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public CVoucherHeader getOutFlowVoucher() {
        return outFlowVoucher;
    }

    public void setOutFlowVoucher(final CVoucherHeader outFlowVoucher) {
        this.outFlowVoucher = outFlowVoucher;
    }

    public CVoucherHeader getInFlowVoucher() {
        return inFlowVoucher;
    }

    public void setInFlowVoucher(final CVoucherHeader inFlowVoucher) {
        this.inFlowVoucher = inFlowVoucher;
    }

    public CVoucherHeader getChallanReceiptVoucher() {
        return challanReceiptVoucher;
    }

    public void setChallanReceiptVoucher(final CVoucherHeader challanReceiptVoucher) {
        this.challanReceiptVoucher = challanReceiptVoucher;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public FixedDeposit getParentId() {
        return parentId;
    }

    public void setParentId(final FixedDeposit parentId) {
        this.parentId = parentId;
    }

    public boolean getExtend() {
        return extend;
    }

    public void setExtend(final boolean extend) {
        this.extend = extend;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

}
