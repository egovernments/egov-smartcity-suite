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
package org.egov.wtms.application.service.collection;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.RestPropertyTaxDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.masters.entity.PayWaterTaxDetails;
import org.egov.wtms.masters.entity.WaterReceiptDetails;
import org.egov.wtms.masters.entity.WaterTaxDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class WaterTaxExternalService {

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ApplicationContext context;

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
    private EgBillDao egBillDAO;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private BankHibernateDAO bankHibernateDAO;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    public WaterReceiptDetails payWaterTax(final PayWaterTaxDetails payWaterTaxDetails) {
        WaterReceiptDetails waterReceiptDetails = null;
        ErrorDetails errorDetails = null;
        String currentInstallmentYear = null;
        final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        BigDecimal totalAmountToBePaid = BigDecimal.ZERO;
        final BillReferenceNumberGenerator billRefeNumber = beanResolver
                .getAutoNumberServiceFor(BillReferenceNumberGenerator.class);
        WaterConnectionDetails waterConnectionDetails = null;
        if (payWaterTaxDetails.getApplicaionNumber() != null && !"".equals(payWaterTaxDetails.getApplicaionNumber()))
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumber(payWaterTaxDetails.getApplicaionNumber());
        else if (payWaterTaxDetails.getConsumerNo() != null)
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.ACTIVE);
        final WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context
                .getBean("waterConnectionBillable");
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(2L);
        ApplicationThreadLocals.setUserId(2L);
        if (ConnectionStatus.INPROGRESS.equals(waterConnectionDetails.getConnectionStatus()))
            currentInstallmentYear = formatYear.format(connectionDemandService
                    .getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.YEARLY, new Date())
                    .getInstallmentYear());
        else if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = formatYear.format(connectionDemandService
                    .getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date())
                    .getInstallmentYear());
        else if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = formatYear.format(connectionDemandService
                    .getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.MONTHLY, new Date())
                    .getInstallmentYear());
        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(connectionDemandService.getBillTypeByCode(BILLTYPE_MANUAL));
        waterConnectionBillable.setTransanctionReferenceNumber(payWaterTaxDetails.getTransactionId());
        final EgBill egBill = generateBill(waterConnectionBillable);
        for (final EgBillDetails billDetails : egBill.getEgBillDetails())
            if (!billDetails.getDescription().contains(PropertyTaxConstants.DEMANDRSN_STR_ADVANCE)
                    && billDetails.getCrAmount().compareTo(BigDecimal.ZERO) > 0)
                totalAmountToBePaid = totalAmountToBePaid.add(billDetails.getCrAmount());

        final BillReceiptInfo billReceiptInfo = getBillReceiptInforForwaterTax(payWaterTaxDetails, egBill);

        if (null != billReceiptInfo) {
            waterReceiptDetails = new WaterReceiptDetails();
            waterReceiptDetails.setReceiptNo(billReceiptInfo.getReceiptNum());
            waterReceiptDetails.setReceiptDate(formatDate(billReceiptInfo.getReceiptDate()));
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
            Installment fromInstallment = null;
            Installment toInstallment = null;
            if (totalAmountToBePaid.compareTo(BigDecimal.ZERO) > 0) {
                final List<ReceiptAccountInfo> receiptAccountsList = new ArrayList<ReceiptAccountInfo>(
                        billReceiptInfo.getAccountDetails());
                Collections.sort(receiptAccountsList, (rcptAcctInfo1, rcptAcctInfo2) -> {
                    if (rcptAcctInfo1.getOrderNumber() != null && rcptAcctInfo2.getOrderNumber() != null)
                        return rcptAcctInfo1.getOrderNumber().compareTo(rcptAcctInfo2.getOrderNumber());
                    return 0;
                });
                for (final ReceiptAccountInfo rcptAcctInfo : receiptAccountsList)
                    if (rcptAcctInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0
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
                            moduleService.getModuleByName(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE),
                            paidFrom[0].trim());
                if (paidTo != null)
                    toInstallment = installmentDao.getInsatllmentByModuleAndDescription(
                            moduleService.getModuleByName(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE),
                            paidTo[0].trim());
            }
            if (totalAmountToBePaid.compareTo(BigDecimal.ZERO) == 0) {
                waterReceiptDetails.setPaymentPeriod(StringUtils.EMPTY);
                waterReceiptDetails.setPaymentType(WaterTaxConstants.PAYMENT_TYPE_ADVANCE);
            } else
                waterReceiptDetails.setPaymentPeriod(DateUtils.getDefaultFormattedDate(fromInstallment.getFromDate())
                        .concat(" to ").concat(DateUtils.getDefaultFormattedDate(toInstallment.getToDate())));

            if (payWaterTaxDetails.getPaymentAmount().compareTo(totalAmountToBePaid) > 0)
                waterReceiptDetails.setPaymentType(WaterTaxConstants.PAYMENT_TYPE_ADVANCE);
            else if (totalAmountToBePaid.compareTo(payWaterTaxDetails.getPaymentAmount()) > 0)
                waterReceiptDetails.setPaymentType(WaterTaxConstants.PAYMENT_TYPE_PARTIALLY);
            else
                waterReceiptDetails.setPaymentType(WaterTaxConstants.PAYMENT_TYPE_FULLY);
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(WaterTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
            errorDetails.setErrorMessage(WaterTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);
            waterReceiptDetails.setErrorDetails(errorDetails);
        }
        return waterReceiptDetails;
    }

    public WaterTaxDetails getWaterTaxDemandDet(final PayWaterTaxDetails payWaterTaxDetails) {
        WaterTaxDetails waterTaxDetails = new WaterTaxDetails();
        WaterConnectionDetails waterConnectionDetails = null;
        ErrorDetails errorDetails = null;
        if (payWaterTaxDetails.getApplicaionNumber() != null && !"".equals(payWaterTaxDetails.getApplicaionNumber()))
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumber(payWaterTaxDetails.getApplicaionNumber());
        else if (payWaterTaxDetails.getConsumerNo() != null)
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    payWaterTaxDetails.getConsumerNo(), ConnectionStatus.ACTIVE);
        if (waterConnectionDetails == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(WaterTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(WaterTaxConstants.WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX
                    + (payWaterTaxDetails.getConsumerNo() != null ? payWaterTaxDetails.getConsumerNo()
                            : payWaterTaxDetails.getApplicaionNumber())
                    + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
            waterTaxDetails.setErrorDetails(errorDetails);
        } else {
            waterTaxDetails.setConsumerNo(waterConnectionDetails.getConnection().getConsumerCode());
            waterTaxDetails = getWaterTaxDetails(waterTaxDetails, waterConnectionDetails);
        }
        return waterTaxDetails;
    }

    public WaterTaxDetails getWaterTaxDemandDetByConsumerCode(final String consumerCode) {
        WaterTaxDetails waterTaxDetails = new WaterTaxDetails();
        WaterConnectionDetails waterConnectionDetails = null;
        ErrorDetails errorDetails = null;
        if (consumerCode != null)
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCodeAndStatus(consumerCode, ConnectionStatus.ACTIVE);
        if (waterConnectionDetails == null) {
            errorDetails = new ErrorDetails();
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

    @Transactional
    public BillReceiptInfo executeCollection(final Payment payment, final EgBill bill, final String source) {

        if (!isCollectionPermitted(bill))
            throw new ApplicationRuntimeException(
                    "Collection is not allowed - current balance is zero and advance coll exists.");

        final List<PaymentInfo> paymentInfoList = preparePaymentInfo(payment);

        final BillInfoImpl billInfo = prepareBillInfo(payment.getAmount(), COLLECTIONTYPE.F, bill, source);
        return collectionService.createReceipt(billInfo, paymentInfoList);
    }

    private List<PaymentInfo> preparePaymentInfo(final Payment payment) {
        final List<PaymentInfo> paymentInfoList = new ArrayList<PaymentInfo>();
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

    /**
     * Apportions the paid amount amongst the appropriate GL codes and returns
     * the collections object that can be sent to the collections API for
     * processing.
     *
     * @param bill
     * @param amountPaid
     * @return
     */
    public BillInfoImpl prepareBillInfo(final BigDecimal amountPaid, final COLLECTIONTYPE collType, final EgBill bill,
            final String source) {
        final BillInfoImpl billInfoImpl = initialiseFromBill(amountPaid, collType, bill);

        final ArrayList<ReceiptDetail> receiptDetails = new ArrayList<ReceiptDetail>(0);
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>(bill.getEgBillDetails());
        Collections.sort(billDetails);
        for (final EgBillDetails billDet : billDetails)
            receiptDetails.add(initReceiptDetail(billDet.getGlcode(), BigDecimal.ZERO, // billDet.getCrAmount(),
                    billDet.getCrAmount(), billDet.getDrAmount(), billDet.getDescription()));
        Boolean isActualDemand = false;
        new WaterTaxCollection(waterTaxUtils).apportionPaidAmount(String.valueOf(bill.getId()), amountPaid,
                receiptDetails);

        for (final EgBillDetails billDet : bill.getEgBillDetails())
            for (final ReceiptDetail rd : receiptDetails)
                // FIX ME
                if (billDet.getGlcode().equals(rd.getAccounthead().getGlcode())
                        && billDet.getDescription().equals(rd.getDescription())) {
                    isActualDemand = billDet.getAdditionalFlag() == 1 ? true : false;
                    BillAccountDetails billAccDetails;
                    billAccDetails = new BillAccountDetails(billDet.getGlcode(), billDet.getOrderNo(), rd.getCramount(),
                            rd.getDramount(), billDet.getFunctionCode(), billDet.getDescription(), isActualDemand,
                            getPurpose(billDet));
                    billInfoImpl.getPayees().get(0).getBillDetails().get(0).addBillAccountDetails(billAccDetails);
                    break;
                }
        billInfoImpl.setTransactionReferenceNumber(bill.getTransanctionReferenceNumber());
        billInfoImpl.setSource(source);
        return billInfoImpl;
    }

    private BillInfoImpl initialiseFromBill(final BigDecimal amountPaid, final COLLECTIONTYPE collType,
            final EgBill bill) {
        BillInfoImpl billInfoImpl = null;
        BillPayeeDetails billPayeeDet = null;
        final List<BillPayeeDetails> billPayeeDetList = new ArrayList<BillPayeeDetails>(0);
        final List<String> collModesList = new ArrayList<String>();
        final String[] collModes = bill.getCollModesNotAllowed().split(",");
        for (final String coll : collModes)
            collModesList.add(coll);
        billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                bill.getFundSourceCode(), bill.getDepartmentCode(), "Water Charge Collection", bill.getCitizenName(),
                bill.getPartPaymentAllowed(), bill.getOverrideAccountHeadsAllowed(), collModesList, collType);
        billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());

        final BillDetails billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(),
                bill.getConsumerId(), bill.getConsumerType(),bill.getBoundaryNum().toString(), bill.getBoundaryType(), bill.getDescription(),
                amountPaid, // the actual amount paid, which might include
                // advances
                bill.getMinAmtPayable());
        billPayeeDet.addBillDetails(billDetails);
        billPayeeDetList.add(billPayeeDet);
        billInfoImpl.setPayees(billPayeeDetList);
        return billInfoImpl;
    }

    public PURPOSE getPurpose(final EgBillDetails billDet) {
        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        if (billDet.getDescription().contains(WaterTaxConstants.DEMANDRSN_REASON_ADVANCE))
            return PURPOSE.ADVANCE_AMOUNT;
        else if (billDet.getEgDemandReason().getEgInstallmentMaster().getToDate()
                .compareTo(finYear.getStartingDate()) < 0)
            return PURPOSE.ARREAR_AMOUNT;
        else if (billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                .compareTo(finYear.getStartingDate()) >= 0
                && billDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                        .compareTo(finYear.getEndingDate()) < 0)
            return PURPOSE.CURRENT_AMOUNT;
        else
            return PURPOSE.OTHERS;
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

    public boolean isCollectionPermitted(final EgBill bill) {
        final boolean allowed = thereIsCurrentBalanceToBePaid(bill);

        return allowed;
    }

    private boolean thereIsCurrentBalanceToBePaid(final EgBill bill) {
        boolean result = false;
        BigDecimal currentBal = BigDecimal.ZERO;
        final List<AppConfigValues> demandreasonGlcode = waterTaxUtils.getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.DEMANDREASONANDGLCODEMAP);
        final Map<String, String> demandReasonGlCodePairmap = new HashMap<String, String>();
        for (final AppConfigValues appConfig : demandreasonGlcode) {
            final String rows[] = appConfig.getValue().split("=");
            demandReasonGlCodePairmap.put(rows[0], rows[1]);

        }
        for (final Map.Entry<String, String> entry : demandReasonGlCodePairmap.entrySet())
            currentBal = currentBal.add(bill.balanceForGLCode(entry.getValue()));
        if (currentBal != null && currentBal.compareTo(BigDecimal.ZERO) > 0)
            result = true;
        return result;
    }

    public BillReceiptInfo getBillReceiptInforForwaterTax(final PayWaterTaxDetails payWaterTaxDetails,
            final EgBill egBill) {

        final Map<String, String> paymentDetailsMap = new HashMap<String, String>(0);
        paymentDetailsMap.put(PropertyTaxConstants.TOTAL_AMOUNT, payWaterTaxDetails.getPaymentAmount().toString());
        paymentDetailsMap.put(PropertyTaxConstants.PAID_BY, payWaterTaxDetails.getPaidBy());
        if (PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE
                .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().toLowerCase())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD
                        .equalsIgnoreCase(payWaterTaxDetails.getPaymentMode().toLowerCase())) {
            paymentDetailsMap.put(ChequePayment.INSTRUMENTNUMBER, payWaterTaxDetails.getChqddNo());
            paymentDetailsMap.put(ChequePayment.INSTRUMENTDATE, payWaterTaxDetails.getChqddDate());
            paymentDetailsMap.put(ChequePayment.BRANCHNAME, payWaterTaxDetails.getBranchName());
            final Long validatesBankId = validateBank(payWaterTaxDetails.getBankName());
            paymentDetailsMap.put(ChequePayment.BANKID, validatesBankId.toString());
            paymentDetailsMap.put(ChequePayment.BANKNAME, payWaterTaxDetails.getBankName());
        }
        final Payment payment = Payment.create(payWaterTaxDetails.getPaymentMode().toLowerCase(), paymentDetailsMap);

        final BillReceiptInfo billReceiptInfo = executeCollection(payment, egBill, payWaterTaxDetails.getSource());
        return billReceiptInfo;
    }

    private String formatDate(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    private Long validateBank(final String bankCodeOrName) {

        Bank bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        if (bank == null)
            // Tries by name if code not found
            bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        return new Long(bank.getId());

    }

    public BillReceiptInfo validateTransanctionIdPresent(final String transantion) {
        return collectionService.getReceiptInfo(WaterTaxConstants.COLLECTION_STRING_SERVICE_CODE, transantion);
    }

    public final EgBill generateBill(final Billable billObj) {
        final EgBill bill = generateBillForConnection(billObj, financialYearDAO);
        egBillDAO.create(bill);
        return bill;
    }

    public WaterTaxDetails getWaterTaxDetails(final WaterTaxDetails waterTaxDetails,
            final WaterConnectionDetails waterConnectionDetails) {
        ErrorDetails errorDetails = null;
        waterTaxDetails.setConsumerNo(waterConnectionDetails.getConnection().getConsumerCode());

        final String propertyIdentifier = waterConnectionDetails.getConnection().getPropertyIdentifier();
        final BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(propertyIdentifier);
        waterTaxDetails.setPropertyAddress(basicProperty.getAddress().toString());
        waterTaxDetails.setLocalityName(basicProperty.getPropertyID().getLocality().getName());

        final List<PropertyOwnerInfo> propOwnerInfos = basicProperty.getPropertyOwnerInfo();
        if (propOwnerInfos.size() > 0) {
            waterTaxDetails.setOwnerName(propOwnerInfos.get(0).getOwner().getName());
            waterTaxDetails.setMobileNo(propOwnerInfos.get(0).getOwner().getMobileNumber());
        }

        final List<Object> list = ptDemandDAO.getTaxDetailsForWaterConnection(
                waterConnectionDetails.getConnection().getConsumerCode(),
                waterConnectionDetails.getConnectionType().name());
        if (list.size() > 0)
            waterTaxDetails.setTaxDetails(new ArrayList<RestPropertyTaxDetails>(0));
        String loopInstallment = "";
        RestPropertyTaxDetails arrearDetails = null;
        BigDecimal total = BigDecimal.ZERO;
        for (final Object record : list) {

            final Object[] data = (Object[]) record;
            final String taxType = (String) data[0];

            final String installment = (String) data[1];
            final BigDecimal dmd = new BigDecimal((Double) data[2]);
            final Double col = (Double) data[3];
            final BigDecimal demand = BigDecimal.valueOf(dmd.intValue());
            final BigDecimal collection = BigDecimal.valueOf(col.doubleValue());
            if (loopInstallment.isEmpty()) {
                loopInstallment = installment;
                arrearDetails = new RestPropertyTaxDetails();
                arrearDetails.setInstallment(installment);
            }
            if (loopInstallment.equals(installment)) {

                if (PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(taxType))
                    arrearDetails.setPenalty(demand.subtract(collection));
                else if (PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(taxType))
                    arrearDetails.setChqBouncePenalty(demand.subtract(collection));
                else
                    total = total.add(demand.subtract(collection));

            } else {

                arrearDetails.setTaxAmount(total);
                arrearDetails
                        .setTotalAmount(total.add(arrearDetails.getPenalty()).add(arrearDetails.getChqBouncePenalty()));
                waterTaxDetails.getTaxDetails().add(arrearDetails);
                loopInstallment = installment;
                arrearDetails = new RestPropertyTaxDetails();
                arrearDetails.setInstallment(installment);
                total = BigDecimal.ZERO;
                if (PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(taxType))
                    arrearDetails.setPenalty(demand.subtract(collection));
                else if (PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(taxType))
                    arrearDetails.setChqBouncePenalty(demand.subtract(collection));
                else
                    total = total.add(demand.subtract(collection));

            }

        }
        if (arrearDetails != null) {
            arrearDetails.setTaxAmount(total);
            arrearDetails
                    .setTotalAmount(total.add(arrearDetails.getPenalty()).add(arrearDetails.getChqBouncePenalty()));
            waterTaxDetails.getTaxDetails().add(arrearDetails);
        }

        errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(WaterTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(WaterTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);

        waterTaxDetails.setErrorDetails(errorDetails);
        return waterTaxDetails;
    }

    public EgBill generateBillForConnection(final Billable billObj, final FinancialYearDAO financialYearDAO) {
        final EgBill bill = new EgBill();
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
        final EgDemand currentDemand = billObj.getCurrentDemand();
        bill.setEgDemand(currentDemand);
        bill.setDescription(billObj.getDescription());
        bill.setDisplayMessage(billObj.getDisplayMessage());
        bill.setEmailId(billObj.getEmailId());

        if (currentDemand != null && currentDemand.getMinAmtPayable() != null)
            bill.setMinAmtPayable(currentDemand.getMinAmtPayable());
        else
            bill.setMinAmtPayable(BigDecimal.ZERO);

        // Get it from the concrete implementation
        final List<EgBillDetails> bd = connectionBillService.getBilldetails(billObj);
        for (final EgBillDetails billdetails : bd) {
            bill.addEgBillDetails(billdetails);
            billdetails.setEgBill(bill);
            billdetails.setPurpose(getPurpose(billdetails).toString());
        }

        bill.setConsumerId(billObj.getConsumerId());
        bill.setConsumerType(billObj.getConsumerType());
        bill.setCallBackForApportion(Boolean.TRUE);
        return bill;
    };
}