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
package com.exilant.eGov.src.reports;

import org.apache.log4j.Logger;

public class RptBillRegisterBean
{
    private static final Logger LOGGER = Logger.getLogger(RptBillRegisterBean.class);
    private String slno;
    private String billDate;
    private String conSupName;
    private String particulars;
    private String billAmount;
    private String approvedBy;
    private String sanctionedAmount;
    private String paymentDate;
    private String disallowedAmount;
    private String balanceAmount;
    public String remarks;
    public int finId;
    public int conSupTypeId;

    public String fundId;
    public String fieldId;
    private String functionaryId;
    private String startDate;
    private String endDate;
    private String voucherNo;
    private String paidAmt;
    private String fundName;
    private String fieldName;
    private String functionaryName;
    private String ulbname;

    /**
     *
     */
    public RptBillRegisterBean() {

        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the slno.
     */
    public String getSlno() {
        return slno;
    }

    /**
     * @param slno The slno to set.
     */
    public void setSlno(final String slno) {
        this.slno = slno;
    }

    /**
     * @return Returns the billDate.
     */
    public String getBillDate() {
        return billDate;
    }

    /**
     * @param billDate The billDate to set.
     */
    public void setBillDate(final String billDate) {
        this.billDate = billDate;
    }

    /**
     * @return Returns the conSupName.
     */
    public String getConSupName() {
        return conSupName;
    }

    /**
     * @param conSupName The conSupName to set.
     */
    public void setConSupName(final String conSupName) {
        this.conSupName = conSupName;
    }

    /**
     * @return Returns the particulars.
     */
    public String getParticulars() {
        return particulars;
    }

    /**
     * @param particulars The particulars to set.
     */
    public void setParticulars(final String particulars) {
        this.particulars = particulars;
    }

    /**
     * @return Returns the billAmount.
     */
    public String getBillAmount() {
        return billAmount;
    }

    /**
     * @param billAmount The billAmount to set.
     */
    public void setBillAmount(final String billAmount) {
        this.billAmount = billAmount;
    }

    /**
     * @return Returns the approvedBy.
     */
    public String getApprovedBy() {
        return approvedBy;
    }

    /**
     * @param approvedBy The approvedBy to set.
     */
    public void setApprovedBy(final String approvedBy) {
        this.approvedBy = approvedBy;
    }

    /**
     * @return Returns the sanctionedDate.
     */
    public String getSanctionedDate() {
        return billDate;
    }

    /**
     * @param sanctionedDate The sanctionedDate to set.
     */
    public void setSanctionedDate(final String sanctionedDate) {
    }

    /**
     * @return Returns the sanctionedAmount.
     */
    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    /**
     * @param sanctionedAmount The sanctionedAmount to set.
     */
    public void setSanctionedAmount(final String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    /**
     * @return Returns the paymentDate.
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate The paymentDate to set.
     */
    public void setPaymentDate(final String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return Returns the disallowedAmount.
     */
    public String getDisallowedAmount() {
        return disallowedAmount;
    }

    /**
     * @param disallowedAmount The disallowedAmount to set.
     */
    public void setDisallowedAmount(final String disallowedAmount) {
        this.disallowedAmount = disallowedAmount;
    }

    /**
     * @return Returns the balanceAmount.
     */
    public String getBalanceAmount() {
        return balanceAmount;
    }

    /**
     * @param balanceAmount The balanceAmount to set.
     */
    public void setBalanceAmount(final String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    /**
     * @return Returns the delayReasons.
     */
    /*
     * public String getDelayReasons() { return delayReasons; }
     */
    /**
     * @param delayReasons The delayReasons to set.
     */
    /*
     * public void setDelayReasons(String delayReasons) { this.delayReasons = delayReasons; }
     */

    /**
     * @return Returns the finId.
     */
    public int getFinId() {
        return finId;
    }

    /**
     * @param finId The finId to set.
     */
    public void setFinId(final int finId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("inside set finId");
        this.finId = finId;
    }

    /**
     * @return Returns the conSupTypeId.
     */
    public int getConSupTypeId() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("inside set conSupId");
        return conSupTypeId;
    }

    /**
     * @param conSupTypeId The conSupTypeId to set.
     */
    public void setConSupTypeId(final int conSupTypeId) {
        this.conSupTypeId = conSupTypeId;
    }

    /**
     * @return Returns the endDate.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The endDate to set.
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return Returns the fieldId.
     */
    public String getFieldId() {
        return fieldId;
    }

    /**
     * @param fieldId The fieldId to set.
     */
    public void setFieldId(final String fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * @return Returns the functionaryId.
     */
    public String getFunctionaryId() {
        return functionaryId;
    }

    /**
     * @param functionaryId The functionaryId to set.
     */
    public void setFunctionaryId(final String functionaryId) {
        this.functionaryId = functionaryId;
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
     * @return Returns the startDate.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate The startDate to set.
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return Returns the voucherNo.
     */
    public String getVoucherNo() {
        return voucherNo;
    }

    /**
     * @param voucherNo The voucherNo to set.
     */
    public void setVoucherNo(final String voucherNo) {
        this.voucherNo = voucherNo;
    }

    /**
     * @return Returns the paidAmt.
     */
    public String getPaidAmt() {
        return paidAmt;
    }

    /**
     * @param paidAmt The paidAmt to set.
     */
    public void setPaidAmt(final String paidAmt) {
        this.paidAmt = paidAmt;
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
     * @return Returns the fundName.
     */
    public String getFundName() {
        return fundName;
    }

    /**
     * @param fundName The fundName to set.
     */
    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    /**
     * @return Returns the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName The fieldName to set.
     */
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return Returns the functionaryName.
     */
    public String getFunctionaryName() {
        return functionaryName;
    }

    /**
     * @param functionaryName The functionaryName to set.
     */
    public void setFunctionaryName(final String functionaryName) {
        this.functionaryName = functionaryName;
    }

    /**
     * @return Returns the ulbname.
     */
    public String getUlbname() {
        return ulbname;
    }

    /**
     * @param ulbname The ulbname to set.
     */
    public void setUlbname(final String ulbname) {
        this.ulbname = ulbname;
    }
}
