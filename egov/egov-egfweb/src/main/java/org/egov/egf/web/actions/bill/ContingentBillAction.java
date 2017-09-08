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
package org.egov.egf.web.actions.bill;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.egf.autonumber.ExpenseBillNumberGenerator;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infstr.models.EgChecklists;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.WorkflowBean;
import org.egov.utils.CheckListHelper;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @author mani
 *
 */
@ParentPackage("egov")
@Results({
        @Result(name = ContingentBillAction.NEW, location = "contingentBill-new.jsp"),
        @Result(name = "messages", location = "contingentBill-messages.jsp"),
        @Result(name = ContingentBillAction.VIEW, location = "contingentBill-view.jsp")
})
public class ContingentBillAction extends BaseBillAction {
    public class COAcomparator implements Comparator<CChartOfAccounts> {
        @Override
        public int compare(final CChartOfAccounts o1, final CChartOfAccounts o2) {
            return o1.getGlcode().compareTo(o2.getGlcode());

        }

    }

    private final static String FORWARD = "Forward";
    private static final String ACCOUNT_DETAIL_TYPE_LIST = "accountDetailTypeList";
    private static final String BILL_SUB_TYPE_LIST = "billSubTypeList";
    private static final String USER_LIST = "userList";
    private static final String DESIGNATION_LIST = "designationList";
    private static final String MODE = "mode";
    private static final String APPROVER_USER_ID = "approverUserId";
    private static final String END = "END";
    private static final String APPROVE = "approve";
    private static final String ACTION_NAME = "actionName";
    private static final String WFITEMSTATE = "wfitemstate";
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ContingentBillAction.class);
    private BigDecimal debitSum = BigDecimal.ZERO;
    private BigDecimal billAmount = BigDecimal.ZERO;
    private EgBillregister bill = new EgBillregister();
    private boolean showPrintPreview;
    private String sanctionedMessge;
    private Department primaryDepartment;
    private String cutOffDate;
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    @Autowired
    private ApplicationSequenceNumberGenerator sequenceGenerator;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Override
    public StateAware getModel() {
        return super.getModel();
    }

    @Override
    public void prepare() {
        super.prepare();
        accountDetailTypeList = persistenceService.findAllBy("from Accountdetailtype where isactive=true order by name");
        addDropdownData(ACCOUNT_DETAIL_TYPE_LIST, accountDetailTypeList);
        addDropdownData(BILL_SUB_TYPE_LIST, getBillSubTypes());
        addDropdownData(USER_LIST, Collections.EMPTY_LIST);
        addDropdownData(DESIGNATION_LIST, Collections.EMPTY_LIST);
        getNetPayableCodes();
        billDetailslist = new ArrayList<VoucherDetails>();
        billDetailslist.add(new VoucherDetails());
        final Map<String, String> mp = new LinkedHashMap<String, String>();
        mp.put("na", getText("na"));
        mp.put("yes", getText("yes"));
        mp.put("no", getText("no"));
        commonBean.setCheckListValuesMap(mp);
        // If the department is mandatory show the logged in users assigned department only.
        if (mandatoryFields.contains("department")) {
            List<Department> deptList;
            deptList = masterDataCache.get("egi-department");
            addDropdownData("departmentList", deptList);
            addDropdownData("billDepartmentList", persistenceService.findAllBy("from Department order by name"));
        }
    }

    public void prepareNewform() {
        billDetailslist = new ArrayList<VoucherDetails>();
        billDetailslist.add(new VoucherDetails());
        billDetailsTableFinal = null;
        billDetailsTableNetFinal = null;
        billDetailsTableCreditFinal = null;
        checkListsTable = null;
        subledgerlist = null;
    }

    private void getNetPayableCodes() {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "contingencyBillPurposeIds");
        final List<AppConfigValues> configValuesByModuleAndKeydefault = appConfigValuesService.getConfigValuesByModuleAndKey(
                "EGF",
                "contingencyBillDefaultPurposeId");
        final String tempCBillDefaulPurposeId = configValuesByModuleAndKeydefault.get(0).getValue();
        final Long cBillDefaulPurposeId = Long.valueOf(tempCBillDefaulPurposeId);
        netPayList = new ArrayList<CChartOfAccounts>();
        // CChartOfAccounts coa;
        List<CChartOfAccounts> accountCodeByPurpose = new ArrayList<CChartOfAccounts>();
        for (int i = 0; i < configValuesByModuleAndKey.size(); i++) {
            try {
                accountCodeByPurpose = chartOfAccountsHibernateDAO.getAccountCodeByPurpose(Integer
                        .valueOf(configValuesByModuleAndKey.get(i).getValue()));
            } catch (final NumberFormatException e) {
                LOGGER.error("Inside getNetPayableCodes" + e.getMessage(), e);
            } catch (final Exception e) {
                LOGGER.error("inside getNetPayableCodes" + e.getMessage());
            }
            for (final CChartOfAccounts coa : accountCodeByPurpose)
                // defaultNetPayCode=coa;
                detailTypeIdandName = coa.getGlcode() + "~" + getDetailTypesForCoaId(coa.getId()) + "^" + detailTypeIdandName;

            if (configValuesByModuleAndKey.get(i).getValue().equals(cBillDefaulPurposeId))
                for (final CChartOfAccounts coa : accountCodeByPurpose)
                    if (coa.getPurposeId().compareTo(cBillDefaulPurposeId) == 0)
                        defaultNetPayCode = coa;
            // detailTypeIdandName=coa.getGlcode()+"~"+getDetailTypesForCoaId(coa.getId())+"^"+detailTypeIdandName;

            netPayList.addAll(accountCodeByPurpose);

        }
        Collections.sort(netPayList, new COAcomparator());
        for (final CChartOfAccounts c : netPayList)
            if (LOGGER.isInfoEnabled())
                LOGGER.info(c.getGlcode());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("netPayList............................." + netPayList.size());
        getSession().put("netPayList", netPayList);
    }

    @Override
    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/bill/contingentBill-newform")
    public String newform() {
        List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        Date date;
        if (!cutOffDateconfigValue.isEmpty())
        {
            try {
                date = df.parse(cutOffDateconfigValue.get(0).getValue());
                cutOffDate = formatter.format(date);
            } catch (ParseException e) {

            }
        }
        reset();
        commonBean.setBillDate(getDefaultDate());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("bigllDetailslist.............................." + billDetailslist.size());
        return NEW;
    }

    @ValidationErrorPage(VIEW)
    @SkipValidation
    @Action(value = "/bill/contingentBill-update")
    public String update() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Contingent Bill Action  | update | start");
        Integer userId = -1;
        try {
            bill = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?",
                    Long.valueOf(parameters.get("billRegisterId")[0]));
            if (null == bill.getEgBillregistermis().getSourcePath()) {
                bill.getEgBillregistermis().setSourcePath(
                        "/EGF/bill/contingentBill!beforeView.action?billRegisterId=" + bill.getId());
            }
            populateWorkflowBean();
            bill = egBillRegisterService.sendForApproval(bill, workflowBean);
            if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("bill.rejected",
                        new String[] { voucherService.getEmployeeNameForPositionId(bill.getState()
                                .getOwnerPosition()) }));
            if (FinancialConstants.BUTTONFORWARD.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("bill.forwarded",
                        new String[] { voucherService.getEmployeeNameForPositionId(bill.getState().getOwnerPosition()) }));
            if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
                addActionMessage(getText("cbill.cancellation.succesful"));
            else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                if ("Closed".equals(bill.getState().getValue()))
                    addActionMessage(getText("bill.final.approval", new String[] { "The File has been approved" }));
                else
                    addActionMessage(getText("bill.approved",
                            new String[] { voucherService.getEmployeeNameForPositionId(bill.getState()
                                    .getOwnerPosition()) }));
            }
        } catch (final ValidationException e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }

        return "messages";
    }

    private void reset() {
        voucherHeader.reset();
        commonBean.reset();
        billDetailsTableCreditFinal = null;
        billDetailsTableFinal = null;
        billDetailsTableNetFinal = null;
        subledgerlist = null;
        billDetailsTableSubledger = null;
        checkListsTable = null;
    }

    public void prepareCreate() {
        loadSchemeSubscheme();
    }

    @Validations(requiredFields = { @RequiredFieldValidator(fieldName = "fundId", message = "", key = REQUIRED),
            /* @RequiredFieldValidator(fieldName = "commonBean.billNumber", message = "", key = REQUIRED), */
            @RequiredFieldValidator(fieldName = "commonBean.billDate", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.billSubType", message = "", key = REQUIRED),
            @RequiredFieldValidator(fieldName = "commonBean.payto", message = "", key = REQUIRED)
            // Commenting function to revert onefunction center mandatory option
            // @RequiredFieldValidator(fieldName = "commonBean.functionName",message="",key=REQUIRED)
    })
    @ValidationErrorPage(value = NEW)
    @Action(value = "/bill/contingentBill-create")
    public String create() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info(billDetailsTableCreditFinal);
        try {
            voucherHeader.setVoucherDate(commonBean.getBillDate());
            voucherHeader.setVoucherNumber(commonBean.getBillNumber());
            String voucherDate = formatter1.format(voucherHeader.getVoucherDate());
            String cutOffDate1 = null;
            if (commonBean.getFunctionId() != null) {
                CFunction function1 = (CFunction) getPersistenceService().find(" from CFunction where id=?",
                        commonBean.getFunctionId().longValue());

                voucherHeader.getVouchermis().setFunction(function1);
            }
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
            // update DirectBankPayment source path
            headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/bill/contingentBill-beforeView.action?billRegisterId=");
            final EgBillregistermis mis = new EgBillregistermis();
            bill = setBillDetailsFromHeaderDetails(bill, mis, true);
            bill = createBillDetails(bill);
            validateLedgerAndSubledger();
            bill = checkBudgetandGenerateNumber(bill);
            // this code should be removed when we enable single function centre change

            validateFields();
            if (!isBillNumberGenerationAuto())
                if (!isBillNumUnique(commonBean.getBillNumber()))
                    throw new ValidationException(Arrays.asList(new ValidationError("bill number", "Duplicate Bill Number : "
                            + commonBean.getBillNumber())));
            populateWorkflowBean();
            bill = egBillRegisterService.createBill(bill, workflowBean, checkListsTable);
            addActionMessage(getText("cbill.transaction.succesful") + bill.getBillnumber());
            billRegisterId = bill.getId();

            if (!cutOffDate.isEmpty() && cutOffDate != null)
            {
                try {
                    date = sdf.parse(cutOffDate);
                    cutOffDate1 = formatter1.format(date);
                } catch (ParseException e) {

                }
            }
            if (cutOffDate1 != null && voucherDate.compareTo(cutOffDate1) <= 0
                    && FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            {
                if (bill.getEgBillregistermis().getBudgetaryAppnumber() != null)
                    addActionMessage(getText("budget.recheck.sucessful", new String[] { bill.getEgBillregistermis()
                            .getBudgetaryAppnumber() }));
            }
            else
            {
                if (bill.getEgBillregistermis().getBudgetaryAppnumber() != null)
                    addActionMessage(getText("budget.recheck.sucessful", new String[] { bill.getEgBillregistermis()
                            .getBudgetaryAppnumber() }));
                addActionMessage(getText("bill.forwarded",
                        new String[] { voucherService.getEmployeeNameForPositionId(bill.getState().getOwnerPosition()) }));
            }
        } catch (final ValidationException e) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Inside catch block");
            if (billDetailsTableSubledger == null)
                billDetailsTableSubledger = new ArrayList<VoucherDetails>();
            if (billDetailsTableSubledger.size() == 0)
                billDetailsTableSubledger.add(new VoucherDetails());
            prepare(); // session gets closed due to the transaction roll back while creating the sequence for the 1st time
            // required to call the prepare method again to populate the data to the screen.
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        }

        return "messages";
    }

    public List<String> getValidActions() {
        List<AppConfigValues> cutOffDateconfigValue = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "DataEntryCutOffDate");
        List<String> validActions = Collections.emptyList();
        if (!cutOffDateconfigValue.isEmpty())
        {
            if (null == bill || null == bill.getId() || bill.getCurrentState().getValue().endsWith("NEW")) {
                validActions = Arrays.asList(FORWARD, FinancialConstants.CREATEANDAPPROVE);
            } else {
                if (bill.getCurrentState() != null) {
                    validActions = this.customizedWorkFlowService.getNextValidActions(bill
                            .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                            getAdditionalRule(), bill.getCurrentState().getValue(),
                            getPendingActions(), bill.getCreatedDate());
                }
            }
        }
        else
        {
            if (null == bill || null == bill.getId() || bill.getCurrentState().getValue().endsWith("NEW")) {
                // read from constant
                validActions = Arrays.asList(FORWARD);
            } else {
                if (bill.getCurrentState() != null) {
                    validActions = this.customizedWorkFlowService.getNextValidActions(bill
                            .getStateType(), getWorkFlowDepartment(), getAmountRule(),
                            getAdditionalRule(), bill.getCurrentState().getValue(),
                            getPendingActions(), bill.getCreatedDate());
                }
            }
        }
        return validActions;
    }

    public String getNextAction() {
        WorkFlowMatrix wfMatrix = null;
        if (bill.getId() != null) {
            if (bill.getCurrentState() != null) {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(bill.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), bill
                                .getCurrentState().getValue(),
                        getPendingActions(), bill
                                .getCreatedDate());
            } else {
                wfMatrix = this.customizedWorkFlowService.getWfMatrix(bill.getStateType(),
                        getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), bill
                                .getCreatedDate());
            }
        }
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    @SkipValidation
    @ValidationErrorPage(value = EDIT)
    public String edit() {
        EgBillregister cbill = null;
        if (getButton().toLowerCase().contains("cancel"))
            cancelBill();
        else
            try {
                cbill = (EgBillregister) persistenceService.find("from EgBillregister where id=?", billRegisterId);
                if (cbill != null && cbill.getState() != null)
                    if (!validateOwner(cbill.getState()))
                        throw new ApplicationRuntimeException("Invalid Aceess");
                voucherHeader.setVoucherDate(commonBean.getBillDate());
                voucherHeader.setVoucherNumber(commonBean.getBillNumber());
                /*
                 * should be removed when enabling single function centre if(commonBean.getFunctionId()!=null){ //CFunction
                 * function=(CFunction) getPersistenceService().find(" from CFunction where id=?",
                 * commonBean.getFunctionId().longValue()); CFunction function =
                 * commonsService.getCFunctionById(commonBean.getFunctionId().longValue());
                 * voucherHeader.getVouchermis().setFunction(function); }
                 */
                validateFields();
                cbill = updateBill(cbill);
                validateLedgerAndSubledger();
                recreateCheckList(cbill);
                forwardBill(cbill);

            } catch (final ValidationException e) {
                LOGGER.error("Inside catch block" + e.getMessage());
                beforeViewWF(cbill);
                if (billDetailsTableSubledger == null)
                    billDetailsTableSubledger = new ArrayList<VoucherDetails>();
                if (billDetailsTableSubledger.size() == 0)
                    billDetailsTableSubledger.add(new VoucherDetails());
                throw e;
            }
        return "messages";
    }

    /**
     *
     */
    private void cancelBill() {

        EgBillregister cbill = null;
        cbill = (EgBillregister) persistenceService.find("from Cbill where id=?", billRegisterId);
        if (cbill != null && cbill.getState() != null)
            if (!validateOwner(cbill.getState()))
                throw new ApplicationRuntimeException("Invalid Aceess");
        if (parameters.get(ACTION_NAME)[0].contains("reject"))
            cbill.getCreatedBy().getId().intValue();
        // billRegisterWorkflowService.transition(parameters.get(ACTION_NAME)[0]+"|"+userId, cbill,parameters.get("comments")[0]);
        cbill.transition().end().withOwner(getPosition()).withComments(parameters.get("comments")[0]);
        final String statusQury = "from EgwStatus where upper(moduletype)=upper('" + FinancialConstants.CONTINGENCYBILL_FIN
                + "') and  upper(description)=upper('" + FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS + "')";
        final EgwStatus egwStatus = (EgwStatus) persistenceService.find(statusQury);
        cbill.setStatus(egwStatus);
        cbill.setBillstatus(FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS);
        // persistenceService.setType(Cbill.class);
        persistenceService.persist(cbill);
        persistenceService.getSession().flush();
        addActionMessage(getText("cbill.cancellation.succesful"));
    }

    private void removeEmptyRows() {
        final List<VoucherDetails> trash = new ArrayList<VoucherDetails>();
        if (billDetailsTableCreditFinal != null)
            for (final VoucherDetails vd : billDetailsTableCreditFinal)
                if (vd == null)
                    trash.add(vd);
                else if (vd.getGlcodeDetail() == null)
                    trash.add(vd);

        for (final VoucherDetails vd : trash)
            billDetailsTableCreditFinal.remove(vd);
        trash.clear();
        if (billDetailsTableFinal != null)
            for (final VoucherDetails vd : billDetailsTableFinal)
                if (vd == null)
                    trash.add(vd);
                else if (vd.getGlcodeDetail() == null)
                    trash.add(vd);
        for (final VoucherDetails vd : trash)
            billDetailsTableFinal.remove(vd);
        trash.clear();

        if (billDetailsTableSubledger != null)
            for (final VoucherDetails vd : billDetailsTableSubledger)
                if (vd == null)
                    trash.add(vd);
                else if (vd.getSubledgerCode() == null || vd.getSubledgerCode().equals(""))
                    trash.add(vd);
        for (final VoucherDetails vd : trash)
            billDetailsTableSubledger.remove(vd);

    }

    private void validateLedgerAndSubledger() {
        final List<VoucherDetails> finalList = new ArrayList<VoucherDetails>();
        removeEmptyRows();
        if (billDetailsTableFinal != null)
            finalList.addAll(billDetailsTableFinal);
        if (billDetailsTableCreditFinal != null)
            finalList.addAll(billDetailsTableCreditFinal);
        if (billDetailsTableNetFinal != null)
            finalList.addAll(billDetailsTableNetFinal);
        billDetailsTableSubledger = rearrangeSubledger(billDetailsTableSubledger);
        if (billDetailsTableSubledger == null)
            billDetailsTableSubledger = new ArrayList<VoucherDetails>();
        if (validateData(finalList, billDetailsTableSubledger))
            throw new ValidationException(Arrays.asList(new ValidationError("Ledger.validation.failed",
                    "Ledger.validation.failed")));
    }

    /**
     * @param billDetailsTableSubledger
     * @return
     */
    private List<VoucherDetails> rearrangeSubledger(final List<VoucherDetails> billDetailsTableSubledger) {
        if (billDetailsTableSubledger != null)
            if (commonBean.getSubledgerType() != null && commonBean.getSubledgerType() > 0) {
                final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                        "from Accountdetailtype where id=? order by name", commonBean.getSubledgerType());
                for (final VoucherDetails vd : billDetailsTableSubledger) {
                    vd.setAmount(vd.getDebitAmountDetail());
                    final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find(
                            "from CChartOfAccounts where glcode=?",
                            vd.getSubledgerCode());
                    vd.setGlcode(coa);
                    vd.setDetailType(detailType);
                    vd.setDetailKeyId(Integer.valueOf(vd.getDetailKey()));
                }
            }
        return billDetailsTableSubledger;
    }

    @SuppressWarnings("unchecked")
    private void recreateCheckList(final EgBillregister bill) {
        final List<EgChecklists> checkLists = persistenceService.findAllBy(
                "from org.egov.infstr.models.EgChecklists where objectid=?",
                billRegisterId);
        for (final EgChecklists chk : checkLists)
            persistenceService.delete(chk);
        // createCheckList(bill);
    }

    private EgBillregister updateBill(EgBillregister bill) {
        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
        headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/bill/contingentBill!beforeView.action?billRegisterId=");
        Boolean recreateBillnumber = false;
        if (bill.getEgBillregistermis().getEgDepartment() != null && voucherHeader.getVouchermis().getDepartmentid() != null)
            if (bill.getEgBillregistermis().getEgDepartment().getId() != voucherHeader.getVouchermis().getDepartmentid().getId())
                recreateBillnumber = true;
        bill = setBillDetailsFromHeaderDetails(bill, bill.getEgBillregistermis(), recreateBillnumber);
        final Set<EgBilldetails> EgBillSet = bill.getEgBilldetailes();
        final Iterator billDetItr = EgBillSet.iterator();
        EgBilldetails billDet = null;
        for (; billDetItr.hasNext();)
            try {
                billDet = (EgBilldetails) billDetItr.next();
                // if(LOGGER.isDebugEnabled()) LOGGER.debug(" billDet "+ billDet.getId());
                billDetItr.remove();
            } catch (final Exception e) {
                LOGGER.error("Inside updateBill" + e.getMessage(), e);

            }
        persistenceService.getSession().flush();
        bill.setEgBilldetailes(EgBillSet);
        EgBillSet.addAll(updateBillDetails(bill));
        checkBudgetandGenerateNumber(bill);
        persistenceService.getSession().refresh(bill);
        // persistenceService.setType(Cbill.class);
        persistenceService.persist(bill);
        persistenceService.getSession().flush();
        return bill;
    }

    private EgBillregister checkBudgetandGenerateNumber(final EgBillregister bill) {
        try {
            final ScriptContext scriptContext = ScriptService.createContext("voucherService", voucherService, "bill", bill);
            scriptService.executeScript("egf.bill.budgetcheck", scriptContext);
        } catch (final ValidationException e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return bill;
    }

    private void forwardBill(final EgBillregister cbill) {
        Integer userId = null;
        if (null != parameters.get(APPROVER_USER_ID) && Integer.valueOf(parameters.get(APPROVER_USER_ID)[0]) != -1)
            userId = Integer.valueOf(parameters.get(APPROVER_USER_ID)[0]);
        else
            userId = ApplicationThreadLocals.getUserId().intValue();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("User selected id is : " + userId);
        addActionMessage(getText("bill.forwarded",
                new String[] { voucherService.getEmployeeNameForPositionId(cbill.getState().getOwnerPosition()) }));
    }

    @SkipValidation
    @Action(value = "/bill/contingentBill-beforeView")
    public String beforeView() throws ClassNotFoundException {
        bill = egBillRegisterService.find("from EgBillregister where id=?", billRegisterId);
        /*
         * if (cbill.getState() != null && cbill.getState().getValue() != null) if
         * ((cbill.getState().getValue().contains("REJECT") || cbill.getState().getValue().contains("reject")) && null !=
         * parameters.get(MODE) && parameters.get(MODE)[0].equalsIgnoreCase(APPROVE)) return beforeEdit();
         */
        bill = prepareForViewModifyReverse();
        addDropdownData(USER_LIST, Collections.EMPTY_LIST);
        addDropdownData("billDepartmentList", persistenceService.findAllBy("from Department order by name"));
        if (null != parameters.get(MODE) && parameters.get(MODE)[0].equalsIgnoreCase(APPROVE)) {
            mode = APPROVE;
        } else
            mode = VIEW;

        return VIEW;
    }

    @SuppressWarnings("unchecked")
    private void beforeViewWF(final EgBillregister cbill) {

        Map<String, Object> map;
        // This was previously loading the designation list according to the bill department since,
        // the bill department was by default loaded to the department dropdown
        // Now, we are loading the primary assignment department as the default in the dropdown(see mingle 2103,2102, 2104)
        // Hence the primary department is passed here
        if (primaryDepartment != null && primaryDepartment.getId() != null)
            map = voucherService.getDesgBYPassingWfItem("cbill.nextUser", cbill, primaryDepartment.getId().intValue());
        else
            map = voucherService.getDesgBYPassingWfItem("cbill.nextUser", cbill, voucherHeader.getVouchermis().getDepartmentid()
                    .getId().intValue());
        addDropdownData(DESIGNATION_LIST, (List<Map<String, Object>>) map.get(DESIGNATION_LIST));
        addDropdownData(USER_LIST, Collections.EMPTY_LIST);
        nextLevel = map.get(WFITEMSTATE) != null ? map.get(WFITEMSTATE).toString() : null;
    }

    private EgBillregister prepareForViewModifyReverse() throws ClassNotFoundException {
        billDetailsTableNetFinal = new ArrayList<VoucherDetails>();
        billDetailsTableCreditFinal = new ArrayList<VoucherDetails>();
        billDetailsTableFinal = new ArrayList<VoucherDetails>();
        billDetailsTableSubledger = new ArrayList<VoucherDetails>();
        checkListsTable = new ArrayList<CheckListHelper>();
        // getNetPayableCodes();
        final EgBillregister cbill = egBillRegisterService.find("from EgBillregister where id=?", billRegisterId);
        getHeadersFromBill(cbill);
        billAmount = cbill.getBillamount();
        final Set<EgBilldetails> egBilldetailes = cbill.getEgBilldetailes();
        for (final EgBilldetails detail : egBilldetailes) {
            // getAll Credits incuding net pay
            final VoucherDetails vd = new VoucherDetails();
            final BigDecimal glcodeid = detail.getGlcodeid();
            final CChartOfAccounts coa = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",
                    Long.valueOf(glcodeid.toString()));
            vd.setGlcodeDetail(coa.getGlcode());
            vd.setGlcodeIdDetail(coa.getId());
            vd.setAccounthead(coa.getName());
            vd.setCreditAmountDetail(detail.getCreditamount() != null ? detail.getCreditamount().setScale(2,
                    BigDecimal.ROUND_HALF_EVEN) : null);
            if (detail.getFunctionid() != null) {
                final CFunction functionById = (CFunction) functionHibernateDAO.findById(detail.getFunctionid().longValue(),
                        false);
                commonBean.setFunctionName(functionById.getName());
                commonBean.setFunctionId(functionById.getId().intValue());
            }
            if (coa.getChartOfAccountDetails().size() > 0)
                vd.setIsSubledger(TRUE);
            else
                vd.setIsSubledger(FALSE);
            if (netPayList.contains(coa)) {
                vd.setCreditAmountDetail(detail.getCreditamount() != null ? detail.getCreditamount().setScale(2,
                        BigDecimal.ROUND_HALF_EVEN) : null);
                billDetailsTableNetFinal.add(vd);

            } else if (detail.getCreditamount() != null && detail.getCreditamount().compareTo(BigDecimal.ZERO) != 0) {
                vd.setCreditAmountDetail(detail.getCreditamount().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                billDetailsTableCreditFinal.add(vd);
            }

            if (detail.getDebitamount() != null && detail.getDebitamount().compareTo(BigDecimal.ZERO) != 0) {
                vd.setDebitAmountDetail(detail.getDebitamount().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                billDetailsTableFinal.add(vd);
            }
            final Set<EgBillPayeedetails> egBillPaydetailes = detail.getEgBillPaydetailes();
            for (final EgBillPayeedetails payeedetail : egBillPaydetailes) {
                final VoucherDetails subVd = new VoucherDetails();
                subVd.setDetailKey(payeedetail.getAccountDetailKeyId().toString());
                subVd.setAccounthead(coa.getName());
                subVd.setGlcodeDetail(coa.getGlcode());
                subVd.setSubledgerCode(coa.getGlcode());
                commonBean.setSubledgerType(payeedetail.getAccountDetailTypeId());
                final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                        "from Accountdetailtype where id=? order by name", payeedetail.getAccountDetailTypeId());
                final String table = detailType.getFullQualifiedName();
                final Class<?> service = Class.forName(table);
                String tableName = service.getSimpleName();
                EntityType entity = null;
                String dataType = "";

                try {
                    final java.lang.reflect.Method method = service.getMethod("getId");

                    dataType = method.getReturnType().getSimpleName();
                    if (dataType.equals("Long"))
                        entity = (EntityType) persistenceService.find(
                                "from " + tableName + " where id=? order by name", payeedetail.getAccountDetailKeyId()
                                        .longValue());
                    else
                        entity = (EntityType) persistenceService.find(
                                "from " + tableName + " where id=? order by name", payeedetail.getAccountDetailKeyId());
                } catch (final Exception e) {
                    LOGGER.error("prepareForViewModifyReverse" + e.getMessage(), e);
                    throw new ApplicationRuntimeException(e.getMessage());
                }

                subVd.setDetailName(entity.getName());
                subVd.setDetailCode(entity.getCode());
                if (detail.getCreditamount() != null && detail.getCreditamount().compareTo(BigDecimal.ZERO) != 0)
                    subVd.setDebitAmountDetail(payeedetail.getCreditAmount());
                else
                    subVd.setDebitAmountDetail(payeedetail.getDebitAmount());

                billDetailsTableSubledger.add(subVd);
            }

        }
        if (billDetailsTableSubledger.size() == 0)
            billDetailsTableSubledger.add(new VoucherDetails());
        final BigDecimal amt = cbill.getPassedamount().setScale(2, BigDecimal.ROUND_HALF_EVEN);
        final String amountInWords = NumberToWord.convertToWord(amt.toString());
        sanctionedMessge = getText("cbill.getsanctioned.message", new String[] { amountInWords,
                cbill.getPassedamount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() });
        sanctionedMessge = sanctionedMessge.substring(0, sanctionedMessge.length() - 15);
        // persistenceService.setType(EgChecklists.class);
        final List<EgChecklists> checkLists = persistenceService.findAllBy(
                "from org.egov.infstr.models.EgChecklists where objectid=?",
                billRegisterId);
        for (final EgChecklists chk : checkLists) {
            final CheckListHelper chkHelper = new CheckListHelper();
            chkHelper.setName(chk.getAppconfigvalue().getValue());
            chkHelper.setVal(chk.getChecklistvalue());
            chkHelper.setId(chk.getAppconfigvalue().getId());
            checkListsTable.add(chkHelper);
        }
        return cbill;
    }

    /**
     * @param cbill
     */
    private void getHeadersFromBill(final EgBillregister cbill) {
        voucherHeader.setFundId(cbill.getEgBillregistermis().getFund());
        voucherHeader.getVouchermis().setDepartmentid(cbill.getEgBillregistermis().getEgDepartment());
        voucherHeader.getVouchermis().setDivisionid(cbill.getEgBillregistermis().getFieldid());
        voucherHeader.getVouchermis().setSchemeid(cbill.getEgBillregistermis().getScheme());
        voucherHeader.getVouchermis().setSubschemeid(cbill.getEgBillregistermis().getSubScheme());
        voucherHeader.getVouchermis().setFundsource(cbill.getEgBillregistermis().getFundsource());
        // voucherHeader.getVouchermis().setFunction(cbill.getEgBillregistermis().getFunction());
        voucherHeader.setDescription(cbill.getNarration());
        commonBean.setBillDate(cbill.getBilldate());
        commonBean.setBillNumber(cbill.getBillnumber());
        if (null != cbill.getEgBillregistermis().getEgBillSubType())
            commonBean.setBillSubType(cbill.getEgBillregistermis().getEgBillSubType().getId().intValue());
        commonBean.setInwardSerialNumber(cbill.getEgBillregistermis().getInwardSerialNumber());
        commonBean.setPartyBillNumber(cbill.getEgBillregistermis().getPartyBillNumber());
        commonBean.setPartyBillDate(cbill.getEgBillregistermis().getPartyBillDate());
        commonBean.setPayto(cbill.getEgBillregistermis().getPayto());
        if (null != cbill.getState())
            commonBean.setStateId(cbill.getState().getId());
        commonBean.setBudgetReappNo(cbill.getEgBillregistermis().getBudgetaryAppnumber());

        final String amountInWords = NumberToWord.amountInWords(cbill.getPassedamount().doubleValue());
        sanctionedMessge = getText("cbill.getsanctioned.message", new String[] { amountInWords,
                cbill.getPassedamount().toString() });
        sanctionedMessge = sanctionedMessge.substring(0, sanctionedMessge.length() - 15);
    }

    @SkipValidation
    @Action(value = "/bill/contingentBill-beforeEdit")
    public String beforeEdit() throws ClassNotFoundException {
        final EgBillregister cbill = prepareForViewModifyReverse();
        addDropdownData(USER_LIST, Collections.EMPTY_LIST);
        if (null != parameters.get(MODE) && parameters.get(MODE)[0].equalsIgnoreCase(APPROVE)) {
            beforeViewWF(cbill);
            mode = APPROVE;
        } else
            mode = "view";
        return EDIT;
    }

    @SkipValidation
    public String beforeReverse() throws ClassNotFoundException {
        prepareForViewModifyReverse();
        return REVERSE;
    }

    public List<CheckListHelper> getCheckListsTable() {
        return checkListsTable;
    }

    public void setCheckListsTable(final List<CheckListHelper> checkListsTable) {
        this.checkListsTable = checkListsTable;
    }

    @Override
    public String execute() {

        try {
            super.execute();
        } catch (final Exception e) {
            LOGGER.error("Inside execute" + e.getMessage(), e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        billDetailslist = new ArrayList<VoucherDetails>();
        billDetailslist.add(new VoucherDetails());
        billDetailslist.add(new VoucherDetails());
        return NEW;
    }

    private EgBillregister createBillDetails(final EgBillregister bill) {
        EgBilldetails billdetails;
        EgBillPayeedetails payeedetails;
        Set<EgBillPayeedetails> payeedetailsSet;
        final Set<EgBilldetails> billdetailsSet = new HashSet<EgBilldetails>();
        // if entity count is 1 or 0 save the payto in billregistermis else dont save
        String entityKey = null;
        int entityCount = 0;
        for (final VoucherDetails vd : billDetailsTableFinal) {
            billdetails = new EgBilldetails();
            billdetails.setGlcodeid(BigDecimal.valueOf(vd.getGlcodeIdDetail()));
            if (commonBean.getFunctionId() != null)
                billdetails.setFunctionid(BigDecimal.valueOf(commonBean.getFunctionId()));
            billdetails.setDebitamount(vd.getDebitAmountDetail());
            debitSum = debitSum.add(vd.getDebitAmountDetail());
            billdetails.setEgBillregister(bill);

            if (vd.getIsSubledger().equalsIgnoreCase(TRUE)) {
                payeedetailsSet = new HashSet<EgBillPayeedetails>();
                for (final VoucherDetails sub : billDetailsTableSubledger)
                    if (vd.getGlcodeDetail().equalsIgnoreCase(sub.getSubledgerCode())) {
                        payeedetails = new EgBillPayeedetails();
                        payeedetails.setDebitAmount(sub.getDebitAmountDetail());

                        payeedetails.setAccountDetailKeyId(Integer.valueOf(sub.getDetailKey()));
                        payeedetails.setAccountDetailTypeId(commonBean.getSubledgerType());
                        payeedetails.setLastUpdatedTime(new Date());
                        billdetails.setLastupdatedtime(new Date());
                        payeedetails.setEgBilldetailsId(billdetails);
                        payeedetailsSet.add(payeedetails);
                        if (entityKey == null)
                            entityKey = sub.getDetailKey();
                        if (!entityKey.equals(sub.getDetailKey()))
                            entityCount++;

                    }
                billdetails.setEgBillPaydetailes(payeedetailsSet);

            }
            billdetails.setLastupdatedtime(new Date());
            billdetailsSet.add(billdetails);
        }
        if (billDetailsTableCreditFinal != null)
            for (final VoucherDetails vd : billDetailsTableCreditFinal) {
                billdetails = new EgBilldetails();
                billdetails.setGlcodeid(BigDecimal.valueOf(vd.getGlcodeIdDetail()));
                if (commonBean.getFunctionId() != null)
                    billdetails.setFunctionid(BigDecimal.valueOf(commonBean.getFunctionId()));
                billdetails.setCreditamount(vd.getCreditAmountDetail());
                billdetails.setEgBillregister(bill);

                if (vd.getIsSubledger().equalsIgnoreCase(TRUE)) {
                    payeedetailsSet = new HashSet<EgBillPayeedetails>();
                    for (final VoucherDetails sub : billDetailsTableSubledger)
                        if (vd.getGlcodeDetail().equalsIgnoreCase(sub.getSubledgerCode())) {
                            payeedetails = new EgBillPayeedetails();
                            payeedetails.setCreditAmount(sub.getDebitAmountDetail());
                            payeedetails.setAccountDetailKeyId(Integer.valueOf(sub.getDetailKey()));
                            payeedetails.setAccountDetailTypeId(commonBean.getSubledgerType());
                            payeedetails.setLastUpdatedTime(new Date());
                            billdetails.setLastupdatedtime(new Date());
                            payeedetails.setEgBilldetailsId(billdetails);
                            payeedetailsSet.add(payeedetails);
                            if (entityKey == null)
                                entityKey = sub.getDetailKey();
                            if (!entityKey.equals(sub.getDetailKey()))
                                entityCount++;

                        }
                    billdetails.setEgBillPaydetailes(payeedetailsSet);

                }
                billdetails.setLastupdatedtime(new Date());
                billdetailsSet.add(billdetails);
            }

        for (final VoucherDetails vd : billDetailsTableNetFinal) {
            billdetails = new EgBilldetails();
            final String netGlCode = vd.getGlcodeDetail();
            final String[] netGl = netGlCode.split("-");

            final CChartOfAccounts netCoa = (CChartOfAccounts) persistenceService
                    .find("from CChartOfAccounts where glcode=?", netGl[0]);
            billdetails.setGlcodeid(BigDecimal.valueOf(netCoa.getId()));
            vd.setGlcodeIdDetail(netCoa.getId());
            if (isOneFunctionCenter())
                if (commonBean.getFunctionId() != null)
                    billdetails.setFunctionid(BigDecimal.valueOf(commonBean.getFunctionId()));
            // uncommenting the above code to implement one function center mandatory code.
            billdetails.setCreditamount(vd.getCreditAmountDetail());
            bill.setBillamount(debitSum);
            bill.setPassedamount(debitSum);
            billdetails.setEgBillregister(bill);
            if (vd.getIsSubledger().equalsIgnoreCase(TRUE)) {
                payeedetailsSet = new HashSet<EgBillPayeedetails>();
                for (final VoucherDetails sub : billDetailsTableSubledger)
                    if (vd.getGlcodeDetail().equalsIgnoreCase(sub.getSubledgerCode())) {
                        payeedetails = new EgBillPayeedetails();
                        payeedetails.setCreditAmount(sub.getDebitAmountDetail());
                        payeedetails.setAccountDetailKeyId(Integer.valueOf(sub.getDetailKey()));
                        payeedetails.setAccountDetailTypeId(commonBean.getSubledgerType());
                        payeedetails.setLastUpdatedTime(new Date());
                        billdetails.setLastupdatedtime(new Date());
                        payeedetails.setEgBilldetailsId(billdetails);
                        payeedetailsSet.add(payeedetails);
                        if (entityKey == null)
                            entityKey = sub.getDetailKey();
                        if (!entityKey.equals(sub.getDetailKey()))
                            entityCount++;
                    }
                billdetails.setEgBillPaydetailes(payeedetailsSet);

            }
            billdetails.setLastupdatedtime(new Date());
            billdetailsSet.add(billdetails);
        }

        bill.setEgBilldetailes(billdetailsSet);
        if (entityCount < 2)
            bill.getEgBillregistermis().setPayto(commonBean.getPayto());
        else
            bill.getEgBillregistermis().setPayto(FinancialConstants.MULTIPLE);
        return bill;
    }

    private Set<EgBilldetails> updateBillDetails(final EgBillregister bill) {
        EgBilldetails billdetails;
        EgBillPayeedetails payeedetails;
        Set<EgBillPayeedetails> payeedetailsSet;
        final Set<EgBilldetails> billdetailsSet = new HashSet<EgBilldetails>();
        // if entity count is 1 or 0 save the payto in billregistermis else dont save
        String entityKey = null;
        int entityCount = 0;
        for (final VoucherDetails vd : billDetailsTableFinal) {
            billdetails = new EgBilldetails();
            billdetails.setGlcodeid(BigDecimal.valueOf(vd.getGlcodeIdDetail()));
            if (commonBean.getFunctionId() != null)
                billdetails.setFunctionid(BigDecimal.valueOf(commonBean.getFunctionId()));
            billdetails.setDebitamount(vd.getDebitAmountDetail());
            debitSum = debitSum.add(vd.getDebitAmountDetail());
            billdetails.setEgBillregister(bill);

            if (vd.getIsSubledger().equalsIgnoreCase(TRUE)) {
                payeedetailsSet = new HashSet<EgBillPayeedetails>();
                for (final VoucherDetails sub : billDetailsTableSubledger) {
                    if (sub == null)
                        continue;
                    if (vd.getGlcodeDetail().equalsIgnoreCase(sub.getSubledgerCode())) {
                        payeedetails = new EgBillPayeedetails();
                        payeedetails.setDebitAmount(sub.getDebitAmountDetail());

                        payeedetails.setAccountDetailKeyId(Integer.valueOf(sub.getDetailKey()));
                        payeedetails.setAccountDetailTypeId(commonBean.getSubledgerType());
                        payeedetails.setEgBilldetailsId(billdetails);
                        payeedetailsSet.add(payeedetails);
                        if (entityKey == null)
                            entityKey = sub.getDetailKey();
                        if (!entityKey.equals(sub.getDetailKey()))
                            entityCount++;

                    }

                }
                billdetails.setEgBillPaydetailes(payeedetailsSet);

            }
            billdetails.setLastupdatedtime(new Date());
            billdetailsSet.add(billdetails);
        }
        if (billDetailsTableCreditFinal != null)
            for (final VoucherDetails vd : billDetailsTableCreditFinal) {
                billdetails = new EgBilldetails();
                billdetails.setGlcodeid(BigDecimal.valueOf(vd.getGlcodeIdDetail()));
                if (commonBean.getFunctionId() != null)
                    billdetails.setFunctionid(BigDecimal.valueOf(commonBean.getFunctionId()));
                billdetails.setCreditamount(vd.getCreditAmountDetail());
                billdetails.setEgBillregister(bill);

                if (vd.getIsSubledger().equalsIgnoreCase(TRUE)) {
                    payeedetailsSet = new HashSet<EgBillPayeedetails>();
                    for (final VoucherDetails sub : billDetailsTableSubledger) {
                        if (sub == null)
                            continue;
                        if (vd.getGlcodeDetail().equalsIgnoreCase(sub.getSubledgerCode())) {
                            payeedetails = new EgBillPayeedetails();
                            payeedetails.setCreditAmount(sub.getDebitAmountDetail());
                            payeedetails.setAccountDetailKeyId(Integer.valueOf(sub.getDetailKey()));
                            payeedetails.setAccountDetailTypeId(commonBean.getSubledgerType());
                            payeedetails.setEgBilldetailsId(billdetails);
                            payeedetailsSet.add(payeedetails);
                            if (entityKey == null)
                                entityKey = sub.getDetailKey();
                            if (!entityKey.equals(sub.getDetailKey()))
                                entityCount++;

                        }
                    }
                    billdetails.setEgBillPaydetailes(payeedetailsSet);

                }
                billdetails.setLastupdatedtime(new Date());
                billdetailsSet.add(billdetails);
            }

        for (final VoucherDetails vd : billDetailsTableNetFinal) {
            billdetails = new EgBilldetails();
            final String netGlCode = vd.getGlcodeDetail();
            final String[] netGl = netGlCode.split("-");

            final CChartOfAccounts netCoa = (CChartOfAccounts) persistenceService
                    .find("from CChartOfAccounts where glcode=?", netGl[0]);
            billdetails.setGlcodeid(BigDecimal.valueOf(netCoa.getId()));
            vd.setGlcodeIdDetail(netCoa.getId());
            // commented - msahoo Function is not required against the liability codes in the Bill
            /*
             * Uncommenting- Shamili :- Uncommented when one function center is made mandatory.
             */
            if (voucherHeader.getIsRestrictedtoOneFunctionCenter())
                if (commonBean.getFunctionId() != null)
                    billdetails.setFunctionid(BigDecimal.valueOf(commonBean.getFunctionId()));
            billdetails.setCreditamount(vd.getCreditAmountDetail());
            bill.setBillamount(debitSum);
            bill.setPassedamount(debitSum);
            billdetails.setEgBillregister(bill);
            if (vd.getIsSubledger().equalsIgnoreCase(TRUE)) {
                payeedetailsSet = new HashSet<EgBillPayeedetails>();
                for (final VoucherDetails sub : billDetailsTableSubledger) {
                    if (sub == null)
                        continue;
                    if (vd.getGlcodeDetail().equalsIgnoreCase(sub.getSubledgerCode())) {
                        payeedetails = new EgBillPayeedetails();
                        payeedetails.setCreditAmount(sub.getDebitAmountDetail());
                        payeedetails.setAccountDetailKeyId(Integer.valueOf(sub.getDetailKey()));
                        payeedetails.setAccountDetailTypeId(commonBean.getSubledgerType());
                        payeedetails.setEgBilldetailsId(billdetails);
                        payeedetailsSet.add(payeedetails);
                        if (entityKey == null)
                            entityKey = sub.getDetailKey();
                        if (!entityKey.equals(sub.getDetailKey()))
                            entityCount++;
                    }
                }
                billdetails.setEgBillPaydetailes(payeedetailsSet);

            }
            billdetails.setLastupdatedtime(new Date());
            billdetailsSet.add(billdetails);
        }

        // bill.setEgBilldetailes(billdetailsSet);
        if (entityCount < 2)
            bill.getEgBillregistermis().setPayto(commonBean.getPayto());
        return billdetailsSet;
    }

    /**
     * @param bill
     * @param headerDetails
     */
    private EgBillregister setBillDetailsFromHeaderDetails(final EgBillregister bill, final EgBillregistermis mis,
            final boolean generateBill) {

        mis.setEgDepartment(voucherHeader.getVouchermis().getDepartmentid());
        mis.setFund(voucherHeader.getFundId());
        mis.setScheme(voucherHeader.getVouchermis().getSchemeid());
        mis.setSubScheme(voucherHeader.getVouchermis().getSubschemeid());
        mis.setFieldid(voucherHeader.getVouchermis().getDivisionid());
        mis.setFundsource(voucherHeader.getVouchermis().getFundsource());
        mis.setFunction(voucherHeader.getVouchermis().getFunction());
        bill.setNarration(voucherHeader.getDescription());
        mis.setNarration(voucherHeader.getDescription());
        // mis.setSourcePath("/EGF/bill/contingentBill!beforeView.action?billRegisterId=");
        final EgBillSubType egBillSubType = (EgBillSubType) persistenceService.find("from EgBillSubType where id=?",
                commonBean.getBillSubType().longValue());
        mis.setEgBillSubType(egBillSubType);
        mis.setInwardSerialNumber(commonBean.getInwardSerialNumber());
        mis.setPartyBillNumber(commonBean.getPartyBillNumber());
        mis.setPartyBillDate(commonBean.getPartyBillDate());
        bill.setBilldate(commonBean.getBillDate());
        bill.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        mis.setEgBillregister(bill);
        mis.setLastupdatedtime(new Date());
        bill.setEgBillregistermis(mis);
        if (generateBill) {
            if (isBillNumberGenerationAuto())
                commonBean.setBillNumber(getNextBillNumber(bill));

            bill.setBillnumber(commonBean.getBillNumber());
        }

        bill.setBillstatus(FinancialConstants.CONTINGENCYBILL_CREATED_STATUS);
        final String statusQury = "from EgwStatus where upper(moduletype)=upper('" + FinancialConstants.CONTINGENCYBILL_FIN
                + "') and  upper(description)=upper('" + FinancialConstants.CONTINGENCYBILL_CREATED_STATUS + "')";
        final EgwStatus egwStatus = (EgwStatus) persistenceService.find(statusQury);
        bill.setStatus(egwStatus);
        bill.setBilltype("Final Bill");

        return bill;
    }

    /**
     * @param bill
     * @return
     */
    private String getNextBillNumber(final EgBillregister bill) {

        ExpenseBillNumberGenerator b = beanResolver.getAutoNumberServiceFor(ExpenseBillNumberGenerator.class);
        final String billNumber = b.getNextNumber(bill);

        return billNumber;
    }

    @SuppressWarnings("unchecked")
    public String getDetailTypesForCoaId(final Long id) {
        final StringBuffer detailTypeIdandName1 = new StringBuffer(500);
        final List<CChartOfAccountDetail> coaDetails = persistenceService.findAllBy(
                "from CChartOfAccountDetail where glCodeId.id=?", id);
        for (final CChartOfAccountDetail coad : coaDetails)
            detailTypeIdandName1.append(coad.getDetailTypeId().getId()).append("`-`");
        return detailTypeIdandName1.toString();

    }

    public String getSanctionedMessge() {
        return sanctionedMessge;
    }

    public void setSanctionedMessge(final String sanctionedMessge) {
        this.sanctionedMessge = sanctionedMessge;
    }

    // setters

    public String getComments() {
        if (!(BigDecimal.ZERO.compareTo(billAmount) == 0))
            return getText("bill.comments", new String[] { billAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).toPlainString() });
        else
            return "";
    }

    public boolean isShowPrintPreview() {
        return showPrintPreview;
    }

    public void setShowPrintPreview(final boolean showPrintPreview) {
        this.showPrintPreview = showPrintPreview;
    }

    public boolean isBillNumUnique(final String billNumber) {

        final String billNum = (String) persistenceService.find("select billnumber from EgBillregister where upper(billnumber)='"
                + billNumber.toUpperCase() + "'");
        if (null == billNum)
            return true;
        else
            return false;
    }

    public Integer getPrimaryDepartment() {
        return primaryDepartment.getId().intValue();
    }

    public WorkflowBean getWorkflowBean() {
        return workflowBean;
    }

    public void setWorkflowBean(WorkflowBean workflowBean) {
        this.workflowBean = workflowBean;
    }

    public String getCurrentState() {
        return bill.getState().getValue();
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

}