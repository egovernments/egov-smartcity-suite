/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.egf.web.actions.report;

import com.exilant.eGov.src.reports.OpeningBalance;
import com.exilant.eGov.src.reports.OpeningBalanceInputBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


@ParentPackage("egov")
@Results({
    @Result(name = "result", location = "openingBalanceReport-result.jsp"),
    @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "openingBalanceReport-"
            + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp")
})
public class OpeningBalanceReportAction extends BaseFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
    private static final long serialVersionUID = -2567999475434622263L;
    private static final Logger LOGGER = Logger.getLogger(OpeningBalanceReportAction.class);
    private OpeningBalanceInputBean openingBalanceReport = new OpeningBalanceInputBean();
    private OpeningBalance openingBalance;
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected ArrayList openingBalanceDisplayList = new ArrayList();
    String heading = "";

    public OpeningBalanceReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return openingBalanceReport;
    }

    public void prepareNewForm() {
        super.prepare();
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        addDropdownData("financialYearList", persistenceService.findAllBy("from CFinancialYear order by finYearRange desc "));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/openingBalanceReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @ReadOnly
    @Validations(requiredFields = { @RequiredFieldValidator(fieldName = "finYear", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @Action(value = "/report/openingBalanceReport-ajaxSearch")
    public String ajaxSearch() throws TaskFailedException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("OpeningBalanceReportAction | Search | start");
        try {
            openingBalanceDisplayList = openingBalance.getOBReport(openingBalanceReport);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        } catch (final Exception e)
        {
            throw new ApplicationRuntimeException(e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("OpeningBalanceReportAction | list | End");
        heading = getGLHeading();
        prepareNewForm();
        persistenceService.getSession().setFlushMode(FlushMode.AUTO);
        return "result";
    }

    private String getGLHeading() {

        String heading = "Opening Balance for the Year ";
        CFinancialYear finYear = new CFinancialYear();
        Fund fund = new Fund();
        Department dept = new Department();
        if (checkNullandEmpty(openingBalanceReport.getFinYear())) {
            finYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id = ?",
                    Long.parseLong(openingBalanceReport.getFinYear()));
            heading = heading + finYear.getFinYearRange();
        }
        if (checkNullandEmpty(openingBalanceReport.getObFund_id())) {
            fund = (Fund) persistenceService
                    .find("from Fund where id = ?", Integer.parseInt(openingBalanceReport.getObFund_id()));
            heading = heading + " under " + fund.getName();
        }

        if (checkNullandEmpty(openingBalanceReport.getDeptId())) {
            dept = (Department) persistenceService.find("from Department where id = ?",
                    Long.parseLong(openingBalanceReport.getDeptId()));
            heading = heading + " and " + dept.getName() + " Department ";
        }
        return heading;
    }

    private boolean checkNullandEmpty(final String column)
    {
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

    public OpeningBalanceInputBean getOpeningBalanceReport() {
        return openingBalanceReport;
    }

    public void setOpeningBalanceReport(final OpeningBalanceInputBean openingBalanceReport) {
        this.openingBalanceReport = openingBalanceReport;
    }

    public OpeningBalance getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(final OpeningBalance openingBalance) {
        this.openingBalance = openingBalance;
    }

    public ArrayList getOpeningBalanceDisplayList() {
        return openingBalanceDisplayList;
    }

    public void setOpeningBalanceDisplayList(final ArrayList openingBalanceDisplayList) {
        this.openingBalanceDisplayList = openingBalanceDisplayList;
    }

}