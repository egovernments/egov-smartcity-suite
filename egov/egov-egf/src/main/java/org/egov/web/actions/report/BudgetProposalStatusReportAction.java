/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.BudgetDetail;
import org.egov.utils.BudgetDetailHelper;
import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
@ParentPackage("egov")
public class BudgetProposalStatusReportAction extends BaseFormAction {
	
	private List<BudgetProposalStatus> budgetProposalStatusDeptList = new ArrayList<BudgetProposalStatus>();
	private List<BudgetProposalStatus> budgetProposalStatusFuncList = new ArrayList<BudgetProposalStatus>();
	private List<Department> departmentList = new ArrayList<Department>();
	private List<CFunction> functionList = new ArrayList<CFunction>();
	private Department department;
	private String fundType;
	private String budgetType;
	private String mode;
	private BudgetDetailHelper budgetDetailHelper;
	private final String asstBudDesg="ASSISTANT";
	private final String smBudDesg="SECTION MANAGER";
	private final String aoBudDesg="ACCOUNTS OFFICER";
	private final String caoBudDesg="CHIEF ACCOUNTS OFFICER";
	private final String asstAdminDesg="ASSISTANT";
	private final String smAdminDesg="SECTION MANAGER";
	private final String asstBudFunc="FMU";
	private final String smBudFunc="FMU";
	private final String aoBudFunc="FMU";
	private final String caoBudFunc="FMU";
	private final String asstAdminFunc="ADMIN";
	private final String smAdminFunc="ADMIN";
	private final String heavyCheckMark = "\u2714";
	private String finYearId;
	private Date todayDate;
	private StringBuffer statementheading=new StringBuffer();
	private FinancialYearHibernateDAO financialYearDAO;
	protected EisCommonService eisCommonService;
	
	public BudgetProposalStatusReportAction(){
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public void prepare(){
		//HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
	}
	
@Action(value="/report/budgetProposalStatusReport-beforeSearch")
	public String beforeSearch(){
		addDropdownData("departmentList", getPersistenceService().findAllBy("from Department order by deptName"));
		return "reportSearch";
	}
	
	public String search(){
		addDropdownData("departmentList", getPersistenceService().findAllBy("from Department order by deptName"));
		
		if(this.mode.equals("function")){
			functionWise();
		}else{
			departmentWise();
		}
		//HibernateUtil.getCurrentSession().setDefaultReadOnly(false);
		return "reportSearch";
	}
	
	public void departmentWise(){
		departmentList = persistenceService.findAllBy("from Department order by id");
		finYearId = financialYearDAO.getCurrYearFiscalId();
		CFinancialYear currYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?",Long.valueOf(finYearId));
		this.setTodayDate(new Date());
		for(Department dept : departmentList){
			BudgetProposalStatus budgetProposalStatus = new BudgetProposalStatus();
			budgetProposalStatus.setDepartment(dept);
			BudgetDetail budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where budget.financialYear.id=? and executingDepartment=? and budget.isbere='RE' and budget.state.value<>'END' and budgetGroup.accountType=?", Long.valueOf(this.finYearId), dept, this.fundType+"_"+this.budgetType);
			if(budgetDetail != null && budgetDetail.getBudget() != null && budgetDetail.getBudget().getState() != null && budgetDetail.getBudget().getState().getOwnerPosition() != null){
				Assignment assignment = (Assignment) persistenceService.find("from Assignment where isPrimary=? and position=?", 'Y', budgetDetail.getBudget().getState().getOwnerPosition());
				if(assignment != null){
					if(eisCommonService.isHod(assignment.getId()))
						budgetProposalStatus.setHod(this.heavyCheckMark);
					else if(assignment.getDesignation().getName().equals(this.asstBudDesg) && assignment.getFunctionary().getName().equals(this.asstBudFunc))
						budgetProposalStatus.setAsstBud(this.heavyCheckMark);
					else if(assignment.getDesignation().getName().equals(this.smBudDesg) && assignment.getFunctionary().getName().equals(this.smBudFunc))
						budgetProposalStatus.setSmBud(this.heavyCheckMark);
					else if(assignment.getDesignation().getName().equals(this.aoBudDesg) && assignment.getFunctionary().getName().equals(this.aoBudFunc))
						budgetProposalStatus.setAoBud(this.heavyCheckMark);
					else if(assignment.getDesignation().getName().equals(this.caoBudDesg) && assignment.getFunctionary().getName().equals(this.caoBudFunc))
						budgetProposalStatus.setCaoBud(this.heavyCheckMark);
				}
			}
			budgetProposalStatusDeptList.add(budgetProposalStatus);
		}
		this.setStatementheading(statementheading.append("Budget Proposal Status for Financial Year ").append(currYear.getFinYearRange()));
	}
	
	public void functionWise(){
		String accountType = budgetDetailHelper.accountTypeForFunctionDeptMap(this.budgetType);
		functionList = persistenceService.findAllBy("select dfm.function from EgDepartmentFunctionMap dfm where dfm.department.id=? and dfm.budgetAccountType=? ",this.department.getId(),accountType);    
		finYearId = financialYearDAO.getCurrYearFiscalId();
		CFinancialYear currYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?",Long.valueOf(finYearId));
		Department dept = (Department) persistenceService.find("from Department where id=?",this.department.getId());
		this.setTodayDate(new Date());
		for(CFunction func : functionList){
			BudgetProposalStatus budgetProposalStatus = new BudgetProposalStatus();
			budgetProposalStatus.setFunction(func);
			BudgetDetail budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where budget.financialYear.id=? and executingDepartment.id=? and budget.isbere='RE' and budget.state.value<>'END' and state.value<>'END' and function=? and budgetGroup.accountType=?", Long.valueOf(this.finYearId), this.department.getId(), func, this.fundType+"_"+this.budgetType);
			if(budgetDetail != null && budgetDetail.getState() != null && budgetDetail.getState().getOwnerPosition() != null){
				Assignment assignment = (Assignment) persistenceService.find("from Assignment where isPrimary=? and position=?", 'Y', budgetDetail.getState().getOwnerPosition());
				if(assignment != null){
					if(assignment.getDesignation().getName().equals(this.asstAdminDesg) && assignment.getFunctionary().getName().equals(this.asstAdminFunc))
						budgetProposalStatus.setAsstAdmin(this.heavyCheckMark);
					else if(assignment.getDesignation().getName().equals(this.smAdminDesg) && assignment.getFunctionary().getName().equals(this.smAdminFunc))
						budgetProposalStatus.setSmAdmin(this.heavyCheckMark);
					else if(eisCommonService.isHod(assignment.getId()))
						budgetProposalStatus.setHod(this.heavyCheckMark);
				}
			}
			budgetProposalStatusFuncList.add(budgetProposalStatus);
		}
		this.setStatementheading(statementheading.append("Budget Detail Proposal Status of Department ").append(dept.getName()).append(" for Financial Year ").append(currYear.getFinYearRange()));
	}

	public List<BudgetProposalStatus> getBudgetProposalStatusDeptList() {
		return budgetProposalStatusDeptList;
	}

	public void setBudgetProposalStatusDeptList(
			List<BudgetProposalStatus> budgetProposalStatusDeptList) {
		this.budgetProposalStatusDeptList = budgetProposalStatusDeptList;
	}

	public List<BudgetProposalStatus> getBudgetProposalStatusFuncList() {
		return budgetProposalStatusFuncList;
	}

	public void setBudgetProposalStatusFuncList(
			List<BudgetProposalStatus> budgetProposalStatusFuncList) {
		this.budgetProposalStatusFuncList = budgetProposalStatusFuncList;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public StringBuffer getStatementheading() {
		return statementheading;
	}

	public void setStatementheading(StringBuffer statementheading) {
		this.statementheading = statementheading;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setBudgetDetailHelper(BudgetDetailHelper budgetDetailHelper) {
		this.budgetDetailHelper = budgetDetailHelper;
	}



}
