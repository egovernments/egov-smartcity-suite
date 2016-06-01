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

package org.egov.collection.service;

import java.math.BigDecimal;
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
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.User;
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
            final Integer accountNumberId, final Integer positionUser, final String[] receiptNumberArray,
            final Date remittanceDate) {

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
        final String receiptSourceCondition = "and receipt.source = ? ";

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
        Date voucherDate = null;
        for (int i = 0; i < serviceNameArr.length; i++) {
            final String serviceName = serviceNameArr[i].trim();
            if (collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_SHOWREMITDATE).equals(
                            CollectionConstants.YES) && remittanceDate!=null)
                voucherDate = remittanceDate;
            else
                try {
                    voucherDate = collectionsUtil.getRemittanceVoucherDate(dateFomatter.parse(receiptDateArray[i]));
                } catch (ParseException e) {
                    LOGGER.error("Error Parsing Date",e);
                }
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
                    cashQueryBuilder.append(receiptSourceCondition);
                    final Object arguments[] = new Object[9];
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
                    arguments[8] = Source.SYSTEM.toString();
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
                    chequeQueryBuilder.append(receiptSourceCondition);
                    final Object arguments[] = new Object[10];

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
                    arguments[9] = Source.SYSTEM.toString();
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

                    final Object arguments[] = new Object[7];

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
                    onlineQueryBuilder.append(receiptSourceCondition);
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
                    arguments[6] = Source.SYSTEM.toString();
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

    /**
     * Method to find all the Cash,Cheque and DD type instruments with status as
     * :new and
     *
     * @return List of HashMap
     */
    @Override
    public List<HashMap<String, Object>> findAllRemittanceDetailsForServiceAndFund(final String boundaryIdList,
            final String serviceCodes, final String fundCodes) {

        final List<HashMap<String, Object>> paramList = new ArrayList<HashMap<String, Object>>();
        // TODO: Fix the sum(ih.instrumentamount) the amount is wrong because of
        // the ujl.boundary in (" + boundaryIdList + ")"
        final String queryBuilder = "SELECT sum(ih.instrumentamount) as INSTRUMENTMAOUNT,date(ch.RECEIPTDATE) AS RECEIPTDATE,"
                + "sd.NAME as SERVICENAME,it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.name AS DEPARTMENTNAME,"
                + "fnd.code AS FUNDCODE,dpt.code AS DEPARTMENTCODE from EGCL_COLLECTIONHEADER ch,"
                + "EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EGCL_SERVICEDETAILS sd,"
                + "EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt";

        final String whereClauseBeforInstumentType = " where ch.id=cm.collectionheader AND "
                + "fnd.id=cm.fund AND dpt.id=cm.department and ci.INSTRUMENTHEADER=ih.ID and "
                + "ch.SERVICEDETAILS=sd.ID and ch.ID=ci.COLLECTIONHEADER and ih.INSTRUMENTTYPE=it.ID and ";

        final String whereClauseForServiceAndFund = " sd.code in (" + serviceCodes + ")" + " and fnd.code in ("
                + fundCodes + ")" + " and ";

        final String whereClause = " AND ih.ID_STATUS=(select id from egw_status where moduletype='"
                + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER + "' " + "and description='"
                + CollectionConstants.INSTRUMENT_NEW_STATUS
                + "') and ih.ISPAYCHEQUE='0' and ch.STATUS=(select id from egw_status where " + "moduletype='"
                + CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code='"
                + CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "') "
                        + "AND ch.source='" + Source.SYSTEM + "' ";

        final String groupByClause = " group by date(ch.RECEIPTDATE),sd.NAME,it.TYPE,fnd.name,dpt.name,fnd.code,dpt.code";
        final String orderBy = " order by RECEIPTDATE";

        /**
         * Query to get the collection of the instrument types Cash,Cheque,DD &
         * Card for bank remittance
         */
        final StringBuilder queryStringForCashChequeDDCard = new StringBuilder(queryBuilder + ",egeis_jurisdiction ujl"
                + whereClauseBeforInstumentType + whereClauseForServiceAndFund + "it.TYPE in ('"
                + CollectionConstants.INSTRUMENTTYPE_CASH + "','" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "',"
                + "'" + CollectionConstants.INSTRUMENTTYPE_DD + "','" + CollectionConstants.INSTRUMENTTYPE_CARD + "') "
                + whereClause + "AND ch.CREATEDBY=ujl.employee and ujl.boundary in (" + boundaryIdList + ")"
                + groupByClause);

        /**
         * If the department of login user is AccountCell .i.e., Department
         * Code-'A',then this user will be able to remit online transaction as
         * well. All the online receipts created by 'citizen' user will be
         * remitted by Account Cell user.
         */
        final User citizenUser = collectionsUtil.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);

        if (boundaryIdList != null && citizenUser != null) {
            final String queryStringForOnline = " union " + queryBuilder + whereClauseBeforInstumentType
                    + whereClauseForServiceAndFund + "it.TYPE='" + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'"
                    + whereClause + "AND ch.CREATEDBY=" + citizenUser.getId() + groupByClause;

            queryStringForCashChequeDDCard.append(queryStringForOnline);
        }

        final Query query = receiptHeaderService.getSession().createSQLQuery(
                queryStringForCashChequeDDCard.toString() + orderBy);

        final List<Object[]> queryResults = query.list();

        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            HashMap<String, Object> objHashMap = new HashMap<String, Object>(0);

            if (i == 0) {
                objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                        || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                            arrayObjectInitialIndex[0]);
                }
            } else {
                final int checknew = receiptHeaderService.checkIfMapObjectExist(paramList, arrayObjectInitialIndex);
                if (checknew == -1) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                    }
                } else {
                    objHashMap = paramList.get(checknew);

                    paramList.remove(checknew);

                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                                arrayObjectInitialIndex[0]);
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        BigDecimal existingAmount = BigDecimal.ZERO;
                        if (objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT) != "")
                            existingAmount = new BigDecimal(objHashMap.get(
                                    CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).toString());
                        existingAmount = existingAmount.add(new BigDecimal(arrayObjectInitialIndex[0].toString()));
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, existingAmount);
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT,
                                arrayObjectInitialIndex[0]);
                }
            }
            if (objHashMap.get(CollectionConstants.BANKREMITTANCE_RECEIPTDATE) != null
                    && objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICENAME) != null)
                paramList.add(objHashMap);
        }
        return paramList;
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
