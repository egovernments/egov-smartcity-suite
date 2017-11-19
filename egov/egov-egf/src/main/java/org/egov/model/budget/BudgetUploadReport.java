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

package org.egov.model.budget;

import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;

import java.math.BigDecimal;

public class BudgetUploadReport {

    private Budget reBudget;

    private Fund fund;

    private Department department;

    private CFunction function;

    private BigDecimal approvedReAmount;

    private BigDecimal planningReAmount;

    private BigDecimal approvedBeAmount;

    private BigDecimal planningBeAmount;

    private String fundCode;

    private String functionCode;

    private String deptCode;

    private String glCode;

    private String beBudgetName;

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(CFunction function) {
        this.function = function;
    }

    public Budget getReBudget() {
        return reBudget;
    }

    public void setReBudget(Budget reBudget) {
        this.reBudget = reBudget;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getBeBudgetName() {
        return beBudgetName;
    }

    public void setBeBudgetName(String beBudgetName) {
        this.beBudgetName = beBudgetName;
    }

    public BigDecimal getApprovedReAmount() {
        return approvedReAmount;
    }

    public void setApprovedReAmount(BigDecimal approvedReAmount) {
        this.approvedReAmount = approvedReAmount;
    }

    public BigDecimal getPlanningReAmount() {
        return planningReAmount;
    }

    public void setPlanningReAmount(BigDecimal planningReAmount) {
        this.planningReAmount = planningReAmount;
    }

    public BigDecimal getApprovedBeAmount() {
        return approvedBeAmount;
    }

    public void setApprovedBeAmount(BigDecimal approvedBeAmount) {
        this.approvedBeAmount = approvedBeAmount;
    }

    public BigDecimal getPlanningBeAmount() {
        return planningBeAmount;
    }

    public void setPlanningBeAmount(BigDecimal planningBeAmount) {
        this.planningBeAmount = planningBeAmount;
    }

}