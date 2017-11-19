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

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class BudgetUpload  {

    private static final long serialVersionUID = 6136656142691290863L;

    private Long id;

    private String reFinYear;

    private String beFinYear;

    private String fundCode;

    private String deptCode;

    private String functionCode;

    private String budgetHead;

    private String narration;

    private String oldACCode;

    private String errorReason;

    private String finalStatus;

    private Long refBudId;

    private Long planningPercentage;

    private BigDecimal beAmount;

    private BigDecimal reAmount;

    private Fund fund;

    private CFunction function;

    private Department dept;

    private CChartOfAccounts coa;
    
   private MultipartFile budgetInXls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReFinYear() {
        return reFinYear;
    }

    public void setReFinYear(String reFinYear) {
        this.reFinYear = reFinYear;
    }

    public String getBeFinYear() {
        return beFinYear;
    }

    public void setBeFinYear(String beFinYear) {
        this.beFinYear = beFinYear;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getOldACCode() {
        return oldACCode;
    }

    public void setOldACCode(String oldACCode) {
        this.oldACCode = oldACCode;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Long getRefBudId() {
        return refBudId;
    }

    public void setRefBudId(Long refBudId) {
        this.refBudId = refBudId;
    }

    public Long getPlanningPercentage() {
        return planningPercentage;
    }

    public void setPlanningPercentage(Long planningPercentage) {
        this.planningPercentage = planningPercentage;
    }

    public BigDecimal getBeAmount() {
        return beAmount;
    }

    public void setBeAmount(BigDecimal beAmount) {
        this.beAmount = beAmount;
    }

    public BigDecimal getReAmount() {
        return reAmount;
    }

    public void setReAmount(BigDecimal reAmount) {
        this.reAmount = reAmount;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(CFunction function) {
        this.function = function;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    public CChartOfAccounts getCoa() {
        return coa;
    }

    public void setCoa(CChartOfAccounts coa) {
        this.coa = coa;
    }

    public MultipartFile getBudgetInXls() {
        return budgetInXls;
    }

    public void setBudgetInXls(MultipartFile budgetInXls) {
        this.budgetInXls = budgetInXls;
    }

    

}
