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
package org.egov.works.contractorbill.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.script.ScriptContext;
import javax.servlet.http.HttpServletRequest;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregistermis;
import org.egov.pims.commons.Position;
import org.egov.services.voucher.VoucherService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.SearchRequestContractorBill;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.repository.ContractorBillRegisterRepository;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ContractorBillRegisterService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractorBillRegisterService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final ContractorBillRegisterRepository contractorBillRegisterRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<ContractorBillRegister> contractorBillRegisterWorkflowService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private LineEstimateService lineEstimateService;

    private final ScriptService scriptExecutionService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private TrackMilestoneService trackMilestoneService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public ContractorBillRegisterService(final ContractorBillRegisterRepository contractorBillRegisterRepository,
            final ScriptService scriptExecutionService) {
        this.contractorBillRegisterRepository = contractorBillRegisterRepository;
        this.scriptExecutionService = scriptExecutionService;
    }

    public ContractorBillRegister getContractorBillById(final Long id) {
        return contractorBillRegisterRepository.findOne(id);
    }

    public Integer getMaxSequenceNumberByWorkOrder(final WorkOrderEstimate workOrderEstimate) {
        return contractorBillRegisterRepository.findMaxBillSequenceNumberByWorkOrder(
                workOrderEstimate.getEstimate().getLineEstimateDetails().getProjectCode().getCode());
    }

    public ContractorBillRegister getContractorBillByBillNumber(final String billNumber) {
        return contractorBillRegisterRepository.findByBillnumber(billNumber);
    }

    @Transactional
    public ContractorBillRegister create(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails, final MultipartFile[] files,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction)
            throws IOException {

        contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                ContractorBillRegister.BillStatus.CREATED.toString()));
        contractorBillRegister.setBillstatus(contractorBillRegister.getStatus().getCode());
        contractorBillRegister.setExpendituretype(WorksConstants.BILL_EXPENDITURE_TYPE);
        final EgBillregistermis egBillRegisterMis = setEgBillRegisterMis(contractorBillRegister, lineEstimateDetails);
        contractorBillRegister.setEgBillregistermis(egBillRegisterMis);

        try {
            checkBudgetAndGenerateBANumber(contractorBillRegister);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        }
        ContractorBillRegister savedContractorBillRegister = contractorBillRegisterRepository.save(contractorBillRegister);

        createContractorBillRegisterWorkflowTransition(savedContractorBillRegister,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        savedContractorBillRegister = contractorBillRegisterRepository.save(contractorBillRegister);

        populateAndSaveMBHeader(savedContractorBillRegister);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, savedContractorBillRegister,
                WorksConstants.CONTRACTORBILL);
        if (!documentDetails.isEmpty()) {
            savedContractorBillRegister.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return savedContractorBillRegister;
    }

    @Transactional
    public ContractorBillRegister updateContractorBillRegister(
            final ContractorBillRegister contractorBillRegister,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String mode,
            final MultipartFile[] files) throws ValidationException, IOException {
        ContractorBillRegister updatedContractorBillRegister = null;

        if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.REJECTED.toString())) {
            if (workFlowAction.equalsIgnoreCase(WorksConstants.FORWARD_ACTION.toString()))
                populateAndSaveMBHeader(contractorBillRegister);
            else if (workFlowAction.equalsIgnoreCase(WorksConstants.CANCEL_ACTION))
                cancelMBHeader(contractorBillRegister);
            updatedContractorBillRegister = update(contractorBillRegister, files);
            contractorBillRegisterStatusChange(updatedContractorBillRegister, workFlowAction, mode);
        } else {
            contractorBillRegisterStatusChange(contractorBillRegister, workFlowAction, mode);

            if (workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE)) {
                contractorBillRegister.setApprovedDate(new Date());
                contractorBillRegister.setApprovedBy(securityUtils.getCurrentUser());
                contractorBillRegister.getEgBillregistermis().setSourcePath(
                        "/egworks/contractorbill/view/" + contractorBillRegister.getId());
                approveMBHeader(contractorBillRegister);
            }
        }
        updatedContractorBillRegister = contractorBillRegisterRepository.save(contractorBillRegister);
        updatedContractorBillRegister.setBillstatus(updatedContractorBillRegister.getStatus().getCode());

        createContractorBillRegisterWorkflowTransition(updatedContractorBillRegister,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        updatedContractorBillRegister = contractorBillRegisterRepository.save(contractorBillRegister);

        return updatedContractorBillRegister;
    }

    private ContractorBillRegister update(final ContractorBillRegister contractorBillRegister,
            final MultipartFile[] files) throws IOException {
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, contractorBillRegister,
                WorksConstants.CONTRACTORBILL);
        if (!documentDetails.isEmpty()) {
            contractorBillRegister.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return contractorBillRegisterRepository.save(contractorBillRegister);
    }

    public Set<EgBilldetails> removeDeletedBillDetails(final Set<EgBilldetails> set,
            final String removedBillDetailsIds) {
        final Set<EgBilldetails> details = new HashSet<EgBilldetails>();
        if (null != removedBillDetailsIds) {
            final String[] ids = removedBillDetailsIds.split(",");
            final List<String> strList = new ArrayList<String>();
            for (final String str : ids)
                strList.add(str);
            for (final EgBilldetails line : set)
                if (line.getId() != null) {
                    if (!strList.contains(line.getId().toString()))
                        details.add(line);
                } else
                    details.add(line);
        } else
            return set;
        return details;
    }

    private EgBillregistermis setEgBillRegisterMis(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails) {
        final EgBillregistermis egBillRegisterMis = contractorBillRegister.getEgBillregistermis();
        egBillRegisterMis.setEgBillregister(contractorBillRegister);
        egBillRegisterMis.setPayto(contractorBillRegister.getWorkOrder().getContractor().getName());
        egBillRegisterMis.setFieldid(lineEstimateDetails.getLineEstimate().getWard());

        if (lineEstimateDetails.getLineEstimate().getFund() != null)
            egBillRegisterMis.setFund(lineEstimateDetails.getLineEstimate().getFund());
        if (lineEstimateDetails.getLineEstimate().getFunction() != null)
            egBillRegisterMis.setFunction(lineEstimateDetails.getLineEstimate().getFunction());
        if (lineEstimateDetails.getLineEstimate().getScheme() != null)
            egBillRegisterMis.setScheme(lineEstimateDetails.getLineEstimate().getScheme());
        if (lineEstimateDetails.getLineEstimate().getSubScheme() != null)
            egBillRegisterMis.setSubScheme(lineEstimateDetails.getLineEstimate().getSubScheme());

        egBillRegisterMis.setEgDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        final CFinancialYear financialYear = financialYearHibernateDAO
                .getFinancialYearByDate(contractorBillRegister.getBilldate());
        egBillRegisterMis.setFinancialyear(financialYear);
        egBillRegisterMis.setLastupdatedtime(new Date());
        return egBillRegisterMis;
    }

    public Long getApprovalPositionByMatrixDesignation(final ContractorBillRegister contractorBillRegister,
            Long approvalPosition, final String additionalRule, final String mode, final String workFlowAction) {
        final WorkFlowMatrix wfmatrix = contractorBillRegisterWorkflowService.getWfMatrix(contractorBillRegister
                .getStateType(), null, null, additionalRule, contractorBillRegister.getCurrentState().getValue(), null);
        if (contractorBillRegister.getStatus() != null && contractorBillRegister.getStatus().getCode() != null)
            if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.CREATED.toString())
                    && contractorBillRegister.getState() != null && !contractorBillRegister.getState().getHistory().isEmpty())
                if (mode.equals("edit") || workFlowAction.equals(WorksConstants.REJECT_ACTION))
                    approvalPosition = contractorBillRegister.getState().getOwnerPosition().getId();
                else
                    approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            contractorBillRegister.getState(), contractorBillRegister.getCreatedBy().getId());
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED))
            approvalPosition = null;

        return approvalPosition;
    }

    public void createContractorBillRegisterWorkflowTransition(final ContractorBillRegister contractorBillRegister,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_CBR_DISPLAYNAME;

        if (null != contractorBillRegister.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(contractorBillRegister.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            final String stateValue = WorksConstants.WF_STATE_REJECTED;
            contractorBillRegister.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition())
                    .withNextAction("")
                    .withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0))
                    && !WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == contractorBillRegister.getState()) {
                wfmatrix = contractorBillRegisterWorkflowService.getWfMatrix(contractorBillRegister.getStateType(), null,
                        null, additionalRule, currState, null);
                contractorBillRegister.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = contractorBillRegisterWorkflowService.getWfMatrix(contractorBillRegister.getStateType(), null,
                        null, additionalRule, contractorBillRegister.getCurrentState().getValue(), null);
                contractorBillRegister.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction("")
                        .withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = contractorBillRegisterWorkflowService.getWfMatrix(contractorBillRegister.getStateType(), null,
                        null, additionalRule, contractorBillRegister.getCurrentState().getValue(), null);
                contractorBillRegister.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public void contractorBillRegisterStatusChange(final ContractorBillRegister contractorBillRegister,
            final String workFlowAction,
            final String mode) throws ValidationException {
        if (null != contractorBillRegister && null != contractorBillRegister.getStatus()
                && null != contractorBillRegister.getStatus().getCode())
            if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.CREATED.toString())
                    && contractorBillRegister.getState() != null
                    && workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE))
                contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                        ContractorBillRegister.BillStatus.APPROVED.toString()));
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                        ContractorBillRegister.BillStatus.REJECTED.toString()));
            else if (contractorBillRegister.getStatus().getCode()
                    .equals(ContractorBillRegister.BillStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                        ContractorBillRegister.BillStatus.CANCELLED.toString()));
            else if (contractorBillRegister.getStatus().getCode()
                    .equals(ContractorBillRegister.BillStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                        ContractorBillRegister.BillStatus.CREATED.toString()));

    }

    public List<ContractorBillRegister> searchContractorBill(final SearchRequestContractorBill searchRequestContractorBill) {
        // TODO Need TO handle in single query
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ContractorBillRegister.class)
                .createAlias("workOrder", "cbrwo")
                .createAlias("cbrwo.contractor", "cbrwocont");

        if (searchRequestContractorBill != null) {
            if (searchRequestContractorBill.getBillFromDate() != null)
                criteria.add(Restrictions.ge("billdate", searchRequestContractorBill.getBillFromDate()));
            if (searchRequestContractorBill.getBillToDate() != null)
                criteria.add(Restrictions.le("billdate", searchRequestContractorBill.getBillToDate()));
            if (searchRequestContractorBill.getBillType() != null)
                criteria.add(Restrictions.eq("billtype", searchRequestContractorBill.getBillType()));
            if (searchRequestContractorBill.getBillNumber() != null)
                criteria.add(Restrictions.ilike("billnumber", searchRequestContractorBill.getBillNumber(), MatchMode.ANYWHERE));
            if (searchRequestContractorBill.getStatus() != null)
                criteria.add(Restrictions.eq("billstatus", searchRequestContractorBill.getStatus()));
            if (searchRequestContractorBill.getWorkIdentificationNumber() != null) {
                final List<String> estimateNumbersforWIN = lineEstimateService
                        .getEstimateNumbersForWorkIdentificationNumber(searchRequestContractorBill.getWorkIdentificationNumber());
                if (estimateNumbersforWIN.isEmpty())
                    estimateNumbersforWIN.add("");
                criteria.add(Restrictions.in("cbrwo.estimateNumber", estimateNumbersforWIN));
            }
            if (searchRequestContractorBill.getContractorName() != null)
                criteria.add(Restrictions.eq("cbrwocont.name", searchRequestContractorBill.getContractorName()).ignoreCase());
            if (searchRequestContractorBill.getDepartment() != null) {
                final List<String> estimateNumbers = lineEstimateService
                        .getEstimateNumberForDepartment(searchRequestContractorBill.getDepartment());
                if (estimateNumbers.isEmpty())
                    estimateNumbers.add("");
                criteria.add(Restrictions.in("cbrwo.estimateNumber", estimateNumbers));
            }
            if (searchRequestContractorBill.isSpillOverFlag()) {
                final List<String> estimateNumbersforSpillOverFlag = lineEstimateService
                        .getEstimateNumbersForSpillOverFlag(searchRequestContractorBill.isSpillOverFlag());
                criteria.add(Restrictions.in("cbrwo.estimateNumber", estimateNumbersforSpillOverFlag));
            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findWorkIdentificationNumbersToSearchContractorBill(final String code) {
        final List<String> workIdNumbers = contractorBillRegisterRepository
                .findWorkIdentificationNumberToSearchContractorBill("%" + code + "%");
        return workIdNumbers;
    }

    public List<String> getApprovedContractorsForCreateContractorBill(final String contractorname) {
        final List<String> results = contractorBillRegisterRepository.findContractorForContractorBill("%" + contractorname + "%",
                WorksConstants.APPROVED);
        return results;
    }

    public BigDecimal getTotalBillAmountByWorkOrder(final WorkOrder workOrder) {
        return contractorBillRegisterRepository.findSumOfBillAmountByWorkOrderAndStatus(workOrder,
                ContractorBillRegister.BillStatus.CANCELLED.toString());
    }

    public ContractorBillRegister checkBudgetAndGenerateBANumber(final ContractorBillRegister contractorBill) {
        final ScriptContext scriptContext = ScriptService.createContext("voucherService", voucherService, "bill",
                contractorBill);
        scriptExecutionService.executeScript("egf.bill.budgetcheck", scriptContext);
        return contractorBill;
    }

    private void populateAndSaveMBHeader(final ContractorBillRegister contractorBillRegister) {
        final MBHeader mbHeader = contractorBillRegister.getMbHeader();
        MBHeader existingMBHeader = null;
        if (contractorBillRegister.getMbHeader() != null && contractorBillRegister.getMbHeader().getId() != null)
            existingMBHeader = mbHeaderService.getMBHeaderById(contractorBillRegister.getMbHeader().getId());
        if (existingMBHeader != null) {
            mbHeader.setCreatedBy(existingMBHeader.getCreatedBy());
            mbHeader.setCreatedDate(existingMBHeader.getCreatedDate());
        }
        mbHeader.setMbAmount(contractorBillRegister.getBillamount());
        mbHeader.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                MBHeader.MeasurementBookStatus.CREATED.toString()));
        mbHeader.setEgBillregister(contractorBillRegister);
        mbHeader.setWorkOrderEstimate(contractorBillRegister.getWorkOrderEstimate());
        mbHeaderService.create(mbHeader);
    }

    private void approveMBHeader(final ContractorBillRegister contractorBillRegister) {
        final MBHeader mbHeader = mbHeaderService.getMBHeaderById(contractorBillRegister.getMbHeader().getId());
        mbHeader.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                MBHeader.MeasurementBookStatus.APPROVED.toString()));
        mbHeaderService.create(mbHeader);
    }

    private void cancelMBHeader(final ContractorBillRegister contractorBillRegister) {
        final MBHeader mbHeader = mbHeaderService.getMBHeaderById(contractorBillRegister.getMbHeader().getId());
        mbHeaderService.cancel(mbHeader);
    }

    public BigDecimal getTotalBillAmountByWorkOrderAndNotContractorBillRegister(final WorkOrder workOrder, final Long id) {
        return contractorBillRegisterRepository.findSumOfBillAmountByWorkOrderAndStatusAndNotContractorBillRegister(workOrder,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), id);
    }

    public List<ContractorBillRegister> searchContractorBillsToCancel(
            final SearchRequestContractorBill searchRequestContractorBill) {
        // TODO Need TO handle in single query
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ContractorBillRegister.class)
                .createAlias("workOrder", "cbrwo")
                .createAlias("cbrwo.contractor", "cbrwocont");

        if (searchRequestContractorBill != null) {
            if (searchRequestContractorBill.getBillNumber() != null)
                criteria.add(Restrictions.ilike("billnumber", searchRequestContractorBill.getBillNumber(), MatchMode.ANYWHERE));
            if (searchRequestContractorBill.getStatus() != null)
                criteria.add(Restrictions.eq("billstatus", searchRequestContractorBill.getStatus()));
            if (searchRequestContractorBill.getWorkIdentificationNumber() != null) {
                final List<String> estimateNumbersforWIN = lineEstimateService
                        .getEstimateNumbersForWorkIdentificationNumber(searchRequestContractorBill.getWorkIdentificationNumber());
                if (estimateNumbersforWIN.isEmpty())
                    estimateNumbersforWIN.add("");
                criteria.add(Restrictions.in("cbrwo.estimateNumber", estimateNumbersforWIN));
            }
            if (searchRequestContractorBill.getDepartment() != null) {
                final List<String> estimateNumbers = lineEstimateService
                        .getEstimateNumberForDepartment(searchRequestContractorBill.getDepartment());
                if (estimateNumbers.isEmpty())
                    estimateNumbers.add("");
                criteria.add(Restrictions.in("cbrwo.estimateNumber", estimateNumbers));
            }
            if (searchRequestContractorBill.getWorkOrderNumber() != null) {
                final List<String> workOrderNumbers = contractorBillRegisterRepository
                        .findWorkOrderNumbersToCancel("%" + searchRequestContractorBill.getWorkOrderNumber() + "%",
                                ContractorBillRegister.BillStatus.APPROVED.toString());
                if (workOrderNumbers.isEmpty())
                    workOrderNumbers.add("");
                criteria.add(Restrictions.in("cbrwo.workOrderNumber", workOrderNumbers));
            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findWorkIdentificationNumbersToSearchContractorBillToCancel(final String code) {
        final List<String> workIdNumbers = contractorBillRegisterRepository
                .findWorkIdentificationNumberToSearchContractorBillToCancel("%" + code + "%",
                        ContractorBillRegister.BillStatus.APPROVED.toString());
        return workIdNumbers;
    }

    public List<String> findBillNumbersToSearchContractorBillToCancel(final String billNumber) {
        final List<String> workIdNumbers = contractorBillRegisterRepository
                .findBillNumberToSearchContractorBillToCancel("%" + billNumber + "%",
                        ContractorBillRegister.BillStatus.APPROVED.toString());
        return workIdNumbers;
    }

    @Transactional
    public ContractorBillRegister cancel(final ContractorBillRegister contractorBillRegister) {
        contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                ContractorBillRegister.BillStatus.CANCELLED.toString()));
        contractorBillRegister.setBillstatus(ContractorBillRegister.BillStatus.CANCELLED.toString());
        final List<MBHeader> mbHeaders = mbHeaderService.getMBHeadersByContractorBill(contractorBillRegister);
        for (final MBHeader mbHeader : mbHeaders)
            mbHeaderService.cancel(mbHeader);
        return contractorBillRegisterRepository.save(contractorBillRegister);
    }

    public EgBillPayeedetails getEgPayeeDetails(final EgBilldetails billDetails, final Integer accountsDetailTypeId,
            final BigDecimal amount, final boolean isDebit, final Integer accountsDetailKeyId) {
        final EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
        egBillPaydetail.setAccountDetailKeyId(accountsDetailKeyId);
        egBillPaydetail.setAccountDetailTypeId(accountsDetailTypeId);
        if (isDebit)
            egBillPaydetail.setDebitAmount(amount);
        else
            egBillPaydetail.setCreditAmount(amount);
        egBillPaydetail.setEgBilldetailsId(billDetails);
        egBillPaydetail.setLastUpdatedTime(new Date());
        return egBillPaydetail;
    }

    public List<Map<String, Object>> getBillDetailsMap(final ContractorBillRegister contractorBillRegister,
            final Model model) {
        final List<Map<String, Object>> billDetailsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> billDetails = new HashMap<String, Object>();

        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        final List<CChartOfAccounts> contractorRefundAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByListOfPurposeName(WorksConstants.CONTRACTOR_REFUND_PURPOSE);
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes()) {
            final CChartOfAccounts coa = chartOfAccountsHibernateDAO
                    .findById(egBilldetails.getGlcodeid().longValue(), false);
            if (egBilldetails.getDebitamount() != null) {
                billDetails = new HashMap<String, Object>();
                billDetails.put("id", egBilldetails.getId());
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getDebitamount());
                billDetails.put("isDebit", true);
                billDetails.put("isNetPayable", false);
                if (!contractorRefundAccountList.isEmpty() && contractorRefundAccountList.contains(coa)) {
                    final String amounts = getTotalDebitAndCreditAmountByAccountCode(
                            contractorBillRegister.getWorkOrderEstimate().getId(), new BigDecimal(coa.getId()),
                            contractorBillRegister.getId() != null ? contractorBillRegister.getId() : -1);
                    final String[] creditDebitAmounts = amounts.split(",");
                    billDetails.put("withHeldAmount", creditDebitAmounts[0]);
                    billDetails.put("RefundedAmount", creditDebitAmounts[1]);
                    billDetails.put("isRefund", true);
                } else
                    billDetails.put("isRefund", false);

            } else if (egBilldetails.getCreditamount() != null) {
                billDetails = new HashMap<String, Object>();
                billDetails.put("id", egBilldetails.getId());
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getCreditamount());
                billDetails.put("isDebit", false);
                if (contractorPayableAccountList != null && !contractorPayableAccountList.isEmpty()
                        && contractorPayableAccountList.contains(coa)) {
                    billDetails.put("isNetPayable", true);
                    model.addAttribute("netPayableAccountId", egBilldetails.getId());
                    model.addAttribute("netPayableAccountCode", coa.getId());
                    model.addAttribute("netPayableAmount", egBilldetails.getCreditamount());
                } else
                    billDetails.put("isNetPayable", false);

            }
            billDetailsList.add(billDetails);
        }
        return billDetailsList;
    }

    public boolean checkForDuplicateAccountCodes(final ContractorBillRegister contractorBillRegister) {
        final Set<Long> glCodeIdSet = new HashSet<Long>();
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes())
            if (egBilldetails.getGlcodeid() != null && !contractorBillRegister.getRefundBillDetails().contains(egBilldetails)) {
                if (glCodeIdSet.contains(egBilldetails.getGlcodeid().longValue()))
                    return false;
                glCodeIdSet.add(egBilldetails.getGlcodeid().longValue());
            }
        return true;
    }

    public boolean validateDuplicateRefundAccountCodes(final ContractorBillRegister contractorBillRegister) {
        final Set<Long> glCodeIdSet = new HashSet<Long>();
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes())
            if (egBilldetails.getGlcodeid() != null && egBilldetails.getDebitamount() != null) {
                if (glCodeIdSet.contains(egBilldetails.getGlcodeid().longValue()))
                    return false;
                glCodeIdSet.add(egBilldetails.getGlcodeid().longValue());
            }
        return true;
    }

    public void validateTotalDebitAndCreditAmount(final ContractorBillRegister contractorBillRegister,
            final BindingResult resultBinder) {
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes()) {
            if (egBilldetails.getDebitamount() != null
                    && !(BigDecimal.ZERO.compareTo(egBilldetails.getDebitamount()) == 0))
                totalDebitAmount = totalDebitAmount.add(egBilldetails.getDebitamount());
            if (egBilldetails.getCreditamount() != null
                    && !(BigDecimal.ZERO.compareTo(egBilldetails.getCreditamount()) == 0))
                totalCreditAmount = totalCreditAmount.add(egBilldetails.getCreditamount());
        }
        if (!(totalDebitAmount.compareTo(totalCreditAmount) == 0))
            resultBinder.reject("error.total.debitamount.creditamount.notequal",
                    "error.total.debitamount.creditamount.notequal");
    }

    public void validateRefundAmount(final ContractorBillRegister contractorBillRegister, final BindingResult resultBinder) {
        int index = 0;
        for (final EgBilldetails egBillDetail : contractorBillRegister.getRefundBillDetails()) {
            if (egBillDetail.getGlcodeid() != null && egBillDetail.getDebitamount() == null)
                resultBinder.rejectValue("refundBillDetails[" + index + "].debitamount", "error.refundamount.required");
            if (egBillDetail.getDebitamount() != null && egBillDetail.getGlcodeid() == null)
                resultBinder.rejectValue("refundBillDetails[" + index + "].glcodeid", "error.refundaccountcode.required");
            if (egBillDetail.getGlcodeid() != null && egBillDetail.getDebitamount() != null) {
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO
                        .findById(egBillDetail.getGlcodeid().longValue(), false);
                final String amounts = getTotalDebitAndCreditAmountByAccountCode(
                        contractorBillRegister.getWorkOrderEstimate().getId(), new BigDecimal(coa.getId()),
                        contractorBillRegister.getId() != null ? contractorBillRegister.getId() : -1);
                if (!org.apache.commons.lang.StringUtils.isBlank(amounts)) {
                    final String[] creditDebitAmounts = amounts.split(",");
                    BigDecimal withheldAmount = BigDecimal.ZERO;
                    BigDecimal refundedAmount = BigDecimal.ZERO;
                    if (!creditDebitAmounts[0].equals("0"))
                        withheldAmount = new BigDecimal(creditDebitAmounts[0]);
                    if (!creditDebitAmounts[1].equals("0"))
                        refundedAmount = new BigDecimal(creditDebitAmounts[1]);

                    if (withheldAmount.equals("0"))
                        resultBinder.reject("error.contractorBill.nowithheldtorefund",
                                new String[] { coa.getGlcode() }, null);
                    else {

                        final BigDecimal validRefundAmount = egBillDetail.getDebitamount().add(refundedAmount);
                        final BigDecimal diffAmount = validRefundAmount.subtract(withheldAmount);
                        if (validRefundAmount.compareTo(new BigDecimal(creditDebitAmounts[0])) == 1
                                && !contractorBillRegister.getWorkOrderEstimate().getEstimate().getLineEstimateDetails()
                                        .getLineEstimate().isSpillOverFlag())
                            resultBinder.reject("error.contractorBill.validate.refundAmount",
                                    new String[] { coa.getGlcode(), diffAmount.toString() }, null);
                    }
                }
            }
            index++;
        }

    }

    public EgBilldetails getBillDetails(final ContractorBillRegister billregister, final EgBilldetails egBilldetails,
            final LineEstimateDetails lineEstimateDetails, final BindingResult resultBinder,
            final HttpServletRequest request) {
        egBilldetails.setFunctionid(new BigDecimal(lineEstimateDetails.getLineEstimate().getFunction().getId()));
        boolean isDebit = false;
        CChartOfAccounts coa = null;
        if (!(BigDecimal.ZERO.compareTo(egBilldetails.getGlcodeid()) == 0))
            coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid().longValue(), false);
        if (coa != null && coa.getId() != null)
            egBilldetails.setGlcodeid(BigDecimal.valueOf(coa.getId()));
        if (egBilldetails.getDebitamount() != null
                && !(BigDecimal.ZERO.compareTo(egBilldetails.getDebitamount()) == 0)) {
            egBilldetails.setDebitamount(egBilldetails.getDebitamount());
            isDebit = true;
        } else if (egBilldetails.getCreditamount() != null
                && !(BigDecimal.ZERO.compareTo(egBilldetails.getCreditamount()) == 0))
            egBilldetails.setCreditamount(egBilldetails.getCreditamount());
        else if (!org.apache.commons.lang.StringUtils.isBlank(request.getParameter("netPayableAccountCode"))
                && request.getParameter("netPayableAccountCode").toString().equals(egBilldetails.getGlcodeid()))
            resultBinder.reject("error.contractorbill.accountdetails.amount.required",
                    "error.contractorbill.accountdetails.amount.required");

        egBilldetails.setEgBillregister(billregister);
        final List<CChartOfAccounts> contractorRefundAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByListOfPurposeName(WorksConstants.CONTRACTOR_REFUND_PURPOSE);
        if (coa != null && coa.getGlcode() != null) {
            Accountdetailtype projectCodeAccountDetailType = null;
            Accountdetailtype contractorAccountDetailType = null;
            if (isDebit && !contractorRefundAccountList.isEmpty() && !contractorRefundAccountList.contains(coa)) {
                projectCodeAccountDetailType = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(coa.getGlcode(),
                        WorksConstants.PROJECTCODE);
                if (projectCodeAccountDetailType == null)
                    resultBinder.reject("error.contractorBill.validate.glcode.for.projectcode.subledger",
                            new String[] { coa.getGlcode() }, null);
            }
            final List<Accountdetailtype> detailCode = chartOfAccountsHibernateDAO
                    .getAccountdetailtypeListByGLCode(coa.getGlcode());
            if (detailCode != null && !detailCode.isEmpty()) {
                if (isDebit && !contractorRefundAccountList.isEmpty() && !contractorRefundAccountList.contains(coa)) {
                    if (projectCodeAccountDetailType != null)
                        egBilldetails.addEgBillPayeedetail(getEgPayeeDetails(egBilldetails,
                                projectCodeAccountDetailType.getId(), egBilldetails.getDebitamount(), isDebit,
                                Integer.valueOf(lineEstimateDetails.getProjectCode().getId().toString())));
                } else {
                    contractorAccountDetailType = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(
                            coa.getGlcode(), WorksConstants.ACCOUNTDETAIL_TYPE_CONTRACTOR);
                    if (contractorAccountDetailType != null)
                        egBilldetails.getEgBillPaydetailes().add(getEgPayeeDetails(egBilldetails,
                                contractorAccountDetailType.getId(),
                                isDebit ? egBilldetails.getDebitamount() : egBilldetails.getCreditamount(), isDebit,
                                Integer.valueOf(billregister.getWorkOrder().getContractor().getId().toString())));

                }

                if (projectCodeAccountDetailType == null && contractorAccountDetailType == null)
                    resultBinder.reject("error.contractorbill.validate.glcode.for.subledger",
                            new String[] { coa.getGlcode() }, null);
            }
        }
        egBilldetails.setLastupdatedtime(new Date());
        return egBilldetails;
    }

    public String getTotalDebitAndCreditAmountByAccountCode(final Long workOrderEstimateId, final BigDecimal glCodeId,
            final Long contractorBillId) {
        return contractorBillRegisterRepository.findSumOfDebitByAccountCodeForWorkOrder(workOrderEstimateId, glCodeId,
                ContractorBillRegister.BillStatus.APPROVED.toString(), contractorBillId);
    }

    public void validateMileStonePercentage(final ContractorBillRegister contractorBillRegister,
            final BindingResult resultBinder) {
        TrackMilestone trackMileStone = null;
        if (contractorBillRegister.getBilltype().equalsIgnoreCase(BillTypes.Final_Bill.toString())) {
            trackMileStone = trackMilestoneService
                    .getCompletionPercentageToCreateContractorFinalBill(contractorBillRegister.getWorkOrderEstimate().getId());
            if (trackMileStone == null)
                resultBinder.reject("error.contractor.finalbill.milestonepercentage",
                        "error.contractor.finalbill.milestonepercentage");
        } else {
            trackMileStone = trackMilestoneService
                    .getMinimumPercentageToCreateContractorBill(contractorBillRegister.getWorkOrderEstimate().getId());
            if (trackMileStone == null)
                resultBinder.reject("error.contractorbil.milestone.percentage",
                        "error.contractorbil.milestone.percentage");
        }
    }

    public void validateZeroCreditAndDebitAmount(final ContractorBillRegister contractorBillRegister,
            final BindingResult resultBinder) {
        for (final EgBilldetails egBillDetail : contractorBillRegister.getEgBilldetailes())
            if (egBillDetail.getCreditamount() != null && BigDecimal.ZERO.compareTo(egBillDetail.getCreditamount()) == 0 ||
                    egBillDetail.getDebitamount() != null && BigDecimal.ZERO.compareTo(egBillDetail.getDebitamount()) == 0) {
                resultBinder.reject("error.creditordebitamount.zero",
                        "error.creditordebitamount.zero");
                break;
            }

    }

}
