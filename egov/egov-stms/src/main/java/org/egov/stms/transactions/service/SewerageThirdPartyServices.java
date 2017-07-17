/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

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
package org.egov.stms.transactions.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillDetails;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillPayeeDetails;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.PaymentInfo;
import org.egov.collection.integration.models.PaymentInfo.TYPE;
import org.egov.collection.integration.models.PaymentInfoCard;
import org.egov.collection.integration.models.PaymentInfoCash;
import org.egov.collection.integration.models.PaymentInfoChequeDD;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.Bank;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentDao;
import org.egov.dcb.bean.CashPayment;
import org.egov.dcb.bean.ChequePayment;
import org.egov.dcb.bean.CreditCardPayment;
import org.egov.dcb.bean.DDPayment;
import org.egov.dcb.bean.Payment;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.stms.entity.PaySewerageTaxDetails;
import org.egov.stms.entity.SewerageReceiptDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.collection.SewerageBillServiceImpl;
import org.egov.stms.transactions.service.collection.SewerageBillable;
import org.egov.stms.transactions.service.collection.SewerageTaxCollection;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class SewerageThirdPartyServices {
    @Autowired
    private SimpleRestClient simpleRestClient;
    @Autowired
    private CollectionIntegrationService collectionService;
    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private BankHibernateDAO bankHibernateDAO;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private SewerageBillServiceImpl sewerageBillServiceImpl;
    private String currentDemand = "currentDemand";
    private static final String WTMS_TAXDUE_RESTURL = "%s/wtms/rest/watertax/due/byptno/%s";

    public AssessmentDetails getPropertyDetails(final String assessmentNumber, final HttpServletRequest request) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + request.getServerName() + ":" + request.getServerPort()
                + "/ptis/rest/property/{assessmentNumber}";
        return restTemplate.getForObject(url, AssessmentDetails.class,
                assessmentNumber);

    }

    public HashMap<String, Object> getWaterTaxDueAndCurrentTax(final String assessmentNo,
            final HttpServletRequest request) {

        final HashMap<String, Object> result = new HashMap<>();
        result.put("WATERTAXDUE", BigDecimal.ZERO);
        result.put("CURRENTWATERCHARGE", BigDecimal.ZERO);
        final String wtmsRestURL = String.format(WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(request, false),
                assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        result.put(
                "WATERTAXDUE",
                waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(Double
                        .valueOf((Double) waterTaxInfo.get("totalTaxDue"))));
        result.put("CURRENTWATERCHARGE", waterTaxInfo.get(currentDemand) == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get(currentDemand))));
        result.put("PROPERTYID", waterTaxInfo.get("propertyID"));
        result.put("CONSUMERCODE", waterTaxInfo.get("consumerCode"));
        return result;
    }

    public BigDecimal getCurrentWaterTax(final String assessmentNo, final HttpServletRequest request) {
        final String wtmsRestURL = String.format(WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(request, false),
                assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get(currentDemand) == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get(currentDemand)));
    }

    public BillReceiptInfo validateTransanctionIdPresent(final String transantion) {
        return collectionService.getReceiptInfo(SewerageTaxConstants.STRING_SERVICE_CODE, transantion);
    }

    public SewerageReceiptDetails paySewerageTax(PaySewerageTaxDetails paySewerageTaxDetails, final HttpServletRequest request) {

        SewerageReceiptDetails sewerageReceiptDetails = null;

        BigDecimal totalAmountToBePaid = BigDecimal.ZERO;
        SewerageApplicationDetails sewerageApplicationDetails = null;
        if (paySewerageTaxDetails.getApplicaionNumber() != null && !"".equals(paySewerageTaxDetails.getApplicaionNumber()))
            sewerageApplicationDetails = sewerageApplicationDetailsService
                    .findByApplicationNumber(paySewerageTaxDetails.getApplicaionNumber());
        else if (paySewerageTaxDetails.getConsumerNo() != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService
                    .findByConnection_ShscNumberAndIsActive(paySewerageTaxDetails.getConsumerNo());

        final SewerageBillable sewerageBillable = buildSewerageBillableObject(paySewerageTaxDetails, request,
                sewerageApplicationDetails);

        final EgBill egBill = generateBillForConnection(sewerageBillable);
        for (final EgBillDetails billDetails : egBill.getEgBillDetails()) {
            totalAmountToBePaid = totalAmountToBePaid.add(billDetails.getCrAmount());

        }

        final BillReceiptInfo billReceiptInfo = getBillReceiptInforForSewerageTax(paySewerageTaxDetails, egBill);

        if (null != billReceiptInfo) {
            sewerageReceiptDetails = buildSewerageReceiptDetailObject(paySewerageTaxDetails, totalAmountToBePaid,
                    billReceiptInfo);
        }
        return sewerageReceiptDetails;

    }

    private SewerageReceiptDetails buildSewerageReceiptDetailObject(PaySewerageTaxDetails paySewerageTaxDetails,
            BigDecimal totalAmountToBePaid, final BillReceiptInfo billReceiptInfo) {
        SewerageReceiptDetails sewerageReceiptDetails;
        ErrorDetails errorDetails;
        sewerageReceiptDetails = new SewerageReceiptDetails();
        sewerageReceiptDetails.setReceiptNo(billReceiptInfo.getReceiptNum());
        sewerageReceiptDetails.setReceiptDate(formatDate(billReceiptInfo.getReceiptDate()));
        sewerageReceiptDetails.setPayeeName(billReceiptInfo.getPayeeName());
        sewerageReceiptDetails.setPayeeAddress(billReceiptInfo.getPayeeAddress());
        sewerageReceiptDetails.setBillReferenceNo(billReceiptInfo.getBillReferenceNum());
        sewerageReceiptDetails.setServiceName(billReceiptInfo.getServiceName());
        sewerageReceiptDetails.setDescription(billReceiptInfo.getDescription());
        sewerageReceiptDetails.setPaidBy(billReceiptInfo.getPaidBy());
        sewerageReceiptDetails.setPaymentMode(paySewerageTaxDetails.getPaymentMode());
        sewerageReceiptDetails.setPaymentAmount(billReceiptInfo.getTotalAmount());
        sewerageReceiptDetails.setTransactionId(billReceiptInfo.getManualReceiptNumber());
        String[] paidFrom = null;
        String[] paidTo = null;
        Installment fromInstallment = null;
        Installment toInstallment = null;
        if (totalAmountToBePaid.compareTo(BigDecimal.ZERO) > 0) {
            final List<ReceiptAccountInfo> receiptAccountsList = new ArrayList<>(
                    billReceiptInfo.getAccountDetails());
            Collections.sort(receiptAccountsList, (rcptAcctInfo1, rcptAcctInfo2) -> {
                if (rcptAcctInfo1.getOrderNumber() != null && rcptAcctInfo2.getOrderNumber() != null)
                    return rcptAcctInfo1.getOrderNumber().compareTo(rcptAcctInfo2.getOrderNumber());
                return 0; 
            });
            for (final ReceiptAccountInfo rcptAcctInfo : receiptAccountsList) {
                if (rcptAcctInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0
                        && !rcptAcctInfo.getDescription().contains(SewerageTaxConstants.FEES_ADVANCE_CODE)) {
                    if (paidFrom == null) {
                        paidFrom = rcptAcctInfo.getDescription().split("Sewerage Tax Collection ", 2);
                        paidFrom = paidFrom[1].split("#", 2);
                    }
                    paidTo = rcptAcctInfo.getDescription().split("Sewerage Tax Collection ", 2);
                    paidTo = paidTo[1].split("#", 2);
                }
            }
            if (paidFrom != null)
                fromInstallment = installmentDao.getInsatllmentByModuleAndDescription(
                        moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME),
                        paidFrom[0].trim());
            if (paidTo != null)
                toInstallment = installmentDao.getInsatllmentByModuleAndDescription(
                        moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME),
                        paidTo[0].trim());
        }
        if (totalAmountToBePaid.compareTo(BigDecimal.ZERO) == 0) {
            sewerageReceiptDetails.setPaymentPeriod(StringUtils.EMPTY);
            sewerageReceiptDetails.setPaymentType(SewerageTaxConstants.FEES_ADVANCE_CODE);
        } else {
            sewerageReceiptDetails.setPaymentPeriod(
                    ((fromInstallment != null) ? DateUtils.getDefaultFormattedDate(fromInstallment.getFromDate()) : "")
                            .concat(" to ").concat((toInstallment != null)
                                    ? DateUtils.getDefaultFormattedDate(toInstallment.getToDate()) : ""));
        }
        if (paySewerageTaxDetails.getPaymentAmount().compareTo(totalAmountToBePaid) > 0)
            sewerageReceiptDetails.setPaymentType(SewerageTaxConstants.FEES_ADVANCE_CODE);
        else if (totalAmountToBePaid.compareTo(paySewerageTaxDetails.getPaymentAmount()) > 0)
            sewerageReceiptDetails.setPaymentType(SewerageTaxConstants.PAYMENT_TYPE_PARTIALLY);
        else
            sewerageReceiptDetails.setPaymentType(SewerageTaxConstants.PAYMENT_TYPE_FULLY);
        errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(SewerageTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(SewerageTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
        sewerageReceiptDetails.setErrorDetails(errorDetails);
        return sewerageReceiptDetails;
    }

    private SewerageBillable buildSewerageBillableObject(PaySewerageTaxDetails paySewerageTaxDetails,
            final HttpServletRequest request, SewerageApplicationDetails sewerageApplicationDetails) {
        final SewerageBillable sewerageBillable = (SewerageBillable) context
                .getBean("sewerageBillable");
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        sewerageBillable.setSewerageApplicationDetails(sewerageApplicationDetails);
        sewerageBillable.setAssessmentDetails(assessmentDetails);
        sewerageBillable.setUserId(2L);// FIX: Hardcoded to get ananymous user
        ApplicationThreadLocals.setUserId(2L);

        final Serializable referenceNumber = sequenceNumberGenerator.getNextSequence(SewerageTaxConstants.SEWERAGE_BILLNUMBER);

        sewerageBillable.setReferenceNumber(String.format("%s%06d", "", referenceNumber));

        sewerageBillable.setBillType(egBillDAO.getBillTypeByCode(SewerageTaxConstants.BILLTYPE_MANUAL));
        sewerageBillable.setTransanctionReferenceNumber(paySewerageTaxDetails.getTransactionId());
        return sewerageBillable;
    }

    public EgBill generateBillForConnection(final Billable billObj) {
        final EgBill bill = new EgBill();
        bill.setBillNo(billObj.getReferenceNumber());
        bill.setBoundaryNum(billObj.getBoundaryNum()!=null?billObj.getBoundaryNum().intValue():null);
        bill.setTransanctionReferenceNumber(billObj.getTransanctionReferenceNumber());
        bill.setBoundaryType(billObj.getBoundaryType());
        bill.setCitizenAddress(billObj.getBillAddress());
        bill.setCitizenName(billObj.getBillPayee());
        bill.setCollModesNotAllowed(billObj.getCollModesNotAllowed());
        bill.setDepartmentCode(billObj.getDepartmentCode());
        bill.setEgBillType(billObj.getBillType());
        bill.setFunctionaryCode(billObj.getFunctionaryCode());
        bill.setFundCode(billObj.getFundCode());
        bill.setFundSourceCode(billObj.getFundSourceCode());
        bill.setIssueDate(new Date());
        bill.setLastDate(billObj.getBillLastDueDate());
        bill.setModule(billObj.getModule());
        bill.setOverrideAccountHeadsAllowed(billObj.getOverrideAccountHeadsAllowed());
        bill.setPartPaymentAllowed(Boolean.TRUE);
        bill.setServiceCode(billObj.getServiceCode());
        bill.setIs_Cancelled("N");
        bill.setIs_History("N");
        bill.setModifiedDate(new Date());
        bill.setTotalAmount(billObj.getTotalAmount());
        bill.setUserId(billObj.getUserId());
        bill.setCreateDate(new Date());
        final EgDemand demandObject = billObj.getCurrentDemand();
        bill.setEgDemand(demandObject);
        bill.setDescription(billObj.getDescription());
        bill.setDisplayMessage(billObj.getDisplayMessage());
        bill.setEmailId(billObj.getEmailId());

        if (demandObject != null && demandObject.getMinAmtPayable() != null)
            bill.setMinAmtPayable(demandObject.getMinAmtPayable());
        else
            bill.setMinAmtPayable(BigDecimal.ZERO);

        // Get it from the concrete implementation
        final List<EgBillDetails> bd = sewerageBillServiceImpl.getSewerageTaxTypeBilldetails(billObj);
        for (final EgBillDetails billdetails : bd) {
            bill.addEgBillDetails(billdetails);
            billdetails.setEgBill(bill);
            billdetails.setPurpose(getPurpose(billdetails).toString());
        }

        bill.setConsumerId(billObj.getConsumerId());
        bill.setConsumerType(billObj.getConsumerType());
        bill.setCallBackForApportion(Boolean.TRUE);
        egBillDAO.create(bill);
        return bill;
    }

    private String formatDate(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public BillReceiptInfo getBillReceiptInforForSewerageTax(final PaySewerageTaxDetails paySewerageTaxDetails,
            final EgBill egBill) {

        final Map<String, String> paymentDetailsMap = new HashMap<>(0);
        paymentDetailsMap.put(PropertyTaxConstants.TOTAL_AMOUNT, paySewerageTaxDetails.getPaymentAmount().toString());
        paymentDetailsMap.put(PropertyTaxConstants.PAID_BY, paySewerageTaxDetails.getPaidBy());
        if (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
                .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().toLowerCase())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
                        .equalsIgnoreCase(paySewerageTaxDetails.getPaymentMode().toLowerCase())) {
            paymentDetailsMap.put(ChequePayment.INSTRUMENTNUMBER, paySewerageTaxDetails.getChqddNo());
            paymentDetailsMap.put(ChequePayment.INSTRUMENTDATE, paySewerageTaxDetails.getChqddDate());
            paymentDetailsMap.put(ChequePayment.BRANCHNAME, paySewerageTaxDetails.getBranchName());
            final Long validatesBankId = getBankId(paySewerageTaxDetails.getBankName());
            paymentDetailsMap.put(ChequePayment.BANKID, validatesBankId.toString());
            paymentDetailsMap.put(ChequePayment.BANKNAME, paySewerageTaxDetails.getBankName());
        }
        final Payment payment = Payment.create(paySewerageTaxDetails.getPaymentMode().toLowerCase(), paymentDetailsMap);

        return executeCollection(payment, egBill, paySewerageTaxDetails.getSource());

    }

    private Long getBankId(final String bankCodeOrName) {

        Bank bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        return bank != null ? Long.valueOf(bank.getId()) : Long.valueOf(0);

    }

    @Transactional
    public BillReceiptInfo executeCollection(final Payment payment, final EgBill bill, final String source) {

        if (!isCollectionPermitted(bill))
            throw new ApplicationRuntimeException(
                    "Collection is not allowed - current balance is zero .");
        if (payment != null) {
            final List<PaymentInfo> paymentInfoList = preparePaymentInfo(payment);

            final BillInfoImpl billInfo = prepareBillInfo(payment.getAmount(), COLLECTIONTYPE.F, bill, source);
            return collectionService.createReceipt(billInfo, paymentInfoList);
        }
        return null;
    }

    private List<PaymentInfo> preparePaymentInfo(final Payment payment) {
        final List<PaymentInfo> paymentInfoList = new ArrayList<>();
        PaymentInfo paymentInfo = null;
        if (payment != null)
            if (payment instanceof ChequePayment) {
                final ChequePayment chequePayment = (ChequePayment) payment;
                paymentInfo = new PaymentInfoChequeDD(chequePayment.getBankId(), chequePayment.getBranchName(),
                        chequePayment.getInstrumentDate(), chequePayment.getInstrumentNumber(), TYPE.cheque,
                        payment.getAmount());

            } else if (payment instanceof DDPayment) {
                final DDPayment chequePayment = (DDPayment) payment;
                paymentInfo = new PaymentInfoChequeDD(chequePayment.getBankId(), chequePayment.getBranchName(),
                        chequePayment.getInstrumentDate(), chequePayment.getInstrumentNumber(), TYPE.dd,
                        payment.getAmount());

            } else if (payment instanceof CreditCardPayment)
                paymentInfo = prepareCardPaymentInfo((CreditCardPayment) payment, new PaymentInfoCard());
            else if (payment instanceof CashPayment)
                paymentInfo = new PaymentInfoCash(payment.getAmount());
        paymentInfoList.add(paymentInfo);
        return paymentInfoList;
    }

    private PaymentInfoCard prepareCardPaymentInfo(final CreditCardPayment cardPayment,
            final PaymentInfoCard paymentInfoCard) {
        paymentInfoCard.setInstrumentNumber(cardPayment.getCreditCardNo());
        paymentInfoCard.setInstrumentAmount(cardPayment.getAmount());
        paymentInfoCard.setExpMonth(cardPayment.getExpMonth());
        paymentInfoCard.setExpYear(cardPayment.getExpYear());
        paymentInfoCard.setCvvNumber(cardPayment.getCvv());
        paymentInfoCard.setCardTypeValue(cardPayment.getCardType());
        paymentInfoCard.setTransactionNumber(cardPayment.getTransactionNumber());
        return paymentInfoCard;
    }

    /**
     * Apportions the paid amount amongst the appropriate GL codes and returns the collections object that can be sent to the
     * collections API for processing.
     *
     * @param bill
     * @param amountPaid
     * @return
     */
    public BillInfoImpl prepareBillInfo(final BigDecimal amountPaid, final COLLECTIONTYPE collType, final EgBill bill,
            final String source) {
        final BillInfoImpl billInfoImpl = initialiseFromBill(amountPaid, collType, bill);

        final ArrayList<ReceiptDetail> receiptDetails = new ArrayList<>(0);
        final List<EgBillDetails> billDetails = new ArrayList<>(bill.getEgBillDetails());

        // Sort Based on installment start date.
        if (!billDetails.isEmpty())
            Collections.sort(billDetails, (c1, c2) -> c1.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                    .compareTo(c2.getEgDemandReason().getEgInstallmentMaster().getFromDate()));

        for (final EgBillDetails billDet : billDetails)
            receiptDetails.add(initReceiptDetail(billDet.getGlcode(), BigDecimal.ZERO, // billDet.getCrAmount(),
                    billDet.getCrAmount(), billDet.getDrAmount(), billDet.getDescription()));

        new SewerageTaxCollection().apportionPaidAmount(String.valueOf(bill.getId()), amountPaid,
                receiptDetails);

        for (final EgBillDetails billDet : bill.getEgBillDetails()) {
            for (final ReceiptDetail rd : receiptDetails) { // If amount greater than zero then only add to receipt creation.
                if (rd.getCramount().compareTo(BigDecimal.ZERO) > 0 && billDet.getGlcode().equals(rd.getAccounthead().getGlcode())
                        && billDet.getDescription().equals(rd.getDescription())) {

                    BillAccountDetails billAccDetails = new BillAccountDetails(billDet.getGlcode(), billDet.getOrderNo(),
                            rd.getCramount(),
                            rd.getDramount(), billDet.getFunctionCode(), billDet.getDescription(),
                            billDet.getAdditionalFlag() == 1 ? true : false,
                            getPurpose(billDet));
                    billInfoImpl.getPayees().get(0).getBillDetails().get(0).addBillAccountDetails(billAccDetails);
                    break;
                }
            }
        }
        billInfoImpl.setTransactionReferenceNumber(bill.getTransanctionReferenceNumber());
        billInfoImpl.setSource(source);
        return billInfoImpl;
    }

    public PURPOSE getPurpose(final EgBillDetails billDet) {
        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        if (billDet.getDescription().contains(SewerageTaxConstants.FEES_ADVANCE_CODE))
            return PURPOSE.ADVANCE_AMOUNT;
        else if (billDet.getEgDemandReason() != null && billDet.getEgDemandReason().getEgInstallmentMaster().getToDate()
                .compareTo(finYear.getStartingDate()) < 0)
            return PURPOSE.ARREAR_AMOUNT;
        else if (billDet.getEgDemandReason() != null && billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                .compareTo(finYear.getStartingDate()) >= 0
                && billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                        .compareTo(finYear.getEndingDate()) < 0)
            return PURPOSE.CURRENT_AMOUNT;
        else
            return PURPOSE.OTHERS;
    }

    private BillInfoImpl initialiseFromBill(final BigDecimal amountPaid, final COLLECTIONTYPE collType,
            final EgBill bill) {
        BillInfoImpl billInfoImpl;
        BillPayeeDetails billPayeeDet;
        final List<BillPayeeDetails> billPayeeDetList = new ArrayList<>(0);
        final List<String> collModesList = new ArrayList<>();
        final String[] collModes = bill.getCollModesNotAllowed().split(",");
        for (final String coll : collModes)
            collModesList.add(coll);
        billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                bill.getFundSourceCode(), bill.getDepartmentCode(), SewerageTaxConstants.DISPLAY_MESSAGE, bill.getCitizenName(),
                bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collModesList, collType);
        billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());

        final BillDetails billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(),
                bill.getConsumerId(), bill.getConsumerType(), bill.getBoundaryNum().toString(), bill.getBoundaryType(),
                bill.getDescription(),
                amountPaid, // the actual amount paid, which might include
                // advances
                bill.getMinAmtPayable());
        billPayeeDet.addBillDetails(billDetails);
        billPayeeDetList.add(billPayeeDet);
        billInfoImpl.setPayees(billPayeeDetList);
        return billInfoImpl;
    }

    private ReceiptDetail initReceiptDetail(final String glCode, final BigDecimal crAmount,
            final BigDecimal crAmountToBePaid, final BigDecimal drAmount, final String description) {
        final ReceiptDetail receiptDetail = new ReceiptDetail();
        final CChartOfAccounts accountHead = new CChartOfAccounts();
        accountHead.setGlcode(glCode);
        receiptDetail.setAccounthead(accountHead);
        receiptDetail.setDescription(description);
        receiptDetail.setCramount(crAmount);
        receiptDetail.setCramountToBePaid(crAmountToBePaid);
        receiptDetail.setDramount(drAmount);
        return receiptDetail;
    }

    private boolean isCollectionPermitted(final EgBill bill) {
        boolean result = false;
        for (final EgBillDetails bd : bill.getEgBillDetails()) {
            if (bd.getCrAmount().compareTo(BigDecimal.ZERO) > 0)
                result = true;
        }
        return result;
    }

}
