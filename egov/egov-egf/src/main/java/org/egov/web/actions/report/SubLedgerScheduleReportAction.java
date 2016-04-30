package org.egov.web.actions.report;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.transactions.RptSubLedgerSchedule;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@ParentPackage("egov")
@Results({
    @Result(name = "result", location = "subLedgerScheduleReport-result.jsp"),
    @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "subLedgerScheduleReport-"
            + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp") })
public class SubLedgerScheduleReportAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = -8747832451260494901L;
    private static final Logger LOGGER = Logger.getLogger(SubLedgerScheduleReportAction.class);
    private GeneralLedgerBean subLedgerScheduleReport = new GeneralLedgerBean();
    @Autowired
    @Qualifier("rptSubLedgerSchedule")
    private RptSubLedgerSchedule rptSubLedgerSchedule;
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected LinkedList subLedgerScheduleDisplayList = new LinkedList();
    String heading = "";

    public SubLedgerScheduleReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return subLedgerScheduleReport;
    }

    public void prepareNewForm() {
        super.prepare();
        addDropdownData("fundList",
                persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        if (subLedgerScheduleReport != null && subLedgerScheduleReport.getGlcode() != null
                && !subLedgerScheduleReport.getGlcode().equalsIgnoreCase(""))
            addDropdownData(
                    "subLedgerTypeList",
                    persistenceService
                    .findAllBy(
                            "select distinct adt from Accountdetailtype adt, CChartOfAccountDetail cad where cad.glCodeId.glcode = ? and cad.detailTypeId = adt ",
                            subLedgerScheduleReport.getGlcode()));
        else
            addDropdownData("subLedgerTypeList", Collections.EMPTY_LIST);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/subLedgerScheduleReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "glcode", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "accEntityId", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/subLedgerScheduleReport-ajaxSearch")
    public String ajaxSearch() throws TaskFailedException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("SubLedgerScheduleReportAction | Search | start");
        try {
            subLedgerScheduleDisplayList = rptSubLedgerSchedule.getSubLedgerTypeSchedule(subLedgerScheduleReport);
        } catch (final ValidationException e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
         }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("SubLedgerScheduleReportAction | list | End");
        heading = getGLHeading();
        prepareNewForm();
        return "result";
    }

    private String getGLHeading() {
        String heading = "";
        CChartOfAccounts glCode = new CChartOfAccounts();
        Fund fund = new Fund();
        if (checkNullandEmpty(subLedgerScheduleReport.getGlcode())
                && checkNullandEmpty(subLedgerScheduleReport.getGlcode())) {
            glCode = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = ?",
                    subLedgerScheduleReport.getGlcode());
            fund = (Fund) persistenceService.find("from Fund where id = ?",
                    Integer.parseInt(subLedgerScheduleReport.getFund_id()));
        }
        heading = "Sub Ledger Schedule Report for " + glCode.getGlcode() + " - " + glCode.getName() + " for "
                + fund.getName() + " from " + subLedgerScheduleReport.getStartDate() + " to "
                + subLedgerScheduleReport.getEndDate() + " and Sub Ledger Type :"
                + subLedgerScheduleReport.getSubLedgerTypeName();
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

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public GeneralLedgerBean getSubLedgerScheduleReport() {
        return subLedgerScheduleReport;
    }

    public void setSubLedgerScheduleReport(final GeneralLedgerBean subLedgerScheduleReport) {
        this.subLedgerScheduleReport = subLedgerScheduleReport;
    }

    public RptSubLedgerSchedule getSubLedgerSchedule() {
        return rptSubLedgerSchedule;
    }

    public void setSubLedgerSchedule(final RptSubLedgerSchedule subLedgerSchedule) {
        rptSubLedgerSchedule = subLedgerSchedule;
    }

    public LinkedList getSubLedgerScheduleDisplayList() {
        return subLedgerScheduleDisplayList;
    }

    public void setSubLedgerScheduleDisplayList(final LinkedList subLedgerScheduleDisplayList) {
        this.subLedgerScheduleDisplayList = subLedgerScheduleDisplayList;
    }

}
