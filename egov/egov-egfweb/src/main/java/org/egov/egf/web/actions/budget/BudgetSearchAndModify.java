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

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.egf.model.BudgetAmountView;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetSearchAndModify extends BudgetSearchAction {
    private static final String ACTIONNAME = "actionName";
    boolean enableApprovedAmount = false;
    boolean enableOriginalAmount = false;
    boolean consolidatedScreen = false;
    private String comments = "";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BudgetSearchAndModify.class);
    protected WorkflowService<Budget> budgetWorkflowService;
    private boolean showDetails = false;
    private boolean isDetailByFunction;
    private ScriptService scriptService;
    @Autowired
    private EgovMasterDataCaching masterDataCache;

    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(final ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    public String modifyList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting modifyList...");
        if (parameters.containsKey(Constants.MODE) && "approve".equals(parameters.get(Constants.MODE)[0])) {
            setMode(parameters.get(Constants.MODE)[0]);
            isApproveAction = true;
            disableBudget = true;
            enableApprovedAmount = true;
            // enableOriginalAmount=true;
        }
        if (getMode() != null && Constants.APPROVE.equals(getMode()))
            budgetDetailApproveList();
        else
            budgetDetailList();
        // setEnablingAmounts();
        currentfinYearRange = topBudget.getFinancialYear().getFinYearRange();
        computePreviousYearRange();
        computeTwopreviousYearRange();
        computeNextYearRange();
        // isDetailByFunction=true;
        // setConsolidatedScreen(budgetRenderService.getConsolidateBudget());
        populateNextYrBEinBudgetDetailList();
        loadApproverUser(savedbudgetDetailList);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Completed modifyList...");
        return Constants.DETAILLIST;
    }

    /*
     * this api is used fro budget detail workflow list
     */
    public String modifyDetailList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("starting modifyDetailList...");
        if (parameters.containsKey(Constants.MODE) && "approve".equals(parameters.get(Constants.MODE)[0])) {
            setMode(parameters.get(Constants.MODE)[0]);
            isApproveAction = true;
            disableBudget = true;
        }
        if (getMode() != null && Constants.APPROVE.equals(getMode()))
            budgetDetailApprove();
        else
            budgetDetailList();
        setEnablingAmounts();
        currentfinYearRange = topBudget.getFinancialYear().getFinYearRange();
        computePreviousYearRange();
        computeTwopreviousYearRange();
        computeNextYearRange();
        // setConsolidatedScreen(budgetRenderService.getConsolidateBudget());
        isDetailByFunction = true;
        populateNextYrBEinBudgetDetailList();
        loadApproverUser(savedbudgetDetailList);
        // return Constants.DETAILLIST;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("completed modifyDetailList");
        return Constants.DETAILLIST;
    }

    public boolean isDetailByFunction() {
        return isDetailByFunction;
    }

    public void setDetailByFunction(final boolean isDetailByFunction) {
        this.isDetailByFunction = isDetailByFunction;
    }

    public String budgetDetailApprove() {

        if (parameters.get("budgetDetail.id")[0] != null) {

            budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where id=?",
                    Long.valueOf(parameters.get("budgetDetail.id")[0]));
            setTopBudget(budgetDetail.getBudget());
            comments = topBudget.getState().getExtraInfo();
        }
        // if u want only selected function centre filter here by owner
        final String query = " from BudgetDetail bd where bd.budget=? and bd.function=" + budgetDetail.getFunction().getId()
                + "  order by bd.function.name,bd.budgetGroup.name";
        savedbudgetDetailList = budgetDetailService.findAllBy(query, topBudget);
        re = checkRe(topBudget);
        // check what actuals needs to be shown for next year be AND possible remove if
        if (LOGGER.isInfoEnabled())
            LOGGER.info("starting populateActualData...");
        if ("BE".equalsIgnoreCase(topBudget.getIsbere())) {
            budgetDetailHelper.getPreviousYearFor(topBudget.getFinancialYear());
            populateActualData(topBudget.getFinancialYear());
        } else
            populateActualData(topBudget.getFinancialYear());
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Completed populateActualData...");
        computeActualAmounts(savedbudgetDetailList);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("finished loading detail List--------------------------------------------------------------");
        return Constants.DETAILLIST;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (parameters.containsKey("action") && Constants.MODIFY.equals(parameters.get("action")[0]))
            if (budgetDetail.getId() != null) {

            } else
                for (int i = 0; i < savedbudgetDetailList.size(); i++)
                    savedbudgetDetailList.set(i, budgetDetailService.findById(savedbudgetDetailList.get(i).getId(), false));
        if (isApproveMode())
            dropdownData.put("budgetList", budgetDetailService.findBudgetsForFY(getFinancialYear()));
        else
            dropdownData.put("budgetList", budgetDetailService.findBudgetsForFYWithNewState(getFinancialYear()));
        addDropdownData("departmentList", masterDataCache.get("egi-department"));
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
    }

    public String update() {
        Budget budget = null;
        Budget b = null;
        if (parameters.get("budget.id") != null) {
            budget = budgetService.find(" from Budget where id=?", Long.valueOf(parameters.get("budget.id")[0]));
            setTopBudget(budget);
        }
        if ("forward".equalsIgnoreCase(parameters.get("actionName")[0]) || parameters.get("actionName")[0].contains("approve"))
            // mode
            // is
            // approve
            // move
            // the
            // object
            approve();
        else {           // if not approve then only update
            for (final BudgetDetail detail : savedbudgetDetailList) {
                validateAmount(detail);
                if (consolidatedScreen)
                    detail.setApprovedAmount(detail.getApprovedAmount().multiply(BigDecimal.valueOf(1000)));
                budgetDetailService.persist(detail);
                b = detail.getBudget();
            }
            if (b != null && b.getId() != null) {
                b = budgetService.find("from Budget where id=?", b.getId());
                if (b.getCurrentState() != null)
                    // need to fix phoenix migration b.getCurrentState().setText1(comments);
                    budgetService.persist(b);
            }
            addActionMessage(getMessage("budgetdetail.updated"));
        }
        setBudgetDetail((BudgetDetail) session().get(Constants.SEARCH_CRITERIA_KEY));
        // Report block
        {
            final Long count = (Long) persistenceService.find("select count(*) from Budget where materializedPath like ?",
                    topBudget.getMaterializedPath() + ".%");
            // if()
            // financialYear=topBudget.getFinancialYear();
            if (count == 0) {
                final BudgetDetail detail = (BudgetDetail) persistenceService.find(
                        "from BudgetDetail where materializedPath like ?",
                        topBudget.getMaterializedPath() + ".%");
                if (detail != null)
                    department = detail.getExecutingDepartment();
            }
            showDetails = true;
        }

        return setUpDataForList();
    }

    private void validateAmount(final BudgetDetail detail) {
        setEnablingAmounts();
        if (consolidatedScreen)
            if (enableApprovedAmount
                    && (detail.getApprovedAmount() == null || (BigDecimal.ZERO.compareTo(detail.getApprovedAmount())) == 0)) {
                loadApproverUser(savedbudgetDetailList);
                throw new ValidationException(Arrays.asList(new ValidationError("approved.amount.mandatory",
                        "approved.amount.mandatory")));
            }
        if (enableOriginalAmount
                && (detail.getOriginalAmount() == null || (BigDecimal.ZERO.compareTo(detail.getOriginalAmount())) == 0)) {
            loadApproverUser(savedbudgetDetailList);
            throw new ValidationException(Arrays.asList(new ValidationError("original.amount.mandatory",
                    "original.amount.mandatory")));
        }

    }

    public String setUpDataForList() {
        if (financialYear == null && getSession().get(Constants.FINANCIALYEARID) != null)
            financialYear = (Long) getSession().get(Constants.FINANCIALYEARID);
        dropdownData.put("budgetList",
                budgetDetailService.findBudgetsForFYWithNewState(financialYear == null ? getFinancialYear() : financialYear));
        return Constants.LIST;
    }

    private boolean isApproveMode() {
        return parameters.containsKey(Constants.MODE) && Constants.APPROVE.equals(parameters.get(Constants.MODE)[0]);
    }

    /**
     * move the budget detail and its parents depending on save or approve
     * @return
     */
    public void approve() {
        Integer userId = null;
        if (parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject"))
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        else if (null != parameters.get("approverUserId") && Integer.valueOf(parameters.get("approverUserId")[0]) != -1)
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        else
            userId = ApplicationThreadLocals.getUserId().intValue();

        final Position positionByUserId = eisCommonService.getPositionByUserId(userId.longValue());
        final PersonalInformation empForCurrentUser = budgetDetailService.getEmpForCurrentUser();
        String name = "";
        if (empForCurrentUser != null)
            name = empForCurrentUser.getName();
        if (name == null)
            name = empForCurrentUser.getEmployeeFirstName();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("===============Processing " + savedbudgetDetailList.size() + "Budget line items");
        if (parameters.get("actionName")[0].contains("approv"))
            for (final BudgetDetail detail : savedbudgetDetailList) {

                validateAmount(detail);
                if (consolidatedScreen)
                    detail.setApprovedAmount(detail.getApprovedAmount().multiply(BigDecimal.valueOf(1000)));
                final String comment = detail.getState() == null ? "" : detail.getState().getExtraInfo();

                detail.transition().progressWithStateCopy().withStateValue("END").withOwner(positionByUserId).withComments(comment);
                budgetDetailService.persist(detail);
                final BudgetDetail detailBE = (BudgetDetail) persistenceService.find("from BudgetDetail where id=?",
                        detail.getNextYrId());
                if (consolidatedScreen)
                    detailBE.setApprovedAmount(detail.getNextYrapprovedAmount().multiply(BigDecimal.valueOf(1000)));
                else
                    detailBE.setApprovedAmount(detail.getNextYrapprovedAmount());
                detailBE.transition().progressWithStateCopy().withStateValue("END").withOwner(getPosition()).withComments(comment);
                budgetDetailService.persist(detailBE);

                // detail.getNextYearBEProposed
                // detail.getNextYearBEFixed
                // detailBE.changeState("END", positionByUserId, comment);

                // budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, comment);
            }
        else
            for (final BudgetDetail detail : savedbudgetDetailList) {

                validateAmount(detail);
                if (consolidatedScreen)
                    detail.setApprovedAmount(detail.getApprovedAmount().multiply(BigDecimal.valueOf(1000)));
                final String comment = detail.getState() == null ? "" : detail.getState().getExtraInfo();

                detail.transition().progressWithStateCopy().withStateValue("Forwarded by " + name).withOwner(positionByUserId).withComments(comment);
                budgetDetailService.persist(detail);
                final BudgetDetail detailBE = (BudgetDetail) persistenceService.find("from BudgetDetail where id=?",
                        detail.getNextYrId());
                // detailBE.setOriginalAmount(detail.getOriginalAmount());
                if (consolidatedScreen)
                    detailBE.setApprovedAmount(detail.getNextYrapprovedAmount().multiply(BigDecimal.valueOf(1000)));
                else
                    detailBE.setApprovedAmount(detail.getNextYrapprovedAmount());
                detailBE.transition().progressWithStateCopy().withStateValue("Forwarded by " + name).withOwner(positionByUserId)
                        .withComments(comment);
                budgetDetailService.persist(detailBE);
                // budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, comment);
            }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Processed Budget line items");
        // if budget is not forwarded yet send the budget else ignore
        if (getTopBudget().getState().getOwnerPosition() != null
                && getTopBudget().getState().getOwnerPosition().getId() != positionByUserId.getId())
            getTopBudget().transition().progressWithStateCopy().withStateValue("Forwarded by " + name).withOwner(positionByUserId)
                    .withComments(comments);
        // add logic for BE approval also
        final Budget beBudget = budgetService.find("from Budget where referenceBudget=?", getTopBudget());
        if (beBudget.getState().getOwnerPosition() != null
                && beBudget.getState().getOwnerPosition().getId() != positionByUserId.getId())
            beBudget.transition().progressWithStateCopy().withStateValue("Forwarded by " + name).withOwner(positionByUserId).withComments(comments);

        // budgetWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, getTopBudget(),comments);

        if (parameters.get("actionName")[0].contains("approv")) {
            if (topBudget.getState().getValue().equals("END"))
                addActionMessage(getMessage("budgetdetail.approved.end"));
            else
                addActionMessage(getMessage("budgetdetail.approved")
                        + budgetService.getEmployeeNameAndDesignationForPosition(positionByUserId));
        } else
            addActionMessage(getMessage("budgetdetail.approved")
                    + budgetService.getEmployeeNameAndDesignationForPosition(positionByUserId));
        showButton = false;
    }

    public String ajaxDeleteBudgetDetail() {
        final Long id = Long.valueOf(parameters.get("id")[0]);
        final BudgetDetail detail = budgetDetailService.findById(id, false);
        budgetDetailService.delete(detail);
        final BudgetDetail criteria = (BudgetDetail) getSession().get(Constants.SEARCH_CRITERIA_KEY);
        savedbudgetDetailList = budgetDetailService.searchBy(criteria);
        return Constants.MODIFYLIST;
    }

    public String budgetDetailApproveList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting budgetDetailApproveList...");
        isApproveAction = true;
        consolidatedScreen = budgetDetailService.toBeConsolidated();
        // if(LOGGER.isInfoEnabled()) LOGGER.info("Budget.id "+parameters.get("budget.id")[0]);
        if (parameters.get("budget.id") != null && parameters.get("budget.id")[0] != null) {
            topBudget = budgetService.findById(Long.valueOf(parameters.get("budget.id")[0]), false);

            comments = topBudget.getState().getExtraInfo();
        } else if (parameters.get("budgetDetail.budget.id")[0] != null)
            topBudget = budgetService.findById(Long.valueOf(parameters.get("budgetDetail.budget.id")[0]), false);
        comments = topBudget.getState().getExtraInfo();
        // budgetDetail=budgetDetailService.find("from BudgetDetail where budget=?",topBudget);
        savedbudgetDetailList = getAllApprovedBudgetDetails(topBudget);
        if (savedbudgetDetailList.size() > 0)
            budgetDetail = savedbudgetDetailList.get(0);
        // budgetDetailService.findAllBudgetDetailsWithReAppropriation(topBudget, criteria);
        re = checkRe(topBudget);
        populateActualData(topBudget.getFinancialYear());
        computeActualAmounts(savedbudgetDetailList);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished budgetDetailApproveList");
        return Constants.DETAILLIST;
    }

    private List<BudgetDetail> getAllApprovedBudgetDetails(final Budget budget) {
        return budgetDetailService
                .findAllBy(
                        "from BudgetDetail where budget=? and (state.value='END' or state.owner=?) order by function.name,budgetGroup.name ",
                        budget, getPosition());
    }

    private void computeActualAmounts(final List<BudgetDetail> budgetDetails) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting computeActualAmounts .... ");
        budgetAmountView = new ArrayList<BudgetAmountView>();
        for (final BudgetDetail detail : budgetDetails) {
            final BudgetAmountView view = new BudgetAmountView();
            view.setId(detail.getId());
            final String previousYearAmount = getPreviousYearBudgetDetailIdsAndAmount().get(detail.getUniqueNo());
            view.setPreviousYearActuals(previousYearAmount == null ? BigDecimal.ZERO.setScale(2) : new BigDecimal(
                    previousYearAmount).setScale(2));
            final String twopreviousYearAmount = getTwopreviousYearBudgetDetailIdsAndAmount().get(detail.getUniqueNo());
            view.setTwopreviousYearActuals(twopreviousYearAmount == null ? BigDecimal.ZERO.setScale(2) : new BigDecimal(
                    twopreviousYearAmount).setScale(2));
            final String currentYearAmount = getBudgetDetailIdsAndAmount().get(detail.getUniqueNo());
            view.setCurrentYearBeActuals(currentYearAmount == null ? BigDecimal.ZERO.setScale(2) : new BigDecimal(
                    currentYearAmount).setScale(2));
            budgetAmountView.add(view);
            // if(LOGGER.isInfoEnabled()) LOGGER.info(view);
            if (detail.getState() != null)
                detail.setComment(detail.getState().getExtraInfo());
            final BigDecimal approvedAmt = detail.getApprovedAmount() == null ? BigDecimal.ZERO : detail.getApprovedAmount()
                    .setScale(2);
            if (re) {
                view.setCurrentYearReApproved(approvedAmt.setScale(2).toString());
                final BigDecimal approvedReAppropriationsTotal = detail.getApprovedReAppropriationsTotal();
                final BigDecimal lastBEAmount = getLastBE(detail);
                final BigDecimal total = approvedReAppropriationsTotal.add(lastBEAmount);
                view.setReappropriation(approvedReAppropriationsTotal.setScale(2).toString());
                view.setLastBEApproved(total.setScale(2).toString());
                view.setLastTotal(total.setScale(2).toString());
            } else {
                view.setCurrentYearBeApproved(approvedAmt.setScale(2).toString());
                final BigDecimal approvedReAppropriationsTotal = detail.getApprovedReAppropriationsTotal();
                final BigDecimal lastBEAmount = getLastBE(detail);
                final BigDecimal total = approvedReAppropriationsTotal.add(lastBEAmount);
                view.setReappropriation(approvedReAppropriationsTotal.toString());
                view.setLastBEApproved(lastBEAmount.toString());
                view.setLastTotal(total.setScale(2).toString());
            }
            detail.setAnticipatoryAmount(detail.getAnticipatoryAmount() == null ? BigDecimal.ZERO : detail
                    .getAnticipatoryAmount().setScale(2));
            detail.getOriginalAmount().setScale(2);
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Done computeActualAmounts ");
    }

    /**
     * @param detail
     * @return
     */
    private BigDecimal getLastBE(final BudgetDetail detail) {
        final BudgetDetail detailWithoutBudget = new BudgetDetail();
        detailWithoutBudget.copyFrom(detail);
        detailWithoutBudget.setBudget(null);
        // List<Object[]> previousYearResult;
        final CFinancialYear financialYear2 = detail.getBudget().getFinancialYear();
        Long finyearId = financialYear2.getId();
        if (detail.getBudget().getIsbere().equalsIgnoreCase("BE")) {
            final Date startingDate = financialYear2.getStartingDate();
            final Date lastyear = subtractYear(startingDate);
            final CFinancialYear lastFinYear = (CFinancialYear) persistenceService.find(
                    "from CFinancialYear where startingDate=? and isActive=true", lastyear);
            if (lastFinYear != null)
                finyearId = lastFinYear.getId();

        }
        BigDecimal approvedAmount = BigDecimal.ZERO;
        final List<BudgetDetail> budgetDetail = budgetDetailService.searchByCriteriaWithTypeAndFY(finyearId, "BE",
                detailWithoutBudget);
        if (budgetDetail != null && budgetDetail.size() > 0)
            approvedAmount = budgetDetail.get(0).getApprovedAmount();
        return approvedAmount.setScale(2);

    }

    void populateActualData(final CFinancialYear financialYear) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting populate Actual data..... ");
        List<Object[]> beforePreviousYearResult, previousYearResult;
        if (financialYear == null)
            return;
        Budget lastYearTopBudget, beforeLastYearTopBudget;
        Constants.DDMMYYYYFORMAT1.format(financialYear.getStartingDate());
        List<AppConfigValues> list = new ArrayList<AppConfigValues>();
        list = getExcludeStatusForBudget();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Starting fetchActualsForFY..... ");

        final List<Object[]> result = budgetDetailService.fetchActualsForFinYear(financialYear, mandatoryFields, topBudget, null,
                null,
                defaultDept, null, list);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Done fetchActualsForFY..... " + result.size());
        for (final Object[] row : result)
            getBudgetDetailIdsAndAmount().put(row[0].toString(), row[1].toString());
        final CFinancialYear lastFinancialYearByDate = getFinancialYearDAO().getPreviousFinancialYearByDate(
                financialYear.getStartingDate());
        final CFinancialYear beforeLastFinancialYearByDate = null;// getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
        lastYearTopBudget = budgetService.find(" from Budget where financialYear.id=? and parent is null  and isbere=?",
                lastFinancialYearByDate.getId(), topBudget.getIsbere());
        beforeLastYearTopBudget = budgetService.find("from Budget where financialYear.id=? and parent is null  and isbere=?",
                beforeLastFinancialYearByDate.getId(), topBudget.getIsbere());

        if (lastYearTopBudget != null)
            previousYearResult = budgetDetailService.fetchActualsForFinYear(lastFinancialYearByDate, mandatoryFields,
                    lastYearTopBudget, topBudget, null, defaultDept, null, list);
        else
            previousYearResult = new ArrayList<Object[]>();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished Fetching previous Year results");

        if (beforeLastYearTopBudget != null)
            beforePreviousYearResult = budgetDetailService.fetchActualsForFinYear(beforeLastFinancialYearByDate, mandatoryFields,
                    beforeLastYearTopBudget, topBudget, null, defaultDept, null, list);
        else
            beforePreviousYearResult = new ArrayList<Object[]>();

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Finished Fetching before Last Year results");
        mapBudgetDetailForPreviousYear(savedbudgetDetailList, previousYearResult, beforePreviousYearResult);

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Ending populate Actual data. ");
    }

    public void mapBudgetDetailForPreviousYear(final List<BudgetDetail> budgetdetail, final List<Object[]> previousYearList,
            final List<Object[]> beforelastYearList) {
        new BudgetDetail();
        if (previousYearList.size() > 0)
            for (final Object[] row : previousYearList)
                getPreviousYearBudgetDetailIdsAndAmount().put(row[0].toString().toString(), row[1].toString());
        if (beforelastYearList.size() > 0)
            for (final Object[] row : beforelastYearList)
                getTwopreviousYearBudgetDetailIdsAndAmount().put(row[0].toString().toString(), row[1].toString());
    }

    public boolean enableApprovedAmount() {
        return enableApprovedAmount;
    }

    public boolean enableOriginalAmount() {
        return enableOriginalAmount;
    }

    @Override
    public void setBudgetDetailWorkflowService(
            final SimpleWorkflowService<BudgetDetail> workflowService) {
        budgetDetailWorkflowService = workflowService;
    }

    public List<WorkflowAction> getValidActions() {
        List<WorkflowAction> validButtons = null;
        if (isReferenceBudget(getTopBudget())) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Budget is Reference budget hence cannot be saved to sent for approval");
        } else
            validButtons = budgetWorkflowService.getValidActions(getTopBudget());
        return validButtons;

    }

    public void populateNextYrBEinBudgetDetailList() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("starting populateNextYrBEinBudgetDetailList");
        if (!savedbudgetDetailList.isEmpty())
            for (final BudgetDetail budgetDetail : savedbudgetDetailList) {
                final BudgetDetail nextYrbudgetDetail = (BudgetDetail) persistenceService.find(
                        "from BudgetDetail where uniqueNo=? and budget.referenceBudget=?", budgetDetail.getUniqueNo(),
                        budgetDetail.getBudget());
                budgetDetail.setNextYrId(nextYrbudgetDetail.getId());
                budgetDetail.setNextYroriginalAmount(nextYrbudgetDetail.getOriginalAmount());
                budgetDetail.setNextYrapprovedAmount(nextYrbudgetDetail.getApprovedAmount());
            }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Completed populateNextYrBEinBudgetDetailList");
    }

    private void setEnablingAmounts() {
        final String value = (String) scriptService.executeScript("BudgetDetail.enable.amounts",
                ScriptService.createContext("wfItem", topBudget, "persistenceService", budgetService));
        if ("approved".equalsIgnoreCase(value))
            enableApprovedAmount = true;
        else if ("original".equalsIgnoreCase(value))
            enableOriginalAmount = true;
    }

    /**
     * reference Budget is one which exists in the system but wont be having active and primary budget as parent it is used for
     * reference . These should be filtered for approval life cycle
     * @param budget
     */

    public boolean isReferenceBudget(final Budget budget) {
        boolean isReference = false;
        if (budget == null) {
            isReference = false;
            return isReference;
        }
        final String mPath = budget.getMaterializedPath();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("meterialized path for the Budget" + mPath);
        if (mPath == null || mPath.isEmpty())
            throw new ApplicationRuntimeException("Materialized path is not set for the Budget " + budget.getName());
        else if (budget.getIsPrimaryBudget() && budget.getIsActiveBudget()) // check for root budget if yes return
        {
            isReference = false;
            return isReference;
        } else { // it should be some child
            final int start = mPath.indexOf('.');
            if (start != -1) {
                final String rootPath = mPath.substring(0, start);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("meterialized path for root the Budget" + "   " + rootPath);
                final Budget rootBudget = budgetService.find("from Budget where materializedPath=?", rootPath);
                if (rootBudget == null)
                    throw new ApplicationRuntimeException("Materialized path is incorrect please verify for " + rootPath);
                else if (rootBudget.getIsPrimaryBudget() && rootBudget.getIsActiveBudget())
                    isReference = false;
                else
                    isReference = true;
            } else
                isReference = true;    // it is not root it is not child of any budget so it is reference
        }
        return isReference;
    }

    public void setBudgetWorkflowService(final WorkflowService<Budget> budgetWorkflowService) {
        this.budgetWorkflowService = budgetWorkflowService;
    }

    /*
     * validates the comments for length 1024
     */
    @Override
    public void validate() {
        for (final BudgetDetail detail : savedbudgetDetailList)
            if (detail.getComment() != null && !detail.getComment().trim().isEmpty() && detail.getComment().length() > 1024)
                addFieldError("Comments Max Length  Exceeded BudgetDetail ", getText("budgetdetail.comments.lengthcheck"));
        if (parameters.get("budget.comments") != null && parameters.get("budget.comments")[0] != null
                && !parameters.get("budget.comments")[0].trim().isEmpty() && parameters.get("budget.comments")[0].length() > 1024)
            addFieldError("Comments Max Length  Exceeded for  Budget", getText("budget.comments.lengthcheck"));
    }

    @SkipValidation
    private void loadApproverUser(final List<BudgetDetail> budgetDetailList) {
        final Map<String, Object> map = voucherService.getDesgBYPassingWfItem("BudgetDetail.nextDesg", null, budgetDetailList
                .get(0)
                .getExecutingDepartment().getId().intValue());
        addDropdownData("departmentList", masterDataCache.get("egi-department"));

        final List<Map<String, Object>> desgList = (List<Map<String, Object>>) map.get("designationList");
        String strDesgId = "", dName = "";
        boolean bDefaultDeptId = false;
        final List<Map<String, Object>> designationList = new ArrayList<Map<String, Object>>();
        Map<String, Object> desgFuncryMap;
        for (final Map<String, Object> desgIdAndName : desgList) {
            desgFuncryMap = new HashMap<String, Object>();

            if (desgIdAndName.get("designationName") != null)
                desgFuncryMap.put("designationName", desgIdAndName.get("designationName"));

            if (desgIdAndName.get("designationId") != null) {
                strDesgId = (String) desgIdAndName.get("designationId");
                if (strDesgId.indexOf("~") != -1) {
                    strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
                    dName = (String) desgIdAndName.get("designationId");
                    dName = dName.substring(dName.indexOf('~') + 1);
                    bDefaultDeptId = true;
                }
                desgFuncryMap.put("designationId", strDesgId);
            }
            designationList.add(desgFuncryMap);
        }
        map.put("designationList", designationList);

        addDropdownData("designationList", (List<Designation>) map.get("designationList"));
        if (bDefaultDeptId && !dName.equals("")) {
            final Department dept = (Department) persistenceService.find("from Department where deptName like '%" + dName + "' ");
            defaultDept = dept.getId().intValue();
        }
        wfitemstate = map.get("wfitemstate") != null ? map.get("wfitemstate").toString() : "";
    }

    private String wfitemstate;
    private VoucherService voucherService;
    private Integer defaultDept;
    private Department department;

    public Integer getDefaultDept() {
        return defaultDept;
    }

    public void setDefaultDept(final Integer defaultDept) {
        this.defaultDept = defaultDept;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public String capitalize(final String value) {
        if (value == null || value.length() == 0)
            return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public Date subtractYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public boolean isShowDetails() {
        return showDetails;
    }

    public void setShowDetails(final boolean showDetails) {
        this.showDetails = showDetails;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public boolean isConsolidatedScreen() {
        return consolidatedScreen;
    }

    public void setConsolidatedScreen(final boolean consolidatedScreen) {
        this.consolidatedScreen = consolidatedScreen;
    }

    public EgovMasterDataCaching getMasterDataCache() {
        return masterDataCache;
    }

    public void setMasterDataCache(EgovMasterDataCaching masterDataCache) {
        this.masterDataCache = masterDataCache;
    }

}
