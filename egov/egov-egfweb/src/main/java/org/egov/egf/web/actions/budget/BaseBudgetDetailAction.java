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
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.model.BudgetAmountView;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
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

public abstract class BaseBudgetDetailAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    protected BudgetDetail budgetDetail = new BudgetDetail();
    protected List<BudgetDetail> budgetDetailList = new ArrayList<BudgetDetail>();
    protected List<BudgetDetail> savedbudgetDetailList = new ArrayList<BudgetDetail>();
    protected BudgetDetailService budgetDetailService;
    BudgetService budgetService;
    private PersistenceService<CFinancialYear, Long> finYearService;
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> gridFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
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
    private EisCommonService eisCommonService;
    BudgetDetailHelper budgetDetailHelper;
    protected boolean addNewDetails = false;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    protected BudgetDetailConfig budgetDetailConfig;
    
    @Autowired
    private EgovMasterDataCaching masterDataCache;

    public boolean isAddNewDetails() {
        return addNewDetails;
    }

    public void setAddNewDetails(final boolean addNewDetails) {
        this.addNewDetails = addNewDetails;
    }

    final static Integer INT_ZERO = 0;
    final static Long LONG_ZERO = Long.valueOf(0);
    FinancialYearDAO financialYearDAO;
    protected boolean re = false;
    private boolean showMessage = false;
    protected List<BigDecimal> beAmounts = new ArrayList<BigDecimal>();
    private Budget referenceBudget;
    private CFinancialYear financialYear;
    protected List<Budget> budgetList = new ArrayList<Budget>();
    protected boolean showRe;
    Long budgetDocumentNumber;
    protected boolean showDetails;
    protected Long searchfunctionid;
    protected Long searchbudgetGroupid;
    private static Logger LOGGER = Logger.getLogger(BaseBudgetDetailAction.class);

    public abstract void populateSavedbudgetDetailListFor(Budget budget);

    public abstract void populateSavedbudgetDetailListForDetail(BudgetDetail bd);

    public void setBudgetDocumentNumber(final Long documentNumber) {
        budgetDocumentNumber = documentNumber;
    }

    public Long getBudgetDocumentNumber() {
        return budgetDocumentNumber;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public boolean isRe() {
        return re;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetHelper) {
        budgetDetailHelper = budgetHelper;
    }

    public void setFinYearService(final PersistenceService<CFinancialYear, Long> finYearService) {
        this.finYearService = finYearService;
    }

    public Date getAsOnDate() {
        return asOnDate == null ? new Date() : asOnDate;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final Integer schemeId) {
        this.schemeId = schemeId;
    }

    public List<Scheme> getSubSchemes() {
        return subSchemes;
    }

    public BaseBudgetDetailAction() {
    }

    @Override
    public String execute() throws Exception {
        return NEW;
    }

    public String create() {
        validateMandatoryFields();
        budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
        saveBudgetDetails(false, financialYear, budgetDetail.getBudget());
        setAsOnDateOnSelectedBudget();
        showMessage = true;
        addActionMessage("Budget details saved successfully");
        return NEW;
    }

    @ValidationErrorPage(value = "new-re")
    public String createRe() {
        showRe = true;
        try {
            getActionMessages().clear();
            validateMandatoryFields();
            budgetDetailHelper.removeEmptyBudgetDetails(budgetDetailList);
            if (!addNewDetails)
                deleteExisting();
            validateAmounts(budgetDetailList);
            saveBudgetDetails(true, financialYear, budgetDetail.getBudget());
            setAsOnDateOnSelectedBudget();
            showMessage = true;
            addActionMessage("RE proposal for current year and BE proposal for next year saved successfully");
            dropdownData.put("budgetList", Collections.EMPTY_LIST);
            budgetDetail = new BudgetDetail();
            budgetDetail.setExecutingDepartment(null);
            // financialYear = null;
        } catch (final ValidationException e) {
            loadBudgets("RE");
            dropdownData.put("budgetList", budgetList);
            referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
            throw e;
        }
        return "new-re";
    }

    @ValidationErrorPage(value = "newDetail-re")
    public String createBudgetDetail() {

        showRe = true;
        re = budgetService.hasReForYear(budgetDetail.getBudget().getFinancialYear().getId());
        try {
            getActionMessages().clear();
            removeEmptyBudgetDetails(budgetDetailList);
            validateIsPrimary();
            if (!rowsEmpty()) {
                if (!addNewDetails)
                    deleteExisting();
                validateMandatoryFields();
                // validateAmounts(budgetDetailList);

                saveNewBudgetDetailItems(true, financialYear, budgetDetail.getBudget());
            }

            // budgetDetail.transition(true).withStateValue("END").withOwner(getPosition()).withComments("");
            setAsOnDateOnSelectedBudget();

            // approve();
            showMessage = true;
            addActionMessage("RE proposal for current year and BE proposal for next year saved successfully");
            dropdownData.put("budgetList", Collections.EMPTY_LIST);
            budgetDetail = new BudgetDetail();
            budgetDetail.setExecutingDepartment(null);
            // financialYear = null;
        } catch (final ValidationException e) {
            loadBudgets("RE");
            dropdownData.put("budgetList", budgetList);
            referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
            throw e;
        }
        return "newDetail-re";
    }

    @ValidationErrorPage(value = "new-re")
    public String createReAndForward() {
        showRe = true;
        try {
            getActionMessages().clear();
            removeEmptyBudgetDetails(budgetDetailList);
            validateIsPrimary();
            if (!rowsEmpty()) {
                if (!addNewDetails)
                    deleteExisting();
                validateMandatoryFields();
                // validateAmounts(budgetDetailList);
                saveBudgetDetails(true, financialYear, budgetDetail.getBudget());
            }
            setAsOnDateOnSelectedBudget();
            approve();
            showMessage = true;
            addActionMessage("RE proposal for current year and BE proposal for next year saved successfully");
            dropdownData.put("budgetList", Collections.EMPTY_LIST);
            budgetDetail = new BudgetDetail();
            budgetDetail.setExecutingDepartment(null);
            // financialYear = null;
        } catch (final ValidationException e) {
            loadBudgets("RE");
            dropdownData.put("budgetList", budgetList);
            referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
            throw e;
        }
        return "new-re";
    }

    /**
     * @param budget deletes the existing selected budgets from db
     */

    private void deleteExisting() {

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Initiating deletion ..........");
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
        final StringBuffer addlCondtion = new StringBuffer(50);
        if (searchfunctionid != null && searchfunctionid != 0)
            addlCondtion.append("and function=" + searchfunctionid);
        if (searchbudgetGroupid != null && searchbudgetGroupid != 0)
            addlCondtion.append("and budgetGroup.id=" + searchbudgetGroupid);
        new ArrayList<BudgetDetail>();
        final int executeUpdate = persistenceService.getSession()
                .createSQLQuery("delete from egf_budgetdetail where budget=" + budgetDetail.getBudget().getId() + addlCondtion)
                .executeUpdate();
        final int executeUpdate2 = persistenceService.getSession()
                .createSQLQuery("delete from egf_budgetdetail where budget=" + referenceBudgetFor.getId() + addlCondtion)
                .executeUpdate();

        /*
         * for (BudgetDetail reDetail :budgetDetailList) { if(reDetail.getId()!=null && reDetail.getId()!=0) { for(BudgetDetail
         * beDetail:result) { if(compareREandBEDetails(reDetail,beDetail)) { if(LOGGER.isInfoEnabled()) LOGGER.info("deleting "
         * +beDetail.getId() +"where budgetHeade is " +beDetail.getBudgetGroup().getName() +" and function  is "
         * +beDetail.getFunction().getName()); persistenceService.getSession().createSQLQuery(
         * "delete from egf_budgetdetail where id=" +beDetail.getId()).executeUpdate(); } } if(LOGGER.isInfoEnabled())
         * LOGGER.info("deleting "+reDetail.getId() +"where budgetHeade is " +reDetail.getBudgetGroup().getName() +
         * " and function  is "+reDetail.getFunction().getName()); .getCurrentSession().createSQLQuery(
         * "delete from egf_budgetdetail where id="+reDetail.getId()).executeUpdate(); reDetail.setId(null); } else {
         * reDetail.setId(null); } }
         */
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Deleting complete. deleted " + executeUpdate + " RE  and " + executeUpdate2 + " BE items ");
        persistenceService.getSession().flush();
    }

    private void validateIsPrimary() {
        if (budgetDetail.getBudget() != null && !budgetDetail.getBudget().getIsPrimaryBudget())
            throw new ValidationException(Arrays.asList(new ValidationError("budget.not.primary", "budget.not.primary")));
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
        if (budgetDetail.getBudget() != null && referenceBudgetFor != null && !referenceBudgetFor.getIsPrimaryBudget())
            throw new ValidationException(Arrays.asList(new ValidationError("budget.not.primary", "budget.not.primary")));
    }

    private boolean rowsEmpty() {
        if (budgetDetailList == null)
            return true;
        for (final BudgetDetail row : budgetDetailList) {
            if (gridFields.contains(Constants.EXECUTING_DEPARTMENT) && row.getExecutingDepartment().getId() != -1
                    && row.getExecutingDepartment().getId() != 0)
                return false;
            if (gridFields.contains(Constants.FUNCTION) && row.getFunction().getId() != -1 && row.getFunction().getId() != 0)
                return false;
            if (gridFields.contains(Constants.FUND) && row.getFund().getId() != -1 && row.getFund().getId() != 0)
                return false;
            if (gridFields.contains(Constants.SCHEME) && row.getScheme().getId() != -1 && row.getScheme().getId() != 0)
                return false;
            if (gridFields.contains(Constants.SUB_SCHEME) && row.getSubScheme().getId() != -1 && row.getSubScheme().getId() != 0)
                return false;
            if (gridFields.contains(Constants.BOUNDARY) && row.getBoundary().getId() != -1 && row.getBoundary().getId() != 0)
                return false;
            if (row.getBudgetGroup().getId() != -1 && row.getBudgetGroup().getId() != 0)
                return false;
        }
        return true;
    }

    private void validateAmounts(final List<BudgetDetail> detailList) {
        for (int i = 0; i < detailList.size(); i++) {
            if (beAmounts.get(i) == null)
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.re.amount",
                        "budgetDetail.re.amount")));
            if (detailList.get(i).getOriginalAmount() == null)
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.be.amount",
                        "budgetDetail.be.amount")));
        }
    }

    protected void validateMandatoryFields() {
        checkHeaderMandatoryField(Constants.EXECUTING_DEPARTMENT, budgetDetail.getExecutingDepartment(),
                "budgetDetail.executingDepartment.mandatory");
        checkHeaderMandatoryField(Constants.FUNCTION, budgetDetail.getFunction(), "budgetDetail.function.mandatory");
        checkHeaderMandatoryField(Constants.FUNCTIONARY, budgetDetail.getFunctionary(), "budgetDetail.functionary.mandatory");
        checkHeaderMandatoryField(Constants.SCHEME, budgetDetail.getScheme(), "budgetDetail.scheme.mandatory");
        checkHeaderMandatoryField(Constants.SUB_SCHEME, budgetDetail.getSubScheme(), "budgetDetail.subScheme.mandatory");
        checkHeaderMandatoryField(Constants.FUND, budgetDetail.getFund(), "budgetDetail.fund.mandatory");
        checkHeaderMandatoryField(Constants.BOUNDARY, budgetDetail.getBoundary(), "budgetDetail.boundary.mandatory");
        for (final BudgetDetail detail : budgetDetailList) {
            setRelatedValues(detail);
            checkGridMandatoryField(Constants.EXECUTING_DEPARTMENT, detail.getExecutingDepartment(),
                    "budgetDetail.executingDepartment.mandatory");
            checkGridMandatoryField(Constants.FUNCTION, detail.getFunction(), "budgetDetail.function.mandatory");
            checkGridMandatoryField(Constants.FUNCTIONARY, detail.getFunctionary(), "budgetDetail.functionary.mandatory");
            checkGridMandatoryField(Constants.SCHEME, detail.getScheme(), "budgetDetail.scheme.mandatory");
            checkGridMandatoryField(Constants.SUB_SCHEME, detail.getSubScheme(), "budgetDetail.subScheme.mandatory");
            checkGridMandatoryField(Constants.FUND, detail.getFund(), "budgetDetail.fund.mandatory");
            checkGridMandatoryField(Constants.BOUNDARY, detail.getBoundary(), "budgetDetail.boundary.mandatory");
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

    private void saveBudgetDetails(final boolean withRe, CFinancialYear finYear, final Budget budget) {
        int index = 0;
        Budget refBudget = null;
        if (withRe) {
            refBudget = budgetService.getReferenceBudgetFor(budget);
            if (refBudget == null)
                throw new ValidationException(Arrays.asList(new ValidationError("no.reference.budget", "no.reference.budget")));
        }
        finYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?", finYear.getId());
        int i = 0;
        for (final BudgetDetail detail : budgetDetailList) {
            if (detail != null) {
                // update only if it is new budgetdetail Item else it is taken care by budget load
                if (detail.getId() == null)
                    detail.setUniqueNo(detail.getFund().getId() + "-" + detail.getExecutingDepartment().getId() + "-"
                            + detail.getFunction().getId() + "-" + detail.getBudgetGroup().getId());
                else
                    detail.setId(null);
                if (withRe)
                    saveAndStartWorkFlowForRe(detail, index, finYear, refBudget);
                else
                    saveAndStartWorkFlow(detail);
            }
            index++;

            if (++i % 5 == 0)
                persistenceService.getSession().flush();
            LOGGER.error("saved" + i + "Item");

        }
        populateSavedbudgetDetailListForDetail(budgetDetailList.get(0));
        budgetDetailList.clear();
    }

    private void saveNewBudgetDetailItems(final boolean withRe, CFinancialYear finYear, final Budget budget) {
        int index = 0;
        Budget refBudget = null;
        if (withRe) {
            refBudget = budgetService.getReferenceBudgetFor(budget);
            if (refBudget == null)
                throw new ValidationException(Arrays.asList(new ValidationError("no.reference.budget", "no.reference.budget")));
        }
        finYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?", finYear.getId());
        int i = 0;
        for (final BudgetDetail detail : budgetDetailList) {
            if (detail != null) {
                // update only if it is new budgetdetail Item else it is taken care by budget load
                detail.transition(true).withStateValue("END").withOwner(getPosition()).withComments("");
                if (detail.getId() == null)
                    detail.setUniqueNo(detail.getFund().getId() + "-" + detail.getExecutingDepartment().getId() + "-"
                            + detail.getFunction().getId() + "-" + detail.getBudgetGroup().getId());
                else
                    detail.setId(null);

                if (withRe)
                    saveAndStartWorkFlowForRe(detail, index, finYear, refBudget);
                else
                    saveAndStartWorkFlow(detail);
            }
            index++;

            if (++i % 5 == 0)
                persistenceService.getSession().flush();
            LOGGER.error("saved" + i + "Item");

        }
        populateSavedbudgetDetailListForDetail(budgetDetailList.get(0));
        budgetDetailList.clear();
    }

    private void setAsOnDateOnSelectedBudget() {
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != null) {
            final Budget selectedBudget = (Budget) getPersistenceService().find("from Budget where id=?",
                    budgetDetail.getBudget().getId());
            selectedBudget.setAsOnDate(getAsOnDate());
            budgetService.persist(selectedBudget);
        }
    }

    void loadBudgets(final String bere) {
        budgetList = new ArrayList<Budget>();
        if (!addNewDetails)
            budgetList
                    .addAll(persistenceService
                            .findAllBy("from Budget where id not in (select parent from Budget where parent is not null) "
                                    +
                                    "and isactivebudget = 1 and state.type='Budget' and isbere='"
                                    + bere.toUpperCase()
                                    + "' and (state.value='NEW' or lower(state.value) like lower('Forwarded by SMADMIN%')) and financialYear.id = "
                                    +
                                    getFinancialYear().getId() + " order by name"));
        else
            budgetList.addAll(persistenceService
                    .findAllBy("from Budget where id not in (select parent from Budget where parent is not null) " +
                            "and isactivebudget = 1 and state.type='Budget' and isbere='" + bere.toUpperCase()
                            + "'  and financialYear.id = " +
                            getFinancialYear().getId() + " order by name"));

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
        defaultToCurrentUsersExecutingDepartment();
        setupDropdownsInHeader();
        re = budgetService.hasReForYear(Long.valueOf(financialYearDAO.getCurrYearFiscalId()));
    }

    private void setupDropdownsInHeader() {
        setupDropdownDataExcluding(Constants.SUB_SCHEME);
        setBudgetDropDown();
        dropdownData.put("budgetGroupList", masterDataCache.get("egf-budgetGroup"));
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
        if (shouldShowField(Constants.FUND))
            dropdownData
                    .put("fundList", persistenceService.findAllBy("from Fund where isNotLeaf=0 and isActive=true order by name"));
        if (shouldShowField(Constants.BOUNDARY))
            dropdownData.put("boundaryList", persistenceService.findAllBy("from Boundary order by name"));
        addDropdownData("financialYearList",
                getPersistenceService().findAllBy("from CFinancialYear where isActive=true order by " +
                        "finYearRange desc "));
        dropdownData.put("departmentList", masterDataCache.get("egi-department"));
        dropdownData.put("designationList", Collections.EMPTY_LIST);
        dropdownData.put("userList", Collections.EMPTY_LIST);
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
        final CFinancialYear finYear = finYearService.findById(budgetDetailHelper.getFinancialYear(), false);
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
                currentYearRange = finYearService.findById(budgetDetailHelper.getFinancialYear(), false).getFinYearRange();
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
                        .findAllBy("from Budget where id not in (select parent from Budget where parent is not null) " +
                                "and isactivebudget = 1 and state.type='Budget' and isbere='RE' and financialYear.id = " +
                                getFinancialYear().getId() + " order by name"));
                dropdownData.put("budgetList", budgetList);
            } else
                dropdownData.put("budgetList", Collections.EMPTY_LIST);
        } else
            dropdownData
                    .put("budgetList",
                            persistenceService
                                    .findAllBy(
                                            "from Budget where id not in (select parent from Budget where parent is not null) and isactivebudget = 1 and state.type='Budget' and (state.value='NEW' or lower(state.value) like lower('Forwarded by SMADMIN%')) order by name"));
    }

    public List<BudgetDetail> getSavedbudgetDetailList() {
        return savedbudgetDetailList;
    }

    private void defaultToCurrentUsersExecutingDepartment() {
        if (shouldShowHeaderField("executingDepartment")) {
            User user = getUser();
        }
        /*
         * if(user.getDepartment() != null){ budgetDetail.setExecutingDepartment(findDepartment(user.get.getId())); }
         */// This fix is for Phoenix Migration.
    }

    private Department findDepartment(final Integer id) {
        return (Department) persistenceService.find("from Department where ID_DEPT=?", id);
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

    protected abstract void saveAndStartWorkFlow(BudgetDetail detail);

    protected abstract void saveAndStartWorkFlowForRe(BudgetDetail detail, int index, CFinancialYear finYear, Budget refBudget);

    protected abstract void approve();

    protected User getUser() {
        return (User) persistenceService.find("from User where id_user=?", ApplicationThreadLocals.getUserId());
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
    public Object getModel() {
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
        budgetAmountView.addAll(populateAmountData(budgetDetailList, getAsOnDate(), budgetDetail.getBudget().getFinancialYear()));
        loadBeAmounts(budgetDetailList);

        return NEW;
    }

    protected void getDetailsFilterdBy() {
        final StringBuffer mainQry = new StringBuffer(100);
        final StringBuffer addlCondtion = new StringBuffer(50);

        if (searchfunctionid != null && searchfunctionid != 0)
            addlCondtion.append("and function.id=" + searchfunctionid);
        if (searchbudgetGroupid != null && searchbudgetGroupid != 0)
            addlCondtion.append("and budgetGroup.id=" + searchbudgetGroupid);
        mainQry.append("from BudgetDetail where budget.id=? " + addlCondtion + " order by function.name,budgetGroup.name ");
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != 0)
            savedbudgetDetailList = budgetDetailService.findAllBy(mainQry.toString(), budgetDetail.getBudget().getId());

    }

    /**
     * @param savedbudgetDetailList2
     */
    protected void loadBeAmounts(final List<BudgetDetail> savedbudgetDetailList2) {
        beAmounts = new ArrayList<BigDecimal>(savedbudgetDetailList2.size());
        // int i=0;
        if (savedbudgetDetailList == null || savedbudgetDetailList.size() == 0)
            return;
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
        if (referenceBudgetFor != null) {
            final List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",
                    referenceBudgetFor.getId());
            for (final BudgetDetail budgetDetail : savedbudgetDetailList)
                for (final BudgetDetail row : result)
                    if (compareDetails(row, budgetDetail))
                        beAmounts.add(row.getOriginalAmount());
        }
    }

    protected boolean compareDetails(final BudgetDetail nextYear, final BudgetDetail current) {
        if (nextYear.getExecutingDepartment() != null && current.getExecutingDepartment() != null
                && current.getExecutingDepartment().getId() != nextYear.getExecutingDepartment().getId())
            return false;
        if (nextYear.getFunction() != null && current.getFunction() != null
                && current.getFunction().getId() != nextYear.getFunction().getId())
            return false;
        if (nextYear.getFund() != null && current.getFund() != null && current.getFund().getId() != nextYear.getFund().getId())
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
        if (nextYear.getExecutingDepartment() != null && current.getExecutingDepartment() != null
                && current.getExecutingDepartment().getId().intValue() != nextYear.getExecutingDepartment().getId().intValue())
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
        loadBudgets("RE");
        dropdownData.put("budgetList", budgetList);
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != null)
            referenceBudget = budgetService.getReferenceBudgetFor(budgetDetail.getBudget());
    }

    private void validateAsOnDate() {
        if (budgetDetail.getBudget() == null)
            throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.budget.mandatory",
                    "budgetDetail.budget.mandatory")));
        if (getAsOnDate().compareTo(budgetDetail.getBudget().getFinancialYear().getStartingDate()) < 0
                || getAsOnDate().compareTo(budgetDetail.getBudget().getFinancialYear().getEndingDate()) > 0)
            throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.invalid.asondate",
                    "budgetDetail.invalid.asondate")));
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
        subSchemes = getPersistenceService().findAllBy("from SubScheme where scheme.id=? and isActive=true order by name",
                schemeId);
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
        for (final BudgetDetail detail : budgetDetails) {
            paramMap = budgetDetailHelper.constructParamMap(getValueStack(), detail);
            final BudgetAmountView view = new BudgetAmountView();
            budgetDetailHelper.populateData(view, paramMap, asOnDate, re);
            final BudgetDetail detailWithoutBudget = new BudgetDetail();
            detailWithoutBudget.copyFrom(detail);
            detailWithoutBudget.setBudget(null);
            final List<BudgetDetail> budgetDetail = budgetDetailService.searchByCriteriaWithTypeAndFY(finYearId, "BE",
                    detailWithoutBudget);
            if (budgetDetail != null && budgetDetail.size() > 0) {
                final BigDecimal approvedAmount = budgetDetail.get(0).getApprovedAmount();
                view.setCurrentYearBeApproved(approvedAmount == null ? BigDecimal.ZERO.toString() : approvedAmount.toString());
                view.setReappropriation(budgetDetail.get(0).getApprovedReAppropriationsTotal().toString());
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
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.re.amount",
                        "budgetDetail.re.amount")));
            if (budgetDetailList.get(i).getOriginalAmount() == null)
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.be.amount",
                        "budgetDetail.be.amount")));
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
}