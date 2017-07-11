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
package org.egov.egf.expensebill.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.service.ChartOfAccountDetailService;
import org.egov.commons.service.CheckListService;
import org.egov.commons.service.FundService;
import org.egov.egf.autonumber.ExpenseBillNumberGenerator;
import org.egov.egf.autonumber.WorksBillNumberGenerator;
import org.egov.egf.billsubtype.service.EgBillSubTypeService;
import org.egov.egf.expensebill.repository.ExpenseBillRepository;
import org.egov.egf.utils.FinancialUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.models.EgChecklists;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillregister;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author venki
 */

@Service
@Transactional(readOnly = true)
public class ExpenseBillService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseBillService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final ExpenseBillRepository expenseBillRepository;

    @Autowired
    private EgBillSubTypeService egBillSubTypeService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private FinancialUtils financialUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    protected AppConfigValueService appConfigValuesService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    private final ScriptService scriptExecutionService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    @Qualifier(value = "voucherService")
    private VoucherService voucherService;

    @Autowired
    private CheckListService checkListService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<EgBillregister> egBillregisterRegisterWorkflowService;

    @Autowired
    private FundService fundService;

    @Autowired
    private ChartOfAccountDetailService chartOfAccountDetailService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public ExpenseBillService(final ExpenseBillRepository expenseBillRepository, final ScriptService scriptExecutionService) {
        this.expenseBillRepository = expenseBillRepository;
        this.scriptExecutionService = scriptExecutionService;
    }

    public EgBillregister getById(final Long id) {
        return expenseBillRepository.findOne(id);
    }

    public EgBillregister getByBillnumber(final String billNumber) {
        return expenseBillRepository.findByBillnumber(billNumber);
    }

    @Transactional
    public EgBillregister create(final EgBillregister egBillregister) {
        return expenseBillRepository.save(egBillregister);
    }

    @Transactional
    public EgBillregister create(final EgBillregister egBillregister, final Long approvalPosition, final String approvalComent,
            final String additionalRule, final String workFlowAction) {
        if (StringUtils.isBlank(egBillregister.getBilltype()))
            egBillregister.setBilltype(FinancialConstants.BILLTYPE_FINAL_BILL);
        if (StringUtils.isBlank(egBillregister.getExpendituretype()))
            egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        egBillregister.setPassedamount(egBillregister.getBillamount());
        egBillregister.getEgBillregistermis().setEgBillregister(egBillregister);
        egBillregister.getEgBillregistermis().setLastupdatedtime(new Date());

        if (egBillregister.getEgBillregistermis().getFund() != null
                && egBillregister.getEgBillregistermis().getFund().getId() != null)
            egBillregister.getEgBillregistermis().setFund(
                    fundService.findOne(egBillregister.getEgBillregistermis().getFund().getId()));
        if (egBillregister.getEgBillregistermis().getEgBillSubType() != null
                && egBillregister.getEgBillregistermis().getEgBillSubType().getId() != null)
            egBillregister.getEgBillregistermis().setEgBillSubType(
                    egBillSubTypeService.getById(egBillregister.getEgBillregistermis().getEgBillSubType().getId()));
        if (egBillregister.getEgBillregistermis().getSchemeId() != null)
            egBillregister.getEgBillregistermis().setScheme(
                    schemeService.findById(egBillregister.getEgBillregistermis().getSchemeId().intValue(), false));
        else
            egBillregister.getEgBillregistermis().setScheme(null);
        if (egBillregister.getEgBillregistermis().getSubSchemeId() != null)
            egBillregister.getEgBillregistermis().setSubScheme(
                    subSchemeService.findById(egBillregister.getEgBillregistermis().getSubSchemeId().intValue(), false));
        else
            egBillregister.getEgBillregistermis().setSubScheme(null);

        if (isBillNumberGenerationAuto())
            egBillregister.setBillnumber(getNextBillNumber(egBillregister));

        try {
            checkBudgetAndGenerateBANumber(egBillregister);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        }

        final List<EgChecklists> checkLists = egBillregister.getCheckLists();

        final EgBillregister savedEgBillregister = expenseBillRepository.save(egBillregister);

        createCheckList(savedEgBillregister, checkLists);

        if (workFlowAction.equals(FinancialConstants.CREATEANDAPPROVE)) {
            if (FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equals(egBillregister.getExpendituretype()))
                savedEgBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS));
        } else {
            savedEgBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                    FinancialConstants.CONTINGENCYBILL_CREATED_STATUS));
            createExpenseBillRegisterWorkflowTransition(savedEgBillregister, approvalPosition, approvalComent, additionalRule,
                    workFlowAction);
        }
        // TODO: add the code to handle new screen for view bills of all type
        savedEgBillregister.getEgBillregistermis().setSourcePath(
                "/EGF/expensebill/view/" + savedEgBillregister.getId().toString());

        return expenseBillRepository.save(savedEgBillregister);
    }

    @Transactional
    public void deleteCheckList(final EgBillregister egBillregister) {
        final List<EgChecklists> checkLists = checkListService.getByObjectId(egBillregister.getId());
        for (final EgChecklists check : checkLists)
            checkListService.delete(check);

    }

    @Transactional
    public void createCheckList(final EgBillregister egBillregister, final List<EgChecklists> checkLists) {
        for (final EgChecklists check : checkLists) {
            check.setObjectid(egBillregister.getId());
            final AppConfigValues configValue = appConfigValuesService.getById(check.getAppconfigvalue().getId());
            check.setAppconfigvalue(configValue);
            checkListService.create(check);
        }

    }

    public void checkBudgetAndGenerateBANumber(final EgBillregister egBillregister) {
        final ScriptContext scriptContext = ScriptService.createContext("voucherService", voucherService, "bill",
                egBillregister);
        scriptExecutionService.executeScript("egf.bill.budgetcheck", scriptContext);
    }

    @Transactional
    public EgBillregister update(final EgBillregister egBillregister, final Long approvalPosition, final String approvalComent,
            final String additionalRule, final String workFlowAction, final String mode) throws ValidationException, IOException {
        EgBillregister updatedegBillregister = null;
        if ("edit".equals(mode)) {
            egBillregister.setPassedamount(egBillregister.getBillamount());
            egBillregister.getEgBillregistermis().setLastupdatedtime(new Date());

            if (egBillregister.getEgBillregistermis().getFund() != null
                    && egBillregister.getEgBillregistermis().getFund().getId() != null)
                egBillregister.getEgBillregistermis().setFund(
                        fundService.findOne(egBillregister.getEgBillregistermis().getFund().getId()));
            if (egBillregister.getEgBillregistermis().getSchemeId() != null)
                egBillregister.getEgBillregistermis().setScheme(
                        schemeService.findById(egBillregister.getEgBillregistermis().getSchemeId().intValue(), false));
            else
                egBillregister.getEgBillregistermis().setScheme(null);
            if (egBillregister.getEgBillregistermis().getSubSchemeId() != null)
                egBillregister.getEgBillregistermis().setSubScheme(
                        subSchemeService.findById(egBillregister.getEgBillregistermis().getSubSchemeId().intValue(), false));
            else
                egBillregister.getEgBillregistermis().setSubScheme(null);

            if (isBillNumberGenerationAuto())
                egBillregister.setBillnumber(getNextBillNumber(egBillregister));

            final List<EgChecklists> checkLists = egBillregister.getCheckLists();

            updatedegBillregister = expenseBillRepository.save(egBillregister);

            deleteCheckList(updatedegBillregister);
            createCheckList(updatedegBillregister, checkLists);
            egBillregister.getEgBillregistermis().setBudgetaryAppnumber(null);
            try {
                checkBudgetAndGenerateBANumber(egBillregister);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            }

        }
        if (updatedegBillregister != null) {
            if (workFlowAction.equals(FinancialConstants.CREATEANDAPPROVE))
                updatedegBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS));
            else {
                expenseBillRegisterStatusChange(updatedegBillregister, workFlowAction);
                createExpenseBillRegisterWorkflowTransition(updatedegBillregister, approvalPosition, approvalComent,
                        additionalRule,
                        workFlowAction);
            }
            updatedegBillregister = expenseBillRepository.save(updatedegBillregister);
        } else {
            if (workFlowAction.equals(FinancialConstants.CREATEANDAPPROVE))
                egBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS));
            else {
                expenseBillRegisterStatusChange(egBillregister, workFlowAction);
                createExpenseBillRegisterWorkflowTransition(egBillregister, approvalPosition, approvalComent,
                        additionalRule,
                        workFlowAction);
            }
            updatedegBillregister = expenseBillRepository.save(egBillregister);
        }

        return updatedegBillregister;
    }

    public void expenseBillRegisterStatusChange(final EgBillregister egBillregister, final String workFlowAction)
            throws ValidationException {
        if (null != egBillregister && null != egBillregister.getStatus()
                && null != egBillregister.getStatus().getCode())
            if (FinancialConstants.CONTINGENCYBILL_CREATED_STATUS.equals(egBillregister.getStatus().getCode())
                    && egBillregister.getState() != null && workFlowAction.equalsIgnoreCase(FinancialConstants.BUTTONAPPROVE))
                egBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS));
            else if (workFlowAction.equals(FinancialConstants.BUTTONREJECT))
                egBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_REJECTED_STATUS));
            else if (FinancialConstants.CONTINGENCYBILL_REJECTED_STATUS.equals(egBillregister.getStatus().getCode())
                    && workFlowAction.equals(FinancialConstants.BUTTONCANCEL))
                egBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS));
            else if (FinancialConstants.CONTINGENCYBILL_REJECTED_STATUS.equals(egBillregister.getStatus().getCode())
                    && workFlowAction.equals(FinancialConstants.BUTTONFORWARD))
                egBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                        FinancialConstants.CONTINGENCYBILL_CREATED_STATUS));

    }

    @Transactional(readOnly = true)
    public boolean isBillNumberGenerationAuto() {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, FinancialConstants.KEY_BILLNUMBER_APPCONFIG);
        if (!configValuesByModuleAndKey.isEmpty())
            return "Y".equals(configValuesByModuleAndKey.get(0).getValue());
        else
            return false;
    }

    private String getNextBillNumber(final EgBillregister bill) {
        if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equals(bill.getExpendituretype())) {
            final WorksBillNumberGenerator b = beanResolver.getAutoNumberServiceFor(WorksBillNumberGenerator.class);
            return b.getNextNumber(bill);
        } else {
            final ExpenseBillNumberGenerator b = beanResolver.getAutoNumberServiceFor(ExpenseBillNumberGenerator.class);
            return b.getNextNumber(bill);
        }
    }

    public void validateSubledgeDetails(EgBillregister egBillregister) {
        final List<EgBillPayeedetails> payeeDetails = new ArrayList<>();
        for (final EgBillPayeedetails payeeDetail : egBillregister.getBillPayeedetails()) {
            CChartOfAccountDetail coaDetail = chartOfAccountDetailService
                    .getByGlcodeIdAndDetailTypeId(payeeDetail.getEgBilldetailsId().getGlcodeid().longValue(),
                            payeeDetail.getAccountDetailTypeId().intValue());
            if (coaDetail != null)
                payeeDetails.add(payeeDetail);
        }
        egBillregister.getBillPayeedetails().clear();
        egBillregister.setBillPayeedetails(payeeDetails);
    }

    public void createExpenseBillRegisterWorkflowTransition(final EgBillregister egBillregister,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Assignment wfInitiator = null;
        Map<String, String> finalDesignationNames = new HashMap<String, String>();
        final String currState = "";
        String stateValue = "";
        if (null != egBillregister.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(egBillregister.getCreatedBy().getId());
        if (FinancialConstants.BUTTONREJECT.toString().equalsIgnoreCase(workFlowAction)) {
            stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
            egBillregister.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition())
                    .withNextAction("")
                    .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_EXPENSE_BILL_DISPLAYNAME);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                wfInitiator = assignmentService.getAssignmentsForPosition(approvalPosition).get(0);
            WorkFlowMatrix wfmatrix;

            wfmatrix = egBillregisterRegisterWorkflowService.getWfMatrix(egBillregister.getStateType(), null,
                    null, additionalRule, FinancialConstants.WF_STATE_FINAL_APPROVAL_PENDING, null);

            if (wfmatrix != null && wfmatrix.getCurrentDesignation() != null) {
                final List<String> finalDesignationName = Arrays.asList(wfmatrix.getCurrentDesignation().split(","));
                for (final String desgName : finalDesignationName)
                    if (desgName != null && !"".equals(desgName.trim()))
                        finalDesignationNames.put(desgName.toUpperCase(), desgName.toUpperCase());
            }

            if (null == egBillregister.getState()) {

                if (wfInitiator.getDesignation() != null
                        && finalDesignationNames.get(wfInitiator.getDesignation().getName().toUpperCase()) != null)
                    stateValue = FinancialConstants.WF_STATE_FINAL_APPROVAL_PENDING;

                wfmatrix = egBillregisterRegisterWorkflowService.getWfMatrix(egBillregister.getStateType(), null,
                        null, additionalRule, currState, null);

                if (stateValue.isEmpty())
                    stateValue = wfmatrix.getNextState();

                egBillregister.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(new Date()).withOwner(wfInitiator.getPosition())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_EXPENSE_BILL_DISPLAYNAME);
            } else if (FinancialConstants.BUTTONCANCEL.toString().equalsIgnoreCase(workFlowAction)) {
                stateValue = FinancialConstants.WORKFLOW_STATE_CANCELLED;
                wfmatrix = egBillregisterRegisterWorkflowService.getWfMatrix(egBillregister.getStateType(), null,
                        null, additionalRule, egBillregister.getCurrentState().getValue(), null);
                egBillregister.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withNextAction("")
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_EXPENSE_BILL_DISPLAYNAME);
            } else if (FinancialConstants.BUTTONAPPROVE.toString().equalsIgnoreCase(workFlowAction)) {
                wfmatrix = egBillregisterRegisterWorkflowService.getWfMatrix(egBillregister.getStateType(), null,
                        null, additionalRule, egBillregister.getCurrentState().getValue(), null);

                if (stateValue.isEmpty())
                    stateValue = wfmatrix.getNextState();

                egBillregister.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(new Date())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_EXPENSE_BILL_DISPLAYNAME);
            } else {
                if (wfInitiator.getDesignation() != null
                        && finalDesignationNames.get(wfInitiator.getDesignation().getName().toUpperCase()) != null)
                    stateValue = FinancialConstants.WF_STATE_FINAL_APPROVAL_PENDING;

                wfmatrix = egBillregisterRegisterWorkflowService.getWfMatrix(egBillregister.getStateType(), null,
                        null, additionalRule, egBillregister.getCurrentState().getValue(), null);

                if (stateValue.isEmpty())
                    stateValue = wfmatrix.getNextState();

                egBillregister.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(new Date()).withOwner(wfInitiator.getPosition())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_EXPENSE_BILL_DISPLAYNAME);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public Long getApprovalPositionByMatrixDesignation(final EgBillregister egBillregister,
            final String additionalRule, final String mode, final String workFlowAction) {
        Long approvalPosition = null;
        final WorkFlowMatrix wfmatrix = egBillregisterRegisterWorkflowService.getWfMatrix(egBillregister
                .getStateType(), null, null, additionalRule, egBillregister.getCurrentState().getValue(), null);
        if (egBillregister.getState() != null && !egBillregister.getState().getHistory().isEmpty()
                && egBillregister.getState().getOwnerPosition() != null)
            approvalPosition = egBillregister.getState().getOwnerPosition().getId();
        else if (wfmatrix != null)
            approvalPosition = financialUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                    egBillregister.getState(), egBillregister.getCreatedBy().getId());
        if (workFlowAction.equals(FinancialConstants.BUTTONCANCEL))
            approvalPosition = null;

        return approvalPosition;
    }

    public boolean isBillNumUnique(final String billnumber) {
        final EgBillregister bill = getByBillnumber(billnumber);
        return bill == null;
    }
}
