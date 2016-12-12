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
package org.egov.works.contractoradvance.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.advance.EgAdvanceRequisitionMis;
import org.egov.pims.commons.Position;
import org.egov.works.autonumber.AdvanceRequisitionNumberGenerator;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus;
import org.egov.works.contractoradvance.entity.SearchRequestContractorRequisition;
import org.egov.works.contractoradvance.repository.ContractorAdvanceRepository;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

@Service
@Transactional(readOnly = true)
public class ContractorAdvanceService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractorAdvanceService.class);

    @Autowired
    private ContractorAdvanceRepository contractorAdvanceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private AdvanceRequisitionNumberGenerator advanceRequisitionNumberGenerator;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<ContractorAdvanceRequisition> contractorAdvanceRequisitionWorkflowService;

    public ContractorAdvanceRequisition getContractorAdvanceRequisitionById(final Long id) {
        return contractorAdvanceRepository.findOne(id);
    }

    public List<String> getAdvanceRequisitionNumberToSearchCR(final String advanceRequisitionNumber) {
        final List<String> advanceRequisitionNumbers = contractorAdvanceRepository
                .findAdvanceRequisitionNumberToSearchCR("%" + advanceRequisitionNumber + "%");
        return advanceRequisitionNumbers;
    }

    public List<String> getWorkOrderNumberToSearchCR(final String workOrderNumber) {
        final List<String> workOrderNumbers = contractorAdvanceRepository
                .findWorkOrderNumberToSearchCR("%" + workOrderNumber + "%");
        return workOrderNumbers;
    }

    public List<String> getContractorsToSearchCR(final String contractorName) {
        final List<String> contractorNames = contractorAdvanceRepository
                .findContractorsToSearchCR("%" + contractorName + "%");
        return contractorNames;
    }

    public List<ContractorAdvanceRequisition> searchContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClause(searchRequestContractorRequisition, queryStr);
        final Query query = setParameterForSearchContractorAdvance(searchRequestContractorRequisition, queryStr);
        final List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = query.getResultList();
        return contractorAdvanceRequisitionList;
    }

    private void buildWhereClause(final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final StringBuilder queryStr) {

        queryStr.append(
                "select car from ContractorAdvanceRequisition as car where 1=1 ");

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceRequisitionNumber()))
            queryStr.append(
                    " and upper(car.advanceRequisitionNumber) = :advanceRequisitionNumber");

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getWorkOrderNumber()))
            queryStr.append(" and upper(car.workOrderEstimate.workOrder.workOrderNumber) = :workOrderNumber");

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getContractorName()))
            queryStr.append(" and upper(car.workOrderEstimate.workOrder.contractor.name) = :contractorName");

        if (searchRequestContractorRequisition.getFromDate() != null)
            queryStr.append(
                    " and car.advanceRequisitionDate >= :fromDate");

        if (searchRequestContractorRequisition.getToDate() != null)
            queryStr.append(
                    " and car.advanceRequisitionDate <= :toDate");

        if (searchRequestContractorRequisition.getEgwStatus() != null)
            queryStr.append(
                    " and car.status.code = :status)");

    }

    private Query setParameterForSearchContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        if (searchRequestContractorRequisition != null) {
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getWorkOrderNumber()))
                qry.setParameter("workOrderNumber",
                        searchRequestContractorRequisition.getWorkOrderNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceRequisitionNumber()))
                qry.setParameter("advanceRequisitionNumber",
                        searchRequestContractorRequisition.getAdvanceRequisitionNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getContractorName()))
                qry.setParameter("contractorName", searchRequestContractorRequisition.getContractorName().toUpperCase());
            if (searchRequestContractorRequisition.getEgwStatus() != null)
                qry.setParameter("status", searchRequestContractorRequisition.getEgwStatus());
            if (searchRequestContractorRequisition.getFromDate() != null)
                qry.setParameter("fromDate", searchRequestContractorRequisition.getFromDate());
            if (searchRequestContractorRequisition.getToDate() != null)
                qry.setParameter("toDate", searchRequestContractorRequisition.getToDate());

        }
        return qry;
    }

    public ContractorAdvanceRequisition getContractorAdvanceByARFNumber(final String arfNumber) {
        return contractorAdvanceRepository.findByAdvanceRequisitionNumber(arfNumber);
    }

    @Transactional
    public ContractorAdvanceRequisition create(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final MultipartFile[] files,
            final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction) throws IOException {

        contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.CONTRACTOR_ADVANCE,
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CREATED.toString()));
        contractorAdvanceRequisition.setAdvanceRequisitionDate(new Date());
        contractorAdvanceRequisition
                .setAdvanceRequisitionNumber(advanceRequisitionNumberGenerator.getNextNumber(contractorAdvanceRequisition));
        contractorAdvanceRequisition.setArftype(WorksConstants.STATUS_MODULE_NAME);
        for (final EgAdvanceRequisitionDetails details : contractorAdvanceRequisition.getEgAdvanceReqDetailses())
            details.setEgAdvanceRequisition(contractorAdvanceRequisition);

        final EgAdvanceRequisitionMis requisitionMis = new EgAdvanceRequisitionMis();
        requisitionMis.setEgAdvanceRequisition(contractorAdvanceRequisition);
        requisitionMis
                .setEgDepartment(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getExecutingDepartment());
        requisitionMis.setFundsource(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFundSource());
        if (contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getLineEstimateDetails() != null) {
            requisitionMis.setFunction(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getLineEstimateDetails()
                    .getLineEstimate().getFunction());
            requisitionMis.setFund(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getLineEstimateDetails()
                    .getLineEstimate().getFund());
            requisitionMis.setScheme(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getLineEstimateDetails()
                    .getLineEstimate().getScheme());
            requisitionMis.setSubScheme(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getLineEstimateDetails()
                    .getLineEstimate().getSubScheme());
        }
        contractorAdvanceRequisition.setEgAdvanceReqMises(requisitionMis);

        ContractorAdvanceRequisition savedContractorAdvanceRequisition = contractorAdvanceRepository
                .save(contractorAdvanceRequisition);

        createContractorAdvanceWorkflowTransition(savedContractorAdvanceRequisition, approvalPosition, approvalComent,
                additionalRule, workFlowAction);

        savedContractorAdvanceRequisition = contractorAdvanceRepository
                .save(savedContractorAdvanceRequisition);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, savedContractorAdvanceRequisition,
                WorksConstants.CONTRACTOR_ADVANCE);
        if (!documentDetails.isEmpty()) {
            savedContractorAdvanceRequisition.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return savedContractorAdvanceRequisition;
    }

    public void createContractorAdvanceWorkflowTransition(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_ARF_DISPLAYNAME;

        if (null != contractorAdvanceRequisition.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(contractorAdvanceRequisition.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            final String stateValue = WorksConstants.WF_STATE_REJECTED;
            contractorAdvanceRequisition.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("").withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == contractorAdvanceRequisition.getState()) {
                wfmatrix = contractorAdvanceRequisitionWorkflowService.getWfMatrix(contractorAdvanceRequisition.getStateType(),
                        null, null, additionalRule, currState, null);
                contractorAdvanceRequisition.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = contractorAdvanceRequisitionWorkflowService.getWfMatrix(contractorAdvanceRequisition.getStateType(),
                        null, null, additionalRule, contractorAdvanceRequisition.getCurrentState().getValue(), null);
                contractorAdvanceRequisition.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("").withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = contractorAdvanceRequisitionWorkflowService.getWfMatrix(contractorAdvanceRequisition.getStateType(),
                        null, contractorAdvanceRequisition.getAdvanceRequisitionAmount(), additionalRule,
                        contractorAdvanceRequisition.getCurrentState().getValue(),
                        contractorAdvanceRequisition.getState().getNextAction());
                contractorAdvanceRequisition.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    @Transactional
    public ContractorAdvanceRequisition updateContractorAdvanceRequisition(
            final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String mode, final MultipartFile[] files)
            throws ValidationException, IOException {
        ContractorAdvanceRequisition updatedContractorAdvanceRequisition = null;

        if (contractorAdvanceRequisition.getStatus().getCode()
                .equals(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.REJECTED.toString())) {
            updatedContractorAdvanceRequisition = update(contractorAdvanceRequisition, files);
            contractorContractorAdvanceStatusChange(updatedContractorAdvanceRequisition, workFlowAction, mode);
        } else {
            contractorContractorAdvanceStatusChange(contractorAdvanceRequisition, workFlowAction, mode);
            if (workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE)) {
                contractorAdvanceRequisition.setApprovedDate(new Date());
                contractorAdvanceRequisition.setApprovedBy(securityUtils.getCurrentUser());
            }
        }
        updatedContractorAdvanceRequisition = contractorAdvanceRepository.save(contractorAdvanceRequisition);

        createContractorAdvanceWorkflowTransition(updatedContractorAdvanceRequisition, approvalPosition, approvalComent,
                additionalRule, workFlowAction);

        updatedContractorAdvanceRequisition = contractorAdvanceRepository.save(updatedContractorAdvanceRequisition);

        return updatedContractorAdvanceRequisition;
    }

    private ContractorAdvanceRequisition update(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final MultipartFile[] files) throws IOException {
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, contractorAdvanceRequisition,
                WorksConstants.CONTRACTOR_ADVANCE);
        if (!documentDetails.isEmpty()) {
            contractorAdvanceRequisition.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return contractorAdvanceRepository.save(contractorAdvanceRequisition);
    }

    public void contractorContractorAdvanceStatusChange(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final String workFlowAction, final String mode) throws ValidationException {
        if (null != contractorAdvanceRequisition && null != contractorAdvanceRequisition.getStatus()
                && null != contractorAdvanceRequisition.getStatus().getCode())
            if (workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString()));
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.REJECTED.toString()));
            else if (contractorAdvanceRequisition.getStatus().getCode()
                    .equals(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString()));
            else if (contractorAdvanceRequisition.getStatus().getCode()
                    .equals(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.RESUBMITTED.toString()));
            else if ((WorksConstants.RESUBMITTED_STATUS.equalsIgnoreCase(contractorAdvanceRequisition.getStatus().getCode()) ||
                    WorksConstants.CREATED_STATUS.equalsIgnoreCase(contractorAdvanceRequisition.getStatus().getCode()) ||
                    WorksConstants.CHECKED_STATUS.equalsIgnoreCase(contractorAdvanceRequisition.getStatus().getCode()))
                    && contractorAdvanceRequisition.getState() != null
                    && WorksConstants.SUBMIT_ACTION.equalsIgnoreCase(workFlowAction))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.CONTRACTOR_ADVANCE,
                        WorksConstants.CHECKED_STATUS));
    }

    public Double getTotalAdvancePaid(final Long contractorAdvanceId, final Long workOrderEstimateId, final String approvedCode) {
        return contractorAdvanceRepository.getTotalAdvancePaid(contractorAdvanceId, workOrderEstimateId, approvedCode);
    }

    public void validateInput(final ContractorAdvanceRequisition contractorAdvanceRequisition, final BindingResult resultBinder) {
        Double advancePaidTillNow = getTotalAdvancePaid(
                contractorAdvanceRequisition.getId() == null ? -1L : contractorAdvanceRequisition.getId(),
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString());
        Double totalMBAmountOfMBs = mbHeaderService.getTotalMBAmountOfMBs(null,
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString());
        if (totalMBAmountOfMBs == null)
            totalMBAmountOfMBs = 0D;
        if (advancePaidTillNow == null)
            advancePaidTillNow = 0D;
        if (contractorAdvanceRequisition.getAdvanceRequisitionAmount()
                .add(BigDecimal.valueOf(advancePaidTillNow + totalMBAmountOfMBs)).compareTo(BigDecimal
                        .valueOf(contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount())) > 0)
            resultBinder.reject("error.advance.exceeded", new String[] {},
                    null);
    }

    public void validateARFInDrafts(final Long workOrderEstimateId, final JsonObject jsonObject, final BindingResult errors) {
        final ContractorAdvanceRequisition contractorAdvanceRequisition = contractorAdvanceRepository
                .findByWorkOrderEstimate_IdAndStatus_codeEquals(workOrderEstimateId, WorksConstants.NEW);
        String userName = "";
        if (contractorAdvanceRequisition != null) {
            userName = worksUtils.getApproverName(contractorAdvanceRequisition.getState().getOwnerPosition().getId());
            final String message = messageSource.getMessage("error.arf.newstatus",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(),
                            contractorAdvanceRequisition.getStatus().getDescription(),
                            userName },
                    null);
            jsonObject.addProperty("draftsError", message);
            if (errors != null)
                errors.reject("draftsError", message);
        }
    }

    public void validateARFInWorkFlow(final Long workOrderEstimateId, final JsonObject jsonObject, final BindingResult errors) {
        final ContractorAdvanceRequisition contractorAdvanceRequisition = contractorAdvanceRepository
                .findByWorkOrderEstimateAndStatus(workOrderEstimateId,
                        ContractorAdvanceRequisitionStatus.CANCELLED.toString(),
                        ContractorAdvanceRequisitionStatus.APPROVED.toString(),
                        ContractorAdvanceRequisitionStatus.NEW.toString());
        String userName = "";
        if (contractorAdvanceRequisition != null) {
            userName = worksUtils.getApproverName(contractorAdvanceRequisition.getState().getOwnerPosition().getId());
            final String message = messageSource.getMessage("error.arf.workflow",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(),
                            contractorAdvanceRequisition.getStatus().getDescription(),
                            userName },
                    null);
            jsonObject.addProperty("workFlowError", message);
            if (errors != null)
                errors.reject("workFlowError", message);
        }
    }
}
