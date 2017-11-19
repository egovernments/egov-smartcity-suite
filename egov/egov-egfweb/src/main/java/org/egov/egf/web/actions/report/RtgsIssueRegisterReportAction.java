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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.model.BankAdviceReportInfo;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Results(value = {
        @Result(name = "search", location = "rtgsIssueRegisterReport-search.jsp"),
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=RtgsIssueRegisterReport.pdf" }),
        @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/xls", "contentDisposition", "no-cache;filename=RtgsIssueRegisterReport.xls" }),
        @Result(name = "HTML", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "text/html" })
})
@ParentPackage("egov")
public class RtgsIssueRegisterReportAction extends ReportAction {

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RtgsIssueRegisterReportAction.class);
    private Date fromDate = new Date();
    private Date toDate = new Date();
    private Bankaccount bankaccount;
    BankAdviceReportInfo bankAdviceInfo;
    private InputStream inputStream;
    private ReportHelper reportHelper;
    String jasperpath = "/reports/templates/rtgsIssueRegisterReportAction.jasper";
    private StringBuffer header = new StringBuffer();
    List<BankAdviceReportInfo> rtgsDisplayList = new ArrayList<BankAdviceReportInfo>();
    List<Object> rtgsReportList = new ArrayList<Object>();
    Map<String, Object> paramMap = new HashMap<String, Object>();
    Boolean searchResult = Boolean.FALSE;
    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();

        addDropdownData("bankList", persistenceService.findAllBy("from Bank where isactive=true order by upper(name)"));
        addDropdownData("bankBranchList", Collections.EMPTY_LIST);
        addDropdownData("bankAccountList", Collections.EMPTY_LIST);
        addDropdownData("accNumList", Collections.EMPTY_LIST);
        addDropdownData("chequeNumberList", Collections.EMPTY_LIST);
        finYearDate();
        mandatoryFields.clear();

    }

    @ValidationErrorPage("search")
    @Action(value = "/report/rtgsIssueRegisterReport-newForm")
    public String newForm() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(fromDate);
            LOGGER.info(toDate);
        }
        return "search";
    }

    private String getUlbName() {
        return ReportUtil.getCityName();
    }

    @SkipValidation
    @Action(value = "/report/rtgsIssueRegisterReport-exportPdf")
    public String exportPdf() throws JRException, IOException {
        search();
        if (rtgsDisplayList.size() > 0) {
            inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), rtgsReportList);
            return "PDF";
        }
        prepare();
        return newForm();
    }

    @SkipValidation
    @Action(value = "/report/rtgsIssueRegisterReport-exportHtml")
    public String exportHtml() {
        search();
        if (rtgsDisplayList.size() > 0) {
            inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), rtgsReportList,
                    JRHtmlExporterParameter.SIZE_UNIT_POINT);
            return "HTML";
        }
        addActionMessage("No data found ");
        prepare();
        return "search";
    }

    @SkipValidation
    @Action(value = "/report/rtgsIssueRegisterReport-exportXls")
    public String exportXls() throws JRException, IOException {
        search();
        if (rtgsDisplayList.size() > 0) {
            inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), rtgsReportList);
            return "XLS";
        }
        prepare();
        return newForm();

    }

    protected Map<String, Object> getParamMap() {

        String fundAndBankHeading = "";
        String dateRange = "";
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = new Date();
        String newFromDate = "";
        String newToDate = "";
        String reportRundate = "";
        fundAndBankHeading = "RTGS Register for "
                + persistenceService.find("select name from Fund where id = ?", Integer.parseInt(parameters.get("fundId")[0]))
                        .toString();

        if (null != parameters.get("rtgsAssignedFromDate")[0] && !parameters.get("rtgsAssignedFromDate")[0].equalsIgnoreCase(""))
            dateRange = "from " + parameters.get("rtgsAssignedFromDate")[0];
        else {
            newFromDate = dateFormat.format(fromDate);
            dateRange = "from " + newFromDate;
        }
        if (null != parameters.get("rtgsAssignedToDate")[0] && !parameters.get("rtgsAssignedToDate")[0].equalsIgnoreCase(""))
            dateRange = dateRange + " to " + parameters.get("rtgsAssignedToDate")[0];
        else {
            newToDate = dateFormat.format(toDate);
            dateRange = dateRange + " to " + newToDate;
        }
        reportRundate = dateFormat.format(date);
        paramMap.put("fundAndBankHeading", fundAndBankHeading);
        paramMap.put("dateRange", dateRange);
        paramMap.put("reportRundate", reportRundate);
        paramMap.put("ulbName", getUlbName());
        paramMap.put("rtgsDetailsList", rtgsDisplayList);
        paramMap.put("rtgsReportList", rtgsReportList);
        return paramMap;
    }

    public void finYearDate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Getting Starting date of financial year ");
        final CFinancialYear date = financialYearDAO.getFinancialYearByDate(new Date());
        persistenceService.getSession().setReadOnly(date, true);
        fromDate = date.getStartingDate();
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    @SuppressWarnings("unchecked")
    @ValidationErrorPage(NEW)
    @ReadOnly
    @Action(value = "/report/rtgsIssueRegisterReport-search")
    public String search() {
        searchResult = Boolean.TRUE;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Seraching RTGS result for given criteria ");

        final Query query = persistenceService.getSession().createSQLQuery(getQueryString().toString())
                .addScalar("ihId", BigDecimalType.INSTANCE)
                .addScalar("rtgsNumber")
                .addScalar("rtgsDate")
                .addScalar("vhId", BigDecimalType.INSTANCE)
                .addScalar("paymentNumber")
                .addScalar("paymentDate")
                .addScalar("paymentAmount")
                .addScalar("department")
                .addScalar("status")
                .addScalar("bank")
                .addScalar("bankBranch")
                .addScalar("dtId", BigDecimalType.INSTANCE)
                .addScalar("dkId", BigDecimalType.INSTANCE)
                .addScalar("accountNumber");
        if (null == parameters.get("rtgsAssignedFromDate")[0] || parameters.get("rtgsAssignedFromDate")[0].equalsIgnoreCase(""))
            query.setDate("finStartDate", new java.sql.Date(fromDate.getTime()));
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Search Query ------------>" + query);

        query.setResultTransformer(Transformers.aliasToBean(BankAdviceReportInfo.class));
        rtgsDisplayList = query.list();
        populateSubLedgerDetails();
        rtgsReportList.addAll(rtgsDisplayList);
        return "search";
    }

    private StringBuffer getQueryString() {
        StringBuffer queryString = new StringBuffer();
        String deptQry = "";
        String fundQry = "";
        String phQry = "";
        StringBuffer bankQry = new StringBuffer("");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuffer instrumentHeaderQry = new StringBuffer("");
        try {
            if (null != parameters.get("departmentid")[0] && !parameters.get("departmentid")[0].equalsIgnoreCase("-1"))
                deptQry = " AND vmis.departmentid     =" + parameters.get("departmentid")[0];
            if (null != parameters.get("rtgsAssignedFromDate")[0]
                    && !parameters.get("rtgsAssignedFromDate")[0].equalsIgnoreCase(""))

                instrumentHeaderQry = instrumentHeaderQry.append(" and   ih.transactiondate >='"
                        + dateFormat.format(formatter.parse(parameters.get("rtgsAssignedFromDate")[0])) + "'");
            else
                instrumentHeaderQry = instrumentHeaderQry.append(" and   ih.transactiondate >=:finStartDate");
            if (null != parameters.get("rtgsAssignedToDate")[0] && !parameters.get("rtgsAssignedToDate")[0].equalsIgnoreCase(""))
                instrumentHeaderQry = instrumentHeaderQry.append(" and   ih.transactiondate  <='"
                        + dateFormat.format(formatter.parse(parameters.get("rtgsAssignedToDate")[0])) + "'");
            if (null != parameters.get("bank")[0] && !parameters.get("bank")[0].equals("-1")
                    && !parameters.get("bank")[0].equalsIgnoreCase(""))
                bankQry = bankQry.append(" AND b.id = " + parameters.get("bank")[0]);
            if (null != parameters.get("bankbranch.id")[0] && !parameters.get("bankbranch.id")[0].equals("-1")
                    && !parameters.get("bankbranch.id")[0].equalsIgnoreCase(""))
                bankQry = bankQry.append(" AND branch.id=" + parameters.get("bankbranch.id")[0]);
            if (null != parameters.get("bankaccount.id")[0] && !parameters.get("bankaccount.id")[0].equals("-1")
                    && !parameters.get("bankaccount.id")[0].equalsIgnoreCase("")) {
                phQry = " AND ph.bankaccountnumberid=" + parameters.get("bankaccount.id")[0];
                instrumentHeaderQry = instrumentHeaderQry.append(" and   ih.bankaccountid ="
                        + parameters.get("bankaccount.id")[0]);
            }
            if (null != parameters.get("instrumentnumber")[0] && !parameters.get("instrumentnumber")[0].equalsIgnoreCase(""))
                instrumentHeaderQry = instrumentHeaderQry.append(" and   ih.transactionnumber ='"
                        + parameters.get("instrumentnumber")[0] + "'");
            if (null != parameters.get("fundId")[0] && !parameters.get("fundId")[0].equalsIgnoreCase(""))
                fundQry = " AND vh.fundId            =" + parameters.get("fundId")[0];

            queryString = queryString
                    .append(" SELECT ih.id as ihId , ih.transactionnumber as rtgsNumber,  ih.transactiondate as rtgsDate, vh.id as vhId,  vh.vouchernumber as paymentNumber,"
                            +
                            " to_char(vh.voucherdate,'dd/mm/yyyy') as paymentDate,   gld.detailtypeid as dtId,  gld.detailkeyid as dkId,   gld.amount as paymentAmount,"
                            +
                            " dept.name as department,   stat.description as status,b.name as bank,branch.branchname as bankBranch, ba.accountnumber as accountNumber FROM Paymentheader ph, voucherheader vh,vouchermis vmis,bankaccount ba,bankbranch branch,bank b,generalledger gl,generalledgerdetail gld,"
                            +
                            " egf_instrumentvoucher iv,  egf_instrumentheader ih,  eg_department dept ,egw_status stat WHERE "
                            +
                            " ph.voucherheaderid   =vh.id AND vmis.voucherheaderid = vh.id "
                            + bankQry.toString()
                            + "  AND ih.bankaccountid = ba.id and branch.id = ba.branchid and branch.bankid = b.id and vh.status = 0 "
                            +
                            fundQry
                            + phQry
                            + " and stat.id= ih.id_status "
                            +
                            " AND dept.id = vmis.departmentid "
                            + deptQry
                            + "  and lower(ph.type)=lower('rtgs') "
                            + instrumentHeaderQry.toString()
                            +
                            " AND IV.VOUCHERHEADERID  IS NOT NULL AND iv.voucherheaderid   =vh.id AND ih.instrumentnumber IS NULL "
                            +
                            " AND ih.id = iv.instrumentheaderid "
                            +
                            " AND vh.type   = 'Payment' and gl.voucherheaderid = vh.id and gld.generalledgerid = gl.id GROUP BY ih.id , ih.transactionnumber,"
                            +
                            " ih.transactiondate, vh.id,  vh.vouchernumber,vh.voucherDate, vmis.departmentid,  dept.name, b.name,branch.branchname,ba.accountnumber,stat.description,gld.detailtypeid,gld.detailkeyid,gld.amount ORDER BY b.name,branch.branchname,ba.accountnumber,ih.transactiondate,ih.transactionnumber,dept.name");
        } catch (ParseException e) {

        }
        return queryString;
    }

    private void populateSubLedgerDetails() {

        final Map<Integer, List<EntityType>> subLedgerList = new HashMap<Integer, List<EntityType>>();
        final Map<Integer, List<Long>> detailTypeMapForGetEntitys = new HashMap<Integer, List<Long>>();
        for (final BankAdviceReportInfo bankAdviceReportInfo : rtgsDisplayList)
            if (detailTypeMapForGetEntitys.get(bankAdviceReportInfo.getDtId().intValue()) == null)
            {
                detailTypeMapForGetEntitys.put(bankAdviceReportInfo.getDtId().intValue(), new ArrayList<Long>());
                detailTypeMapForGetEntitys.get(bankAdviceReportInfo.getDtId().intValue()).add(
                        bankAdviceReportInfo.getDkId().longValue());
            } else
                detailTypeMapForGetEntitys.get(bankAdviceReportInfo.getDtId().intValue()).add(
                        bankAdviceReportInfo.getDkId().longValue());

        for (final Integer keyGroup : detailTypeMapForGetEntitys.keySet())
            try
            {
                final List<EntityType> subDetail = new ArrayList<EntityType>();
                final Accountdetailtype detailType = (Accountdetailtype) persistenceService.find(
                        "from Accountdetailtype where id=? order by name", keyGroup);
                final String table = detailType.getFullQualifiedName();
                final Class<?> service = Class.forName(table);
                String simpleName = service.getSimpleName();
                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

                final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                        .getServletContext());
                final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
                final List<Long> entityIds = new ArrayList<Long>(detailTypeMapForGetEntitys.get(keyGroup));
                int size = entityIds.size();
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(entityService + " size " + size);
                if (size > 999)
                {
                    int fromIndex = 0;
                    int toIndex = 0;
                    final int step = 1000;

                    while (size - step >= 0)
                    {
                        toIndex += step;
                        final List<EntityType> returnList = (List<EntityType>) entityService.getEntitiesById(entityIds.subList(
                                fromIndex, toIndex));

                        if (returnList != null)
                            subDetail.addAll(returnList);

                        fromIndex = toIndex;
                        size -= step;

                    }

                    if (size > 0)
                    {
                        fromIndex = toIndex;
                        toIndex = fromIndex + size;
                        final List<EntityType> returnList = (List<EntityType>) entityService.getEntitiesById(entityIds.subList(
                                fromIndex, toIndex));
                        if (returnList != null)
                            subDetail.addAll(returnList);
                    }

                    subLedgerList.put(keyGroup, subDetail);
                } else
                {
                    subDetail.addAll(entityService.getEntitiesById(entityIds));
                    subLedgerList.put(keyGroup, subDetail);
                }

            } catch (final ClassCastException e) {
                LOGGER.error(e);
            } catch (final Exception e)
            {
                LOGGER.error("Exception to get EntityType=" + e.getMessage());
            }
        List<EntityType> subDetail = new ArrayList<EntityType>();

        for (final Integer keyGroup : subLedgerList.keySet())
            for (final BankAdviceReportInfo bankAdviceReportInfo : rtgsDisplayList)
                if (bankAdviceReportInfo.getDtId() != null)
                    if (keyGroup.equals(bankAdviceReportInfo.getDtId().intValue())) {

                        subDetail = subLedgerList.get(keyGroup);

                        for (final EntityType entityType : subDetail)
                            if (bankAdviceReportInfo.getDtId() != null)
                                if (entityType.getEntityId().equals(bankAdviceReportInfo.getDkId().intValue()))
                                    if (entityType != null)
                                        bankAdviceReportInfo.setPartyName(entityType.getName().toUpperCase());
                    }
        for (final BankAdviceReportInfo bankAdviceReportInfo : rtgsDisplayList)
            if (bankAdviceReportInfo.getStatus().equalsIgnoreCase("new"))
                bankAdviceReportInfo.setStatus("Assigned");
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Bankaccount getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(final Bankaccount bankaccount) {
        this.bankaccount = bankaccount;
    }

    public void setHeader(final StringBuffer header) {
        this.header = header;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public StringBuffer getHeader() {
        return header;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public List<BankAdviceReportInfo> getRtgsDisplayList() {
        return rtgsDisplayList;
    }

    public void setRtgsDisplayList(final List<BankAdviceReportInfo> rtgsDisplayList) {
        this.rtgsDisplayList = rtgsDisplayList;
    }

    public List<Object> getRtgsReportList() {
        return rtgsReportList;
    }

    public void setRtgsReportList(final List<Object> rtgsReportList) {
        this.rtgsReportList = rtgsReportList;
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    public Boolean getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(final Boolean searchResult) {
        this.searchResult = searchResult;
    }

}