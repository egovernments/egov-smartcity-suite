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
package org.egov.collection.web.actions.receipts;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.AccountPayeeDetail;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptDetailInfo;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.handler.BillInfoMarshaller;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.integration.services.DebitAccountHeadDetailsService;
import org.egov.collection.service.CollectionService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.dao.FundSourceHibernateDAO;
import org.egov.commons.dao.SchemeHibernateDAO;
import org.egov.commons.dao.SubSchemeHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({@Result(name = ReceiptAction.NEW, location = "receipt-new.jsp"),
        @Result(name = ReceiptAction.INDEX, location = "receipt-index.jsp"),
        @Result(name = ReceiptAction.REDIRECT, location = "receipt-redirect.jsp"),
        @Result(name = CollectionConstants.REPORT, location = "receipt-report.jsp")})
public class ReceiptAction extends BaseFormAction {
    protected static final String REDIRECT = "redirect";
    private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";
    private static final String BANK_BRANCH_LIST = "bankBranchList";
    private static final Logger LOGGER = Logger.getLogger(ReceiptAction.class);
    private static final long serialVersionUID = 1L;
    private static final String CANCEL = "cancel";
    protected List<String> headerFields;
    protected List<String> mandatoryFields;
    private String reportId;
    /**
     * A <code>String</code> representing the input xml coming from the billing system
     */
    private String collectXML;
    private FinancialsUtil financialsUtil;
    /**
     * A <code>Long</code> array of receipt header ids , which have to be displayed for view/print/cancel purposes
     */
    private Long[] selectedReceipts;
    /**
     * An array of <code>ReceiptHeader</code> instances which have to be displayed for view/print/cancel purposes
     */
    private ReceiptHeader[] receipts;
    private ReceiptHeaderService receiptHeaderService;
    private CollectionService collectionService;
    private CollectionsUtil collectionsUtil;
    private List<ReceiptHeader> receiptHeaderValues = new ArrayList<>(0);
    // Instrument information derived from UI
    private List<InstrumentHeader> instrumentProxyList;
    private int instrumentCount;
    private BigDecimal cashOrCardInstrumenttotal = BigDecimal.ZERO;
    private BigDecimal chequeInstrumenttotal = BigDecimal.ZERO;
    private BigDecimal instrumenttotal = BigDecimal.ZERO;
    private String reasonForCancellation;
    private String target = "view";
    private String paidBy;
    private ReceiptHeader receiptHeader = new ReceiptHeader();
    /**
     * A <code>Long</code> value representing the receipt header id captured from the front end, which has to be cancelled.
     */
    private Long oldReceiptId;
    private String fundName;
    private Boolean overrideAccountHeads = Boolean.FALSE;
    private Boolean partPaymentAllowed;
    private Boolean callbackForApportioning = Boolean.FALSE;
    private BigDecimal totalAmntToBeCollected;
    private Boolean cashAllowed = Boolean.TRUE;
    private Boolean cardAllowed = Boolean.TRUE;
    private Boolean chequeAllowed = Boolean.TRUE;
    private Boolean ddAllowed = Boolean.TRUE;
    private Boolean bankAllowed = Boolean.TRUE;
    private Boolean onlineAllowed = Boolean.TRUE;
    private Boolean isReceiptCancelEnable = Boolean.TRUE;
    /**
     * An instance of <code>InstrumentHeader</code> representing the cash instrument details entered by the user during receipt
     * creation
     */
    private InstrumentHeader instrHeaderCash;
    /**
     * An instance of <code>InstrumentHeader</code> representing the card instrument details entered by the user during receipt
     * creation
     */
    private InstrumentHeader instrHeaderCard;
    /**
     * An instance of <code>InstrumentHeader</code> representing the 'bank' instrument details entered by the user during receipt
     * creation
     */
    private InstrumentHeader instrHeaderBank;
    /**
     * An instance of <code>InstrumentHeader</code> representing the online instrument details entered by the user during receipt
     * creation
     */
    private InstrumentHeader instrHeaderOnline;
    private Date voucherDate;
    private String voucherNum;
    private List<ReceiptDetailInfo> subLedgerlist;
    private List<ReceiptDetailInfo> billCreditDetailslist;
    private List<ReceiptDetailInfo> billRebateDetailslist;
    private String billSource = "bill";
    private ReceiptMisc receiptMisc = new ReceiptMisc();
    private String deptId;
    private BigDecimal totalDebitAmount;
    /**
     * A code>String</code> representing the service name
     */
    private String serviceName;

    /**
     * A <code>List</code> of <code>String</code> informations sent by the billing system indicating which are the modes of
     * payment that are not allowed during receipt creation
     */
    private List<String> collectionModesNotAllowed = new ArrayList<>(0);

    /**
     * The <code>User</code> representing the counter operator who has created the receipt
     */
    private User receiptCreatedByCounterOperator;

    /**
     * A <code>List</code> of <code>ReceiptPayeeDetails</code> representing the model for the action.
     */

    private List<ReceiptDetail> receiptDetailList = new ArrayList<>(0);

    private String instrumentTypeCashOrCard;

    private CollectionCommon collectionCommon;

    private Long bankAccountId;

    private Integer bankBranchId;

    private String payeename = "";

    private Date manualReceiptDate;

    private String manualReceiptNumber;

    private Boolean manualReceiptNumberAndDateReq = Boolean.FALSE;

    private Boolean receiptBulkUpload = Boolean.FALSE;

    private PersistenceService<ServiceCategory, Long> serviceCategoryService;

    private PersistenceService<ServiceDetails, Long> serviceDetailsService;

    private Long serviceId;

    @Autowired
    private FundHibernateDAO fundDAO;

    @Autowired
    private FunctionHibernateDAO functionDAO;

    @Autowired
    private FunctionaryHibernateDAO functionaryDAO;

    @Autowired
    private SchemeHibernateDAO schemeDAO;

    @Autowired
    private FundSourceHibernateDAO fundSourceDAO;

    @Autowired
    private BankBranchHibernateDAO bankBranchDAO;

    @Autowired
    private BankHibernateDAO bankDAO;

    @Autowired
    private BankaccountHibernateDAO bankAccountDAO;

    @Autowired
    private SubSchemeHibernateDAO subSchemeDAO;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsDAO;

    @Autowired
    private EgwStatusHibernateDAO statusDAO;

    private List<CChartOfAccounts> bankCOAList;
    private Long functionId;

    private String instrumentType;

    private PaymentRequest paymentRequest;

    @Autowired
    private ApplicationContext beanProvider;

    @Override
    public void prepare() {
        super.prepare();
        BillInfoImpl collDetails;
        setReceiptCreatedByCounterOperator(collectionsUtil.getLoggedInUser());
        // populates model when request is from the billing system
        if (getCollectXML() != null && !getCollectXML().isEmpty()) {
            final String decodedCollectXML = decodeBillXML();
            try {
                collDetails = BillInfoMarshaller.toObject(decodedCollectXML);

                final Fund fund = fundDAO.fundByCode(collDetails.getFundCode());
                if (fund == null)
                    addActionError(getText("billreceipt.improperbilldata.missingfund"));

                setFundName(fund.getName());

                final Department dept = (Department) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_DEPARTMENT_BY_CODE, collDetails.getDepartmentCode());
                if (dept == null)
                    addActionError(getText("billreceipt.improperbilldata.missingdepartment"));

                final ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_SERVICE_BY_CODE, collDetails.getServiceCode());
                setServiceName(service.getName());
                setCollectionModesNotAllowed(collDetails.getCollectionModesNotAllowed());
                setOverrideAccountHeads(collDetails.getOverrideAccountHeadsAllowed());
                setCallbackForApportioning(collDetails.getCallbackForApportioning());
                setPartPaymentAllowed(collDetails.getPartPaymentAllowed());
                totalAmntToBeCollected = BigDecimal.ZERO;

                // populate bank account list
                populateBankBranchList(true);
                receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(collDetails, fund, dept);
                totalAmntToBeCollected = totalAmntToBeCollected.add(receiptHeader.getTotalAmountToBeCollected());
                for (final ReceiptDetail rDetails : receiptHeader.getReceiptDetails())
                    rDetails.getCramountToBePaid().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
                            BigDecimal.ROUND_UP);
                setReceiptDetailList(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));

                if (totalAmntToBeCollected.compareTo(BigDecimal.ZERO) == -1) {
                    addActionError(getText("billreceipt.totalamountlessthanzero.error"));
                    LOGGER.info(getText("billreceipt.totalamountlessthanzero.error"));
                } else
                    setTotalAmntToBeCollected(totalAmntToBeCollected.setScale(
                            CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));
            } catch (final Exception e) {
                LOGGER.error(getText("billreceipt.error.improperbilldata"), e);
                addActionError(getText("billreceipt.error.improperbilldata"));
            }
        }
        addDropdownData("serviceCategoryList",
                serviceCategoryService.findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_SERVICE_CATEGORY));
        addDropdownData("serviceList", Collections.emptyList());
        if (instrumentProxyList == null)
            instrumentCount = 0;
        else
            instrumentCount = instrumentProxyList.size();
    }

    private String decodeBillXML() {
        String decodedBillXml = "";
        try {
            decodedBillXml = java.net.URLDecoder.decode(getCollectXML(), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(getText("billreceipt.error.improperbilldata") + e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return decodedBillXml;
    }

    /**
     * @param populate
     */
    private void populateBankBranchList(final boolean populate) {
        final AjaxBankRemittanceAction ajaxBankRemittanceAction = new AjaxBankRemittanceAction();
        ajaxBankRemittanceAction.setServiceName(getServiceName());
        ajaxBankRemittanceAction.setPersistenceService(getPersistenceService());

        if (populate) {
            ajaxBankRemittanceAction.setFundName(getFundName());
            ajaxBankRemittanceAction.bankBranchList();
            addDropdownData(BANK_BRANCH_LIST, ajaxBankRemittanceAction.getBankBranchArrayList());
            addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
        } else // to load branch list and account list while returning after an
            // error
            if (getServiceName() != null && receiptMisc.getFund() != null) {
                final Fund fund = fundDAO.fundById(receiptMisc.getFund().getId(), false);
                ajaxBankRemittanceAction.setFundName(fund.getName());
                ajaxBankRemittanceAction.bankBranchList();
                addDropdownData(BANK_BRANCH_LIST, ajaxBankRemittanceAction.getBankBranchArrayList());

                // account list should be populated only if bank branch had been
                // chosen
                if (bankBranchId != null && bankBranchId != 0) {
                    final Bankbranch branch = bankBranchDAO.findById(bankBranchId, false);

                    ajaxBankRemittanceAction.setBranchId(branch.getId());
                    ajaxBankRemittanceAction.accountList();
                    addDropdownData(ACCOUNT_NUMBER_LIST, ajaxBankRemittanceAction.getBankAccountArrayList());
                } else
                    addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
            } else {
                addDropdownData(BANK_BRANCH_LIST, Collections.emptyList());
                addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
            }
    }

    /**
     * This method checks for the modes of payment allowed
     */
    private void setCollModesNotAllowedForRemitReceipt(final String collModesNotAllowed) {
        final List modesNotAllowed = Arrays.asList(collModesNotAllowed == null ? Collections.emptyList()
                : collModesNotAllowed.split(","));

        if (modesNotAllowed != null && modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CASH))
            setCashAllowed(Boolean.FALSE);

        if (modesNotAllowed != null && modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CARD))
            setCardAllowed(Boolean.FALSE);
        if (modesNotAllowed != null && modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
            setChequeAllowed(Boolean.FALSE);

        if (modesNotAllowed != null && modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_DD))
            setDdAllowed(Boolean.FALSE);

        if (modesNotAllowed != null && modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_BANK))
            setBankAllowed(Boolean.FALSE);
    }

    /**
     * This method checks for the modes of payment allowed
     */
    private void setCollectionModesNotAllowed() {

        final List<String> modesNotAllowed = collectionsUtil
                .getCollectionModesNotAllowed(getReceiptCreatedByCounterOperator());

        final List<String> collModesNotAllowed = getCollectionModesNotAllowed();

        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CASH) || collModesNotAllowed != null
                && collModesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CASH))
            setCashAllowed(Boolean.FALSE);

        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CARD) || collModesNotAllowed != null
                && collModesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CARD))
            setCardAllowed(Boolean.FALSE);
        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || collModesNotAllowed != null
                && collModesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
            setChequeAllowed(Boolean.FALSE);

        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_DD) || collModesNotAllowed != null
                && collModesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_DD))
            setDdAllowed(Boolean.FALSE);

        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_BANK) || collModesNotAllowed != null
                && collModesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_BANK))
            setBankAllowed(Boolean.FALSE);

        if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_ONLINE) || isBillSourcemisc()
                || collModesNotAllowed != null
                && collModesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_ONLINE))
            setOnlineAllowed(Boolean.FALSE);
    }

    /**
     * To set the receiptpayee details for misc receipts
     */
    private boolean setMiscReceiptDetails() {
        if (CollectionConstants.BLANK.equals(payeename))
            payeename = collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                    CollectionConstants.APPCONFIG_VALUE_PAYEEFORMISCRECEIPTS);

        ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICE_CODE_COLLECTIONS);
        if (null != serviceId && serviceId != -1)
            service = serviceDetailsService.findById(serviceId, false);
        receiptHeader.setPartPaymentAllowed(false);
        receiptHeader.setService(service);
        final Fund fund = fundDAO.fundById(receiptMisc.getFund().getId(), false);
        Functionary functionary = null;
        Scheme scheme = null;
        SubScheme subscheme = null;
        try {
            if (receiptMisc.getIdFunctionary() != null)
                functionary = functionaryDAO.functionaryById(receiptMisc.getIdFunctionary().getId());
            if (receiptMisc.getScheme() != null && receiptMisc.getScheme().getId() != -1)
                scheme = schemeDAO.getSchemeById(receiptMisc.getScheme().getId());
            if (receiptMisc.getSubscheme() != null && receiptMisc.getSubscheme().getId() != -1)
                subscheme = subSchemeDAO.getSubSchemeById(receiptMisc.getSubscheme().getId());
        } catch (final Exception e) {
            LOGGER.error("Error in getting functionary for id [" + receiptMisc.getIdFunctionary().getId() + "]", e);
        }

        Fundsource fundSource = null;
        if (receiptMisc.getFundsource() != null && receiptMisc.getFundsource().getId() != null)
            fundSource = fundSourceDAO.fundsourceById(receiptMisc.getFundsource().getId().intValue());
        final Department dept = (Department) getPersistenceService().findByNamedQuery(
                CollectionConstants.QUERY_DEPARTMENT_BY_ID, Long.valueOf(deptId));
        receiptHeader.setReceiptMisc(new ReceiptMisc(null, fund, functionary, fundSource, dept, receiptHeader, scheme,
                subscheme, null));
        totalAmntToBeCollected = BigDecimal.ZERO;
        int m = 0;
        BigDecimal debitamount = BigDecimal.ZERO;
        removeEmptyRows(billCreditDetailslist);
        removeEmptyRows(billRebateDetailslist);
        removeEmptyRows(subLedgerlist);
        if (validateData(billCreditDetailslist, subLedgerlist))
            for (final ReceiptDetailInfo voucherDetails : billCreditDetailslist) {
                final CChartOfAccounts account = chartOfAccountsDAO.getCChartOfAccountsByGlCode(voucherDetails
                        .getGlcodeDetail());
                CFunction function = null;
                if (functionId != null)
                    function = functionDAO.getFunctionById(functionId);
                ReceiptDetail receiptDetail = new ReceiptDetail(account, function,
                        voucherDetails.getCreditAmountDetail(), voucherDetails.getDebitAmountDetail(), BigDecimal.ZERO,
                        Long.valueOf(m), null, true, receiptHeader, PURPOSE.OTHERS.toString());

                if (voucherDetails.getCreditAmountDetail() == null)
                    receiptDetail.setCramount(BigDecimal.ZERO);
                else
                    receiptDetail.setCramount(voucherDetails.getCreditAmountDetail());

                if (voucherDetails.getDebitAmountDetail() == null)
                    receiptDetail.setDramount(BigDecimal.ZERO);
                else
                    receiptDetail.setDramount(voucherDetails.getDebitAmountDetail());

                receiptDetail = setAccountPayeeDetails(subLedgerlist, receiptDetail);
                receiptHeader.addReceiptDetail(receiptDetail);
                debitamount = debitamount.add(voucherDetails.getCreditAmountDetail());
                debitamount = debitamount.subtract(voucherDetails.getDebitAmountDetail());
                m++;
            }
        else
            return false;
        if (validateRebateData(billRebateDetailslist, subLedgerlist)) {
            for (final ReceiptDetailInfo voucherDetails : billRebateDetailslist)
                if (voucherDetails.getGlcodeDetail() != null
                        && org.apache.commons.lang.StringUtils.isNotBlank(voucherDetails.getGlcodeDetail())) {
                    final CChartOfAccounts account = chartOfAccountsDAO.getCChartOfAccountsByGlCode(voucherDetails
                            .getGlcodeDetail());
                    CFunction function = null;
                    if (voucherDetails.getFunctionIdDetail() != null)
                        function = functionDAO.getFunctionById(voucherDetails.getFunctionIdDetail());
                    ReceiptDetail receiptDetail = new ReceiptDetail(account, function,
                            voucherDetails.getCreditAmountDetail(), voucherDetails.getDebitAmountDetail(),
                            BigDecimal.ZERO, Long.valueOf(m), null, true, receiptHeader, PURPOSE.OTHERS.toString());

                    if (voucherDetails.getDebitAmountDetail() == null)
                        receiptDetail.setDramount(BigDecimal.ZERO);
                    else
                        receiptDetail.setDramount(voucherDetails.getDebitAmountDetail());
                    if (voucherDetails.getCreditAmountDetail() == null)
                        receiptDetail.setCramount(BigDecimal.ZERO);
                    else
                        receiptDetail.setCramount(voucherDetails.getCreditAmountDetail());

                    receiptDetail = setAccountPayeeDetails(subLedgerlist, receiptDetail);
                    receiptHeader.addReceiptDetail(receiptDetail);
                    debitamount = debitamount.add(voucherDetails.getCreditAmountDetail());
                    debitamount = debitamount.subtract(voucherDetails.getDebitAmountDetail());
                    m++;
                }
        } else
            return false;
        setTotalDebitAmount(debitamount);
        return true;
    }

    public ReceiptDetail setAccountPayeeDetails(final List<ReceiptDetailInfo> subLedgerlist,
                                                final ReceiptDetail receiptDetail) {
        for (final ReceiptDetailInfo subvoucherDetails : subLedgerlist)
            if (subvoucherDetails.getGlcode() != null && subvoucherDetails.getGlcode().getId() != 0
                    && subvoucherDetails.getGlcode().getId().equals(receiptDetail.getAccounthead().getId())) {

                final Accountdetailtype accdetailtype = (Accountdetailtype) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID, subvoucherDetails.getDetailType().getId());
                final Accountdetailkey accdetailkey = (Accountdetailkey) getPersistenceService().findByNamedQuery(
                        CollectionConstants.QUERY_ACCOUNTDETAILKEY_BY_DETAILKEY, subvoucherDetails.getDetailKeyId(),
                        subvoucherDetails.getDetailType().getId());

                final AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(accdetailtype, accdetailkey,
                        subvoucherDetails.getAmount(), receiptDetail);

                receiptDetail.addAccountPayeeDetail(accPayeeDetail);
            }
        return receiptDetail;
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/receipts/receipt-newform")
    public String newform() {

        final String manualReceiptInfoRequired = collectionsUtil.getAppConfigValue(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.MANUALRECEIPTINFOREQUIRED);
        if (CollectionConstants.YES.equalsIgnoreCase(manualReceiptInfoRequired))
            setManualReceiptNumberAndDateReq(Boolean.TRUE);

        final String[] receiptType = parameters.get("Receipt");
        if (receiptType != null && "Misc".equalsIgnoreCase(receiptType[0]))
            createMisc();
        // set collection modes allowed rule through script

        setCollectionModesNotAllowed();
        return NEW;
    }

    /**
     * This method is invoked when user creates a receipt.
     *
     * @return
     */
    @ValidationErrorPage(value = "new")
    @Action(value = "/receipts/receipt-save")
    public String save() {
        String returnValue;
        if (instrumentTypeCashOrCard != null
                && instrumentTypeCashOrCard.equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
            if (callbackForApportioning && !overrideAccountHeads)
                apportionBillAmount();
            ServiceDetails paymentService;
            paymentService = (ServiceDetails) getPersistenceService().findByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICECODE_SBIMOPS);
            if (null != paymentService)
                setPaymentRequest(collectionService.populateAndPersistReceipts(paymentService, receiptHeader,
                        receiptDetailList, instrHeaderOnline.getInstrumentAmount(),
                        CollectionConstants.COLLECTION_TYPE_COUNTER));
            return REDIRECT;
        } else {
            List<InstrumentHeader> receiptInstrList = new ArrayList<>(0);
            LOGGER.info("Receipt creation process is started !!!!!!");
            ReceiptHeader rhForValidation = null;
            final long startTimeMillis = System.currentTimeMillis();
            if (manualReceiptNumber != null && manualReceiptDate != null) {
                final CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(manualReceiptDate);
                rhForValidation = receiptHeaderService.findByNamedQuery(
                        CollectionConstants.QUERY_RECEIPT_BY_SERVICE_MANUALRECEIPTNO_AND_DATE, manualReceiptNumber,
                        receiptHeader.getService().getCode(), financialYear.getStartingDate(),
                        financialYear.getEndingDate(), CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
            }

            if (rhForValidation == null) {
                // For interday cancellation
                if (oldReceiptId != null) {
                    final ReceiptHeader receiptHeaderToBeCancelled = receiptHeaderService.findById(oldReceiptId, false);

                    receiptHeaderToBeCancelled.setStatus(statusDAO.getStatusByModuleAndCode(
                            CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                            CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
                    receiptHeaderToBeCancelled.setReasonForCancellation(reasonForCancellation);
                    // set isReconciled to false before calling update to
                    // billing system for
                    // cancel receipt
                    receiptHeaderToBeCancelled.setIsReconciled(false);
                    receiptHeader.setLocation(receiptHeaderToBeCancelled.getLocation());
                    receiptHeaderService.persist(receiptHeaderToBeCancelled);
                    if (receiptHeaderToBeCancelled.getReceipttype() == CollectionConstants.RECEIPT_TYPE_BILL) {
                        populateReceiptModelWithExistingReceiptInfo(receiptHeaderToBeCancelled);
                        LOGGER.info("Receipt Cancelled with Receipt Number(recreateNewReceiptOnCancellation): "
                                + receiptHeaderToBeCancelled.getReceiptnumber() + "; Consumer Code: "
                                + receiptHeaderToBeCancelled.getConsumerCode());
                    }
                }

                if ("misc".equalsIgnoreCase(billSource)) {
                    createMisc();
                    if (!setMiscReceiptDetails())
                        returnValue = NEW;
                } else {
                    if (callbackForApportioning && !overrideAccountHeads)
                        apportionBillAmount();
                    if (receiptDetailList == null || receiptDetailList.isEmpty())
                        throw new ApplicationRuntimeException(
                                "Receipt could not be created as the apportioned receipt detail list is empty");
                    else {
                        BigDecimal totalCreditAmount = BigDecimal.ZERO;
                        for (final ReceiptDetail receiptDetail : receiptDetailList)
                            totalCreditAmount = totalCreditAmount.add(receiptDetail.getCramount());
                        if (totalCreditAmount.intValue() == 0)
                            throw new ApplicationRuntimeException("Apportioning Failed at the Billing System: "
                                    + receiptHeader.getService().getCode() + ", for bill number: "
                                    + receiptHeader.getReferencenumber());
                        else
                            receiptHeader.setReceiptDetails(new HashSet(receiptDetailList));
                    }
                }
                int noOfNewlyCreatedReceipts = 0;
                boolean setInstrument = true;

                // only newly created receipts need to be initialised with the
                // data.
                // The cancelled receipt can be excluded from this processing.
                if (receiptHeader.getStatus() == null) {
                    noOfNewlyCreatedReceipts++;
                    // Set created by Date as this required to generate receipt
                    // number before persist
                    if (manualReceiptDate == null)
                        receiptHeader.setReceiptdate(new Date());
                    else {
                        // If the receipt has been manually created, the receipt
                        // date is same as the date of manual creation.
                        // set Createdby, in MySavelistner if createdBy is null
                        // it set both createdBy and createdDate with
                        // currentDate.
                        // Thus overridding the manualReceiptDate set above
                        // receiptHeader.setCreatedBy(collectionsUtil.getLoggedInUser());
                        receiptHeader.setManualreceiptdate(manualReceiptDate);
                        receiptHeader.setReceiptdate(manualReceiptDate);
                        receiptHeader.setVoucherDate(manualReceiptDate);
                    }
                    if (manualReceiptNumber != null)
                        receiptHeader.setManualreceiptnumber(manualReceiptNumber);
                    if (isBillSourcemisc()) {
                        receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_ADHOC);
                        receiptHeader.setVoucherDate(voucherDate);
                        receiptHeader.setReceiptdate(voucherDate);
                        receiptHeader.setVoucherNum(voucherNum);
                        receiptHeader.setIsReconciled(Boolean.TRUE);
                        receiptHeader.setManualreceiptdate(manualReceiptDate);
                        receiptHeader.setPayeeName(StringEscapeUtils.unescapeHtml(paidBy));

                    } else {
                        receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
                        receiptHeader.setIsModifiable(Boolean.TRUE);
                        receiptHeader.setIsReconciled(Boolean.FALSE);
                    }
                    // serviceType =
                    // receiptHeader.getService().getServiceType();
                    receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);
                    // Bank Collection Operator location is not captured.
                    if (!collectionsUtil.isBankCollectionOperator(receiptCreatedByCounterOperator)
                            && receiptHeader.getLocation() == null)
                        receiptHeader.setLocation(collectionsUtil.getLocationOfUser(getSession()));
                    receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                            CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                            CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED));
                    receiptHeader.setPaidBy(StringEscapeUtils.unescapeHtml(paidBy));
                    receiptHeader.setSource(Source.SYSTEM.toString());

                    // If this is a new receipt in lieu of cancelling old
                    // receipt, update
                    // old receipt id to the reference collection header id
                    // field of this new receipt.
                    if (getOldReceiptId() != null)
                        receiptHeader.setReceiptHeader(receiptHeaderService.findById(getOldReceiptId(), false));
                    if (setInstrument) {
                        receiptInstrList = populateInstrumentDetails();
                        setInstrument = false;
                    }

                    receiptHeader.setReceiptInstrument(new HashSet(receiptInstrList));

                    BigDecimal debitAmount = BigDecimal.ZERO;

                    for (final ReceiptDetail creditChangeReceiptDetail : receiptDetailList)
                        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
                            if (creditChangeReceiptDetail.getReceiptHeader().getReferencenumber()
                                    .equals(receiptDetail.getReceiptHeader().getReferencenumber())
                                    && receiptDetail.getOrdernumber()
                                    .equals(creditChangeReceiptDetail.getOrdernumber())) {

                                receiptDetail.setCramount(creditChangeReceiptDetail.getCramount());
                                receiptDetail.setDramount(creditChangeReceiptDetail.getDramount());
                                // calculate sum of creditamounts as a debit
                                // value to create a
                                // debit account head and add to receipt details
                                debitAmount = debitAmount.add(creditChangeReceiptDetail.getCramount());
                                debitAmount = debitAmount.subtract(creditChangeReceiptDetail.getDramount());
                            }

                    if (chequeInstrumenttotal != null && chequeInstrumenttotal.compareTo(BigDecimal.ZERO) != 0)
                        receiptHeader.setTotalAmount(chequeInstrumenttotal);

                    if (cashOrCardInstrumenttotal != null && cashOrCardInstrumenttotal.compareTo(BigDecimal.ZERO) != 0)
                        receiptHeader.setTotalAmount(cashOrCardInstrumenttotal);
                    DebitAccountHeadDetailsService debitAccountHeadService = (DebitAccountHeadDetailsService) beanProvider
                            .getBean(collectionsUtil.getBeanNameForDebitAccountHead());
                    if (isBillSourcemisc())
                        receiptHeader.addReceiptDetail(debitAccountHeadService.addDebitAccountHeadDetails(
                                totalDebitAmount, receiptHeader, chequeInstrumenttotal, cashOrCardInstrumenttotal,
                                instrumentTypeCashOrCard));
                    else
                        receiptHeader.addReceiptDetail(debitAccountHeadService.addDebitAccountHeadDetails(debitAmount,
                                receiptHeader, chequeInstrumenttotal, cashOrCardInstrumenttotal,
                                instrumentTypeCashOrCard));

                }
                // }// end of looping through receipt headers
                // }// end of looping through model receipt payee list

                LOGGER.info("Call back for apportioning is completed");
                // billing system
                receiptHeaderService.populateAndPersistReceipts(receiptHeader, receiptInstrList);

                // populate all receipt header ids except the cancelled receipt
                // (in effect the newly created receipts)
                selectedReceipts = new Long[noOfNewlyCreatedReceipts];
                int i = 0;
                if (!receiptHeader.getId().equals(oldReceiptId)) {
                    selectedReceipts[i] = receiptHeader.getId();
                    i++;
                }

                final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
                LOGGER.info("$$$$$$ Receipt Persisted with Receipt Number: "
                        + receiptHeader.getReceiptnumber()
                        + (receiptHeader.getConsumerCode() != null ? " and consumer code: "
                        + receiptHeader.getConsumerCode() : "")
                        + "; Time taken(ms) = " + elapsedTimeMillis);
                // Do not invoke print receipt in case of bulk upload.
                if (!receiptBulkUpload)
                    returnValue = printReceipts();
                else
                    returnValue = SUCCESS;
            } else {
                if (rhForValidation.getService().getCode().equals(CollectionConstants.SERVICECODE_PROPERTYTAX))
                    addActionError("Entered Manual receipt number already exists for the index number"
                            + rhForValidation.getConsumerCode()
                            + ".Please enter a valid manual receipt number and create the receipt.");
                else
                    addActionError("Receipt already exists for the service ");
                returnValue = NEW;
            }
        }
        return returnValue;
    }

    public void createMisc() {
        headerFields = new ArrayList<>(0);
        mandatoryFields = new ArrayList<>(0);
        getHeaderMandateFields();
        setupDropdownDataExcluding();

        headerFields.remove(CollectionConstants.FUNDSOURCE);
        headerFields.remove(CollectionConstants.SCHEME);
        headerFields.remove(CollectionConstants.SUBSCHEME);
        if (headerFields.contains(CollectionConstants.DEPARTMENT))
            addDropdownData("departmentList",
                    persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS));
        if (headerFields.contains(CollectionConstants.FUNCTIONARY))
            addDropdownData("functionaryList",
                    persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUNCTIONARY));
        if (headerFields.contains(CollectionConstants.FUND))
            addDropdownData("fundList", collectionsUtil.getAllFunds());
        if (headerFields.contains(CollectionConstants.FUNCTION))
            addDropdownData("functionList", functionDAO.getAllActiveFunctions());
        if (headerFields.contains(CollectionConstants.FIELD))
            addDropdownData("fieldList", persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FIELD));
        if (headerFields.contains(CollectionConstants.FUNDSOURCE))
            addDropdownData("fundsourceList",
                    persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUNDSOURCE));
        if (headerFields.contains(CollectionConstants.SCHEME))
            if (receiptMisc.getFund() == null || receiptMisc.getFund().getId() == null)
                addDropdownData("schemeList", Collections.emptyList());
            else
                addDropdownData("schemeList", persistenceService.findAllByNamedQuery(
                        CollectionConstants.QUERY_SCHEME_BY_FUNDID, receiptMisc.getFund().getId()));
        if (headerFields.contains(CollectionConstants.SUBSCHEME))
            if (receiptMisc.getScheme() == null || receiptMisc.getScheme().getId() == null)
                addDropdownData("subschemeList", Collections.emptyList());
            else
                addDropdownData("subschemeList", persistenceService.findAllByNamedQuery(
                        CollectionConstants.QUERY_SUBSCHEME_BY_SCHEMEID, receiptMisc.getScheme().getId()));

        if (billCreditDetailslist == null) {
            billCreditDetailslist = new ArrayList<>(0);
            billRebateDetailslist = new ArrayList<>(0);
            subLedgerlist = new ArrayList<>(0);
            billRebateDetailslist.add(new ReceiptDetailInfo());
            billCreditDetailslist.add(new ReceiptDetailInfo());
            subLedgerlist.add(new ReceiptDetailInfo());
        }
        billSource = "misc";
        receiptHeader.setPartPaymentAllowed(false);
        setHeaderFields(headerFields);
        setMandatoryFields(mandatoryFields);
        final Department dept = collectionsUtil.getDepartmentOfLoggedInUser();
        if (dept == null)
            throw new ValidationException(Arrays.asList(new ValidationError("Department does not exists",
                    "viewchallan.validation.error.user.notexists")));
        if (getDeptId() == null)
            setDeptId(dept.getId().toString());
        populateBankBranchList(false);
    }

    public boolean isBillSourcemisc() {
        boolean flag = false;
        if ("misc".equalsIgnoreCase(getBillSource()))
            flag = true;
        return flag;
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    protected void getHeaderMandateFields() {
        final List<AppConfigValues> appConfigValuesList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MISMandatoryAttributesModule, CollectionConstants.MISMandatoryAttributesKey);

        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            final String value = appConfigVal.getValue();
            final String header = value.substring(0, value.indexOf('|'));
            headerFields.add(header);
            final String mandate = value.substring(value.indexOf('|') + 1);
            if ("M".equalsIgnoreCase(mandate))
                mandatoryFields.add(header);
        }
        /*
         * if (!"Auto".equalsIgnoreCase(new VoucherTypeForULB().readVoucherTypes("Receipt"))) { headerFields.add("vouchernumber");
         * mandatoryFields.add("vouchernumber"); }
         */
        mandatoryFields.add("voucherdate");
    }

    private List<InstrumentHeader> populateInstrumentDetails() {
        List<InstrumentHeader> instrumentHeaderList = new ArrayList<>(0);

        if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrumentTypeCashOrCard)) {
            instrHeaderCash.setInstrumentType(financialsUtil
                    .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));

            instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
            // the cash amount is set into the object through binding
            // this total is needed for creating debit account head

            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCash.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderCash);
        }
        if (CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrumentTypeCashOrCard)) {
            instrHeaderCard.setInstrumentType(financialsUtil
                    .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CARD));
            if (instrHeaderCard.getTransactionDate() == null)
                instrHeaderCard.setTransactionDate(new Date());
            instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);

            // the instrumentNumber, transactionNumber, instrumentAmount are
            // set into the object directly through binding
            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCard.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderCard);
        }

        if (CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrumentTypeCashOrCard)) {
            instrHeaderBank.setInstrumentType(financialsUtil
                    .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_BANK));
            if (instrHeaderBank.getTransactionDate() == null)
                instrHeaderBank.setTransactionDate(new Date());
            instrHeaderBank.setIsPayCheque(CollectionConstants.ZERO_INT);

            final Bankaccount account = bankAccountDAO.findById(bankAccountId, false);

            instrHeaderBank.setBankAccountId(account);
            instrHeaderBank.setBankBranchName(account.getBankbranch().getBranchname());

            // the instrumentNumber, transactionNumber, instrumentAmount are
            // set into the object directly through binding
            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderBank.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderBank);
        }
        if (CollectionConstants.INSTRUMENTTYPE_ONLINE.equals(instrumentTypeCashOrCard)) {
            instrHeaderOnline.setInstrumentType(financialsUtil
                    .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_ONLINE));

            instrHeaderOnline.setIsPayCheque(CollectionConstants.ZERO_INT);
            // the cash amount is set into the object through binding
            // this total is needed for creating debit account head

            cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderOnline.getInstrumentAmount());

            instrumentHeaderList.add(instrHeaderOnline);
        }

        // cheque/DD types
        if (instrumentProxyList != null && !CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrumentTypeCashOrCard)
                && !CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrumentTypeCashOrCard)
                && !CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrumentTypeCashOrCard))
            if (getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                    || getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_DD))
                instrumentHeaderList = populateInstrumentHeaderForChequeDD(instrumentHeaderList, instrumentProxyList);
        instrumentHeaderList = receiptHeaderService.createInstrument(instrumentHeaderList);
        return instrumentHeaderList;
    }

    /**
     * This instrument creates instrument header instances for the receipt, when the instrument type is Cheque or DD. The created
     * <code>InstrumentHeader</code> instance is persisted
     *
     * @param k an int value representing the index of the instrument type as chosen from the front end
     * @return an <code>InstrumentHeader</code> instance populated with the instrument details
     */
    private List<InstrumentHeader> populateInstrumentHeaderForChequeDD(
            final List<InstrumentHeader> instrumentHeaderList, final List<InstrumentHeader> instrumentProxyList) {

        for (final InstrumentHeader instrumentHeader : instrumentProxyList) {
            if (getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
                instrumentHeader.setInstrumentType(financialsUtil
                        .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE));
            else if (getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_DD))
                instrumentHeader.setInstrumentType(financialsUtil
                        .getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD));
            if (instrumentHeader.getBankId() != null && instrumentHeader.getBankId().getId() == null) {
                addActionError("Bank is not exist");
                throw new ApplicationRuntimeException("Bank is not exist");

            } else if (instrumentHeader.getBankId() != null && instrumentHeader.getBankId().getId() != null)
                instrumentHeader.setBankId(bankDAO.findById(Integer.valueOf(instrumentHeader.getBankId().getId()),
                        false));
            chequeInstrumenttotal = chequeInstrumenttotal.add(instrumentHeader.getInstrumentAmount());
            instrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
            instrumentHeaderList.add(instrumentHeader);
        }
        return instrumentHeaderList;
    }

    /**
     * This method create a new receipt header object with details contained in given receipt header object. Both the receipt
     * header objects are added to the same parent <code>ReceiptPayeeDetail</code> object which in turn is added to the model.
     *
     * @param oldReceiptHeader the instance of <code>ReceiptHeader</code> whose data is to be copied
     */
    private void populateReceiptModelWithExistingReceiptInfo(final ReceiptHeader oldReceiptHeader) {
        totalAmntToBeCollected = BigDecimal.ZERO;

        receiptHeader = new ReceiptHeader(oldReceiptHeader.getReferencenumber(), oldReceiptHeader.getReferencedate(),
                oldReceiptHeader.getConsumerCode(), oldReceiptHeader.getReferenceDesc(),
                oldReceiptHeader.getTotalAmount(), oldReceiptHeader.getMinimumAmount(),
                oldReceiptHeader.getPartPaymentAllowed(), oldReceiptHeader.getOverrideAccountHeads(),
                oldReceiptHeader.getCallbackForApportioning(), oldReceiptHeader.getDisplayMsg(),
                oldReceiptHeader.getService(), oldReceiptHeader.getCollModesNotAllwd(),
                oldReceiptHeader.getPayeeName(), oldReceiptHeader.getPayeeAddress(), oldReceiptHeader.getPayeeEmail(),
                oldReceiptHeader.getConsumerType());
        if (oldReceiptHeader.getCollModesNotAllwd() != null)
            setCollectionModesNotAllowed(Arrays.asList(oldReceiptHeader.getCollModesNotAllwd().split(",")));
        setOverrideAccountHeads(oldReceiptHeader.getOverrideAccountHeads());
        setPartPaymentAllowed(oldReceiptHeader.getPartPaymentAllowed());
        setServiceName(oldReceiptHeader.getService().getName());

        receiptHeader.setReceiptMisc(new ReceiptMisc(oldReceiptHeader.getReceiptMisc().getBoundary(), oldReceiptHeader
                .getReceiptMisc().getFund(), oldReceiptHeader.getReceiptMisc().getIdFunctionary(), oldReceiptHeader
                .getReceiptMisc().getFundsource(),
                oldReceiptHeader.getReceiptMisc().getDepartment(), receiptHeader,
                oldReceiptHeader.getReceiptMisc().getScheme(), oldReceiptHeader.getReceiptMisc().getSubscheme(), null));
        receiptHeader.setLocation(oldReceiptHeader.getLocation());
        bankCOAList = chartOfAccountsDAO.getBankChartofAccountCodeList();
        for (final ReceiptDetail oldDetail : oldReceiptHeader.getReceiptDetails())
            // debit account heads for revenue accounts should not be considered
            if (oldDetail.getOrdernumber() != null
                    && !FinancialsUtil
                    .isRevenueAccountHead(oldDetail.getAccounthead(), bankCOAList, persistenceService)) {
                final ReceiptDetail receiptDetail = new ReceiptDetail(oldDetail.getAccounthead(),
                        oldDetail.getFunction(), oldDetail.getCramount(), oldDetail.getDramount(),
                        oldDetail.getCramount(), oldDetail.getOrdernumber(), oldDetail.getDescription(),
                        oldDetail.getIsActualDemand(), receiptHeader, oldDetail.getPurpose());
                receiptDetail.setCramountToBePaid(oldDetail.getCramountToBePaid());
                receiptDetail.setCramount(oldDetail.getCramount());
                if (oldDetail.getAccountPayeeDetails() != null)
                    for (final AccountPayeeDetail oldAccountPayeeDetail : oldDetail.getAccountPayeeDetails()) {
                        final AccountPayeeDetail accountPayeeDetail = new AccountPayeeDetail(
                                oldAccountPayeeDetail.getAccountDetailType(),
                                oldAccountPayeeDetail.getAccountDetailKey(), oldAccountPayeeDetail.getAmount(),
                                receiptDetail);
                        receiptDetail.addAccountPayeeDetail(accountPayeeDetail);
                    }

                if (oldDetail.getIsActualDemand())
                    totalAmntToBeCollected = totalAmntToBeCollected.add(oldDetail.getCramountToBePaid())
                            .subtract(oldDetail.getDramount())
                            .setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP);
                setTotalAmntToBeCollected(totalAmntToBeCollected);
                receiptHeader.addReceiptDetail(receiptDetail);
            }

        if (oldReceiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_ADHOC) {
            loadReceiptDetails(receiptHeader);
            createMisc();
            if (oldReceiptHeader.getVoucherNum() != null)
                setVoucherNum(voucherNum);
        }
        setReceiptDetailList(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));
        setCollModesNotAllowedForRemitReceipt(oldReceiptHeader.getCollModesNotAllwd());
    }

    private void loadReceiptDetails(final ReceiptHeader receiptHeader) {
        setReceiptMisc(receiptHeader.getReceiptMisc());
        setBillCreditDetailslist(collectionCommon.setReceiptDetailsList(receiptHeader,
                CollectionConstants.COLLECTIONSAMOUNTTPE_CREDIT));
        setBillRebateDetailslist(collectionCommon.setReceiptDetailsList(receiptHeader,
                CollectionConstants.COLLECTIONSAMOUNTTPE_DEBIT));
        setSubLedgerlist(collectionCommon.setAccountPayeeList(receiptHeader));
    }

    /**
     * Same method handles both view and print modes. If print receipts flag is passed as true, the PDF receipt will be generated
     * in such a way that it will show the print dialog box whenever it is opened.
     *
     * @param printReceipts Flag indicating whether the receipts are to be printed
     * @return Result page ("view")
     */
    private String viewReceipts(final boolean printReceipts) {
        if (selectedReceipts == null || selectedReceipts.length == 0)
            throw new ApplicationRuntimeException("No receipts selected to view!");

        receipts = new ReceiptHeader[selectedReceipts.length];
        for (int i = 0; i < selectedReceipts.length; i++)
            try {
                receipts[i] = receiptHeaderService.findById(selectedReceipts[i], false);
            } catch (final Exception e) {
                LOGGER.error("Error in printReceipts", e);
            }

        try {
            reportId = collectionCommon.generateReport(receipts, printReceipts);
        } catch (final Exception e) {
            final String errMsg = "Error during report generation!";
            LOGGER.error(errMsg, e);
            throw new ApplicationRuntimeException(errMsg, e);
        }

        return CollectionConstants.REPORT;
    }

    @Action(value = "/receipts/receipt-viewReceipts")
    public String viewReceipts() {
        return viewReceipts(false);
    }

    @Action(value = "/receipts/receipt-printReceipts")
    public String printReceipts() {
        return viewReceipts(true);
    }

    @ValidationErrorPage(value = "error")
    @Action(value = "/receipts/receipt-cancel")
    public String cancel() {
        final List<AppConfigValues> appConfigValuesList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.APPCONFIG_VALUE_COLLECTIONCREATORRECEIPTCANCELROLE);
        String value;
        Boolean isRoleToCheckCreator = Boolean.FALSE;
        User user = collectionsUtil.getLoggedInUser();
        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            value = appConfigVal.getValue();
            for (final Role role : user.getRoles())
                if (role != null && role.getName().equals(value))
                    isRoleToCheckCreator = true;
        }

        if (getSelectedReceipts() != null && getSelectedReceipts().length > 0) {
            receipts = new ReceiptHeader[selectedReceipts.length];
            for (int i = 0; i < selectedReceipts.length; i++) {
                receipts[i] = (ReceiptHeader) getPersistenceService().findByNamedQuery("getReceiptHeaderById",
                        Long.valueOf(selectedReceipts[i]));
                if (isRoleToCheckCreator)
                    isReceiptCancelEnable = (receipts[i].getCreatedBy().getId().compareTo(user.getId()) == 0) ? true : false;
            }
        }
        return CANCEL;
    }

    /**
     * This method is invoked when receipt is cancelled
     *
     * @return
     */
    @Action(value = "/receipts/receipt-saveOnCancel")
    public String saveOnCancel() {
        boolean isInstrumentDeposited = false;

        final ReceiptHeader receiptHeaderToBeCancelled = receiptHeaderService.findById(oldReceiptId, false);

        LOGGER.info("Receipt Header to be Cancelled : " + receiptHeaderToBeCancelled.getReceiptnumber());

        for (final InstrumentHeader instrumentHeader : receiptHeaderToBeCancelled.getReceiptInstrument())
            if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                if (instrumentHeader.getStatusId().getDescription()
                        .equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
                    isInstrumentDeposited = true;
                    break;
                }
            } else if (instrumentHeader.getStatusId().getDescription()
                    .equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
                isInstrumentDeposited = true;
                break;
            }

        if (isInstrumentDeposited) {
            // if instrument has been deposited create a new receipt in place of
            // the cancelled

            populateReceiptModelWithExistingReceiptInfo(receiptHeaderToBeCancelled);
            setFundName(receiptHeaderToBeCancelled.getReceiptMisc().getFund().getName());
            setServiceName(receiptHeaderToBeCancelled.getService().getName());
            setServiceId(receiptHeaderToBeCancelled.getService().getId());
            addDropdownData(
                    "serviceList",
                    receiptHeaderToBeCancelled.getService() != null ? getPersistenceService().findAllByNamedQuery(
                            CollectionConstants.QUERY_SERVICE_DETAIL_BY_CATEGORY,
                            receiptHeaderToBeCancelled.getService().getServiceCategory().getId(), Boolean.TRUE)
                            : Collections.emptyList());
            populateBankBranchList(true);
            return NEW;
        } else {
            // if instrument has not been deposited, cancel the old instrument,
            // reverse the
            // voucher and persist
            receiptHeaderToBeCancelled.setStatus(statusDAO.getStatusByModuleAndCode(
                    CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
            receiptHeaderToBeCancelled.setIsReconciled(false);
            receiptHeaderToBeCancelled.setReasonForCancellation(reasonForCancellation);

            for (final InstrumentHeader instrumentHeader : receiptHeaderToBeCancelled.getReceiptInstrument())
                instrumentHeader.setStatusId(statusDAO.getStatusByModuleAndCode(
                        CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
                        CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED));
            for (final ReceiptVoucher receiptVoucher : receiptHeaderToBeCancelled.getReceiptVoucher())
                receiptHeaderService.createReversalVoucher(receiptVoucher);

            receiptHeaderService.persist(receiptHeaderToBeCancelled);

            receiptHeaderValues.clear();
            receiptHeaderValues.add(receiptHeaderToBeCancelled);
            LOGGER.info("Receipt Cancelled with Receipt Number(saveOnCancel): "
                    + receiptHeaderToBeCancelled.getReceiptnumber() + "; Consumer Code: "
                    + receiptHeaderToBeCancelled.getConsumerCode());
        }
        target = "cancel";
        return INDEX;
    }

    public String amountInWords(final BigDecimal amount) {
        return NumberUtil.amountInWords(amount);
    }

    /**
     * @return the receiptHeaderValues
     */
    public List<ReceiptHeader> getReceiptHeaderValues() {
        return receiptHeaderValues;
    }

    /**
     * @param receiptHeaderValues the receiptHeaderValues to set
     */
    public void setReceiptHeaderValues(final List<ReceiptHeader> receiptHeaderValues) {
        this.receiptHeaderValues = receiptHeaderValues;
    }

    public String getReasonForCancellation() {
        return reasonForCancellation;
    }

    public void setReasonForCancellation(final String reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @return the paidBy
     */
    public String getPaidBy() {
        return StringUtils.escapeJavaScript(paidBy);
    }

    /**
     * @param paidBy the paidBy to set
     */
    public void setPaidBy(final String paidBy) {
        this.paidBy = paidBy;
    }

    /**
     * @return the oldReceiptId
     */
    public Long getOldReceiptId() {
        return oldReceiptId;
    }

    /**
     * @param oldReceiptId the oldReceiptId to set
     */
    public void setOldReceiptId(final Long oldReceiptId) {
        this.oldReceiptId = oldReceiptId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(final Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public BigDecimal getTotalAmntToBeCollected() {
        return totalAmntToBeCollected;
    }

    public void setTotalAmntToBeCollected(final BigDecimal totalAmntToBeCollected) {
        this.totalAmntToBeCollected = totalAmntToBeCollected;
    }

    public InstrumentHeader getInstrHeaderCash() {
        return instrHeaderCash;
    }

    public void setInstrHeaderCash(final InstrumentHeader instrHeaderCash) {
        this.instrHeaderCash = instrHeaderCash;
    }

    public InstrumentHeader getInstrHeaderCard() {
        return instrHeaderCard;
    }

    public void setInstrHeaderCard(final InstrumentHeader instrHeaderCard) {
        this.instrHeaderCard = instrHeaderCard;
    }

    public InstrumentHeader getInstrHeaderBank() {
        return instrHeaderBank;
    }

    public void setInstrHeaderBank(final InstrumentHeader instrHeaderBank) {
        this.instrHeaderBank = instrHeaderBank;
    }

    public InstrumentHeader getInstrHeaderOnline() {
        return instrHeaderOnline;
    }

    public void setInstrHeaderOnline(final InstrumentHeader instrHeaderOnline) {
        this.instrHeaderOnline = instrHeaderOnline;
    }

    public List<String> getCollectionModesNotAllowed() {
        return collectionModesNotAllowed;
    }

    public void setCollectionModesNotAllowed(final List<String> collectionModesNotAllowed) {
        this.collectionModesNotAllowed = collectionModesNotAllowed;
    }

    public User getReceiptCreatedByCounterOperator() {
        return receiptCreatedByCounterOperator;
    }

    public void setReceiptCreatedByCounterOperator(final User receiptCreatedByCounterOperator) {
        this.receiptCreatedByCounterOperator = receiptCreatedByCounterOperator;
    }

    public List<ReceiptDetail> getReceiptDetailList() {
        return receiptDetailList;
    }

    public void setReceiptDetailList(final List<ReceiptDetail> receiptDetailList) {
        this.receiptDetailList = receiptDetailList;
    }

    public String getInstrumentTypeCashOrCard() {
        return instrumentTypeCashOrCard;
    }

    public void setInstrumentTypeCashOrCard(final String instrumentTypeCashOrCard) {
        this.instrumentTypeCashOrCard = instrumentTypeCashOrCard;
    }

    /**
     * This getter will be invoked by framework from UI. It returns the total number of bill accounts that are present in the XML
     * arriving from the billing system
     *
     * @return
     */
    public Integer getTotalNoOfAccounts() {
        Integer totalNoOfAccounts = 0;
        totalNoOfAccounts += receiptHeader.getReceiptDetails().size();
        return totalNoOfAccounts;
    }

    /**
     * This getter will be invoked by framework from UI. This value will be used during bill apportioning.
     *
     * @return
     */
    public BigDecimal getMinimumAmount() {
        return null;
    }

    public Boolean getOverrideAccountHeads() {
        return overrideAccountHeads;
    }

    public void setOverrideAccountHeads(final Boolean overrideAccountHeads) {
        this.overrideAccountHeads = overrideAccountHeads;
    }

    public Boolean getCallbackForApportioning() {
        return callbackForApportioning;
    }

    public void setCallbackForApportioning(final Boolean callbackForApportioning) {
        this.callbackForApportioning = callbackForApportioning;
    }

    public Boolean getPartPaymentAllowed() {
        return partPaymentAllowed;
    }

    public void setPartPaymentAllowed(final Boolean partPaymentAllowed) {
        this.partPaymentAllowed = partPaymentAllowed;
    }

    public BigDecimal getCashOrCardInstrumenttotal() {
        return cashOrCardInstrumenttotal;
    }

    public void setCashOrCardInstrumenttotal(final BigDecimal cashOrCardInstrumenttotal) {
        this.cashOrCardInstrumenttotal = cashOrCardInstrumenttotal;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public Boolean getCashAllowed() {
        return cashAllowed;
    }

    public void setCashAllowed(final Boolean cashAllowed) {
        this.cashAllowed = cashAllowed;
    }

    public Boolean getCardAllowed() {
        return cardAllowed;
    }

    public void setCardAllowed(final Boolean cardAllowed) {
        this.cardAllowed = cardAllowed;
    }

    public Boolean getChequeAllowed() {
        return chequeAllowed;
    }

    public void setChequeAllowed(final Boolean chequeAllowed) {
        this.chequeAllowed = chequeAllowed;
    }

    public Boolean getDdAllowed() {
        return ddAllowed;
    }

    public void setDdAllowed(final Boolean ddAllowed) {
        this.ddAllowed = ddAllowed;
    }

    public Boolean getBankAllowed() {
        return bankAllowed;
    }

    public void setBankAllowed(final Boolean bankAllowed) {
        this.bankAllowed = bankAllowed;
    }

    /**
     * @return the voucherDate
     */
    public Date getVoucherDate() {
        return voucherDate;
    }

    /**
     * @param voucherDate the voucherDate to set
     */
    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    /**
     * @return the voucherNumber
     */
    public String getVoucherNum() {
        return voucherNum;
    }

    /**
     * @param voucherNumber the voucherNumber to set
     */
    public void setVoucherNum(final String voucherNum) {
        this.voucherNum = voucherNum;
    }

    /**
     * This getter will be invoked by framework from UI. This value will be used during misc receipts for account details
     *
     * @return
     */
    public List<ReceiptDetailInfo> getBillCreditDetailslist() {
        return billCreditDetailslist;
    }

    public void setBillCreditDetailslist(final List<ReceiptDetailInfo> billCreditDetailslist) {
        this.billCreditDetailslist = billCreditDetailslist;
    }

    public List<ReceiptDetailInfo> getBillRebateDetailslist() {
        return billRebateDetailslist;
    }

    public void setBillRebateDetailslist(final List<ReceiptDetailInfo> billRebateDetailslist) {
        this.billRebateDetailslist = billRebateDetailslist;
    }

    public List<ReceiptDetailInfo> getSubLedgerlist() {
        return subLedgerlist;
    }

    public void setSubLedgerlist(final List<ReceiptDetailInfo> subLedgerlist) {
        this.subLedgerlist = subLedgerlist;
    }

    public String getBillSource() {
        return billSource;
    }

    public void setBillSource(final String billSource) {
        this.billSource = billSource;
    }

    /**
     * @return the receiptPayeeDetailsService
     */
    public ReceiptMisc getReceiptMisc() {
        return receiptMisc;
    }

    public void setReceiptMisc(final ReceiptMisc receiptMisc) {
        this.receiptMisc = receiptMisc;
    }

    /**
     * @return the reportId
     */
    public String getReportId() {
        return reportId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(final String deptId) {
        this.deptId = deptId;
    }

    public BigDecimal getTotalDebitAmount() {
        return totalDebitAmount;
    }

    public void setTotalDebitAmount(final BigDecimal totalDebitAmount) {
        this.totalDebitAmount = totalDebitAmount;
    }

    protected boolean validateData(final List<ReceiptDetailInfo> billDetailslistd,
                                   final List<ReceiptDetailInfo> subLedgerList) {
        BigDecimal totalDrAmt = BigDecimal.ZERO;
        BigDecimal totalCrAmt = BigDecimal.ZERO;
        Integer index = 0;
        boolean isDataValid = true;
        for (final ReceiptDetailInfo rDetails : billDetailslistd) {
            index = index + 1;
            totalDrAmt = totalDrAmt.add(rDetails.getDebitAmountDetail());
            totalCrAmt = totalCrAmt.add(rDetails.getCreditAmountDetail());
            if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getGlcodeDetail().trim().length() == 0) {

                addActionError(getText("miscreceipt.accdetail.emptyaccrow", new String[]{index.toString()}));
                isDataValid = false;
            } else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getGlcodeDetail().trim().length() != 0) {
                addActionError(getText("miscreceipt.accdetail.amountZero", new String[]{rDetails.getGlcodeDetail()}));
                isDataValid = false;
            } else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
                    && rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0) {
                addActionError(getText("miscreceipt.accdetail.amount", new String[]{rDetails.getGlcodeDetail()}));
                isDataValid = false;
            } else if ((rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0 || rDetails
                    .getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
                    && rDetails.getGlcodeDetail().trim().length() == 0) {
                addActionError(getText("miscreceipt.accdetail.accmissing", new String[]{index.toString()}));
                isDataValid = false;
            }

        }
        if (isDataValid)
            isDataValid = validateSubledgerDetails(billCreditDetailslist, subLedgerList);
        return isDataValid;
    }

    protected boolean validateRebateData(final List<ReceiptDetailInfo> billDetailslistd,
                                         final List<ReceiptDetailInfo> subLedgerList) {
        Integer index = 0;
        boolean isDataValid = true;
        for (final ReceiptDetailInfo rDetails : billDetailslistd) {
            index = index + 1;
            if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
                    && rDetails.getGlcodeDetail().trim().length() != 0) {
                addActionError(getText("miscreceipt.accdetail.amountZero", new String[]{rDetails.getGlcodeDetail()}));
                isDataValid = false;
            } else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
                    && rDetails.getGlcodeDetail().trim().length() == 0) {
                addActionError(getText("miscreceipt.accdetail.accmissing", new String[]{index.toString()}));
                isDataValid = false;
            }

        }
        if (isDataValid)
            isDataValid = validateSubledgerDetails(billRebateDetailslist, subLedgerList);
        return isDataValid;
    }

    protected boolean validateSubledgerDetails(final List<ReceiptDetailInfo> billRebateDetailslist,
                                               final List<ReceiptDetailInfo> subLedgerlist) {
        Map<String, Object> accountDetailMap;
        final Map<String, BigDecimal> subledAmtmap = new HashMap<>(0);
        List<Map<String, Object>> subLegAccMap = null; // this list will contain
        // the details about the
        // account coe those are
        // detail codes.
        for (final ReceiptDetailInfo rDetails : billRebateDetailslist) {
            final CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(
                    " from CChartOfAccountDetail" + " where glCodeId=(select id from CChartOfAccounts where glcode=?)",
                    rDetails.getGlcodeDetail());
            if (null != chartOfAccountDetail) {
                accountDetailMap = new HashMap<>();
                accountDetailMap.put("glcodeId", rDetails.getGlcodeIdDetail());
                accountDetailMap.put("glcode", rDetails.getGlcodeDetail());
                if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    accountDetailMap.put("amount", rDetails.getCreditAmountDetail());
                else if (rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0)
                    accountDetailMap.put("amount", rDetails.getDebitAmountDetail());
                if (null == subLegAccMap) {
                    subLegAccMap = new ArrayList<>();
                    subLegAccMap.add(accountDetailMap);
                } else
                    subLegAccMap.add(accountDetailMap);

            }
        }
        if (null != subLegAccMap) {
            final Map<String, String> subLedgerMap = new HashMap<>();
            for (final ReceiptDetailInfo rDetails : subLedgerlist)
                if (rDetails.getGlcode().getId() != 0) {
                    if (null == subledAmtmap.get(rDetails.getGlcode().getId().toString()))
                        subledAmtmap.put(rDetails.getGlcode().getId().toString(), rDetails.getAmount());
                    else {
                        final BigDecimal debitTotalAmount = subledAmtmap.get(rDetails.getGlcode().getId().toString())
                                .add(rDetails.getAmount());
                        subledAmtmap.put(rDetails.getGlcode().getId().toString(), debitTotalAmount);
                    }
                    final StringBuilder subledgerDetailRow = new StringBuilder();
                    subledgerDetailRow.append(rDetails.getGlcode().getId().toString())
                            .append(rDetails.getDetailType().getId().toString())
                            .append(rDetails.getDetailKeyId().toString());
                    if (null == subLedgerMap.get(subledgerDetailRow.toString()))
                        subLedgerMap.put(subledgerDetailRow.toString(), subledgerDetailRow.toString());
                    else {
                        addActionError(getText("miscreciept.samesubledger.repeated"));
                        return false;
                    }

                }
            for (final Map<String, Object> map : subLegAccMap) {
                final String glcodeId = map.get("glcodeId").toString();
                if (null == subledAmtmap.get(glcodeId)) {
                    addActionError(getText("miscreciept.subledger.entrymissing", new String[]{map.get("glcode")
                            .toString()}));
                    return false;
                } else if (subledAmtmap.get(glcodeId).compareTo(new BigDecimal(map.get("amount").toString())) != 0) {
                    addActionError(getText("miscreciept.subledger.amtnotmatchinng", new String[]{map.get("glcode")
                            .toString()}));
                    return false;
                }
            }
        }
        final List<CFinancialYear> list = persistenceService.findAllBy(
                "from CFinancialYear where isActiveForPosting=true and startingDate <= ? and endingDate >= ?",
                getVoucherDate(), getVoucherDate());
        if (list.isEmpty()) {
            addActionError(getText("miscreciept.fYear.notActive"));
            return false;
        }
        return true;
    }

    public void apportionBillAmount() {
        receiptDetailList = collectionCommon.apportionBillAmount(instrumenttotal, (ArrayList) getReceiptDetailList());
    }

    void removeEmptyRows(final List<ReceiptDetailInfo> list) {
        for (final Iterator<ReceiptDetailInfo> detail = list.iterator(); detail.hasNext(); )
            if (detail.next() == null)
                detail.remove();
    }

    public void setFinancialsUtil(final FinancialsUtil financialsUtil) {
        this.financialsUtil = financialsUtil;
    }

    public List<String> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(final List<String> headerFields) {
        this.headerFields = headerFields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(final List<String> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public Integer getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final Integer bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    /**
     * @param collectionCommon the collectionCommon to set
     */
    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    /**
     * @param receiptHeaderService The receipt header service to set
     */
    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public String getCollectXML() {
        return collectXML;
    }

    public void setCollectXML(final String collectXML) {
        this.collectXML = collectXML;
    }

    @Override
    public Object getModel() {
        return receiptHeader;
    }

    public ReceiptHeader[] getReceipts() {
        return receipts;
    }

    public void setReceipts(final ReceiptHeader[] receipts) {
        this.receipts = receipts;
    }

    public Long[] getSelectedReceipts() {
        return selectedReceipts;
    }

    public void setSelectedReceipts(final Long[] selectedReceipts) {
        this.selectedReceipts = selectedReceipts;
    }

    public void setPayeename(final String payeename) {
        this.payeename = payeename;
    }

    public Date getManualReceiptDate() {
        return manualReceiptDate;
    }

    public void setManualReceiptDate(final Date manualReceiptDate) {
        this.manualReceiptDate = manualReceiptDate;
    }

    public Boolean getReceiptBulkUpload() {
        return receiptBulkUpload;
    }

    public void setReceiptBulkUpload(final Boolean receiptBulkUpload) {
        this.receiptBulkUpload = receiptBulkUpload;
    }

    public BigDecimal getInstrumenttotal() {
        return instrumenttotal;
    }

    public void setInstrumenttotal(final BigDecimal instrumenttotal) {
        this.instrumenttotal = instrumenttotal;
    }

    public void setServiceDetailsService(final PersistenceService<ServiceDetails, Long> serviceDetailsService) {
        this.serviceDetailsService = serviceDetailsService;
    }

    public List<InstrumentHeader> getInstrumentProxyList() {
        return instrumentProxyList;
    }

    public void setInstrumentProxyList(final List<InstrumentHeader> instrumentProxyList) {
        this.instrumentProxyList = instrumentProxyList;
    }

    public int getInstrumentCount() {
        return instrumentCount;
    }

    public void setInstrumentCount(final int instrumentCount) {
        this.instrumentCount = instrumentCount;
    }

    /**
     * @return the manualReceiptNumber
     */
    public String getManualReceiptNumber() {
        return manualReceiptNumber;
    }

    /**
     * @param manualReceiptNumber the manualReceiptNumber to set
     */
    public void setManualReceiptNumber(final String manualReceiptNumber) {
        this.manualReceiptNumber = manualReceiptNumber;
    }

    /**
     * @return the manualReceiptNumberAndDateReq
     */
    public Boolean getManualReceiptNumberAndDateReq() {
        return manualReceiptNumberAndDateReq;
    }

    /**
     * @param manualReceiptNumberAndDateReq the manualReceiptNumberAndDateReq to set
     */
    public void setManualReceiptNumberAndDateReq(final Boolean manualReceiptNumberAndDateReq) {
        this.manualReceiptNumberAndDateReq = manualReceiptNumberAndDateReq;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(final ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public void setServiceCategoryService(final PersistenceService<ServiceCategory, Long> serviceCategoryService) {
        this.serviceCategoryService = serviceCategoryService;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Long functionId) {
        this.functionId = functionId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(final Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(final String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Boolean getOnlineAllowed() {
        return onlineAllowed;
    }

    public void setOnlineAllowed(final Boolean onlineAllowed) {
        this.onlineAllowed = onlineAllowed;
    }

    public CollectionService getCollectionService() {
        return collectionService;
    }

    public void setCollectionService(final CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(final PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public Boolean getIsReceiptCancelEnable() {
        return isReceiptCancelEnable;
    }

    public void setIsReceiptCancelEnable(Boolean isReceiptCancelEnable) {
        this.isReceiptCancelEnable = isReceiptCancelEnable;
    }

}
