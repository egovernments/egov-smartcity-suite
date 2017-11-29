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
package org.egov.egf.model;

import java.math.BigDecimal;
import java.sql.Date;

public class BudgetAppDisplay {

    private String serailNumber;
    private String billAndVoucherNumber;
    private BigDecimal billAmount;
    private BigDecimal cumulativeAmount;
    private BigDecimal balanceAvailableAmount;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private Date voucherDate;
    private String description;
    private String billNumber;
    private String VoucherNumber;
    private Date billDate;
    private String bdgApprNumber;
    private Date createdDate;
    private Date billCreatedDate;

    public String getSerailNumber() {
        return serailNumber;
    }

    public void setSerailNumber(final String serailNumber) {
        this.serailNumber = serailNumber;
    }

    public String getBillAndVoucherNumber() {
        return billAndVoucherNumber;
    }

    public void setBillAndVoucherNumber(final String billAndVoucherNumber) {
        this.billAndVoucherNumber = billAndVoucherNumber;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(final BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public BigDecimal getCumulativeAmount() {
        return cumulativeAmount;
    }

    public void setCumulativeAmount(final BigDecimal cumulativeAmount) {
        this.cumulativeAmount = cumulativeAmount;
    }

    public BigDecimal getBalanceAvailableAmount() {
        return balanceAvailableAmount;
    }

    public void setBalanceAvailableAmount(final BigDecimal balanceAvailableAmount) {
        this.balanceAvailableAmount = balanceAvailableAmount;
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

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getVoucherNumber() {
        return VoucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        VoucherNumber = voucherNumber;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(final Date billDate) {
        this.billDate = billDate;
    }

    public String getBdgApprNumber() {
        return bdgApprNumber;
    }

    public void setBdgApprNumber(final String bdgApprNumber) {
        this.bdgApprNumber = bdgApprNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getBillCreatedDate() {
        return billCreatedDate;
    }

    public void setBillCreatedDate(Date billCreatedDate) {
        this.billCreatedDate = billCreatedDate;
    }

}
