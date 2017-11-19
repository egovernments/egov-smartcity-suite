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

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Transactional(readOnly = true)
public class BankAdviceReportInfo {

    private String partyName;
    private String accountNumber;
    private String ifscCode;
    private String micrCode;
    private String bank;
    private String bankBranch;
    private BigDecimal amount;
    private String rtgsNumber;
    private Date rtgsDate;
    private String department;
    private BigDecimal paymentAmount;
    private String status;
    private BigDecimal vhId;
    private BigDecimal ihId;
    private String paymentNumber;
    private String paymentDate;
    private BigDecimal dtId;
    private BigDecimal dkId;

    public BigDecimal getDtId() {
        return dtId;
    }

    public void setDtId(final BigDecimal dtId) {
        this.dtId = dtId;
    }

    public BigDecimal getDkId() {
        return dkId;
    }

    public void setDkId(final BigDecimal dkId) {
        this.dkId = dkId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public BigDecimal getIhId() {
        return ihId;
    }

    public void setIhId(final BigDecimal ihId) {
        this.ihId = ihId;
    }

    public void setIfscCode(final String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getMicrCode() {
        return micrCode;
    }

    public void setMicrCode(final String micrCode) {
        this.micrCode = micrCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getBank() {
        return bank;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBank(final String bank) {
        this.bank = bank;
    }

    public void setBankBranch(final String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getRtgsNumber() {
        return rtgsNumber;
    }

    public Date getRtgsDate() {
        return rtgsDate;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setRtgsNumber(final String rtgsNumber) {
        this.rtgsNumber = rtgsNumber;
    }

    public void setRtgsDate(final Date rtgsDate) {
        this.rtgsDate = rtgsDate;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public void setPaymentAmount(final BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentNumber(final String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void setPaymentDate(final String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getVhId() {
        return vhId;
    }

    public void setVhId(final BigDecimal vhId) {
        this.vhId = vhId;
    }

}
