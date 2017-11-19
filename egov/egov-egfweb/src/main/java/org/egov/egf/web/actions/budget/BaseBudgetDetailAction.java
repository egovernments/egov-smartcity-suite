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
package org.egov.egf.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.model.BudgetAmountView;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.services.budget.BudgetDetailActionHelper;
import org.egov.services.budget.BudgetDetailHelperBean;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

public abstract class BaseBudgetDetailAction extends GenericWorkFlowAction {
    private static final String EGF_BUDGET_GROUP = "egf-budgetGroup";
    private static final String EGI_FUNCTIONARY = "egi-functionary";
    private static final String EGI_FUNCTION = "egi-function";
    private static final String EGI_DEPARTMENT = "egi-department";
    private static final String HOD_NOT_FOUND = "hod.not.found";
    private static final long serialVersionUID = 1L;
    protected BudgetDetail budgetDetail = new BudgetDetail();
    protected List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
    protected List<BudgetDetail> savedbudgetDetailList = new ArrayList<BudgetDetail>();

    @Autowired
    @Qualifier("budgetDetailService")
    protected BudgetDetailService budgetDetailService;

    @Autowired
    @Qualifier("budgetService")
    protected BudgetService budgetService;

    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> gridFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
    public WorkflowBean workflowBean = new WorkflowBean();
    protected WorkflowService<Budget> budgetWorkflowService;
    protected WorkflowService<BudgetDetail> budgetDetailWorkflowService;
    protected boolean headerDisabled = false;
    protected List<BudgetAmountView> budgetAmountView = new ArrayList<BudgetAmountView>();
    protected String currentYearRange;
    protected String previousYearRange;
    private String nextYearRange;
    protected String lastButOneYearRange;
    protected List<Scheme> subSchemes;
    protected Integer schemeId;
    protected Date asOnDate;

    @Autowired
    private EisCommonService eisCommonService;
    protected BudgetDetailHelper budgetDetailHelper;
    protected boolean addNewDetails = false;

    @Autowired
    private BudgetDetailActionHelper budgetDetailActionHelper;

    private static final String NEWRE = "new-re";
    private static final String BUDGETLIST = "budgetList";
    private static final String RE = "RE";
    private static final String SAVE = "budgetdetail.save";
    private static final String BUDGETRE = "budgetDetail.re.amount";
    private static final String BUDGETBE = "budgetDetail.be.amount";
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    protected BudgetDetailConfig budgetDetailConfig;

    private static final String BUDGETMANDATORY = "budgetDetail.budget.mandatory";

    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private EgovMasterDataCaching masterDataCache;

    protected boolean re = false;
    private boolean showMessage = false;
    protected List<BigDecimal> beAmounts = new ArrayList<BigDecimal>();
    private Budget referenceBudget;
    private CFinancialYear financialYear;
    protected List<Budget> budgetList = new ArrayList<Budget>();
    protected boolean showRe;
    public Long budgetDocumentNumber;
    protected boolean showDetails;
    protected Long searchfunctionid;
    protected Long searchbudgetGroupid;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    private static Logger LOGGER = Logger.getLogger(BaseBudgetDetailAction.class);

    public abstract void populateSavedbudgetDetailListFor(Budget budget);

    public abstract void populateSavedbudgetDetailListForDetail(BudgetDetail bd);

    protected abstract void saveAndStartWorkFlow(BudgetDetail detail, WorkflowBean workflowBean);

    protected abstract void saveAndStartWorkFlowForRe(BudgetDetail detail, int index, CFinancialYear finYear,
            Budget refBudget, WorkflowBean workflowBean);

    protected abstract void approve();

    @Override
    public String execute() {
        return NEW;
    }

    @ValidationErrorPage(value = NEWRE)
    @Action(value = "/budget/budgetProposalDetail-createRE")
    public String createRe() {
        showRe = true;
        try {
            getActionMessages().clear();
            validateMandatoryFields();
            budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);

            budgetDetail = budgetDetailList.get(0);
            validateAmounts(budgetDetailList);
            Assignment assignment = new Assignment();
            if (!FinancialConstants.BUTTONSAVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {

                final List<Assignment> assignmentList = assignmentService
                        .findAllAssignmentsByHODDeptAndDates(budgetDetail.getExecutingDepartment().getId(), new Date());
                if (assignmentList.isEmpty())
                    throw new ValidationException(Arrays.asList(new ValidationError(HOD_NOT_FOUND, HOD_NOT_FOUND)));
                assignment = assignmentList.get(0);
                approverPositionId = assignment.getPosition().getId();
            } else
                approverPositionId = getPosition().getId();
            populateWorkflowBean();
            financialYear = financialYearService.findOne(financialYear.getId());
            final EgwStatus egwStatus = egwStatusHibernateDAO.getStatusByModuleAndCode(FinancialConstants.BUDGETDETAIL,
                    FinancialConstants.BUDGETDETAIL_CREATED_STATUS);
            budgetDetailActionHelper.create(new BudgetDetailHelperBean(addNewDetails, beAmounts, budgetDetailList,
                    egwStatus, budgetDetail, searchbudgetGroupid, searchfunctionid, workflowBean));
            setAsOnDateOnSelectedBudget();

            showMessage = true;
            if (FinancialConstants.BUTTONSAVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText(SAVE));
            else
                addActionMessage(getText("budgetdetail.forwarded") + assignment.getEmployee().getName());
            dropdownData.put(BUDGETLIST, Collections.emptyList());
            budgetDetail = new BudgetDetail();
            budgetDetail.setExecutingDepartment(null);
        } catch (final ValidationException e) {
            loadBudgets(RE);
            dropdownData.put(BUDGETLIST, budgetList);
            referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
            throw e;
        }
        return NEWRE;
    }

    /**
     * @param budget deletes the existing selected budgets from db
     */

    private void validateAmounts(final List<BudgetDetail> detailList) {
        for (int i = 0; i < detailList.size(); i++) {
            if (beAmounts.get(i) == null)
                throw new ValidationException(Arrays.asList(new ValidationError(BUDGETRE, BUDGETRE)));
            if (detailList.get(i).getOriginalAmount() == null)
                throw new ValidationException(Arrays.asList(new ValidationError(BUDGETBE, BUDGETBE)));
        }
    }

    protected void validateMandatoryFields() {
        final String deptMandatory = "budgetDetail.executingDepartment.mandatory";
        final String functionMandatory = "budgetDetail.function.mandatory";
        final String functionaryMandatory = "budgetDetail.functionary.mandatory";
        final String fundMandatory = "budgetDetail.fund.mandatory";
        checkHeaderMandatoryField(Constants.EXECUTING_DEPARTMENT, budgetDetail.getExecutingDepartment(), deptMandatory);
        checkHeaderMandatoryField(Constants.FUNCTION, budgetDetail.getFunction(), functionMandatory);
        checkHeaderMandatoryField(Constants.FUNCTIONARY, budgetDetail.getFunctionary(), functionaryMandatory);
        checkHeaderMandatoryField(Constants.FUND, budgetDetail.getFund(), fundMandatory);

        for (final BudgetDetail detail : budgetDetailList) {
            setRelatedValues(detail);
            checkGridMandatoryField(Constants.EXECUTING_DEPARTMENT, detail.getExecutingDepartment(), deptMandatory);
            checkGridMandatoryField(Constants.FUNCTION, detail.getFunction(), functionMandatory);
            checkGridMandatoryField(Constants.FUNCTIONARY, detail.getFunctionary(), functionaryMandatory);
            checkGridMandatoryField(Constants.FUND, detail.getFund(), fundMandatory);
        }
    }

    private void setRelatedValues(final BudgetDetail detail) {
        if (detail.getExecutingDepartment() != null && detail.getExecutingDepartment().getId() == 0)
            detail.setExecutingDepartment(null);
        if (detail.getFunction() != null && detail.getFunction().getId() == 0)
            detail.setFunction(null);
        if (detail.getScheme() != null && detail.getScheme().getId() == 0)
            detail.setScheme(null);
        if (detail.getSubScheme() != null && detail.getSubScheme().getId() == 0)
            detail.setSubScheme(null);
        if (detail.getFunctionary() != null && detail.getFunctionary().getId() == 0)
            detail.setFunctionary(null);
        if (detail.getBoundary() != null && detail.getBoundary().getId() == 0)
            detail.setBoundary(null);
        if (detail.getFund() != null && detail.getFund().getId() == 0)
            detail.setFund(null);
    }

    protected void checkHeaderMandatoryField(final String fieldName, final Object value, final String errorKey) {
        if (headerFields.contains(fieldName) && mandatoryFields.contains(fieldName) && value == null)
            throw new ValidationException(Arrays.asList(new ValidationError(errorKey, errorKey)));
    }

    protected void checkGridMandatoryField(final String fieldName, final Object value, final String errorKey) {
        if (gridFields.contains(fieldName) && mandatoryFields.contains(fieldName) && value == null)
            throw new ValidationException(Arrays.asList(new ValidationError(errorKey, errorKey)));
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    private void setAsOnDateOnSelectedBudget() {
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != null) {
            final Budget selectedBudget = (Budget) getPersistenceService().find("from Budget where id=?",
                    budgetDetail.getBudget().getId());
            selectedBudget.setAsOnDate(getAsOnDate());
            budgetService.update(selectedBudget);
        }
    }

    public void loadBudgets(final String bere) {
        budgetList = new ArrayList<Budget>();
        if (!addNewDetails)
            budgetList.addAll(persistenceService
                    .findAllBy("from Budget where id not in (select parent from Budget where parent is not null) "
                            + "and isactivebudget = true  and isbere='" + bere.toUpperCase()
                            + "' and status.code!='Approved' and financialYear.id = " + getFinancialYear().getId()
                            + " order by name"));
        else
            budgetList.addAll(persistenceService
                    .findAllBy("from Budget where id not in (select parent from Budget where parent is not null) "
                            + "and isactivebudget = true  and isbere='" + bere.toUpperCase()
                            + "'  and financialYear.id = " + getFinancialYear().getId() + " order by name"));

    }

    @Override
    public void prepare() {
        super.prepare();
        populateFinancialYear();
        headerFields = budgetDetailConfig.getHeaderFields();
        gridFields = budgetDetailConfig.getGridFields();
        mandatoryFields = budgetDetailConfig.getMandatoryFields();
        addRelatedEntity("budget", Budget.class);
        addRelatedEntity(Constants.BUDGET_GROUP, BudgetGroup.class);
        if (shouldShowField(Constants.FUNCTIONARY))
            addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
        if (shouldShowField(Constants.FUNCTION))
            addRelatedEntity(Constants.FUNCTION, CFunction.class);
        if (shouldShowField(Constants.SCHEME))
            addRelatedEntity(Constants.SCHEME, Scheme.class);
        if (shouldShowField(Constants.SUB_SCHEME))
            addRelatedEntity(Constants.SUB_SCHEME, SubScheme.class);
        if (shouldShowField(Constants.FUND))
            addRelatedEntity(Constants.FUND, Fund.class);
        if (shouldShowField(Constants.EXECUTING_DEPARTMENT))
            addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
        if (shouldShowField(Constants.BOUNDARY))
            addRelatedEntity(Constants.BOUNDARY, Boundary.class);
        setupDropdownsInHeader();
        re = budgetService
                .hasReForYear(Long.valueOf(financialYearService.getFinancialYearByDate(new Date()).getId().toString()));
    }

    private void setupDropdownsInHeader() {
        setupDropdownDataExcluding(Constants.SUB_SCHEME);
        setBudgetDropDown();
        dropdownData.put("budgetGroupList", masterDataCache.get(EGF_BUDGET_GROUP));
        if (shouldShowField(Constants.SUB_SCHEME))
            dropdownData.put("subSchemeList", Collections.EMPTY_LIST);
        if (shouldShowField(Constants.FUNCTIONARY))
            dropdownData.put("functionaryList", masterDataCache.get(EGI_FUNCTIONARY));
        if (shouldShowField(Constants.FUNCTION))
            dropdownData.put("functionList", masterDataCache.get(EGI_FUNCTION));
        if (shouldShowField(Constants.SCHEME))
            dropdownData.put("schemeList",
                    persistenceService.findAllBy("from Scheme where isActive=true order by name"));
        if (shouldShowField(Constants.EXECUTING_DEPARTMENT))
            dropdownData.put("executingDepartmentList", masterDataCache.get(EGI_DEPARTMENT));
        if (shouldShowField(Constants.FUND))
            dropdownData.put("fundList",
                    persistenceService.findAllBy("from Fund where isNotLeaf=false and isActive=true order by name"));
        if (shouldShowField(Constants.BOUNDARY))
            dropdownData.put("boundaryList", persistenceService.findAllBy("from Boundary order by name"));
        addDropdownData("financialYearList", getPersistenceService()
                .findAllBy("from CFinancialYear where isActive=true order by " + "finYearRange desc "));
        dropdownData.put("departmentList", masterDataCache.get(EGI_DEPARTMENT));
        dropdownData.put("designationList", Collections.emptyList());
        dropdownData.put("userList", Collections.emptyList());
    }

    protected void populateFinancialYear() {
        final Budget budget = budgetDetail.getBudget();
        if (budget != null) {
            populateYearRange(budget);
            return;
        }
        if (request.get("id") != null) {
            final Long id = (Long) request.get("id");
            if (!Long.valueOf(0).equals(id)) {
                final Budget b = budgetService.findById(id, false);
                populateYearRange(b);
                return;
            }
        }

        final CFinancialYear finYear = financialYearService.findOne(budgetDetailHelper.getFinancialYear());
        currentYearRange = finYear.getFinYearRange();
        computePreviousYearRange();
        computeLastButOneYearRange();
        computeNextYearRange();
    }

    private void populateYearRange(final Budget budget) {
        if (budget != null) {
            if (budget.getFinancialYear() != null)
                currentYearRange = budget.getFinancialYear().getFinYearRange();
            else
                currentYearRange = financialYearService.findOne(budgetDetailHelper.getFinancialYear())
                        .getFinYearRange();
            computePreviousYearRange();
            computeLastButOneYearRange();
            computeNextYearRange();
        }
    }

    public String computeYearRange(final String range) {
        if (StringUtils.isNotBlank(range)) {
            final String[] list = range.split("-");
            return subtract(list[0]) + "-" + subtract(list[1]);
        }
        return "";
    }

    public String addYearRange(final String range) {
        if (StringUtils.isNotBlank(range)) {
            final String[] list = range.split("-");
            return add(list[0]) + "-" + add(list[1]);
        }
        return "";
    }

    private void computeLastButOneYearRange() {
        if (StringUtils.isNotBlank(previousYearRange)) {
            final String[] list = previousYearRange.split("-");
            lastButOneYearRange = subtract(list[0]) + "-" + subtract(list[1]);
        }
    }

    protected void computePreviousYearRange() {
        if (StringUtils.isNotBlank(currentYearRange)) {
            final String[] list = currentYearRange.split("-");
            previousYearRange = subtract(list[0]) + "-" + subtract(list[1]);
        }
    }

    protected void computeNextYearRange() {
        if (StringUtils.isNotBlank(currentYearRange)) {
            final String[] list = currentYearRange.split("-");
            nextYearRange = add(list[0]) + "-" + add(list[1]);
        }
    }

    protected String subtract(final String value) {
        final int val = Integer.parseInt(value) - 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    protected String add(final String value) {
        final int val = Integer.parseInt(value) + 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    public void populateWorkflowBean() {
        workflowBean.setApproverPositionId(approverPositionId);
        workflowBean.setApproverComments(approverComments);
        workflowBean.setWorkFlowAction(workFlowAction);
        workflowBean.setCurrentState(currentState);
    }

    public String getCurrentYearRange() {
        return currentYearRange;
    }

    public String getPreviousYearRange() {
        return previousYearRange;
    }

    public String getLastButOneYearRange() {
        return lastButOneYearRange;
    }

    protected void setBudgetDropDown() {
        if (addNewDetails) {
            if (getFinancialYear() != null && getFinancialYear().getId() != null) {
                budgetList.addAll(persistenceService
                        .findAllBy("from Budget where id not in (select parent from Budget where parent is not null) "
                                + "and isactivebudget = true and state.type='Budget' and isbere='RE' and financialYear.id = "
                                + getFinancialYear().getId() + " order by name"));
                dropdownData.put(BUDGETLIST, budgetList);
            } else
                dropdownData.put(BUDGETLIST, Collections.emptyList());
        } else
            dropdownData.put(BUDGETLIST, persistenceService.findAllBy(
                    "from Budget where id not in (select parent from Budget where parent is not null) and isactivebudget = true and state.type='Budget' and (state.value='NEW' or lower(state.value) like lower('Forwarded by SMADMIN%')) order by name"));
    }

    public List<BudgetDetail> getSavedbudgetDetailList() {
        return savedbudgetDetailList;
    }

    public List<BudgetDetail> getBudgetDetailList() {
        return budgetDetailList;
    }

    public void setBudgetDetailList(final List<BudgetDetail> budgetDetailList) {
        this.budgetDetailList = budgetDetailList;
    }

    public List<BudgetAmountView> getBudgetAmountView() {
        return budgetAmountView;
    }

    protected User getUser() {
        return (User) persistenceService.find("from User where id=?", ApplicationThreadLocals.getUserId());
    }

    protected Position getPosition() {
        return eisCommonService.getPositionByUserId(ApplicationThreadLocals.getUserId());
    }

    protected Position getPositionByUserId(final Integer userId) {
        return eisCommonService.getPositionByUserId(userId.longValue());
    }

    public List<String> getHeaderFields() {
        return headerFields;
    }

    public void setBudgetDetailWorkflowService(final WorkflowService<BudgetDetail> budgetDetailWorkflowService) {
        this.budgetDetailWorkflowService = budgetDetailWorkflowService;
    }

    @Override
    public StateAware getModel() {
        return budgetDetail;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetails) {
        budgetDetail = budgetDetails;
    }

    public void setBudgetDetailService(final BudgetDetailService budgetDetailsService) {
        budgetDetailService = budgetDetailsService;
    }

    public String loadActuals() {
        validateAsOnDate();
        getDetailsFilterdBy();

        re = budgetService.hasReForYear(budgetDetail.getBudget().getFinancialYear().getId());
        budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
        budgetAmountView.addAll(
                populateAmountData(budgetDetailList, getAsOnDate(), budgetDetail.getBudget().getFinancialYear()));
        loadBeAmounts(budgetDetailList, beAmounts);
        return NEW;
    }

    protected void getDetailsFilterdBy() {
        final StringBuilder mainQry = new StringBuilder(100);

        mainQry.append("from BudgetDetail where budget.id=? and status.code = 'NEW' order by function.name,budgetGroup.name ");
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != 0)
            savedbudgetDetailList = budgetDetailService.findAllBy(mainQry.toString(), budgetDetail.getBudget().getId());

    }

    /**
     * @param savedbudgetDetailList2
     */
    protected void loadBeAmounts(final List<BudgetDetail> savedbudgetDetailList2, final List<BigDecimal> amounts) {
        beAmounts = new ArrayList<>(savedbudgetDetailList2.size());
        if (savedbudgetDetailList.isEmpty()) {
            beAmounts = amounts;
            return;
        }
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
        if (referenceBudgetFor != null) {
            final List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",
                    referenceBudgetFor.getId());

            if (!savedbudgetDetailList.isEmpty())
                amounts.subList(0, savedbudgetDetailList.size()).clear();

            for (final BudgetDetail bd : savedbudgetDetailList)
                for (final BudgetDetail row : result)
                    if (compareDetails(row, bd))
                        beAmounts.add(row.getOriginalAmount());
        }
        beAmounts.addAll(amounts);
    }

    protected boolean compareDetails(final BudgetDetail nextYear, final BudgetDetail current) {
        if (nextYear.getExecutingDepartment() != null && current.getExecutingDepartment() != null
                && current.getExecutingDepartment().getId() != nextYear.getExecutingDepartment().getId())
            return false;
        if (nextYear.getFunction() != null && current.getFunction() != null
                && current.getFunction().getId() != nextYear.getFunction().getId())
            return false;
        if (nextYear.getFund() != null && current.getFund() != null
                && current.getFund().getId() != nextYear.getFund().getId())
            return false;
        if (nextYear.getFunctionary() != null && current.getFunctionary() != null
                && current.getFunctionary().getId() != nextYear.getFunctionary().getId())
            return false;
        if (nextYear.getScheme() != null && current.getScheme() != null
                && current.getScheme().getId() != nextYear.getScheme().getId())
            return false;
        if (nextYear.getSubScheme() != null && current.getSubScheme() != null
                && current.getSubScheme().getId() != nextYear.getSubScheme().getId())
            return false;
        if (nextYear.getBoundary() != null && current.getBoundary() != null
                && current.getBoundary().getId() != nextYear.getBoundary().getId())
            return false;
        if (nextYear.getBudgetGroup() != null && current.getBudgetGroup() != null
                && current.getBudgetGroup().getId() != nextYear.getBudgetGroup().getId())
            return false;
        if (nextYear.getBudget() != null && current.getBudget() != null
                && current.getBudget().getId() == nextYear.getBudget().getId())
            return false;
        return true;
    }

    protected boolean compareREandBEDetails(final BudgetDetail nextYear, final BudgetDetail current) {
        if (nextYear.getExecutingDepartment() != null && current.getExecutingDepartment() != null && current
                .getExecutingDepartment().getId().intValue() != nextYear.getExecutingDepartment().getId().intValue())
            return false;
        if (nextYear.getFunction() != null && current.getFunction() != null
                && current.getFunction().getId().intValue() != nextYear.getFunction().getId().intValue())
            return false;
        if (nextYear.getFund() != null && current.getFund() != null
                && current.getFund().getId().intValue() != nextYear.getFund().getId().intValue())
            return false;
        if (nextYear.getFunctionary() != null && current.getFunctionary() != null
                && current.getFunctionary().getId().intValue() != nextYear.getFunctionary().getId().intValue())
            return false;
        if (nextYear.getScheme() != null && current.getScheme() != null
                && current.getScheme().getId().intValue() != nextYear.getScheme().getId().intValue())
            return false;
        if (nextYear.getSubScheme() != null && current.getSubScheme() != null
                && current.getSubScheme().getId().intValue() != nextYear.getSubScheme().getId().intValue())
            return false;
        if (nextYear.getBoundary() != null && current.getBoundary() != null
                && current.getBoundary().getId().intValue() != nextYear.getBoundary().getId().intValue())
            return false;
        if (nextYear.getBudgetGroup() != null && current.getBudgetGroup() != null
                && current.getBudgetGroup().getId().intValue() != nextYear.getBudgetGroup().getId().intValue())
            return false;
        return true;
    }

    protected void populateBudgetList() {
        loadBudgets(RE);
        dropdownData.put(BUDGETLIST, budgetList);
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != null)
            referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
    }

    private void validateAsOnDate() {
        if (budgetDetail.getBudget() == null)
            throw new ValidationException(Arrays.asList(new ValidationError(BUDGETMANDATORY, BUDGETMANDATORY)));
        financialYearService.findOne(budgetDetail.getBudget().getFinancialYear().getId()).getStartingDate();
        financialYearService.findOne(budgetDetail.getBudget().getFinancialYear().getId()).getEndingDate();
    }

    public Date getPreviousYearFor(final Date date) {
        final GregorianCalendar previousYearToDate = new GregorianCalendar();
        previousYearToDate.setTime(date);
        final int prevYear = previousYearToDate.get(Calendar.YEAR) - 1;
        previousYearToDate.set(Calendar.YEAR, prevYear);
        return previousYearToDate.getTime();
    }

    public List<String> getGridFields() {
        return gridFields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public boolean isHeaderDisabled() {
        return headerDisabled;
    }

    public final boolean shouldShowHeaderField(final String field) {
        return headerFields.isEmpty() || headerFields.contains(field);
    }

    public final boolean shouldShowField(final String field) {
        if (headerFields.isEmpty() && gridFields.isEmpty())
            return true;
        return shouldShowHeaderField(field) || shouldShowGridField(field);
    }

    public final boolean shouldShowGridField(final String field) {
        return gridFields.isEmpty() || gridFields.contains(field);
    }

    public String ajaxLoadSubSchemes() {
        subSchemes = getPersistenceService()
                .findAllBy("from SubScheme where scheme.id=? and isActive=true order by name", schemeId);
        return Constants.SUBSCHEMES;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    protected ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    public List<BudgetAmountView> populateAmountData(final List<BudgetDetail> budgetDetails, final Date asOnDate,
            final CFinancialYear finYear) {
        final List<BudgetAmountView> list = new ArrayList<BudgetAmountView>();
        Map<String, Object> paramMap;
        final Long finYearId = finYear.getId();
        final List<AppConfigValues> appList = appConfigValueService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, FinancialConstants.APPCONFIG_COA_MAJORCODE_LENGTH);
        if (appList.isEmpty())
            throw new ValidationException(StringUtils.EMPTY, "coa.majorcode.not.defined");
        final int majorcodelength = Integer.valueOf(appList.get(0).getValue());
        final List<AppConfigValues> appListExcludeStatus = appConfigValueService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, FinancialConstants.APPCONFIG_EXCLUDE_STATUS);
        if (appListExcludeStatus.isEmpty())
            throw new ValidationException(StringUtils.EMPTY, "exclude.status.not.defined");

        final CFinancialYear finyear = financialYearService.getFinancialYearByDate(asOnDate);

        final Date fromdate = finyear.getStartingDate();
        final String voucherstatusExclude = appListExcludeStatus.get(0).getValue();
        for (final BudgetDetail detail : budgetDetails) {
            paramMap = budgetDetailHelper.constructParamMap(getValueStack(), detail);
            paramMap.put("MAJORCODELENGTH", majorcodelength);
            paramMap.put("VOUCHERSTATUSEXCLUDE", voucherstatusExclude);
            paramMap.put("FYFROMDATE", fromdate);
            final BudgetAmountView view = new BudgetAmountView();
            budgetDetailHelper.populateData(view, paramMap, asOnDate, re);
            final BudgetDetail detailWithoutBudget = new BudgetDetail();
            detailWithoutBudget.copyFrom(detail);
            detailWithoutBudget.setBudget(null);
            final List<BudgetDetail> bd = budgetDetailService.searchByCriteriaWithTypeAndFY(finYearId, "BE",
                    detailWithoutBudget);
            if (!bd.isEmpty()) {
                final BigDecimal approvedAmount = bd.get(0).getApprovedAmount();
                view.setCurrentYearBeApproved(
                        approvedAmount == null ? BigDecimal.ZERO.toString() : approvedAmount.toString());
                view.setReappropriation(bd.get(0).getApprovedReAppropriationsTotal().toString());
            }
            view.setTotal(new BigDecimal(view.getCurrentYearBeApproved()).add(new BigDecimal(view.getReappropriation()))
                    .toString());
            list.add(view);
        }
        return list;
    }

    public void setShowMessage(final boolean showMessage) {
        this.showMessage = showMessage;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setBeAmounts(final List<BigDecimal> beAmounts) {
        this.beAmounts = beAmounts;
    }

    public List<BigDecimal> getBeAmounts() {
        return beAmounts;
    }

    public void setNextYearRange(final String nextYearRange) {
        this.nextYearRange = nextYearRange;
    }

    public String getNextYearRange() {
        return nextYearRange;
    }

    public void setReferenceBudget(final Budget referenceBudget) {
        this.referenceBudget = referenceBudget;
    }

    public Budget getReferenceBudget() {
        return referenceBudget;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setBudgetList(final List<Budget> budgetList) {
        this.budgetList = budgetList;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public void setBudgetWorkflowService(final WorkflowService<Budget> budgetWorkflowService) {
        this.budgetWorkflowService = budgetWorkflowService;
    }

    public void removeEmptyBudgetDetails(final List<BudgetDetail> budgetDetailList) {
        int i = 0;
        for (final Iterator<BudgetDetail> detail = budgetDetailList.iterator(); detail.hasNext();) {
            if (detail.next() == null)
                detail.remove();
            if (beAmounts.get(i) == null)
                throw new ValidationException(Arrays.asList(new ValidationError(BUDGETRE, BUDGETRE)));
            if (budgetDetailList.get(i).getOriginalAmount() == null)
                throw new ValidationException(Arrays.asList(new ValidationError(BUDGETBE, BUDGETBE)));
            i++;
        }
    }

    /**
     * @return the showDetails
     */
    public boolean isShowDetails() {
        return showDetails;
    }

    /**
     * @param showDetails the showDetails to set
     */
    public void setShowDetails(final boolean showDetails) {
        this.showDetails = showDetails;
    }

    public Long getSearchfunctionid() {
        return searchfunctionid;
    }

    public void setSearchfunctionid(final Long searchfunctionid) {
        this.searchfunctionid = searchfunctionid;
    }

    public Long getSearchbudgetGroupid() {
        return searchbudgetGroupid;
    }

    public void setSearchbudgetGroupid(final Long searchbudgetGroupid) {
        this.searchbudgetGroupid = searchbudgetGroupid;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public boolean isAddNewDetails() {
        return addNewDetails;
    }

    public void setAddNewDetails(final boolean addDetails) {
        addNewDetails = addDetails;
    }

    public void setBudgetDocumentNumber(final Long documentNumber) {
        budgetDocumentNumber = documentNumber;
    }

    public Long getBudgetDocumentNumber() {
        return budgetDocumentNumber;
    }

    public boolean isRe() {
        return re;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetHelper) {
        budgetDetailHelper = budgetHelper;
    }

    public Date getAsOnDate() {
        return asOnDate == null ? new Date() : asOnDate;
    }

    public void setAsOnDate(final Date date) {
        asOnDate = date;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final Integer scheme) {
        schemeId = scheme;
    }

    public List<Scheme> getSubSchemes() {
        return subSchemes;
    }

}