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
/*
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

import org.apache.log4j.Logger;

public class GeneralLedgerReportBean
{
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReportBean.class);
    private String glCode1;
    private String glCode2;
    private String snapShotDateTime;
    private String forRevEntry;
    private String fund_id;
    private String fundSource_id;
    private String startDate;
    private String endDate;
    private String totalCount;
    private String isConfirmedCount;
    private String reportType;
    private String forRunningBalance;
    private String boundary;
    private String ulbName;
    private String mode;
    private String accEntityKey;
    private String accEntityId;
    private String entityName;
    private String accEntitycode;
    private Integer subledger;

    private String rcptVchrDate;
    private String rcptVchrNo;
    private String rcptAccCode;
    private String rcptFuncCode;
    private String rcptBgtCode;
    private String rcptParticulars;
    private String rcptcashInHandAmt;
    private String rcptChqInHandAmt;
    private String rcptSrcOfFinance;
    private String pmtVchrDate;
    private String pmtVchrNo;
    private String pmtAccCode;
    private String pmtFuncCode;
    private String pmtBgtCode;
    private String pmtParticulars;
    private String pmtCashInHandAmt;
    private String pmtChqInHandAmt;
    private String pmtSrcOfFinance;
    private String CGN;
    private String Cheque;
    private String accountCode;
    private String fundName;

    private String netRcptAmount;
    private String netPymntAmount;

    private String departmentId;
    private String functionaryId;
    private String fieldId;
    public String functionCodeId;
    public String functionCode;
    public String heading;

    public GeneralLedgerReportBean(final String str)
    {
        glCode1 = "";
        glCode2 = "";
        snapShotDateTime = "";
        forRevEntry = "";
        fund_id = "";
        fundSource_id = "";
        startDate = "";
        endDate = "";
        totalCount = "";
        isConfirmedCount = "";
        reportType = "";
        forRunningBalance = "";
        boundary = "";
        ulbName = "";
        mode = "";
        accEntityKey = "";
        accEntityId = "";

        rcptVchrDate = str;
        rcptVchrNo = str;
        rcptAccCode = str;
        rcptFuncCode = str;
        rcptBgtCode = str;
        rcptParticulars = str;
        rcptcashInHandAmt = str;
        rcptChqInHandAmt = str;
        rcptSrcOfFinance = str;
        pmtVchrDate = str;
        pmtVchrNo = str;
        pmtAccCode = str;
        pmtFuncCode = str;
        pmtBgtCode = str;
        pmtParticulars = str;
        pmtCashInHandAmt = str;
        pmtChqInHandAmt = str;
        pmtSrcOfFinance = str;
        CGN = "";
        Cheque = str;
        accountCode = "";
        fundName = "";
        netRcptAmount = str;
        netPymntAmount = str;
        functionCodeId = null;
        functionCode = null;
        heading = null;
    }

    public GeneralLedgerReportBean()
    {
        glCode1 = "";
        glCode2 = "";
        snapShotDateTime = "";
        forRevEntry = "";
        fund_id = "";
        fundSource_id = "";
        startDate = "";
        endDate = "";
        totalCount = "";
        isConfirmedCount = "";
        reportType = "";
        forRunningBalance = "";
        boundary = "";
        ulbName = "";
        accEntityKey = "";
        accEntityId = "";

        rcptVchrDate = "&nbsp;";
        rcptVchrNo = "&nbsp;";
        rcptAccCode = "&nbsp;";
        rcptFuncCode = "&nbsp;";
        rcptBgtCode = "&nbsp;";
        rcptParticulars = "&nbsp;";
        rcptcashInHandAmt = "&nbsp;";
        rcptChqInHandAmt = "&nbsp;";
        rcptSrcOfFinance = "&nbsp;";
        pmtVchrDate = "&nbsp;";
        pmtVchrNo = "&nbsp;";
        pmtAccCode = "&nbsp;";
        pmtFuncCode = "&nbsp;";
        pmtBgtCode = "&nbsp;";
        pmtParticulars = "&nbsp;";
        pmtCashInHandAmt = "&nbsp;";
        pmtChqInHandAmt = "&nbsp;";
        pmtSrcOfFinance = "&nbsp;";
        CGN = "";
        Cheque = "";
        mode = "";
        accountCode = "";
        fundName = "";
        netRcptAmount = "&nbsp;";
        netPymntAmount = "&nbsp;";

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
     * @return Returns the accEntityId.
     */
    public String getAccEntityId() {
        return accEntityId;
    }

    /**
     * @param accEntityId The accEntityId to set.
     */
    public void setAccEntityId(final String accEntityId) {
        this.accEntityId = accEntityId;
    }

    /**
     * @return Returns the accEntityKey.
     */
    public String getAccEntityKey() {
        return accEntityKey;
    }

    /**
     * @param accEntityKey The accEntityKey to set.
     */
    public void setAccEntityKey(final String accEntityKey) {
        this.accEntityKey = accEntityKey;
    }

    /**
     * @return Returns the forRevEntry.
     */
    public String getForRevEntry() {
        return forRevEntry;
    }

    /**
     * @param forRevEntry The forRevEntry to set.
     */
    public void setForRevEntry(final String forRevEntry) {
        this.forRevEntry = forRevEntry;
    }

    /**
     * @return Returns the fund_id.
     */
    public String getFund_id() {
        return fund_id;
    }

    /**
     * @param fund_id The fund_id to set.
     */
    public void setFund_id(final String fund_id) {
        this.fund_id = fund_id;
    }

    /**
     * @return Returns the fundSource_id.
     */
    public String getFundSource_id() {
        return fundSource_id;
    }

    /**
     * @param fundSource_id The fundSource_id to set.
     */
    public void setFundSource_id(final String fundSource_id) {
        this.fundSource_id = fundSource_id;
    }

    /**
     * @return Returns the glCode1.
     */
    public String getGlCode1() {
        return glCode1;
    }

    /**
     * @param glCode1 The glCode1 to set.
     */
    public void setGlCode1(final String glCode1) {
        this.glCode1 = glCode1;
    }

    /**
     * @return Returns the glCode2.
     */
    public String getGlCode2() {
        return glCode2;
    }

    /**
     * @param glCode2 The glCode2 to set.
     */
    public void setGlCode2(final String glCode2) {
        this.glCode2 = glCode2;
    }

    /**
     * @return Returns the snapShotDateTime.
     */
    public String getSnapShotDateTime() {
        return snapShotDateTime;
    }

    /**
     * @param snapShotDateTime The snapShotDateTime to set.
     */
    public void setSnapShotDateTime(final String snapShotDateTime) {
        this.snapShotDateTime = snapShotDateTime;
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
     * @return Returns the totalCount.
     */
    public String getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount The totalCount to set.
     */
    public void setTotalCount(final String totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return Returns the isConfirmedCount.
     */
    public String getIsConfirmedCount() {
        return isConfirmedCount;
    }

    /**
     * @param isConfirmedCount The isConfirmedCount to set.
     */
    public void setIsConfirmedCount(final String isConfirmedCount) {
        this.isConfirmedCount = isConfirmedCount;
    }

    /**
     * @return Returns the cGN.
     */

    public void setReportType(final String rptType) {
        reportType = rptType;

    }

    /**
     * @param chequeInHand to set.
     */
    public String getReportType() {
        return reportType;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;

    }

    /**
     * @param chequeInHand to set.
     */
    public String getUlbName() {
        return ulbName;
    }

    public void setForRunningBalance(final String forRunningBalance) {
        this.forRunningBalance = forRunningBalance;

    }

    public String getForRunningBalance() {
        return forRunningBalance;
    }

    /**
     *
     * @param rptType
     */
    public void setRcptVchrNo(final String vchrNo) {
        rcptVchrNo = vchrNo;
    }

    public String getRcptVchrNo() {
        return rcptVchrNo;
    }

    public void setRcptVchrDate(final String rcptVchrDate) {
        this.rcptVchrDate = rcptVchrDate;
    }

    public String getRcptVchrDate() {
        return rcptVchrDate;
    }

    public void setRcptAccCode(final String rcptAccCode) {
        this.rcptAccCode = rcptAccCode;
    }

    public String getRcptAccCode() {
        return rcptAccCode;
    }

    public void setRcptFuncCode(final String rcptFuncCode) {
        this.rcptFuncCode = rcptFuncCode;
    }

    public String getRcptFuncCode() {
        return rcptFuncCode;
    }

    public void setRcptBgtCode(final String rcptBgtCode) {
        this.rcptBgtCode = rcptBgtCode;
    }

    public String getRcptBgtCode() {
        return rcptBgtCode;
    }

    public void setRcptParticulars(final String rcptParticulars) {
        this.rcptParticulars = rcptParticulars;
    }

    public String getRcptParticulars() {
        return rcptParticulars;
    }

    public void setRcptcashInHandAmt(final String rcptcashInHandAmt) {
        this.rcptcashInHandAmt = rcptcashInHandAmt;
    }

    public String getRcptcashInHandAmt() {
        return rcptcashInHandAmt;
    }

    public void setRcptChqInHandAmt(final String rcptChqInHandAmt) {
        this.rcptChqInHandAmt = rcptChqInHandAmt;
    }

    public String getRcptChqInHandAmt() {
        return rcptChqInHandAmt;
    }

    public void setRcptSrcOfFinance(final String rcptSrcOfFinance) {
        this.rcptSrcOfFinance = rcptSrcOfFinance;
    }

    public String getRcptSrcOfFinance() {
        return rcptSrcOfFinance;
    }

    public void setpmtVchrDate(final String pmtVchrDate) {
        this.pmtVchrDate = pmtVchrDate;
    }

    public String getpmtVchrDate() {
        return pmtVchrDate;
    }

    public void setPmtVchrNo(final String pmtVchrNo) {
        this.pmtVchrNo = pmtVchrNo;
    }

    public String getPmtVchrNo() {
        return pmtVchrNo;
    }

    public void setPmtAccCode(final String pmtAccCode) {
        this.pmtAccCode = pmtAccCode;
    }

    public String getPmtAccCode() {
        return pmtAccCode;
    }

    public void setPmtFuncCode(final String pmtFuncCode) {
        this.pmtFuncCode = pmtFuncCode;
    }

    public String getPmtFuncCode() {
        return pmtFuncCode;
    }

    public void setPmtBgtCode(final String pmtBgtCode) {
        this.pmtBgtCode = pmtBgtCode;
    }

    public String getPmtBgtCode() {
        return pmtBgtCode;
    }

    public void setPmtParticulars(final String pmtParticulars) {
        this.pmtParticulars = pmtParticulars;
    }

    public String getPmtParticulars() {
        return pmtParticulars;
    }

    public void setPmtCashInHandAmt(final String pmtCashInHandAmt) {
        this.pmtCashInHandAmt = pmtCashInHandAmt;
    }

    public String getPmtCashInHandAmt() {
        return pmtCashInHandAmt;
    }

    public void setPmtChqInHandAmt(final String pmtChqInHandAmt) {
        this.pmtChqInHandAmt = pmtChqInHandAmt;
    }

    public String getPmtChqInHandAmt() {
        return pmtChqInHandAmt;
    }

    public void setPmtSrcOfFinance(final String pmtSrcOfFinance) {
        this.pmtSrcOfFinance = pmtSrcOfFinance;
    }

    public String getPmtSrcOfFinance() {
        return pmtSrcOfFinance;
    }

    public String getCGN() {
        return CGN;
    }

    /**
     * @param cgn The cGN to set.
     */
    public void setCGN(final String cgn) {
        CGN = cgn;
    }

    /**
     * @return Returns the endDate.
     */
    public String getBoundary() {
        return boundary;
    }

    /**
     * @param endDate The endDate to set.
     */
    public void setBoundary(final String boundary) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("after set boundary:" + boundary);
        this.boundary = boundary;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("after set this.boundary:" + this.boundary);
    }

    /**
     * @return Returns the cheque.
     */
    public String getCheque() {
        return Cheque;
    }

    /**
     * @param cheque The cheque to set.
     */
    public void setCheque(final String cheque) {
        Cheque = cheque;
    }

    /**
     * @return Returns the mode.
     */
    public String getMode() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("111111111111111111111111after get mode:" + mode);
        return mode;
    }

    /**
     * @param mode The mode to set.
     */
    public void setMode(final String mode) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("111111111111111111111111after get mode:" + mode);
        this.mode = mode;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("111111111111111111111111after get mode:" + this.mode);
    }

    /**
     * @return Returns the accountCode.
     */
    public String getAccountCode()
    {
        return accountCode;
    }

    /**
     * @param accountCode The accountCode to set.
     */
    public void setAccountCode(final String accountCode)
    {
        this.accountCode = accountCode;
    }

    /**
     * @return Returns the fundName.
     */
    public String getFundName()
    {
        return fundName;
    }

    /**
     * @param fundName The fundName to set.
     */
    public void setFundName(final String fundName)
    {
        this.fundName = fundName;
    }

    /**
     * @return the netPymntAmount
     */
    public String getNetPymntAmount()
    {
        return netPymntAmount;
    }

    /**
     * @param netPymntAmount the netPymntAmount to set
     */
    public void setNetPymntAmount(final String netPymntAmount)
    {
        this.netPymntAmount = netPymntAmount;
    }

    /**
     * @return the netRcptAmount
     */
    public String getNetRcptAmount()
    {
        return netRcptAmount;
    }

    /**
     * @param netRcptAmount the netRcptAmount to set
     */
    public void setNetRcptAmount(final String netRcptAmount)
    {
        this.netRcptAmount = netRcptAmount;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final String departmentId) {
        this.departmentId = departmentId;
    }

    public String getFunctionaryId() {
        return functionaryId;
    }

    public void setFunctionaryId(final String functionaryId) {
        this.functionaryId = functionaryId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(final String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFunctionCodeId() {
        return functionCodeId;
    }

    public void setFunctionCodeId(final String functionCodeId) {
        this.functionCodeId = functionCodeId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(final String functionCode) {
        this.functionCode = functionCode;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(final String entityName) {
        this.entityName = entityName;
    }

    public String getAccEntitycode() {
        return accEntitycode;
    }

    public void setAccEntitycode(final String accEntitycode) {
        this.accEntitycode = accEntitycode;
    }

    public Integer getSubledger() {
        return subledger;
    }

    public void setSubledger(final Integer subledger) {
        this.subledger = subledger;
    }

}
