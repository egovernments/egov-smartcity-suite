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
package org.egov.collection.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.models.BillAccountDetails;
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
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.service.CommonsServiceImpl;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.MoneyUtils;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
public class CollectionCommon {

    private static final Logger LOGGER = Logger.getLogger(CollectionCommon.class);
    private ReportService reportService;
    protected PersistenceService persistenceService;
    private ReceiptHeaderService receiptHeaderService;

    private CommonsServiceImpl commonsServiceImpl;
    @Autowired
    private BoundaryService boundaryService; 
    private EgovCommon egovCommon;
    private CollectionsUtil collectionsUtil;
    private FinancialsUtil financialsUtil;

    /**
     * 
     * @param receiptHeaderService
     *            the receipt header Service to be set
     */
    public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    /**
     * @param persistenceService
     *            the persistenceService to set
     */
    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * @param reportService
     *            the reportService to set
     */
    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * @param collectionsUtil
     *            the collectionsUtil to set
     */
    public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @param FinancialsUtil
     *            the FinancialsUtil to set
     */
    public void setFinancialsUtil(FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    /**
     * @param serviceCode
     *            Billing service code for which the receipt template is to be
     *            returned
     * @return Receipt template to be used for given billing service code.
     */
    private String getReceiptTemplateName(char receiptType, String serviceCode) {
        String templateName = null;

        switch (receiptType) {
        case CollectionConstants.RECEIPT_TYPE_BILL:
            templateName = serviceCode + CollectionConstants.SEPARATOR_UNDERSCORE
                    + CollectionConstants.RECEIPT_TEMPLATE_NAME;// <servicecode>_collection_receipt
            if (!reportService.isValidTemplate(templateName)) {
                LOGGER.info("Billing system specific report template [" + templateName
                        + "] not available. Using the default template [" + CollectionConstants.RECEIPT_TEMPLATE_NAME
                        + "]");
                templateName = CollectionConstants.RECEIPT_TEMPLATE_NAME;

                if (!reportService.isValidTemplate(templateName)) {
                    // No template available for creating the receipt report.
                    // Throw
                    // exception.
                    String errMsg = "Report template [" + templateName
                            + "] not available! Receipt report cannot be generated.";
                    LOGGER.error(errMsg);
                    throw new EGOVRuntimeException(errMsg);
                }
            }
            break;
        case CollectionConstants.RECEIPT_TYPE_CHALLAN:
            templateName = CollectionConstants.CHALLAN_RECEIPT_TEMPLATE_NAME;
            break;
        case CollectionConstants.RECEIPT_TYPE_ADHOC:
            templateName = serviceCode + CollectionConstants.SEPARATOR_UNDERSCORE
                    + CollectionConstants.RECEIPT_TEMPLATE_NAME;
            if (!reportService.isValidTemplate(templateName)) {
                LOGGER.info("Billing system specific report template [" + templateName
                        + "] not available. Using the default template [" + CollectionConstants.RECEIPT_TEMPLATE_NAME
                        + "]");
                templateName = CollectionConstants.RECEIPT_TEMPLATE_NAME;

                if (!reportService.isValidTemplate(templateName)) {
                    // No template available for creating the receipt report.
                    // Throw
                    // exception.
                    String errMsg = "Report template [" + templateName
                            + "] not available!Miscellaneous Receipt report cannot be generated.";
                    LOGGER.error(errMsg);
                    throw new EGOVRuntimeException(errMsg);
                }
            }
            break;
        }

        return templateName;
    }

    public ReceiptDetail addDebitAccountHeadDetails(BigDecimal debitAmount, ReceiptHeader receiptHeader,
            BigDecimal chequeInstrumenttotal, BigDecimal otherInstrumenttotal, String instrumentType) {

        Map<String, Object> cashChequeInfoMap  = egovCommon.getCashChequeInfoForBoundary();
        ReceiptDetail newReceiptDetail = new ReceiptDetail();
        if (chequeInstrumenttotal.toString() != null
                && !chequeInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_INT)
                && !chequeInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_DOUBLE)) {

            String chequeInHandGLCode = cashChequeInfoMap.get(CollectionConstants.MAP_KEY_EGOVCOMMON_CHEQUEINHAND)
                    .toString();

            newReceiptDetail.setAccounthead(commonsServiceImpl.getCChartOfAccountsByGlCode(chequeInHandGLCode));

            newReceiptDetail.setDramount(debitAmount);
            newReceiptDetail.setCramount(BigDecimal.valueOf(0));
            newReceiptDetail.setReceiptHeader(receiptHeader);
        }

        if (otherInstrumenttotal.toString() != null
                && !otherInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_INT)
                && !otherInstrumenttotal.toString().trim().equals(CollectionConstants.ZERO_DOUBLE)) {
        	if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                String cashInHandGLCode  = cashChequeInfoMap.get(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND)
                        .toString();
                 
                newReceiptDetail.setAccounthead(commonsServiceImpl.getCChartOfAccountsByGlCode(cashInHandGLCode));
            } else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
                newReceiptDetail
                        .setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                                CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE,
                                CollectionConstants.INSTRUMENTTYPE_CARD));
            } else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_BANK)) {
                newReceiptDetail.setAccounthead(receiptHeader.getReceiptInstrument().iterator().next()
                        .getBankAccountId().getChartofaccounts());

            } else if (instrumentType.equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
                newReceiptDetail.setAccounthead((CChartOfAccounts) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHARTOFACCOUNT_BY_INSTRTYPE,
                        CollectionConstants.INSTRUMENTTYPE_ONLINE));
            }
            newReceiptDetail.setDramount(debitAmount);
            newReceiptDetail.setCramount(BigDecimal.valueOf(0));
            newReceiptDetail.setReceiptHeader(receiptHeader);
        }

        return newReceiptDetail;
    }

    /**
     * Updates the billing system with receipt information
     */
    @Transactional
    public void updateBillingSystemWithReceiptInfo(ReceiptHeader receiptHeader) {

        String serviceCode = null;
       // ReceiptHeader rh = payeeDetails.getReceiptHeaders().iterator().next();
        /**
         * for each receipt created, send the details back to the billing system
         */
        LOGGER.info("$$$$$$ Update Billing system for Service Code :" + receiptHeader.getService().getCode()
				+ (receiptHeader.getConsumerCode() != null ? " and consumer code: " + receiptHeader.getConsumerCode() : ""));
        Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>();
       // for (ReceiptHeader receiptHeader : payeeDetails.getReceiptHeaders()) {
            billReceipts.add(new BillReceiptInfoImpl(receiptHeader));
            if (serviceCode == null) {
                serviceCode = receiptHeader.getService().getCode();
            }
       // }

        if (receiptHeaderService.updateBillingSystem(serviceCode, billReceipts)) {
            //for (ReceiptHeader receiptHeader : payeeDetails.getReceiptHeaders()) {
                receiptHeader.setIsReconciled(true);
            //}
            // the receipts should be persisted again
            receiptHeaderService.persist(receiptHeader);
        }
        LOGGER.info("$$$$$$ Billing system updated for Service Code :" + receiptHeader.getService().getCode()
				+ (receiptHeader.getConsumerCode() != null ? " and consumer code: " + receiptHeader.getConsumerCode() : ""));
    }

    /**
     * This method initialises the model, a list of
     * <code>ReceiptPayeeDetails</code> objects with the information contained
     * in the unmarshalled <code>BillCollection</code> instance.
     */
    public ReceiptHeader initialiseReceiptModelWithBillInfo(BillInfo collDetails, Fund fund,
            Department dept) {
        ReceiptHeader receiptHeader = null;
        
    	StringBuilder collModesNotAllowed = new StringBuilder();
        if (collDetails.getCollectionModesNotAllowed() != null) {
            for (String collModeNotAllwd : collDetails.getCollectionModesNotAllowed()) {
                if (collModesNotAllowed.length() > 0) {
                    collModesNotAllowed.append(',');
                }
                collModesNotAllowed.append(collModeNotAllwd);
            }
        }

        for (BillPayeeDetails billPayee : collDetails.getPayees()) {
        	receiptHeader = new ReceiptHeader();
            for (BillDetails billDetail : billPayee.getBillDetails()) {
                ServiceDetails service = (ServiceDetails) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_SERVICE_BY_CODE, collDetails.getServiceCode());

                receiptHeader = new ReceiptHeader(billDetail.getRefNo(), billDetail.getBilldate(),
                        billDetail.getConsumerCode(), billDetail.getDescription(), billDetail.getTotalAmount(),
                        billDetail.getMinimumAmount(), collDetails.getPartPaymentAllowed(), collDetails
                                .getOverrideAccountHeadsAllowed(), collDetails.getCallbackForApportioning(),
                        collDetails.getDisplayMessage(), service, collModesNotAllowed.toString(),billPayee.getPayeeName(),billPayee.getPayeeAddress());

                Boundary boundary = boundaryService.getActiveBoundaryByBndryNumAndTypeAndHierarchyTypeCode(Long.valueOf(billDetail.getBoundaryNum()), billDetail
                        .getBoundaryType(), CollectionConstants.BOUNDARY_HIER_CODE_ADMIN);

                Functionary functionary = (Functionary) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_FUNCTIONARY_BY_CODE, collDetails.getFunctionaryCode());
                Fundsource fundSource = commonsServiceImpl.getFundSourceByCode(collDetails.getFundSourceCode());

                ReceiptMisc receiptMisc = new ReceiptMisc(boundary, fund, functionary, fundSource, dept, receiptHeader,
                        null, null,null);
                receiptHeader.setReceiptMisc(receiptMisc);

                BigDecimal totalAmountToBeCollected = BigDecimal.valueOf(0);

                Collections.sort(billDetail.getAccounts());

                for (BillAccountDetails billAccount : billDetail.getAccounts()) {
                    CChartOfAccounts account = null ;//= common.getCChartOfAccountsByGlCode(billAccount.getGlCode());
                    CFunction function = commonsServiceImpl.getFunctionByCode(billAccount.getFunctionCode());
                    if (billAccount.getIsActualDemand()) {
						totalAmountToBeCollected = totalAmountToBeCollected.add(billAccount.getCrAmount()).subtract(billAccount.getDrAmount());
					}
                    ReceiptDetail receiptDetail = new ReceiptDetail(account, function, billAccount.getCrAmount()
                            .subtract(billAccount.getDrAmount()), billAccount.getDrAmount(), billAccount.getCrAmount(),
                            Long.valueOf(billAccount.getOrder()), billAccount.getDescription(), billAccount.getIsActualDemand(), receiptHeader);
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
    public PaymentResponse createPaymentResponse(ServiceDetails paymentServiceDetails, String response) {
        PaymentGatewayAdaptor paymentGatewayAdaptor = this.getPaymentGatewayAdaptor(paymentServiceDetails.getCode());

        PaymentResponse paymentResponse = paymentGatewayAdaptor.parsePaymentResponse(response);

        return paymentResponse;
    }

    /**
     * This method generates a report for the given array of receipts
     * 
     * @param receipts
     *            an array of <code>ReceiptHeader</code> objects for which the
     *            report is to be generated
     * 
     * @param session
     *            a <code>Map</code> of String and Object key- value pairs
     *            containing the session information
     * 
     * @param flag
     *            a boolean value indicating if the generated report should also
     *            have the print option
     * 
     * @return an integer representing the report id
     */
    public Integer generateReport(ReceiptHeader[] receipts, Map<String, Object> session, boolean flag) {
        String serviceCode = receipts[0].getService().getCode();
        char receiptType = receipts[0].getReceipttype();
        List<BillReceiptInfo> receiptList = new ArrayList<BillReceiptInfo>();

        String templateName = getReceiptTemplateName(receiptType, serviceCode);
        System.out.print(" template name : " + templateName);
        Map reportParams = new HashMap<String, Object>();
        reportParams.put(CollectionConstants.REPORT_PARAM_COLLECTIONS_UTIL, collectionsUtil);
        
        if (receiptType == CollectionConstants.RECEIPT_TYPE_CHALLAN) {
            reportParams.put(CollectionConstants.REPORT_PARAM_EGOV_COMMON, egovCommon);
            for (ReceiptHeader receiptHeader : receipts) {
                ReceiptHeader receipHeaderRefObj = (ReceiptHeader) persistenceService.findByNamedQuery(
                        CollectionConstants.QUERY_CHALLANRECEIPT_BY_REFERENCEID, receiptHeader.getId());
                receiptList.add(new BillReceiptInfoImpl(receiptHeader, egovCommon, receipHeaderRefObj));
            }
        } else {
            for (ReceiptHeader receiptHeader : receipts) {
                receiptList.add(new BillReceiptInfoImpl(receiptHeader));
            }
        }

        ReportRequest reportInput = new ReportRequest(templateName, receiptList, reportParams);

        // Set the flag so that print dialog box is automatically opened
        // whenever the PDF is opened
        reportInput.setPrintDialogOnOpenReport(flag);

        return ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), session);
    }

    /**
     * This method generates a challan for the given receipt
     * 
     * @param receipt
     *            <code>ReceiptHeader</code> object for which the report is to
     *            be generated
     * 
     * @param session
     *            a <code>Map</code> of String and Object key- value pairs
     *            containing the session information
     * 
     * @param flag
     *            a boolean value indicating if the generated challan should
     *            also have the print option
     * 
     * @return an integer representing the report id
     */
    public Integer generateChallan(ReceiptHeader receipt, Map<String, Object> session, boolean flag) {
        List<BillReceiptInfo> receiptList = new ArrayList<BillReceiptInfo>();
        //receiptList.add(new BillReceiptInfoImpl(receipt, egovCommon, new ReceiptHeader()));

        String templateName = CollectionConstants.CHALLAN_TEMPLATE_NAME;
        Map reportParams = new HashMap<String, Object>();
        reportParams.put("EGOV_COMMON", egovCommon);
        ReportRequest reportInput = new ReportRequest(templateName, receiptList, reportParams);

        // Set the flag so that print dialog box is automatically opened
        // whenever the PDF is opened
        reportInput.setPrintDialogOnOpenReport(flag);

        return ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), session);
    }

    @Transactional
    public PaymentRequest createPaymentRequest(ServiceDetails paymentServiceDetails, ReceiptHeader receiptHeader) {

        PaymentGatewayAdaptor paymentGatewayAdaptor = this.getPaymentGatewayAdaptor(paymentServiceDetails.getCode());

        PaymentRequest paymentRequest = paymentGatewayAdaptor
                .createPaymentRequest(paymentServiceDetails, receiptHeader);

        return paymentRequest;
    }

    protected PaymentGatewayAdaptor getPaymentGatewayAdaptor(String serviceCode) {
        return (PaymentGatewayAdaptor) collectionsUtil.getBean(serviceCode
                + CollectionConstants.PAYMENTGATEWAYADAPTOR_INTERFACE_SUFFIX);
    }

   /* *//**
     * @param egovCommon
     *            the egovCommon to set
     *//*
    public void setEgovCommon(EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }*/

    public List<ReceiptDetailInfo> setReceiptDetailsList(ReceiptHeader rh, String amountType) {
        // To Load receipt details to billDetailslist
        List<ReceiptDetailInfo> billDetailslist = new ArrayList<ReceiptDetailInfo>();
        List<CChartOfAccounts> bankCOAList =  FinancialsUtil.getBankChartofAccountCodeList();
        for (ReceiptDetail rDetails : rh.getReceiptDetails()) {
            if (!FinancialsUtil.isRevenueAccountHead(rDetails.getAccounthead(),bankCOAList)) {
                ReceiptDetailInfo rInfo = new ReceiptDetailInfo();
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
                        && (rDetails.getCramount().compareTo(BigDecimal.ZERO) != 0)) {
                    billDetailslist.add(rInfo);
                } else if ((amountType.equals(CollectionConstants.COLLECTIONSAMOUNTTPE_DEBIT) || amountType
                        .equals(CollectionConstants.COLLECTIONSAMOUNTTPE_BOTH))
                        && (rDetails.getDramount().compareTo(BigDecimal.ZERO) != 0)) {
                    billDetailslist.add(rInfo);
                }
            }
        }
        if (billDetailslist.isEmpty()) {
            billDetailslist.add(new ReceiptDetailInfo());
        }
        return billDetailslist;
    }

    public List<ReceiptDetailInfo> setAccountPayeeList(ReceiptHeader rh) {
        // To load subledgerlist data to subLedgerlist
        List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
        try {

            for (ReceiptDetail rDetails : rh.getReceiptDetails()) {
                for (AccountPayeeDetail aDetail : rDetails.getAccountPayeeDetails()) {
                    ReceiptDetailInfo rInfo = new ReceiptDetailInfo();
                    rInfo.setAmount(aDetail.getAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
                            BigDecimal.ROUND_UP));
                    rInfo.setCreditAmountDetail(BigDecimal.ZERO);
                    rInfo.setDebitAmountDetail(BigDecimal.ZERO);
                    EntityType entityType = egovCommon.getEntityType(aDetail.getAccountDetailType(), aDetail
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

            }

        } catch (Exception e) {
            LOGGER.error("Exception while setting subledger details", e);
        }
        if (subLedgerlist.isEmpty()) {
            subLedgerlist.add(new ReceiptDetailInfo());
        }
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
    @Transactional
    public void cancelChallanReceiptOnCreation(ReceiptHeader receiptHeader) {
        ReceiptHeader receiptHeaderToBeCancelled = receiptHeaderService.findById(receiptHeader.getReceiptHeader()
                .getId(), false);

        receiptHeaderToBeCancelled.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));

        receiptHeaderService.persist(receiptHeaderToBeCancelled);
    }

    /**
     * This method create a new receipt header object with details contained in
     * given receipt header object.
     * 
     * Both the receipt header objects are added to the same parent
     * <code>ReceiptPayeeDetail</code> object .
     * 
     * @param oldReceiptHeader
     *            the instance of <code>ReceiptHeader</code> whose data is to be
     *            copied
     */
    @Transactional
    public ReceiptHeader createPendingReceiptFromCancelledChallanReceipt(ReceiptHeader oldReceiptHeader) {

        ReceiptHeader newReceiptHeader = new ReceiptHeader(true, oldReceiptHeader.getIsModifiable(), oldReceiptHeader
                .getReceipttype(), oldReceiptHeader.getCollectiontype(), oldReceiptHeader.getPaidBy(), oldReceiptHeader
                .getService(), oldReceiptHeader.getReferencenumber(), oldReceiptHeader.getReferenceDesc(),
                oldReceiptHeader.getTotalAmount());
        /*
         * //receipt number is PENDING newReceiptHeader.setReceiptnumber(null);
         */

        // receipt status is PENDING
        newReceiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_PENDING));
        // the new receipt has reference to the cancelled receipt
        // newReceiptHeader.setReferenceCollectionHeaderId(oldReceiptHeader.getId());
        newReceiptHeader.setReceiptHeader(oldReceiptHeader);

        ReceiptMisc receiptMisc = new ReceiptMisc(oldReceiptHeader.getReceiptMisc().getBoundary(), oldReceiptHeader
                .getReceiptMisc().getFund(), null, null, oldReceiptHeader.getReceiptMisc().getDepartment(),
                newReceiptHeader, null, null,null);
        newReceiptHeader.setReceiptMisc(receiptMisc);

        List<CChartOfAccounts> bankCOAList =  FinancialsUtil.getBankChartofAccountCodeList();
        
        for (ReceiptDetail oldDetail : oldReceiptHeader.getReceiptDetails()) {
            // debit account heads should not be considered
            // This is to omit revenueheadaccounts
            if (!FinancialsUtil.isRevenueAccountHead(oldDetail.getAccounthead(),bankCOAList)) {
                ReceiptDetail receiptDetail = new ReceiptDetail(oldDetail.getAccounthead(), oldDetail.getFunction(),
                        oldDetail.getCramountToBePaid(), oldDetail.getDramount(), oldDetail.getCramount(), oldDetail
                                .getOrdernumber(), oldDetail.getDescription(), oldDetail.getIsActualDemand(),
                        newReceiptHeader);
                receiptDetail.setCramount(oldDetail.getCramount());
                receiptDetail.setFinancialYear(oldDetail.getFinancialYear());

                for (AccountPayeeDetail oldAccountPayeeDetail : oldDetail.getAccountPayeeDetails()) {
                    AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(oldAccountPayeeDetail
                            .getAccountDetailType(), oldAccountPayeeDetail.getAccountDetailKey(), oldAccountPayeeDetail
                            .getAmount(), receiptDetail);
                    receiptDetail.addAccountPayeeDetail(accPayeeDetail);
                }

                newReceiptHeader.addReceiptDetail(receiptDetail);
            }
        }

        if (oldReceiptHeader.getChallan() != null) {
            oldReceiptHeader.getChallan().setReceiptHeader(newReceiptHeader);
            newReceiptHeader.setChallan(oldReceiptHeader.getChallan());
        }
/*
        newReceiptHeader.setReceiptPayeeDetails(payee);
        payee.addReceiptHeader(newReceiptHeader);
*/
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
     * 
     * @param cancelInstrument
     *            a boolean value indicating if the instrument should be
     *            cancelled
     */
    @Transactional
    public void cancelChallanReceipt(ReceiptHeader receiptHeader, boolean cancelInstrument) {
        String instrumentType = "";
        /**
         * The receipt header to be cancelled is the object retrieved in the
         * prepare method
         */

        receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
        receiptHeader.setIsReconciled(true); // have to check this for
        // scheduler

        if (cancelInstrument) {
            for (InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument()) {

                instrumentHeader.setStatusId(commonsServiceImpl.getStatusByModuleAndCode(
                        CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
                        CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED));

                instrumentType = instrumentHeader.getInstrumentType().getType();
            }
        }

        for (ReceiptVoucher receiptVoucher : receiptHeader.getReceiptVoucher()) {
            receiptHeaderService.createReversalVoucher(receiptVoucher, instrumentType);
        }

        // persist the cancelled receipt
        receiptHeaderService.persist(receiptHeader);

        LOGGER.info("Receipt " + receiptHeader.getReceiptnumber() + " has been cancelled");
    }

    /**
     * This method is used to get the financial Year Id for the given date
     * 
     * @return the financial year id if exists else 0
     */
    public String getFinancialYearIdByDate(Date date) {
        CFinancialYear fYear = null;
        fYear = (CFinancialYear) persistenceService.findByNamedQuery(CollectionConstants.QUERY_GETFINANCIALYEARBYDATE,
                date);
        if (fYear != null) {
            return fYear.getId().toString();
        }
        return CollectionConstants.ZERO_INT;
    }

  public InstrumentHeader validateAndConstructCashInstrument(PaymentInfoCash paytInfoCash) {
        if (paytInfoCash.getInstrumentAmount() == null
                || paytInfoCash.getInstrumentAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new EGOVRuntimeException("Invalid Cash Instrument Amount[" + paytInfoCash.getInstrumentAmount() + "]");
        }

        InstrumentHeader instrHeaderCash = new InstrumentHeader();
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
     * 
     * if(!(CollectionConstants.BLANK.equals(invalidCardPaytMsg))) throw new
     * EGOVRuntimeException(invalidCardPaytMsg);
     * 
     * //Process Card Payment by invoking BillDesk API MerchantInfo merchantInfo
     * = processCardPayment(paytInfoCard,receiptHeader);
     * 
     * InstrumentHeader instrHeaderCard = new InstrumentHeader();
     * 
     * if(merchantInfo.getAuthStatus().equals(CollectionConstants.
     * PGI_AUTHORISATION_CODE_SUCCESS)) {
     * instrHeaderCard.setInstrumentType(financialsUtil.getInstrumentTypeByType(
     * CollectionConstants.INSTRUMENTTYPE_CARD));
     * instrHeaderCard.setInstrumentAmount(new
     * BigDecimal(merchantInfo.getTxnAmount()));
     * instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);
     * 
     * //this value has to be captured from bill desk
     * instrHeaderCard.setTransactionNumber(merchantInfo.getTxnReferenceNo());
     * //instrument number is last 4 char of card number
     * instrHeaderCard.setInstrumentNumber(merchantInfo.getCcno().substring(
     * paytInfoCard.getInstrumentNumber().length()-4));
     * 
     * 
     * SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",
     * Locale.getDefault()); Date transactionDate = null; try { transactionDate
     * = sdf.parse(merchantInfo.getTxnDate()); } catch (ParseException e) {
     * LOGGER.error("Error occured in parsing the transaction date [" +
     * merchantInfo.getTxnDate() + "]", e);
     * 
     * throw new EGOVRuntimeException("Error in parsing date"); }
     * instrHeaderCard.setTransactionDate(transactionDate);
     * 
     * OnlinePayment onlinePayment = new OnlinePayment();
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
     * 
     * receiptHeader.setOnlinePayment(onlinePayment); }
     * 
     * return instrHeaderCard; }
     */

    /**
     * Checks if the bank instrument number, transaction number, transaction
     * date, bank branch, bank account number are valid
     * 
     * @param paytInfoBank
     * 
     * @return
     */
    public InstrumentHeader validateAndConstructBankInstrument(PaymentInfoBank paytInfoBank) {
        String invalidBankPaytMsg = "";
        if (paytInfoBank.getInstrumentAmount() == null
                || paytInfoBank.getInstrumentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            invalidBankPaytMsg += "Invalid Bank Instrument Amount[" + paytInfoBank.getInstrumentAmount() + "] \n";
        }
        if (paytInfoBank.getTransactionNumber() == null || paytInfoBank.getTransactionNumber() < 0
                || String.valueOf(paytInfoBank.getTransactionNumber()).length() != 6) {
            invalidBankPaytMsg += "Invalid Bank Transaction Number[" + paytInfoBank.getInstrumentAmount() + "] \n";
        }
        if (paytInfoBank.getTransactionDate() == null) {
            invalidBankPaytMsg += "Missing Bank Transaction Date \n";
        }
        if (new Date().compareTo(paytInfoBank.getTransactionDate()) == -1) {
            invalidBankPaytMsg += "Bank Transaction Date[" + paytInfoBank.getTransactionDate()
                    + "] cannot be a future date \n";
        }
        Bankaccount account = null;
        if (paytInfoBank.getBankAccountId() == null) {
            invalidBankPaytMsg += "Missing Bank Account Id \n";
        } else {
            account = commonsServiceImpl.getBankaccountById(paytInfoBank.getBankAccountId().intValue());

            if (account == null) {
                invalidBankPaytMsg += "No account found for bank account id[" + paytInfoBank.getBankAccountId()
                        + "] \n";
            }
        }

        if (!(CollectionConstants.BLANK.equals(invalidBankPaytMsg)))
            throw new EGOVRuntimeException(invalidBankPaytMsg);

        InstrumentHeader instrHeaderBank = new InstrumentHeader();

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
     * 
     * @return
     */
   public InstrumentHeader validateAndConstructChequeDDInstrument(PaymentInfoChequeDD paytInfoChequeDD) {
        String invalidChequeDDPaytMsg = "";
        if (paytInfoChequeDD.getInstrumentAmount() == null
                || paytInfoChequeDD.getInstrumentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            invalidChequeDDPaytMsg += "Invalid cheque/DD Instrument Amount[" + paytInfoChequeDD.getInstrumentAmount()
                    + "] \n";
        }
        if (paytInfoChequeDD.getInstrumentNumber() == null
                || CollectionConstants.BLANK.equals(paytInfoChequeDD.getInstrumentNumber())
                || !MoneyUtils.isInteger(paytInfoChequeDD.getInstrumentNumber())
                || paytInfoChequeDD.getInstrumentNumber().length() != 6) {
            invalidChequeDDPaytMsg += "Invalid Cheque/DD Instrument Number[" + paytInfoChequeDD.getInstrumentNumber()
                    + "]. \n";
        }
        if (paytInfoChequeDD.getInstrumentDate() == null) {
            invalidChequeDDPaytMsg += "Missing Cheque/DD Transaction Date \n";
        }
        if (new Date().compareTo(paytInfoChequeDD.getInstrumentDate()) == -1) {
            invalidChequeDDPaytMsg += "Cheque/DD Transaction Date[" + paytInfoChequeDD.getInstrumentDate()
                    + "] cannot be a future date \n";
        }
        Bank bank = null;
        if (paytInfoChequeDD.getBankId() != null) {
            bank = commonsServiceImpl.getBankById(paytInfoChequeDD.getBankId().intValue());
            if (bank == null) {
                invalidChequeDDPaytMsg += "No bank present for bank id [" + paytInfoChequeDD.getBankId() + "] \n";
            }
        }

        if (!(CollectionConstants.BLANK.equals(invalidChequeDDPaytMsg)))
            throw new EGOVRuntimeException(invalidChequeDDPaytMsg);

        InstrumentHeader instrHeaderChequeDD = new InstrumentHeader();

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

    public ArrayList<ReceiptDetail> apportionBillAmount(BigDecimal actualAmountPaid,
            ArrayList<ReceiptDetail> receiptDetails) {
        ReceiptHeader receiptHeader = receiptDetails.get(0).getReceiptHeader();
        BillingIntegrationService billingService = (BillingIntegrationService) collectionsUtil.getBean(receiptHeader
                .getService().getCode()
                + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
        billingService.apportionPaidAmount(receiptHeader.getReferencenumber(), actualAmountPaid, receiptDetails);
        return receiptDetails;
    }

    /**
     * Validate and construct InstrumentHeader object for Instrument type ATM
     * Checks if the bank instrument number, transaction number, transaction
     * date, bank branch, bank account number are valid
     * 
     * @param paytInfoATM
     * 
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
     * paytInfoATM.getTransactionNumber()<0){
     * invalidATMPaytMsg+="Invalid Bank Transaction Number[" +
     * paytInfoATM.getInstrumentAmount() + "] \n"; }
     * if(paytInfoATM.getTransactionDate()==null){
     * invalidATMPaytMsg+="Missing Bank Transaction Date \n"; } if(new
     * Date().compareTo(paytInfoATM.getTransactionDate())==-1){
     * invalidATMPaytMsg
     * +="Bank Transaction Date["+paytInfoATM.getTransactionDate
     * ()+"] cannot be a future date \n"; }
     * 
     * Bank bank = null; if (paytInfoATM.getBankId() != null) {
     * bank=commonsServiceImpl.getBankById(paytInfoATM.getBankId().intValue());
     * if(bank==null){ invalidATMPaytMsg+="No bank present for bank id ["+
     * paytInfoATM.getBankId()+"] \n"; } }
     * 
     * if(!(CollectionConstants.BLANK.equals(invalidATMPaytMsg))) throw new
     * EGOVRuntimeException(invalidATMPaytMsg);
     * 
     * InstrumentHeader instrHeaderATM = new InstrumentHeader();
     * 
     * instrHeaderATM.setInstrumentType(financialsUtil.getInstrumentTypeByType(
     * CollectionConstants.INSTRUMENTTYPE_ATM));
     * 
     * instrHeaderATM.setBankId(bank);
     * instrHeaderATM.setTransactionNumber(String
     * .valueOf(paytInfoATM.getTransactionNumber()));
     * instrHeaderATM.setInstrumentAmount(paytInfoATM.getInstrumentAmount());
     * instrHeaderATM.setTransactionDate(paytInfoATM.getTransactionDate());
     * instrHeaderATM.setIsPayCheque(CollectionConstants.ZERO_INT);
     * 
     * return instrHeaderATM; }
     */
}
