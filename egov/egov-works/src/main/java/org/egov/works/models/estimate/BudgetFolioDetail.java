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
package org.egov.works.models.estimate;

import java.io.Serializable;
import java.math.BigDecimal;

public class BudgetFolioDetail implements Serializable {

    private static final long serialVersionUID = -984572211887333131L;
    private Integer srlNo;
    private String budgetApprNo;
    private String estimateNo;
    private String nameOfWork;
    private String estimateDate;
    private Double workValue;
    private Double appropriatedValue;
    private String appDate;
    private String appType;
    private Double cumulativeTotal;
    private BigDecimal balanceAvailable;
    private Double expensesIncurred;
    private Double cumulativeExpensesIncurred;
    private Double actualBalanceAvailable;
    private String workIdentificationNumber;

    public String getBudgetApprNo() {
        return budgetApprNo;
    }

    public void setBudgetApprNo(final String budgetApprNo) {
        this.budgetApprNo = budgetApprNo;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(final String estimateNo) {
        this.estimateNo = estimateNo;
    }

    public String getNameOfWork() {
        return nameOfWork;
    }

    public void setNameOfWork(final String nameOfWork) {
        this.nameOfWork = nameOfWork;
    }

    public String getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final String estimateDate) {
        this.estimateDate = estimateDate;
    }

    public Double getWorkValue() {
        return workValue;
    }

    public void setWorkValue(final Double workValue) {
        this.workValue = workValue;
    }

    public Double getAppropriatedValue() {
        return appropriatedValue;
    }

    public void setAppropriatedValue(final Double appropriatedValue) {
        this.appropriatedValue = appropriatedValue;
    }

    public Double getCumulativeTotal() {
        return cumulativeTotal;
    }

    public BigDecimal getBalanceAvailable() {
        return balanceAvailable;
    }

    public void setCumulativeTotal(final Double cumulativeTotal) {
        this.cumulativeTotal = cumulativeTotal;
    }

    public void setBalanceAvailable(final BigDecimal balanceAvailable) {
        this.balanceAvailable = balanceAvailable;
    }

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(final Integer srlNo) {
        this.srlNo = srlNo;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(final String appDate) {
        this.appDate = appDate;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(final String appType) {
        this.appType = appType;
    }

    public Double getExpensesIncurred() {
        return expensesIncurred;
    }

    public void setExpensesIncurred(final Double expensesIncurred) {
        this.expensesIncurred = expensesIncurred;
    }

    public Double getCumulativeExpensesIncurred() {
        return cumulativeExpensesIncurred;
    }

    public void setCumulativeExpensesIncurred(final Double cumulativeExpensesIncurred) {
        this.cumulativeExpensesIncurred = cumulativeExpensesIncurred;
    }

    public Double getActualBalanceAvailable() {
        return actualBalanceAvailable;
    }

    public void setActualBalanceAvailable(final Double actualBalanceAvailable) {
        this.actualBalanceAvailable = actualBalanceAvailable;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }
}
