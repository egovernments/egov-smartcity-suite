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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.repository.AccountdetailtypeRepository;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.advance.EgAdvanceReqPayeeDetails;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.advance.EgAdvanceRequisitionMis;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.pims.commons.Position;
import org.egov.works.autonumber.AdvanceBillNumberGenerator;
import org.egov.works.autonumber.AdvanceRequisitionNumberGenerator;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus;
import org.egov.works.contractoradvance.entity.SearchRequestContractorRequisition;
import org.egov.works.contractoradvance.repository.ContractorAdvanceRepository;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderEstimate;
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
    private ExpenseBillService expenseBillService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<ContractorAdvanceRequisition> contractorAdvanceRequisitionWorkflowService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private AccountdetailtypeRepository accountdetailtypeRepository;

    @Autowired
    private AdvanceBillNumberGenerator advanceBillNumberGenerator;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

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

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceBillNumber()))
            queryStr.append(
                    " and upper(car.egAdvanceReqMises.egBillregister.billnumber) = :advanceBillNumber)");

    }

    private Query setParameterForSearchContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        if (searchRequestContractorRequisition != null) {
            setSearchParameterForContractorAdvance(searchRequestContractorRequisition, qry);
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceBillNumber()))
                qry.setParameter("advanceBillNumber",
                        searchRequestContractorRequisition.getAdvanceBillNumber().toUpperCase());

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
                ContractorAdvanceRequisitionStatus.CREATED.toString()));
        contractorAdvanceRequisition.setAdvanceRequisitionDate(new Date());
        contractorAdvanceRequisition
                .setAdvanceRequisitionNumber(advanceRequisitionNumberGenerator.getNextNumber(contractorAdvanceRequisition));
        contractorAdvanceRequisition.setArftype(WorksConstants.STATUS_MODULE_NAME);
        for (final EgAdvanceRequisitionDetails details : contractorAdvanceRequisition.getEgAdvanceReqDetailses())
            details.setEgAdvanceRequisition(contractorAdvanceRequisition);
        contractorAdvanceRequisition.setEgAdvanceReqMises(setEgAdvanceReqMis(contractorAdvanceRequisition));

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

    private EgAdvanceRequisitionMis setEgAdvanceReqMis(final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        final EgAdvanceRequisitionMis requisitionMis = new EgAdvanceRequisitionMis();
        requisitionMis.setEgAdvanceRequisition(contractorAdvanceRequisition);
        requisitionMis.setFieldId(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getWard());
        requisitionMis.setPayto(contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getName());
        requisitionMis
                .setEgDepartment(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getExecutingDepartment());
        requisitionMis.setFundsource(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFundSource());
        if (!contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().isEmpty()) {
            requisitionMis.setFunction(
                    contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunction());
            requisitionMis.setFunctionaryId(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate()
                    .getFinancialDetails().get(0).getFunctionary());
            requisitionMis.setFund(
                    contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFund());
            requisitionMis.setScheme(
                    contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getScheme());
            requisitionMis.setSubScheme(contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getFinancialDetails()
                    .get(0).getSubScheme());
        }
        return requisitionMis;
    }

    public void getEgAdvanceRequisitionDetails(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final EgAdvanceRequisitionDetails egAdvanceRequisitionDetails, final BindingResult errors) {
        boolean isDebit = false;
        CChartOfAccounts coa = null;
        if (egAdvanceRequisitionDetails.getChartofaccounts().getId() != 0)
            coa = chartOfAccountsHibernateDAO.findById(egAdvanceRequisitionDetails.getChartofaccounts().getId(), false);
        if (egAdvanceRequisitionDetails.getDebitamount() != null
                && BigDecimal.ZERO.compareTo(egAdvanceRequisitionDetails.getDebitamount()) != 0)
            isDebit = true;
        if (coa != null && coa.getGlcode() != null) {
            Accountdetailtype contractorAccountDetailType = null;
            contractorAccountDetailType = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(
                    coa.getGlcode(), WorksConstants.ACCOUNTDETAIL_TYPE_CONTRACTOR);
            if (contractorAccountDetailType != null) {
                if (egAdvanceRequisitionDetails.getEgAdvanceReqpayeeDetailses().isEmpty())
                    egAdvanceRequisitionDetails.getEgAdvanceReqpayeeDetailses()
                            .add(getEgAdvanceReqPayeeDetails(egAdvanceRequisitionDetails,
                                    contractorAccountDetailType.getId(), new EgAdvanceReqPayeeDetails(),
                                    isDebit ? egAdvanceRequisitionDetails.getDebitamount()
                                            : egAdvanceRequisitionDetails.getCreditamount(),
                                    isDebit,
                                    Integer.valueOf(
                                            contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor()
                                                    .getId().toString())));
                else
                    for (EgAdvanceReqPayeeDetails payeeDetails : egAdvanceRequisitionDetails.getEgAdvanceReqpayeeDetailses())
                        payeeDetails = getEgAdvanceReqPayeeDetails(egAdvanceRequisitionDetails,
                                contractorAccountDetailType.getId(), payeeDetails,
                                isDebit ? egAdvanceRequisitionDetails.getDebitamount()
                                        : egAdvanceRequisitionDetails.getCreditamount(),
                                isDebit,
                                Integer.valueOf(
                                        contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor()
                                                .getId().toString()));
            } else
                errors.reject("error.contractoradvance.validate.glcode.for.subledger", new String[] { coa.getGlcode() },
                        null);
        }
    }

    public EgAdvanceReqPayeeDetails getEgAdvanceReqPayeeDetails(final EgAdvanceRequisitionDetails egAdvanceRequisitionDetails,
            final Integer accountsDetailTypeId, final EgAdvanceReqPayeeDetails egAdvanceReqPayeeDetails,
            final BigDecimal amount, final boolean isDebit, final Integer accountsDetailKeyId) {
        egAdvanceReqPayeeDetails.setAccountdetailKeyId(accountsDetailKeyId);
        egAdvanceReqPayeeDetails.setAccountDetailType(accountdetailtypeRepository.findOne(accountsDetailTypeId));
        if (isDebit)
            egAdvanceReqPayeeDetails.setDebitAmount(amount);
        else
            egAdvanceReqPayeeDetails.setCreditAmount(amount);
        egAdvanceReqPayeeDetails.setEgAdvanceRequisitionDetails(egAdvanceRequisitionDetails);
        return egAdvanceReqPayeeDetails;
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
            contractorAdvanceRequisition.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("").withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0))
                    && !WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction))
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
                contractorAdvanceRequisition.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("").withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = contractorAdvanceRequisitionWorkflowService.getWfMatrix(contractorAdvanceRequisition.getStateType(),
                        null, contractorAdvanceRequisition.getAdvanceRequisitionAmount(), additionalRule,
                        contractorAdvanceRequisition.getCurrentState().getValue(),
                        contractorAdvanceRequisition.getState().getNextAction());
                contractorAdvanceRequisition.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
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
                .equals(ContractorAdvanceRequisitionStatus.REJECTED.toString())) {
            updatedContractorAdvanceRequisition = update(contractorAdvanceRequisition, files);
            contractorContractorAdvanceStatusChange(updatedContractorAdvanceRequisition, workFlowAction, mode);
        } else {
            contractorContractorAdvanceStatusChange(contractorAdvanceRequisition, workFlowAction, mode);
            if (workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE)) {
                contractorAdvanceRequisition.setApprovedBy(securityUtils.getCurrentUser());
                createAndApproveAdvanceBills(contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister(),
                        new ArrayList<>());
                contractorAdvanceRequisition.getEgAdvanceReqMises().setSourcePath(WorksConstants.CONTRACTOR_ADVANCE_VIEW_URL
                        + contractorAdvanceRequisition.getId());
            }
        }
        updatedContractorAdvanceRequisition = contractorAdvanceRepository.save(contractorAdvanceRequisition);

        createContractorAdvanceWorkflowTransition(updatedContractorAdvanceRequisition, approvalPosition, approvalComent,
                additionalRule, workFlowAction);

        updatedContractorAdvanceRequisition = contractorAdvanceRepository.save(updatedContractorAdvanceRequisition);

        return updatedContractorAdvanceRequisition;
    }

    @Transactional
    public List<String> createAndApproveAdvanceBills(final EgBillregister egBillregister, final List<String> errorMessages) {
        validateLedgerAndSubledger(egBillregister, errorMessages);
        if (errorMessages.isEmpty()) {
            expenseBillService.create(egBillregister);
            egBillregister.getEgBillregistermis().setSourcePath(WorksConstants.EXPENSE_BILL_VIEW_URL
                    + egBillregister.getId());
        }
        return errorMessages;
    }

    @Transactional
    public void generateAdvanceBills(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final EgBillregister egBillregister, final BindingResult errors) {
        populateBillRegister(contractorAdvanceRequisition, egBillregister);
        populateBillDetails(contractorAdvanceRequisition, egBillregister, errors);
        populateBillregistermis(contractorAdvanceRequisition, egBillregister);
    }

    private void populateBillRegister(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final EgBillregister egBillregister) {
        egBillregister.setApprovalComent(contractorAdvanceRequisition.getApprovalComent());
        egBillregister.setApprovalDepartment(contractorAdvanceRequisition.getApprovalDepartment());
        egBillregister.setApprovedOn(contractorAdvanceRequisition.getApprovedDate());
        egBillregister.setApprover(contractorAdvanceRequisition.getApprovedBy());
        egBillregister.setBillamount(contractorAdvanceRequisition.getAdvanceRequisitionAmount());
        egBillregister.setBilldate(contractorAdvanceRequisition.getApprovedDate());
        egBillregister.setBillnumber(advanceBillNumberGenerator.getNextNumber(egBillregister));
        egBillregister.setBillstatus(ContractorAdvanceRequisitionStatus.APPROVED.toString());
        egBillregister.setBilltype(BillTypes.Final_Bill.toString());
        egBillregister.setPassedamount(contractorAdvanceRequisition.getAdvanceRequisitionAmount());
        egBillregister.setStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.ADVANCE_BILL,
                WorksConstants.APPROVED));
        egBillregister.setExpendituretype(WorksConstants.BILL_ADVANCE_TYPE);
    }

    private void populateBillDetails(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final EgBillregister egBillregister, final BindingResult errors) {
        for (final EgAdvanceRequisitionDetails details : contractorAdvanceRequisition.getEgAdvanceReqDetailses()) {
            final EgBilldetails egBilldetails = new EgBilldetails();
            egBilldetails.setFunctionid(BigDecimal
                    .valueOf(contractorAdvanceRequisition.getEgAdvanceReqMises().getFunction().getId()));
            egBilldetails.setCreditamount(details.getCreditamount());
            egBilldetails.setDebitamount(details.getDebitamount());
            egBilldetails.setChartOfAccounts(details.getChartofaccounts());
            egBilldetails.setEgBillregister(egBillregister);
            egBilldetails.setGlcodeid(BigDecimal.valueOf(details.getChartofaccounts().getId()));
            egBilldetails.setLastupdatedtime(new Date());
            egBillregister.addEgBilldetailes(getEgBillPayeeDetails(contractorAdvanceRequisition, egBilldetails, errors));
        }
    }

    private EgBilldetails getEgBillPayeeDetails(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final EgBilldetails egBilldetails, final BindingResult errors) {
        boolean isDebit = false;
        CChartOfAccounts coa = null;
        if (BigDecimal.ZERO.compareTo(egBilldetails.getGlcodeid()) != 0)
            coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid().longValue(), false);
        if (egBilldetails.getDebitamount() != null
                && BigDecimal.ZERO.compareTo(egBilldetails.getDebitamount()) != 0)
            isDebit = true;
        if (coa != null && coa.getGlcode() != null) {
            Accountdetailtype contractorAccountDetailType = null;
            contractorAccountDetailType = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(
                    coa.getGlcode(), WorksConstants.ACCOUNTDETAIL_TYPE_CONTRACTOR);
            if (contractorAccountDetailType != null)
                egBilldetails.getEgBillPaydetailes().add(getEgPayeeDetails(egBilldetails,
                        contractorAccountDetailType.getId(),
                        isDebit ? egBilldetails.getDebitamount() : egBilldetails.getCreditamount(), isDebit,
                        Integer.valueOf(
                                contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getId()
                                        .toString())));
            else
                errors.reject("error.contractoradvance.validate.glcode.for.subledger", new String[] { coa.getGlcode() },
                        null);
        }

        return egBilldetails;
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

    private void populateBillregistermis(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final EgBillregister egBillregister) {
        final EgBillregistermis billregistermis = new EgBillregistermis();
        billregistermis.setEgBillregister(egBillregister);
        billregistermis.setFieldid(contractorAdvanceRequisition.getEgAdvanceReqMises().getFieldId());
        billregistermis.setFunction(contractorAdvanceRequisition.getEgAdvanceReqMises().getFunction());
        billregistermis.setFunctionaryid(contractorAdvanceRequisition.getEgAdvanceReqMises().getFunctionaryId());
        billregistermis.setFund(contractorAdvanceRequisition.getEgAdvanceReqMises().getFund());
        billregistermis.setPayto(contractorAdvanceRequisition.getEgAdvanceReqMises().getPayto());
        billregistermis.setScheme(contractorAdvanceRequisition.getEgAdvanceReqMises().getScheme());
        billregistermis.setSubScheme(contractorAdvanceRequisition.getEgAdvanceReqMises().getSubScheme());
        billregistermis
                .setEgDepartment(contractorAdvanceRequisition.getEgAdvanceReqMises().getEgDepartment());
        billregistermis.setFundsource(contractorAdvanceRequisition.getEgAdvanceReqMises().getFundsource());
        billregistermis.setLastupdatedtime(new Date());
        egBillregister.setEgBillregistermis(billregistermis);
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
                        ContractorAdvanceRequisitionStatus.APPROVED.toString()));
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisitionStatus.REJECTED.toString()));
            else if (contractorAdvanceRequisition.getStatus().getCode()
                    .equals(ContractorAdvanceRequisitionStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisitionStatus.CANCELLED.toString()));
            else if (contractorAdvanceRequisition.getStatus().getCode()
                    .equals(ContractorAdvanceRequisitionStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                contractorAdvanceRequisition.setStatus(worksUtils.getStatusByModuleAndCode(
                        WorksConstants.CONTRACTOR_ADVANCE,
                        ContractorAdvanceRequisitionStatus.RESUBMITTED.toString()));
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

    public Double getTotalAdvanceBillsPaid(final Long workOrderEstimateId, final String approvedCode) {
        return contractorAdvanceRepository.getTotalAdvanceBillsPaid(workOrderEstimateId, approvedCode);
    }

    public void validateInput(final ContractorAdvanceRequisition contractorAdvanceRequisition, final BindingResult errors) {
        Double advancePaidTillNow = getTotalAdvancePaid(
                contractorAdvanceRequisition.getId() == null ? -1L : contractorAdvanceRequisition.getId(),
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                ContractorAdvanceRequisitionStatus.APPROVED.toString());
        Double totalPartBillsAmount = contractorBillRegisterService.getTotalPartBillsAmount(
                Long.valueOf(contractorAdvanceRequisition.getWorkOrderEstimate().getId()),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString(),
                BillTypes.Part_Bill.toString());
        final List<MBHeader> mbHeaders = mbHeaderService
                .getMBHeadersByWorkOrderEstimateIdAndNotEgwStatusCode(contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString());
        if (!mbHeaders.isEmpty())
            errors.reject("error.mb.created", new String[] {}, null);
        if (totalPartBillsAmount == null)
            totalPartBillsAmount = 0D;
        if (advancePaidTillNow == null)
            advancePaidTillNow = 0D;
        if (contractorAdvanceRequisition.getAdvanceRequisitionAmount()
                .add(BigDecimal.valueOf(advancePaidTillNow + totalPartBillsAmount)).compareTo(BigDecimal
                        .valueOf(contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount())) > 0) {
            final Double diffAmount = advancePaidTillNow + totalPartBillsAmount
                    + contractorAdvanceRequisition.getAdvanceRequisitionAmount().longValue()
                    - contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount();
            errors.reject("error.advance.exceeded", new String[] { diffAmount.toString() },
                    null);
        }
    }

    public void validateARFInDrafts(final Long contractorAdvanceRegisterId, final Long workOrderEstimateId,
            final JsonObject jsonObject, final BindingResult errors) {
        ContractorAdvanceRequisition contractorAdvanceRequisition = null;
        contractorAdvanceRequisition = contractorAdvanceRepository
                .findByWorkOrderEstimate_IdAndStatus_codeEquals(workOrderEstimateId, WorksConstants.NEW);
        String userName = "";
        if (contractorAdvanceRequisition != null && contractorAdvanceRequisition.getState() != null
                && contractorAdvanceRequisition.getState().getOwnerPosition() != null) {
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

    public void validateARFInWorkFlow(final Long contractorAdvanceRegisterId, final Long workOrderEstimateId,
            final JsonObject jsonObject, final BindingResult errors) {
        ContractorAdvanceRequisition contractorAdvanceRequisition = null;
        contractorAdvanceRequisition = contractorAdvanceRepository
                .findByWorkOrderEstimateAndStatus(workOrderEstimateId,
                        ContractorAdvanceRequisitionStatus.CANCELLED.toString(),
                        ContractorAdvanceRequisitionStatus.APPROVED.toString(),
                        ContractorAdvanceRequisitionStatus.NEW.toString());
        String userName = "";
        if (contractorAdvanceRequisition != null && contractorAdvanceRequisition.getState() != null
                && contractorAdvanceRequisition.getState().getOwnerPosition() != null) {
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

    public List<String> findAdvanceBillNumber(final String advanceBillNumber) {
        final List<String> advanceBillNumbers = contractorAdvanceRepository
                .findAdvanceBillNumber("%" + advanceBillNumber + "%");
        return advanceBillNumbers;
    }

    public void validateLedgerAndSubledger(final EgBillregister egBillregister, final List<String> errorMessages) {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {
            if (details.getDebitamount() != null)
                totalDrAmt = totalDrAmt.add(details.getDebitamount());
            if (details.getCreditamount() != null)
                totalCrAmt = totalCrAmt.add(details.getCreditamount());
            if (details.getGlcodeid() == null)
                errorMessages.add(messageSource.getMessage("msg.advance.bill.accdetail.accmissing", new String[] {}, null));

            if (details.getDebitamount() != null && details.getCreditamount() != null
                    && details.getDebitamount().equals(BigDecimal.ZERO) && details.getCreditamount().equals(BigDecimal.ZERO)
                    && details.getGlcodeid() != null)
                errorMessages.add(messageSource.getMessage("msg.advance.bill.accdetail.amountzero",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null));

            if (details.getDebitamount() != null && details.getCreditamount() != null
                    && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1
                    && details.getCreditamount().compareTo(BigDecimal.ZERO) == 1)
                errorMessages.add(messageSource.getMessage("msg.advance.bill.accdetail.amount",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null));
        }
        if (totalDrAmt.compareTo(totalCrAmt) != 0)
            errorMessages.add(messageSource.getMessage("msg.advance.bill.accdetail.drcrmatch", new String[] {}, null));
        validateSubledgerDetails(egBillregister, errorMessages);
    }

    protected void validateSubledgerDetails(final EgBillregister egBillregister, final List<String> errorMessages) {
        Boolean check;
        BigDecimal detailAmt;
        BigDecimal payeeDetailAmt;
        for (final EgBilldetails details : egBillregister.getEgBilldetailes()) {

            detailAmt = BigDecimal.ZERO;
            payeeDetailAmt = BigDecimal.ZERO;

            if (details.getDebitamount() != null && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1)
                detailAmt = details.getDebitamount();
            else if (details.getCreditamount() != null &&
                    details.getCreditamount().compareTo(BigDecimal.ZERO) == 1)
                detailAmt = details.getCreditamount();

            for (final EgBillPayeedetails payeeDetails : details.getEgBillPaydetailes()) {

                if (payeeDetails.getDebitAmount() != null && payeeDetails.getCreditAmount() != null
                        && payeeDetails.getDebitAmount().equals(BigDecimal.ZERO)
                        && payeeDetails.getCreditAmount().equals(BigDecimal.ZERO))
                    errorMessages.add(messageSource.getMessage("msg.advance.bill.subledger.amountzero",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null));

                if (payeeDetails.getDebitAmount() != null)
                    payeeDetailAmt = payeeDetailAmt.add(payeeDetails.getDebitAmount());
                else if (payeeDetails.getCreditAmount() != null)
                    payeeDetailAmt = payeeDetailAmt.add(payeeDetails.getCreditAmount());

                check = false;
                for (final CChartOfAccountDetail coaDetails : details.getChartOfAccounts().getChartOfAccountDetails())
                    if (payeeDetails.getAccountDetailTypeId() == coaDetails.getDetailTypeId().getId())
                        check = true;
                if (!check)
                    errorMessages.add(messageSource.getMessage("msg.advance.bill.subledger.mismatch",
                            new String[] { details.getChartOfAccounts().getGlcode() }, null));

            }

            if (detailAmt.compareTo(payeeDetailAmt) != 0 && !details.getEgBillPaydetailes().isEmpty())
                errorMessages.add(messageSource.getMessage("msg.advance.bill.subledger.amtnotmatchinng",
                        new String[] { details.getChartOfAccounts().getGlcode() }, null));
        }
    }

    public List<ContractorAdvanceRequisition> getContractorAdvancesToCancelLOA(final WorkOrderEstimate workOrderEstimate) {
        return contractorAdvanceRepository.findByWorkOrderEstimate_IdAndStatus_CodeNot(workOrderEstimate.getId(),
                WorksConstants.CANCELLED_STATUS);
    }

    public List<User> getAdvanceRequisitionCreatedByUsers() {
        return contractorAdvanceRepository
                .getAdvanceRequisitionCreatedByUsers(ContractorAdvanceRequisitionStatus.APPROVED.toString());
    }

    public List<String> findAdvanceRequisitionNumberToCancelContractorAdvance(final String advanceRequisitionNumber) {
        final List<String> advanceRequisitionNumbers = contractorAdvanceRepository
                .findAdvanceRequisitionNumberToCancelContractorAdvance("%" + advanceRequisitionNumber + "%",
                        ContractorAdvanceRequisitionStatus.APPROVED.toString());
        return advanceRequisitionNumbers;
    }

    public List<String> findContractorsToCancelContractorAdvance(final String contractorName) {
        final List<String> contractorNames = contractorAdvanceRepository
                .findContractorsToCancelContractorAdvance("%" + contractorName + "%",
                        ContractorAdvanceRequisitionStatus.APPROVED.toString());
        return contractorNames;
    }

    public List<String> findWorkOrderNumberToCancelContractorAdvance(final String workOrderNumber) {
        final List<String> workOrderNumbers = contractorAdvanceRepository
                .findWorkOrderNumberToCancelContractorAdvance("%" + workOrderNumber + "%",
                        ContractorAdvanceRequisitionStatus.APPROVED.toString());
        return workOrderNumbers;
    }

    public List<ContractorAdvanceRequisition> searchContractorAdvanceToCancel(
            final SearchRequestContractorRequisition searchRequestContractorRequisition) {
        final StringBuilder queryStr = new StringBuilder(500);

        queryStr.append(
                "select car from ContractorAdvanceRequisition as car where car.status.code =:contractorAdvanceStatus ");

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

        if (searchRequestContractorRequisition.getCreatedBy() != null)
            queryStr.append(
                    " and car.createdBy.id = :createdBy)");

        final Query query = setParameterToCancelContractorAdvance(searchRequestContractorRequisition, queryStr);
        final List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = query.getResultList();
        return contractorAdvanceRequisitionList;
    }

    private Query setParameterToCancelContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("contractorAdvanceStatus", ContractorAdvanceRequisitionStatus.APPROVED.toString());
        if (searchRequestContractorRequisition != null) {
            setSearchParameterForContractorAdvance(searchRequestContractorRequisition, qry);
            if (searchRequestContractorRequisition.getCreatedBy() != null)
                qry.setParameter("createdBy",
                        searchRequestContractorRequisition.getCreatedBy());

        }
        return qry;
    }

    @Transactional
    public ContractorAdvanceRequisition cancelContractorAdvance(final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        contractorAdvanceRequisition.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                WorksConstants.CONTRACTOR_ADVANCE, ContractorAdvanceRequisitionStatus.CANCELLED.toString()));
        if (contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister() != null) {
            contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister()
                    .setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                            WorksConstants.ADVANCE_BILL, WorksConstants.CANCELLED_STATUS));
            contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister()
                    .setBillstatus(ContractorAdvanceRequisitionStatus.CANCELLED.toString());

        }
        return contractorAdvanceRepository.save(contractorAdvanceRequisition);
    }

    public String getAdvanceRequisitionGreaterThanCurrent(final Long workOrderEstimateId, final Date createdDate) {

        final List<ContractorAdvanceRequisition> contractorAdvanceRequisitions = contractorAdvanceRepository
                .findByWorkOrderEstimate_idAndCreatedDateAfterAndStatus_codeNotLike(workOrderEstimateId, createdDate,
                        ContractorAdvanceRequisitionStatus.CANCELLED.toString());
        final StringBuilder advanceRequistion = new StringBuilder();
        for (final ContractorAdvanceRequisition revisionAbstractEstimate : contractorAdvanceRequisitions)
            advanceRequistion.append(revisionAbstractEstimate.getAdvanceRequisitionNumber()).append(',');

        return advanceRequistion.toString();
    }

    private void setSearchParameterForContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition, final Query qry) {
        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getWorkOrderNumber()))
            qry.setParameter("workOrderNumber",
                    searchRequestContractorRequisition.getWorkOrderNumber().toUpperCase());
        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceRequisitionNumber()))
            qry.setParameter("advanceRequisitionNumber",
                    searchRequestContractorRequisition.getAdvanceRequisitionNumber().toUpperCase());
        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getContractorName()))
            qry.setParameter("contractorName", searchRequestContractorRequisition.getContractorName().toUpperCase());
        if (searchRequestContractorRequisition.getFromDate() != null)
            qry.setParameter("fromDate", searchRequestContractorRequisition.getFromDate());
        if (searchRequestContractorRequisition.getToDate() != null) {
            final DateTime dateTime = new DateTime(searchRequestContractorRequisition.getToDate().getTime()).plusDays(1);
            qry.setParameter("toDate", dateTime.toDate());
        }
        if (searchRequestContractorRequisition.getEgwStatus() != null)
            qry.setParameter("status", searchRequestContractorRequisition.getEgwStatus());
    }

}