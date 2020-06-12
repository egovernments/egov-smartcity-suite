/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.wtms.application.service.collection;

import org.apache.commons.lang.StringUtils;
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
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.RestPropertyTaxDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.masters.entity.PayWaterTaxDetails;
import org.egov.wtms.masters.entity.WaterReceiptDetails;
import org.egov.wtms.masters.entity.WaterTaxDetails;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.infra.utils.DateUtils.getFormattedDate;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.BILLTYPE_AUTO;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PAYMENT_TYPE_ADVANCE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_CHARGES_SERVICE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROPERTY_MODULE_NAME;

public class WaterTaxExternalService {

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private CollectionIntegrationService collectionService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private ConnectionBillService connectionBillService;

    @Autowired
    private WaterConnectionBillable waterConnectionBillable;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private BankHibernateDAO bankHibernateDAO;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    public WaterReceiptDetails payWaterTax(PayWaterTaxDetails payWaterTaxDetails) {

        WaterReceiptDetails waterReceiptDetails = null;
        String currentInstallmentYear = EMPTY;
        BigDecimal totalAmountToBePaid = ZERO;
        WaterConnectionDetails waterConnectionDetails = new WaterConnectionDetails();

        String consumerNo = payWaterTaxDetails.getConsumerNo();
        if (isNotBlank(consumerNo) || isNotBlank(payWaterTaxDetails.getApplicationNumber()))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    isNotBlank(consumerNo) ? consumerNo : payWaterTaxDetails.getApplicationNumber(), ACTIVE);

        if (INPROGRESS.equals(waterConnectionDetails.getConnectionStatus()))
            currentInstallmentYear = toYearFormat(connectionDemandService.getCurrentInstallment(
                    MODULE_NAME, WaterTaxConstants.YEARLY, new Date()).getInstallmentYear());
        else if (ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = toYearFormat(connectionDemandService.getCurrentInstallment(
                    PROPERTY_MODULE_NAME, null, new Date()).getInstallmentYear());
        else if (ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = toYearFormat(connectionDemandService.getCurrentInstallment(
                    MODULE_NAME, WaterTaxConstants.MONTHLY, new Date()).getInstallmentYear());

        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);

        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(2L);
        ApplicationThreadLocals.setUserId(2L);

        BillReferenceNumberGenerator billRefeNumber = beanResolver.getAutoNumberServiceFor(BillReferenceNumberGenerator.class);
        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(connectionDemandService.getBillTypeByCode(BILLTYPE_AUTO));
        waterConnectionBillable.setTransanctionReferenceNumber(payWaterTaxDetails.getTransactionId());
        waterConnectionBillable.setServiceCode(WATERTAX_CHARGES_SERVICE_CODE);
        waterConnectionBillable.getCurrentDemand().setMinAmtPayable(ZERO);

        EgBill egBill = generateBill(waterConnectionBillable);
        for (EgBillDetails billDetails : egBill.getEgBillDetails())
            if (!billDetails.getDescription().contains(PropertyTaxConstants.DEMANDRSN_STR_ADVANCE)
                    && billDetails.getCrAmount().signum() > 0)
                totalAmountToBePaid = totalAmountToBePaid.add(billDetails.getCrAmount());

        BillReceiptInfo billReceiptInfo = getBillReceiptInforForwaterTax(payWaterTaxDetails, egBill);

        if (billReceiptInfo != null) {
            waterReceiptDetails = new WaterReceiptDetails();
            waterReceiptDetails.setReceiptNo(billReceiptInfo.getReceiptNum());
            waterReceiptDetails.setReceiptDate(getFormattedDate(billReceiptInfo.getReceiptDate(), "dd-MM-yyyy"));
            waterReceiptDetails.setPayeeName(billReceiptInfo.getPayeeName());
            waterReceiptDetails.setPayeeAddress(billReceiptInfo.getPayeeAddress());
            waterReceiptDetails.setBillReferenceNo(billReceiptInfo.getBillReferenceNum());
            waterReceiptDetails.setServiceName(billReceiptInfo.getServiceName());
            waterReceiptDetails.setDescription(billReceiptInfo.getDescription());
            waterReceiptDetails.setPaidBy(billReceiptInfo.getPaidBy());
            waterReceiptDetails.setPaymentMode(payWaterTaxDetails.getPaymentMode());
            waterReceiptDetails.setPaymentAmount(billReceiptInfo.getTotalAmount());
            waterReceiptDetails.setTransactionId(billReceiptInfo.getManualReceiptNumber());
            String[] paidFrom = null;
            String[] paidTo = null;
            Installment fromInstallment = new Installment();
            Installment toInstallment = new Installment();
            if (totalAmountToBePaid.signum() > 0) {
                List<ReceiptAccountInfo> receiptAccountsList = new ArrayList<>(billReceiptInfo.getAccountDetails());

                Collections.sort(receiptAccountsList, (rcptAcctInfo1, rcptAcctInfo2) -> {
                    if (rcptAcctInfo1.getOrderNumber() != null && rcptAcctInfo2.getOrderNumber() != null)
                        return rcptAcctInfo1.getOrderNumber().compareTo(rcptAcctInfo2.getOrderNumber());
                    return 0;
                });
                for (ReceiptAccountInfo rcptAcctInfo : receiptAccountsList)
                    if (rcptAcctInfo.getCrAmount().signum() > 0
                            && !rcptAcctInfo.getDescription().contains(WaterTaxConstants.DEMANDRSN_REASON_ADVANCE)) {
                        if (paidFrom == null) {
                            paidFrom = rcptAcctInfo.getDescription().split("-", 2);
                            paidFrom = paidFrom[1].split("#", 2);
                        }
                        paidTo = rcptAcctInfo.getDescription().split("-", 2);
                        paidTo = paidTo[1].split("#", 2);
                    }

                if (paidFrom != null)
                    fromInstallment = installmentDao.getInsatllmentByModuleAndDescription(
                            moduleService.getModuleByName(PROPERTY_MODULE_NAME), paidFrom[0].trim());
                if (paidTo != null)
                    toInstallment = installmentDao.getInsatllmentByModuleAndDescription(
                            moduleService.getModuleByName(PROPERTY_MODULE_NAME), paidTo[0].trim());
            }

            if (totalAmountToBePaid.signum() == 0) {
                waterReceiptDetails.setPaymentPeriod(EMPTY);
                waterReceiptDetails.setPaymentType(PAYMENT_TYPE_ADVANCE);
            } else
                waterReceiptDetails.setPaymentPeriod(getDefaultFormattedDate(fromInstallment.getFromDate())
                        .concat(" to ").concat(getDefaultFormattedDate(toInstallment.getToDate())));

            if (payWaterTaxDetails.getPaymentAmount().compareTo(totalAmountToBePaid) > 0)
                waterReceiptDetails.setPaymentType(PAYMENT_TYPE_ADVANCE);
            else if (totalAmountToBePaid.compareTo(payWaterTaxDetails.getPaymentAmount()) > 0)
                waterReceiptDetails.setPaymentType(WaterTaxConstants.PAYMENT_TYPE_PARTIALLY);
            else
                waterReceiptDetails.setPaymentType(WaterTaxConstants.PAYMENT_TYPE_FULLY);
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(WaterTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(WaterTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
            waterReceiptDetails.setErrorDetails(errorDetails);
        }
        return waterReceiptDetails;
    }

    public WaterTaxDetails getWaterTaxDemandDet(PayWaterTaxDetails payWaterTaxDetails) {

        WaterConnectionDetails waterConnectionDetails = null;
        if (isNotBlank(payWaterTaxDetails.getApplicationNumber()))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(payWaterTaxDetails.getApplicationNumber());
        else if (isNotBlank(payWaterTaxDetails.getConsumerNo()))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    payWaterTaxDetails.getConsumerNo(), ACTIVE);

        WaterTaxDetails waterTaxDetails = new WaterTaxDetails();
        ErrorDetails errorDetails = new ErrorDetails();
        if (waterConnectionDetails == null) {
            errorDetails.setErrorCode(WaterTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(WaterTaxConstants.WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX + (payWaterTaxDetails.getConsumerNo() != null
                    ? payWaterTaxDetails.getConsumerNo()
                    : payWaterTaxDetails.getApplicationNumber()) + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
            waterTaxDetails.setErrorDetails(errorDetails);
        } else {
            waterTaxDetails.setConsumerNo(waterConnectionDetails.getConnection().getConsumerCode());
            waterTaxDetails = getWaterTaxDetails(waterTaxDetails, waterConnectionDetails);
        }
        return waterTaxDetails;
    }

    public WaterTaxDetails getWaterTaxDemandDetByConsumerCode(String consumerCode) {

        WaterTaxDetails waterTaxDetails = new WaterTaxDetails();
        WaterConnectionDetails waterConnectionDetails = null;
        ErrorDetails errorDetails = new ErrorDetails();

        if (isNotBlank(consumerCode))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(consumerCode, ACTIVE);
        if (waterConnectionDetails == null) {
            errorDetails.setErrorCode(WaterTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(WaterTaxConstants.WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX
                    + consumerCode + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
            waterTaxDetails.setErrorDetails(errorDetails);
        } else {
            waterTaxDetails.setConsumerNo(consumerCode);
            waterTaxDetails = getWaterTaxDetails(waterTaxDetails, waterConnectionDetails);
        }
        return waterTaxDetails;
    }
    public List<WaterTaxDetails> getWaterTaxDemandDetListByConsumerCodes(List<String> consumerCodes) {

        WaterTaxDetails waterTaxDetails = new WaterTaxDetails();
        List<WaterTaxDetails> waterTaxDetailsList = new  ArrayList<>();
        List<WaterConnectionDetails> waterConnectionDetailsList = new  ArrayList<>();
        ErrorDetails errorDetails = new ErrorDetails();
        if (!(consumerCodes.isEmpty()))
           waterConnectionDetailsList =  waterConnectionDetailsService.findByApplicationNumbersOrConsumerCodesAndStatus(consumerCodes, ACTIVE);
        
        if (waterConnectionDetailsList.isEmpty()) {
            errorDetails.setErrorCode(WaterTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(WaterTaxConstants.WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX
                    + StringUtils.join(consumerCodes, ",") + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
            waterTaxDetails.setErrorDetails(errorDetails);
            waterTaxDetailsList.add(waterTaxDetails);
        } else {
                waterTaxDetailsList = getWaterTaxDetails(waterTaxDetailsList, waterConnectionDetailsList);
        }
        return waterTaxDetailsList;
    }
    @Transactional
    public BillReceiptInfo executeCollection(Payment payment, EgBill bill, String source) {

        if (!isCollectionPermitted(bill))
            throw new ApplicationRuntimeException("Collection is not allowed - current balance is zero and advance coll exists.");

        List<PaymentInfo> paymentInfoList = preparePaymentInfo(payment);

        BillInfoImpl billInfo = prepareBillInfo(payment.getAmount(), COLLECTIONTYPE.F, bill, source);
        return collectionService.createReceipt(billInfo, paymentInfoList);
    }

    private List<PaymentInfo> preparePaymentInfo(Payment payment) {

        List<PaymentInfo> paymentInfoList = new ArrayList<>();
        PaymentInfo paymentInfo = null;
        if (payment != null)
            if (payment instanceof ChequePayment) {
                ChequePayment chequePayment = (ChequePayment) payment;
                paymentInfo = new PaymentInfoChequeDD(chequePayment.getBankId(), chequePayment.getBranchName(),
                        chequePayment.getInstrumentDate(), chequePayment.getInstrumentNumber(), TYPE.cheque,
                        payment.getAmount());

            } else if (payment instanceof DDPayment) {
                DDPayment chequePayment = (DDPayment) payment;
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

    /**
     * Apportions the paid amount amongst the appropriate GL codes and returns
     * the collections object that can be sent to the collections API for
     * processing.
     *
     * @param bill
     * @param amountPaid
     * @return
     */
    public BillInfoImpl prepareBillInfo(BigDecimal amountPaid, COLLECTIONTYPE collType, EgBill bill, String source) {

        ArrayList<ReceiptDetail> receiptDetails = new ArrayList<>(0);
        List<EgBillDetails> billDetails = new ArrayList<>(bill.getEgBillDetails());
        Collections.sort(billDetails);
        for (EgBillDetails billDet : billDetails)
            receiptDetails.add(initReceiptDetail(billDet.getGlcode(), ZERO, // billDet.getCrAmount(),
                    billDet.getCrAmount(), billDet.getDrAmount(), billDet.getDescription()));
        boolean isActualDemand ;
        new WaterTaxCollection(waterTaxUtils).apportionPaidAmount(String.valueOf(bill.getId()), amountPaid, receiptDetails);

        BillInfoImpl billInfoImpl = initialiseFromBill(amountPaid, collType, bill);
        for (EgBillDetails billDet : bill.getEgBillDetails())
            for (ReceiptDetail rd : receiptDetails)
                // FIX ME
                if (billDet.getGlcode().equals(rd.getAccounthead().getGlcode())
                        && billDet.getDescription().equals(rd.getDescription())) {
                    isActualDemand = billDet.getAdditionalFlag() == 1;
                    BillAccountDetails billAccDetails;
                    billAccDetails = new BillAccountDetails(billDet.getGlcode(), billDet.getOrderNo(), rd.getCramount(),
                            rd.getDramount(), billDet.getFunctionCode(), billDet.getDescription(), isActualDemand, getPurpose(billDet));
                    billInfoImpl.getPayees().get(0).getBillDetails().get(0).addBillAccountDetails(billAccDetails);
                    break;
                }
        billInfoImpl.setTransactionReferenceNumber(bill.getTransanctionReferenceNumber());
        billInfoImpl.setSource(source);
        return billInfoImpl;
    }

    private BillInfoImpl initialiseFromBill(BigDecimal amountPaid, COLLECTIONTYPE collType, EgBill bill) {

        List<BillPayeeDetails> billPayeeDetList = new ArrayList<>(0);
        List<String> collModesList = new ArrayList<>();
        String[] collModes = bill.getCollModesNotAllowed().split(",");
        for (String coll : collModes)
            collModesList.add(coll);
        BillInfoImpl billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                bill.getFundSourceCode(), bill.getDepartmentCode(), "Water Charge Collection", bill.getCitizenName(),
                bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collModesList, collType);
        BillPayeeDetails billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());

        BillDetails billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(),
                bill.getConsumerId(), bill.getConsumerType(), bill.getBoundaryNum().toString(), bill.getBoundaryType(), bill.getDescription(),
                amountPaid, // the actual amount paid, which might include
                // advances
                bill.getMinAmtPayable());
        billPayeeDet.addBillDetails(billDetails);
        billPayeeDetList.add(billPayeeDet);
        billInfoImpl.setPayees(billPayeeDetList);
        return billInfoImpl;
    }

    public PURPOSE getPurpose(EgBillDetails billDet) {
        CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        if (billDet.getDescription().contains(WaterTaxConstants.DEMANDRSN_REASON_ADVANCE))
            return PURPOSE.ADVANCE_AMOUNT;
        else if (billDet.getEgDemandReason().getEgInstallmentMaster().getToDate().compareTo(finYear.getStartingDate()) < 0)
            return PURPOSE.ARREAR_AMOUNT;
        else if (billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate().compareTo(finYear.getStartingDate()) >= 0
                && billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate().compareTo(finYear.getEndingDate()) < 0)
            return PURPOSE.CURRENT_AMOUNT;
        else
            return PURPOSE.OTHERS;
    }

    private ReceiptDetail initReceiptDetail(String glCode, BigDecimal crAmount, BigDecimal crAmountToBePaid,
                                            BigDecimal drAmount, String description) {
        ReceiptDetail receiptDetail = new ReceiptDetail();
        CChartOfAccounts accountHead = new CChartOfAccounts();
        accountHead.setGlcode(glCode);
        receiptDetail.setAccounthead(accountHead);
        receiptDetail.setDescription(description);
        receiptDetail.setCramount(crAmount);
        receiptDetail.setCramountToBePaid(crAmountToBePaid);
        receiptDetail.setDramount(drAmount);
        return receiptDetail;
    }

    private PaymentInfoCard prepareCardPaymentInfo(CreditCardPayment cardPayment,
                                                   PaymentInfoCard paymentInfoCard) {
        paymentInfoCard.setInstrumentNumber(cardPayment.getCreditCardNo());
        paymentInfoCard.setInstrumentAmount(cardPayment.getAmount());
        paymentInfoCard.setExpMonth(cardPayment.getExpMonth());
        paymentInfoCard.setExpYear(cardPayment.getExpYear());
        paymentInfoCard.setCvvNumber(cardPayment.getCvv());
        paymentInfoCard.setCardTypeValue(cardPayment.getCardType());
        paymentInfoCard.setTransactionNumber(cardPayment.getTransactionNumber());
        return paymentInfoCard;
    }

    public boolean isCollectionPermitted(EgBill bill) {
        return thereIsCurrentBalanceToBePaid(bill);
    }

    private boolean thereIsCurrentBalanceToBePaid(EgBill bill) {

        List<AppConfigValues> demandreasonGlcode = waterTaxUtils.getAppConfigValueByModuleNameAndKeyName(MODULE_NAME,
                WaterTaxConstants.DEMANDREASONANDGLCODEMAP);
        Map<String, String> demandReasonGlCodePairmap = new HashMap<>();
        for (AppConfigValues appConfig : demandreasonGlcode) {
            String[] rows = appConfig.getValue().split("=");
            demandReasonGlCodePairmap.put(rows[0], rows[1]);
        }
        boolean result = false;
        BigDecimal currentBal = ZERO;
        for (Map.Entry<String, String> entry : demandReasonGlCodePairmap.entrySet())
            currentBal = currentBal.add(bill.balanceForGLCode(entry.getValue()));
        if (currentBal.signum() > 0)
            result = true;
        return result;
    }

    public BillReceiptInfo getBillReceiptInforForwaterTax(PayWaterTaxDetails payWaterTaxDetails,
                                                          EgBill egBill) {

        Map<String, String> paymentDetailsMap = new HashMap<>(0);
        paymentDetailsMap.put(PropertyTaxConstants.TOTAL_AMOUNT, payWaterTaxDetails.getPaymentAmount().toString());
        paymentDetailsMap.put(PropertyTaxConstants.PAID_BY, payWaterTaxDetails.getPaidBy());
        if (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().toLowerCase())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().toLowerCase())) {
            paymentDetailsMap.put(ChequePayment.INSTRUMENTNUMBER, payWaterTaxDetails.getChqddNo());
            paymentDetailsMap.put(ChequePayment.INSTRUMENTDATE, payWaterTaxDetails.getChqddDate());
            paymentDetailsMap.put(ChequePayment.BRANCHNAME, payWaterTaxDetails.getBranchName());
            paymentDetailsMap.put(ChequePayment.BANKID, validateBank(payWaterTaxDetails.getBankName()).toString());
            paymentDetailsMap.put(ChequePayment.BANKNAME, payWaterTaxDetails.getBankName());
        }
        Payment payment = Payment.create(payWaterTaxDetails.getPaymentMode().toLowerCase(), paymentDetailsMap);

        return executeCollection(payment, egBill, payWaterTaxDetails.getSource());
    }

    private Long validateBank(String bankCodeOrName) {

        Bank bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        if (bank == null)
            // Tries by name if code not found
            bank = bankHibernateDAO.getBankByName(bankCodeOrName);
        return Long.valueOf(bank.getId());

    }

    public BillReceiptInfo validateTransanctionIdPresent(String transantion) {
        return collectionService.getReceiptInfo(WaterTaxConstants.COLLECTION_STRING_SERVICE_CODE, transantion);
    }

    public EgBill generateBill(Billable billObj) {
        EgBill bill = generateBillForConnection(billObj, financialYearDAO);
        egBillDAO.create(bill);
        return bill;
    }

    public WaterTaxDetails getWaterTaxDetails(WaterTaxDetails waterTaxDetails,
                                              WaterConnectionDetails waterConnectionDetails) {

        String propertyIdentifier = waterConnectionDetails.getConnection().getPropertyIdentifier();
        BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(propertyIdentifier);
        waterTaxDetails.setPropertyAddress(basicProperty.getAddress().toString());
        waterTaxDetails.setLocalityName(basicProperty.getPropertyID().getLocality().getName());
        waterTaxDetails.setConsumerNo(waterConnectionDetails.getConnection().getConsumerCode());

        List<PropertyOwnerInfo> propOwnerInfos = basicProperty.getPropertyOwnerInfo();
        if (!propOwnerInfos.isEmpty()) {
            waterTaxDetails.setOwnerName(propOwnerInfos.get(0).getOwner().getName());
            waterTaxDetails.setMobileNo(propOwnerInfos.get(0).getOwner().getMobileNumber());
        }
        waterTaxDetails.setTaxDetails(new ArrayList<>(0));
        BigDecimal waterTaxDueAmount = waterConnectionDetailsService.getWaterTaxDueAmount(waterConnectionDetails);

        RestPropertyTaxDetails arrearDetails = new RestPropertyTaxDetails();
        arrearDetails.setTotalAmount(waterTaxDueAmount.signum() >= 0 ? waterTaxDueAmount : ZERO);
        arrearDetails.setTaxAmount(waterTaxDueAmount.signum() >= 0 ? waterTaxDueAmount : ZERO);
        arrearDetails.setInstallment(financialYearDAO.getFinancialYearByDate(new Date()).getFinYearRange());
        waterTaxDetails.getTaxDetails().add(arrearDetails);
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(WaterTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(WaterTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);

        waterTaxDetails.setErrorDetails(errorDetails);
        return waterTaxDetails;
    }


    public List<WaterTaxDetails> getWaterTaxDetails(List<WaterTaxDetails> waterTaxDetailsList,
            List<WaterConnectionDetails> waterConnectionDetailsList) {
        WaterTaxDetails waterTaxDetails = new WaterTaxDetails();
        WaterConnection waterConnection =null;
        BasicProperty basicProperty =null;
        for (WaterConnectionDetails waterConnectionDetails : waterConnectionDetailsList) {
            waterConnection= waterConnectionDetails.getConnection();
            waterTaxDetails.setConsumerNo(StringUtils.isNotBlank(waterConnectionDetails.getApplicationNumber())
                    ? waterConnectionDetails.getApplicationNumber() : waterConnection.getConsumerCode());
            basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(waterConnection.getPropertyIdentifier());
            waterTaxDetails.setPropertyAddress(basicProperty.getAddress().toString());
            waterTaxDetails.setLocalityName(basicProperty.getPropertyID().getLocality().getName());
            waterTaxDetails.setConsumerNo(waterConnection.getConsumerCode());

            List<PropertyOwnerInfo> propOwnerInfos = basicProperty.getPropertyOwnerInfo();
            if (!propOwnerInfos.isEmpty()) {
                waterTaxDetails.setOwnerName(propOwnerInfos.get(0).getOwner().getName());
                waterTaxDetails.setMobileNo(propOwnerInfos.get(0).getOwner().getMobileNumber());
            }
            waterTaxDetails.setTaxDetails(new ArrayList<>(0));
            BigDecimal waterTaxDueAmount = waterConnectionDetailsService.getWaterTaxDueAmount(waterConnectionDetails);
            RestPropertyTaxDetails arrearDetails = new RestPropertyTaxDetails();
            arrearDetails.setTotalAmount(waterTaxDueAmount.signum() >= 0 ? waterTaxDueAmount : ZERO);
            arrearDetails.setTaxAmount(waterTaxDueAmount.signum() >= 0 ? waterTaxDueAmount : ZERO);
            arrearDetails.setInstallment(financialYearDAO.getFinancialYearByDate(new Date()).getFinYearRange());
            waterTaxDetails.getTaxDetails().add(arrearDetails);
            
            ErrorDetails errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(WaterTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(WaterTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
            waterTaxDetails.setErrorDetails(errorDetails);
            waterTaxDetailsList.add(waterTaxDetails);
        }
        return waterTaxDetailsList;
    }

    public EgBill generateBillForConnection(Billable billObj, FinancialYearDAO financialYearDAO) {
        EgBill bill = new EgBill();
        bill.setBillNo(billObj.getReferenceNumber());
        bill.setBoundaryNum(billObj.getBoundaryNum().intValue());
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
        EgDemand currentDemand = billObj.getCurrentDemand();
        bill.setEgDemand(currentDemand);
        bill.setDescription(billObj.getDescription());
        bill.setDisplayMessage(billObj.getDisplayMessage());
        bill.setEmailId(billObj.getEmailId());

        if (currentDemand != null && currentDemand.getMinAmtPayable() != null)
            bill.setMinAmtPayable(currentDemand.getMinAmtPayable());
        else
            bill.setMinAmtPayable(ZERO);

        // Get it from the concrete implementation
        List<EgBillDetails> bd = connectionBillService.getBilldetails(billObj);
        for (EgBillDetails billdetails : bd) {
            bill.addEgBillDetails(billdetails);
            billdetails.setEgBill(bill);
            billdetails.setPurpose(getPurpose(billdetails).toString());
        }

        bill.setConsumerId(billObj.getConsumerId());
        bill.setConsumerType(billObj.getConsumerType());
        bill.setCallBackForApportion(Boolean.TRUE);
        return bill;
    }
}