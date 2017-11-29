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
package org.egov.model.voucher;

import org.egov.utils.FinancialConstants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class CommonBean {
    private String chequeInHand;
    private String cashInHand;
    private String bankBranchId;
    private String accountNumberId;
    private String fromBankAccountId;
    private String chequeNumber;
    private String chequeDate;
    private String fromBankId;
    private String bankId;
    private String toBankId;
    private BigDecimal amount;
    private String boundaryLevel;
    private String accnumnar;
    private BigDecimal availableBalance;
    private String fromBankBranchId;
    private String toBankBranchId;
    private String result;
    private String mode;
    private BigDecimal accountBalance;
    private String saveMode;
    private String modeOfCollection;
    private String fromBankBalance;
    private String toBankBalance;
    private String modeOfPayment;
    private String documentNumber;
    private Date documentDate;
    private Long documentId;
    private String linkReferenceNumber;
    private String paidTo;
    private String billNumber;
    private Integer subledgerType;
    private Date billDate;
    private Integer billSubType;
    private String inwardSerialNumber;
    private String partyBillNumber;
    private Date partyBillDate;
    private Map checkListValuesMap;
    private String functionName;
    private Integer functionId;
    private String payto;
    private Long stateId;
    private String budgetReappNo;

    public Long getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(final Long recoveryId) {
        this.recoveryId = recoveryId;
    }

    private Long recoveryId;

    public String getBudgetReappNo() {
        return budgetReappNo;
    }

    public void setBudgetReappNo(final String budgetReappNo) {
        this.budgetReappNo = budgetReappNo;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(final Long stateId) {
        this.stateId = stateId;
    }

    public String getPayto() {
        return payto;
    }

    public void setPayto(final String payto) {
        this.payto = payto;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Integer functionId) {
        this.functionId = functionId;
    }

    public String getInwardSerialNumber() {
        return inwardSerialNumber;
    }

    public void setInwardSerialNumber(final String inwardSerialNumber) {
        this.inwardSerialNumber = inwardSerialNumber;
    }

    public String getPartyBillNumber() {
        return partyBillNumber;
    }

    public void setPartyBillNumber(final String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
    }

    public Date getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(final Date partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public Map getCheckListValuesMap() {
        return checkListValuesMap;
    }

    public void setCheckListValuesMap(final Map checkListValues) {
        checkListValuesMap = checkListValues;
    }

    /**
     * @return the fromBankBalance
     */
    public String getFromBankBalance() {
        return fromBankBalance;
    }

    /**
     * @param fromBankBalance the fromBankBalance to set
     */
    public void setFromBankBalance(final String fromBankBalance) {
        this.fromBankBalance = fromBankBalance;
    }

    /**
     * @return the toBankBalance
     */
    public String getToBankBalance() {
        return toBankBalance;
    }

    /**
     * @param toBankBalance the toBankBalance to set
     */
    public void setToBankBalance(final String toBankBalance) {
        this.toBankBalance = toBankBalance;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(final String saveMode) {
        this.saveMode = saveMode;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(final BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getFromBankId() {
        return fromBankId;
    }

    public void setFromBankId(final String fromBankId) {
        this.fromBankId = fromBankId;
    }

    public String getToBankId() {
        return toBankId;
    }

    public void setToBankId(final String toBankId) {
        this.toBankId = toBankId;
    }

    public String getFromBankBranchId() {
        return fromBankBranchId;
    }

    public void setFromBankBranchId(final String fromBankBranchId) {
        this.fromBankBranchId = fromBankBranchId;
    }

    public String getToBankBranchId() {
        return toBankBranchId;
    }

    public void setToBankBranchId(final String toBankBranchId) {
        this.toBankBranchId = toBankBranchId;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(final String chequeDate) {
        this.chequeDate = chequeDate;
    }

    private String toBankAccountId;

    public String getFromBankAccountId() {
        return fromBankAccountId;
    }

    public void setFromBankAccountId(final String fromBankAccountId) {
        this.fromBankAccountId = fromBankAccountId;
    }

    public String getToBankAccountId() {
        return toBankAccountId;
    }

    public void setToBankAccountId(final String toBankAccountId) {
        this.toBankAccountId = toBankAccountId;
    }

    public String getChequeInHand() {
        return chequeInHand;
    }

    public void setChequeInHand(final String chequeInHand) {
        this.chequeInHand = chequeInHand;
    }

    public String getCashInHand() {
        return cashInHand;
    }

    public void setCashInHand(final String cashInHand) {
        this.cashInHand = cashInHand;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getAccountNumberId() {
        return accountNumberId;
    }

    public void setAccountNumberId(final String accountNumberId) {
        this.accountNumberId = accountNumberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getBoundaryLevel() {
        return boundaryLevel;
    }

    public void setBoundaryLevel(final String boundaryLevel) {
        this.boundaryLevel = boundaryLevel;
    }

    public String getAccnumnar() {
        return accnumnar;
    }

    public void setAccnumnar(final String accnumnar) {
        this.accnumnar = accnumnar;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(final BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(final String bankId) {
        this.bankId = bankId;
    }

    public String getModeOfCollection() {
        return modeOfCollection;
    }

    public void setModeOfCollection(final String modeOfCollection) {
        this.modeOfCollection = modeOfCollection;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(final String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(final Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(final String paidTo) {
        this.paidTo = paidTo;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public Integer getSubledgerType() {
        return subledgerType;
    }

    public void setSubledgerType(final Integer subledgerType) {
        this.subledgerType = subledgerType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(final Date billDate) {
        this.billDate = billDate;
    }

    public Integer getBillSubType() {
        return billSubType;
    }

    public void setBillSubType(final Integer billSubType) {
        this.billSubType = billSubType;
    }

    public void reset() {
        chequeInHand = null;
        cashInHand = null;
        bankBranchId = null;
        accountNumberId = null;
        fromBankAccountId = null;
        chequeNumber = null;
        chequeDate = null;
        fromBankId = null;
        bankId = null;
        toBankId = null;
        amount = null;
        boundaryLevel = null;
        accnumnar = null;
        availableBalance = null;
        fromBankBranchId = null;
        toBankBranchId = null;
        toBankAccountId = null;
        modeOfCollection = FinancialConstants.MODEOFCOLLECTION_CHEQUE;
        fromBankBalance = null;
        toBankBalance = null;
        modeOfPayment = FinancialConstants.MODEOFPAYMENT_CHEQUE;
        documentNumber = null;
        documentDate = null;
        paidTo = null;
        billNumber = null;
        billDate = null;
        billSubType = null;
        subledgerType = null;
        inwardSerialNumber = null;
        partyBillNumber = null;
        partyBillDate = null;
        // this.checkListValuesMap=null;
        functionName = null;
        functionId = null;
        payto = null;
        budgetReappNo = null;
        recoveryId = null;
        documentId = null;// this is the id of linkrefrenceVouchernumber
        linkReferenceNumber = "";
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final Long documentId) {
        this.documentId = documentId;
    }

    public String getLinkReferenceNumber() {
        return linkReferenceNumber;
    }

    public void setLinkReferenceNumber(final String linkReferenceNumber) {
        this.linkReferenceNumber = linkReferenceNumber;
    }
}
