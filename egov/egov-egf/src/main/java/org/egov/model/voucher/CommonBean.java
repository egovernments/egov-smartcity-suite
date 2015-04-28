/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.model.voucher;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.egov.utils.FinancialConstants;

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
	public void setRecoveryId(Long recoveryId) {
		this.recoveryId = recoveryId;
	}
	private Long recoveryId;
	public String getBudgetReappNo() {
		return budgetReappNo;
	}
	
	public void setBudgetReappNo(String budgetReappNo) {   
		this.budgetReappNo = budgetReappNo;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getPayto() {
		return payto;
	}
	public void setPayto(String payto) {
		this.payto = payto;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public String getInwardSerialNumber() {
		return inwardSerialNumber;
	}
	public void setInwardSerialNumber(String inwardSerialNumber) {
		this.inwardSerialNumber = inwardSerialNumber;
	}
	public String getPartyBillNumber() {
		return partyBillNumber;
	}
	public void setPartyBillNumber(String partyBillNumber) {
		this.partyBillNumber = partyBillNumber;
	}
	public Date getPartyBillDate() {
		return partyBillDate;
	}
	public void setPartyBillDate(Date partyBillDate) {
		this.partyBillDate = partyBillDate;
	}
	public Map getCheckListValuesMap() {
		return checkListValuesMap;
	}
	public void setCheckListValuesMap(Map checkListValues) {
		this.checkListValuesMap = checkListValues;
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
	public void setFromBankBalance(String fromBankBalance) {
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
	public void setToBankBalance(String toBankBalance) {
		this.toBankBalance = toBankBalance;
	}
	public String getSaveMode() {
		return saveMode;
	}
	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getFromBankId() {
		return fromBankId;
	}
	public void setFromBankId(String fromBankId) {
		this.fromBankId = fromBankId;
	}
	public String getToBankId() {
		return toBankId;
	}
	public void setToBankId(String toBankId) {
		this.toBankId = toBankId;
	}
	public String getFromBankBranchId() {
		return fromBankBranchId;
	}
	public void setFromBankBranchId(String fromBankBranchId) {
		this.fromBankBranchId = fromBankBranchId;
	}
	public String getToBankBranchId() {
		return toBankBranchId;
	}
	public void setToBankBranchId(String toBankBranchId) {
		this.toBankBranchId = toBankBranchId;
	}
	
	
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	private String toBankAccountId;
	public String getFromBankAccountId() {
		return fromBankAccountId;
	}
	public void setFromBankAccountId(String fromBankAccountId) {
		this.fromBankAccountId = fromBankAccountId;
	}
	public String getToBankAccountId() {
		return toBankAccountId;
	}
	public void setToBankAccountId(String toBankAccountId) {
		this.toBankAccountId = toBankAccountId;
	}
	
	public String getChequeInHand() {
		return chequeInHand;
	}
	public void setChequeInHand(String chequeInHand) {
		this.chequeInHand = chequeInHand;
	}
	public String getCashInHand() {
		return cashInHand;
	}
	public void setCashInHand(String cashInHand) {
		this.cashInHand = cashInHand;
	}
	public String getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(String bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	public String getAccountNumberId() {
		return accountNumberId;
	}
	public void setAccountNumberId(String accountNumberId) {
		this.accountNumberId = accountNumberId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBoundaryLevel() {
		return boundaryLevel;
	}
	public void setBoundaryLevel(String boundaryLevel) {
		this.boundaryLevel = boundaryLevel;
	}
	public String getAccnumnar() {
		return accnumnar;
	}
	public void setAccnumnar(String accnumnar) {
		this.accnumnar = accnumnar;
	}
	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getModeOfCollection() {
		return modeOfCollection;
	}
	public void setModeOfCollection(String modeOfCollection) {
		this.modeOfCollection = modeOfCollection;
	}
	
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public String getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Integer getSubledgerType() {
		return subledgerType;
	}
	public void setSubledgerType(Integer subledgerType) {
		this.subledgerType = subledgerType;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Integer getBillSubType() {
		return billSubType;
	}
	public void setBillSubType(Integer billSubType) {
		this.billSubType = billSubType;
	}
	public  void reset() {
		this.chequeInHand = null;
		this.cashInHand = null;
		this.bankBranchId = null;
		this.accountNumberId = null;
		this.fromBankAccountId = null;
		this.chequeNumber = null;
		this.chequeDate = null;
		this.fromBankId = null;
		this.bankId = null;
		this.toBankId = null;
		this.amount = null;
		this.boundaryLevel = null;
		this.accnumnar = null;
		this.availableBalance = null;
		this.fromBankBranchId = null;
		this.toBankBranchId = null;
		this.toBankAccountId = null;
		this.modeOfCollection=FinancialConstants.MODEOFCOLLECTION_CHEQUE;
		this.fromBankBalance=null;
		this.toBankBalance=null;
		this.modeOfPayment=FinancialConstants.MODEOFPAYMENT_CHEQUE;
		this.documentNumber=null;
		this.documentDate=null;
		this.paidTo=null;
		this.billNumber=null;
		this.billDate=null;
		this.billSubType=null;
		this.subledgerType=null;
		this.inwardSerialNumber=null;
		this.partyBillNumber=null;
		this.partyBillDate=null;
		//this.checkListValuesMap=null; 
		this.functionName=null;
		this.functionId=null;
		this.payto=null;
		this.budgetReappNo=null;
		this.recoveryId=null;  
	    this.documentId=null;//this is the id of linkrefrenceVouchernumber	
		this.linkReferenceNumber="";
	}
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public String getLinkReferenceNumber() {
		return linkReferenceNumber;
	}
	public void setLinkReferenceNumber(String linkReferenceNumber) {
		this.linkReferenceNumber = linkReferenceNumber;
	}
}
