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
package org.egov.egf.voucher.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.FiscalPeriodHibernateDAO;
import org.egov.commons.service.FundService;
import org.egov.egf.autonumber.JVBillNumberGenerator;
import org.egov.egf.autonumber.VouchernumberGenerator;
import org.egov.egf.billsubtype.service.EgBillSubTypeService;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.egf.utils.FinancialUtils;
import org.egov.egf.voucher.repository.JournalVoucherRepository;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.pims.commons.Position;
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

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

/**
 * @author venki
 */

@Service
@Transactional(readOnly = true)
public class JournalVoucherService {

    private static final String JOURNALVOUCHER = "JOURNALVOUCHER";

    private static final Logger LOG = LoggerFactory.getLogger(JournalVoucherService.class);

    enum voucherTypeEnum {
        JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT;
    }

    enum voucherSubTypeEnum {
        JOURNALVOUCHER, CONTRA, RECEIPT, PAYMENT, PURCHASEJOURNAL, PENSIONJOURNAL, PURCHASE, WORKS, CONTRACTORJOURNAL, FIXEDASSETJOURNAL, FIXEDASSET, PENSION, WORKSJOURNAL, CONTINGENTJOURNAL, SALARY, SALARYJOURNAL, EXPENSE, EXPENSEJOURNAL, JVGENERAL;
    }

    @PersistenceContext
    private EntityManager entityManager;

    private final JournalVoucherRepository journalVoucherRepository;

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

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    @Qualifier(value = "voucherService")
    private VoucherService voucherService;

    @Autowired
    private VoucherTypeForULB voucherTypeForULB;

    @Autowired
    private FiscalPeriodHibernateDAO fiscalPeriodHibernateDAO;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<CVoucherHeader> cvoucherHeaderRegisterWorkflowService;

    @Autowired
    private FundService fundService;

    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Autowired
    private EgBillSubTypeService egBillSubTypeService;

    @Autowired
    private ExpenseBillService expenseBillService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public JournalVoucherService(final JournalVoucherRepository journalVoucherRepository) {
        this.journalVoucherRepository = journalVoucherRepository;
    }

    public CVoucherHeader getById(final Long id) {
        return journalVoucherRepository.findOne(id);
    }

    public CVoucherHeader getByVoucherNumber(final String voucherNumber) {
        return journalVoucherRepository.findByVoucherNumber(voucherNumber);
    }

    @Transactional
    public CVoucherHeader create(final CVoucherHeader voucherHeader, final Long approvalPosition, final String approvalComent,
                                 final String additionalRule, final String workFlowAction) {

        voucherHeader.setFiscalPeriodId(
                fiscalPeriodHibernateDAO.getFiscalPeriodByDate(voucherHeader.getVoucherDate()).getId().intValue());
        if (voucherHeader.getFundId() != null
                && voucherHeader.getFundId().getId() != null)
            voucherHeader.setFundId(fundService.findOne(voucherHeader.getFundId().getId()));
        if (voucherHeader.getVouchermis().getSchemeid() != null
                && voucherHeader.getVouchermis().getSchemeid().getId() != null)
            voucherHeader.getVouchermis().setSchemeid(
                    schemeService.findById(voucherHeader.getVouchermis().getSchemeid().getId(), false));
        else
            voucherHeader.getVouchermis().setSchemeid(null);
        if (voucherHeader.getVouchermis().getSubschemeid() != null
                && voucherHeader.getVouchermis().getSubschemeid().getId() != null)
            voucherHeader.getVouchermis().setSubschemeid(
                    subSchemeService.findById(voucherHeader.getVouchermis().getSubschemeid().getId(), false));
        else
            voucherHeader.getVouchermis().setSubschemeid(null);

        populateVoucherNumber(voucherHeader, null);
        populateCGVNNumber(voucherHeader);

        if (!FinancialConstants.JOURNALVOUCHER_NAME_GENERAL.equalsIgnoreCase(voucherHeader.getName()))
            createBillForVoucherHeader(voucherHeader);

        final CVoucherHeader savedVoucherHeader = journalVoucherRepository.save(voucherHeader);

        voucherHeader.getVouchermis().setSourcePath(
                "/EGF/voucher/journalVoucherModify-beforeModify.action?voucherHeader.id="
                        + voucherHeader.getId());
        update(voucherHeader);

        if (workFlowAction.equals(FinancialConstants.CREATEANDAPPROVE))
            voucherHeader.setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
        else {
            voucherHeader.setStatus(FinancialConstants.PREAPPROVEDVOUCHERSTATUS);
            createVoucherHeaderRegisterWorkflowTransition(savedVoucherHeader, approvalPosition, approvalComent, additionalRule,
                    workFlowAction);
        }

        return journalVoucherRepository.save(savedVoucherHeader);
    }

    @Transactional
    public CVoucherHeader update(final CVoucherHeader voucherHeader) {

        return journalVoucherRepository.save(voucherHeader);
    }

    @Transactional
    public void createBillForVoucherHeader(final CVoucherHeader voucherHeader) {
        final EgBillregister egBillregister = new EgBillregister();
        EgwStatus egwstatus = null;
        if (FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL.equalsIgnoreCase(voucherHeader.getName())) {
            egwstatus = financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTRACTOR_BILL,
                    FinancialConstants.CONTRACTORBILL_APPROVED_STATUS);
            egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS);
        } else if (FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL.equalsIgnoreCase(voucherHeader.getName())) {
            egwstatus = financialUtils.getStatusByModuleAndCode(FinancialConstants.SBILL,
                    FinancialConstants.SALARYBILL_APPROVED_STATUS);
            egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE);
        } else if (FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL.equalsIgnoreCase(voucherHeader.getName())) {
            egwstatus = financialUtils.getStatusByModuleAndCode(FinancialConstants.SALARYBILL,
                    FinancialConstants.SALARYBILL_APPROVED_STATUS);
            egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY);
        } else if (FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL.equalsIgnoreCase(voucherHeader.getName())) {
            egwstatus = financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                    FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS);
            egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        } else if (FinancialConstants.JOURNALVOUCHER_NAME_PENSIONJOURNAL.equalsIgnoreCase(voucherHeader.getName())) {
            egwstatus = financialUtils.getStatusByModuleAndCode(FinancialConstants.PENSIONBILL,
                    FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
            egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION);
        }
        egBillregister.setStatus(egwstatus);
        if (null != voucherHeader.getBillDate())
            egBillregister.setBilldate(voucherHeader.getBillDate());
        else
            egBillregister.setBilldate(voucherHeader.getVoucherDate());
        if (null != voucherHeader.getVouchermis().getDivisionid())
            egBillregister.setFieldid(new BigDecimal(voucherHeader
                    .getVouchermis().getDivisionid().getId().toString()));
        egBillregister.setNarration(voucherHeader.getDescription());
        egBillregister.setIsactive(true);
        egBillregister.setBilltype(FinancialConstants.BILLTYPE_FINAL_BILL);
        egBillregister.setPassedamount(voucherHeader.getTotalAmount());
        egBillregister.setBillamount(voucherHeader.getTotalAmount());

        final EgBillregistermis egBillregistermis = new EgBillregistermis();
        egBillregistermis.setFund(voucherHeader.getFundId());
        egBillregistermis.setEgDepartment(voucherHeader.getVouchermis().getDepartmentid());
        egBillregistermis.setFunctionaryid(voucherHeader.getVouchermis().getFunctionary());
        egBillregistermis.setFunction(voucherHeader.getVouchermis().getFunction());
        egBillregistermis.setFundsource(voucherHeader.getVouchermis().getFundsource());
        egBillregistermis.setScheme(voucherHeader.getVouchermis().getSchemeid());
        egBillregistermis.setSubScheme(voucherHeader.getVouchermis().getSubschemeid());
        egBillregistermis.setNarration(voucherHeader.getDescription());
        egBillregistermis.setPartyBillDate(voucherHeader.getPartyBillDate());
        egBillregistermis.setPayto(voucherHeader.getPartyName());
        egBillregistermis.setPartyBillNumber(voucherHeader.getPartyBillNumber());
        egBillregistermis.setFieldid(voucherHeader.getVouchermis().getDivisionid());
        if (FinancialConstants.VOUCHER_TYPE_FIXEDASSET.equalsIgnoreCase(voucherHeader.getVoucherNumType())) {
            final EgBillSubType egBillSubType = egBillSubTypeService.getByExpenditureTypeAndName(
                    FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE, FinancialConstants.STANDARD_SUBTYPE_FIXED_ASSET);
            egBillregistermis.setEgBillSubType(egBillSubType);
        }
        egBillregistermis.setLastupdatedtime(new Date());
        egBillregistermis.setVoucherHeader(voucherHeader);
        egBillregister.setEgBillregistermis(egBillregistermis);
        if (null != voucherHeader.getBillNumber() && StringUtils.isNotEmpty(voucherHeader.getBillNumber()))
            egBillregister.setBillnumber(voucherHeader.getBillNumber());
        else {
            final JVBillNumberGenerator b = beanResolver.getAutoNumberServiceFor(JVBillNumberGenerator.class);
            final String billNumber = b.getNextNumber(egBillregister);

            egBillregister.setBillnumber(billNumber);
        }
        if (!expenseBillService.isBillNumUnique(egBillregister.getBillnumber()))
            throw new ValidationException(Arrays.asList(new ValidationError("bill number",
                    "Duplicate Bill Number : " + egBillregister.getBillnumber())));

        egBillregistermis.setEgBillregister(egBillregister);

        populateBillDetails(egBillregister, voucherHeader);
        expenseBillService.create(egBillregister);
    }

    private void populateBillDetails(final EgBillregister egBillregister, final CVoucherHeader voucherHeader) {
        egBillregister.getEgBilldetailes().clear();
        EgBilldetails details;
        for (final CGeneralLedger gl : voucherHeader.getGeneralLedger()) {
            details = new EgBilldetails();
            details.setEgBillregister(egBillregister);
            details.setLastupdatedtime(new Date());
            details.setDebitamount(BigDecimal.valueOf(gl.getDebitAmount()));
            details.setCreditamount(BigDecimal.valueOf(gl.getCreditAmount()));
            if (gl.getFunctionId() != null)
                details.setFunctionid(BigDecimal.valueOf(gl.getFunctionId()));
            details.setGlcodeid(BigDecimal.valueOf(gl.getGlcodeId().getId()));
            EgBillPayeedetails payeeDetails;
            for (final CGeneralLedgerDetail gld : gl.getGeneralLedgerDetails()) {
                payeeDetails = new EgBillPayeedetails();
                payeeDetails.setAccountDetailTypeId(gld.getDetailTypeId().getId());
                payeeDetails.setAccountDetailKeyId(gld.getDetailKeyId());
                if (details.getDebitamount() != null && details.getDebitamount().compareTo(BigDecimal.ZERO) == 1) {
                    payeeDetails.setDebitAmount(gld.getAmount());
                    payeeDetails.setCreditAmount(BigDecimal.ZERO);
                } else {
                    payeeDetails.setDebitAmount(BigDecimal.ZERO);
                    payeeDetails.setCreditAmount(gld.getAmount());
                }
                details.getEgBillPaydetailes().add(payeeDetails);
            }
            egBillregister.getEgBilldetailes().add(details);

        }
    }

    public void createVoucherHeaderRegisterWorkflowTransition(final CVoucherHeader voucherHeader,
                                                              final Long approvalPosition, final String approvalComent, final String additionalRule,
                                                              final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        if (null != voucherHeader.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(voucherHeader.getCreatedBy().getId());
        if (FinancialConstants.BUTTONREJECT.toString().equalsIgnoreCase(workFlowAction)) {
            final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
            voucherHeader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition())
                    .withNextAction("")
                    .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_VOUCHER_DISPLAYNAME);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix;
            if (null == voucherHeader.getState()) {
                wfmatrix = cvoucherHeaderRegisterWorkflowService.getWfMatrix(voucherHeader.getStateType(), null,
                        null, additionalRule, currState, null);
                voucherHeader.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_VOUCHER_DISPLAYNAME);
            } else if (FinancialConstants.BUTTONAPPROVE.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_APPROVED;
                voucherHeader.setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
                wfmatrix = cvoucherHeaderRegisterWorkflowService.getWfMatrix(voucherHeader.getStateType(), null,
                        null, additionalRule, voucherHeader.getCurrentState().getValue(), null);
                voucherHeader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction("")
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_VOUCHER_DISPLAYNAME);
            } else if (FinancialConstants.BUTTONCANCEL.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_CANCELLED;
                voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
                wfmatrix = cvoucherHeaderRegisterWorkflowService.getWfMatrix(voucherHeader.getStateType(), null,
                        null, additionalRule, voucherHeader.getCurrentState().getValue(), null);
                voucherHeader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction("")
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_VOUCHER_DISPLAYNAME);
            } else {
                wfmatrix = cvoucherHeaderRegisterWorkflowService.getWfMatrix(voucherHeader.getStateType(), null,
                        null, additionalRule, voucherHeader.getCurrentState().getValue(), null);
                voucherHeader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_VOUCHER_DISPLAYNAME);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public Long getApprovalPositionByMatrixDesignation(final CVoucherHeader voucherHeader,
                                                       final String additionalRule, final String mode, final String workFlowAction) {
        Long approvalPosition = null;
        final WorkFlowMatrix wfmatrix = cvoucherHeaderRegisterWorkflowService.getWfMatrix(voucherHeader
                .getStateType(), null, null, additionalRule, voucherHeader.getCurrentState().getValue(), null);
        if (voucherHeader.getState() != null && !voucherHeader.getState().getHistory().isEmpty()
                && voucherHeader.getState().getOwnerPosition() != null)
            approvalPosition = voucherHeader.getState().getOwnerPosition().getId();
        else if (wfmatrix != null)
            approvalPosition = financialUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                    voucherHeader.getState(), voucherHeader.getCreatedBy().getId());
        if (workFlowAction.equals(FinancialConstants.BUTTONCANCEL))
            approvalPosition = null;

        return approvalPosition;
    }

    public void populateVoucherNumber(final CVoucherHeader voucherHeader, final Long moduleId) {

        String vNumGenMode;
        String voucherType = voucherHeader.getType();
        if (FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL
                .equalsIgnoreCase(voucherType))
            vNumGenMode = voucherTypeForULB.readVoucherTypes(FinancialConstants.VOUCHER_TYPE_JOURNAL);
        else
            vNumGenMode = voucherTypeForULB.readVoucherTypes(voucherType);
        voucherType = voucherType.toUpperCase().replaceAll(" ", "");

        String voucherSubType = null;
        if (voucherHeader.getVoucherSubType() != null)
            voucherSubType = voucherHeader.getVoucherSubType().toUpperCase().replaceAll(" ", "");

        if (moduleId != null)
            vNumGenMode = FinancialConstants.AUTO;

        if (FinancialConstants.AUTO.equals(vNumGenMode)) {
            voucherHeader.setVoucherNumberPrefix(getVoucherNumberPrefix(voucherType, voucherSubType));
            final VouchernumberGenerator v = beanResolver.getAutoNumberServiceFor(VouchernumberGenerator.class);

            final String strVoucherNumber = v.getNextNumber(voucherHeader);

            voucherHeader.setVoucherNumber(strVoucherNumber);
        }
    }

    private String getVoucherNumberPrefix(final String type, String vsubtype) {

        // if sub type is null use type
        if (vsubtype == null)
            vsubtype = type;
        final String subtype = vsubtype.toUpperCase().trim();
        String voucherNumberPrefix = null;
        String typetoCheck = subtype;

        if (FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(subtype))
            typetoCheck = JOURNALVOUCHER;

        switch (voucherSubTypeEnum.valueOf(typetoCheck)) {
            case JVGENERAL:
                voucherNumberPrefix = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
                break;
            case JOURNALVOUCHER:
                voucherNumberPrefix = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
                break;
            case CONTRA:
                voucherNumberPrefix = FinancialConstants.CONTRA_VOUCHERNO_TYPE;
                break;
            case RECEIPT:
                voucherNumberPrefix = FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
                break;
            case PAYMENT:
                voucherNumberPrefix = FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
                break;
            case PURCHASEJOURNAL:
                voucherNumberPrefix = FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
                break;
            case WORKS:
                voucherNumberPrefix = FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
                break;
            case CONTRACTORJOURNAL:
                voucherNumberPrefix = FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
                break;
            case WORKSJOURNAL:
                voucherNumberPrefix = FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
                break;
            case FIXEDASSETJOURNAL:
                voucherNumberPrefix = FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
                break;
            case CONTINGENTJOURNAL:
                voucherNumberPrefix = FinancialConstants.CBILL_VOUCHERNO_TYPE;
                break;
            case PURCHASE:
                voucherNumberPrefix = FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
                break;
            case EXPENSEJOURNAL:
                voucherNumberPrefix = FinancialConstants.CBILL_VOUCHERNO_TYPE;
                break;
            case EXPENSE:
                voucherNumberPrefix = FinancialConstants.CBILL_VOUCHERNO_TYPE;
                break;
            case SALARYJOURNAL:
                voucherNumberPrefix = FinancialConstants.SALBILL_VOUCHERNO_TYPE;
                break;
            case SALARY:
                voucherNumberPrefix = FinancialConstants.SALBILL_VOUCHERNO_TYPE;
                break;
            case FIXEDASSET:
                voucherNumberPrefix = FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
                break;
            case PENSIONJOURNAL:
                voucherNumberPrefix = FinancialConstants.PENBILL_VOUCHERNO_TYPE;
                break;
            case PENSION:
                voucherNumberPrefix = FinancialConstants.PENBILL_VOUCHERNO_TYPE;
                break;
            default: // if subtype is invalid then use type
                if (voucherNumberPrefix == null)
                    voucherNumberPrefix = checkWithVoucherType(type);
                break;
        }
        return voucherNumberPrefix;

    }

    private String checkWithVoucherType(final String type) {
        String typetoCheck = type;
        if (FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(type))
            typetoCheck = JOURNALVOUCHER;
        String voucherNumberPrefix = null;
        switch (voucherTypeEnum.valueOf(typetoCheck)) {
            case JOURNALVOUCHER:
                voucherNumberPrefix = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
                break;
            case CONTRA:
                voucherNumberPrefix = FinancialConstants.CONTRA_VOUCHERNO_TYPE;
                break;
            case RECEIPT:
                voucherNumberPrefix = FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
                break;
            case PAYMENT:
                voucherNumberPrefix = FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
                break;
            default:// do nothing
                break;
        }
        return voucherNumberPrefix;

    }

    public void populateCGVNNumber(final CVoucherHeader voucherHeader) {
        String cgvnNumber;

        String sequenceName;

        final CFiscalPeriod fiscalPeriod = fiscalPeriodHibernateDAO.getFiscalPeriodByDate(voucherHeader.getVoucherDate());
        if (fiscalPeriod == null)     throw new ApplicationRuntimeException("Fiscal period is not defined for the voucher date");
        sequenceName = "sq_" + voucherHeader.getFundId().getIdentifier() + "_" + getCgnType(voucherHeader.getType()).toLowerCase()
                + "_cgvn_"
                + fiscalPeriod.getName();
        final Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);

        cgvnNumber = String.format("%s/%s/%s%010d", voucherHeader.getFundId().getIdentifier(),
                getCgnType(voucherHeader.getType()), "CGVN",
                nextSequence);

        voucherHeader.setCgvn(cgvnNumber);
    }

    protected String getCgnType(final String vouType) {
        final String vType = vouType.toUpperCase().replaceAll(" ", "");
        String cgnType = null;
        String typetoCheck = vType;
        if (FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(vType))
            typetoCheck = JOURNALVOUCHER;

        switch (voucherTypeEnum.valueOf(typetoCheck.toUpperCase())) {
            case JOURNALVOUCHER:
                cgnType = "JVG";
                break;
            case CONTRA:
                cgnType = "CSL";
                break;
            case RECEIPT:
                cgnType = "MSR";
                break;
            case PAYMENT:
                cgnType = "DBP";
                break;
            default:// do nothing
                break;
        }
        return cgnType;
    }

}
