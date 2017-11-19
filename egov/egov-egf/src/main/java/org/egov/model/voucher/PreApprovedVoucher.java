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
package org.egov.model.voucher;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;

import java.math.BigDecimal;
import java.util.Date;

public class PreApprovedVoucher {

    private Long functionIdDetail;
    private String functionDetail;
    private Long glcodeIdDetail;
    private String glcodeDetail;
    private String accounthead;
    private BigDecimal debitAmountDetail = BigDecimal.ZERO;
    private BigDecimal creditAmountDetail = BigDecimal.ZERO;

    private CChartOfAccounts glcode;
    private Accountdetailtype detailType;
    private Integer detailKeyId;
    private String detailKey;
    private String detailCode;
    private BigDecimal debitAmount = BigDecimal.ZERO;
    private BigDecimal creditAmount = BigDecimal.ZERO;
    private BigDecimal amount = BigDecimal.ZERO;
    private Date voucherDate;

    public Long getFunctionIdDetail() {
        return functionIdDetail;
    }

    public void setFunctionIdDetail(final Long functionIdDetail) {
        this.functionIdDetail = functionIdDetail;
    }

    public String getFunctionDetail() {
        return functionDetail;
    }

    public void setFunctionDetail(final String functionDetail) {
        this.functionDetail = functionDetail;
    }

    public Long getGlcodeIdDetail() {
        return glcodeIdDetail;
    }

    public void setGlcodeIdDetail(final Long glcodeIdDetail) {
        this.glcodeIdDetail = glcodeIdDetail;
    }

    public String getGlcodeDetail() {
        return glcodeDetail;
    }

    public void setGlcodeDetail(final String glcodeDetail) {
        this.glcodeDetail = glcodeDetail;
    }

    public BigDecimal getDebitAmountDetail() {
        return debitAmountDetail;
    }

    public void setDebitAmountDetail(final BigDecimal debitAmountDetail) {
        this.debitAmountDetail = debitAmountDetail;
    }

    public BigDecimal getCreditAmountDetail() {
        return creditAmountDetail;
    }

    public void setCreditAmountDetail(final BigDecimal creditAmountDetail) {
        this.creditAmountDetail = creditAmountDetail;
    }

    public CChartOfAccounts getGlcode() {
        return glcode;
    }

    public void setGlcode(final CChartOfAccounts glcode) {
        this.glcode = glcode;
    }

    public Accountdetailtype getDetailType() {
        return detailType;
    }

    public void setDetailType(final Accountdetailtype detailType) {
        this.detailType = detailType;
    }

    public Integer getDetailKeyId() {
        return detailKeyId;
    }

    public void setDetailKeyId(final Integer detailKeyId) {
        this.detailKeyId = detailKeyId;
    }

    public String getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(final String detailKey) {
        this.detailKey = detailKey;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(final String detailCode) {
        this.detailCode = detailCode;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(final BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getAccounthead() {
        return accounthead;
    }

    public void setAccounthead(final String accounthead) {
        this.accounthead = accounthead;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

}
