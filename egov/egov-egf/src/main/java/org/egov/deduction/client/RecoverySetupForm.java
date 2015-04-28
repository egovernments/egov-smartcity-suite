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
 * Created on Oct 1, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.deduction.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Iliyaraja
 */
public class RecoverySetupForm extends ActionForm {
	
	public String[] getDocType() {
		return docType;
	}
	
	public void setDocType(String[] docType) {
		this.docType = docType;
	}
	
	public String[] getEduCessPercentage() {
		return eduCessPercentage;
	}
	
	public void setEduCessPercentage(String[] eduCessPercentage) {
		this.eduCessPercentage = eduCessPercentage;
	}
	
	public String[] getHighAmount() {
		return highAmount;
	}
	
	public void setHighAmount(String[] highAmount) {
		this.highAmount = highAmount;
	}
	
	public String[] getId() {
		return id;
	}
	
	public void setId(String[] id) {
		this.id = id;
	}
	
	public String[] getITPercentage() {
		return ITPercentage;
	}
	
	public void setITPercentage(String[] percentage) {
		ITPercentage = percentage;
	}
	
	public String[] getLowAmount() {
		return lowAmount;
	}
	
	public void setLowAmount(String[] lowAmount) {
		this.lowAmount = lowAmount;
	}
	
	public String[] getPartyType() {
		return partyType;
	}
	
	public void setPartyType(String[] partyType) {
		this.partyType = partyType;
	}
	
	public String getRecAccDesc() {
		return recAccDesc;
	}
	
	public void setRecAccDesc(String recAccDesc) {
		this.recAccDesc = recAccDesc;
	}
	
	public String getRecovAccCodeId() {
		return recovAccCodeId;
	}
	
	public void setRecovAccCodeId(String recovAccCodeId) {
		this.recovAccCodeId = recovAccCodeId;
	}
	
	public String getRecovAppliedTo() {
		return recovAppliedTo;
	}
	
	public void setRecovAppliedTo(String recovAppliedTo) {
		this.recovAppliedTo = recovAppliedTo;
	}
	
	public String getRecovBSRCode() {
		return recovBSRCode;
	}
	
	public void setRecovBSRCode(String recovBSRCode) {
		this.recovBSRCode = recovBSRCode;
	}
	
	public String getRecovCode() {
		return recovCode;
	}
	
	public void setRecovCode(String recovCode) {
		this.recovCode = recovCode;
	}
	
	public String[] getRecovDateFrom() {
		return recovDateFrom;
	}
	
	public void setRecovDateFrom(String[] recovDateFrom) {
		this.recovDateFrom = recovDateFrom;
	}
	
	public String[] getRecovDateTo() {
		return recovDateTo;
	}
	
	public void setRecovDateTo(String[] recovDateTo) {
		this.recovDateTo = recovDateTo;
	}
	
	public String getRecovName() {
		return recovName;
	}
	
	public void setRecovName(String recovName) {
		this.recovName = recovName;
	}
	
	public String getRecovRemitTo() {
		return recovRemitTo;
	}
	
	public void setRecovRemitTo(String recovRemitTo) {
		this.recovRemitTo = recovRemitTo;
	}
	
	public String[] getSubType() {
		return subType;
	}
	
	public void setSubType(String[] subType) {
		this.subType = subType;
	}
	
	public String[] getSurPercentage() {
		return surPercentage;
	}
	
	public void setSurPercentage(String[] surPercentage) {
		this.surPercentage = surPercentage;
	}
	
	public String[] getTotalPercentage() {
		return totalPercentage;
	}
	
	public void setTotalPercentage(String[] totalPercentage) {
		this.totalPercentage = totalPercentage;
	}
	
	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}
	
	/**
	 * @param bank
	 *            the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	/**
	 * @return the bankLoan
	 */
	public String getBankLoan() {
		return bankLoan;
	}
	
	/**
	 * @param bankLoan
	 *            the bankLoan to set
	 */
	public void setBankLoan(String bankLoan) {
		this.bankLoan = bankLoan;
	}
	
	
	public String getTdsIdHidden() {
		return tdsIdHidden;
	}
	
	public void setTdsIdHidden(String tdsIdHidden) {
		this.tdsIdHidden = tdsIdHidden;
	}
	
	public String getTdsTypeId() {
		return tdsTypeId;
	}
	
	public void setTdsTypeId(String tdsTypeId) {
		this.tdsTypeId = tdsTypeId;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest req) {
		this.subType = null;
		
		this.recovDateFrom = null;
		
		this.recovDateTo = null;
		
		this.lowAmount = null;
		
		this.highAmount = null;
		
		this.ITPercentage = null;
		
		this.surPercentage = null;
		
		this.eduCessPercentage = null;
	}
	
	public String[] getFlatAmount() {
		return flatAmount;
	}
	
	public void setFlatAmount(String[] flatAmount) {
		this.flatAmount = flatAmount;
	}
	
	public String[] getAppliedToHiddenId() {
		return appliedToHiddenId;
	}
	
	public void setAppliedToHiddenId(String[] appliedToHiddenId) {
		this.appliedToHiddenId = appliedToHiddenId;
	}
	
	/**
	 * @return the capLimit
	 */
	public String getCapLimit() {
		return capLimit;
	}
	
	/**
	 * @param capLimit
	 *            the capLimit to set
	 */
	public void setCapLimit(String capLimit) {
		this.capLimit = capLimit;
	}
	
	public String getIsEarning() {
		return isEarning;
	}
	
	public void setIsEarning(String isEarning) {
		this.isEarning = isEarning;
	}
	
	public String getEmprecovAccCodeId() {
		return emprecovAccCodeId;
	}
	
	public void setEmprecovAccCodeId(String emprecovAccCodeId) {
		this.emprecovAccCodeId = emprecovAccCodeId;
	}
	
	public String getEmprecAccDesc() {
		return emprecAccDesc;
	}
	
	public void setEmprecAccDesc(String emprecAccDesc) {
		this.emprecAccDesc = emprecAccDesc;
	}
	
	public String getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}

	public String getPayTo() {
		return payTo;
	}

	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getCumulativeAmountLow() {
		return cumulativeAmountLow;
	}

	public void setCumulativeAmountLow(String[] cumulativeAmountLow) {
		this.cumulativeAmountLow = cumulativeAmountLow;
	}

	public String[] getCumulativeAmountHigh() {
		return cumulativeAmountHigh;
	}

	public void setCumulativeAmountHigh(String[] cumulativeAmountHigh) {
		this.cumulativeAmountHigh = cumulativeAmountHigh;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
	public String getRecMode() {
		return recMode;
	}

	public void setRecMode(String recoverymode) {
		this.recMode = recoverymode;
	}

	public String getRecovIFSCCode() {
		return recovIFSCCode;
	}

	public void setRecovIFSCCode(String recovIFSCCode) {
		this.recovIFSCCode = recovIFSCCode;
	}

	public String getRecovBankAccount() {
		return recovBankAccount;
	}

	public void setRecovBankAccount(String recovBankAccount) {
		this.recovBankAccount = recovBankAccount;
	}

	private String[] id;
	private String recovCode;
	private String recovName;
	private String recovAccCodeId;
	private String recAccDesc;
	private String emprecovAccCodeId;
	private String emprecAccDesc;
	private String recovAppliedTo;
	private String recovRemitTo;
	private String recovBSRCode;
	private String recMode;
	private String[] appliedToHiddenId;
	private String[] partyType;
	private String[] docType;
	private String[] subType;
	private String[] recovDateFrom;
	private String[] recovDateTo;
	
	private String[] lowAmount;
	private String[] highAmount;
	private String[] ITPercentage;
	private String[] surPercentage;
	private String[] eduCessPercentage;
	private String[] totalPercentage;
	private String[] flatAmount;
	
	private String tdsTypeId;
	private String tdsIdHidden;
	
	
	private String bankLoan;
	private String bank;
	
	private String capLimit;
	private String isEarning;
	private String calculationType;
	private String payTo;
	private String description;

	private String[] cumulativeAmountLow;
	private String[] cumulativeAmountHigh;
	
	private String section;
	
	private String recovIFSCCode;
	private String recovBankAccount;

}
