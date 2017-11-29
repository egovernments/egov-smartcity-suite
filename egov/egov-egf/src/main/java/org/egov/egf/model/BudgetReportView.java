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

public class BudgetReportView {
    private Long id;
    String departmentCode = "";
    String functionCode = "";
    String budgetGroupName = "";
    private BigDecimal actualsLastYear = BigDecimal.ZERO;
    private BigDecimal beCurrentYearApproved = BigDecimal.ZERO;
    private BigDecimal reCurrentYearApproved = BigDecimal.ZERO;
    private BigDecimal beNextYearApproved = BigDecimal.ZERO;
    private BigDecimal reCurrentYearOriginal = BigDecimal.ZERO;
    private BigDecimal beNextYearOriginal = BigDecimal.ZERO;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(final String functionCode) {
        this.functionCode = functionCode;
    }

    public String getBudgetGroupName() {
        return budgetGroupName;
    }

    public void setBudgetGroupName(final String budgetGroupName) {
        this.budgetGroupName = budgetGroupName;
    }

    public BigDecimal getActualsLastYear() {
        return actualsLastYear;
    }

    public void setActualsLastYear(final BigDecimal actuals) {
        actualsLastYear = actuals;
    }

    public BigDecimal getBeCurrentYearApproved() {
        return beCurrentYearApproved;
    }

    public void setBeCurrentYearApproved(final BigDecimal beCurrentYear) {
        beCurrentYearApproved = beCurrentYear;
    }

    public BigDecimal getReCurrentYearApproved() {
        return reCurrentYearApproved;
    }

    public void setReCurrentYearApproved(final BigDecimal reCurrentYear) {
        reCurrentYearApproved = reCurrentYear;
    }

    public BigDecimal getBeNextYearApproved() {
        return beNextYearApproved;
    }

    public void setBeNextYearApproved(final BigDecimal beNextYear) {
        beNextYearApproved = beNextYear;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setReCurrentYearOriginal(final BigDecimal reCurrentYearOriginal) {
        this.reCurrentYearOriginal = reCurrentYearOriginal;
    }

    public BigDecimal getReCurrentYearOriginal() {
        return reCurrentYearOriginal;
    }

    public void setBeNextYearOriginal(final BigDecimal beNextYearOriginal) {
        this.beNextYearOriginal = beNextYearOriginal;
    }

    public BigDecimal getBeNextYearOriginal() {
        return beNextYearOriginal;
    }
}
