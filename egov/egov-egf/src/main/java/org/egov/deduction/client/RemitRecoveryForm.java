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
 * RemitRecoveryForm.java Created on Oct 1, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.client;

import org.apache.struts.action.ActionForm;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Sathish P
 * @version 1.00
 */

public class RemitRecoveryForm extends ActionForm
{
    /**
     *
     */
    private static final long serialVersionUID = -6823935171069641448L;
    private String fund;
    private String recovery;
    private String month;
    private String year;
    private String[] remittanceGldtlId;
    private String[] relationId;
    private String[] partyName;
    private String[] partyPAN;
    private String[] partyAddress;
    private String[] refNo;
    private String[] refDate;
    private String[] dedAmount;
    private String[] remittAmt;
    private String totalDedAmt;
    private String totalRemittAmt;
    private String remitTo;
    private String pymntVhNo;
    private String pymntVhDate;
    private String bank;
    private String bankAccount;
    private String chequeNo;
    private String chequeDate;

    private String fromDate;
    private String toDate;
    private String vhNo;
    private String mode;
    private String remittanceId;
    private Boolean isChqSurrendered = false;
    private String newChequeNo;
    private String newChequeDate;
    private String pymntVhNoPrefix;

    /**
     * @return the remittanceId
     */
    public String getRemittanceId()
    {
        return remittanceId;
    }

    /**
     * @param remittanceId the remittanceId to set
     */
    public void setRemittanceId(final String remittanceId)
    {
        this.remittanceId = remittanceId;
    }

    /**
     * @return the bank
     */
    public String getBank()
    {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(final String bank)
    {
        this.bank = bank;
    }

    /**
     * @return the bankAccount
     */
    public String getBankAccount()
    {
        return bankAccount;
    }

    /**
     * @param bankAccount the bankAccount to set
     */
    public void setBankAccount(final String bankAccount)
    {
        this.bankAccount = bankAccount;
    }

    /**
     * @return the chequeDate
     */
    public String getChequeDate()
    {
        return chequeDate;
    }

    /**
     * @param chequeDate the chequeDate to set
     */
    public void setChequeDate(final String chequeDate)
    {
        this.chequeDate = chequeDate;
    }

    /**
     * @return the chequeNo
     */
    public String getChequeNo()
    {
        return chequeNo;
    }

    /**
     * @param chequeNo the chequeNo to set
     */
    public void setChequeNo(final String chequeNo)
    {
        this.chequeNo = chequeNo;
    }

    /**
     * @return the dedAmount
     */
    public String[] getDedAmount()
    {
        return dedAmount;
    }

    /**
     * @param dedAmount the dedAmount to set
     */
    public void setDedAmount(final String[] dedAmount)
    {
        this.dedAmount = dedAmount;
    }

    /**
     * @return the fund
     */
    public String getFund()
    {
        return fund;
    }

    /**
     * @param fund the fund to set
     */
    public void setFund(final String fund)
    {
        this.fund = fund;
    }

    /**
     * @return the month
     */
    public String getMonth()
    {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(final String month)
    {
        this.month = month;
    }

    /**
     * @return the partyAddress
     */
    public String[] getPartyAddress()
    {
        return partyAddress;
    }

    /**
     * @param partyAddress the partyAddress to set
     */
    public void setPartyAddress(final String[] partyAddress)
    {
        this.partyAddress = partyAddress;
    }

    /**
     * @return the partyName
     */
    public String[] getPartyName()
    {
        return partyName;
    }

    /**
     * @param partyName the partyName to set
     */
    public void setPartyName(final String[] partyName)
    {
        this.partyName = partyName;
    }

    /**
     * @return the partyPAN
     */
    public String[] getPartyPAN()
    {
        return partyPAN;
    }

    /**
     * @param partyPAN the partyPAN to set
     */
    public void setPartyPAN(final String[] partyPAN)
    {
        this.partyPAN = partyPAN;
    }

    /**
     * @return the pymntVhDate
     */
    public String getPymntVhDate()
    {
        return pymntVhDate;
    }

    /**
     * @param pymntVhDate the pymntVhDate to set
     */
    public void setPymntVhDate(final String pymntVhDate)
    {
        this.pymntVhDate = pymntVhDate;
    }

    /**
     * @return the pymntVhNo
     */
    public String getPymntVhNo()
    {
        return pymntVhNo;
    }

    /**
     * @param pymntVhNo the pymntVhNo to set
     */
    public void setPymntVhNo(final String pymntVhNo)
    {
        this.pymntVhNo = pymntVhNo;
    }

    /**
     * @return the recovery
     */
    public String getRecovery()
    {
        return recovery;
    }

    /**
     * @param recovery the recovery to set
     */
    public void setRecovery(final String recovery)
    {
        this.recovery = recovery;
    }

    /**
     * @return the refDate
     */
    public String[] getRefDate()
    {
        return refDate;
    }

    /**
     * @param refDate the refDate to set
     */
    public void setRefDate(final String[] refDate)
    {
        this.refDate = refDate;
    }

    /**
     * @return the refNo
     */
    public String[] getRefNo()
    {
        return refNo;
    }

    /**
     * @param refNo the refNo to set
     */
    public void setRefNo(final String[] refNo)
    {
        this.refNo = refNo;
    }

    /**
     * @return the remitTo
     */
    public String getRemitTo()
    {
        return remitTo;
    }

    /**
     * @param remitTo the remitTo to set
     */
    public void setRemitTo(final String remitTo)
    {
        this.remitTo = remitTo;
    }

    /**
     * @return the year
     */
    public String getYear()
    {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(final String year)
    {
        this.year = year;
    }

    /**
     * @return the totalDedAmt
     */
    public String getTotalDedAmt()
    {
        return totalDedAmt;
    }

    /**
     * @param totalDedAmt the totalDedAmt to set
     */
    public void setTotalDedAmt(final String totalDedAmt)
    {
        this.totalDedAmt = totalDedAmt;
    }

    /**
     * @return the totalRemittAmt
     */
    public String getTotalRemittAmt()
    {
        return totalRemittAmt;
    }

    /**
     * @param totalRemittAmt the totalRemittAmt to set
     */
    public void setTotalRemittAmt(final String totalRemittAmt)
    {
        this.totalRemittAmt = totalRemittAmt;
    }

    /**
     * @return the remittAmt
     */
    public String[] getRemittAmt()
    {
        return remittAmt;
    }

    /**
     * @param remittAmt the remittAmt to set
     */
    public void setRemittAmt(final String[] remittAmt)
    {
        this.remittAmt = remittAmt;
    }

    /**
     * @return the remittanceGldtlId
     */
    public String[] getRemittanceGldtlId()
    {
        return remittanceGldtlId;
    }

    /**
     * @param remittanceGldtlId the remittanceGldtlId to set
     */
    public void setRemittanceGldtlId(final String[] remittanceGldtlId)
    {
        this.remittanceGldtlId = remittanceGldtlId;
    }

    /**
     * @return the relationId
     */
    public String[] getRelationId()
    {
        return relationId;
    }

    /**
     * @param relationId the relationId to set
     */
    public void setRelationId(final String[] relationId)
    {
        this.relationId = relationId;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate()
    {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(final String fromDate)
    {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate()
    {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(final String toDate)
    {
        this.toDate = toDate;
    }

    /**
     * @return the vhNo
     */
    public String getVhNo()
    {
        return vhNo;
    }

    /**
     * @param vhNo the vhNo to set
     */
    public void setVhNo(final String vhNo)
    {
        this.vhNo = vhNo;
    }

    /**
     * @return the mode
     */
    public String getMode()
    {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(final String mode)
    {
        this.mode = mode;
    }

    /**
     * @return the isChqSurrendered
     */
    public Boolean getIsChqSurrendered()
    {
        return isChqSurrendered;
    }

    /**
     * @param isChqSurrendered the isChqSurrendered to set
     */
    public void setIsChqSurrendered(final Boolean isChqSurrendered)
    {
        this.isChqSurrendered = isChqSurrendered;
    }

    /**
     * @return the pymntVhNoPrefix
     */
    public String getPymntVhNoPrefix()
    {
        return pymntVhNoPrefix;
    }

    /**
     * @param pymntVhNoPrefix the pymntVhNoPrefix to set
     */
    public void setPymntVhNoPrefix(final String pymntVhNoPrefix)
    {
        this.pymntVhNoPrefix = pymntVhNoPrefix;
    }

    /**
     * @return the newChequeDate
     */
    public String getNewChequeDate()
    {
        return newChequeDate;
    }

    /**
     * @param newChequeDate the newChequeDate to set
     */
    public void setNewChequeDate(final String newChequeDate)
    {
        this.newChequeDate = newChequeDate;
    }

    /**
     * @return the newChequeNo
     */
    public String getNewChequeNo()
    {
        return newChequeNo;
    }

    /**
     * @param newChequeNo the newChequeNo to set
     */
    public void setNewChequeNo(final String newChequeNo)
    {
        this.newChequeNo = newChequeNo;
    }

}
