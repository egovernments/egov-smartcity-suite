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
package org.egov.egf.web.actions.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.Vouchermis;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.advance.EgAdvanceReqPayeeDetails;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.advance.EgAdvanceRequisitionMis;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.CommonBean;
import org.egov.pims.commons.Designation;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Results({ @Result(name = AdvancePaymentAction.NEW, location = "advancePayment-" + AdvancePaymentAction.NEW + ".jsp"),
        @Result(name = "view", location = "advancePayment-view.jsp") })
public class AdvancePaymentAction extends BasePaymentAction {
    /**
     *
     */
    private static final long serialVersionUID = -9121375867863538525L;
    private static final Logger LOGGER = Logger.getLogger(AdvancePaymentAction.class);
    private EgAdvanceRequisition advanceRequisition = new EgAdvanceRequisition();
    private Paymentheader paymentheader;
    private PaymentService paymentService;
    private VoucherService voucherService;
    private SimpleWorkflowService<Paymentheader> paymentWorkflowService;
    private PersistenceService<Miscbilldetail, Long> miscbilldetailService;
    private Map<String, String> modeOfPaymentMap = new LinkedHashMap<String, String>();
    private EgovCommon egovCommon;
    @Autowired 
    @Qualifier("persistenceService") 
    private PersistenceService persistenceService;
    @Autowired
    CreateVoucher createVoucher;
    private Fund fund;
    private Long advanceRequisitionId;
    private String wfitemstate;
    private Integer departmentId;
    private CommonBean commonBean;
    private String paymentMode = FinancialConstants.MODEOFPAYMENT_CHEQUE;
    private String typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS + ","
            + FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
    private static final String PAYMENTID = "paymentid";
    private static final String CANCEL_ACTION = "cancel";
    private static final String REJECT_ACTION = "reject";
    private static final String ACTION_NAME = "actionName";
    private static final String VIEW = "view";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
    private static final String FAILED = "Transaction failed";
    private String description;
    public boolean showApprove = false;
    private BigDecimal balance;
    private ScriptService scriptService;

    @Autowired
    private EgovMasterDataCaching masterDataCache;

    @Override
    public void prepare() {
        super.prepare();
        if (advanceRequisitionId != null) {
            advanceRequisition = (EgAdvanceRequisition) persistenceService.find("from EgAdvanceRequisition where id=?",
                    advanceRequisitionId);
            populateFund();
            loadBankBranch(fund);
        }

        getModeOfPayment();
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
        addDropdownData("accountNumberList", Collections.EMPTY_LIST);
    }

    private void getModeOfPayment() {
        modeOfPaymentMap = new LinkedHashMap<String, String>();
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CHEQUE, getText(FinancialConstants.MODEOFPAYMENT_CHEQUE));
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_RTGS, getText(FinancialConstants.MODEOFPAYMENT_RTGS));
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CASH, getText(FinancialConstants.MODEOFPAYMENT_CASH));
    }

    @Override
    @SkipValidation
    @Action(value = "/payment/advancePayment-newform")
    public String newform() {
        voucherHeader.reset();
        commonBean.reset();
        commonBean.setModeOfPayment(FinancialConstants.MODEOFPAYMENT_CHEQUE);
        voucherHeader.setVouchermis(new Vouchermis());
        loadDefalutDates();
        loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        return NEW;
    }

    private void populateFund() {
        if (advanceRequisition != null && advanceRequisition.getEgAdvanceReqMises() != null)
            fund = advanceRequisition.getEgAdvanceReqMises().getFund();
    }

    private void loadBankBranch(final Fund fund) {
        addDropdownData(
                "bankBranchList",
                persistenceService
                        .findAllBy(
                                "from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = true and type in (?,?) ) "
                                        + " and br.isactive=true and br.bank.isactive = true order by br.bank.name asc",
                                fund, FinancialConstants.TYPEOFACCOUNT_PAYMENTS,
                                FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS));
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/payment/advancePayment-save")
    public String save() {

        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_ADVANCE);
        voucherHeader.setDescription(description);
        try {
            validateAdvancePaymentExists();
            if (commonBean.getModeOfPayment().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
                validateRTGS();
            voucherHeader = createVoucherAndledger();
            paymentheader = paymentService.createPaymentHeader(voucherHeader,
                    Integer.valueOf(commonBean.getAccountNumberId()), commonBean.getModeOfPayment(),
                    advanceRequisition.getAdvanceRequisitionAmount());

            createMiscBill();
            paymentheader.transition().start().withOwner(paymentService.getPosition());
            advanceRequisition.getEgAdvanceReqMises().setVoucherheader(paymentheader.getVoucherheader());
            sendForApproval();
            addActionMessage(getText("arf.payment.transaction.success") + " " + voucherHeader.getVoucherNumber());

        } catch (final ValidationException e) {
            final List<ValidationError> errorList = e.getErrors();
            for (final ValidationError error : errorList)
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    prepare();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                } else
                    throw new ValidationException(e.getErrors());
            populateBankAccounts(Integer.parseInt(commonBean.getBankId().split("-")[1]), fund.getId());
        } catch (final NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
            loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        }
        return VIEW;
    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    public String sendForApproval() {
        paymentheader = getPayment();
        action = parameters.get(ACTIONNAME)[0];

        Integer userId = null;
        if (parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains(REJECT_ACTION))
            userId = paymentheader.getCreatedBy().getId().intValue();
        else if (null != parameters.get("approverUserId") && Integer.valueOf(parameters.get("approverUserId")[0]) != -1)
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        else
            userId = ApplicationThreadLocals.getUserId().intValue();

        paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0] + "|" + userId, paymentheader,
                parameters.get("comments")[0]);
        paymentService.persist(paymentheader);
        if (parameters.get(ACTIONNAME)[0].contains("approve")) {
            if ("END".equals(paymentheader.getState().getValue()))
                addActionMessage(getText("payment.voucher.final.approval"));
            else
                addActionMessage(getText("payment.voucher.approved",
                        new String[] { paymentService.getEmployeeNameForPositionId(paymentheader.getState()
                                .getOwnerPosition()) }));
            setAction(parameters.get(ACTIONNAME)[0]);

        } else
            addActionMessage(getText("payment.voucher.rejected",
                    new String[] { paymentService.getEmployeeNameForPositionId(paymentheader.getState()
                            .getOwnerPosition()) }));
        return viewInboxItem();
    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    @Action(value = "/payment/advancePayment-viewInboxItem")
    public String viewInboxItem() {
        paymentheader = getPayment();
        showApprove = true;
        // voucherHeader.setId(paymentheader.getVoucherheader().getId());
        prepareForView();
        loadApproverUser(voucherHeader.getType());
        return VIEW;
    }

    @SkipValidation
    @Action(value = "/payment/advancePayment-view")
    public String view() {
        prepareForView();
        wfitemstate = "END"; // required to hide the approver drop down when
                             // view is form source
        return VIEW;
    }

    @SuppressWarnings("unchecked")
    private void prepareForView() {
        voucherHeader = (CVoucherHeader) persistenceService.getSession().load(CVoucherHeader.class,
                voucherHeader.getId());
        paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?",
                voucherHeader);
        advanceRequisition = (EgAdvanceRequisition) persistenceService.find(
                "from EgAdvanceRequisition where egAdvanceReqMises.voucherheader = ?", voucherHeader);
        advanceRequisitionId = advanceRequisition.getId();
        commonBean.setAmount(paymentheader.getPaymentAmount());
        commonBean.setAccountNumberId(paymentheader.getBankaccount().getId().toString());

        final String bankBranchId = paymentheader.getBankaccount().getBankbranch().getBank().getId() + "-"
                + paymentheader.getBankaccount().getBankbranch().getId();
        commonBean.setBankId(bankBranchId);
        commonBean.setModeOfPayment(paymentheader.getType().toUpperCase());
        final Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService.find(
                " from Miscbilldetail where payVoucherHeader=?", voucherHeader);

        commonBean.setPaidTo(miscbillDetail.getPaidto());
        loadAjaxedDropDowns();
        // find it last so that rest of the data loaded
        if (!"view".equalsIgnoreCase(showMode))
            validateUser("balancecheck");
    }

    private void loadAjaxedDropDowns() {
        populateFund();
        loadBankBranch(fund);
        populateBankAccounts(paymentheader.getBankaccount().getBankbranch().getId(), fund.getId());
    }

    private void populateBankAccounts(final Integer bankBranchId, final Integer fundId) {
        addDropdownData("accountNumberList", persistenceService.findAllBy(
                "from Bankaccount ba where ba.bankbranch.id=? and ba.fund.id=? and ba.type in (?,?) "
                        + "and ba.isactive=true order by ba.chartofaccounts.glcode", bankBranchId, fundId,
                FinancialConstants.TYPEOFACCOUNT_PAYMENTS, FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS));
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    public boolean validateUser(final String purpose) {
        final Script validScript = (Script) getPersistenceService().findAllByNamedQuery(Script.BY_NAME,
                "Paymentheader.show.bankbalance").get(0);
        final List<String> list = (List<String>) scriptService.executeScript(validScript,
                ScriptService.createContext("persistenceService", paymentService, "purpose", purpose));

        if (list.get(0).equals("true"))
            try {
                canCheckBalance = true;
                commonBean.setAvailableBalance(egovCommon.getAccountBalance(new Date(), paymentheader.getBankaccount()
                        .getId(), paymentheader.getPaymentAmount(), paymentheader.getId(), paymentheader
                        .getBankaccount().getChartofaccounts().getId()));
                balance = commonBean.getAvailableBalance();
                return true;
            } catch (final Exception e) {
                balance = BigDecimal.valueOf(-1);
                return true;
            }
        else
            return false;
    }

    private void createMiscBill() {
        final Miscbilldetail miscbilldetail = new Miscbilldetail();
        // Since we are not creating any bill for advance payment, we are
        // updating bill no, bill date and bill amount from ARF
        miscbilldetail.setBillnumber(advanceRequisition.getAdvanceRequisitionNumber());
        miscbilldetail.setBilldate(advanceRequisition.getAdvanceRequisitionDate());
        miscbilldetail.setBillamount(advanceRequisition.getAdvanceRequisitionAmount());
        miscbilldetail.setPassedamount(advanceRequisition.getAdvanceRequisitionAmount());
        miscbilldetail.setPaidamount(advanceRequisition.getAdvanceRequisitionAmount());
        miscbilldetail.setPaidto(advanceRequisition.getEgAdvanceReqMises().getPayto());
        miscbilldetail.setPayVoucherHeader(paymentheader.getVoucherheader());
        miscbilldetailService.persist(miscbilldetail);
    }

    private CVoucherHeader createVoucherAndledger() {
        try {
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
            headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/payment/advancePayment!view.action?voucherHeader.id=");
            HashMap<String, Object> detailMap = null;
            HashMap<String, Object> subledgertDetailMap = null;
            final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, advanceRequisition.getAdvanceRequisitionAmount());
            detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
            final Bankaccount account = (Bankaccount) persistenceService.getSession().load(Bankaccount.class,
                    Integer.valueOf(commonBean.getAccountNumberId()));
            detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            final Map<String, Object> glcodeMap = new HashMap<String, Object>();
            detailMap = new HashMap<String, Object>();

            detailMap.put(VoucherConstant.DEBITAMOUNT, advanceRequisition.getAdvanceRequisitionAmount());
            detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
            for (final EgAdvanceRequisitionDetails advanceRequisitionDetails : advanceRequisition
                    .getEgAdvanceReqDetailses())
                detailMap.put(VoucherConstant.GLCODE, advanceRequisitionDetails.getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            glcodeMap.put(detailMap.get(VoucherConstant.GLCODE).toString(), VoucherConstant.DEBIT);

            subledgertDetailMap = new HashMap<String, Object>();
            for (final EgAdvanceRequisitionDetails advanceDetail : advanceRequisition.getEgAdvanceReqDetailses())
                for (final EgAdvanceReqPayeeDetails payeeDetail : advanceDetail.getEgAdvanceReqpayeeDetailses()) {
                    subledgertDetailMap = new HashMap<String, Object>();
                    subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, payeeDetail.getDebitAmount());
                    subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, payeeDetail.getCreditAmount());
                    subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, payeeDetail.getAccountDetailType().getId());
                    subledgertDetailMap.put(VoucherConstant.DETAILKEYID, payeeDetail.getAccountdetailKeyId());
                    subledgertDetailMap.put(VoucherConstant.GLCODE, advanceDetail.getChartofaccounts().getGlcode());
                    subledgerDetails.add(subledgertDetailMap);
                }

            voucherHeader = createVoucher.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);

        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
        return voucherHeader;

    }

    public String getComments() {
        return getText("payment.comments", new String[] { paymentheader.getPaymentAmount().toPlainString() });
    }

    @Override
    protected HashMap<String, Object> createHeaderAndMisDetails() throws ValidationException {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());

        final EgAdvanceRequisitionMis egAdvanceReqMises = advanceRequisition.getEgAdvanceReqMises();
        if (egAdvanceReqMises != null) {
            if (egAdvanceReqMises.getFund() != null && egAdvanceReqMises.getFund().getId() != null)
                headerdetails.put(VoucherConstant.FUNDCODE, egAdvanceReqMises.getFund().getCode());
            if (egAdvanceReqMises.getEgDepartment() != null && egAdvanceReqMises.getEgDepartment().getId() != null)
                headerdetails.put(VoucherConstant.DEPARTMENTCODE, egAdvanceReqMises.getEgDepartment().getCode());
            if (egAdvanceReqMises.getFundsource() != null && egAdvanceReqMises.getFundsource().getId() != null)
                headerdetails.put(VoucherConstant.FUNDSOURCECODE, egAdvanceReqMises.getFundsource().getCode());
            if (egAdvanceReqMises.getScheme() != null && egAdvanceReqMises.getScheme().getId() != null)
                headerdetails.put(VoucherConstant.SCHEMECODE, egAdvanceReqMises.getScheme().getCode());
            if (egAdvanceReqMises.getSubScheme() != null && egAdvanceReqMises.getSubScheme().getId() != null)
                headerdetails.put(VoucherConstant.SUBSCHEMECODE, egAdvanceReqMises.getSubScheme().getCode());
            if (egAdvanceReqMises.getFunctionaryId() != null && egAdvanceReqMises.getFunctionaryId().getId() != null)
                headerdetails.put(VoucherConstant.FUNCTIONARYCODE, egAdvanceReqMises.getFunctionaryId().getCode());
            if (egAdvanceReqMises.getFunction() != null && egAdvanceReqMises.getFunction().getId() != null)
                headerdetails.put(VoucherConstant.FUNCTIONCODE, egAdvanceReqMises.getFunction().getCode());
            if (egAdvanceReqMises.getFieldId() != null && egAdvanceReqMises.getFieldId().getId() != null)
                headerdetails.put(VoucherConstant.DIVISIONID, egAdvanceReqMises.getFieldId().getId());
        }
        return headerdetails;
    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    public String cancelPayment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting cancelPayment...");
        paymentheader = getPayment();
        voucherHeader = paymentheader.getVoucherheader();
        voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
        // persistenceService.setType(CVoucherHeader.class);
        paymentheader.transition().end();
        persistenceService.persist(voucherHeader);
        addActionMessage(getText("payment.cancel.success"));
        action = parameters.get(ACTIONNAME)[0];
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed cancelPayment...");
        return VIEW;
    }

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        return voucherHeader;
    }

    @SkipValidation
    public List<String> getValidActions() {
        return null;
    }

    public Paymentheader getPayment() {
        String paymentid = null;
        if (parameters.get(PAYMENTID) == null) {
            final Object obj = getSession().get(PAYMENTID);
            if (obj != null)
                paymentid = (String) obj;
        } else
            paymentid = parameters.get(PAYMENTID)[0];
        if (paymentheader == null && paymentid != null)
            paymentheader = (Paymentheader) persistenceService.getSession().load(Paymentheader.class,
                    Long.valueOf(paymentid));
        if (paymentheader == null)
            paymentheader = new Paymentheader();

        return paymentheader;
    }

    @SuppressWarnings("unchecked")
    private void loadApproverUser(String atype) {
        final String scriptName = "paymentHeader.nextDesg";
        if (paymentheader != null && paymentheader.getPaymentAmount() != null) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("paymentheader.getPaymentAmount() >>>>>>>>>>>>>>>>>>> :" + paymentheader.getPaymentAmount());
            atype = atype + "|" + paymentheader.getPaymentAmount();
        } else
            atype = atype + "|";
        departmentId = voucherService.getCurrentDepartment().getId().intValue();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("departmentId :" + departmentId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (paymentheader != null && paymentheader.getVoucherheader().getFiscalPeriodId() != null)
            map = voucherService.getDesgByDeptAndTypeAndVoucherDate(atype, scriptName, paymentheader.getVoucherheader()
                    .getVoucherDate(), paymentheader);
        else
            map = voucherService.getDesgByDeptAndTypeAndVoucherDate(atype, scriptName, new Date(), paymentheader);
        addDropdownData("departmentList", masterDataCache.get("egi-department"));

        final List<Map<String, Object>> desgList = (List<Map<String, Object>>) map.get("designationList");
        String strDesgId = "", dName = "";
        boolean bDefaultDeptId = false;
        final List<Map<String, Object>> designationList = new ArrayList<Map<String, Object>>();
        Map<String, Object> desgFuncryMap;
        for (final Map<String, Object> desgIdAndName : desgList) {
            desgFuncryMap = new HashMap<String, Object>();

            if (desgIdAndName.get("designationName") != null)
                desgFuncryMap.put("designationName", desgIdAndName.get("designationName"));

            if (desgIdAndName.get("designationId") != null) {
                strDesgId = (String) desgIdAndName.get("designationId");
                if (strDesgId.indexOf("~") != -1) {
                    strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
                    dName = (String) desgIdAndName.get("designationId");
                    dName = dName.substring(dName.indexOf('~') + 1);
                    bDefaultDeptId = true;
                }
                desgFuncryMap.put("designationId", strDesgId);
            }
            designationList.add(desgFuncryMap);
        }
        map.put("designationList", designationList);

        addDropdownData("designationList", (List<Designation>) map.get("designationList"));

        if (bDefaultDeptId && !dName.equals("")) {
            final Department dept = (Department) persistenceService.find("from Department where deptName like '%"
                    + dName + "' ");
            departmentId = dept.getId().intValue();
        }
        wfitemstate = map.get("wfitemstate") != null ? map.get("wfitemstate").toString() : "";
    }

    @Override
    public void validate() {
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];

        if (!(actionName.equalsIgnoreCase(REJECT_ACTION) || actionName.equalsIgnoreCase(CANCEL_ACTION))) {
            if (voucherHeader.getVoucherDate() == null)
                addFieldError("voucherDate", getText("arf.payment.voucherdate.required"));
            if (!DateUtils.compareDates(voucherHeader.getVoucherDate(), advanceRequisition.getAdvanceRequisitionDate()))
                addFieldError("advanceRequisitionDate",
                        getText("arf.payment.voucherdate.lessthan.advancerequisitiondate"));
            if (!DateUtils.compareDates(new Date(), voucherHeader.getVoucherDate()))
                addFieldError("advanceRequisitionDate",
                        getText("arf.validate.payment.voucherdate.greaterthan.currentDate"));

            if (org.apache.commons.lang.StringUtils.isBlank(commonBean.getBankId())
                    || commonBean.getBankId().equals("-1"))
                addFieldError("commonBean.bankId", getText("arf.bankbranch.required"));

            if (org.apache.commons.lang.StringUtils.isBlank(commonBean.getAccountNumberId())
                    || commonBean.getAccountNumberId().equals("-1"))
                addFieldError("commonBean.accountNumberId", getText("arf.accountnumber.required"));
            if (org.apache.commons.lang.StringUtils.isBlank(commonBean.getModeOfPayment()))
                addFieldError("commonBean.modeOfPayment", getText("arf.modeOfPayment.required"));

        }
    }

    private void validateRTGS() {
        EntityType entity = null;
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        for (final EgAdvanceRequisitionDetails advanceDetail : advanceRequisition.getEgAdvanceReqDetailses())
            if (!advanceDetail.getEgAdvanceReqpayeeDetailses().isEmpty())
                for (final EgAdvanceReqPayeeDetails payeeDetail : advanceDetail.getEgAdvanceReqpayeeDetailses()) {
                    try {
                        entity = paymentService.getEntity(payeeDetail.getAccountDetailType().getId(),
                                payeeDetail.getAccountdetailKeyId());
                        if (entity == null)
                            throw new ValidationException(Arrays.asList(new ValidationError("no.entity.for.detailkey",
                                    getText("no.entity.for.detailkey",
                                            new String[] { entity.getCode() + "-" + entity.getName() }))));
                    } catch (final ApplicationException e) {
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "Exception to get EntityType  ", e.getMessage())));
                    }

                    if (org.apache.commons.lang.StringUtils.isBlank(entity.getPanno())
                            || org.apache.commons.lang.StringUtils.isBlank(entity.getBankname())
                            || org.apache.commons.lang.StringUtils.isBlank(entity.getBankaccount())
                            || org.apache.commons.lang.StringUtils.isBlank(entity.getIfsccode())) {
                        populateBankAccounts(Integer.parseInt(commonBean.getBankId().split("-")[1]), fund.getId());
                        LOGGER.error(getText("validate.paymentMode.rtgs.subledger.details",
                                new String[] { entity.getCode() + "-" + entity.getName() }));
                        errors.add(new ValidationError("validate.paymentMode.rtgs.subledger.details", getText(
                                "validate.paymentMode.rtgs.subledger.details", new String[] { entity.getCode() + "-"
                                        + entity.getName() })));
                        throw new ValidationException(errors);

                    }
                }
            else
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "no.subledger.cannot.create.rtgs.payment",
                        "arf.payment.no.subledger.cannot.create.rtgs.payment")));
    }

    private void validateAdvancePaymentExists() {

        if (advanceRequisition != null && advanceRequisition.getEgAdvanceReqMises().getVoucherheader() != null
                && advanceRequisition.getEgAdvanceReqMises().getVoucherheader().getStatus() != 4) {
            populateBankAccounts(Integer.parseInt(commonBean.getBankId().split("-")[1]), fund.getId());
            throw new ValidationException(Arrays.asList(new ValidationError("arf.payment.uniqueCheck.validate.message",
                    getText("arf.payment.uniqueCheck.validate.message",
                            new String[] { advanceRequisition.getAdvanceRequisitionNumber() }))));
        }
    }

    public void setAdvanceRequisition(final EgAdvanceRequisition advanceRequisition) {
        this.advanceRequisition = advanceRequisition;
    }

    public EgAdvanceRequisition getAdvanceRequisition() {
        return advanceRequisition;
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void setPaymentWorkflowService(final SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
        this.paymentWorkflowService = paymentWorkflowService;
    }

    public void setPaymentMode(final String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public VoucherService getVoucherService() {
        return voucherService;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public EgovCommon getEgovCommon() {
        return egovCommon;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Fund getFund() {
        return fund;
    }

    public Long getAdvanceRequisitionId() {
        return advanceRequisitionId;
    }

    public void setAdvanceRequisitionId(final Long advanceRequisitionId) {
        this.advanceRequisitionId = advanceRequisitionId;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(final String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public CommonBean getCommonBean() {
        return commonBean;
    }

    public void setCommonBean(final CommonBean commonBean) {
        this.commonBean = commonBean;
    }

    public Map<String, String> getModeOfPaymentMap() {
        return modeOfPaymentMap;
    }

    public void setModeOfPaymentMap(final Map<String, String> modeOfPaymentMap) {
        this.modeOfPaymentMap = modeOfPaymentMap;
    }

    public Paymentheader getPaymentheader() {
        return paymentheader;
    }

    public void setPaymentheader(final Paymentheader paymentheader) {
        this.paymentheader = paymentheader;
    }

    public void setMiscbilldetailService(final PersistenceService<Miscbilldetail, Long> miscbilldetailService) {
        this.miscbilldetailService = miscbilldetailService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(final ScriptService scriptService) {
        this.scriptService = scriptService;
    }

}