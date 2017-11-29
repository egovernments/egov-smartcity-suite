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
package org.egov.works.web.actions.estimate;

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetGroup;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.entity.FinancingSource;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Results({ @Result(name = FinancialDetailAction.PRINT, type = "stream", location = "budgetFolioPDF", params = {
        "inputName", "budgetFolioPDF", "contentType", "application/pdf", "contentDisposition", "no-cache" }),
        @Result(name = AbstractEstimateAction.NEW, location = "financialDetail-add.jsp")
})
public class FinancialDetailAction extends BaseFormAction {

    private static final long serialVersionUID = -8120661601900686441L;
    private static final String BUDGET_DETAILS_SAVE = "budget_details_save";
    private static final Logger logger = Logger.getLogger(FinancialDetailAction.class);
    private static final String BUDGET_GROUP_LIST = "budgetGroupList";
    private FinancialDetail financialDetail = new FinancialDetail();
    private AbstractEstimateService abstractEstimateService;
    @Autowired
    private UserService userService;
    private WorkflowService<AbstractEstimate> estimateWorkflowService;
    private static final String MODULE_NAME = "Works";
    private static final String KEY_NAME = "SKIP_BUDGET_CHECK";

    private AbstractEstimate abstractEstimate;
    private List<FinancingSource> financingSourceList = new LinkedList<FinancingSource>();
    private List<MultiYearEstimate> actionMultiYearEstimateValues = new LinkedList<MultiYearEstimate>();
    private List<Fundsource> fundSourceList;
    private Long estimateId;
    private Long id;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    private String status = "TECH_SANCTIONED";
    public static final String ADD = "add";
    private BudgetGroupDAO budgetGroupDAO;
    private Date financialYearStartDate;
    private Integer approverUserId;
    private Long departmentId;
    private Integer designationId;
    private String approverComments;
    private boolean skipBudget = false;
    private String isEnableSelect = "false";
    private String source = " ";
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    /*
     * added by prashanth on 2nd nov 09 for disp user and desgination in success page
     */
    String employeeName;
    String designation;
    private WorksService worksService;

    public static final String PRINT = "print";
    private List<BudgetFolioDetail> approvedBudgetFolioDetails;
    private BigDecimal totalGrant = BigDecimal.ZERO;
    private BigDecimal totalGrantPerc = BigDecimal.ZERO;
    private InputStream budgetFolioPDF;
    private ReportService reportService;
    private Double latestCumulative = 0.00D;
    private BigDecimal latestBalance = BigDecimal.ZERO;
    private BigDecimal totalDepositAmount = BigDecimal.ZERO;
    private Double latestCumulativeExpense = 0.00D;
    private static final String SEARCH_BUDGET_FOLIO = "searchBudgetFolio";
    private static final String SEARCH_DEPOSIT_WORKS_FOLIO = "searchDepositWorksFolio";
    private static final String BUDGET_GROUP_SEARCH_LIST = "budgetHeadList";
    private Map<String, Object> queryParamMap = new HashMap<String, Object>();
    private Long userDepartment;
    private Date reportDate;
    private BudgetDetailsDAO budgetDetailsDAO;
    private Map<String, String> mandatoryFields = new HashMap<String, String>();
    private String option;
    private String deptName = "";
    private static final String Fund = "fund";
    private static final String FUNCTION = "function";
    private static final String COA_LIST = "coaList";
    private static final String APP = "app";
    private static final String KEY_DEPOSIT = "WORKS_DEPOSIT_OTHER_WORKS";
    private static final String SOURCE_INBOX = "inbox";
    private Long depositCodeId;
    private String sourcepage;

    private String appValue;

    private String appValueLabel;
    private DepositWorksUsageService depositWorksUsageService;
    private PersistenceService<DepositCode, Long> depositCodeService;
    public static final String RESULTS = "searchResult";
    private Date asOnDate;
    private String code;
    private Integer fundId;
    private Long glcodeId;
    private Integer functionId;
    private String roadCutDepCodeFlag;
    public Integer finYearId;
    private String finYearRangeStr;
    private String currentFinancialYearId;
    @Autowired
    private FundSourceHibernateDAO fundSourceDAO;
    @Autowired
    private FundHibernateDAO fundDao;
    @Autowired
    private FunctionHibernateDAO functionHibDao;
    @Autowired
    private FunctionaryHibernateDAO functionaryDao;
    private static final String SCHEME_MANDATORYCHECK_BUDGETHEAD = "SCHEME_MANDATORYCHECK_BUDGETHEAD";
    private String budgetHeadGlcode;

    public FinancialDetail getFinancialDetail() {
        return financialDetail;
    }

    public void setFinancialDetail(final FinancialDetail financialDetail) {
        this.financialDetail = financialDetail;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public FinancialDetailAction() {
        addRelatedEntity(Fund, Fund.class);
        addRelatedEntity(FUNCTION, CFunction.class);
        addRelatedEntity("functionary", Functionary.class);
        addRelatedEntity("scheme", Scheme.class);
        addRelatedEntity("subScheme", SubScheme.class);
        addRelatedEntity("budgetGroup", BudgetGroup.class);
        addRelatedEntity("coa", CChartOfAccounts.class);

    }

    @Override
    public String execute() {
        return ADD;
    }

    @Action(value = "/estimate/financialDetail-add")
    public String add() {
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(abstractEstimate, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        return ADD;
    }

    public String edit() {
        setBudgetHeadGlcode(worksService.getWorksConfigValue(SCHEME_MANDATORYCHECK_BUDGETHEAD));
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(abstractEstimate, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        return ADD;
    }

    public String save() {
        setBudgetHeadGlcode(worksService.getWorksConfigValue(SCHEME_MANDATORYCHECK_BUDGETHEAD));
        populateFinancialDetail();
        persistFinancialDetail();
        if (financialDetail.getAbstractEstimate() != null
                && financialDetail.getAbstractEstimate().getMultiYearEstimates() != null
                && actionMultiYearEstimateValues.size() != 0)
            populateMultiYearEstimates();
        addActionMessage("The financial details for estimate " + abstractEstimate.getEstimateNumber()
                + " was saved successfully");
        return EDIT;
    }

    public String saveAndSubmit() {
        setBudgetHeadGlcode(worksService.getWorksConfigValue(SCHEME_MANDATORYCHECK_BUDGETHEAD));
        populateFinancialDetail();
        persistFinancialDetail();
        if (financialDetail.getAbstractEstimate() != null
                && financialDetail.getAbstractEstimate().getMultiYearEstimates() != null
                && actionMultiYearEstimateValues.size() != 0)
            populateMultiYearEstimates();
        final String actionName = parameters.get("actionName")[0];
        abstractEstimate.setApproverUserId(approverUserId);
        Long finYearId = null;
        Boolean isApprYearEntry = Boolean.FALSE;
        if (abstractEstimateService.isPreviousYearApprRequired(abstractEstimate.getFinancialDetails().get(0)))
            finYearId = Long.parseLong(finHibernateDao.getPrevYearFiscalId());
        else
            finYearId = Long.parseLong(finHibernateDao.getCurrYearFiscalId());
        if (!isSkipBudgetCheck()) {
            for (final MultiYearEstimate multiYearEstimate : financialDetail.getAbstractEstimate()
                    .getMultiYearEstimates())
                if (multiYearEstimate != null && multiYearEstimate.getFinancialYear().getId().compareTo(finYearId) == 0
                        && multiYearEstimate.getPercentage() > 0)
                    isApprYearEntry = Boolean.TRUE;
        } else
            isApprYearEntry = Boolean.TRUE;
        if (!isApprYearEntry)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "There is no entry in the year wise estimate for selected appropriation year",
                    "There is no entry in the year wise estimate for selected appropriation year")));

        try {
            abstractEstimate = estimateWorkflowService.transition(actionName, abstractEstimate, approverComments);
        } catch (final ValidationException exp) {
            final List<ValidationError> errorList = exp.getErrors();
            for (final ValidationError error : errorList) {
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    abstractEstimate = abstractEstimateService.findById(estimateId, false);
                    financialDetail.setAbstractEstimate(abstractEstimate);
                    prepare();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                }
                throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
            }
            throw exp;
        }
        addActionMessage("The financial details for estimate " + abstractEstimate.getEstimateNumber()
                + " was submitted successfully.");

        /* start for customizing workflow message display */
        if (abstractEstimate.getEgwStatus() != null
                && !"NEW".equalsIgnoreCase(abstractEstimate.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(abstractEstimate.getState().getOwnerPosition(),
                    abstractEstimate.getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setEmployeeName(empName);
                setDesignation(designation);
            }
        }
        /* end */
        return SUCCESS;
    }

    private void populateFinancialDetail() {
        financialDetail.getFinancingSources().clear();
        populateFinancingSourceDetails();
        // Clear budget group if it is deposit works else clear the COA
        if (isSkipBudget())
            financialDetail.setBudgetGroup(null);
        else
            financialDetail.setCoa(null);
    }

    private void persistFinancialDetail() {

        if (depositCodeId != null && depositCodeId != -1)
            abstractEstimate.setDepositCode(depositCodeService.findById(depositCodeId, false));

        if (getMaxFinancingSource(financingSourceList).getFundSource() != null
                && fundSourceDAO.fundsourceById(getMaxFinancingSource(financingSourceList).getFundSource().getId().intValue()) != null)
            abstractEstimate.setFundSource(fundSourceDAO.fundsourceById(getMaxFinancingSource(financingSourceList)
                    .getFundSource().getId().intValue()));

        abstractEstimate = abstractEstimateService.persistFinancialDetail(financialDetail, abstractEstimate);
        // to lazy load the financial detail id.
        abstractEstimate.getFinancialDetails().get(0).getId();

        financialDetail = abstractEstimate.getFinancialDetails().get(0);
    }

    public boolean getIsPreviousApprAllowed() {
        return "yes".equalsIgnoreCase(worksService.getWorksConfigValue("PREVIOUS_YEAR_APPROPRIATION_ALLOWED"));
    }

    @Override
    public void prepare() {
        final AjaxFinancialDetailAction ajaxFinancialDetailAction = new AjaxFinancialDetailAction();
        ajaxFinancialDetailAction.setPersistenceService(getPersistenceService());
        ajaxFinancialDetailAction.setBudgetGroupDAO(budgetGroupDAO);
        abstractEstimateService.setBudgetGroupDAO(budgetGroupDAO);
        abstractEstimate = abstractEstimateService.findById(estimateId, false);
        if (abstractEstimate != null) {
            // Incase of revision estimate, get the parent's financial details
            if (abstractEstimate.getParent() != null)
                financialDetail = abstractEstimate.getParent().getFinancialDetails().get(0);
            if (abstractEstimate.getFinancialDetails() != null && abstractEstimate.getFinancialDetails().size() > 0)
                financialDetail = abstractEstimate.getFinancialDetails().get(0);
            financialDetail.setAbstractEstimate(abstractEstimate);
        }
        super.prepare();
        if (financialDetail.getApprYear() == null)
            financialDetail.setApprYear("running");
        setupDropdownDataExcluding(Fund, FUNCTION, "functionary", "scheme", "subScheme", "budgetGroup", "coa");

        addDropdownData("fundList", fundDao.findAllActiveIsLeafFunds());
        addDropdownData("functionList", functionHibDao.getAllActiveFunctions());
        addDropdownData("functionaryList", functionaryDao.findAllActiveFunctionary());
        final List departmentList = getPersistenceService().findAllBy("from DepartmentImpl");
        addDropdownData("userDepartmentList", departmentList);
        addDropdownData("executingDepartmentList", departmentList);
        addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=true"));
        final List<CFinancialYear> finYrList = worksService.getAllFinancialYearsForWorks();
        addDropdownData("finYearList", finYrList);
        finYearRangeStr = generateFinYrList(finYrList);

        final CFinancialYear financialYear = finHibernateDao.getFinYearByDate(new Date());
        if (financialYear != null)
            currentFinancialYearId = financialYear.getId().toString();

        try {
            addDropdownData(BUDGET_GROUP_LIST, Collections.EMPTY_LIST);
            addDropdownData(BUDGET_GROUP_SEARCH_LIST, new ArrayList<BudgetGroup>());
        } catch (final Exception e) {
            logger.error("---Budgetunavailable : Unable to load budget information---" + e.getMessage());
            addFieldError("budgetunavailable", "Unable to load budget information");
        }
        populateSchemeList(ajaxFinancialDetailAction, financialDetail.getFund() != null,
                financialDetail.getAbstractEstimate() != null);
        populateSubSchemeList(ajaxFinancialDetailAction, financialDetail.getScheme() != null,
                financialDetail.getAbstractEstimate() != null);
        try {
            populateBudgetGroupList(ajaxFinancialDetailAction, financialDetail.getFunction() != null,
                    financialDetail.getAbstractEstimate() != null);
            populateBudgetHeadList(ajaxFinancialDetailAction, financialDetail.getFunction() != null,
                    getReportDate() != null);
        } catch (final ApplicationException e) {
            logger.error("---Budgetunavailable: Unable to load budget data---" + e.getMessage());
            addFieldError("budgetunavailable", "Unable to load budget data");
        } catch (final Exception e) {
            logger.error("---Budgetunavailable: Unable to load budget data from database---" + e.getMessage());
            addFieldError("budgetunavailable", "Unable to load budget data from database");
        }
        if (getDropdownData().get(BUDGET_GROUP_LIST) == null)
            addDropdownData(BUDGET_GROUP_LIST, new ArrayList<BudgetGroup>());

        fundSourceList = fundSourceDAO.findAllActiveIsLeafFundSources();
        if (abstractEstimateService.getLatestAssignmentForCurrentLoginUser() != null)
            departmentId = abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDepartment().getId();
        checkMandataryFields();
        if (isSkipBudgetCheck())
            if (StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)))
                addDropdownData(COA_LIST, chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                        .getWorksConfigValue(KEY_DEPOSIT))));
            else
                addDropdownData(COA_LIST, Collections.EMPTY_LIST);
        else
            addDropdownData(COA_LIST, Collections.EMPTY_LIST);

        if (!StringUtils.isBlank(option)
                && ("input".equalsIgnoreCase(option) || "searchDepositWorksFolioDetails".equalsIgnoreCase(option))) {
            asOnDate = new Date();
            try {
                final String config = worksService.getWorksConfigValue("SLDEPOSITCODE_SHOW_FUNDS");
                final List<String> code = new ArrayList<String>();

                if (config == null)
                    addDropdownData("fundList", code);
                else {
                    for (int i = 0; i < config.split(",").length; i++)
                        code.add(config.split(",")[i]);
                    addDropdownData("fundList",
                            getPersistenceService().findAllByNamedQuery("getListOfFundsForCodes", code));
                }

            } catch (final Exception v) {
                logger.error("---Unable to load funds for Deposit Works folio Reports---" + v.getMessage());
                addFieldError("Fund.notfound", "depositWorksFolioReport.loadFund.error");
            }
            if (StringUtils.isNotBlank(worksService.getWorksConfigValue(KEY_DEPOSIT)))
                addDropdownData(COA_LIST, chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer.valueOf(worksService
                        .getWorksConfigValue(KEY_DEPOSIT))));
            else
                addDropdownData(COA_LIST, Collections.EMPTY_LIST);
        }
        budgetHeadGlcode = worksService.getWorksConfigValue(SCHEME_MANDATORYCHECK_BUDGETHEAD);

    }

    public void checkMandataryFields() {
        final List<AppConfigValues> appConfigList = worksService.getAppConfigValue("EGF",
                "budgetaryCheck_groupby_values");
        AppConfigValues appConfigValues = null;
        if (appConfigList != null)
            appConfigValues = appConfigList.get(0);

        if (appConfigValues != null)
            if (appConfigValues.getValue().indexOf(",") == -1)
                mandatoryFields.put(appConfigValues.getValue(), "M");
            else {
                final String[] values = StringUtils.split(appConfigValues.getValue(), ",");
                for (final String value : values)
                    mandatoryFields.put(value, "M");
            }
    }

    protected void populateSubSchemeList(final AjaxFinancialDetailAction ajaxFinancialDetailAction,
            final boolean schemePopulated, final boolean datePresent) {
        if (schemePopulated && datePresent) {
            ajaxFinancialDetailAction.setSchemeId(financialDetail.getScheme().getId());
            ajaxFinancialDetailAction.setEstimateDate(financialDetail.getAbstractEstimate().getEstimateDate());
            ajaxFinancialDetailAction.loadSubSchemes();
            addDropdownData("subSchemeList", ajaxFinancialDetailAction.getSubSchemes());
        } else
            addDropdownData("subSchemeList", Collections.emptyList());
    }

    protected void populateSchemeList(final AjaxFinancialDetailAction ajaxFinancialDetailAction,
            final boolean fundPopulated, final boolean datePresent) {
        if (fundPopulated && datePresent) {
            ajaxFinancialDetailAction.setFundId(financialDetail.getFund().getId());
            ajaxFinancialDetailAction.setEstimateDate(financialDetail.getAbstractEstimate().getEstimateDate());
            ajaxFinancialDetailAction.loadSchemes();
            addDropdownData("schemeList", ajaxFinancialDetailAction.getSchemes());
        } else
            addDropdownData("schemeList", Collections.emptyList());
    }

    protected void populateBudgetGroupList(final AjaxFinancialDetailAction ajaxFinancialDetailAction,
            final boolean functionPopulated, final boolean datePresent) throws Exception {
        if (functionPopulated && datePresent) {
            ajaxFinancialDetailAction.setFunctionId(financialDetail.getFunction().getId());
            ajaxFinancialDetailAction.setEstimateDate(financialDetail.getAbstractEstimate().getEstimateDate());
            ajaxFinancialDetailAction.loadBudgetGroups();
            addDropdownData(BUDGET_GROUP_LIST, ajaxFinancialDetailAction.getBudgetGroups());
        } else if (!functionPopulated)
            try {
                addDropdownData(BUDGET_GROUP_LIST, budgetGroupDAO.getBudgetGroupList());
            } catch (final ApplicationRuntimeException e) {
                logger.error("---Unable to load budget head---" + e.getMessage());
                addFieldError("budgetheadexception", "Unable to load budget head ");
            }
    }

    protected void populateBudgetHeadList(final AjaxFinancialDetailAction ajaxFinancialDetailAction,
            final boolean functionPopulated, final boolean datePresent) throws Exception {
        if (functionPopulated && datePresent) {
            ajaxFinancialDetailAction.setFunctionId(financialDetail.getFunction().getId());
            ajaxFinancialDetailAction.setEstimateDate(getReportDate());
            ajaxFinancialDetailAction.loadBudgetGroups();
            addDropdownData(BUDGET_GROUP_SEARCH_LIST, ajaxFinancialDetailAction.getBudgetGroups());
        } else
            addDropdownData(BUDGET_GROUP_SEARCH_LIST, new ArrayList<BudgetGroup>());
    }

    protected void populateFinancingSourceDetails() {
        for (final FinancingSource finSource : financingSourceList)
            if (validFinancingSource(finSource)) {
                finSource.setFundSource((Fundsource) getPersistenceService().find("from Fundsource where id = ? ",
                        finSource.getFundSource().getId()));
                financialDetail.addFinancingSource(finSource);
            }
    }

    protected void populateMultiYearEstimates() {
        financialDetail.getAbstractEstimate().getMultiYearEstimates().clear();
        final List<ValidationError> multiYearErrors = new ArrayList<ValidationError>();
        double totalPerc = 0.0;
        Boolean isPercError = Boolean.FALSE;
        Boolean isFinYearError = Boolean.FALSE;
        for (final MultiYearEstimate multiYearEstimate : actionMultiYearEstimateValues)
            if (multiYearEstimate != null) {
                if (multiYearEstimate.getFinancialYear() != null
                        && multiYearEstimate.getFinancialYear().getId() != null
                        && multiYearEstimate.getFinancialYear().getId() != -1)
                    multiYearEstimate.setFinancialYear((CFinancialYear) getPersistenceService().find(
                            "from CFinancialYear where id = ?", multiYearEstimate.getFinancialYear().getId()));
                multiYearEstimate.setAbstractEstimate(financialDetail.getAbstractEstimate());
                totalPerc = totalPerc + multiYearEstimate.getPercentage();
                financialDetail.getAbstractEstimate().addMultiYearEstimate(multiYearEstimate);
                if (!isPercError && multiYearEstimate != null && multiYearEstimate.getPercentage() <= 0) {
                    multiYearErrors.add(new ValidationError("percentage",
                            "multiYearEstimate.percentage.percentage_greater_than_0"));
                    isPercError = Boolean.TRUE;
                }
                if (!isFinYearError
                        && (multiYearEstimate.getFinancialYear() == null || multiYearEstimate.getFinancialYear() != null
                                && (multiYearEstimate.getFinancialYear().getId() == null || multiYearEstimate
                                        .getFinancialYear().getId() == -1))) {
                    multiYearErrors.add(new ValidationError("financialYear", "multiYeareEstimate.financialYear.null"));
                    isFinYearError = Boolean.TRUE;
                }
            }

        if (totalPerc < 100)
            multiYearErrors
                    .add(new ValidationError("percentage", "multiYearEstimate.percentage.percentage_equals_100"));

        if (totalPerc > 100)
            multiYearErrors.add(new ValidationError("percentage",
                    "multiYearEstimate.percentage.percentage_greater_than_100"));

        if (multiYearErrors.size() != 0)
            throw new ValidationException(multiYearErrors);
    }

    protected boolean validFinancingSource(final FinancingSource finSource) {
        if (finSource != null && finSource.getFundSource() != null && finSource.getFundSource().getId() != null)
            return true;

        return false;
    }

    @Override
    public Object getModel() {
        return financialDetail;
    }

    protected void setModel(final FinancialDetail financialDetail) {
        this.financialDetail = financialDetail;
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List getEstimateStatuses() {
        return persistenceService.findAllBy(
                "from EgwStatus s where moduletype=? and s.code<> 'BUDGETARY_APPR_VALIDATED' " + "order by orderId",
                AbstractEstimate.class.getSimpleName());
    }

    public void setBudgetGroupDAO(final BudgetGroupDAO budgetGroupDAO) {
        this.budgetGroupDAO = budgetGroupDAO;
    }

    public BudgetGroupDAO getBudgetGroupDAO() {
        return budgetGroupDAO;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<FinancingSource> getFinancingSourceList() {
        return financingSourceList;
    }

    public void setFinancingSourceList(final List<FinancingSource> financingSourceList) {
        this.financingSourceList = financingSourceList;
    }

    public List<Fundsource> getFundSourceList() {
        return fundSourceList;
    }

    public void setFundSourceList(final List<Fundsource> fundSourceList) {
        this.fundSourceList = fundSourceList;
    }

    public void setEstimateWorkflowService(final WorkflowService<AbstractEstimate> workflow) {
        estimateWorkflowService = workflow;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    /**
     * @param worksService the worksService to set
     */
    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public Date getFinancialYearStartDate() {
        financialYearStartDate = finHibernateDao.getFinancialYearByFinYearRange(
                worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE")).getStartingDate();
        return financialYearStartDate;
    }

    public void setFinancialYearStartDate(final Date financialYearStartDate) {
        this.financialYearStartDate = financialYearStartDate;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public String getApproverComments() {
        return approverComments;
    }

    public void setApproverComments(final String approverComments) {
        this.approverComments = approverComments;
    }

    @ValidationErrorPage(value = "searchBudgetFolio")
    public String searchBudgetFolio() {
        if (!StringUtils.isBlank(option) && "searchdetails".equalsIgnoreCase(option))
            search("menu");
        return SEARCH_BUDGET_FOLIO;
    }

    // Added for Deposit Works Folio Report
    public String searchDepositWorksFolio() {
        if (!StringUtils.isBlank(option) && "searchDepositWorksFolioDetails".equalsIgnoreCase(option))
            viewDepositWorksFolioReport();
        return SEARCH_DEPOSIT_WORKS_FOLIO;
    }

    public String viewDepositFolio() {
        final Fund fund = fundDao.fundById(fundId, false);
        final DepositCode depositCode = depositCodeService.findById(depositCodeId, false);
        final CChartOfAccounts coa = chartOfAccountsHibernateDAO.findById(glcodeId, false);
        financialDetail.setCoa(coa);
        financialDetail.setFund(fund);
        code = depositCode.getCode() + "-" + depositCode.getCodeName();
        asOnDate = new Date();
        isEnableSelect = "true";

        return SEARCH_DEPOSIT_WORKS_FOLIO;
    }

    public String viewDepositWorksFolio() {
        // Incase of revision estimate, get the parent's financial details
        if (abstractEstimate.getParent() != null)
            financialDetail = abstractEstimate.getParent().getFinancialDetails().get(0);
        else
            financialDetail = abstractEstimate.getFinancialDetails().get(0);
        code = abstractEstimate.getDepositCode().getCode() + "-" + abstractEstimate.getDepositCode().getCodeName();
        depositCodeId = abstractEstimate.getDepositCode().getId();
        final AbstractEstimateAppropriation astractEstimateAppropriation = abstractEstimateService
                .getEstimateAppropriationService().findByNamedQuery("getLatestDepositWorksUsageForEstimate",
                        abstractEstimate.getId());
        if (astractEstimateAppropriation != null)
            asOnDate = astractEstimateAppropriation.getDepositWorksUsage().getAppropriationDate();
        isEnableSelect = "true";

        /*
         * if(!StringUtils.isBlank(option) && "searchDepositWorksFolioDetails".equalsIgnoreCase(option)){
         * viewDepositWorksFolioReport(); }
         */return SEARCH_DEPOSIT_WORKS_FOLIO;
    }

    public String viewDepositWorksFolioReport() throws ValidationException {
        Fund fund = null;
        CChartOfAccounts coa = null;
        try {
            final AbstractEstimate abstractEstimate = new AbstractEstimate();
            final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(
                    "from Accountdetailtype where name=?", "DEPOSITCODE");
            fund = (Fund) persistenceService.find("from Fund where id=?", financialDetail.getFund().getId());
            coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?", financialDetail
                    .getCoa().getId());
            final Map<String, Object> reportParams = getDepositFolioDetails(abstractEstimate, fund, coa,
                    accountdetailtype, depositCodeId, asOnDate);
            totalDepositAmount = (BigDecimal) reportParams.get("totalDeposit");

            if (latestCumulative != 0.00D || totalDepositAmount != BigDecimal.ZERO) {
                final BudgetFolioDetail e = new BudgetFolioDetail();
                e.setSrlNo(Integer.getInteger(""));
                e.setBudgetApprNo("<b>Latest Status :</b>");
                e.setEstimateNo("");
                e.setNameOfWork("");
                e.setEstimateDate("");
                e.setCumulativeTotal(latestCumulative);
                e.setBalanceAvailable(totalDepositAmount);
                e.setCumulativeExpensesIncurred(latestCumulativeExpense);
                e.setActualBalanceAvailable(totalDepositAmount.doubleValue() - latestCumulativeExpense);
                final List<BudgetFolioDetail> tempList = new ArrayList<BudgetFolioDetail>();
                tempList.add(e);
                if (approvedBudgetFolioDetails == null || approvedBudgetFolioDetails.isEmpty())
                    approvedBudgetFolioDetails = new ArrayList<BudgetFolioDetail>();
                else
                    approvedBudgetFolioDetails.add(e);
            } else
                approvedBudgetFolioDetails = new ArrayList<BudgetFolioDetail>();
        } catch (final ValidationException e) {
            logger.error("GlCodeValidation >>" + e.getErrors().get(0).getMessage());
            addFieldError("glCodeValidate", e.getErrors().get(0).getMessage());
        }
        return RESULTS;
    }

    public void search(final String src) {
        if (APP.equalsIgnoreCase(src) && abstractEstimate != null
                && abstractEstimate.getFinancialDetails().get(0) != null)
            financialDetail = abstractEstimate.getFinancialDetails().get(0);

        if (financialDetail != null && financialDetail.getFund() != null && financialDetail.getFund().getId() != null
                && financialDetail.getFund().getId() != -1)
            queryParamMap.put("fundid", financialDetail.getFund().getId());

        if (financialDetail != null && financialDetail.getFunction() != null
                && financialDetail.getFunction().getId() != null && financialDetail.getFunction().getId() != -1)
            queryParamMap.put("functionid", financialDetail.getFunction().getId());
        if (financialDetail != null && financialDetail.getBudgetGroup() != null
                && financialDetail.getBudgetGroup().getId() != null && financialDetail.getBudgetGroup().getId() != -1) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            budgetheadid.add(financialDetail.getBudgetGroup());
            queryParamMap.put("budgetheadid", budgetheadid);
        }

        if (APP.equalsIgnoreCase(src) && financialDetail != null
                && financialDetail.getAbstractEstimate().getUserDepartment() != null)
            queryParamMap.put("deptid", financialDetail.getAbstractEstimate().getUserDepartment().getId());
        else if (getUserDepartment() != null && getUserDepartment() != -1)
            queryParamMap.put("deptid", getUserDepartment());

        if (APP.equalsIgnoreCase(src) && abstractEstimate != null
                && abstractEstimate.getLeastFinancialYearForEstimate() != null
                && abstractEstimate.getLeastFinancialYearForEstimate().getId() != null) {
            queryParamMap.put("financialyearid", financialDetail.getAbstractEstimate()
                    .getLeastFinancialYearForEstimate().getId());
            queryParamMap.put("fromDate", financialDetail.getAbstractEstimate().getLeastFinancialYearForEstimate()
                    .getStartingDate());
            queryParamMap.put("toDate", new Date());
        } else if (getReportDate() != null) {
            if (!DateUtils.compareDates(new Date(), getReportDate()))
                throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.currentDate.reportDate",
                        getText("greaterthan.currentDate.reportDate"))));
            CFinancialYear finyear = null;
            try {
                finyear = abstractEstimateService.getCurrentFinancialYear(getReportDate());
            } catch (final ApplicationRuntimeException noFinYearExp) {
                if (noFinYearExp.getMessage().equals("Financial Year Id does not exist."))
                    throw new ValidationException(Arrays.asList(new ValidationError(noFinYearExp.getMessage(),
                            noFinYearExp.getMessage())));
                else
                    throw noFinYearExp;

            }
            if (finyear != null && finyear.getId() != null)
                queryParamMap.put("financialyearid", finyear.getId());
            queryParamMap.put("toDate", getReportDate());
        }

        if (!queryParamMap.isEmpty() && getFieldErrors().isEmpty()) {
            BigDecimal planningBudgetPerc = new BigDecimal(0);
            try {
                totalGrant = budgetDetailsDAO.getBudgetedAmtForYear(queryParamMap);
                planningBudgetPerc = getPlanningBudgetPercentage(queryParamMap);
            } catch (final ValidationException valEx) {
                logger.error(valEx);
            }
            // String appValue =
            // worksService.getWorksConfigValue(PERCENTAGE_GRANT);

            if (planningBudgetPerc != null && !planningBudgetPerc.equals(0)) {
                setAppValueLabel(planningBudgetPerc.toString());
                totalGrantPerc = totalGrant.multiply(planningBudgetPerc.divide(new BigDecimal(100)));
                queryParamMap.put("totalGrantPerc", totalGrantPerc);
            }
            final Map<String, List> approvedBudgetFolioDetailsMap = abstractEstimateService
                    .getApprovedAppropriationDetailsForBugetHead(queryParamMap);
            approvedBudgetFolioDetails = new ArrayList<BudgetFolioDetail>();
            if (approvedBudgetFolioDetailsMap != null && !approvedBudgetFolioDetailsMap.isEmpty()) {
                approvedBudgetFolioDetails = approvedBudgetFolioDetailsMap.get("budgetFolioList");
                setReportLatestValues(approvedBudgetFolioDetailsMap);
            }
        }
    }

    private BigDecimal getPlanningBudgetPercentage(final Map<String, Object> queryParamMap) {
        return budgetDetailsDAO.getPlanningPercentForYear(queryParamMap);

    }

    /**
     * This method display report screen for budgetFolio
     */
    // @SkipValidation
    public String viewBudgetFolio() {
        // Incase of revision estimate, get the parent's financial details
        if (abstractEstimate.getParent() != null)
            financialDetail = abstractEstimate.getParent().getFinancialDetails().get(0);
        else
            financialDetail = abstractEstimate.getFinancialDetails().get(0);
        setUserDepartment(abstractEstimate.getUserDepartment().getId());
        addDropdownData(BUDGET_GROUP_SEARCH_LIST, dropdownData.get(BUDGET_GROUP_LIST));
        final AbstractEstimateAppropriation astractEstimateAppropriation = abstractEstimateService
                .getEstimateAppropriationService().findByNamedQuery("getLatestBudgetUsageForEstimate",
                        abstractEstimate.getId());
        if (astractEstimateAppropriation != null)
            reportDate = new Date(astractEstimateAppropriation.getBudgetUsage().getUpdatedTime().getTime());
        isEnableSelect = "true";
        return SEARCH_BUDGET_FOLIO;
    }

    /*
     * public void getTotalGrantAppValue() throws NumberFormatException{ if(StringUtils
     * .isNotBlank(worksService.getWorksConfigValue(PERCENTAGE_GRANT)))
     * appValue=worksService.getWorksConfigValue(PERCENTAGE_GRANT); if(StringUtils.isNotBlank(appValue)){ Double
     * appValueDbl=Double.parseDouble(appValue); appValueDbl=appValueDbl.doubleValue() *100; appValueLabel=appValueDbl.toString();
     * setAppValueLabel(appValueLabel); } }
     */

    /**
     * print pdf
     *
     * @throws JRException ,Exception
     */
    // @SkipValidation
    public String viewBudgetFolioPdf() throws JRException, Exception {
        Map reportParams = new HashMap();
        if (!StringUtils.isBlank(option) && "searchPdf".equalsIgnoreCase(option)) {
            search("menu");
            if (getUserDepartment() != null && getUserDepartment() != -1)
                reportParams.put("departmentName", deptName);

            if (financialDetail != null && financialDetail.getFunction() != null
                    && financialDetail.getFunction() != null && financialDetail.getFunction().getName() != null)
                reportParams.put("functionCenter", financialDetail.getFunction().getName());
            if (financialDetail != null && financialDetail.getBudgetGroup() != null
                    && financialDetail.getBudgetGroup().getId() != null
                    && financialDetail.getBudgetGroup().getId() != -1)
                reportParams.put("budgetHead", financialDetail.getBudgetGroup().getName());

            if (financialDetail != null && financialDetail.getFund() != null
                    && financialDetail.getFund().getId() != null && financialDetail.getFund().getId() != -1)
                reportParams.put(Fund, financialDetail.getFund().getName());

            reportParams.put("totalGrant", totalGrant);
            reportParams.put("totalGrantPer", totalGrantPerc);
            // reportParams.put("appValue", totalGrantPerc);
            // getTotalGrantAppValue();
            reportParams.put("appValueLabel", appValueLabel);
        } else {
            search(APP);
            reportParams = abstractEstimateService.createBudgetFolioHeaderJasperObject(abstractEstimate, totalGrant,
                    totalGrantPerc);
            // getTotalGrantAppValue();
            reportParams.put("appValueLabel", appValueLabel);
        }

        reportParams.put("latestCumulative", latestCumulative);
        reportParams.put("latestBalance", latestBalance);
        final ReportRequest reportRequest = new ReportRequest("BudgetFolio", approvedBudgetFolioDetails, reportParams);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            budgetFolioPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    public String viewDepositFolioPDF() throws ParseException {
        Map<String, Object> reportParams = null;
        Fund fund = null;
        CChartOfAccounts coa = null;
        CFunction function = null;
        Date appropriationDate = new Date();
        final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(
                "from Accountdetailtype where name=?", "DEPOSITCODE");
        if (abstractEstimate != null && abstractEstimate.getFinancialDetails() != null
                && !abstractEstimate.getFinancialDetails().isEmpty()
                && abstractEstimate.getFinancialDetails().get(0) != null && parameters.get("fundId") == null) {
            fund = abstractEstimate.getFinancialDetails().get(0).getFund();
            coa = abstractEstimate.getFinancialDetails().get(0).getCoa();
            if (abstractEstimate.getDepositCode() != null)
                depositCodeId = abstractEstimate.getDepositCode().getId();
            function = abstractEstimate.getFinancialDetails().get(0).getFunction();
        } else {
            Integer fundId = 0;
            Long glcodeId = null;
            Long functionId = null;
            if (parameters.get("fundId") != null)
                fundId = Integer.parseInt(parameters.get("fundId")[0]);
            if (parameters.get("glcodeId") != null)
                glcodeId = Long.valueOf(parameters.get("glcodeId")[0]);
            if (parameters.get("depositCodeId") != null)
                depositCodeId = Long.valueOf(parameters.get("depositCodeId")[0]);
            if (parameters.get("functionId") != null) {
                functionId = Long.valueOf(parameters.get("functionId")[0]);
                function = (CFunction) persistenceService.find("from CFunction where id=?", functionId);
            }
            if (parameters.get("asOnDate") != null) {
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
                appropriationDate = sdf.parse(parameters.get("asOnDate")[0]);
            }
            fund = (Fund) persistenceService.find("from Fund where id=?", fundId);
            coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?", glcodeId);
        }
        reportParams = getDepositFolioDetails(abstractEstimate, fund, coa, accountdetailtype, depositCodeId,
                appropriationDate);
        if (function != null)
            reportParams.put(FUNCTION, function.getName());
        reportParams.put("ABSTRACT_ESTIMATE", abstractEstimate);
        final ReportRequest reportRequest = new ReportRequest("DepositFolio", approvedBudgetFolioDetails, reportParams);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            budgetFolioPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    public Map<String, Object> getDepositFolioDetails(final AbstractEstimate abstractEstimate, final Fund fund,
            final CChartOfAccounts coa, final Accountdetailtype accountdetailtype, final Long depositCodeId,
            final Date appropriationDate) {
        final DepositCode depositCode = (DepositCode) persistenceService.find("from DepositCode where id=?",
                depositCodeId);

        final HashMap<String, Object> reportParams = new HashMap<String, Object>();
        final Map<String, List> approvedBudgetFolioDetailsMap = depositWorksUsageService.getDepositFolioDetails(
                abstractEstimate, fund, coa, accountdetailtype, depositCodeId, appropriationDate);

        final BigDecimal getTotalDepositAmount = depositWorksUsageService.getTotalDepositWorksAmount(fund, coa,
                accountdetailtype, depositCodeId, appropriationDate);

        if (approvedBudgetFolioDetailsMap.isEmpty()) {
            latestCumulative = 0.0D;
            latestCumulativeExpense = 0.0D;
        } else {
            approvedBudgetFolioDetails = new ArrayList<BudgetFolioDetail>();
            approvedBudgetFolioDetails = approvedBudgetFolioDetailsMap.get("depositFolioList");
            final List calculatedValuesList = approvedBudgetFolioDetailsMap.get("calculatedValues");
            latestCumulative = (Double) calculatedValuesList.get(0);
            latestCumulativeExpense = (Double) approvedBudgetFolioDetailsMap.get("totalCumulativeExpensesIncurred")
                    .get(0);
        }
        reportParams.put("fund", fund.getName());
        reportParams.put("depositCode", depositCode.getCode());
        reportParams.put("depositworksName", depositCode.getCodeName());
        reportParams.put("totalDeposit", getTotalDepositAmount);
        reportParams.put("latestCumulative", latestCumulative);
        reportParams.put("latestBalance", new BigDecimal(getTotalDepositAmount.doubleValue() - latestCumulative));
        reportParams.put("latestCumulativeExpense", latestCumulativeExpense);
        reportParams.put("totalActualBalanceAvailable", getTotalDepositAmount.doubleValue() - latestCumulativeExpense);
        return reportParams;
    }

    public void setReportLatestValues(final Map<String, List> approvedBudgetFolioDetailsMap) {
        final List calculatedValuesList = approvedBudgetFolioDetailsMap.get("calculatedValues");
        latestCumulative = (Double) calculatedValuesList.get(0);
        latestBalance = (BigDecimal) calculatedValuesList.get(1);
    }

    public BigDecimal getTotalGrant() {
        return totalGrant;
    }

    public BigDecimal getTotalGrantPerc() {
        return totalGrantPerc;
    }

    public List<BudgetFolioDetail> getApprovedBudgetFolioDetails() {
        return approvedBudgetFolioDetails;
    }

    public InputStream getBudgetFolioPDF() {
        return budgetFolioPDF;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setApprovedBudgetFolioDetails(final List<BudgetFolioDetail> approvedBudgetFolioDetails) {
        this.approvedBudgetFolioDetails = approvedBudgetFolioDetails;
    }

    public Double getLatestCumulative() {
        return latestCumulative;
    }

    public BigDecimal getLatestBalance() {
        return latestBalance;
    }

    public Map<String, Object> getQueryParamMap() {
        return queryParamMap;
    }

    public void setQueryParamMap(final Map<String, Object> queryParamMap) {
        this.queryParamMap = queryParamMap;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(final Date reportDate) {
        this.reportDate = reportDate;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public String getOption() {
        return option;
    }

    public void setOption(final String option) {
        this.option = option;
    }

    public Map<String, String> getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(final Map<String, String> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    @Override
    public void validate() {
        if (skipBudget && parameters.get("actionName") != null) {
            if (financialDetail.getCoa() == null || financialDetail.getCoa() != null
                    && (financialDetail.getCoa().getId() == null || financialDetail.getCoa().getId() == -1))
                addFieldError("depoist.accountCode", getText("estimate.deposit.accountCode.mandatory"));
            if (depositCodeId == null || depositCodeId == -1)
                addFieldError("depoistCode", getText("estimate.depositCode.mandatory"));
        }
        if (parameters.get("actionName") != null && parameters.get("actionName")[0] != null
                && parameters.get("actionName")[0].equals(BUDGET_DETAILS_SAVE))
            if (financialDetail.getBudgetGroup() != null && financialDetail.getBudgetGroup().getId() != null
                    && financialDetail.getBudgetGroup().getId() != -1)
                if (financialDetail.getBudgetGroup().getMaxCode().getGlcode()
                        .startsWith(worksService.getWorksConfigValue(SCHEME_MANDATORYCHECK_BUDGETHEAD))) {
                    if (financialDetail.getScheme() == null || financialDetail.getScheme() != null
                            && financialDetail.getScheme().getId() == null || financialDetail.getScheme() != null
                                    && financialDetail.getScheme().getId() == -1)
                        addActionError(getText("mandatory.scheme"));
                    if (financialDetail.getSubScheme() == null || financialDetail.getSubScheme() != null
                            && financialDetail.getSubScheme().getId() == null || financialDetail.getSubScheme() != null
                                    && financialDetail.getSubScheme().getId() == -1)
                        addActionError(getText("mandatory.subScheme"));
                }

        if ("searchdetails".equalsIgnoreCase(option)) {
            if (!StringUtils.isBlank(mandatoryFields.get(Fund))
                    && (financialDetail.getFund() == null || financialDetail.getFund().getId() == null || financialDetail
                            .getFund().getId() == -1))
                addFieldError(Fund, getText("budgetfolio.fund.mandatory"));
            if (!StringUtils.isBlank(mandatoryFields.get("department"))
                    && (userDepartment == null || userDepartment == -1))
                addFieldError("userDepartment", getText("budgetfolio.user.department.mandatory"));
            if (financialDetail.getFunction() == null || financialDetail.getFunction().getId() == null
                    || financialDetail.getFunction().getId() == -1)
                addFieldError(FUNCTION, getText("budgetfolio.function.mandatory"));
            if (financialDetail.getBudgetGroup() == null || financialDetail.getBudgetGroup().getId() == null
                    || financialDetail.getBudgetGroup().getId() == -1)
                addFieldError("budgetGroup", getText("budgetfolio.budgetGroup.mandatory"));
            if (finYearId == null || finYearId == -1)
                addFieldError("finYearId", getText("budgetfolio.finYear.mandatory"));
        }
    }

    public Boolean isSkipBudgetCheck() {
        final List<String> depositTypeList = getAppConfigValuesToSkipBudget();

        logger.info("lenght of appconfig values>>>>>> " + depositTypeList.size());
        if (abstractEstimate != null && abstractEstimate.getId() != null)
            for (final String type : depositTypeList)
                if (type.equals(abstractEstimate.getNatureOfWork().getName()))
                    skipBudget = true;
        return skipBudget;
    }

    private FinancingSource getMaxFinancingSource(final List<FinancingSource> financingSources) {
        double max = 0.0;
        FinancingSource maxFinSource = null;
        for (final FinancingSource finSource : financingSources)
            if (finSource != null)
                if (finSource.getPercentage() > max) {
                    max = finSource.getPercentage();
                    maxFinSource = finSource;
                }

        return maxFinSource;
    }

    private String generateFinYrList(final List<CFinancialYear> finYrList) {
        final Date todaysDate = new Date();
        final StringBuffer finStrBfr = new StringBuffer();
        for (final CFinancialYear yr : finYrList)
            if (yr.getStartingDate().compareTo(todaysDate) <= 0 && yr.getEndingDate().compareTo(todaysDate) >= 0)
                finStrBfr.append("id:" + yr.getId() + "--"
                        + DateUtils.getFormattedDate(yr.getStartingDate(), "dd/MM/yyyy") + "--"
                        + DateUtils.getFormattedDate(todaysDate, "dd/MM/yyyy"));
            else
                finStrBfr.append("id:" + yr.getId() + "--"
                        + DateUtils.getFormattedDate(yr.getStartingDate(), "dd/MM/yyyy") + "--"
                        + DateUtils.getFormattedDate(yr.getEndingDate(), "dd/MM/yyyy"));
        return finStrBfr.toString();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(final String deptName) {
        this.deptName = deptName;
    }

    public List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
    }

    public String getAppValue() {
        return appValue;
    }

    public void setAppValue(final String appValue) {
        this.appValue = appValue;
    }

    public AbstractEstimateService getAbstractEstimateService() {
        return abstractEstimateService;
    }

    public String getAppValueLabel() {
        return appValueLabel;
    }

    public void setAppValueLabel(final String appValueLabel) {
        this.appValueLabel = appValueLabel;
    }

    public void setDepositWorksUsageService(final DepositWorksUsageService depositWorksUsageService) {
        this.depositWorksUsageService = depositWorksUsageService;
    }

    public boolean isSkipBudget() {
        return skipBudget;
    }

    public void setSkipBudget(final boolean skipBudget) {
        this.skipBudget = skipBudget;
    }

    public Long getDepositCodeId() {
        return depositCodeId;
    }

    public void setDepositCodeId(final Long depositCodeId) {
        this.depositCodeId = depositCodeId;
    }

    public void setDepositCodeService(final PersistenceService<DepositCode, Long> depositCodeService) {
        this.depositCodeService = depositCodeService;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public void setLatestCumulative(final Double latestCumulative) {
        this.latestCumulative = latestCumulative;
    }

    public BigDecimal getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setTotalDepositAmount(final BigDecimal totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public void setLatestBalance(final BigDecimal latestBalance) {
        this.latestBalance = latestBalance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getIsEnableSelect() {
        return isEnableSelect;
    }

    public void setIsEnableSelect(final String isEnableSelect) {
        this.isEnableSelect = isEnableSelect;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(final Integer fundId) {
        this.fundId = fundId;
    }

    public Long getGlcodeId() {
        return glcodeId;
    }

    public void setGlcodeId(final Long glcodeId) {
        this.glcodeId = glcodeId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Integer functionId) {
        this.functionId = functionId;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public List<MultiYearEstimate> getActionMultiYearEstimateValues() {
        return actionMultiYearEstimateValues;
    }

    public void setActionMultiYearEstimateValues(final List<MultiYearEstimate> actionMultiYearEstimateValues) {
        this.actionMultiYearEstimateValues = actionMultiYearEstimateValues;
    }

    public String getRoadCutDepCodeFlag() {
        return roadCutDepCodeFlag;
    }

    public void setRoadCutDepCodeFlag(final String roadCutDepCodeFlag) {
        this.roadCutDepCodeFlag = roadCutDepCodeFlag;
    }

    public Long getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(final Long userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(final String currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public String getFinYearRangeStr() {
        return finYearRangeStr;
    }

    public Integer getFinYearId() {
        return finYearId;
    }

    public void setFinYearId(final Integer finYearId) {
        this.finYearId = finYearId;
    }

    public String getBudgetHeadGlcode() {
        return budgetHeadGlcode;
    }

    public void setBudgetHeadGlcode(final String budgetHeadGlcode) {
        this.budgetHeadGlcode = budgetHeadGlcode;
    }

}
