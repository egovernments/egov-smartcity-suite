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
 * BrsEntries.java  Created on Aug 25, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
public class BrsEntries
{

    public String getPayCheque() {
        return payCheque;
    }

    public void setPayCheque(final String payCheque) {
        this.payCheque = payCheque;
    }

    /**
     * @return Returns the voucherHeaderId.
     */
    public String getVoucherHeaderId() {
        return voucherHeaderId;
    }

    /**
     * @param voucherHeaderId The voucherHeaderId to set.
     */
    public void setVoucherHeaderId(final String voucherHeaderId) {
        this.voucherHeaderId = voucherHeaderId;
    }

    private String id = "";
    private String refNo = "";
    private String type = "";
    private String txnDate = "";
    private String txnAmount = "";
    private String remarks = "";
    private String glCodeId = "";
    private String instrumentHeaderId;

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

    public String getCgnum() {
        return cgnum;
    }

    public void setCgnum(final String cgnum) {
        this.cgnum = cgnum;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(final String accNumber) {
        this.accNumber = accNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(final String payTo) {
        this.payTo = payTo;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(final String voucherType) {
        this.voucherType = voucherType;
    }

    // added for DishonoredCheque
    private String voucherNumber = "";
    private String cgnum = "";
    private String voucherHeaderId = "";
    private String payinSlipVHeaderId = "";
    private String fundId = "";
    private String fundSourceId = "";
    private String chequeNumber = "";
    private String chequeDate = "";
    private String amount = "";
    private String lotNumber = "";
    private String lotType = "";
    private String field = "";

    private String voucherType = "";
    private String bankName = "";
    private String accNumber = "";
    private String accIdParam = "";
    private String payTo = "";
    private String payCheque = "";
    private String departmentId;
    private String functionaryId;
    private String functionId = "";

    /**
     * @return Returns the field.
     */
    public String getField() {
        return field;
    }

    /**
     * @param field The field to set.
     */
    public void setField(final String field) {
        this.field = field;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return Returns the refNo.
     */
    public String getRefNo() {
        return refNo;
    }

    /**
     * @param refNo The refNo to set.
     */
    public void setRefNo(final String refNo) {
        this.refNo = refNo;
    }

    /**
     * @return Returns the remarks.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks The remarks to set.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return Returns the txnAmount.
     */
    public String getTxnAmount() {
        return txnAmount;
    }

    /**
     * @param txnAmount The txnAmount to set.
     */
    public void setTxnAmount(final String txnAmount) {
        this.txnAmount = txnAmount;
    }

    /**
     * @return Returns the txnDate.
     */
    public String getTxnDate() {
        return txnDate;
    }

    /**
     * @param txnDate The txnDate to set.
     */
    public void setTxnDate(final String txnDate) {
        this.txnDate = txnDate;
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
     * @return Returns the glCodeId.
     */
    public String getGlCodeId() {
        return glCodeId;
    }

    /**
     * @param type The glCodeId to set.
     */
    public void setGlCodeId(final String glCodeId) {
        this.glCodeId = glCodeId;
    }

    /**
     * @return Returns the amount.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount The amount to set.
     */
    public void setAmount(final String amount) {
        this.amount = amount;
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
     * @return Returns the fundId.
     */
    public String getFundId() {
        return fundId;
    }

    /**
     * @param fundId The fundId to set.
     */
    public void setFundId(final String fundId) {
        this.fundId = fundId;
    }

    /**
     * @return Returns the fundSourceId.
     */
    public String getFundSourceId() {
        return fundSourceId;
    }

    /**
     * @param fundSourceId The fundSourceId to set.
     */
    public void setFundSourceId(final String fundSourceId) {
        this.fundSourceId = fundSourceId;
    }

    /**
     * @return Returns the lotNumber.
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * @param lotNumber The lotNumber to set.
     */
    public void setLotNumber(final String lotNumber) {
        this.lotNumber = lotNumber;
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
     * @return Returns the payinSlipVHeaderId.
     */
    public String getPayinSlipVHeaderId() {
        return payinSlipVHeaderId;
    }

    /**
     * @param payinSlipVHeaderId The payinSlipVHeaderId to set.
     */
    public void setPayinSlipVHeaderId(final String payinSlipVHeaderId) {
        this.payinSlipVHeaderId = payinSlipVHeaderId;
    }

    /**
     * @return Returns the lotType.
     */
    public String getLotType() {
        return lotType;
    }

    /**
     * @param lotType The lotType to set.
     */
    public void setLotType(final String lotType) {
        this.lotType = lotType;
    }

    public String getAccIdParam() {
        return accIdParam;
    }

    public void setAccIdParam(final String accIdParam) {
        this.accIdParam = accIdParam;
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
    public void setDepartmentId(final String departmentId) {
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
    public void setFunctionaryId(final String functionaryId) {
        this.functionaryId = functionaryId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final String functionId) {
        this.functionId = functionId;
    }
}
