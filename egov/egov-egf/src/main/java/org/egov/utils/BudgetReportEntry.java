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
package org.egov.utils;

import java.math.BigDecimal;

public class BudgetReportEntry {
    String glCode;
    BigDecimal budgetedAmtForYear = BigDecimal.ZERO;
    BigDecimal soFarAppropriated = BigDecimal.ZERO;
    BigDecimal balance = BigDecimal.ZERO;
    BigDecimal cumilativeIncludingCurrentBill = BigDecimal.ZERO;
    BigDecimal currentBalanceAvailable = BigDecimal.ZERO;
    BigDecimal currentBillAmount = BigDecimal.ZERO;
    String fundName;
    String functionName;
    String departmentName;
    String financialYear;
    String AccountCode;
    String budgetApprNumber;

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public BigDecimal getBudgetedAmtForYear() {
        return budgetedAmtForYear;
    }

    public void setBudgetedAmtForYear(final BigDecimal budgetedAmtForYear) {
        this.budgetedAmtForYear = budgetedAmtForYear;
    }

    public BigDecimal getSoFarAppropriated() {
        return soFarAppropriated;
    }

    public void setSoFarAppropriated(final BigDecimal soFarAppropriated) {
        this.soFarAppropriated = soFarAppropriated;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCumilativeIncludingCurrentBill() {
        return cumilativeIncludingCurrentBill;
    }

    public void setCumilativeIncludingCurrentBill(
            final BigDecimal cumilativeIncludingCurrentBill) {
        this.cumilativeIncludingCurrentBill = cumilativeIncludingCurrentBill;
    }

    public BigDecimal getCurrentBalanceAvailable() {
        return currentBalanceAvailable;
    }

    public void setCurrentBalanceAvailable(final BigDecimal currentBalanceAvailable) {
        this.currentBalanceAvailable = currentBalanceAvailable;
    }

    public BigDecimal getCurrentBillAmount() {
        return currentBillAmount;
    }

    public void setCurrentBillAmount(final BigDecimal currentBillAmount) {
        this.currentBillAmount = currentBillAmount;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final String financialYear) {
        this.financialYear = financialYear;
    }

    public String getAccountCode() {
        return AccountCode;
    }

    public void setAccountCode(final String accountCode) {
        AccountCode = accountCode;
    }

    public String getBudgetApprNumber() {
        return budgetApprNumber;
    }

    public void setBudgetApprNumber(final String budgetApprNumber) {
        this.budgetApprNumber = budgetApprNumber;
    }

}
