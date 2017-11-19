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
package org.egov.collection.web.actions.receipts;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.DishonoredChequeBean;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.services.DishonorChequeService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.model.instrument.InstrumentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Results({ @Result(name = DishonoredChequeAction.SEARCH, location = "dishonoredCheque-search.jsp"),
        @Result(name = DishonoredChequeAction.SUCCESS, location = "dishonoredCheque-success.jsp"),
        @Result(name = "process", location = "dishonoredCheque-process.jsp"),
        @Result(name = "accountList", location = "dishonoredCheque-accountList.jsp") })
@ParentPackage("egov")
public class DishonoredChequeAction extends SearchFormAction {

    private static final long serialVersionUID = 2871716607884152080L;
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public static final String SEARCH = "search";
    private List bankBranchList;
    @Autowired
    private BankBranchHibernateDAO bankBranchHibernateDAO;
    @Autowired
    private BankaccountHibernateDAO bankaccountHibernateDAO;
    private String bankBranchId;
    private List accountNumberList;
    private Map instrumentModesMap;
    private String chequeNumber;
    private Date chequeDate;
    private String instHeaderIds;
    private String voucherHeaderIds;
    private String receiptHeaderIds;
    private String receiptGLDetails;
    private String remittanceGLDetails;
    private Date transactionDate;
    private String dishonorReason;
    private String remarks;
    private String instrumentMode;
    private String referenceNo;
    private Long accountNumber;
    private EgovPaginatedList paginatedList;
    private List<DishonoredChequeBean> generalLedger = new ArrayList<DishonoredChequeBean>(0);
    private List<DishonoredChequeBean> subLedgerDetails = new ArrayList<DishonoredChequeBean>(0);
    private List<DishonoredChequeBean> remittanceGeneralLedger = new ArrayList<DishonoredChequeBean>(0);
    protected List<DishonoredChequeBean> dishonoredChequeDisplayList = new ArrayList<DishonoredChequeBean>(0);
    @Autowired
    private ReceiptHeaderService receiptHeaderService;
    @Autowired
    private DishonorChequeService dishonorChequeService;
    private BigDecimal reversalAmount;

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData(CollectionConstants.DROPDOWN_DATA_BANKBRANCH_LIST, bankBranchHibernateDAO.getAllBankBranchs());
        addDropdownData(CollectionConstants.DROPDOWN_DATA_ACCOUNT_NO_LIST, Collections.EMPTY_LIST);
        addDropdownData(CollectionConstants.DROPDOWN_DATA_DISHONOR_REASONS_LIST, persistenceService.getSession()
                .createSQLQuery("select * from egf_instrument_dishonor_reason").list());

        instrumentModesMap = CollectionConstants.INSTRUMENT_MODES_MAP;
    }

    @Action(value = "/receipts/dishonoredCheque-getAccountNumbers")
    public String getAccountNumbers() {
        try {
            Long branchId = null;
            if (!bankBranchId.equals("-1") && bankBranchId != null && bankBranchId != "") {
                final String id[] = bankBranchId.split("-");
                branchId = Long.parseLong(id[1]);
            }
            accountNumberList = bankaccountHibernateDAO.getBankAccountByBankBranch(branchId.intValue());
        } catch (final Exception ex) {
            LOGGER.error("Exception Encountered!!!" + ex.getMessage(), ex);
        }
        return "accountList";
    }

    @Override
    @SkipValidation
    @Action(value = "/receipts/dishonoredCheque-search")
    public String search() {
        return SEARCH;
    }

    @Action(value = "/receipts/dishonoredCheque-list")
    public String list() throws Exception {
        setPageSize(30);
        super.search();
        prepareResults();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("DishonoredChequeAction | list | End");
        return SEARCH;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {

        Long bankId = null;
        if (isNotBlank(bankBranchId) && !bankBranchId.equals("-1")) {
            final String id[] = bankBranchId.split("-");
            bankId = Long.parseLong(id[0]);
        }
        final InstrumentType instType = (InstrumentType) getPersistenceService().find(
                "from InstrumentType where type=?", instrumentMode);
        final String searchQuery = receiptHeaderService.getReceiptHeaderforDishonor(instType.getId(), accountNumber,
                bankId, chequeNumber, chequeDate.toString());
        final String srchQry = "select rpt.id as receiptheaderid,ih.id as instrumentheaderid,rpt.receiptnumber as receiptnumber,rpt.receiptdate as receiptdate,ih.instrumentnumber as instrumentnumber,"
                + "ih.instrumentdate as instrumentdate,ih.instrumentamount as instrumentamount,b.name as bankname,ba.accountnumber as accountnumber,ih.payto as payto,status.description as description "
                + searchQuery + " ORDER BY rpt.receiptnumber, rpt.receiptdate ";
        final String countQry = "select count(distinct rpt) " + searchQuery + "";
        return new SearchQuerySQL(srchQry, countQry, null);

    }

    @ValidationErrorPage(value = "process")
    @Action(value = "/receipts/dishonoredCheque-create")
    public String create() throws Exception {
        try {
            final DishonoredChequeBean chequeForm = new DishonoredChequeBean();
            chequeForm.setTransactionDate(transactionDate);
            chequeForm.setDishonorReason(dishonorReason);
            chequeForm.setReferenceNo(referenceNo);
            chequeForm.setRemarks(remarks);
            chequeForm.setInstHeaderIds(instHeaderIds);
            chequeForm.setVoucherHeaderIds(voucherHeaderIds);
            chequeForm.setReceiptHeaderIds(receiptHeaderIds);
            chequeForm.setReceiptGLDetails(receiptGLDetails);
            chequeForm.setRemittanceGLDetails(remittanceGLDetails);

            dishonorChequeService.createDishonorCheque(chequeForm);
        } catch (final ValidationException e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            LOGGER.error("Error in DishonorCheque >>>>" + e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return SUCCESS;
    }

    private void prepareResults() {
        LOGGER.debug("Entering into prepareResults");
        paginatedList = (EgovPaginatedList) searchResult;
        final List<Object[]> list = paginatedList.getList();
        paginatedList.setList(populateDishonorChequeBean(list));
        LOGGER.debug("Exiting from prepareResults");
    }

    public List<DishonoredChequeBean> populateDishonorChequeBean(final List<Object[]> list) {
        Long receiptId;
        List<DishonoredChequeBean> dishonoredChequeList = new ArrayList<DishonoredChequeBean>();
        for (final Object[] object : list) {
            receiptId = getLongValue(object[0]);
            final DishonoredChequeBean chequeBean = new DishonoredChequeBean();
            final ReceiptVoucher receiptVoucher = (ReceiptVoucher) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_RECEIPT_VOUCHER_BY_RECEIPTID, receiptId);
            if (receiptVoucher != null) {
                chequeBean.setVoucherHeaderId(receiptVoucher.getVoucherheader().getId());
                chequeBean.setVoucherNumber(receiptVoucher.getVoucherheader().getVoucherNumber());
            }
            chequeBean.setReceiptHeaderid(receiptId);
            chequeBean.setInstrumentHeaderid(getLongValue(object[1]));
            chequeBean.setReceiptNumber(getStringValue(object[2]));
            chequeBean.setReceiptDate(getDateValue(object[3]));
            chequeBean.setInstrumentNumber(getStringValue(object[4]));
            chequeBean.setInstrumentDate(getDateValue(object[5]));
            chequeBean.setInstrumentAmount(getBigDecimalValue(object[6]));
            chequeBean.setBankName(getStringValue(object[7]));
            chequeBean.setAccountNumber(getStringValue(object[8]));
            chequeBean.setPayTo(getStringValue(object[9]));
            chequeBean.setDescription(getStringValue(object[10]));
            dishonoredChequeList.add(chequeBean);
        }
        return dishonoredChequeList;
    }

    @Action(value = "/receipts/dishonoredCheque-process")
    public String process() throws Exception {
        getReversalGlCodes();
        return "process";
    }

    /**
     * Populates all the glcodes for which reversal rd entries have to be made
     * fetches all glcodes with creditamount > 0 for receipt and fetches all
     * glcodes with debitamount > 0 for all others(payment,contra)
     */
    @SuppressWarnings("unchecked")
    public void getReversalGlCodes() {
        List<Object[]> glCodes = new ArrayList<Object[]>(0);
        List<Object[]> glCodescredit = new ArrayList<Object[]>(0);
        List<Object[]> remittanceDetailsCredit = new ArrayList<Object[]>(0);
        List<Object[]> instrumentDetails = new ArrayList<Object[]>(0);
        reversalAmount = (BigDecimal) persistenceService
                .find("select sum(instrumentAmount) from InstrumentHeader where id in (" + instHeaderIds + ")");
        glCodescredit = persistenceService
                .findAllBy("select rh.id ,accounthead.id, accounthead.glcode,accounthead.name, sum(rd.cramount),sum(rd.dramount),function.id from ReceiptDetail rd  inner join rd.accounthead as accounthead inner join rd.receiptHeader as rh inner join rd.function as function where rh.id in("
                        + receiptHeaderIds
                        + ") and rd.dramount<>0 and rd.cramount=0 and accounthead.glcode not in (select glcode from CChartOfAccounts where purposeId in (select id from AccountCodePurpose where name='Cheque In Hand')) group by rh.id ,accounthead.id,accounthead.glcode,accounthead.name,function.id order by accounthead");

        glCodes = persistenceService
                .findAllBy("select rh.id ,accounthead.id, accounthead.glcode,accounthead.name, sum(rd.cramount),sum(rd.dramount),function.id from ReceiptDetail rd  inner join rd.accounthead as accounthead inner join rd.receiptHeader as rh inner join rd.function as function where rh.id in("
                        + receiptHeaderIds
                        + ") and rd.cramount<>0 and rd.dramount=0  group by rh.id ,accounthead.id,accounthead.glcode,accounthead.name,function.id order by accounthead");
        glCodes.addAll(glCodescredit);

        String reversalGlCodesStr = "";
        for (final Object[] rd : glCodes) {
            final DishonoredChequeBean detail = new DishonoredChequeBean();
            final ReceiptVoucher receiptVoucher = (ReceiptVoucher) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_RECEIPT_VOUCHER_BY_RECEIPTID, getLongValue(rd[0]));
            if (receiptVoucher != null) {
                detail.setVoucherHeaderId(receiptVoucher.getVoucherheader().getId());
            }
            detail.setGlcodeId(getStringValue(rd[1]));
            detail.setGlcode(getStringValue(rd[2]));
            detail.setDescription(getStringValue(rd[3]));
            detail.setDebitAmount(getStringValue(rd[4]));
            detail.setCreditAmount(getStringValue(rd[5]));
            detail.setFunctionId(getStringValue(rd[6]));
            generalLedger.add(detail);
            if (reversalGlCodesStr.equalsIgnoreCase(""))
                reversalGlCodesStr = "'" + getStringValue(rd[2]) + "'";
            else
                reversalGlCodesStr = reversalGlCodesStr + "," + "'" + getStringValue(rd[2]) + "'";
        }
        remittanceDetailsCredit = persistenceService
                .findAllBy("select rh.id ,accounthead.id, accounthead.glcode,accounthead.name, sum(rd.cramount),sum(rd.dramount),function.id from ReceiptDetail rd  inner join rd.accounthead as accounthead inner join rd.receiptHeader as rh inner join rd.function as function inner join rh.receiptInstrument ri where ri.id in ("
                        + instHeaderIds
                        + ") and rd.dramount<>0 and rd.cramount=0  group by rh.id ,accounthead.id,accounthead.glcode,accounthead.name,function.id order by accounthead");
        for (final Object[] rd : remittanceDetailsCredit) {
            final DishonoredChequeBean detail = new DishonoredChequeBean();
            final ReceiptVoucher receiptVoucher = (ReceiptVoucher) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_RECEIPT_VOUCHER_BY_RECEIPTID, getLongValue(rd[0]));
            if (receiptVoucher != null) {
                detail.setVoucherHeaderId(receiptVoucher.getVoucherheader().getId());
            }
            detail.setGlcodeId(getStringValue(rd[1]));
            detail.setGlcode(getStringValue(rd[2]));
            detail.setDescription(getStringValue(rd[3]));
            detail.setDebitAmount(getStringValue(rd[4]));
            detail.setCreditAmount(getStringValue(rd[5]));
            detail.setFunctionId(getStringValue(rd[6]));
            remittanceGeneralLedger.add(detail);
        }
        instrumentDetails = persistenceService
                .getSession()
                .createSQLQuery(
                        "select rpt.id as receiptheaderid,ih.id as instrumentheaderid,rpt.receiptnumber as receiptnumber,rpt.receiptdate as receiptdate,ih.instrumentnumber as instrumentnumber,"
                                + "ih.instrumentdate as instrumentdate,ih.instrumentamount as instrumentamount,b.name as bankname,ba.accountnumber as accountnumber,ih.payto as payto,status.description as description from egcl_collectionheader rpt,egcl_collectioninstrument ci,egf_instrumentheader ih,egw_status status,bank b,"
                                + "bankbranch bb,bankaccount ba where rpt.id = ci.collectionheader AND ci.instrumentheader = ih.id AND status.id = ih.id_status "
                                + "AND b.id = bb.bankid AND bb.id = ba.branchid AND ba.id = ih.bankaccountid and ih.id in  ("
                                + instHeaderIds + ")").list();
        dishonoredChequeDisplayList = populateDishonorChequeBean(instrumentDetails);
    }

    protected String getStringValue(final Object object) {
        return object != null ? object.toString() : "";
    }

    protected String getDateValue(final Object object) {

        return object != null ? formatter.format((Date) object) : "";
    }

    protected Long getLongValue(final Object object) {

        return object != null ? Long.valueOf(object.toString()) : null;
    }

    private BigDecimal getBigDecimalValue(final Object object) {
        return object != null ? new BigDecimal(object.toString()).setScale(2) : BigDecimal.ZERO.setScale(2);
    }

    public List getBankBranchList() {
        return bankBranchList;
    }

    public void setBankBranchList(final List bankBranchList) {
        this.bankBranchList = bankBranchList;
    }

    public Map getInstrumentModesMap() {
        return instrumentModesMap;
    }

    public void setInstrumentModesMap(final Map instrumentModesList) {
        instrumentModesMap = instrumentModesList;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public List getAccountNumberList() {
        return accountNumberList;
    }

    public void setAccountNumberList(final List accountNumberList) {
        this.accountNumberList = accountNumberList;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(final Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public String getInstrumentMode() {
        return instrumentMode;
    }

    public void setInstrumentMode(final String instrumentMode) {
        this.instrumentMode = instrumentMode;
    }

    public String getInstHeaderIds() {
        return instHeaderIds;
    }

    public void setInstHeaderIds(final String instHeaderIds) {
        this.instHeaderIds = instHeaderIds;
    }

    public String getVoucherHeaderIds() {
        return voucherHeaderIds;
    }

    public void setVoucherHeaderIds(final String voucherHeaderIds) {
        this.voucherHeaderIds = voucherHeaderIds;
    }

    public List<DishonoredChequeBean> getGeneralLedger() {
        return generalLedger;
    }

    public void setGeneralLedger(final List<DishonoredChequeBean> generalLedger) {
        this.generalLedger = generalLedger;
    }

    public List<DishonoredChequeBean> getSubLedgerDetails() {
        return subLedgerDetails;
    }

    public void setSubLedgerDetails(final List<DishonoredChequeBean> subLedgerDetails) {
        this.subLedgerDetails = subLedgerDetails;
    }

    public BigDecimal getReversalAmount() {
        return reversalAmount;
    }

    public void setReversalAmount(final BigDecimal reversalAmount) {
        this.reversalAmount = reversalAmount;
    }

    public List<DishonoredChequeBean> getRemittanceGeneralLedger() {
        return remittanceGeneralLedger;
    }

    public void setRemittanceGeneralLedger(final List<DishonoredChequeBean> remittanceGeneralLedger) {
        this.remittanceGeneralLedger = remittanceGeneralLedger;
    }

    public String getReceiptGLDetails() {
        return receiptGLDetails;
    }

    public void setReceiptGLDetails(final String receiptGLDetails) {
        this.receiptGLDetails = receiptGLDetails;
    }

    public String getRemittanceGLDetails() {
        return remittanceGLDetails;
    }

    public void setRemittanceGLDetails(final String remittanceGLDetails) {
        this.remittanceGLDetails = remittanceGLDetails;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDishonorReason() {
        return dishonorReason;
    }

    public void setDishonorReason(final String dishonorReason) {
        this.dishonorReason = dishonorReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(final String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(final Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<DishonoredChequeBean> getDishonoredChequeDisplayList() {
        return dishonoredChequeDisplayList;
    }

    public void setDishonoredChequeDisplayList(final List<DishonoredChequeBean> dishonoredChequeDisplayList) {
        this.dishonoredChequeDisplayList = dishonoredChequeDisplayList;
    }

    public String getReceiptHeaderIds() {
        return receiptHeaderIds;
    }

    public void setReceiptHeaderIds(final String receiptHeaderIds) {
        this.receiptHeaderIds = receiptHeaderIds;
    }

}
