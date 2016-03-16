package org.egov.web.actions.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.exilant.eGov.src.reports.GeneralLedgerReport;
import com.exilant.eGov.src.reports.GeneralLedgerReportBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Results({ @Result(name = "search", location = "subLedgerReport-search.jsp") })
public class SubLedgerReportAction extends BaseFormAction {

    private static final long serialVersionUID = 908820938838841653L;
    private static final Logger LOGGER = Logger.getLogger(SubLedgerReportAction.class);
    private GeneralLedgerReportBean subLedgerReport = new GeneralLedgerReportBean();
    // private GeneralLedgerReport subLedger = new GeneralLedgerReport();
    @Autowired
    @Qualifier("generalLedgerReport")
    private GeneralLedgerReport generalLedgerReport;
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected LinkedList subLedgerDisplayList = new LinkedList();
    String heading = "";
    private String drillDownFromSchedule;

    public SubLedgerReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return subLedgerReport;
    }

    public void prepareNewForm() {
        super.prepare();
        addDropdownData("fundList",
                persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        if (subLedgerReport != null && subLedgerReport.getGlCode1() != null
                && !subLedgerReport.getGlCode1().equalsIgnoreCase(""))
            addDropdownData(
                    "subLedgerTypeList",
                    persistenceService
                    .findAllBy(
                            "select distinct adt from Accountdetailtype adt, CChartOfAccountDetail cad where cad.glCodeId.glcode = ? and cad.detailTypeId = adt ",
                            subLedgerReport.getGlCode1()));
        else
            addDropdownData("subLedgerTypeList", Collections.EMPTY_LIST);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/subLedgerReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "glCode1", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "subledger", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "accEntitycode", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/subLedgerReport-search")
    public String search() throws TaskFailedException {
        subLedgerReport.setReportType("sl");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("SubLedgerAction | Search | start");
        try {
            subLedgerDisplayList = generalLedgerReport.getGeneralLedgerList(subLedgerReport);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("SubLedgerAction | list | End");
        heading = getGLHeading();
        subLedgerReport.setHeading(heading);
        prepareNewForm();
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    private String getGLHeading() {

        String heading = "";
        CChartOfAccounts glCode = new CChartOfAccounts();
        Fund fund = new Fund();
        if (checkNullandEmpty(subLedgerReport.getGlCode1()) && checkNullandEmpty(subLedgerReport.getGlCode1())) {
            glCode = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = ?",
                    subLedgerReport.getGlCode1());
            fund = (Fund) persistenceService.find("from Fund where id = ?",
                    Integer.parseInt(subLedgerReport.getFund_id()));
        }
        heading = "Sub Ledger Report for " + subLedgerReport.getEntityName() + " in " + glCode.getName() + " under "
                + fund.getName() + " from " + subLedgerReport.getStartDate() + " to " + subLedgerReport.getEndDate();
        if (checkNullandEmpty(subLedgerReport.getDepartmentId())) {
            final Department dept = (Department) persistenceService.find("from Department where id = ?",
                    Integer.parseInt(subLedgerReport.getDepartmentId()));
            heading = heading + " under " + dept.getName() + " ";
        }

        return heading;
    }

    private boolean checkNullandEmpty(final String column) {
        if (column != null && !column.isEmpty())
            return true;
        else
            return false;

    }

    public String getHeading() {
        return heading;
    }

    public GeneralLedgerReportBean getSubLedgerReport() {
        return subLedgerReport;
    }

    public void setSubLedgerReport(final GeneralLedgerReportBean subLedgerReport) {
        this.subLedgerReport = subLedgerReport;
    }

    public GeneralLedgerReport getSubLedger() {
        return generalLedgerReport;
    }

    public void setSubLedger(final GeneralLedgerReport generalLedgerReport) {
        this.generalLedgerReport = generalLedgerReport;
    }

    public LinkedList getSubLedgerDisplayList() {
        return subLedgerDisplayList;
    }

    public void setSubLedgerDisplayList(final LinkedList subLedgerDisplayList) {
        this.subLedgerDisplayList = subLedgerDisplayList;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public String getDrillDownFromSchedule() {
        return drillDownFromSchedule;
    }

    public void setDrillDownFromSchedule(final String drillDownFromSchedule) {
        this.drillDownFromSchedule = drillDownFromSchedule;
    }

}
