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
package org.egov.services.payment;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.ObjectTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.ChequeAssignment;
import org.egov.model.payment.PaymentBean;
import org.egov.model.payment.Paymentheader;
import org.egov.model.recoveries.Recovery;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.services.cheque.ChequeAssignmentService;
import org.egov.services.cheque.ChequeService;
import org.egov.services.instrument.InstrumentHeaderService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.report.FundFlowService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PaymentService extends PersistenceService<Paymentheader, Long> {
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class);
    public SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",
            Constants.LOCALE);
    public final SimpleDateFormat formatter = new SimpleDateFormat(
            "dd/MM/yyyy", Constants.LOCALE);
    private static final String PAYMENTID = "paymentid";
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;
    @Autowired
    private CreateVoucher createVoucher;
    public List<CChartOfAccounts> purchaseBillGlcodeList = new ArrayList<CChartOfAccounts>();
    public List<CChartOfAccounts> worksBillGlcodeList = new ArrayList<CChartOfAccounts>();
    public List<CChartOfAccounts> salaryBillGlcodeList = new ArrayList<CChartOfAccounts>();
    public List<CChartOfAccounts> pensionBillGlcodeList = new ArrayList<CChartOfAccounts>();
    public List<CChartOfAccounts> contingentBillGlcodeList = new ArrayList<CChartOfAccounts>();
    public List<BigDecimal> cBillGlcodeIdList = null;
    protected List<Miscbilldetail> miscBillList = null;
    @Autowired
    private EGovernCommon eGovernCommon;
    private static final String EMPTY_STRING = "";
    private static final String DELIMETER = "~";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving Data";
    private static final String TRANSACTION_FAILED = "Transaction failed";
    private List<HashMap<String, Object>> accountcodedetails = null;
    private List<HashMap<String, Object>> subledgerdetails = null;
    @Autowired
    @Qualifier("instrumentService")
    private InstrumentService instrumentService;
    @Autowired
    @Qualifier("instrumentHeaderService")
    private InstrumentHeaderService instrumentHeaderService;
    @Autowired
    @Qualifier("chequeService")
    private ChequeService chequeService;
    private User user = null;
    private int conBillIdlength = 0;
    public Integer selectedRows = 0;
    private final Date currentDate = new Date();
    private List<InstrumentVoucher> instVoucherList;
    @Autowired
    private EisCommonService eisCommonService;
    private BillsAccountingService billsAccountingService;
    @Autowired
    private EgovCommon egovCommon;
    private FundFlowService fundFlowService;
    private ChequeAssignmentService chequeAssignmentService;
    private VoucherService voucherService;
    private ObjectTypeService objectTypeService;
    @Autowired
    private ChartOfAccountsHibernateDAO coaDAO;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    @Autowired
    @Qualifier("miscbilldetailService")
    private MiscbilldetailService miscbilldetailService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ChartOfAccounts chartOfAccounts;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService;

    public PaymentService(Class<Paymentheader> type) {
        super(type);
    }

    public BigDecimal getAccountBalance(final String accountId,
            final String voucherDate, final BigDecimal amount,
            final Long paymentId, final Long accGlcodeID) throws ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getAccountBalance...");
        egovCommon.setPersistenceService(persistenceService);
        egovCommon.setFundFlowService(fundFlowService);
        egovCommon.setAppConfigValuesService(appConfigValuesService);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getAccountBalance.");
        return egovCommon.getAccountBalance(formatter.parse(voucherDate),
                Long.valueOf(accountId), amount, paymentId, accGlcodeID);
    }

    public boolean isChequeNoGenerationAuto() {
        boolean retVal = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting isChequeNoGenerationAuto...");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey(Constants.EGF,
                        "Cheque_no_generation_auto");
        final String chequeNoGeneration = appList.get(0).getValue();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed isChequeNoGenerationAuto.");
        if (chequeNoGeneration.equalsIgnoreCase("Y"))
            retVal = true;
        else
            retVal = false;
        return retVal;
    }

    public boolean isRtgsNoGenerationAuto() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting isRtgsNoGenerationAuto...");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey(Constants.EGF,
                        "RTGSNO_GENERATION_AUTO");
        final String chequeNoGeneration = appList.get(0).getValue();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed isRtgsNoGenerationAuto.");
        if (chequeNoGeneration.equalsIgnoreCase("Y"))
            return true;
        else
            return false;
    }

    public Paymentheader createPayment(final Map<String, String[]> parameters,
            final HashMap<String, Object> headerdetails,
            final List<HashMap<String, Object>> accountcodedetails,
            final List<HashMap<String, Object>> subledgerdetails,
            final Bankaccount bankaccount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting createPayment...");
        final CVoucherHeader voucherHeader = createVoucher
                .createPreApprovedVoucher(headerdetails, accountcodedetails,
                        subledgerdetails);
        final Paymentheader paymentheader = createPaymentHeader(voucherHeader,
                bankaccount, parameters);
        paymentheader
                .getVoucherheader()
                .getVouchermis()
                .setSourcePath(
                        "/EGF/payment/payment-view.action?paymentid="
                                + paymentheader.getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createPayment.");
        return paymentheader;
    }

    @Transactional
    public Paymentheader createPayment(final Map<String, String[]> parameters,
            final List<PaymentBean> billList,
            final EgBillregister billregister, WorkflowBean workflowBean)
            throws ApplicationRuntimeException, ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting createPayment...");
        Paymentheader paymentheader = null;
        try {
            accountcodedetails = new ArrayList<HashMap<String, Object>>();
            subledgerdetails = new ArrayList<HashMap<String, Object>>();
            conBillIdlength = 0;
            getGlcodeIds();

            final HashMap<String, Object> headerdetails = new HashMap<String, Object>();

            user = (User) persistenceService.find(" from User where id = ?",
                    ApplicationThreadLocals.getUserId());
            if (billList != null && billList.size() > 0
                    && "salary".equalsIgnoreCase(billList.get(0).getExpType()))
                headerdetails.put(VoucherConstant.VOUCHERNAME,
                        "Salary Bill Payment");
            else if (billList != null && billList.size() > 0
                    && "pension".equalsIgnoreCase(billList.get(0).getExpType()))
                headerdetails.put(VoucherConstant.VOUCHERNAME,
                        "Pension Bill Payment");
            else
                headerdetails.put(VoucherConstant.VOUCHERNAME, "Bill Payment");
            headerdetails.put(VoucherConstant.VOUCHERTYPE, "Payment");
            if (parameters.get(VoucherConstant.DESCRIPTION) != null)
                headerdetails.put(VoucherConstant.DESCRIPTION,
                        parameters.get(VoucherConstant.DESCRIPTION)[0]);

            if (parameters.get(VoucherConstant.VOUCHERDATE) != null
                    && !parameters.get(VoucherConstant.VOUCHERDATE)[0]
                            .equals(EMPTY_STRING))
                headerdetails.put(VoucherConstant.VOUCHERDATE, formatter
                        .parse(parameters.get(VoucherConstant.VOUCHERDATE)[0]));

            if (billregister.getEgBillregistermis().getFund() != null)
                headerdetails.put(VoucherConstant.FUNDCODE, billregister
                        .getEgBillregistermis().getFund().getCode());

            if (parameters.get(VoucherConstant.VOUCHERNUMBER) != null)
                headerdetails.put(VoucherConstant.VOUCHERNUMBER,
                        parameters.get(VoucherConstant.VOUCHERNUMBER)[0]);

            if (billregister.getEgBillregistermis().getEgDepartment() != null)
                headerdetails.put(VoucherConstant.DEPARTMENTCODE, billregister
                        .getEgBillregistermis().getEgDepartment().getCode());

            if (billregister.getEgBillregistermis().getFundsource() != null)
                headerdetails.put(VoucherConstant.FUNDSOURCECODE, billregister
                        .getEgBillregistermis().getFundsource().getCode());

            if (billregister.getEgBillregistermis().getScheme() != null)
                headerdetails.put(VoucherConstant.SCHEMECODE, billregister
                        .getEgBillregistermis().getScheme().getCode());

            if (billregister.getEgBillregistermis().getSubScheme() != null)
                headerdetails.put(VoucherConstant.SUBSCHEMECODE, billregister
                        .getEgBillregistermis().getSubScheme().getCode());

            if (billregister.getEgBillregistermis().getFunctionaryid() != null)
                headerdetails.put(VoucherConstant.FUNCTIONARYCODE, billregister
                        .getEgBillregistermis().getFunctionaryid().getCode());

            if (billregister.getEgBillregistermis().getFunction() != null)
                headerdetails.put(VoucherConstant.FUNCTIONCODE, billregister
                        .getEgBillregistermis().getFunction().getCode());

            if (billregister.getEgBillregistermis().getFieldid() != null)
                headerdetails.put(VoucherConstant.DIVISIONID, billregister
                        .getEgBillregistermis().getFieldid().getId());

            final String[] contractorids = parameters.get("contractorIds")[0] == null
                    || parameters.get("contractorIds")[0].equals("") ? null
                            : parameters.get("contractorIds")[0].split(",");
            final String[] supplierids = parameters.get("supplierIds")[0] == null
                    || parameters.get("supplierIds")[0].equals("") ? null
                            : parameters.get("supplierIds")[0].split(",");
            final String[] salaryids = parameters.get("salaryIds")[0] == null
                    || parameters.get("salaryIds")[0].equals("") ? null
                            : parameters.get("salaryIds")[0].split(",");
            final String[] pensionids = parameters.get("pensionIds")[0] == null
                    || parameters.get("pensionIds")[0].equals("") ? null
                            : parameters.get("pensionIds")[0].split(",");
            String[] contingencyIds = null;

            if (parameters.get("contingentIds") != null)
                contingencyIds = parameters.get("contingentIds")[0] == null
                        || parameters.get("contingentIds")[0].equals("") ? null
                                : parameters.get("contingentIds")[0].split(",");

            miscBillList = new ArrayList<Miscbilldetail>();

            prepareVoucherdetails(contractorids, parameters,
                    worksBillGlcodeList, billList);
            /*
             * if (contractorids != null) conBillIdlength = contractorids.length;
             */
            prepareVoucherdetails(supplierids, parameters,
                    purchaseBillGlcodeList, billList);
            prepareVoucherdetails(contingencyIds, parameters,
                    contingentBillGlcodeList, billList);
            prepareVoucherdetails(salaryids, parameters, salaryBillGlcodeList,
                    billList);
            prepareVoucherdetails(pensionids, parameters,
                    pensionBillGlcodeList, billList);

            // credit to the bank glcode
            final HashMap<String, Object> accdetailsMap = new HashMap<String, Object>();
            final Bankaccount ba = (Bankaccount) persistenceService.find(
                    " from Bankaccount where id = ? ",
                    Long.valueOf(parameters.get("bankaccount")[0]));
            accdetailsMap.put(VoucherConstant.GLCODE, ba.getChartofaccounts()
                    .getGlcode());
            accdetailsMap.put(VoucherConstant.NARRATION, ba
                    .getChartofaccounts().getName());
            accdetailsMap.put(VoucherConstant.DEBITAMOUNT, 0);
            accdetailsMap.put(VoucherConstant.CREDITAMOUNT,
                    parameters.get("grandTotal")[0]);
            accountcodedetails.add(accdetailsMap);

            final CVoucherHeader voucherHeader = createVoucher
                    .createPreApprovedVoucher(headerdetails,
                            accountcodedetails, subledgerdetails);
            paymentheader = createPaymentHeader(voucherHeader, ba, parameters);

            entityManager.flush();
            for (final Miscbilldetail miscbilldetail : miscBillList) {
                miscbilldetail.setPayVoucherHeader(voucherHeader);
                miscbilldetailService.create(miscbilldetail);
            }

            paymentheader
                    .getVoucherheader()
                    .getVouchermis()
                    .setSourcePath(
                            "/EGF/payment/payment-view.action?" + PAYMENTID
                                    + "=" + paymentheader.getId());
            if (FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && voucherHeader.getState() == null) {
                paymentheader.getVoucherheader().setStatus(
                        FinancialConstants.CREATEDVOUCHERSTATUS);
            } else {
                paymentheader = transitionWorkFlow(paymentheader, workflowBean);
                applyAuditing(paymentheader.getState());
                applyAuditing(paymentheader);
            }
            update(paymentheader);
            entityManager.flush();
        } catch (final ValidationException e) {

            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("createPayment", e.getErrors()
                    .get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("createPayment", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createPayment.");
        return paymentheader;
    }

    protected Assignment getWorkflowInitiator(final Paymentheader paymentheader) {
        Assignment wfInitiator = assignmentService.findByEmployeeAndGivenDate(
                paymentheader.getCreatedBy().getId(), new Date()).get(0);
        return wfInitiator;
    }

    @Transactional
    public Paymentheader transitionWorkFlow(final Paymentheader paymentheader,
            WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService
                .findByEmployeeAndGivenDate(user.getId(), new Date()).get(0);
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != paymentheader.getId())
            wfInitiator = getWorkflowInitiator(paymentheader);

        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean
                .getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                paymentheader.transition().end()
                        .withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
                paymentheader
                        .transition().progressWithStateCopy()
                        .withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue)
                        .withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition())
                        .withNextAction(
                                FinancialConstants.WF_STATE_EOA_Approval_Pending);
            }

        } else if (FinancialConstants.BUTTONAPPROVE
                .equalsIgnoreCase(workflowBean.getWorkFlowAction())) {

            final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService
                    .getWfMatrix(paymentheader.getStateType(), null, null,
                            null, paymentheader.getCurrentState().getValue(),
                            null);

            paymentheader
                    .transition().end()
                    .withSenderName(user.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(wfmatrix.getCurrentDesignation() + " Approved")
                    .withDateInfo(currentDate.toDate()).withOwner(getPosition())
                    .withNextAction(wfmatrix.getNextAction());

            paymentheader.getVoucherheader().setStatus(
                    FinancialConstants.CREATEDVOUCHERSTATUS);

        } else if (FinancialConstants.BUTTONCANCEL
                .equalsIgnoreCase(workflowBean.getWorkFlowAction())) {

            paymentheader.getVoucherheader().setStatus(
                    FinancialConstants.CANCELLEDVOUCHERSTATUS);
            paymentheader
                    .transition().end()
                    .withStateValue(FinancialConstants.WORKFLOW_STATE_CANCELLED)
                    .withSenderName(user.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else {
            if (null != workflowBean.getApproverPositionId()
                    && workflowBean.getApproverPositionId() != -1)
                pos = (Position) persistenceService.find(
                        "from Position where id=?",
                        workflowBean.getApproverPositionId());
            if (null == paymentheader.getState()) {

                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService
                        .getWfMatrix(paymentheader.getStateType(), null, null,
                                null, workflowBean.getCurrentState(), null);
                paymentheader.transition().start()
                        .withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());

            } else if (paymentheader.getCurrentState().getNextAction()
                    .equalsIgnoreCase("END"))
                paymentheader.transition().progressWithStateCopy().end()
                        .withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                if (!paymentheader.getCurrentState().getValue().equalsIgnoreCase(workflowBean.getCurrentState())) {
                    return paymentheader;
                }
                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService
                        .getWfMatrix(paymentheader.getStateType(), null, null,
                                null, paymentheader.getCurrentState()
                                        .getValue(),
                                null);
                paymentheader.transition().progressWithStateCopy().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }
        }

        return paymentheader;
    }

    @SkipValidation
    public void getPaymentBills(Paymentheader paymentheader) {
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("Inside getPaymentBills");
        try {
            miscBillList = miscbilldetailService
                    .findAllBy(
                            " from Miscbilldetail where payVoucherHeader.id = ? order by paidto",
                            paymentheader.getVoucherheader().getId());
        } catch (final Exception e) {
            throw new ValidationException("",
                    "Total Paid Amount Exceeding Net Amount For This Bill");
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Retrived bill details fro the paymentheader");
    }

    /**
     * Partial payment is not allowed for netpayable subledger practically all bills will have single entity in netpayable Cbill
     * is not allowed to make partial payment
     * 
     * @param ids
     * @param parameters
     * @param glcodeList
     * @param billList
     */
    private void prepareVoucherdetails(final String[] ids,
            final Map<String, String[]> parameters,
            final List<CChartOfAccounts> glcodeList,
            final List<PaymentBean> billList) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareVoucherdetails...");
        EgBillregister egBillregister = null;
        CGeneralLedger gl = null;
        CGeneralLedgerDetail ledgerDetail = null;
        String tmp = "";
        final HashMap<String, BigDecimal> tmpaccdetailsMap = new HashMap<String, BigDecimal>();
        final HashMap<String, BigDecimal> tmpsublegDetailMap = new HashMap<String, BigDecimal>();
        HashMap<String, Object> accdetailsMap = null;
        HashMap<String, Object> sublegDetailMap = null;
        List<PaymentBean> tempBillList = new ArrayList<PaymentBean>();
        if (ids != null && billList != null)
            for (PaymentBean bean : billList)
                for (String billId : ids)
                    if (bean.getBillId().toString().equalsIgnoreCase(billId))
                        tempBillList.add(bean);

        // String disableExp
        // =parameters.get("disableExpenditureType")==null?"false":parameters.get("disableExpenditureType")[0];
        final String changePartyName = parameters.get("changePartyName") == null ? "false"
                : parameters.get("changePartyName")[0];
        final String newPartyName = parameters.get("newPartyName") == null ? ""
                : parameters.get("newPartyName")[0];

        if (ids != null)
            for (int i = 0; i < ids.length; i++) // do the aggregation
            {
                egBillregister = (EgBillregister) persistenceService.find(
                        "from EgBillregister where id = ? ",
                        Long.valueOf(ids[i]));
                if ("true".equalsIgnoreCase(changePartyName))
                    generateMiscBillForSalary(egBillregister,
                            tempBillList.get(i + conBillIdlength)
                                    .getPaymentAmt(),
                            tempBillList.get(i + conBillIdlength).getNetAmt(),
                            newPartyName);
                else
                    generateMiscBill(egBillregister,
                            tempBillList.get(i + conBillIdlength)
                                    .getPaymentAmt(),
                            tempBillList.get(i + conBillIdlength).getNetAmt());
                gl = getPayableAccount(ids[i], glcodeList, "getGeneralLedger");
                if (gl == null)
                    throw new ValidationException(
                            "Voucher is created with invalid netpayble code so payment is not allowed for this bill ",
                            "Voucher is created with invalid netpayble code so payment is not allowed for this bill");
                tmp = gl.getGlcodeId().getGlcode() + DELIMETER
                        + gl.getGlcodeId().getName();
                if (tmpaccdetailsMap.get(tmp) == null)
                    tmpaccdetailsMap.put(tmp,
                            tempBillList.get(i + conBillIdlength)
                                    .getPaymentAmt());
                else
                    tmpaccdetailsMap.put(
                            tmp,
                            tmpaccdetailsMap.get(tmp).add(
                                    tempBillList.get(i + conBillIdlength)
                                            .getPaymentAmt()));
                // if for a ledger row more than one ledgerdetail -then it is
                // having multiple subledger
                // amount is not same as ledger amount then it is partial
                // payment
                // in such condition throw error saying partial payment is not
                // allowed for this
                if (gl.getGeneralLedgerDetails().size() > 1
                        && tempBillList
                                .get(i + conBillIdlength)
                                .getPaymentAmt()
                                .compareTo(
                                        BigDecimal.valueOf(gl.getCreditAmount())) != 0)
                    throw new ValidationException(
                            Arrays.asList(new ValidationError(
                                    "partial.payment.not.allowed.for",
                                    "Partial payment not allowed for "
                                            + tempBillList.get(
                                                    i + conBillIdlength)
                                                    .getBillNumber())));
                // partial payment and multple subledger ends
                final Iterator it = gl.getGeneralLedgerDetails().iterator();
                while (it.hasNext()) {
                    ledgerDetail = (CGeneralLedgerDetail) it.next();
                    if ("Salary".equalsIgnoreCase(tempBillList.get(
                            i + conBillIdlength).getExpType()))
                        tmp = gl.getId() + DELIMETER
                                + gl.getGlcodeId().getGlcode() + DELIMETER
                                + ledgerDetail.getDetailTypeId().getId()
                                + DELIMETER + ledgerDetail.getDetailKeyId();
                    else
                        tmp = gl.getGlcodeId().getGlcode() + DELIMETER
                                + ledgerDetail.getDetailTypeId().getId()
                                + DELIMETER + ledgerDetail.getDetailKeyId();

                    // if(billList.get(i+conBillIdlength).getPaymentAmt().compareTo(val))
                    if (tmpsublegDetailMap.get(tmp) == null) {
                        if (gl.getGeneralLedgerDetails().size() > 1
                                && tempBillList
                                        .get(i + conBillIdlength)
                                        .getPaymentAmt()
                                        .compareTo(
                                                BigDecimal.valueOf(gl
                                                        .getCreditAmount())) == 0)
                            tmpsublegDetailMap.put(tmp,
                                    ledgerDetail.getAmount());
                        else
                            tmpsublegDetailMap.put(tmp,
                                    tempBillList.get(i + conBillIdlength)
                                            .getPaymentAmt());

                    } else if (FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT
                            .equalsIgnoreCase(tempBillList.get(
                                    i + conBillIdlength).getExpType()))
                        tmpsublegDetailMap.put(tmp, tmpsublegDetailMap.get(tmp)
                                .add(ledgerDetail.getAmount()));
                    else if (gl.getGeneralLedgerDetails().size() > 1
                            && tempBillList
                                    .get(i + conBillIdlength)
                                    .getPaymentAmt()
                                    .compareTo(
                                            BigDecimal.valueOf(gl
                                                    .getCreditAmount())) == 0)
                        tmpsublegDetailMap.put(tmp, tmpsublegDetailMap.get(tmp)
                                .add(ledgerDetail.getAmount()));
                    else
                        tmpsublegDetailMap.put(
                                tmp,
                                tmpsublegDetailMap.get(tmp).add(
                                        tempBillList.get(i + conBillIdlength)
                                                .getPaymentAmt()));
                }
            }

        // form the accountdetaillist and subledger list for bills
        Iterator conIterator = tmpaccdetailsMap.keySet().iterator();
        String key = "";

        while (conIterator.hasNext()) {
            key = conIterator.next().toString();
            accdetailsMap = new HashMap<String, Object>();
            accdetailsMap.put(VoucherConstant.GLCODE, key.split(DELIMETER)[0]);
            accdetailsMap.put(VoucherConstant.NARRATION,
                    key.split(DELIMETER)[1]);
            accdetailsMap.put(VoucherConstant.DEBITAMOUNT,
                    tmpaccdetailsMap.get(key));
            accdetailsMap.put(VoucherConstant.CREDITAMOUNT, 0);
            accountcodedetails.add(accdetailsMap);
        }

        conIterator = tmpsublegDetailMap.keySet().iterator();
        while (conIterator.hasNext()) {
            key = conIterator.next().toString();
            sublegDetailMap = new HashMap<String, Object>();
            if (key.split(DELIMETER).length == 4) {
                sublegDetailMap.put(VoucherConstant.GLCODE,
                        key.split(DELIMETER)[1]);
                sublegDetailMap.put(VoucherConstant.DETAILTYPEID,
                        key.split(DELIMETER)[2]);
                sublegDetailMap.put(VoucherConstant.DETAILKEYID,
                        key.split(DELIMETER)[3]);
            } else {
                sublegDetailMap.put(VoucherConstant.GLCODE,
                        key.split(DELIMETER)[0]);
                sublegDetailMap.put(VoucherConstant.DETAILTYPEID,
                        key.split(DELIMETER)[1]);
                sublegDetailMap.put(VoucherConstant.DETAILKEYID,
                        key.split(DELIMETER)[2]);
            }
            sublegDetailMap.put(VoucherConstant.DEBITAMOUNT,
                    tmpsublegDetailMap.get(key));
            sublegDetailMap.put(VoucherConstant.CREDITAMOUNT,
                    BigDecimal.valueOf(0));
            subledgerdetails.add(sublegDetailMap);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareVoucherdetails.");

    }

    protected Paymentheader createPaymentHeader(
            final CVoucherHeader voucherHeader, final Bankaccount ba,
            final Map<String, String[]> parameters) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting createPaymentHeader...");
        final Paymentheader paymentheader = new Paymentheader();
        paymentheader.setType(parameters.get("paymentMode")[0]);
        paymentheader.setVoucherheader(voucherHeader);
        paymentheader.setBankaccount(ba);
        paymentheader.setPaymentAmount(BigDecimal.valueOf(Double
                .valueOf(parameters.get("grandTotal")[0])));
        applyAuditing(paymentheader);
        persist(paymentheader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createPaymentHeader.");
        return paymentheader;
    }

    @Transactional
    protected void generateMiscBill(final EgBillregister egBillregister,
            final BigDecimal paidAmt, final BigDecimal passedAmt) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting generateMiscBill...");
        // check whether full payment is done if done throw error
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Verifying total paid amount generateMiscBill...");
        final BigDecimal sum = (BigDecimal) persistenceService.find(
                "select sum(paidamount) from Miscbilldetail where billVoucherHeader=? and "
                        + "payVoucherHeader.status not in (1,2,4)",
                egBillregister.getEgBillregistermis().getVoucherHeader());
        if (sum != null) {
            egBillregister.getPassedamount().subtract(sum);
            if (egBillregister.getPassedamount().compareTo(sum) <= 0) {
                final String errorMsg = " Canot continue payment as bill "
                        + egBillregister.getBillnumber() + " is fully paid";
                throw new ValidationException(
                        Arrays.asList(new ValidationError(errorMsg, errorMsg)));
            }
        }

        final Miscbilldetail miscbilldetail = new Miscbilldetail();
        miscbilldetail.setBillnumber(egBillregister.getBillnumber());
        miscbilldetail.setBilldate(egBillregister.getBilldate());
        miscbilldetail.setBillamount(egBillregister.getBillamount());
        miscbilldetail.setPassedamount(passedAmt);
        miscbilldetail.setPaidamount(paidAmt);
        miscbilldetail.setPaidby(user);
        miscbilldetail.setPaidto(egBillregister.getEgBillregistermis()
                .getPayto().trim());
        miscbilldetail.setBillVoucherHeader(egBillregister
                .getEgBillregistermis().getVoucherHeader());
        miscBillList.add(miscbilldetail);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed generateMiscBill.");
    }

    protected void generateMiscBillForSalary(
            final EgBillregister egBillregister, final BigDecimal paidAmt,
            final BigDecimal passedAmt, final String newPartyName) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting generateMiscBillForSalary...");
        final Miscbilldetail miscbilldetail = new Miscbilldetail();
        miscbilldetail.setBillnumber(egBillregister.getBillnumber());
        miscbilldetail.setBilldate(egBillregister.getBilldate());
        miscbilldetail.setBillamount(egBillregister.getBillamount());
        miscbilldetail.setPassedamount(passedAmt);
        miscbilldetail.setPaidamount(paidAmt);
        miscbilldetail.setPaidby(user);
        miscbilldetail.setPaidto(newPartyName.trim());
        miscbilldetail.setBillVoucherHeader(egBillregister
                .getEgBillregistermis().getVoucherHeader());
        miscBillList.add(miscbilldetail);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed generateMiscBillForSalary.");
    }

    public Paymentheader updatePayment(final Map<String, String[]> parameters,
            final List<PaymentBean> billList, final Paymentheader payheader)
            throws ApplicationRuntimeException, ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting updatePayment...");
        Paymentheader paymentheader = null;
        try {
            miscBillList = new ArrayList<Miscbilldetail>();
            user = (User) persistenceService.find(" from User where id = ?",
                    ApplicationThreadLocals.getUserId());
            final Bankaccount ba = (Bankaccount) persistenceService.find(
                    "from Bankaccount where id=?", payheader.getBankaccount()
                            .getId());
            paymentheader = (Paymentheader) persistenceService.find(
                    " from Paymentheader where id=? ", payheader.getId());
            deleteMiscBill(paymentheader.getVoucherheader().getId());
            final CVoucherHeader voucher = updateVoucher(parameters, billList,
                    ba, payheader);
            // CVoucherHeader voucher =
            // updateVoucher(parameters,billList,ba,con,payheader);
            // update payment table
            paymentheader.setPaymentAmount(new BigDecimal(parameters
                    .get("grandTotal")[0]));
            paymentheader.setType(payheader.getType());
            paymentheader.setBankaccount(ba);
            paymentheader.setVoucherheader(voucher);
            update(paymentheader);
            for (final Miscbilldetail miscbilldetail : miscBillList) {
                miscbilldetail.setPayVoucherHeader(paymentheader
                        .getVoucherheader());
                miscbilldetailService.create(miscbilldetail);

            }
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("createPayment", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed updatePayment.");
        return paymentheader;
    }

    private CVoucherHeader updateVoucher(
            final Map<String, String[]> parameters,
            final List<PaymentBean> billList, final Bankaccount ba,
            final Paymentheader paymentheader) throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting updateVoucher...");
        final CVoucherHeader existingVH = (CVoucherHeader) persistenceService
                .find(" from CVoucherHeader where id=?", paymentheader
                        .getVoucherheader().getId());
        createVoucher.deleteVoucherdetailAndGL(existingVH);
        updateVoucherHeader(parameters, existingVH,
                paymentheader.getVoucherheader());
        prepareVoucherDetailsForModify(billList, parameters, ba);

        final List<Transaxtion> transactions = createVoucher.createTransaction(
                null, accountcodedetails, subledgerdetails, existingVH);
        getSession().flush();
        Transaxtion txnList[] = new Transaxtion[transactions.size()];
        txnList = transactions.toArray(txnList);
        if (!chartOfAccounts.postTransaxtions(txnList,
                sdf.format(existingVH.getVoucherDate())))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    EXCEPTION_WHILE_SAVING_DATA, TRANSACTION_FAILED)));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed updateVoucher.");
        return existingVH;
    }

    private void updateVoucherHeader(final Map<String, String[]> parameters,
            final CVoucherHeader existingVH, final CVoucherHeader voucherHeader)
            throws Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting updateVoucherHeader...");

        voucherHeader.setFundId(existingVH.getFundId());

        voucherService.getUpdatedVNumCGVN(existingVH, voucherHeader,
                existingVH.getType());

        /*
         * String voucherNumber = VoucherHelper.getGeneratedVoucherNumber(existingVH .getFundId().getId(), autoVoucherType,
         * voucherHeader.getVoucherDate(), vNumGenMode, manualVoucherNumber);
         */
        // existingVH.setVoucherNumber(voucherNumber);
        /*
         * if ("Auto".equalsIgnoreCase(vNumGenMode)) { if(LOGGER.isDebugEnabled()) LOGGER.debug(
         * "Voucher number generation mode is : "+ vNumGenMode); existingVH
         * .setVoucherNumber(cmImpl.getTxnNumber(existingVH.getFundId() .getId().toString(),autoVoucherType,vDate,con)); }else {
         * existingVH.setVoucherNumber (parameters.get("voucherNumberPrefix")[0]+parameters .get("voucherNumberSuffix")[0]); }
         */
        final String vType = existingVH.getVoucherNumber().substring(0,
                Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Voucher type  : " + vType);
        String eg_voucher = eGovernCommon.getEg_Voucher(vType, existingVH
                .getFiscalPeriodId().toString());
        for (int i = eg_voucher.length(); i < 5; i++)
            eg_voucher = "0" + eg_voucher;
        existingVH.setDescription(voucherHeader.getDescription());
        existingVH.setVoucherDate(voucherHeader.getVoucherDate());
        existingVH.setCgvn(vType + eg_voucher);
        existingVH.setLastModifiedDate(new Date());
        existingVH.setLastModifiedBy(user);
        // persistenceService.setType(CVoucherHeader.class);
        persistenceService.update(existingVH);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed updateVoucherHeader.");
    }

    private void prepareVoucherDetailsForModify(
            final List<PaymentBean> paymentBillList,
            final Map<String, String[]> parameters, final Bankaccount ba) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareVoucherDetailsForModify...");
        CGeneralLedger gl = null;
        EgBillregister br = null;
        CGeneralLedgerDetail ledgerDetail = null;
        getGlcodeIds();
        String tmp = "";
        final HashMap<String, BigDecimal> tmpaccdetailsMap = new HashMap<String, BigDecimal>();
        final HashMap<String, BigDecimal> tmpsublegDetailMap = new HashMap<String, BigDecimal>();
        HashMap<String, Object> accdetailsMap = null;
        HashMap<String, Object> sublegDetailMap = null;
        accountcodedetails = new ArrayList<HashMap<String, Object>>();
        subledgerdetails = new ArrayList<HashMap<String, Object>>();
        final String changePartyName = parameters.get("changePartyName") == null ? "false"
                : parameters.get("changePartyName")[0];
        final String newPartyName = parameters.get("newPartyName") == null ? ""
                : parameters.get("newPartyName")[0];

        for (final PaymentBean bean : paymentBillList)
            if (bean != null) {
                br = (EgBillregister) persistenceService
                        .find(" from EgBillregister br where br.egBillregistermis.voucherHeader.id=?",
                                bean.getCsBillId());
                if ("true".equalsIgnoreCase(changePartyName))
                    prepareMiscBillForSalary(bean, br, newPartyName);
                else
                    prepareMiscBill(bean, br);
                if (br.getExpendituretype().equals("Works"))
                    gl = getPayableAccount(bean.getCsBillId().toString(),
                            worksBillGlcodeList,
                            "getGeneralLedgerByVoucherHeaderId");
                else if (br.getExpendituretype().equals("Purchase"))
                    gl = getPayableAccount(bean.getCsBillId().toString(),
                            purchaseBillGlcodeList,
                            "getGeneralLedgerByVoucherHeaderId");
                else
                    gl = getPayableAccount(bean.getCsBillId().toString(),
                            contingentBillGlcodeList,
                            "getGeneralLedgerByVoucherHeaderId");

                tmp = gl.getGlcodeId().getGlcode() + DELIMETER
                        + gl.getGlcodeId().getName();
                if (tmpaccdetailsMap.get(tmp) == null)
                    tmpaccdetailsMap.put(tmp, bean.getPaymentAmt());
                else
                    tmpaccdetailsMap
                            .put(tmp,
                                    tmpaccdetailsMap.get(tmp).add(
                                            bean.getPaymentAmt()));

                final Iterator it = gl.getGeneralLedgerDetails().iterator();
                while (it.hasNext()) {
                    ledgerDetail = (CGeneralLedgerDetail) it.next();

                    tmp = gl.getGlcodeId().getGlcode() + DELIMETER
                            + ledgerDetail.getDetailTypeId() + DELIMETER
                            + ledgerDetail.getDetailKeyId();
                    if (tmpsublegDetailMap.get(tmp) == null)
                        tmpsublegDetailMap.put(tmp, bean.getPaymentAmt());
                    else
                        tmpsublegDetailMap.put(tmp, tmpsublegDetailMap.get(tmp)
                                .add(bean.getPaymentAmt()));
                }
            }

        // form the accountdetaillist and subledger list for bills
        Iterator conIterator = tmpaccdetailsMap.keySet().iterator();
        String key = "";

        while (conIterator.hasNext()) {
            key = conIterator.next().toString();
            accdetailsMap = new HashMap<String, Object>();
            accdetailsMap.put(VoucherConstant.GLCODE, key.split(DELIMETER)[0]);
            accdetailsMap.put(VoucherConstant.NARRATION,
                    key.split(DELIMETER)[1]);
            accdetailsMap.put(VoucherConstant.DEBITAMOUNT,
                    tmpaccdetailsMap.get(key));
            accdetailsMap.put(VoucherConstant.CREDITAMOUNT, 0);
            accountcodedetails.add(accdetailsMap);
        }

        conIterator = tmpsublegDetailMap.keySet().iterator();
        while (conIterator.hasNext()) {
            key = conIterator.next().toString();
            sublegDetailMap = new HashMap<String, Object>();
            sublegDetailMap
                    .put(VoucherConstant.GLCODE, key.split(DELIMETER)[0]);
            sublegDetailMap.put(VoucherConstant.DETAILTYPEID,
                    key.split(DELIMETER)[1]);
            sublegDetailMap.put(VoucherConstant.DETAILKEYID,
                    key.split(DELIMETER)[2]);
            sublegDetailMap.put(VoucherConstant.DEBITAMOUNT,
                    tmpsublegDetailMap.get(key));
            subledgerdetails.add(sublegDetailMap);
        }

        accdetailsMap = new HashMap<String, Object>();
        accdetailsMap.put(VoucherConstant.GLCODE, ba.getChartofaccounts()
                .getGlcode());
        accdetailsMap.put(VoucherConstant.NARRATION, ba.getChartofaccounts()
                .getName());
        accdetailsMap.put(VoucherConstant.DEBITAMOUNT, 0);
        accdetailsMap.put(VoucherConstant.CREDITAMOUNT,
                parameters.get("grandTotal")[0]);
        accountcodedetails.add(accdetailsMap);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareVoucherDetailsForModify.");

    }

    protected void prepareMiscBill(final PaymentBean bean,
            final EgBillregister br) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareMiscBill...");
        final Miscbilldetail miscbilldetail = new Miscbilldetail();
        miscbilldetail.setBillnumber(bean.getBillNumber());
        miscbilldetail.setBilldate(bean.getBillDate());
        miscbilldetail.setBillamount(bean.getNetAmt());
        miscbilldetail.setPassedamount(bean.getPassedAmt());
        miscbilldetail.setPaidamount(bean.getPaymentAmt());
        miscbilldetail.setPaidby(user);
        miscbilldetail.setPaidto(bean.getPayTo());
        miscbilldetail.setBillVoucherHeader(br.getEgBillregistermis()
                .getVoucherHeader());
        miscBillList.add(miscbilldetail);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareMiscBill.");

    }

    protected void prepareMiscBillForSalary(final PaymentBean bean,
            final EgBillregister br, final String newPartyName) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareMiscBillForSalary...");
        final Miscbilldetail miscbilldetail = new Miscbilldetail();
        miscbilldetail.setBillnumber(bean.getBillNumber());
        miscbilldetail.setBilldate(bean.getBillDate());
        miscbilldetail.setBillamount(bean.getNetAmt());
        miscbilldetail.setPassedamount(bean.getPassedAmt());
        miscbilldetail.setPaidamount(bean.getPaymentAmt());
        miscbilldetail.setPaidby(user);
        miscbilldetail.setPaidto(newPartyName);
        miscbilldetail.setBillVoucherHeader(br.getEgBillregistermis()
                .getVoucherHeader());
        miscBillList.add(miscbilldetail);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareMiscBillForSalary.");
    }

    protected void deleteMiscBill(final Long payVHId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting deleteMiscBill...");
        try {
            final Query st = getSession().createSQLQuery(
                    "delete from miscbilldetail where PAYVHID=" + payVHId);
            st.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Inside exception deleteMiscBill" + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed deleteMiscBill.");
    }

    private CGeneralLedger getPayableAccount(final String id,
            final List<CChartOfAccounts> glcodeIdList, final String namedQuery) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getPayableAccount...");
        return (CGeneralLedger) persistenceService.findByNamedQuery(namedQuery,
                Long.valueOf(id), glcodeIdList);
    }

    public void getGlcodeIds() throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getGlcodeIds...");
        try {
            List<AppConfigValues> appList;

            worksBillGlcodeList = populateGlCodeIds(Constants.WORKS_BILL_PURPOSE_IDS);
            purchaseBillGlcodeList = populateGlCodeIds(Constants.PURCHASE_BILL_PURPOSE_IDS);
            salaryBillGlcodeList = populateGlCodeIds("salaryBillPurposeIds");
            pensionBillGlcodeList = populateGlCodeIds(Constants.PENSION_BILL_PURPOSE_IDS);

            // Contingent Bill
            appList = appConfigValuesService.getConfigValuesByModuleAndKey(
                    Constants.EGF, Constants.CONTINGENCY_BILL_PURPOSE_IDS);
            cBillGlcodeIdList = new ArrayList<BigDecimal>();
            if (appList != null && appList.size() > 0) {
                final Integer iPurposeIds[] = new Integer[appList.size()];
                int z = 0;
                for (final AppConfigValues appConfigValues : appList) {
                    iPurposeIds[z] = Integer.parseInt(appConfigValues
                            .getValue());
                    z++;
                }
                final List<CChartOfAccounts> coaList = coaDAO
                        .getAccountCodeByListOfPurposeId(iPurposeIds);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Size contingentBillGlcodeList"
                            + coaList.size());
                contingentBillGlcodeList = coaList;
                for (final CChartOfAccounts coa1 : coaList)
                    // if(LOGGER.isDebugEnabled())
                    // LOGGER.debug("Adding to contingentBillGlcodeList"+coa1.getGlcode()+":::"+coa1.getPurposeId());
                    cBillGlcodeIdList.add(BigDecimal.valueOf(coa1.getId()));
            }
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getGlcodeIds.");
    }

    private List<CChartOfAccounts> populateGlCodeIds(final String appConfigKey)
            throws ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting populateGlCodeIds...");
        final List<CChartOfAccounts> glCodeList = new ArrayList<CChartOfAccounts>();
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey(Constants.EGF, appConfigKey);
        final String purposeids = appList.get(0).getValue();
        if (purposeids != null && !purposeids.equals("")) {
            final String purposeIds[] = purposeids.split(",");
            for (final String purposeId : purposeIds) {
                final List<CChartOfAccounts> coaList = coaDAO
                        .getAccountCodeByPurpose(Integer.parseInt(purposeId));
                for (final CChartOfAccounts coa1 : coaList)
                    glCodeList.add(coa1);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed populateGlCodeIds.");
        return glCodeList;
    }

    public Map<Long, BigDecimal> getDeductionAmt(
            final List<EgBillregister> billList, final String type) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getDeductionAmt...");
        // getGlcodeIds();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Calling getDeductionAmt..................................$$$$$$$$$$$$$$$$$$$$$$ ");
        final Map<String, List<CChartOfAccounts>> glCodeList = new HashMap<String, List<CChartOfAccounts>>();
        glCodeList.put("Works", worksBillGlcodeList);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("Works"+glCodeList.size());
        glCodeList.put("Purchase", purchaseBillGlcodeList);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug(glCodeList.size());
        glCodeList.put(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,
                contingentBillGlcodeList);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug(glCodeList.size());
        glCodeList.put(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,
                salaryBillGlcodeList);
        glCodeList.put(FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION,
                pensionBillGlcodeList);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug(glCodeList.size());
        final Map<Long, BigDecimal> deductionAmtMap = new HashMap<Long, BigDecimal>();
        final List<CChartOfAccounts> list = glCodeList.get(type);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Calling getDeductionAmt..................................$$$$$$$$$$$$$$$$$$$$$$ "
                    + list.size());
        for (final CChartOfAccounts coa : list)
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("#################################"
                        + coa.getGlcode() + ":::::" + coa.getPurposeId());
        populateDeductionData(billList, deductionAmtMap, type,
                glCodeList.get(type));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getDeductionAmt.");
        return deductionAmtMap;
    }

    private void populateDeductionData(final List<EgBillregister> billList,
            final Map<Long, BigDecimal> deductionAmtMap, final String type,
            final List<CChartOfAccounts> glcodeList) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting populateDeductionData...");
        List<Object[]> dedList;
        final List<Long> billIds = new ArrayList<Long>();
        if (billList != null && billList.size() != 0)
            for (final EgBillregister row : billList)
                billIds.add(row.getId());
        if (billList != null && billList.size() != 0) {
            dedList = getDeductionList(type, glcodeList);
            if (dedList != null && dedList.size() != 0)
                for (final Object[] obj : dedList) {
                    final BigInteger id = ((BigInteger) obj[0]);
                    if (billIds.contains(id.longValue()))
                        deductionAmtMap.put(id.longValue(),
                                obj[1] == null ? BigDecimal.ZERO
                                        : (BigDecimal) obj[1]);
                }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed populateDeductionData.");
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> getDeductionList(final String expendituretype,
            final List<CChartOfAccounts> glcodeList) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getDeductionList...");
        List<Object[]> dedList;
        final String mainquery = "select bill.id as id, sum (gl.creditAmount) from eg_Billregister bill,eg_billregistermis billmis left join "
                + "voucherheader vh on vh.id=billmis.voucherheaderid left join (select sum(paidamount) as paidamount,billvhid as billvhid from miscbilldetail misc,voucherheader vh1 where  misc.payvhid=vh1.id and vh1.status not in (1,2,4) group by "
                + "billvhid) misc on misc.billvhid=vh.id,GeneralLedger gl where billmis.voucherheaderid is not null and billmis.billid=bill.id and "
                + "vh.status=0 and bill.expendituretype='"
                + expendituretype
                + "' and gl.voucherHeaderId=billmis.voucherHeaderid and gl.glcodeId not in(:glCodeList) and "
                + "gl.creditAmount>0 and (misc.billvhid is null or (bill.passedamount > misc.paidamount)) group by bill.id";
        dedList = getSession().createSQLQuery(mainquery)
                .setParameterList("glCodeList", glcodeList).list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getDeductionList.");
        return dedList;
    }

    private List<Object[]> getEarlierPaymentAmtList(final String expendituretype) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getEarlierPaymentAmtList...");
        List<Object[]> dedList;
        final String mainquery = "select bill.id as id,misc.paidamount from eg_Billregister bill,eg_billregistermis billmis left join "
                + "voucherheader vh on vh.id=billmis.voucherheaderid left join (select sum(paidamount) as paidamount,billvhid as billvhid from miscbilldetail  misc,voucherheader vh where  misc.payvhid=vh.id and vh.status not in (1,2,4)    group by "
                + "billvhid) misc on misc.billvhid=vh.id where billmis.voucherheaderid is not null and billmis.billid=bill.id and "
                + "vh.status=0 and bill.expendituretype='"
                + expendituretype
                + "' and (bill.passedamount > misc.paidamount)";
        dedList = getSession().createSQLQuery(mainquery).list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getEarlierPaymentAmtList.");
        return dedList;
    }

    public Map<Long, BigDecimal> getEarlierPaymentAmt(
            final List<EgBillregister> billList, final String type) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getEarlierPaymentAmt...");
        final Map<Long, BigDecimal> paymentAmtMap = new HashMap<Long, BigDecimal>();
        List<Object[]> paidList;
        final List<Long> billIds = new ArrayList<Long>();
        if (billList != null && billList.size() != 0)
            for (final EgBillregister row : billList)
                billIds.add(row.getId());
        if (billList != null && billList.size() != 0) {
            paidList = getEarlierPaymentAmtList(type);
            if (paidList != null && paidList.size() != 0)
                for (final Object[] obj : paidList) {
                    final long id = ((BigInteger) obj[0]).longValue();
                    if (billIds.contains(id))
                        paymentAmtMap.put(((BigInteger) obj[0]).longValue(),
                                obj[1] == null ? BigDecimal.ZERO
                                        : (BigDecimal) obj[1]);
                }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getEarlierPaymentAmt.");
        return paymentAmtMap;
    }

    private void validateEntity(final EntityType entity) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateEntity...");
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (StringUtils.isBlank(entity.getPanno())
                || StringUtils.isBlank(entity.getBankname())
                || StringUtils.isBlank(entity.getBankaccount())
                || StringUtils.isBlank(entity.getIfsccode())) {
            LOGGER.error("BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
                    + entity.getName());
            errors.add(new ValidationError(
                    "paymentMode",
                    "BankName, BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
                            + entity.getName()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateEntity.");
    }

    /**
     * if mode is Create - checking with bill id, if mode is modify- checking with billvoucherid
     * 
     * @param bean
     * @param mode
     * @throws ValidationException
     * @throws ApplicationException
     */

    private void validateCBill(final PaymentBean bean, final String mode)
            throws ValidationException, ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateCBill...");
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        EntityType entity = null;
        List<Object[]> list = null;

        // check the payee deails for payable code
        if (mode.equalsIgnoreCase("Create"))
            list = persistenceService.findAllByNamedQuery(
                    "getPayeeDetailsForPayableCode", bean.getBillId(),
                    cBillGlcodeIdList);
        else
            list = persistenceService.findAllByNamedQuery(
                    "getPayeeDetailsForPayableCodeForVoucher",
                    bean.getBillId(), contingentBillGlcodeList);

        if (list == null || list.size() == 0) {
            // check the payeedetails for debit code
            if (mode.equalsIgnoreCase("Create"))
                list = persistenceService.findAllByNamedQuery(
                        "getPayeeDetailsForDebitCode", bean.getBillId());
            else
                list = persistenceService.findAllByNamedQuery(
                        "getPayeeDetailsForDebitCodeForVoucher",
                        bean.getBillId());
            if (list == null || list.size() == 0) {
                LOGGER.error("Sub ledger details are missing for this bill id ->"
                        + bean.getBillId());
                errors.add(new ValidationError("entityType",
                        "Sub ledger details are missing for this bill number : "
                                + bean.getBillNumber()));
                throw new ValidationException(errors);
            } else
                for (final Object[] obj : list) {
                    entity = getEntity(Integer.valueOf(obj[0].toString()),
                            Long.valueOf(obj[1].toString()));
                    validateEntity(entity);
                }
        } else
            for (final Object[] obj : list) {
                entity = getEntity(Integer.valueOf(obj[0].toString()),
                        Long.valueOf(obj[1].toString()));
                validateEntity(entity);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateCBill.");
    }

    public void validateForRTGSPayment(final List<PaymentBean> billList,
            final String type) throws ValidationException, ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateForRTGSPayment...");
        getGlcodeIds();
        EntityType entity = null;
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        Object[] obj = null;
        if (billList != null)
            for (final PaymentBean bean : billList) {
                if (bean == null)
                    continue;

                if (type.equals("Contractor"))
                    obj = (Object[]) persistenceService.findByNamedQuery(
                            "getGlDetailForPayableCode", bean.getBillId(),
                            worksBillGlcodeList);
                else if (type.equals("Supplier"))
                    obj = (Object[]) persistenceService.findByNamedQuery(
                            "getGlDetailForPayableCode", bean.getBillId(),
                            purchaseBillGlcodeList);
                else if (type
                        .equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
                    validateCBill(bean, "Create");

                if (type.equals("Contractor") || type.equals("Supplier")) {
                    if (obj == null) {
                        LOGGER.error("Sub ledger details are missing for this bill id ->"
                                + bean.getBillId());
                        errors.add(new ValidationError("entityType",
                                "Sub ledger details are missing for this bill number : "
                                        + bean.getBillNumber()));
                        throw new ValidationException(errors);
                    }
                    entity = getEntity(Integer.valueOf(obj[0].toString()),
                            (Serializable) obj[1]);

                    if (type.equals("Supplier")
                            && (StringUtils.isBlank(entity.getTinno())
                                    || StringUtils
                                            .isBlank(entity.getBankname())
                                    || StringUtils.isBlank(entity
                                            .getBankaccount())
                                    || StringUtils
                                            .isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError(
                                "paymentMode",
                                "BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                        + entity.getName()));
                        throw new ValidationException(errors);
                    } else
                        validateEntity(entity);
                }
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateForRTGSPayment.");
    }

    public void validateForContractorSupplierDetailCodes(
            final List<PaymentBean> billList, final String type)
            throws ValidationException, ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateForRTGSPayment...");
        getGlcodeIds();
        EntityType entity = null;
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        Object[] obj = null;
        if (billList != null)
            for (final PaymentBean bean : billList) {
                if (bean == null)
                    continue;

                if (type.equals("Contractor"))
                    obj = (Object[]) persistenceService.findByNamedQuery(
                            "getGlDetailForPayableCode", bean.getBillId(),
                            worksBillGlcodeList);
                else if (type.equals("Supplier"))
                    obj = (Object[]) persistenceService.findByNamedQuery(
                            "getGlDetailForPayableCode", bean.getBillId(),
                            purchaseBillGlcodeList);
                else if (type
                        .equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
                    validateCBill(bean, "Create");

                if (type.equals("Contractor") || type.equals("Supplier")) {
                    if (obj == null) {
                        LOGGER.error("Sub ledger details are missing for this bill id ->"
                                + bean.getBillId());
                        errors.add(new ValidationError("entityType",
                                "Sub ledger details are missing for this bill number : "
                                        + bean.getBillNumber()));
                        throw new ValidationException(errors);
                    }
                    entity = getEntity(Integer.valueOf(obj[0].toString()),
                            (Serializable) obj[1]);

                    if (type.equals("Supplier")
                            && (StringUtils.isBlank(entity.getTinno())
                                    || StringUtils
                                            .isBlank(entity.getBankname())
                                    || StringUtils.isBlank(entity
                                            .getBankaccount())
                                    || StringUtils
                                            .isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError(
                                "paymentMode",
                                "BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                        + entity.getName()));
                        throw new ValidationException(errors);
                    } else
                        validateEntity(entity);
                }
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateForRTGSPayment.");
    }

    public void validatePaymentForRTGSAssignment(
            final List<ChequeAssignment> billList, final String type)
            throws ValidationException, ApplicationException {
        Long billId = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateForRTGSPayment...");
        getGlcodeIds();
        EntityType entity = null;
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        Object[] obj = null;
        if (billList != null)
            for (final ChequeAssignment bean : billList) {
                billId = bean.getBillId().longValue();
                if (bean == null)
                    continue;

                if (type.equals("Contractor"))
                    obj = (Object[]) persistenceService.findByNamedQuery(
                            "getGlDetailForPayableCode", billId,
                            worksBillGlcodeList);
                else if (type.equals("Supplier"))
                    obj = (Object[]) persistenceService.findByNamedQuery(
                            "getGlDetailForPayableCode", billId,
                            purchaseBillGlcodeList);
                /*
                 * else if(type.equals(FinancialConstants. STANDARD_EXPENDITURETYPE_CONTINGENT)) { validateCBill(bean,"Create"); }
                 */

                if (type.equals("Contractor") || type.equals("Supplier")) {
                    if (obj == null) {
                        LOGGER.error("Sub ledger details are missing for this bill id ->"
                                + billId);
                        errors.add(new ValidationError("entityType",
                                "Sub ledger details are missing for this bill number : "
                                        + bean.getBillNumber()));
                        throw new ValidationException(errors);
                    }
                    entity = getEntity(Integer.valueOf(obj[0].toString()),
                            (Serializable) obj[1]);

                    if (type.equals("Supplier")
                            && (StringUtils.isBlank(entity.getTinno())
                                    || StringUtils
                                            .isBlank(entity.getBankname())
                                    || StringUtils.isBlank(entity
                                            .getBankaccount())
                                    || StringUtils
                                            .isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError(
                                "paymentMode",
                                "BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Assignment for "
                                        + bean.getVoucherNumber()
                                        + "\\n Party Name " + entity.getName()));
                        throw new ValidationException(errors);
                    } else if (StringUtils.isBlank(entity.getPanno())
                            || StringUtils.isBlank(entity.getBankname())
                            || StringUtils.isBlank(entity.getBankaccount())
                            || StringUtils.isBlank(entity.getIfsccode())) {
                        LOGGER.error("Mandatory details for RTGS Assignment for "
                                + bean.getVoucherNumber()
                                + " for Party Name "
                                + entity.getName() + " is missing missing");
                        errors.add(new ValidationError("paymentMode",
                                "Mandatory details for RTGS Assignment for "
                                        + bean.getVoucherNumber()
                                        + " for Party Name " + entity.getName()
                                        + " is missing missing"));
                        throw new ValidationException(errors);
                    }
                }
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateForRTGSPayment.");
    }

    // Returns true if atleast 1 element in billList is selected
    public boolean isAnyItemSelected(final List<PaymentBean> billList,
            final String paymentMd, final Date restrictedDate)
            throws ValidationException {
        int billDateFlag = 0;
        if (billList != null)
            for (final PaymentBean bean : billList)
                if (bean != null
                        && bean.getBillDate().compareTo(restrictedDate) > 0
                        && !paymentMd.equalsIgnoreCase("RTGS"))
                    billDateFlag++;
        if (billDateFlag > 0)
            return true;
        return false;
    }

    public boolean isRestrictPaymentToOnlyRtgsForContractor() {
        boolean retVal = true;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting isRestrictPaymentToOnlyRtgsForContractor...");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey(Constants.EGF,
                        "RESTRICT_PAYEMENT_TOONLY_RTGS_FOR_CONTRACTOR_CODES");
        final String restrictingPayment = appList.get(0).getValue();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed isRestrictPaymentToOnlyRtgsForContractor.");
        if (restrictingPayment.equalsIgnoreCase("Y"))
            retVal = true;
        else
            retVal = false;
        return retVal;
    }

    /**
     *
     * @param billList
     * @param restrictedDate
     * @return
     * @throws NumberFormatException
     * @throws ApplicationException this api check if the voucher contains subledger on credit side and
     */
    public boolean CheckForContractorSubledgerCodes(
            final List<PaymentBean> billList, final Date restrictedDate)
            throws NumberFormatException, ApplicationException {
        boolean retVal = false;
        int billDateFlag = 0;
        final String query = "Select gld.detailkeyid from generalledger gl,voucherheader vh, generalledgerdetail gld "
                + "where gl.id= gld.generalledgerid "
                + "and vh.id= gl.voucherheaderid "
                + "and gl.creditamount>0 and gld.detailtypeid in "
                + "(select id from accountdetailtype t where t.name in   ('Creditor','contractor') ) "
                + "and vh.vouchernumber=?";
        if (null != billList && !billList.isEmpty())
            for (final PaymentBean bean : billList)
                if (bean != null) {
                    final SQLQuery createSQLQuery = getSession()
                            .createSQLQuery(query);
                    createSQLQuery.setString(0, bean.getBillVoucherNumber());
                    if (createSQLQuery.list().size() > 0
                            && bean.getBillDate().compareTo(restrictedDate) > 0) {
                        billDateFlag++;
                        break;

                    }
                }
        if (billDateFlag > 0)
            retVal = true;
        return retVal;
    }

    public void validateRTGSPaymentForModify(final List<PaymentBean> billList)
            throws ValidationException, ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting validateRTGSPaymentForModify...");
        getGlcodeIds();
        EntityType entity = null;
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (billList != null)
            for (final PaymentBean bean : billList) {
                if (bean == null)
                    continue;
                final Object[] obj = (Object[]) persistenceService
                        .find("select gld.detailTypeId.id,gld.detailKeyId,billmis.egBillregister.expendituretype from CGeneralLedgerDetail gld,CGeneralLedger gl,EgBillregistermis billmis where gl.id=gld.generalLedgerId.id and billmis.voucherHeader = gl.voucherHeaderId and billmis.voucherHeader.id=?",
                                bean.getCsBillId());
                if (obj == null) {
                    LOGGER.error("Sub ledger details are missing for this bill number ->"
                            + bean.getBillNumber());
                    errors.add(new ValidationError("entityType",
                            "Sub ledger details are missing for this bill number->"
                                    + bean.getBillNumber()));
                    throw new ValidationException(errors);
                }

                if (obj[2]
                        .equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
                    validateCBill(bean, "Modify");
                else {
                    entity = getEntity(Integer.valueOf(obj[0].toString()),
                            (Serializable) obj[1]);
                    if (obj[2].equals("Works")
                            && (StringUtils.isBlank(entity.getPanno())
                                    || StringUtils
                                            .isBlank(entity.getBankname())
                                    || StringUtils.isBlank(entity
                                            .getBankaccount())
                                    || StringUtils
                                            .isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError(
                                "paymentMode",
                                "BankName, BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
                                        + entity.getName()));
                        throw new ValidationException(errors);
                    }
                    if (obj[2].equals("Purchase")
                            && (StringUtils.isBlank(entity.getTinno())
                                    || StringUtils
                                            .isBlank(entity.getBankname())
                                    || StringUtils.isBlank(entity
                                            .getBankaccount())
                                    || StringUtils
                                            .isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError(
                                "paymentMode",
                                "BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                        + entity.getName()));
                        throw new ValidationException(errors);
                    }
                }
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed validateRTGSPaymentForModify.");
    }

    public EntityType getEntity(final Integer detailTypeId,
            final Serializable detailKeyId) throws ApplicationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getEntity...");
        EntityType entity;
        try {
            final Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService
                    .find(" from Accountdetailtype where id=?", detailTypeId);
            final Class<?> service = Class.forName(accountdetailtype
                    .getFullQualifiedName());
            final String detailTypeName = service.getSimpleName();
            String dataType = "";
            final java.lang.reflect.Method method = service.getMethod("getId");
            dataType = method.getReturnType().getSimpleName();
            if (dataType.equals("Long"))
                entity = (EntityType) persistenceService.find("from "
                        + detailTypeName + " where id=? order by name",
                        Long.valueOf(detailKeyId + ""));
            else
                entity = (EntityType) persistenceService.find("from "
                        + detailTypeName + " where id=? order by name",
                        Integer.valueOf(detailKeyId + ""));

        } catch (final Exception e) {
            LOGGER.error("Exception to get EntityType=" + e.getMessage());
            throw new ApplicationException("Exception to get EntityType="
                    + e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getEntity.");
        return entity;
    }

    public List<PaymentBean> getMiscBillList(final Paymentheader header) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getMiscBillList...");
        List<PaymentBean> paymentBeanList = null;
        final Query query = getSession()
                .createSQLQuery(
                        "select mb.billvhId as billId,mb.billnumber as billNumber,mb.billdate as billDate,mb.paidto as payTo,mb.amount as netAmt,  "
                                + " mb.passedamount as passedAmt,mb.paidamount as paymentAmt,br.expendituretype as expType from miscbilldetail mb, eg_billregister br , eg_billregistermis mis "
                                + " where mb.payvhid="
                                + header.getVoucherheader().getId()
                                + " and br.id= mis.billid and mis.voucherheaderid=billvhid order by mb.paidto,mb.BILLDATE")
                .addScalar("billId", BigDecimalType.INSTANCE)
                .addScalar("billNumber")
                .addScalar("billDate")
                .addScalar("payTo")
                .addScalar("netAmt", BigDecimalType.INSTANCE)
                .addScalar("passedAmt", BigDecimalType.INSTANCE)
                .addScalar("paymentAmt", BigDecimalType.INSTANCE)
                .addScalar("expType")
                .setResultTransformer(
                        Transformers.aliasToBean(PaymentBean.class));
        paymentBeanList = query.list();
        BigDecimal earlierAmt;
        for (final PaymentBean bean : paymentBeanList) {
            bean.setIsSelected(true);
            earlierAmt = (BigDecimal) persistenceService.find(
                    " select sum(paidamount) from Miscbilldetail where billVoucherHeader.id=?"
                            + " and payVoucherHeader.status not in(?,?)",
                    bean.getCsBillId(),
                    FinancialConstants.CANCELLEDVOUCHERSTATUS,
                    FinancialConstants.REVERSEDVOUCHERSTATUS);
            if (earlierAmt == null)
                earlierAmt = BigDecimal.ZERO;
            bean.setEarlierPaymentAmt(earlierAmt.subtract(bean.getPaymentAmt()));
            bean.setPayableAmt(bean.getNetAmt().subtract(
                    bean.getEarlierPaymentAmt()));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getMiscBillList.");
        return paymentBeanList;
    }

    // this will be used for all paymentVouchers
    // List<ChequeAssignment> chequeList = null;
    public List<ChequeAssignment> getPaymentVoucherForRTGSInstrument(
            final Map<String, String[]> parameters,
            final CVoucherHeader voucherHeader) throws ApplicationException,
            ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPaymentVoucherNotInInstrument...");
        List<ChequeAssignment> chequeAssignmentList = new ArrayList<ChequeAssignment>();

        final StringBuffer sql = new StringBuffer();
        /*
         * EgBillSubType egSubType = new EgBillSubType(); egSubType = (EgBillSubType) persistenceService.find(
         * " from EgBillSubType where name = ?", FinancialConstants.BILLSUBTYPE_TNEBBILL); if (egSubType.getId() != null)
         * sql.append(" and (bmis.billsubtype is null or bmis.billsubtype not in (" + egSubType.getId() + "))");
         */if (!"".equals(parameters.get("fromDate")[0]))
            sql.append(" and vh.voucherDate>='"
                    + sdf.format(formatter.parse(parameters.get("fromDate")[0]))
                    + "' ");
        if (!"".equals(parameters.get("toDate")[0]))
            sql.append(" and vh.voucherDate<='"
                    + sdf.format(formatter.parse(parameters.get("toDate")[0]))
                    + "'");
        if (!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
            sql.append(" and vh.voucherNumber like '%"
                    + voucherHeader.getVoucherNumber() + "%'");
        if (voucherHeader.getFundId() != null)
            sql.append(" and vh.fundId=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            sql.append(" and vmis.fundsourceId="
                    + voucherHeader.getVouchermis().getFundsource().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and vmis.departmentid="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vmis.schemeid="
                    + voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and vmis.subschemeid="
                    + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and vmis.functionaryid="
                    + voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and vmis.divisionid="
                    + voucherHeader.getVouchermis().getDivisionid().getId());
        if (parameters.get("bankaccount") != null
                && !parameters.get("bankaccount")[0].equals("-1")) {
            sql.append(" and ph.bankaccountnumberid="
                    + parameters.get("bankaccount")[0]);
            sql.append(" and lower(ph.type)=lower('"
                    + parameters.get("paymentMode")[0] + "')");
            sql.append(" and ph.bankaccountnumberid=ba.id");
        } else
            sql.append(" and ph.bankaccountnumberid=ba.id").append(
                    " and lower(ph.type)=lower('"
                            + parameters.get("paymentMode")[0] + "')");
        sql.append(" and vmis.departmentid     =dept.id  ");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");
        final String approvedstatus = appList.get(0).getValue();
        final List<String> descriptionList = new ArrayList<String>();
        descriptionList.add("New");
        descriptionList.add("Reconciled");
        final List<EgwStatus> egwStatusList = egwStatusDAO
                .getStatusListByModuleAndCodeList("Instrument", descriptionList);
        String statusId = "";
        for (final EgwStatus egwStatus : egwStatusList)
            statusId = statusId + egwStatus.getId() + ",";
        statusId = statusId.substring(0, statusId.length() - 1);

        persistenceService.find(" from Bankaccount where id=?",
                Long.valueOf(parameters.get("bankaccount")[0]));
        Query query = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("statusId -- > " + statusId);

        chequeList = new ArrayList<ChequeAssignment>();

        if (voucherHeader.getName() == null
                || !voucherHeader.getName().equalsIgnoreCase(
                        FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) { // /
                                                                              // Only
                                                                              // for
                                                                              // bill
                                                                              // payment
                                                                              // screen
            query = getSession()
                    .createSQLQuery(
                            " select  vh.id as voucherid ,vh.voucherNumber as voucherNumber ,"
                                    + " dept.name   AS departmentName, vh.voucherDate as voucherDate,"
                                    + " misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,current_date as chequeDate"
                                    + " , ba.accountnumber   AS bankAccNumber, ba.id  AS bankAccountId ,"
                                    + " bill.id                 as billId, bill.billnumber       as billNumber ,bill.expenditureType as expenditureType"
                                    + " from Paymentheader ph, eg_department dept,"
                                    + " bankaccount ba, voucherheader vh   LEFT JOIN "
                                    + " EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,"
                                    + " vouchermis vmis, Miscbilldetail misbill ,eg_billregistermis bmis, eg_billregister bill "
                                    + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id "
                                    + " and vh.status ="
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " and bmis.voucherheaderid=misbill.billvhid     and bmis.billid=bill.Id"
                                    + " and  IV.VOUCHERHEADERID IS NULL  and vh.type='"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' "
                                    + " and vh.name NOT IN ('"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "' , '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                    + "','"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                    + "') "
                                    + " group by vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,misbill.paidto, "
                                    + " ba.accountnumber, ba.id , bill.id, bill.billnumber,bill.expenditureType "
                                    + " order by ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("billId", LongType.INSTANCE)
                    .addScalar("billNumber")
                    .addScalar("expenditureType")
                    .setResultTransformer(
                            Transformers.aliasToBean(ChequeAssignment.class));

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" for non salary and remittance" + query);
            LOGGER.info(" for non salary and remittance" + query);
            chequeAssignmentList = query.list();
            // below one handles
            // assign-->surrendar-->assign-->surrendar-->.......
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("checking  cheque assigned and surrendard");
            query = getSession()
                    .createSQLQuery(
                            "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,"
                                    + " dept.name   AS departmentName, vh.voucherDate as voucherDate, misbill.paidto as         paidTo"
                                    + ",sum(misbill.paidamount) as paidAmount,current_date as chequeDate , ba.accountnumber AS bankAccNumber "
                                    + " , ba.id  AS bankAccountId , "
                                    + " bill.id    as billId, bill.billnumber as billNumber ,bill.expenditureType as expenditureType"
                                    + " from Paymentheader ph,eg_department dept, bankaccount ba,eg_billregistermis bmis, "
                                    + " eg_billregister bill ,voucherheader vh   LEFT "
                                    + " JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH "
                                    + " ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill "
                                    + ",(select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) as table1"
                                    + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id "
                                    + " and vh.status ="
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " and bmis.voucherheaderid=misbill.billvhid     and bmis.billid=bill.Id "
                                    + " and  IV.VOUCHERHEADERID IS NOT  NULL and iv.instrumentheaderid=table1.maxihid and  table1.iv1vhid=vh.id and ih.id_status not in ("
                                    + statusId
                                    + ") and vh.type='"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' and vh.name NOT IN ('"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "' , '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                    + "', '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                    + "') "
                                    + " group by   vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,misbill.paidto,ba.accountnumber,"
                                    + " ba.id , bill.id, bill.billnumber ,bill.expenditureType order by ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("billId", LongType.INSTANCE)
                    .addScalar("billNumber")
                    .addScalar("expenditureType")
                    .setResultTransformer(
                            Transformers.aliasToBean(ChequeAssignment.class));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" Surrendered rtgs nos" + query);
            LOGGER.info(" Surrendered rtgs nos" + query);
            chequeAssignmentList.addAll(query.list());

        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPaymentVoucherNotInInstrument.");
        return chequeAssignmentList;
    }

    public List<ChequeAssignment> getPaymentVoucherForTNEBRTGSInstrument(
            final Map<String, String[]> parameters,
            final CVoucherHeader voucherHeader) throws ApplicationException,
            ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPaymentVoucherNotInInstrument...");
        List<ChequeAssignment> chequeAssignmentList = new ArrayList<ChequeAssignment>();

        final StringBuffer sql = new StringBuffer();
        final StringBuffer TNEBsql = new StringBuffer();
        EgBillSubType egSubType = new EgBillSubType();
        egSubType = (EgBillSubType) persistenceService.find(
                " from EgBillSubType where name = ?",
                FinancialConstants.BILLSUBTYPE_TNEBBILL);
        if (egSubType.getId() != null)
            TNEBsql.append(" bmis.billsubtype = " + egSubType.getId() + "");
        if (parameters.get("region")[0] != null
                && !parameters.get("region")[0].equalsIgnoreCase(""))
            TNEBsql.append(" and ebd.region = '" + parameters.get("region")[0]
                    + "'");
        if (!"".equals(parameters.get("fromDate")[0]))
            sql.append(" and vh.voucherDate>='"
                    + sdf.format(formatter.parse(parameters.get("fromDate")[0]))
                    + "' ");
        if (!"".equals(parameters.get("toDate")[0]))
            sql.append(" and vh.voucherDate<='"
                    + sdf.format(formatter.parse(parameters.get("toDate")[0]))
                    + "'");
        if (!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
            sql.append(" and vh.voucherNumber like '%"
                    + voucherHeader.getVoucherNumber() + "%'");
        if (voucherHeader.getFundId() != null)
            sql.append(" and vh.fundId=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            sql.append(" and vmis.fundsourceId="
                    + voucherHeader.getVouchermis().getFundsource().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and vmis.departmentid="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vmis.schemeid="
                    + voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and vmis.subschemeid="
                    + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and vmis.functionaryid="
                    + voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and vmis.divisionid="
                    + voucherHeader.getVouchermis().getDivisionid().getId());
        if (parameters.get("bankaccount") != null
                && !parameters.get("bankaccount")[0].equals("-1")) {
            sql.append(" and ph.bankaccountnumberid="
                    + parameters.get("bankaccount")[0]);
            sql.append(" and lower(ph.type)=lower('"
                    + parameters.get("paymentMode")[0] + "')");
            sql.append(" and ph.bankaccountnumberid=ba.id");
        } else
            sql.append(" and ph.bankaccountnumberid=ba.id").append(
                    " and lower(ph.type)=lower('"
                            + parameters.get("paymentMode")[0] + "')");
        sql.append(" and vmis.departmentid     =dept.id  ");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");
        final String approvedstatus = appList.get(0).getValue();
        final List<String> descriptionList = new ArrayList<String>();
        descriptionList.add("New");
        descriptionList.add("Reconciled");
        final List<EgwStatus> egwStatusList = egwStatusDAO
                .getStatusListByModuleAndCodeList("Instrument", descriptionList);
        String statusId = "";
        for (final EgwStatus egwStatus : egwStatusList)
            statusId = statusId + egwStatus.getId() + ",";
        statusId = statusId.substring(0, statusId.length() - 1);

        persistenceService.find(" from Bankaccount where id=?",
                Long.valueOf(parameters.get("bankaccount")[0]));
        String payTo = null;
        try {
            final List<AppConfigValues> configValues = appConfigValuesService
                    .getConfigValuesByModuleAndKey(
                            FinancialConstants.MODULE_NAME_APPCONFIG,
                            FinancialConstants.EB_VOUCHER_PROPERTY_BANKBRANCH);

            for (final AppConfigValues appConfigVal : configValues)
                payTo = appConfigVal.getValue();
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(
                    "Appconfig value for EB Voucher propartys is not defined in the system");
        }
        if (payTo != null)
            payTo = payTo.substring(0, 20);
        Query query = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("statusId -- > " + statusId);

        chequeList = new ArrayList<ChequeAssignment>();

        if (voucherHeader.getName() == null
                || !voucherHeader.getName().equalsIgnoreCase(
                        FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) { // /
                                                                              // Only
                                                                              // for
                                                                              // bill
                                                                              // payment
                                                                              // screen
            query = getSession()
                    .createSQLQuery(
                            " SELECT vh.id AS voucherid , vh.voucherNumber AS voucherNumber , dept.name   AS departmentName, "
                                    + " vh.voucherDate AS voucherDate, '"
                                    + payTo
                                    + "' AS paidTo , ph.paymentamount AS paidAmount, current_date AS chequeDate ,ba.accountnumber AS bankAccNumber ,"
                                    + " ba.id AS bankAccountId FROM paymentheader ph , eg_department dept, bankaccount ba, voucherheader vh LEFT JOIN EGF_INSTRUMENTVOUCHER IV "
                                    + " ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis "
                                    + " WHERE ph.voucherheaderid IN ( SELECT DISTINCT misbill.payvhid "
                                    + " FROM egf_ebdetails ebd , eg_billregistermis bmis, eg_billregister bill , Miscbilldetail misbill WHERE  bill.id = ebd.billid "
                                    + " AND bmis.billid = bill.id AND  "
                                    + TNEBsql
                                    + " AND bmis.voucherheaderid = misbill.billvhid ) AND ph.voucherheaderid = vh.id "
                                    + " AND vmis.voucherheaderid  = vh.id AND vh.status = "
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " AND ph.bankaccountnumberid=ba.id AND vmis.departmentid = dept.id AND IV.VOUCHERHEADERID IS NULL AND "
                                    + " vh.type = '"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' AND vh.name NOT IN "
                                    + " ('"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "' , '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                    + "', '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                    + "') "
                                    + " GROUP BY vh.id,vh.voucherNumber,dept.name , vh.voucherDate, ba.accountnumber, ba.id , ph.paymentamount ORDER BY ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo", StringType.INSTANCE)
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .setResultTransformer(
                            Transformers.aliasToBean(ChequeAssignment.class));

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" for non salary and remittance" + query);
            LOGGER.info(" for non salary and remittance" + query);
            chequeAssignmentList = query.list();
            // below one handles
            // assign-->surrendar-->assign-->surrendar-->.......
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("checking  cheque assigned and surrendard");
            query = getSession()
                    .createSQLQuery(
                            " SELECT vh.id AS voucherid , vh.voucherNumber AS voucherNumber , dept.name   AS departmentName, "
                                    + " vh.voucherDate AS voucherDate, '"
                                    + payTo
                                    + "' AS paidTo , ph.paymentamount AS paidAmount, current_date AS chequeDate ,ba.accountnumber AS bankAccNumber ,"
                                    + " ba.id AS bankAccountId FROM paymentheader ph , eg_department dept, bankaccount ba, voucherheader vh LEFT JOIN EGF_INSTRUMENTVOUCHER IV "
                                    + " ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis,(SELECT MAX(iv1.instrumentheaderid) AS maxihid,"
                                    + " iv1.voucherheaderid AS iv1vhid FROM egf_instrumentvoucher iv1 GROUP BY iv1.voucherheaderid ) as table1 WHERE ph.voucherheaderid IN ( SELECT DISTINCT misbill.payvhid "
                                    + " FROM egf_ebdetails ebd , eg_billregistermis bmis, eg_billregister bill , Miscbilldetail misbill WHERE bill.id = ebd.billid "
                                    + " AND bmis.billid = bill.id AND "
                                    + TNEBsql
                                    + " AND bmis.voucherheaderid = misbill.billvhid ) AND ph.voucherheaderid = vh.id "
                                    + " AND vmis.voucherheaderid  = vh.id AND vh.status = "
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " AND ph.bankaccountnumberid=ba.id AND vmis.departmentid = dept.id AND IV.VOUCHERHEADERID IS NOT NULL AND iv.instrumentheaderid = table1.maxihid AND table1.iv1vhid = vh.id AND "
                                    + " ih.id_status NOT IN ("
                                    + statusId
                                    + ") AND vh.type = '"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' AND vh.name NOT IN "
                                    + " ('"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "' , '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                    + "', '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                    + "') "
                                    + " GROUP BY vh.id,vh.voucherNumber,dept.name , vh.voucherDate, ba.accountnumber, ba.id , ph.paymentamount ORDER BY ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo", StringType.INSTANCE)
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .setResultTransformer(
                            Transformers.aliasToBean(ChequeAssignment.class));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" Surrendered rtgs nos" + query);
            LOGGER.info(" Surrendered rtgs nos" + query);
            chequeAssignmentList.addAll(query.list());

        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPaymentVoucherNotInInstrument.");
        return chequeAssignmentList;
    }

    public List<ChequeAssignment> getDirectBankPaymentVoucherForRTGSInstrument(
            final Map<String, String[]> parameters,
            final CVoucherHeader voucherHeader) throws ApplicationException,
            ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPaymentVoucherNotInInstrument...");
        final List<ChequeAssignment> chequeAssignmentList = new ArrayList<ChequeAssignment>();

        final StringBuffer sql = new StringBuffer();
        if (!"".equals(parameters.get("fromDate")[0]))
            sql.append(" and vh.voucherDate>='"
                    + sdf.format(formatter.parse(parameters.get("fromDate")[0]))
                    + "' ");
        if (!"".equals(parameters.get("toDate")[0]))
            sql.append(" and vh.voucherDate<='"
                    + sdf.format(formatter.parse(parameters.get("toDate")[0]))
                    + "'");
        if (!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
            sql.append(" and vh.voucherNumber like '%"
                    + voucherHeader.getVoucherNumber() + "%'");
        if (voucherHeader.getFundId() != null)
            sql.append(" and vh.fundId=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            sql.append(" and vmis.fundsourceId="
                    + voucherHeader.getVouchermis().getFundsource().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and vmis.departmentid="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vmis.schemeid="
                    + voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and vmis.subschemeid="
                    + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and vmis.functionaryid="
                    + voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and vmis.divisionid="
                    + voucherHeader.getVouchermis().getDivisionid().getId());
        if (parameters.get("bankaccount") != null
                && !parameters.get("bankaccount")[0].equals("-1")) {
            sql.append(" and ph.bankaccountnumberid="
                    + parameters.get("bankaccount")[0]);
            sql.append(" and lower(ph.type)=lower('"
                    + parameters.get("paymentMode")[0] + "')");
            sql.append(" and ph.bankaccountnumberid=ba.id");
        } else
            sql.append(" and ph.bankaccountnumberid=ba.id").append(
                    " and lower(ph.type)=lower('"
                            + parameters.get("paymentMode")[0] + "')");
        sql.append(" and vmis.departmentid     =dept.id  ");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");
        final String approvedstatus = appList.get(0).getValue();
        final List<String> descriptionList = new ArrayList<String>();
        descriptionList.add("New");
        descriptionList.add("Reconciled");
        final List<EgwStatus> egwStatusList = egwStatusDAO
                .getStatusListByModuleAndCodeList("Instrument", descriptionList);
        String statusId = "";
        for (final EgwStatus egwStatus : egwStatusList)
            statusId = statusId + egwStatus.getId() + ",";
        statusId = statusId.substring(0, statusId.length() - 1);

        persistenceService.find(" from Bankaccount where id=?",
                Long.valueOf(parameters.get("bankaccount")[0]));
        Query query = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("statusId -- > " + statusId);

        chequeList = new ArrayList<ChequeAssignment>();

        if (voucherHeader.getName() == null
                || !voucherHeader.getName().equalsIgnoreCase(
                        FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) { // /
                                                                              // Only
                                                                              // for
                                                                              // bill
                                                                              // payment
                                                                              // screen
            query = getSession()
                    .createSQLQuery(
                            " SELECT vh.id   AS voucherid , vh.voucherNumber        AS voucherNumber ,dept.name          AS departmentName,"
                                    + "  vh.voucherDate  AS voucherDate , misbill.paidto  AS paidTo, SUM(misbill.paidamount) AS paidAmount,current_date AS chequeDate,"
                                    + "   ba.accountnumber  AS bankAccNumber, ba.id  AS bankAccountId ,vh.name     AS expenditureType    FROM Paymentheader ph,"
                                    + "  voucherheader vh,  vouchermis vmis,  Miscbilldetail misbill,  eg_department dept,  bankaccount ba,  egf_instrumentvoucher iv RIGHT OUTER JOIN voucherheader pvh"
                                    + " ON (pvh.id    =iv.VOUCHERHEADERID) WHERE ph.voucherheaderid  =misbill.payvhid AND ph.voucherheaderid    =vh.id AND vh.name  ='"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK
                                    + "'"
                                    + " AND vmis.voucherheaderid  = vh.id  AND vh.status   ="
                                    + approvedstatus
                                    + sql
                                    + " AND pvh.id      =vh.id AND iv.id     IS NULL"
                                    + " AND dept.id  = vmis.departmentid AND ph.bankaccountnumberid= ba.id GROUP BY vh.id,   vh.voucherNumber,  dept.name , "
                                    + " vh.voucherDate,  misbill.paidto,ba.accountnumber,  ba.id,vh.name"
                                    + " UNION SELECT vh.id  AS voucherid ,vh.voucherNumber AS voucherNumber ,dept.name AS departmentName,vh.voucherDate          AS voucherDate ,"
                                    + "  misbill.paidto          AS paidTo, SUM(misbill.paidamount) AS paidAmount, current_date   AS chequeDate,ba.accountnumber  AS bankAccNumber,"
                                    + " ba.id   AS bankAccountId, vh.name  AS expenditureType FROM Paymentheader ph,voucherheader vh,vouchermis vmis,eg_department dept,bankaccount ba,"
                                    + " Miscbilldetail misbill, egf_instrumentvoucher iv RIGHT OUTER JOIN voucherheader pvh ON (pvh.id=iv.VOUCHERHEADERID) LEFT OUTER JOIN egf_instrumentheader ih"
                                    + " ON (ih.ID   =iv.INSTRUMENTHEADERID) WHERE ph.voucherheaderid  =misbill.payvhid AND ph.voucherheaderid    =vh.id AND vh.name ='"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK
                                    + "'"
                                    + " AND vmis.voucherheaderid  = vh.id AND vh.status  ="
                                    + approvedstatus
                                    + sql
                                    + " AND pvh.id   =vh.id AND dept.id          = vmis.departmentid"
                                    + " AND ph.bankaccountnumberid= ba.id AND ih.id IN  (SELECT MAX(ih.id)  FROM egf_instrumentvoucher iv  RIGHT OUTER JOIN voucherheader pvh"
                                    + " ON (pvh.id=iv.VOUCHERHEADERID) LEFT OUTER JOIN egf_instrumentheader ih   ON (ih.ID    =iv.INSTRUMENTHEADERID)  WHERE pvh.id =vh.id   "
                                    + "  "
                                    + " ) and ih.id_status not in ("
                                    + statusId
                                    + ") GROUP BY vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,  misbill.paidto,  ba.accountnumber,  ba.id,  vh.name                                           "
                                    + " order by bankAccountId, departmentName,  voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("expenditureType")
                    .setResultTransformer(
                            Transformers.aliasToBean(ChequeAssignment.class));

            chequeAssignmentList.addAll(query.list());

        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPaymentVoucherNotInInstrument.");
        return chequeAssignmentList;
    }

    // this will be used for all paymentVouchers
    private List<ChequeAssignment> chequeList = null;

    public List<ChequeAssignment> getPaymentVoucherNotInInstrument(
            final Map<String, String[]> parameters,
            final CVoucherHeader voucherHeader) throws ApplicationException,
            ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPaymentVoucherNotInInstrument...");
        List<ChequeAssignment> chequeAssignmentList = new ArrayList<ChequeAssignment>();
        if (parameters.get("paymentMode")[0]
                .equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
            final String billType = parameters.get("billType")[0];
            chequeAssignmentService.setStatusAndFilterValues(parameters,
                    voucherHeader);
            if (voucherHeader.getName() != null)
                if (voucherHeader.getName().equalsIgnoreCase(
                        FinancialConstants.PAYMENTVOUCHER_NAME_PENSION)) {

                    final String[] voucherName = new String[1];
                    voucherName[0] = voucherHeader.getName();
                    parameters.put("voucherName", voucherName);
                }
            // parameters.put("voucherHeader", );
            if (billType == null || billType.equalsIgnoreCase("-1")
                    || billType.equalsIgnoreCase("0"))

                chequeAssignmentList.addAll(chequeAssignmentService
                        .getPaymentVoucherNotInInstrument(parameters));
            else if (billType
                    .equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
                chequeAssignmentList.addAll(chequeAssignmentService
                        .getExpenseBillPayments());
            else if (billType
                    .equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS
                            + "-"
                            + FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE))
                chequeAssignmentList
                        .addAll(chequeAssignmentService
                                .getContractorSupplierPaymentsForChequeAssignment(parameters));
            else if (billType
                    .equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK))
                chequeAssignmentList.addAll(chequeAssignmentService
                        .getDirectBankPaymentsForChequeAssignment());
        } else {
            final StringBuffer sql = new StringBuffer();
            if (!"".equals(parameters.get("fromDate")[0]))
                sql.append(" and vh.voucherDate>='"
                        + sdf.format(formatter.parse(parameters.get("fromDate")[0]))
                        + "' ");
            if (!"".equals(parameters.get("toDate")[0]))
                sql.append(" and vh.voucherDate<='"
                        + sdf.format(formatter.parse(parameters.get("toDate")[0]))
                        + "'");
            if (!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
                sql.append(" and vh.voucherNumber like '%"
                        + voucherHeader.getVoucherNumber() + "%'");
            if (voucherHeader.getFundId() != null)
                sql.append(" and vh.fundId="
                        + voucherHeader.getFundId().getId());
            if (voucherHeader.getVouchermis().getFundsource() != null)
                sql.append(" and vmis.fundsourceId="
                        + voucherHeader.getVouchermis().getFundsource().getId());
            if (voucherHeader.getVouchermis().getDepartmentid() != null)
                sql.append(" and vmis.departmentid="
                        + voucherHeader.getVouchermis().getDepartmentid()
                                .getId());
            if (voucherHeader.getVouchermis().getSchemeid() != null)
                sql.append(" and vmis.schemeid="
                        + voucherHeader.getVouchermis().getSchemeid().getId());
            if (voucherHeader.getVouchermis().getSubschemeid() != null)
                sql.append(" and vmis.subschemeid="
                        + voucherHeader.getVouchermis().getSubschemeid()
                                .getId());
            if (voucherHeader.getVouchermis().getFunctionary() != null)
                sql.append(" and vmis.functionaryid="
                        + voucherHeader.getVouchermis().getFunctionary()
                                .getId());
            if (voucherHeader.getVouchermis().getDivisionid() != null)
                sql.append(" and vmis.divisionid="
                        + voucherHeader.getVouchermis().getDivisionid().getId());
            sql.append(" and ph.bankaccountnumberid="
                    + parameters.get("bankaccount")[0]);
            sql.append(" and lower(ph.type)=lower('"
                    + parameters.get("paymentMode")[0] + "')");

            final List<AppConfigValues> appList = appConfigValuesService
                    .getConfigValuesByModuleAndKey("EGF",
                            "APPROVEDVOUCHERSTATUS");
            final String approvedstatus = appList.get(0).getValue();
            final List<String> descriptionList = new ArrayList<String>();
            descriptionList.add("New");
            descriptionList.add("Reconciled");
            final List<EgwStatus> egwStatusList = egwStatusDAO
                    .getStatusListByModuleAndCodeList("Instrument",
                            descriptionList);
            String statusId = "";
            for (final EgwStatus egwStatus : egwStatusList)
                statusId = statusId + egwStatus.getId() + ",";
            statusId = statusId.substring(0, statusId.length() - 1);

            persistenceService.find(" from Bankaccount where id=?",
                    Long.valueOf(parameters.get("bankaccount")[0]));
            Query query = null;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("statusId -- > " + statusId);

            chequeList = new ArrayList<ChequeAssignment>();

            if (voucherHeader.getName() != null
                    && voucherHeader.getName().equalsIgnoreCase(
                            FinancialConstants.PAYMENTVOUCHER_NAME_SALARY)) {
                query = getSession()
                        .createSQLQuery(
                                "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate,  misbill.paidto as paidTo from Paymentheader ph,voucherheader vh  LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID  ,vouchermis vmis, Miscbilldetail misbill "
                                        + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
                                        + approvedstatus
                                        + " "
                                        + sql
                                        + " "
                                        + " and  IV.VOUCHERHEADERID IS NULL  and vh.type='"
                                        + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                        + "' and vh.name ='"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                        + "'"
                                        + " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by vh.voucherNumber ")
                        .addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount")
                        .addScalar("chequeDate")
                        .addScalar("paidTo")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for salary " + query);
                chequeAssignmentList = query.list();
                // below one handles
                // assign-->surrendar-->assign-->surrendar-->.......
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("checking  cheque assigned and surrendard");
                query = getSession()
                        .createSQLQuery(
                                "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate,  misbill.paidto as paidTo from Paymentheader ph,voucherheader vh  LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID  ,vouchermis vmis, Miscbilldetail misbill "
                                        + ", (select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) as table1 "
                                        + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
                                        + approvedstatus
                                        + " "
                                        + sql
                                        + " "
                                        + " and IV.VOUCHERHEADERID IS NOT  NULL  and iv.instrumentheaderid=table1.maxihid and table1.iv1vhid=vh.id and  ih.id_status not in ("
                                        + statusId
                                        + ")  and vh.type='"
                                        + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                        + "' and vh.name ='"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                        + "'"
                                        + " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by vh.voucherNumber ")
                        .addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .addScalar("paidTo")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for salary " + query);
                chequeAssignmentList.addAll(query.list());
                final List<ChequeAssignment> tempChequeAssignmentList = chequeAssignmentList;
                Float paidAmt;
                String paidTo, nextPaidTo;
                int i, j;
                // /
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" interating  "
                            + tempChequeAssignmentList.size() + " times");
                for (i = 0; i < tempChequeAssignmentList.size(); i++)
                    for (j = i + 1; j < tempChequeAssignmentList.size(); j++)
                        if (tempChequeAssignmentList
                                .get(j)
                                .getVoucherid()
                                .equals(tempChequeAssignmentList.get(i)
                                        .getVoucherid())
                                && tempChequeAssignmentList
                                        .get(j)
                                        .getVoucherNumber()
                                        .equals(tempChequeAssignmentList.get(i)
                                                .getVoucherNumber())) {
                            paidAmt = tempChequeAssignmentList.get(i)
                                    .getPaidAmount().floatValue();
                            paidAmt += tempChequeAssignmentList.get(j)
                                    .getPaidAmount().floatValue();
                            tempChequeAssignmentList.get(i).setPaidAmount(
                                    new BigDecimal(paidAmt));
                            paidTo = tempChequeAssignmentList.get(i)
                                    .getPaidTo();
                            nextPaidTo = tempChequeAssignmentList.get(j)
                                    .getPaidTo();
                            tempChequeAssignmentList.get(i).setPaidTo(
                                    paidTo + " , " + nextPaidTo);
                            tempChequeAssignmentList.remove(j);
                            j--;
                        }
                if (tempChequeAssignmentList.size() != chequeAssignmentList
                        .size())
                    chequeAssignmentList = tempChequeAssignmentList;
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" interating  "
                            + tempChequeAssignmentList.size() + " Done");
            } else if (voucherHeader.getName() != null
                    && voucherHeader.getName().equalsIgnoreCase(
                            FinancialConstants.PAYMENTVOUCHER_NAME_PENSION)) {
                query = getSession()
                        .createSQLQuery(
                                "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate,  misbill.paidto as paidTo from Paymentheader ph,voucherheader vh  LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID  ,vouchermis vmis, Miscbilldetail misbill "
                                        + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
                                        + approvedstatus
                                        + " "
                                        + sql
                                        + " "
                                        + " and  IV.VOUCHERHEADERID IS NULL  and vh.type='"
                                        + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                        + "' and vh.name ='"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                        + "'"
                                        + " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by vh.voucherNumber ")
                        .addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .addScalar("paidTo")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for salary " + query);
                chequeAssignmentList = query.list();
                // below one handles
                // assign-->surrendar-->assign-->surrendar-->.......
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("checking  cheque assigned and surrendard");
                query = getSession()
                        .createSQLQuery(
                                "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate,  misbill.paidto as paidTo from Paymentheader ph,voucherheader vh  LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID  ,vouchermis vmis, Miscbilldetail misbill "
                                        + ", (select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) as table1 "
                                        + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
                                        + approvedstatus
                                        + " "
                                        + sql
                                        + " "
                                        + " and IV.VOUCHERHEADERID IS NOT  NULL  and iv.instrumentheaderid=table1.maxihid and table1.iv1vhid=vh.id and  ih.id_status not in ("
                                        + statusId
                                        + ")  and vh.type='"
                                        + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                        + "' and vh.name ='"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                        + "'"
                                        + " group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by vh.voucherNumber ")
                        .addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .addScalar("paidTo")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for salary " + query);
                chequeAssignmentList.addAll(query.list());
                final List<ChequeAssignment> tempChequeAssignmentList = chequeAssignmentList;
                Float paidAmt;
                String paidTo, nextPaidTo;
                int i, j;
                // /
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" interating  "
                            + tempChequeAssignmentList.size() + " times");
                for (i = 0; i < tempChequeAssignmentList.size(); i++)
                    for (j = i + 1; j < tempChequeAssignmentList.size(); j++)
                        if (tempChequeAssignmentList
                                .get(j)
                                .getVoucherid()
                                .equals(tempChequeAssignmentList.get(i)
                                        .getVoucherid())
                                && tempChequeAssignmentList
                                        .get(j)
                                        .getVoucherNumber()
                                        .equals(tempChequeAssignmentList.get(i)
                                                .getVoucherNumber())) {
                            paidAmt = tempChequeAssignmentList.get(i)
                                    .getPaidAmount().floatValue();
                            paidAmt += tempChequeAssignmentList.get(j)
                                    .getPaidAmount().floatValue();
                            tempChequeAssignmentList.get(i).setPaidAmount(
                                    new BigDecimal(paidAmt));
                            paidTo = tempChequeAssignmentList.get(i)
                                    .getPaidTo();
                            nextPaidTo = tempChequeAssignmentList.get(j)
                                    .getPaidTo();
                            tempChequeAssignmentList.get(i).setPaidTo(
                                    paidTo + " , " + nextPaidTo);
                            tempChequeAssignmentList.remove(j);
                            j--;
                        }
                if (tempChequeAssignmentList.size() != chequeAssignmentList
                        .size())
                    chequeAssignmentList = tempChequeAssignmentList;
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" interating  "
                            + tempChequeAssignmentList.size() + " Done");
            } else if (voucherHeader.getName() == null
                    || !voucherHeader.getName().equalsIgnoreCase(
                            FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE)) {// /
                                                                                 // do
                                                                                 // not
                                                                                 // include
                                                                                 // salary
                                                                                 // payments
                                                                                 // and
                                                                                 // remittances
                query = getSession()
                        .createSQLQuery(
                                "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate from Paymentheader ph,voucherheader vh   LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill "
                                        +

                                        " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
                                        + approvedstatus
                                        + " "
                                        + sql
                                        + " "
                                        + " and  IV.VOUCHERHEADERID IS NULL  and vh.type='"
                                        + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                        + "' and vh.name NOT IN "
                                        + " ('"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                        + "' , '"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                        + "','"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                        + "') "
                                        + " group by vh.id,vh.voucherNumber,vh.voucherDate order by vh.voucherNumber ")
                        .addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for non salary and remittance" + query);
                chequeAssignmentList = query.list();
                // below one handles
                // assign-->surrendar-->assign-->surrendar-->.......
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("checking  cheque assigned and surrendard");
                query = getSession()
                        .createSQLQuery(
                                "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate from Paymentheader ph,voucherheader vh   LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill "
                                        + ", (select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) as table1 "
                                        + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="
                                        + approvedstatus
                                        + " "
                                        + sql
                                        + " "
                                        + " and  IV.VOUCHERHEADERID IS NOT  NULL and iv.instrumentheaderid=table1.maxihid and  table1.iv1vhid=vh.id and ih.id_status not in ("
                                        + statusId
                                        + ") and vh.type='"
                                        + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                        + "' and vh.name NOT IN"
                                        + " ('"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                        + "' , '"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                                        + "','"
                                        + FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                                        + "') "
                                        + " group by vh.id,vh.voucherNumber,vh.voucherDate order by vh.voucherNumber ")
                        .addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for non salary and remittance" + query);
                chequeAssignmentList.addAll(query.list());

            } else {
                StringBuilder tempquery1 = new StringBuilder();

                tempquery1.append(
                        "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate,misbill.paidto as paidTo from Paymentheader ph,voucherheader vh  LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill,Eg_remittance  rem ");
                tempquery1.append(" where ph.voucherheaderid=misbill.payvhid and  rem.paymentvhid=vh.id ");
                String recoveryId = parameters.get("recoveryId")[0];
                if (!recoveryId.isEmpty())
                    tempquery1.append(" and rem.tdsid= " + parameters.get("recoveryId")[0]);
                tempquery1.append("  and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status =");
                tempquery1.append(approvedstatus);
                tempquery1.append(" ");
                tempquery1.append(sql);
                tempquery1.append(" ");
                tempquery1.append(" and IV.VOUCHERHEADERID IS NULL  and vh.type='");
                tempquery1.append(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
                tempquery1.append("' and vh.name ='");
                tempquery1.append(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE);
                tempquery1.append("'");
                tempquery1.append(" group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by vh.voucherNumber ");
                query = getSession()
                        .createSQLQuery(tempquery1.toString()).addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .addScalar("paidTo")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for salary and remittance" + query);
                chequeAssignmentList = query.list();
                // below one handles
                // assign-->surrendar-->assign-->surrendar-->.......
                StringBuilder tempquery = new StringBuilder();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("checking  cheque assigned and surrendard");

                tempquery.append(
                        "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,current_date as chequeDate,misbill.paidto as paidTo from Paymentheader ph,voucherheader vh  LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill,Eg_remittance  rem ");
                tempquery.append(
                        ", (select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) table1");
                tempquery.append(" where ph.voucherheaderid=misbill.payvhid and  rem.paymentvhid=vh.id ");
                String recoveryId1 = parameters.get("recoveryId")[0];
                if (!recoveryId1.isEmpty())
                    tempquery.append(" and rem.tdsid= " + parameters.get("recoveryId")[0]);
                tempquery.append("  and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status =");
                tempquery.append(approvedstatus);
                tempquery.append(" ");
                tempquery.append(sql);
                tempquery.append(" ");
                tempquery.append(
                        " and  IV.VOUCHERHEADERID IS NOT  NULL  and iv.instrumentheaderid=table1.maxihid and table1.iv1vhid=vh.id and ih.id_status not in (");
                tempquery.append(statusId);
                tempquery.append(") and vh.type='");
                tempquery.append(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
                tempquery.append("' and vh.name ='");
                tempquery.append(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE);
                tempquery.append("'");
                tempquery.append(" group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by vh.voucherNumber ");
                query = getSession()
                        .createSQLQuery(tempquery.toString()).addScalar("voucherid", LongType.INSTANCE)
                        .addScalar("voucherNumber")
                        .addScalar("voucherDate")
                        .addScalar("paidAmount", BigDecimalType.INSTANCE)
                        .addScalar("chequeDate")
                        .addScalar("paidTo")
                        .setResultTransformer(
                                Transformers
                                        .aliasToBean(ChequeAssignment.class));
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(" for salary and remittance" + query);
                chequeAssignmentList.addAll(query.list());
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPaymentVoucherNotInInstrument.");
        return chequeAssignmentList;
    }

    /*
     * private Map getSubledgerAmtForDeduction(final Long payVhId) { if (LOGGER.isDebugEnabled()) LOGGER.debug(
     * "Starting getSubledgerAmtForDeduction..."); final Map<String, BigDecimal> map = new HashMap<String, BigDecimal>(); final
     * Query query = getSession() .createSQLQuery(
     * " select gld.detailtypeid,gld.detailkeyid,sum(gld.amount) from generalledgerdetail gld,generalledger gl, voucherheader vh, miscbilldetail misbill "
     * +
     * " where vh.id= gl.voucherheaderid and gl.id=gld.generalledgerid and gl.creditamount>0 and gl.glcodeid NOT in (:glcodeIdList) and misbill.billvhid=vh.id "
     * + " and misbill.payvhid =" + payVhId + " group by gld.detailtypeid,gld.detailkeyid ");
     * query.setParameterList("glcodeIdList", cBillGlcodeIdList); final List<Object[]> list = query.list(); if (list != null &&
     * !list.isEmpty()) for (final Object[] ob : list) map.put(ob[0].toString() + DELIMETER + ob[1].toString(), (BigDecimal)
     * ob[2]); if (LOGGER.isDebugEnabled()) LOGGER.debug("Completed getSubledgerAmtForDeduction."); return map; }
     */

    @Transactional
    public List<InstrumentHeader> createInstrument(
            final List<ChequeAssignment> chequeAssignmentList,
            final String paymentMode, final Integer bankaccount,
            final Map<String, String[]> parameters, final Department dept)
            throws ApplicationRuntimeException, Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting createInstrument...");
        List<InstrumentHeader> instHeaderList = new ArrayList<InstrumentHeader>();
        final List<Long> selectedPaymentVHList = new ArrayList<Long>();
        final Map<String, BigDecimal> payeeMap = new HashMap<String, BigDecimal>();
        BigDecimal totalPaidAmt = BigDecimal.ZERO;

        for (final ChequeAssignment assignment : chequeAssignmentList)
            if (assignment.getIsSelected()) {
                selectedPaymentVHList.add(assignment.getVoucherid());
                if (payeeMap.containsKey(assignment.getPaidTo() + DELIMETER
                        + assignment.getDetailtypeid() + DELIMETER
                        + assignment.getDetailkeyid())) // concatenate the
                                                        // amount, if the
                                                        // party's are same
                    payeeMap.put(
                            assignment.getPaidTo() + DELIMETER
                                    + assignment.getDetailtypeid() + DELIMETER
                                    + assignment.getDetailkeyid(),
                            payeeMap.get(
                                    assignment.getPaidTo() + DELIMETER
                                            + assignment.getDetailtypeid()
                                            + DELIMETER
                                            + assignment.getDetailkeyid())
                                    .add(
                                            assignment.getPaidAmount()));
                else
                    payeeMap.put(assignment.getPaidTo() + DELIMETER
                            + assignment.getDetailtypeid() + DELIMETER
                            + assignment.getDetailkeyid(),
                            assignment.getPaidAmount());
                totalPaidAmt = totalPaidAmt.add(assignment.getPaidAmount());
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("selectedPaymentList===" + selectedPaymentVHList);
        final Bankaccount account = (Bankaccount) persistenceService.find(
                " from Bankaccount where  id=?", bankaccount.longValue());
        // get voucherList
        final List<CVoucherHeader> voucherList = persistenceService
                .findAllByNamedQuery("getVoucherList", selectedPaymentVHList);

        final List<Map<String, Object>> instrumentHeaderList = new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String, Object>>();

        if (paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
            final Map<Long, CVoucherHeader> paymentVoucherMap = new HashMap<Long, CVoucherHeader>();
            for (final CVoucherHeader voucherHeader : voucherList)
                paymentVoucherMap.put(voucherHeader.getId(), voucherHeader);

            if (isChequeNoGenerationAuto()) // if cheque number generation is
                                            // auto
            {
                // get chequeNumber
                final String chequeNo = chequeService.nextChequeNumber(account
                        .getId().toString(), payeeMap.size(), dept.getId()
                                .intValue());
                final String[] chequeNoArray = StringUtils.split(chequeNo, ",");

                // create instrument header
                final Map<String, String> chequeNoMap = new HashMap<String, String>();
                final Iterator iterator = payeeMap.keySet().iterator();
                String key; // paidTo+delimeter+detailtypeid+delimeter+detailkeyid
                int i = 0;
                while (iterator.hasNext()) {
                    key = iterator.next().toString();
                    instrumentHeaderList.add(prepareInstrumentHeader(account,
                            chequeNoArray[i],
                            FinancialConstants.MODEOFPAYMENT_CHEQUE
                                    .toLowerCase(),
                            key.split(DELIMETER)[0],
                            payeeMap.get(key), currentDate, key, null));
                    chequeNoMap.put(key, chequeNoArray[i]);
                    i++;
                }
                instHeaderList = instrumentService
                        .addToInstrument(instrumentHeaderList);

                // create instrument voucher
                for (final ChequeAssignment assignment : chequeAssignmentList)
                    if (assignment.getIsSelected())
                        instrumentVoucherList
                                .add(preapreInstrumentVoucher(paymentVoucherMap
                                        .get(assignment.getVoucherid()),
                                        account, chequeNoMap.get(assignment
                                                .getPaidTo()
                                                + DELIMETER
                                                + assignment.getDetailtypeid()
                                                + DELIMETER
                                                + assignment.getDetailkeyid()),
                                        assignment.getPaidTo()));
                instVoucherList = instrumentService
                        .updateInstrumentVoucherReference(instrumentVoucherList);
            } else // for manual cheque number
            {
                String text = "";
                final Map<String, BigDecimal> partyChequeNoMap = new HashMap<String, BigDecimal>();
                for (final ChequeAssignment assignment : chequeAssignmentList)
                    if (assignment.getIsSelected()) {
                        text = assignment.getPaidTo() + DELIMETER
                                + assignment.getDetailtypeid() + DELIMETER
                                + assignment.getDetailkeyid() + DELIMETER
                                + assignment.getChequeNumber() + DELIMETER
                                + formatter.format(assignment.getChequeDate())
                                + DELIMETER + assignment.getSerialNo();
                        if (partyChequeNoMap.containsKey(text))
                            partyChequeNoMap.put(
                                    text,
                                    partyChequeNoMap.get(text).add(
                                            assignment.getPaidAmount()));
                        else
                            partyChequeNoMap.put(text,
                                    assignment.getPaidAmount());
                    }
                final Iterator iterator = partyChequeNoMap.keySet().iterator();
                String key;
                while (iterator.hasNext()) // create instrument header
                {
                    key = iterator.next().toString();

                    instrumentHeaderList.add(prepareInstrumentHeader(account,
                            key.split(DELIMETER)[3],
                            FinancialConstants.MODEOFPAYMENT_CHEQUE
                                    .toLowerCase(),
                            key.split(DELIMETER)[0],
                            partyChequeNoMap.get(key), formatter.parse(key
                                    .split(DELIMETER)[4]),
                            key, key
                                    .split(DELIMETER)[5]));
                }
                instHeaderList = instrumentService
                        .addToInstrument(instrumentHeaderList);
                // create instrument voucher
                for (final ChequeAssignment assignment : chequeAssignmentList)
                    if (assignment.getIsSelected())
                        instrumentVoucherList
                                .add(preapreInstrumentVoucher(paymentVoucherMap
                                        .get(assignment.getVoucherid()),
                                        account, assignment.getChequeNumber(),
                                        assignment.getPaidTo(), assignment
                                                .getSerialNo()));
                instVoucherList = instrumentService
                        .updateInstrumentVoucherReference(instrumentVoucherList);
            } // end of manual cheque number
        }
        // if it's cash or RTGS
        else {
            final String chequeNo;
            if (paymentMode.equals(FinancialConstants.MODEOFPAYMENT_RTGS))
                instrumentHeaderList.add(prepareInstrumentHeaderForRtgs(
                        account, parameters.get("rtgsRefNo")[0], totalPaidAmt,
                        formatter.parse(parameters.get("rtgsDate")[0]), ""));
            else if (isChequeNoGenerationAuto()) // if cheque number generation
                                                 // is auto
            {
                chequeNo = chequeService.nextChequeNumber(account.getId()
                        .toString(), 1, dept.getId().intValue());
                instrumentHeaderList.add(prepareInstrumentHeader(account,
                        chequeNo,
                        FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
                        parameters.get("inFavourOf")[0], totalPaidAmt,
                        currentDate, "", null));
            } else
                instrumentHeaderList.add(prepareInstrumentHeader(account,
                        parameters.get("chequeNo")[0],
                        FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
                        parameters.get("inFavourOf")[0], totalPaidAmt,
                        formatter.parse(parameters.get("chequeDt")[0]), "",
                        parameters.get("serialNo")[0]));

            instHeaderList = instrumentService
                    .addToInstrument(instrumentHeaderList);

            final List<Paymentheader> paymentList = persistenceService
                    .findAllByNamedQuery("getPaymentList",
                            selectedPaymentVHList);
            Map<String, Object> instrumentVoucherMap = null;
            for (final Paymentheader paymentheader : paymentList) {
                instrumentVoucherMap = new HashMap<String, Object>();
                instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER,
                        paymentheader.getVoucherheader());
                instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
                        instHeaderList.get(0));
                instrumentVoucherList.add(instrumentVoucherMap);
            }
            instVoucherList = instrumentService
                    .updateInstrumentVoucherReference(instrumentVoucherList);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createInstrument.");
        return instHeaderList;
    }

    @Transactional
    public List<InstrumentHeader> reassignInstrument(
            final List<ChequeAssignment> chequeAssignmentList,
            final String paymentMode, final Integer bankaccount,
            final Map<String, String[]> parameters, final Department dept)
            throws ApplicationRuntimeException, Exception {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting reassignInstrument...");
        List<InstrumentHeader> instHeaderList = new ArrayList<InstrumentHeader>();
        final List<Long> selectedPaymentVHList = new ArrayList<Long>();
        final Map<String, BigDecimal> payeeMap = new HashMap<String, BigDecimal>();
        BigDecimal totalPaidAmt = BigDecimal.ZERO;

        for (final ChequeAssignment assignment : chequeAssignmentList)
            if (assignment.getIsSelected()) {
                selectedPaymentVHList.add(assignment.getVoucherid());
                if (payeeMap.containsKey(assignment.getPaidTo() + DELIMETER
                        + assignment.getDetailtypeid() + DELIMETER
                        + assignment.getDetailkeyid())) // concatenate the
                                                        // amount, if the
                                                        // party's are same
                    payeeMap.put(
                            assignment.getPaidTo() + DELIMETER
                                    + assignment.getDetailtypeid() + DELIMETER
                                    + assignment.getDetailkeyid(),
                            payeeMap.get(
                                    assignment.getPaidTo() + DELIMETER
                                            + assignment.getDetailtypeid()
                                            + DELIMETER
                                            + assignment.getDetailkeyid())
                                    .add(
                                            assignment.getPaidAmount()));
                else
                    payeeMap.put(assignment.getPaidTo() + DELIMETER
                            + assignment.getDetailtypeid() + DELIMETER
                            + assignment.getDetailkeyid(),
                            assignment.getPaidAmount());
                totalPaidAmt = totalPaidAmt.add(assignment.getPaidAmount());
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("selectedPaymentList===" + selectedPaymentVHList);
        final Bankaccount account = (Bankaccount) persistenceService.find(
                " from Bankaccount where  id=?", bankaccount.longValue());
        // get voucherList
        final List<CVoucherHeader> voucherList = persistenceService
                .findAllByNamedQuery("getVoucherList", selectedPaymentVHList);

        new ArrayList<Map<String, Object>>();
        final List<Map<String, Object>> instrumentVoucherList = new ArrayList<Map<String, Object>>();

        if (paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE)) {
            final Map<Long, CVoucherHeader> paymentVoucherMap = new HashMap<Long, CVoucherHeader>();
            for (final CVoucherHeader voucherHeader : voucherList)
                paymentVoucherMap.put(voucherHeader.getId(), voucherHeader);

            String text = "";
            final Map<String, BigDecimal> partyChequeNoMap = new HashMap<String, BigDecimal>();
            for (final ChequeAssignment assignment : chequeAssignmentList)
                if (assignment.getIsSelected()) {
                    text = assignment.getPaidTo() + DELIMETER
                            + assignment.getDetailtypeid() + DELIMETER
                            + assignment.getDetailkeyid() + DELIMETER
                            + assignment.getChequeNumber() + DELIMETER
                            + formatter.format(assignment.getChequeDate())
                            + DELIMETER + assignment.getSerialNo();
                    if (partyChequeNoMap.containsKey(text))
                        partyChequeNoMap.put(text, partyChequeNoMap.get(text)
                                .add(assignment.getPaidAmount()));
                    else
                        partyChequeNoMap.put(text, assignment.getPaidAmount());
                }
            instHeaderList = new ArrayList<InstrumentHeader>();
            final Iterator iterator = partyChequeNoMap.keySet().iterator();
            String key;

            while (iterator.hasNext()) // create instrument header
            {
                key = iterator.next().toString();
                instHeaderList.add(reassignInstrumentHeader(account,
                        key.split(DELIMETER)[3],
                        FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
                        key.split(DELIMETER)[0], partyChequeNoMap.get(key),
                        formatter.parse(key.split(DELIMETER)[4]), key,
                        key.split(DELIMETER)[5]));
            }
            // instHeaderList =
            // instrumentService.addToInstrument(instrumentHeaderList);
            // create instrument voucher
            for (final ChequeAssignment assignment : chequeAssignmentList)
                if (assignment.getIsSelected())
                    instrumentVoucherList.add(reassignInstrumentVoucher(
                            paymentVoucherMap.get(assignment.getVoucherid()),
                            account, assignment.getChequeNumber(),
                            assignment.getPaidTo(), assignment.getSerialNo()));
            instVoucherList = instrumentService
                    .updateInstrumentVoucherReference(instrumentVoucherList);
            // end of manual cheque number
        } else // if it's cash or RTGS
        {

            instHeaderList.add(reassignInstrumentHeader(account,
                    parameters.get("chequeNo")[0],
                    FinancialConstants.MODEOFPAYMENT_CHEQUE.toLowerCase(),
                    parameters.get("inFavourOf")[0], totalPaidAmt,
                    formatter.parse(parameters.get("chequeDt")[0]), "",
                    parameters.get("serialNo")[0]));
            reassignInstrumentVoucher(null, account,
                    parameters.get("chequeNo")[0],
                    parameters.get("inFavourOf")[0],
                    parameters.get("serialNo")[0]);
            // instHeaderList =
            // instrumentService.addToInstrument(instrumentHeaderList);

            final List<Paymentheader> paymentList = persistenceService
                    .findAllByNamedQuery("getPaymentList",
                            selectedPaymentVHList);
            Map<String, Object> instrumentVoucherMap = null;
            for (final Paymentheader paymentheader : paymentList) {
                instrumentVoucherMap = new HashMap<String, Object>();
                instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER,
                        paymentheader.getVoucherheader());
                instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
                        instHeaderList.get(0));
                instrumentVoucherList.add(instrumentVoucherMap);
            }
            instVoucherList = instrumentService
                    .updateInstrumentVoucherReference(instrumentVoucherList);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed reassignInstrument.");
        return instHeaderList;
    }

    protected Map<String, Object> prepareInstrumentHeader(
            final Bankaccount account, final String chqNo,
            final String instType, final String partyName,
            final BigDecimal amount, final Date date, final String key,
            final String serialNo) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareInstrumentHeader...");
        final Map<String, Object> instrumentHeaderMap = new HashMap<String, Object>();
        instrumentHeaderMap.put(VoucherConstant.IS_PAYCHECK, "1");
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_TYPE, instType);
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_AMOUNT, amount);
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_NUMBER, chqNo);
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_SERIALNO, serialNo);
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_DATE, date);
        instrumentHeaderMap.put(VoucherConstant.BANK_CODE, account
                .getBankbranch().getBank().getCode());
        instrumentHeaderMap.put(VoucherConstant.PAY_TO, partyName);
        instrumentHeaderMap.put(VoucherConstant.BANKACCOUNTID, account.getId());
        if (!key.equals("")) {
            if (key.split(DELIMETER)[1] != null
                    && !key.split(DELIMETER)[1].equals("")
                    && !key.split(DELIMETER)[1].equals("null"))
                instrumentHeaderMap.put(VoucherConstant.DETAIL_TYPE_ID,
                        Integer.valueOf(key.split(DELIMETER)[1]));
            if (key.split(DELIMETER)[2] != null
                    && !key.split(DELIMETER)[2].equals("")
                    && !key.split(DELIMETER)[2].equals("null"))
                instrumentHeaderMap.put(VoucherConstant.DETAIL_KEY_ID,
                        Long.valueOf(key.split(DELIMETER)[2]));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareInstrumentHeader.");
        return instrumentHeaderMap;
    }

    protected Map<String, Object> prepareInstrumentHeaderForRtgs(
            final Bankaccount account, final String txnNo,
            final BigDecimal amount, final Date date, final String key) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting prepareInstrumentHeaderForRtgs...");
        final Map<String, Object> instrumentHeaderMap = new HashMap<String, Object>();
        instrumentHeaderMap.put(VoucherConstant.IS_PAYCHECK, "1");
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_TYPE,
                FinancialConstants.INSTRUMENT_TYPE_ADVICE);
        instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_AMOUNT, amount);
        instrumentHeaderMap.put(VoucherConstant.TRANSACTION_NUMBER, txnNo);
        instrumentHeaderMap.put(VoucherConstant.TRANSACTION_DATE, date);
        instrumentHeaderMap.put(VoucherConstant.BANK_CODE, account
                .getBankbranch().getBank().getCode());
        // / instrumentHeaderMap.put(VoucherConstant.PAY_TO, partyName);
        instrumentHeaderMap.put(VoucherConstant.BANKACCOUNTID, account.getId());
        if (!key.equals("")) {
            if (key.split(DELIMETER)[1] != null
                    && !key.split(DELIMETER)[1].equals("")
                    && !key.split(DELIMETER)[1].equals("null"))
                instrumentHeaderMap.put(VoucherConstant.DETAIL_TYPE_ID,
                        Integer.valueOf(key.split(DELIMETER)[1]));
            if (key.split(DELIMETER)[2] != null
                    && !key.split(DELIMETER)[2].equals("")
                    && !key.split(DELIMETER)[2].equals("null"))
                instrumentHeaderMap.put(VoucherConstant.DETAIL_KEY_ID,
                        Long.valueOf(key.split(DELIMETER)[2]));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed prepareInstrumentHeaderForRtgs.");
        return instrumentHeaderMap;
    }

    @Transactional
    public InstrumentHeader reassignInstrumentHeader(final Bankaccount account,
            final String chqNo, final String instType, final String partyName,
            final BigDecimal amount, final Date date, final String key,
            final String serialNo) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting reassignInstrumentHeader...");
        final InstrumentHeader ih = (InstrumentHeader) persistenceService
                .find("from InstrumentHeader where instrumentNumber=? and bankAccountId=? and isPayCheque='1' ",
                        chqNo, account);
        ih.setIsPayCheque("1");
        // ih.setInstrumentType(instType);
        ih.setInstrumentAmount(amount);
        ih.setSerialNo(financialYearDAO.findById(Long.valueOf(serialNo), false));
        // instrumentHeaderMap.put(VoucherConstant.INSTRUMENT_NUMBER, chqNo);
        ih.setInstrumentDate(date);
        // ih.setBank(account.getBankbranch().getBank().getCode());
        ih.setPayTo(partyName);
        ih.setBankAccountId(account);
        ih.setSurrendarReason(null);
        ih.setStatusId(instrumentService
                .getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
        if (!key.equals("")) {
            if (key.split(DELIMETER)[1] != null
                    && !key.split(DELIMETER)[1].equals("")
                    && !key.split(DELIMETER)[1].equals("null"))
                ih.setDetailTypeId((Accountdetailtype) persistenceService.find(
                        "from Accountdetailtype where id=?",
                        Integer.valueOf(key.split(DELIMETER)[1])));
            if (key.split(DELIMETER)[2] != null
                    && !key.split(DELIMETER)[2].equals("")
                    && !key.split(DELIMETER)[2].equals("null"))
                ih.setDetailKeyId(Long.valueOf(key.split(DELIMETER)[2]));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed reassignInstrumentHeader.");
        return instrumentHeaderService.persist(ih);
    }

    protected Map<String, Object> preapreInstrumentVoucher(
            final CVoucherHeader voucherHeader, final Bankaccount account,
            final String chqNo, final String paidTo, final String serialno) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting preapreInstrumentVoucher...");
        final Map<String, Object> instrumentVoucherMap = new HashMap<String, Object>();
        instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, voucherHeader);
        // get the InstrumnetHeader for the party & chequeno & bankaccountid
        final InstrumentHeader instrumentHeader = instrumentService
                .getInstrumentHeader(account.getId(), chqNo, paidTo, serialno);
        instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
                instrumentHeader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed preapreInstrumentVoucher.");
        return instrumentVoucherMap;
    }

    protected Map<String, Object> preapreInstrumentVoucher(
            final CVoucherHeader voucherHeader, final Bankaccount account,
            final String chqNo, final String paidTo) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting preapreInstrumentVoucher...");
        final Map<String, Object> instrumentVoucherMap = new HashMap<String, Object>();
        instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, voucherHeader);
        // get the InstrumnetHeader for the party & chequeno & bankaccountid
        final InstrumentHeader instrumentHeader = instrumentService
                .getInstrumentHeader(account.getId().longValue(), chqNo, paidTo);
        instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
                instrumentHeader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed preapreInstrumentVoucher.");
        return instrumentVoucherMap;
    }

    @Transactional
    public Map<String, Object> reassignInstrumentVoucher(
            final CVoucherHeader voucherHeader, final Bankaccount account,
            final String chqNo, final String paidTo, final String serailNo) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting reassignInstrumentVoucher...");

        final InstrumentHeader instrumentHeader = instrumentService
                .getInstrumentHeader(account.getId(), chqNo, paidTo, serailNo);

        final List<InstrumentVoucher> findAllBy = persistenceService.findAllBy(
                "from InstrumentVoucher where instrumentHeaderId=?",
                instrumentHeader);
        for (final InstrumentVoucher iv : findAllBy)
            persistenceService.delete(iv);
        getSession().refresh(instrumentHeader);
        getSession().flush();
        // instrumentHeader=
        // instrumentService.instrumentHeaderService.persist(instrumentHeader);
        final Map<String, Object> instrumentVoucherMap = new HashMap<String, Object>();
        instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, voucherHeader);
        // get the InstrumnetHeader for the party & chequeno & bankaccountid

        instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER,
                instrumentHeader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed reassignInstrumentVoucher.");
        return instrumentVoucherMap;
    }

    public List<PaymentBean> getCSList(final List<EgBillregister> billList,
            final Map<Long, BigDecimal> deductionAmtMap,
            final Map<Long, BigDecimal> paidAmtMap) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getCSList...");
        final List<PaymentBean> contractorList = new ArrayList<PaymentBean>();
        PaymentBean paymentBean = null;
        if (billList != null && !billList.isEmpty())
            for (final EgBillregister billregister : billList) {
                paymentBean = new PaymentBean();
                paymentBean.setCsBillId(billregister.getId());
                paymentBean.setBillNumber(billregister.getBillnumber());
                paymentBean.setBillDate(billregister.getBilldate());
                paymentBean.setExpType(billregister.getExpendituretype());

                if (billregister.getExpendituretype().equals(
                        FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)) {
                    if (billregister.getEgBillregistermis().getEgBillSubType() != null) {
                        if (billregister.getEgBillregistermis()
                                .getEgBillSubType().getName()
                                .equalsIgnoreCase("TNEB"))
                            paymentBean.setPayTo(billregister
                                    .getEgBillregistermis().getPayto());
                        else
                            paymentBean
                                    .setPayTo(getPayeeNameForCBill(billregister));
                    } else
                        paymentBean
                                .setPayTo(getPayeeNameForCBill(billregister));
                } else
                    paymentBean.setPayTo(billregister.getEgBillregistermis()
                            .getPayto());
                paymentBean.setDeductionAmt(deductionAmtMap.get(paymentBean
                        .getCsBillId()) == null ? BigDecimal.ZERO
                                : deductionAmtMap.get(paymentBean.getCsBillId()));
                final BigDecimal passedamount = billregister.getPassedamount() == null ? BigDecimal.ZERO
                        : billregister.getPassedamount().setScale(2,
                                BigDecimal.ROUND_HALF_EVEN);
                paymentBean.setNetAmt(passedamount.subtract(paymentBean
                        .getDeductionAmt() == null ? BigDecimal.ZERO
                                : paymentBean.getDeductionAmt()));
                paymentBean.setEarlierPaymentAmt(paidAmtMap.get(paymentBean
                        .getCsBillId()) == null ? BigDecimal.ZERO : paidAmtMap
                                .get(paymentBean.getCsBillId()));
                paymentBean.setPayableAmt(paymentBean.getNetAmt().subtract(
                        paymentBean.getEarlierPaymentAmt()));
                paymentBean.setPaymentAmt(paymentBean.getPayableAmt());
                if (paymentBean.getPaymentAmt().compareTo(BigDecimal.ZERO) == 0)
                    continue;
                if (billregister.getEgBillregistermis().getFund() != null)
                    paymentBean.setFundName(billregister.getEgBillregistermis()
                            .getFund().getName());
                if (billregister.getEgBillregistermis().getEgDepartment() != null)
                    paymentBean.setDeptName(billregister.getEgBillregistermis()
                            .getEgDepartment().getName());
                if (billregister.getEgBillregistermis().getScheme() != null)
                    paymentBean.setSchemeName(billregister
                            .getEgBillregistermis().getScheme().getName());
                if (billregister.getEgBillregistermis().getSubScheme() != null)
                    paymentBean.setSubschemeName(billregister
                            .getEgBillregistermis().getSubScheme().getName());
                if (billregister.getEgBillregistermis().getFunctionaryid() != null)
                    paymentBean.setFunctionaryName(billregister
                            .getEgBillregistermis().getFunctionaryid()
                            .getName());
                if (billregister.getEgBillregistermis().getFunction() != null)
                    paymentBean.setFunctionName(billregister
                            .getEgBillregistermis().getFunction().getName());
                if (billregister.getEgBillregistermis().getFundsource() != null)
                    paymentBean.setFundsourceName(billregister
                            .getEgBillregistermis().getFundsource().getName());
                if (billregister.getEgBillregistermis().getFieldid() != null)
                    paymentBean.setFieldName(billregister
                            .getEgBillregistermis().getFieldid().getName());
                if (billregister.getEgBillregistermis().getVoucherHeader() != null) {
                    paymentBean.setBillVoucherNumber(billregister
                            .getEgBillregistermis().getVoucherHeader()
                            .getVoucherNumber());
                    paymentBean.setBillVoucherDate(billregister
                            .getEgBillregistermis().getVoucherHeader()
                            .getVoucherDate());
                    paymentBean.setBillVoucherId(billregister
                            .getEgBillregistermis().getVoucherHeader()
                            .getId());
                }
                if (billregister.getEgBillregistermis().getEgBillSubType() != null)
                    if (billregister.getEgBillregistermis().getEgBillSubType()
                            .getName().equalsIgnoreCase("TNEB")) {
                        final String region = (String) persistenceService
                                .find("select region from EBDetails where egBillregister.id = ?",
                                        billregister.getId());

                        if (region != null)
                            paymentBean.setRegion(region);
                    }

                contractorList.add(paymentBean);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getCSList.");
        return contractorList;
    }

    public String getPayeeNameForCBill(final EgBillregister bill) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPayeeNameForCBill...");
        List<Object[]> obj = null;
        String payeeName = "";
        // check the payeedetails for payable code
        obj = persistenceService.findAllByNamedQuery(
                "getPayeeDetailsForPayableCode", bill.getId(),
                cBillGlcodeIdList);
        if (obj == null || obj.size() == 0) {
            // check the payeedetails for debit code
            obj = persistenceService.findAllByNamedQuery(
                    "getPayeeDetailsForDebitCode", bill.getId());
            if (obj == null || obj.size() == 0)
                payeeName = bill.getEgBillregistermis().getPayto();
            else if (obj.size() > 1)
                payeeName = FinancialConstants.MULTIPLE;
            else
                payeeName = bill.getEgBillregistermis().getPayto();
        } else if (obj.size() > 1)
            payeeName = FinancialConstants.MULTIPLE;
        else
            payeeName = bill.getEgBillregistermis().getPayto();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPayeeNameForCBill.");
        return payeeName;
    }

    @Transactional
    public Paymentheader createPaymentHeader(
            final CVoucherHeader voucherHeader, final Integer bankaccountId,
            final String type, final BigDecimal amount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting createPaymentHeader...");
        Paymentheader paymentheader = new Paymentheader();
        paymentheader.setType(type);
        paymentheader.setVoucherheader(voucherHeader);
        final Bankaccount bankaccount = (Bankaccount) getSession().load(
                Bankaccount.class, bankaccountId.longValue());
        paymentheader.setBankaccount(bankaccount);
        paymentheader.setPaymentAmount(amount);
        applyAuditing(paymentheader);
        create(paymentheader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed createPaymentHeader.");
        return paymentheader;
    }

    public Paymentheader getPaymentHeaderByVoucherHeaderId(Long voucherHeaderId) {
        Paymentheader paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where voucherheader.id=?",
                voucherHeaderId);

        return paymentheader;
    }

    public Paymentheader updatePaymentHeader(final Paymentheader paymentheader,
            final CVoucherHeader voucherHeader, final Integer bankaccountId,
            final String type, final BigDecimal amount) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting updatePaymentHeader...");
        paymentheader.setType(type);
        paymentheader.setVoucherheader(voucherHeader);
        final Bankaccount bankaccount = (Bankaccount) persistenceService.find(
                "from Bankaccount where id=?", bankaccountId);
        paymentheader.setBankaccount(bankaccount);
        paymentheader.setPaymentAmount(amount);
        // persistenceService.setType(Paymentheader.class);
        persistenceService.persist(paymentheader);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed updatePaymentHeader.");
        return paymentheader;
    }

    public List<ChequeAssignment> getPaymentVoucherForRemittanceRTGSInstrument(
            final Map<String, String[]> parameters,
            final CVoucherHeader voucherHeader) throws ApplicationException,
            ParseException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getPaymentVoucherNotInInstrument...");

        List<ChequeAssignment> chequeAssignmentList = new ArrayList<ChequeAssignment>();
        boolean nonSubledger = false;

        final StringBuffer sql = new StringBuffer();
        if (!"".equals(parameters.get("fromDate")[0]))
            sql.append(" and vh.voucherDate>='"
                    + sdf.format(formatter.parse(parameters.get("fromDate")[0]))
                    + "' ");
        if (!"".equals(parameters.get("toDate")[0]))
            sql.append(" and vh.voucherDate<='"
                    + sdf.format(formatter.parse(parameters.get("toDate")[0]))
                    + "'");
        if (!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
            sql.append(" and vh.voucherNumber like '%"
                    + voucherHeader.getVoucherNumber() + "%'");
        if (voucherHeader.getFundId() != null)
            sql.append(" and vh.fundId=" + voucherHeader.getFundId().getId());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            sql.append(" and vmis.fundsourceId="
                    + voucherHeader.getVouchermis().getFundsource().getId());
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and vmis.departmentid="
                    + voucherHeader.getVouchermis().getDepartmentid().getId());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vmis.schemeid="
                    + voucherHeader.getVouchermis().getSchemeid().getId());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and vmis.subschemeid="
                    + voucherHeader.getVouchermis().getSubschemeid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and vmis.functionaryid="
                    + voucherHeader.getVouchermis().getFunctionary().getId());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and vmis.divisionid="
                    + voucherHeader.getVouchermis().getDivisionid().getId());
        if (parameters.get("bankaccount") != null
                && !parameters.get("bankaccount")[0].equals("-1")) {
            sql.append(" and ph.bankaccountnumberid="
                    + parameters.get("bankaccount")[0]);
            sql.append(" and lower(ph.type)=lower('"
                    + parameters.get("paymentMode")[0] + "')");
            sql.append(" and ph.bankaccountnumberid=ba.id");
        } else
            sql.append(" and ph.bankaccountnumberid=ba.id")
                    // TODO this is hardcode to rtgs (read from financials Constants)
                    .append(" and lower(ph.type)=lower('"
                            + parameters.get("paymentMode")[0] + "')");

        if (!"0".equals(parameters.get("drawingOfficerId")[0]))
            sql.append(" and ph.drawingofficer_id = "
                    + new Long(parameters.get("drawingOfficerId")[0]));
        if (!"".equals(parameters.get("recoveryId")[0])) {
            final Recovery recovery = (Recovery) persistenceService.find(
                    "from Recovery where id=?",
                    new Long(parameters.get("recoveryId")[0]));
            if (recovery.getChartofaccounts().getChartOfAccountDetails()
                    .isEmpty())
                nonSubledger = true;
            sql.append(" and gl.glcodeid = "
                    + recovery.getChartofaccounts().getId());
        } else
            sql.append(" and gl.glcodeid in (select distinct glcodeid from tds where remittance_mode='A')");
        sql.append(" and vmis.departmentid     =dept.id  ");
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");
        final String approvedstatus = appList.get(0).getValue();
        final List<String> descriptionList = new ArrayList<String>();
        descriptionList.add("New");
        descriptionList.add("Reconciled");
        final List<EgwStatus> egwStatusList = egwStatusDAO
                .getStatusListByModuleAndCodeList("Instrument", descriptionList);
        String statusId = "";
        for (final EgwStatus egwStatus : egwStatusList)
            statusId = statusId + egwStatus.getId() + ",";
        statusId = statusId.substring(0, statusId.length() - 1);

        persistenceService.find(" from Bankaccount where id=?",
                Long.valueOf(parameters.get("bankaccount")[0]));
        Query query = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("statusId -- > " + statusId);

        chequeList = new ArrayList<ChequeAssignment>();

        if (voucherHeader.getName() == null
                || !voucherHeader.getName().equalsIgnoreCase(
                        FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE))
            // / Only for bill payment screen
            if (nonSubledger) {
            query = getSession()
                    .createSQLQuery(
                            " select  vh.id as voucherid ,vh.voucherNumber as voucherNumber ,"
                                    + " dept.name   AS departmentName, vh.voucherDate as voucherDate,"
                                    + "  recovery.remitted as paidTo,sum(misbill.paidamount) as paidAmount,current_date as chequeDate"
                                    + " , ba.accountnumber   AS bankAccNumber, ba.id  AS bankAccountId ,"
                                    + " gl.glcodeid as glcodeId,"
                                    + " CONCAT(CONCAT(DO.name,'/'),do.tan) AS drawingOfficerNameTAN "
                                    + " from Paymentheader ph, eg_department dept,"
                                    + " bankaccount ba, voucherheader vh   LEFT JOIN "
                                    + " EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,"
                                    + " vouchermis vmis, Miscbilldetail misbill ,generalledger gl,eg_drawingofficer do,tds recovery"
                                    + " where recovery.type = '"
                                    + parameters.get("recoveryCode")[0]
                                    + "' and ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id "
                                    + " and vh.id= gl.voucherheaderid and ph.drawingofficer_id= do.id "
                                    + " and vh.status ="
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " and  IV.VOUCHERHEADERID IS NULL  and vh.type='"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' "
                                    + " and vh.name = '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "' "
                                    + " group by vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,misbill.paidto, "
                                    + " ba.accountnumber, ba.id ,"
                                    + " gl.glcodeid,DO.name,do.tan,recovery.remitted "
                                    + " order by ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("glcodeId", LongType.INSTANCE)
                    .addScalar("drawingOfficerNameTAN")
                    .setResultTransformer(
                            Transformers
                                    .aliasToBean(ChequeAssignment.class));
            // TODO Changet the debug statement to appropriate sentence
            if (LOGGER.isDebugEnabled())
            LOGGER.debug(" for non salary and remittance" + query);
            chequeAssignmentList = query.list();
            // below one handles
            // assign-->surrendar-->assign-->surrendar-->.......
            if (LOGGER.isDebugEnabled())
            LOGGER.debug("checking  cheque assigned and surrendard");
            query = getSession()
                    .createSQLQuery(
                            "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,"
                                    + " dept.name   AS departmentName, vh.voucherDate as voucherDate, recovery.remitted as paidTo"
                                    + " ,sum(misbill.paidamount) as paidAmount,current_date as chequeDate , ba.accountnumber AS bankAccNumber "
                                    + " , ba.id  AS bankAccountId , "
                                    + " gl.glcodeid as glcodeId,"
                                    + " CONCAT(CONCAT(DO.name,'/'),do.tan) AS drawingOfficerNameTAN "
                                    + " from Paymentheader ph,eg_department dept, bankaccount ba,"
                                    + " generalledger gl,eg_drawingofficer do, voucherheader vh   LEFT "
                                    + " JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH "
                                    + " ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill,tds recovery"
                                    + ",(select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) as table1 "
                                    + " where recovery.type = '"
                                    + parameters.get("recoveryCode")[0]
                                    + "' and  ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id "
                                    + " and vh.status ="
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " and  IV.VOUCHERHEADERID IS NOT  NULL and iv.instrumentheaderid=table1.maxihid "
                                    + " and  table1.iv1vhid=vh.id and ih.id_status not in ("
                                    + statusId
                                    + ") "
                                    + " and vh.type='"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' and vh.name = '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "'"
                                    + " and vh.id= gl.voucherheaderid "
                                    + " and ph.drawingofficer_id= do.id "
                                    + " group by   vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,misbill.paidto,ba.accountnumber,"
                                    + " ba.id ,"
                                    + " gl.glcodeid,DO.name,do.tan,recovery.remitted  order by ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("glcodeId", LongType.INSTANCE)
                    .addScalar("drawingOfficerNameTAN")
                    .setResultTransformer(
                            Transformers
                                    .aliasToBean(ChequeAssignment.class));
            if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Surrendered rtgs nos" + query);
            chequeAssignmentList.addAll(query.list());

            } else {
            query = getSession()
                    .createSQLQuery(
                            " select  vh.id as voucherid ,vh.voucherNumber as voucherNumber ,"
                                    + " dept.name   AS departmentName, vh.voucherDate as voucherDate,"
                                    + " misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,current_date as chequeDate"
                                    + " , ba.accountnumber   AS bankAccNumber, ba.id  AS bankAccountId ,"
                                    + " gl.glcodeid as glcodeId,"
                                    + " CONCAT(CONCAT(DO.name,'/'),do.tan) AS drawingOfficerNameTAN "
                                    + " from Paymentheader ph, eg_department dept,"
                                    + " bankaccount ba, voucherheader vh   LEFT JOIN "
                                    + " EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID,"
                                    + " vouchermis vmis, Miscbilldetail misbill ,  generalledgerdetail gld,generalledger gl,eg_drawingofficer do"
                                    + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id "
                                    + " and vh.id= gl.voucherheaderid and gl.id=gld.generalledgerid and ph.drawingofficer_id= do.id "
                                    + " and vh.status ="
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " and  IV.VOUCHERHEADERID IS NULL  and vh.type='"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' "
                                    + " and vh.name = '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "' "
                                    + " group by vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,misbill.paidto, "
                                    + " ba.accountnumber, ba.id ,"
                                    + " gl.glcodeid,DO.name,do.tan "
                                    + " order by ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("glcodeId", LongType.INSTANCE)
                    .addScalar("drawingOfficerNameTAN")
                    .setResultTransformer(
                            Transformers
                                    .aliasToBean(ChequeAssignment.class));
            // TODO Changet the debug statement to appropriate sentence
            if (LOGGER.isDebugEnabled())
            LOGGER.debug(" for non salary and remittance" + query);
            chequeAssignmentList = query.list();
            // below one handles
            // assign-->surrendar-->assign-->surrendar-->.......
            if (LOGGER.isDebugEnabled())
            LOGGER.debug("checking  cheque assigned and surrendard");
            query = getSession()
                    .createSQLQuery(
                            "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,"
                                    + " dept.name   AS departmentName, vh.voucherDate as voucherDate, misbill.paidto as         paidTo"
                                    + " ,sum(misbill.paidamount) as paidAmount,current_date as chequeDate , ba.accountnumber AS bankAccNumber "
                                    + " , ba.id  AS bankAccountId , "
                                    + " gl.glcodeid as glcodeId,"
                                    + " CONCAT(CONCAT(DO.name,'/'),do.tan) AS drawingOfficerNameTAN "
                                    + " from Paymentheader ph,eg_department dept, bankaccount ba,"
                                    + " generalledgerdetail gld,generalledger gl,eg_drawingofficer do, voucherheader vh   LEFT "
                                    + " JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID LEFT JOIN EGF_INSTRUMENTHEADER IH "
                                    + " ON IV.INSTRUMENTHEADERID=IH.ID,vouchermis vmis, Miscbilldetail misbill "
                                    + ",(select max(iv1.instrumentheaderid) as maxihid,iv1.voucherheaderid as iv1vhid from egf_instrumentvoucher iv1 group by iv1.voucherheaderid) as table1"
                                    + " where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id "
                                    + " and vh.status ="
                                    + approvedstatus
                                    + " "
                                    + sql
                                    + " "
                                    + " and  IV.VOUCHERHEADERID IS NOT  NULL and iv.instrumentheaderid=table1.maxihid "
                                    + " and  table1.iv1vhid=vh.id and ih.id_status not in ("
                                    + statusId
                                    + ") "
                                    + " and vh.type='"
                                    + FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT
                                    + "' and vh.name = '"
                                    + FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE
                                    + "'"
                                    + " and vh.id= gl.voucherheaderid and gl.id=gld.generalledgerid  and gl.glcodeid in (select distinct glcodeid from tds where remittance_mode='A')"
                                    + " and ph.drawingofficer_id= do.id "
                                    + " group by   vh.id,  vh.voucherNumber,  dept.name ,  vh.voucherDate,misbill.paidto,ba.accountnumber,"
                                    + " ba.id ,"
                                    + " gl.glcodeid,DO.name,do.tan  order by ba.id,dept.name,vh.voucherNumber ")
                    .addScalar("voucherid", LongType.INSTANCE)
                    .addScalar("voucherNumber")
                    .addScalar("departmentName")
                    .addScalar("voucherDate")
                    .addScalar("paidTo")
                    .addScalar("paidAmount", BigDecimalType.INSTANCE)
                    .addScalar("chequeDate")
                    .addScalar("bankAccNumber")
                    .addScalar("bankAccountId", LongType.INSTANCE)
                    .addScalar("glcodeId", LongType.INSTANCE)
                    .addScalar("drawingOfficerNameTAN")
                    .setResultTransformer(
                            Transformers
                                    .aliasToBean(ChequeAssignment.class));
            if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Surrendered rtgs nos" + query);
            chequeAssignmentList.addAll(query.list());
            }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed getPaymentVoucherNotInInstrument.");
        return chequeAssignmentList;
    }

    @SkipValidation
    public List getPayeeDetailsForExpenseBill(
            final Map<String, String[]> parameters,
            final CVoucherHeader voucherHeader) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getPayeeDetailsForExpenseBill...");

        return new ArrayList(); //

    }

    /**
     *
     * @param paymentVoucherId
     * @return updates the generalledger remittancedate to null of bill voucher when remittance payment is cancelled
     */
    public int backUpdateRemittanceDateInGL(final Long paymentVoucherId) {
        int count;
        final String sql = "update   generalledger set remittancedate=null where remittancedate is  not null and  id in "
                + "(select generalledgerid from generalledgerdetail where id in "
                + "(select gldtlid from eg_remittance_gldtl where  id in"
                + " (select remittancegldtlid from eg_remittance_detail where remittanceid in"
                + " (select id from eg_remittance where paymentvhid="
                + paymentVoucherId + "))))";

        count = getSession().createSQLQuery(sql).executeUpdate();
        if (count == 0) {
            // This is for non control codes
            final String sql1 = " update generalledger set remittancedate = null where id in "
                    + " (select generalledgerid from eg_remittance_detail where remittanceid in "
                    + " (select id from eg_remittance where paymentvhid ="
                    + paymentVoucherId + "))";
            count = getSession().createSQLQuery(sql1).executeUpdate();
        }
        return count;
    }

    public Position getSuperiourPositionByPosition() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getSuperiourPositionByPosition...");
        return eisCommonService.getSuperiorPositionByObjectTypeAndPositionFrom(
                objectTypeService.getObjectTypeByName("Payment").getId(),
                getPosition().getId());
    }

    public Position getPosition() throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getPosition...");
        return eisCommonService.getPositionByUserId(ApplicationThreadLocals
                .getUserId());
    }

    public String getFunctionaryAndDesignation() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getFunctionaryAndDesignation...");
        final Assignment assignment = getAssignment();
        return assignment.getFunctionary().getName() + "-"
                + assignment.getDesignation().getName();
    }

    public Assignment getAssignment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getAssignment...");
        // TODO: Now employee is extending user so passing userid to get
        // assingment -- changes done by Vaibhav
        return eisCommonService.getLatestAssignmentForEmployeeByToDate(
                ApplicationThreadLocals.getUserId(), new Date());
    }

    public Position getPositionForEmployee(final Employee emp)
            throws ApplicationRuntimeException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getPositionForEmployee...");
        return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getId());
    }

    public PersonalInformation getEmpForCurrentUser() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getEmpForCurrentUser...");
        return eisCommonService.getEmployeeByUserId(ApplicationThreadLocals
                .getUserId());
    }

    public String getEmployeeNameForPositionId(final Position pos)
            throws ApplicationRuntimeException {
        Assignment assignment = assignmentService.getAssignmentsForPosition(
                +pos.getId(), new Date()).get(0);
        return assignment.getEmployee().getName() + " ("
                + assignment.getDesignation().getName() + ")";
    }

    public void finalApproval(final Long voucherid) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside finalApproval...");
        billsAccountingService.createVoucherfromPreApprovedVoucher(voucherid);
    }

    public void setBillsAccountingService(
            final BillsAccountingService billsAccountingService) {
        this.billsAccountingService = billsAccountingService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void setChequeService(final ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    public List<InstrumentVoucher> getInstVoucherList() {
        return instVoucherList;
    }

    public void setInstVoucherList(final List<InstrumentVoucher> instVoucherList) {
        this.instVoucherList = instVoucherList;
    }

    public FundFlowService getFundFlowService() {
        return fundFlowService;
    }

    public void setFundFlowService(final FundFlowService fundFlowService) {
        this.fundFlowService = fundFlowService;
    }

    public void setChequeAssignmentService(
            final ChequeAssignmentService chequeAssignmentService) {
        this.chequeAssignmentService = chequeAssignmentService;
    }

    public void setPersistenceService(
            final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public CreateVoucher getCreateVoucher() {
        return createVoucher;
    }

    public void setCreateVoucher(final CreateVoucher createVoucher) {
        this.createVoucher = createVoucher;
    }

}