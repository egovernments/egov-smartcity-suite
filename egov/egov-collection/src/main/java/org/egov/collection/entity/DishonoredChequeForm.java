/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.collection.entity;

import java.io.Serializable;
import java.util.Date;

public class DishonoredChequeForm implements Serializable
{
    private static final long serialVersionUID = 2038253371190828409L;
    private String voucherHeaderId;
    private String glcodeId;
    private String functionId;
    private String glcode;
    private String description;
    private String debitAmount;
    private String creditAmount;
    private String detailKeyId;
    private String detailTypeId;
    private String amount;
   

    private Date transactionDate;
    private String dishonorReason;
    private String remarks;
    private String referenceNo;
    private String instHeaderIds;
    private String voucherHeaderIds;
    private String receiptGLDetails;
    private String remittanceGLDetails;
    
    public String getVoucherHeaderId() {
        return voucherHeaderId;
    }

    public void setVoucherHeaderId(String voucherHeaderId) {
        this.voucherHeaderId = voucherHeaderId;
    }

    public String getGlcodeId() {
        return glcodeId;
    }

    public void setGlcodeId(String glcodeId) {
        this.glcodeId = glcodeId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(String glcode) {
        this.glcode = glcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getDetailKeyId() {
        return detailKeyId;
    }

    public void setDetailKeyId(String detailKeyId) {
        this.detailKeyId = detailKeyId;
    }

    public String getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(String detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDishonorReason() {
        return dishonorReason;
    }

    public void setDishonorReason(String dishonorReason) {
        this.dishonorReason = dishonorReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getInstHeaderIds() {
        return instHeaderIds;
    }

    public void setInstHeaderIds(String instHeaderIds) {
        this.instHeaderIds = instHeaderIds;
    }

    public String getVoucherHeaderIds() {
        return voucherHeaderIds;
    }

    public void setVoucherHeaderIds(String voucherHeaderIds) {
        this.voucherHeaderIds = voucherHeaderIds;
    }

    public String getReceiptGLDetails() {
        return receiptGLDetails;
    }

    public void setReceiptGLDetails(String receiptGLDetails) {
        this.receiptGLDetails = receiptGLDetails;
    }

    public String getRemittanceGLDetails() {
        return remittanceGLDetails;
    }

    public void setRemittanceGLDetails(String remittanceGLDetails) {
        this.remittanceGLDetails = remittanceGLDetails;
    }

}
