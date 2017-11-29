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

package org.egov.egf.web.actions.brs;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.receipt.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({
    @Result(name = DishonoredChequeAction.SEARCH, location = "dishonoredCheque-search.jsp"),
    @Result(name = DishonoredChequeAction.SUCCESS, location = "dishonoredCheque-success.jsp")
})
public class DishonoredChequeAction extends SearchFormAction {

    private static final long serialVersionUID = 1998083631926900402L;
    public static final String SEARCH = "search";
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private String bankBranchId;
    private Long accountCodes;
    private String instrumentMode;
    private String chequeNo;
    private Date chqDDDate;
    private EgovPaginatedList paginatedList;
    @Autowired
    @Qualifier("receiptService")
    private ReceiptService receiptService;
    @Autowired
    private InstrumentService instrumentService;
    private String installmentIds;
    protected List<DishonoredChequeBean> dishonoredChequeDisplayList = new ArrayList<DishonoredChequeBean>();

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    public void prepare()
    {
        super.prepare();
        addDropdownData("bankBranchList",
                persistenceService.findAllBy("select bb from Bankbranch bb where bb.isactive=true order by bb.bank.name"));
        final AjaxDishonoredAction ajaxDishonoredAction = new AjaxDishonoredAction();
        ajaxDishonoredAction.setPersistenceService(getPersistenceService());
        populateAccountCodes(ajaxDishonoredAction);
    }

    public List getBankBranch() {
        
        return null;
    }

    private void populateAccountCodes(final AjaxDishonoredAction ajaxDishonoredAction) {
        if (bankBranchId != null && bankBranchId != "-1" && bankBranchId != "") {
            ajaxDishonoredAction.setBankBranchId(bankBranchId);
            ajaxDishonoredAction.populateAccountCodes();
            addDropdownData("accountCodeList", ajaxDishonoredAction.getBankAccountList());
        } else
            addDropdownData("accountCodeList", Collections.emptyList());
    }

    @Actions({
        @Action(value = "/brs/dishonoredCheque-search")
    })
    public String show() {
        return SEARCH;
    }

    @Action(value = "/brs/dishonoredCheque-list")
    public String list() throws Exception {
        setPageSize(30);
        super.search();
        prepareResults();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("EBConsumerAction | list | End");
        return SEARCH;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {

        Long bankId = null;
        if (!bankBranchId.equals("-1") && bankBranchId != null && bankBranchId != "") {
            final String id[] = bankBranchId.split("-");
            bankId = Long.parseLong(id[0]);
        }
        final String searchQuery = receiptService.getReceiptHeaderforDishonor(instrumentMode, accountCodes, bankId, chequeNo,
                chqDDDate.toString());
        final String srchQry = "select rpt.id as receiptheaderid,ih.id as instrumentheaderid,rpt.receiptnumber as receiptnumber,rpt.receiptdate as receiptdate,ih.instrumentnumber as instrumentnumber,"
                + "ih.instrumentdate as instrumentdate,ih.instrumentamount as instrumentamount,b.name as bankname,ba.accountnumber as accountnumber,ih.payto as payto,status.description as description "
                + searchQuery + " ORDER BY rpt.receiptnumber, rpt.receiptdate ";
        final String countQry = "select count(distinct rpt) " + searchQuery + "";
        return new SearchQuerySQL(srchQry, countQry, null);

    }

    @Action(value = "/brs/dishonoredCheque-dishonorCheque")
    public String dishonorCheque() throws Exception {
        final String installmentIdsStr[] = installmentIds.split(",");
        for (final String installmentIdStr : installmentIdsStr) {
            InstrumentHeader ih = new InstrumentHeader();
            ih = (InstrumentHeader) getPersistenceService().find("from InstrumentHeader where id=?",
                    Long.valueOf(installmentIdStr));
            instrumentService.cancelInstrument(ih);
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

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public Long getAccountCodes() {
        return accountCodes;
    }

    public void setAccountCodes(final Long accountCodes) {
        this.accountCodes = accountCodes;
    }

    public String getInstrumentMode() {
        return instrumentMode;
    }

    public void setInstrumentMode(final String instrumentMode) {
        this.instrumentMode = instrumentMode;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(final String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public Date getChqDDDate() {
        return chqDDDate;
    }

    public void setChqDDDate(final Date chqDDDate) {
        this.chqDDDate = chqDDDate;
    }

    public String getInstallmentIds() {
        return installmentIds;
    }

    public void setInstallmentIds(final String installmentIds) {
        this.installmentIds = installmentIds;
    }

    public EgovPaginatedList getPaginatedList() {
        return paginatedList;
    }

    public void setPaginatedList(final EgovPaginatedList paginatedList) {
        this.paginatedList = paginatedList;
    }

    public List<DishonoredChequeBean> getDishonoredChequeDisplayList() {
        return dishonoredChequeDisplayList;
    }

    public void setDishonoredChequeDisplayList(
            final List<DishonoredChequeBean> dishonoredChequeDisplayList) {
        this.dishonoredChequeDisplayList = dishonoredChequeDisplayList;
    }

}