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


import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.egf.model.Statement;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.services.report.IncomeExpenditureScheduleService;
import org.egov.services.report.IncomeExpenditureService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@ParentPackage("egov")
@Results({
    @Result(name = "report", location = "incomeExpenditureReport-report.jsp"),
    @Result(name = "scheduleResults", location = "incomeExpenditureReport-scheduleResults.jsp"),
    @Result(name = "allScheduleResults", location = "incomeExpenditureReport-allScheduleResults.jsp"),
    @Result(name = "results", location = "incomeExpenditureReport-results.jsp"),
    @Result(name = "PDF", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
            Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION,
    "no-cache;filename=IncomeExpenditureStatement.pdf" }),
    @Result(name = "XLS", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
            Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION,
    "no-cache;filename=IncomeExpenditureStatement.xls" })
})
public class IncomeExpenditureReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 91711010096900620L;
    private static final String INCOME_EXPENSE_PDF = "PDF";
    private static final String INCOME_EXPENSE_XLS = "XLS";
    private static SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    InputStream inputStream;
    ReportHelper reportHelper;
    Statement incomeExpenditureStatement = new Statement();
    IncomeExpenditureService incomeExpenditureService;
    IncomeExpenditureScheduleService incomeExpenditureScheduleService;
    private String majorCode;
    private String minorCode;
    private String scheduleNo;
    private String financialYearId;
    // private String asOndate;
    private Date todayDate;
    private String asOnDateRange;
    private String period;
    private Integer fundId;
    private final StringBuffer heading = new StringBuffer();
    private StringBuffer scheduleheading = new StringBuffer();
    private StringBuffer statementheading = new StringBuffer();
    List<CChartOfAccounts> listChartOfAccounts;
    private boolean detailReport = false;
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public void setIncomeExpenditureService(final IncomeExpenditureService incomeExpenditureService) {
        this.incomeExpenditureService = incomeExpenditureService;
    }

    public void setIncomeExpenditureScheduleService(final IncomeExpenditureScheduleService incomeExpenditureScheduleService) {
        this.incomeExpenditureScheduleService = incomeExpenditureScheduleService;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Statement getIncomeExpenditureStatement() {
        return incomeExpenditureStatement;
    }

    public IncomeExpenditureReportAction() {
        addRelatedEntity("department", Department.class);
        addRelatedEntity("function", CFunction.class);
        addRelatedEntity("functionary", Functionary.class);
        addRelatedEntity("financialYear", CFinancialYear.class);
        addRelatedEntity("field", Boundary.class);
        addRelatedEntity("fund", Fund.class);
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        if (!parameters.containsKey("showDropDown")) {
            addDropdownData("departmentList", masterDataCache.get("egi-department"));
            addDropdownData("functionList", masterDataCache.get("egi-function"));
            addDropdownData("functionaryList", masterDataCache.get("egi-functionary"));
            addDropdownData("fundDropDownList", masterDataCache.get("egi-fund"));
            addDropdownData("fieldList", masterDataCache.get("egi-ward"));
            addDropdownData("financialYearList",
                    getPersistenceService().findAllBy("from CFinancialYear where isActive=true  order by finYearRange desc "));
        }
    }

    protected void setRelatedEntitesOn() {
        setTodayDate(new Date());
        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0) {
            incomeExpenditureStatement.setFund((Fund) getPersistenceService().find("from Fund where id=?",
                    incomeExpenditureStatement.getFund().getId()));
            heading.append(" in " + incomeExpenditureStatement.getFund().getName());
        }
        if (incomeExpenditureStatement.getDepartment() != null && incomeExpenditureStatement.getDepartment().getId() != null
                && incomeExpenditureStatement.getDepartment().getId() != 0) {
            incomeExpenditureStatement.setDepartment((Department) getPersistenceService().find("from Department where id=?",
                    incomeExpenditureStatement.getDepartment().getId()));
            heading.append(" in " + incomeExpenditureStatement.getDepartment().getName() + " Department");
        } else
            incomeExpenditureStatement.setDepartment(null);
        if (incomeExpenditureStatement.getFinancialYear() != null
                && incomeExpenditureStatement.getFinancialYear().getId() != null
                && incomeExpenditureStatement.getFinancialYear().getId() != 0) {
            incomeExpenditureStatement.setFinancialYear((CFinancialYear) getPersistenceService().find(
                    "from CFinancialYear where id=?", incomeExpenditureStatement.getFinancialYear().getId()));
            heading.append(" for the Financial Year " + incomeExpenditureStatement.getFinancialYear().getFinYearRange());
        }
        if (incomeExpenditureStatement.getFunction() != null && incomeExpenditureStatement.getFunction().getId() != null
                && incomeExpenditureStatement.getFunction().getId() != 0) {
            incomeExpenditureStatement.setFunction((CFunction) getPersistenceService().find("from CFunction where id=?",
                    incomeExpenditureStatement.getFunction().getId()));
            heading.append(" in Function Code " + incomeExpenditureStatement.getFunction().getName());
        }
        if (incomeExpenditureStatement.getField() != null && incomeExpenditureStatement.getField().getId() != null
                && incomeExpenditureStatement.getField().getId() != 0) {
            incomeExpenditureStatement.setField((Boundary) getPersistenceService().find("from Boundary where id=?",
                    incomeExpenditureStatement.getField().getId()));
            heading.append(" in the field value" + incomeExpenditureStatement.getField().getName());
        }

        if (incomeExpenditureStatement.getFunctionary() != null && incomeExpenditureStatement.getFunctionary().getId() != null
                && incomeExpenditureStatement.getFunctionary().getId() != 0) {
            incomeExpenditureStatement.setFunctionary((Functionary) getPersistenceService().find("from Functionary where id=?",
                    incomeExpenditureStatement.getFunctionary().getId()));
            heading.append(" and " + incomeExpenditureStatement.getFunctionary().getName() + " Functionary");
        }

    }

    public void setIncomeExpenditureStatement(final Statement incomeExpenditureStatement) {
        this.incomeExpenditureStatement = incomeExpenditureStatement;
    }

    @Override
    public Object getModel() {
        return incomeExpenditureStatement;
    }

    @Action(value = "/report/incomeExpenditureReport-generateIncomeExpenditureReport")
    public String generateIncomeExpenditureReport() {
        return "report";
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateIncomeExpenditureSubReport")
    public String generateIncomeExpenditureSubReport() {
        setDetailReport(false);
        populateDataSourceForSchedule();
        return "scheduleResults";
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateScheduleReport")
    public String generateScheduleReport() {
        populateDataSourceForAllSchedules();
        return "allScheduleResults";
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateDetailCodeReport")
    public String generateDetailCodeReport() {
        setDetailReport(true);
        populateSchedulewiseDetailCodeReport();
        return "scheduleResults";
    }

    private void populateSchedulewiseDetailCodeReport() {
        setRelatedEntitesOn();
        scheduleheading.append("Income And Expenditure Schedule Statement").append(heading);
        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0) {
            final List<Fund> fundlist = new ArrayList<Fund>();
            fundlist.add(incomeExpenditureStatement.getFund());
            incomeExpenditureStatement.setFunds(fundlist);
            incomeExpenditureScheduleService.populateDetailcode(incomeExpenditureStatement);

        } else {
            incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
            incomeExpenditureScheduleService.populateDetailcode(incomeExpenditureStatement);
        }
    }


    private void populateDataSourceForSchedule() {
        setDetailReport(false);
        setRelatedEntitesOn();

        scheduleheading.append("Income And Expenditure Schedule Statement").append(heading);
        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0) {
            final List<Fund> fundlist = new ArrayList<Fund>();
            fundlist.add(incomeExpenditureStatement.getFund());
            incomeExpenditureStatement.setFunds(fundlist);
            incomeExpenditureScheduleService.populateDataForLedgerSchedule(incomeExpenditureStatement,
                    parameters.get("majorCode")[0]);

        } else {
            incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
            incomeExpenditureScheduleService.populateDataForLedgerSchedule(incomeExpenditureStatement,
                    parameters.get("majorCode")[0]);

        }
    }

    private void populateDataSourceForAllSchedules() {
        setRelatedEntitesOn();
        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0) {
            final List<Fund> fundlist = new ArrayList<Fund>();
            fundlist.add(incomeExpenditureStatement.getFund());
            incomeExpenditureStatement.setFunds(fundlist);
            incomeExpenditureScheduleService.populateDataForAllSchedules(incomeExpenditureStatement);
        } else {
            incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
            incomeExpenditureScheduleService.populateDataForAllSchedules(incomeExpenditureStatement);
        }
    }

    public String printIncomeExpenditureReport() {
        populateDataSource();
        return "report";
    }

    @Action(value = "/report/incomeExpenditureReport-ajaxPrintIncomeExpenditureReport")
    public String ajaxPrintIncomeExpenditureReport() {
        populateDataSource();
        return "results";
    }

    protected void populateDataSource() {

        setRelatedEntitesOn();

        statementheading.append("Income And Expenditure Statement").append(heading);
        if (incomeExpenditureStatement.getFund() != null && incomeExpenditureStatement.getFund().getId() != null
                && incomeExpenditureStatement.getFund().getId() != 0) {
            final List<Fund> fundlist = new ArrayList<Fund>();
            fundlist.add(incomeExpenditureStatement.getFund());
            incomeExpenditureStatement.setFunds(fundlist);
            incomeExpenditureService.populateIEStatement(incomeExpenditureStatement);
        } else {
            incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
            incomeExpenditureService.populateIEStatement(incomeExpenditureStatement);
        }
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateIncomeExpenditurePdf")
    public String generateIncomeExpenditurePdf() throws Exception {
        populateDataSource();
        final String heading = ReportUtil.getCityName() + "\\n" + statementheading.toString();
        final String subtitle = "Report Run Date-" + FORMATDDMMYYYY.format(getTodayDate());
        final JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement, heading,
                getPreviousYearToDate(), getCurrentYearToDate(), subtitle, true);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return INCOME_EXPENSE_PDF;
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateDetailCodePdf")
    public String generateDetailCodePdf() throws Exception {
        populateSchedulewiseDetailCodeReport();
        final String heading = ReportUtil.getCityName() + "\\n" + statementheading.toString();
        final String subtitle = "Report Run Date-" + FORMATDDMMYYYY.format(getTodayDate());
        final JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement, heading,
                getPreviousYearToDate(), getCurrentYearToDate(), subtitle, true);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return INCOME_EXPENSE_PDF;
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateDetailCodeXls")
    public String generateDetailCodeXls() throws Exception {
        populateSchedulewiseDetailCodeReport();
        final String heading = ReportUtil.getCityName() + "\\n" + statementheading.toString();
        final String subtitle = "Report Run Date-" + FORMATDDMMYYYY.format(getTodayDate())
                + "                                               ";
        final JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement, heading,
                getPreviousYearToDate(), getCurrentYearToDate(), subtitle, true);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return INCOME_EXPENSE_XLS;
    }

   /* public String getUlbName() {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }*/


    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateIncomeExpenditureXls")
    public String generateIncomeExpenditureXls() throws Exception {
        populateDataSource();
        final String heading = ReportUtil.getCityName() + "\\n" + statementheading.toString();
        final String subtitle = "Report Run Date-" + FORMATDDMMYYYY.format(getTodayDate())
                + "                                               ";
        final JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement, heading,
                getPreviousYearToDate(), getCurrentYearToDate(), subtitle, true);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return INCOME_EXPENSE_XLS;
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateSchedulePdf")
    public String generateSchedulePdf() throws Exception {
        populateDataSourceForAllSchedules();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,
                getText("report.ie.heading"), heading.toString(),
                getPreviousYearToDate(), getCurrentYearToDate(), false);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return INCOME_EXPENSE_PDF;
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateScheduleXls")
    public String generateScheduleXls() throws Exception {
        populateDataSourceForAllSchedules();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,
                getText("report.ie.heading"), heading.toString(),
                getPreviousYearToDate(), getCurrentYearToDate(), false);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return INCOME_EXPENSE_XLS;
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateIncomeExpenditureSchedulePdf")
    public String generateIncomeExpenditureSchedulePdf() throws Exception {
        populateDataSourceForSchedule();
        final String heading = ReportUtil.getCityName() + "\\n" + scheduleheading.toString();
        final String subtitle = "Report Run Date-" + FORMATDDMMYYYY.format(getTodayDate())
                + "                                             ";
        final JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement, heading,
                getPreviousYearToDate(), getCurrentYearToDate(), subtitle, false);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return INCOME_EXPENSE_PDF;
    }

    @ReadOnly
    @Action(value = "/report/incomeExpenditureReport-generateIncomeExpenditureScheduleXls")
    public String generateIncomeExpenditureScheduleXls() throws Exception {
        populateDataSourceForSchedule();
        final String heading = ReportUtil.getCityName() + "\\n" + scheduleheading.toString();
        // Blank space for space didvidion between left and right corner
        final String subtitle = "Report Run Date-" + FORMATDDMMYYYY.format(getTodayDate()) + "					  						 ";
        final JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement, heading,
                getPreviousYearToDate(), getCurrentYearToDate(), subtitle, false);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return INCOME_EXPENSE_XLS;
    }

    public String getCurrentYearToDate() {
        return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getToDate(incomeExpenditureStatement));
    }

    public String getPreviousYearToDate() {
        return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(incomeExpenditureService
                .getToDate(incomeExpenditureStatement)));
    }

    public String getCurrentYearFromDate() {
        return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getFromDate(incomeExpenditureStatement));
    }

    public String getPreviousYearFromDate() {
        return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(incomeExpenditureService
                .getFromDate(incomeExpenditureStatement)));
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(final Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(final String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMinorCode() {
        return minorCode;
    }

    public void setMinorCode(final String minorCode) {
        this.minorCode = minorCode;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(final String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public List<CChartOfAccounts> getListChartOfAccounts() {
        return listChartOfAccounts;
    }

    public void setListChartOfAccounts(final List<CChartOfAccounts> listChartOfAccounts) {
        this.listChartOfAccounts = listChartOfAccounts;
    }

    public String getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(final String financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(final Integer fundId) {
        this.fundId = fundId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public String getAsOnDateRange() {
        return asOnDateRange;
    }

    public void setAsOnDateRange(final String asOnDateRange) {
        this.asOnDateRange = asOnDateRange;
    }

    public StringBuffer getScheduleheading() {
        return scheduleheading;
    }

    public void setScheduleheading(final StringBuffer scheduleheading) {
        this.scheduleheading = scheduleheading;
    }

    public StringBuffer getStatementheading() {
        return statementheading;
    }

    public void setStatementheading(final StringBuffer statementheading) {
        this.statementheading = statementheading;
    }

    public boolean isDetailReport() {
        return detailReport;
    }

    public void setDetailReport(final boolean detailReport) {
        this.detailReport = detailReport;
    }

}