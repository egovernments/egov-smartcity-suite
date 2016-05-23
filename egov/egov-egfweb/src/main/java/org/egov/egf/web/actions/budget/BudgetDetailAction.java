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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.egf.model.BudgetAmountView;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ParentPackage("egov")

@Results({
    @Result(name = "new-re", location = "budgetDetail-new-re.jsp"),
    @Result(name = "budgets", location = "budgetDetail-budgets.jsp"),
    @Result(name = "functions", location = "budgetDetail-functions.jsp"),
    @Result(name = "budgetGroup", location = "budgetDetail-budgetGroup.jsp"),
    @Result(name = "AJAX_RESULT", type = "stream", location = "returnStream", params = { "contentType", "text/plain" })
})
public class BudgetDetailAction extends BaseBudgetDetailAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final long serialVersionUID = 1L;

    private static final String ACTIONNAME = "actionName";
    private Budget topBudget;
    private Map<Long, BigDecimal> beNextYearAmounts = new HashMap<Long, BigDecimal>();
    private static Logger LOGGER = Logger.getLogger(BudgetDetailAction.class);
    String streamResult = "";
    private Long function;
    private Long budgetGroups;
    List<CFunction> functionList = Collections.EMPTY_LIST;
    List<BudgetGroup> budgetGroupList = Collections.EMPTY_LIST;

    public void setBudgetGroupList(final List budgetGroupList) {
        this.budgetGroupList = budgetGroupList;
    }

    /**
     * @return the streamResult
     */
    public InputStream getReturnStream() {
        final ByteArrayInputStream is = new ByteArrayInputStream(streamResult.getBytes());
        return is;
    }

    public BudgetDetailAction(final BudgetDetailConfig budgetDetailConfig) {
        super(budgetDetailConfig);
    }

    @Override
    
    protected void saveAndStartWorkFlow(final BudgetDetail detail) {
        try {
            if (budgetDocumentNumber != null && budgetDetail.getBudget() != null) {
                final Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
                b.setDocumentNumber(budgetDocumentNumber);
                budgetService.persist(b);
                persistenceService.getSession().flush();
            }
            final BudgetDetail persist = budgetDetailService.createBudgetDetail(detail, getPosition(), getPersistenceService());
            populateSavedbudgetDetailListFor(persist.getBudget());
            headerDisabled = true;
        } catch (final ValidationException e) {
            LOGGER.error("Duplication in budget details" + e.getMessage(), e);
            handleDuplicateBudgetDetailError(e);
            populateSavedbudgetDetailListFor(budgetDetail.getBudget());
        }
    }

    protected void handleDuplicateBudgetDetailError(final ValidationException e) {
        for (final ValidationError error : e.getErrors())
            if ("budgetDetail.duplicate".equals(error.getKey())) {
                headerDisabled = true;
                break;
            }
        throw e;
    }

    @Override
    public void populateSavedbudgetDetailListFor(final Budget budget) {
        if (budget != null)
            savedbudgetDetailList = budgetDetailService.findAllBy(
                    "from BudgetDetail where budget=? order by function.name,budgetGroup.name", budget);
    }

    @Override
    public void populateSavedbudgetDetailListForDetail(final BudgetDetail bd) {
        if (bd != null)
            // find all RE for the functin
            savedbudgetDetailList = budgetDetailService.findAllBy(
                    "from BudgetDetail where budget=? and function=? order by function.name,budgetGroup.name", bd.getBudget(),
                    bd.getFunction());
        // find all next year be for the function
        savedbudgetDetailList
        .addAll(budgetDetailService
                .findAllBy(
                        "from BudgetDetail where budget=(select bd from Budget bd where referenceBudget=?) and function=? order by function.name,budgetGroup.name",
                        bd.getBudget(), bd.getFunction()));
    }

    public String ajaxLoadBudgetDetailList() {
        final Long id = (Long) request.get("id");
        if (!Long.valueOf(0).equals(id)) {
            savedbudgetDetailList = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?", id);
            final Budget budget = budgetService.findById(id, false);
            re = budgetService.hasReForYear(budget.getFinancialYear().getId());
            budgetDetail.setBudget(budget);
            setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
            budgetDocumentNumber = budget.getDocumentNumber();
        }
        populateBeNextYearAmounts();
        populateFinancialYear();
        return Constants.SAVED_DATA;
    }

    @SkipValidation
    public String loadBudgetDetailList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Initiating load budgets .....");
        final Long id = budgetDetail.getBudget().getId();
        showRe = true;
        getDetailsFilterdBy();
        final Budget budget = budgetService.findById(id, false);
        re = budgetService.hasReForYear(budget.getFinancialYear().getId());
        budgetDetail.setBudget(budget);
        setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
        budgetDocumentNumber = budget.getDocumentNumber();
        budgetAmountView = new ArrayList<BudgetAmountView>(savedbudgetDetailList.size());
        for (int i = 0; i < savedbudgetDetailList.size(); i++)
            budgetAmountView.add(new BudgetAmountView());
        budgetDetailList = savedbudgetDetailList;
        populateBeNextYearAmountsAndBEAmounts();
        populateFinancialYear();
        // loadAjaxedFunctionAndBudgetGroup();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Budgets Loadded Succesfully");
        showDetails = true;
        return "new-re";

    }

    @SuppressWarnings("unchecked")
    private void loadAjaxedFunctionAndBudgetGroup() {
        if (budgetDetail.getBudget() != null)
        {
            String sqlStr = "select distinct (f.name)  as name,f.id as id   from function f,egf_budgetdetail bd where  f.id=bd.function and bd.budget="
                    + budgetDetail.getBudget().getId() + "  order  by f.name";
            SQLQuery sqlQuery = persistenceService.getSession().createSQLQuery(sqlStr);
            sqlQuery.addScalar("name")
            .addScalar("id", LongType.INSTANCE)
            .setResultTransformer(Transformers.aliasToBean(CFunction.class));
            functionList = sqlQuery.list();
            sqlStr = "select  distinct (bg.name) as name ,bg.id  as id from egf_budgetgroup bg,egf_budgetdetail  bd where  bg.id=bd.budgetgroup and bd.budget="
                    + budgetDetail.getBudget().getId() + "  order  by bg.name";
            sqlQuery = persistenceService.getSession().createSQLQuery(sqlStr);
            sqlQuery.addScalar("name")
            .addScalar("id", LongType.INSTANCE)
            .setResultTransformer(Transformers.aliasToBean(BudgetGroup.class));
            budgetGroupList = sqlQuery.list();
        }

    }

    @Action(value = "/budget/budgetDetail-ajaxLoadBudgets")
    public String ajaxLoadBudgets() {
        final String bere = parameters.get("bere")[0];
        loadBudgets(bere);
        return "budgets";
    }

    @Action(value = "/budget/budgetDetail-ajaxLoadFunctions")
    public String ajaxLoadFunctions() {
        final Long id = (Long) request.get("id");
        final String sqlStr = "select distinct (f.name)  as name,f.id as id   from function f,egf_budgetdetail bd where  f.id=bd.function and bd.budget="
                + id + "  order  by f.name";
        final SQLQuery sqlQuery = persistenceService.getSession().createSQLQuery(sqlStr);
        sqlQuery.addScalar("name")
        .addScalar("id", LongType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(CFunction.class));
        functionList = sqlQuery.list();
        return "functions";
    }

    @Action(value = "/budget/budgetDetail-ajaxLoadBudgetGroups")
    public String ajaxLoadBudgetGroups() {
        final Long id = (Long) request.get("id");
        final String sqlStr = "select  distinct (bg.name) as name ,bg.id  as id from egf_budgetgroup bg,egf_budgetdetail  bd where  bg.id=bd.budgetgroup and bd.budget="
                + id + "  order  by bg.name";
        final SQLQuery sqlQuery = persistenceService.getSession().createSQLQuery(sqlStr);
        sqlQuery.addScalar("name")
        .addScalar("id", LongType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(BudgetGroup.class));
        budgetGroupList = sqlQuery.list();
        return "budgetGroup";
    }

    
    public String saveAndNew() {
        return create();
    }

    
    public String saveAndNewRe() {
        return createRe();
    }

    @Override
    public void prepare() {
        super.prepare();
        populateSavedbudgetDetailListFor(budgetDetail.getBudget());
        if (parameters.containsKey("re"))
            dropdownData.put("budgetList", Collections.EMPTY_LIST);
        loadAjaxedFunctionAndBudgetGroup();
    }

    @Override
    public boolean isShowMessage() {
        return super.isShowMessage();
    }

    public String getActionMessage() {
        if (getActionMessages() != null && getActionMessages().iterator() != null
                && getActionMessages().iterator().next() != null)
            return getActionMessages().iterator().next().toString();
        else
            return "";
    }

    private void populateBeNextYearAmounts() {
        if (savedbudgetDetailList == null || savedbudgetDetailList.size() == 0)
            return;
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
        if (referenceBudgetFor != null) {
            final List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",
                    referenceBudgetFor.getId());
            for (final BudgetDetail budgetDetail : savedbudgetDetailList)
                for (final BudgetDetail row : result)
                    if (compareDetails(row, budgetDetail))
                        beNextYearAmounts.put(budgetDetail.getId(), row.getOriginalAmount().setScale(2));
        }
    }

    private void populateBeNextYearAmountsAndBEAmounts() {
        if (savedbudgetDetailList == null || savedbudgetDetailList.size() == 0)
            return;
        beAmounts = new ArrayList<BigDecimal>(savedbudgetDetailList.size());
        final Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
        if (referenceBudgetFor != null) {
            final List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",
                    referenceBudgetFor.getId());
            for (final BudgetDetail budgetDetail : savedbudgetDetailList)
                for (final BudgetDetail row : result)
                    if (compareDetails(row, budgetDetail)) {
                        beNextYearAmounts.put(budgetDetail.getId(), row.getOriginalAmount().setScale(2));
                        beAmounts.add(row.getOriginalAmount());
                    }
        }
    }

    @Override
    
    protected void saveAndStartWorkFlowForRe(final BudgetDetail detail, final int index, final CFinancialYear finYear,
            final Budget refBudget) {
        try {
            if (budgetDocumentNumber != null && budgetDetail.getBudget() != null) {
                final Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
                b.setDocumentNumber(budgetDocumentNumber);
                budgetService.persist(b);
                persistenceService.getSession().flush();
            }
            detail.getBudget().setFinancialYear(finYear);
            final BudgetDetail reCurrentYear = budgetDetailService.createBudgetDetail(detail, getPosition(),
                    getPersistenceService());

            headerDisabled = true;
            BudgetDetail beNextYear = new BudgetDetail();
            // beNextYear=detail.clone();
            beNextYear.copyFrom(detail);
            beNextYear.setBudget(refBudget);
            beNextYear.setOriginalAmount(beAmounts.get(index));
            beNextYear.setDocumentNumber(detail.getDocumentNumber());
            beNextYear.setAnticipatoryAmount(reCurrentYear.getAnticipatoryAmount());
            beNextYear = budgetDetailService.createBudgetDetail(beNextYear, getPosition(), getPersistenceService());
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            populateBeNextYearAmounts();
            handleDuplicateBudgetDetailError(e);
            populateSavedbudgetDetailListFor(budgetDetail.getBudget());
            throw e;
        }
    }

    @Override
    public void approve() {
        topBudget = savedbudgetDetailList.get(0).getBudget();
        setTopBudget(topBudget);
        String budgetComment="";
        if (parameters.get("budget.comments") != null){
            budgetComment = parameters.get("budget.comments")[0];
        }
        Integer userId = null;
        if (parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject"))
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        else if (null != parameters.get("approverUserId") && Integer.valueOf(parameters.get("approverUserId")[0]) != -1)
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        else
            userId = ApplicationThreadLocals.getUserId().intValue();

        for (final BudgetDetail detail : savedbudgetDetailList)
            // budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, detail.getComment());
            if (new String("forward").equals(parameters.get(ACTIONNAME)[0]))
                detail.transition(true).withStateValue("Forwarded by " + getPosition().getName())
                .withOwner(getPositionByUserId(userId)).withComments(detail.getComment());
        // forwardBudget(budgetComment, userId); //for RE
        setTopBudget(budgetService.getReferenceBudgetFor(topBudget));
        // forwardBudget(budgetComment, userId); //for BE

        if (parameters.get("actionName")[0].contains("approve")) {
            if (topBudget.getState().getValue().equals("END"))
                addActionMessage(getMessage("budgetdetail.approved.end"));
            else
                addActionMessage(getMessage("budgetdetail.approved")
                        + budgetService.getEmployeeNameAndDesignationForPosition(topBudget.getState().getOwnerPosition()));
        }
        else
            addActionMessage(getMessage("budgetdetail.approved")
                    + budgetService.getEmployeeNameAndDesignationForPosition(topBudget.getState().getOwnerPosition()));
    }

    @Action(value = "/budget/budgetDetail-newRe")
    public String newRe() {
        showRe = true;
        // setFinancialYear(null);
        return "new-re";
    }

    @ValidationErrorPage(value = "new-re")
    @Action(value = "/budget/budgetDetail-loadActualsForRe")
    public String loadActualsForRe() {
        showRe = true;
        try {
            loadActuals();
            showDetails = true;
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            populateBudgetList();
            throw e;
        }
        populateBudgetList();
        return "new-re";
    }

    public void setShowRe(final boolean showRe) {
        this.showRe = showRe;
    }

    public boolean isShowRe() {
        return showRe;
    }

    protected String getMessage(final String key) {
        return getText(key);
    }

    public Budget getTopBudget()
    {
        return topBudget;
    }

    public void setTopBudget(final Budget topBudget)
    {
        this.topBudget = topBudget;
    }

    public void setBeNextYearAmounts(final Map<Long, BigDecimal> beNextYearAmounts) {
        this.beNextYearAmounts = beNextYearAmounts;
    }

    public Map<Long, BigDecimal> getBeNextYearAmounts() {
        return beNextYearAmounts;
    }

    public List getFunctionList() {
        return functionList;
    }

    public void setFunctionList(final List functionList) {
        this.functionList = functionList;
    }

    public List getBudgetGroupList() {
        return budgetGroupList;
    }

    public Long getFunction() {
        return function;
    }

    public void setFunction(final Long function) {
        this.function = function;
    }

    public Long getBudgetGroups() {
        return budgetGroups;
    }

    public void setBudgetGroups(final Long budgetGroups) {
        this.budgetGroups = budgetGroups;
    }

}