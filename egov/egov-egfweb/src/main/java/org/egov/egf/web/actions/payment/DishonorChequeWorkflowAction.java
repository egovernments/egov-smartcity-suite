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
package org.egov.egf.web.actions.payment;

//import com.exilant.eGov.src.domain.BankEntries;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.DishonorCheque;
import org.egov.model.instrument.DishonorChequeDetails;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.recoveries.Recovery;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.services.instrument.DishonorChequeService;
import org.egov.services.instrument.FinancialIntegrationService;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


@Results({
        @Result(name = "viewMessage", location = "dishonorChequeWorkflow-viewMessage.jsp"),
        @Result(name = "view", location = "dishonorChequeWorkflow-view.jsp")
})
public class DishonorChequeWorkflowAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DishonorChequeWorkflowAction.class);
    public PersistenceService<InstrumentHeader, Long> instrumentHeaderService;
    private EisUtilService eisService;
    private DishonorChequeService finDishonorChequeService;
    private SimpleWorkflowService<DishonorCheque> dishonorChequeWorkflowService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    private InstrumentService instrumentService;
    private static final String RECEIPT = "Receipt";
    private static final String JOURNAL_VOUCHER = "Journal Voucher";
    private EisCommonService eisCommonService;
    private boolean isRestrictedtoOneFunctionCenter;
    private @Autowired CreateVoucher createVoucher;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    private CVoucherHeader paymentVoucher;
    private static final String APPROVE = "approve";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    private VoucherHelper voucherHelper;
    private List<String> validActions = Collections.emptyList();
    protected Long dishonourChqId;
    private DishonorCheque dishonorChequeView = new DishonorCheque();
    private PersistenceService<CChartOfAccounts, Long> chartOfAccountService;
    private List<Department> approverDepartmentList = new ArrayList<Department>();
    private CVoucherHeader bankChargesReversalVoucher;

    private String actionName;
    private String approverDepartment;
    private Integer approverPositionId;
    private String approverDesignation;
    private String mode = null;
    private List desgnationList;
    
   // private @Autowired BankEntries bankEntries;

    public DishonorChequeWorkflowAction() {
        this.addRelatedEntity("instrumentHeader", InstrumentHeader.class);
        this.addRelatedEntity("originalVoucherHeader", CVoucherHeader.class);
        this.addRelatedEntity("status", EgwStatus.class);
        this.addRelatedEntity("bankchargeGlCodeId", CChartOfAccounts.class);
        this.addRelatedEntity("modifiedBy", User.class);
        this.addRelatedEntity("createdBy", User.class);
        this.addRelatedEntity("state", State.class);

    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("approverList", Collections.EMPTY_LIST);
    }

    @Override
    public Object getModel() {
        return dishonorChequeView;
    }

    @SkipValidation
    @Action(value = "/payment/dishonorChequeWorkflow-view")
    public String view()
    {

        mode = "approve";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(">>>>>>" + dishonourChqId);
        dishonorChequeView = finDishonorChequeService.find(" from DishonorCheque where id=?", dishonourChqId);
        for (final DishonorChequeDetails dc : dishonorChequeView.getDetails())
            if (dc.getFunctionId() != null)
            {
                final CFunction function = (CFunction) persistenceService.find("from CFunction  where id=? ", dc.getFunctionId()
                        .longValue());
                dc.setFunction(function);
            }
        populateWorkflowEntities();
        return "view";
    }

    public List<String> getValidActions()
    {
        List<String> validActionsList = Collections.emptyList();
        String tempValidAction = null;
        if (null == dishonorChequeView || dishonorChequeView.getId() == null)
            validActions = Arrays.asList("forward");
        else {
            final String validAction = (String) persistenceService.find(
                    "select validActions from WorkFlowMatrix where objectType=? " +
                            "and currentState =?", dishonorChequeView.getStateType(), dishonorChequeView.getCurrentState()
                            .getValue());
            if (null != validAction) {
                final StringTokenizer strToken = new StringTokenizer(validAction, ",");
                tempValidAction = null;
                validActionsList = new ArrayList<String>();
                while (strToken.hasMoreElements()) {
                    tempValidAction = strToken.nextToken();
                    validActionsList.add(tempValidAction);
                }
            }

        }
        validActions = validActionsList;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(">>>>>>" + validActions);
        return validActions;
    }

    public String getNextAction() {
        String nextActionTemp = "";
        if (null != dishonorChequeView && null != dishonorChequeView.getId())
            nextActionTemp = (String) persistenceService.find("select nextAction from WorkFlowMatrix where objectType=? " +
                    " and currentState=?", dishonorChequeView.getStateType(), dishonorChequeView.getCurrentState().getValue());
        return nextActionTemp;

    }

    public void populateWorkflowEntities() {
        approverDepartmentList = persistenceService.findAllBy("from Department order by deptName");
        eisCommonService.getEmployeeByUserId(ApplicationThreadLocals.getUserId());
        desgnationList = persistenceService.findAllBy("from Designation where name=?", "ACCOUNTS OFFICER");
        addDropdownData("approverDepartmentList", approverDepartmentList);
        addDropdownData("desgnationList", desgnationList);

    }

    private String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

  /*  BankEntries createBankEntry() {
        bankEntries.setBankAccountId(dishonorChequeView.getInstrumentHeader().getBankAccountId().getId().intValue());
        bankEntries.setRefNo(dishonorChequeView.getBankReferenceNumber());
        bankEntries.setTxnAmount(dishonorChequeView.getBankChargesAmt().toString());
        bankEntries.setType("P");
        bankEntries.setRemarks(dishonorChequeView.getBankreason());
        bankEntries.setTxnDate(sdf.format(dishonorChequeView.getTransactionDate()));
        bankEntries.setGlcodeId(dishonorChequeView.getBankchargeGlCodeId().getId().toString());
        return bankEntries;
    }
*/
    private CVoucherHeader createVoucherHeader(final String type, final String reason) throws ParseException {
        final CVoucherHeader voucherHeader = new CVoucherHeader();
        voucherHeader.setType(type);
        voucherHeader.setVoucherDate(dishonorChequeView.getTransactionDate());
        voucherHeader.setDescription(reason);
        return voucherHeader;
    }

    public CVoucherHeader createBankReversalVoucher() throws ParseException, HibernateException, TaskFailedException,
            SQLException {
        CVoucherHeader BankVoucher = null;
       // bankEntries = createBankEntry();
        final String narration = "Reversal Bank Charges Entry for receipt number "
                + dishonorChequeView.getOriginalVoucherHeader().getVoucherNumber() +
                ", Cheque Number " + dishonorChequeView.getInstrumentHeader().getInstrumentNumber() + " Cheque Dated :"
                + getFormattedDate(dishonorChequeView.getInstrumentHeader().getInstrumentDate());

        BankVoucher = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT, dishonorChequeView.getBankreason());
        if (!(null == dishonorChequeView.getBankreason() && dishonorChequeView.getBankreason().equals("")))
            BankVoucher.setDescription(narration);
        else
            BankVoucher.setDescription(dishonorChequeView.getBankreason());
        // BankVoucher.setDescription(narration);
        final InstrumentHeader instrument = instrumentService.addToInstrument(
                createInstruments("1", dishonorChequeView.getBankChargesAmt(), FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK))
                .get(0);
        instrumentService
                .updateInstrumentOtherDetailsStatus(instrument, dishonorChequeView.getTransactionDate(), BigDecimal.ZERO);
        BankVoucher.setName("Bank Entry");
        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(BankVoucher);

        // here is subledger
        BankVoucher = createVoucher(BankVoucher, headerDetails, "Bank Entry");

        updateInstrumentVoucherReference(Arrays.asList(instrument), BankVoucher);
        //bankEntries.setVoucherheaderId(BankVoucher.getId().toString());
       // bankEntries.setInstrumentHeaderId(instrument.getId());
       /// bankEntries.insert();

        return BankVoucher;

    }

    private void updateInstrumentDetailsAfterDishonor() {
        final InstrumentHeader instHeader = instrumentHeaderService.findByNamedQuery("INSTRUMENTHEADERBYID", dishonorChequeView
                .getInstrumentHeader().getId());
        // set the instrument status of dishonored state
        instHeader.setStatusId(getDishonoredStatus());
        instrumentHeaderService.persist(instHeader);
        final String instOtherDetailUpdate = "Update InstrumentOtherDetails iod set iod.dishonorBankRefNo=:refNo, iod.modifiedBy.id=:modifiedby , iod.modifiedDate=:modifiedDate , iod.instrumentStatusDate=:InstrumentUpdatedDate where "
                +
                " iod.instrumentHeaderId=:instrumentHeaderId ";
        final Query instOtherDetailUpdateQuery = persistenceService.getSession().createQuery(instOtherDetailUpdate.toString());
        instOtherDetailUpdateQuery.setString("refNo", dishonorChequeView.getBankReferenceNumber());
        instOtherDetailUpdateQuery.setLong("modifiedby", ApplicationThreadLocals.getUserId().intValue());
        instOtherDetailUpdateQuery.setDate("modifiedDate", new Date());
        instOtherDetailUpdateQuery.setDate("InstrumentUpdatedDate", dishonorChequeView.getTransactionDate());
        instOtherDetailUpdateQuery.setLong("instrumentHeaderId", dishonorChequeView.getInstrumentHeader().getId());

        instOtherDetailUpdateQuery.executeUpdate();
    }

    @Action(value = "/payment/dishonorChequeWorkflow-save")
    public String save() throws ParseException {
        LOGGER.info(">>>>>>>>>>" + getActionName());
        String returnValue = "view";
        final String actionNm = getActionName();
        Integer userId = null;   
        if (actionNm.equalsIgnoreCase(APPROVE)) {
            userId=   parameters.get("approverUserId") != null ? Integer.valueOf(parameters.get("approverUserId")[0]) :
                ApplicationThreadLocals.getUserId().intValue();
            createReversalVoucher();
            updateInstrumentDetailsAfterDishonor();
            addActionMessage("Cheque Dishonored Succesfully");
            mode = "print";
            dishonorChequeView.setBankchargesVoucherHeader(bankChargesReversalVoucher);
            dishonorChequeView.setReversalVoucherHeader(paymentVoucher);
            startChequeWorkflow(dishonorChequeView, actionNm, null);
            final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                    .getServletContext());
            final FinancialIntegrationService financialService = (FinancialIntegrationService) wac
                    .getBean("financialIntegrationService");
            try {
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug(dishonorChequeView.getInstrumentHeader());
                    LOGGER.debug("Calling integrated system");

                }

                if (null != financialService)
                {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Calling integrated service is " + financialService.getClass().getSimpleName());
                    financialService.updateCollectionsOnInstrumentDishonor(dishonorChequeView.getInstrumentHeader().getId());
                }
                else
                {
                    LOGGER.error("Unable to find the Integrated modules service  with name financialIntegrationService in  Web Applicaton context. ");
                    throw new ApplicationRuntimeException("Integrated modules service not found");

                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Completed  integrated system update.");
            } catch (final RuntimeException e) {
                LOGGER.error("Error in updating integrated system  " + e.getMessage(), e);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exception", e.getMessage()));
                throw new ValidationException(errors);
            }
            returnValue = "viewMessage";

        }
        else if (actionNm.equalsIgnoreCase("forward")) {
            userId =  parameters.get("approverUserId") != null ? Integer.valueOf(parameters.get("approverUserId")[0]) :
                ApplicationThreadLocals.getUserId().intValue();
            startChequeWorkflow(dishonorChequeView, actionNm,
                    null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);
            final EmployeeView nextUser = (EmployeeView) persistenceService.find("from EmployeeView where position.id=?", null/*
             * phoenix
             * dishonorChequeView
             * .
             * getApproverPositionId
             * ()
             */);
            addActionMessage(" Cheque is forwared successfully  " + nextUser.getName());
            returnValue = "viewMessage";

        } else if (actionNm.equalsIgnoreCase("cancel")) {
            startChequeWorkflow(dishonorChequeView, actionNm,
                    null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);
            final InstrumentHeader instHeader = instrumentHeaderService.findByNamedQuery("INSTRUMENTHEADERBYID", dishonorChequeView
                    .getInstrumentHeader().getId());
            // set the instrument status of deposited on cancel
            instHeader.setStatusId(getDepositedStatus());
            instrumentHeaderService.persist(instHeader);
            final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                    .getServletContext());
            final FinancialIntegrationService financialService = (FinancialIntegrationService) wac
                    .getBean("financialIntegrationService");
            if (null != financialService)
                financialService.updateSourceInstrumentVoucher(FinancialIntegrationService.EVENT_INSTRUMENT_DISHONOR_CANCEL,
                        dishonorChequeView.getInstrumentHeader().getId());
            addActionMessage(getText("Dishonor Workflow Cancelled"));
            returnValue = "viewMessage";
        } else if (actionNm.equalsIgnoreCase("reject")) {
            final InstrumentOtherDetails iob = (InstrumentOtherDetails) persistenceService
                    .find("from InstrumentOtherDetails where instrumentHeaderId.id=?", dishonorChequeView.getInstrumentHeader()
                            .getId());
            final User approverUser = (User) persistenceService.find("from User where id=?", iob.getPayinslipId().getCreatedBy()
                    .getId());
            eisService.getPrimaryPositionForUser(approverUser.getId(), new Date());
            // phoenix migration dishonorChequeView.setApproverPositionId(approvePos.getId());
            startChequeWorkflow(dishonorChequeView, actionNm,
                    null != parameters.get("approverComments") ? parameters.get("approverComments")[0] : null);
            addActionMessage(getText("Cheque is rejected Sent back to " + approverUser.getName()));
            returnValue = "viewMessage";
        }

        return returnValue;
    }

    // Create Payment Voucher for -------------->Receipt Reversal
    private String createReversalVoucher() throws ParseException {

        final String appConfigKey = "GJV_FOR_RCPT_CHQ_DISHON";
        final AppConfigValues appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, appConfigKey).get(0);
        appConfigValues.getValue();
        CVoucherHeader voucherHeader = null;
        // DishonoredEntriesDelegate delegat e = new DishonoredEntriesDelegate();

        // Create bank charges
        if (dishonorChequeView.getBankChargesAmt().compareTo(BigDecimal.ZERO) > 0)
            try {
                bankChargesReversalVoucher = createBankReversalVoucher();
            } catch (final HibernateException e) {
                LOGGER.error(e.getMessage(), e);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exception", e.getMessage()));
                throw new ValidationException(errors);
            } catch (final TaskFailedException e) {
                LOGGER.error(e.getMessage(), e);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exception", e.getMessage()));
                throw new ValidationException(errors);
            } catch (final SQLException e) {
                LOGGER.error(e.getMessage(), e);
                final List<ValidationError> errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("exception", e.getMessage()));
                throw new ValidationException(errors);
            }
        // Create bank charges receipt reversal voucher
        if (null != dishonorChequeView.getOriginalVoucherHeader().getType()
                && dishonorChequeView.getOriginalVoucherHeader().getType().equalsIgnoreCase(RECEIPT)
                || JOURNAL_VOUCHER.equalsIgnoreCase(dishonorChequeView.getOriginalVoucherHeader().getType())) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("CREATING RECEIPT Reversal  >>>>>>>>>>");

            voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL,
                    dishonorChequeView.getInstrumentDishonorReason());
            /*
             * //If reversal for receipt, then according to appconfig value get the prefix for voucher. if
             * (dishonorChequeView.getOriginalVoucherHeader().getType().equalsIgnoreCase(RECEIPT) &&
             * gjvForRcpt.equalsIgnoreCase("Y")){ voucherHeader =
             * createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL
             * ,dishonorChequeView.getInstrumentDishonorReason()); } else { voucherHeader =
             * createVoucherHeader(FinancialConstants.
             * STANDARD_VOUCHER_TYPE_PAYMENT,dishonorChequeView.getInstrumentDishonorReason()); }
             */
            if (!(null == dishonorChequeView.getInstrumentDishonorReason() && dishonorChequeView.getInstrumentDishonorReason()
                    .equals(""))) {
                final String narration = "Reversal Voucher Entry for receipt number "
                        + dishonorChequeView.getOriginalVoucherHeader().getVoucherNumber() +
                        ", Cheque Number " + dishonorChequeView.getInstrumentHeader().getInstrumentNumber() + " Cheque Dated :"
                        + getFormattedDate(dishonorChequeView.getInstrumentHeader().getInstrumentDate());
                voucherHeader.setDescription(narration);
            } else
                voucherHeader.setDescription(dishonorChequeView.getInstrumentDishonorReason());
            final List<InstrumentHeader> instrument = instrumentService.addToInstrument(createInstruments("1", getTotalAmount(),
                    FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK));
            instrument.get(0).setStatusId(getReconciledStatus());
            instrumentHeaderService.persist(instrument.get(0));
            // if(LOGGER.isInfoEnabled()) LOGGER.info("---------------------------"+debitAmount.toString());
            instrumentService.updateInstrumentOtherDetailsStatus(instrument.get(0), dishonorChequeView.getTransactionDate(),
                    getTotalAmount());
            voucherHeader.setName("Receipt Reversal");
            // voucherHeader.setDescription(dishonorChequeView.getInstrumentDishonorReason());
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader);
            paymentVoucher = createVoucher(voucherHeader, headerDetails, "Receipt Reversal");
            // String reversalVhIdValue = paymentVoucher.getId().toString();
            instrumentService.addToBankReconcilation(voucherHeader, instrument.get(0));
            updateInstrumentVoucherReference(instrument, paymentVoucher);

        }

        return "view";
    }

    private EgwStatus getReconciledStatus() {
        return egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
    }

    private BigDecimal getTotalAmount() {
        BigDecimal total = new BigDecimal(0);
        List<DishonorChequeDetails> tempdetails = new ArrayList<DishonorChequeDetails>();
        dishonorChequeView.setDetails(new HashSet<DishonorChequeDetails>(tempdetails));
        tempdetails = persistenceService.findAllBy(" from DishonorChequeDetails where header.id=? ", dishonorChequeView.getId());
        for (final DishonorChequeDetails dishonordetails : tempdetails)
            if (null != dishonordetails.getDebitAmt() && dishonordetails.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                total = total.add(dishonordetails.getDebitAmt());
            else
                total = total
                        .add(null != dishonordetails.getCreditAmount() ? dishonordetails.getCreditAmount() : BigDecimal.ZERO);
        return total;
    }

    void updateInstrumentVoucherReference(final List<InstrumentHeader> instrumentList, final CVoucherHeader voucherHeader) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Instrument header", instrumentList.get(0));
        iMap.put("Voucher header", voucherHeader);
        iList.add(iMap);
        instrumentService.updateInstrumentVoucherReference(iList);
    }

    private EgwStatus getDepositedStatus() {
        return egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
    }

    private EgwStatus getDishonoredStatus() {
        return egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.STATUS_MODULE_INSTRUMENT,
                FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
    }

    private List<HashMap<String, Object>> populateSubledgerDetails(final List<HashMap<String, Object>> accountdetails) {
        LOGGER.debug("Populating Subledger");
        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> subledgerMap = new HashMap<String, Object>();
        BigDecimal value = BigDecimal.ZERO;
        String reversalGlCodesStr = "";
        List<Object[]> slDetailsCredit = new ArrayList<Object[]>();
        List<Object[]> slDetailsDebit = new ArrayList<Object[]>();
        StringBuffer reversalGlCodes = new StringBuffer();
        final List<DishonorChequeDetails> tempdetails = persistenceService.findAllBy(
                "from DishonorChequeDetails where header.id=? ",
                dishonorChequeView.getId());
        for (final DishonorChequeDetails dishonordetails : tempdetails)
            reversalGlCodes = reversalGlCodes.append(dishonordetails.getGlcodeId().getGlcode()).append(',');

        reversalGlCodesStr = reversalGlCodes.substring(0, reversalGlCodes.length() - 1);
        new StringBuffer();
        // dishonCheqForm.setGlcodeChList(glCode);
        slDetailsCredit = persistenceService
                .findAllBy("select distinct gl.glcode, gd.detailTypeId.id, gd.detailKeyId,SUM(gd.amount)" +
                        " from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in("
                        + dishonorChequeView.getOriginalVoucherHeader().getId() + ")" +
                        " and gl.id = gd.generalLedgerId.id and gl.debitAmount >0 and gl.glcode in (" + reversalGlCodesStr
                        + ") group by gl.glcode, gd.detailTypeId.id, gd.detailKeyId");

        slDetailsDebit = persistenceService.findAllBy("select distinct gl.glcode, gd.detailTypeId.id, gd.detailKeyId,SUM(gd.amount)"
                +
                " from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in("
                + dishonorChequeView.getOriginalVoucherHeader().getId() + ")" +
                " and gl.id = gd.generalLedgerId.id and gl.creditAmount >0 and gl.glcode in (" + reversalGlCodesStr
                + ") group by gl.glcode, gd.detailTypeId.id, gd.detailKeyId");
        LOGGER.debug("Debit Side Subledger list size is " + slDetailsDebit.size());
        LOGGER.debug("Credit Side Subledger list size is " + slDetailsCredit.size());

        for (final HashMap<String, Object> chk : accountdetails) {
            if (null != slDetailsDebit && slDetailsDebit.size() > 0)
                for (final Object[] obj : slDetailsDebit) {
                    LOGGER.info(">>>>>>" + obj[0]);
                    subledgerMap = new HashMap<String, Object>();
                    if (chk.get(VoucherConstant.GLCODE).equals(obj[0])) {
                        value = BigDecimal.ZERO;
                        subledgerMap.put(VoucherConstant.GLCODE, obj[0].toString());
                        subledgerMap.put(VoucherConstant.DETAILTYPEID, obj[1].toString());
                        subledgerMap.put(VoucherConstant.DETAILKEYID, obj[2].toString());
                        value = new BigDecimal(chk.get(VoucherConstant.DEBITAMOUNT).toString());
                        if (value.compareTo(BigDecimal.ZERO) > 0)
                            subledgerMap.put(VoucherConstant.DEBITAMOUNT, chk.get(VoucherConstant.DEBITAMOUNT));
                        else {
                            subledgerMap.put(VoucherConstant.CREDITAMOUNT, chk.get(VoucherConstant.CREDITAMOUNT));
                            List<Recovery> tdslist = new ArrayList<Recovery>();
                            tdslist = persistenceService.findAllBy(" from Recovery where chartofaccounts.glcode="
                                    + obj[0].toString());
                            if (!tdslist.isEmpty()) {
                                for (final Recovery tds : tdslist)
                                    if (tds.getType().equals(obj[0].toString()))
                                        subledgerMap.put(VoucherConstant.TDSID, tds.getId());
                            } else
                                throw new ApplicationRuntimeException(
                                        "DishonoredChequeAction |  populatesubledgerDetails | not able to find either debit or credit amount");
                        }
                        subledgerDetails.add(subledgerMap);
                    }
                }
            if (null != slDetailsCredit && slDetailsCredit.size() > 0)
                for (final Object[] obj : slDetailsCredit) {
                    LOGGER.info(">>>>>>" + obj[0]);
                    subledgerMap = new HashMap<String, Object>();
                    if (chk.get(VoucherConstant.GLCODE).equals(obj[0])) {
                        value = BigDecimal.ZERO;
                        subledgerMap.put(VoucherConstant.GLCODE, obj[0].toString());
                        subledgerMap.put(VoucherConstant.DETAILTYPEID, obj[1].toString());
                        subledgerMap.put(VoucherConstant.DETAILKEYID, obj[2].toString());
                        value = new BigDecimal(chk.get(VoucherConstant.DEBITAMOUNT).toString());
                        if (value.compareTo(BigDecimal.ZERO) > 0)
                            subledgerMap.put(VoucherConstant.DEBITAMOUNT, chk.get(VoucherConstant.DEBITAMOUNT));
                        else {
                            subledgerMap.put(VoucherConstant.CREDITAMOUNT, chk.get(VoucherConstant.CREDITAMOUNT));
                            List<Recovery> tdslist = new ArrayList<Recovery>();
                            tdslist = persistenceService.findAllBy(" from Recovery where chartofaccounts.glcode="
                                    + obj[0].toString());
                            if (!tdslist.isEmpty()) {
                                for (final Recovery tds : tdslist)
                                    if (tds.getType().equals(obj[0].toString()))
                                        subledgerMap.put(VoucherConstant.TDSID, tds.getId());
                            } else
                                throw new ApplicationRuntimeException(
                                        "DishonoredChequeAction |  populatesubledgerDetails | not able to find either debit or credit amount");
                        }
                        subledgerDetails.add(subledgerMap);
                    }
                }
        }
        return subledgerDetails;
    }

    private CVoucherHeader createVoucher(final CVoucherHeader voucher, final HashMap<String, Object> headerDetails,
            final String voucherType) {
        CVoucherHeader voucherHeader = null;
        List<HashMap<String, Object>> accountdetails = null;
        List<HashMap<String, Object>> subledgerDetails = null;
        try {
            headerDetails.put(VoucherConstant.SOURCEPATH, "");
            // subledgerDetails
            if ("Receipt Reversal".equalsIgnoreCase(voucherType)) {
                accountdetails = populateAccountDetails();
                subledgerDetails = populateSubledgerDetails(accountdetails);
            } else if ("Bank Entry".equalsIgnoreCase(voucherType)) {
                accountdetails = populateBankChargesAccountDetails();
                subledgerDetails = new ArrayList<HashMap<String, Object>>();
            }
            // TODO from headerDetails accountdetails subledgerDetails from these 3 populate intermediate objects and create
            // voucher at final aproval.
            voucherHeader = createVoucher.createVoucher(headerDetails, accountdetails, subledgerDetails);
            voucherHeader.getVouchermis().setSourcePath("");
            voucherHeader.setOriginalvcId(null);
        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return voucherHeader;
    }

    private List<HashMap<String, Object>> populateAccountDetails() {
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        final List<DishonorChequeDetails> tempdetails = persistenceService.findAllBy(
                "from DishonorChequeDetails where header.id=? ",
                dishonorChequeView.getId());
        BigDecimal totalAmountDbt = BigDecimal.ZERO;
        BigDecimal totalAmountCrd = BigDecimal.ZERO;
        CFunction glFunctionObj = new CFunction();
        if (RECEIPT.equals(dishonorChequeView.getOriginalVoucherHeader().getType())
                || JOURNAL_VOUCHER.equals(dishonorChequeView.getOriginalVoucherHeader().getType())) {

            for (final DishonorChequeDetails dishonordetails : tempdetails)
                if (null != dishonordetails.getDebitAmt()) {
                    final BigDecimal amount = dishonordetails.getDebitAmt();
                    final String code = dishonordetails.getGlcodeId().getGlcode();
                    final Integer funct = dishonordetails.getFunctionId();
                    if (null != funct && funct > 0)
                        glFunctionObj = (CFunction) persistenceService.find(" from CFunction fn where id=?", funct.longValue());
                    if (amount.compareTo(BigDecimal.ZERO) != 0)
                        accountdetails.add(populateDetailMap(code, BigDecimal.ZERO, amount, glFunctionObj.getCode()));

                    totalAmountDbt = totalAmountDbt.add(amount);
                }

            for (final DishonorChequeDetails dishonordetails : tempdetails)
                if (null != dishonordetails.getCreditAmount()) {
                    final BigDecimal amount = dishonordetails.getCreditAmount();
                    final String code = dishonordetails.getGlcodeId().getGlcode();
                    final Integer funct = dishonordetails.getFunctionId();
                    if (null != funct && funct > 0)
                        glFunctionObj = (CFunction) persistenceService.find(" from CFunction fn where id=?", funct.longValue());
                    if (null != amount && amount.compareTo(BigDecimal.ZERO) != 0)
                    {
                        accountdetails.add(populateDetailMap(code, BigDecimal.ZERO, amount, glFunctionObj.getCode()));
                        totalAmountCrd = totalAmountCrd.add(amount);
                    }

                }

            accountdetails.add(populateDetailMap(dishonorChequeView.getInstrumentHeader().getBankAccountId().getChartofaccounts()
                    .getGlcode(), totalAmountDbt.subtract(totalAmountCrd), BigDecimal.ZERO));
        } else {
            for (final DishonorChequeDetails dishonordetails : tempdetails)
                if (null != dishonordetails.getDebitAmt()) {
                    final BigDecimal amount = dishonordetails.getDebitAmt();
                    final String code = dishonordetails.getGlcodeId().getGlcode();
                    accountdetails.add(populateDetailMap(code, BigDecimal.ZERO, amount));
                    totalAmountDbt = totalAmountDbt.add(amount);
                }
            accountdetails.add(populateDetailMap(dishonorChequeView.getInstrumentHeader().getBankAccountId().getChartofaccounts()
                    .getGlcode(),
                    BigDecimal.ZERO, totalAmountDbt));
        }
        return accountdetails;
    }

    List<HashMap<String, Object>> populateBankChargesAccountDetails() {
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        // List<DishonorChequeDetails> tempdetails= persistenceService.findAllBy("from DishonorChequeDetails where header.id=? ",
        // dishonorChequeView.getId());
        BigDecimal totalAmountDbt = BigDecimal.ZERO;
        BigDecimal totalAmountCrd = BigDecimal.ZERO;
        new CFunction();

        if (null != dishonorChequeView.getBankChargesAmt()
                && dishonorChequeView.getBankChargesAmt().compareTo(BigDecimal.ZERO) > 0) {
            final BigDecimal amount = dishonorChequeView.getBankChargesAmt();
            final String code = dishonorChequeView.getInstrumentHeader().getBankAccountId().getChartofaccounts().getGlcode();
            accountdetails.add(populateDetailMap(code, amount, BigDecimal.ZERO));
            totalAmountDbt = totalAmountDbt.add(amount);
        }

        /*
         * accountdetails.add(populateDetailMap(dishonorChequeView.getInstrumentHeader().getBankAccountId().getChartofaccounts().
         * getGlcode(), BigDecimal.ZERO,totalAmountDbt));
         */

        if (null != dishonorChequeView.getBankChargesAmt() && null != dishonorChequeView.getBankchargeGlCodeId()) {
            final BigDecimal amount = dishonorChequeView.getBankChargesAmt();
            final String code = dishonorChequeView.getBankchargeGlCodeId().getGlcode();
            accountdetails.add(populateDetailMap(code, BigDecimal.ZERO, amount));
            totalAmountCrd = totalAmountCrd.add(amount);
        }
        /*
         * accountdetails.add(populateDetailMap(dishonorChequeView.getInstrumentHeader().getBankAccountId().getChartofaccounts().
         * getGlcode(), totalAmountCrd,BigDecimal.ZERO));
         */

        return accountdetails;
    }

    HashMap<String, Object> populateDetailMap(final String glCode, final BigDecimal creditAmount, final BigDecimal debitAmount) {
        final HashMap<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, creditAmount.toString());
        detailMap.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
        detailMap.put(VoucherConstant.GLCODE, glCode);
        // detailMap.put(VoucherConstant.FUNCTIONCODE, glCode);
        return detailMap;
    }

    HashMap<String, Object> populateDetailMap(final String glCode, final BigDecimal creditAmount, final BigDecimal debitAmount,
            final String function) {
        final HashMap<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, creditAmount.toString());
        detailMap.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
        detailMap.put(VoucherConstant.GLCODE, glCode);
        detailMap.put(VoucherConstant.FUNCTIONCODE, function);
        return detailMap;
    }

    public void startChequeWorkflow(final DishonorCheque dishonorCheque, final String workFlowAction,
            final String approverComments)
    {
        // Get cheque creator details

        if (null == dishonorCheque.getState()) {
            final Position pos = eisService.getPrimaryPositionForUser(null/* phoenix dishonorCheque.getApproverPositionId() */,
                    new Date());
            dishonorCheque.transition().start().withOwner(pos).withComments("DishonorCheque Work flow started");
            dishonorChequeWorkflowService.transition("forward", dishonorCheque, "Created by SM");
        }

        if (null != workFlowAction && !"".equals(workFlowAction)) {
            final String comments = null == approverComments || "".equals(approverComments.trim()) ? "" : approverComments;
            dishonorChequeWorkflowService.transition(workFlowAction.toLowerCase(), dishonorCheque, comments);
        }
    }

    HashMap<String, Object> createHeaderAndMisDetails(final CVoucherHeader voucherHeader) throws ValidationException {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());
        headerdetails
                .put(VoucherConstant.ORIGIONALVOUCHER, String.valueOf(dishonorChequeView.getOriginalVoucherHeader().getId()));
        if (voucherHeader.getType().equalsIgnoreCase("Bank Entry"))
            headerdetails.put(VoucherConstant.STATUS, 0);
        else
            headerdetails.put(VoucherConstant.STATUS, 0);
        if (null != dishonorChequeView.getOriginalVoucherHeader().getVouchermis().getDepartmentid())
            headerdetails.put(
                    VoucherConstant.DEPARTMENTCODE,
                    ((Department) persistenceService.find("from Department where id=?", dishonorChequeView
                            .getOriginalVoucherHeader().getVouchermis().getDepartmentid().getId())).getCode());
        if (null != dishonorChequeView.getOriginalVoucherHeader().getFundId())
            headerdetails.put(
                    VoucherConstant.FUNDCODE,
                    ((Fund) persistenceService.find("from Fund where id=?", dishonorChequeView.getOriginalVoucherHeader()
                            .getFundId().getId())).getCode());
        if (null != dishonorChequeView.getOriginalVoucherHeader().getVouchermis().getFundsource())
            headerdetails.put(
                    VoucherConstant.FUNDSOURCECODE,
                    ((Fundsource) persistenceService.find("from Fundsource where id=?", dishonorChequeView
                            .getOriginalVoucherHeader().getVouchermis().getFundsource().getId())).getCode());
        if (null != dishonorChequeView.getOriginalVoucherHeader().getVouchermis().getFunction())
            headerdetails.put(
                    VoucherConstant.FUNCTIONCODE,
                    ((CFunction) persistenceService.find("from CFunction where id=?", dishonorChequeView
                            .getOriginalVoucherHeader().getVouchermis().getFunction())).getCode());
        if (null != dishonorChequeView.getOriginalVoucherHeader().getVouchermis().getDivisionid())
            headerdetails.put(VoucherConstant.DIVISIONID, dishonorChequeView.getOriginalVoucherHeader().getVouchermis()
                    .getDivisionid().getId());
        return headerdetails;
    }

    List<Map<String, Object>> createInstruments(final String isPayCheque, final BigDecimal instrumentAmount,
            final String instrumentType) {
        final Map<String, Object> iMap = new HashMap<String, Object>();
        final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
        iMap.put("Transaction number", dishonorChequeView.getInstrumentHeader().getInstrumentNumber());
        iMap.put("Transaction date", dishonorChequeView.getTransactionDate());
        iMap.put("Instrument amount", null != instrumentAmount ? instrumentAmount.doubleValue() : 0);
        iMap.put("Instrument type", instrumentType);
        iMap.put("Bank code", dishonorChequeView.getInstrumentHeader().getBankAccountId().getBankbranch().getBank().getCode());
        iMap.put("Bank branch name", dishonorChequeView.getInstrumentHeader().getBankAccountId().getBankbranch()
                .getBranchaddress1());
        iMap.put("Bank account id", dishonorChequeView.getInstrumentHeader().getBankAccountId().getId());
        iMap.put("Is pay cheque", isPayCheque);
        iList.add(iMap);
        return iList;
    }

    public boolean isRestrictedtoOneFunctionCenter() {
        return isRestrictedtoOneFunctionCenter;
    }

    public void setRestrictedtoOneFunctionCenter(
            final boolean isRestrictedtoOneFunctionCenter) {
        this.isRestrictedtoOneFunctionCenter = isRestrictedtoOneFunctionCenter;
    }

    public PersistenceService<InstrumentHeader, Long> getInstrumentHeaderService() {
        return instrumentHeaderService;
    }

    @Override
    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public EisUtilService getEisService() {
        return eisService;
    }

    public PersistenceService<CChartOfAccounts, Long> getChartOfAccountService() {
        return chartOfAccountService;
    }

    public void setInstrumentHeaderService(
            final PersistenceService<InstrumentHeader, Long> instrumentHeaderService) {
        this.instrumentHeaderService = instrumentHeaderService;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public void setChartOfAccountService(
            final PersistenceService<CChartOfAccounts, Long> chartOfAccountService) {
        this.chartOfAccountService = chartOfAccountService;
    }

    public void setFinDishonorChequeService(final DishonorChequeService finDishonorChequeService) {
        this.finDishonorChequeService = finDishonorChequeService;
    }

    public VoucherHelper getVoucherHelper() {
        return voucherHelper;
    }

    public void setVoucherHelper(final VoucherHelper voucherHelper) {
        this.voucherHelper = voucherHelper;
    }

    public Long getDishonourChqId() {
        return dishonourChqId;
    }

    public DishonorCheque getDishonorChequeView() {
        return dishonorChequeView;
    }

    public void setDishonourChqId(final Long dishonourChqId) {
        this.dishonourChqId = dishonourChqId;
    }

    public void setDishonorChequeView(final DishonorCheque dishonorChequeView) {
        this.dishonorChequeView = dishonorChequeView;
    }

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public String getActionName() {
        return actionName;
    }

    public InstrumentService getInstrumentService() {
        return instrumentService;
    }

    public void setInstrumentService(final InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public CVoucherHeader getPaymentVoucher() {
        return paymentVoucher;
    }

    public void setPaymentVoucher(final CVoucherHeader paymentVoucher) {
        this.paymentVoucher = paymentVoucher;
    }

    public CVoucherHeader getBankChargesReversalVoucher() {
        return bankChargesReversalVoucher;
    }

    public void setBankChargesReversalVoucher(
            final CVoucherHeader bankChargesReversalVoucher) {
        this.bankChargesReversalVoucher = bankChargesReversalVoucher;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public String getApproverDepartment() {
        return approverDepartment;
    }

    public List<Department> getApproverDepartmentList() {
        return approverDepartmentList;
    }

    public void setApproverDepartmentList(
            final List<Department> approverDepartmentList) {
        this.approverDepartmentList = approverDepartmentList;
    }

    public void setApproverDepartment(final String approverDepartment) {
        this.approverDepartment = approverDepartment;
    }

    public Integer getApproverPositionId() {
        return approverPositionId;
    }

    public void setApproverPositionId(final Integer approverPositionId) {
        this.approverPositionId = approverPositionId;
    }

    public String getApproverDesignation() {
        return approverDesignation;
    }

    public void setApproverDesignation(final String approverDesignation) {
        this.approverDesignation = approverDesignation;
    }

    public SimpleWorkflowService<DishonorCheque> getDishonorChequeWorkflowService() {
        return dishonorChequeWorkflowService;
    }

    public void setDishonorChequeWorkflowService(
            final SimpleWorkflowService<DishonorCheque> dishonorChequeWorkflowService) {
        this.dishonorChequeWorkflowService = dishonorChequeWorkflowService;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }
}