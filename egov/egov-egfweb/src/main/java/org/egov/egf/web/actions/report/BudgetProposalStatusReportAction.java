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
package org.egov.egf.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.model.BudgetProposalStatus;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetDetail;
import org.egov.utils.BudgetDetailHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@ParentPackage("egov")
@Results({
    @Result(name = "reportSearch", location = "budgetProposalStatusReport-reportSearch.jsp")
})
public class BudgetProposalStatusReportAction extends BaseFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;


    /**
     *
     */
    private static final long serialVersionUID = 2763108637417639564L;
    private List<BudgetProposalStatus> budgetProposalStatusDeptList = new ArrayList<BudgetProposalStatus>();
    private List<BudgetProposalStatus> budgetProposalStatusFuncList = new ArrayList<BudgetProposalStatus>();
    private List<Department> departmentList = new ArrayList<Department>();
    private List<CFunction> functionList = new ArrayList<CFunction>();
    private Department department;
    private String fundType;
    private String budgetType;
    private String mode;
    private BudgetDetailHelper budgetDetailHelper;
    private final String asstBudDesg = "ASSISTANT";
    private final String smBudDesg = "SECTION MANAGER";
    private final String aoBudDesg = "ACCOUNTS OFFICER";
    private final String caoBudDesg = "CHIEF ACCOUNTS OFFICER";
    private final String asstAdminDesg = "ASSISTANT";
    private final String smAdminDesg = "SECTION MANAGER";
    private final String asstBudFunc = "FMU";
    private final String smBudFunc = "FMU";
    private final String aoBudFunc = "FMU";
    private final String caoBudFunc = "FMU";
    private final String asstAdminFunc = "ADMIN";
    private final String smAdminFunc = "ADMIN";
    private final String heavyCheckMark = "\u2714";
    private String finYearId;
    private Date todayDate;
    private StringBuffer statementheading = new StringBuffer();
    private FinancialYearHibernateDAO financialYearDAO;
    protected EisCommonService eisCommonService;

    public BudgetProposalStatusReportAction() {
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void prepare() {
        // persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
    }

    @Action(value = "/report/budgetProposalStatusReport-beforeSearch")
    public String beforeSearch() {
        addDropdownData("departmentList", getPersistenceService().findAllBy("from Department order by deptName"));
        return "reportSearch";
    }

    @Action(value = "/report/budgetProposalStatusReport-search")
    public String search() {
        addDropdownData("departmentList", getPersistenceService().findAllBy("from Department order by deptName"));

        if (mode.equals("function"))
            functionWise();
        else
            departmentWise();
        // persistenceService.getSession().setDefaultReadOnly(false);
        return "reportSearch";
    }

    public void departmentWise() {
        departmentList = persistenceService.findAllBy("from Department order by id");
        finYearId = financialYearDAO.getCurrYearFiscalId();
        final CFinancialYear currYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?",
                Long.valueOf(finYearId));
        setTodayDate(new Date());
        for (final Department dept : departmentList) {
            final BudgetProposalStatus budgetProposalStatus = new BudgetProposalStatus();
            budgetProposalStatus.setDepartment(dept);
            final BudgetDetail budgetDetail = (BudgetDetail) persistenceService
                    .find("from BudgetDetail where budget.financialYear.id=? and executingDepartment=? and budget.isbere='RE' and budget.state.value<>'END' and budgetGroup.accountType=?",
                            Long.valueOf(finYearId), dept, fundType + "_" + budgetType);
            if (budgetDetail != null && budgetDetail.getBudget() != null && budgetDetail.getBudget().getState() != null
                    && budgetDetail.getBudget().getState().getOwnerPosition() != null) {
                final Assignment assignment = (Assignment) persistenceService.find(
                        "from Assignment where isPrimary=? and position=?",
                        'Y', budgetDetail.getBudget().getState().getOwnerPosition());
                if (assignment != null)
                    if (eisCommonService.isHod(assignment.getId()))
                        budgetProposalStatus.setHod(heavyCheckMark);
                    else if (assignment.getDesignation().getName().equals(asstBudDesg)
                            && assignment.getFunctionary().getName().equals(asstBudFunc))
                        budgetProposalStatus.setAsstBud(heavyCheckMark);
                    else if (assignment.getDesignation().getName().equals(smBudDesg)
                            && assignment.getFunctionary().getName().equals(smBudFunc))
                        budgetProposalStatus.setSmBud(heavyCheckMark);
                    else if (assignment.getDesignation().getName().equals(aoBudDesg)
                            && assignment.getFunctionary().getName().equals(aoBudFunc))
                        budgetProposalStatus.setAoBud(heavyCheckMark);
                    else if (assignment.getDesignation().getName().equals(caoBudDesg)
                            && assignment.getFunctionary().getName().equals(caoBudFunc))
                        budgetProposalStatus.setCaoBud(heavyCheckMark);
            }
            budgetProposalStatusDeptList.add(budgetProposalStatus);
        }
        setStatementheading(statementheading.append("Budget Proposal Status for Financial Year ").append(
                currYear.getFinYearRange()));
    }

    public void functionWise() {
        final String accountType = budgetDetailHelper.accountTypeForFunctionDeptMap(budgetType);
        functionList = persistenceService.findAllBy(
                "select dfm.function from EgDepartmentFunctionMap dfm where dfm.department.id=? and dfm.budgetAccountType=? ",
                department.getId(), accountType);
        finYearId = financialYearDAO.getCurrYearFiscalId();
        final CFinancialYear currYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?",
                Long.valueOf(finYearId));
        final Department dept = (Department) persistenceService.find("from Department where id=?", department.getId());
        setTodayDate(new Date());
        for (final CFunction func : functionList) {
            final BudgetProposalStatus budgetProposalStatus = new BudgetProposalStatus();
            budgetProposalStatus.setFunction(func);
            final BudgetDetail budgetDetail = (BudgetDetail) persistenceService
                    .find("from BudgetDetail where budget.financialYear.id=? and executingDepartment.id=? and budget.isbere='RE' and budget.state.value<>'END' and state.value<>'END' and function=? and budgetGroup.accountType=?",
                            Long.valueOf(finYearId), department.getId(), func, fundType + "_" + budgetType);
            if (budgetDetail != null && budgetDetail.getState() != null && budgetDetail.getState().getOwnerPosition() != null) {
                final Assignment assignment = (Assignment) persistenceService.find(
                        "from Assignment where isPrimary=? and position=?",
                        'Y', budgetDetail.getState().getOwnerPosition());
                if (assignment != null)
                    if (assignment.getDesignation().getName().equals(asstAdminDesg)
                            && assignment.getFunctionary().getName().equals(asstAdminFunc))
                        budgetProposalStatus.setAsstAdmin(heavyCheckMark);
                    else if (assignment.getDesignation().getName().equals(smAdminDesg)
                            && assignment.getFunctionary().getName().equals(smAdminFunc))
                        budgetProposalStatus.setSmAdmin(heavyCheckMark);
                    else if (eisCommonService.isHod(assignment.getId()))
                        budgetProposalStatus.setHod(heavyCheckMark);
            }
            budgetProposalStatusFuncList.add(budgetProposalStatus);
        }
        setStatementheading(statementheading.append("Budget Detail Proposal Status of Department ").append(dept.getName())
                .append(" for Financial Year ").append(currYear.getFinYearRange()));
    }

    public List<BudgetProposalStatus> getBudgetProposalStatusDeptList() {
        return budgetProposalStatusDeptList;
    }

    public void setBudgetProposalStatusDeptList(
            final List<BudgetProposalStatus> budgetProposalStatusDeptList) {
        this.budgetProposalStatusDeptList = budgetProposalStatusDeptList;
    }

    public List<BudgetProposalStatus> getBudgetProposalStatusFuncList() {
        return budgetProposalStatusFuncList;
    }

    public void setBudgetProposalStatusFuncList(
            final List<BudgetProposalStatus> budgetProposalStatusFuncList) {
        this.budgetProposalStatusFuncList = budgetProposalStatusFuncList;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(final Date todayDate) {
        this.todayDate = todayDate;
    }

    public StringBuffer getStatementheading() {
        return statementheading;
    }

    public void setStatementheading(final StringBuffer statementheading) {
        this.statementheading = statementheading;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(final String fundType) {
        this.fundType = fundType;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(final String budgetType) {
        this.budgetType = budgetType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetDetailHelper) {
        this.budgetDetailHelper = budgetDetailHelper;
    }

}