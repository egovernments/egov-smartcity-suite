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
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.model.CommonReportBean;
import org.egov.egf.model.FunctionwiseIE;
import org.egov.egf.model.ReportSearch;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.services.report.FunctionwiseIEService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Results(value = {
        @Result(name = "functionwiseIE-PDF", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", "contentDisposition",
        "no-cache;filename=FunctionwiseIE.pdf" }),
        @Result(name = "functionwiseIE-XLS", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", "contentDisposition",
        "no-cache;filename=FunctionwiseIE.xls" }),
        @Result(name = "functionwiseIE-HTML", type = "stream", location = Constants.INPUT_STREAM, params = {
                Constants.INPUT_NAME, Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "text/html" })
})

@ParentPackage("egov")
public class FunctionwiseIEAction extends ReportAction
{
    private static final long serialVersionUID = 1L;
    protected InputStream inputStream;
    private ReportHelper reportHelper;
    private FunctionwiseIEService functionwiseIEService;
    private final FunctionwiseIE functionwiseIE = new FunctionwiseIE();
    private CityService cityService;
    private City cityWebsite;
    
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired AppConfigValueService appConfigValuesService;
    private FinancialYearDAO financialYearDAO;
    private String heading = "";
    private Date todayDate;
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public FinancialYearDAO getFinancialYearDAO() {
        return financialYearDAO;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    private static final Logger LOGGER = Logger.getLogger(FunctionwiseIEAction.class);
    private List<CommonReportBean> ieWithBudgetList;

    public void setCityService(final CityService cityService) {
        this.cityService = cityService;
    }

    public void setFunctionwiseIEService(final FunctionwiseIEService functionwiseIEService) {
        this.functionwiseIEService = functionwiseIEService;
    }

    @Override
    public void prepare()
    {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        if (reportSearch.getStartDate() == null || reportSearch.getStartDate().equals(""))
            reportSearch.setStartDate(sdf.format(((CFinancialYear) persistenceService
                    .find(" from CFinancialYear where startingDate <= '" + formatter.format(new Date()) + "' and endingDate >= '"
                            + formatter.format(new Date()) + "'")).getStartingDate()));
        if (reportSearch.getEndDate() == null || reportSearch.getEndDate().equals(""))
            reportSearch.setEndDate(sdf.format(new Date()));
        setTodayDate(new Date());
    }

    public void preparebeforesearchWithBudget()
    {
        addDropdownData("fundList", masterDataCache.get("egi-fund"));
        addDropdownData("functionList", masterDataCache.get("egi-function"));
    }

    @Override
    public Object getModel() {
        return reportSearch;
    }

    @SkipValidation
    @Action(value = "/report/functionwiseIE-beforesearch")
    public String beforesearch()
    {
        return REPORT;
    }

    @SkipValidation
    @Action(value = "/report/functionwiseIE-beforeSearchWithBudget")
    public String beforeSearchWithBudget()
    {
        return "reportWithBudget";
    }

    @SkipValidation
    @Action(value = "/report/functionwiseIE-exportMajorAndMinorCodewise")
    public String exportMajorAndMinorCodewise()
    {
        try {
            searchWithBudget();
            removEmptyRows(reportSearch);
            if (reportSearch.getExportType().equalsIgnoreCase("xls"))
            {
                inputStream = reportHelper.exportXls(inputStream,
                        reportHelper.exportMajorAndMinorCodewise(ieWithBudgetList, cityWebsite.getName(), reportSearch, heading));
                return "functionwiseIE-XLS";
            }
            else
            {
                inputStream = reportHelper.exportPdf(inputStream,
                        reportHelper.exportMajorAndMinorCodewise(ieWithBudgetList, cityWebsite.getName(), reportSearch, heading));
                return "functionwiseIE-PDF";
            }
        } catch (final JRException e) {
            LOGGER.error(e, e);
        } catch (final IOException e) {
            LOGGER.error(e, e);
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }
        return "functionwiseIE-XLS";
    }

    @SkipValidation
    @Action(value = "/report/functionwiseIE-exportDeptwise")
    public String exportDeptwise()
    {
        try {
            deptWiseIEWithBudget();
            removEmptyRows(reportSearch);
            if (reportSearch.getExportType().equalsIgnoreCase("xls"))
            {
                inputStream = reportHelper.exportXls(inputStream,
                        reportHelper.exportDeptwise(ieWithBudgetList, cityWebsite.getName(), reportSearch, heading));
                return "functionwiseIE-XLS";
            }
            else
            {
                inputStream = reportHelper.exportPdf(inputStream,
                        reportHelper.exportDeptwise(ieWithBudgetList, cityWebsite.getName(), reportSearch, heading));
                return "functionwiseIE-PDF";
            }
        } catch (final JRException e) {
            LOGGER.error(e, e);
        } catch (final IOException e) {
            LOGGER.error(e, e);
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }
        return "functionwiseIE-XLS";
    }

    @SkipValidation
    @Action(value = "/report/functionwiseIE-exportDetailwise")
    public String exportDetailwise()
    {
        try {
            detailWiseIEWithBudget();
            removEmptyRows(reportSearch);
            if (reportSearch.getExportType().equalsIgnoreCase("xls"))
            {
                inputStream = reportHelper.exportXls(inputStream,
                        reportHelper.exportDetailwise(ieWithBudgetList, cityWebsite.getName(), reportSearch, heading));
                return "functionwiseIE-XLS";
            }
            else
            {
                inputStream = reportHelper.exportPdf(inputStream,
                        reportHelper.exportDetailwise(ieWithBudgetList, cityWebsite.getName(), reportSearch, heading));
                return "functionwiseIE-PDF";
            }
        } catch (final JRException e) {
            LOGGER.error(e, e);
        } catch (final IOException e) {
            LOGGER.error(e, e);
        } catch (final Exception e) {
            LOGGER.error(e, e);
        }
        return "functionwiseIE-XLS";
    }

    /**
     *
     * @param reportSearch will be called only while generating pdf JSP already has this logic
     */
    private void removEmptyRows(final ReportSearch reportSearch) {
        final List<CommonReportBean> ieWithBudgetList2 = new ArrayList<CommonReportBean>();
        ieWithBudgetList2.addAll(ieWithBudgetList);
        if (reportSearch.getIncExp().equalsIgnoreCase("E"))
        {
            for (final CommonReportBean crb : ieWithBudgetList2)
                if (crb.isZero())
                    ieWithBudgetList.remove(crb);
        } else
            for (final CommonReportBean crb : ieWithBudgetList2)
                if (crb.isZeroForIncome())
                    ieWithBudgetList.remove(crb);
        int i = 1;
        for (final CommonReportBean crb : ieWithBudgetList)
            crb.setSlNo(i++);
    }

    /*
     * private void getFromfile(String name) { FileInputStream fis = null; ObjectInputStream in = null; try { fis = new
     * FileInputStream(name); in = new ObjectInputStream(fis); ieWithBudgetList =(ArrayList<CommonReportBean>) in.readObject();
     * in.close(); } catch (Exception e) { } }
     */

    @SkipValidation
    public String searchWithBudget() throws Exception
    {

        setDatasForBudgetWise();
        populateDataSourceWithBudget(reportSearch);
        heading = generateHeading();
        return "resultWithBudget";
    }

    private void setDatasForBudgetWise() {
        final Integer majorCodeLen = Integer.valueOf(appConfigValuesService.getConfigValuesByModuleAndKey
                (Constants.EGF, FinancialConstants.APPCONFIG_COA_MAJORCODE_LENGTH).get(0).getValue());
        reportSearch.setMajorCodeLen(majorCodeLen);
        final Integer minorCodeLen = Integer.valueOf(appConfigValuesService.getConfigValuesByModuleAndKey
                (Constants.EGF, FinancialConstants.APPCONFIG_COA_MINORCODE_LENGTH).get(0).getValue());

        reportSearch.setMinorCodeLen(minorCodeLen);

        if (reportSearch.getAsOnDate() != null)
        {
            final CFinancialYear financialYearByDate = getFinancialYearDAO().getFinancialYearByDate(reportSearch.getAsOnDate());
            reportSearch.setFinYearId(financialYearByDate.getId());
            reportSearch.setYearStartDate(financialYearByDate.getStartingDate());
            setPreviousYearDates();
        }
        /*
         * if(reportSearch.getIncExp().equalsIgnoreCase("I")) { setPreviousYearDates(); }
         */
    }

    private void setPreviousYearDates() {
        final CFinancialYear previousfinancialYear = getFinancialYearDAO().getPreviousFinancialYearByDate(
                reportSearch.getAsOnDate());
        reportSearch.setPreviousFinYearId(previousfinancialYear.getId());
        reportSearch.setPreviousYearStartDate(previousfinancialYear.getStartingDate());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(reportSearch.getAsOnDate());
        cal.add(Calendar.YEAR, -1);
        reportSearch.setPreviousYearDate(cal.getTime());
    }

    private String generateHeading() {
        final StringBuffer heading = new StringBuffer(256);
        heading.append(" FunctionWise ");
        if (reportSearch.getIncExp().equalsIgnoreCase("E"))
            heading.append(" Expense Subsidary Register ");
        else
            heading.append(" Income Subsidary Register ");
        if (reportSearch.getFunction() != null && reportSearch.getFunction().getId() != null
                && reportSearch.getFunction().getId() != -1)
        {
            heading.append(" For the Function Code ");
            final String code = (String) persistenceService.find("select code from CFunction where id=?", reportSearch
                    .getFunction()
                    .getId());
            heading.append(code);
        }
        if (reportSearch.getScheduleName() != null)
            heading.append(" For Schedule " + reportSearch.getScheduleName());
        if (reportSearch.getDepartment() != null && !reportSearch.getDepartment().getName().isEmpty())
            heading.append(" For Department " + reportSearch.getDepartment().getName());
        if (reportSearch.getFund() != null && reportSearch.getFund().getId() != -1)
        {
            heading.append(" In Fund ");
            final String name = (String) persistenceService.find("select name from Fund where id=?", reportSearch.getFund()
                    .getId());
            heading.append(name);
        }

        heading.append(" from " + sdf.format(reportSearch.getYearStartDate()) + " - " + sdf.format(reportSearch.getAsOnDate()));

        return heading.toString();

    }

    @Action(value = "/report/functionwiseIE-deptWiseIEWithBudget")
    public String deptWiseIEWithBudget() throws Exception
    {
        setDatasForBudgetWise();
        reportSearch.setByDepartment(true);

        reportSearch.setDeptList(masterDataCache.get("egi-department"));

        populateDataSourceWithBudget(reportSearch);
        /*
         * if(reportSearch.getIncExp().equalsIgnoreCase("E")) getFromfile("deptE"); else getFromfile("deptI");
         */
        heading = generateHeading();
        return "deptWiseWithBudget";
    }

    @Action(value = "/report/functionwiseIE-detailWiseIEWithBudget")
    public String detailWiseIEWithBudget() throws Exception
    {
        setDatasForBudgetWise();
        // override minor code length with detail code for detail report
        final Integer minorCodeLen = Integer.valueOf(appConfigValuesService.getConfigValuesByModuleAndKey
                (Constants.EGF, FinancialConstants.APPCONFIG_COA_DETAILCODE_LENGTH).get(0).getValue());
        reportSearch.setMinorCodeLen(minorCodeLen);
        reportSearch.setByDepartment(true);
        reportSearch.setByDetailCode(true);
        reportSearch.setDeptList(masterDataCache.get("egi-department"));

        populateDataSourceWithBudget(reportSearch);
        /*
         * if(reportSearch.getIncExp().equalsIgnoreCase("E")) getFromfile("detailE"); else getFromfile("detailI");
         */
        heading = generateHeading();
        return "detailWiseWithBudget";

    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    @Action(value = "/report/functionwiseIE-search")
    public String search() throws Exception
    {
        final Integer majorCodeLen = Integer.valueOf(appConfigValuesService.getConfigValuesByModuleAndKey
                (Constants.EGF, FinancialConstants.APPCONFIG_COA_MAJORCODE_LENGTH).get(0).getValue());
        reportSearch.setMajorCodeLen(majorCodeLen);
        populateDataSource(reportSearch);

        return "print";
    }

    public String generateFunctionwiseIEHtml() throws Exception
    {
        populateDataSource(reportSearch);
        inputStream = reportHelper.exportHtml(
                inputStream,
                reportHelper.generateFunctionwiseIEJasperPrint(functionwiseIE, cityWebsite.getName(),
                        getvalue(reportSearch.getIncExp())));
        return "functionwiseIE-HTML";
    }

    @Action(value = "/report/functionwiseIE-generateFunctionwiseIEPdf")
    public String generateFunctionwiseIEPdf() throws Exception {
        populateDataSource(reportSearch);
        inputStream = reportHelper.exportPdf(
                inputStream,
                reportHelper.generateFunctionwiseIEJasperPrint(functionwiseIE, cityWebsite.getName(),
                        getvalue(reportSearch.getIncExp())));
        return "functionwiseIE-PDF";
    }

    @Action(value = "/report/functionwiseIE-generateFunctionwiseIEXls")
    public String generateFunctionwiseIEXls() throws Exception {
        populateDataSource(reportSearch);
        inputStream = reportHelper.exportXls(
                inputStream,
                reportHelper.generateFunctionwiseIEJasperPrint(functionwiseIE, cityWebsite.getName(),
                        getvalue(reportSearch.getIncExp())));
        return "functionwiseIE-XLS";
    }

    public void populateDataSource(final ReportSearch reportSearch) throws Exception
    {
        // functionwiseIEService.setReportSearch(reportSearch);
        functionwiseIEService.populateData(functionwiseIE, reportSearch);
        cityWebsite = cityService.getCityByURL((String) getSession().get("cityurl"));
        functionwiseIE.setCityName(cityWebsite.getName());
    }

    public void populateDataSourceWithBudget(final ReportSearch reportSearch) throws Exception
    {
        if (reportSearch.getIncExp().equalsIgnoreCase("E"))
            ieWithBudgetList = functionwiseIEService.populateDataWithBudget(functionwiseIE, reportSearch);
        else
            ieWithBudgetList = functionwiseIEService.populateIncomeDataWithBudget(functionwiseIE, reportSearch);

        cityWebsite = cityService.getCityByURL((String) getSession().get("cityurl"));
        functionwiseIE.setCityName(cityWebsite.getName());
    }

    public List<CommonReportBean> getIeWithBudgetList() {
        return ieWithBudgetList;
    }

    public void setIeWithBudgetList(final List<CommonReportBean> ieWithBudgetList) {
        this.ieWithBudgetList = ieWithBudgetList;
    }

    public String getvalue(final String incExp)
    {
        if ("I".equals(incExp))
            return "Income";
        else
            return "Expense";
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    @Override
    public void validate()
    {
        if (reportSearch.getIncExp() == null || reportSearch.getIncExp().equals("-1"))
            addFieldError("incExp", getMessage("report.income.expense.mandatory"));
        if (reportSearch.getStartDate() == null || reportSearch.getStartDate().equals(""))
            addFieldError("startDate", getMessage("report.startdate.mandatory"));
        if (reportSearch.getEndDate() == null || reportSearch.getEndDate().equals(""))
            addFieldError("endDate", getMessage("report.enddate.mandatory"));
        try {
            if (!reportSearch.getStartDate().equals(""))
                sdf.parse(reportSearch.getStartDate());
        } catch (final Exception e) {
            LOGGER.error("ERROR" + e.getMessage(), e);
            addFieldError("startDate", getMessage("report.startdate.invalid.format"));
        }
        try {
            if (!reportSearch.getEndDate().equals(""))
                sdf.parse(reportSearch.getEndDate());
        } catch (final Exception e) {
            LOGGER.error("ERROR" + e.getMessage(), e);
            addFieldError("endDate", getMessage("report.enddate.invalid.format"));
        }
        super.validate();
    }

    public FunctionwiseIE getFunctionwiseIE() {
        return functionwiseIE;
    }

    protected String getMessage(final String key) {
        return getText(key);
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(final Date todayDate) {
        this.todayDate = todayDate;
    }
}