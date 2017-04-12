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
package org.egov.collection.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.BranchUserMap;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.models.BillAccountDetails;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillDetails;
import org.egov.collection.integration.models.BillInfo;
import org.egov.collection.integration.models.BillPayeeDetails;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.PaymentInfoBank;
import org.egov.collection.integration.models.PaymentInfoCash;
import org.egov.collection.integration.models.PaymentInfoChequeDD;
import org.egov.collection.integration.pgi.PaymentGatewayAdaptor;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.MoneyUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class CollectionCommon {

    private static final Logger LOGGER = Logger.getLogger(CollectionCommon.class);

    protected PersistenceService persistenceService;
    private ReceiptHeaderService receiptHeaderService;

    @Autowired
    private BoundaryService boundaryService;
    private EgovCommon egovCommon;
    private CollectionsUtil collectionsUtil;
    private FinancialsUtil financialsUtil;
    @Autowired
    private FundSourceHibernateDAO fundSourceDAO;
    @Autowired
    private FunctionHibernateDAO functionDAO;
    @Autowired
    private BankHibernateDAO bankDAO;
    @Autowired
    private BankaccountHibernateDAO bankAccountDAO;
    @Autowired
    private EgwStatusHibernateDAO statusDAO;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    @Autowired
    private ReportViewerUtil reportViewerUtil;
    @Autowired
    @Qualifier("branchUserMapService")
    private PersistenceService<BranchUserMap, Long> branchUserMapService;

    /**
     * @param receiptHeaderService
     *            the receipt header Service to be set
     */
    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    /**
     * @param persistenceService
     *            the persistenceService to set
     */
    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * @param collectionsUtil
     *            the collectionsUtil to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @param FinancialsUtil
     *            the FinancialsUtil to set
     */
    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    public ReceiptDetail addDebitAccountHeadDetails(final BigDecimal debitAmount, final ReceiptHeader receiptHeader,
            final BigDecimal chequeInstrumenttotal, final BigDecimal otherInstrumenttotal, final String instrumentType) {

        final ReceiptDetail newReceiptDetail = new ReceiptDetail();
        newReceiptDetail.setPurpose(PURPOSE.OTHERS.toString());
        if (chequeInstrumenttotal.toString() != null
                && !chequeInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_INT)
                && !chequeInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_DOUBLE)) {

            newReceiptDetail.setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE, CollectionConstants.INSTRUMENTTYPE_CHEQUE));

            newReceiptDetail.setDramount(debitAmount);
            newReceiptDetail.setCramount(BigDecimal.valueOf(0));
            newReceiptDetail.setReceiptHeader(receiptHeader);
            newReceiptDetail.setFunction(receiptHeader.getReceiptDetails().iterator().next().getFunction());
        }

        if (otherInstrumenttotal.toString() != null
                && !otherInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_INT)
                && !otherInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_DOUBLE)) {
            if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CASH))
                newReceiptDetail
                .setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE,
                        CollectionConstants.INSTRUMENTTYPE_CASH));
            else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CARD))
                newReceiptDetail
                .setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE,
                        CollectionConstants.INSTRUMENTTYPE_CARD));
            else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_BANK))
                newReceiptDetail.setAccounthead(receiptHeader.getReceiptInstrument().iterator().next()
                        .getBankAccountId().getChartofaccounts());
            else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))
                newReceiptDetail.setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE_SERVICE,
                        CollectionConstants.INSTRUMENTTYPE_ONLINE, receiptHeader.getOnlinePayment().getService()
                        .getId()));
            newReceiptDetail.setDramount(debitAmount);
            newReceiptDetail.setCramount(BigDecimal.ZERO);
            newReceiptDetail.setReceiptHeader(receiptHeader);
            newReceiptDetail.setFunction(receiptHeader.getReceiptDetails().iterator().next().getFunction());
        }
        return newReceiptDetail;
    }

    /**
     * This method initialises the model, a list of
     * <code>ReceiptPayeeDetails</code> objects with the information contained
     * in the unmarshalled <code>BillCollection</code> instance.
     */
    public ReceiptHeader initialiseReceiptModelWithBillInfo(final BillInfo collDetails, final Fund fund,
            final Department dept) throws ValidationException {
        ReceiptHeader receiptHeader = null;

        final StringBuilder collModesNotAllowed = new StringBuilder();
        if (collDetails.getCollectionModesNotAllowed() != null)
            for (final String collModeNotAllwd : collDetails.getCollectionModesNotAllowed()) {
                if (collModesNotAllowed.length() > 0)
                    collModesNotAllowed.append(',');
                collModesNotAllowed.append(collModeNotAllwd);
            }

        for (final BillPayeeDetails billPayee : collDetails.getPayees()) {
            receiptHeader = new ReceiptHeader();
            for (final BillDetails billDetail : billPayee.getBillDetails()) {
                final ServiceDetails service = (ServiceDetails) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_SERVICE_BY_CODE, collDetails.getServiceCode());
                if (service == null)
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            "billreceipt.improperbilldata.missingservice",
                            "billreceipt.improperbilldata.missingservice")));

                receiptHeader = new ReceiptHeader(billDetail.getRefNo(), billDetail.getBilldate(),
                        billDetail.getConsumerCode(), billDetail.getDescription(), billDetail.getTotalAmount(),
                        billDetail.getMinimumAmount(), collDetails.getPartPaymentAllowed(),
                        collDetails.getOverrideAccountHeadsAllowed(), collDetails.getCallbackForApportioning(),
                        collDetails.getDisplayMessage(), service, collModesNotAllowed.toString(),
                        billPayee.getPayeeName(), billPayee.getPayeeAddress(), billPayee.getPayeeEmail(),
                        billDetail.getConsumerType());

                if (collDetails.getTransactionReferenceNumber() != null) {
                    receiptHeader.setManualreceiptnumber(collDetails.getTransactionReferenceNumber());
                    receiptHeader.setManualreceiptdate(new Date());
                }

                final Boundary boundary = boundaryService.getActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCode(
                        Long.valueOf(billDetail.getBoundaryNum()), billDetail.getBoundaryType(),
                        CollectionConstants.BOUNDARY_HIER_CODE_ADMIN);

                final Functionary functionary = (Functionary) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_FUNCTIONARY_BY_CODE, collDetails.getFunctionaryCode());
                final Fundsource fundSource = fundSourceDAO.getFundSourceByCode(collDetails.getFundSourceCode());

                // For Bank Collection Operator set branchdeposited from
                // branchuser map
                final User loggedInUser = collectionsUtil.getLoggedInUser();
                Bankbranch bankBranch = null;
                if (collectionsUtil.isBankCollectionOperator(loggedInUser)) {
                    final BranchUserMap branchUserMap = branchUserMapService.findByNamedQuery(
                            CollectionConstants.QUERY_ACTIVE_BRANCHUSER_BY_USER, loggedInUser.getId());
                    if (branchUserMap != null && branchUserMap.getBankbranch() != null)
                        bankBranch = branchUserMap.getBankbranch();
                }
                final ReceiptMisc receiptMisc = new ReceiptMisc(boundary, fund, functionary, fundSource, dept,
                        receiptHeader, null, null, bankBranch);

                receiptHeader.setReceiptMisc(receiptMisc);

                BigDecimal totalAmountToBeCollected = BigDecimal.valueOf(0);

                Collections.sort(billDetail.getAccounts());

                for (final BillAccountDetails billAccount : billDetail.getAccounts()) {
                    final CChartOfAccounts account = chartOfAccountsHibernateDAO
                            .getCChartOfAccountsByGlCode(billAccount.getGlCode());
                    final CFunction function = functionDAO.getFunctionByCode(billAccount.getFunctionCode());
                    if (billAccount.getIsActualDemand())
                        totalAmountToBeCollected = totalAmountToBeCollected.add(billAccount.getCrAmount()).subtract(
                                billAccount.getDrAmount());
                    final ReceiptDetail receiptDetail = new ReceiptDetail(account, function, billAccount.getCrAmount()
                            .subtract(billAccount.getDrAmount()), billAccount.getDrAmount(), billAccount.getCrAmount(),
                            Long.valueOf(billAccount.getOrder()), billAccount.getDescription(),
                            billAccount.getIsActualDemand(), receiptHeader, billAccount.getPurpose().toString());
                    receiptHeader.addReceiptDetail(receiptDetail);
                }
                receiptHeader.setTotalAmountToBeCollected(totalAmountToBeCollected);
            }

        }
        return receiptHeader;
    }

    /**
     * This method returns the payment response object for the given response
     * string.
     *
     * @param paymentServiceDetails
     * @param response
     * @return
     */
    public PaymentResponse createPaymentResponse(final ServiceDetails paymentServiceDetails, final String response) {
        final PaymentGatewayAdaptor paymentGatewayAdaptor = getPaymentGatewayAdaptor(paymentServiceDetails.getCode());
        return paymentGatewayAdaptor.parsePaymentResponse(response);
    }

    /**
     * This method generates a report for the given array of receipts
     *
     * @param receipts
     *            an array of <code>ReceiptHeader</code> objects for which the
     *            report is to be generated
     * @param flag
     *            a boolean value indicating if the generated report should also
     *            have the print option
     * @return an integer representing the report id
     */
    public String generateReport(final ReceiptHeader[] receipts, final boolean flag) {
        final String serviceCode = receipts[0].getService().getCode();
        final char receiptType = receipts[0].getReceipttype();
        final List<BillReceiptInfo> receiptList = new ArrayList<>(0);

        final String templateName = collectionsUtil.getReceiptTemplateName(receiptType, serviceCode);
        LOGGER.info(" template name : " + templateName);
        final Map<String, Object> reportParams = new HashMap<>(0);
        reportParams.put(CollectionConstants.REPORT_PARAM_COLLECTIONS_UTIL, collectionsUtil);

        if (receiptType == CollectionConstants.RECEIPT_TYPE_CHALLAN) {
            reportParams.put(CollectionConstants.REPORT_PARAM_EGOV_COMMON, egovCommon);
            for (final ReceiptHeader receiptHeader : receipts) {
                final ReceiptHeader receipHeaderRefObj = (ReceiptHeader) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHALLANRECEIPT_BY_REFERENCEID, receiptHeader.getId());
                receiptList.add(new BillReceiptInfoImpl(receiptHeader, egovCommon, receipHeaderRefObj,
                        chartOfAccountsHibernateDAO, persistenceService));
            }
        } else
            for (final ReceiptHeader receiptHeader : receipts) {
                String additionalMessage = null;
                if (receiptType == CollectionConstants.RECEIPT_TYPE_BILL
                        && !receiptHeader.getService().getCode().equals(CollectionConstants.SERVICECODE_LAMS))
                    additionalMessage = receiptHeaderService.getAdditionalInfoForReceipt(serviceCode,
                            new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService,
                                    null));
                if (additionalMessage != null)
                    receiptList.add(new BillReceiptInfoImpl(receiptHeader, additionalMessage,
                            chartOfAccountsHibernateDAO, persistenceService));
                else
                    receiptList.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO,
                            persistenceService, null));
            }
        final ReportRequest reportInput = new ReportRequest(templateName, receiptList, reportParams);

        // Set the flag so that print dialog box is automatically opened
        // whenever the PDF is opened
        reportInput.setReportFormat(ReportFormat.PDF);
        reportInput.setPrintDialogOnOpenReport(flag);
        return reportViewerUtil.addReportToTempCache(collectionsUtil.createReport(reportInput));
    }

    /**
     * This method generates a challan for the given receipt
     *
     * @param receipt
     *            <code>ReceiptHeader</code> object for which the report is to
     *            be generated
     * @param flag
     *            a boolean value indicating if the generated challan should
     *            also have the print option
     * @return an integer representing the report id
     */
    public String generateChallan(final ReceiptHeader receipt, final boolean flag) {
        final List<BillReceiptInfo> receiptList = new ArrayList<>(0);
        receiptList.add(new BillReceiptInfoImpl(receipt, egovCommon, new ReceiptHeader(), chartOfAccountsHibernateDAO,
                persistenceService));

        final String templateName = CollectionConstants.CHALLAN_TEMPLATE_NAME;
        final Map<String, Object> reportParams = new HashMap<>(0);
        reportParams.put("EGOV_COMMON", egovCommon);
        final ReportRequest reportInput = new ReportRequest(templateName, receiptList, reportParams);

        // Set the flag so that print dialog box is automatically opened
        // whenever the PDF is opened
        reportInput.setPrintDialogOnOpenReport(flag);
        return reportViewerUtil.addReportToTempCache(collectionsUtil.createReport(reportInput));
    }

    public PaymentRequest createPaymentRequest(final ServiceDetails paymentServiceDetails,
            final ReceiptHeader receiptHeader) {

        final PaymentGatewayAdaptor paymentGatewayAdaptor = getPaymentGatewayAdaptor(paymentServiceDetails.getCode());

        return paymentGatewayAdaptor.createPaymentRequest(paymentServiceDetails, receiptHeader);
    }

    protected PaymentGatewayAdaptor getPaymentGatewayAdaptor(final String serviceCode) {
        return (PaymentGatewayAdaptor) collectionsUtil.getBean(serviceCode
                + CollectionConstants.PAYMENTGATEWAYADAPTOR_INTERFACE_SUFFIX);
    }

    /* *//**
     * @param egovCommon
     *            the egovCommon to set
     */
    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public List<ReceiptDetailInfo> setReceiptDetailsList(final ReceiptHeader rh, final String amountType) {
        // To Load receipt details to billDetailslist
        final List<ReceiptDetailInfo> billDetailslist = new ArrayList<>(0);
        final List<CChartOfAccounts> bankCOAList = chartOfAccountsHibernateDAO.getBankChartofAccountCodeList();
        for (final ReceiptDetail rDetails : rh.getReceiptDetails())
            if (!FinancialsUtil.isRevenueAccountHead(rDetails.getAccounthead(), bankCOAList, persistenceService)) {
                final ReceiptDetailInfo rInfo = new ReceiptDetailInfo();
                rInfo.setGlcodeDetail(rDetails.getAccounthead().getGlcode());
                rInfo.setGlcodeIdDetail(rDetails.getAccounthead().getId());
                rInfo.setCreditAmountDetail(rDetails.getCramount().setScale(
                        CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));
                rInfo.setDebitAmountDetail(rDetails.getDramount().setScale(
                        CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));
                rInfo.setAccounthead(rDetails.getAccounthead().getName());
                rInfo.setAmount(BigDecimal.ZERO);
                if (rDetails.getFinancialYear() != null) {
                    rInfo.setFinancialYearId(rDetails.getFinancialYear().getId());
                    rInfo.setFinancialYearRange(rDetails.getFinancialYear().getFinYearRange());
                }
                if (rDetails.getFunction() != null) {
                    rInfo.setFunctionDetail(rDetails.getFunction().getCode());
                    rInfo.setFunctionIdDetail(rDetails.getFunction().getId());
                }
                if ((amountType.equals(CollectionConstants.COLLECTIONSAMOUNTTPE_CREDIT) || amountType
                        .equals(CollectionConstants.COLLECTIONSAMOUNTTPE_BOTH))
                        && rDetails.getCramount().compareTo(BigDecimal.ZERO) != 0)
                    billDetailslist.add(rInfo);
                else if ((amountType.equals(CollectionConstants.COLLECTIONSAMOUNTTPE_DEBIT) || amountType
                        .equals(CollectionConstants.COLLECTIONSAMOUNTTPE_BOTH))
                        && rDetails.getDramount().compareTo(BigDecimal.ZERO) != 0)
                    billDetailslist.add(rInfo);
            }
        if (billDetailslist.isEmpty())
            billDetailslist.add(new ReceiptDetailInfo());
        return billDetailslist;
    }

    public List<ReceiptDetailInfo> setAccountPayeeList(final ReceiptHeader rh) {
        // To load subledgerlist data to subLedgerlist
        final List<ReceiptDetailInfo> subLedgerlist = new ArrayList<>(0);
        try {

            for (final ReceiptDetail rDetails : rh.getReceiptDetails())
                for (final AccountPayeeDetail aDetail : rDetails.getAccountPayeeDetails()) {
                    final ReceiptDetailInfo rInfo = new ReceiptDetailInfo();
                    rInfo.setAmount(aDetail.getAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
                            BigDecimal.ROUND_UP));
                    rInfo.setCreditAmountDetail(BigDecimal.ZERO);
                    rInfo.setDebitAmountDetail(BigDecimal.ZERO);
                    final EntityType entityType = egovCommon.getEntityType(aDetail.getAccountDetailType(), aDetail
                            .getAccountDetailKey().getDetailkey());
                    if (entityType != null) {
                        rInfo.setDetailCode(entityType.getCode());
                        rInfo.setDetailKey(entityType.getName());
                    }
                    rInfo.setDetailKeyId(aDetail.getAccountDetailKey().getDetailkey());
                    rInfo.setDetailType(aDetail.getAccountDetailType());
                    rInfo.setDetailTypeName(aDetail.getAccountDetailType().getName());
                    rInfo.setGlcode(rDetails.getAccounthead());
                    rInfo.setGlcodeDetail(rDetails.getAccounthead().getGlcode());
                    rInfo.setSubledgerCode("");
                    subLedgerlist.add(rInfo);
                }

        } catch (final Exception e) {
            LOGGER.error("Exception while setting subledger details", e);
            throw new ApplicationRuntimeException("Exception while setting subledger details", e);
        }
        if (subLedgerlist.isEmpty())
            subLedgerlist.add(new ReceiptDetailInfo());
        return subLedgerlist;
    }

    /**
     * This method cancels the receipt against a challan. The reason for
     * cancellation is set and the staus is changed to CANCELLED.
     *
     * @param receiptHeader
     *            the <code>ReceiptHeader</code> which contains a reference to
     *            the receipt to be cancelled.
     */

    public void cancelChallanReceiptOnCreation(final ReceiptHeader receiptHeader) {
        final ReceiptHeader receiptHeaderToBeCancelled = receiptHeaderService.findById(receiptHeader.getReceiptHeader()
                .getId(), false);

        receiptHeaderToBeCancelled.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));

        receiptHeaderService.persist(receiptHeaderToBeCancelled);
    }

    /**
     * This method create a new receipt header object with details contained in
     * given receipt header object. Both the receipt header objects are added to
     * the same parent <code>ReceiptPayeeDetail</code> object .
     *
     * @param oldReceiptHeader
     *            the instance of <code>ReceiptHeader</code> whose data is to be
     *            copied
     */

    public ReceiptHeader createPendingReceiptFromCancelledChallanReceipt(final ReceiptHeader oldReceiptHeader) {

        final ReceiptHeader newReceiptHeader = new ReceiptHeader(true, oldReceiptHeader.getIsModifiable(),
                oldReceiptHeader.getReceipttype(), oldReceiptHeader.getCollectiontype(), oldReceiptHeader.getPaidBy(),
                oldReceiptHeader.getService(), oldReceiptHeader.getReferencenumber(),
                oldReceiptHeader.getReferenceDesc(), oldReceiptHeader.getTotalAmount());
        // receipt status is PENDING
        newReceiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_PENDING));
        // the new receipt has reference to the cancelled receipt
        // newReceiptHeader.setReferenceCollectionHeaderId(oldReceiptHeader.getId());
        newReceiptHeader.setReceiptHeader(oldReceiptHeader);

        final ReceiptMisc receiptMisc = new ReceiptMisc(oldReceiptHeader.getReceiptMisc().getBoundary(),
                oldReceiptHeader.getReceiptMisc().getFund(), null, null, oldReceiptHeader.getReceiptMisc()
                .getDepartment(), newReceiptHeader, null, null, null);
        newReceiptHeader.setReceiptMisc(receiptMisc);
        newReceiptHeader.setReceiptdate(new Date());
        final List<CChartOfAccounts> bankCOAList = chartOfAccountsHibernateDAO.getBankChartofAccountCodeList();

        for (final ReceiptDetail oldDetail : oldReceiptHeader.getReceiptDetails())
            // debit account heads should not be considered
            // This is to omit revenueheadaccounts
            if (!FinancialsUtil.isRevenueAccountHead(oldDetail.getAccounthead(), bankCOAList, persistenceService)) {
                final ReceiptDetail receiptDetail = new ReceiptDetail(oldDetail.getAccounthead(),
                        oldDetail.getFunction(), oldDetail.getCramountToBePaid(), oldDetail.getDramount(),
                        oldDetail.getCramount(), oldDetail.getOrdernumber(), oldDetail.getDescription(),
                        oldDetail.getIsActualDemand(), newReceiptHeader, oldDetail.getPurpose());
                receiptDetail.setCramount(oldDetail.getCramount());
                receiptDetail.setFinancialYear(oldDetail.getFinancialYear());

                for (final AccountPayeeDetail oldAccountPayeeDetail : oldDetail.getAccountPayeeDetails()) {
                    final AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(
                            oldAccountPayeeDetail.getAccountDetailType(), oldAccountPayeeDetail.getAccountDetailKey(),
                            oldAccountPayeeDetail.getAmount(), receiptDetail);
                    receiptDetail.addAccountPayeeDetail(accPayeeDetail);
                }

                newReceiptHeader.addReceiptDetail(receiptDetail);
            }

        if (oldReceiptHeader.getChallan() != null) {
            oldReceiptHeader.getChallan().setReceiptHeader(newReceiptHeader);
            newReceiptHeader.setChallan(oldReceiptHeader.getChallan());
        }
        return newReceiptHeader;
    }

    /**
     * This method cancels the given receipt. The voucher for the instrument is
     * reversed. The instrument may be cancelled based on the input parameter.
     * (For post remittance cancellation of a receipt for a challan which has
     * become invalid, the instrument should not be cancelled)
     *
     * @param receiptHeader
     *            the <code>ReceiptHeader</code> instance which has to be
     *            cancelled
     * @param cancelInstrument
     *            a boolean value indicating if the instrument should be
     *            cancelled
     */
    @Transactional
    public void cancelChallanReceipt(final ReceiptHeader receiptHeader, final boolean cancelInstrument) {
        /**
         * The receipt header to be cancelled is the object retrieved in the
         * prepare method
         */

        receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
        receiptHeader.setIsReconciled(true); // have to check this for
        // scheduler

        if (cancelInstrument)
            for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument())
                instrumentHeader.setStatusId(statusDAO.getStatusByModuleAndCode(
                        CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
                        CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED));

        for (final ReceiptVoucher receiptVoucher : receiptHeader.getReceiptVoucher())
            receiptHeaderService.createReversalVoucher(receiptVoucher);

        // persist the cancelled receipt
        receiptHeaderService.persist(receiptHeader);

        LOGGER.info("Receipt " + receiptHeader.getReceiptnumber() + " has been cancelled");
    }

    /**
     * This method is used to get the financial Year Id for the given date
     *
     * @return the financial year id if exists else 0
     */
    public String getFinancialYearIdByDate(final Date date) {
        CFinancialYear fYear;
        fYear = (CFinancialYear) persistenceService.findByNamedQuery(CollectionConstants.QUERY_GETFINANCIALYEARBYDATE,
                date);
        if (fYear != null)
            return fYear.getId().toString();
        return CollectionConstants.ZERO_INT;
    }

    public InstrumentHeader validateAndConstructCashInstrument(final PaymentInfoCash paytInfoCash) {
        if (paytInfoCash.getInstrumentAmount() == null
                || paytInfoCash.getInstrumentAmount().compareTo(BigDecimal.ZERO) == 0)
            throw new ApplicationRuntimeException("Invalid Cash Instrument Amount["
                    + paytInfoCash.getInstrumentAmount() + "]");

        final InstrumentHeader instrHeaderCash = new InstrumentHeader();
        instrHeaderCash.setInstrumentType(financialsUtil
                .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));
        instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
        instrHeaderCash.setInstrumentAmount(paytInfoCash.getInstrumentAmount());
        return instrHeaderCash;
    }

    /**
     * Checks if the card instrument amount, transaction number, transaction
     * date, bank branch, bank account number are valid
     *
     * @param paytInfoBank
     * @return
     */
    /*
     * public InstrumentHeader
     * validateAndConstructCardInstrument(PaymentInfoCard
     * paytInfoCard,ReceiptHeader receiptHeader) { String invalidCardPaytMsg="";
     * if(paytInfoCard.getInstrumentAmount()==null ||
     * paytInfoCard.getInstrumentAmount().compareTo(BigDecimal.ZERO)<=0){
     * invalidCardPaytMsg+="Invalid Bank Instrument Amount[" +
     * paytInfoCard.getInstrumentAmount() + "] \n"; }
     * if(paytInfoCard.getInstrumentNumber()==null ||
     * CollectionConstants.BLANK.equals(paytInfoCard.getInstrumentNumber()) ||
     * paytInfoCard.getInstrumentNumber().length()<4){
     * invalidCardPaytMsg+="Invalid Card Instrument Number[" +
     * paytInfoCard.getInstrumentNumber() + ". \n"; }
     * if(!(CollectionConstants.BLANK.equals(invalidCardPaytMsg))) throw new
     * ApplicationRuntimeException(invalidCardPaytMsg); //Process Card Payment
     * by invoking BillDesk API MerchantInfo merchantInfo =
     * processCardPayment(paytInfoCard,receiptHeader); InstrumentHeader
     * instrHeaderCard = new InstrumentHeader();
     * if(merchantInfo.getAuthStatus().equals(CollectionConstants.
     * PGI_AUTHORISATION_CODE_SUCCESS)) {
     * instrHeaderCard.setInstrumentType(financialsUtil.getInstrumentTypeByType(
     * CollectionConstants.INSTRUMENTTYPE_CARD));
     * instrHeaderCard.setInstrumentAmount(new
     * BigDecimal(merchantInfo.getTxnAmount()));
     * instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT); //this
     * value has to be captured from bill desk
     * instrHeaderCard.setTransactionNumber(merchantInfo.getTxnReferenceNo());
     * //instrument number is last 4 char of card number
     * instrHeaderCard.setInstrumentNumber(merchantInfo.getCcno().substring(
     * paytInfoCard.getInstrumentNumber().length()-4)); SimpleDateFormat sdf =
     * new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); Date
     * transactionDate = null; try { transactionDate =
     * sdf.parse(merchantInfo.getTxnDate()); } catch (ParseException e) {
     * LOGGER.error("Error occured in parsing the transaction date [" +
     * merchantInfo.getTxnDate() + "]", e); throw new
     * ApplicationRuntimeException("Error in parsing date"); }
     * instrHeaderCard.setTransactionDate(transactionDate); OnlinePayment
     * onlinePayment = new OnlinePayment();
     * onlinePayment.setReceiptHeader(receiptHeader); onlinePayment.setStatus(
     * collectionsUtil.getEgwStatusForModuleAndCode(
     * CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
     * CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS));
     * onlinePayment.setService
     * ((ServiceDetails)persistenceService.findByNamedQuery
     * (CollectionConstants.QUERY_SERVICE_BY_CODE,
     * CollectionConstants.SERVICECODE_PGI_BILLDESK));
     * onlinePayment.setTransactionNumber(merchantInfo.getTxnReferenceNo());
     * onlinePayment.setTransactionAmount(new
     * BigDecimal(merchantInfo.getTxnAmount()));
     * onlinePayment.setTransactionDate(transactionDate);
     * onlinePayment.setAuthorisationStatusCode(merchantInfo.getAuthStatus());
     * receiptHeader.setOnlinePayment(onlinePayment); } return instrHeaderCard;
     * }
     */

    /**
     * Checks if the bank instrument number, transaction number, transaction
     * date, bank branch, bank account number are valid
     *
     * @param paytInfoBank
     * @return
     */
    public InstrumentHeader validateAndConstructBankInstrument(final PaymentInfoBank paytInfoBank) {
        final StringBuilder invalidBankPaytMsg = new StringBuilder();
        if (paytInfoBank.getInstrumentAmount() == null
                || paytInfoBank.getInstrumentAmount().compareTo(BigDecimal.ZERO) <= 0)
            invalidBankPaytMsg.append("Invalid Bank Instrument Amount[" + paytInfoBank.getInstrumentAmount()).append(
                    "] \n");
        if (paytInfoBank.getTransactionNumber() == null || paytInfoBank.getTransactionNumber() < 0
                || String.valueOf(paytInfoBank.getTransactionNumber()).length() != 6)
            invalidBankPaytMsg.append("Invalid Bank Transaction Number[").append(paytInfoBank.getInstrumentAmount())
            .append("] \n");
        if (paytInfoBank.getTransactionDate() == null)
            invalidBankPaytMsg.append("Missing Bank Transaction Date \n");
        if (new Date().compareTo(paytInfoBank.getTransactionDate()) == -1)
            invalidBankPaytMsg.append("Bank Transaction Date[" + paytInfoBank.getTransactionDate()).append(
                    "] cannot be a future date \n");
        Bankaccount account = null;
        if (paytInfoBank.getBankAccountId() == null)
            invalidBankPaytMsg.append("Missing Bank Account Id \n");
        else {
            account = bankAccountDAO.findById(paytInfoBank.getBankAccountId().intValue(), false);

            if (account == null)
                invalidBankPaytMsg.append("No account found for bank account id[" + paytInfoBank.getBankAccountId())
                .append("] \n");
        }

        if (!CollectionConstants.BLANK.equals(invalidBankPaytMsg))
            throw new ApplicationRuntimeException(invalidBankPaytMsg.toString());

        final InstrumentHeader instrHeaderBank = new InstrumentHeader();

        instrHeaderBank.setInstrumentType(financialsUtil
                .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_BANK));

        instrHeaderBank.setBankAccountId(account);
        instrHeaderBank.setBankId(account.getBankbranch().getBank());
        instrHeaderBank.setBankBranchName(account.getBankbranch().getBranchname());
        instrHeaderBank.setTransactionNumber(String.valueOf(paytInfoBank.getTransactionNumber()));
        instrHeaderBank.setInstrumentAmount(paytInfoBank.getInstrumentAmount());
        instrHeaderBank.setTransactionDate(paytInfoBank.getTransactionDate());
        instrHeaderBank.setIsPayCheque(CollectionConstants.ZERO_INT);

        return instrHeaderBank;
    }

    /**
     * Checks if the cheque/DD instrument number, instrument date, are valid. An
     * exception is thrown if the payment details are invalid, else an
     * InstrumentHeader object is created from the payment details, and
     * returned.
     *
     * @param paytInfoBank
     * @return
     */
    public InstrumentHeader validateAndConstructChequeDDInstrument(final PaymentInfoChequeDD paytInfoChequeDD) {
        final StringBuilder invalidChequeDDPaytMsg = new StringBuilder();
        if (paytInfoChequeDD.getInstrumentAmount() == null
                || paytInfoChequeDD.getInstrumentAmount().compareTo(BigDecimal.ZERO) <= 0)
            invalidChequeDDPaytMsg.append("Invalid cheque/DD Instrument Amount[")
            .append(paytInfoChequeDD.getInstrumentAmount()).append("] \n");
        if (paytInfoChequeDD.getInstrumentNumber() == null
                || CollectionConstants.BLANK.equals(paytInfoChequeDD.getInstrumentNumber())
                || !MoneyUtils.isInteger(paytInfoChequeDD.getInstrumentNumber())
                || paytInfoChequeDD.getInstrumentNumber().length() != 6)
            invalidChequeDDPaytMsg.append("Invalid Cheque/DD Instrument Number[")
            .append(paytInfoChequeDD.getInstrumentNumber()).append("]. \n");
        if (paytInfoChequeDD.getInstrumentDate() == null)
            invalidChequeDDPaytMsg.append("Missing Cheque/DD Transaction Date \n");
        if (new Date().compareTo(paytInfoChequeDD.getInstrumentDate()) == -1)
            invalidChequeDDPaytMsg.append("Cheque/DD Transaction Date[").append(paytInfoChequeDD.getInstrumentDate())
            .append("] cannot be a future date \n");
        Bank bank = null;
        if (paytInfoChequeDD.getBankId() != null) {
            bank = bankDAO.findById(paytInfoChequeDD.getBankId().intValue(), false);
            if (bank == null)
                invalidChequeDDPaytMsg.append("No bank present for bank id [").append(paytInfoChequeDD.getBankId())
                .append("] \n");
        }

        if (!invalidChequeDDPaytMsg.toString().isEmpty())
            throw new ApplicationRuntimeException(invalidChequeDDPaytMsg.toString());

        final InstrumentHeader instrHeaderChequeDD = new InstrumentHeader();

        instrHeaderChequeDD.setIsPayCheque(CollectionConstants.ZERO_INT);
        instrHeaderChequeDD.setInstrumentAmount(paytInfoChequeDD.getInstrumentAmount());
        instrHeaderChequeDD.setInstrumentType(financialsUtil.getInstrumentTypeByType(paytInfoChequeDD
                .getInstrumentType().toString()));

        instrHeaderChequeDD.setInstrumentNumber(String.valueOf(paytInfoChequeDD.getInstrumentNumber()));
        instrHeaderChequeDD.setBankBranchName(paytInfoChequeDD.getBranchName());

        instrHeaderChequeDD.setInstrumentDate(paytInfoChequeDD.getInstrumentDate());
        instrHeaderChequeDD.setBankId(bank);

        return instrHeaderChequeDD;
    }

    public List<ReceiptDetail> apportionBillAmount(final BigDecimal actualAmountPaid,
            final ArrayList<ReceiptDetail> receiptDetails) {
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        final ReceiptHeader receiptHeader = receiptDetails.get(0).getReceiptHeader();
        final BillingIntegrationService billingService = (BillingIntegrationService) collectionsUtil
                .getBean(receiptHeader.getService().getCode() + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
        billingService.apportionPaidAmount(receiptHeader.getReferencenumber(), actualAmountPaid, receiptDetails);
        for (final ReceiptDetail receiptDetail : receiptDetails)
            totalCreditAmount = totalCreditAmount.add(receiptDetail.getCramount());
        if (totalCreditAmount.intValue() == 0)
            throw new ApplicationRuntimeException("Apportioning Failed at the Billing System: "
                    + receiptHeader.getService().getCode() + ", for bill number: " + receiptHeader.getReferencenumber());
        return receiptDetails;
    }

    public PersistenceService<BranchUserMap, Long> getBranchUserMapService() {
        return branchUserMapService;
    }

    public void setBranchUserMapService(final PersistenceService<BranchUserMap, Long> branchUserMapService) {
        this.branchUserMapService = branchUserMapService;
    }

    /**
     * Validate and construct InstrumentHeader object for Instrument type ATM
     * Checks if the bank instrument number, transaction number, transaction
     * date, bank branch, bank account number are valid
     *
     * @param paytInfoATM
     * @return
     */
    /*
     * public InstrumentHeader validateAndConstructATMInstrument(PaymentInfoATM
     * paytInfoATM) { String invalidATMPaytMsg="";
     * if(paytInfoATM.getInstrumentAmount()==null ||
     * paytInfoATM.getInstrumentAmount().compareTo(BigDecimal.ZERO)<=0){
     * invalidATMPaytMsg+="Invalid Bank Instrument Amount[" +
     * paytInfoATM.getInstrumentAmount() + "] \n"; }
     * if(paytInfoATM.getTransactionNumber()==null ||
     * paytInfoATM.getTransactionNumber()<0){ invalidATMPaytMsg+=
     * "Invalid Bank Transaction Number[" + paytInfoATM.getInstrumentAmount() +
     * "] \n"; } if(paytInfoATM.getTransactionDate()==null){
     * invalidATMPaytMsg+="Missing Bank Transaction Date \n"; } if(new
     * Date().compareTo(paytInfoATM.getTransactionDate())==-1){
     * invalidATMPaytMsg +="Bank Transaction Date["
     * +paytInfoATM.getTransactionDate ()+"] cannot be a future date \n"; } Bank
     * bank = null; if (paytInfoATM.getBankId() != null) {
     * bank=commonsServiceImpl.getBankById(paytInfoATM.getBankId().intValue());
     * if(bank==null){ invalidATMPaytMsg+= "No bank present for bank id ["+
     * paytInfoATM.getBankId()+"] \n"; } }
     * if(!(CollectionConstants.BLANK.equals(invalidATMPaytMsg))) throw new
     * ApplicationRuntimeException(invalidATMPaytMsg); InstrumentHeader
     * instrHeaderATM = new InstrumentHeader();
     * instrHeaderATM.setInstrumentType(financialsUtil.getInstrumentTypeByType(
     * CollectionConstants.INSTRUMENTTYPE_ATM)); instrHeaderATM.setBankId(bank);
     * instrHeaderATM.setTransactionNumber(String
     * .valueOf(paytInfoATM.getTransactionNumber()));
     * instrHeaderATM.setInstrumentAmount(paytInfoATM.getInstrumentAmount());
     * instrHeaderATM.setTransactionDate(paytInfoATM.getTransactionDate());
     * instrHeaderATM.setIsPayCheque(CollectionConstants.ZERO_INT); return
     * instrHeaderATM; }
     */
}