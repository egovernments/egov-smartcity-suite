/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.egf.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.egf.model.BudgetAmountView;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.pims.commons.Position;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

@ParentPackage("egov")
@Results({
        @Result(name = Constants.DETAILLIST, location = "budgetSearch-" + Constants.DETAILLIST + ".jsp"),
        @Result(name = Constants.BUDGETS, location = "budgetSearch-" + Constants.BUDGETS + ".jsp"),
        @Result(name = Constants.LIST, location = "budgetSearch-" + Constants.LIST + ".jsp")
})
public class BudgetSearchAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BudgetSearchAction.class);
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> gridFields = new ArrayList<String>();
    protected BudgetDetail budgetDetail = new BudgetDetail();
    private final List<Budget> budgetList = new ArrayList<Budget>();
    private final List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
    protected List<BudgetDetail> savedbudgetDetailList = new ArrayList<BudgetDetail>();
    protected List<BudgetAmountView> budgetAmountView = new ArrayList<BudgetAmountView>();
    protected SimpleWorkflowService<BudgetDetail> budgetDetailWorkflowService;
    protected Long financialYear;
    protected List<Budget> budgets;
    protected boolean isApproveAction = false;
    protected String mode;
    protected boolean showButton = true;
    protected EisCommonService eisCommonService;
    protected boolean disableBudget = false;
    BudgetDetailHelper budgetDetailHelper;
    boolean errorMessage = true;
    boolean re;
    protected Budget topBudget = null;
    String message = "";
    protected List<String> mandatoryFields = new ArrayList<String>();
    private Map<Long, String> previuosYearBudgetDetailMap = new TreeMap<Long, String>();
    private Map<Long, String> beforePreviousYearBudgetDetailMap = new TreeMap<Long, String>();
    private Map<String, String> budgetDetailIdsAndAmount = new HashMap<String, String>();
    private Map<String, String> previousYearBudgetDetailIdsAndAmount = new HashMap<String, String>();
    private Map<String, String> twopreviousYearBudgetDetailIdsAndAmount = new HashMap<String, String>();
    protected FinancialYearHibernateDAO financialYearDAO;
    protected String currentfinYearRange = "";
    protected String nextfinYearRange = "";
    private String previousfinYearRange = "";
    private String twopreviousfinYearRange = "";
    private boolean shouldShowREAppropriations = true;
    List<AppConfigValues> excludeList = new ArrayList<AppConfigValues>();

    @Autowired
    @Qualifier("persistenceService")
    protected PersistenceService persistenceService;
    @Autowired
    protected BudgetDetailConfig budgetDetailConfig;
    @Autowired
    @Qualifier("budgetDetailService")
    protected BudgetDetailService budgetDetailService;
    @Autowired
    @Qualifier("budgetService")
    protected BudgetService budgetService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    @Qualifier("masterDataCache")
    private EgovMasterDataCaching masterDataCache;

    public String getMessage() {
        return message;
    }

    public boolean isRe() {
        return re;
    }

    public List<AppConfigValues> getExcludeStatusForBudget() {
        excludeList = appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF, "exclude_status_forbudget_actual");
        return excludeList;
    }

    public boolean isErrorMessage() {
        return errorMessage;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetHelper) {
        budgetDetailHelper = budgetHelper;
    }

    public BudgetDetailService getBudgetDetailService() {
        return budgetDetailService;
    }

    public boolean isDisableBudget() {
        return disableBudget;
    }

    public void setDisableBudget(final boolean disableBudget) {
        this.disableBudget = disableBudget;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public Long getFinancialYear() {
        return financialYear == null ? budgetDetailHelper.getFinancialYear() : financialYear;
    }

    public void setFinancialYear(final Long financialYearRange) {
        financialYear = financialYearRange;
    }

    public List<BudgetAmountView> getBudgetAmountView() {
        return budgetAmountView;
    }

    protected String getMessage(final String key) {
        return getText(key);
    }

    public List<BudgetDetail> getSavedbudgetDetailList() {
        return savedbudgetDetailList;
    }

    public void setBudgetDetailService(final BudgetDetailService budgetDetailService) {
        this.budgetDetailService = budgetDetailService;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    @Override
    public String execute() throws Exception {
        if (parameters.containsKey(Constants.MODE))
            setMode(parameters.get(Constants.MODE)[0]);
        errorMessage = false;
        return Constants.LIST;
    }

    public boolean showbuttons() {
        return showButton;
    }

    public BudgetSearchAction() {
    }

    @Override
    public void prepare() {
        super.prepare();
        headerFields = budgetDetailConfig.getHeaderFields();
        gridFields = budgetDetailConfig.getGridFields();
        mandatoryFields = budgetDetailConfig.getMandatoryFields();
        addRelatedEntity("budget", Budget.class);
        addRelatedEntity("budgetGroup", BudgetGroup.class);
        if (shouldShowField(Constants.FUNCTIONARY))
            addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
        if (shouldShowField(Constants.FUNCTION))
            addRelatedEntity(Constants.FUNCTION, CFunction.class);
        if (shouldShowField(Constants.FUND))
            addRelatedEntity(Constants.FUND, Fund.class);
        if (shouldShowField(Constants.SCHEME))
            addRelatedEntity(Constants.SCHEME, Scheme.class);
        if (shouldShowField(Constants.SUB_SCHEME))
            addRelatedEntity(Constants.SUB_SCHEME, SubScheme.class);
        if (shouldShowField(Constants.EXECUTING_DEPARTMENT))
            addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
        if (shouldShowField(Constants.BOUNDARY))
            addRelatedEntity(Constants.BOUNDARY, Boundary.class);
        if (!parameters.containsKey("skipPrepare")) {
            headerFields = budgetDetailConfig.getHeaderFields();
            gridFields = budgetDetailConfig.getGridFields();
            // setupDropdownDataExcluding(Constants.SUB_SCHEME);
            dropdownData.put("budgetGroupList", masterDataCache.get("egf-budgetGroup"));
            dropdownData.put("budgetList", budgetDetailService.findApprovedBudgetsForFY(getFinancialYear()));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("done findApprovedBudgetsForFY");
            dropdownData.put("financialYearList",
                    persistenceService.findAllBy("from CFinancialYear where isActive=true order by finYearRange desc"));
            if (shouldShowField(Constants.SUB_SCHEME))
                dropdownData.put("subSchemeList", Collections.EMPTY_LIST);
            if (shouldShowField(Constants.FUNCTIONARY))
                dropdownData.put("functionaryList", masterDataCache.get("egi-functionary"));
            if (shouldShowField(Constants.FUNCTION))
                dropdownData.put("functionList", masterDataCache.get("egi-function"));
            if (shouldShowField(Constants.SCHEME))
                dropdownData.put("schemeList", persistenceService.findAllBy("from Scheme where isActive=true order by name"));
            if (shouldShowField(Constants.EXECUTING_DEPARTMENT))
                dropdownData.put("executingDepartmentList", masterDataCache.get("egi-department"));
            if (shouldShowField(Constants.BOUNDARY))
                dropdownData.put("boundaryList", persistenceService.findAllBy("from Boundary order by name"));
            if (shouldShowField(Constants.FUND))
                dropdownData.put("fundList",
                        persistenceService.findAllBy("from Fund where isActive=true order by name"));
        }
    }

    @Override
    public Object getModel() {
        return budgetDetail;
    }

    // for modify screen
    public String list() {
        if (parameters.containsKey(Constants.MODE) && "approve".equals(parameters.get(Constants.MODE)[0])) {
            setMode(parameters.get(Constants.MODE)[0]);
            isApproveAction = true;
            disableBudget = true;
        }
        if (budgetDetail.getBudget() != null) {
            persistenceService.getSession().refresh(budgetDetail.getBudget());

            if (budgetDetail.getBudget().getFinancialYear() == null)
                budgetDetail.setBudget(budgetService.find("from Budget where id=?", budgetDetail.getBudget().getId()));
            financialYear = budgetDetail.getBudget().getFinancialYear().getId();
            if (isApproveAction == true)
                budgetList.add(budgetService.find(
                        "select budget from Budget budget  join budget.state as state where budget.id=? and state.owner=? ",
                        budgetDetail.getBudget().getId(), getPosition()));
            else
                budgetList.add(budgetService.find(
                        "select budget from Budget budget  join budget.state as state where budget.id=? and state.value=? ",
                        budgetDetail.getBudget().getId(), "NEW"));

        } else {
            final List<BudgetDetail> budgetDetails = budgetDetailService.searchByCriteriaAndFY(financialYear, budgetDetail,
                    isApproveAction, getPosition());
            for (final BudgetDetail budgetDetail : budgetDetails)
                if (!budgetList.contains(budgetDetail.getBudget()))
                    budgetList.add(budgetDetail.getBudget());
        }
        // budgetList=removeReferenceBudgets(budgetList);
        getSession().put(Constants.SEARCH_CRITERIA_KEY, budgetDetail);
        getSession().put("financialyearid", financialYear);
        if (budgetList.isEmpty())
            message = getText("no.data.found");
        return Constants.LIST;
    }

    // serach screen
    @Action(value = "/budget/budgetSearch-groupedBudgets")
    public String groupedBudgets() {
        final Budget budget = budgetDetail.getBudget();
        // Dont restrict search by the selected budget, but by all budgets in the tree of selected budget
        budgetDetail.setBudget(null);
        if (budget != null && budget.getId() != null && budget.getId() != 0)
            budgetList.addAll(budgetDetailService.findBudgetTree(budget, budgetDetail));
        else if (budget != null && budget.getFinancialYear() != null && budget.getFinancialYear().getId() != null)
            budgetList.addAll(budgetDetailService.findBudgetTree(
                    budgetDetailService.findApprovedPrimaryParentBudgetForFY(budget.getFinancialYear().getId()), budgetDetail));
        getSession().put(Constants.SEARCH_CRITERIA_KEY, budgetDetail);
        if (budgetList.isEmpty())
            addActionError(getText("budget.no.details.found"));
        return Constants.LIST;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public final boolean shouldShowHeaderField(final String field) {
        return headerFields.isEmpty() || headerFields.contains(field);
    }

    public final boolean shouldShowGridField(final String field) {
        return gridFields.isEmpty() || gridFields.contains(field);
    }

    public boolean showApprovalDetails() {
        boolean result = false;
        final String mode = getMode();
        if (mode != null && mode.equals("approve")) {
            isApproveAction = true;
            result = isApproveAction;
        }
        return result;
    }

    // for modify screen
    public String budgetDetailList() {
        if (parameters.get("budget.id") != null) {
            final Budget Budget = budgetService.findById(Long.valueOf(parameters.get("budget.id")[0]), false);
            setTopBudget(Budget);
        }
        final BudgetDetail criteria = (BudgetDetail) persistenceService.getSession().createCriteria(
                Constants.SEARCH_CRITERIA_KEY);
        criteria.setBudget(budgetDetail.getBudget());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "Before getting List------------------------------------------------------------------------------------");
        savedbudgetDetailList = budgetDetailService.searchBy(criteria);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("After getting List----------------------------------------------------------------"
                    + savedbudgetDetailList.size());
        re = checkRe(budgetDetail.getBudget());

        computeAmounts(savedbudgetDetailList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "After compute-------------------------------------------------------------------------------------------");
        return Constants.DETAILLIST;
    }

    public BigDecimal divideAndRoundStrToBigDec(final String amountStr) {
        BigDecimal value = new BigDecimal(amountStr);
        value = value.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    public String divideAndRoundBigDecToString(final BigDecimal amount) {
        BigDecimal value = amount;
        value = value.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
        return value.toString();
    }

    protected boolean checkRe(final Budget budget) {
        if (budget != null)
            if ("RE".equalsIgnoreCase(budget.getIsbere()))
                return true;
        return false;
    }

    // for search screen
    @Action(value = "/budget/budgetSearch-groupedBudgetDetailList")
    public String groupedBudgetDetailList() {

        final BudgetDetail criteria = new BudgetDetail();
        /*
         * final BudgetDetail criteria = (BudgetDetail) persistenceService.getSession().createCriteria(
         * Constants.SEARCH_CRITERIA_KEY);
         */
        Budget budget = budgetDetail.getBudget();
        if (budget != null && budget.getId() != null) {
            budget = (Budget) persistenceService.find("from Budget where id=?", budget.getId());
            currentfinYearRange = budget.getFinancialYear().getFinYearRange();
            computePreviousYearRange();
            computeTwopreviousYearRange();
        }
        criteria.setBudget(null);
        savedbudgetDetailList = budgetDetailService.findAllBudgetDetailsWithReAppropriation(budget, criteria);
        re = checkRe(budget);
        computeAmounts(savedbudgetDetailList);
        populateActualData(budget.getFinancialYear());
        return Constants.DETAILLIST;
    }

    protected void computePreviousYearRange() {
        if (StringUtils.isNotBlank(currentfinYearRange)) {
            final String[] list = currentfinYearRange.split("-");
            previousfinYearRange = subtract(list[0]) + "-" + subtract(list[1]);
        }
    }

    protected void computeTwopreviousYearRange() {
        computePreviousYearRange();
        if (StringUtils.isNotBlank(previousfinYearRange)) {
            final String[] list = previousfinYearRange.split("-");
            twopreviousfinYearRange = subtract(list[0]) + "-" + subtract(list[1]);
        }
    }

    protected void computeNextYearRange() {
        if (StringUtils.isNotBlank(currentfinYearRange)) {
            final String[] list = currentfinYearRange.split("-");
            nextfinYearRange = addition(list[0]) + "-" + addition(list[1]);
        }
    }

    protected String subtract(final String value) {
        final int val = Integer.parseInt(value) - 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    protected String addition(final String value) {
        final int val = Integer.parseInt(value) + 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    protected ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    public void computeAmounts(final List<BudgetDetail> budgetDetails) {
        budgetAmountView = new ArrayList<BudgetAmountView>();
        for (final BudgetDetail detail : budgetDetails) {
            final BudgetAmountView view = new BudgetAmountView();
            budgetAmountView.add(view);
            if (detail.getState() != null)
                detail.setComment(detail.getState().getExtraInfo());
            final BigDecimal approvedAmt = detail.getApprovedAmount() == null ? BigDecimal.ZERO
                    : divideAndRoundStrToBigDec(detail
                            .getApprovedAmount().toString());
            if (re) {
                if (getConsiderReAppropriationAsSeperate())
                    view.setCurrentYearReApproved(divideAndRoundBigDecToString(approvedAmt));
                else {
                    view.setCurrentYearReApproved(divideAndRoundBigDecToString(calculateTotal(detail)));
                    shouldShowREAppropriations = false;
                }
            } else
                view.setCurrentYearBeApproved(divideAndRoundBigDecToString(approvedAmt));
            detail.setAnticipatoryAmount(detail.getAnticipatoryAmount() == null ? BigDecimal.ZERO
                    : divideAndRoundStrToBigDec(detail.getAnticipatoryAmount().toString()));
            detail.setOriginalAmount(divideAndRoundStrToBigDec(detail.getOriginalAmount().toString()));
        }
    }

    public BigDecimal calculateTotal(final BudgetDetail detail) {
        final BigDecimal approvedAmount = detail.getApprovedAmount() == null ? BigDecimal.ZERO : detail.getApprovedAmount();
        final BigDecimal approvedReAppropriationsTotal = detail.getApprovedReAppropriationsTotal() == null ? BigDecimal.ZERO
                : detail
                        .getApprovedReAppropriationsTotal();
        return approvedAmount.add(approvedReAppropriationsTotal);
    }

    private void populateActualData(final CFinancialYear financialYear) {
        String fromDate = Constants.DDMMYYYYFORMAT2.format(financialYear.getStartingDate());
        String toVoucherDate = Constants.DDMMYYYYFORMAT2.format(new Date());
        final List<Object[]> result = budgetDetailService.fetchActualsForFYDate(fromDate,
                toVoucherDate, mandatoryFields);
        for (final Object[] row : result)
            budgetDetailIdsAndAmount.put(row[0].toString(), row[1].toString());
        fromDate = Constants.DDMMYYYYFORMAT2.format(subtractYear(financialYear.getStartingDate()));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, -1);
        String toVoucherDate1 = Constants.DDMMYYYYFORMAT2.format(cal.getTime());

        final List<Object[]> previousYearResult = budgetDetailService.fetchActualsForFYDate(fromDate, toVoucherDate1,
                mandatoryFields);
        for (final Object[] row : previousYearResult)
            previousYearBudgetDetailIdsAndAmount.put(row[0].toString(), row[1].toString());
    }

    public Date subtractYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    @Action(value = "/budget/budgetSearch-ajaxLoadBudget")
    public String ajaxLoadBudget() {
        budgets = budgetDetailService.findApprovedBudgetsForFY(getFinancialYear());
        return Constants.BUDGETS;
    }

    public Position getPosition() throws ApplicationRuntimeException {
        Position pos;
        try {
            // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
            pos = eisCommonService.getPrimaryAssignmentPositionForEmp(ApplicationThreadLocals.getUserId());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Unable to get Position for the user");
        }
        return pos;
    }

    protected User getUser() {
        final User user = (User) persistenceService.find("from User where id_user=?", ApplicationThreadLocals.getUserId());
        return user;
    }

    public void setBudgetDetailWorkflowService(final SimpleWorkflowService<BudgetDetail> workflowService) {
        budgetDetailWorkflowService = workflowService;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    /**
     * @return the topBudget
     */
    public Budget getTopBudget() {
        return topBudget;
    }

    /**
     * @param topBudget the topBudget to set
     */
    public void setTopBudget(final Budget topBudget) {
        this.topBudget = topBudget;
    }

    public final boolean shouldShowField(final String field) {
        if (headerFields.isEmpty() && gridFields.isEmpty())
            return true;
        return shouldShowHeaderField(field) || shouldShowGridField(field);
    }

    public void setBudgetDetailIdsAndAmount(final Map<String, String> budgetDetailIdsAndAmount) {
        this.budgetDetailIdsAndAmount = budgetDetailIdsAndAmount;
    }

    public Map<String, String> getBudgetDetailIdsAndAmount() {
        return budgetDetailIdsAndAmount;
    }

    public void setPreviousYearBudgetDetailIdsAndAmount(
            final Map<String, String> previousYearBudgetDetailIdsAndAmount) {
        this.previousYearBudgetDetailIdsAndAmount = previousYearBudgetDetailIdsAndAmount;
    }

    public Map<String, String> getPreviousYearBudgetDetailIdsAndAmount() {
        return previousYearBudgetDetailIdsAndAmount;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public FinancialYearHibernateDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setCurrentfinYearRange(final String currentfinYearRange) {
        this.currentfinYearRange = currentfinYearRange;
    }

    public String getCurrentfinYearRange() {
        return currentfinYearRange;
    }

    public void setPreviousfinYearRange(final String previousfinYearRange) {
        this.previousfinYearRange = previousfinYearRange;
    }

    public String getPreviousfinYearRange() {
        return previousfinYearRange;
    }

    private boolean getConsiderReAppropriationAsSeperate() {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "CONSIDER_RE_REAPPROPRIATION_AS_SEPARATE");
        String appValue = "-1";
        appValue = appList.get(0).getValue();
        return "Y".equalsIgnoreCase(appValue);
    }

    public boolean getShouldShowREAppropriations() {
        return shouldShowREAppropriations;
    }

    public Map<String, String> getTwopreviousYearBudgetDetailIdsAndAmount() {
        return twopreviousYearBudgetDetailIdsAndAmount;
    }

    public void setTwopreviousYearBudgetDetailIdsAndAmount(
            final Map<String, String> twopreviousYearBudgetDetailIdsAndAmount) {
        this.twopreviousYearBudgetDetailIdsAndAmount = twopreviousYearBudgetDetailIdsAndAmount;
    }

    public String getTwopreviousfinYearRange() {
        return twopreviousfinYearRange;
    }

    public void setTwopreviousfinYearRange(final String twopreviousfinYearRange) {
        this.twopreviousfinYearRange = twopreviousfinYearRange;
    }

    public Map<Long, String> getPreviuosYearBudgetDetailMap() {
        return previuosYearBudgetDetailMap;
    }

    public void setPreviuosYearBudgetDetailMap(
            final Map<Long, String> previuosYearBudgetDetailMap) {
        this.previuosYearBudgetDetailMap = previuosYearBudgetDetailMap;
    }

    public Map<Long, String> getBeforePreviousYearBudgetDetailMap() {
        return beforePreviousYearBudgetDetailMap;
    }

    public void setBeforePreviousYearBudgetDetailMap(
            final Map<Long, String> beforePreviousYearBudgetDetailMap) {
        this.beforePreviousYearBudgetDetailMap = beforePreviousYearBudgetDetailMap;
    }

    public String getNextfinYearRange() {
        return nextfinYearRange;
    }

    public void setNextfinYearRange(final String nextfinYearRange) {
        this.nextfinYearRange = nextfinYearRange;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public EgovMasterDataCaching getMasterDataCache() {
        return masterDataCache;
    }

    public void setAppConfigValuesService(
            AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public void setMasterDataCache(EgovMasterDataCaching masterDataCache) {
        this.masterDataCache = masterDataCache;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }
}