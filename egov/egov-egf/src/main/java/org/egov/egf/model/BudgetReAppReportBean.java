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
import java.util.Date;

/*
 * Bean used for the BudgetAppropriation Report
 */

public class BudgetReAppReportBean {
    private Integer slNo;
    private String department;
    private String fund;
    private String function;
    private String budgetHead;
    private String budgetAppropriationNo;
    private String appDate;
    private Date appropriationDate;
    private BigDecimal additionAmount = new BigDecimal("0.0");
    private BigDecimal deductionAmount = new BigDecimal("0.0");
    private BigDecimal actualAmount = new BigDecimal("0.0");

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(final String fund) {
        this.fund = fund;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(final String function) {
        this.function = function;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(final String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getBudgetAppropriationNo() {
        return budgetAppropriationNo;
    }

    public void setBudgetAppropriationNo(final String budgetAppropriationNo) {
        this.budgetAppropriationNo = budgetAppropriationNo;
    }

    public Date getAppropriationDate() {
        return appropriationDate;
    }

    public void setAppropriationDate(final Date appropriationDate) {
        this.appropriationDate = appropriationDate;
    }

    public BigDecimal getAdditionAmount() {
        return additionAmount;
    }

    public void setAdditionAmount(final BigDecimal additionAmount) {
        this.additionAmount = additionAmount;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(final BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(final BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(final Integer slNo) {
        this.slNo = slNo;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(final String appDate) {
        this.appDate = appDate;
    }
}
