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
package org.egov.collection.web.actions.receipts;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.Challan;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.services.DebitAccountHeadDetailsService;
import org.egov.collection.service.ChallanService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.commons.Position;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({ @Result(name = ChallanAction.NEW, location = "challan-new.jsp"),
        @Result(name = CollectionConstants.CREATERECEIPT, location = "challan-createReceipt.jsp"),
        @Result(name = CollectionConstants.CANCELRECEIPT, location = "challan-cancelReceipt.jsp"),
        @Result(name = ChallanAction.SUCCESS, location = "challan-success.jsp"),
        @Result(name = CollectionConstants.VIEW, location = "challan-view.jsp"),
        @Result(name = CollectionConstants.REPORT, location = "challan-report.jsp"),
        @Result(name = ChallanAction.ERROR, location = "challan-error.jsp") })
public class ChallanAction extends BaseFormAction {

    private static final Logger LOGGER = Logger.getLogger(ChallanAction.class);
    private static final long serialVersionUID = 1L;

    protected List<String> headerFields;

    protected List<String> mandatoryFields;
    private List<ReceiptDetailInfo> subLedgerlist;
    private List<ReceiptDetailInfo> billDetailslist;

    private ReceiptHeader receiptHeader = new ReceiptHeader();

    private CollectionsUtil collectionsUtil;
    private FinancialsUtil financialsUtil;

    @Autowired
    private BoundaryService boundaryService;

    private String deptId;
    private Long boundaryId;

    private Department dept;
    private Boundary boundary;
    private CFunction function;

    private Long receiptId;

    private final String VIEW = CollectionConstants.VIEW;
    private CollectionCommon collectionCommon;
    private ReceiptHeaderService receiptHeaderService;
    private SimpleWorkflowService<Challan> challanWorkflowService;

    // Added for Challan Approval
    private String challanId;
    private String approvalRemarks;
    private Long positionUser;
    private Integer designationId;

    /**
     * A <code>String</code> value representing the challan number for which the challan has to be retrieved
     */
    private String challanNumber;

    private Boolean cashAllowed = Boolean.TRUE;
    private Boolean cardAllowed = Boolean.TRUE;
    private Boolean chequeAllowed = Boolean.TRUE;
    private Boolean ddAllowed = Boolean.TRUE;
    private Boolean bankAllowed = Boolean.TRUE;
    private Boolean onlineAllowed = Boolean.TRUE;

    /**
     * An instance of <code>InstrumentHeader</code> representing the cash instrument details entered by the user during receipt
     * creation
     */
    private InstrumentHeader instrHeaderCash;

    /**
     * An instance of <code>InstrumentHeader</code> representing the card instrument details entered by the user during receipt
     * creation
     */
    private InstrumentHeader instrHeaderCard;

    // Instrument information derived from UI
    private List<InstrumentHeader> instrumentProxyList;
    private int instrumentCount;

    private BigDecimal cashOrCardInstrumenttotal = BigDecimal.ZERO;

    private BigDecimal chequeInstrumenttotal = BigDecimal.ZERO;

    private String instrumentTypeCashOrCard;

    /**
     * A String representing the action chosen to be performed from the UI
     */
    private String actionName;

    /**
     * A String representing the action chosen to be performed from the UI
     */
    private String sourcePage;

    /**
     * A String representing the voucher number for the challan
     */
    private String voucherNumber;

    private final List<ValidationError> errors = new ArrayList<>(0);

    private String reportId;

    private Position position = null;

    /**
     * A <code>Long</code> array of receipt header ids , which have to be displayed for view/print/cancel purposes
     */
    private Long[] selectedReceipts;

    private String currentFinancialYearId;

    private ServiceDetails service;

    private PersistenceService<ServiceCategory, Long> serviceCategoryService;

    private PersistenceService<ServiceDetails, Long> serviceDetailsService;

    @Autowired
    private BankHibernateDAO bankDAO;

    @Autowired
    private FundHibernateDAO fundDAO;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsDAO;

    @Autowired
    private FunctionHibernateDAO functionDAO;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    private Long serviceCategoryId;

    private Long serviceId;
    private ChallanService challanService;
    private String approverName;
    private Long functionId;
    private Date cutOffDate;
    private String instrumentType;
    @Autowired
    private ApplicationContext beanProvider;

    public ChallanAction() {
        addRelatedEntity("receiptMisc.fund", Fund.class);
        addRelatedEntity("challan.service", ServiceDetails.class);
    }

    @Override
    public Object getModel() {
        if (receiptHeader.getReceiptMisc() == null)
            receiptHeader.setReceiptMisc(new ReceiptMisc());
        if (receiptHeader.getChallan() == null)
            receiptHeader.setChallan(new Challan());
        return receiptHeader;
    }

    /**
     * This method is invoked when the user clicks on Create Challan from Menu Tree
     *
     * @return
     */
    @Action(value = "/receipts/challan-newform")
    @ValidationErrorPage(value = ERROR)
    @SkipValidation
    public String newform() {
        setLoginDept();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cutOffDate = sdf.parse(collectionsUtil.getAppConfigValue(
                    CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.APPCONFIG_VALUE_COLLECTIONDATAENTRYCUTOFFDATE));
        } catch (final ParseException e) {
            LOGGER.error(getText("Error parsing Cut Off Date") + e.getMessage());
        }
        return NEW;
    }

    private void setLoginDept() {
        final Department loginUserDepartment = collectionsUtil.getDepartmentOfLoggedInUser();
        if (loginUserDepartment == null)
            throw new ValidationException(Arrays.asList(new ValidationError("Department does not exists",
                    "viewchallan.validation.error.user.notexists")));
        setDeptId(loginUserDepartment.getId().toString());
        setDept(loginUserDepartment);
        addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForChallanApproval());
    }

    /**
     * This method is invoked when user clicks on Save challan from Create Challan UI. All workflow transitions for the challan
     * happen through this method.
     *
     * @return the string
     */
    @Action(value = "/receipts/challan-save")
    @ValidationErrorPage(value = NEW)
    public String save() {

        if (!actionName.equals(CollectionConstants.WF_ACTION_NAME_REJECT_CHALLAN))
            if (getPositionUser() == null || getPositionUser() == -1)
                position = collectionsUtil.getPositionOfUser(collectionsUtil.getLoggedInUser());
            else
                position = collectionsUtil.getPositionById(positionUser);

        // this should changed later and made applicable to all wflow states
        if (actionName.equals(CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN)
                || actionName.equals(CollectionConstants.WF_ACTION_NAME_MODIFY_CHALLAN)
                || actionName.equals(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN))
            return saveChallan();
        else
            challanService.workflowtransition(receiptHeader.getChallan(), position, actionName, approvalRemarks);

        if (receiptHeader.getChallan().getState() != null
                && receiptHeader.getChallan().getState().getOwnerPosition() != null)
            approverName = collectionsUtil.getApproverName(receiptHeader.getChallan().getState().getOwnerPosition());
        return SUCCESS;
    }

    /**
     * This method in invoked when the user clicks on Create Challan Receipt from Menu Tree.
     *
     * @return the string
     */
    @ValidationErrorPage(value = "createReceipt")
    @SkipValidation
    @Action(value = "/receipts/challan-createReceipt")
    public String createReceipt() {
        if (challanNumber != null && !"".equals(challanNumber)) {
            receiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_VALIDRECEIPT_BY_CHALLANNO, challanNumber);

            if (receiptHeader == null) {
                receiptHeader = new ReceiptHeader();
                errors.add(new ValidationError(getText("challan.notfound.message"),
                        "No Valid Challan Found. Please check the challan number."));
                throw new ValidationException(errors);
            }

            if (CollectionConstants.CHALLAN_STATUS_CODE_CANCELLED.equals(receiptHeader.getChallan().getStatus()
                    .getCode())) {
                errors.add(new ValidationError(getText("challan.cancel.receipt.error"),
                        "Challan is cancelled. Cannot create Receipt for Challan."));
                throw new ValidationException(errors);
            }

            if (CollectionConstants.RECEIPT_STATUS_CODE_PENDING.equals(receiptHeader.getStatus().getCode())) {
                loadReceiptDetails();
                setCollectionModesNotAllowed();
            } else {
                errors.add(new ValidationError(getText("challanreceipt.created.message",
                        new String[] { receiptHeader.getReceiptnumber() }),
                        "Receipt Already Created For this Challan. Receipt Number is "
                                + receiptHeader.getReceiptnumber()));
                throw new ValidationException(errors);
            }
        }

        return CollectionConstants.CREATERECEIPT;
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/receipts/challan-saveChallan")
    public String saveChallan() {

        receiptHeader.getReceiptDetails().clear();
        errors.clear();
        addDropdownData("approverDepartmentList", collectionsUtil.getDepartmentsAllowedForChallanApproval());
        populateAndPersistChallanReceipt();
        if (receiptHeader.getChallan().getState() != null
                && receiptHeader.getChallan().getState().getOwnerPosition() != null &&
                !CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName))
            addActionMessage(getText(
                    "challan.savechallan.success",
                    new String[] {
                            collectionsUtil.getApproverName(receiptHeader.getChallan().getState().getOwnerPosition()),
                            receiptHeader.getChallan().getChallanNumber() }));

        if (CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName)) {
            addActionMessage(getText("challan.validatesuccess.message"));
            return viewChallan();
        }
        return viewChallan();
    }

    /**
     * This method is invoked to view the challan.
     *
     * @return
     */
    @Action(value = "/receipts/challan-viewChallan")
    @ValidationErrorPage(value = ERROR)
    @SkipValidation
    public String viewChallan() {
        if (challanId == null)
            receiptHeader = receiptHeaderService.findById(receiptId, false);
        else {
            receiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_RECEIPT_BY_CHALLANID, Long.valueOf(challanId));
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                cutOffDate = sdf.parse(collectionsUtil.getAppConfigValue(
                        CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.APPCONFIG_VALUE_COLLECTIONDATAENTRYCUTOFFDATE));
            } catch (final ParseException e) {
                LOGGER.error(getText("Error parsing Cut Off Date") + e.getMessage());
            }
        }
        setLoginDept();
        loadReceiptDetails();
        return VIEW;
    }

    /**
     * This method is invoked on click of create button from Create Challan Receipt Screen
     *
     * @return
     */
    @ValidationErrorPage(value = "createReceipt")
    @Action(value = "/receipts/challan-saveOrupdate")
    public String saveOrupdate() {
        try {
            errors.clear();

            // for post remittance cancellation
            if (receiptHeader.getReceiptHeader() != null)
                collectionCommon.cancelChallanReceiptOnCreation(receiptHeader);

            boolean setInstrument = true;
            List<InstrumentHeader> receiptInstrList = new ArrayList<>();

            receiptHeader.setIsReconciled(Boolean.FALSE);
            receiptHeader.setIsModifiable(Boolean.TRUE);
            receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);
            // is this reqd
            receiptHeader.setLocation(collectionsUtil.getLocationOfUser(getSession()));
            receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                    CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                    CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED));
            receiptHeader.setCreatedBy(collectionsUtil.getLoggedInUser());
            receiptHeader.setCreatedDate(new Date());

            if (setInstrument) {
                receiptInstrList = populateInstrumentDetails();
                setInstrument = false;
            }

            receiptHeader.setReceiptInstrument(new HashSet(receiptInstrList));

            BigDecimal debitAmount = BigDecimal.ZERO;

            for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
                debitAmount = debitAmount.add(receiptDetail.getCramount());

            DebitAccountHeadDetailsService debitAccountHeadService = (DebitAccountHeadDetailsService) beanProvider
                    .getBean(collectionsUtil.getBeanNameForDebitAccountHead());
            receiptHeader.addReceiptDetail(debitAccountHeadService.addDebitAccountHeadDetails(debitAmount, receiptHeader,
                    chequeInstrumenttotal, cashOrCardInstrumenttotal, instrumentTypeCashOrCard));

            if (chequeInstrumenttotal != null && chequeInstrumenttotal.compareTo(BigDecimal.ZERO) != 0)
                receiptHeader.setTotalAmount(chequeInstrumenttotal);

            if (cashOrCardInstrumenttotal != null && cashOrCardInstrumenttotal.compareTo(BigDecimal.ZERO) != 0)
                receiptHeader.setTotalAmount(cashOrCardInstrumenttotal);
            receiptHeaderService.setReceiptNumber(receiptHeader);
            receiptHeaderService.populateAndPersistReceipts(receiptHeader, receiptInstrList);
            final ReceiptHeader[] receipts = new ReceiptHeader[1];
            receipts[0] = receiptHeader;

            try {
                reportId = collectionCommon.generateReport(receipts, true);
            } catch (final Exception e) {
                LOGGER.error(CollectionConstants.REPORT_GENERATION_ERROR, e);
                throw new ApplicationRuntimeException(CollectionConstants.REPORT_GENERATION_ERROR, e);
            }
            return CollectionConstants.REPORT;
        } catch (final StaleObjectStateException exp) {
            errors.add(new ValidationError(getText("challanreceipt.created.staleobjectstate"),
                    "Receipt Already Created For this Challan.Go to Search Receipt screen to Re-print the receipt."));
            LOGGER.error("Receipt Already Created For this Challan", exp);
            throw new ValidationException(errors);
        } catch (final Exception exp) {
            errors.add(new ValidationError(getText("challanreceipt.create.errorincreate"),
                    "Error occured in Challan Receipt creation, please try again."));
            LOGGER.error("Error occured in Challan Receipt creation, please try again", exp);
            throw new ValidationException(errors);
        }
    }

    /**
     * This method generates the report for the requested challan
     *
     * @return
     */
    @Action(value = "/receipts/challan-printChallan")
    public String printChallan() {

        try {
            reportId = collectionCommon.generateChallan(receiptHeader, true);
        } catch (final Exception e) {
            LOGGER.error(CollectionConstants.REPORT_GENERATION_ERROR, e);
            throw new ApplicationRuntimeException(CollectionConstants.REPORT_GENERATION_ERROR, e);
        }
        setSourcePage("viewChallan");
        return CollectionConstants.REPORT;
    }

    /**
     * This method directs the user to cancel the requested challan receipt
     *
     * @return
     */
    @Action(value = "/receipts/challan-cancelReceipt")
    @SkipValidation
    public String cancelReceipt() {
        if (getSelectedReceipts() != null && getSelectedReceipts().length > 0) {
            receiptHeader = receiptHeaderService.findById(Long.valueOf(selectedReceipts[0]), false);
            loadReceiptDetails();
        }
        return CollectionConstants.CANCELRECEIPT;
    }

    /**
     * This method is invoked when receipt is cancelled
     *
     * @return
     */
    @Action(value = "/receipts/challan-saveOnCancel")
    @SkipValidation
    public String saveOnCancel() {
        boolean isInstrumentDeposited = false;
        setSourcePage(CollectionConstants.CANCELRECEIPT);

        /**
         * the model is the receipt header which has to be cancelled
         */
        for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument())
            if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                if (instrumentHeader.getStatusId().getDescription()
                        .equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
                    isInstrumentDeposited = true;
                    break;
                }
            } else if (instrumentHeader.getStatusId().getDescription()
                    .equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
                isInstrumentDeposited = true;
                break;
            }

        // post remittance cancellation
        if (isInstrumentDeposited) {

            if (collectionsUtil.checkChallanValidity(receiptHeader.getChallan())) {
                /**
                 * if instrument has been deposited create a new receipt in place of the cancelled the model is turned into a copy
                 * of the receipt to be cancelled without the instrument details
                 */

                // the reason for cancellation has to be persisted
                receiptHeaderService.persist(receiptHeader);

                receiptHeader = collectionCommon.createPendingReceiptFromCancelledChallanReceipt(receiptHeader);
                LOGGER.info(" Created a receipt in PENDING status in lieu of the cancelled receipt ");

                loadReceiptDetails();

                // set collection modes allowed rule through script
                setCollectionModesNotAllowed();

                return CollectionConstants.CREATERECEIPT;
            } else {
                // the receipt is cancelled, voucher is reversed, but instrument
                // is not cancelled
                collectionCommon.cancelChallanReceipt(receiptHeader, false);

                addActionMessage(getText("challan.expired.message",
                        "Please note that a new receipt can not be created as the corresponding challan "
                                + receiptHeader.getChallan().getChallanNumber() + " has expired"));
            }
        }
        // if instrument has not been deposited, cancel the old instrument,
        // reverse the
        // voucher and persist
        else {
            collectionCommon.cancelChallanReceipt(receiptHeader, true);

            // End work-flow for the cancelled receipt
            if (!receiptHeader.getState().getValue().equals(CollectionConstants.WF_STATE_END))
                receiptHeaderService.endReceiptWorkFlowOnCancellation(receiptHeader);
            // if the challan is valid, recreate a new receipt in pending state
            // and populate it with the
            // cancelled receipt details (except instrument and voucher details)
            if (collectionsUtil.checkChallanValidity(receiptHeader.getChallan())) {

                final ReceiptHeader newReceipt = collectionCommon
                        .createPendingReceiptFromCancelledChallanReceipt(receiptHeader);

                receiptHeaderService.persist(newReceipt);

                LOGGER.info(" Created a receipt in PENDING status in lieu of the cancelled receipt ");
            }
        }
        return SUCCESS;
    }

    public List<WorkflowAction> getValidActions() {
        Challan challan = receiptHeader.getChallan();
        if (challan == null)
            challan = new Challan();
        return challanWorkflowService.getValidActions(challan);
    }

    /**
     * Populate Instrument Details.
     *
     * @return the list
     */
    private List<InstrumentHeader> populateInstrumentDetails() {
        List<InstrumentHeader> instrumentHeaderList = new ArrayList<>();

        if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrumentTypeCashOrCard)) {
            instrHeaderCash.setInstrumentType(financialsUtil
                    .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));

            instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);

            // the cash amount is set into the object through binding
            // this total is needed for creating debit account head
            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCash.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderCash);
        }
        if (CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrumentTypeCashOrCard)) {
            instrHeaderCard.setInstrumentType(financialsUtil
                    .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CARD));

            if (instrHeaderCard.getTransactionDate() == null)
                instrHeaderCard.setTransactionDate(new Date());
            instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);

            // the instrumentNumber, transactionNumber, instrumentAmount are
            // set into the object directly through binding
            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCard.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderCard);
        }

        // cheque/DD types
        if (instrumentProxyList != null)
            if (getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                    || getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_DD))
                instrumentHeaderList = populateInstrumentHeaderForChequeDD(instrumentHeaderList, instrumentProxyList);
        instrumentHeaderList = receiptHeaderService.createInstrument(instrumentHeaderList);

        return instrumentHeaderList;
    }

    /**
     * This instrument creates instrument header instances for the receipt, when the instrument type is Cheque or DD. The created
     * <code>InstrumentHeader</code> instance is persisted
     *
     * @param k an int value representing the index of the instrument type as chosen from the front end
     * @return an <code>InstrumentHeader</code> instance populated with the instrument details
     */
    private List<InstrumentHeader> populateInstrumentHeaderForChequeDD(
            final List<InstrumentHeader> instrumentHeaderList, final List<InstrumentHeader> instrumentProxyList) {

        for (final InstrumentHeader instrumentHeader : instrumentProxyList) {
            if (getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
                instrumentHeader.setInstrumentType(financialsUtil
                        .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE));
            else if (getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_DD))
                instrumentHeader.setInstrumentType(financialsUtil
                        .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD));
            if (instrumentHeader.getBankId() != null) {
                final Bank bank = bankDAO.findById(instrumentHeader.getBankId().getId(), false);
                instrumentHeader.setBankId(bank);
            }
            chequeInstrumenttotal = chequeInstrumenttotal.add(instrumentHeader.getInstrumentAmount());
            instrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
            instrumentHeaderList.add(instrumentHeader);
        }
        return instrumentHeaderList;
    }

    /**
     * This method creates a receipt along with the challan. The receipt is created in PENDING status where as the challan is
     * created with a CREATED status. The receipt is actually created later when there is a request for it to be created against
     * the challan.
     */
    private void populateAndPersistChallanReceipt() {

        if (voucherNumber != null && !"".equals(voucherNumber)) {
            final CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_VOUCHERHEADER_BY_VOUCHERNUMBER, voucherNumber);

            if (voucherHeader == null)
                errors.add(new ValidationError("challan.invalid.vouchernumber",
                        "Voucher not found. Please check the voucher number."));
            receiptHeader.getChallan().setVoucherHeader(voucherHeader);
        }

        receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_PENDING));

        // recon flag should be set as false when the receipt is actually
        // created against the challan
        receiptHeader.setIsReconciled(Boolean.TRUE);
        receiptHeader.setIsModifiable(Boolean.FALSE);
        receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_CHALLAN);
        receiptHeader.setPaidBy(CollectionConstants.CHAIRPERSON);
        receiptHeader.setSource(Source.SYSTEM.toString());
        receiptHeader.setReceiptdate(new Date());
        receiptHeader.setService(serviceDetailsService.findById(serviceId, false));
        receiptHeader.getService().setServiceCategory(serviceCategoryService.findById(serviceCategoryId, false));

        receiptHeader.getReceiptMisc().setFund(
                fundDAO.fundById(receiptHeader.getReceiptMisc().getFund().getId(), false));

        final Department depart = (Department) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_DEPARTMENT_BY_ID, Long.valueOf(deptId));
        receiptHeader.getReceiptMisc().setDepartment(depart);
        if (boundaryId != null)
            receiptHeader.getReceiptMisc().setBoundary(boundaryService.getBoundaryById(boundaryId));
        receiptHeader.getReceiptMisc().setReceiptHeader(receiptHeader);

        BigDecimal debitamount = BigDecimal.ZERO;
        removeEmptyRows(billDetailslist);
        removeEmptyRows(subLedgerlist);
        int m = 0;

        validateAccountDetails();

        BigDecimal totalAmt = BigDecimal.ZERO;

        for (final ReceiptDetailInfo rDetails : billDetailslist) {
            final CChartOfAccounts account = chartOfAccountsDAO.getCChartOfAccountsByGlCode(rDetails.getGlcodeDetail());
            CFunction funcn = null;
            if (functionId != null)
                funcn = functionDAO.getFunctionById(functionId);
            ReceiptDetail receiptDetail = new ReceiptDetail(account, funcn, rDetails.getCreditAmountDetail(),
                    rDetails.getDebitAmountDetail(), null, Long.valueOf(m), null, null, receiptHeader,
                    PURPOSE.OTHERS.toString());
            receiptDetail.setCramount(rDetails.getCreditAmountDetail());

            totalAmt = totalAmt.add(receiptDetail.getCramount()).subtract(receiptDetail.getDramount());

            final CFinancialYear financialYear = financialYearDAO.findById(rDetails.getFinancialYearId(), false);
            receiptDetail.setFinancialYear(financialYear);

            if (rDetails.getCreditAmountDetail() == null)
                receiptDetail.setCramount(BigDecimal.ZERO);
            else
                receiptDetail.setCramount(rDetails.getCreditAmountDetail());

            if (rDetails.getDebitAmountDetail() == null)
                receiptDetail.setDramount(BigDecimal.ZERO);
            else
                receiptDetail.setDramount(rDetails.getDebitAmountDetail());

            receiptDetail = setAccountPayeeDetails(subLedgerlist, receiptDetail);
            receiptHeader.addReceiptDetail(receiptDetail);
            debitamount = debitamount.add(rDetails.getCreditAmountDetail());
            debitamount = debitamount.subtract(rDetails.getDebitAmountDetail());
            m++;
        }

        receiptHeader.setTotalAmount(totalAmt);
        receiptHeader.getChallan().setStatus(
                collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_CHALLAN,
                        CollectionConstants.CHALLAN_STATUS_CODE_CREATED));
        // set service in challan
        if (receiptHeader.getChallan().getService() != null && receiptHeader.getChallan().getService().getId() != null)
            receiptHeader.getChallan().setService(
                    (ServiceDetails) getPersistenceService().findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_ID,
                            receiptHeader.getChallan().getService().getId()));
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cutOffDate = sdf.parse(collectionsUtil.getAppConfigValue(
                    CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.APPCONFIG_VALUE_COLLECTIONDATAENTRYCUTOFFDATE));
        } catch (final ParseException e) {
            LOGGER.error(getText("Error parsing Cut Off Date") + e.getMessage());
        }
        if (receiptHeader.getChallan().getChallanDate().before(cutOffDate))
            actionName = CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN;
        receiptHeaderService.persistChallan(receiptHeader, position, actionName, approvalRemarks);
        receiptId = receiptHeader.getId();

        LOGGER.info("Persisted Challan and Created Receipt In Pending State For the Challan");
    }

    public ReceiptDetail setAccountPayeeDetails(final List<ReceiptDetailInfo> subLedgerlist,
            final ReceiptDetail receiptDetail) {
        for (final ReceiptDetailInfo subvoucherDetails : subLedgerlist)
            if (subvoucherDetails.getGlcode() != null && subvoucherDetails.getGlcode().getId() != 0
                    && subvoucherDetails.getGlcode().getId().equals(receiptDetail.getAccounthead().getId())) {

                final Accountdetailtype accdetailtype = (Accountdetailtype) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID, subvoucherDetails.getDetailType().getId());
                final Accountdetailkey accdetailkey = (Accountdetailkey) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_ACCOUNTDETAILKEY_BY_DETAILKEY, subvoucherDetails.getDetailKeyId(),
                        subvoucherDetails.getDetailType().getId());

                final AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(accdetailtype, accdetailkey,
                        subvoucherDetails.getAmount(), receiptDetail);

                receiptDetail.addAccountPayeeDetail(accPayeeDetail);
            }
        return receiptDetail;
    }

    /**
     * Validate Account Details.
     */
    protected void validateAccountDetails() {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        Integer index = 0;

        for (final ReceiptDetailInfo rDetails : billDetailslist) {
            index = index + 1;
            totalDrAmt = totalDrAmt.add(rDetails.getDebitAmountDetail());
            totalCrAmt = totalCrAmt.add(rDetails.getCreditAmountDetail());

            if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getGlcodeDetail().trim().length() == 0)
                errors.add(new ValidationError("challan.accdetail.emptyaccrow",
                        "No data entered in Account Details grid row : {0}", new String[] { index.toString() }));
            else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getGlcodeDetail().trim().length() != 0)
                errors.add(new ValidationError("challan.accdetail.amountZero",
                        "Enter debit/credit amount for the account code : {0}", new String[] { rDetails
                                .getGlcodeDetail() }));
            else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
                    && rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
                errors.add(new ValidationError("challan.accdetail.amount",
                        "Please enter either debit/credit amount for the account code : {0}", new String[] { rDetails
                                .getGlcodeDetail() }));
            else if ((rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0 || rDetails
                    .getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
                    && rDetails.getGlcodeDetail().trim().length() == 0)
                errors.add(new ValidationError("challan.accdetail.accmissing",
                        "Account code is missing for credit/debit supplied field in account grid row :{0}",
                        new String[] { "" + index }));
        }

        validateSubledgerDetails();

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    /**
     * Validate Subledger Details.
     */
    protected void validateSubledgerDetails() {

        Map<String, Object> accountDetailMap;
        final Map<String, BigDecimal> subledAmtmap = new HashMap<>();
        // this list will contain the details about the account code those are
        // detail codes.
        List<Map<String, Object>> subLegAccMap = null;
        for (final ReceiptDetailInfo rDetails : billDetailslist) {

            final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(
                    " from CChartOfAccountDetail" + " where glCodeId=(select id from CChartOfAccounts where glcode=?)",
                    rDetails.getGlcodeDetail());

            if (null != chartOfAccountDetail) {
                accountDetailMap = new HashMap<>();
                accountDetailMap.put("glcodeId", rDetails.getGlcodeIdDetail());
                accountDetailMap.put("glcode", rDetails.getGlcodeDetail());
                if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    accountDetailMap.put("amount", rDetails.getCreditAmountDetail());
                else if (rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    accountDetailMap.put("amount", rDetails.getDebitAmountDetail());
                if (null == subLegAccMap) {
                    subLegAccMap = new ArrayList<>();
                    subLegAccMap.add(accountDetailMap);
                } else
                    subLegAccMap.add(accountDetailMap);
            }
        }

        if (null != subLegAccMap) {
            final Map<String, String> subLedgerMap = new HashMap<>();

            for (final ReceiptDetailInfo rDetails : subLedgerlist)
                if (rDetails.getGlcode() != null && rDetails.getGlcode().getId() != 0) {
                    if (null == subledAmtmap.get(rDetails.getGlcode().getId().toString()))
                        subledAmtmap.put(rDetails.getGlcode().getId().toString(), rDetails.getAmount());
                    else {
                        final BigDecimal debitTotalAmount = subledAmtmap.get(rDetails.getGlcode().getId().toString())
                                .add(rDetails.getAmount());
                        subledAmtmap.put(rDetails.getGlcode().getId().toString(), debitTotalAmount);
                    }

                    final StringBuilder subledgerDetailRow = new StringBuilder();
                    subledgerDetailRow.append(rDetails.getGlcode().getId().toString())
                            .append(rDetails.getDetailType().getId().toString())
                            .append(rDetails.getDetailKeyId().toString());
                    if (null == subLedgerMap.get(subledgerDetailRow.toString()))
                        subLedgerMap.put(subledgerDetailRow.toString(), subledgerDetailRow.toString());
                    else
                        errors.add(new ValidationError("miscreciept.samesubledger.repeated",
                                "Same subledger should not allow for same account code"));
                }

            for (final Map<String, Object> map : subLegAccMap) {
                final String glcodeId = map.get("glcodeId").toString();
                if (null == subledAmtmap.get(glcodeId))
                    errors.add(new ValidationError("miscreciept.samesubledger.entrymissing",
                            "Subledger detail entry is missing for account code: {0}", new String[] { map.get("glcode")
                                    .toString() }));
                else if (!subledAmtmap.get(glcodeId).equals(new BigDecimal(map.get("amount").toString())))
                    errors.add(new ValidationError("miscreciept.samesubledger.entrymissing",
                            "Total subledger amount is not matching for account code : {0}", new String[] { map.get(
                                    "glcode").toString() }));
            }
        }
    }

    /**
     * Removes the empty rows.
     *
     * @param list the list
     */
    void removeEmptyRows(final List list) {
        for (final Iterator<ReceiptDetailInfo> detail = list.iterator(); detail.hasNext();)
            if (detail.next() == null)
                detail.remove();
    }

    /**
     * Load receipt details.
     */
    private void loadReceiptDetails() {
        setDeptId(receiptHeader.getReceiptMisc().getDepartment().getId().toString());
        setDept(receiptHeader.getReceiptMisc().getDepartment());
        if (!receiptHeader.getReceiptDetails().isEmpty()) {
            final CFunction functn = receiptHeader.getReceiptDetails().iterator().next().getFunction();
            if (functn != null) {
                setFunctionId(functn.getId());
                setFunction(functn);
            }
        }
        setBoundary(receiptHeader.getReceiptMisc().getBoundary());
        setServiceCategoryId(receiptHeader.getService().getServiceCategory().getId());
        setServiceId(receiptHeader.getService().getId());
        if (null != receiptHeader.getService() && null != receiptHeader.getService().getServiceCategory()
                && receiptHeader.getService().getServiceCategory().getId() != -1)
            addDropdownData("serviceList", serviceDetailsService.findAllByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CATEGORY_FOR_TYPE, receiptHeader.getService()
                            .getServiceCategory().getId(),
                    CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION,
                    Boolean.TRUE));
        else
            addDropdownData("serviceList", Collections.emptyList());
        setBillDetailslist(collectionCommon.setReceiptDetailsList(receiptHeader,
                CollectionConstants.COLLECTIONSAMOUNTTPE_BOTH));
        setSubLedgerlist(collectionCommon.setAccountPayeeList(receiptHeader));
        for (final ReceiptDetail rDetails : receiptHeader.getReceiptDetails())
            if (rDetails.getFunction() != null)
                setFunction(rDetails.getFunction());

        if (receiptHeader.getReceiptMisc().getBoundary() != null)
            setBoundaryId(receiptHeader.getReceiptMisc().getBoundary().getId());

        if (receiptHeader.getChallan() != null && receiptHeader.getChallan().getVoucherHeader() != null)
            setVoucherNumber(receiptHeader.getChallan().getVoucherHeader().getVoucherNumber());
        if (receiptHeader.getTotalAmount() != null)
            receiptHeader.setTotalAmount(receiptHeader.getTotalAmount().setScale(
                    CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));
    }

    /**
     * This method checks for the modes of payment allowed
     */
    private void setCollectionModesNotAllowed() {
        final List<String> modesNotAllowed = collectionsUtil.getCollectionModesNotAllowed(collectionsUtil
                .getLoggedInUser());
        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CASH))
            setCashAllowed(Boolean.FALSE);
        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CARD))
            setCardAllowed(Boolean.FALSE);
        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
            setChequeAllowed(Boolean.FALSE);
        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_DD))
            setDdAllowed(Boolean.FALSE);
        // if
        // (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_BANK))
        setBankAllowed(Boolean.FALSE);
        setOnlineAllowed(Boolean.FALSE);
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
     */
    @Override
    public void prepare() {

        setupChallanPage();

        if (receiptId != null) {
            receiptHeader = receiptHeaderService.findById(receiptId, false);
            receiptHeader = receiptHeaderService.merge(receiptHeader);

            if (receiptHeader.getChallan() != null && receiptHeader.getChallan().getService() != null
                    && receiptHeader.getChallan().getService().getId() == -1)
                receiptHeader.getChallan().setService(null);
        }
        addDropdownData("designationMasterList", Collections.emptyList());
        addDropdownData("postionUserList", Collections.emptyList());
        setCurrentFinancialYearId(collectionCommon.getFinancialYearIdByDate(new Date()));
        /**
         * super class prepare is called at the end to ensure that the modified values are available to the model. The super class
         * prepare need not run for cancel challan as the values should not be modified
         **/
        if (!CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN.equals(actionName))
            super.prepare();
    }

    /**
     * Setup challan page.
     */
    public void setupChallanPage() {
        headerFields = new ArrayList<>();
        mandatoryFields = new ArrayList<>();
        getHeaderMandateFields();

        if (headerFields.contains(CollectionConstants.FUND)) {
            setupDropdownDataExcluding("receiptMisc.fund");
            addDropdownData("fundList", collectionsUtil.getAllFunds());
        }
        addDropdownData("serviceCategoryList",
                serviceCategoryService.findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_SERVICE_CATEGORY));
        if (null != service && null != service.getServiceCategory() && service.getServiceCategory().getId() != -1)
            addDropdownData("serviceList", serviceDetailsService.findAllByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CATEGORY_FOR_TYPE, service.getServiceCategory().getId(),
                    CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION, Boolean.TRUE));
        else if (serviceCategoryId != null)
            addDropdownData("serviceList", serviceDetailsService.findAllByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CATEGORY_FOR_TYPE, serviceCategoryId,
                    CollectionConstants.SERVICE_TYPE_CHALLAN_COLLECTION, Boolean.TRUE));
        else
            addDropdownData("serviceList", Collections.emptyList());
        if (headerFields.contains(CollectionConstants.DEPARTMENT))
            addDropdownData("departmentList",
                    persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS));
        if (headerFields.contains(CollectionConstants.FUNCTION))
            addDropdownData("functionList", functionDAO.getAllActiveFunctions());
        if (headerFields.contains(CollectionConstants.FIELD))
            addDropdownData("fieldList", persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FIELD));
        setupDropdownDataExcluding("challan.service");
        addDropdownData("financialYearList",
                persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_ACTIVE_FINANCIAL_YEAR));
        if (getBillDetailslist() == null) {
            setBillDetailslist(new ArrayList<ReceiptDetailInfo>());
            getBillDetailslist().add(new ReceiptDetailInfo());
        }
        if (getSubLedgerlist() == null) {
            setSubLedgerlist(new ArrayList<ReceiptDetailInfo>());
            getSubLedgerlist().add(new ReceiptDetailInfo());
        }
        setHeaderFields(headerFields);
        setMandatoryFields(mandatoryFields);
        if (instrumentProxyList == null)
            instrumentCount = 0;
        else
            instrumentCount = instrumentProxyList.size();
        addDropdownData("bankBranchList", Collections.emptyList());
        addDropdownData("accountNumberList", Collections.emptyList());
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    /**
     * Gets the header mandate fields.
     *
     * @return the header mandate fields
     */
    protected void getHeaderMandateFields() {
        final List<AppConfigValues> appConfigList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MISMandatoryAttributesModule, CollectionConstants.MISMandatoryAttributesKey);

        for (final AppConfigValues appConfigVal : appConfigList) {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf('|'));
            headerFields.add(header);
            final String mandate = value.substring(value.indexOf('|') + 1);
            if (CollectionConstants.Mandatory.equalsIgnoreCase(mandate))
                mandatoryFields.add(header);
        }
    }

    public List getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(final List headerFields) {
        this.headerFields = headerFields;
    }

    public List getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(final List mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(final String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public List<ReceiptDetailInfo> getBillDetailslist() {
        return billDetailslist;
    }

    public void setBillDetailslist(final List<ReceiptDetailInfo> billDetailslist) {
        this.billDetailslist = billDetailslist;
    }

    public List<ReceiptDetailInfo> getSubLedgerlist() {
        return subLedgerlist;
    }

    public void setSubLedgerlist(final List<ReceiptDetailInfo> subLedgerlist) {
        this.subLedgerlist = subLedgerlist;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(final Department dept) {
        this.dept = dept;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(final Boundary boundary) {
        this.boundary = boundary;
    }

    public Long getBoundaryId() {
        return boundaryId;
    }

    public void setBoundaryId(final Long boundaryId) {
        this.boundaryId = boundaryId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(final String deptId) {
        this.deptId = deptId;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(final ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public void setReceiptId(final Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    /**
     * @return the challanId
     */
    public String getChallanId() {
        return challanId;
    }

    /**
     * @param challanId the challanId to set
     */
    public void setChallanId(final String challanId) {
        this.challanId = challanId;
    }

    /**
     * @return the approvalRemarks
     */
    public String getApprovalRemarks() {
        return approvalRemarks;
    }

    /**
     * @param approvalRemarks the approvalRemarks to set
     */
    public void setApprovalRemarks(final String approvalRemarks) {
        this.approvalRemarks = approvalRemarks;
    }

    /**
     * @return the positionUser
     */
    public Long getPositionUser() {
        return positionUser;
    }

    /**
     * @param positionUser the positionUser to set
     */
    public void setPositionUser(final Long positionUser) {
        this.positionUser = positionUser;
    }

    /**
     * @param collectionCommon the collectionCommon to set
     */
    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    /**
     * @param receiptHeaderService The receipt header service to set
     */
    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public Boolean getCashAllowed() {
        return cashAllowed;
    }

    public void setCashAllowed(final Boolean cashAllowed) {
        this.cashAllowed = cashAllowed;
    }

    public Boolean getCardAllowed() {
        return cardAllowed;
    }

    public void setCardAllowed(final Boolean cardAllowed) {
        this.cardAllowed = cardAllowed;
    }

    public InstrumentHeader getInstrHeaderCash() {
        return instrHeaderCash;
    }

    public void setInstrHeaderCash(final InstrumentHeader instrHeaderCash) {
        this.instrHeaderCash = instrHeaderCash;
    }

    public InstrumentHeader getInstrHeaderCard() {
        return instrHeaderCard;
    }

    public void setInstrHeaderCard(final InstrumentHeader instrHeaderCard) {
        this.instrHeaderCard = instrHeaderCard;
    }

    public BigDecimal getCashOrCardInstrumenttotal() {
        return cashOrCardInstrumenttotal;
    }

    public void setCashOrCardInstrumenttotal(final BigDecimal cashOrCardInstrumenttotal) {
        this.cashOrCardInstrumenttotal = cashOrCardInstrumenttotal;
    }

    public BigDecimal getChequeInstrumenttotal() {
        return chequeInstrumenttotal;
    }

    public void setChequeInstrumenttotal(final BigDecimal chequeInstrumenttotal) {
        this.chequeInstrumenttotal = chequeInstrumenttotal;
    }

    public String getInstrumentTypeCashOrCard() {
        return instrumentTypeCashOrCard;
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    public void setInstrumentTypeCashOrCard(final String instrumentTypeCashOrCard) {
        this.instrumentTypeCashOrCard = instrumentTypeCashOrCard;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public String getReportId() {
        return reportId;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(final String sourcePage) {
        this.sourcePage = sourcePage;
    }

    /**
     * @return the designationId
     */
    public Integer getDesignationId() {
        return designationId;
    }

    /**
     * @param designationId the designationId to set
     */
    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Long[] getSelectedReceipts() {
        return selectedReceipts;
    }

    public void setSelectedReceipts(final Long[] selectedReceipts) {
        this.selectedReceipts = selectedReceipts;
    }

    public String getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(final String currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public String amountInWords(final BigDecimal amount) {
        return NumberUtil.amountInWords(amount);
    }

    public List<InstrumentHeader> getInstrumentProxyList() {
        return instrumentProxyList;
    }

    public void setInstrumentProxyList(final List<InstrumentHeader> instrumentProxyList) {
        this.instrumentProxyList = instrumentProxyList;
    }

    public int getInstrumentCount() {
        return instrumentCount;
    }

    public void setInstrumentCount(final int instrumentCount) {
        this.instrumentCount = instrumentCount;
    }

    public ServiceDetails getService() {
        return service;
    }

    public void setService(final ServiceDetails service) {
        this.service = service;
    }

    public void setServiceCategoryService(final PersistenceService<ServiceCategory, Long> serviceCategoryService) {
        this.serviceCategoryService = serviceCategoryService;
    }

    public void setServiceDetailsService(final PersistenceService<ServiceDetails, Long> serviceDetailsService) {
        this.serviceDetailsService = serviceDetailsService;
    }

    public void setChallanWorkflowService(final SimpleWorkflowService<Challan> challanWorkflowService) {
        this.challanWorkflowService = challanWorkflowService;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(final Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(final Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public void setChallanService(final ChallanService challanService) {
        this.challanService = challanService;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(final String approverName) {
        this.approverName = approverName;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Long functionId) {
        this.functionId = functionId;
    }

    public Date getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(final Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public Boolean getBankAllowed() {
        return bankAllowed;
    }

    public void setBankAllowed(final Boolean bankAllowed) {
        this.bankAllowed = bankAllowed;
    }

    public Boolean getChequeAllowed() {
        return chequeAllowed;
    }

    public void setChequeAllowed(final Boolean chequeAllowed) {
        this.chequeAllowed = chequeAllowed;
    }

    public Boolean getDdAllowed() {
        return ddAllowed;
    }

    public void setDdAllowed(final Boolean ddAllowed) {
        this.ddAllowed = ddAllowed;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(final String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Boolean getOnlineAllowed() {
        return onlineAllowed;
    }

    public void setOnlineAllowed(final Boolean onlineAllowed) {
        this.onlineAllowed = onlineAllowed;
    }

    @Override
    public void validate() {
        super.validate();
        final Department loginUserDepartment = collectionsUtil.getDepartmentOfLoggedInUser();
        if (loginUserDepartment != null)
            setLoginDept();
        if (receiptHeader.getReceiptdate() != null
                && receiptHeader.getReceiptdate().before(
                        financialYearDAO.getFinancialYearByDate(new Date()).getStartingDate()))
            addActionError(getText("challan.error.receiptdate.lessthan.financialyear"));
        if (receiptHeader.getChallan().getChallanDate() != null
                && receiptHeader.getChallan().getChallanDate()
                        .before(financialYearDAO.getFinancialYearByDate(new Date()).getStartingDate()))
            addActionError(getText("challan.error.challandate.lessthan.financialyear"));
        if (receiptHeader.getReceiptdate() != null
                && receiptHeader.getReceiptdate().before(receiptHeader.getChallan().getChallanDate()))
            addActionError(getText("challan.error.receiptdate.lessthan.challandate"));
    }

}
