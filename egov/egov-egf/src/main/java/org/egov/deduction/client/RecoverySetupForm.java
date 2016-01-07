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

    /**
     *
     */
    private static final long serialVersionUID = -6329868980764809830L;

    public String[] getDocType() {
        return docType;
    }

    public void setDocType(final String[] docType) {
        this.docType = docType;
    }

    public String[] getEduCessPercentage() {
        return eduCessPercentage;
    }

    public void setEduCessPercentage(final String[] eduCessPercentage) {
        this.eduCessPercentage = eduCessPercentage;
    }

    public String[] getHighAmount() {
        return highAmount;
    }

    public void setHighAmount(final String[] highAmount) {
        this.highAmount = highAmount;
    }

    public String[] getId() {
        return id;
    }

    public void setId(final String[] id) {
        this.id = id;
    }

    public String[] getITPercentage() {
        return ITPercentage;
    }

    public void setITPercentage(final String[] percentage) {
        ITPercentage = percentage;
    }

    public String[] getLowAmount() {
        return lowAmount;
    }

    public void setLowAmount(final String[] lowAmount) {
        this.lowAmount = lowAmount;
    }

    public String[] getPartyType() {
        return partyType;
    }

    public void setPartyType(final String[] partyType) {
        this.partyType = partyType;
    }

    public String getRecAccDesc() {
        return recAccDesc;
    }

    public void setRecAccDesc(final String recAccDesc) {
        this.recAccDesc = recAccDesc;
    }

    public String getRecovAccCodeId() {
        return recovAccCodeId;
    }

    public void setRecovAccCodeId(final String recovAccCodeId) {
        this.recovAccCodeId = recovAccCodeId;
    }

    public String getRecovAppliedTo() {
        return recovAppliedTo;
    }

    public void setRecovAppliedTo(final String recovAppliedTo) {
        this.recovAppliedTo = recovAppliedTo;
    }

    public String getRecovBSRCode() {
        return recovBSRCode;
    }

    public void setRecovBSRCode(final String recovBSRCode) {
        this.recovBSRCode = recovBSRCode;
    }

    public String getRecovCode() {
        return recovCode;
    }

    public void setRecovCode(final String recovCode) {
        this.recovCode = recovCode;
    }

    public String[] getRecovDateFrom() {
        return recovDateFrom;
    }

    public void setRecovDateFrom(final String[] recovDateFrom) {
        this.recovDateFrom = recovDateFrom;
    }

    public String[] getRecovDateTo() {
        return recovDateTo;
    }

    public void setRecovDateTo(final String[] recovDateTo) {
        this.recovDateTo = recovDateTo;
    }

    public String getRecovName() {
        return recovName;
    }

    public void setRecovName(final String recovName) {
        this.recovName = recovName;
    }

    public String getRecovRemitTo() {
        return recovRemitTo;
    }

    public void setRecovRemitTo(final String recovRemitTo) {
        this.recovRemitTo = recovRemitTo;
    }

    public String[] getSubType() {
        return subType;
    }

    public void setSubType(final String[] subType) {
        this.subType = subType;
    }

    public String[] getSurPercentage() {
        return surPercentage;
    }

    public void setSurPercentage(final String[] surPercentage) {
        this.surPercentage = surPercentage;
    }

    public String[] getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(final String[] totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    /**
     * @return the bank
     */
    public String getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(final String bank) {
        this.bank = bank;
    }

    /**
     * @return the bankLoan
     */
    public String getBankLoan() {
        return bankLoan;
    }

    /**
     * @param bankLoan the bankLoan to set
     */
    public void setBankLoan(final String bankLoan) {
        this.bankLoan = bankLoan;
    }

    public String getTdsIdHidden() {
        return tdsIdHidden;
    }

    public void setTdsIdHidden(final String tdsIdHidden) {
        this.tdsIdHidden = tdsIdHidden;
    }

    public String getTdsTypeId() {
        return tdsTypeId;
    }

    public void setTdsTypeId(final String tdsTypeId) {
        this.tdsTypeId = tdsTypeId;
    }

    @Override
    public void reset(final ActionMapping mapping, final HttpServletRequest req) {
        subType = null;

        recovDateFrom = null;

        recovDateTo = null;

        lowAmount = null;

        highAmount = null;

        ITPercentage = null;

        surPercentage = null;

        eduCessPercentage = null;
    }

    public String[] getFlatAmount() {
        return flatAmount;
    }

    public void setFlatAmount(final String[] flatAmount) {
        this.flatAmount = flatAmount;
    }

    public String[] getAppliedToHiddenId() {
        return appliedToHiddenId;
    }

    public void setAppliedToHiddenId(final String[] appliedToHiddenId) {
        this.appliedToHiddenId = appliedToHiddenId;
    }

    /**
     * @return the capLimit
     */
    public String getCapLimit() {
        return capLimit;
    }

    /**
     * @param capLimit the capLimit to set
     */
    public void setCapLimit(final String capLimit) {
        this.capLimit = capLimit;
    }

    public String getIsEarning() {
        return isEarning;
    }

    public void setIsEarning(final String isEarning) {
        this.isEarning = isEarning;
    }

    public String getEmprecovAccCodeId() {
        return emprecovAccCodeId;
    }

    public void setEmprecovAccCodeId(final String emprecovAccCodeId) {
        this.emprecovAccCodeId = emprecovAccCodeId;
    }

    public String getEmprecAccDesc() {
        return emprecAccDesc;
    }

    public void setEmprecAccDesc(final String emprecAccDesc) {
        this.emprecAccDesc = emprecAccDesc;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(final String calculationType) {
        this.calculationType = calculationType;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(final String payTo) {
        this.payTo = payTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String[] getCumulativeAmountLow() {
        return cumulativeAmountLow;
    }

    public void setCumulativeAmountLow(final String[] cumulativeAmountLow) {
        this.cumulativeAmountLow = cumulativeAmountLow;
    }

    public String[] getCumulativeAmountHigh() {
        return cumulativeAmountHigh;
    }

    public void setCumulativeAmountHigh(final String[] cumulativeAmountHigh) {
        this.cumulativeAmountHigh = cumulativeAmountHigh;
    }

    public String getSection() {
        return section;
    }

    public void setSection(final String section) {
        this.section = section;
    }

    public String getRecMode() {
        return recMode;
    }

    public void setRecMode(final String recoverymode) {
        recMode = recoverymode;
    }

    public String getRecovIFSCCode() {
        return recovIFSCCode;
    }

    public void setRecovIFSCCode(final String recovIFSCCode) {
        this.recovIFSCCode = recovIFSCCode;
    }

    public String getRecovBankAccount() {
        return recovBankAccount;
    }

    public void setRecovBankAccount(final String recovBankAccount) {
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
