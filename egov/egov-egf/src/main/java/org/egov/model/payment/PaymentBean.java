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

import java.math.BigDecimal;
import java.util.Date;

public class PaymentBean {
    private Long billId;
    private Long billVoucherId;
    private String billNumber;
    private Date billDate;
    private String payTo;
    private String expType;
    private BigDecimal netAmt;
    private BigDecimal earlierPaymentAmt;
    private BigDecimal payableAmt;
    private BigDecimal paymentAmt;
    private BigDecimal deductionAmt;
    private BigDecimal passedAmt;
    private String fundName;
    private String deptName;
    private String functionName;
    private String schemeName;
    private String subschemeName;
    private String functionaryName;
    private String fundsourceName;
    private String fieldName;
    private boolean isSelected=false;
    private String billVoucherNumber;
    private Date billVoucherDate;
    private String region;

    public String getExpType() {
        return expType;
    }

    public void setExpType(final String expType) {
        this.expType = expType;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(final BigDecimal billId) {
        this.billId = billId.longValue();
    }

    public Long getCsBillId() {
        return billId;
    }

    public void setCsBillId(final Long voucherHeaderId) {
        billId = voucherHeaderId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(final Date billDate) {
        this.billDate = billDate;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(final String payTo) {
        this.payTo = payTo;
    }

    public BigDecimal getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(final BigDecimal netAmt) {
        this.netAmt = netAmt;
    }

    public BigDecimal getEarlierPaymentAmt() {
        return earlierPaymentAmt;
    }

    public void setEarlierPaymentAmt(final BigDecimal earlierPaymentAmt) {
        this.earlierPaymentAmt = earlierPaymentAmt;
    }

    public BigDecimal getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(final BigDecimal payableAmt) {
        this.payableAmt = payableAmt;
    }

    public BigDecimal getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(final BigDecimal paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public BigDecimal getDeductionAmt() {
        return deductionAmt;
    }

    public void setDeductionAmt(final BigDecimal deductionAmt) {
        this.deductionAmt = deductionAmt;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(final String deptName) {
        this.deptName = deptName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(final String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSubschemeName() {
        return subschemeName;
    }

    public void setSubschemeName(final String subschemeName) {
        this.subschemeName = subschemeName;
    }

    public String getFunctionaryName() {
        return functionaryName;
    }

    public void setFunctionaryName(final String functionaryName) {
        this.functionaryName = functionaryName;
    }

    public String getFundsourceName() {
        return fundsourceName;
    }

    public void setFundsourceName(final String fundsourceName) {
        this.fundsourceName = fundsourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public BigDecimal getPassedAmt() {
        return passedAmt;
    }

    public void setPassedAmt(final BigDecimal passedAmt) {
        this.passedAmt = passedAmt;
    }

    public String getBillVoucherNumber() {
        return billVoucherNumber;
    }

    public void setBillVoucherNumber(final String billVoucherNumber) {
        this.billVoucherNumber = billVoucherNumber;
    }

    public Date getBillVoucherDate() {
        return billVoucherDate;
    }

    public void setBillVoucherDate(final Date billVoucherDate) {
        this.billVoucherDate = billVoucherDate;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public Long getBillVoucherId() {
        return billVoucherId;
    }

    public void setBillVoucherId(Long billVoucherId) {
        this.billVoucherId = billVoucherId;
    }
    
    
}
