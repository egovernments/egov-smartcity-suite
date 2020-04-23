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
package org.egov.collection.web.actions.citizen;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.handler.BillInfoMarshaller;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.integration.services.DebitAccountHeadDetailsService;
import org.egov.collection.integration.services.ReconciliationService;
import org.egov.collection.service.CollectionService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@ParentPackage("egov")
@Results({ @Result(name = OnlineReceiptAction.NEW, location = "onlineReceipt-new.jsp"),
        @Result(name = OnlineReceiptAction.REDIRECT, location = "onlineReceipt-redirect.jsp"),
        @Result(name = OnlineReceiptAction.RESULT, location = "onlineReceipt-result.jsp"),
        @Result(name = OnlineReceiptAction.RECONRESULT, location = "onlineReceipt-reconresult.jsp"),
        @Result(name = CollectionConstants.REPORT, location = "onlineReceipt-report.jsp") })
public class OnlineReceiptAction extends BaseFormAction {

    public static final String REDIRECT = "redirect";
    protected static final String RESULT = "result";
    protected static final String RECONRESULT = "reconresult";
    private static final Logger LOGGER = Logger.getLogger(OnlineReceiptAction.class);
    private static final long serialVersionUID = 1L;
    private final List<ValidationError> errors = new ArrayList<>(0);
    private CollectionsUtil collectionsUtil;
    private ReceiptHeaderService receiptHeaderService;
    private CollectionService collectionService;
    private CollectionCommon collectionCommon;
    private BigDecimal onlineInstrumenttotal = BigDecimal.ZERO;
    private List<ReceiptDetail> receiptDetailList = new ArrayList<>(0);
    private BillInfoImpl collDetails = new BillInfoImpl();
    private String collectXML;
    private String serviceName;
    private List<String> collectionModesNotAllowed = new ArrayList<>(0);
    private Boolean overrideAccountHeads;
    private Boolean partPaymentAllowed;
    private BigDecimal totalAmountToBeCollected;
    private BigDecimal paymentAmount;
    private ReceiptHeader onlinePaymentReceiptHeader;
    private Long receiptId;
    private ReceiptHeader[] receipts;
    private Integer paymentServiceId = -1;
    private String reportId;
    private String serviceCode;
    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;
    private String responseMsg = "";
    private Boolean callbackForApportioning;
    private String receiptNumber;
    private String consumerCode;
    private String receiptResponse = CollectionConstants.BLANK;
    private ReceiptHeader receiptHeader;
    private String refNumber;
    private List<ServiceDetails> serviceDetailsList = new ArrayList<>(0);
    @Autowired
    private FundHibernateDAO fundDAO;
    private Boolean isTransactionPending = Boolean.FALSE;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    private String[] transactionId;
    private Long[] selectedReceipts;
    private String[] transactionDate;
    private String[] statusCode;
    private String[] remarks;
    private BigDecimal minimumAmount;
    private String displayMsg;
    private String payeeName;
    private List<ReceiptHeader> penidngTransaction = new ArrayList<>();
    private Long repayTransactionId;
    private static final String REPAY_VALIDATION_KEY = "onlineReceipts.repay.validate";
    private static final String VIEW_VALIDATION_KEY = "onlineReceipts.view.validate";
    @Autowired
    private ReconciliationService reconciliationService;

    @Autowired
    private ApplicationContext beanProvider;

    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/citizen/onlineReceipt-newform")
    public String newform() {
        pendingOnlineTransactions();
        return NEW;
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/citizen/onlineReceipt-saveNew")
    public String saveNew() {
        /**
         * initialise receipt info,persist receipt, create bill desk payment object and redirect to payment screen
         */
        if (callbackForApportioning != null && overrideAccountHeads != null && callbackForApportioning && !overrideAccountHeads)
            apportionBillAmount();
        ServiceDetails paymentService = null;
        if (null != paymentServiceId && paymentServiceId > 0)
            paymentService = (ServiceDetails) getPersistenceService().findByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_ID, paymentServiceId.longValue());
        setPaymentRequest(collectionService.populateAndPersistReceipts(paymentService, receiptHeader,
                getReceiptDetailList(), paymentAmount, CollectionConstants.COLLECTION_TYPE_ONLINECOLLECTION));
        return REDIRECT;
    }

    /**
     * @return
     */
    @ValidationErrorPage(value = RESULT)
    @Action(value = "/citizen/onlineReceipt-acceptMessageFromPaymentGateway")
    public String acceptMessageFromPaymentGateway() {
        LOGGER.info("responseMsg:	" + responseMsg);
        ServiceDetails paymentService;
        if (null != paymentServiceId && paymentServiceId > 0)
            paymentService = (ServiceDetails) getPersistenceService().findByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_ID, paymentServiceId.longValue());
        else
            paymentService = (ServiceDetails) getPersistenceService().findByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICECODE_PGI_BILLDESK);
        try {
            setPaymentResponse(collectionCommon.createPaymentResponse(paymentService, getMsg()));
        } catch (final ApplicationRuntimeException egovEx) {
            LOGGER.error("acceptMessageFromPaymentGateway" + egovEx);
            throw new ValidationException(Arrays.asList(new ValidationError(egovEx.getMessage(), egovEx.getMessage())));
        }

        onlinePaymentReceiptHeader = receiptHeaderService.findByNamedQuery(
                CollectionConstants.QUERY_PENDING_RECEIPT_BY_ID_AND_CONSUMERCODE, Long.valueOf(paymentResponse.getReceiptId()),
                paymentResponse.getAdditionalInfo6());

        if (onlinePaymentReceiptHeader != null) {
            if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
                processSuccessMsg();
            else if ((onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode()
                    .equals(CollectionConstants.SERVICECODE_PGI_BILLDESK)
                    && CollectionConstants.PGI_AUTHORISATION_CODE_WAITINGFOR_PAY_GATEWAY_RESPONSE
                            .equals(paymentResponse.getAuthStatus()))
                    || (onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode()
                            .equals(CollectionConstants.SERVICECODE_ATOM) &&
                            CollectionConstants.ATOM_AUTHORISATION_CODES_WAITINGFOR_PAY_GATEWAY_RESPONSE
                                    .contains(paymentResponse.getAuthStatus()))
                    || (onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode()
                            .equals(CollectionConstants.SERVICECODE_AXIS) &&
                            CollectionConstants.AXIS_AUTHORISATION_CODES_WAITINGFOR_PAY_GATEWAY_RESPONSE
                                    .contains(paymentResponse.getAuthStatus()))
                    || (onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode()
                            .equals(CollectionConstants.SERVICECODE_SBIMOPS)
                            && CollectionConstants.PGI_AUTHORISATION_CODE_PENDING.equals(paymentResponse.getAuthStatus()))) {
                final EgwStatus paymentStatus = collectionsUtil.getStatusForModuleAndCode(
                        CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                        CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
                onlinePaymentReceiptHeader.getOnlinePayment().setStatus(paymentStatus);
                onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(
                        paymentResponse.getAuthStatus());
                onlinePaymentReceiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
            } else
                processFailureMsg();
        } else {
            errors.add(
                    new ValidationError(getText("online.broken.transaction.error"), getText("online.broken.transaction.error")));
            LOGGER.info("onlinePaymentReceiptHeader object is null");
        }
        return RESULT;
    }

    /**
     * This method processes the failure message arriving from the payment gateway. The receipt and the online transaction are
     * both cancelled. The authorisation status for reason of failure is also persisted. The reason for payment failure is
     * displayed back to the user
     */
    private void processFailureMsg() {
        onlinePaymentReceiptHeader.setStatus(collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_FAILED));

        final EgwStatus paymentStatus = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_ONLINEPAYMENT, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
        onlinePaymentReceiptHeader.getOnlinePayment().setStatus(paymentStatus);

        onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(paymentResponse.getAuthStatus());

        receiptHeaderService.persist(onlinePaymentReceiptHeader);

        LOGGER.debug("Cancelled receipt after receiving failure message from the payment gateway");

        addActionError(getText(onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode().toLowerCase()
                + ".pgi."
                + paymentResponse.getAuthStatus()));
        receiptResponse = "FAILURE|NA";
    }

    /**
     * This method processes the success message arriving from the payment gateway. The receipt status is changed from PENDING to
     * APPROVED and the online transaction status is changed from PENDING to SUCCCESS. The authorisation status for success(0300)
     * for the online transaction is also persisted. An instrument of type 'ONLINE' is created with the transaction details and
     * are persisted along with the receipt details. Voucher for the receipt is created and the Financial System is updated. The
     * billing system is updated about the receipt creation. In case update to financial systems/billing system fails, the receipt
     * creation is rolled back and the receipt/payment status continues to be in PENDING state ( and will be reconciled manually).
     */
    private void processSuccessMsg() {
        errors.clear();

        // If receipt is already present in system, returns the existing
        // receiptNumber.

        if (onlinePaymentReceiptHeader.getReceiptnumber() != null
                && onlinePaymentReceiptHeader.getReceiptnumber().length() > 0)
            receiptResponse = "SUCCESS|" + onlinePaymentReceiptHeader.getReceiptnumber();
        else {

            onlinePaymentReceiptHeader = receiptHeaderService.createOnlineSuccessPayment(onlinePaymentReceiptHeader,
                    paymentResponse.getTxnDate(), paymentResponse.getTxnReferenceNo(), paymentResponse.getTxnAmount(),
                    paymentResponse.getAuthStatus(), null);
            receiptResponse = "SUCCESS|" + onlinePaymentReceiptHeader.getReceiptnumber();
        }
    }

    /**
     * This method is invoked for manually reconciling online payments. If a payment is reconciled as a Success Payment, the
     * receipt is created, the receipt is marked as APPROVED , the payment is marked as SUCCESS, and the voucher is created. If a
     * payment is reconciled as To Be Refunded or Refunded, the transaction details are persisted, receipt is marked as FAILED and
     * the payment is marked as TO BE REFUNDED/REFUNDED respectively. The billing system is updated about all the payments that
     * have been successful.
     *
     * @return
     */
    @ValidationErrorPage(value = "reconresult")
    @Action(value = "/citizen/onlineReceipt-reconcileOnlinePayment")
    public String reconcileOnlinePayment() {

        final ReceiptHeader[] receipts = new ReceiptHeader[selectedReceipts.length];

        Date transDate = null;

        errors.clear();

        for (int i = 0; i < getSelectedReceipts().length; i++) {
            receipts[i] = receiptHeaderService.findById(selectedReceipts[i], false);

            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            if (getTransactionDate()[i] != null) {
                final String vdt = getTransactionDate()[i];
                try {
                    transDate = sdf.parse(vdt);
                } catch (final ParseException e) {
                    LOGGER.debug("Error occured while parsing date " + e.getMessage());
                }
            }

            if (getStatusCode()[i].equals(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS)) {
                final List<ReceiptDetail> existingReceiptDetails = new ArrayList<>(0);

                for (final ReceiptDetail receiptDetail : receipts[i].getReceiptDetails())
                    if (!FinancialsUtil.isRevenueAccountHead(receiptDetail.getAccounthead(),
                            chartOfAccountsHibernateDAO.getBankChartofAccountCodeList(), persistenceService)) {
                        final ReceiptDetail newReceiptDetail = new ReceiptDetail();
                        if (receiptDetail.getOrdernumber() != null)
                            newReceiptDetail.setOrdernumber(receiptDetail.getOrdernumber());
                        if (receiptDetail.getDescription() != null)
                            newReceiptDetail.setDescription(receiptDetail.getDescription());
                        if (receiptDetail.getIsActualDemand() != null)
                            newReceiptDetail.setIsActualDemand(receiptDetail.getIsActualDemand());
                        if (receiptDetail.getFunction() != null)
                            newReceiptDetail.setFunction(receiptDetail.getFunction());
                        if (receiptDetail.getCramountToBePaid() != null)
                            newReceiptDetail.setCramountToBePaid(receiptDetail.getCramountToBePaid());
                        newReceiptDetail.setCramount(receiptDetail.getCramount());
                        newReceiptDetail.setAccounthead(receiptDetail.getAccounthead());
                        newReceiptDetail.setDramount(receiptDetail.getDramount());
                        newReceiptDetail.setPurpose(receiptDetail.getPurpose());
                        existingReceiptDetails.add(newReceiptDetail);
                    }
                final List<ReceiptDetail> reapportionedList = collectionsUtil.reconstructReceiptDetail(receipts[i],
                        existingReceiptDetails);

                List<ReceiptDetail> reconstructedList = new ArrayList<ReceiptDetail>();
                if (reapportionedList != null && !reapportionedList.isEmpty()) {
                    reconstructedList = reapportionedList;
                } else {
                    reconstructedList = existingReceiptDetails;
                }

                ReceiptDetail debitAccountDetail = null;
                if (reconstructedList != null && !reconstructedList.isEmpty()) {
                    DebitAccountHeadDetailsService debitAccountHeadService = (DebitAccountHeadDetailsService) beanProvider
                            .getBean(collectionsUtil.getBeanNameForDebitAccountHead());
                    debitAccountDetail = debitAccountHeadService.addDebitAccountHeadDetails(receipts[i].getTotalAmount(),
                            receipts[i], BigDecimal.ZERO, receipts[i].getTotalAmount(),
                            CollectionConstants.INSTRUMENTTYPE_ONLINE);
                }

                receiptHeaderService.reconcileOnlineSuccessPayment(receipts[i], transDate, getTransactionId()[i],
                        receipts[i].getTotalAmount(), null, reconstructedList, debitAccountDetail, getRemarks()[i]);

                LOGGER.debug("Manually reconciled a success online payment");
            }

            if (CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED.equals(getStatusCode()[i])
                    || CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED.equals(getStatusCode()[i])
                    || CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE.equals(getStatusCode()[i])) {
                receipts[i].setStatus(collectionsUtil
                        .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_FAILED));
                receipts[i].getOnlinePayment().setRemarks(getRemarks()[i]);

                if (CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED.equals(getStatusCode()[i])
                        || CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED.equals(getStatusCode()[i])) {
                    receipts[i].getOnlinePayment().setTransactionNumber(getTransactionId()[i]);
                    receipts[i].getOnlinePayment().setTransactionAmount(receipts[i].getTotalAmount());
                    receipts[i].getOnlinePayment().setTransactionDate(transDate);
                }

                // set online payment status as TO BE REFUNDED/REFUNDED/FALURE
                receipts[i].getOnlinePayment().setStatus(
                        collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                                getStatusCode()[i]));
                receiptHeaderService.persist(receipts[i]);

                LOGGER.debug("Manually reconciled an online payment to " + getStatusCode()[i] + " state.");
            }
        }
        return RECONRESULT;
    }

    /**
     * If the online payment is pending, citizen can manually reconcile the transaction.
     * @return
     */
    @Action(value = "/citizen/onlineReceipt-repay")
    public String repay() {
        if (null != receiptHeader && isNotBlank(receiptHeader.getConsumerCode())
                && isNotBlank(receiptHeader.getService().getCode()))
            penidngTransaction = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE,
                    receiptHeader.getService().getCode(),
                    receiptHeader.getConsumerCode(), CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        if (getRepayTransactionId() == null || penidngTransaction == null
                || (getRepayTransactionId() != null && penidngTransaction
                        .stream().noneMatch(pendingReceipt -> pendingReceipt.getId().equals(getRepayTransactionId()))))
            throw new ValidationException(Arrays.asList(new ValidationError(REPAY_VALIDATION_KEY,
                    getText(REPAY_VALIDATION_KEY))));
        onlinePaymentReceiptHeader = receiptHeaderService.findById(getRepayTransactionId(), false);
        if (onlinePaymentReceiptHeader == null)
            throw new ValidationException(Arrays.asList(new ValidationError(REPAY_VALIDATION_KEY,
                    REPAY_VALIDATION_KEY)));
        paymentResponse = collectionCommon.repayReconciliation(onlinePaymentReceiptHeader.getOnlinePayment().getService(),
                onlinePaymentReceiptHeader.getOnlinePayment());
        if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
            reconciliationService.processSuccessMsg(onlinePaymentReceiptHeader, paymentResponse);
        else if (CollectionConstants.PGI_AUTHORISATION_CODE_PENDING.equals(paymentResponse.getAuthStatus())) {
            addActionMessage(getText("onlineReceipts.pending.validate",
                    new String[] { onlinePaymentReceiptHeader.getConsumerCode(),
                            onlinePaymentReceiptHeader.getId().toString() }));
        } else {
            reconciliationService.processFailureMsg(onlinePaymentReceiptHeader, paymentResponse);
            addActionError(getText(onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode().toLowerCase() + ".pgi." +
                    paymentResponse.getAuthStatus()));
        }
        return RESULT;
    }

    @Action(value = "/citizen/onlineReceipt-view")
    public String view() {
        if (getReceiptId() == null) {
            LOGGER.error(getText(VIEW_VALIDATION_KEY));
            throw new ValidationException(Arrays.asList(new ValidationError(VIEW_VALIDATION_KEY,
                    VIEW_VALIDATION_KEY)));
        } else {
            setReceipts(new ReceiptHeader[1]);
            receipts[0] = receiptHeaderService.findById(getReceiptId(), false);
            try {
                reportId = collectionCommon.generateReport(receipts, true);
            } catch (final Exception e) {
                LOGGER.error(CollectionConstants.REPORT_GENERATION_ERROR, e);
                throw new ApplicationRuntimeException(CollectionConstants.REPORT_GENERATION_ERROR, e);
            }
        }
        return CollectionConstants.REPORT;
    }

    @Action(value = "/citizen/onlineReceipt-viewReceipt")
    public String viewReceipt() {
        LOGGER.debug("::VIEWRECEIPT API::: Receipt Number=" + getReceiptNumber() + ", Consumer Code="
                + getConsumerCode());
        final ReceiptHeader receiptHead = (ReceiptHeader) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_RECEIPT_BY_SERVICE_RECEIPTNUMBER_CONSUMERCODE, getReceiptNumber(),
                getReceiptNumber(), getConsumerCode());

        if (receiptHead != null) {
            setReceiptId(receiptHead.getId());
            return view();
        } else
            throw new ValidationException(Arrays.asList(new ValidationError(VIEW_VALIDATION_KEY,
                    VIEW_VALIDATION_KEY)));

    }

    @Override
    public void prepare() {
        super.prepare();
        // populates model when request is from the billing system
        if (isNotBlank(getCollectXML())) {
            final String decodedCollectXml = decodeBillXML();
            try {
                collDetails = BillInfoMarshaller.toObject(decodedCollectXml);
                final Fund fund = fundDAO.fundByCode(collDetails.getFundCode());
                if (fund == null)
                    addActionError(getText("billreceipt.improperbilldata.missingfund"));

                final Department dept = (Department) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_DEPARTMENT_BY_CODE, collDetails.getDepartmentCode());
                if (dept == null)
                    addActionError(getText("billreceipt.improperbilldata.missingdepartment"));

                final ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_SERVICE_BY_CODE, collDetails.getServiceCode());

                setServiceName(service.getName());
                setCollectionModesNotAllowed(collDetails.getCollectionModesNotAllowed());
                setOverrideAccountHeads(collDetails.getOverrideAccountHeadsAllowed());
                setPartPaymentAllowed(collDetails.getPartPaymentAllowed());
                setCallbackForApportioning(collDetails.getCallbackForApportioning());
                totalAmountToBeCollected = BigDecimal.ZERO;

                receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(collDetails, fund, dept);
                setRefNumber(receiptHeader.getReferencenumber());
                setConsumerCode(isNotBlank(receiptHeader.getConsumerCode()) ? receiptHeader.getConsumerCode()
                        : CollectionConstants.BLANK);
                setDisplayMsg(isNotBlank(receiptHeader.getDisplayMsg()) ? receiptHeader.getDisplayMsg()
                        : CollectionConstants.BLANK);
                setPayeeName(isNotBlank(receiptHeader.getPayeeName()) ? receiptHeader.getPayeeName()
                        : CollectionConstants.BLANK);
                minimumAmount = receiptHeader.getMinimumAmount() == null ? BigDecimal.ZERO : receiptHeader.getMinimumAmount();
                totalAmountToBeCollected = totalAmountToBeCollected.add(receiptHeader.getTotalAmountToBeCollected());
                setReceiptDetailList(new ArrayList<>(receiptHeader.getReceiptDetails()));

                if (totalAmountToBeCollected.compareTo(BigDecimal.ZERO) < 0) {
                    addActionError(getText("billreceipt.totalamountlessthanzero.error"));
                    LOGGER.info(getText("billreceipt.totalamountlessthanzero.error"));
                } else
                    setTotalAmountToBeCollected(totalAmountToBeCollected.setScale(
                            CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));
            } catch (final Exception exp) {
                LOGGER.error(getText("billreceipt.error.improperbilldata"), exp);
                addActionError(getText("billreceipt.error.improperbilldata"));
            }
        }
        addDropdownData(
                "paymentServiceList",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE,
                        CollectionConstants.SERVICE_TYPE_PAYMENT));
        constructServiceDetailsList();
        // Fetching pending transaction by consumer code. If transaction is in pending status display message
        /*
         * if (null != receiptHeader && isNotBlank(receiptHeader.getConsumerCode()) &&
         * isNotBlank(receiptHeader.getService().getCode())) { penidngTransaction = getPersistenceService().findAllByNamedQuery(
         * CollectionConstants.QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE,
         * receiptHeader.getService().getCode(), receiptHeader.getConsumerCode(),
         * CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING); if (!penidngTransaction.isEmpty()) { isTransactionPending =
         * Boolean.TRUE; addActionMessage(getText("onlineReceipts.pending.validate", new String[] {
         * penidngTransaction.get(0).getConsumerCode(), penidngTransaction.get(0).getId().toString() })); } }
         */
    }

    // Fetching pending transaction by consumer code. If transaction is in pending status display message
    private void pendingOnlineTransactions() {
        if (null != receiptHeader && isNotBlank(receiptHeader.getConsumerCode())
                && isNotBlank(receiptHeader.getService().getCode())) {
            penidngTransaction = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE,
                    receiptHeader.getService().getCode(),
                    receiptHeader.getConsumerCode(), CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
            if (!penidngTransaction.isEmpty()) {
                isTransactionPending = Boolean.TRUE;
                addActionMessage(getText("onlineReceipts.pending.validate",
                        new String[] { penidngTransaction.get(0).getConsumerCode(),
                                penidngTransaction.get(0).getId().toString() }));
            }
        }

    }

    private String decodeBillXML() {
        String decodedBillXml = "";
        try {
            decodedBillXml = java.net.URLDecoder.decode(getCollectXML(), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            LOGGER.error("decodeBillXML", e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return decodedBillXml;
    }

    /**
     * Construct list of Payment Gateway to appear in the UI based on the configuration defined in AppConfig. Default is ALL,
     * which means all payment gateway will appear for the billing service. Otherwise, only the payment gateway codes defined as
     * comma separated values will appear for the Billing Service.
     */
    @SuppressWarnings("unchecked")
    private void constructServiceDetailsList() {
        final List<AppConfigValues> appConfigValuesList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.PGI_BILLINGSERVICE_CONFIGKEY);
        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            final String value = appConfigVal.getValue();
            final String attr = value.substring(0, value.indexOf('|'));
            if (attr.equalsIgnoreCase(collDetails.getServiceCode())) {
                final String attrVal = value.substring(value.indexOf('|') + 1);
                final List<String> serviceCodes = new ArrayList<>(0);
                for (final String code : attrVal.split(","))
                    serviceCodes.add(code);
                serviceDetailsList = getPersistenceService().findAllByNamedQuery(
                        CollectionConstants.QUERY_ACTIVE_SERVICES_BY_CODES, CollectionConstants.SERVICE_TYPE_PAYMENT,
                        serviceCodes);
            }
        }
        if (serviceDetailsList.isEmpty())
            serviceDetailsList = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_SERVICES_BY_TYPE, CollectionConstants.SERVICE_TYPE_PAYMENT);
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(final String[] remarks) {
        this.remarks = remarks;
    }

    public String[] getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final String[] transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String[] getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String[] transactionId) {
        this.transactionId = transactionId;
    }

    public Long[] getSelectedReceipts() {
        return selectedReceipts;
    }

    public void setSelectedReceipts(final Long[] selectedReceipts) {
        this.selectedReceipts = selectedReceipts;
    }

    public String[] getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final String[] statusCode) {
        this.statusCode = statusCode;
    }

    public String getReportId() {
        return reportId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(final String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(final Long receiptId) {
        this.receiptId = receiptId;
    }

    public ReceiptHeader[] getReceipts() {
        return receipts;
    }

    public void setReceipts(final ReceiptHeader[] receipts) {
        this.receipts = receipts;
    }

    /**
     * This getter will be invoked by framework from UI. It returns the total number of bill accounts that are present in the XML
     * arriving from the billing system
     *
     * @return
     */
    public Integer getTotalNoOfAccounts() {
        return receiptHeader.getReceiptDetails().size();
    }

    /**
     * This getter will be invoked by framework from UI. It returns the total amount of bill accounts that are present in the XML
     * arriving from the billing system
     *
     * @return
     */
    public BigDecimal getTotalAmountToBeCollected() {
        return totalAmountToBeCollected;
    }

    public void setTotalAmountToBeCollected(final BigDecimal totalAmountToBeCollected) {
        this.totalAmountToBeCollected = totalAmountToBeCollected;
    }

    /**
     * This getter will be invoked by the framework from UI. It returns the amount payed by the citizen.
     *
     * @return the paymentAmount
     */
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * @param paymentAmount the paymentAmount to set
     */
    public void setPaymentAmount(final BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * @return the paymentRequest
     */
    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    /**
     * @param paymentRequest the paymentRequest to set
     */
    public void setPaymentRequest(final PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public PaymentResponse getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(final PaymentResponse paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    /**
     * @return the paymentServiceId
     */
    public Integer getPaymentServiceId() {
        return paymentServiceId;
    }

    /**
     * @param paymentServiceId the paymentServiceId to set
     */
    public void setPaymentServiceId(final Integer paymentServiceId) {
        this.paymentServiceId = paymentServiceId;
    }

    public String getMsg() {
        if (responseMsg != null && responseMsg == "") {
            final HttpServletRequest httpRequest = ServletActionContext.getRequest();
            final Enumeration<String> paramNames = httpRequest.getParameterNames();
            final Map<String, String> responseMap = new HashMap<>(0);
            while (paramNames.hasMoreElements()) {
                final String paramName = paramNames.nextElement();
                final String paramValue = httpRequest.getParameter(paramName);
                if (isNotBlank(paramValue))
                    responseMap.put(paramName, paramValue);
            }
            responseMsg = responseMap.toString();
        }
        LOGGER.debug("responseMsg::::::" + responseMsg);
        return responseMsg;
    }

    public void setMsg(final String successMsg) {
        responseMsg = successMsg;
    }

    public ReceiptHeader getOnlinePaymentReceiptHeader() {
        return onlinePaymentReceiptHeader;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the collectionModesNotAllowed
     */
    public List<String> getCollectionModesNotAllowed() {
        return collectionModesNotAllowed;
    }

    /**
     * @param collectionModesNotAllowed the collectionModesNotAllowed to set
     */
    public void setCollectionModesNotAllowed(final List<String> collectionModesNotAllowed) {
        this.collectionModesNotAllowed = collectionModesNotAllowed;
    }

    /**
     * @return the overrideAccountHeads
     */
    public Boolean getOverrideAccountHeads() {
        return overrideAccountHeads;
    }

    /**
     * @param overrideAccountHeads the overrideAccountHeads to set
     */
    public void setOverrideAccountHeads(final Boolean overrideAccountHeads) {
        this.overrideAccountHeads = overrideAccountHeads;
    }

    /**
     * @return the partPaymentAllowed
     */
    public Boolean getPartPaymentAllowed() {
        return partPaymentAllowed;
    }

    /**
     * @param partPaymentAllowed the partPaymentAllowed to set
     */
    public void setPartPaymentAllowed(final Boolean partPaymentAllowed) {
        this.partPaymentAllowed = partPaymentAllowed;
    }

    /**
     * @return the collectXML
     */
    public String getCollectXML() {
        return collectXML;
    }

    /**
     * @param collectXML the collectXML to set
     */
    public void setCollectXML(final String collectXML) {
        this.collectXML = collectXML;
    }

    /**
     * @return the receiptDetailList
     */
    public List<ReceiptDetail> getReceiptDetailList() {
        return receiptDetailList;
    }

    /**
     * @param receiptDetailList the receiptDetailList to set
     */
    public void setReceiptDetailList(final List<ReceiptDetail> receiptDetailList) {
        this.receiptDetailList = receiptDetailList;
    }

    /**
     * @return the onlineInstrumenttotal
     */
    public BigDecimal getOnlineInstrumenttotal() {
        return onlineInstrumenttotal;
    }

    /**
     * @param onlineInstrumenttotal the onlineInstrumenttotal to set
     */
    public void setOnlineInstrumenttotal(final BigDecimal onlineInstrumenttotal) {
        this.onlineInstrumenttotal = onlineInstrumenttotal;
    }

    private void apportionBillAmount() {
        if (callbackForApportioning != null && overrideAccountHeads != null && callbackForApportioning
                && !overrideAccountHeads) {
            receiptDetailList = collectionCommon.apportionBillAmount(paymentAmount,
                    (ArrayList<ReceiptDetail>) getReceiptDetailList());
        }
    }

    /**
     * @return the callbackForApportioning
     */
    public Boolean getCallbackForApportioning() {
        return callbackForApportioning;
    }

    /**
     * @param callbackForApportioning the callbackForApportioning to set
     */
    public void setCallbackForApportioning(final Boolean callbackForApportioning) {
        this.callbackForApportioning = callbackForApportioning;
    }

    /**
     * @return the receiptNumber
     */
    public String getReceiptNumber() {
        return receiptNumber;
    }

    /**
     * @param receiptNumber the receiptNumber to set
     */
    public void setReceiptNumber(final String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    /**
     * @return the consumerCode
     */
    public String getConsumerCode() {
        return consumerCode;
    }

    /**
     * @param consumerCode the consumerCode to set
     */
    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    /**
     * @return the receiptResponse
     */
    public String getReceiptResponse() {
        return receiptResponse;
    }

    /**
     * @param receiptResponse the receiptResponse to set
     */
    public void setReceiptResponse(final String receiptResponse) {
        this.receiptResponse = receiptResponse;
    }

    public List<ServiceDetails> getServiceDetailsList() {
        return serviceDetailsList;
    }

    public void setServiceDetailsList(final List<ServiceDetails> serviceDetailsList) {
        this.serviceDetailsList = serviceDetailsList;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(final String refNumber) {
        this.refNumber = refNumber;
    }

    public void setCollectionService(final CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    public Boolean getIsTransactionPending() {
        return isTransactionPending;
    }

    public void setIsTransactionPending(Boolean isTransactionPending) {
        this.isTransactionPending = isTransactionPending;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public String getDisplayMsg() {
        return displayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public List<ReceiptHeader> getPenidngTransaction() {
        return penidngTransaction;
    }

    public void setPenidngTransaction(List<ReceiptHeader> penidngTransaction) {
        this.penidngTransaction = penidngTransaction;

    }

    public Long getRepayTransactionId() {
        return repayTransactionId;
    }

    public void setRepayTransactionId(Long repayTransactionId) {
        this.repayTransactionId = repayTransactionId;
    }
}