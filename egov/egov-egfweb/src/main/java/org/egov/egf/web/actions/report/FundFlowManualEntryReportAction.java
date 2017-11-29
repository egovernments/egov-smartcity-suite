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
package org.egov.egf.web.actions.report;


import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Fund;
import org.egov.egf.model.ReportSearch;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.report.FundFlowBean;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Results(value = {
        @Result(name = "PDF", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION,
        "no-cache;filename=ManualEntryReport.pdf" }),
        @Result(name = "XLS", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION,
        "no-cache;filename=ManualEntryReport.xls" })
})
@SuppressWarnings("serial")
@ParentPackage("egov")
@Validation

public class FundFlowManualEntryReportAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = -6817974441103230738L;
    protected ReportSearch reportSearch = new ReportSearch();
    private String selectedAccountNumber;
    public List<Object> manualEntryReportList = new ArrayList<Object>();
    FundFlowBean manualEntry = new FundFlowBean();
    List<FundFlowBean> entryReportList = new ArrayList<FundFlowBean>();
    private InputStream inputStream;
    private ReportHelper reportHelper;
    private StringBuffer heading = new StringBuffer();
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    private static final Logger LOGGER = Logger.getLogger(FundFlowManualEntryReportAction.class);
    private final Map<String, Object> paramMap = new HashMap<String, Object>();
    BigDecimal grandTotal = BigDecimal.ZERO;
    private static final String JASPERPATH = "/reports/templates/manualEntryReport.jasper";
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    @Override
    public Object getModel() {

        return reportSearch;
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        addDropdownData("fundList", masterDataCache.get("egi-fund"));
        addDropdownData("bankList", Collections.EMPTY_LIST);
        addDropdownData("accNumList", Collections.EMPTY_LIST);
    }

    @Action(value = "/report/fundFlowManualEntryReport-newForm")
    public String newForm()
    {
        manualEntryReportList = null;
        return NEW;
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/report/fundFlowManualEntryReport-search")
    public String search() {

        populateReAppropriationData();
        return NEW;
    }

    @SuppressWarnings("unchecked")
    private void populateReAppropriationData() {
        setRelatedEntitesOn();
        getResultList();

    }

    @SuppressWarnings("unchecked")
    public void getResultList() {

        // manualEntryReportList = new ArrayList<Map<String,Object>>();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  getResultList ");
        Date startdt = null;
        Date enddt = null;
        grandTotal = BigDecimal.ZERO;
        try {
            startdt = Constants.DDMMYYYYFORMAT2.parse(reportSearch.getStartDate());
            enddt = Constants.DDMMYYYYFORMAT2.parse(reportSearch.getEndDate());
        } catch (final ParseException e) {
            LOGGER.error("Error in parsing Date ");
        }
        final Criteria critQuery = persistenceService.getSession().createCriteria(FundFlowBean.class)

                .add(Restrictions.between("reportDate", startdt, enddt))
                .add(Restrictions.ne("currentReceipt", BigDecimal.ZERO))
                .add(Restrictions.eq("bankAccountId", BigDecimal.valueOf(reportSearch.getBankAccount().getId())))
                .addOrder(Order.asc("reportDate"));
        entryReportList.addAll(critQuery.list());

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("No of Fund flow Manual entry during the period " + reportSearch.getStartDate()
                    + "to " + reportSearch.getEndDate() + "is " + entryReportList.size());

        for (final FundFlowBean entry : entryReportList)
        {
            manualEntry = new FundFlowBean();
            // manulEntryMap.put("slNo", ++index);
            manualEntry.setReportDate(entry.getReportDate());
            manualEntry.setCurrentReceipt(entry.getCurrentReceipt());
            grandTotal = grandTotal.add(entry.getCurrentReceipt());
            manualEntryReportList.add(manualEntry);
            // }
        }
        getSession().put("entryResultReportList", entryReportList);
        getSession().put("headingStr", heading);
        getSession().put("total", grandTotal);
        getSession().put("manualEntryReportList", manualEntryReportList);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("manualEntryReportList+" + manualEntryReportList.size());
    }

    @SuppressWarnings("unchecked")
    private void populateData() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Getting the value from session");
        entryReportList = (List<FundFlowBean>) getSession().get("entryResultReportList");

        for (final FundFlowBean entry : entryReportList)
        {
            manualEntry = new FundFlowBean();
            manualEntry.setReportDate(entry.getReportDate());
            manualEntry.setCurrentReceipt(entry.getCurrentReceipt());
            manualEntryReportList.add(manualEntry);
        }
        setParamMap();
    }

    protected void setRelatedEntitesOn() {
        if (LOGGER.isInfoEnabled())
            LOGGER.info(" Inside setRelatedEntitesOn:-  Adding heading and getting object values");

        heading.append("Manual Entry Report for ");
        if (reportSearch.getBankAccount() != null && reportSearch.getBankAccount().getId() != null
                && reportSearch.getBankAccount().getId() != 0) {
            reportSearch.setBankAccount((Bankaccount) getPersistenceService().find("from Bankaccount where id=?",
                    Integer.parseInt(reportSearch.getBankAccount().getId().toString())));
            heading.append(" Bank Name -" + reportSearch.getBankAccount().getBankbranch().getBank().getName());
            heading.append(" Account Number-" + reportSearch.getBankAccount().getAccountnumber());
        }
        if (reportSearch.getStartDate() != null && reportSearch.getEndDate() != null)
            heading.append(" From " + reportSearch.getStartDate() + " to " + reportSearch.getEndDate());
    }

    public FundFlowManualEntryReportAction() {
        addRelatedEntity("fund", Fund.class);
        addRelatedEntity("bankAccount", Bankaccount.class);
        addRelatedEntity("bankbranch", Bankbranch.class);
        addRelatedEntity("bank", Bank.class);
    }

    @SuppressWarnings("unchecked")
    public String getUlbName() {
        final Query query = persistenceService.getSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    public void setParamMap()
    {
        final String str = getSession().get("headingStr").toString();
        final BigDecimal amt = (BigDecimal) getSession().get("total");
        getUlbName();
        paramMap.put("title", getUlbName());
        paramMap.put("heading", str);
        paramMap.put("grandTotal", amt);

    }

    protected Map<String, Object> getParamMap() {
        return paramMap;
    }

    public String generatePdf() throws Exception {
        populateData();
        inputStream = reportHelper.exportPdf(inputStream, JASPERPATH, getParamMap(), manualEntryReportList);

        return "PDF";
    }

    public String generateXls() throws Exception {
        populateData();
        inputStream = reportHelper.exportXls(inputStream, JASPERPATH, getParamMap(), manualEntryReportList);
        return "XLS";
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    public ReportSearch getReportSearch() {
        return reportSearch;
    }

    public void setReportSearch(final ReportSearch reportSearch) {
        this.reportSearch = reportSearch;
    }

    public StringBuffer getHeading() {
        return heading;
    }

    public String getSelectedAccountNumber() {
        return selectedAccountNumber;
    }

    public void setSelectedAccountNumber(final String selectedAccountNumber) {
        this.selectedAccountNumber = selectedAccountNumber;
    }

    public void setHeading(final StringBuffer heading) {
        this.heading = heading;
    }

    public List<Object> getManualEntryReportList() {
        return manualEntryReportList;
    }

    public List<FundFlowBean> getEntryReportList() {
        return entryReportList;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setManualEntryReportList(
            final List<Object> manualEntryReportList) {
        this.manualEntryReportList = manualEntryReportList;
    }

    public void setEntryReportList(final List<FundFlowBean> entryReportList) {
        this.entryReportList = entryReportList;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(final BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

}