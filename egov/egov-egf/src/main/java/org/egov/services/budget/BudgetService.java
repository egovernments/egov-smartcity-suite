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
package org.egov.services.budget;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Manikanta
 *
 */
public class BudgetService extends PersistenceService<Budget, Long> {
    private static final Logger LOGGER = Logger.getLogger(BudgetService.class);
    
    @Autowired
    protected EisCommonService eisCommonService;
    protected WorkflowService<Budget> budgetWorkflowService;
    @Autowired
    @Qualifier("workflowService")
    protected SimpleWorkflowService<BudgetDetail> budgetDetailWorkflowService;
    
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public BudgetService() {
        super(Budget.class);
    }

    public BudgetService(final Class<Budget> type) {
        super(type);
    }

    public Budget getByName(final String name) {
        return find("from Budget b where b.name = ?", name);
    }

    public User getUser() {
        return (User) ((PersistenceService) this).find(" from User where id=?", ApplicationThreadLocals.getUserId());
    }

    public Position getPositionForEmployee(final Employee emp) throws ApplicationRuntimeException
    {
        return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getId());
    }

    /**
     * return the department of any one budget detail will work for only leaf budget not for non leaf budgets
     * @param budget
     * @return Department
     * @throws ApplicationRuntimeException
     */
    public Department getDepartmentForBudget(final Budget budget) throws ApplicationRuntimeException
    {

        Department dept = null;
        final List<BudgetDetail> detailList = ((PersistenceService) this).findAllBy(
                "from  BudgetDetail budgetDetail where budgetDetail.budget=?", budget);
        if (detailList.isEmpty() || detailList.size() == 0)
            throw new ApplicationRuntimeException("Details not found for the Budget" + budget.getName());
        else if (detailList.get(0).getExecutingDepartment() == null)
            throw new ApplicationRuntimeException("Department not found for the Budget" + budget.getName());
        else
            dept = detailList.get(0).getExecutingDepartment();
        return dept;
    }

    /**
     * returns department of the employee from assignment for the current date
     * @param emp
     * @return department
     */
    public Department depertmentForEmployee(final Employee emp)
    {
        Department dept = null;
        final Date currDate = new Date();
        try {
            final Assignment empAssignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(emp.getId(), currDate);
            dept = empAssignment.getDepartment();
            return dept;
        } catch (final NullPointerException ne)
        {
            throw new ApplicationRuntimeException(ne.getMessage());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error while getting Department fort the employee" + emp.getName());
        }
    }

    public boolean hasReForYear(final Long financialYear) {
        return checkForRe("from  Budget where financialYear.id=? and isbere='RE' and isActiveBudget=true", financialYear);
    }

    public boolean hasApprovedBeForYear(final Long financialYear) {
        return checkForRe(
                "from  Budget where financialYear.id=? and isbere='BE' and isActiveBudget=true and parent is null and isPrimaryBudget=true and status.code='Approved'",
                financialYear);
    }

    public boolean hasApprovedReForYear(final Long financialYear) {
        return checkForRe(
                "from  Budget where financialYear.id=? and isbere='RE' and isActiveBudget=true and parent is null and isPrimaryBudget=true and status.code='Approved'",
                financialYear);
    }

    /**
     *
     * @param financialYear
     * @return boolean Finds out whether RE is created and Approved for the given date
     */
    public boolean hasApprovedReAsonDate(final Long finYearId, final Date budgetApprovedDate) {
        final Query qry = getSession()
                .createQuery(
                        "select name from  Budget where financialYear.id=:finYearId and isbere='RE' "
                                +
                                "and isActiveBudget=true and parent is null and isPrimaryBudget=true and status.code='Approved' and to_date(state.createdDate)<=:budgetApprovedDate");
        qry.setParameter("finYearId", finYearId);
        qry.setParameter("budgetApprovedDate", budgetApprovedDate);
        final String approvedBudgetName = (String) qry.uniqueResult();
        return approvedBudgetName == null ? false : true;
    }

    private boolean checkForRe(final String query, final Long financialYear) {
        final Budget budget = find(query, financialYear);
        if (budget == null)
            return false;
        return true;
    }

    /**
     * finds all the child budget tree for approval or rejection
     * @param b
     * @param position
     * @return
     */
    public List<Budget> moveBudgetTree(final Budget b, final Position position) {
        final List<Budget> budgetsList = findAllBy("from Budget b where b.materializedPath like '" + b.getMaterializedPath()
                + ".%'");
        return budgetsList;
    }

    /**
     * @param budgetsList
     * @return The Base Budget Item is set from action class for which the details are moved Budget b is used to avoid recursive
     * class
     */
    public void moveDetailsTree(final List budgetsList, final Budget b, final String actionName) {
        for (final Object o : budgetsList) {
            final Budget childBudget = (Budget) o;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Budget name " + childBudget.getName() + "moved details are ...");
            final List<BudgetDetail> unsavedbudgetDetailList = new ArrayList<BudgetDetail>();
            // unsavedbudgetDetailList.addAll(budgetDetailService.getRemainingDetailsForApproveOrReject(childBudget));
            // move rest of the details
            for (final BudgetDetail detail : unsavedbudgetDetailList) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("moveDetails" + detail.getApprovedAmount());
                budgetDetailWorkflowService.transition(actionName, detail, detail.getComment());
            }
        }
    }

    public boolean canForwardParent(final Position position, final Budget b) {
        final Budget treeBudget = b;
        final List<Budget> totalCountList = findAllBy("from Budget where parent=? and isActiveBudget=?", treeBudget.getParent(),
                true);
        // can forward the budget even though one of the child is not in the state of forwarding if isActiveBudget is set as false
        final List<Budget> budgetList = findAllBy("from Budget where state.owner=? and parent.id=? and isActiveBudget=?",
                position,
                treeBudget.getParent().getId(), true);
        if (totalCountList.size() == budgetList.size()) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Approving Parent Budget:... " + treeBudget.getParent().getName());
            return true;
        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Still some ChildBudgets are pending to  move  Parent .Exiting.... ");
            return false;
        }
    }

    /**
     *
     * @param bgroupList
     * @return List of CChartOfAccounts objects associated with each BudgetGroup object in the list , null if no CChartOfAccounts
     * objects are associated with any BudgetGroup object in the list.
     * @throws ValidationException
     *
     */

    @SuppressWarnings("unchecked")
    public List<CChartOfAccounts> getAccountCodeForBudgetHead(final List<BudgetGroup> bgroupList) throws ValidationException
    {

        if (bgroupList == null)
            throw new ValidationException(Arrays.asList(new ValidationError("BudgetGroup List is Null",
                    "budgetgroup.list.is.null")));
        if (bgroupList.size() == 0)
            throw new ValidationException(Arrays.asList(new ValidationError("BudgetGroup List is Empty",
                    "budgetgroup.list.is.empty")));
        final List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
        final Integer maxpossibleGlcodeLength = Integer.parseInt(EGovConfig.getProperty("egf_config.xml", "glcodeMaxLength", "",
                "AccountCode"));

        CChartOfAccounts maxCode, minCode;
        String maxGlcodeStr, minGlcodeStr, glcodeFrom, glcodeTo;
        final String multipleZeros = new String("00000000000000000000");
        final String multipleNines = new String("99999999999999999999");
        for (final BudgetGroup bdgtgrp : bgroupList)
            if (((PersistenceService) this).find("from BudgetGroup where id = ? ", bdgtgrp.getId()) == null)
                throw new ValidationException(Arrays.asList(new ValidationError("BudgetGroup with id:" + bdgtgrp.getId()
                        + " and name:" + bdgtgrp.getName() + " does not exist ", "BudgetGroup with id:" + bdgtgrp.getId()
                        + " and name:" + bdgtgrp.getName() + " does not exist ")));
        List<CChartOfAccounts> clist;
        for (final BudgetGroup bdgtgrp : bgroupList)
        {
            if (bdgtgrp.getMajorCode() == null)
            {
                maxCode = bdgtgrp.getMaxCode();
                minCode = bdgtgrp.getMinCode();
                if (maxCode != null && minCode != null) {
                    maxGlcodeStr = maxCode.getGlcode();
                    minGlcodeStr = minCode.getGlcode();
                    if (maxpossibleGlcodeLength == maxGlcodeStr.length())
                    {
                        glcodeFrom = minGlcodeStr;
                        glcodeTo = maxGlcodeStr;
                    }
                    else
                    {
                        glcodeFrom = minGlcodeStr + multipleZeros.substring(0, maxpossibleGlcodeLength - minGlcodeStr.length());
                        glcodeTo = maxGlcodeStr + multipleNines.substring(0, maxpossibleGlcodeLength - maxGlcodeStr.length());
                    }
                    final String query = new String(
                            "from  CChartOfAccounts coa where cast(coa.glcode,long) between ? and ? and coa.classification = ?  and coa.isActiveForPosting=? ");
                    clist = ((PersistenceService) this).findAllBy(query, Long.parseLong(glcodeFrom),
                            Long.parseLong(glcodeTo), Long.valueOf(4), Long.valueOf(1));

                }
                else
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            "Maxcode or Mincode is null also Majorcode is null for BudgetGroup:" + bdgtgrp.getName(),
                            "maxcode.or.mincode.is.null.and.majorcode.is.null.for.budgetgroup:" + bdgtgrp.getName())));
            } else
                clist = ((PersistenceService) this).findAllBy(
                        "from  CChartOfAccounts coa where coa.glcode like '" + bdgtgrp.getMajorCode().getGlcode().toString()
                                + "%' and coa.classification = ? and coa.isActiveForPosting= ? ", Long.valueOf(4),
                        Long.valueOf(1));
            if (clist != null && clist.size() != 0)
                coaList.addAll(clist);
        }

        return coaList.size() == 0 ? null : coaList;
    }

    public void setBudgetWorkflowService(final WorkflowService<Budget> budgetWorkflowService) {
        this.budgetWorkflowService = budgetWorkflowService;
    }

    public void setBudgetDetailWorkflowService(final SimpleWorkflowService<BudgetDetail> budgetDetailWorkflowService) {
        this.budgetDetailWorkflowService = budgetDetailWorkflowService;
    }

    public String getEmployeeNameAndDesignationForPosition(final Position pos) throws ApplicationRuntimeException {
        final Employee pi = eisCommonService.getPrimaryAssignmentEmployeeForPos(pos.getId());
        final Assignment assignment = eisCommonService.getLatestAssignmentForEmployee(pi.getId());
        return pi.getName() + " (" + assignment.getDesignation().getName() + ")";
    }

    public PersonalInformation getEmpForCurrentUser() {
        return eisCommonService.getEmployeeByUserId(ApplicationThreadLocals.getUserId());
    }

    public Budget getReferenceBudgetFor(final Budget budget) {
        Budget refBudget = null;
        try {
            refBudget = find("from Budget where referenceBudget.id=?", budget.getId());
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return refBudget;
    }

    public boolean isLeaf(final Budget budget) {
        final List<Budget> budgetList = findAllBy(
                "from Budget where financialYear.id=? and id in (select parent from Budget where financialYear.id=? and parent.id=?)",
                budget.getFinancialYear().getId(), budget.getFinancialYear().getId(), budget.getId());
        return budgetList == null || budgetList.isEmpty();
    }

    public List getFYForNonApprovedBudgets() {
        return findAllBy("select distinct b.financialYear from Budget b where b.status.code=!'Approved' and isActiveBudget=true and isPrimaryBudget=true order by b.financialYear.finYearRange desc");
    }

    public Budget getBudget(String budgetHead, String deptCode, String budgetType, String fyear) {
        String budgetName;
        if (budgetHead.substring(0, 1).equalsIgnoreCase("1")
                || budgetHead.substring(0, 1).equalsIgnoreCase("2"))
            budgetName = deptCode + "-" + budgetType + "-Rev-" + fyear;
        else
            budgetName = deptCode + "-" + budgetType + "-Cap-" + fyear;

        return getByName(budgetName);
    }

    public List<Budget> getBudgetsForUploadReport() {
        return findAllBy("select distinct b from Budget b where b.name like '%RE%' and b.materializedPath  in (select distinct substring(bd.materializedPath,  1 , 1) from BudgetDetail bd where bd.status.code = 'Created')");
    }

    @Transactional
    public void updateByMaterializedPath(final String materializedPath) {
        EgwStatus approvedStatus = egwStatusDAO.getStatusByModuleAndCode("BUDGET", "Approved");
        EgwStatus createdStatus = egwStatusDAO.getStatusByModuleAndCode("BUDGET", "Created");
        persistenceService
                .getSession()
                .createSQLQuery(
                        "update egf_budget set status = :approvedStatus where status =:createdStatus and  materializedPath like'"
                                + materializedPath + "%'").setLong("approvedStatus", approvedStatus.getId())
                .setLong("createdStatus", createdStatus.getId()).executeUpdate();
    }
}