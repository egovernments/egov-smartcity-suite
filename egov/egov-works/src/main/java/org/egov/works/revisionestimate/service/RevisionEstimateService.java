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
package org.egov.works.revisionestimate.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.UOMService;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.abstractestimate.repository.ActivityRepository;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.masters.service.ScheduleCategoryService;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBDetailsService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate.RevisionEstimateStatus;
import org.egov.works.revisionestimate.entity.RevisionWorkOrder;
import org.egov.works.revisionestimate.entity.SearchRevisionEstimate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.revisionestimate.repository.RevisionEstimateRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.entity.WorkOrderMeasurementSheet;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.egov.works.workorder.service.WorkOrderMeasurementSheetService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.google.gson.JsonObject;

@Service
@Transactional(readOnly = true)
public class RevisionEstimateService {

    private static final Logger LOG = LoggerFactory.getLogger(RevisionEstimateService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final RevisionEstimateRepository revisionEstimateRepository;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<RevisionAbstractEstimate> revisionAbstractEstimateWorkflowService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private UOMService uomService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private RevisionWorkOrderService revisionWorkOrderService;

    @Autowired
    private WorkOrderMeasurementSheetService workOrderMeasurementSheetService;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private MBDetailsService mBDetailsService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    public RevisionEstimateService(final RevisionEstimateRepository revisionEstimateRepository) {
        this.revisionEstimateRepository = revisionEstimateRepository;
    }

    public List<RevisionAbstractEstimate> findApprovedRevisionEstimatesByParent(final Long id) {
        return revisionEstimateRepository.findByParent_IdAndStatus(id,
                RevisionAbstractEstimate.RevisionEstimateStatus.APPROVED.toString());
    }

    public List<RevisionAbstractEstimate> findApprovedRevisionEstimatesByParentForView(final Long id, final Long reId) {
        return revisionEstimateRepository.findByParent_IdAndStatusForView(id, reId,
                RevisionAbstractEstimate.RevisionEstimateStatus.APPROVED.toString());
    }

    public RevisionAbstractEstimate getRevisionEstimateById(final Long id) {
        return revisionEstimateRepository.findOne(id);
    }

    public List<User> getRevisionEstimateCreatedByUsers() {
        return revisionEstimateRepository.findRevisionEstimateCreatedByUsers();
    }

    public List<String> getRevisionEstimateByEstimateNumberLike(final String revisionEstimateNumber) {
        return revisionEstimateRepository.findDistinctEstimateNumberContainingIgnoreCase("%" + revisionEstimateNumber + "%");
    }

    @Transactional
    public RevisionAbstractEstimate createRevisionEstimate(final RevisionAbstractEstimate revisionEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {

        mergeSorAndNonSorActivities(revisionEstimate);
        final AbstractEstimate abstractEstimate = revisionEstimate.getParent();
        final List<RevisionAbstractEstimate> revisionEstimates = revisionEstimateRepository
                .findByParent_Id(abstractEstimate.getId());

        Integer reCount = revisionEstimates.size();
        if (revisionEstimate.getId() != null)
            reCount = reCount - 1;
        if (revisionEstimate.getId() == null) {
            revisionEstimate.setParent(abstractEstimate);
            revisionEstimate.setEstimateDate(new Date());
            revisionEstimate.setEstimateNumber(abstractEstimate.getEstimateNumber()
                    + "/RE".concat(Integer.toString(++reCount)));
            revisionEstimate.setName("Revision Estimate for: " + abstractEstimate.getName());
            revisionEstimate.setDescription("Revision Estimate for: " + abstractEstimate.getDescription());
            revisionEstimate.setNatureOfWork(abstractEstimate.getNatureOfWork());
            revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
            revisionEstimate.setWard(abstractEstimate.getWard());
            revisionEstimate.setParentCategory(abstractEstimate.getParentCategory());
        }

        mergeChangeQuantityActivities(revisionEstimate);

        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getParent().getId());

        revisionEstimateRepository.save(revisionEstimate);

        if (WorksConstants.CREATE_AND_APPROVE.toString().equalsIgnoreCase(workFlowAction))
            doBudgetoryAppropriation(workFlowAction, revisionEstimate);

        if (WorksConstants.CREATE_AND_APPROVE.toString().equalsIgnoreCase(workFlowAction)) {
            createRevisionWorkOrder(workOrderEstimate, revisionEstimate);
            revisionEstimate.getParent().setTotalIncludingRE(
                    revisionEstimate.getParent().getTotalIncludingRE() + revisionEstimate.getWorkValue());
        }

        createRevisionEstimateWorkflowTransition(revisionEstimate, approvalPosition, approvalComent, additionalRule,
                workFlowAction);

        revisionEstimateRepository.save(revisionEstimate);

        return revisionEstimate;
    }

    private void doBudgetoryAppropriation(final String workFlowAction, final RevisionAbstractEstimate revisionEstimate) {
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(revisionEstimate.getParent().getFinancialDetails().get(0).getBudgetGroup().getId());
        final boolean flag = estimateAppropriationService.checkConsumeEncumbranceBudgetForRevisionEstimate(
                revisionEstimate,
                worksUtils.getFinancialYearByDate(new Date()).getId(),
                revisionEstimate.getEstimateValue().doubleValue(),
                budgetheadid);

        if (!flag)
            throw new ValidationException(org.apache.commons.lang.StringUtils.EMPTY,
                    "error.budgetappropriation.insufficient.amount");

    }

    private void removeEmptyMS(final Activity activity) {
        final List<MeasurementSheet> toRemove = new LinkedList<MeasurementSheet>();
        for (final MeasurementSheet ms : activity.getMeasurementSheetList())
            if (ms.getQuantity() == null || ms.getQuantity() != null && ms.getQuantity().equals(""))
                toRemove.add(ms);

        for (final MeasurementSheet msremove : toRemove)
            activity.getMeasurementSheetList().remove(msremove);
    }

    @Transactional
    public RevisionAbstractEstimate updateRevisionEstimate(final RevisionAbstractEstimate revisionEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String removedActivityIds, final WorkOrderEstimate workOrderEstimate)
            throws ValidationException, IOException {

        RevisionAbstractEstimate updateRevisionEstimate = null;

        if ((EstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode())
                || EstimateStatus.REJECTED.toString().equals(revisionEstimate.getEgwStatus().getCode()))
                && !WorksConstants.CANCEL_ACTION.equals(workFlowAction)) {

            mergeSorAndNonSorActivities(revisionEstimate);
            mergeChangeQuantityActivities(revisionEstimate);

            List<Activity> activities = new ArrayList<Activity>(revisionEstimate.getActivities());
            activities = removeDeletedActivities(activities, removedActivityIds);
            revisionEstimate.setActivities(activities);
        }
        if ((WorksConstants.APPROVE_ACTION.toString().equalsIgnoreCase(workFlowAction)
                || WorksConstants.CREATE_AND_APPROVE.toString().equalsIgnoreCase(workFlowAction))
                && !BudgetControlType.BudgetCheckOption.NONE.toString()
                        .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
            doBudgetoryAppropriation(workFlowAction, revisionEstimate);
        revisionEstimate
                .setApprovedBy(entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
        revisionEstimate.setApprovedDate(new Date());
        updateRevisionEstimate = revisionEstimateRepository.save(revisionEstimate);

        revisionEstimateStatusChange(updateRevisionEstimate, workFlowAction);

        createRevisionEstimateWorkflowTransition(updateRevisionEstimate, approvalPosition, approvalComent, additionalRule,
                workFlowAction);

        if (WorksConstants.APPROVE_ACTION.toString().equalsIgnoreCase(workFlowAction)
                || WorksConstants.CREATE_AND_APPROVE.toString().equalsIgnoreCase(workFlowAction)) {
            createRevisionWorkOrder(workOrderEstimate, updateRevisionEstimate);
            updateRevisionEstimate.getParent().setTotalIncludingRE(
                    updateRevisionEstimate.getParent().getTotalIncludingRE() + updateRevisionEstimate.getWorkValue());
        }

        revisionEstimateRepository.save(updateRevisionEstimate);

        return updateRevisionEstimate;
    }

    private void createRevisionWorkOrder(final WorkOrderEstimate workOrderEstimate,
            final RevisionAbstractEstimate updateRevisionEstimate) {
        RevisionWorkOrder revisionWorkOrder = new RevisionWorkOrder();
        revisionWorkOrder = createRevisionWorkOrder(updateRevisionEstimate, revisionWorkOrder, workOrderEstimate);
        revisionWorkOrderService.create(revisionWorkOrder);
    }

    private RevisionWorkOrder createRevisionWorkOrder(final RevisionAbstractEstimate revisionEstimate,
            final RevisionWorkOrder revisionWorkOrder, final WorkOrderEstimate workOrderEstimate) {

        final List<RevisionAbstractEstimate> revisionEstimates = revisionEstimateRepository
                .findByParent_Id(revisionEstimate.getParent().getId());

        Integer reCount = revisionEstimates.size();
        revisionWorkOrder.setParent(workOrderEstimate.getWorkOrder());
        revisionWorkOrder.setWorkOrderDate(revisionEstimate.getEstimateDate());
        revisionWorkOrder.setWorkOrderNumber(
                workOrderEstimate.getWorkOrder().getWorkOrderNumber() + "/RW".concat(Integer.toString(++reCount)));
        revisionWorkOrder.setContractor(workOrderEstimate.getWorkOrder().getContractor());
        revisionWorkOrder.setEngineerIncharge(workOrderEstimate.getWorkOrder().getEngineerIncharge());
        revisionWorkOrder.setEmdAmountDeposited(0);
        revisionWorkOrder.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.WORKORDER, WorksConstants.APPROVED));
        revisionWorkOrder.setApprovedDate(new Date());
        populateWorkOrderActivities(revisionWorkOrder, revisionEstimate);

        revisionWorkOrder.getParent().setTotalIncludingRE(
                revisionWorkOrder.getParent().getTotalIncludingRE() + revisionWorkOrder.getWorkOrderAmount());
        return revisionWorkOrder;
    }

    protected void populateWorkOrderActivities(final RevisionWorkOrder revisionWorkOrder,
            final RevisionAbstractEstimate revisionEstimate) {
        final WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
        workOrderEstimate.setEstimate(revisionEstimate);
        workOrderEstimate.setWorkOrder(revisionWorkOrder);
        addWorkOrderEstimateActivities(workOrderEstimate, revisionWorkOrder, revisionEstimate);
        revisionWorkOrder.addWorkOrderEstimate(workOrderEstimate);
    }

    private void addWorkOrderEstimateActivities(final WorkOrderEstimate workOrderEstimate,
            final RevisionWorkOrder revisionWorkOrder, final RevisionAbstractEstimate revisionEstimate) {
        double woTotalAmount = 0;
        double approvedAmount = 0;
        final Double tenderFinalizedPercentage = revisionWorkOrder.getParent().getTenderFinalizedPercentage();
        for (final Activity activity : revisionEstimate.getActivities()) {
            final WorkOrderActivity workOrderActivity = new WorkOrderActivity();
            workOrderActivity.setActivity(activity);
            populateWorkOrderMeasurementSheet(workOrderActivity);
            approvedAmount = 0;
            if (activity != null
                    && activity.getRevisionType() != null
                    && (RevisionType.NON_TENDERED_ITEM.toString()
                            .equalsIgnoreCase(activity.getRevisionType().toString())
                            || RevisionType.LUMP_SUM_ITEM.toString().equalsIgnoreCase(activity.getRevisionType()
                                    .toString())))
                workOrderActivity.setApprovedRate(activity.getRate());
            else if (activity != null
                    && activity.getRevisionType() != null
                    && (RevisionType.ADDITIONAL_QUANTITY.toString()
                            .equalsIgnoreCase(activity.getRevisionType().toString())
                            || RevisionType.REDUCED_QUANTITY.toString().equalsIgnoreCase(activity
                                    .getRevisionType().toString())))
                if (!tenderFinalizedPercentage.equals(Double.valueOf(0)))
                    workOrderActivity.setApprovedRate(activity.getRate() + activity.getRate() * tenderFinalizedPercentage / 100);
                else
                    workOrderActivity.setApprovedRate(activity.getRate());
            workOrderActivity.setApprovedQuantity(activity.getQuantity());
            approvedAmount = new Money(activity.getRate() * workOrderActivity.getApprovedQuantity())
                    .getValue();
            // If it is a reduced quantity, then the work order activity's
            // amount needs to be negative, else the RevWO amount will always
            // keep
            // increasing even if reduction quantities are present in the RE
            if (activity.getRevisionType() != null && RevisionType.REDUCED_QUANTITY.equals(activity.getRevisionType()))
                approvedAmount = approvedAmount * -1;

            // Apply percentage for change quantity items for tender items in case of percentage
            // tender
            if (activity != null && activity.getParent() != null
                    && activity.getParent().getRevisionType() == null
                    && (RevisionType.ADDITIONAL_QUANTITY.toString()
                            .equalsIgnoreCase(activity.getRevisionType().toString())
                            || RevisionType.REDUCED_QUANTITY.toString().equalsIgnoreCase(activity
                                    .getRevisionType().toString())))
                if (!tenderFinalizedPercentage.equals(Double.valueOf(0)))
                    approvedAmount = approvedAmount + approvedAmount * tenderFinalizedPercentage / 100;

            woTotalAmount = woTotalAmount + approvedAmount;
            workOrderActivity.setApprovedAmount(approvedAmount);
            workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
            workOrderEstimate.addWorkOrderActivity(workOrderActivity);
        }
        workOrderEstimate.getWorkOrder().setWorkOrderAmount(woTotalAmount);
        workOrderEstimate.setEstimateWOAmount(woTotalAmount);
    }

    private void populateWorkOrderMeasurementSheet(final WorkOrderActivity workOrderActivity) {
        WorkOrderMeasurementSheet workOrderMeasurementSheet;
        for (final MeasurementSheet mSheet : workOrderActivity.getActivity().getMeasurementSheetList()) {
            workOrderMeasurementSheet = new WorkOrderMeasurementSheet();
            workOrderMeasurementSheet.setNo(mSheet.getNo());
            workOrderMeasurementSheet.setLength(mSheet.getLength());
            workOrderMeasurementSheet.setWidth(mSheet.getWidth());
            workOrderMeasurementSheet.setDepthOrHeight(mSheet.getDepthOrHeight());
            workOrderMeasurementSheet.setQuantity(mSheet.getQuantity());
            workOrderMeasurementSheet.setMeasurementSheet(mSheet);
            workOrderMeasurementSheet.setWoActivity(workOrderActivity);
            workOrderActivity.getWorkOrderMeasurementSheets().add(workOrderMeasurementSheet);
        }
    }

    private List<Activity> removeDeletedActivities(final List<Activity> activities, final String removedActivityIds) {
        final List<Activity> activityList = new ArrayList<Activity>();
        if (null != removedActivityIds) {
            final String[] ids = removedActivityIds.split(",");
            final List<String> strList = new ArrayList<String>();
            for (final String str : ids)
                strList.add(str);
            for (final Activity activity : activities)
                if (activity.getId() != null) {
                    if (!strList.contains(activity.getId().toString()))
                        activityList.add(activity);
                } else
                    activityList.add(activity);
        } else
            return activities;
        return activityList;
    }

    public void createRevisionEstimateWorkflowTransition(final RevisionAbstractEstimate revisionEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        WorkFlowMatrix wfmatrix = null;

        if (null != revisionEstimate.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(revisionEstimate.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment))
                revisionEstimate.transition().progressWithStateCopy().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            else
                revisionEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.WF_STATE_REJECTED)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition()).withNextAction("")
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
        } else if (WorksConstants.SAVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (revisionEstimate.getState() == null)
                revisionEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(WorksConstants.ESTIMATE_ONSAVE_NEXTACTION_VALUE)
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0))
                    && !WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)
                    && !WorksConstants.APPROVE_ACTION.toString().equalsIgnoreCase(workFlowAction)
                    && !WorksConstants.CREATE_AND_APPROVE.equalsIgnoreCase(workFlowAction))
                pos = positionMasterService.getPositionById(approvalPosition);
            if (null == revisionEstimate.getState()) {
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null,
                        revisionEstimate.getEstimateValue(), additionalRule, currState, null);
                revisionEstimate.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                revisionEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("")
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            } else if (WorksConstants.APPROVE_ACTION.toString().equalsIgnoreCase(workFlowAction)
                    || WorksConstants.CREATE_AND_APPROVE.equalsIgnoreCase(workFlowAction)) {
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null,
                        revisionEstimate.getEstimateValue(),
                        additionalRule, revisionEstimate.getCurrentState().getValue(),
                        revisionEstimate.getCurrentState().getNextAction());
                revisionEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            } else {
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null,
                        revisionEstimate.getEstimateValue(),
                        additionalRule, revisionEstimate.getCurrentState().getValue(),
                        revisionEstimate.getCurrentState().getNextAction());
                revisionEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            }
        }
    }

    private void mergeSorAndNonSorActivities(final RevisionAbstractEstimate revisionEstimate) {
        for (final Activity activity : revisionEstimate.getNonTenderedActivities())
            if (activity.getId() == null) {
                activity.setAbstractEstimate(revisionEstimate);
                activity.setRevisionType(RevisionType.NON_TENDERED_ITEM);
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getSORActivities())
                    if (oldActivity.getId().equals(activity.getId()))
                        updateActivity(oldActivity, activity);
        for (final Activity activity : revisionEstimate.getLumpSumActivities())
            if (activity.getId() == null) {
                activity.setAbstractEstimate(revisionEstimate);
                activity.setRevisionType(RevisionType.LUMP_SUM_ITEM);
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getNonSORActivities())
                    if (oldActivity.getId().equals(activity.getId()))
                        updateActivity(oldActivity, activity);
        if (LOG.isDebugEnabled())
            for (final Activity ac : revisionEstimate.getActivities())
                LOG.debug(ac.getMeasurementSheetList().size() + "    " + ac.getQuantity());

        for (final Activity ac : revisionEstimate.getNonTenderedActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);

        for (final Activity ac : revisionEstimate.getLumpSumActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);
    }

    private void mergeChangeQuantityActivities(final RevisionAbstractEstimate revisionEstimate) {

        for (final Activity activity : revisionEstimate.getChangeQuantityActivities())
            if (activity.getId() == null) {
                removeEmptyMS(activity);
                final Activity act = activityRepository.findOne(activity.getParent().getId());
                activity.setParent(act);
                activity.setAbstractEstimate(revisionEstimate);
                activity.setSchedule(act.getSchedule());
                activity.setNonSor(act.getNonSor());
                activity.setUom(act.getUom());
                activity.setRate(act.getRate());
                if ("-".equals(activity.getSignValue()))
                    activity.setRevisionType(RevisionType.REDUCED_QUANTITY);
                else
                    activity.setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
                activity.setEstimateRate(act.getEstimateRate());

                for (final MeasurementSheet ms : activity.getMeasurementSheetList()) {
                    final MeasurementSheet sheet = measurementSheetService.findOne(ms.getParent().getId());
                    ms.setActivity(activity);
                    ms.setParent(sheet);
                    ms.setIdentifier(sheet.getIdentifier());
                    ms.setRemarks(sheet.getRemarks());
                    ms.setSlNo(sheet.getSlNo());
                }
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getActivities())
                    if (oldActivity.getId() != null && oldActivity.getId().equals(activity.getId()))
                        updateChangeQuantityActivity(oldActivity, activity);
    }

    private void updateChangeQuantityActivity(final Activity oldActivity, final Activity activity) {
        final Activity parent = activityRepository.findOne(activity.getParent().getId());
        oldActivity.setParent(parent);
        oldActivity.setSchedule(parent.getSchedule());
        oldActivity.setAmt(activity.getAmt());
        oldActivity.setNonSor(parent.getNonSor());
        oldActivity.setQuantity(activity.getQuantity());
        oldActivity.setRate(parent.getRate());
        oldActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
        oldActivity.setEstimateRate(parent.getEstimateRate());
        oldActivity.setUom(parent.getUom());
        oldActivity.setMeasurementSheetList(mergeCQMeasurementSheet(oldActivity, activity));
        if ("+".equals(activity.getSignValue()))
            oldActivity.setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
        else
            oldActivity.setRevisionType(RevisionType.REDUCED_QUANTITY);
    }

    private List<MeasurementSheet> mergeMeasurementSheet(final Activity oldActivity, final Activity activity) {
        final List<MeasurementSheet> newMsList = new LinkedList<MeasurementSheet>(
                oldActivity.getMeasurementSheetList());
        for (final MeasurementSheet msnew : activity.getMeasurementSheetList()) {
            if (msnew.getId() == null && msnew.getQuantity() != null) {
                msnew.setActivity(oldActivity);
                oldActivity.getMeasurementSheetList().add(msnew);
                continue;
            }

            for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList())
                if (msnew.getId().longValue() == msold.getId().longValue()) {
                    msold.setLength(msnew.getLength());
                    msold.setWidth(msnew.getWidth());
                    msold.setDepthOrHeight(msnew.getDepthOrHeight());
                    msold.setNo(msnew.getNo());
                    msold.setActivity(msnew.getActivity());
                    if (msnew.getParent() != null) {
                        msold.setIdentifier(msnew.getParent().getIdentifier());
                        msold.setRemarks(msnew.getParent().getRemarks());
                        msold.setSlNo(msnew.getParent().getSlNo());
                    }
                    msold.setQuantity(msnew.getQuantity());
                    newMsList.add(msold);
                }

        }
        final List<MeasurementSheet> toRemove = new LinkedList<MeasurementSheet>();
        for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList()) {
            Boolean found = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug(oldActivity.getMeasurementSheetList().size() + "activity.getMeasurementSheetList()");
                LOG.debug(msold.getId() + "------msold.getId()");
            }
            if (msold.getId() == null)
                continue;

            for (final MeasurementSheet msnew : activity.getMeasurementSheetList())
                if (msnew.getId() == null) {
                    // found=true;
                } else if (msnew.getId().longValue() == msold.getId().longValue()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(msnew.getId() + "------msnew.getId()");
                        LOG.debug(msnew.getRemarks() + "------remarks");
                    }

                    found = true;
                }

            if (!found)
                toRemove.add(msold);

        }

        for (final MeasurementSheet msremove : toRemove) {
            if (LOG.isInfoEnabled())
                LOG.info("...........Removing rows....................Of MeasurementSheet" + msremove.getId());
            oldActivity.getMeasurementSheetList().remove(msremove);
        }

        removeEmptyMS(oldActivity);
        return oldActivity.getMeasurementSheetList();

    }

    private List<MeasurementSheet> mergeCQMeasurementSheet(final Activity oldActivity, final Activity activity) {
        for (final MeasurementSheet msnew : activity.getMeasurementSheetList()) {
            if (msnew.getId() == null && msnew.getQuantity() != null) {
                msnew.setActivity(oldActivity);
                if (msnew.getParent() != null)
                    msnew.setIdentifier(msnew.getParent().getIdentifier());
                oldActivity.getMeasurementSheetList().add(msnew);
                continue;
            }
            for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList())
                if (msnew.getId() == msold.getId()) {
                    msold.setLength(msnew.getLength());
                    msold.setWidth(msnew.getWidth());
                    msold.setDepthOrHeight(msnew.getDepthOrHeight());
                    msold.setNo(msnew.getNo());
                    msold.setIdentifier(msnew.getParent().getIdentifier());
                    msold.setRemarks(msnew.getParent().getRemarks());
                    msold.setSlNo(msnew.getParent().getSlNo());
                    msold.setQuantity(msnew.getQuantity());
                    msold.setActivity(oldActivity);
                }
        }

        removeEmptyMS(oldActivity);
        return oldActivity.getMeasurementSheetList();
    }

    private void updateActivity(final Activity oldActivity, final Activity activity) {
        oldActivity.setSchedule(activity.getSchedule());
        oldActivity.setAmt(activity.getAmt());
        oldActivity.setNonSor(activity.getNonSor());
        oldActivity.setQuantity(activity.getQuantity());
        oldActivity.setRate(activity.getRate());
        oldActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
        oldActivity.setEstimateRate(activity.getEstimateRate());
        oldActivity.setUom(activity.getUom());
        oldActivity.setMeasurementSheetList(mergeMeasurementSheet(oldActivity, activity));
    }

    public void populateHeaderActivities(final RevisionAbstractEstimate revisionEstimate,
            final List<RevisionAbstractEstimate> revisionAbstractEstimates, final Model model) {
        // Adding Original Activities
        final List<Activity> sorActivities = new ArrayList<>(revisionEstimate.getParent().getSORActivities());
        final List<Activity> nonSorActivities = new ArrayList<>(revisionEstimate.getParent().getNonSORActivities());

        final List<Activity> nonTenderedActivities = new ArrayList<>();
        final List<Activity> lumpSumActivities = new ArrayList<>();

        final Map<Long, String> previousEstimates = new HashMap<>();

        Boolean measurementsPresent = false;

        // Populating Revision Estimate NonTendered and Lump Sum Activities
        for (final RevisionAbstractEstimate re : revisionAbstractEstimates) {
            for (final Activity activity : re.getActivities())
                if (activity.getParent() == null && activity.getRevisionType() != null
                        && RevisionType.NON_TENDERED_ITEM.equals(activity.getRevisionType()))
                    nonTenderedActivities.add(activity);
                else if (activity.getParent() == null && activity.getRevisionType() != null
                        && RevisionType.LUMP_SUM_ITEM.equals(activity.getRevisionType()))
                    lumpSumActivities.add(activity);
            previousEstimates.put(re.getId(), re.getEstimateNumber());
            if (!measurementsPresent)
                measurementsPresent = measurementSheetService.existsByEstimate(re.getId());
        }

        // Populating Revision Estimate Change Quantity Activities
        for (final RevisionAbstractEstimate re : revisionAbstractEstimates)
            for (final Activity activity : re.getActivities())
                if (activity.getParent() != null && activity.getSchedule() != null)
                    populateChangeQuantityActivities(activity, sorActivities, nonTenderedActivities);
                else if (activity.getParent() != null && activity.getSchedule() == null)
                    populateChangeQuantityActivities(activity, nonSorActivities, lumpSumActivities);
        if (!revisionAbstractEstimates.isEmpty()) {
            for (final Activity sa : sorActivities)
                if (!sa.getMeasurementSheetList().isEmpty())
                    for (final MeasurementSheet ms : sa.getMeasurementSheetList())
                        deriveMeasurementSheetQuantity(ms, revisionEstimate.getId());

            for (final Activity sa : nonSorActivities)
                if (!sa.getMeasurementSheetList().isEmpty())
                    for (final MeasurementSheet ms : sa.getMeasurementSheetList())
                        deriveMeasurementSheetQuantity(ms, revisionEstimate.getId());

            for (final Activity sa : nonTenderedActivities)
                if (!sa.getMeasurementSheetList().isEmpty())
                    for (final MeasurementSheet ms : sa.getMeasurementSheetList())
                        deriveMeasurementSheetQuantity(ms, revisionEstimate.getId());

            for (final Activity sa : lumpSumActivities)
                if (!sa.getMeasurementSheetList().isEmpty())
                    for (final MeasurementSheet ms : sa.getMeasurementSheetList())
                        deriveMeasurementSheetQuantity(ms, revisionEstimate.getId());
        }
        revisionEstimate.getSorActivities().addAll(sorActivities);
        revisionEstimate.getNonSorActivities().addAll(nonSorActivities);
        revisionEstimate.getChangeQuantityNTActivities().addAll(nonTenderedActivities);
        revisionEstimate.getChangeQuantityLSActivities().addAll(lumpSumActivities);

        model.addAttribute("previousEstimates", previousEstimates);
        model.addAttribute("measurementsPresent", measurementsPresent);
    }

    public void populateChangeQuantityActivities(final Activity activity, final List<Activity> activities,
            final List<Activity> nonTenderedLumpSumActivities) {
        for (final Activity sa : activities)
            if (activity.getParent().getId().equals(sa.getId()))
                if (activity.getRevisionType() != null
                        && RevisionType.ADDITIONAL_QUANTITY.equals(activity.getRevisionType())) {
                    sa.setQuantity(sa.getQuantity() + activity.getQuantity());
                    sa.setQuantityChanged(true);
                } else if (activity.getRevisionType() != null
                        && RevisionType.REDUCED_QUANTITY.equals(activity.getRevisionType())) {
                    sa.setQuantity(sa.getQuantity() - activity.getQuantity());
                    sa.setQuantityChanged(true);
                }

        for (final Activity sa : nonTenderedLumpSumActivities)
            if (activity.getParent().getId().equals(sa.getId()))
                if (activity.getRevisionType() != null
                        && RevisionType.ADDITIONAL_QUANTITY.equals(activity.getRevisionType())) {
                    sa.setQuantity(sa.getQuantity() + activity.getQuantity());
                    sa.setQuantityChanged(true);
                } else if (activity.getRevisionType() != null
                        && RevisionType.REDUCED_QUANTITY.equals(activity.getRevisionType())) {
                    sa.setQuantity(sa.getQuantity() - activity.getQuantity());
                    sa.setQuantityChanged(true);
                }
    }

    public void loadDataForView(final RevisionAbstractEstimate revisionEstimate, final WorkOrderEstimate workOrderEstimate,
            final Model model) {
        loadViewData(revisionEstimate, workOrderEstimate, model);
        prepareNonTenderedAndLumpSumActivities(revisionEstimate);
        prepareChangeQuantityActivities(revisionEstimate);
    }

    public void loadViewData(final RevisionAbstractEstimate revisionEstimate, final WorkOrderEstimate workOrderEstimate,
            final Model model) {
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);
        model.addAttribute("uoms", uomService.findAll());
        List<RevisionAbstractEstimate> revisionAbstractEstimates;
        if (revisionEstimate != null && revisionEstimate.getId() != null)
            revisionAbstractEstimates = findApprovedRevisionEstimatesByParentForView(
                    workOrderEstimate.getEstimate().getId(), revisionEstimate.getId());
        else
            revisionAbstractEstimates = findApprovedRevisionEstimatesByParent(
                    workOrderEstimate.getEstimate().getId());
        populateHeaderActivities(revisionEstimate, revisionAbstractEstimates, model);
        model.addAttribute("revisionEstimate", revisionEstimate);
        model.addAttribute("exceptionaluoms", worksUtils.getExceptionalUOMS());
        model.addAttribute("workOrderDate",
                DateUtils.getDefaultFormattedDate(workOrderEstimate.getWorkOrder().getWorkOrderDate()));
        model.addAttribute("workOrderEstimate", workOrderEstimate);
        model.addAttribute("scheduleCategories", scheduleCategoryService.getAllScheduleCategories());
        model.addAttribute("stateType", revisionEstimate.getClass().getSimpleName());
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());
    }

    public void revisionEstimateStatusChange(final RevisionAbstractEstimate revisionEstimate,
            final String workFlowAction) {
        if (WorksConstants.SAVE_ACTION.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.NEW.toString()));
        else if (RevisionEstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode())
                && WorksConstants.FORWARD_ACTION.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.CREATED.toString()));
        else if (WorksConstants.APPROVE_ACTION.equals(workFlowAction) || WorksConstants.CREATE_AND_APPROVE.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.APPROVED.toString()));
        else if (WorksConstants.REJECT_ACTION.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.REJECTED.toString()));
        else if (WorksConstants.CANCEL_ACTION.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.CANCELLED.toString()));
        else if (RevisionEstimateStatus.REJECTED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                && WorksConstants.FORWARD_ACTION.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.RESUBMITTED.toString()));
        else if (WorksConstants.SUBMIT_ACTION.equals(workFlowAction))
            revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                    WorksConstants.REVISIONABSTRACTESTIMATE, RevisionEstimateStatus.CHECKED.toString()));

    }

    public List<SearchRevisionEstimate> searchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate) {
        Query query = null;
        query = entityManager.unwrap(Session.class)
                .createSQLQuery(getQueryForSearchRevisionEstimates(searchRevisionEstimate))
                .addScalar("id", LongType.INSTANCE).addScalar("aeId", LongType.INSTANCE)
                .addScalar("woId", LongType.INSTANCE).addScalar("revisionEstimateNumber")
                .addScalar("reDate", DateType.INSTANCE).addScalar("loaNumber", StringType.INSTANCE)
                .addScalar("contractorName", StringType.INSTANCE).addScalar("estimateNumber", StringType.INSTANCE)
                .addScalar("reValue", BigDecimalType.INSTANCE).addScalar("revisionEstimateStatus", StringType.INSTANCE)
                .addScalar("currentOwner", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(SearchRevisionEstimate.class));
        query = setParameterForSearchRevisionEstimates(searchRevisionEstimate, query);
        return query.list();
    }

    private String getQueryForSearchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate) {
        final StringBuilder filterConditions = new StringBuilder();

        if (searchRevisionEstimate != null) {

            if (searchRevisionEstimate.getRevisionEstimateNumber() != null)
                filterConditions.append(" AND aec.estimateNumber =:estimateNumber ");

            if (searchRevisionEstimate.getFromDate() != null)
                filterConditions.append(" AND aec.estimateDate >=:fromDate ");

            if (searchRevisionEstimate.getToDate() != null)
                filterConditions.append(" AND aec.estimateDate <=:toDate ");

            if (searchRevisionEstimate.getLoaNumber() != null)
                filterConditions.append(" AND lower(wo.workOrder_Number) like :loaNumber ");

            if (searchRevisionEstimate.getCreatedBy() != null)
                filterConditions.append(" AND aec.createdBy =:createdBy ");

            if (searchRevisionEstimate.getStatus() != null)
                filterConditions.append(" AND aec.status =:status ");

        }
        final StringBuilder query = new StringBuilder();
        query.append(" SELECT distinct re.id                     AS id, ");
        query.append(" aep.id                           AS aeId ,  ");
        query.append(" wo.id                            AS woId ,  ");
        query.append(" aec.estimateNumber               AS revisionEstimateNumber ,  ");
        query.append(" aec.estimateDate                 AS reDate ,  ");
        query.append(" wo.workOrder_Number              AS loaNumber ,  ");
        query.append(" contractor.name                  AS contractorName,  ");
        query.append(" aep.estimateNumber               AS estimateNumber,  ");
        query.append(" aec.estimateValue                AS reValue,  ");
        query.append(" status.description               AS revisionEstimateStatus, ");
        query.append(" u.name                           AS currentOwner  ");
        query.append(" FROM egw_revision_estimate re,egw_abstractestimate aec,egw_abstractestimate aep,");
        query.append(" egw_workorder wo,egw_workorder_estimate woe,egw_contractor contractor,egw_status status,eg_user u ");
        query.append(" WHERE aec.parent = aep.id ");
        query.append(" AND aec.id = re.id ");
        query.append(" AND aep.id = woe.abstractestimate_id ");
        query.append(" AND woe.workorder_id = wo.id ");
        query.append(" AND wo.contractor_id = contractor.id ");
        query.append(" AND aec.createdby = u.id ");
        query.append(" AND aec.status = status.id ");
        query.append(filterConditions.toString());
        return query.toString();
    }

    private Query setParameterForSearchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate, final Query query) {

        if (searchRevisionEstimate != null) {

            if (searchRevisionEstimate.getRevisionEstimateNumber() != null)
                query.setString("estimateNumber", searchRevisionEstimate.getRevisionEstimateNumber());

            if (searchRevisionEstimate.getFromDate() != null)
                query.setDate("fromDate", searchRevisionEstimate.getFromDate());

            if (searchRevisionEstimate.getToDate() != null)
                query.setDate("toDate", searchRevisionEstimate.getToDate());

            if (searchRevisionEstimate.getLoaNumber() != null)
                query.setString("loaNumber", "%" + searchRevisionEstimate.getLoaNumber().toLowerCase() + "%");

            if (searchRevisionEstimate.getCreatedBy() != null)
                query.setLong("createdBy", searchRevisionEstimate.getCreatedBy());

            if (searchRevisionEstimate.getStatus() != null)
                query.setLong("status", searchRevisionEstimate.getStatus());

        }

        return query;
    }

    public List<Activity> searchActivities(final Long estimateId, final String sorType) {
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                "select act from Activity act where (act.abstractEstimate.egwStatus.code = :aeStatus or act.abstractEstimate.egwStatus.code = :reStatus) ");
        if (estimateId != null)
            queryStr.append(
                    " and (act.abstractEstimate.id = :estimateId or act.abstractEstimate.parent.id = :estimateId )");
        if (sorType != null && "SOR".equalsIgnoreCase(sorType))
            queryStr.append(" and act.schedule != null");
        if (sorType != null && "Non Sor".equalsIgnoreCase(sorType))
            queryStr.append(" and act.schedule = null");

        queryStr.append(" order by act.id");

        Query query = entityManager.unwrap(Session.class)
                .createQuery(queryStr.toString());

        query = setParameterForSearchActivities(estimateId, query);
        return query.list();
    }

    private Query setParameterForSearchActivities(final Long estimateId, final Query query) {
        if (estimateId != null)
            query.setLong("estimateId", estimateId);
        query.setString("aeStatus", EstimateStatus.APPROVED.toString());
        query.setString("reStatus", RevisionEstimateStatus.APPROVED.toString());
        return query;
    }

    public void validateChangeQuantityActivities(final RevisionAbstractEstimate revisionEstimate,
            final BindingResult bindErrors) {
        for (final Activity activity : revisionEstimate.getChangeQuantityActivities()) {
            if (activity.getQuantity() <= 0)
                bindErrors.reject("error.change.quantity.zero", "error.change.quantity.zero");
            if (activity.getRate() <= 0)
                bindErrors.reject("error.rates.zero", "error.rates.zero");

            if ("-".equals(activity.getSignValue())) {
                final WorkOrderActivity workOrderActivity = workOrderActivityService
                        .getWorkOrderActivityByActivity(activity.getParent().getId());
                if (workOrderActivity != null) {
                    Double consumedQuantity = mbHeaderService.getPreviousCumulativeQuantity(-1L, workOrderActivity.getId());
                    if (consumedQuantity == null)
                        consumedQuantity = 0D;
                    if ("-".equals(activity.getSignValue())
                            && workOrderActivity.getApprovedQuantity() - consumedQuantity - activity.getQuantity() < 0)
                        bindErrors.reject("error.change.quantity", "error.change.quantity");
                }
            }
        }
    }

    public List<String> findRENumbersToCancel(final String estimateNumber) {
        return revisionEstimateRepository.getRENumbersToCancel("%" + estimateNumber + "%",
                AbstractEstimate.EstimateStatus.APPROVED.toString(),
                AbstractEstimate.EstimateStatus.CANCELLED.toString());
    }

    public List<SearchRevisionEstimate> searchRevisionEstimatesToCancel(
            final SearchRevisionEstimate searchRevisionEstimate) {
        Query query = null;
        query = entityManager.unwrap(Session.class)
                .createSQLQuery(createQueryForSearchRevisionEstimates(searchRevisionEstimate))
                .addScalar("id", LongType.INSTANCE).addScalar("woId", LongType.INSTANCE)
                .addScalar("revisionEstimateNumber", StringType.INSTANCE).addScalar("reDate", DateType.INSTANCE)
                .addScalar("loaNumber", StringType.INSTANCE).addScalar("estimateNumber", StringType.INSTANCE)
                .addScalar("reValue", BigDecimalType.INSTANCE).addScalar("revisionEstimateStatus", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(SearchRevisionEstimate.class));
        query = setQueryParameterForSearchRevisionEstimates(searchRevisionEstimate, query);
        return query.list();
    }

    private String createQueryForSearchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate) {
        final StringBuilder filterConditions = new StringBuilder();

        if (searchRevisionEstimate != null) {

            if (searchRevisionEstimate.getRevisionEstimateNumber() != null)
                filterConditions.append(" AND aec.estimateNumber =:estimateNumber ");

            if (searchRevisionEstimate.getFromDate() != null)
                filterConditions.append(" AND aec.estimateDate >=:fromDate ");

            if (searchRevisionEstimate.getToDate() != null)
                filterConditions.append(" AND aec.estimateDate <=:toDate ");

            if (searchRevisionEstimate.getLoaNumber() != null)
                filterConditions.append(" AND wo.workOrder_Number like :loaNumber ");

            if (searchRevisionEstimate.getCreatedBy() != null)
                filterConditions.append(" AND aec.createdBy =:createdBy ");

        }
        final StringBuilder query = new StringBuilder();
        query.append(" SELECT distinct re.id            AS id, ");
        query.append(" wo.id                            AS woId ,  ");
        query.append(" aec.estimateNumber               AS revisionEstimateNumber ,  ");
        query.append(" aec.estimateDate                 AS reDate ,  ");
        query.append(" wo.workOrder_Number              AS loaNumber ,  ");
        query.append(" aep.estimateNumber               AS estimateNumber,  ");
        query.append(" aec.estimateValue                AS reValue,  ");
        query.append(" status.description               AS revisionEstimateStatus ");
        query.append(" FROM egw_revision_estimate re,egw_abstractestimate aec,egw_abstractestimate aep,");
        query.append(" egw_workorder wo,egw_workorder_estimate woe,egw_status status ");
        query.append(" WHERE aec.parent = aep.id ");
        query.append(" AND aec.id = re.id ");
        query.append(" AND aep.id = woe.abstractestimate_id ");
        query.append(" AND woe.workorder_id = wo.id ");
        query.append(" AND aec.status = status.id AND status.code =:restatus ");
        query.append(filterConditions.toString());
        return query.toString();
    }

    private Query setQueryParameterForSearchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate,
            final Query query) {

        if (searchRevisionEstimate != null) {

            if (StringUtils.isNotBlank(searchRevisionEstimate.getRevisionEstimateNumber()))
                query.setString("estimateNumber", searchRevisionEstimate.getRevisionEstimateNumber());

            if (searchRevisionEstimate.getFromDate() != null)
                query.setDate("fromDate", searchRevisionEstimate.getFromDate());

            if (searchRevisionEstimate.getToDate() != null)
                query.setDate("toDate", searchRevisionEstimate.getToDate());

            if (StringUtils.isNotBlank(searchRevisionEstimate.getLoaNumber()))
                query.setString("loaNumber", "%" + searchRevisionEstimate.getLoaNumber() + "%");

            if (searchRevisionEstimate.getCreatedBy() != null)
                query.setLong("createdBy", searchRevisionEstimate.getCreatedBy());

            if (StringUtils.isNotBlank(searchRevisionEstimate.getRevisionEstimateStatus()))
                query.setString("restatus", searchRevisionEstimate.getRevisionEstimateStatus());

        }
        return query;
    }

    public String checkIfMBCreatedForRENonTenderedLumpSum(final RevisionAbstractEstimate revisionEstimate,
            final WorkOrderEstimate workOrderEstimate) {
        final StringBuilder mbRefNumbersForRE = new StringBuilder();
        // Checking MB's exists for Non Tendered and lumpsum activities
        final List<MBHeader> mbHeaders = mbHeaderService
                .getMBHeadersForTenderedLumpSumAcivitiesToCancelRE(revisionEstimate, workOrderEstimate);
        if (!mbHeaders.isEmpty())
            for (final MBHeader mBHeader : mbHeaders)
                mbRefNumbersForRE.append(mBHeader.getMbRefNo()).append(",");
        return mbRefNumbersForRE.toString();

    }

    public String checkIfMBCreatedForREChangedQuantity(final RevisionAbstractEstimate revisionEstimate,
            final WorkOrderEstimate workOrderEstimate) {
        final List<Long> originalActivtityIdList = new ArrayList<>();
        String message = "";
        // Checking MB's for change quantity activities
        final WorkOrderEstimate revisionWorkOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getId());
        final List<WorkOrderActivity> reWoaList = workOrderActivityService
                .getChangedQuantityActivities(revisionEstimate, revisionWorkOrderEstimate);
        for (final WorkOrderActivity woa : reWoaList)
            originalActivtityIdList.add(woa.getActivity().getParent().getId());
        List<Object[]> activityIdQuantityList = null;
        if (originalActivtityIdList != null && originalActivtityIdList.size() > 0)
            activityIdQuantityList = mBDetailsService.getMBDetailsByWorkOrderActivity(originalActivtityIdList);
        if (activityIdQuantityList != null && activityIdQuantityList.size() > 0)
            for (final WorkOrderActivity revWoa : reWoaList)
                for (final Object[] activityIdQuantity : activityIdQuantityList)
                    if (Long.parseLong(activityIdQuantity[0].toString()) == revWoa.getActivity().getParent().getId()
                            .longValue()) {
                        Long activityId = null;
                        if (revWoa.getActivity().getParent() == null)
                            activityId = revWoa.getActivity().getId();
                        else
                            activityId = revWoa.getActivity().getParent().getId();

                        final Double originalQuantity = (Double) workOrderActivityService
                                .getQuantityForActivity(activityId);
                        final Object revEstQuantityObj = workOrderActivityService
                                .getREActivityQuantity(revisionEstimate.getId(), activityId);
                        final double revEstQuantity = revEstQuantityObj == null ? 0.0 : (Double) revEstQuantityObj;
                        if (originalQuantity + revEstQuantity >= Double.parseDouble(activityIdQuantity[1].toString()))
                            continue;
                        else {

                            final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                                    WorksConstants.WORKS_MODULE_NAME,
                                    WorksConstants.APPCONFIG_KEY_MB_QUANTITY_TOLERANCE_LEVEL);
                            final AppConfigValues value = values.get(0);
                            Double maxPercent = Double.valueOf(value.getValue());
                            if (maxPercent != null)
                                maxPercent += 100;
                            else
                                maxPercent = 100d;
                            final Double maxAllowedQuantity = maxPercent * (originalQuantity + revEstQuantity) / 100;
                            if (maxAllowedQuantity >= Double.parseDouble(activityIdQuantity[1].toString()))
                                continue;
                            else {
                                message = messageSource.getMessage("error.mbexistsfor.rechangequantity",
                                        new String[] { revisionEstimate.getEstimateNumber() }, null);
                                break;
                            }
                        }
                    }
        return message;
    }

    @Transactional
    public RevisionAbstractEstimate cancelRevisionEstimate(final RevisionAbstractEstimate revisionEstimate) {
        revisionEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                WorksConstants.REVISIONABSTRACTESTIMATE, RevisionAbstractEstimate.EstimateStatus.CANCELLED.toString()));

        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getId());
        final RevisionWorkOrder revisionWorkOrder = revisionWorkOrderService
                .getRevisionWorkOrderById(workOrderEstimate.getWorkOrder().getId());
        if (revisionWorkOrder != null) {
            revisionWorkOrder.setEgwStatus(
                    worksUtils.getStatusByModuleAndCode(WorksConstants.WORKORDER, WorksConstants.CANCELLED_STATUS));
            revisionWorkOrder.getParent().setTotalIncludingRE(
                    revisionWorkOrder.getParent().getTotalIncludingRE() - revisionWorkOrder.getWorkOrderAmount());
            revisionWorkOrderService.create(revisionWorkOrder);
        }
        if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
            estimateAppropriationService.releaseBudgetOnRejectForEstimate(revisionEstimate,
                    revisionEstimate.getEstimateValue().doubleValue(), null);
        revisionEstimate.getParent().setTotalIncludingRE(
                revisionEstimate.getParent().getTotalIncludingRE() - revisionEstimate.getWorkValue());
        return revisionEstimateRepository.save(revisionEstimate);
    }

    public String getRevisionEstimatesGreaterThanCurrent(final Long parentId, final Date createdDate) {
        final List<RevisionAbstractEstimate> revisionEstimatesList = revisionEstimateRepository
                .findByParent_idAndCreatedDateAfterAndEgwStatus_codeNotLike(parentId, createdDate,
                        WorksConstants.CANCELLED_STATUS);
        final StringBuilder revisionEstimates = new StringBuilder();
        for (final RevisionAbstractEstimate revisionAbstractEstimate : revisionEstimatesList)
            revisionEstimates.append(revisionAbstractEstimate.getEstimateNumber()).append(",");

        return revisionEstimates.toString();
    }

    public void validateREInDrafts(final Long estimateId, final JsonObject jsonObject, final BindingResult errors) {
        final RevisionAbstractEstimate revisionEstimate = revisionEstimateRepository
                .findByParent_IdAndEgwStatus_codeEquals(estimateId, WorksConstants.NEW);
        String userName = "";
        if (revisionEstimate != null) {
            final String message = messageSource.getMessage("error.re.newstatus",
                    new String[] { revisionEstimate.getEstimateNumber(), revisionEstimate.getEgwStatus().getDescription(),
                            userName },
                    null);
            userName = worksUtils.getApproverName(revisionEstimate.getState().getOwnerPosition().getId());
            jsonObject.addProperty("draftsError", message);
            if (errors != null)
                errors.reject("draftsError", message);
        }
    }

    public void validateREInWorkFlow(final Long estimateId, final JsonObject jsonObject, final BindingResult errors) {
        final RevisionAbstractEstimate revisionEstimate = revisionEstimateRepository.findByParentAndStatus(estimateId,
                RevisionEstimateStatus.CANCELLED.toString(), RevisionEstimateStatus.APPROVED.toString(),
                RevisionEstimateStatus.NEW.toString());
        String userName = "";
        if (revisionEstimate != null) {
            final String message = messageSource.getMessage("error.re.workflow",
                    new String[] { revisionEstimate.getEstimateNumber(), revisionEstimate.getEgwStatus().getDescription(),
                            userName },
                    null);
            userName = worksUtils.getApproverName(revisionEstimate.getState().getOwnerPosition().getId());
            jsonObject.addProperty("workFlowError", message);
            if (errors != null)
                errors.reject("workFlowError", message);
        }
    }

    public void deriveWorkOrderActivityQuantity(final WorkOrderActivity workOrderActivity, final Long reId) {
        if (!workOrderActivity.getWorkOrderMeasurementSheets().isEmpty())
            for (final WorkOrderMeasurementSheet woms : workOrderActivity.getWorkOrderMeasurementSheets()) {
                final List<WorkOrderMeasurementSheet> rewomsList = workOrderMeasurementSheetService
                        .findByMeasurementSheetParentId_ForView(woms.getMeasurementSheet().getId(), reId);
                Double no = woms.getNo() == null ? 0 : woms.getNo().doubleValue();
                Double length = woms.getLength() == null ? 0 : woms.getLength().doubleValue();
                Double width = woms.getWidth() == null ? 0 : woms.getWidth().doubleValue();
                Double depthOrHeight = woms.getDepthOrHeight() == null ? 0 : woms.getDepthOrHeight().doubleValue();
                for (final WorkOrderMeasurementSheet rems : rewomsList) {
                    if (rems.getNo() != null)
                        no = no + rems.getNo().doubleValue();
                    if (rems.getLength() != null)
                        length = length + rems.getLength().doubleValue();
                    if (rems.getWidth() != null)
                        width = width + rems.getWidth().doubleValue();
                    if (rems.getDepthOrHeight() != null)
                        depthOrHeight = depthOrHeight + rems.getDepthOrHeight().doubleValue();
                }
                /*
                 * else { if (rems.getNo() != null) no = no - rems.getNo().doubleValue(); if (rems.getLength() != null) length =
                 * length - rems.getLength().doubleValue(); if (rems.getWidth() != null) width = width -
                 * rems.getWidth().doubleValue(); if (rems.getDepthOrHeight() != null) depthOrHeight = depthOrHeight -
                 * rems.getDepthOrHeight().doubleValue(); }
                 */
                if (no != null && no != 0)
                    woms.setNo(new BigDecimal(no));
                if (length != null && length != 0)
                    woms.setLength(new BigDecimal(length));
                if (width != null && width != 0)
                    woms.setWidth(new BigDecimal(width));
                if (depthOrHeight != null && depthOrHeight != 0)
                    woms.setDepthOrHeight(new BigDecimal(depthOrHeight));

                woms.setQuantity(new BigDecimal(
                        (no == null || no == 0 ? 1 : no.doubleValue())
                                * (length == null || length == 0 ? 1 : length.doubleValue())
                                * (width == null || width == 0 ? 1 : width.doubleValue())
                                * (depthOrHeight == null || depthOrHeight == 0 ? 1 : depthOrHeight.doubleValue())));
            }
        Double qty = 0d;
        for (final WorkOrderMeasurementSheet woms : workOrderActivity.getWorkOrderMeasurementSheets())
            if (woms.getMeasurementSheet().getIdentifier() == 'A')
                qty = qty + woms.getQuantity().doubleValue();
            else
                qty = qty - woms.getQuantity().doubleValue();
        if (!workOrderActivity.getWorkOrderMeasurementSheets().isEmpty())
            workOrderActivity.setApprovedQuantity(qty);
    }

    public void deriveMeasurementSheetQuantity(final MeasurementSheet measurementSheet, final Long reId) {
        List<MeasurementSheet> remsList = new ArrayList<>();
        if (reId != null)
            remsList = measurementSheetService.findByParentIdForView(measurementSheet.getId(), reId);
        else
            remsList = measurementSheetService.findByParentId(measurementSheet.getId());
        Double no = measurementSheet.getNo() == null ? 0 : measurementSheet.getNo().doubleValue();
        Double length = measurementSheet.getLength() == null ? 0 : measurementSheet.getLength().doubleValue();
        Double width = measurementSheet.getWidth() == null ? 0 : measurementSheet.getWidth().doubleValue();
        Double depthOrHeight = measurementSheet.getDepthOrHeight() == null ? 0
                : measurementSheet.getDepthOrHeight().doubleValue();
        Double quantity = measurementSheet.getQuantity() == null ? 0 : measurementSheet.getQuantity().doubleValue();
        for (final MeasurementSheet rems : remsList)
            if (rems.getId() != measurementSheet.getId()) {
                if (rems.getNo() != null)
                    no = no + rems.getNo().doubleValue();
                if (rems.getLength() != null)
                    length = length + rems.getLength().doubleValue();
                if (rems.getWidth() != null)
                    width = width + rems.getWidth().doubleValue();
                if (rems.getDepthOrHeight() != null)
                    depthOrHeight = depthOrHeight + rems.getDepthOrHeight().doubleValue();

                quantity = quantity + rems.getQuantity().doubleValue();
            }
        if (no != null && no != 0)
            measurementSheet.setNo(new BigDecimal(no));
        if (length != null && length != 0)
            measurementSheet.setLength(new BigDecimal(length));
        if (width != null && width != 0)
            measurementSheet.setWidth(new BigDecimal(width));
        if (depthOrHeight != null && depthOrHeight != 0)
            measurementSheet.setDepthOrHeight(new BigDecimal(depthOrHeight));

        measurementSheet.setQuantity(new BigDecimal(quantity.toString()));
    }

    public List<RevisionAbstractEstimate> findRevisionEstimatesByParentAndStatus(final Long parentId) {
        return revisionEstimateRepository.findByParent_IdAndEgwStatus_codeNotLike(parentId,
                "%" + WorksConstants.CANCELLED_STATUS + "%");
    }

    public void prepareNonTenderedAndLumpSumActivities(final RevisionAbstractEstimate revisionEstimate) {

        for (final Activity activity : revisionEstimate.getActivities())
            if (activity.getSchedule() != null && activity.getParent() == null)
                revisionEstimate.getNonTenderedActivities().add(activity);
            else if (activity.getNonSor() != null && activity.getParent() == null)
                revisionEstimate.getLumpSumActivities().add(activity);
    }

    public void prepareChangeQuantityActivities(final RevisionAbstractEstimate revisionEstimate) {
        for (final Activity activity : revisionEstimate.getActivities())
            if (activity.getParent() != null) {
                final WorkOrderActivity workOrderActivity = workOrderActivityService
                        .getWorkOrderActivityByActivity(activity.getParent().getId());
                if (workOrderActivity != null) {
                    deriveWorkOrderActivityQuantity(workOrderActivity, revisionEstimate.getId());
                    final Double consumedQuantity = mbHeaderService.getPreviousCumulativeQuantity(-1L, workOrderActivity.getId());
                    activity.setConsumedQuantity(consumedQuantity == null ? 0 : consumedQuantity);
                    activity.setEstimateQuantity(workOrderActivity.getApprovedQuantity());
                }
                revisionEstimate.getChangeQuantityActivities().add(activity);
            }
    }

    public void validateNontenderedActivities(final RevisionAbstractEstimate revisionEstimate,
            final BindingResult bindErrors) {
        for (final Activity activity : revisionEstimate.getNonTenderedActivities()) {
            if (activity.getQuantity() <= 0)
                bindErrors.reject("error.notendered.quantity.zero", "error.notendered.quantity.zero");
            if (activity.getRate() <= 0)
                bindErrors.reject("error.rates.zero", "error.rates.zero");
        }
    }

    public void validateLumpsumActivities(final RevisionAbstractEstimate revisionEstimate,
            final BindingResult bindErrors) {
        for (final Activity activity : revisionEstimate.getLumpSumActivities()) {
            if (activity.getQuantity() <= 0)
                bindErrors.reject("error.lumpsum.quantity.zero", "error.lumpsum.quantity.zero");
            if (activity.getRate() <= 0)
                bindErrors.reject("error.rates.zero", "error.rates.zero");
        }
    }

    @SuppressWarnings("unchecked")
    public void validateWorkflowActionButton(final RevisionAbstractEstimate revisionAbstractEstimate,
            final BindingResult bindErrors,
            final String additionalRule, final String workFlowAction) {
        final Map<String, Object> map = new HashMap<String, Object>();

        map.putAll((Map<String, Object>) scriptService.executeScript(WorksConstants.REVISIONESTIMATE_APPROVALRULES,
                ScriptService.createContext("estimateValue", revisionAbstractEstimate.getEstimateValue(),
                        "cityGrade", additionalRule)));
        final boolean validateWorkflowButton = (boolean) map.get("createAndApproveFieldsRequired");
        if (validateWorkflowButton && WorksConstants.FORWARD_ACTION.toString().equalsIgnoreCase(workFlowAction))
            bindErrors.reject("error.create.approve", "error.create.approve");
        else if (!validateWorkflowButton && WorksConstants.CREATE_AND_APPROVE.toString().equalsIgnoreCase(workFlowAction))
            bindErrors.reject("error.forward.approve", "error.forward.approve");
    }

}
