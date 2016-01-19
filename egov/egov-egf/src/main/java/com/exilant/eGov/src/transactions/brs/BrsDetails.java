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
 * BrsDetails.java  Created on Aug 17, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

import java.util.List;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
public class BrsDetails {

    public String getCgnum() {
        return cgnum;
    }

    public void setCgnum(final String cgnum) {
        this.cgnum = cgnum;
    }

    String voucherNumber;
    String cgnum;
    String voucherDate;
    String type;
    String chequeNumber;
    String chequeDate;
    int recordId;
    String chequeAmount;
    String txnType;
    String instrumentHeaderId;
    List<String> VoucherNumbers;

    /**
     * @return the voucherNumbers
     */
    public List<String> getVoucherNumbers() {
        return VoucherNumbers;
    }

    /**
     * @param voucherNumbers the voucherNumbers to set
     */
    public void setVoucherNumbers(final List<String> voucherNumbers) {
        VoucherNumbers = voucherNumbers;
    }

    /**
     * @return the voucherHeaderIds
     */
    public List<Long> getVoucherHeaderIds() {
        return voucherHeaderIds;
    }

    /**
     * @param voucherHeaderIds the voucherHeaderIds to set
     */
    public void setVoucherHeaderIds(final List<Long> voucherHeaderIds) {
        this.voucherHeaderIds = voucherHeaderIds;
    }

    /**
     * @return the voucherDates
     */
    public List<String> getVoucherDates() {
        return voucherDates;
    }

    /**
     * @param voucherDates the voucherDates to set
     */
    public void setVoucherDates(final List<String> voucherDates) {
        this.voucherDates = voucherDates;
    }

    List<Long> voucherHeaderIds;
    List<String> voucherDates;

    /**
     * @return the instrumentHeaderId
     */
    public String getInstrumentHeaderId() {
        return instrumentHeaderId;
    }

    /**
     * @param instrumentHeaderId the instrumentHeaderId to set
     */
    public void setInstrumentHeaderId(final String instrumentHeaderId) {
        this.instrumentHeaderId = instrumentHeaderId;
    }

    /**
     * @return Returns the chequeAmount.
     */
    public String getChequeAmount() {
        return chequeAmount;
    }

    /**
     * @param chequeAmount The chequeAmount to set.
     */
    public void setChequeAmount(final String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    /**
     * @return Returns the chequeDate.
     */
    public String getChequeDate() {
        return chequeDate;
    }

    /**
     * @param chequeDate The chequeDate to set.
     */
    public void setChequeDate(final String chequeDate) {
        this.chequeDate = chequeDate;
    }

    /**
     * @return Returns the chequeNumber.
     */
    public String getChequeNumber() {
        return chequeNumber;
    }

    /**
     * @param chequeNumber The chequeNumber to set.
     */
    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    /**
     * @return Returns the recordId.
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * @param recordId The recordId to set.
     */
    public void setRecordId(final int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return Returns the voucherDate.
     */
    public String getVoucherDate() {
        return voucherDate;
    }

    /**
     * @param voucherDate The voucherDate to set.
     */
    public void setVoucherDate(final String voucherDate) {
        this.voucherDate = voucherDate;
    }

    /**
     * @return Returns the voucherNumber.
     */
    public String getVoucherNumber() {
        return voucherNumber;
    }

    /**
     * @param voucherNumber The voucherNumber to set.
     */
    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    /**
     * @return Returns the txnType.
     */
    public String getTxnType() {
        return txnType;
    }

    /**
     * @param txnType The txnType to set.
     */
    public void setTxnType(final String txnType) {
        this.txnType = txnType;
    }
}
