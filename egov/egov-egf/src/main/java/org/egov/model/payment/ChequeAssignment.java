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
package org.egov.model.payment;

import org.egov.commons.Bankaccount;

import java.math.BigDecimal;
import java.util.Date;

public class ChequeAssignment implements java.io.Serializable {
    private Long voucherid;
    private Date voucherDate;
    private String voucherNumber;
    private String bankAccNumber;
    private BigDecimal paidAmount;
    private String paidTo;
    private String bankName;
    private String chequeNumber;
    private String departmentName;
    private boolean isSelected;
    private Date chequeDate;
    private Long detailtypeid;
    private Long detailkeyid;
    private Long billVHId;
    private String serialNo;
    private Long bankAccountId;
    private Bankaccount bankAcc;
    private BigDecimal rtgsTotalPaidAmt;
    private String bankBranchHeader;
    private boolean addRtgs;
    private boolean headerval;
    private Long billId;
    private String billNumber;
    private String expenditureType;
    private Long glcodeId;
    private String drawingOfficerNameTAN;
    private BigDecimal receiptAmount;
    private BigDecimal deductedAmount;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo;
    }

    public Long getVoucherid() {
        return voucherid;
    }

    public void setVoucherid(Long voucherid) {
        this.voucherid = voucherid;
    }

    public void setVoucherid(final BigDecimal voucherid) {
        this.voucherid = voucherid.longValue();
    }

    public Long getVoucherHeaderId() {
        return voucherid;
    }

    public void setVoucherHeaderId(final Long voucherHeaderId) {
        voucherid = voucherHeaderId;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(final BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(final String paidTo) {
        this.paidTo = paidTo;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(final Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Bankaccount getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(final Bankaccount bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBankAccNumber() {
        return bankAccNumber;
    }

    public void setBankAccNumber(final String bankAccNumber) {
        this.bankAccNumber = bankAccNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getRtgsTotalPaidAmt() {
        return rtgsTotalPaidAmt;
    }

    public void setRtgsTotalPaidAmt(final BigDecimal rtgsTotalPaidAmt) {
        this.rtgsTotalPaidAmt = rtgsTotalPaidAmt;
    }

    public String getBankBranchHeader() {
        return bankBranchHeader;
    }

    public void setBankBranchHeader(final String bankBranchHeader) {
        this.bankBranchHeader = bankBranchHeader;
    }

    public boolean isAddRtgs() {
        return addRtgs;
    }

    public void setAddRtgs(final boolean addRtgs) {
        this.addRtgs = addRtgs;
    }

    public boolean isHeaderval() {
        return headerval;
    }

    public void setHeaderval(final boolean headerval) {
        this.headerval = headerval;
    }

    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(final String expenditureType) {
        this.expenditureType = expenditureType;
    }

    public String getDrawingOfficerNameTAN() {
        return drawingOfficerNameTAN;
    }

    public void setDrawingOfficerNameTAN(final String drawingOfficerNameTAN) {
        this.drawingOfficerNameTAN = drawingOfficerNameTAN;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(final BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(final BigDecimal deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public Long getDetailtypeid() {
        return detailtypeid;
    }

    public void setDetailtypeid(Long detailtypeid) {
        this.detailtypeid = detailtypeid;
    }

    public Long getDetailkeyid() {
        return detailkeyid;
    }

    public void setDetailkeyid(Long detailkeyid) {
        this.detailkeyid = detailkeyid;
    }

    public Long getBillVHId() {
        return billVHId;
    }

    public void setBillVHId(Long billVHId) {
        this.billVHId = billVHId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Long getGlcodeId() {
        return glcodeId;
    }

    public void setGlcodeId(Long glcodeId) {
        this.glcodeId = glcodeId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }
}
