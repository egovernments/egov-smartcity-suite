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
package org.egov.works.web.actions.estimate;

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.assets.model.Asset;
import org.egov.common.entity.UOM;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Fundsource;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.StateService;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.AssetsForEstimate;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.entity.OverheadValue;
import org.egov.works.models.masters.DepositCode;
import org.egov.works.models.masters.NatureOfWork;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ParentPackage("egov")
@Results({ @Result(name = AbstractEstimateAction.PRINT, type = "stream", location = "XlsInputStream", params = {
        "inputName", "XlsInputStream", "contentType", "application/xls", "contentDisposition",
        "no-cache;filename=AbstractEstimate-BillOfQuantites.xls" }),
        @Result(name = AbstractEstimateAction.NEW, location = "abstractEstimate-new.jsp"),
        @Result(name = AbstractEstimateAction.MAPS, location = "abstractEstimate-maps.jsp"),
        @Result(name = AbstractEstimateAction.EDIT, location = "abstractEstimate-edit.jsp"),
        @Result(name = AbstractEstimateAction.SUCCESS, location = "abstractEstimate-success.jsp"),
        @Result(name = AbstractEstimateAction.HISTORY, location = "abstractEstimate-history.jsp") })
public class AbstractEstimateAction extends GenericWorkFlowAction {

    private static final long serialVersionUID = -4801105778751138267L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    private static final String CANCEL_ACTION = "Cancel";
    private static final String SAVE_ACTION = "Save";
    private static final Object REJECT_ACTION = "Reject";
    private static final Object FORWARD_ACTION = "Forward";
    private static final String SOURCE_SEARCH = "search";
    /* private static final String SOURCE_INBOX = "inbox"; */
    private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
    public static final String MAPS = "maps";
    public static final String HISTORY = "history";
    public static final String ABSTRACTESTIMATE = "AbstractEstimate";
    private AbstractEstimate abstractEstimate = new AbstractEstimate();
    private List<Activity> sorActivities = new LinkedList<Activity>();
    private List<Activity> nonSorActivities = new LinkedList<Activity>();
    private List<OverheadValue> actionOverheadValues = new LinkedList<OverheadValue>();
    private List<AssetsForEstimate> actionAssetValues = new LinkedList<AssetsForEstimate>();
    private List<MultiYearEstimate> actionMultiYearEstimateValues = new LinkedList<MultiYearEstimate>();
    private AbstractEstimateService abstractEstimateService;

    @Autowired
    private EmployeeService employeeService;
    /*
     * @Autowired private UserService userService;
     */

    private String messageKey;
    private String sourcepage = "";
    private String assetStatus;
    private Integer approverUserId;
    private Long departmentId;
    private Integer designationId;
    private String approverComments;
    private Long stateValue;
    private String estimateValue;
    private ReportService reportService;
    public static final String BOQ = "Bill Of Qunatities";
    private InputStream xlsInputStream;
    public static final String PRINT = "print";
    private String mode = "";
    private boolean isAllowEstDateModify = false;

    private String employeeName;
    private String designation;
    private WorksService worksService;
    private Long estimateId;
    private String cancellationReason;
    private String cancelRemarks;
    private String errorCode;
    private String mapMode;
    private String latitude;
    private String longitude;
    @Autowired
    private EisUtilService eisService;
    private List<Object> woDetails;
    private List<Object> wpDetails;
    private BigDecimal paymentReleased = BigDecimal.ZERO;
    private ContractorBillService contractorBillService;
    @Autowired
    private FundSourceHibernateDAO fundSourceHibernateDAO;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<AbstractEstimate> abstractEstimateWorkflowService;
    private Long stateId;
    private final List<StateHistory> workflowHistory = new LinkedList<StateHistory>();
    @Autowired
    private StateService stateService;

    public String getMessageKey() {
        return messageKey;
    }

    private String currentFinancialYearId;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public AbstractEstimateAction() {
        addRelatedEntity("fundSource", Fundsource.class);
        addRelatedEntity("userDepartment", Department.class);
        addRelatedEntity("executingDepartment", Department.class);
        addRelatedEntity("ward", Boundary.class);
        addRelatedEntity("natureOfWork", NatureOfWork.class);
        addRelatedEntity("category", EgwTypeOfWork.class);
        addRelatedEntity("parentCategory", EgwTypeOfWork.class);
        addRelatedEntity("depositCode", DepositCode.class);
    }

    @Action(value = "/estimate/abstractEstimate-edit")
    public String edit() {

        // TODO:Fixme - commented out for time being since the validation not working properly
        /*
         * if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) { final User user =
         * userService.getUserById(worksService.getCurrentLoggedInUserId()); final boolean isValidUser =
         * worksService.validateWorkflowForUser(abstractEstimate, user); if (isValidUser) throw new ApplicationRuntimeException(
         * "Error: Invalid Owner - No permission to view this page."); } else
         */if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";
        getWorkOrderDetails();
        return EDIT;
    }

    private void getWorkOrderDetails() {
        final ArrayList<Integer> projectCodeIdList = new ArrayList<Integer>();
        if (WorksConstants.ADMIN_SANCTIONED_STATUS.equalsIgnoreCase(abstractEstimate.getEgwStatus().getCode())) {
            woDetails = abstractEstimateService.getWODetailsForEstimateId(id);
            wpDetails = abstractEstimateService.getWPDetailsForEstimateId(id);
            if (abstractEstimate.getProjectCode() != null) {
                projectCodeIdList.add(abstractEstimate.getProjectCode().getId().intValue());
                paymentReleased = contractorBillService.getTotalExpenditure(projectCodeIdList,
                        WorksConstants.PROJECTCODE);
            }
        }
    }

    @SkipValidation
    @Action(value = "/estimate/abstractEstimate-viewBillOfQuantitiesXls")
    public String viewBillOfQuantitiesXls() throws JRException, Exception {
        final AbstractEstimate estimate = abstractEstimateService.findById(id, false);
        final ReportRequest reportRequest = new ReportRequest("BillOfQuantities", estimate.getSORActivities(),
                createHeaderParams(estimate, BOQ));
        reportRequest.setReportFormat(ReportFormat.XLS);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            xlsInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    private Map createHeaderParams(final AbstractEstimate estimate, final String type) {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        if (type.equalsIgnoreCase(BOQ)) {
            reportParams.put("workName", estimate.getName());
            reportParams.put("deptName", estimate.getExecutingDepartment().getName());
            reportParams.put("estimateNo", estimate.getEstimateNumber());
            reportParams.put("activitySize", estimate.getSORActivities() == null ? 0 : estimate.getSORActivities()
                    .size());
            reportParams.put("NonSOR_Activities", estimate.getNonSORActivities());
            reportParams.put("grandTotalAmt", getGrandTotalForEstimate(estimate));
        }
        return reportParams;
    }

    private Double getGrandTotalForEstimate(final AbstractEstimate estimate) {
        Double total = 0d;
        for (final Activity act : estimate.getActivities())
            total += act.getAmount().getValue();
        return total;
    }

    @Action(value = "/estimate/abstractEstimate-workflowHistory")
    public String workflowHistory() {
        if (stateId != null) {
            final State state = stateService.getStateById(stateId);
            workflowHistory.addAll(state.getHistory());
            workflowHistory.add(new StateHistory(state));
        }
        return HISTORY;
    }

    @Override
    public StateAware getModel() {
        return abstractEstimate;
    }

    protected void setModel(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    @Override
    public void prepare() {
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setEisService(eisService);

        if (id != null && EDIT.equals("edit")) {
            abstractEstimate = abstractEstimateService.findById(id, false);
            abstractEstimate = abstractEstimateService.merge(abstractEstimate);
        }
        super.prepare();

        final CFinancialYear financialYear = getCurrentFinancialYear();
        if (financialYear != null)
            currentFinancialYearId = financialYear.getId().toString();

        setupDropdownDataExcluding("ward", "category", "parentCategory", "fundSource", "depositCode");

        addDropdownData("parentCategoryList",
                getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
        List<UOM> uomList = getPersistenceService().findAllBy("from UOM  order by upper(uom)");
        if (id == null && abstractEstimate.getEgwStatus() == null
                || !SOURCE_SEARCH.equals(sourcepage) && abstractEstimate.getEgwStatus() != null
                        && abstractEstimate.getEgwStatus().getCode().equals("REJECTED")
                || id != null
                        && abstractEstimate.getEgwStatus() != null && abstractEstimate.getEgwStatus().getCode().equals("NEW"))
            uomList = abstractEstimateService.prepareUomListByExcludingSpecialUoms(uomList);
        addDropdownData("uomList", uomList);
        addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=true"));
        addDropdownData("scheduleCategoryList",
                getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)"));

        populateCategoryList(ajaxEstimateAction, abstractEstimate.getParentCategory() != null);
        populateOverheadsList(ajaxEstimateAction, abstractEstimate.getEstimateDate() != null);
        addDropdownData("fundSourceList", fundSourceHibernateDAO.findAllActiveIsLeafFundSources());

        final Assignment latestAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
        if (latestAssignment != null) {
            approverDepartment = latestAssignment.getDepartment().getId().toString();
            // Executing department needs to be defaulted to logged in user Primary assignment department
            if (abstractEstimate != null && abstractEstimate.getId() == null && abstractEstimate.getExecutingDepartment() == null)
                abstractEstimate.setExecutingDepartment(latestAssignment.getDepartment());
        }
    }

    @Action(value = "/estimate/abstractEstimate-save")
    public String save() {
        // final String actionName = parameters.get("actionName")[0];
        if (workFlowAction != null
                && !(workFlowAction.equals(REJECT_ACTION) || workFlowAction.equals(SAVE_ACTION) || workFlowAction
                        .equals(FORWARD_ACTION)))
            validateNonSorActivities();
        if (!workFlowAction.equals(REJECT_ACTION))
            validateDeptForDepositWorks();

        if (!(CANCEL_ACTION.equals(workFlowAction) && abstractEstimate.getId() == null))
            if (abstractEstimate.getEgwStatus() != null
                    && !(abstractEstimate.getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.REJECTED.toString())
                            && (workFlowAction.equals(FORWARD_ACTION) || workFlowAction.equals(SAVE_ACTION)) || abstractEstimate
                                    .getEgwStatus().getCode().equals("NEW"))) {
                // If Estimate is in work flow other than Rejected or in Drafts
                // case then do nothing(do not delete child tables and insert
                // again)
            } else
                populateEstimateDetails(workFlowAction);

        if (workFlowAction.equals(FORWARD_ACTION)
                && (abstractEstimate.getEgwStatus() == null || abstractEstimate.getEgwStatus() != null
                        && (abstractEstimate.getEgwStatus().getCode().equals(AbstractEstimate.EstimateStatus.REJECTED.toString())
                                || abstractEstimate
                                        .getEgwStatus().getCode().equals("NEW")))) {
            validateForAssetSelection();
            validateForLatLongSelection();
        }

        try {
            transitionWorkFlow(abstractEstimate);
            abstractEstimateService.applyAuditing(abstractEstimate.getState());
            abstractEstimateService.setEstimateNumber(abstractEstimate);

            if (abstractEstimate.getEgwStatus() != null
                    && abstractEstimate.getEgwStatus().getCode()
                            .equals(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())) {
                abstractEstimateService.setProjectCode(abstractEstimate);
                abstractEstimate.setApprovedDate(new Date());
            }

            abstractEstimate = abstractEstimateService.persist(abstractEstimate);

        } catch (final ValidationException valException) {
            throw new ValidationException(valException.getErrors());
        }
        messageKey = "estimate." + workFlowAction;
        addActionMessage(getText(messageKey, "The estimate was saved successfully"));

        if (SAVE_ACTION.equals(workFlowAction))
            sourcepage = "inbox";
        return SAVE_ACTION.equals(workFlowAction) ? EDIT : SUCCESS;
    }

    public void transitionWorkFlow(final AbstractEstimate abstractEstimate) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position position = null;
        Assignment wfInitiator = null;

        wfInitiator = getWorkflowInitiator(abstractEstimate);

        if (CANCEL_ACTION.equals(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                abstractEstimate.transition().end().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(AbstractEstimate.EstimateStatus.CANCELLED.toString()).withDateInfo(currentDate.toDate())
                        .withNextAction("END");
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(ABSTRACTESTIMATE,
                        AbstractEstimate.EstimateStatus.CANCELLED.toString()));
            }
        } else if (REJECT_ACTION.equals(workFlowAction)) {
            abstractEstimate.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(approverComments)
                    .withStateValue(AbstractEstimate.EstimateStatus.REJECTED.toString()).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("");
            abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(ABSTRACTESTIMATE,
                    AbstractEstimate.EstimateStatus.REJECTED.toString()));

        } else if (SAVE_ACTION.equals(workFlowAction)) {
            if (abstractEstimate.getState() == null) {
                final WorkFlowMatrix wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null,
                        null, getAdditionalRule(), currentState, null);
                abstractEstimate.transition().start().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(wfmatrix.getCurrentState()).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition());
                abstractEstimate
                        .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(ABSTRACTESTIMATE, "NEW"));
            }
        } else {
            if (null != approverPositionId && approverPositionId != -1)
                position = (Position) persistenceService.find("from Position where id=?", approverPositionId);
            if (abstractEstimate.getState() == null) {
                final WorkFlowMatrix wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null,
                        null, getAdditionalRule(), currentState, null);
                abstractEstimate.transition().start().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(position)
                        .withNextAction(wfmatrix.getNextAction());
                abstractEstimate
                        .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(ABSTRACTESTIMATE, wfmatrix.getNextStatus()));
            } else {
                final WorkFlowMatrix wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null,
                        null, getAdditionalRule(), abstractEstimate.getCurrentState().getValue(), null);
                if (wfmatrix.getNextAction() != null && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    abstractEstimate.transition().end().withSenderName(user.getName()).withComments(approverComments)
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                            .withNextAction(wfmatrix.getNextAction());
                else
                    abstractEstimate.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(approverComments)
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(position)
                            .withNextAction(wfmatrix.getNextAction());
                abstractEstimate
                        .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(ABSTRACTESTIMATE, wfmatrix.getNextStatus()));
            }

        }
        if (!(CANCEL_ACTION.equals(workFlowAction) || SAVE_ACTION.equals(workFlowAction)))
            setApproverAndDesignation(abstractEstimate);
    }

    public void setApproverAndDesignation(final AbstractEstimate abstractEstimate) {
        /* start for customizing workflow message display */
        if (abstractEstimate.getEgwStatus() != null
                && !"NEW".equalsIgnoreCase(abstractEstimate.getEgwStatus().getCode())) {
            Date date = new Date();
            if (abstractEstimate.getState().getCreatedDate() != null)
                date = abstractEstimate.getState().getCreatedDate();
            final String result = worksService.getEmpNameDesignation(abstractEstimate.getState().getOwnerPosition(),
                    date);
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setEmployeeName(empName);
                setDesignation(designation);
            }
        }
        /* end */
    }

    private Assignment getWorkflowInitiator(final AbstractEstimate abstractEstimate) {
        Assignment wfInitiator;
        if (abstractEstimate.getCreatedBy() == null)
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(worksService.getCurrentLoggedInUserId());
        else
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(abstractEstimate.getCreatedBy().getId());
        return wfInitiator;
    }

    private void validateForAssetSelection() {
        if (abstractEstimate.getNatureOfWork() != null
                && !(abstractEstimate.getNatureOfWork().getName().equals(WorksConstants.DEPOSIT_WORKS_THIRDPARTY_ASSET)
                        || abstractEstimate
                                .getNatureOfWork().getName().equals(WorksConstants.DEPOSIT_WORKS_NO_ASSET_CREATED))) {
            final String isAssetRequired = worksService.getWorksConfigValue("ASSET_MANDATORY");
            if (isAssetRequired.equals("yes") && actionAssetValues != null) {
                boolean isAssetPresent = false;
                for (final AssetsForEstimate assetValue : actionAssetValues)
                    if (assetValue != null && assetValue.getAsset() != null && assetValue.getAsset().getId() != null) {
                        isAssetPresent = true;
                        break;
                    }
                if (!isAssetPresent)
                    throw new ValidationException(Arrays.asList(new ValidationError("estimate.asset.required",
                            "estimate.asset.required")));
            }
        }
    }

    private void validateForLatLongSelection() {
        if (abstractEstimate.getLatitude() == null || abstractEstimate.getLongitude() == null
                || StringUtils.isBlank(abstractEstimate.getLocation()))
            throw new ValidationException(
                    Arrays.asList(new ValidationError("estimate.latlon.required", "estimate.latlon.required")));
    }

    @Action(value = "/estimate/abstractEstimate-maps")
    public String maps() {
        return MAPS;
    }

    // TODO:Fixme - check and remove this
    private void validateNonSorActivities() {
        final Set<String> exceptionSor = worksService.getExceptionSOR().keySet();
        for (final Activity activity : nonSorActivities)
            if (activity != null && activity.getNonSor().getUom() != null) {
                final UOM nonSorUom = (UOM) getPersistenceService().find("from UOM where id = ?",
                        activity.getNonSor().getUom().getId());
                if (nonSorUom != null && exceptionSor.contains(nonSorUom.getUom())) {
                    setSourcepage("inbox");
                    throw new ValidationException(Arrays.asList(new ValidationError("validate.nonSor.uom",
                            "validate.nonSor.uom")));
                }
            }
    }

    public String downloadTemplate() {
        return "template";
    }

    protected void populateCategoryList(final AjaxEstimateAction ajaxEstimateAction, final boolean categoryPopulated) {
        if (categoryPopulated) {
            ajaxEstimateAction.setCategory(abstractEstimate.getParentCategory().getId());
            ajaxEstimateAction.subcategories();
            addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());
        } else
            addDropdownData("categoryList", Collections.emptyList());
    }

    protected void populateOverheadsList(final AjaxEstimateAction ajaxEstimateAction, final boolean estimateDatePresent) {
        if (estimateDatePresent) {
            ajaxEstimateAction.setEstDate(abstractEstimate.getEstimateDate());
            ajaxEstimateAction.overheads();
            addDropdownData("overheadsList", ajaxEstimateAction.getOverheads());
        } else {
            ajaxEstimateAction.setEstDate(new Date());
            ajaxEstimateAction.overheads();
            addDropdownData("overheadsList", ajaxEstimateAction.getOverheads());
        }
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    @Action(value = "/estimate/abstractEstimate-newform")
    public String newform() {
        return NEW;
    }

    private void populateEstimateDetails(final String actionName) {
        abstractEstimate.getActivities().clear();
        abstractEstimate.getOverheadValues().clear();
        abstractEstimate.getAssetValues().clear();
        abstractEstimate.getMultiYearEstimates().clear();

        populateSorActivities();
        populateNonSorActivities();
        populateActivities();
        populateOverheads();
        populateAssets();
        populateMultiYearEstimates();
        abstractEstimate.setWorkValue(abstractEstimate.getWorkValue());
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        populateOverheadsList(ajaxEstimateAction, abstractEstimate.getEstimateDate() != null);

        if (!(SAVE_ACTION.equals(actionName) || CANCEL_ACTION.equals(actionName) || REJECT_ACTION.equals(actionName))
                && abstractEstimate.getWorkValue() <= 0.0) {
            errorCode = "estimate.workvalue.null";
            throw new ValidationException(Arrays.asList(new ValidationError("estimate.workvalue.null",
                    "estimate.workvalue.null")));

        }
    }

    protected void populateSorActivities() {
        for (final Activity activity : sorActivities)
            if (validSorActivity(activity)) {
                activity.setSchedule((ScheduleOfRate) getPersistenceService().find("from ScheduleOfRate where id = ?",
                        activity.getSchedule().getId()));
                activity.setUom(activity.getSchedule().getUom());
                abstractEstimate.addActivity(activity);
            }
    }

    protected boolean validSorActivity(final Activity activity) {
        if (activity != null && activity.getSchedule() != null && activity.getSchedule().getId() != null)
            return true;

        return false;
    }

    protected void populateNonSorActivities() {
        for (final Activity activity : nonSorActivities)
            if (activity != null) {
                activity.setUom(activity.getNonSor().getUom());
                // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting triggered
                // on update of estimate for child objects
                activity.getNonSor().setCreatedBy(worksService.getCurrentLoggedInUser());
                activity.getNonSor().setCreatedDate(new Date());
                activity.getNonSor().setLastModifiedBy(worksService.getCurrentLoggedInUser());
                activity.getNonSor().setLastModifiedDate(new Date());
                abstractEstimate.addActivity(activity);
            }
    }

    private void populateActivities() {
        for (final Activity activity : abstractEstimate.getActivities()) {
            // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting triggered on
            // update of estimate for child objects
            activity.setCreatedBy(worksService.getCurrentLoggedInUser());
            activity.setCreatedDate(new Date());
            activity.setLastModifiedBy(worksService.getCurrentLoggedInUser());
            activity.setLastModifiedDate(new Date());
            activity.setAbstractEstimate(abstractEstimate);
        }
    }

    protected void populateOverheads() {
        for (final OverheadValue overheadValue : actionOverheadValues)
            if (validOverhead(overheadValue)) {
                overheadValue.setOverhead((Overhead) getPersistenceService().find("from Overhead where id = ?",
                        overheadValue.getOverhead().getId()));
                overheadValue.setAbstractEstimate(abstractEstimate);
                // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting triggered
                // on update of estimate for child objects
                overheadValue.setCreatedBy(worksService.getCurrentLoggedInUser());
                overheadValue.setCreatedDate(new Date());
                overheadValue.setLastModifiedBy(worksService.getCurrentLoggedInUser());
                overheadValue.setLastModifiedDate(new Date());
                abstractEstimate.addOverheadValue(overheadValue);
            }
    }

    protected boolean validOverhead(final OverheadValue overheadValue) {
        if (overheadValue != null && overheadValue.getOverhead() != null && overheadValue.getOverhead().getId() != null
                && overheadValue.getOverhead().getId() != -1 && overheadValue.getOverhead().getId() != 0)
            return true;
        return false;
    }

    protected void populateAssets() {
        final List<ValidationError> valErrList = new LinkedList<ValidationError>();
        final List<String> strStatus = getStatusList();
        final Set<String> validAssetCodes = new HashSet<String>();
        for (final AssetsForEstimate assetValue : actionAssetValues)
            if (validAsset(assetValue)) {
                final Asset lAsset = (Asset) getPersistenceService().find("from Asset where code = ?",
                        assetValue.getAsset().getCode());
                if (lAsset == null) {
                    final String message = "Asset code \'" + assetValue.getAsset().getCode()
                            + "\' does not exist. Please create the asset before link.";
                    valErrList.add(new ValidationError(message, message));
                } else {
                    if (!checkValidStatus(lAsset, strStatus)) {
                        final String message = "Asset code \'" + assetValue.getAsset().getCode()
                                + "\' can't be link for selected nature of work.";
                        valErrList.add(new ValidationError(message, message));
                    }
                    if (validAssetCodes.contains(lAsset.getCode())) {
                        final String message = "Please remove the duplicate entry for Asset code \'" + lAsset.getCode()
                                + "\'";
                        valErrList.add(new ValidationError(message, message));
                    } else
                        validAssetCodes.add(lAsset.getCode());
                    assetValue.setAsset(lAsset);
                    assetValue.setAbstractEstimate(abstractEstimate);
                    // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting
                    // triggered on update of estimate for child objects
                    assetValue.setCreatedBy(worksService.getCurrentLoggedInUser());
                    assetValue.setCreatedDate(new Date());
                    assetValue.setLastModifiedBy(worksService.getCurrentLoggedInUser());
                    assetValue.setLastModifiedDate(new Date());
                    abstractEstimate.addAssetValue(assetValue);
                }
            }
        if (!valErrList.isEmpty())
            throw new ValidationException(valErrList);

    }

    private List<String> getStatusList() {
        List<String> strStatus = null;
        if (assetStatus == null)
            strStatus = new ArrayList<String>();
        else
            strStatus = Arrays.asList(assetStatus.split(","));

        return strStatus;
    }

    private boolean checkValidStatus(final Asset ass, final List<String> strStatus) {
        for (final String desc : strStatus)
            if (desc.trim().equalsIgnoreCase(ass.getStatus().getDescription()))
                return true;
        return false;
    }

    protected boolean validAsset(final AssetsForEstimate assetValue) {
        if (assetValue != null && assetValue.getAsset() != null && assetValue.getAsset().getCode() != null
                && !assetValue.getAsset().getCode().isEmpty())
            return true;
        return false;
    }

    protected void populateMultiYearEstimates() {
        int count = 1;
        double totalPerc = 0.0;
        for (final MultiYearEstimate multiYearEstimate : actionMultiYearEstimateValues) {
            if (validMultiYearEstimate(multiYearEstimate)) {
                multiYearEstimate.setFinancialYear((CFinancialYear) getPersistenceService().find(
                        "from CFinancialYear where id = ?", multiYearEstimate.getFinancialYear().getId()));
                multiYearEstimate.setAbstractEstimate(abstractEstimate);
                totalPerc = totalPerc + multiYearEstimate.getPercentage();
                // TODO:Fixme - Setting auditable properties by time being since HibernateEventListener is not getting triggered
                // on update of estimate for child objects
                multiYearEstimate.setCreatedBy(worksService.getCurrentLoggedInUser());
                multiYearEstimate.setCreatedDate(new Date());
                multiYearEstimate.setLastModifiedBy(worksService.getCurrentLoggedInUser());
                multiYearEstimate.setLastModifiedDate(new Date());
                abstractEstimate.addMultiYearEstimate(multiYearEstimate);
            }
            if (multiYearEstimate != null && actionMultiYearEstimateValues.size() == count && totalPerc != 0.0
                    && totalPerc < 100)
                throw new ValidationException(Arrays.asList(new ValidationError("percentage",
                        "multiYearEstimate.percentage.percentage_equals_100")));

            if (multiYearEstimate != null && multiYearEstimate.getFinancialYear() == null || multiYearEstimate != null
                    && multiYearEstimate.getFinancialYear() != null
                    && multiYearEstimate.getFinancialYear().getId() != null
                    && multiYearEstimate.getFinancialYear().getId() == 0)
                throw new ValidationException(Arrays.asList(new ValidationError("financialYear",
                        "multiYeareEstimate.financialYear.null")));
            count++;
        }
    }

    protected boolean validMultiYearEstimate(final MultiYearEstimate multiYearEstimate) {
        if (multiYearEstimate != null && multiYearEstimate.getFinancialYear() != null
                && multiYearEstimate.getFinancialYear().getId() != null
                && multiYearEstimate.getFinancialYear().getId() != 0 && multiYearEstimate.getPercentage() >= 0.0)
            return true;

        return false;
    }

    public String cancelApprovedEstimate() {
        abstractEstimate = abstractEstimateService.findById(estimateId, false);

        final String oldEstimateNo = abstractEstimate.getEstimateNumber();
        final Employee employee = employeeService.getEmployeeById(worksService.getCurrentLoggedInUserId());
        String empName = "";
        if (employee != null)
            empName = employee.getName();
        if (cancelRemarks != null && StringUtils.isNotBlank(cancelRemarks))
            cancellationReason.concat(" : ").concat(cancelRemarks).concat(". ")
                    .concat(getText("estimate.cancel.cancelledby")).concat(": ").concat(empName);
        else
            cancellationReason.concat(". ").concat(getText("estimate.cancel.cancelledby")).concat(": ")
                    .concat(empName);

        // suffix /C for estimate number
        final String newEstNo = abstractEstimate.getEstimateNumber() + "/C";
        abstractEstimate.setEstimateNumber(newEstNo);

        // If type is deposit works then release Deposit works amount
        if (isSkipBudgetCheck())
            abstractEstimateService
                    .releaseDepositWorksAmountOnReject(abstractEstimate.getFinancialDetails().get(0));
        else
            // If it is Budget work then release latest budget consumed
            abstractEstimateService.releaseBudgetOnReject(abstractEstimate.getFinancialDetails().get(0));
        // Make corresponding project code as inactive
        abstractEstimate.getProjectCode().setActive(false);

        // TODO - The setter methods of variables in State.java are
        // protected. Need to alternative way to solve this issue.
        // Set the status and workflow state to cancelled
        /*****
         * State oldEndState = abstractEstimate.getCurrentState(); Position owner = prsnlInfo.getAssignment(new
         * Date()).getPosition(); oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
         * oldEndState.setModifiedBy(prsnlInfo.getUserMaster()); oldEndState.setCreatedDate(new Date());
         * oldEndState.setModifiedDate(new Date()); oldEndState.setOwner(owner); oldEndState.setValue(AbstractEstimate
         * .EstimateStatus.CANCELLED.toString()); oldEndState.setText1(cancellationText); abstractEstimate.changeState("END",
         * owner, null);
         *******/

        abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("AbstractEstimate",
                AbstractEstimate.EstimateStatus.CANCELLED.toString()));
        getBudgetUsageListForEstimateNumber(oldEstimateNo);

        messageKey = "estimate.cancel";

        return SUCCESS;
    }

    private void getBudgetUsageListForEstimateNumber(final String estimateNo) {
        final List<BudgetUsage> budgetUsageList = abstractEstimateService.getBudgetUsageListForEstNo(estimateNo);
        if (budgetUsageList != null && !budgetUsageList.isEmpty())
            for (final BudgetUsage bu : budgetUsageList)
                bu.setReferenceNumber(abstractEstimate.getEstimateNumber());
    }

    private void validateDeptForDepositWorks() {
        if (isDepositWorksType())
            if (abstractEstimate.getUserDepartment() != null
                    && abstractEstimate.getUserDepartment().getId() != null
                    && abstractEstimate.getExecutingDepartment() != null
                    && abstractEstimate.getExecutingDepartment().getId() != null
                    && abstractEstimate.getExecutingDepartment().getId() != abstractEstimate.getUserDepartment()
                            .getId()) {
                abstractEstimate.getActivities().clear();
                abstractEstimate.getAssetValues().clear();
                abstractEstimate.getOverheadValues().clear();
                abstractEstimate.getMultiYearEstimates().clear();
                populateSorActivities();
                populateNonSorActivities();
                populateActivities();
                populateOverheads();
                populateAssets();
                populateMultiYearEstimates();
                errorCode = "estimate.depositworks.dept.check";
                throw new ValidationException(Arrays.asList(new ValidationError("estimate.depositworks.dept.check",
                        "estimate.depositworks.dept.check")));
            }
    }

    private Boolean isDepositWorksType() {
        boolean isDepositWorks = false;
        final List<String> depositTypeList = getAppConfigValuesToSkipBudget();
        for (final String type : depositTypeList)
            if (type.equals(abstractEstimate.getNatureOfWork().getName()))
                isDepositWorks = true;
        return isDepositWorks;
    }

    private Boolean isSkipBudgetCheck() {
        boolean skipBudget = false;
        if (abstractEstimate != null && abstractEstimate.getId() != null)
            skipBudget = isDepositWorksType();
        return skipBudget;
    }

    public AbstractEstimateService getAbstractEstimateService() {
        return abstractEstimateService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public List<Activity> getSorActivities() {
        return sorActivities;
    }

    public void setSorActivities(final List<Activity> sorActivities) {
        this.sorActivities = sorActivities;
    }

    public List<Activity> getNonSorActivities() {
        return nonSorActivities;
    }

    public void setNonSorActivities(final List<Activity> nonSorActivities) {
        this.nonSorActivities = nonSorActivities;
    }

    public List<OverheadValue> getActionOverheadValues() {
        return actionOverheadValues;
    }

    public void setActionOverheadValues(final List<OverheadValue> actionOverheadValues) {
        this.actionOverheadValues = actionOverheadValues;
    }

    public List<AssetsForEstimate> getActionAssetValues() {
        return actionAssetValues;
    }

    public void setActionAssetValues(final List<AssetsForEstimate> actionAssetValues) {
        this.actionAssetValues = actionAssetValues;
    }

    public String getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(final String currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    protected CFinancialYear getCurrentFinancialYear() {
        if (abstractEstimate.getEstimateDate() != null)
            return financialYearHibernateDAO.getFinYearByDate(abstractEstimate.getEstimateDate());
        else
            return financialYearHibernateDAO.getFinYearByDate(new Date());
    }

    public List<MultiYearEstimate> getActionMultiYearEstimateValues() {
        return actionMultiYearEstimateValues;
    }

    public void setActionMultiYearEstimateValues(final List<MultiYearEstimate> actionMultiYearEstimateValues) {
        this.actionMultiYearEstimateValues = actionMultiYearEstimateValues;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(final String assetStatus) {
        this.assetStatus = assetStatus;
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

    @Override
    public String getApproverComments() {
        return approverComments;
    }

    @Override
    public void setApproverComments(final String approverComments) {
        this.approverComments = approverComments;
    }

    public Long getStateValue() {
        return stateValue;
    }

    public void setStateValue(final Long stateValue) {
        this.stateValue = stateValue;
    }

    public List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues(WorksConstants.WORKS_MODULE_NAME, KEY_NAME);
    }

    public String getEstimateValue() {
        return estimateValue;
    }

    public void setEstimateValue(final String estimateValue) {
        this.estimateValue = estimateValue;
    }

    /*
     * public void setUserService(final UserService userService) { this.userService = userService; }
     */

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public InputStream getXlsInputStream() {
        return xlsInputStream;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setCancelRemarks(final String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean getIsAllowEstDateModify() {
        return isAllowEstDateModify;
    }

    public void setIsAllowEstDateModify(final boolean isAllowEstDateModify) {
        this.isAllowEstDateModify = isAllowEstDateModify;
    }

    public String getMapMode() {
        return mapMode;
    }

    public void setMapMode(final String mapMode) {
        this.mapMode = mapMode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getPaymentReleased() {
        return paymentReleased;
    }

    public void setPaymentReleased(final BigDecimal paymentReleased) {
        this.paymentReleased = paymentReleased;
    }

    public ContractorBillService getContractorBillService() {
        return contractorBillService;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    public List<Object> getWoDetails() {
        return woDetails;
    }

    public void setWoDetails(final List<Object> woDetails) {
        this.woDetails = woDetails;
    }

    public List<Object> getWpDetails() {
        return wpDetails;
    }

    public void setWpDetails(final List<Object> wpDetails) {
        this.wpDetails = wpDetails;
    }

    public void setStateId(final Long stateId) {
        this.stateId = stateId;
    }

    public List<StateHistory> getWorkflowHistory() {
        return workflowHistory;
    }

}
