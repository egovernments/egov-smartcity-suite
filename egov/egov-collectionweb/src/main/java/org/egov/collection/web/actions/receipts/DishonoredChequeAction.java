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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.web.actions.bean.DishonoredChequeBean;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentType;
import org.springframework.beans.factory.annotation.Autowired;

@Results({ @Result(name = DishonoredChequeAction.SEARCH, location = "dishonoredCheque-search.jsp"),
        @Result(name = DishonoredChequeAction.SUCCESS, location = "dishonoredCheque-success.jsp"),
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
    public PersistenceService<InstrumentHeader, Long> instrumentHeaderService;
    private String instHeaderIds;
    private String instrumentMode;
    private Long accountNumber;
    private EgovPaginatedList paginatedList;
    protected List<DishonoredChequeBean> dishonoredChequeDisplayList = new ArrayList<DishonoredChequeBean>();
    private ReceiptHeaderService receiptHeaderService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData(CollectionConstants.DROPDOWN_DATA_BANKBRANCH_LIST, bankBranchHibernateDAO.getAllBankBranchs());
        addDropdownData(CollectionConstants.DROPDOWN_DATA_ACCOUNT_NO_LIST, new ArrayList());
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
        final String srchQry = "select rpt.id as receiptheaderid,ih.id as instrumentheaderid,rpt.receiptnumber as receiptnumber,rpt.receiptdate as receiptdate,ih.instrumentnumber as instrumentnumber,"
                + "ih.instrumentdate as instrumentdate,ih.instrumentamount as instrumentamount,b.name as bankname,ba.accountnumber as accountnumber,ih.payto as payto,status.description as description "
                + searchQuery + " ORDER BY rpt.receiptnumber, rpt.receiptdate ";
        final String countQry = "select count(distinct rpt) " + searchQuery + "";
        return new SearchQuerySQL(srchQry, countQry, null);

    }

    @Action(value = "/receipts/dishonoredCheque-dishonorCheque")
    public String dishonorCheque() throws Exception {
        final String installmentIdsStr[] = instHeaderIds.split(",");
        for (final String installmentIdStr : installmentIdsStr) {
            final InstrumentHeader iHeader = (InstrumentHeader) getPersistenceService().find(
                    "from InstrumentHeader where id=?", Long.valueOf(installmentIdStr));
            final EgwStatus statusDishonored = egwStatusDAO.getStatusByModuleAndCode(
                    CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_DISHONORED_STATUS);
            iHeader.setStatusId(statusDishonored);
            final ReceiptHeader receiptHeader = (ReceiptHeader) getPersistenceService().find(
                    "select DISTINCT (receipt) from org.egov.collection.entity.ReceiptHeader receipt "
                + "join receipt.receiptInstrument as instruments where instruments.id=?", Long.valueOf(installmentIdStr));
            final EgwStatus statusBounced = egwStatusDAO.getStatusByModuleAndCode(
                    CollectionConstants.MODULE_NAME_RECEIPTHEADER,
                    CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED);
            receiptHeader.setStatus(statusBounced);
            instrumentHeaderService.update(iHeader);
            receiptHeaderService.update(receiptHeader);
        }
        return SUCCESS;
    }

    private void prepareResults() {

        LOGGER.debug("Entering into prepareResults");
        paginatedList = (EgovPaginatedList) searchResult;
        final List<Object[]> list = paginatedList.getList();

        for (final Object[] object : list) {
            final DishonoredChequeBean chequeBean = new DishonoredChequeBean();
            chequeBean.setReceiptHeaderid(getLongValue(object[0]));
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

            dishonoredChequeDisplayList.add(chequeBean);
        }
        paginatedList.setList(dishonoredChequeDisplayList);
        LOGGER.debug("Exiting from prepareResults");
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

    public ReceiptHeaderService getReceiptHeaderService() {
        return receiptHeaderService;
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

    public PersistenceService<InstrumentHeader, Long> getInstrumentHeaderService() {
        return instrumentHeaderService;
    }

    public void setInstrumentHeaderService(final PersistenceService<InstrumentHeader, Long> instrumentHeaderService) {
        this.instrumentHeaderService = instrumentHeaderService;
    }

    public String getInstHeaderIds() {
        return instHeaderIds;
    }

    public void setInstHeaderIds(String instHeaderIds) {
        this.instHeaderIds = instHeaderIds;
    }

}
