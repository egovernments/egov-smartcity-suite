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
/*
 * DishonoredChequeForm.java  Created on May 31, 2008
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

import org.apache.struts.action.ActionForm;

/**
 * @author Iliyaraja S
 *
 * @Version 1.00
 */
public class DishonoredChequeForm extends ActionForm
{

	private String bankId;
	private String paramTxnDate;
	private String passVoucherId;
	private String passPayinVHId;
	private String passFundId;
	private String passFunctionId;
	private String passFundSrcId;
	private String refNo;// chqno
	private String passRefNo;
	private String glcodeChList;
	private String instrumentHeaderId;
	private String instrumentMode;

	private String accCodedescCh;
	private String glcodeBkId;
	private String glcodeForBank;
	private String accCodedescBk;
	private String accId;
	private String chequeNo;
	private String bankFromDate;
	private String bankToDate;
	private String chqReason;
	private String bankChReason;
	private String selected;

	private String bankTotalAmt;
	private String todayDateBk;
	private String voucherTypeParam;
	private String passedAmount;
	private String voucherTxnDate;
	private String chqReasonP;
	private String passAccId;
	private boolean isReceiptInstrument;  
	
	
	private String selectedVhid;
	private String selectedRefNo;
	private Integer selectedIndex;
	private String selectedBankCharges;

	

	private String[] chqAmt;
	private String[] bankChargeAmt;
	private String[] passVoucher;
	private String[] postTxn;
	//private String[] refNo;
	private String[] passChqDate;
	private String[] passChqNo;
	private String[] voucherId;

	private String[] glcodeChId;
	private String[] functionChId;

	private String[] functionCode;
	private String[] glcodeChIdP;
	public Integer getSelectedIndex() {
		return selectedIndex;
	}
	public void setSelectedIndex(Integer selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	private String[] debitAmount;
	private String[] creditAmount;
	private String[] chqTotalAmtP;
	private String departmentId;
	private String functionaryId;

	private String functionId;
	private String subledgerDetails;




	//voucher header details for reversal voucher
	private String vouHName;
	private String voucherNumber;
	private String vouDate;
	private String fund;
	private String refDate;//chqdate
	private String reason="";

	//voucher header details for bank charges
	private String vouHNameBC;
	private String voucherNumberBC;
	private String vouDateBC;
	private String reasonBC;


	//Account code details for reversal voucher
	private String[] reversalAccCode;
	private String[] reversalDescn;
	private String[] reversalDebitAmount;
	private String[] reversalCreditAmount;

	//Account code details for bank charges
	private String[] reversalAccCodeBC;
	private String[] reversalDescnBC;
	private String[] reversalDebitAmountBC;
	private String[] reversalCreditAmountBC;


	private String reversalVoucherNumber;
	private String bankChargesVoucherNumber;

	private Integer approverDept;
	private Integer approverDesig;
	private Integer approverEmpAssignmentId;
	private String dishonorReasons;



	public String getDishonorReasons() {
		return dishonorReasons;
	}
	public void setDishonorReasons(String dishonorReasons) {
		this.dishonorReasons = dishonorReasons;
	}
	public boolean isReceiptInstrument() {
		return isReceiptInstrument;
	}
	public void setReceiptInstrument(boolean isReceiptInstrument) {
		this.isReceiptInstrument = isReceiptInstrument;
	}
	public String getAccCodedescBk() {
		return accCodedescBk;
	}
	public void setAccCodedescBk(String accCodedescBk) {
		this.accCodedescBk = accCodedescBk;
	}
	public String getAccCodedescCh() {
		return accCodedescCh;
	}
	public void setAccCodedescCh(String accCodedescCh) {
		this.accCodedescCh = accCodedescCh;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String[] getBankChargeAmt() {
		return bankChargeAmt;
	}
	public void setBankChargeAmt(String[] bankChargeAmt) {
		this.bankChargeAmt = bankChargeAmt;
	}
	public String getBankChReason() {
		return bankChReason;
	}
	public void setBankChReason(String bankChReason) {
		this.bankChReason = bankChReason;
	}
	public String getBankFromDate() {
		return bankFromDate;
	}
	public void setBankFromDate(String bankFromDate) {
		this.bankFromDate = bankFromDate;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankToDate() {
		return bankToDate;
	}
	public void setBankToDate(String bankToDate) {
		this.bankToDate = bankToDate;
	}
	public String getBankTotalAmt() {
		return bankTotalAmt;
	}
	public void setBankTotalAmt(String bankTotalAmt) {
		this.bankTotalAmt = bankTotalAmt;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getInstrumentMode() {
		return instrumentMode;
	}
	public void setInstrumentMode(String instrumentMode) {
		this.instrumentMode = instrumentMode;
	}
	public String[] getChqAmt() {
		return chqAmt;
	}
	public void setChqAmt(String[] chqAmt) {
		this.chqAmt = chqAmt;
	}
	public String getChqReason() {
		return chqReason;
	}
	public void setChqReason(String chqReason) {
		this.chqReason = chqReason;
	}
	public String getChqReasonP() {
		return chqReasonP;
	}
	public void setChqReasonP(String chqReasonP) {
		this.chqReasonP = chqReasonP;
	}

	public String[] getChqTotalAmtP() {
		return chqTotalAmtP;
	}
	public void setChqTotalAmtP(String[] chqTotalAmtP) {
		this.chqTotalAmtP = chqTotalAmtP;
	}
	public String getGlcodeBkId() {
		return glcodeBkId;
	}
	public void setGlcodeBkId(String glcodeBkId) {
		this.glcodeBkId = glcodeBkId;
	}
	public String[] getGlcodeChId() {
		return glcodeChId;
	}
	public void setGlcodeChId(String[] glcodeChId) {
		this.glcodeChId = glcodeChId;
	}
	public String[] getGlcodeChIdP() {
		return glcodeChIdP;
	}
	public void setGlcodeChIdP(String[] glcodeChIdP) {
		this.glcodeChIdP = glcodeChIdP;
	}
	public String getGlcodeChList() {
		return glcodeChList;
	}
	public void setGlcodeChList(String glcodeChList) {
		this.glcodeChList = glcodeChList;
	}
	public String getGlcodeForBank() {
		return glcodeForBank;
	}
	public void setGlcodeForBank(String glcodeForBank) {
		this.glcodeForBank = glcodeForBank;
	}
	public String getParamTxnDate() {
		return paramTxnDate;
	}
	public void setParamTxnDate(String paramTxnDate) {
		this.paramTxnDate = paramTxnDate;
	}
	public String getPassAccId() {
		return passAccId;
	}
	public void setPassAccId(String passAccId) {
		this.passAccId = passAccId;
	}
	public String[] getPassChqDate() {
		return passChqDate;
	}
	public void setPassChqDate(String[] passChqDate) {
		this.passChqDate = passChqDate;
	}
	public String[] getPassChqNo() {
		return passChqNo;
	}
	public String getSelectedVhid() {
		return selectedVhid;
	}
	public String getSelectedRefNo() {
		return selectedRefNo;
	}
	public String getSelectedBankCharges() {
		return selectedBankCharges;
	}
	public void setSelectedVhid(String selectedVhid) {
		this.selectedVhid = selectedVhid;
	}
	public void setSelectedRefNo(String selectedRefNo) {
		this.selectedRefNo = selectedRefNo;
	}
	public void setSelectedBankCharges(String selectedBankCharges) {
		this.selectedBankCharges = selectedBankCharges;
	}
	public void setPassChqNo(String[] passChqNo) {
		this.passChqNo = passChqNo;
	}
	public String getPassedAmount() {
		return passedAmount;
	}
	public void setPassedAmount(String passedAmount) {
		this.passedAmount = passedAmount;
	}
	public String getPassFundId() {
		return passFundId;
	}
	public void setPassFundId(String passFundId) {
		this.passFundId = passFundId;
	}
	public String getPassFundSrcId() {
		return passFundSrcId;
	}
	public void setPassFundSrcId(String passFundSrcId) {
		this.passFundSrcId = passFundSrcId;
	}
	public String getPassPayinVHId() {
		return passPayinVHId;
	}
	public void setPassPayinVHId(String passPayinVHId) {
		this.passPayinVHId = passPayinVHId;
	}
	public String getPassRefNo() {
		return passRefNo;
	}
	public void setPassRefNo(String passRefNo) {
		this.passRefNo = passRefNo;
	}
	public String[] getPassVoucher() {
		return passVoucher;
	}
	public void setPassVoucher(String[] passVoucher) {
		this.passVoucher = passVoucher;
	}
	public String getPassVoucherId() {
		return passVoucherId;
	}
	public void setPassVoucherId(String passVoucherId) {
		this.passVoucherId = passVoucherId;
	}

	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getTodayDateBk() {
		return todayDateBk;
	}
	public void setTodayDateBk(String todayDateBk) {
		this.todayDateBk = todayDateBk;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRefDate() {
		return refDate;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}
	public String[] getReversalAccCode() {
		return reversalAccCode;
	}
	public void setReversalAccCode(String[] reversalAccCode) {
		this.reversalAccCode = reversalAccCode;
	}
	public String[] getReversalCreditAmount() {
		return reversalCreditAmount;
	}
	public void setReversalCreditAmount(String[] reversalCreditAmount) {
		this.reversalCreditAmount = reversalCreditAmount;
	}
	public String[] getReversalDebitAmount() {
		return reversalDebitAmount;
	}
	public void setReversalDebitAmount(String[] reversalDebitAmount) {
		this.reversalDebitAmount = reversalDebitAmount;
	}
	public String[] getReversalDescn() {
		return reversalDescn;
	}
	public void setReversalDescn(String[] reversalDescn) {
		this.reversalDescn = reversalDescn;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getVouDate() {
		return vouDate;
	}
	public void setVouDate(String vouDate) {
		this.vouDate = vouDate;
	}
	public String getVouHName() {
		return vouHName;
	}
	public void setVouHName(String vouHName) {
		this.vouHName = vouHName;
	}
	public String getReasonBC() {
		return reasonBC;
	}
	public void setReasonBC(String reasonBC) {
		this.reasonBC = reasonBC;
	}
	public String[] getReversalAccCodeBC() {
		return reversalAccCodeBC;
	}
	public void setReversalAccCodeBC(String[] reversalAccCodeBC) {
		this.reversalAccCodeBC = reversalAccCodeBC;
	}
	public String[] getReversalCreditAmountBC() {
		return reversalCreditAmountBC;
	}
	public void setReversalCreditAmountBC(String[] reversalCreditAmountBC) {
		this.reversalCreditAmountBC = reversalCreditAmountBC;
	}
	public String[] getReversalDebitAmountBC() {
		return reversalDebitAmountBC;
	}
	public void setReversalDebitAmountBC(String[] reversalDebitAmountBC) {
		this.reversalDebitAmountBC = reversalDebitAmountBC;
	}
	public String[] getReversalDescnBC() {
		return reversalDescnBC;
	}
	public void setReversalDescnBC(String[] reversalDescnBC) {
		this.reversalDescnBC = reversalDescnBC;
	}
	public String getVoucherNumberBC() {
		return voucherNumberBC;
	}
	public void setVoucherNumberBC(String voucherNumberBC) {
		this.voucherNumberBC = voucherNumberBC;
	}
	public String getVouDateBC() {
		return vouDateBC;
	}
	public void setVouDateBC(String vouDateBC) {
		this.vouDateBC = vouDateBC;
	}
	public String getVouHNameBC() {
		return vouHNameBC;
	}
	public void setVouHNameBC(String vouHNameBC) {
		this.vouHNameBC = vouHNameBC;
	}
	public String getVoucherTxnDate() {
		return voucherTxnDate;
	}
	public void setVoucherTxnDate(String voucherTxnDate) {
		this.voucherTxnDate = voucherTxnDate;
	}
	public String getVoucherTypeParam() {
		return voucherTypeParam;
	}
	public void setVoucherTypeParam(String voucherTypeParam) {
		this.voucherTypeParam = voucherTypeParam;
	}
	/**
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return the functionaryId
	 */
	public String getFunctionaryId() {
		return functionaryId;
	}
	/**
	 * @param functionaryId the functionaryId to set
	 */
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String getInstrumentHeaderId() {
		return instrumentHeaderId;
	}
	public void setInstrumentHeaderId(String instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}

	public String getReversalVoucherNumber() {
		return reversalVoucherNumber;
	}
	public void setReversalVoucherNumber(String reversalVoucherNumber) {
		this.reversalVoucherNumber = reversalVoucherNumber;
	}
	public String getBankChargesVoucherNumber() {
		return bankChargesVoucherNumber;
	}
	public void setBankChargesVoucherNumber(String bankChargesVoucherNumber) {
		this.bankChargesVoucherNumber = bankChargesVoucherNumber;
	}
	public String[] getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(String[] debitAmount) {
		this.debitAmount = debitAmount;
	}
	public String[] getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(String[] creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getSubledgerDetails() {
		return subledgerDetails;
	}
	public void setSubledgerDetails(String subledgerDetails) {
		this.subledgerDetails = subledgerDetails;
	}

	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String[] getFunctionChId() {
		return functionChId;
	}

	public void setFunctionChId(String[] functionChId) {
		this.functionChId = functionChId;
	}
	public String[] getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String[] functionCode) {
		this.functionCode = functionCode;
	}
	public String getPassFunctionId() {
		return passFunctionId;
	}
	public void setPassFunctionId(String passFunctionId) {
		this.passFunctionId = passFunctionId;
	}
	public Integer getApproverDept() {
		return approverDept;
	}
	public void setApproverDept(Integer approverDept) {
		this.approverDept = approverDept;
	}
	public Integer getApproverDesig() {
		return approverDesig;
	}
	public void setApproverDesig(Integer approverDesig) {
		this.approverDesig = approverDesig;
	}
	public Integer getApproverEmpAssignmentId() {
		return approverEmpAssignmentId;
	}
	public void setApproverEmpAssignmentId(Integer approverEmpAssignmentId) {
		this.approverEmpAssignmentId = approverEmpAssignmentId;
	}
	public String[] getPostTxn() {
		return postTxn;
	}
	public void setPostTxn(String[] postTxn) {
		this.postTxn = postTxn;
	}
	public String[] getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String[] voucherId) {
		this.voucherId = voucherId;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}

}
