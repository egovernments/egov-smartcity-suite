/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.collection.web.actions.receipts;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.DishonoredChequeBean;
import org.egov.collection.entity.DishonoredChequeForm;
import org.egov.collection.integration.services.DishonorChequeActionHelper;
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

@Results({ @Result(name = DishonoredChequeAction.SEARCH, location = "dishonoredCheque-search.jsp"),
        @Result(name = DishonoredChequeAction.SUCCESS, location = "dishonoredCheque-success.jsp"),
        @Result(name = "process", location = "dishonoredCheque-process.jsp"),
        @Result(name = "accountList", location = "dishonoredCheque-accountList.jsp") })
@ParentPackage("egov")
public class DishonoredChequeAction extends SearchFormAction {

    private static final long serialVersionUID = 2871716607884152080L;
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);
    private static final String PAYMENT = "Payment";
    private static final String RECEIPT = "Receipt";
    private static final String JOURNAL_VOUCHER = "Journal Voucher";
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
    private String receiptGLDetails;
    private String remittanceGLDetails;
    private Date transactionDate;
    private String dishonorReason;
    private String remarks;
    private String instrumentMode;
    private String referenceNo;
    private Long accountNumber;
    private EgovPaginatedList paginatedList;
    private List<DishonoredChequeForm> generalLedger = new ArrayList<DishonoredChequeForm>();
    private List<DishonoredChequeForm> subLedgerDetails = new ArrayList<DishonoredChequeForm>();
    private List<DishonoredChequeForm> remittanceGeneralLedger = new ArrayList<DishonoredChequeForm>();
    protected List<DishonoredChequeBean> dishonoredChequeDisplayList = new ArrayList<DishonoredChequeBean>(0);
    @Autowired
    private ReceiptHeaderService receiptHeaderService;
    @Autowired
    private DishonorChequeActionHelper dishonorChequeActionHelper;
    private BigDecimal reversalAmount;

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData(CollectionConstants.DROPDOWN_DATA_BANKBRANCH_LIST, bankBranchHibernateDAO.getAllBankBranchs());
        addDropdownData(CollectionConstants.DROPDOWN_DATA_ACCOUNT_NO_LIST, Collections.EMPTY_LIST);
        addDropdownData(CollectionConstants.DROPDOWN_DATA_DISHONOR_REASONS_LIST, (List<String>) persistenceService.getSession()
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
        if (!bankBranchId.equals("-1") && bankBranchId != null && bankBranchId != "") {
            final String id[] = bankBranchId.split("-");
            bankId = Long.parseLong(id[0]);
        }
        final InstrumentType instType = (InstrumentType) getPersistenceService().find(
                "from InstrumentType where type=?", instrumentMode);
        final String searchQuery = receiptHeaderService.getReceiptHeaderforDishonor(instType.getId(), accountNumber,
                bankId, chequeNumber, chequeDate.toString());
        final String srchQry = "select vh.id as voucherHeaderId,vh.vouchernumber as voucherNumber, rpt.id as receiptheaderid,ih.id as instrumentheaderid,rpt.receiptnumber as receiptnumber,rpt.receiptdate as receiptdate,ih.instrumentnumber as instrumentnumber,"
                + "ih.instrumentdate as instrumentdate,ih.instrumentamount as instrumentamount,b.name as bankname,ba.accountnumber as accountnumber,ih.payto as payto,status.description as description "
                + searchQuery + " ORDER BY rpt.receiptnumber, rpt.receiptdate ";
        final String countQry = "select count(distinct rpt) " + searchQuery + "";
        return new SearchQuerySQL(srchQry, countQry, null);

    }

    @ValidationErrorPage(value = "process")
    @Action(value = "/receipts/dishonoredCheque-create")
    public String create() throws Exception {
        try {
            DishonoredChequeForm chequeForm = new DishonoredChequeForm();
            chequeForm.setTransactionDate(transactionDate);
            chequeForm.setDishonorReason(dishonorReason);
            chequeForm.setReferenceNo(referenceNo);
            chequeForm.setRemarks(remarks);
            chequeForm.setInstHeaderIds(instHeaderIds);
            chequeForm.setVoucherHeaderIds(voucherHeaderIds);
            chequeForm.setReceiptGLDetails(receiptGLDetails);
            chequeForm.setRemittanceGLDetails(remittanceGLDetails);

            dishonorChequeActionHelper.createDishonorCheque(chequeForm);
        } catch (final ValidationException e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            e.printStackTrace();
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

        for (final Object[] object : list) {
            final DishonoredChequeBean chequeBean = new DishonoredChequeBean();
            chequeBean.setVoucherHeaderId(getLongValue(object[0]));
            chequeBean.setVoucherNumber(getStringValue(object[1]));
            chequeBean.setReceiptHeaderid(getLongValue(object[2]));
            chequeBean.setInstrumentHeaderid(getLongValue(object[3]));
            chequeBean.setReceiptNumber(getStringValue(object[4]));
            chequeBean.setReceiptDate(getDateValue(object[5]));
            chequeBean.setInstrumentNumber(getStringValue(object[6]));
            chequeBean.setInstrumentDate(getDateValue(object[7]));
            chequeBean.setInstrumentAmount(getBigDecimalValue(object[8]));
            chequeBean.setBankName(getStringValue(object[9]));
            chequeBean.setAccountNumber(getStringValue(object[10]));
            chequeBean.setPayTo(getStringValue(object[11]));
            chequeBean.setDescription(getStringValue(object[12]));

            dishonoredChequeDisplayList.add(chequeBean);
        }
        paginatedList.setList(dishonoredChequeDisplayList);
        LOGGER.debug("Exiting from prepareResults");
    }

    @Action(value = "/receipts/dishonoredCheque-process")
    public String process() throws Exception {
        getReversalGlCodes();
        return "process";
    }

    /**
     * Populates all the glcodes for which reversal gl entries have to be made fetches all glcodes with creditamount > 0 for
     * receipt and fetches all glcodes with debitamount > 0 for all others(payment,contra)
     */
    @SuppressWarnings("unchecked")
    public void getReversalGlCodes() {
        List<Object[]> glCodes = new ArrayList<Object[]>();
        List<Object[]> glCodescredit = new ArrayList<Object[]>();
        List<Object[]> slDetailsCredit = new ArrayList<Object[]>();
        List<Object[]> slDetailsDebit = new ArrayList<Object[]>();
        List<Object[]> remittanceDetailsCredit = new ArrayList<Object[]>();
        List<Object[]> instrumentDetails = new ArrayList<Object[]>();
        reversalAmount = (BigDecimal) persistenceService
                .find("select sum(instrumentAmount) from InstrumentHeader where id in (" + instHeaderIds + ")");
        glCodescredit = persistenceService
                .findAllBy("select gl.voucherHeaderId.id ,gl.glcodeId.id, gl.glcode,gl.glcodeId.name, sum(gl.creditAmount),sum(gl.debitAmount),gl.functionId from CGeneralLedger gl where gl.voucherHeaderId in("
                        + voucherHeaderIds
                        + ") and gl.debitAmount<>0 and gl.creditAmount=0 and gl.glcode not in (select glcode from CChartOfAccounts where purposeId in (select id from AccountCodePurpose where name='Cheque In Hand')) group by gl.voucherHeaderId.id ,gl.glcodeId.id,gl.glcode,gl.glcodeId.name,gl.functionId order by gl.glcode");
        glCodes = persistenceService
                .findAllBy("select gl.voucherHeaderId.id ,gl.glcodeId.id,gl.glcode,gl.glcodeId.name,sum(gl.creditAmount),sum(gl.debitAmount),gl.functionId  from CGeneralLedger gl where gl.voucherHeaderId in("
                        + voucherHeaderIds
                        + ") and gl.creditAmount<>0 and gl.debitAmount=0 group by gl.voucherHeaderId.id ,gl.glcodeId.id, gl.glcode,gl.glcodeId.name,gl.functionId order by gl.glcode");
        glCodes.addAll(glCodescredit);

        String reversalGlCodesStr = "";
        for (Object[] gl : glCodes) {
            DishonoredChequeForm detail = new DishonoredChequeForm();
            detail.setVoucherHeaderId(getStringValue(gl[0]));
            detail.setGlcodeId(getStringValue(gl[1]));
            detail.setGlcode(getStringValue(gl[2]));
            detail.setDescription(getStringValue(gl[3]));
            detail.setDebitAmount(getStringValue(gl[4]));
            detail.setCreditAmount(getStringValue(gl[5]));
            detail.setFunctionId(getStringValue(gl[6]));
            generalLedger.add(detail);
            if (reversalGlCodesStr.equalsIgnoreCase("")) {
                reversalGlCodesStr = "'" + getStringValue(gl[2]) + "'";
            } else {
                reversalGlCodesStr = reversalGlCodesStr + "," + "'" + getStringValue(gl[2]) + "'";
            }
        }

        remittanceDetailsCredit = persistenceService
                .findAllBy("select gl.voucherHeaderId.id ,gl.glcodeId.id, gl.glcode,gl.glcodeId.name, sum(gl.creditAmount),sum(gl.debitAmount),gl.functionId from CGeneralLedger gl ,InstrumentOtherDetails iod where gl.voucherHeaderId.id = iod.payinslipId.id and iod.instrumentHeaderId.id   in ("
                        + instHeaderIds
                        + ") and gl.debitAmount<>0 and gl.creditAmount=0  group by gl.voucherHeaderId.id ,gl.glcodeId.id,gl.glcode,gl.glcodeId.name,gl.functionId order by gl.glcode");
        for (Object[] gl : remittanceDetailsCredit) {
            DishonoredChequeForm detail = new DishonoredChequeForm();
            detail.setVoucherHeaderId(getStringValue(gl[0]));
            detail.setGlcodeId(getStringValue(gl[1]));
            detail.setGlcode(getStringValue(gl[2]));
            detail.setDescription(getStringValue(gl[3]));
            detail.setDebitAmount(getStringValue(gl[4]));
            detail.setCreditAmount(getStringValue(gl[5]));
            detail.setFunctionId(getStringValue(gl[6]));
            remittanceGeneralLedger.add(detail);
        }
        instrumentDetails = persistenceService
                .getSession()
                .createSQLQuery(
                        "select vh.id as voucherHeaderId,vh.vouchernumber as voucherNumber, rpt.id as receiptheaderid,ih.id as instrumentheaderid,rpt.receiptnumber as receiptnumber,rpt.receiptdate as receiptdate,ih.instrumentnumber as instrumentnumber,"
                                + "ih.instrumentdate as instrumentdate,ih.instrumentamount as instrumentamount,b.name as bankname,ba.accountnumber as accountnumber,ih.payto as payto,status.description as description from voucherheader vh,egf_instrumentvoucher iv,egcl_collectionheader rpt,egcl_collectioninstrument ci,egf_instrumentheader ih,egw_status status,bank b,"
                                + "bankbranch bb,bankaccount ba where  iv.voucherheaderid=vh.id and iv.instrumentheaderid = ih.id  and rpt.id = ci.collectionheader AND ci.instrumentheader = ih.id AND status.id = ih.id_status "
                                + "AND b.id = bb.bankid AND bb.id = ba.branchid AND ba.id = ih.bankaccountid and ih.id in  ("
                                + instHeaderIds
                                + ")").list();
        dishonoredChequeDisplayList = new ArrayList<DishonoredChequeBean>(0);
        for (Object[] object : instrumentDetails) {
            final DishonoredChequeBean chequeBean = new DishonoredChequeBean();
            chequeBean.setVoucherHeaderId(getLongValue(object[0]));
            chequeBean.setVoucherNumber(getStringValue(object[1]));
            chequeBean.setReceiptHeaderid(getLongValue(object[2]));
            chequeBean.setInstrumentHeaderid(getLongValue(object[3]));
            chequeBean.setReceiptNumber(getStringValue(object[4]));
            chequeBean.setReceiptDate(getDateValue(object[5]));
            chequeBean.setInstrumentNumber(getStringValue(object[6]));
            chequeBean.setInstrumentDate(getDateValue(object[7]));
            chequeBean.setInstrumentAmount(getBigDecimalValue(object[8]));
            chequeBean.setBankName(getStringValue(object[9]));
            chequeBean.setAccountNumber(getStringValue(object[10]));
            chequeBean.setPayTo(getStringValue(object[11]));
            chequeBean.setDescription(getStringValue(object[12]));
            dishonoredChequeDisplayList.add(chequeBean);
        }
        /*
         * slDetailsCredit = persistenceService
         * .findAllBy("select distinct gl.glcode, gd.detailTypeId.id, gd.detailKeyId,SUM(gd.amount)" +
         * " from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in(" + voucherHeaderIds + ")" +
         * " and gl.id = gd.generalLedgerId and gl.debitAmount >0 and gl.glcode in (" + reversalGlCodesStr +
         * ") group by gl.glcode, gd.detailTypeId.id, gd.detailKeyId"); for (Object[] sl : slDetailsCredit) { DishonoredChequeForm
         * detail = new DishonoredChequeForm(); detail.setGlcode(getStringValue(sl[0]));
         * detail.setDetailTypeId(getStringValue(sl[1])); detail.setDetailKeyId(getStringValue(sl[2]));
         * detail.setAmount(getStringValue(sl[3])); subLedgerDetails.add(detail); } slDetailsDebit = persistenceService
         * .findAllBy("select distinct gl.glcode, gd.detailTypeId.id, gd.detailKeyId,SUM(gd.amount)" +
         * " from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in(" + voucherHeaderIds + ")" +
         * " and gl.id = gd.generalLedgerId and gl.creditAmount >0 and gl.glcode in (" + reversalGlCodesStr +
         * ") group by gl.glcode, gd.detailTypeId.id, gd.detailKeyId"); for (Object[] sl : slDetailsDebit) { DishonoredChequeForm
         * detail = new DishonoredChequeForm(); detail.setGlcode(getStringValue(sl[0]));
         * detail.setDetailTypeId(getStringValue(sl[1])); detail.setDetailKeyId(getStringValue(sl[2]));
         * detail.setAmount(getStringValue(sl[3])); subLedgerDetails.add(detail); }
         */
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

    public void setVoucherHeaderIds(String voucherHeaderIds) {
        this.voucherHeaderIds = voucherHeaderIds;
    }

    public List<DishonoredChequeForm> getGeneralLedger() {
        return generalLedger;
    }

    public void setGeneralLedger(List<DishonoredChequeForm> generalLedger) {
        this.generalLedger = generalLedger;
    }

    public List<DishonoredChequeForm> getSubLedgerDetails() {
        return subLedgerDetails;
    }

    public void setSubLedgerDetails(List<DishonoredChequeForm> subLedgerDetails) {
        this.subLedgerDetails = subLedgerDetails;
    }

    public BigDecimal getReversalAmount() {
        return reversalAmount;
    }

    public void setReversalAmount(BigDecimal reversalAmount) {
        this.reversalAmount = reversalAmount;
    }

    public List<DishonoredChequeForm> getRemittanceGeneralLedger() {
        return remittanceGeneralLedger;
    }

    public void setRemittanceGeneralLedger(List<DishonoredChequeForm> remittanceGeneralLedger) {
        this.remittanceGeneralLedger = remittanceGeneralLedger;
    }

    public String getReceiptGLDetails() {
        return receiptGLDetails;
    }

    public void setReceiptGLDetails(String receiptGLDetails) {
        this.receiptGLDetails = receiptGLDetails;
    }

    public String getRemittanceGLDetails() {
        return remittanceGLDetails;
    }

    public void setRemittanceGLDetails(String remittanceGLDetails) {
        this.remittanceGLDetails = remittanceGLDetails;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDishonorReason() {
        return dishonorReason;
    }

    public void setDishonorReason(String dishonorReason) {
        this.dishonorReason = dishonorReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<DishonoredChequeBean> getDishonoredChequeDisplayList() {
        return dishonoredChequeDisplayList;
    }

    public void setDishonoredChequeDisplayList(List<DishonoredChequeBean> dishonoredChequeDisplayList) {
        this.dishonoredChequeDisplayList = dishonoredChequeDisplayList;
    }

}
