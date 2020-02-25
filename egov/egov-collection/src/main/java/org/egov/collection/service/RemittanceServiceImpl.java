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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.BranchUserMap;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.Remittance;
import org.egov.collection.entity.RemittanceDetail;
import org.egov.collection.entity.RemittanceInstrument;
import org.egov.collection.integration.services.RemittanceSchedulerService;
import org.egov.collection.service.spec.RemittanceSpec;
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
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
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
    @Autowired
    @Qualifier("remittanceInstrumentService")
    private PersistenceService<RemittanceInstrument, Long> remittanceInstrumentService;
    @Autowired
    private transient RemittanceSchedulerService remittanceSchedulerService;

    @PersistenceContext
    private EntityManager entityManager;
    
    private static final String SERVICE_QUERY = new StringBuilder("select distinct sd.code as servicecode from "
            + " EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd where asm.servicedetails=sd.ID "
            + " and asm.bankaccount= :accountNumberId").toString();
    private static final String FUND_QUERY = new StringBuilder()
            .append("select fd.code as fundcode from BANKACCOUNT ba,FUND fd")
            .append(" where fd.ID=ba.FUNDID and ba.id= :accountNumberId").toString();
    private static final String BANK_ACCOUNT_NUMBER_QUERY = "select distinct ba.accountnumber from BANKACCOUNT ba where ba.id =:accountNumberId";

    /**
     * Create Contra Vouchers for String array passed of serviceName, totalCashAmount, totalChequeAmount, totalCardAmount and
     *
     * @param serviceName
     * @param approverIdArr
     * @param totalCashAmount
     * @param totalAmount
     * @return List of Contra Vouchers Created
     */
    @Transactional
    @Override
    public List<ReceiptHeader> createCashBankRemittance(RemittanceSpec remittanceSpec) {

        final List<ReceiptHeader> bankRemittanceList = new ArrayList<>(0);
        final List<ReceiptHeader> bankRemitList = new ArrayList<>();
        final SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        financialsUtil.prepareForUpdateInstrumentDepositSQL();
        final String cashInHandQueryString = "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT "
                + "WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE= '"
                + CollectionConstants.INSTRUMENTTYPE_CASH + "'";
        final String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.collection.entity.ReceiptHeader receipt "
                + "join receipt.receiptInstrument as instruments join receipt.receiptMisc as receiptMisc where ";
        final String serviceNameCondition = "receipt.service.name=:servicename ";
        final String receiptDateCondition = "and date(receipt.receiptdate)=:receiptdate ";
        final String instrumentStatusCondition = "and instruments.statusId.id=:instrstatusid ";
        final String instrumentTypeCondition = "and instruments.instrumentType.type =:instrtype ";
        final String receiptFundCondition = "and receiptMisc.fund.code =:fundcode ";
        final String receiptDepartmentCondition = "and receiptMisc.department.code =:departmetcode ";
        final String receiptSourceCondition = "and receipt.source =:source ";
        String depositedBranchCondition = CollectionConstants.BLANK;
        String receiptApproverCondition = CollectionConstants.BLANK;
        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser())) {
            BranchUserMap branchUserMap = branchUserMapService.findByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_BRANCHUSER_BY_USER, collectionsUtil.getLoggedInUser().getId());
            depositedBranchCondition = "and receiptMisc.depositedBranch.id=" + branchUserMap.getBankbranch().getId();
        } else {
            depositedBranchCondition = " and receiptMisc.depositedBranch is null ";
            if (remittanceSpec.getApproverIdArray().length > 0) {
                String approverIdListStr = Strings.join(
                        Arrays.stream(remittanceSpec.getApproverIdArray())
                                .filter(idString -> StringUtils.isNotEmpty(idString)).iterator(),
                        ',');
                receiptApproverCondition = " and receipt.lastModifiedBy in (" + approverIdListStr + ") ";
            }
        }

        final Query cashInHand = persistenceService.getSession().createNativeQuery(cashInHandQueryString);

        String cashInHandGLCode = null;
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
        final EgwStatus instrumentStatusReconciled = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_RECONCILED_STATUS);

        if (!cashInHand.list().isEmpty())
            cashInHandGLCode = cashInHand.list().get(0).toString();
        collectionsUtil.getVoucherType();
        Boolean showRemitDate = false;
        BigDecimal totalCashAmt = BigDecimal.ZERO;
        BigDecimal totalCashVoucherAmt = BigDecimal.ZERO;
        String fundCode = "";
        Date voucherDate = null;
        List<InstrumentHeader> instrumentHeaderListCash;
        if (collectionsUtil
                .getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_SHOWREMITDATE)
                .equals(CollectionConstants.YES))
            showRemitDate = true;

        final Bankaccount depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?1",
                Long.valueOf(remittanceSpec.getAccountNumberId().longValue()));
        final String serviceGlCode = depositedBankAccount.getChartofaccounts().getGlcode();
        for (int i = 0; i < remittanceSpec.getServiceNameArray().length; i++) {
            final String serviceName = remittanceSpec.getServiceNameArray()[i].trim();
            if (showRemitDate && remittanceSpec.getRemittanceDate() != null)
                voucherDate = remittanceSpec.getRemittanceDate();
            else
                try {
                    collectionsUtil.getRemittanceVoucherDate(dateFomatter.parse(remittanceSpec.getReceiptDateArray()[i]));
                } catch (final ParseException e) {
                    LOGGER.error("Error Parsing Date", e);
                }
            if (serviceName != null && serviceName.length() > 0) {
                final ServiceDetails serviceDetails = (ServiceDetails) persistenceService
                        .findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);

                // If Cash Amount is present
                if (remittanceSpec.getTotalAmount()[i].trim() != null
                        && remittanceSpec.getTotalAmount()[i].trim().length() > 0
                        && cashInHandGLCode != null) {
                    final StringBuilder cashQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    cashQueryBuilder.append(serviceNameCondition);
                    cashQueryBuilder.append(receiptDateCondition);
                    cashQueryBuilder.append(instrumentStatusCondition);
                    cashQueryBuilder.append(instrumentTypeCondition);
                    cashQueryBuilder.append(receiptFundCondition);
                    cashQueryBuilder.append(receiptDepartmentCondition);
                    cashQueryBuilder.append(
                            "and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=:moduletype and code=:receiptstatus) ");
                    cashQueryBuilder.append(receiptSourceCondition);
                    cashQueryBuilder.append(depositedBranchCondition);
                    cashQueryBuilder.append(receiptApproverCondition);

                    Query query = persistenceService.getSession().createQuery(cashQueryBuilder.toString());
                    query.setParameter("servicename", serviceName);
                    try {
                        query.setParameter("receiptdate", dateFomatter.parse(remittanceSpec.getReceiptDateArray()[i]));
                    } catch (final ParseException exp) {
                        LOGGER.debug("Exception in parsing date  " + remittanceSpec.getReceiptDateArray()[i] + " - "
                                + exp.getMessage());
                        throw new ApplicationRuntimeException("Exception while parsing date", exp);
                    }
                    query.setParameter("instrstatusid", instrmentStatusNew.getId());
                    query.setParameter("instrtype", CollectionConstants.INSTRUMENTTYPE_CASH);
                    query.setParameter("fundcode", remittanceSpec.getFundCodeArray()[i]);
                    query.setParameter("departmetcode", remittanceSpec.getDepartmentCodeArray()[i]);
                    query.setParameter("moduletype", CollectionConstants.MODULE_NAME_RECEIPTHEADER);
                    query.setParameter("receiptstatus", CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
                    query.setParameter("source", Source.SYSTEM.toString());
                    fundCode = remittanceSpec.getFundCodeArray()[i];
                    instrumentHeaderListCash = query.getResultList();
                    totalCashAmt = totalCashAmt.add(new BigDecimal(remittanceSpec.getTotalAmount()[i]));
                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation())
                        totalCashVoucherAmt = totalCashVoucherAmt.add(new BigDecimal(remittanceSpec.getTotalAmount()[i]));
                    else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCash, instrumentStatusReconciled,
                                depositedBankAccount);
                    bankRemittanceList.addAll(getRemittanceList(serviceDetails, instrumentHeaderListCash));
                }
            }
            for (final ReceiptHeader receiptHeader : bankRemittanceList)
                if (!bankRemitList.contains(receiptHeader))
                    bankRemitList.add(receiptHeader);
        }
        if (totalCashVoucherAmt.compareTo(totalCashAmt) != 0) {
            String validationMessage = "There is a difference of amount " + totalCashAmt.subtract(totalCashVoucherAmt)
                    + " between bank challan and the remittance voucher , please contact system administrator ";
            throw new ValidationException(Arrays.asList(new ValidationError(validationMessage, validationMessage)));
        }
        final Remittance remittance = populateAndPersistRemittance(totalCashAmt, BigDecimal.ZERO, fundCode,
                cashInHandGLCode, null, serviceGlCode, functionCode, bankRemitList, createVoucher, voucherDate,
                depositedBankAccount, totalCashVoucherAmt, BigDecimal.ZERO, Collections.EMPTY_LIST);

        for (final ReceiptHeader receiptHeader : bankRemitList) {
            receiptHeader.setStatus(receiptStatusRemitted);
            receiptHeader.setRemittanceReferenceNumber(remittance.getReferenceNumber());
            receiptHeaderService.update(receiptHeader);
            // receiptHeaderService.updateCollectionIndexAndPushMail(receiptHeader);
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

        final Query query = getCurrentSession()
                .createQuery(
                        "select rh from org.egov.collection.entity.ReceiptHeader rh left join rh.receiptInstrument as ri where  rh.service.code=:serviceCode and ri.id in (:instrumentHeaderIdList)");
        query.setParameter("serviceCode", serviceDetails.getCode(), StringType.INSTANCE);
        query.setParameterList("instrumentHeaderIdList", instHeaderList);
        bankRemittanceList.addAll(query.list());
        return bankRemittanceList;
    }

    @Transactional
    public Remittance populateAndPersistRemittance(final BigDecimal totalCashAmount, final BigDecimal totalChequeAmount,
            final String fundCode, final String cashInHandGLCode, final String chequeInHandGLcode,
            final String serviceGLCode, final String functionCode, final List<ReceiptHeader> receiptHeadList,
            final String createVoucher, final Date voucherDate, final Bankaccount depositedBankAccount,
            final BigDecimal totalCashVoucherAmt, final BigDecimal totalChequeVoucherAmt, List<String> instrumentId) {
        CVoucherHeader voucherHeader;
        final CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(new Date());
        BigDecimal totalAmount = BigDecimal.ZERO;
        final Remittance remittance = new Remittance();
        final List<RemittanceDetail> remittanceDetailsList = new ArrayList<>();
        Boolean isChequeAmount = Boolean.FALSE;
        remittance.setReferenceDate(voucherDate);
        final EgwStatus receiptStatusApproved = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_REMITTANCE, CollectionConstants.REMITTANCE_STATUS_CODE_APPROVED);
        remittance.setStatus(receiptStatusApproved);
        remittance.setReferenceNumber(collectionsNumberGenerator.generateRemittanceNumber(financialYear));
        remittance.setFund(fundHibernateDAO.fundByCode(fundCode));
        remittance.setFunction(functionHibernateDAO.getFunctionByCode(functionCode));
        remittance.setCollectionRemittance(new HashSet<>(receiptHeadList));
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
            isChequeAmount = Boolean.TRUE;
        }
        remittanceDetailsList.addAll(getRemittanceDetailsList(BigDecimal.ZERO, totalAmount, serviceGLCode, remittance));
        remittance.setRemittanceDetails(new HashSet<>(remittanceDetailsList));
        HashSet<RemittanceInstrument> remittanceInstrumentSet = new HashSet<>();
        remittancePersistService.persist(remittance);
        if (CollectionConstants.YES.equalsIgnoreCase(createVoucher)
                && (totalCashVoucherAmt.compareTo(BigDecimal.ZERO) > 0
                        || totalChequeVoucherAmt.compareTo(BigDecimal.ZERO) > 0)) {
            voucherHeader = createVoucherForRemittance(cashInHandGLCode, chequeInHandGLcode, serviceGLCode,
                    functionCode, totalCashVoucherAmt, totalChequeVoucherAmt, voucherDate, fundCode);
            remittance.setVoucherHeader(voucherHeader);
            for (ReceiptHeader receiptHeader : receiptHeadList)
                for (InstrumentHeader instHead : receiptHeader.getReceiptInstrument())
                    if (!isChequeAmount || isChequeAmount && instrumentId.contains(instHead.getId().toString()))
                        remittanceInstrumentSet.add(prepareRemittanceInstrument(remittance, instHead));
            remittance.addRemittanceInstruments(remittanceInstrumentSet);
            remittancePersistService.persist(remittance);
        }
        return remittance;
    }

    private RemittanceInstrument prepareRemittanceInstrument(Remittance remittance, InstrumentHeader instrumentHeader) {
        RemittanceInstrument remittanceInstrument = new RemittanceInstrument();
        remittanceInstrument.setRemittance(remittance);
        remittanceInstrument.setInstrumentHeader(instrumentHeader);
        remittanceInstrument.setReconciled(Boolean.FALSE);
        remittanceInstrument = (RemittanceInstrument) persistenceService.persist(remittanceInstrument);
        return remittanceInstrument;
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
     * Method to find all the Cash instruments with status as :new and
     *
     * @return List of HashMap
     */
    @Override
    public List<HashMap<String, Object>> findCashRemittanceDetailsForServiceAndFund(final String approverIdList,
            final String boundaryIdList, final String serviceCodes, final String fundCodes, final Date startDate,
            final Date endDate) {

        final List<HashMap<String, Object>> paramList = new ArrayList<>();
        final String whereClauseBeforInstumentType = " WHERE us.id = ch.lastmodifiedby AND ch.id = cm.collectionheader AND "
                + "fnd.id=cm.fund AND dpt.id=cm.department and ci.INSTRUMENTHEADER=ih.ID and "
                + "ch.SERVICEDETAILS=sd.ID and ch.ID=ci.COLLECTIONHEADER and ih.INSTRUMENTTYPE=it.ID and ";

        final String whereClauseForServiceAndFund = " sd.code in (" + serviceCodes + ")" + " and fnd.code in ("
                + fundCodes + ")" + " and ";

        StringBuilder whereClause = new StringBuilder(" AND ih.ID_STATUS=(select id from egw_status where moduletype='")
                .append(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER).append("' " + "and description='")
                .append(CollectionConstants.INSTRUMENT_NEW_STATUS)
                .append("') and ih.ISPAYCHEQUE='0' and ch.STATUS=(select id from egw_status where moduletype='")
                .append(CollectionConstants.MODULE_NAME_RECEIPTHEADER).append("' and code='")
                .append(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED).append("') AND ch.source='")
                .append(Source.SYSTEM).append("' ");
        if (startDate != null && endDate != null)
            whereClause.append(" AND date(ch.receiptdate) between '").append(startDate).append("' and '")
                    .append(endDate).append("' ");

        String groupByClause = " group by date(ch.RECEIPTDATE),sd.NAME,it.TYPE,fnd.name,dpt.name,fnd.code,dpt.code,us.id,us.name ";
        final String orderBy = " order by RECEIPTDATE";

        /**
         * Query to get the collection of the instrument types Cash for bank remittance
         */
        final StringBuilder queryStringForCash = new StringBuilder(
                "SELECT sum(ih.instrumentamount) as INSTRUMENTMAOUNT,date(ch.RECEIPTDATE) AS RECEIPTDATE,")
                        .append("sd.NAME as SERVICENAME,it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.name AS DEPARTMENTNAME,")
                        .append("fnd.code AS FUNDCODE,dpt.code AS DEPARTMENTCODE, us.name AS APPROVERNAME, us.id AS APPROVERID from EGCL_COLLECTIONHEADER ch,")
                        .append("EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EGCL_SERVICEDETAILS sd,")
                        .append("EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt, eg_user us")
                        .append(whereClauseBeforInstumentType + whereClauseForServiceAndFund + "it.TYPE in ('")
                        .append(CollectionConstants.INSTRUMENTTYPE_CASH + "')");

        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser())) {
            BranchUserMap branchUserMap = branchUserMapService.findByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_BRANCHUSER_BY_USER, collectionsUtil.getLoggedInUser().getId());
            queryStringForCash.append(whereClause + " AND cm.depositedbranch=" + branchUserMap.getBankbranch().getId());
        } else {
            groupByClause += ",ch.lastmodifiedby ";
            // no Mapping
            if (approverIdList.isEmpty()) {
                queryStringForCash.append(whereClause).append(" AND cm.depositedbranch is null")
                        .append(" AND ch.CREATEDBY in ")
                        .append(" (select distinct ujl.employee from egeis_jurisdiction ujl where ujl.boundary in (")
                        .append(boundaryIdList).append(")) ");

            } else {
                // Has Mapping, wither user selected one approver or we have a
                // comma separated list of approver id's
                queryStringForCash.append(whereClause).append(" AND ch.lastmodifiedby in (").append(approverIdList)
                        .append(") ");
            }
        }

        queryStringForCash.append(groupByClause);

        final Query query = receiptHeaderService.getSession()
                .createNativeQuery(queryStringForCash.toString() + orderBy);

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
                objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_APPROVERNAME, arrayObjectInitialIndex[8]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_APPROVERID, arrayObjectInitialIndex[9]);

            } else {
                final int checknew = receiptHeaderService.checkIfMapObjectExist(paramList, arrayObjectInitialIndex);
                if (checknew == -1) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_APPROVERNAME, arrayObjectInitialIndex[8]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_APPROVERID, arrayObjectInitialIndex[9]);
                } else {
                    objHashMap = paramList.get(checknew);
                    paramList.remove(checknew);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT,
                            arrayObjectInitialIndex[0]);
                }
            }
            if (objHashMap.get(CollectionConstants.BANKREMITTANCE_RECEIPTDATE) != null
                    && objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICENAME) != null)
                paramList.add(objHashMap);
        }
        return paramList;
    }

    /**
     * Method to find all the Cheque and DD type instruments with status as :new and
     *
     * @return List of HashMap
     */
    @Override
    public List<HashMap<String, Object>> findChequeRemittanceDetailsForServiceAndFund(final String approverIdList,
            final String boundaryIdList, final String serviceCodes, final String fundCodes, final Date startDate,
            final Date endDate) {

        final List<HashMap<String, Object>> paramList = new ArrayList<>();
        final StringBuilder chequeRemittanceListQuery = new StringBuilder(
                "SELECT ih.instrumentamount as INSTRUMENTMAOUNT,date(ch.RECEIPTDATE) AS RECEIPTDATE,")
                        .append(" ch.RECEIPTNUMBER AS RECEIPTNUMBER,ih.INSTRUMENTNUMBER AS INSTRUMENTNUMBER,ih.INSTRUMENTDATE as INSTRUMENTDATE,sd.NAME as SERVICENAME, ")
                        .append("it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.name AS DEPARTMENTNAME,")
                        .append("fnd.code AS FUNDCODE,dpt.code AS DEPARTMENTCODE,ih.ID as INSTRUMENTID,ih.BANKBRANCHNAME as bankbranchname,bank.NAME as bankname,us.name AS APPROVERNAME")
                        .append(" from EGCL_COLLECTIONHEADER ch,EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EGCL_SERVICEDETAILS sd, eg_user us,")
                        .append("EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt, BANK bank  where ch.id=cm.collectionheader AND ")
                        .append("fnd.id=cm.fund AND dpt.id=cm.department and ci.INSTRUMENTHEADER=ih.ID and ")
                        .append("ch.SERVICEDETAILS=sd.ID and ch.ID=ci.COLLECTIONHEADER and ih.INSTRUMENTTYPE=it.ID and ih.BANKID=bank.ID AND")
                        .append(" us.id = ch.lastmodifiedby AND")
                        .append(" sd.code in (" + serviceCodes + ")" + " and fnd.code in (")
                        .append(fundCodes + ")" + " and  it.TYPE in ('" + CollectionConstants.INSTRUMENTTYPE_CHEQUE
                                + "','")
                        .append(CollectionConstants.INSTRUMENTTYPE_DD)
                        .append("')  AND ih.ID_STATUS=(select id from egw_status where moduletype='")
                        .append(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER).append("' ")
                        .append("and description='").append(CollectionConstants.INSTRUMENT_NEW_STATUS)
                        .append("') and ih.ISPAYCHEQUE='0' and ch.STATUS in(select id from egw_status where moduletype='")
                        .append(CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code in('")
                        .append(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "','")
                        .append(CollectionConstants.RECEIPT_STATUS_CODE_PARTIAL_REMITTED)
                        .append("')) " + " AND ch.source='" + Source.SYSTEM + "' ");

        if (startDate != null && endDate != null)
            chequeRemittanceListQuery
                    .append(" AND date(ch.receiptdate) between '" + startDate + "' and '" + endDate + "' ");
        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser())) {
            BranchUserMap branchUserMap = branchUserMapService.findByNamedQuery(
                    CollectionConstants.QUERY_ACTIVE_BRANCHUSER_BY_USER, collectionsUtil.getLoggedInUser().getId());
            chequeRemittanceListQuery.append(" AND cm.depositedbranch=" + branchUserMap.getBankbranch().getId());
        } else
            chequeRemittanceListQuery.append(" AND ch.lastmodifiedby in (" + approverIdList + ")");
        chequeRemittanceListQuery.append(" order by RECEIPTDATE,bankname ");
        final Query query = receiptHeaderService.getSession().createNativeQuery(chequeRemittanceListQuery.toString());

        final List<Object[]> queryResults = query.list();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            HashMap<String, Object> objHashMap = new HashMap<>(0);
            if (i == 0) {
                objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, arrayObjectInitialIndex[0]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTNUMBER, arrayObjectInitialIndex[2]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_CHEQUEDD_NUMBER, arrayObjectInitialIndex[3]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_CHEQUEDD_DATE,
                        dateFormat.format(arrayObjectInitialIndex[4]));
                objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[5]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[7]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[8]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[9]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[10]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_INSTRUMENTID, arrayObjectInitialIndex[11]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DRAWEE_BANKBRANCH, arrayObjectInitialIndex[12]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_DRAWEE_BANK, arrayObjectInitialIndex[13]);
                objHashMap.put(CollectionConstants.BANKREMITTANCE_APPROVERNAME, arrayObjectInitialIndex[14]);
            } else {
                final int checknew = receiptHeaderService.checkIfChequeMapObjectExist(paramList,
                        arrayObjectInitialIndex);
                if (checknew == -1) {
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
                            arrayObjectInitialIndex[0]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTDATE, arrayObjectInitialIndex[1]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_RECEIPTNUMBER, arrayObjectInitialIndex[2]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_CHEQUEDD_NUMBER, arrayObjectInitialIndex[3]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_CHEQUEDD_DATE,
                            dateFormat.format(arrayObjectInitialIndex[4]));
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[5]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[7]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[8]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[9]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[10]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_INSTRUMENTID, arrayObjectInitialIndex[11]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DRAWEE_BANKBRANCH, arrayObjectInitialIndex[12]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_DRAWEE_BANK, arrayObjectInitialIndex[13]);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_APPROVERNAME, arrayObjectInitialIndex[14]);
                } else {
                    objHashMap = paramList.get(checknew);
                    paramList.remove(checknew);
                    objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT,
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

    @Transactional
    @Override
    public List<ReceiptHeader> createChequeBankRemittance(RemittanceSpec remittanceSpec) {
        final List<ReceiptHeader> bankRemittanceList = new ArrayList<>(0);
        final List<ReceiptHeader> bankRemitList = new ArrayList<>();
        final SimpleDateFormat dateFomatter = new SimpleDateFormat(CollectionConstants.DATE_FORMAT_YYYYMMDD, Locale.getDefault());
        final String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.collection.entity.ReceiptHeader receipt "
                + "join receipt.receiptInstrument as instruments join receipt.receiptMisc as receiptMisc where instruments.id=?1";

        StringBuilder chequeInHandQuery = new StringBuilder(
                "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT ")
                        .append("WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE='")
                        .append(CollectionConstants.INSTRUMENTTYPE_CHEQUE).append("'");
        final Query chequeInHand = persistenceService.getSession().createSQLQuery(chequeInHandQuery.toString());
        String chequeInHandGlcode = null;
        if (!chequeInHand.list().isEmpty())
            chequeInHandGlcode = chequeInHand.list().get(0).toString();

        final String createVoucher = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_CREATEVOUCHER_FOR_REMITTANCE);
        final String functionCode = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_FUNCTIONCODE);
        final EgwStatus instrmentStatusNew = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_NEW_STATUS);
        final EgwStatus instrumentStatusDeposited = collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);
        final EgwStatus receiptStatusRemitted = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_REMITTED);
        final EgwStatus receiptStatusPartialRemitted = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_PARTIAL_REMITTED);

        Boolean showRemitDate = false;
        BigDecimal totalChequeAmount = BigDecimal.ZERO;
        BigDecimal totalChequeVoucherAmt = BigDecimal.ZERO;
        String fundCode = CollectionConstants.BLANK;
        Date voucherDate = null;
        List<InstrumentHeader> instrumentHeaderListCheque;
        if (collectionsUtil
                .getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_SHOWREMITDATE)
                .equals(CollectionConstants.YES))
            showRemitDate = true;

        final Bankaccount depositedBankAccount = (Bankaccount) persistenceService.find("from Bankaccount where id=?1",
                Long.valueOf(remittanceSpec.getAccountNumberId().longValue()));
        final String serviceGlCode = depositedBankAccount.getChartofaccounts().getGlcode();

        Boolean voucherTypeForChequeDDCard = false;
        if (collectionsUtil
                .getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                        CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD)
                .equals(CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))
            voucherTypeForChequeDDCard = true;
        final Map<String, Object> instrumentDepositMap = financialsUtil.prepareForUpdateInstrumentDepositSQL();
        for (int i = 0; i < remittanceSpec.getServiceNameArray().length; i++) {
            final String serviceName = remittanceSpec.getServiceNameArray()[i].trim();
            if (serviceName != null && serviceName.length() > 0) {
                if (showRemitDate && remittanceSpec.getRemittanceDate() != null)
                    voucherDate = remittanceSpec.getRemittanceDate();
                else
                    try {
                        collectionsUtil.getRemittanceVoucherDate(dateFomatter.parse(remittanceSpec.getReceiptDateArray()[i]));
                    } catch (final ParseException e) {
                        LOGGER.error("Error Parsing Date", e);
                    }
                final ServiceDetails serviceDetails = (ServiceDetails) persistenceService
                        .findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);

                // If Cheque Amount is present
                if (remittanceSpec.getTotalAmount()[i].trim() != null
                        && remittanceSpec.getTotalAmount()[i].trim().length() > 0
                        && chequeInHandGlcode != null) {
                    final StringBuilder chequeQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
                    /*
                     * final Object arguments[] = new Object[1]; arguments[0] =
                     * Long.valueOf(remittanceDetails.getInstrumentIdArray()[i]);
                     */
                    fundCode = remittanceSpec.getFundCodeArray()[i];
                    instrumentHeaderListCheque = persistenceService.findAllBy(chequeQueryBuilder.toString(),
                            Long.valueOf(remittanceSpec.getInstrumentIdArray()[i]));
                    totalChequeAmount = totalChequeAmount.add(new BigDecimal(remittanceSpec.getTotalAmount()[i]));
                    if (CollectionConstants.YES.equalsIgnoreCase(createVoucher) && serviceDetails.getVoucherCreation())
                        totalChequeVoucherAmt = totalChequeVoucherAmt.add(new BigDecimal(remittanceSpec.getTotalAmount()[i]));
                    else
                        financialsUtil.updateInstrumentHeader(instrumentHeaderListCheque, instrumentStatusDeposited,
                                depositedBankAccount);
                    bankRemittanceList.addAll(getRemittanceList(serviceDetails, instrumentHeaderListCheque));
                }
                for (final ReceiptHeader receiptHeader : bankRemittanceList)
                    if (!bankRemitList.contains(receiptHeader))
                        bankRemitList.add(receiptHeader);
            }
        }
        if (totalChequeVoucherAmt.compareTo(totalChequeAmount) != 0) {
            String validationMessage = "There is a difference of amount "
                    + totalChequeAmount.subtract(totalChequeVoucherAmt)
                    + " between bank challan and the remittance voucher , please contact system administrator ";
            throw new ValidationException(Arrays.asList(new ValidationError(validationMessage, validationMessage)));
        }
        List<String> instrumentIdList = Arrays.asList(remittanceSpec.getInstrumentIdArray());
        final Remittance remittance = populateAndPersistRemittance(BigDecimal.ZERO, totalChequeAmount, fundCode, null,
                chequeInHandGlcode, serviceGlCode, functionCode, bankRemitList, createVoucher, voucherDate,
                depositedBankAccount, BigDecimal.ZERO, totalChequeVoucherAmt, instrumentIdList);

        // For cheque update instrument status to deposited.
        for (final RemittanceInstrument bankRemitInstrument : remittance.getRemittanceInstruments()) {
            final Map<String, Object> chequeMap = remittanceSchedulerService.constructInstrumentMap(
                    instrumentDepositMap, depositedBankAccount, bankRemitInstrument.getInstrumentHeader(),
                    remittance.getVoucherHeader());
            if (voucherTypeForChequeDDCard)
                financialsUtil.updateCheque_DD_Card_Deposit_Receipt(chequeMap);
            else
                financialsUtil.updateCheque_DD_Card_Deposit(chequeMap, remittance.getVoucherHeader(),
                        bankRemitInstrument.getInstrumentHeader(), depositedBankAccount);
            bankRemitInstrument.setReconciled(Boolean.TRUE);
            remittanceInstrumentService.persist(bankRemitInstrument);
        }

        for (final ReceiptHeader receiptHeader : bankRemitList) {

            if (receiptHeader.getReceiptInstrument().size() == 1)
                receiptHeader.setStatus(receiptStatusRemitted);
            else {

                boolean allInstrumentsRemitted = Boolean.TRUE;
                for (InstrumentHeader instrumentHead : receiptHeader.getReceiptInstrument())
                    if (!instrumentIdList.contains(instrumentHead.getId().toString())
                            && instrumentHead.getStatusId().getCode().equals(instrmentStatusNew.getCode()))
                        allInstrumentsRemitted = Boolean.FALSE;
                if (allInstrumentsRemitted)
                    receiptHeader.setStatus(receiptStatusRemitted);
                else
                    receiptHeader.setStatus(receiptStatusPartialRemitted);// check receipt has multiple instruments and if few
                // instrument deposited few are not deposite then set
                // receipt status as partial remitted.
            }
            receiptHeader.setRemittanceReferenceNumber(remittance.getReferenceNumber());
            receiptHeaderService.update(receiptHeader);
            receiptHeaderService.updateCollectionIndexAndPushMail(receiptHeader);
        }
        return bankRemitList;
    }

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    public List<String> getServiceCodeList(Integer accountNumberId) {
        final Query serviceQuery = persistenceService.getSession().createSQLQuery(SERVICE_QUERY);
        serviceQuery.setLong("accountNumberId", accountNumberId);
        return serviceQuery.list();
    }

    public String getFundCodeList(Integer accountNumberId) {
        final Query fundQuery = persistenceService.getSession().createSQLQuery(FUND_QUERY);
        fundQuery.setLong("accountNumberId", accountNumberId);
        return fundQuery.list().get(0).toString();
    }

/*    public String getAccountNumberByBankAccountId(Integer accountNumberId) {
        String accountNumber = CollectionConstants.BLANK;
        if (accountNumberId != null) {
            final Query bankAccountQry = persistenceService.getSession().createSQLQuery(BANK_ACCOUNT_NUMBER_QUERY);
            bankAccountQry.setLong("accountNumberId", accountNumberId);
            final Object bankAccountResult = bankAccountQry.uniqueResult();
            accountNumber = (String) bankAccountResult;
        }
        return accountNumber;
    }*/

    public Boolean showRemittanceDate() {
        final String showRemitDate = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTION_BANKREMITTANCE_SHOWREMITDATE);
        if (!showRemitDate.isEmpty() && showRemitDate.equals(CollectionConstants.YES))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    
    public String[] removeNullValue(String[] receiptDateArray) {
        final List<String> list = new ArrayList<>();
        for (final String s : receiptDateArray)
            if (s != null && s.length() > 0)
                list.add(s);
        return list.toArray(new String[list.size()]);
    }
    
    public Double getSum(final String[] array) {
        Double sum = 0.0;
        for (final String num : array)
            if (!num.isEmpty())
                sum = sum + Double.valueOf(num);
        return sum;
    }


}
