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
package org.egov.model.brs;

import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

public class AutoReconcileBean extends BaseModel {
    private static final long serialVersionUID = -3495802029238920474L;
    private String accountnumber;
    private Long accountId;
    private Date txDate;
    private String txDateStr;
    private String Type;
    private String instrumentNo;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal balance;
    private String narration;
    private String CSLno;
    private Date createdDate;
    private String action;
    private Date reconciliationDate;
    private String errorCode;
    private String errorMessage;
    private String noDetailsFound;

    public void setAccountId(final Long accountId) {
        this.accountId = accountId;
    }

    public Date getReconciliationDate() {
        return reconciliationDate;
    }

    public void setReconciliationDate(final Date reconciliationDate) {
        this.reconciliationDate = reconciliationDate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(final String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Date getTxDate() {
        return txDate;
    }

    public void setTxDate(final Date txDate) {
        this.txDate = txDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(final String type) {
        Type = type;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentNo(final String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(final BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(final BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getCSLno() {
        return CSLno;
    }

    public void setCSLno(final String lno) {
        CSLno = lno;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getTxDateStr() {
        return txDateStr;
    }

    public void setTxDateStr(final String txDateStr) {
        this.txDateStr = txDateStr;
    }

    @Override
    public String toString()
    {
        final StringBuffer str = new StringBuffer(1024);
        str.append(" id :").append(id)
        .append(" txDateStr:").append(txDateStr)
        .append(" instrumentNo:").append(instrumentNo)
        .append(" narration:").append(narration)
        .append(" debit:").append(debit)
        .append(" credit:").append(credit)
        .append(" balance:").append(balance);
        return str.toString();

    }

    public String getNoDetailsFound() {
        return noDetailsFound;
    }

    public void setNoDetailsFound(final String noDetailsFound) {
        this.noDetailsFound = noDetailsFound;
    }

}
