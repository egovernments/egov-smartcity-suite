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
package org.egov.works.web.actions.revisionEstimate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.common.entity.UOM;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.NonSor;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionWorkOrder;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.RevisionEstimateService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.WorkOrderServiceImpl;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ParentPackage("egov")
@Result(name = RevisionEstimateAction.NEW, location = "revisionEstimate-new.jsp")
public class RevisionEstimateAction extends GenericWorkFlowAction {

    private static final long serialVersionUID = 7691992958619463320L;
    private static final Logger LOGGER = Logger.getLogger(RevisionEstimateAction.class);
    private RevisionAbstractEstimate revisionEstimate = new RevisionAbstractEstimate();
    private RevisionWorkOrder revisionWorkOrder = new RevisionWorkOrder();

    private WorkflowService<RevisionAbstractEstimate> workflowService;
    private PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService;
    private PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService;

    private AbstractEstimateService abstractEstimateService;
    private String messageKey;
    private WorksService worksService;
    private String employeeName;
    private String designation;
    private String additionalRuleValue;
    private String departmentName;
    private Long id;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private EmployeeServiceOld employeeService;
    private Integer approverPositionId;
    private WorkOrderService workOrderService;
    private List<Activity> originalRevisedActivityList = new LinkedList<Activity>();
    private double originalTotalAmount = 0;
    private double originalTotalTax = 0;
    private double originalWorkValueIncludingTax = 0;

    private String sourcepage = "";
    private Long originalEstimateId;
    private Long originalWOId;
    private double revisionEstimatesValue = 0;
    private double revisionWOValue = 0;
    private List<RevisionAbstractEstimate> reList = new LinkedList<RevisionAbstractEstimate>();
    private AbstractEstimate abstractEstimate = new AbstractEstimate();
    private WorkOrder workOrder = new WorkOrder();

    private List<Activity> sorActivities = new LinkedList<Activity>();
    private List<Activity> nonSorActivities = new LinkedList<Activity>();
    private static final String CANCEL_ACTION = "cancel";
    private static final String SAVE_ACTION = "save";
    private static final Object REJECT_ACTION = "reject";
    private static final String SOURCE_INBOX = "inbox";
    private static final String ACTION_NAME = "actionName";
    private PersistenceService<NonSor, Long> nonSorService;

    private String estimateValue;
    private int reCount = 1;
    @Autowired
    private UserService userService;
    private Long logedInUserDept;
    private static final String ACTIVITY_SEARCH = "activitySearch";
    private String activityCode;
    private String activityDesc;
    private List<WorkOrderActivity> activityList; // for search page
    private String workorderNo;
    private List<WorkOrderActivity> changeQuantityActivities = new LinkedList<WorkOrderActivity>();
    private RevisionEstimateService revisionEstimateService;
    // private PersistenceService<Script, Long> scriptService;
    private boolean isBudgetRejectionNoPresent;
    private BigDecimal amountRuleValue;
    @Autowired
    private ScriptService scriptService;

    public RevisionEstimateAction() {
        addRelatedEntity("executingDepartment", Department.class);
    }

    @Override
    public StateAware getModel() {
        return revisionEstimate;
    }

    public String edit() {
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(revisionEstimate, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        // This logic is used to get the sor rate based on original(parent)
        // workOrderDate for RevisionEstimate
        // and based on estimateDate for original(parent) AbstractEstimate
        for (final Activity activity : revisionEstimate.getSORActivities()) {
            double sorRate = 0.0;

            Date workOrderDate = new Date();
            workOrderDate = workOrder.getWorkOrderDate();
            if (activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()) || activity.getRevisionType()
                                    .toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString())))
                sorRate = activity.getSORRateForDate(workOrderDate).getValue();
            else if (activity.getAbstractEstimate().getParent() != null
                    && activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) || activity
                                    .getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
                if (activity.getParent() != null
                        && activity.getParent().getRevisionType() != null
                        && activity.getParent().getRevisionType().toString()
                                .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()))
                    sorRate = activity.getSORRateForDate(workOrderDate).getValue();
                else
                    sorRate = activity.getSORRateForDate(activity.getAbstractEstimate().getParent().getEstimateDate())
                            .getValue();
            activity.setSORCurrentRate(new Money(sorRate));
        }

        return NEW;
    }

    @Action(value = "/revisionEstimate/revisionEstimate-newform")
    public String newform() {
        LOGGER.debug("RevisionEstimateAction | newform | Start");
        return NEW;
    }

    @Override
    public void prepare() {

        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());

        if (id != null) {
            revisionEstimate = revisionAbstractEstimateService.findById(id, false);
            revisionEstimate = revisionAbstractEstimateService.merge(revisionEstimate);
            originalEstimateId = revisionEstimate.getParent().getId();

            abstractEstimate = (AbstractEstimate) persistenceService.find(" from AbstractEstimate where id=?",
                    originalEstimateId);

            WorkOrderEstimate revWorkOrderEstimate = null;
            if ("CANCELLED".equals(revisionEstimate.getEgwStatus().getCode()))
                revWorkOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                        " from WorkOrderEstimate where estimate.id=? and workOrder.egwStatus.code='CANCELLED'", id);
            else
                revWorkOrderEstimate = (WorkOrderEstimate) persistenceService.find(
                        " from WorkOrderEstimate where estimate.id=? and workOrder.egwStatus.code!='CANCELLED'", id);
            originalWOId = revWorkOrderEstimate.getWorkOrder().getParent().getId();
            revisionWorkOrder = revisionWorkOrderService.findById(revWorkOrderEstimate.getWorkOrder().getId(), false);

            reList = revisionAbstractEstimateService.findAllByNamedQuery("REVISION_ESTIMATES_BY_ESTID_WOID",
                    abstractEstimate.getId(), originalWOId);

            workOrderService.calculateCumulativeDetailsForRE(revWorkOrderEstimate);
            revisionWorkOrder.getWorkOrderEstimates().get(0)
                    .setWorkOrderActivities(revWorkOrderEstimate.getWorkOrderActivities());

        } else {
            abstractEstimate = (AbstractEstimate) persistenceService.find(" from AbstractEstimate where id=?",
                    originalEstimateId);
            reList = revisionAbstractEstimateService.findAllByNamedQuery("REVISION_ESTIMATES_BY_ESTID_WOID",
                    abstractEstimate.getId(), originalWOId);
        }

        workOrder = (WorkOrder) persistenceService.find(" from WorkOrder where id=?", originalWOId);

        double amnt = 0;
        if (reList != null && !reList.isEmpty()) {
            for (final RevisionAbstractEstimate re : reList)
                // Don't consider previous REs
                if (revisionEstimate == null || revisionEstimate.getCreatedDate() == null || revisionEstimate != null
                        && revisionEstimate.getCreatedDate() != null
                        && re.getCreatedDate().before(revisionEstimate.getCreatedDate()))
                    amnt += re.getTotalAmount().getValue();
            amnt += abstractEstimate.getTotalAmount().getValue();
        } else
            amnt += abstractEstimate.getTotalAmount().getValue();
        revisionEstimatesValue = amnt;

        if (abstractEstimate != null) {
            final List<AbstractEstimate> originalRevisedEstimateList = new LinkedList<AbstractEstimate>();
            originalRevisedEstimateList.add(abstractEstimate);
            originalRevisedEstimateList.addAll(reList);
            for (final AbstractEstimate ae : originalRevisedEstimateList)
                // Don't consider previous REs
                if (revisionEstimate == null || revisionEstimate.getCreatedDate() == null || revisionEstimate != null
                        && revisionEstimate.getCreatedDate() != null
                        && ae.getCreatedDate().before(revisionEstimate.getCreatedDate())) {
                    originalRevisedActivityList.addAll(ae.getActivities());
                    originalTotalAmount = originalTotalAmount + ae.getWorkValue();
                    originalTotalTax = originalTotalTax + ae.getTotalTax().getValue();
                    originalWorkValueIncludingTax = originalWorkValueIncludingTax
                            + ae.getWorkValueIncludingTaxes().getValue();
                }
        }

        super.prepare();
        setupDropdownDataExcluding("");
        List<UOM> uomList = getPersistenceService().findAllBy("from UOM order by upper(uom)");
        if ("createRE".equals(sourcepage) || !"search".equals(sourcepage) && revisionEstimate.getEgwStatus() != null
                && revisionEstimate.getEgwStatus().getCode().equals("REJECTED"))
            uomList = abstractEstimateService.prepareUomListByExcludingSpecialUoms(uomList);
        addDropdownData("uomList", uomList);
        addDropdownData("scheduleCategoryList",
                getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)"));
        if (abstractEstimateService.getLatestAssignmentForCurrentLoginUser() != null)
            logedInUserDept = abstractEstimateService.getLatestAssignmentForCurrentLoginUser().getDepartment().getId();
    }

    @Override
    protected String getAdditionalRule() {
        final ScriptContext scriptContext = ScriptService.createContext("estimate", revisionEstimate);
        return scriptService.executeScript("works.estimate.department.type", scriptContext).toString();
        // List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT",
        // "works.estimate.department.type");
        // return
        // scripts.get(0).eval(Script.createContext("estimate",revisionEstimate)).toString();
    }

    @Override
    protected BigDecimal getAmountRule() {
        return new BigDecimal(revisionEstimate.getWorkValue());
    }

    public String save() {
        final String actionName = parameters.get("actionName")[0];
        if (actionName != null
                && !(actionName.equals("Reject") || actionName.equals("Cancel") || actionName.equals("Forward")))
            validateNonSorActivities();
        if (actionName != null && actionName.equals("Reject")) {
            isBudgetRejectionNoPresent = true;
            if (revisionEstimate.getEgwStatus() != null
                    && (revisionEstimate.getEgwStatus().getCode().equalsIgnoreCase("CREATED") || revisionEstimate
                            .getEgwStatus().getCode().equalsIgnoreCase("RESUBMITTED")))
                isBudgetRejectionNoPresent = false;
        }
        // **revisionEstimate.setApproverPositionId(approverPositionId);

        final String deptName = getWorkFlowDepartment();
        String curStatus;
        if (revisionEstimate.getEgwStatus() != null)
            curStatus = revisionEstimate.getEgwStatus().getCode();
        else
            curStatus = "NEW";
        if (revisionWorkOrder != null && (curStatus.equals("NEW") || curStatus.equals("REJECTED")))
            revisionWorkOrder.getWorkOrderEstimates().clear();

        if (curStatus.equals("NEW") || curStatus.equals("REJECTED"))
            saveREstimate(actionName);
        if ((actionName.equalsIgnoreCase("Forward") || actionName.equalsIgnoreCase("Approve"))
                && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), deptName, getAmountRule(),
                        getAdditionalRule(), curStatus, getPendingActions()) == null)
            throw new ValidationException(Arrays.asList(new ValidationError("revisionEstimate.workflow.notdefined",
                    getText("revisionEstimate.workflow.notdefined", new String[] { deptName }))));

        if (!(SAVE_ACTION.equalsIgnoreCase(actionName) || CANCEL_ACTION.equalsIgnoreCase(actionName) || REJECT_ACTION
                .equals(actionName)) && revisionEstimate.getActivities().isEmpty())
            throw new ValidationException(Arrays.asList(new ValidationError("revisionEstimate.activities.empty",
                    "revisionEstimate.activities.empty")));

        revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);

        if (actionName.equalsIgnoreCase("save")) {
            revisionEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("AbstractEstimate", "NEW"));
            if (id == null) {
                final Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(
                        worksService.getCurrentLoggedInUserId()).getIdPersonalInformation());
                // revisionEstimate = (RevisionAbstractEstimate)
                // workflowService.start(revisionEstimate, pos,
                // "Revision Estimate created.");
                revisionEstimate.transition().start().withOwner(pos)
                        .withComments("Revision Estimate created.");
            }
            addActionMessage(getText(messageKey, "The Revision Estimate was saved successfully"));
            revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);
        } else {
            if (id == null) {
                revisionEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("AbstractEstimate", "NEW"));
                final Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(
                        worksService.getCurrentLoggedInUserId()).getIdPersonalInformation());
                // revisionEstimate = (RevisionAbstractEstimate)
                // workflowService.start(revisionEstimate, pos,
                // "Revision Estimate created.");
                revisionEstimate.transition().start().withOwner(pos)
                        .withComments("Revision Estimate created.");
                revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);
            }
            revisionEstimate.setAmountRule(getAmountRule());
            revisionEstimate.setAdditionalRule(getAdditionalRule());
            workflowService.transition(actionName, revisionEstimate, approverComments);
            revisionEstimate = revisionAbstractEstimateService.persist(revisionEstimate);
        }

        revisionWorkOrder = createRevisionWO(curStatus);
        revisionWorkOrder = revisionWorkOrderService.persist(revisionWorkOrder);
        messageKey = "revisionEstimate." + actionName;
        getDesignation(revisionEstimate);
        return SAVE_ACTION.equalsIgnoreCase(actionName) ? EDIT : SUCCESS;
    }

    private void saveREstimate(final String actionName) {
        createRevisionEstimate();
    }

    protected void createRevisionEstimate() {
        final List<AbstractEstimate> revisionEstimates = abstractEstimateService.findAllBy(
                "from AbstractEstimate where parent.id=?", originalEstimateId);

        reCount = reCount + revisionEstimates.size();
        if (revisionEstimate.getId() != null)
            reCount = reCount - 1;
        if (id == null) {
            revisionEstimate.setParent(abstractEstimate);
            revisionEstimate.setEstimateDate(new Date());
            revisionEstimate.setEstimateNumber(abstractEstimate.getEstimateNumber()
                    + "/RE".concat(Integer.toString(reCount)));
            revisionEstimate.setName("Revision Estimate for: " + abstractEstimate.getName());
            revisionEstimate.setDescription("Revision Estimate for: " + abstractEstimate.getDescription());
            revisionEstimate.setNatureOfWork(abstractEstimate.getNatureOfWork());
            revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
            revisionEstimate.setUserDepartment(abstractEstimate.getUserDepartment());
            revisionEstimate.setWard(abstractEstimate.getWard());
            revisionEstimate.setDepositCode(abstractEstimate.getDepositCode());
            revisionEstimate.setFundSource(abstractEstimate.getFundSource());
        }
        revisionEstimate.deleteNonSORActivities();
        revisionEstimate.getActivities().clear();
        populateSorActivities(revisionEstimate);
        populateNonSorActivities(revisionEstimate);
        populateActivities(revisionEstimate);
        populateChangeQuantityItems();
    }

    public boolean getShowBudgetFolio() {
        return revisionEstimateService.getShowBudgetFolio(revisionEstimate);
    }

    public boolean getShowDepositFolio() {
        return revisionEstimateService.getShowDepositFolio(revisionEstimate);
    }

    protected void populateChangeQuantityItems() {
        for (final WorkOrderActivity woa : changeQuantityActivities)
            if (woa != null) {
                final WorkOrderActivity parentWOA = (WorkOrderActivity) getPersistenceService().find(
                        "from WorkOrderActivity where id=?", woa.getId());
                final TenderResponse tenderResponse = (TenderResponse) persistenceService.find(
                        "from TenderResponse tr where tr.egwStatus.code = 'APPROVED' and tr.negotiationNumber = ?",
                        workOrder.getNegotiationNumber());
                woa.getActivity().setAbstractEstimate(revisionEstimate);
                if ("-".equals(woa.getActivity().getSignValue()))
                    woa.getActivity().setRevisionType(RevisionType.REDUCED_QUANTITY);
                else
                    woa.getActivity().setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
                woa.getActivity().setParent(parentWOA.getActivity());
                woa.getActivity().setUom(parentWOA.getActivity().getUom());
                // Consider conversion factor
                if (tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(WorksConstants.PERC_TENDER))
                    woa.getActivity().setRate(parentWOA.getActivity().getRate());
                else if (revisionEstimate.getId() == null) {
                    if (parentWOA.getActivity() != null
                            && parentWOA.getActivity().getRevisionType() != null
                            && parentWOA.getActivity().getRevisionType().toString()
                                    .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()))
                        woa.getActivity().setRate(
                              woa.getActivity().getRate()
                                        * parentWOA.getActivity()
                                                .getConversionFactorForRE(workOrder.getWorkOrderDate()));
                    else
                        woa.getActivity().setRate(
                               woa.getActivity().getRate()
                                        * parentWOA.getActivity().getConversionFactor());
                } else
                    woa.getActivity().setRate(woa.getActivity().getRate());
                if (parentWOA.getActivity().getNonSor() == null)
                    woa.getActivity().setSchedule(parentWOA.getActivity().getSchedule());
                else
                    woa.getActivity().setNonSor(parentWOA.getActivity().getNonSor());

                revisionEstimate.addActivity(woa.getActivity());
            }

    }

    public String searchActivitiesForRE() {
        final Map<String, Object> criteriaMap = new HashMap<String, Object>();
        if (originalWOId != null)
            criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ID, originalWOId);
        if (originalEstimateId != null)
            criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ESTIMATE_ID, originalEstimateId);
        if (activityCode != null && !"".equalsIgnoreCase(activityCode))
            criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_CODE, activityCode);
        if (activityDesc != null && !"".equalsIgnoreCase(activityDesc))
            criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_DESC, activityDesc);

        final List<WorkOrderActivity> tempActivityList = workOrderService
                .searchWOActivitiesForChangeQuantity(criteriaMap);
        activityList = new ArrayList<WorkOrderActivity>();
        for (final WorkOrderActivity woa : tempActivityList)
            if (woa.getActivity() != null
                    && woa.getActivity().getRevisionType() != null
                    && (woa.getActivity().getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) || woa.getActivity()
                                    .getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
                ; // In search result, don't show the change quantity work order
            // activities
            else
                activityList.add(woa); // Only add, original woa or woa that are
        // created for Non tendered and lumpsum
        // activities

        return ACTIVITY_SEARCH;
    }

    protected RevisionWorkOrder createRevisionWO(final String curStatus) {

        RevisionWorkOrder revisionWO = new RevisionWorkOrder();
        if (revisionWorkOrder != null && revisionWorkOrder.getId() != null)
            revisionWO = revisionWorkOrder;
        else {
            revisionWO.setParent(workOrder);
            revisionWO.setWorkOrderDate(revisionEstimate.getEstimateDate());
            revisionWO.setWorkOrderNumber(workOrder.getWorkOrderNumber() + "/RW".concat(Integer.toString(reCount)));
            revisionWO.setContractor(workOrder.getContractor());
            revisionWO.setEngineerIncharge(workOrder.getEngineerIncharge());
            revisionWO.setEmdAmountDeposited(0.00001);
            revisionWO.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("WorkOrder", "NEW"));
        }

        if (parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Approve"))
            revisionWO.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("WorkOrder", "APPROVED"));

        if (parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Cancel"))
            revisionWO.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("WorkOrder", "CANCELLED"));

        if (curStatus.equals("NEW") || curStatus.equals("REJECTED"))
            populateWorkOrderActivities(revisionWO);
        return revisionWO;
    }

    protected void populateWorkOrderActivities(final RevisionWorkOrder revisionWO) {
        final WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
        workOrderEstimate.setEstimate(revisionEstimate);
        workOrderEstimate.setWorkOrder(revisionWO);
        addWorkOrderEstimateActivities(workOrderEstimate, revisionWO);
        revisionWO.addWorkOrderEstimate(workOrderEstimate);
    }

    private void addWorkOrderEstimateActivities(final WorkOrderEstimate workOrderEstimate,
            final RevisionWorkOrder revisionWO) {
        double woTotalAmount = 0;
        double approvedAmount = 0;
        final TenderResponse tenderResponse = (TenderResponse) persistenceService.find(
                "from TenderResponse tr where tr.egwStatus.code = 'APPROVED' and tr.negotiationNumber = ?", revisionWO
                        .getParent().getNegotiationNumber());
        for (final Activity activity : revisionEstimate.getActivities()) {
            final WorkOrderActivity workOrderActivity = new WorkOrderActivity();
            workOrderActivity.setActivity(activity);
            approvedAmount = 0;
            if (activity != null
                    && activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()) || activity.getRevisionType()
                                    .toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString())))
                workOrderActivity.setApprovedRate(activity.getRate()
                        / activity.getConversionFactorForRE(workOrder.getWorkOrderDate()));
            else if (activity != null
                    && activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) || activity
                                    .getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
                if (tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(WorksConstants.PERC_TENDER))
                    workOrderActivity.setApprovedRate(activity.getRate()
                            / activity.getConversionFactorForRE(revisionEstimate.getParent().getEstimateDate()));
                else {
                    final WorkOrderActivity parentWOA = (WorkOrderActivity) persistenceService
                            .find("from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.egwStatus.code = ? and woa.activity.id = ? ",
                                    WorksConstants.APPROVED, activity.getParent().getId());
                    workOrderActivity.setApprovedRate(parentWOA.getApprovedRate());
                }
            workOrderActivity.setApprovedQuantity(activity.getQuantity());
            approvedAmount = new Money(activity.getRate() * workOrderActivity.getApprovedQuantity())
                    .getValue();
            // If it is a reduced quantity, then the work order activity's
            // amount needs to be negative, else the RevWO amount will always
            // keep
            // increasing even if reduction quantities are present in the RE
            if (activity.getRevisionType() != null && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
                approvedAmount = approvedAmount * -1;

            // Apply percentage for change quantity items in case of percentage
            // tender
            if (activity != null
                    && activity.getRevisionType() != null
                    && (activity.getRevisionType().toString()
                            .equalsIgnoreCase(RevisionType.ADDITIONAL_QUANTITY.toString()) || activity
                                    .getRevisionType().toString().equalsIgnoreCase(RevisionType.REDUCED_QUANTITY.toString())))
                if (tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(WorksConstants.PERC_TENDER))
                    approvedAmount = approvedAmount + approvedAmount * tenderResponse.getPercNegotiatedAmountRate()
                            / 100;

            woTotalAmount = woTotalAmount + approvedAmount;
            workOrderActivity.setApprovedAmount(approvedAmount);
            workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
            workOrderEstimate.addWorkOrderActivity(workOrderActivity);
        }
        workOrderEstimate.getWorkOrder().setWorkOrderAmount(woTotalAmount);
    }

    public String loadSearchForActivity() {
        return ACTIVITY_SEARCH;
    }

    protected void populateSorActivities(final AbstractEstimate abstractEstimate) {
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

    protected void populateNonSorActivities(final AbstractEstimate abstractEstimate) {
        for (final Activity activity : nonSorActivities)
            if (activity != null) {
                activity.setUom(activity.getNonSor().getUom());
                if (activity.getNonSor().getId() != null && activity.getNonSor().getId() != 0
                        && activity.getNonSor().getId() != 1) {
                    final NonSor nonsor = (NonSor) getPersistenceService().find("from NonSor where id = ?",
                            activity.getNonSor().getId());
                    if (nonsor == null) {// In case of error on save of
                        // estimate, the nonsor is not yet
                        // persisted .
                        activity.getNonSor().setId(null);// To clear the id
                        // which got created
                        // through previous
                        // persist
                        nonSorService.persist(activity.getNonSor());
                    } else
                        activity.setNonSor(nonsor);

                } else if (activity.getNonSor() != null)
                    nonSorService.persist(activity.getNonSor());

                abstractEstimate.addActivity(activity);
            }
    }

    private void populateActivities(final AbstractEstimate abstractEstimate) {
        int count = 1;
        for (final Activity activity : abstractEstimate.getActivities()) {
            activity.setAbstractEstimate(abstractEstimate);
            if (activity.getSchedule() != null)
                activity.setRevisionType(RevisionType.NON_TENDERED_ITEM);
            else
                activity.setRevisionType(RevisionType.LUMP_SUM_ITEM);

            if (activity.getSrlNo() != null)
                activity.setSrlNo(count++);
        }
    }

    public void getDesignation(final RevisionAbstractEstimate revisionEstimate) {
        /* start for customizing workflow message display */
        if (revisionEstimate.getEgwStatus() != null
                && !"NEW".equalsIgnoreCase(revisionEstimate.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(revisionEstimate.getState().getOwnerPosition(),
                    revisionEstimate.getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setEmployeeName(empName);
                setDesignation(designation);
            }
        }
        /* end */
    }

    private void validateNonSorActivities() {
        final Set<String> exceptionSor = worksService.getExceptionSOR().keySet();
        for (final Activity activity : nonSorActivities)
            if (activity != null && activity.getNonSor().getUom() != null) {
                final UOM nonSorUom = (UOM) getPersistenceService().find("from UOM where id = ?",
                        activity.getNonSor().getUom().getId());
                if (nonSorUom != null && exceptionSor.contains(nonSorUom.getUom()))
                    throw new ValidationException(Arrays.asList(new ValidationError("validate.nonSor.uom",
                            "validate.nonSor.uom")));
            }
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public RevisionAbstractEstimate getRevisionEstimate() {
        return revisionEstimate;
    }

    public void setRevisionEstimate(final RevisionAbstractEstimate revisionEstimate) {
        this.revisionEstimate = revisionEstimate;
    }

    @Override
    protected String getPendingActions() {
        return getModel().getState() != null ? getModel().getState().getNextAction() : "";
    }

    public void setRevisionEstimateWorkflowService(final WorkflowService<RevisionAbstractEstimate> workflowService) {
        this.workflowService = workflowService;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
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

    public String getAdditionalRuleValue() {
        additionalRuleValue = getAdditionalRule();
        return additionalRuleValue;
    }

    public void setAdditionalRuleValue(final String additionalRuleValue) {
        this.additionalRuleValue = additionalRuleValue;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setEmployeeService(final EmployeeServiceOld employeeService) {
        this.employeeService = employeeService;
    }

    public AbstractEstimateService getAbstractEstimateService() {
        return abstractEstimateService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setRevisionAbstractEstimateService(
            final PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
        this.revisionAbstractEstimateService = revisionAbstractEstimateService;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public WorkOrderService getWorkOrderService() {
        return workOrderService;
    }

    public List<Activity> getOriginalRevisedActivityList() {
        return originalRevisedActivityList;
    }

    public void setOriginalRevisedActivityList(final List<Activity> originalRevisedActivityList) {
        this.originalRevisedActivityList = originalRevisedActivityList;
    }

    public double getOriginalTotalAmount() {
        return originalTotalAmount;
    }

    public void setOriginalTotalAmount(final double originalTotalAmount) {
        this.originalTotalAmount = originalTotalAmount;
    }

    public double getOriginalTotalTax() {
        return originalTotalTax;
    }

    public void setOriginalTotalTax(final double originalTotalTax) {
        this.originalTotalTax = originalTotalTax;
    }

    public double getOriginalWorkValueIncludingTax() {
        return originalWorkValueIncludingTax;
    }

    public void setOriginalWorkValueIncludingTax(final double originalWorkValueIncludingTax) {
        this.originalWorkValueIncludingTax = originalWorkValueIncludingTax;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public Long getOriginalEstimateId() {
        return originalEstimateId;
    }

    public void setOriginalEstimateId(final Long originalEstimateId) {
        this.originalEstimateId = originalEstimateId;
    }

    public double getRevisionEstimatesValue() {
        return revisionEstimatesValue;
    }

    public void setRevisionEstimatesValue(final double revisionEstimatesValue) {
        this.revisionEstimatesValue = revisionEstimatesValue;
    }

    public double getRevisionWOValue() {
        return revisionWOValue;
    }

    public void setRevisionWOValue(final double revisionWOValue) {
        this.revisionWOValue = revisionWOValue;
    }

    public List<RevisionAbstractEstimate> getReList() {
        return reList;
    }

    public void setReList(final List<RevisionAbstractEstimate> reList) {
        this.reList = reList;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Long getOriginalWOId() {
        return originalWOId;
    }

    public void setOriginalWOId(final Long originalWOId) {
        this.originalWOId = originalWOId;
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

    public PersistenceService<NonSor, Long> getNonSorService() {
        return nonSorService;
    }

    public void setNonSorService(final PersistenceService<NonSor, Long> nonSorService) {
        this.nonSorService = nonSorService;
    }

    public String getEstimateValue() {
        return estimateValue;
    }

    public void setEstimateValue(final String estimateValue) {
        this.estimateValue = estimateValue;
    }

    public RevisionWorkOrder getRevisionWorkOrder() {
        return revisionWorkOrder;
    }

    public void setRevisionWorkOrder(final RevisionWorkOrder revisionWorkOrder) {
        this.revisionWorkOrder = revisionWorkOrder;
    }

    public void setRevisionWorkOrderService(final PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
        this.revisionWorkOrderService = revisionWorkOrderService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public Integer getApproverPositionId() {
        return approverPositionId;
    }

    public void setApproverPositionId(final Integer approverPositionId) {
        this.approverPositionId = approverPositionId;
    }

    public Long getLogedInUserDept() {
        return logedInUserDept;
    }

    public void setLogedInUserDept(final Long logedInUserDept) {
        this.logedInUserDept = logedInUserDept;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public List<WorkOrderActivity> getActivityList() {
        return activityList;
    }

    public void setActivityCode(final String activityCode) {
        this.activityCode = activityCode;
    }

    public void setActivityDesc(final String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public void setActivityList(final List<WorkOrderActivity> activityList) {
        this.activityList = activityList;
    }

    public String getWorkorderNo() {
        return workorderNo;
    }

    public void setWorkorderNo(final String workorderNo) {
        this.workorderNo = workorderNo;
    }

    public List<WorkOrderActivity> getChangeQuantityActivities() {
        return changeQuantityActivities;
    }

    public void setChangeQuantityActivities(final List<WorkOrderActivity> changeQuantityActivities) {
        this.changeQuantityActivities = changeQuantityActivities;
    }

    public void setRevisionEstimateService(final RevisionEstimateService revisionEstimateService) {
        this.revisionEstimateService = revisionEstimateService;
    }

    public boolean getIsBudgetRejectionNoPresent() {
        return isBudgetRejectionNoPresent;
    }

    public BigDecimal getAmountRuleValue() {
        amountRuleValue = getAmountRule();
        return amountRuleValue;
    }

    public void setAmountRuleValue(final BigDecimal amountRuleValue) {
        this.amountRuleValue = amountRuleValue;
    }
}