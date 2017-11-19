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

package org.egov.collection.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.BranchUserMap;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.Remittance;
import org.egov.collection.entity.RemittanceDetail;
import org.egov.collection.entity.RemittanceInstrument;
import org.egov.collection.utils.CollectionsNumberGenerator;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class RemittanceServiceImpl extends RemittanceService {
    private static final long serialVersionUID = 5581301494846870670L;
    private static final Logger LOGGER = Logger.getLogger(ReceiptHeaderService.class);
    private CollectionsUtil collectionsUtil;
    private FinancialsUtil financialsUtil;
    private ReceiptHeaderService receiptHeaderService;
    private PersistenceService persistenceService;
    private CollectionsNumberGenerator collectionsNumberGenerator;
    @Autowired
    private FundHibernateDAO fundHibernateDAO;
    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;
    @Autowired
    private ChartOfAccountsDAO chartOfAccountsDAO;
    private PersistenceService<Remittance, Long> remittancePersistService;
    @Autowired
    @Qualifier("branchUserMapService")
    private PersistenceService<BranchUserMap, Long> branchUserMapService;

    /**
     * Create Contra Vouchers for String array passed of serviceName, totalCashAmount, totalChequeAmount, totalCardAmount and
     *
     * @param serviceName
     * @param totalCashAmount
     * @param totalAmount
     * @return List of Contra Vouchers Created
     */
    @Transactional
    @Override
    public List<ReceiptHeader> createBankRemittance(final String[] serviceNameArr, final String[] totalCashAmount,
            final String[] totalAmount, final String[] totalCardAmount, final String[] receiptDateArray,
            final String[] fundCodeArray, final String[] departmentCodeArray, final Integer accountNumberId,
            final Integer positionUser, final String[] receiptNumberArray, final Date remittanceDate) {

        final List<ReceiptHeader> bankRemittanceList = new ArrayList<>(0);
        final List<ReceiptHeader> bankRemitList = new ArrayList<>();
        final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        financialsUtil.prepareForUpdateInstrumentDepositSQL();
        final String instrumentGlCodeQueryString = "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT "
                + "WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE=";
        final String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.collection.entity.ReceiptHeader receipt "
                + "join receipt.receiptInstrument as instruments join receipt.receiptMisc as receiptMisc where ";
        final String serviceNameCondition = "receipt.service.name=? ";
        final String receiptDateCondition = "and date(receipt.receiptdate)=? ";
        final String instrumentStatusCondition = "and instruments.statusId.id=? ";
        final String instrumentTypeCondition = "and instruments.instrumentType.type = ? ";
        final String receiptFundCondition = "and receiptMisc.fund.code = ? ";
        final String receiptDepartmentCondition = "and receiptMisc.department.code = ? ";
        final String receiptSourceCondition = "and receipt.source = ? ";
        String depositedBranchCondition = "";
        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser())) {
            BranchUserMap branchUserMap = branchUserMapService.findByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_BRANCHUSER_BY_USER,
                    collectionsUtil.getLoggedInUser().getId());
            depositedBranchCondition = "and receiptMisc.depositedBranch.id=" + branchUserMap.getBankbranch().getId();
        } else
            depositedBranchCondition = "and receiptMisc.depositedBranch is null";

        final String cashInHandQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CASH
                + "'";
        final String chequeInHandQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'";
        final String cardPaymentQueryString = instrumentGlCodeQueryString + "'"
                + CollectionConstants.INSTRUMENTTYPE_CARD + "'";
        final Query cashInHand = persistenceService.getSession().createSQLQuery(cashInHandQueryString);
        final Query chequeInHand = persistenceService.getSession().createSQLQuery(chequeInHandQueryString);
        final Query cardPaymentAccount = persistenceService.getSession().createSQLQuery(cardPaymentQueryString);

        String cashInHandGLCode = null;
        String chequeInHandGlcode = null;

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
            cardPaymentAccount.list().get(0).toString();
        collectionsUtil.getVoucherType();
        Boolean showRemitDate = false;
        BigDecimal totalCashAmt = BigDecimal.ZERO;
        BigDecimal totalChequeAmount = BigDecimal.ZERO;
        BigDecimal totalCashVoucherAmt = BigDecimal.ZERO;
        BigDecimal totalChequeVoucherAmt = BigDecimal.ZERO;
        String fundCode = "";
        Date voucherDate = null;
        List<InstrumentHeader> instrumentHeaderListCash;
        List<InstrumentHeader> instrumentHeaderListCheque;
        if (collectionsUtil
                .getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_SHOWREMITDATE)
                .equals(CollectionConstants.YES))
            showRemitDate = true;
        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);
        final Bankaccount depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                Long.valueOf(accountNumberId.longValue()));
        final String serviceGlCode = depositedBankAccount.getChartofaccounts().getGlcode();
        for (int i = 0; i < serviceNameArr.length; i++) {
            final String serviceName = serviceNameArr[i].trim();
            if (showRemitDate && remittanceDate != null)
                voucherDate = remittanceDate;
            else
                try {
                    collectionsUtil.getRemittanceVoucherDate(dateFomatter.parse(receiptDateArray[i]));
                } catch (final ParseException e) {
                    LOGGER.error("Error Parsing Date", e);
                }
            if (serviceName != null && serviceName.length() > 0) {
                persistenceService.findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);
                final ServiceDetails serviceDetails = (ServiceDetails) persistenceService
                        .findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);

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
                    cashQueryBuilder.append(
                            "and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");
                    cashQueryBuilder.append(receiptSourceCondition);
                    cashQueryBuilder.append(depositedBranchCondition);
                    final Object arguments[] = new Object[9];
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
                    fundCode = fundCodeArray[i];
                    instrumentHeaderListCash = persistenceService.findAllBy(cashQueryBuilder.toString(), arguments);
                    totalCashAmt = totalCashAmt.add(new BigDecimal(totalCashAmount[i]));
                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation())
                        totalCashVoucherAmt = totalCashVoucherAmt.add(new BigDecimal(totalCashAmount[i]));
                    else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCash, instrumentStatusDeposited,
                                depositedBankAccount);
                    bankRemittanceList.addAll(getRemittanceList(serviceDetails, instrumentHeaderListCash));
                }
                // If Cheque Amount is present
                if (totalAmount[i].trim() != null && totalAmount[i].trim().length() > 0 && chequeInHandGlcode != null) {
                    final StringBuilder chequeQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    chequeQueryBuilder.append(serviceNameCondition);
                    chequeQueryBuilder.append(receiptDateCondition);
                    chequeQueryBuilder.append(instrumentStatusCondition);
                    chequeQueryBuilder.append("and instruments.instrumentType.type in ( ?, ?)");
                    chequeQueryBuilder.append(
                            "and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");
                    chequeQueryBuilder.append(receiptFundCondition);
                    chequeQueryBuilder.append(receiptDepartmentCondition);
                    chequeQueryBuilder.append(receiptSourceCondition);
                    chequeQueryBuilder.append(depositedBranchCondition);
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
                    fundCode = fundCodeArray[i];
                    instrumentHeaderListCheque = persistenceService.findAllBy(chequeQueryBuilder.toString(), arguments);
                    totalChequeAmount = totalChequeAmount.add(new BigDecimal(totalAmount[i]));
                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation())
                        totalChequeVoucherAmt = totalChequeVoucherAmt.add(new BigDecimal(totalAmount[i]));
                    else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCheque, instrumentStatusDeposited,
                                depositedBankAccount);
                    bankRemittanceList.addAll(getRemittanceList(serviceDetails, instrumentHeaderListCheque));
                }
            }
            for (final ReceiptHeader receiptHeader : bankRemittanceList)
                if (!bankRemitList.contains(receiptHeader))
                    bankRemitList.add(receiptHeader);
        }
        final Remittance remittance = populateAndPersistRemittance(totalCashAmt, totalChequeAmount, fundCode,
                cashInHandGLCode, chequeInHandGlcode, serviceGlCode, functionCode, bankRemitList, createVoucher,
                voucherDate, depositedBankAccount, totalCashVoucherAmt, totalChequeVoucherAmt);

        for (final ReceiptHeader receiptHeader : bankRemitList) {
            receiptHeader.setStatus(receiptStatusRemitted);
            receiptHeader.setRemittanceReferenceNumber(remittance.getReferenceNumber());
            receiptHeaderService.update(receiptHeader);
        }
        return bankRemitList;
    }

    @Transactional
    public CVoucherHeader createVoucherForRemittance(final String cashInHandGLCode, final String chequeInHandGLcode,
            final String serviceGLCode, final String functionCode, final BigDecimal totalCashVoucherAmt,
            final BigDecimal totalChequeVoucherAmt, final Date voucherDate, final String fundCode) {
        CVoucherHeader voucherHeader;
        final List<HashMap<String, Object>> accountCodeList = new ArrayList<>(0);
        HashMap<String, Object> accountcodedetailsHashMap;
        if (totalCashVoucherAmt.compareTo(BigDecimal.ZERO) > 0 && !cashInHandGLCode.isEmpty()) {
            accountcodedetailsHashMap = prepareAccountCodeDetails(cashInHandGLCode, functionCode, totalCashVoucherAmt,
                    BigDecimal.ZERO);
            accountCodeList.add(accountcodedetailsHashMap);
        }
        if (totalChequeVoucherAmt.compareTo(BigDecimal.ZERO) > 0 && !chequeInHandGLcode.isEmpty()) {
            accountcodedetailsHashMap = prepareAccountCodeDetails(chequeInHandGLcode, functionCode,
                    totalChequeVoucherAmt, BigDecimal.ZERO);
            accountCodeList.add(accountcodedetailsHashMap);
        }
        final BigDecimal totalDebitAmount = totalChequeVoucherAmt.add(totalCashVoucherAmt);
        if (!serviceGLCode.isEmpty()) {
            accountcodedetailsHashMap = prepareAccountCodeDetails(serviceGLCode, functionCode, BigDecimal.ZERO,
                    totalDebitAmount);
            accountCodeList.add(accountcodedetailsHashMap);
        }
        voucherHeader = financialsUtil.createRemittanceVoucher(prepareHeaderDetails(fundCode, voucherDate),
                accountCodeList, new ArrayList<HashMap<String, Object>>(0));
        return voucherHeader;
    }

    @SuppressWarnings("unchecked")
    public List<ReceiptHeader> getRemittanceList(final ServiceDetails serviceDetails,
            final List<InstrumentHeader> instrumentHeaderList) {
        final List<Long> instHeaderList = new ArrayList<>();
        for (final InstrumentHeader instHead : instrumentHeaderList)
            instHeaderList.add(instHead.getId());
        final List<ReceiptHeader> bankRemittanceList = new ArrayList<>();
        final List<ReceiptHeader> receiptHeaders = persistenceService.findAllByNamedQuery(
                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTHEADER_AND_SERVICECODE, serviceDetails.getCode(),
                instHeaderList);
        bankRemittanceList.addAll(receiptHeaders);
        return bankRemittanceList;
    }

    @Transactional
    public Remittance populateAndPersistRemittance(final BigDecimal totalCashAmount, final BigDecimal totalChequeAmount,
            final String fundCode, final String cashInHandGLCode, final String chequeInHandGLcode,
            final String serviceGLCode, final String functionCode, final List<ReceiptHeader> receiptHeadList,
            final String createVoucher, final Date voucherDate, final Bankaccount depositedBankAccount,
            final BigDecimal totalCashVoucherAmt, final BigDecimal totalChequeVoucherAmt) {
        CVoucherHeader voucherHeader;
        final CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(new Date());
        BigDecimal totalAmount = BigDecimal.ZERO;
        final Remittance remittance = new Remittance();
        final List<RemittanceDetail> remittanceDetailsList = new ArrayList<>();
        remittance.setReferenceDate(new Date());
        final EgwStatus receiptStatusApproved = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_REMITTANCE, CollectionConstants.REMITTANCE_STATUS_CODE_APPROVED);
        remittance.setStatus(receiptStatusApproved);
        remittance.setReferenceNumber(collectionsNumberGenerator.generateRemittanceNumber(financialYear));
        remittance.setFund(fundHibernateDAO.fundByCode(fundCode));
        remittance.setFunction(functionHibernateDAO.getFunctionByCode(functionCode));
        remittance.setCollectionRemittance(new HashSet<ReceiptHeader>(receiptHeadList));
        remittance.setBankAccount(depositedBankAccount);
        if (totalCashAmount != null && totalCashAmount.compareTo(BigDecimal.ZERO) > 0 && cashInHandGLCode != null) {
            remittanceDetailsList
                    .addAll(getRemittanceDetailsList(totalCashAmount, BigDecimal.ZERO, cashInHandGLCode, remittance));
            totalAmount = totalAmount.add(totalCashAmount);
        }
        if (totalChequeAmount != null && totalChequeAmount.compareTo(BigDecimal.ZERO) > 0
                && chequeInHandGLcode != null) {
            remittanceDetailsList.addAll(
                    getRemittanceDetailsList(totalChequeAmount, BigDecimal.ZERO, chequeInHandGLcode, remittance));
            totalAmount = totalAmount.add(totalChequeAmount);
        }
        remittanceDetailsList.addAll(getRemittanceDetailsList(BigDecimal.ZERO, totalAmount, serviceGLCode, remittance));
        remittance.setRemittanceDetails(new HashSet<RemittanceDetail>(remittanceDetailsList));
        remittancePersistService.persist(remittance);
        if (CollectionConstants.YES.equalsIgnoreCase(createVoucher)
                && (totalCashVoucherAmt.compareTo(BigDecimal.ZERO) > 0
                        || totalChequeVoucherAmt.compareTo(BigDecimal.ZERO) > 0)) {
            voucherHeader = createVoucherForRemittance(cashInHandGLCode, chequeInHandGLcode, serviceGLCode,
                    functionCode, totalCashVoucherAmt, totalChequeVoucherAmt, voucherDate, fundCode);
            remittance.setVoucherHeader(voucherHeader);
            for (ReceiptHeader receiptHeader : receiptHeadList) {
                for (InstrumentHeader instHead : receiptHeader.getReceiptInstrument()) {
                    persistRemittanceInstrument(remittance, instHead);
                }
            }
            remittancePersistService.persist(remittance);
        }
        return remittance;
    }

    @Transactional
    public void persistRemittanceInstrument(Remittance remittance, InstrumentHeader instrumentHeader) {
        RemittanceInstrument remittanceInstrument = new RemittanceInstrument();
        remittanceInstrument.setRemittance(remittance);
        remittanceInstrument.setInstrumentHeader(instrumentHeader);
        remittanceInstrument.setReconciled(Boolean.FALSE);
        persistenceService.persist(remittanceInstrument);
    }

    public HashMap<String, Object> prepareAccountCodeDetails(final String glCode, final String functionCode,
            final BigDecimal creditAmount, final BigDecimal debitAmount) {
        final HashMap<String, Object> accountcodedetailsHashMap = new HashMap<>(0);
        accountcodedetailsHashMap.put(VoucherConstant.GLCODE, glCode);
        accountcodedetailsHashMap.put(VoucherConstant.FUNCTIONCODE, functionCode);
        accountcodedetailsHashMap.put(VoucherConstant.CREDITAMOUNT, creditAmount);
        accountcodedetailsHashMap.put(VoucherConstant.DEBITAMOUNT, debitAmount);
        return accountcodedetailsHashMap;
    }

    public HashMap<String, Object> prepareHeaderDetails(final String fundCode, final Date voucherDate) {
        final HashMap<String, Object> headerdetails = new HashMap<>(0);
        if (collectionsUtil.getVoucherType()) {
            headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
            headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
        } else {
            headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
            headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
        }
        headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
        headerdetails.put(VoucherConstant.FUNDCODE, fundCode);
        headerdetails.put(VoucherConstant.DEPARTMENTCODE, CollectionConstants.DEPT_CODE_FOR_ACCOUNTS);
        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
        headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
        return headerdetails;
    }

    public List<RemittanceDetail> getRemittanceDetailsList(final BigDecimal creditAmount, final BigDecimal debitAmount,
            final String glCode, final Remittance remittance) {
        final List<RemittanceDetail> remittanceDetailsList = new ArrayList<>();
        final RemittanceDetail remittanceDetail = new RemittanceDetail();
        remittanceDetail.setCreditAmount(creditAmount);
        remittanceDetail.setDebitAmount(debitAmount);
        remittanceDetail.setRemittance(remittance);
        remittanceDetail.setChartOfAccount(chartOfAccountsDAO.getCChartOfAccountsByGlCode(glCode));
        remittanceDetailsList.add(remittanceDetail);
        return remittanceDetailsList;
    }

    /**
     * Method to find all the Cash,Cheque and DD type instruments with status as :new and
     *
     * @return List of HashMap
     */
    @Override
    public List<HashMap<String, Object>> findAllRemittanceDetailsForServiceAndFund(final String boundaryIdList,
            final String serviceCodes, final String fundCodes, final Date startDate, final Date endDate,
            final String paymentMode) {

        final List<HashMap<String, Object>> paramList = new ArrayList<>();
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

        String whereClause = " AND ih.ID_STATUS=(select id from egw_status where moduletype='"
                + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER + "' " + "and description='"
                + CollectionConstants.INSTRUMENT_NEW_STATUS
                + "') and ih.ISPAYCHEQUE='0' and ch.STATUS=(select id from egw_status where " + "moduletype='"
                + CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code='"
                + CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "') " + " AND ch.source='" + Source.SYSTEM + "' ";
        if (startDate != null && endDate != null)
            whereClause = whereClause + " AND date(ch.receiptdate) between '" + startDate + "' and '" + endDate + "' ";

        final String groupByClause = " group by date(ch.RECEIPTDATE),sd.NAME,it.TYPE,fnd.name,dpt.name,fnd.code,dpt.code";
        final String orderBy = " order by RECEIPTDATE";

        /**
         * Query to get the collection of the instrument types Cash,Cheque,DD & Card for bank remittance
         */
        final StringBuilder queryStringForCashChequeDDCard = new StringBuilder(queryBuilder
                + whereClauseBeforInstumentType + whereClauseForServiceAndFund + "it.TYPE in ");
        if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_CASH))
            queryStringForCashChequeDDCard.append("('" + CollectionConstants.INSTRUMENTTYPE_CASH + "')");
        else if (paymentMode.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD))
            queryStringForCashChequeDDCard.append("('" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'," + "'"
                    + CollectionConstants.INSTRUMENTTYPE_DD + "') ");
        else
            queryStringForCashChequeDDCard.append(
                    "('" + CollectionConstants.INSTRUMENTTYPE_CASH + "','" + CollectionConstants.INSTRUMENTTYPE_CHEQUE
                            + "'," + "'" + CollectionConstants.INSTRUMENTTYPE_DD + "') ");
        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser())) {
            BranchUserMap branchUserMap = branchUserMapService.findByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_BRANCHUSER_BY_USER,
                    collectionsUtil.getLoggedInUser().getId());
            queryStringForCashChequeDDCard
                    .append(whereClause + " AND cm.depositedbranch=" + branchUserMap.getBankbranch().getId());
        } else
            queryStringForCashChequeDDCard.append(whereClause
                    + " AND cm.depositedbranch is null AND ch.CREATEDBY in (select distinct ujl.employee from egeis_jurisdiction ujl where ujl.boundary in ("
                    + boundaryIdList + "))");

        queryStringForCashChequeDDCard.append(groupByClause);

        final Query query = receiptHeaderService.getSession()
                .createSQLQuery(queryStringForCashChequeDDCard.toString() + orderBy);

        final List<Object[]> queryResults = query.list();

        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            HashMap<String, Object> objHashMap = new HashMap<>(0);

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
                }
                if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                        || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
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
                    }
                    if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                            || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                                arrayObjectInitialIndex[0]);
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
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
                        if (!objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).equals(""))
                            existingAmount = new BigDecimal(objHashMap
                                    .get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).toString());
                        existingAmount = existingAmount.add(new BigDecimal(arrayObjectInitialIndex[0].toString()));
                        objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, existingAmount);
                    }
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

    public CollectionsNumberGenerator getCollectionsNumberGenerator() {
        return collectionsNumberGenerator;
    }

    public void setCollectionsNumberGenerator(final CollectionsNumberGenerator collectionsNumberGenerator) {
        this.collectionsNumberGenerator = collectionsNumberGenerator;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setRemittancePersistService(final PersistenceService<Remittance, Long> remittancePersistService) {
        this.remittancePersistService = remittancePersistService;
    }
}