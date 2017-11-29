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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.model.BudgetReAppropriationView;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetReAppropriation;
import org.egov.model.budget.BudgetReAppropriationMisc;
import org.egov.pims.commons.Position;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetReAppropriationService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = "new", location = "budgetReAppropriation-new.jsp"),
        @Result(name = "search", location = "budgetReAppropriation-search.jsp"),
        @Result(name = "beRe", location = "budgetReAppropriation-beRe.jsp")
})
public class BudgetReAppropriationAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final String BERE = "beRe";
    private static final Logger LOGGER = Logger.getLogger(BudgetReAppropriationAction.class);
    private List<BudgetReAppropriationView> budgetReAppropriationList = new ArrayList<BudgetReAppropriationView>();
    private List<BudgetReAppropriationView> newBudgetReAppropriationList = new ArrayList<BudgetReAppropriationView>();
    @Autowired
    protected BudgetDetailConfig budgetDetailConfig;
    private BudgetDetail budgetDetail;
    protected Budget budget;
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> gridFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
    @Autowired
    private BudgetDetailHelper budgetDetailHelper;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;
    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;
    @Autowired
    @Qualifier("budgetReAppropriationService")
    private BudgetReAppropriationService budgetReAppropriationService;
    @Autowired
    @Qualifier("workflowService")
    private WorkflowService<BudgetReAppropriation> budgetReAppropriationWorkflowService;
    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;
    private CFinancialYear financialYear;
    private String beRe = Constants.BE;
    private String sequenceNumber;
    private BudgetReAppropriationMisc appropriationMisc = new BudgetReAppropriationMisc();
    private List<BudgetReAppropriation> reAppropriationList = null;
    private String type = "";
    private String finalStatus = "";
    private static final String ACTIONNAME = "actionName";
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private EgovMasterDataCaching masterDataCache;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    private String message = "";

    public BudgetReAppropriationMisc getAppropriationMisc() {
        return appropriationMisc;
    }

    public void setAppropriationMisc(final BudgetReAppropriationMisc appropriationMisc) {
        this.appropriationMisc = appropriationMisc;
    }

    public String getBeRe() {
        return beRe;
    }

    public void setBudgetReAppropriationService(
            final BudgetReAppropriationService budgetReAppropriationService) {
        this.budgetReAppropriationService = budgetReAppropriationService;
    }

    public void setBeRe(final String beRe) {
        this.beRe = beRe;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetDetailHelper) {
        this.budgetDetailHelper = budgetDetailHelper;
    }

    public List<BudgetReAppropriationView> getNewBudgetReAppropriationList() {
        return newBudgetReAppropriationList;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public List<BudgetReAppropriationView> getBudgetReAppropriationList() {
        return budgetReAppropriationList;
    }

    public Budget getBudget() {
        return budget;
    }

    public List<String> getHeaderFields() {
        return headerFields;
    }

    public List<String> getGridFields() {
        return gridFields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public BudgetReAppropriationAction() {
    }

    protected void setupDropdownsInHeader() {
        setupDropdownDataExcluding(Constants.SUB_SCHEME);
        finalStatus = getFinalStatus();
        dropdownData.put("financialYearList", getFinancialYearDropDown());
        if (financialYear != null && financialYear.getId() != 0L)
            dropdownData.put("budgetList", getApprovedBudgetsForFY(financialYear.getId(), finalStatus));
        else
            dropdownData.put("budgetList", Collections.EMPTY_LIST);
        dropdownData.put("budgetGroupList", persistenceService.findAllBy("from BudgetGroup where isActive=true order by name"));
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
                    .put("fundList",
                            persistenceService.findAllBy("from Fund where isNotLeaf=false and isActive=true order by name"));
        if (shouldShowField(Constants.BOUNDARY))
            dropdownData.put("boundaryList", persistenceService.findAllBy("from Boundary order by name"));
        dropdownData.put("finYearList",
                getPersistenceService().findAllBy("from CFinancialYear where isActive=true order by finYearRange desc "));
    }

    public final boolean shouldShowField(final String fieldName) {
        if (headerFields.isEmpty() && gridFields.isEmpty())
            return true;
        return budgetDetailConfig.shouldShowField(headerFields, fieldName)
                || budgetDetailConfig.shouldShowField(gridFields, fieldName);
    }

    public boolean shouldShowHeaderField(final String fieldName) {
        return budgetDetailConfig.shouldShowField(headerFields, fieldName);
    }

    public boolean shouldShowGridField(final String fieldName) {
        return budgetDetailConfig.shouldShowField(gridFields, fieldName);
    }

    @Override
    public String execute() throws Exception {
        return NEW;
    }

    @Override
    public void prepare() {
        super.prepare();
        headerFields = budgetDetailConfig.getHeaderFields();
        gridFields = budgetDetailConfig.getGridFields();
        mandatoryFields = budgetDetailConfig.getMandatoryFields();
        addRelatedEntity("budgetGroup", BudgetGroup.class);
        addRelatedEntity("budget", Budget.class);
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
        appropriationMisc.setReAppropriationDate(new Date());
        if (financialYear != null && financialYear.getId() != 0L && budgetService.hasApprovedReForYear(financialYear.getId()))
            beRe = Constants.RE;
        setupDropdownsInHeader();
        dropdownData.put("departmentList", masterDataCache.get("egi-department"));
        dropdownData.put("designationList", Collections.EMPTY_LIST);
        dropdownData.put("userList", Collections.EMPTY_LIST);
    }

    @Override
    public Object getModel() {
        return budgetDetail;
    }

    @Action(value = "/budget/budgetReAppropriation-create")
    public String create() {
        save(ApplicationThreadLocals.getUserId().intValue());
        return NEW;
    }

    @Action(value = "/budget/budgetReAppropriation-createAndForward")
    public String createAndForward() {
        final BudgetReAppropriationMisc misc = save(getUserId());
        addActionMessage(getText("budget.reapp.approved.end"));
        /*
         * final Position owner = misc.getState().getOwnerPosition(); if
         * ("END".equalsIgnoreCase(misc.getCurrentState().getValue())) addActionMessage(getText("budget.reapp.approved.end"));
         * else addActionMessage(getText("budget.reapp.approved") +
         * budgetService.getEmployeeNameAndDesignationForPosition(owner));
         */
        clearFields();
        return NEW;
    }

    private void clearFields() {
        budgetDetail = new BudgetDetail();
        budgetReAppropriationList = new ArrayList<BudgetReAppropriationView>();
        newBudgetReAppropriationList = new ArrayList<BudgetReAppropriationView>();
    }

    private BudgetReAppropriationMisc save(final Integer userId) {
        boolean reAppropriationCreated = false;
        boolean reAppForNewBudgetCreated = false;
        BudgetReAppropriationMisc misc = null;
        if (financialYear != null && financialYear.getId() != 0)
            financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?", financialYear.getId());
        try {
            misc = budgetReAppropriationService.createBudgetReAppropriationMisc(parameters.get(ACTIONNAME)[0] + "|" + userId,
                    beRe, financialYear, appropriationMisc, getPosition());
            removeEmptyReAppropriation(budgetReAppropriationList);
            reAppropriationCreated = budgetReAppropriationService.createReAppropriation(parameters.get(ACTIONNAME)[0] + "|"
                    + userId,
                    budgetReAppropriationList, getPosition(), financialYear, beRe, misc,
                    parameters.get("appropriationMisc.reAppropriationDate")[0]);
            removeEmptyReAppropriation(newBudgetReAppropriationList);
            reAppForNewBudgetCreated = budgetReAppropriationService.createReAppropriationForNewBudgetDetail(
                    parameters.get(ACTIONNAME)[0] + "|" + userId,
                    newBudgetReAppropriationList, getPosition(), misc);
            if (!reAppropriationCreated && !reAppForNewBudgetCreated)
                throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.budgetGroup.mandatory",
                        "budgetDetail.budgetGroup.mandatory")));
            newBudgetReAppropriationList.clear();
            budgetReAppropriationList.clear();
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),
                    e.getMessage())));
        }
        if (reAppropriationCreated)
            addActionMessage(getText("budget.reappropriation.existing.saved") + misc.getSequenceNumber());
        if (reAppForNewBudgetCreated)
            addActionMessage(getText("budget.reappropriation.new.saved") + misc.getSequenceNumber());
        clearFields();
        return misc;
    }

    private Integer getUserId() {
        Integer userId = null;
        if (null != parameters.get("approverUserId") && Integer.valueOf(parameters.get("approverUserId")[0]) != -1)
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        else
            userId = ApplicationThreadLocals.getUserId().intValue();
        return userId;
    }

    protected BudgetReAppropriationMisc createBudgetReAppropriationMisc(final String actionName) {
        final Budget budget = new Budget();
        budget.setIsbere(beRe);
        budget.setFinancialYear(financialYear);
        final BudgetDetail budgetDetail = new BudgetDetail();
        budgetDetail.setBudget(budget);
        return budgetReAppropriationService.createReAppropriationMisc(actionName, appropriationMisc, budgetDetail, getPosition());
    }

    public void setBudgetDetailService(final BudgetDetailService budgetDetailService) {
        this.budgetDetailService = budgetDetailService;
    }

    protected Position getPosition() {
        try {
            return eisCommonService.getPositionByUserId(ApplicationThreadLocals.getUserId());
        } catch (final ValidationException e) {
            throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getMessage(),
                    e.getErrors().get(0).getMessage())));
        } catch (final Exception e) {
            throw new ValidationException(Arrays.asList(new ValidationError("Do transaction with proper user",
                    "Do transaction with proper user")));
        }
    }

    public void removeEmptyReAppropriation(final List<BudgetReAppropriationView> reAppropriationList) {
        for (final Iterator<BudgetReAppropriationView> detail = reAppropriationList.iterator(); detail.hasNext();)
            if (detail.next() == null)
                detail.remove();
    }

    protected String getFinalStatus() {
        return appConfigValuesService.getConfigValuesByModuleAndKey(Constants.EGF, "budget_final_approval_status").get(0)
                .getValue();
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/budget/budgetReAppropriation-loadActuals")
    public String loadActuals() {
        removeEmptyReAppropriation(budgetReAppropriationList);
        removeEmptyReAppropriation(newBudgetReAppropriationList);
        if (budgetReAppropriationService.rowsToAddForExistingDetails(budgetReAppropriationList))
            loadData(budgetReAppropriationList);
        if (budgetReAppropriationService.rowsToAddExists(newBudgetReAppropriationList))
            loadData(newBudgetReAppropriationList);
        return NEW;
    }

    private void loadData(final List<BudgetReAppropriationView> reAppList) {
        budgetReAppropriationService.validateMandatoryFields(reAppList);
        for (final BudgetReAppropriationView entry : reAppList) {
            entry.setBudgetDetail(budgetReAppropriationService.setRelatedValues(entry.getBudgetDetail()));
            final List<BudgetDetail> detailList = budgetDetailService.searchByCriteriaWithTypeAndFY(financialYear.getId(), beRe,
                    entry.getBudgetDetail());
            if (detailList.size() == 1) {
                final BudgetDetail budgetDetail = detailList.get(0);
                final Map<String, Object> paramMap = budgetDetailHelper.constructParamMap(getValueStack(), budgetDetail);
                paramMap.put(Constants.ASONDATE, appropriationMisc.getReAppropriationDate());
                entry.setActuals(budgetDetailHelper.getTotalActualsFor(paramMap, appropriationMisc.getReAppropriationDate()));
                entry.setApprovedAmount(budgetDetail.getApprovedAmount());
                // this is total of reappropriated amount
                entry.setAppropriatedAmount(budgetDetail.getApprovedReAppropriationsTotal());
                entry.setAvailableAmount(entry.getApprovedAmount().add(entry.getAppropriatedAmount())
                        .subtract(entry.getActuals()));
                if (budgetDetail.getPlanningPercent() == null)
                    // TODO change the planningPercentage to planningPercent
                    entry.setPlanningPercent(BigDecimal.ZERO);
                else
                    entry.setPlanningPercent(budgetDetail.getPlanningPercent());

                if (budgetDetail.getPlanningPercent() == BigDecimal.ZERO)
                    entry.setPlanningBudgetApproved(entry.getApprovedAmount().add(entry.getAppropriatedAmount()));
                else
                    // TODO put the division after the multiplication
                    entry.setPlanningBudgetApproved(entry.getApprovedAmount().add(entry.getAppropriatedAmount())
                            .multiply(entry.getPlanningPercent()).divide(new BigDecimal(String.valueOf(100))));
                entry.setPlanningBudgetUsage(budgetDetailsDAO.getPlanningBudgetUsage(budgetDetail));
                entry.setPlanningBudgetAvailable(entry.getPlanningBudgetApproved().subtract(entry.getPlanningBudgetUsage()));
                // entry.setActuals(entry.getActuals().add(budgetDetailHelper.getBillAmountForBudgetCheck(paramMap)));
            }
        }
    }

    @Action(value = "/budget/budgetReAppropriation-ajaxLoadBeRe")
    public String ajaxLoadBeRe() {
        if (parameters.get("id") != null) {
            final Long id = Long.valueOf(parameters.get("id")[0]);
            if (id != 0L && budgetService.hasApprovedReForYear(id))
                beRe = Constants.RE;
            else
                beRe = Constants.BE;
            dropdownData.put("budgetList", getApprovedBudgetsForFY(id, finalStatus));
        }
        return BERE;
    }

    protected ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    List getFinancialYearDropDown() {
        List<Long> ids = new ArrayList<Long>();
        ids = (List<Long>) persistenceService
                .findAllBy(
                        "select distinct financialYear.id from Budget where isActiveBudget=true and isPrimaryBudget=true and status.code='Approved'");
        Query query;
        if (!ids.isEmpty()) {
            query = persistenceService.getSession()
                    .createQuery("from CFinancialYear where id in (:ids) order by finYearRange desc")
                    .setParameterList("ids", ids);
            return query.list();
        }
        return new ArrayList();
    }

    protected List getApprovedBudgetsForFY(final Long id, final String finalStatus) {
        if (id != null && id != 0L)
            return budgetService
                    .findAllBy(
                            "from Budget where id not in (select parent from Budget where parent is not null) and isactivebudget = true and status.moduletype='BUDGET' and status.code='"
                                    + finalStatus + "' and financialYear.id=? and isbere=? order by name",
                            id, beRe);
        return new ArrayList();
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    @Action(value = "/budget/budgetReAppropriation-beforeSearch")
    public String beforeSearch() {
        return "search";
    }

    @SkipValidation
    @Action(value = "/budget/budgetReAppropriation-search")
    public String search() {
        String sql = " ba.budgetDetail.budget.financialYear=" + financialYear.getId() + " and ba.budgetDetail.budget.isbere='"
                + budgetDetail.getBudget().getIsbere() + "' ";
        if (budgetDetail.getFund().getId() != null && budgetDetail.getFund().getId() != 0)
            sql = sql + " and ba.budgetDetail.fund=" + budgetDetail.getFund().getId();
        if (budgetDetail.getExecutingDepartment() != null && budgetDetail.getExecutingDepartment().getId() != 0)
            sql = sql + " and ba.budgetDetail.executingDepartment=" + budgetDetail.getExecutingDepartment().getId();
        if (budgetDetail.getFunction() != null && budgetDetail.getFunction().getId() != 0)
            sql = sql + " and ba.budgetDetail.function=" + budgetDetail.getFunction().getId();
        if (budgetDetail.getFunctionary() != null && budgetDetail.getFunctionary().getId() != 0)
            sql = sql + " and ba.budgetDetail.functionary=" + budgetDetail.getFunctionary().getId();
        if (budgetDetail.getScheme() != null && budgetDetail.getScheme().getId() != 0)
            sql = sql + " and ba.budgetDetail.scheme=" + budgetDetail.getScheme().getId();
        if (budgetDetail.getSubScheme() != null && budgetDetail.getSubScheme().getId() != 0)
            sql = sql + " and ba.budgetDetail.subScheme=" + budgetDetail.getSubScheme().getId();
        if (budgetDetail.getBoundary() != null && budgetDetail.getBoundary().getId() != 0)
            sql = sql + " and ba.budgetDetail.boundary=" + budgetDetail.getBoundary().getId();
        if (budgetDetail.getBudgetGroup().getId() != null && budgetDetail.getBudgetGroup().getId() != 0)
            sql = sql + " and ba.budgetDetail.budgetGroup=" + budgetDetail.getBudgetGroup().getId();
        if (type.equals("A"))
            sql = sql + " and ba.additionAmount is not null and ba.additionAmount!=0 ";
        else if (type.equals("R"))
            sql = sql + " and ba.deductionAmount is not null and ba.deductionAmount!=0 ";

        if (LOGGER.isInfoEnabled())
            LOGGER.info("search query==" + sql);
        reAppropriationList = getPersistenceService()
                .findAllBy(
                        " from BudgetReAppropriation ba where ba.status.code='Approved' and "
                                + sql
                                + " order by ba.budgetDetail.fund,ba.budgetDetail.executingDepartment,ba.budgetDetail.function,ba.reAppropriationMisc.sequenceNumber");
        return "search";
    }

    public void transition(final String actionName, final BudgetReAppropriation detail, final String comment) {
        budgetReAppropriationWorkflowService.transition(actionName, detail, comment);
    }

    public List<BudgetReAppropriation> getReAppropriationList() {
        return reAppropriationList;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setReAppropriationList(final List<BudgetReAppropriation> reAppropriationList) {
        this.reAppropriationList = reAppropriationList;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setBudgetReAppropriationWorkflowService(
            final WorkflowService<BudgetReAppropriation> budgetReAppropriationWorkflowService) {
        this.budgetReAppropriationWorkflowService = budgetReAppropriationWorkflowService;
    }

    public BudgetDetailsDAO getBudgetDetailsDAO() {
        return budgetDetailsDAO;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

}