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
/**
 *
 */
package org.egov.egf.web.actions.payment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Vouchermis;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.CommonBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.WorkflowBean;
import org.egov.payment.services.PaymentActionHelper;
import org.egov.services.contra.ContraService;
import org.egov.services.payment.MiscbilldetailService;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DatabaseConnectionException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @author mani
 */
@ParentPackage("egov")
@Results({
        @Result(name = DirectBankPaymentAction.NEW, location = "directBankPayment-" + DirectBankPaymentAction.NEW
                + ".jsp"),
        @Result(name = DirectBankPaymentAction.EDIT, location = "directBankPayment-" + DirectBankPaymentAction.EDIT
                + ".jsp"),
        @Result(name = "reverse", location = "directBankPayment-reverse.jsp"),
        @Result(name = "view", location = "directBankPayment-view.jsp") })
public class DirectBankPaymentAction extends BasePaymentAction {
    private final static String FORWARD = "Forward";
    private static final String FAILED_WHILE_REVERSING = "Failed while Reversing";
    private static final String FAILED = "Transaction failed";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
    private static final long serialVersionUID = 1L;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private CreateVoucher createVoucher;
    private PaymentService paymentService;
    @Autowired
    private PaymentActionHelper paymentActionHelper;
    private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
    public Map<String, String> modeOfPaymentMap;
    private static final String MDP_CHEQUE = FinancialConstants.MODEOFPAYMENT_CHEQUE;
    private static final String MDP_RTGS = FinancialConstants.MODEOFPAYMENT_RTGS;
    private static final String MDP_CASH = FinancialConstants.MODEOFPAYMENT_CASH;
    private String button;
    private VoucherService voucherService;
    private static final Logger LOGGER = Logger.getLogger(DirectBankPaymentAction.class);
    public static final String ZERO = "0";
    private static final String VIEW = "view";
    private static final String REVERSE = "reverse";
    private static final String REQUIRED = "required";
    private static final String PAYMENTID = "paymentid";
    private List<VoucherDetails> billDetailslist;
    private List<VoucherDetails> subLedgerlist;
    boolean showChequeNumber;
    private CommonBean commonBean;
    private Paymentheader paymentheader = new Paymentheader();
    public boolean showApprove = false;

    private Integer departmentId;
    private String wfitemstate;
    private String typeOfAccount;
    private List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();
    private BigDecimal balance;
    private ScriptService scriptService;
    private ChartOfAccounts chartOfAccounts;
    @Autowired
    @Qualifier("miscbilldetailService")
    private MiscbilldetailService miscbilldetailService;
    private String cutOffDate;
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        return voucherHeader;

    }

    @Override
    public void prepare() {
        super.prepare();
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK);
        modeOfPaymentMap = new LinkedHashMap<String, String>();
        modeOfPaymentMap.put(MDP_CHEQUE, getText(MDP_CHEQUE));
        modeOfPaymentMap.put(MDP_CASH, getText(MDP_CASH));
        modeOfPaymentMap.put(MDP_RTGS, getText(MDP_RTGS));

        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
        typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS + ","
                + FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
    }

    public void prepareNewform() {
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("accNumList", Collections.EMPTY_LIST);

    }

    @Override
    @SkipValidation
    @Action(value = "/payment/directBankPayment-newform")
    public String newform() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Resetting all........................... ");
        final List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty())
            try {
                date = df.parse(cutOffDateconfigValue.get(0).getValue());
                cutOffDate = formatter.format(date);
            } catch (final ParseException e) {

            }
        voucherHeader.reset();
        commonBean.reset();
        commonBean.setModeOfPayment(MDP_CHEQUE);
        voucherHeader.setVouchermis(new Vouchermis());
        // voucherHeader.getVouchermis().setDepartmentid((Department)paymentService.getAssignment().getDeptId());
        billDetailslist = new ArrayList<VoucherDetails>();
        billDetailslist.add(new VoucherDetails());
        subLedgerlist = new ArrayList<VoucherDetails>();
        subLedgerlist.add(new VoucherDetails());
        loadDefalutDates();
        // loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        if (getBankBalanceCheck() == null || "".equals(getBankBalanceCheck()))
            addActionMessage(getText("payment.bankbalance.controltype"));
        return NEW;
    }

    @Validations(requiredFields = { @RequiredFieldValidator(fieldName = "fundId", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "voucherNumber", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.bankId", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.accountNumberId", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.amount", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "voucherDate", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.documentNumber", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.documentDate", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.paidTo", message = "", key = REQUIRED) })
    @SkipValidation
    @ValidationErrorPage(value = NEW)
    @Action(value = "/payment/directBankPayment-create")
    public String create() {
        CVoucherHeader billVhId = null;
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        loadAjaxedDropDowns();
        removeEmptyRowsAccoutDetail(billDetailslist);
        removeEmptyRowsSubledger(subLedgerlist);
        final String voucherDate = formatter1.format(voucherHeader.getVoucherDate());
        String cutOffDate1 = null;
        try {
            if (!validateDBPData(billDetailslist, subLedgerlist)) {
                if (commonBean.getModeOfPayment().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("calling Validate RTGS");
                    validateRTGS();
                }

                if (showMode != null && showMode.equalsIgnoreCase("nonbillPayment"))
                    if (voucherHeader.getId() != null)
                        billVhId = persistenceService.getSession().load(CVoucherHeader.class,
                                voucherHeader.getId());
                voucherHeader.setId(null);
                populateWorkflowBean();
                paymentheader = paymentActionHelper.createDirectBankPayment(paymentheader, voucherHeader, billVhId,
                        commonBean, billDetailslist, subLedgerlist, workflowBean);
                showMode = "create";

                if (!cutOffDate.isEmpty() && cutOffDate != null)
                    try {
                        date = sdf.parse(cutOffDate);
                        cutOffDate1 = formatter1.format(date);
                    } catch (final ParseException e) {

                    }
                if (cutOffDate1 != null && voucherDate.compareTo(cutOffDate1) <= 0
                        && FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                    if (paymentheader.getVoucherheader().getVouchermis().getBudgetaryAppnumber() == null)
                        addActionMessage(getText("directbankpayment.transaction.success")
                                + paymentheader.getVoucherheader().getVoucherNumber());
                    else
                        addActionMessage(getText("directbankpayment.transaction.success")
                                + paymentheader.getVoucherheader().getVoucherNumber() + " and "
                                + getText("budget.recheck.sucessful", new String[] {
                                        paymentheader.getVoucherheader().getVouchermis().getBudgetaryAppnumber() }));
                } else {
                    if (paymentheader.getVoucherheader().getVouchermis().getBudgetaryAppnumber() == null)
                        addActionMessage(getText("directbankpayment.transaction.success")
                                + paymentheader.getVoucherheader().getVoucherNumber());
                    else
                        addActionMessage(getText("directbankpayment.transaction.success")
                                + paymentheader.getVoucherheader().getVoucherNumber() + " and "
                                + getText("budget.recheck.sucessful", new String[] {
                                        paymentheader.getVoucherheader().getVouchermis().getBudgetaryAppnumber() }));
                    addActionMessage(getText("payment.voucher.approved", new String[] { paymentService
                            .getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
                }
            } else
                throw new ValidationException(
                        Arrays.asList(new ValidationError("engine.validation.failed", "Validation Faild")));

        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;

        } finally {
            if (subLedgerlist.size() == 0)
                subLedgerlist.add(new VoucherDetails());
            // loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        }
        return VIEW;
    }

    // @ValidationErrorPage(value="/error/error,jsp")

    public void prepareNonBillPayment() {
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("accNumList", Collections.EMPTY_LIST);
        commonBean.setModeOfPayment(MDP_CHEQUE);
    }

    @ValidationErrorPage(value = NEW)
    @SkipValidation
    public String nonBillPayment() {
        voucherHeader = persistenceService.getSession().load(CVoucherHeader.class,
                voucherHeader.getId());
        final String vName = voucherHeader.getName();
        String appconfigKey = "";
        if (vName.equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL))
            appconfigKey = "worksBillPurposeIds";
        else if (vName.equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL))
            appconfigKey = "purchaseBillPurposeIds";
        else if (vName.equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL))
            appconfigKey = "salaryBillPurposeIds";
        final AppConfigValues appConfigValues = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, appconfigKey).get(0);
        final String purposeValue = appConfigValues.getValue();
        final CGeneralLedger netPay = (CGeneralLedger) persistenceService.find(
                "from CGeneralLedger where voucherHeaderId.id=? and glcodeId.purposeId=?", voucherHeader.getId(),
                purposeValue);
        if (netPay == null)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "net.payable.not.selected.or.selected.wrongly",
                    "Either Net payable code is not selected or wrongly selected in voucher .Payment creation Failed")));
        billDetailslist = new ArrayList<VoucherDetails>();
        subLedgerlist = new ArrayList<VoucherDetails>();
        final VoucherDetails vd = new VoucherDetails();
        vd.setGlcodeDetail(netPay.getGlcode());
        vd.setGlcodeIdDetail(netPay.getGlcodeId().getId());
        vd.setAccounthead(netPay.getGlcodeId().getName());
        vd.setDebitAmountDetail(BigDecimal.valueOf(netPay.getCreditAmount()));
        if (netPay.getFunctionId() != null) {
            vd.setFunctionIdDetail(Long.valueOf(netPay.getFunctionId()));
            final CFunction function = persistenceService.getSession().load(CFunction.class,
                    Long.valueOf(netPay.getFunctionId()));
            vd.setFunctionDetail(function.getId().toString());
        }
        commonBean.setAmount(BigDecimal.valueOf(netPay.getCreditAmount()));
        billDetailslist.add(vd);
        final Set<CGeneralLedgerDetail> generalLedgerDetails = netPay.getGeneralLedgerDetails();
        final int i = 0;
        for (final CGeneralLedgerDetail gldetail : generalLedgerDetails) {
            final VoucherDetails vdetails = new VoucherDetails();
            vdetails.setSubledgerCode(netPay.getGlcode());
            vdetails.setAmount(gldetail.getAmount());
            // vdetails.setDebitAmountDetail(vdetails.getAmount());
            vdetails.setGlcodeDetail(netPay.getGlcode());
            vdetails.setGlcode(netPay.getGlcodeId());
            vdetails.setSubledgerCode(netPay.getGlcode());
            vdetails.setAccounthead(netPay.getGlcodeId().getName());
            final Accountdetailtype detailType = persistenceService.getSession()
                    .load(Accountdetailtype.class, gldetail.getDetailTypeId());
            vdetails.setDetailTypeName(detailType.getName());
            vdetails.setDetailType(detailType);
            vdetails.setDetailKey(gldetail.getDetailKeyId().toString());
            vdetails.setDetailKeyId(gldetail.getDetailKeyId());

            final String table = detailType.getFullQualifiedName();
            Class<?> service;
            try {
                service = Class.forName(table);
            } catch (final ClassNotFoundException e1) {
                LOGGER.error(e1.getMessage(), e1);
                throw new ValidationException(
                        Arrays.asList(new ValidationError("application.error", "application.error")));
            }
            String simpleName = service.getSimpleName();
            // simpleName=simpleName.toLowerCase()+"Service";
            simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";
            final WebApplicationContext wac = WebApplicationContextUtils
                    .getWebApplicationContext(ServletActionContext.getServletContext());
            final PersistenceService entityPersistenceService = (PersistenceService) wac.getBean(simpleName);
            String dataType = "";
            try {
                final Class aClass = Class.forName(table);
                final java.lang.reflect.Method method = aClass.getMethod("getId");
                dataType = method.getReturnType().getSimpleName();
            } catch (final Exception e) {
                throw new ApplicationRuntimeException(e.getMessage());
            }
            EntityType entity = null;
            if (dataType.equals("Long"))
                entity = (EntityType) entityPersistenceService
                        .findById(Long.valueOf(gldetail.getDetailKeyId().toString()), false);
            else
                entity = (EntityType) entityPersistenceService.findById(gldetail.getDetailKeyId(), false);
            vdetails.setDetailCode(entity.getCode());
            vdetails.setDetailName(entity.getName());
            vdetails.setDetailKey(entity.getName());
            if (i == 0)
                commonBean.setPaidTo(entity.getName());
            subLedgerlist.add(vdetails);

        }
        if (subLedgerlist.size() == 0)
            subLedgerlist.add(new VoucherDetails());
        loadAjaxedDropDowns();
        return NEW;
    }

    private void validateRTGS() {
        {
            EntityType entity = null;
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            String type = null;
            // handle null
            if (subLedgerlist != null && !subLedgerlist.isEmpty())
                for (final VoucherDetails voucherDetail : subLedgerlist) {
                    try {
                        type = voucherDetail.getDetailTypeName();
                        entity = paymentService.getEntity(voucherDetail.getDetailType().getId(),
                                voucherDetail.getDetailKeyId());
                        if (entity == null)
                            throw new ValidationException(Arrays.asList(new ValidationError("No.entity.for.detailkey",
                                    "There is no entity defined for" + voucherDetail.getDetailCode(),
                                    new String[] { voucherDetail.getDetailCode() })));
                    } catch (final ApplicationException e) {
                        throw new ValidationException(
                                Arrays.asList(new ValidationError("Exception to get EntityType  ", e.getMessage())));
                    }
                    voucherDetail.setDetailType(persistenceService.getSession()
                            .load(Accountdetailtype.class, voucherDetail.getDetailType().getId()));

                    // type will be null in case of DBP
                    if (type.equalsIgnoreCase("Contractor") && (StringUtils.isBlank(entity.getPanno())
                            || StringUtils.isBlank(entity.getBankname()) || StringUtils.isBlank(entity.getBankaccount())
                            || StringUtils.isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError("paymentMode",
                                "BankName, BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
                                        + entity.getName()));
                        throw new ValidationException(errors);

                    } else if (type.equalsIgnoreCase("Supplier") && (StringUtils.isBlank(entity.getTinno())
                            || StringUtils.isBlank(entity.getBankname()) || StringUtils.isBlank(entity.getBankaccount())
                            || StringUtils.isBlank(entity.getIfsccode()))) {
                        LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                + entity.getName());
                        errors.add(new ValidationError("paymentMode",
                                "BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
                                        + entity.getName()));
                        throw new ValidationException(errors);
                    }

                    else if (StringUtils.isBlank(entity.getBankname()) || StringUtils.isBlank(entity.getBankaccount())
                            || StringUtils.isBlank(entity.getIfsccode())) {
                        LOGGER.error("BankAccount,IFSC Code is mandatory for RTGS Payment for " + entity.getName());
                        errors.add(new ValidationError("paymentMode",
                                "BankName, BankAccount,IFSC Code is mandatory for RTGS Payment for type " + type
                                        + " and Entity " + entity.getName()));
                        throw new ValidationException(errors);
                    }
                }
            else
                throw new ValidationException(
                        Arrays.asList(new ValidationError("no.subledger.cannot.create.rtgs.payment",
                                "There is no subledger selected cannot create RTGS Payment")));
        }

    }

    private void updateMiscBillDetail(final CVoucherHeader billVhId) {
        final Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService
                .find(" from Miscbilldetail where payVoucherHeader=?", voucherHeader);
        miscbillDetail.setBillnumber(commonBean.getDocumentNumber());
        miscbillDetail.setBilldate(commonBean.getDocumentDate());
        miscbillDetail.setBillVoucherHeader(billVhId);
        miscbillDetail.setBillamount(commonBean.getAmount());
        miscbillDetail.setPayVoucherHeader(voucherHeader);
        miscbillDetail.setPaidamount(commonBean.getAmount());
        miscbillDetail.setPassedamount(commonBean.getAmount());
        miscbillDetail.setPaidamount(commonBean.getAmount());
        miscbillDetail.setPaidto(commonBean.getPaidTo().trim());
        miscbilldetailService.persist(miscbillDetail);
    }

    @SkipValidation
    @Action(value = "/payment/directBankPayment-beforeView")
    public String beforeView() {
        prepareForViewModifyReverse();
        wfitemstate = "END"; // requird to hide the approver drop down when view
                             // is form source
        return VIEW;
    }

    @SkipValidation
    @Action(value = "/payment/directBankPayment-beforeEdit")
    public String beforeEdit() {
        prepareForViewModifyReverse();
        return EDIT;
    }

    @SkipValidation
    @Action(value = "/payment/directBankPayment-beforeReverse")
    public String beforeReverse() {
        prepareForViewModifyReverse();
        return REVERSE;
    }

    @SuppressWarnings("unchecked")
    private void prepareForViewModifyReverse() {
        final StringBuffer instrumentQuery = new StringBuffer(100);
        instrumentQuery
                .append("select  distinct ih from InstrumentHeader ih join ih.instrumentVouchers iv where iv.voucherHeaderId.id=?")
                .append(" order by ih.id");
        voucherHeader = persistenceService.getSession().load(CVoucherHeader.class,
                voucherHeader.getId());
        paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?",
                voucherHeader);
        commonBean.setAmount(paymentheader.getPaymentAmount());
        commonBean.setAccountNumberId(paymentheader.getBankaccount().getId().toString());
        commonBean.setAccnumnar(paymentheader.getBankaccount().getNarration());

        final String bankBranchId = paymentheader.getBankaccount().getBankbranch().getBank().getId() + "-"
                + paymentheader.getBankaccount().getBankbranch().getId();
        commonBean.setBankId(bankBranchId);
        commonBean.setModeOfPayment(paymentheader.getType());
        final Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService
                .find(" from Miscbilldetail where payVoucherHeader=?", voucherHeader);
        commonBean.setDocumentNumber(miscbillDetail.getBillnumber());
        commonBean.setDocumentDate(miscbillDetail.getBilldate());
        commonBean.setPaidTo(miscbillDetail.getPaidto());
        if (miscbillDetail.getBillVoucherHeader() != null) {
            commonBean.setDocumentId(miscbillDetail.getBillVoucherHeader().getId());
            commonBean.setLinkReferenceNumber(miscbillDetail.getBillVoucherHeader().getVoucherNumber());
        }

        final String bankGlcode = paymentheader.getBankaccount().getChartofaccounts().getGlcode();
        VoucherDetails bankdetail = null;
        final Map<String, Object> vhInfoMap = voucherService.getVoucherInfo(voucherHeader.getId());

        // voucherHeader =
        // (CVoucherHeader)vhInfoMap.get(Constants.VOUCHERHEADER);
        billDetailslist = (List<VoucherDetails>) vhInfoMap.get(Constants.GLDEATILLIST);
        subLedgerlist = (List<VoucherDetails>) vhInfoMap.get("subLedgerDetail");

        for (final VoucherDetails vd : billDetailslist)
            if (vd.getGlcodeDetail().equalsIgnoreCase(bankGlcode))
                bankdetail = vd;
        if (bankdetail != null)
            billDetailslist.remove(bankdetail);
        loadAjaxedDropDowns();
        // find it last so that rest of the data loaded
        if ("view".equalsIgnoreCase(showMode)) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("fetching cheque detail ------------------------");

            instrumentHeaderList = getPersistenceService().findAllBy(instrumentQuery.toString(),
                    paymentheader.getVoucherheader().getId());
        }
    }

    private void loadAjaxedDropDowns() {
        loadSchemeSubscheme();
        loadBankBranchForFund();
        loadBankAccountNumber(commonBean.getBankId());
        loadFundSource();
    }

    @SuppressWarnings("deprecation")
    @Action(value = "/payment/directBankPayment-edit")
    public String edit() throws SQLException {
        CVoucherHeader billVhId = null;
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        removeEmptyRowsAccoutDetail(billDetailslist);
        removeEmptyRowsSubledger(subLedgerlist);
        validateFields();
        voucherHeader = voucherService.updateVoucherHeader(voucherHeader);

        try {
            if (!validateDBPData(billDetailslist, subLedgerlist)) {
                if (commonBean.getModeOfPayment().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                    validateRTGS();

                reCreateLedger();
                paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?",
                        voucherHeader);
                paymentService.updatePaymentHeader(paymentheader, voucherHeader,
                        Integer.valueOf(commonBean.getAccountNumberId()), commonBean.getModeOfPayment(),
                        commonBean.getAmount());
                if (commonBean.getDocumentId() != null)
                    billVhId = persistenceService.getSession().load(CVoucherHeader.class,
                            commonBean.getDocumentId());
                updateMiscBillDetail(billVhId);
                sendForApproval();
                addActionMessage(getText("directbankpayment.transaction.success") + voucherHeader.getVoucherNumber());
            } else
                throw new ValidationException(
                        Arrays.asList(new ValidationError("engine.validation.failed", "Validation Faild")));
        } catch (final NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }

        finally {
            if (subLedgerlist.size() == 0)
                subLedgerlist.add(new VoucherDetails());
            loadAjaxedDropDowns();
        }

        return VIEW;
    }

    @ValidationErrorPage("reverse")
    public String reverse() {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CVoucherHeader reversalVoucher = null;
        final HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
        reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
        reversalVoucherMap.put("Reversal voucher type", "Receipt");
        // what should be the name
        reversalVoucherMap.put("Reversal voucher name", "Direct");
        try {
            reversalVoucherMap.put("Reversal voucher date", sdf.parse(getReversalVoucherDate()));
        } catch (final ParseException e1) {
            throw new ValidationException(
                    Arrays.asList(new ValidationError("reversalVocuherDate", "reversalVocuherDate.notinproperformat")));
        }
        reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
        final List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
        reversalList.add(reversalVoucherMap);
        try {
            reversalVoucher = createVoucher.reverseVoucher(reversalList);
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(
                    Arrays.asList(new ValidationError(FAILED_WHILE_REVERSING, FAILED_WHILE_REVERSING)));
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(
                    Arrays.asList(new ValidationError("Date is not in proper Format", "Date is not in proper Format")));
        }
        loadAjaxedDropDowns();
        addActionMessage(getText("directbankpayment.reverse.transaction.success") + reversalVoucher.getVoucherNumber());
        // phoenix migration voucherHeader.setId(reversalVoucher.getId());
        return REVERSE;
    }

    private void reCreateLedger() {
        try {
            createVoucher.deleteVoucherdetailAndGL(voucherHeader);
            persistenceService.getSession().flush();
            HashMap<String, Object> detailMap = null;

            // HashMap<String, Object> headerMap = null;
            HashMap<String, Object> subledgertDetailMap = null;
            final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            /*
             * headerMap = new HashMap<String, Object>(); // Adding function to the map for the mandatiry field
             * //createVoucher.validateFunction(headerMap,billDetailslist);
             * //createVoucher.validateFunction(headerMap,subLedgerlist);
             */
            detailMap.put(VoucherConstant.CREDITAMOUNT, commonBean.getAmount().toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
            final Bankaccount account = persistenceService.getSession().load(Bankaccount.class,
                    Integer.valueOf(commonBean.getAccountNumberId()));
            detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
            // Addind this line since function code is made mandatory for all
            // transaction
            if (voucherHeader.getVouchermis().getFunction() != null)
                detailMap.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
            accountdetails.add(detailMap);
            final Map<String, Object> glcodeMap = new HashMap<String, Object>();

            for (final VoucherDetails voucherDetail : billDetailslist) {
                detailMap = new HashMap<String, Object>();
                if (voucherDetail.getFunctionIdDetail() != null) {
                    final CFunction function = persistenceService.getSession().load(CFunction.class,
                            voucherDetail.getFunctionIdDetail());
                    detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
                }
                if (voucherHeader.getVouchermis().getFunction() != null
                        && !voucherHeader.getVouchermis().getFunction().equals("0"))
                    detailMap.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
                if (voucherDetail.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0) {

                    detailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getDebitAmountDetail().toString());
                    detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
                    detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
                    accountdetails.add(detailMap);
                    glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.DEBIT);
                } else {
                    detailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getCreditAmountDetail().toString());
                    detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
                    detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
                    accountdetails.add(detailMap);
                    glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.CREDIT);
                }
            }

            for (final VoucherDetails voucherDetail : subLedgerlist) {
                subledgertDetailMap = new HashMap<String, Object>();
                final String amountType = glcodeMap.get(voucherDetail.getSubledgerCode()) != null
                        ? glcodeMap.get(voucherDetail.getSubledgerCode()).toString() : null; // Debit
                                                                                             // or
                                                                                             // Credit.
                if (null != amountType && amountType.equalsIgnoreCase(VoucherConstant.DEBIT))
                    subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getAmount());
                else if (null != amountType)
                    subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getAmount());
                subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, voucherDetail.getDetailType().getId());
                subledgertDetailMap.put(VoucherConstant.DETAILKEYID, voucherDetail.getDetailKeyId());
                subledgertDetailMap.put(VoucherConstant.GLCODE, voucherDetail.getSubledgerCode());
                subledgerDetails.add(subledgertDetailMap);
            }

            // createVoucher.validateFunction(accountdetails,subledgerDetails);
            final List<Transaxtion> transactions = createVoucher.createTransaction(null, accountdetails,
                    subledgerDetails, voucherHeader);
            persistenceService.getSession().flush();

            Transaxtion txnList[] = new Transaxtion[transactions.size()];
            txnList = transactions.toArray(txnList);
            final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
            if (!chartOfAccounts.postTransaxtions(txnList, formatter.format(voucherHeader.getVoucherDate())))
                throw new ValidationException(
                        Arrays.asList(new ValidationError("Exception While Saving Data", "Transaction Failed")));
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final DatabaseConnectionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final TaskFailedException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        }

    }

    protected boolean validateDBPData(final List<VoucherDetails> billDetailslist,
            final List<VoucherDetails> subLedgerList) {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        totalCrAmt = totalCrAmt.add(commonBean.getAmount());
        int index = 0;
        boolean isValFailed = false;
        // isValFailed = validateOnlyRTGS();
        for (final VoucherDetails voucherDetails : billDetailslist) {
            index = index + 1;
            totalDrAmt = totalDrAmt.add(voucherDetails.getDebitAmountDetail());
            totalCrAmt = totalCrAmt.add(voucherDetails.getCreditAmountDetail());
            if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && voucherDetails.getGlcodeDetail().trim().length() == 0) {

                addActionError(getText("journalvoucher.accdetail.emptyaccrow", new String[] { "" + index }));
                isValFailed = true;
            } else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && voucherDetails.getGlcodeDetail().trim().length() != 0) {
                addActionError(getText("journalvoucher.accdetail.amountZero",
                        new String[] { voucherDetails.getGlcodeDetail() }));
                isValFailed = true;
            } else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
                    && voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0) {
                addActionError(
                        getText("journalvoucher.accdetail.amount", new String[] { voucherDetails.getGlcodeDetail() }));
                isValFailed = true;
            } else if ((voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
                    || voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
                    && voucherDetails.getGlcodeDetail().trim().length() == 0) {
                addActionError(getText("journalvoucher.accdetail.accmissing", new String[] { "" + index }));
                isValFailed = true;
            }

        }
        if (totalDrAmt.compareTo(totalCrAmt) != 0 && !isValFailed) {
            addActionError(getText("journalvoucher.accdetail.drcrmatch"));
            isValFailed = true;
        }

        else if (!isValFailed)
            isValFailed = validateSubledgerDetails(billDetailslist, subLedgerList);

        return isValFailed;
    }

    /*
     * private boolean validateOnlyRTGS() { boolean isValFailed = false; final String paymentRestrictionDateForCJV =
     * paymentService.getAppConfDateValForCJVPaymentModeRTGS(); Date rtgsModeRestrictionDateForCJV = null; try {
     * rtgsModeRestrictionDateForCJV = formatter.parse(paymentRestrictionDateForCJV); } catch (final ParseException e) { // TODO
     * Auto-generated catch block } if (voucherHeader.getVoucherDate().after(rtgsModeRestrictionDateForCJV) &&
     * !commonBean.getModeOfPayment().equalsIgnoreCase(MDP_RTGS)) { EntityType entity = null; new ArrayList<ValidationError>();
     * Relation rel = null; String type = null; if (subLedgerlist != null && !subLedgerlist.isEmpty()) for (final VoucherDetails
     * voucherDetail : subLedgerlist) { try { type = voucherDetail.getDetailTypeName(); entity =
     * paymentService.getEntity(voucherDetail.getDetailType().getId(), voucherDetail.getDetailKeyId()); if (entity == null) throw
     * new ValidationException(Arrays.asList(new ValidationError("No.entity.for.detailkey", "There is no entity defined for" +
     * voucherDetail.getDetailCode(), new String[] { voucherDetail.getDetailCode() }))); } catch (final ApplicationException e) {
     * throw new ValidationException(Arrays.asList(new ValidationError("Exception to get EntityType  ", e .getMessage()))); }
     * voucherDetail.setDetailType((Accountdetailtype) persistenceService.getSession().load( Accountdetailtype.class,
     * voucherDetail.getDetailType() .getId())); if (voucherDetail.getDetailType().getName().equalsIgnoreCase("creditor")) { rel =
     * (Relation) entity; type = rel.getRelationtype().getName(); } if (type.equalsIgnoreCase("Supplier") ||
     * type.equalsIgnoreCase("Contractor")) { isValFailed = true; throw new ValidationException( Arrays.asList(new
     * ValidationError(
     * "Payment Mode of any bill having Contractor/Supplier subledger should  RTGS For Bill Date Greater than 01-Oct-2013" ,
     * "Payment Mode of any bill having Contractor/Supplier subledger should  RTGS For Bill Date Greater than 01-Oct-2013" ))); }
     * } } return isValFailed; }
     */

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    @Action(value = "/payment/directBankPayment-viewInboxItem")
    public String viewInboxItem() {
        paymentheader = getPayment();
        showApprove = true;
        if (paymentheader.getVoucherheader() != null)
            voucherHeader.setId(paymentheader.getVoucherheader().getId());
        prepareForViewModifyReverse();
        return VIEW;

    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    @Action(value = "/payment/directBankPayment-sendForApproval")
    public String sendForApproval() {

        if (paymentheader.getId() == null)
            paymentheader = getPayment();

        populateWorkflowBean();
        paymentheader = paymentActionHelper.sendForApproval(paymentheader, workflowBean);

        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.rejected", new String[] {
                    paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
        if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.approved", new String[] {
                    paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
        if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.cancelled"));
        else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if ("Closed".equals(paymentheader.getState().getValue()))
                addActionMessage(getText("payment.voucher.final.approval"));
            else
                addActionMessage(getText("payment.voucher.approved", new String[] {
                        paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
            setAction(workflowBean.getWorkFlowAction());

        }
        showMode = "view";
        return viewInboxItem();
    }

    @Override
    public List<String> getValidActions() {
        List<String> validActions = Collections.emptyList();
        final List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        if (cutOffDateconfigValue != null && !cutOffDateconfigValue.isEmpty()) {
            if (null == paymentheader || null == paymentheader.getId()
                    || paymentheader.getCurrentState().getValue().endsWith("NEW"))
                validActions = Arrays.asList(FORWARD, FinancialConstants.CREATEANDAPPROVE);
            else if (paymentheader.getCurrentState() != null)
                validActions = customizedWorkFlowService.getNextValidActions(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        paymentheader.getCurrentState().getValue(), getPendingActions(),
                        paymentheader.getCreatedDate());
        } else if (null == paymentheader || null == paymentheader.getId()
                || paymentheader.getCurrentState().getValue().endsWith("NEW"))
            validActions = Arrays.asList(FORWARD);
        else if (paymentheader.getCurrentState() != null)
            validActions = customizedWorkFlowService.getNextValidActions(paymentheader.getStateType(),
                    getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                    paymentheader.getCurrentState().getValue(), getPendingActions(),
                    paymentheader.getCreatedDate());
        return validActions;
    }

    @Override
    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (paymentheader.getId() != null)
            if (paymentheader.getCurrentState() != null)
                wfMatrix = customizedWorkFlowService.getWfMatrix(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        paymentheader.getCurrentState().getValue(), getPendingActions(),
                        paymentheader.getCreatedDate());
            else
                wfMatrix = customizedWorkFlowService.getWfMatrix(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), paymentheader.getCreatedDate());
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    public Paymentheader getPayment() {
        String paymentid = null;
        paymentid = parameters.get(PAYMENTID)[0];
        if (paymentid != null)
            paymentheader = paymentService.findById(Long.valueOf(paymentid), false);
        if (paymentheader == null)
            paymentheader = new Paymentheader();

        return paymentheader;
    }

    @ValidationErrorPage(value = "beforeEdit")
    @SkipValidation
    public String cancelPayment() {
        voucherHeader = persistenceService.getSession().load(CVoucherHeader.class,
                voucherHeader.getId());
        paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?",
                voucherHeader);
        voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
        // persistenceService.setType(CVoucherHeader.class);
        paymentheader.transition().end();
        persistenceService.persist(voucherHeader);
        addActionMessage(getText("payment.cancel.success"));
        action = parameters.get(ACTIONNAME)[0];
        return beforeView();
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void setContraService(final ContraService contraService) {
    }

    public boolean isShowChequeNumber() {
        return showChequeNumber;
    }

    public void setShowChequeNumber(final boolean showChequeNumber) {
        this.showChequeNumber = showChequeNumber;
    }

    public List<VoucherDetails> getSubLedgerlist() {
        return subLedgerlist;
    }

    public void setSubLedgerlist(final List<VoucherDetails> subLedgerlist) {
        this.subLedgerlist = subLedgerlist;
    }

    public List<VoucherDetails> getBillDetailslist() {
        return billDetailslist;
    }

    public void setBillDetailslist(final List<VoucherDetails> billDetailslist) {
        this.billDetailslist = billDetailslist;
    }

    public void setCommonBean(final CommonBean commonBean) {
        this.commonBean = commonBean;
    }

    public CommonBean getCommonBean() {
        return commonBean;
    }

    public Map<String, String> getModeOfPaymentMap() {
        return modeOfPaymentMap;
    }

    public void setModeOfPaymentMap(final Map<String, String> modeOfPaymentMap) {
        this.modeOfPaymentMap = modeOfPaymentMap;
    }

    public String getButton() {
        return button;
    }

    public void setButton(final String button) {
        this.button = button;
    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public Paymentheader getPaymentheader() {
        return paymentheader;
    }

    public void setPaymentheader(final Paymentheader paymentheader) {
        this.paymentheader = paymentheader;
    }

    public void setPaymentWorkflowService(final SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public String getComments() {
        return getText("payment.comments", new String[] {
                paymentheader.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toPlainString() });
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(final String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public List<InstrumentHeader> getInstrumentHeaderList() {
        return instrumentHeaderList;
    }

    public void setInstrumentHeaderList(final List<InstrumentHeader> instrumentHeaderList) {
        this.instrumentHeaderList = instrumentHeaderList;
    }

    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(final ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    public ChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(final ChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(final WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    @Override
    public String getCurrentState() {
        return paymentheader.getState().getValue();
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(final String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }
}