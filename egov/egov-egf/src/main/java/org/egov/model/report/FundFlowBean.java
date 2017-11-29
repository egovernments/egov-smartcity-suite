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
/**
 *
 */
package org.egov.model.report;

import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mani
 *
 */
public class FundFlowBean extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = -8431490953442990148L;
    private String bankName;
    private BigDecimal bankAccountId;
    private String glcode;
    private Date reportDate;
    private String accountNumber;
    private String fundName;
    private Boolean walkinPaymentAccount;
    private BigDecimal openingBalance = BigDecimal.ZERO.setScale(2);
    private BigDecimal currentReceipt = BigDecimal.ZERO.setScale(2);
    private BigDecimal FundsAvailable = BigDecimal.ZERO.setScale(2);
    private BigDecimal btbPayment = BigDecimal.ZERO.setScale(2);// this will be on a perticular day
    private BigDecimal btbReceipt = BigDecimal.ZERO.setScale(2);// this will be on a perticular day
    private BigDecimal concurranceBPV = BigDecimal.ZERO.setScale(2);// this will be on a perticular day
    private BigDecimal outStandingBPV = BigDecimal.ZERO.setScale(2);// this will be till date
    private BigDecimal closingBalance = BigDecimal.ZERO.setScale(2);
    private String codeId;

    public FundFlowBean() {
    };

    public FundFlowBean(final String fundName, final String accountNumber, final BigDecimal openingBalance,
            final BigDecimal currentReceipt,
            final BigDecimal btbPayment) {
        super();
        this.fundName = fundName;
        this.accountNumber = accountNumber;
        this.currentReceipt = currentReceipt;
        this.btbPayment = btbPayment;
        this.openingBalance = openingBalance;
    }

    public FundFlowBean(final String fundName, final String accountNumber, final BigDecimal openingBalance,
            final BigDecimal currentReceipt,
            final BigDecimal btbPayment, final BigDecimal btbReceipt) {
        super();
        this.fundName = fundName;
        this.accountNumber = accountNumber;
        this.currentReceipt = currentReceipt;
        this.btbPayment = btbPayment;
        this.btbReceipt = btbReceipt;
        this.openingBalance = openingBalance;
    }

    public FundFlowBean(final String fundName, final String accountNumber, final BigDecimal openingBalance,
            final BigDecimal currentReceipt,
            final BigDecimal btbPayment, final BigDecimal btbReceipt, final BigDecimal concurranceBPV,
            final BigDecimal outStandingBPV) {
        super();
        this.fundName = fundName;
        this.accountNumber = accountNumber;
        this.openingBalance = openingBalance;
        this.currentReceipt = currentReceipt;
        this.btbPayment = btbPayment;
        this.btbReceipt = btbReceipt;
        this.concurranceBPV = concurranceBPV;
        this.outStandingBPV = outStandingBPV;

    }

    public BigDecimal getOutStandingBPV() {

        return outStandingBPV;
    }

    public void setOutStandingBPV(final BigDecimal outStandingBPV) {
        this.outStandingBPV = outStandingBPV == null ? BigDecimal.ZERO.setScale(2) : outStandingBPV.setScale(2);
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(final Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(final String codeId) {
        this.codeId = codeId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(final BigDecimal openingBalance) {
        this.openingBalance = openingBalance == null ? BigDecimal.ZERO.setScale(2) : openingBalance.setScale(2);
    }

    public BigDecimal getCurrentReceipt() {
        return currentReceipt;
    }

    public void setCurrentReceipt(final BigDecimal currentReceipt) {
        this.currentReceipt = currentReceipt == null ? BigDecimal.ZERO.setScale(2) : currentReceipt.setScale(2);
    }

    public BigDecimal getFundsAvailable() {
        return FundsAvailable;
    }

    public void setFundsAvailable(final BigDecimal fundsAvailable) {
        FundsAvailable = fundsAvailable == null ? BigDecimal.ZERO.setScale(2) : fundsAvailable.setScale(2);
    }

    public BigDecimal getBtbPayment() {
        return btbPayment;
    }

    public void setBtbPayment(final BigDecimal btbPayment) {
        this.btbPayment = btbPayment == null ? BigDecimal.ZERO.setScale(2) : btbPayment.setScale(2);
    }

    public BigDecimal getBtbReceipt() {
        return btbReceipt;
    }

    public void setBtbReceipt(final BigDecimal btbReceipt) {
        this.btbReceipt = btbReceipt == null ? BigDecimal.ZERO.setScale(2) : btbReceipt.setScale(2);
    }

    public BigDecimal getConcurranceBPV() {
        return concurranceBPV;
    }

    public void setConcurranceBPV(final BigDecimal concurranceBPV) {
        this.concurranceBPV = concurranceBPV == null ? BigDecimal.ZERO.setScale(2) : concurranceBPV.setScale(2);
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(final BigDecimal closingBalance) {
        this.closingBalance = closingBalance == null ? BigDecimal.ZERO.setScale(2) : closingBalance.setScale(2);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(final BigDecimal bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(final String glcode) {
        this.glcode = glcode;
    }

    public Boolean getWalkinPaymentAccount() {
        return walkinPaymentAccount;
    }

    public void setWalkinPaymentAccount(final Boolean walkinPaymentAccount) {
        this.walkinPaymentAccount = walkinPaymentAccount;
    }

    @Override
    public String toString()
    {

        return "id:" + id + ",acccode:" + accountNumber + ",createdDate:" + createdDate + ",modifiedDate:" + modifiedDate
                + ",opb:" + openingBalance;
    }
}
