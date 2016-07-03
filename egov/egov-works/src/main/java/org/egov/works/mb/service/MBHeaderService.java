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
package org.egov.works.mb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.SearchRequestMBHeader;
import org.egov.works.mb.repository.MBHeaderRepository;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.service.OfflineStatusService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
@Transactional(readOnly = true)
public class MBHeaderService {

    private static final Logger LOG = LoggerFactory.getLogger(MBHeaderService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final MBHeaderRepository mbHeaderRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<MBHeader> mbHeaderWorkflowService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;
    
    @Autowired
    private OfflineStatusService offlineStatusService;
    
    @Autowired
    private AppConfigValueService appConfigValuesService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public MBHeaderService(final MBHeaderRepository mbHeaderRepository) {
        this.mbHeaderRepository = mbHeaderRepository;
    }

    public MBHeader getMBHeaderById(final Long id) {
        return mbHeaderRepository.findOne(id);
    }

    public List<MBHeader> getApprovedMBsForContractorBillByWorkOrderEstimateId(final Long workOrderEstimateId) {
        return mbHeaderRepository.findByWorkOrderEstimateId(workOrderEstimateId, WorksConstants.APPROVED,
                WorksConstants.CANCELLED);
    }

    public List<MBHeader> getMBHeadersByWorkOrder(final WorkOrder workOrder) {
        return mbHeaderRepository.findByWorkOrder(workOrder);
    }

    public List<MBHeader> getMBHeadersByWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        return mbHeaderRepository.findByWorkOrderEstimate(workOrderEstimate);
    }

    public List<MBHeader> getApprovedMBHeadersByWorkOrder(final WorkOrder workOrder) {
        return mbHeaderRepository.findByWorkOrderAndEgwStatus_codeEqualsOrderById(workOrder,
                MBHeader.MeasurementBookStatus.APPROVED.toString());
    }

    public List<MBHeader> getApprovedMBHeadersByContractorBill(final ContractorBillRegister contractorBillRegister) {
        return mbHeaderRepository.findByEgBillregisterAndEgwStatus_codeEquals(contractorBillRegister,
                MBHeader.MeasurementBookStatus.APPROVED.toString());
    }

    public List<MBHeader> getMBHeadersByContractorBill(final ContractorBillRegister contractorBillRegister) {
        return mbHeaderRepository.findByEgBillregister(contractorBillRegister);
    }
    
    public List<MBHeader> getMBHeadersByWorkOrderEstimateIdAndNotEgwStatusCode(final Long workOrderEstimateId, final String statusCode) {
        return mbHeaderRepository.findByWorkOrderEstimate_IdAndEgwStatus_codeNotOrderById(workOrderEstimateId, statusCode);
    }

    @Transactional
    public MBHeader create(final MBHeader mbHeader) {
        final MBHeader savedMBHeader = mbHeaderRepository.save(mbHeader);
        return savedMBHeader;
    }

    @Transactional
    public MBHeader create(final MBHeader mbHeader, final Long approvalPosition, final String approvalComent,
            final String workFlowAction) {
        mbHeader.setWorkOrder(letterOfAcceptanceService.getWorkOrderById(mbHeader.getWorkOrder().getId()));
        mbHeader.setWorkOrderEstimate(workOrderEstimateService.getWorkOrderEstimateById(mbHeader.getWorkOrderEstimate().getId()));
        if (mbHeader.getState() == null)
            if (workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.MBHEADER, MBHeader.MeasurementBookStatus.CREATED.toString()));
            else
                mbHeader.setEgwStatus(worksUtils
                        .getStatusByModuleAndCode(WorksConstants.MBHEADER, MBHeader.MeasurementBookStatus.NEW.toString()));
        mergeSorAndNonSorMBDetails(mbHeader);
        final MBHeader savedMBHeader = mbHeaderRepository.save(mbHeader);

        createMBHeaderWorkflowTransition(savedMBHeader, approvalPosition, approvalComent, null,
                workFlowAction);

        return savedMBHeader;
    }

    @Transactional
    public MBHeader update(final MBHeader mbHeader, final Long approvalPosition, final String approvalComent,
            final String workFlowAction, final String removedDetailIds) {
        
        Boolean isMBEditable = false;
        
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_MB_SECOND_LEVEL_EDIT);
                final AppConfigValues value = values.get(0);
                if (value.getValue().equalsIgnoreCase("Yes"))
                    isMBEditable = true;

        if ((mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString())
                || mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.REJECTED.toString()) || isMBEditable)
                && !workFlowAction.equals(WorksConstants.CANCEL_ACTION)) {
            mergeSorAndNonSorMBDetails(mbHeader);
            List<MBDetails> mbDetails = new ArrayList<MBDetails>(mbHeader.getMbDetails());
            mbDetails = removeDeletedMBDetails(mbDetails, removedDetailIds);
            mbHeader.setMbDetails(mbDetails);
            for (final MBDetails details : mbHeader.getMbDetails())
                details.setMbHeader(mbHeader);
        }

        final MBHeader updatedMBHeader = mbHeaderRepository.save(mbHeader);

        mbHeaderStatusChange(mbHeader, workFlowAction);

        if (mbHeader.getEgwStatus().getCode().equalsIgnoreCase(MBHeader.MeasurementBookStatus.APPROVED.toString()))
            mbHeader.setApprovedDate(new Date());

        createMBHeaderWorkflowTransition(updatedMBHeader, approvalPosition, approvalComent, null,
                workFlowAction);

        return updatedMBHeader;
    }

    private List<MBDetails> removeDeletedMBDetails(final List<MBDetails> mbDetails, final String removedDetailIds) {
        final List<MBDetails> mbDetailsList = new ArrayList<MBDetails>();
        if (null != removedDetailIds) {
            final String[] ids = removedDetailIds.split(",");
            final List<String> strList = new ArrayList<String>();
            for (final String str : ids)
                strList.add(str);
            for (final MBDetails details : mbDetails)
                if (details.getId() != null) {
                    if (!strList.contains(details.getId().toString()))
                        mbDetailsList.add(details);
                } else
                    mbDetailsList.add(details);
        } else
            return mbDetails;
        return mbDetailsList;
    }

    private void mergeSorAndNonSorMBDetails(final MBHeader mbHeader) {
        for (final MBDetails mbDetails : mbHeader.getSorMbDetails())
            if (mbDetails.getId() == null) {
                mbDetails.setMbHeader(mbHeader);
                mbDetails.setWorkOrderActivity(
                        workOrderActivityService.getWorkOrderActivityById(mbDetails.getWorkOrderActivity().getId()));
                mbHeader.addMbDetails(mbDetails);
            } else
                for (final MBDetails oldMBDetails : mbHeader.getSORMBDetails())
                    if (oldMBDetails.getId().equals(mbDetails.getId()))
                        updateMBDetails(oldMBDetails, mbDetails);
        for (final MBDetails mbDetails : mbHeader.getNonSorMbDetails())
            if (mbDetails.getId() == null) {
                mbDetails.setMbHeader(mbHeader);
                mbDetails.setWorkOrderActivity(
                        workOrderActivityService.getWorkOrderActivityById(mbDetails.getWorkOrderActivity().getId()));
                mbHeader.addMbDetails(mbDetails);
            } else
                for (final MBDetails oldMBDetails : mbHeader.getNonSORMBDetails())
                    if (oldMBDetails.getId().equals(mbDetails.getId()))
                        updateMBDetails(oldMBDetails, mbDetails);
    }

    private void updateMBDetails(final MBDetails oldMBDetails, final MBDetails mbDetails) {
        oldMBDetails.setQuantity(mbDetails.getQuantity());
        oldMBDetails.setRate(mbDetails.getRate());
        oldMBDetails.setRemarks(mbDetails.getRemarks());
    }

    public void mbHeaderStatusChange(final MBHeader mbHeader, final String workFlowAction) {
        if (null != mbHeader && null != mbHeader.getEgwStatus()
                && null != mbHeader.getEgwStatus().getCode())
            if (workFlowAction.equals(WorksConstants.SAVE_ACTION))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                        MBHeader.MeasurementBookStatus.NEW.toString()));
            else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.NEW.toString()))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                        MBHeader.MeasurementBookStatus.CREATED.toString()));
            else if (workFlowAction.equals(WorksConstants.APPROVE_ACTION))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                        MBHeader.MeasurementBookStatus.APPROVED.toString()));
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                        MBHeader.MeasurementBookStatus.REJECTED.toString()));
            else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                        MBHeader.MeasurementBookStatus.CANCELLED.toString()));
            else if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                        MBHeader.MeasurementBookStatus.CREATED.toString()));
    }

    @Transactional
    public MBHeader cancel(final MBHeader mbHeader) {
        mbHeader.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                MBHeader.MeasurementBookStatus.CANCELLED.toString()));
        final MBHeader savedMBHeader = mbHeaderRepository.save(mbHeader);
        return savedMBHeader;
    }

    public List<User> getMBHeaderCreatedByUsers() {
        return mbHeaderRepository.findMBHeaderCreatedByUsers();
    }

    public List<MBHeader> searchMBHeader(final SearchRequestMBHeader searchRequestMBHeader) {

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(MBHeader.class, "mbh")
                .createAlias("workOrderEstimate", "woe").createAlias("workOrderEstimate.workOrder", "wo")
                .createAlias("workOrderEstimate.estimate", "e")
                .createAlias("workOrderEstimate.estimate.executingDepartment", "department")
                .createAlias("workOrderEstimate.workOrder.contractor", "woc").createAlias("mbh.egwStatus", "status");

        if (searchRequestMBHeader != null) {
            if (searchRequestMBHeader.getMbReferenceNumber() != null)
                criteria.add(
                        Restrictions.ilike("mbh.mbRefNo", "%" + searchRequestMBHeader.getMbReferenceNumber() + "%"));
            if (searchRequestMBHeader.getWorkOrderNumber() != null)
                criteria.add(
                        Restrictions.eq("wo.workOrderNumber", searchRequestMBHeader.getWorkOrderNumber()).ignoreCase());
            if (searchRequestMBHeader.getFromDate() != null)
                criteria.add(Restrictions.ge("mbh.mbDate", searchRequestMBHeader.getFromDate()));
            if (searchRequestMBHeader.getToDate() != null)
                criteria.add(Restrictions.le("mbh.mbDate", searchRequestMBHeader.getToDate()));
            if (searchRequestMBHeader.getEstimateNumber() != null)
                criteria.add(
                        Restrictions.eq("e.estimateNumber", searchRequestMBHeader.getEstimateNumber()).ignoreCase());
            if (StringUtils.isNotBlank(searchRequestMBHeader.getContractorName()))
                criteria.add(Restrictions.ge("woc.name", searchRequestMBHeader.getContractorName()));
            if (searchRequestMBHeader.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id", searchRequestMBHeader.getDepartment()));
            if (searchRequestMBHeader.getMbStatus() != null)
                criteria.add(
                        Restrictions.eq("status.id", Integer.valueOf(searchRequestMBHeader.getMbStatus().toString())));
            if (searchRequestMBHeader.getCreatedBy() != null)
                criteria.add(Restrictions.eq("mbh.createdBy.id", searchRequestMBHeader.getCreatedBy()));

        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<EgwStatus> getMBHeaderStatus() {
        final List<EgwStatus> statusList = egwStatusHibernateDAO.getStatusByModule(WorksConstants.MBHEADER);
        final List<EgwStatus> latestStatusList = new ArrayList<EgwStatus>();
        if (!statusList.isEmpty())
            for (final EgwStatus egwStatus : statusList)
                if (!egwStatus.getCode().equals(WorksConstants.NEW))
                    latestStatusList.add(egwStatus);
        return latestStatusList;
    }

    public void validateMBInDrafts(final Long workOrderEstimateId, final JsonObject jsonObject, final BindingResult errors) {
        final MBHeader mbHeader = mbHeaderRepository
                .findByWorkOrderEstimate_IdAndEgwStatus_codeEquals(workOrderEstimateId, WorksConstants.NEW);
        String userName = "";
        if (mbHeader != null) {
            userName = worksUtils.getApproverName(mbHeader.getState().getOwnerPosition().getId());
            jsonObject.addProperty("draftsError", messageSource.getMessage("error.mbheader.newstatus",
                    new String[] { mbHeader.getMbRefNo(), mbHeader.getEgwStatus().getDescription(), userName }, null));
            if (errors != null)
                errors.reject("draftsError", messageSource.getMessage("error.mbheader.newstatus",
                        new String[] { mbHeader.getMbRefNo(), mbHeader.getEgwStatus().getDescription(), userName }, null));
        }
    }

    public void validateMBInWorkFlow(final Long workOrderEstimateId, final JsonObject jsonObject, final BindingResult errors) {
        final MBHeader mBHeader = mbHeaderRepository.findByWorkOrderEstimateAndStatus(workOrderEstimateId,
                WorksConstants.CANCELLED_STATUS, WorksConstants.APPROVED, WorksConstants.NEW);
        String userName = "";
        if (mBHeader != null) {
            userName = worksUtils.getApproverName(mBHeader.getState().getOwnerPosition().getId());
            jsonObject.addProperty("workFlowError", messageSource.getMessage("error.mbheader.workflow",
                    new String[] { mBHeader.getMbRefNo(), mBHeader.getEgwStatus().getDescription(), userName }, null));
            if (errors != null)
                errors.reject("workFlowError", messageSource.getMessage("error.mbheader.workflow",
                        new String[] { mBHeader.getMbRefNo(), mBHeader.getEgwStatus().getDescription(), userName }, null));
        }
    }

    public Long getApprovalPositionByMatrixDesignation(final MBHeader mbHeader, Long approvalPosition,
            final String additionalRule, final String mode, final String workFlowAction) {
        final WorkFlowMatrix wfmatrix = mbHeaderWorkflowService.getWfMatrix(mbHeader.getStateType(),
                null, null, additionalRule, mbHeader.getCurrentState().getValue(), null);
        if (mbHeader.getEgwStatus() != null && mbHeader.getEgwStatus().getCode() != null)
            if (mbHeader.getEgwStatus().getCode().equals(MBHeader.MeasurementBookStatus.CREATED.toString())
                    && mbHeader.getState() != null)
                if (mode.equals("edit"))
                    approvalPosition = mbHeader.getState().getOwnerPosition().getId();
                else
                    approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            mbHeader.getState(), mbHeader.getCreatedBy().getId());
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED))
            approvalPosition = null;

        return approvalPosition;
    }

    public Double getPreviousCumulativeQuantity(final Long mbHeaderId, final Long woActivityId) {
        return mbHeaderRepository.getPreviousCumulativeQuantity(mbHeaderId, WorksConstants.CANCELLED, woActivityId);
    }

    public void createMBHeaderWorkflowTransition(final MBHeader mbHeader,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_DISPLAYNAME_MBHEADER;
        WorkFlowMatrix wfmatrix = null;

        if (null != mbHeader.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(mbHeader.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment))
                mbHeader.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            else
                mbHeader.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.WF_STATE_REJECTED)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition()).withNextAction("")
                        .withNatureOfTask(natureOfwork);
        } else if (WorksConstants.SAVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            wfmatrix = mbHeaderWorkflowService.getWfMatrix(mbHeader.getStateType(), null, null,
                    additionalRule, WorksConstants.NEW, null);
            if (mbHeader.getState() == null)
                mbHeader.transition(true).start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(WorksConstants.ESTIMATE_ONSAVE_NEXTACTION_VALUE).withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            if (null == mbHeader.getState()) {
                wfmatrix = mbHeaderWorkflowService.getWfMatrix(mbHeader.getStateType(), null, null,
                        additionalRule, currState, null);
                mbHeader.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = mbHeaderWorkflowService.getWfMatrix(mbHeader.getStateType(), null, null,
                        additionalRule, mbHeader.getCurrentState().getValue(), null);
                mbHeader.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("").withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = mbHeaderWorkflowService.getWfMatrix(mbHeader.getStateType(), null, null,
                        additionalRule, mbHeader.getCurrentState().getValue(), null);
                mbHeader.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public void fillWorkflowData(final JsonObject jsonObject, final HttpServletRequest request, final MBHeader mbHeader) {
        jsonObject.addProperty("stateType", mbHeader.getClass().getSimpleName());
        if (mbHeader.getCurrentState() != null
                && !mbHeader.getCurrentState().getValue().equals(WorksConstants.NEW))
            jsonObject.addProperty("currentState", mbHeader.getCurrentState().getValue());
        if (mbHeader.getState() != null && mbHeader.getState().getNextAction() != null)
            jsonObject.addProperty("nextAction", mbHeader.getState().getNextAction());

        jsonObject.addProperty("id", mbHeader.getId());

        if (!mbHeader.getMbDetails().isEmpty()) {
            final JsonArray jsonArray = new JsonArray();
            for (final MBDetails mbDetails : mbHeader.getMbDetails()) {
                final JsonObject id = new JsonObject();
                id.addProperty("id", mbDetails.getId());
                if (mbDetails.getWorkOrderActivity().getActivity().getSchedule() != null)
                    id.addProperty("sorType", "SOR");
                else
                    id.addProperty("sorType", "Non SOR");

                jsonArray.add(id);
            }
            jsonObject.add("detailIds", jsonArray);
        } else
            jsonObject.add("detailIds", new JsonArray());
    }
    
    public void validateMBHeader(final MBHeader mbHeader, final JsonObject jsonObject, final BindingResult errors) {
//        validateMBInDrafts(mbHeader.getWorkOrderEstimate().getId(), jsonObject, errors);
//        validateMBInWorkFlow(mbHeader.getWorkOrderEstimate().getId(), jsonObject, errors);
        
        Double totalMBAmountOfMBs = getTotalMBAmountOfMBs(mbHeader.getId(),
                mbHeader.getWorkOrder().getId(), mbHeader.getWorkOrderEstimate().getId(),
                MBHeader.MeasurementBookStatus.CANCELLED.toString());
        
        if (totalMBAmountOfMBs != null) {
            Double totalMBAmount = totalMBAmountOfMBs + mbHeader.getMbAmount().doubleValue();
            if (mbHeader.getWorkOrder()
                    .getWorkOrderAmount() < totalMBAmount) {
                jsonObject.addProperty("errorSumOfMBWorkOrderAmount", messageSource.getMessage("error.sum.mb.workorder.amount",
                        new String[] { totalMBAmount.toString(), new Double(mbHeader.getWorkOrder().getWorkOrderAmount()).toString() },
                        null));
                errors.reject("errorSumOfMBWorkOrderAmount", messageSource.getMessage("error.sum.mb.workorder.amount",
                        new String[] { totalMBAmount.toString(), new Double(mbHeader.getWorkOrder().getWorkOrderAmount()).toString() },
                        null));
            }
        }
        
        final List<MBHeader> mbHeaders = getMBHeadersByWorkOrderEstimateIdAndNotEgwStatusCode(
                mbHeader.getWorkOrderEstimate().getId(), MBHeader.MeasurementBookStatus.CANCELLED.toString());
        if (!mbHeaders.isEmpty()) {
            Integer lastToPageNumber = mbHeaders.get(mbHeaders.size() - 1).getToPageNo();
            if (lastToPageNumber != null && lastToPageNumber > mbHeader.getFromPageNo()
                    && mbHeaders.get(mbHeaders.size() - 1).getId() != mbHeader.getId()) {
                jsonObject.addProperty("errorLastToPageNumber", messageSource.getMessage("error.from.last.to.page",
                        new String[] { },
                        null));
                errors.reject("errorLastToPageNumber", messageSource.getMessage("error.from.last.to.page",
                        new String[] { },
                        null));
            }
        }
        
        final OfflineStatus offlineStatus = offlineStatusService.getOfflineStatusByObjectIdAndObjectTypeAndStatus(
                mbHeader.getWorkOrder().getId(), WorksConstants.WORKORDER,
                OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
        if (offlineStatus != null) {
            if (offlineStatus.getStatusDate().after(mbHeader.getMbDate())) {
                jsonObject.addProperty("errorEntryCommencedDate", messageSource.getMessage("error.mb.entry.date.commenced.date",
                        new String[] { },
                        null));
                errors.reject("errorEntryCommencedDate", messageSource.getMessage("error.mb.entry.date.commenced.date",
                        new String[] { },
                        null));
            }
            if(mbHeader.getMbIssuedDate() != null && offlineStatus.getStatusDate().after(mbHeader.getMbIssuedDate()))
                if (offlineStatus.getStatusDate().after(mbHeader.getMbDate())) {
                    jsonObject.addProperty("errorIssuedCommencedDate", messageSource.getMessage("error.mb.issued.date.commenced.date",
                            new String[] { },
                            null));
                    errors.reject("errorIssuedCommencedDate", messageSource.getMessage("error.mb.issued.date.commenced.date",
                            new String[] { },
                            null));
                }
        }
        
        if (mbHeader.getMbIssuedDate() != null && mbHeader.getMbIssuedDate().after(mbHeader.getMbDate())) {
            jsonObject.addProperty("errorEntryIssuedDate", messageSource.getMessage("error.mb.issued.date.entry.date",
                    new String[] { },
                    null));
            errors.reject("errorEntryIssuedDate", messageSource.getMessage("error.mb.issued.date.entry.date",
                    new String[] { },
                    null));
        }
        if (mbHeader.getFromPageNo() > mbHeader.getToPageNo()) {
            jsonObject.addProperty("errorFromToPageNumber", messageSource.getMessage("error.from.to.page",
                    new String[] { },
                    null));
            errors.reject("errorFromToPageNumber", messageSource.getMessage("error.from.to.page",
                    new String[] { },
                    null));
        }
    }
    
    public void validateMBDetails(final MBHeader mbHeader, final JsonObject jsonObject, final BindingResult errors) {
        if (mbHeader.getSorMbDetails().isEmpty() && mbHeader.getNonSorMbDetails().isEmpty()) {
            jsonObject.addProperty("errorSorNonSorMandatory", messageSource.getMessage("error.mb.sor.nonsor.required",
                    new String[] { },
                    null));
            errors.reject("errorSorNonSorMandatory", messageSource.getMessage("error.mb.sor.nonsor.required",
                    new String[] { },
                    null));
        }
        
        for (final MBDetails details : mbHeader.getSorMbDetails()) {
            validateMBDetail(details, jsonObject, errors);
        }
        
        for (final MBDetails details : mbHeader.getNonSorMbDetails()) {
            validateMBDetail(details, jsonObject, errors);
        }
    }
    
    private void validateMBDetail(final MBDetails details, final JsonObject jsonObject, final BindingResult errors) {
        Double prevCumulativeQuantity = getPreviousCumulativeQuantity(details.getMbHeader().getId(),
                details.getWorkOrderActivity().getId());
        if (prevCumulativeQuantity == null)
            prevCumulativeQuantity = 0D;
        if (details.getWorkOrderActivity().getApprovedQuantity() < (details.getQuantity() + prevCumulativeQuantity)) {
            jsonObject.addProperty("errorApprovedCumulativeQuantity", messageSource.getMessage("error.approved.quantity.cumulative",
                    new String[] { },
                    null));
            errors.reject("errorApprovedCumulativeQuantity", messageSource.getMessage("error.approved.quantity.cumulative",
                    new String[] { },
                    null));
        }
    }

    public Double getTotalMBAmountOfMBs(Long mbHeaderId, Long workOrderId, Long workOrderEstimateId, String statusCode) {
        return mbHeaderRepository.getTotalMBAmountOfMBs(mbHeaderId, workOrderId, workOrderEstimateId, statusCode);
    }
}