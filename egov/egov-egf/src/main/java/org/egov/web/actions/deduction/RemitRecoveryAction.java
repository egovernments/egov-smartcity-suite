/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/**
 *
 */
package org.egov.web.actions.deduction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.deduction.model.EgRemittance;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.deduction.RemittanceBean;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.Paymentheader;
import org.egov.model.recoveries.Recovery;
import org.egov.model.service.RecoveryService;
import org.egov.model.voucher.CommonBean;
import org.egov.model.voucher.WorkflowBean;
import org.egov.payment.services.PaymentActionHelper;
import org.egov.pims.commons.Designation;
import org.egov.services.deduction.RemitRecoveryService;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.payment.BasePaymentAction;
import org.egov.web.actions.voucher.CommonAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.opensymphony.xwork2.validator.annotations.Validation;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
@Validation
@Results({
        @Result(name = RemitRecoveryAction.NEW, location = "remitRecovery-" + RemitRecoveryAction.NEW + ".jsp"),
        @Result(name = "messages", location = "remitRecovery-messages.jsp"),
        @Result(name = "view", location = "remitRecovery-view.jsp"),
        @Result(name = "remitDetail", location = "remitRecovery-remitDetail.jsp")
})
public class RemitRecoveryAction extends BasePaymentAction {

    private static final String DESIGNATION_ID = "designationId";
    private static final String DESIGNATION_NAME = "designationName";
    private static final String DESIGNATION_LIST = "designationList";
    private static final String UNCHECKED = "unchecked";
    private static final String MESSAGES = "messages";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RemitRecoveryAction.class);
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
    private RemittanceBean remittanceBean = new RemittanceBean();
    private FinancialYearHibernateDAO financialYearDAO;
    private RemitRecoveryService remitRecoveryService;
    private VoucherService voucherService;
    private List<RemittanceBean> listRemitBean;
    private RecoveryService recoveryService;
    private CommonAction common;
    private Map<String, String> modeOfCollectionMap = new HashMap<String, String>();
    private PaymentService paymentService;
    private Paymentheader paymentheader = new Paymentheader();
    private static final String PAYMENTID = "paymentid";
    private static final String VIEW = "view";
    private SimpleWorkflowService<Paymentheader> paymentWorkflowService;
    public boolean showApprove = false;
    private CommonBean commonBean;
    private String modeOfPayment;
    private @Autowired CreateVoucher createVoucher;
    private Integer departmentId;
    private String wfitemstate;
    private String comments;
    private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
    private static final String FAILED = "Transaction failed";
    private static final String SAVE_EXCEPTION = "Exception while saving data";
    // private String typeOfAccount;
    private Recovery recovery;
    private VoucherHibernateDAO voucherHibDAO;
    private EgovCommon egovCommon;
    private boolean showCancel = false;
    private boolean showButtons = true;
    private BigDecimal balance;
    private String remittedTo = "";
    private final boolean remit = false;
    private List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();
    private ScriptService scriptService;
    @Autowired
    private PaymentActionHelper paymentActionHelper;
    private ChartOfAccounts chartOfAccounts;
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public StateAware getModel() {
        voucherHeader = (CVoucherHeader) super.getModel();
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        return voucherHeader;
    }

    public RemitRecoveryAction()
    {
        voucherHeader.setVouchermis(new Vouchermis());
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
        addRelatedEntity("fundsourceId", Fundsource.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside Prepare method");
        final List<Recovery> listRecovery = recoveryService.getAllActiveRecoverys();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | Tds list size : " + listRecovery.size());
        addDropdownData("recoveryList", listRecovery);
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("accNumList", Collections.EMPTY_LIST);
        modeOfCollectionMap.put("cash", getText("cash.consolidated.cheque"));
    }

    @Override
    @Action(value = "/deduction/remitRecovery-newform")
    public String newform() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | newform | start");
        reset();
        loadDefalutDates();
        return NEW;
    }

    private void reset() {
        commonBean.reset();
        voucherHeader.reset();
        remittanceBean = new RemittanceBean();
        if (remit)
            listRemitBean = new ArrayList<RemittanceBean>();
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/deduction/remitRecovery-search")
    public String search() {
        listRemitBean = new ArrayList<RemittanceBean>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | Search | Start");
        validateFields();
        listRemitBean = remitRecoveryService.getRecoveryDetails(remittanceBean, voucherHeader);
        if (listRemitBean == null)
            listRemitBean = new ArrayList<RemittanceBean>();

        return NEW;
    }

    public void prepareRemit()
    {
        addDropdownData("userList", Collections.EMPTY_LIST);
        loadDefalutDates();
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/deduction/remitRecovery-remit")
    public String remit() {
        voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | remit | start");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | remit | size before filter" + listRemitBean.size());
        final Recovery recov = (Recovery) persistenceService.find("from Recovery where id=?", remittanceBean.getRecoveryId());
        if (recov != null)
            remittedTo = recov.getRemitted();
        final Predicate remitPredicate = new RemittanceBean();
        for (final RemittanceBean rbean : listRemitBean)
            rbean.setPartialAmount(rbean.getAmount());
        CollectionUtils.filter(listRemitBean, remitPredicate);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | remit | size after filter" + listRemitBean.size());
        setModeOfPayment(FinancialConstants.MODEOFPAYMENT_CASH);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryAction | remit | end");
        return "remitDetail";
    }

    @ValidationErrorPage(value = "remitDetail")
    @Action(value = "/deduction/remitRecovery-create")
    public String create()
    {
        try {
            validateFields();
            voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
            voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE);
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
            recovery = (Recovery) persistenceService.find("from Recovery where id=?", remittanceBean.getRecoveryId());
            populateWorkflowBean();
            paymentheader = paymentActionHelper.createRemittancePayment(paymentheader, voucherHeader,
                    Integer.valueOf(commonBean.getAccountNumberId()), getModeOfPayment(), remittanceBean.getTotalAmount(),
                    listRemitBean, recovery, remittanceBean, remittedTo, workflowBean, headerDetails, commonBean);
            addActionMessage(getText("remittancepayment.transaction.success")
                    + paymentheader.getVoucherheader().getVoucherNumber());
            addActionMessage(getText("payment.voucher.approved",
                    new String[] { paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));

        } catch (final ValidationException e) {
            loadAjaxedDropDowns();
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            loadAjaxedDropDowns();
            LOGGER.error(e.getMessage());
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return MESSAGES;
    }

    /**
     *
     * @param vh
     *
     * 1.Creates one EgRemittance with paymentvoucher is set 2.updates every EgRemittanceGldtl for selected Bills 3.Creates
     * EgRemittanceDetail per selected Bill
     *
     */
    private List<HashMap<String, Object>> addSubledgerGroupBy(final List<HashMap<String, Object>> subledgerDetails,
            final String glcode) {
        final Map<Integer, List<Integer>> detailTypesMap = new HashMap<Integer, List<Integer>>();
        Integer detailTypeId = null;
        final List<Integer> detailTypeList = new ArrayList<Integer>();
        HashMap<String, Object> subledgertDetailMap = null;
        for (final RemittanceBean rbean : listRemitBean)
        {
            detailTypeId = rbean.getDetailTypeId();
            if (detailTypeList.contains(detailTypeId))
            {
                if (detailTypesMap.get(detailTypeId).contains(rbean.getDetailKeyid()))
                    continue;
                else
                    detailTypesMap.get(detailTypeId).add(rbean.getDetailKeyid());

            }
            else
            {
                detailTypeList.add(detailTypeId);
                detailTypesMap.put(detailTypeId, new ArrayList<Integer>());
                detailTypesMap.get(detailTypeId).add(rbean.getDetailKeyid());

            }

        }
        final Set<Entry<Integer, List<Integer>>> entrySet = detailTypesMap.entrySet();
        final List<RemittanceBean> tempRemitBean = listRemitBean;
        for (final Entry<Integer, List<Integer>> o : entrySet)
        {
            final List<Integer> value = o.getValue();
            for (final Integer detailKey : value)
            {
                BigDecimal sumPerDetailKey = BigDecimal.ZERO;
                // int lastIndexOf = tempRemitBean.lastIndexOf(detailKey);
                for (final RemittanceBean remittanceBean2 : tempRemitBean)
                    if (remittanceBean2.getDetailKeyid() != null && remittanceBean2.getDetailKeyid().equals(detailKey))
                        sumPerDetailKey = sumPerDetailKey.add(remittanceBean2.getPartialAmount());
                subledgertDetailMap = new HashMap<String, Object>();
                subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, sumPerDetailKey);
                subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, BigDecimal.ZERO);
                subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, o.getKey().toString());
                subledgertDetailMap.put(VoucherConstant.DETAILKEYID, detailKey);
                subledgertDetailMap.put(VoucherConstant.GLCODE, glcode);
                subledgerDetails.add(subledgertDetailMap);
            }

        }
        return subledgerDetails;
    }

    public Paymentheader getPayment() {
        String paymentid = null;
        // System.out.println("Payment id is"+parameters.get(PAYMENTID));
        if (parameters.get(PAYMENTID) == null)
        {
            final Object obj = getSession().get(PAYMENTID);
            if (obj != null)
                paymentid = (String) obj;
        } else
            paymentid = parameters.get(PAYMENTID)[0];
        if (paymentheader == null && paymentid != null)
            paymentheader = paymentService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
        if (paymentheader == null)
            paymentheader = new Paymentheader();

        return paymentheader;
    }

    @SuppressWarnings(UNCHECKED)
    private void loadApproverUser(final String type) {
        addDropdownData("userList", Collections.EMPTY_LIST);
        final String scriptName = "paymentHeader.nextDesg";
        String atype = type;
        if (paymentheader != null && paymentheader.getPaymentAmount() != null)
        {
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

        final List<Map<String, Object>> desgList = (List<Map<String, Object>>) map.get(DESIGNATION_LIST);
        String strDesgId = "", dName = "";
        boolean bDefaultDeptId = false;
        final List<Map<String, Object>> designationList = new ArrayList<Map<String, Object>>();
        Map<String, Object> desgFuncryMap;
        for (final Map<String, Object> desgIdAndName : desgList) {
            desgFuncryMap = new HashMap<String, Object>();

            if (desgIdAndName.get(DESIGNATION_NAME) != null)
                desgFuncryMap.put(DESIGNATION_NAME, desgIdAndName.get(DESIGNATION_NAME));

            if (desgIdAndName.get(DESIGNATION_ID) != null) {
                strDesgId = (String) desgIdAndName.get(DESIGNATION_ID);
                if (strDesgId.indexOf('~') != -1) {
                    strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
                    dName = (String) desgIdAndName.get(DESIGNATION_ID);
                    dName = dName.substring(dName.indexOf('~') + 1);
                    bDefaultDeptId = true;
                }
                desgFuncryMap.put(DESIGNATION_ID, strDesgId);
            }
            designationList.add(desgFuncryMap);
        }
        map.put(DESIGNATION_LIST, designationList);

        addDropdownData(DESIGNATION_LIST, (List<Designation>) map.get(DESIGNATION_LIST));

        if (bDefaultDeptId && !"".equals(dName)) {
            final Department dept = (Department) persistenceService.find("from Department where deptName like '%" + dName + "' ");
            departmentId = dept.getId().intValue();
        }
        wfitemstate = map.get("wfitemstate") == null ? "" : map.get("wfitemstate").toString();
    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    @Action(value = "/deduction/remitRecovery-sendForApproval")
    public String sendForApproval() {
        paymentheader = paymentService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
        populateWorkflowBean();
        paymentheader = paymentActionHelper.sendForApproval(paymentheader, workflowBean);

        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.rejected",
                    new String[] { paymentService.getEmployeeNameForPositionId(paymentheader.getState()
                            .getOwnerPosition()) }));
        if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.approved", new String[] { paymentService
                    .getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition()) }));
        if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            addActionMessage(getText("payment.voucher.cancelled"));
        else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if ("Closed".equals(paymentheader.getState().getValue()))
                addActionMessage(getText("payment.voucher.final.approval"));
            else
                addActionMessage(getText("payment.voucher.approved",
                        new String[] { paymentService.getEmployeeNameForPositionId(paymentheader.getState()
                                .getOwnerPosition()) }));
            setAction(workflowBean.getWorkFlowAction());

        }

        return MESSAGES;
    }

    /**
     * @return messages
     */
    /**
     * @return
     */
    private String cancelPayment() {
        voucherHeader = paymentheader.getVoucherheader();
        final int count = paymentService.backUpdateRemittanceDateInGL(voucherHeader.getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Remittance Cancellation updated " + count + " generalledger entries");
        voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
        // persistenceService.setType(CVoucherHeader.class);
        persistenceService.persist(voucherHeader);
        paymentheader.transition(true).end();
        addActionMessage(getText("payment.voucher.cancelled"));
        return MESSAGES;
    }

    @ValidationErrorPage(value = VIEW)
    @SkipValidation
    @Action(value = "/deduction/remitRecovery-viewInboxItem")
    public String viewInboxItem() {
        paymentheader = paymentService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
       /* if (paymentheader.getState().getValue() != null && !paymentheader.getState().getValue().isEmpty()
                && paymentheader.getState().getValue().contains("Reject"))
        {
            voucherHeader.setId(paymentheader.getVoucherheader().getId());
            showCancel = true;
            return beforeEdit();
        }*/
        showApprove = true;
        voucherHeader.setId(paymentheader.getVoucherheader().getId());
        prepareForViewModifyReverse();
        // loadApproverUser(voucherHeader.getType());
        return VIEW;
    }

    /**
     * @return
     */
    public String beforeEdit() {
        showApprove = true;
        // voucherHeader.setId(paymentheader.getVoucherheader().getId());
        prepareForViewModifyReverse();
        //loadApproverUser(voucherHeader.getType());
        return EDIT;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/deduction/remitRecovery-edit")
    public String edit()
    {
        try {
            validateFields();
            voucherHeader = voucherService.updateVoucherHeader(voucherHeader);
            reCreateLedger();
            updateRemittanceDetail();
            paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?", voucherHeader);
            paymentService.updatePaymentHeader(paymentheader, voucherHeader, Integer.valueOf(commonBean.getAccountNumberId()),
                    getModeOfPayment(), remittanceBean.getTotalAmount());
            final Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService.find(
                    " from Miscbilldetail where payVoucherHeader=?", voucherHeader);
            miscbillDetail.setPaidto(remittedTo);
            // persistenceService.setType(Miscbilldetail.class);
            persistenceService.persist(miscbillDetail);
            // updateMiscBillDetail();

            sendForApproval();
            addActionMessage(getText("remittancepayment.transaction.success") + voucherHeader.getVoucherNumber());
        } catch (final ValidationException e) {
            //loadApproverUser(voucherHeader.getType());
            loadAjaxedDropDowns();
            throw e;
        }
        return MESSAGES;
    }

    /**
     *
     */
    private void updateRemittanceDetail() {
        for (final RemittanceBean rbean : listRemitBean)
        {
            final EgRemittanceDetail egrDetail = (EgRemittanceDetail) persistenceService.find(
                    "from EgRemittanceDetail where id=?",
                    rbean.getRemittanceId());
            egrDetail.setRemittedamt(rbean.getPartialAmount());
            // persistenceService.setType(EgRemittanceDetail.class);
            persistenceService.persist(egrDetail);
        }

    }

    /**
     *
     *
     */
    private void reCreateLedger() {

        try {
            createVoucher.deleteVoucherdetailAndGL(voucherHeader);
            HibernateUtil.getCurrentSession().flush();
            HashMap<String, Object> detailMap = null;
            final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, remittanceBean.getTotalAmount().toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
            final Bankaccount account = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                    Integer.valueOf(commonBean.getAccountNumberId()));
            detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
            detailMap.put(VoucherConstant.DEBITAMOUNT, remittanceBean.getTotalAmount().toString());
            recovery = (Recovery) persistenceService.find("from Recovery where id=?", remittanceBean.getRecoveryId());
            detailMap.put(VoucherConstant.GLCODE, recovery.getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            subledgerDetails = addSubledgerGroupBy(subledgerDetails, recovery.getChartofaccounts().getGlcode());

            final List<Transaxtion> transactions = createVoucher.createTransaction(null, accountdetails, subledgerDetails,
                    voucherHeader);
            HibernateUtil.getCurrentSession().flush();

            Transaxtion txnList[] = new Transaxtion[transactions.size()];
            txnList = transactions.toArray(txnList);
            final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
            if (!chartOfAccounts.postTransaxtions(txnList, formatter.format(voucherHeader.getVoucherDate())))
                throw new ValidationException(Arrays.asList(new ValidationError("Exception While Saving Data",
                        "Transaction Failed")));

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(SAVE_EXCEPTION, FAILED)));
        }

    }

    @Action(value = "/deduction/remitRecovery-beforeView")
    public String beforeView()
    {
        prepareForViewModifyReverse();
        wfitemstate = "END"; // requird to hide the approver drop down when view is form source
        showApprove = true;
        showButtons = false;
        // loadApproverUser(voucherHeader.getType());
        return VIEW;
    }

    @SuppressWarnings(UNCHECKED)
    private void prepareForViewModifyReverse() {
        final StringBuffer instrumentQuery = new StringBuffer(100);
        instrumentQuery.append(
                "select  distinct ih from InstrumentHeader ih join ih.instrumentVouchers iv where iv.voucherHeaderId.id=?")
                .append(" order by ih.id");
        remittanceBean = new RemittanceBean();
        voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", voucherHeader.getId());
        paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?", voucherHeader);
        final Miscbilldetail miscBill = (Miscbilldetail) persistenceService.find("from Miscbilldetail where payVoucherHeader=?",
                voucherHeader);
        remittedTo = miscBill.getPaidto();
        commonBean.setAmount(paymentheader.getPaymentAmount());
        commonBean.setAccountNumberId(paymentheader.getBankaccount().getId().toString());
        commonBean.setAccnumnar(paymentheader.getBankaccount().getNarration());

        final String bankBranchId = paymentheader.getBankaccount().getBankbranch().getBank().getId() + "-"
                + paymentheader.getBankaccount().getBankbranch().getId();

        commonBean.setBankId(bankBranchId);
        setModeOfPayment(paymentheader.getType());
        remittanceBean.setTotalAmount(paymentheader.getPaymentAmount());
        setComments(getText("payment.comments", new String[] { paymentheader.getPaymentAmount().toPlainString() }));
        getRemittanceFromVoucher();

        loadAjaxedDropDowns();
        // find it last so that rest of the data loaded
        if (!"view".equalsIgnoreCase(showMode)) {
            // validateUser("balancecheck");
        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("fetching cheque detail ------------------------");
            instrumentHeaderList = getPersistenceService()
                    .findAllBy(instrumentQuery.toString(), paymentheader.getVoucherheader().getId());
        }
    }

    private void getRemittanceFromVoucher() {
        listRemitBean = new ArrayList<RemittanceBean>();

        final List<EgRemittance> remitList = persistenceService.findAllBy(
                "from EgRemittance where voucherheader=?", voucherHeader);
        for (final EgRemittance remit : remitList)
        {
            RemittanceBean rbean = null;
            final Set<EgRemittanceDetail> egRemittanceDetail = remit.getEgRemittanceDetail();
            for (final EgRemittanceDetail remitDtl : egRemittanceDetail)
            {
                rbean = new RemittanceBean();
                rbean.setPartialAmount(remitDtl.getRemittedamt());
                rbean.setAmount(remitDtl.getRemittedamt());
                if (remitDtl.getEgRemittanceGldtl() != null) {
                    rbean.setDetailTypeId(remitDtl.getEgRemittanceGldtl().getGeneralledgerdetail().getAccountdetailtype().getId());
                    rbean.setDetailKeyid(remitDtl.getEgRemittanceGldtl().getGeneralledgerdetail().getDetailkeyid().intValue());
                    rbean.setRemittance_gl_dtlId(remitDtl.getEgRemittanceGldtl().getId());
                    rbean.setDeductionAmount(remitDtl.getEgRemittanceGldtl().getGldtlamt());
                } else
                    rbean.setDeductionAmount(remitDtl.getRemittedamt());
                rbean.setRemittanceId(remitDtl.getId());
                BigDecimal calculatedEarlierPayment = BigDecimal.ZERO;
                if (remitDtl.getEgRemittanceGldtl() != null)
                    calculatedEarlierPayment = calculateEarlierPayment(remitDtl);
                if (calculatedEarlierPayment.compareTo(BigDecimal.ZERO) == 0)
                    rbean.setEarlierPayment(BigDecimal.ZERO);
                else if (remit.getVoucherheader().getStatus().intValue() == FinancialConstants.CANCELLEDVOUCHERSTATUS
                        .intValue()
                        || remit.getVoucherheader().getStatus().intValue() == FinancialConstants.REVERSALVOUCHERSTATUS.intValue())
                    rbean.setEarlierPayment(calculatedEarlierPayment);
                else
                    rbean.setEarlierPayment(calculatedEarlierPayment.subtract(remitDtl.getRemittedamt()));
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (remitDtl.getEgRemittanceGldtl() != null) {
                    final EntityType entity = voucherHibDAO.getEntityInfo(remitDtl.getEgRemittanceGldtl()
                            .getGeneralledgerdetail()
                            .getDetailkeyid().intValue(), remitDtl.getEgRemittanceGldtl().getGeneralledgerdetail()
                            .getAccountdetailtype().getId());
                    rbean.setPartyCode(entity.getCode());
                    rbean.setPartyName(entity.getName());
                    rbean.setPanNo(entity.getPanno());
                    rbean.setVoucherDate(sdf.format(remitDtl.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralledger()
                            .getVoucherHeaderId().getVoucherDate()));
                    rbean.setVoucherNumber(remitDtl.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralledger()
                            .getVoucherHeaderId().getVoucherNumber());
                    rbean.setVoucherName(remitDtl.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralledger()
                            .getVoucherHeaderId().getName());
                } else if (remitDtl.getGeneralLedger().getVoucherHeaderId() != null) {
                    rbean.setVoucherDate(sdf.format(remitDtl.getGeneralLedger().getVoucherHeaderId().getVoucherDate()));
                    rbean.setVoucherNumber(remitDtl.getGeneralLedger().getVoucherHeaderId().getVoucherNumber());
                    rbean.setVoucherName(remitDtl.getGeneralLedger().getVoucherHeaderId().getName());
                }
                listRemitBean.add(rbean);

            }

            if (remittanceBean.getRecoveryId() == null)
                remittanceBean.setRecoveryId(remit.getRecovery().getId());

        }
    }

    /**
     * @param remitDtl
     * @param remit
     * @return
     */
    private BigDecimal calculateEarlierPayment(final EgRemittanceDetail remitDtl) {
        final BigDecimal sum = (BigDecimal) persistenceService.find(
                "select sum(egr.remittedamt) from EgRemittanceDetail egr where " +
                        " egr.egRemittanceGldtl=?  and egr.egRemittance.voucherheader.status  NOT in (?,?)",
                remitDtl.getEgRemittanceGldtl(),
                FinancialConstants.CANCELLEDVOUCHERSTATUS, FinancialConstants.REVERSEDVOUCHERSTATUS);
        if (sum == null)
            return BigDecimal.ZERO;
        else
            return sum;
    }

    @SuppressWarnings(UNCHECKED)
    @SkipValidation
    public boolean validateUser(final String purpose) {
        final Script validScript = (Script) getPersistenceService().findAllByNamedQuery(Script.BY_NAME,
                "Paymentheader.show.bankbalance").get(0);
        final List<String> list = (List<String>) scriptService.executeScript(validScript,
                ScriptService.createContext("persistenceService", paymentService, "purpose", purpose));
        if (list.get(0).equals("true"))
            try {
                canCheckBalance = true;
                commonBean.setAvailableBalance(egovCommon.getAccountBalance(new Date(), paymentheader.getBankaccount().getId(),
                        paymentheader.getPaymentAmount(), paymentheader.getId(), paymentheader.getBankaccount()
                                .getChartofaccounts().getId()));
                balance = commonBean.getAvailableBalance();
                return true;
            } catch (final Exception e) {
                balance = BigDecimal.valueOf(-1);

                return true;
            }
        else
            return false;
    }

    private void loadAjaxedDropDowns() {
        loadSchemeSubscheme();
        loadBankBranchForFundAndType();
        loadBankAccountNumberForFundAndType();
    }

    private void loadBankBranchForFundAndType() {
        common.setFundId(voucherHeader.getFundId().getId());
        common.setTypeOfAccount("RECEIPTS_PAYMENTS,PAYMENTS");
        common.ajaxLoadBanksByFundAndType();
        addDropdownData("bankList", common.getBankBranchList());
    }

    private void loadBankAccountNumberForFundAndType()
    {
        Bankaccount bankaccount = null;

        if (paymentheader != null)
        {
            bankaccount = paymentheader.getBankaccount();
            common.setBranchId(bankaccount.getBankbranch().getId());
            common.setBankId(bankaccount.getBankbranch().getBank().getId());
        } else if (commonBean.getAccountNumberId() != null && !commonBean.getAccountNumberId().equals("-1")
                && !commonBean.getAccountNumberId().equals(""))
        {
            bankaccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                    Integer.valueOf(commonBean.getAccountNumberId()));
            common.setBranchId(bankaccount.getBankbranch().getId());
            common.setBankId(bankaccount.getBankbranch().getBank().getId());
        }
        if (common.getBranchId() != null)
        {
            common.setTypeOfAccount("RECEIPTS_PAYMENTS,PAYMENTS");
            common.ajaxLoadAccNumAndType();
            addDropdownData("accNumList", common.getAccNumList());
        } else
            addDropdownData("accNumList", Collections.EMPTY_LIST);
    }

    /*
     * not used private void updateMiscBillDetail() { Miscbilldetail miscbillDetail = (Miscbilldetail)
     * persistenceService.find(" from Miscbilldetail where payVoucherHeader=?", voucherHeader);
     * miscbillDetail.setBillamount(remittanceBean.getTotalAmount());
     * miscbillDetail.setPaidamount(remittanceBean.getTotalAmount());
     * miscbillDetail.setPassedamount(remittanceBean.getTotalAmount()); miscbillDetail.setPayVoucherHeader(voucherHeader);
     * //miscbillDetail.setBillVoucherHeader(vhid); miscbillDetail.setPaidto(recovery.getRemitted());
     * //persistenceService.setType(Miscbilldetail.class); persistenceService.persist(miscbillDetail); }
     */

    @Override
    protected void loadDefalutDates() {
        final Date currDate = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            voucherHeader.setVoucherDate(sdf.parse(sdf.format(currDate)));
        } catch (final ParseException e) {
            throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting voucher date",
                    "Transaction failed")));
        }
    }

    public List<String> getValidActions() {
        List<String> validActions = Collections.emptyList();
        if (null == paymentheader || null == paymentheader.getId() || paymentheader.getCurrentState().getValue().endsWith("NEW")) {
            validActions = Arrays.asList("Forward");
        } else {
            if (paymentheader.getCurrentState() != null) {
                validActions = this.customizedWorkFlowService.getNextValidActions(paymentheader
                        .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                        getAdditionalRule(), paymentheader.getCurrentState().getValue(),
                        getPendingActions(), paymentheader.getCreatedDate());
            }
        }
        return validActions;
    }

    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (paymentheader.getId() != null) {
            if (paymentheader.getCurrentState() != null) {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), paymentheader
                                .getCurrentState().getValue(), getPendingActions(), paymentheader
                                .getCreatedDate());
            } else {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(paymentheader.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), paymentheader
                                .getCreatedDate());
            }
        }
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    @Override
    public CVoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    @Override
    public void setVoucherHeader(final CVoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public RemittanceBean getRemittanceBean() {
        return remittanceBean;
    }

    public void setRemittanceBean(final RemittanceBean remittanceBean) {
        this.remittanceBean = remittanceBean;
    }

    public void setRemitRecoveryService(final RemitRecoveryService remitRecoveryService) {
        this.remitRecoveryService = remitRecoveryService;
    }

    public List<RemittanceBean> getListRemitBean() {
        return listRemitBean;
    }

    public void setListRemitBean(final List<RemittanceBean> listRemitBean) {
        this.listRemitBean = listRemitBean;
    }

    public void setRecoveryService(final RecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    public void setCommon(final CommonAction common) {
        this.common = common;
    }

    public Paymentheader getPaymentheader() {
        return paymentheader;
    }

    public void setPaymentheader(final Paymentheader paymentheader) {
        this.paymentheader = paymentheader;
    }

    public void setPaymentService(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void setPaymentWorkflowService(final SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
        this.paymentWorkflowService = paymentWorkflowService;
    }

    public void setVoucherService(final VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public void setCommonBean(final CommonBean commonBean) {
        this.commonBean = commonBean;
    }

    public CommonBean getCommonBean() {
        return commonBean;
    }

    public void setFinancialYearDAO(final FinancialYearHibernateDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public Map<String, String> getModeOfCollectionMap() {
        return modeOfCollectionMap;
    }

    public void setModeOfCollectionMap(final Map<String, String> modeOfCollectionMap) {
        this.modeOfCollectionMap = modeOfCollectionMap;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(final String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public void setVoucherHibDAO(final VoucherHibernateDAO voucherHibDAO) {
        this.voucherHibDAO = voucherHibDAO;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(final String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public boolean isShowButtons() {
        return showButtons;
    }

    public void setShowButtons(final boolean showButtons) {
        this.showButtons = showButtons;
    }

    public boolean isShowCancel() {
        return showCancel;
    }

    public void setShowCancel(final boolean showCancel) {
        this.showCancel = showCancel;
    }

    public void setRemittedTo(final String remittedTo) {
        this.remittedTo = remittedTo;
    }

    public String getRemittedTo() {
        return remittedTo;
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

    public void setChartOfAccounts(ChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public String getCurrentState() {
        return paymentheader.getState().getValue();
    }

}
