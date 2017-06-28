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
package org.egov.works.letterofacceptance.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.AssetsForEstimate;
import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.ContractorBillRegister.BillStatus;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.repository.ContractorBillRegisterRepository;
import org.egov.works.letterofacceptance.entity.SearchRequestContractor;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.entity.WorkOrderHistory;
import org.egov.works.letterofacceptance.repository.LetterOfAcceptanceRepository;
import org.egov.works.letterofacceptance.repository.WorkOrderHistoryRepository;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.masters.entity.ContractorDetail;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.service.MilestoneService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.AssetsForWorkOrder;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.entity.WorkOrderMeasurementSheet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LetterOfAcceptanceService {

    private static final Logger LOG = LoggerFactory.getLogger(LetterOfAcceptanceService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @Autowired
    private WorkOrderHistoryRepository workOrderHistoryRepository;

    @Autowired
    private ContractorBillRegisterRepository contractorBillRegisterRepository;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<WorkOrder> workOrderWorkflowService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private MBHeaderService mBHeaderService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LetterOfAcceptanceService(final LetterOfAcceptanceRepository letterOfAcceptanceRepository) {
        this.letterOfAcceptanceRepository = letterOfAcceptanceRepository;
    }

    public WorkOrder getWorkOrderById(final Long id) {
        return letterOfAcceptanceRepository.findById(id);
    }

    public List<String> getApprovedWorkOrderByNumber(final String workOrderNumber) {
        return letterOfAcceptanceRepository
                .findDistinctWorkOrderNumberContainingIgnoreCase("%" + workOrderNumber + "%");
    }

    @Transactional
    public WorkOrder create(WorkOrder workOrder, final MultipartFile[] files, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction,
            final AbstractEstimate abstractEstimate) throws IOException {

        if (StringUtils.isNotBlank(workOrder.getPercentageSign()) && "-".equals(workOrder.getPercentageSign()))
            workOrder.setTenderFinalizedPercentage(workOrder.getTenderFinalizedPercentage() * -1);
        workOrder.setTotalIncludingRE(workOrder.getWorkOrderAmount());
        WorkOrder workOrderObj = createWorkOrderActivities(workOrder);

        workOrderObj = createAssetsForWorkOrder(workOrderObj);
        WorkOrder savedworkOrder;
        if (abstractEstimate != null && abstractEstimate.isSpillOverFlag() && abstractEstimate.isWorkOrderCreated())
            workOrder.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER, WorksConstants.APPROVED));
        else
            createWorkOrderWorkflowTransition(workOrderObj, approvalPosition, approvalComent, additionalRule,
                    workFlowAction);

        savedworkOrder = letterOfAcceptanceRepository.save(workOrderObj);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, savedworkOrder,
                WorksConstants.WORKORDER);
        if (!documentDetails.isEmpty()) {
            savedworkOrder.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return savedworkOrder;
    }

    public void createWorkOrderWorkflowTransition(final WorkOrder workOrder, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_DISPLAYNAME_WORKORDER;
        WorkFlowMatrix wfmatrix = null;

        if (null != workOrder.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(workOrder.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            workOrder.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER, WorksConstants.REJECTED));
            if (wfInitiator.equals(userAssignment))
                workOrder.transition().progressWithStateCopy().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            else
                workOrder.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.WF_STATE_REJECTED)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition()).withNextAction("")
                        .withNatureOfTask(natureOfwork);
        } else if (WorksConstants.SAVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            wfmatrix = workOrderWorkflowService.getWfMatrix(workOrder.getStateType(), null,
                    new BigDecimal(workOrder.getWorkOrderAmount()), additionalRule, WorksConstants.NEW, null);
            if (workOrder.getState() == null)
                workOrder.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            else
                workOrder.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0))
                    && !WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction))
                pos = positionMasterService.getPositionById(approvalPosition);
            if (null == workOrder.getState()) {
                workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                        WorksConstants.CREATED_STATUS));
                wfmatrix = workOrderWorkflowService.getWfMatrix(workOrder.getStateType(), null,
                        new BigDecimal(workOrder.getWorkOrderAmount()), additionalRule, currState, null);
                workOrder.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                        WorksConstants.CANCELLED_STATUS));
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                workOrder.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("").withNatureOfTask(natureOfwork);
            } else if (WorksConstants.APPROVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                wfmatrix = workOrderWorkflowService.getWfMatrix(workOrder.getStateType(), null, null, additionalRule,
                        workOrder.getCurrentState().getValue(), null);
                workOrder.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction("")
                        .withNatureOfTask(natureOfwork);
            } else {
                if (workOrder.getEgwStatus().getCode().equals(WorksConstants.REJECTED.toString())
                        && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                    workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                            WorksConstants.RESUBMITTED_STATUS));
                else
                    workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                            WorksConstants.CREATED_STATUS));

                wfmatrix = workOrderWorkflowService.getWfMatrix(workOrder.getStateType(), null,
                        new BigDecimal(workOrder.getWorkOrderAmount()), additionalRule,
                        workOrder.getCurrentState().getValue(), workOrder.getState().getNextAction());
                workOrder.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public Long getApprovalPositionByMatrixDesignation(final WorkOrder workOrder, Long approvalPosition,
            final String additionalRule, final String mode, final String workFlowAction) {
        final WorkFlowMatrix wfmatrix = workOrderWorkflowService.getWfMatrix(workOrder.getStateType(), null, null,
                additionalRule, workOrder.getCurrentState().getValue(), null);
        if (workOrder.getEgwStatus() != null && workOrder.getEgwStatus().getCode() != null)
            if ((workOrder.getEgwStatus().getCode().equals(WorksConstants.CREATED_STATUS)
                    || workOrder.getEgwStatus().getCode().equals(WorksConstants.RESUBMITTED_STATUS))
                    && workOrder.getState() != null)
                if (mode.equals("edit"))
                    approvalPosition = workOrder.getState().getOwnerPosition().getId();
                else
                    approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            workOrder.getState(), workOrder.getCreatedBy().getId());
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED))
            approvalPosition = null;

        return approvalPosition;
    }

    private WorkOrder createAssetsForWorkOrder(final WorkOrder workOrder) {
        AssetsForWorkOrder assetsForWorkOrder = null;
        final WorkOrderEstimate workOrderEstimate = workOrder.getWorkOrderEstimates() != null
                ? workOrder.getWorkOrderEstimates().get(0) : null;
        if (workOrderEstimate != null) {
            workOrderEstimate.getAssetValues().clear();
            for (final AssetsForEstimate assetsForEstimate : workOrderEstimate.getEstimate().getAssetValues()) {
                assetsForWorkOrder = new AssetsForWorkOrder();
                assetsForWorkOrder.setAsset(assetsForEstimate.getAsset());
                assetsForWorkOrder.setWorkOrderEstimate(workOrderEstimate);
                workOrder.getWorkOrderEstimates().get(0).getAssetValues().add(assetsForWorkOrder);

            }
        }
        return workOrder;
    }

    /*
     * This method will create work order activities Populate work order activities for Percentage Tender. The Item Rate tender
     * logic will be implemented later when we take up Item Rate use case.
     */
    private WorkOrder createWorkOrderActivities(final WorkOrder workOrder) {
        WorkOrderActivity workOrderActivity = null;
        final Double tenderFinalizedPercentage = workOrder.getTenderFinalizedPercentage();
        final WorkOrderEstimate workOrderEstimate = workOrder.getWorkOrderEstimates() != null
                ? workOrder.getWorkOrderEstimates().get(0) : null;
        if (workOrderEstimate != null) {
            workOrderEstimate.getWorkOrderActivities().clear();
            for (final Activity activity : workOrderEstimate.getEstimate().getActivities()) {
                workOrderActivity = new WorkOrderActivity();
                if (!tenderFinalizedPercentage.equals(Double.valueOf(0)))
                    workOrderActivity.setApprovedRate(
                            activity.getRate() + activity.getRate() * workOrder.getTenderFinalizedPercentage() / 100);
                else
                    workOrderActivity.setApprovedRate(activity.getRate());
                workOrderActivity.setApprovedQuantity(activity.getQuantity());
                workOrderActivity.setApprovedAmount(
                        workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity());
                createWorkOrderMSheet(workOrderActivity, activity);
                workOrderActivity.setActivity(activity);
                workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
                workOrder.getWorkOrderEstimates().get(0).getWorkOrderActivities().add(workOrderActivity);
            }
        }
        return workOrder;
    }

    private void createWorkOrderMSheet(final WorkOrderActivity workOrderActivity, final Activity activity) {
        LOG.info("adding Msheet to work order......");
        final List<WorkOrderMeasurementSheet> womSheetList = new ArrayList<WorkOrderMeasurementSheet>();
        WorkOrderMeasurementSheet womSheet = null;
        for (final MeasurementSheet msheet : activity.getMeasurementSheetList()) {
            womSheet = new WorkOrderMeasurementSheet();
            womSheet.setNo(msheet.getNo());
            womSheet.setLength(msheet.getLength());
            womSheet.setWidth(msheet.getWidth());
            womSheet.setDepthOrHeight(msheet.getDepthOrHeight());
            womSheet.setWoActivity(workOrderActivity);
            womSheet.setQuantity(msheet.getQuantity());
            womSheet.setMeasurementSheet(msheet);
            womSheetList.add(womSheet);
            LOG.info("added msheet" + msheet.getId());

        }
        workOrderActivity.setWorkOrderMeasurementSheets(womSheetList);

    }

    public WorkOrderEstimate createWorkOrderEstimate(final WorkOrder workOrder) {
        workOrder.getWorkOrderEstimates().clear();
        final WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
        workOrderEstimate.setWorkOrder(workOrder);
        workOrderEstimate.setEstimate(
                estimateService.getAbstractEstimateByEstimateNumberAndStatus(workOrder.getEstimateNumber()));

        workOrderEstimate.setEstimateWOAmount(workOrder.getWorkOrderAmount());
        workOrder.addWorkOrderEstimate(workOrderEstimate);
        return workOrderEstimate;
    }

    public WorkOrder getWorkOrderByWorkOrderNumber(final String workOrderNumber) {
        return letterOfAcceptanceRepository.findByWorkOrderNumberAndEgwStatus_codeNotLike(workOrderNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public List<Long> getEngineerInchargeDesignationIds() {
        final List<Long> designationIds = new ArrayList<Long>();
        final List<String> designationNames = new ArrayList<String>();
        final List<AppConfigValues> configList = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_ENGINEERINCHARGE_DESIGNATION);
        for (final AppConfigValues value : configList)
            designationNames.add(value.getValue().toUpperCase());
        final List<Designation> designations = designationService.getDesignationsByNames(designationNames);
        for (final Designation designation : designations)
            designationIds.add(designation.getId());
        return designationIds;
    }

    public List<Assignment> getEngineerInchargeList(final Long departmentId, final List<Long> designationIds) {
        return assignmentService.findByDepartmentDesignationsAndGivenDate(departmentId, designationIds, new Date());
    }

    public WorkOrder getWorkOrderByEstimateNumber(final String estimateNumber) {
        return letterOfAcceptanceRepository.findByEstimateNumberAndEgwStatus_codeNotLike(estimateNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public WorkOrder getLetterOfAcceptanceDocumentAttachments(final WorkOrder workOrder) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(workOrder.getId(), WorksConstants.WORKORDER);
        workOrder.setDocumentDetails(documentDetailsList);
        return workOrder;
    }

    public WorkOrder getApprovedWorkOrder(final String workOrderNumber) {
        return letterOfAcceptanceRepository.findByWorkOrderNumberAndEgwStatus_codeEquals(workOrderNumber,
                WorksConstants.APPROVED);
    }

    public List<WorkOrder> searchLetterOfAcceptance(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        // TODO Need TO handle in single query
        final List<String> estimateNumbers = lineEstimateDetailsRepository
                .findEstimateNumbersForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
        if (estimateNumbers.isEmpty())
            estimateNumbers.add("");
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrder.class, "wo")
                .addOrder(Order.asc("workOrderDate")).createAlias("wo.contractor", "woc")
                .createAlias("egwStatus", "status");
        criteria.add(Restrictions.isNull("parent.id"));
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(Restrictions.eq("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber())
                        .ignoreCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                criteria.add(Restrictions.ge("workOrderDate", searchRequestLetterOfAcceptance.getFromDate()));
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                criteria.add(Restrictions.le("workOrderDate", searchRequestLetterOfAcceptance.getToDate()));
            if (searchRequestLetterOfAcceptance.getName() != null)
                criteria.add(Restrictions.eq("woc.name", searchRequestLetterOfAcceptance.getName()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                criteria.add(Restrictions.ilike("fileNumber", searchRequestLetterOfAcceptance.getFileNumber(),
                        MatchMode.ANYWHERE));
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber())
                        .ignoreCase());
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                criteria.add(Restrictions.eq("status.code", searchRequestLetterOfAcceptance.getEgwStatus()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<WorkOrderEstimate> searchLetterOfAcceptanceForContractorBill(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
        StringBuilder queryStr = new StringBuilder(500);
        /*
         * This block will get LOA's where BOQ is created and final bill is not created
         */
        getWorkOrdersWhereBoqIsCreated(searchRequestLetterOfAcceptance, queryStr);
        Query query = setParameterForLetterOfAcceptanceForContractorBill(searchRequestLetterOfAcceptance, queryStr);
        query.setParameter("offlineStatus", WorkOrder.OfflineStatuses.WORK_COMMENCED.toString().toLowerCase());
        query.setParameter("objectType", WorksConstants.WORKORDER);
        workOrderEstimateList = query.getResultList();

        /*
         * This block will get LOA's where BOQ is not created and final bill is not created
         */
        if (searchRequestLetterOfAcceptance.getMbRefNumber() == null
                || searchRequestLetterOfAcceptance.getMbRefNumber().isEmpty()) {
            queryStr = new StringBuilder(500);
            getWorkOrdersWhereBoqIsNotCreated(searchRequestLetterOfAcceptance, queryStr);
            query = setParameterForLetterOfAcceptanceForContractorBill(searchRequestLetterOfAcceptance, queryStr);
            workOrderEstimateList.addAll(query.getResultList());
        }

        return workOrderEstimateList;
    }

    private Query setParameterForLetterOfAcceptanceForContractorBill(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("woStatus", WorksConstants.APPROVED);
        if (queryStr.toString().indexOf("mbStatus") != -1)
            qry.setParameter("mbStatus", WorksConstants.APPROVED);
        qry.setParameter("billStatus", ContractorBillRegister.BillStatus.CANCELLED.toString());
        qry.setParameter("billType", BillTypes.Final_Bill.toString());
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                qry.setParameter("executingDepartment", searchRequestLetterOfAcceptance.getDepartmentName());
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                qry.setParameter("fromWorkOrderDate", searchRequestLetterOfAcceptance.getFromDate());
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                qry.setParameter("toWorkOrderDate", searchRequestLetterOfAcceptance.getToDate());
            if (searchRequestLetterOfAcceptance.getName() != null)
                qry.setParameter("contractorName", searchRequestLetterOfAcceptance.getName().toUpperCase());
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                qry.setParameter("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getMbRefNumber() != null
                    && !searchRequestLetterOfAcceptance.getMbRefNumber().isEmpty())
                qry.setParameter("mbRefNo", searchRequestLetterOfAcceptance.getMbRefNumber().toUpperCase());

        }
        return qry;
    }

    private void getWorkOrdersWhereBoqIsNotCreated(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final StringBuilder queryStr) {
        queryStr.append(
                " select distinct woe from WorkOrderEstimate woe where woe.workOrder.egwStatus.code = :woStatus and not exists (select cbr.workOrderEstimate from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and  upper(cbr.billstatus) != :billStatus and cbr.billtype = :billType and cbr.workOrderEstimate.id is not null) and  not exists (select workOrderEstimate  from WorkOrderActivity where woe.id =workOrderEstimate.id ) ");

        if (searchRequestLetterOfAcceptance != null)
            getSubQuery(searchRequestLetterOfAcceptance, queryStr);

    }

    private void getWorkOrdersWhereBoqIsCreated(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        queryStr.append(
                " select distinct woe from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.workOrder.egwStatus.code = :woStatus and  not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) !=:billStatus and cbr.billtype =:billType and cbr.workOrderEstimate.id is not null) ");
        queryStr.append(
                " and exists (select workOrderEstimate  from WorkOrderActivity where woe.id =workOrderEstimate.id ) ");

        queryStr.append(
                " and  exists (select mb.workOrderEstimate from MBHeader mb where mb.egwStatus.code =:mbStatus and woe = mb.workOrderEstimate and not exists (select cbr from ContractorBillRegister cbr where upper(cbr.billstatus) != :billStatus and cbr = mb.egBillregister) ");
        if (searchRequestLetterOfAcceptance != null && searchRequestLetterOfAcceptance.getMbRefNumber() != null)
            queryStr.append(" and upper(mb.mbRefNo) =:mbRefNo ) ");
        else
            queryStr.append(")");

        queryStr.append(
                " and exists ( select distinct(woe1) from WorkOrderEstimate as woe1 where woe1 = woe and woe1.workOrder.id = (select distinct(os.objectId) from OfflineStatus as os where os.id = (select max(status.id) from OfflineStatus status where status.objectType = :objectType and status.objectId = woe1.workOrder.id) and os.objectId = woe1.workOrder.id and lower(os.egwStatus.code) = :offlineStatus and os.objectType = :objectType ) ) ");
        if (searchRequestLetterOfAcceptance != null)
            getSubQuery(searchRequestLetterOfAcceptance, queryStr);

    }

    private void getSubQuery(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
            queryStr.append(" and woe.estimate.executingDepartment.id =:executingDepartment ");
        if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
            queryStr.append(" and woe.workOrder.workOrderNumber =:workOrderNumber ");
        if (searchRequestLetterOfAcceptance.getFromDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate >=:fromWorkOrderDate ");
        if (searchRequestLetterOfAcceptance.getToDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate <=:toWorkOrderDate ");
        if (searchRequestLetterOfAcceptance.getName() != null)
            queryStr.append(" and upper(woe.workOrder.contractor.name) =:contractorName ");
        if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
            queryStr.append(" and upper(woe.workOrder.estimateNumber) =:estimateNumber ");

    }

    public List<String> getApprovedEstimateNumbersToModifyLOA(final String name) {
        return letterOfAcceptanceRepository.findDistinctEstimateNumberToModifyLOA("%" + name + "%",
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString(),
                BillTypes.Final_Bill.toString());
    }

    public List<String> findDistinctContractorsInWorkOrderByCodeOrName(final String name) {
        final List<String> results = letterOfAcceptanceRepository
                .findDistinctContractorByContractor_codeAndNameContainingIgnoreCase("%" + name + "%");
        return results;
    }

    public List<String> findLoaEstimateNumbersForContractorBill(final String estimateNumber) {
        final List<WorkOrder> workorders = letterOfAcceptanceRepository
                .findByEstimateNumberAndEgwStatus_codeEquals(estimateNumber, WorksConstants.APPROVED);
        final List<String> results = new ArrayList<String>();
        for (final WorkOrder details : workorders)
            results.add(details.getEstimateNumber());
        return results;
    }

    public List<String> getApprovedWorkOrdersForCreateContractorBill(final String workOrderNumber) {
        final Set<String> result = new HashSet<String>();
        final List<String> results = letterOfAcceptanceRepository.findWorkOrderNumberForContractorBill(
                "%" + workOrderNumber + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        results.addAll(letterOfAcceptanceRepository.findWorkOrderNumberForContractorBillWithMB(
                "%" + workOrderNumber + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString()));
        result.addAll(results);
        return new ArrayList<String>(result);

    }

    public List<String> getApprovedEstimateNumbersForCreateContractorBill(final String estimateNumber) {
        final Set<String> result = new HashSet<String>();
        final List<String> results = letterOfAcceptanceRepository.findEstimateNumberForContractorBill(
                "%" + estimateNumber + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        results.addAll(letterOfAcceptanceRepository.findEstimateNumberForContractorBillWithMB(
                "%" + estimateNumber + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString()));
        result.addAll(results);
        return new ArrayList<String>(result);
    }

    public List<String> getApprovedContractorsForCreateContractorBill(final String contractorname) {
        final Set<String> result = new HashSet<String>();
        final List<String> results = letterOfAcceptanceRepository.findContractorForContractorBill(
                "%" + contractorname + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        results.addAll(letterOfAcceptanceRepository.findContractorForContractorBillWithMB("%" + contractorname + "%",
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString(),
                BillTypes.Final_Bill.toString()));
        result.addAll(results);
        return new ArrayList<String>(result);
    }

    public Boolean validateContractorBillInWorkflowForWorkorder(final Long workOrderId) {
        final List<String> results = letterOfAcceptanceRepository.getContractorBillInWorkflowForWorkorder(workOrderId,
                ContractorBillRegister.BillStatus.CANCELLED.toString(),
                ContractorBillRegister.BillStatus.APPROVED.toString());
        if (results.isEmpty())
            return true;
        else
            return false;
    }

    public List<ContractorDetail> searchContractorDetails(final SearchRequestContractor searchRequestContractor) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ContractorDetail.class, "cd")
                .createAlias("contractor", "contractor").createAlias("cd.status", "status");
        if (searchRequestContractor != null) {
            if (searchRequestContractor.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id", searchRequestContractor.getDepartment()));
            if (searchRequestContractor.getContractorClass() != null)
                criteria.add(Restrictions.ge("grade.id", searchRequestContractor.getContractorClass()));
            if (searchRequestContractor.getContractorCode() != null)
                criteria.add(
                        Restrictions.eq("contractor.code", searchRequestContractor.getContractorCode()).ignoreCase());
            if (searchRequestContractor.getNameOfAgency() != null)
                criteria.add(Restrictions.ilike("contractor.name", searchRequestContractor.getNameOfAgency(),
                        MatchMode.ANYWHERE));
        }
        criteria.add(Restrictions.eq("status.code", WorksConstants.ACTIVE).ignoreCase());
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findLoaWorkOrderNumberForMilestone(final String workOrderNumber) {
        final List<WorkOrder> workorders = letterOfAcceptanceRepository
                .findByWorkOrderNumberContainingIgnoreCaseAndEgwStatus_codeEqualsAndParent_idIsNull(workOrderNumber,
                        WorksConstants.APPROVED);
        final List<String> results = new ArrayList<String>();
        for (final WorkOrder details : workorders)
            results.add(details.getWorkOrderNumber());
        return results;
    }

    public List<String> findWorkIdentificationNumbersToCreateMilestone(final String code) {
        final List<String> workIdNumbers = letterOfAcceptanceRepository
                .findWorkIdentificationNumberToCreateMilestone("%" + code + "%");
        return workIdNumbers;
    }

    public List<WorkOrderEstimate> getLoaForCreateMilestone(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClause(searchRequestLetterOfAcceptance, queryStr);
        final Query query = setParameterForMilestone(searchRequestLetterOfAcceptance, queryStr);
        final List<WorkOrderEstimate> workOrderEstimateList = query.getResultList();
        return workOrderEstimateList;
    }

    private void buildWhereClause(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {

        queryStr.append(
                "select distinct(woe) from WorkOrderEstimate woe where woe.workOrder.egwStatus.moduletype = :moduleType and woe.workOrder.egwStatus.code = :status and woe.workOrder.parent.id is null and not exists (select ms.workOrderEstimate.workOrder.id from Milestone ms where ms.workOrderEstimate.workOrder.id = woe.workOrder.id and upper(woe.workOrder.egwStatus.code)  != upper(:workorderstatus) and upper(ms.status.code)  != upper(:milestonestatus))")
        .append(" and woe.estimate.executingDepartment.id = :departmentName");

        if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkIdentificationNumber()))
            queryStr.append(" and upper(woe.estimate.projectCode.code) = :workIdentificationNumber");

        if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getEstimateNumber()))
            queryStr.append(" and upper(woe.estimate.estimateNumber) = :estimateNumber");

        if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkOrderNumber()))
            queryStr.append(" and upper(woe.workOrder.workOrderNumber) = :workOrderNumber");

        if (searchRequestLetterOfAcceptance.getTypeOfWork() != null)
            queryStr.append(" and woe.estimate.parentCategory.id = :typeOfWork");

        if (searchRequestLetterOfAcceptance.getSubTypeOfWork() != null)
            queryStr.append(" and woe.estimate.category.id = :subTypeOfWork");

        if (searchRequestLetterOfAcceptance.getAdminSanctionFromDate() != null)
            queryStr.append(
                    " and woe.estimate.adminSanctionDate >= :adminSanctionFromDate)");

        if (searchRequestLetterOfAcceptance.getAdminSanctionToDate() != null)
            queryStr.append(
                    " and woe.estimate.adminSanctionDate <= :adminSanctionToDate)");

    }

    private Query setParameterForMilestone(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("status", WorksConstants.APPROVED);
        qry.setParameter("moduleType", WorksConstants.WORKORDER);
        qry.setParameter("workorderstatus", WorksConstants.CANCELLED_STATUS);
        qry.setParameter("milestonestatus", WorksConstants.CANCELLED_STATUS);
        if (searchRequestLetterOfAcceptance != null) {
            qry.setParameter("departmentName", searchRequestLetterOfAcceptance.getDepartmentName());
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkIdentificationNumber()))
                qry.setParameter("workIdentificationNumber",
                        searchRequestLetterOfAcceptance.getWorkIdentificationNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getEstimateNumber()))
                qry.setParameter("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkOrderNumber()))
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getTypeOfWork() != null)
                qry.setParameter("typeOfWork", searchRequestLetterOfAcceptance.getTypeOfWork());
            if (searchRequestLetterOfAcceptance.getSubTypeOfWork() != null)
                qry.setParameter("subTypeOfWork", searchRequestLetterOfAcceptance.getSubTypeOfWork());
            if (searchRequestLetterOfAcceptance.getAdminSanctionFromDate() != null)
                qry.setParameter("adminSanctionFromDate", searchRequestLetterOfAcceptance.getAdminSanctionFromDate());
            if (searchRequestLetterOfAcceptance.getAdminSanctionToDate() != null)
                qry.setParameter("adminSanctionToDate", searchRequestLetterOfAcceptance.getAdminSanctionToDate());

        }
        return qry;
    }

    public Double getGrossBillAmountOfBillsCreated(final String workOrderNumber, final String status,
            final String billstatus) {
        return letterOfAcceptanceRepository.getGrossBillAmountOfBillsCreated(workOrderNumber, status, billstatus);
    }

    @Transactional
    public WorkOrder forward(final WorkOrder workOrder, final Long approvalPosition, final String approvalComent,
            final String additionalRule, final String workFlowAction) throws ValidationException {

        createWorkOrderWorkflowTransition(workOrder, approvalPosition, approvalComent, additionalRule, workFlowAction);

        final WorkOrder savedworkOrder = letterOfAcceptanceRepository.save(workOrder);

        workOrderStatusChange(savedworkOrder, workFlowAction);

        return savedworkOrder;

    }

    private void workOrderStatusChange(final WorkOrder workOrder, final String workFlowAction) {

        if (WorksConstants.ACTION_APPROVE.equalsIgnoreCase(workFlowAction))
            workOrder.setEgwStatus(
                    worksUtils.getStatusByModuleAndCode(WorksConstants.WORKORDER, WorksConstants.APPROVED));

        else if ((WorksConstants.RESUBMITTED_STATUS.equalsIgnoreCase(workOrder.getEgwStatus().getCode())
                || WorksConstants.CREATED_STATUS.equalsIgnoreCase(workOrder.getEgwStatus().getCode())
                || WorksConstants.CHECKED_STATUS.equalsIgnoreCase(workOrder.getEgwStatus().getCode()))
                && workOrder.getState() != null && WorksConstants.SUBMIT_ACTION.equalsIgnoreCase(workFlowAction))
            workOrder.setEgwStatus(
                    worksUtils.getStatusByModuleAndCode(WorksConstants.WORKORDER, WorksConstants.CHECKED_STATUS));

    }

    @Transactional
    public WorkOrder update(final WorkOrder workOrder, final AbstractEstimate abstractEstimate,
            final Double appropriationAmount, final Double revisedWorkOrderAmount) throws ValidationException {
        final WorkOrderHistory history = new WorkOrderHistory();
        history.setWorkOrder(workOrder);
        history.setWorkOrderAmount(workOrder.getWorkOrderAmount());
        history.setRevisedWorkOrderAmount(revisedWorkOrderAmount);

        workOrderHistoryRepository.save(history);

        workOrder.setWorkOrderAmount(revisedWorkOrderAmount);
        if (StringUtils.isNotBlank(workOrder.getPercentageSign()) && workOrder.getPercentageSign().equals("-"))
            workOrder.setTenderFinalizedPercentage(workOrder.getTenderFinalizedPercentage() * -1);
        if (workOrder.getPercentageSign().equals("+")) {
            if (appropriationAmount > 0 && !BudgetControlType.BudgetCheckOption.NONE.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {

                final List<Long> budgetheadid = new ArrayList<Long>();
                budgetheadid.add(abstractEstimate.getFinancialDetails().get(0).getBudgetGroup().getId());
                final boolean flag = estimateAppropriationService.checkConsumeEncumbranceBudgetForAbstractEstimate(
                        abstractEstimate,
                        worksUtils.getFinancialYearByDate(new Date()).getId(), appropriationAmount, budgetheadid);

                if (!flag)
                    throw new ValidationException("", "error.budgetappropriation.insufficient.amount");
            }
        } else if (workOrder.getPercentageSign().equals("-"))
            if (appropriationAmount > 0 && !BudgetControlType.BudgetCheckOption.NONE.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {
                final String appropriationNumber = estimateAppropriationService
                        .generateBudgetAppropriationNumber(abstractEstimate.getEstimateDate());
                try{              
                    estimateAppropriationService.releaseBudgetOnRejectForEstimate(abstractEstimate, appropriationAmount,
                                        appropriationNumber);
                }catch(final ValidationException v) {
                    throw new ValidationException(v.getErrors());
                }
            }
        final WorkOrder savedworkOrder = letterOfAcceptanceRepository.save(workOrder);
        return savedworkOrder;
    }

    public List<WorkOrderEstimate> searchLetterOfAcceptanceToModify(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                "select distinct(woe) from WorkOrderEstimate woe where woe.workOrder.egwStatus.code =:workOrderStatus and not exists (select distinct(cbr.workOrderEstimate.workOrder) from ContractorBillRegister as cbr where woe.workOrder.id = cbr.workOrderEstimate.workOrder.id and upper(cbr.billstatus) != :billstatus and cbr.billtype = :billtype)");

        queryStr.append(
                " and not exists (select woa.workOrderEstimate from WorkOrderActivity as woa where woe.id = woa.workOrderEstimate.id )");

        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                queryStr.append(" and upper(woe.workOrder.workOrderNumber) =:workOrderNumber");
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                queryStr.append(" and woe.workOrder.fileNumber like upper(:fileNumber)");
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                queryStr.append(" and woe.workOrder.workOrderDate >= :workOrderFromDate");
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                queryStr.append(" and woe.workOrder.workOrderDate <= :workOrderToDate");
            if (searchRequestLetterOfAcceptance.getName() != null)
                queryStr.append(
                        " and (upper(woe.workOrder.contractor.name) like upper(:contractorName) or upper(woe.workOrder.contractor.code) like upper(:contractorCode)) ");
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                queryStr.append(" and woe.estimate.executingDepartment.id =:department");
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                queryStr.append(" and upper(woe.estimate.estimateNumber) =:estimateNumber");

        }
        final Query query = setQueryParametersForModifyLOA(searchRequestLetterOfAcceptance, queryStr);
        workOrderEstimateList = query.getResultList();
        return workOrderEstimateList;
    }

    private Query setQueryParametersForModifyLOA(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                qry.setParameter("fileNumber", "%" + searchRequestLetterOfAcceptance.getFileNumber() + "%");
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                qry.setParameter("workOrderFromDate", searchRequestLetterOfAcceptance.getFromDate());
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                qry.setParameter("workOrderToDate", searchRequestLetterOfAcceptance.getToDate());
            if (searchRequestLetterOfAcceptance.getName() != null) {
                qry.setParameter("contractorName", "%" + searchRequestLetterOfAcceptance.getName() + "%");
                qry.setParameter("contractorCode", "%" + searchRequestLetterOfAcceptance.getName() + "%");
            }
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                qry.setParameter("department", searchRequestLetterOfAcceptance.getDepartmentName());
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                qry.setParameter("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            qry.setParameter("workOrderStatus", WorksConstants.APPROVED);
            qry.setParameter("billtype", BillTypes.Final_Bill.toString());
            qry.setParameter("billstatus", BillStatus.CANCELLED.toString());
        }
        return qry;
    }

    public List<WorkOrder> searchLOAsToCancel(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        List<WorkOrder> workOrderList = new ArrayList<WorkOrder>();
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                "select distinct(wo) from WorkOrder wo where  wo.parent.id is null and wo.egwStatus.code =:workOrderStatus");
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                queryStr.append(" and upper(wo.workOrderNumber) like upper(:workOrderNumber)");
            if (searchRequestLetterOfAcceptance.getContractor() != null)
                queryStr.append(
                        " and upper(wo.contractor.name) like upper(:contractorName) or upper(wo.contractor.code) like upper(:contractorCode) ");
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                queryStr.append(
                        " and exists (select distinct(woe.workOrder) from WorkOrderEstimate woe where woe.estimate.executingDepartment.id = :department and woe.workOrder = wo)");
            if (searchRequestLetterOfAcceptance.getWorkIdentificationNumber() != null)
                queryStr.append(
                        " and exists (select distinct(woe.workOrder) from WorkOrderEstimate woe where woe.estimate.projectCode.code = :projectCode and woe.workOrder = wo)");
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null
                    && !searchRequestLetterOfAcceptance.getEgwStatus().equals(WorksConstants.APPROVED))
                queryStr.append(
                        " and wo.id = (select distinct(os.objectId) from OfflineStatus as os where os.id = (select max(status.id) from OfflineStatus status where status.objectType = :objectType and status.objectId = wo.id) and os.objectId = wo.id and lower(os.egwStatus.code) = :offlineStatus and os.objectType = :objectType )");
        }
        final Query query = setQueryParameters(searchRequestLetterOfAcceptance, queryStr);
        workOrderList = query.getResultList();
        return workOrderList;
    }

    private Query setQueryParameters(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", "%" + searchRequestLetterOfAcceptance.getWorkOrderNumber() + "%");
            if (searchRequestLetterOfAcceptance.getContractor() != null) {
                qry.setParameter("contractorName", "%" + searchRequestLetterOfAcceptance.getContractor() + "%");
                qry.setParameter("contractorCode", "%" + searchRequestLetterOfAcceptance.getContractor() + "%");
            }
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                qry.setParameter("department", searchRequestLetterOfAcceptance.getDepartmentName());
            if (searchRequestLetterOfAcceptance.getWorkIdentificationNumber() != null)
                qry.setParameter("projectCode", searchRequestLetterOfAcceptance.getWorkIdentificationNumber());
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null
                    && !searchRequestLetterOfAcceptance.getEgwStatus().equals(WorksConstants.APPROVED)) {
                qry.setParameter("offlineStatus",
                        searchRequestLetterOfAcceptance.getEgwStatus().toString().toLowerCase());
                qry.setParameter("objectType", WorksConstants.WORKORDER);
            }
            qry.setParameter("workOrderStatus", WorksConstants.APPROVED);
        }
        return qry;
    }

    public List<String> findWorkIdentificationNumbersToSearchLOAToCancel(final String code) {
        final List<String> workIdNumbers = letterOfAcceptanceRepository
                .findWorkIdentificationNumbersToSearchLOAToCancel("%" + code + "%", WorksConstants.APPROVED.toString());
        return workIdNumbers;
    }

    public List<String> findContractorsToSearchLOAToCancel(final String code) {
        final List<String> contractors = letterOfAcceptanceRepository
                .findContractorsToSearchLOAToCancel("%" + code + "%", WorksConstants.APPROVED.toString());
        return contractors;
    }

    public String checkIfBillsCreated(final Long id) {
        String billNumbers = "";
        final WorkOrder workOrder = letterOfAcceptanceRepository.findById(id);
        final List<ContractorBillRegister> bills = contractorBillRegisterRepository
                .findByWorkOrderAndBillstatusNot(workOrder, ContractorBillRegister.BillStatus.CANCELLED.toString());
        if (bills == null || bills.isEmpty())
            return "";
        else
            for (final ContractorBillRegister cbr : bills)
                billNumbers += cbr.getBillnumber() + ", ";
        return billNumbers;
    }

    public boolean checkIfMileStonesCreated(final WorkOrder workOrder) {
        Boolean flag = false;
        for (final WorkOrderEstimate woe : workOrder.getWorkOrderEstimates()) {
            final List<Milestone> milestones = milestoneService.getMilestoneByWorkOrderEstimateId(woe.getId());
            for (final Milestone ms : milestones) {
                if (!ms.getStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED)) {
                    flag = true;
                    break;
                }
                if (flag)
                    break;
            }
        }
        return flag;
    }

    @Transactional
    public WorkOrder cancel(final WorkOrder workOrder) {
        workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                WorksConstants.CANCELLED_STATUS));
        workOrder.setStatus(WorksConstants.CANCELLED.toString());
        return letterOfAcceptanceRepository.save(workOrder);
    }

    public List<WorkOrder> findWorkOrderByEstimateNumberAndEgwStatus(final String estimateNumber) {
        return letterOfAcceptanceRepository.findByEstimateNumberAndEgwStatus_codeEquals(estimateNumber,
                WorksConstants.APPROVED);
    }

    public List<String> getEstimateNumbersToCancelLineEstimate(final Long lineEstimateId) {
        final List<String> estimateNumbers = letterOfAcceptanceRepository
                .findEstimateNumbersToCancelLineEstimate(lineEstimateId, WorksConstants.APPROVED);
        return estimateNumbers;
    }

    public List<String> getWorkOrderNumbersForViewEstimatePhotograph(final String workOrderNumber) {
        final List<String> workOrderNumbers = letterOfAcceptanceRepository.findworkOrderNumbersToViewEstimatePhotograph(
                "%" + workOrderNumber + "%", WorksConstants.APPROVED.toString());
        return workOrderNumbers;
    }

    public List<String> getContractorsNamesForViewEstimatePhotograph(final String contractorName) {
        final List<String> contractorNames = letterOfAcceptanceRepository.findContractorsToViewEstimatePhotograph(
                "%" + contractorName + "%", WorksConstants.APPROVED.toString());
        return contractorNames;
    }

    public List<String> getEstimateNumbersForApprovedLoa(final String estimateNumber) {
        final List<WorkOrder> workorders = letterOfAcceptanceRepository
                .findByEstimateNumberContainingIgnoreCaseAndEgwStatus_codeEquals(estimateNumber,
                        WorksConstants.APPROVED);
        final List<String> results = new ArrayList<String>();
        for (final WorkOrder details : workorders)
            results.add(details.getEstimateNumber());
        return results;
    }

    public WorkOrder getWorkOrderDocuments(final WorkOrder workOrder) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(workOrder.getId(), WorksConstants.WORKORDER);
        workOrder.setDocumentDetails(documentDetailsList);
        return workOrder;
    }

    public List<Long> getWorkOrdersForLoaStatus(final String offlineStatus) {
        return letterOfAcceptanceRepository.findWorkOrderForLoaStatus(offlineStatus, WorksConstants.WORKORDER);
    }

    public List<WorkOrderEstimate> searchLetterOfAcceptanceForOfflineStatus(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        List<WorkOrderEstimate> workOrderList = new ArrayList<WorkOrderEstimate>();
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                "select distinct(woe) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and  woe.workOrder.egwStatus.code =:workOrderStatus ");
        queryStr.append(
                " and exists (select woa.workOrderEstimate from WorkOrderActivity as woa where woe.id = woa.workOrderEstimate.id )");
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                queryStr.append(" and upper(woe.workOrder.workOrderNumber) =:workOrderNumber");
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                queryStr.append(" and woe.workOrder.fileNumber like upper(:fileNumber)");
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                queryStr.append(" and woe.workOrder.workOrderDate >= :workOrderFromDate");
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                queryStr.append(" and woe.workOrder.workOrderDate <= :workOrderToDate");
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                queryStr.append(" and woe.estimate.executingDepartment.id =:department");
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                queryStr.append(" and upper(woe.estimate.estimateNumber) =:estimateNumber");
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                if (searchRequestLetterOfAcceptance.getEgwStatus().equals(WorksConstants.APPROVED))
                    queryStr.append(
                            " and not exists (select distinct(os.objectId) from OfflineStatus as os where os.objectType = :objectType and woe.workOrder.id = os.objectId )");
                else if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                    queryStr.append(
                            " and woe.workOrder.id = (select distinct(os.objectId) from OfflineStatus as os where os.id = (select max(status.id) from OfflineStatus status where status.objectType = :objectType and status.objectId = woe.workOrder.id) and os.objectId = woe.workOrder.id and lower(os.egwStatus.code) = :offlineStatus and os.objectType = :objectType )");
        }
        if (searchRequestLetterOfAcceptance.getContractorName() != null)
            queryStr.append(
                    " and (upper(woe.workOrder.contractor.name) like upper(:contractorName) or upper(woe.workOrder.contractor.code) like upper(:contractorCode)) ");
        final Query query = setQueryParametersForOfflineStatus(searchRequestLetterOfAcceptance, queryStr);
        workOrderList = query.getResultList();
        return workOrderList;
    }

    private Query setQueryParametersForOfflineStatus(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                qry.setParameter("fileNumber", "%" + searchRequestLetterOfAcceptance.getFileNumber() + "%");
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                qry.setParameter("workOrderFromDate", searchRequestLetterOfAcceptance.getFromDate());
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                qry.setParameter("workOrderToDate", searchRequestLetterOfAcceptance.getToDate());
            if (searchRequestLetterOfAcceptance.getContractorName() != null) {
                qry.setParameter("contractorName", "%" + searchRequestLetterOfAcceptance.getContractorName() + "%");
                qry.setParameter("contractorCode", "%" + searchRequestLetterOfAcceptance.getContractorName() + "%");
            }
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                qry.setParameter("department", searchRequestLetterOfAcceptance.getDepartmentName());
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                qry.setParameter("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null) {
                qry.setParameter("objectType", WorksConstants.WORKORDER);
                if (!searchRequestLetterOfAcceptance.getEgwStatus().equals(WorksConstants.APPROVED))
                    qry.setParameter("offlineStatus",
                            searchRequestLetterOfAcceptance.getEgwStatus().toString().toLowerCase());
            }
            qry.setParameter("workOrderStatus", WorksConstants.APPROVED);

        }
        return qry;
    }

    public String checkIfMBCreatedForLOA(final WorkOrderEstimate workOrderEstimate) {
        String mbrefNumbres = "";
        final List<MBHeader> mbHeaders = mBHeaderService.getMBHeadersToCancelLOA(workOrderEstimate);
        for (final MBHeader mBHeader : mbHeaders)
            mbrefNumbres += mBHeader.getMbRefNo() + ", ";
        if (mbrefNumbres.equals(""))
            return "";
        else
            return mbrefNumbres;
    }

    public String checkIfARFCreatedForLOA(final WorkOrderEstimate workOrderEstimate) {
        String arfNumbers = StringUtils.EMPTY;
        final List<ContractorAdvanceRequisition> advanceRequisitions = contractorAdvanceService
                .getContractorAdvancesToCancelLOA(workOrderEstimate);
        for (final ContractorAdvanceRequisition advanceRequisition : advanceRequisitions)
            arfNumbers += advanceRequisition.getAdvanceRequisitionNumber() + ", ";
        if (arfNumbers.equals(StringUtils.EMPTY))
            return StringUtils.EMPTY;
        else
            return arfNumbers;
    }

    public List<String> getApprovedEstimateNumbersForModfyLOA(final String estimateNumber) {
        final List<String> estimateNumbers = letterOfAcceptanceRepository
                .findEstimateNumbersToModifyLOA("%" + estimateNumber + "%", WorksConstants.APPROVED.toString());
        return estimateNumbers;
    }

    public List<String> getApprovedWorkOrderNumberForModfyLOA(final String workOrderNumber) {
        final List<String> workOrderNumbers = letterOfAcceptanceRepository
                .findWorkOrderNumbersToModifyLOA("%" + workOrderNumber + "%", WorksConstants.APPROVED.toString());
        return workOrderNumbers;
    }

    public List<String> getApprovedEstimateNumbersForSetOfflineStatus(final String estimateNumber) {
        final List<String> estimateNumbers = letterOfAcceptanceRepository
                .findEstimateNumbersToSetOfflineStatus("%" + estimateNumber + "%", WorksConstants.APPROVED.toString());
        return estimateNumbers;
    }

    public List<String> getApprovedWorkOrderNumberForSetOfflineStatus(final String workOrderNumber) {
        final List<String> workOrderNumbers = letterOfAcceptanceRepository.findWorkOrderNumbersToSetOfflineStatus(
                "%" + workOrderNumber + "%", WorksConstants.APPROVED.toString());
        return workOrderNumbers;
    }

    public List<String> getApprovedContractorForSetOfflineStatus(final String contractorName) {
        final List<String> contractorNames = letterOfAcceptanceRepository
                .findContractorToSetOfflineStatus("%" + contractorName + "%", WorksConstants.APPROVED.toString());
        return contractorNames;
    }

    public List<String> getApprovedContractorsForModfyLOA(final String contractorName) {
        final List<String> contractorNames = letterOfAcceptanceRepository
                .findContractorToModifyLOA("%" + contractorName + "%", WorksConstants.APPROVED.toString());
        return contractorNames;
    }

    public List<User> getWorkAssignedUsers() {
        return letterOfAcceptanceRepository.getWorkAssignedUsers(WorksConstants.APPROVED.toString());
    }

    public List<String> findContractorsToSearchLOAToCreateRE(final String code) {
        final List<String> contractors = letterOfAcceptanceRepository
                .findContractorsToSearchLOAToCreateRE("%" + code + "%", WorksConstants.APPROVED.toString());
        return contractors;
    }

    public List<String> findContractorsToCreateCR(final String code) {
        final List<String> contractors = letterOfAcceptanceRepository.findContractorsToCreateCR("%" + code + "%",
                WorksConstants.APPROVED);
        return contractors;
    }

    public List<String> findEstimateNumbersToCreateCR(final String estimateNumber) {
        final List<String> estimateNumbers = letterOfAcceptanceRepository.findEstimateNumbersToCreateCR(
                "%" + estimateNumber + "%", WorksConstants.APPROVED, WorksConstants.CANCELLED_STATUS);
        return estimateNumbers;
    }

    public List<String> findWorkOrderNumbersToCreateCR(final String workOrderNumber) {
        final List<String> workOrderNumbers = letterOfAcceptanceRepository.findWorkOrderNumbersToCreateCR(
                "%" + workOrderNumber + "%", WorksConstants.APPROVED, WorksConstants.CANCELLED_STATUS);
        return workOrderNumbers;
    }

    public void sendSmsToContractor(final WorkOrderEstimate workOrderEstimate) {
        if (workOrderEstimate.getEstimate().getLineEstimateDetails() != null
                && !workOrderEstimate.getEstimate().getLineEstimateDetails().getLineEstimate().isWorkOrderCreated()) {
            final String mobileNo = workOrderEstimate.getWorkOrder().getContractor().getMobileNumber();

            if (StringUtils.isNotBlank(mobileNo)) {
                final String smsMessage = messageSource
                        .getMessage("msg.contractor.loa.approval.sms",
                                new String[] { workOrderEstimate.getWorkOrder().getWorkOrderNumber(),
                                        String.format("%.2f",
                                                Double.valueOf(
                                                        workOrderEstimate.getWorkOrder().getWorkOrderAmount())) },
                                null);
                messagingService.sendSMS(mobileNo, smsMessage);
            }
        }
    }

    public void sendEmailToContractor(final WorkOrderEstimate workOrderEstimate) {
        if (workOrderEstimate.getEstimate().getLineEstimateDetails() != null
                && !workOrderEstimate.getEstimate().getLineEstimateDetails().getLineEstimate().isWorkOrderCreated()) {
            final String emailID = workOrderEstimate.getWorkOrder().getContractor().getEmail();

            if (StringUtils.isNotBlank(emailID)) {
                final Date workOrderDate = workOrderEstimate.getWorkOrder().getWorkOrderDate();
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                final String emailSubject = messageSource.getMessage("msg.contractor.loa.approval.email.subject",
                        new String[] { sdf.format(workOrderDate) }, null);
                final String emailMessage = messageSource.getMessage("msg.contractor.loa.approval.email.message",
                        new String[] { workOrderEstimate.getEstimate().getEstimateNumber(),
                                workOrderEstimate.getWorkOrder().getWorkOrderNumber(),
                                String.format("%.2f",
                                        Double.valueOf(workOrderEstimate.getWorkOrder().getWorkOrderAmount())),
                                ApplicationThreadLocals.getMunicipalityName() },
                        null);
                messagingService.sendEmail(emailID, emailSubject, emailMessage);
            }
        }
    }

    public List<String> getContractorsNamesToViewEstimatePhotograph(final String contractorName) {
        return letterOfAcceptanceRepository.findContractorsToSearchEstimatePhotograph("%" + contractorName + "%",
                WorksConstants.APPROVED);
    }

    public List<String> getWorkOrderNumbersToViewEstimatePhotograph(final String workOrderNumber) {
        return letterOfAcceptanceRepository.findworkOrderNumbersToSearchEstimatePhotograph("%" + workOrderNumber + "%",
                WorksConstants.APPROVED);
    }
}
