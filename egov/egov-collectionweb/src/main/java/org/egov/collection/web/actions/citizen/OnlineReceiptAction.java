/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.web.actions.citizen;

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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.handler.BillCollectXmlHandler;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = OnlineReceiptAction.NEW, location = "onlineReceipt-new.jsp"),
    @Result(name = OnlineReceiptAction.REDIRECT, location = "onlineReceipt-redirect.jsp"),
    @Result(name = OnlineReceiptAction.RESULT, location = "onlineReceipt-result.jsp"),
    @Result(name = OnlineReceiptAction.RECONRESULT, location = "onlineReceipt-reconresult.jsp"),
    @Result(name = CollectionConstants.REPORT, location = "onlineReceipt-report.jsp") })
public class OnlineReceiptAction extends BaseFormAction implements ServletRequestAware {

    private static final Logger LOGGER = Logger.getLogger(OnlineReceiptAction.class);
    public static final String REDIRECT = "redirect";
    private static final long serialVersionUID = 1L;

    private CollectionsUtil collectionsUtil;
    private ReceiptHeaderService receiptHeaderService;
    private CollectionCommon collectionCommon;
    private final List<ValidationError> errors = new ArrayList<ValidationError>(0);

    private BigDecimal onlineInstrumenttotal = BigDecimal.ZERO;
    private List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>(0);
    private BillInfoImpl collDetails = new BillInfoImpl();
    private BillCollectXmlHandler xmlHandler;
    private String collectXML;
    private String serviceName;
    private List<String> collectionModesNotAllowed = new ArrayList<String>(0);
    private Boolean overrideAccountHeads;
    private Boolean partPaymentAllowed;
    private BigDecimal totalAmountToBeCollected;
    private BigDecimal paymentAmount;
    private ReceiptHeader onlinePaymentReceiptHeader;
    private Long receiptId;
    private ReceiptHeader[] receipts;
    private Integer paymentServiceId = -1;
    private Integer reportId = -1;
    private String serviceCode;
    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;
    private String responseMsg = "";
    private HttpSession session = null;
    private HttpServletRequest request;
    protected static final String RESULT = "result";
    protected static final String RECONRESULT = "reconresult";
    private Boolean callbackForApportioning;
    private String receiptNumber;
    private String consumerCode;
    private String receiptResponse = "";
    private ReceiptHeader receiptHeader;
    private String refNumber;
    private List<ServiceDetails> serviceDetailsList = new ArrayList<ServiceDetails>(0);
    @Autowired
    private EgwStatusHibernateDAO statusDAO;
    @Autowired
    private FundHibernateDAO fundDAO;
    private List<OnlinePayment> lastThreeOnlinePayments = new ArrayList<OnlinePayment>(0);
    private Boolean onlinePayPending = Boolean.FALSE;
    private final String brokenTransactionErrorMessage = "If the amount has been deducted from "
            + "your account, then no further action is required from you right now. Such transactions are normally "
            + "resolved within 24 hours so you can check and download the receipt then." + "\n \n"
            + "If the amount has not been deducted from your account, then please check your "
            + "internet connection and try to pay again after some time. If the transaction fails again, "
            + "please contact cell in Corporation.";

    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/citizen/onlineReceipt-newform")
    public String newform() {
        return NEW;
    }

    public String testOnlinePaytMsg() {
        return "PaytGatewayTest";
    }

    @Action(value = "/citizen/onlineReceipt-saveNew")
    public String saveNew() {
        /**
         * initialise receipt info,persist receipt, create bill desk payment object and redirect to payment screen
         */
        if (callbackForApportioning && !overrideAccountHeads)
            apportionBillAmount();
        populateAndPersistReceipts();
        return REDIRECT;
    }

    /**
     * @return
     */
    @ValidationErrorPage(value = "result")
    @Action(value = "/citizen/onlineReceipt-acceptMessageFromPaymentGateway")
    public String acceptMessageFromPaymentGateway() {

        System.currentTimeMillis();

        if (getTestReceiptId() != null) {
            responseMsg = "MerchantID|CustomerID|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"
                    + "BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"
                    + "10-05-2010 15:39:09|" + getTestAuthStatusCode() + "|SettlementType|" + getTestReceiptId()
                    + "|AdditionalInfo2|AdditionalInfo3|AdditionalInfo4|"
                    + "AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription";
            /*
             * String checksum = PGIUtil.doDigest(responseMsg, CollectionConstants.UNIQUE_CHECKSUM_KEY); responseMsg += "|" +
             * checksum;
             */
            serviceCode = "BDPGI";
        }

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
            throw new ValidationException(Arrays.asList(new ValidationError(egovEx.getMessage(), egovEx.getMessage())));
        }

        onlinePaymentReceiptHeader = receiptHeaderService.findByNamedQuery(
                CollectionConstants.QUERY_RECEIPT_BY_ID_AND_CONSUMERCODE, Long.valueOf(paymentResponse.getReceiptId()),
                paymentResponse.getAdditionalInfo6());

        if (onlinePaymentReceiptHeader != null) {
            if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
                processSuccessMsg();
            else if (paymentService.getCode().equals(CollectionConstants.SERVICECODE_PGI_BILLDESK)
                    && CollectionConstants.PGI_AUTHORISATION_CODE_WAITINGFOR_PAY_GATEWAY_RESPONSE.equals(paymentResponse
                            .getAuthStatus())) {
                final EgwStatus paymentStatus = statusDAO.getStatusByModuleAndCode(
                        CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                        CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
                onlinePaymentReceiptHeader.getOnlinePayment().setStatus(paymentStatus);
                onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(
                        paymentResponse.getAuthStatus());
                onlinePaymentReceiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
            } else
                processFailureMsg();
        } else {
            errors.add(new ValidationError(brokenTransactionErrorMessage, brokenTransactionErrorMessage));
            LOGGER.info("onlinePaymentReceiptHeader object is null");
        }
        return RESULT;
    }

    // TO BE REMOVED ONCE THE TEST URL IS UP
    private Long testReceiptId;
    // TO BE REMOVED ONCE THE TEST URL IS UP
    private String testAuthStatusCode;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    // TO BE REMOVED ONCE THE TEST URL IS UP
    public Long getTestReceiptId() {
        return testReceiptId;
    }

    // TO BE REMOVED ONCE THE TEST URL IS UP
    public void setTestReceiptId(final Long testReceiptId) {
        this.testReceiptId = testReceiptId;
    }

    // TO BE REMOVED ONCE THE TEST URL IS UP
    public String getTestAuthStatusCode() {
        return testAuthStatusCode;
    }

    // TO BE REMOVED ONCE THE TEST URL IS UP
    public void setTestAuthStatusCode(final String testAuthStatusCode) {
        this.testAuthStatusCode = testAuthStatusCode;
    }

    /**
     * This method processes the failure message arriving from the payment gateway. The receipt and the online transaction are
     * both cancelled. The authorisation status for reason of failure is also persisted. The reason for payment failure is
     * displayed back to the user
     */
    private void processFailureMsg() {

        final EgwStatus receiptStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_FAILED);
        onlinePaymentReceiptHeader.setStatus(receiptStatus);

        final EgwStatus paymentStatus = statusDAO.getStatusByModuleAndCode(
                CollectionConstants.MODULE_NAME_ONLINEPAYMENT, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
        onlinePaymentReceiptHeader.getOnlinePayment().setStatus(paymentStatus);

        onlinePaymentReceiptHeader.getOnlinePayment().setAuthorisationStatusCode(paymentResponse.getAuthStatus());

        receiptHeaderService.persist(onlinePaymentReceiptHeader);

        LOGGER.debug("Cancelled receipt after receiving failure message from the payment gateway");

        addActionError(getText(onlinePaymentReceiptHeader.getOnlinePayment().getService().getCode().toLowerCase()
                + ".pgi." + onlinePaymentReceiptHeader.getService().getCode().toLowerCase() + "."
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
                    paymentResponse.getAuthStatus(), null, null);
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
                final List<ReceiptDetail> existingReceiptDetails = new ArrayList<ReceiptDetail>(0);

                for (final ReceiptDetail receiptDetail : receipts[i].getReceiptDetails())
                    if (!FinancialsUtil.isRevenueAccountHead(receiptDetail.getAccounthead(),
                            chartOfAccountsHibernateDAO.getBankChartofAccountCodeList())) {
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
                        existingReceiptDetails.add(newReceiptDetail);
                    }
                final List<ReceiptDetail> reconstructedList = collectionsUtil.reconstructReceiptDetail(receipts[i],
                        existingReceiptDetails);

                ReceiptDetail debitAccountDetail = null;
                if (reconstructedList != null)
                    debitAccountDetail = collectionCommon.addDebitAccountHeadDetails(
                            receipts[i].getTotalAmount(), receipts[i], BigDecimal.ZERO,
                            receipts[i].getTotalAmount(), CollectionConstants.INSTRUMENTTYPE_ONLINE);

                receiptHeaderService.reconcileOnlineSuccessPayment(receipts[i], transDate, getTransactionId()[i],
                        receipts[i].getTotalAmount(), null,
                        reconstructedList, debitAccountDetail);

                LOGGER.debug("Manually reconciled a success online payment");
            }

            if (CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED.equals(getStatusCode()[i])
                    || CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED.equals(getStatusCode()[i])) {
                final EgwStatus receiptStatus = collectionsUtil
                        .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_FAILED);
                receipts[i].setStatus(receiptStatus);

                receipts[i].getOnlinePayment().setTransactionNumber(getTransactionId()[i]);
                receipts[i].getOnlinePayment().setTransactionAmount(receipts[i].getTotalAmount());
                receipts[i].getOnlinePayment().setTransactionDate(transDate);
                receipts[i].getOnlinePayment().setRemarks(getRemarks()[i]);

                // set online payment status as TO BE REFUNDED/REFUNDED
                if (getStatusCode()[i].equals(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED))
                    receipts[i].getOnlinePayment().setStatus(
                            collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED));
                else
                    receipts[i].getOnlinePayment().setStatus(
                            collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED));

                receiptHeaderService.persist(receipts[i]);

                LOGGER.debug("Manually reconciled an online payment to " + getStatusCode()[i] + " state.");
            }
        }
        return RECONRESULT;
    }

    @Action(value = "/citizen/onlineReceipt-view")
    public String view() {
        setReceipts(new ReceiptHeader[1]);
        receipts[0] = receiptHeaderService.findById(getReceiptId(), false);

        try {
            setReportId(collectionCommon.generateReport(receipts, getSession(), true));
        } catch (final Exception e) {
            LOGGER.error(CollectionConstants.REPORT_GENERATION_ERROR, e);
            throw new ApplicationRuntimeException(CollectionConstants.REPORT_GENERATION_ERROR, e);
        }
        return CollectionConstants.REPORT;
    }

    @Action(value = "/citizen/onlineReceipt-viewReceipt")
    public String viewReceipt() {
        LOGGER.debug("::VIEWRECEIPT API:::  ServiceCode: " + getServiceCode() + ", Receipt Number="
                + getReceiptNumber() + ", Consumer Code=" + getConsumerCode());
        final ReceiptHeader receiptHeader = (ReceiptHeader) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_RECEIPT_BY_SERVICE_RECEIPTNUMBER_CONSUMERCODE, getServiceCode(),
                getReceiptNumber(), getReceiptNumber(), getConsumerCode());

        if (receiptHeader != null) {
            setReceiptId(receiptHeader.getId());
            return view();
        } else
            throw new ValidationException(Arrays.asList(new ValidationError("No Receipt Data Found",
                    "No Receipt Data Found")));

    }

    @Override
    public void prepare() {
        super.prepare();

        // set user id of citizen to thread locals for base model
        session = request.getSession();

        final User user = collectionsUtil.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);
        EgovThreadLocals.setUserId(user.getId());
        session.setAttribute(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME, user.getUsername());

        // populates model when request is from the billing system
        if (StringUtils.isNotBlank(getCollectXML())) {
            final String decodedCollectXml = decodeBillXML();
            try {
                collDetails = (BillInfoImpl) xmlHandler.toObject(decodedCollectXml);
                // modelPayeeList.clear();

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
                totalAmountToBeCollected = BigDecimal.valueOf(0);

                receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(collDetails, fund, dept);
                setRefNumber(receiptHeader.getReferencenumber());
                totalAmountToBeCollected = totalAmountToBeCollected.add(receiptHeader.getTotalAmountToBeCollected());
                for (final ReceiptDetail rDetails : receiptHeader.getReceiptDetails())
                    rDetails.getCramountToBePaid().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
                            BigDecimal.ROUND_UP);
                setReceiptDetailList(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));

                if (totalAmountToBeCollected.compareTo(BigDecimal.ZERO) == -1) {
                    addActionError(getText("billreceipt.totalamountlessthanzero.error"));
                    LOGGER.info(getText("billreceipt.totalamountlessthanzero.error"));
                } else
                    setTotalAmountToBeCollected(totalAmountToBeCollected.setScale(
                            CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));
            } catch (final Exception e) {
                LOGGER.error(getText("billreceipt.error.improperbilldata") + e.getMessage());
                addActionError(getText("billreceipt.error.improperbilldata"));
            }
        }
        addDropdownData(
                "paymentServiceList",
                getPersistenceService().findAllByNamedQuery(CollectionConstants.QUERY_SERVICES_BY_TYPE,
                        CollectionConstants.SERVICE_TYPE_PAYMENT));
        constructServiceDetailsList();
        // Fetching Last three online transaction for the Consumer Code
        if (null != consumerCode && !"".equals(consumerCode))
            lastThreeOnlinePayments = collectionsUtil.getOnlineTransactionHistory(consumerCode);
        for (final OnlinePayment onlinePay : lastThreeOnlinePayments)
            if (onlinePay.getStatus().getCode().equals(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING))
                onlinePayPending = Boolean.TRUE;
    }

    private String decodeBillXML() {
        String decodedBillXml = "";
        try {
            decodedBillXml = java.net.URLDecoder.decode(getCollectXML(), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return decodedBillXml;
    }

    /**
     * Construct list of Payment Gateway to appear in the UI based on the configuration defined in AppConfig. Default is ALL,
     * which means all payment gateway will appear for the billing service. Otherwise, only the payment gateway codes defined as
     * comma separated values will appear for the Billing Service.
     */
    private void constructServiceDetailsList() {
        final List<AppConfigValues> appConfigValuesList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.PGI_BILLINGSERVICE_CONFIGKEY);
        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            final String value = appConfigVal.getValue();
            final String attr = value.substring(0, value.indexOf('|'));
            if (attr.equalsIgnoreCase(collDetails.getServiceCode())) {
                final String attrVal = value.substring(value.indexOf('|') + 1);
                final List<String> serviceCodes = new ArrayList<String>(0);
                for (final String code : attrVal.split(","))
                    serviceCodes.add(code);
                serviceDetailsList = getPersistenceService().findAllByNamedQuery(
                        CollectionConstants.QUERY_ACTIVE_SERVICES_BY_CODES, CollectionConstants.SERVICE_TYPE_PAYMENT,
                        serviceCodes);
            }
        }
        if (serviceDetailsList.size() == 0)
            serviceDetailsList = getPersistenceService().findAllByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_SERVICES_BY_TYPE, CollectionConstants.SERVICE_TYPE_PAYMENT);
    }

    private void populateAndPersistReceipts() {
        final ServiceDetails paymentService = (ServiceDetails) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_ID, paymentServiceId.longValue());

        // for (ReceiptPayeeDetails payee : modelPayeeList) {
        // for (ReceiptHeader receiptHeader : payee.getReceiptHeaders()) {

        // only newly created receipts need to be initialised with the
        // data.
        // The cancelled receipt can be excluded from this processing.
        if (receiptHeader.getStatus() == null) {
            receiptHeader.setReceiptdate(new Date());
            receiptHeader.setReferencenumber(getRefNumber());
            receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
            receiptHeader.setIsModifiable(Boolean.FALSE);
            // recon flag should be set as false when the receipt is
            // actually
            // created on successful online transaction
            receiptHeader.setIsReconciled(Boolean.TRUE);
            receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_ONLINECOLLECTION);
            receiptHeader.setStatus(statusDAO.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                    CollectionConstants.RECEIPT_STATUS_CODE_PENDING));
            receiptHeader.setSource(Source.SYSTEM.toString());

            setOnlineInstrumenttotal(getOnlineInstrumenttotal().add(getPaymentAmount()));

            BigDecimal debitAmount = BigDecimal.ZERO;

            for (final ReceiptDetail creditChangeReceiptDetail : getReceiptDetailList()) {
                // calculate sum of creditamounts as a debit value to
                // create a
                // debit account head and add to receipt details
                debitAmount = debitAmount.add(creditChangeReceiptDetail.getCramount());
                debitAmount = debitAmount.subtract(creditChangeReceiptDetail.getDramount());

                for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
                    if (creditChangeReceiptDetail.getReceiptHeader().getReferencenumber()
                            .equals(receiptDetail.getReceiptHeader().getReferencenumber())
                            && receiptDetail.getOrdernumber().equals(creditChangeReceiptDetail.getOrdernumber()))
                        receiptDetail.setCramount(creditChangeReceiptDetail.getCramount());
            }
            // end of outer for loop
            receiptHeader.setTotalAmount(onlineInstrumenttotal);

            receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader,
                    BigDecimal.ZERO, onlineInstrumenttotal, CollectionConstants.INSTRUMENTTYPE_ONLINE));

            // Add Online Payment Details
            final OnlinePayment onlinePayment = new OnlinePayment();

            onlinePayment.setStatus(statusDAO.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING));
            onlinePayment.setReceiptHeader(receiptHeader);
            onlinePayment.setService(paymentService);

            receiptHeader.setOnlinePayment(onlinePayment);
        }
        receiptHeaderService.persistReceiptsObject(receiptHeader);

        /**
         * Construct Request Object For The Payment Gateway
         */

        setPaymentRequest(collectionCommon.createPaymentRequest(paymentService, receiptHeader));

    }// end of method

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    private String[] transactionId;
    private Long[] selectedReceipts;
    private String[] transactionDate;
    private String[] statusCode;
    private String[] remarks;

    public String[] getRemarks() {
        return remarks;
    }

    public void setRemarks(final String[] remarks) {
        this.remarks = remarks;
    }

    public void setTransactionId(final String[] transactionId) {
        this.transactionId = transactionId;
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

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
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
        final Integer totalNoOfAccounts = receiptHeader.getReceiptDetails().size();
        return totalNoOfAccounts;
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
            final HttpServletRequest request = ServletActionContext.getRequest();
            final Enumeration paramNames = request.getParameterNames();
            final Map<String, String> responseMap = new HashMap<String, String>(0);
            while (paramNames.hasMoreElements()) {
                final String paramName = (String) paramNames.nextElement();
                final String paramValue = request.getParameter(paramName);
                if (null != paramValue && !"".equals(paramValue))
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
     * @param xmlHandler the xmlHandler to set
     */
    public void setXmlHandler(final BillCollectXmlHandler xmlHandler) {
        this.xmlHandler = xmlHandler;
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
        receiptDetailList = collectionCommon.apportionBillAmount(paymentAmount,
                (ArrayList<ReceiptDetail>) getReceiptDetailList());
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

    @Override
    public void setServletRequest(final HttpServletRequest arg0) {
        request = arg0;
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

    public List<OnlinePayment> getLastThreeOnlinePayments() {
        return lastThreeOnlinePayments;
    }

    public void setLastThreeOnlinePayments(final List<OnlinePayment> lastThreeOnlinePayments) {
        this.lastThreeOnlinePayments = lastThreeOnlinePayments;
    }

    public Boolean getOnlinePayPending() {
        return onlinePayPending;
    }

    public void setOnlinePayPending(final Boolean onlinePayPending) {
        this.onlinePayPending = onlinePayPending;
    }
}