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
 * Created on June 2,2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;

public class DishonoredChequeBean
{

    // For search criteria parameter
    private String startDate = "";
    private String endDate = "";
    private String chequeNo = "";
    private String fundLst = "";
    private String mode = "";

    // For search results
    private String voucherHeaderId = "";
    private String payinSlipVHeaderId = "";
    private String cgnum = "";
    private String voucherNumber = "";
    private String voucherType = "";

    private String fundId = "";
    private String chequeNumber = "";
    private String chequeDate = "";
    private String amount = "";

    private String bankName = "";
    private String accNumber = "";
    private String accIdParam = "";
    private String recChequeDate = "";

    private String bankRefNumber = "";
    private String bankChargeAmt = "";
    private String payeeName = "";

    public String getAccIdParam() {
        return accIdParam;
    }

    public void setAccIdParam(final String accIdParam) {
        this.accIdParam = accIdParam;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(final String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getCgnum() {
        return cgnum;
    }

    public void setCgnum(final String cgnum) {
        this.cgnum = cgnum;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(final String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(final String fundId) {
        this.fundId = fundId;
    }

    public String getPayinSlipVHeaderId() {
        return payinSlipVHeaderId;
    }

    public void setPayinSlipVHeaderId(final String payinSlipVHeaderId) {
        this.payinSlipVHeaderId = payinSlipVHeaderId;
    }

    public String getRecChequeDate() {
        return recChequeDate;
    }

    public void setRecChequeDate(final String recChequeDate) {
        this.recChequeDate = recChequeDate;
    }

    public String getVoucherHeaderId() {
        return voucherHeaderId;
    }

    public void setVoucherHeaderId(final String voucherHeaderId) {
        this.voucherHeaderId = voucherHeaderId;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(final String voucherType) {
        this.voucherType = voucherType;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(final String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public String getFundLst() {
        return fundLst;
    }

    public void setFundLst(final String fundLst) {
        this.fundLst = fundLst;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getBankChargeAmt() {
        return bankChargeAmt;
    }

    public void setBankChargeAmt(final String bankChargeAmt) {
        this.bankChargeAmt = bankChargeAmt;
    }

    public String getBankRefNumber() {
        return bankRefNumber;
    }

    public void setBankRefNumber(final String bankRefNumber) {
        this.bankRefNumber = bankRefNumber;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(final String payeeName) {
        this.payeeName = payeeName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

}
