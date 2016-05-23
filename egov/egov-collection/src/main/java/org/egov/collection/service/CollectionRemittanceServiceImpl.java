package org.egov.collection.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

public class CollectionRemittanceServiceImpl extends CollectionRemittanceService {
    private static final long serialVersionUID = 5581301494846870670L;
    private static final Logger LOGGER = Logger.getLogger(ReceiptHeaderService.class);
    private CollectionsUtil collectionsUtil;
    private FinancialsUtil financialsUtil;
    private ReceiptHeaderService receiptHeaderService;
    private PersistenceService persistenceService;

    /**
     * Create Contra Vouchers for String array passed of serviceName,
     * totalCashAmount, totalChequeAmount, totalCardAmount and totalOnlineAcount
     *
     * @param serviceName
     * @param totalCashAmount
     * @param totalChequeAmount
     * @return List of Contra Vouchers Created
     */
    @Transactional
    @Override
    public List<ReceiptHeader> createBankRemittance(final String[] serviceNameArr, final String[] totalCashAmount,
            final String[] totalChequeAmount, final String[] totalCardAmount, final String[] totalOnlineAmount,
            final String[] receiptDateArray, final String[] fundCodeArray, final String[] departmentCodeArray,
            final Integer accountNumberId, final Integer positionUser, final String[] receiptNumberArray) {

        final List<ReceiptHeader> bankRemittanceList = new ArrayList<ReceiptHeader>(0);
        final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Map<String, Object> instrumentDepositeMap = financialsUtil.prepareForUpdateInstrumentDepositSQL();
        final String instrumentGlCodeQueryString = "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT "
                + "WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE=";
        final String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.collection.entity.ReceiptHeader receipt "
                + "join receipt.receiptInstrument as instruments where ";
        final String serviceNameCondition = "receipt.service.name=? ";
        final String receiptDateCondition = "and date(receipt.receiptdate)=? ";
        final String instrumentStatusCondition = "and instruments.statusId.id=? ";
        final String instrumentTypeCondition = "and instruments.instrumentType.type = ? ";
        final String receiptFundCondition = "and receipt.receiptMisc.fund.code = ? ";
        final String receiptDepartmentCondition = "and receipt.receiptMisc.department.code = ? ";

        final String cashInHandQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CASH + "'";
        final String chequeInHandQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'";
        final String cardPaymentQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CARD + "'";
        final String onlinePaymentQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'";

        final Query cashInHand = persistenceService.getSession().createSQLQuery(cashInHandQueryString);
        final Query chequeInHand = persistenceService.getSession().createSQLQuery(chequeInHandQueryString);
        final Query cardPaymentAccount = persistenceService.getSession().createSQLQuery(cardPaymentQueryString);
        final Query onlinePaymentAccount = persistenceService.getSession().createSQLQuery(onlinePaymentQueryString);

        String cashInHandGLCode = null, chequeInHandGlcode = null, cardPaymentGlCode = null, onlinePaymentGlCode = null;

        final String voucherWorkflowMsg = "Voucher Workflow Started";

        final String createVoucher = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_CREATEVOUCHER_FOR_REMITTANCE);
        final String functionCode = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_FUNCTIONCODE);
        final EgwStatus instrmentStatusNew = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_NEW_STATUS);
        final EgwStatus receiptStatusRemitted = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_REMITTED);

        if (!cashInHand.list().isEmpty())
            cashInHandGLCode = cashInHand.list().get(0).toString();
        if (!chequeInHand.list().isEmpty())
            chequeInHandGlcode = chequeInHand.list().get(0).toString();
        if (!cardPaymentAccount.list().isEmpty())
            cardPaymentGlCode = cardPaymentAccount.list().get(0).toString();
        if (!onlinePaymentAccount.list().isEmpty())
            onlinePaymentGlCode = onlinePaymentAccount.list().get(0).toString();

        /**
         * Get the AppConfig parameter defined for the Remittance voucher type
         * in case of instrument type Cheque,DD & Card
         */

        Boolean voucherTypeForChequeDDCard = false;

        if (collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD).equals(
                CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))
            voucherTypeForChequeDDCard = true;

        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);

        for (int i = 0; i < serviceNameArr.length; i++) {
            final String serviceName = serviceNameArr[i].trim();
            final Date voucherDate = collectionsUtil.getRemittanceVoucherDate(receiptDateArray[i]);

            if (serviceName != null && serviceName.length() > 0) {
                final Bankaccount depositedBankAccount = (Bankaccount) persistenceService.find(
                        "from Bankaccount where id=?", Long.valueOf(accountNumberId.longValue()));
                final ServiceDetails serviceDetails = (ServiceDetails) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);

                final String serviceGlCode = depositedBankAccount.getChartofaccounts().getGlcode();

                // If Cash Amount is present
                if (totalCashAmount[i].trim() != null && totalCashAmount[i].trim().length() > 0
                        && cashInHandGLCode != null) {
                    final StringBuilder cashQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    cashQueryBuilder.append(serviceNameCondition);
                    cashQueryBuilder.append(receiptDateCondition);
                    cashQueryBuilder.append(instrumentStatusCondition);
                    cashQueryBuilder.append(instrumentTypeCondition);
                    cashQueryBuilder.append(receiptFundCondition);
                    cashQueryBuilder.append(receiptDepartmentCondition);
                    cashQueryBuilder
                            .append("and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");

                    final Object arguments[] = new Object[8];
                    CVoucherHeader voucherHeaderCash = null;

                    arguments[0] = serviceName;
                    try {
                        arguments[1] = dateFomatter.parse(receiptDateArray[i]);
                    } catch (final ParseException exp) {
                        LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
                        throw new ApplicationRuntimeException("Exception while parsing date", exp);
                    }
                    arguments[2] = instrmentStatusNew.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_CASH;
                    arguments[4] = fundCodeArray[i];
                    arguments[5] = departmentCodeArray[i];
                    arguments[6] = CollectionConstants.MODULE_NAME_RECEIPTHEADER;
                    arguments[7] = CollectionConstants.RECEIPT_STATUS_CODE_APPROVED;

                    final List<InstrumentHeader> instrumentHeaderListCash = persistenceService.findAllBy(
                            cashQueryBuilder.toString(), arguments);

                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);

                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        final List<HashMap<String, Object>> accountCodeCashList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditCashHashMap = new HashMap<String, Object>(
                                0);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.GLCODE, cashInHandGLCode);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.CREDITAMOUNT, totalCashAmount[i]);
                        accountcodedetailsCreditCashHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeCashList.add(accountcodedetailsCreditCashHashMap);

                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCashAmount[i]);
                        accountCodeCashList.add(accountcodedetailsDebitHashMap);

                        voucherHeaderCash = financialsUtil.createRemittanceVoucher(headerdetails, accountCodeCashList,
                                new ArrayList<HashMap<String, Object>>(0));

                        receiptHeaderService.updateCashRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherDate, depositedBankAccount, serviceGlCode, instrumentHeaderListCash,
                                voucherHeaderCash);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCash, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListCash) {
                        List<ReceiptHeader> receiptHeaders = persistenceService.findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        if (voucherHeaderCash != null)
                            receiptHeaders = receiptHeaderService.setVoucherNumber(receiptHeaders, voucherHeaderCash);
                        bankRemittanceList.addAll(receiptHeaders);
                    }

                }
                // If Cheque Amount is present
                if (totalChequeAmount[i].trim() != null && totalChequeAmount[i].trim().length() > 0
                        && chequeInHandGlcode != null) {
                    final StringBuilder chequeQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    chequeQueryBuilder.append(serviceNameCondition);
                    chequeQueryBuilder.append(receiptDateCondition);
                    chequeQueryBuilder.append(instrumentStatusCondition);
                    chequeQueryBuilder.append("and instruments.instrumentType.type in ( ?, ?)");
                    chequeQueryBuilder
                            .append("and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");
                    chequeQueryBuilder.append(receiptFundCondition);
                    chequeQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[9];

                    arguments[0] = serviceName;
                    try {
                        arguments[1] = dateFomatter.parse(receiptDateArray[i]);
                    } catch (final ParseException exp) {
                        LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
                        throw new ApplicationRuntimeException("Exception while parsing date", exp);
                    }
                    arguments[2] = instrmentStatusNew.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_CHEQUE;
                    arguments[4] = CollectionConstants.INSTRUMENTTYPE_DD;
                    arguments[5] = CollectionConstants.MODULE_NAME_RECEIPTHEADER;
                    arguments[6] = CollectionConstants.RECEIPT_STATUS_CODE_APPROVED;
                    arguments[7] = fundCodeArray[i];
                    arguments[8] = departmentCodeArray[i];
                    CVoucherHeader voucherHeaderCheque = null;
                    final List<InstrumentHeader> instrumentHeaderListCheque = persistenceService.findAllBy(
                            chequeQueryBuilder.toString(), arguments);
                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);
                        final List<HashMap<String, Object>> accountCodeChequeList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditChequeHashMap = new HashMap<String, Object>(
                                0);

                        if (voucherTypeForChequeDDCard) {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);

                        } else {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        }
                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.GLCODE, chequeInHandGlcode);
                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.CREDITAMOUNT, totalChequeAmount[i]);
                        accountcodedetailsCreditChequeHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeChequeList.add(accountcodedetailsCreditChequeHashMap);

                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalChequeAmount[i]);
                        accountCodeChequeList.add(accountcodedetailsDebitHashMap);

                        voucherHeaderCheque = financialsUtil.createRemittanceVoucher(headerdetails,
                                accountCodeChequeList, new ArrayList<HashMap<String, Object>>(0));
                        receiptHeaderService.updateChequeCardRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherTypeForChequeDDCard, voucherDate, depositedBankAccount, serviceGlCode,
                                instrumentHeaderListCheque, voucherHeaderCheque);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCheque, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListCheque) {
                        List<ReceiptHeader> receiptHeaders = persistenceService.findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        if (voucherHeaderCheque != null)
                            receiptHeaders = receiptHeaderService.setVoucherNumber(receiptHeaders, voucherHeaderCheque);
                        bankRemittanceList.addAll(receiptHeaders);
                    }
                }
                // If card amount is present
                if (totalCardAmount[i].trim() != null && totalCardAmount[i].trim().length() > 0
                        && cardPaymentGlCode != null) {
                    final StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    onlineQueryBuilder.append(serviceNameCondition);
                    onlineQueryBuilder.append(receiptDateCondition);
                    onlineQueryBuilder.append(instrumentStatusCondition);
                    onlineQueryBuilder.append(instrumentTypeCondition);
                    onlineQueryBuilder.append(receiptFundCondition);
                    onlineQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[6];

                    arguments[0] = serviceName;
                    try {
                        arguments[1] = dateFomatter.parse(receiptDateArray[i]);
                    } catch (final ParseException exp) {
                        LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
                        throw new ApplicationRuntimeException("Exception while parsing date", exp);
                    }
                    arguments[2] = instrmentStatusNew.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_CARD;
                    arguments[4] = fundCodeArray[i];
                    arguments[5] = departmentCodeArray[i];
                    CVoucherHeader voucherHeaderCard = null;
                    final List<InstrumentHeader> instrumentHeaderListOnline = persistenceService.findAllBy(
                            onlineQueryBuilder.toString(), arguments);

                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);

                        if (voucherTypeForChequeDDCard) {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
                        } else {

                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        }
                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        final List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>(
                                0);

                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, cardPaymentGlCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalCardAmount[i]);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCardAmount[i]);
                        accountCodeOnlineList.add(accountcodedetailsDebitHashMap);

                        voucherHeaderCard = financialsUtil.createRemittanceVoucher(headerdetails,
                                accountCodeOnlineList, new ArrayList<HashMap<String, Object>>(0));
                        receiptHeaderService.updateChequeCardRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherTypeForChequeDDCard, voucherDate, depositedBankAccount, serviceGlCode,
                                instrumentHeaderListOnline, voucherHeaderCard);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListOnline, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListOnline) {
                        List<ReceiptHeader> receiptHeaders = persistenceService.findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        if (voucherHeaderCard != null)
                            receiptHeaders = receiptHeaderService.setVoucherNumber(receiptHeaders, voucherHeaderCard);
                        bankRemittanceList.addAll(receiptHeaders);
                    }

                }
                // If online amount is present
                if (totalOnlineAmount[i].trim() != null && totalOnlineAmount[i].trim().length() > 0
                        && onlinePaymentGlCode != null) {
                    final StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    onlineQueryBuilder.append(serviceNameCondition);
                    onlineQueryBuilder.append(receiptDateCondition);
                    onlineQueryBuilder.append(instrumentStatusCondition);
                    onlineQueryBuilder.append(instrumentTypeCondition);
                    onlineQueryBuilder.append(receiptFundCondition);
                    onlineQueryBuilder.append(receiptDepartmentCondition);

                    final Object arguments[] = new Object[6];

                    arguments[0] = serviceName;
                    try {
                        arguments[1] = dateFomatter.parse(receiptDateArray[i]);
                    } catch (final ParseException exp) {
                        LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
                        throw new ApplicationRuntimeException("Exception while parsing date", exp);
                    }
                    arguments[2] = instrmentStatusNew.getId();
                    arguments[3] = CollectionConstants.INSTRUMENTTYPE_ONLINE;
                    arguments[4] = fundCodeArray[i];
                    arguments[5] = departmentCodeArray[i];

                    final List<InstrumentHeader> instrumentHeaderListOnline = persistenceService.findAllBy(
                            onlineQueryBuilder.toString(), arguments);
                    CVoucherHeader voucherHeaderCard = null;

                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation()) {
                        final HashMap<String, Object> headerdetails = new HashMap<String, Object>(0);

                        if (voucherTypeForChequeDDCard) {
                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
                        } else {

                            headerdetails.put(VoucherConstant.VOUCHERNAME,
                                    CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                            headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                    CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        }
                        headerdetails.put(VoucherConstant.VOUCHERNAME,
                                CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
                        headerdetails.put(VoucherConstant.VOUCHERTYPE,
                                CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
                        headerdetails
                                .put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
                        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
                        headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
                        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
                        headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null
                                : serviceDetails.getFundSource().getCode());
                        headerdetails.put(VoucherConstant.FUNCTIONARYCODE,
                                serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary()
                                        .getCode());
                        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

                        final List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>(
                                0);
                        final HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>(
                                0);

                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, onlinePaymentGlCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalOnlineAmount[i]);
                        accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

                        accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
                        final HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>(0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
                        accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalOnlineAmount[i]);
                        accountCodeOnlineList.add(accountcodedetailsDebitHashMap);

                        voucherHeaderCard = financialsUtil.createRemittanceVoucher(headerdetails,
                                accountCodeOnlineList, new ArrayList<HashMap<String, Object>>(0));
                        receiptHeaderService.updateChequeCardRemittance(instrumentDepositeMap, voucherWorkflowMsg,
                                voucherTypeForChequeDDCard, voucherDate, depositedBankAccount, serviceGlCode,
                                instrumentHeaderListOnline, voucherHeaderCard);
                    } else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListOnline, instrumentStatusDeposited,
                                depositedBankAccount);

                    for (final InstrumentHeader instHead : instrumentHeaderListOnline) {
                        List<ReceiptHeader> receiptHeaders = persistenceService.findAllByNamedQuery(
                                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE,
                                instHead.getId(), serviceDetails.getCode());
                        if (voucherHeaderCard != null)
                            receiptHeaders = receiptHeaderService.setVoucherNumber(receiptHeaders, voucherHeaderCard);
                        bankRemittanceList.addAll(receiptHeaders);
                    }

                }
            }
        }
        final List<ReceiptHeader> bankRemitList = new ArrayList<ReceiptHeader>();
        for (final ReceiptHeader receiptHeader : bankRemittanceList) {
            receiptHeader.setStatus(receiptStatusRemitted);
            if (!bankRemitList.contains(receiptHeader))
                bankRemitList.add(receiptHeader);
            persistenceService.update(receiptHeader);
            persistenceService.getSession().flush();
        }
        return bankRemitList;

    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
