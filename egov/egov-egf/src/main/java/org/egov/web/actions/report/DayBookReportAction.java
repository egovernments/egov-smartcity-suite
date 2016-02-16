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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Fund;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.reports.DayBookList;
import com.exilant.eGov.src.reports.DayBookReportBean;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Transactional(readOnly = true)
@ParentPackage("egov")
@Results({
    @Result(name = "result", location = "dayBookReport-result.jsp"),
    @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "dayBookReport-"
            + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp")
})
public class DayBookReportAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 641276108961752283L;
    private static final Logger LOGGER = Logger.getLogger(DayBookReportAction.class);
    private DayBookReportBean dayBookReport = new DayBookReportBean();
    private DayBookList dayBook = new DayBookList();
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected LinkedList dayBookDisplayList = new LinkedList();
    String heading = "";

    public DayBookReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return dayBookReport;
    }

    public void prepareNewForm() {
        super.prepare();
        HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
        HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
        addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/dayBookReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "fundId", message = "", key = FinancialConstants.REQUIRED),})
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @Action(value = "/report/dayBookReport-ajaxSearch")
    public String ajaxSearch() throws TaskFailedException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("dayBookAction | Search | start");
        try {
            dayBookDisplayList = dayBook.getDayBookList(dayBookReport);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        } catch (final Exception e)
        {
            throw new ApplicationRuntimeException(e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("dayBookAction | list | End");
        heading = getGLHeading();
        prepareNewForm();
        HibernateUtil.getCurrentSession().setFlushMode(FlushMode.AUTO);
        return "result";
    }

    private String getGLHeading() {

        String heading = "Day Book report from " + dayBookReport.getStartDate() + " to " + dayBookReport.getEndDate();
        Fund fund = new Fund();
        if (checkNullandEmpty(dayBookReport.getFundId())) {
            fund = (Fund) persistenceService.find("from Fund where id = ?", Integer.parseInt(dayBookReport.getFundId()));
            heading = heading + " under " + fund.getName() + " ";
        }
        return heading;
    }

    private boolean checkNullandEmpty(final String column)
    {
        if (column != null && !column.isEmpty() && !column.equalsIgnoreCase("0"))
            return true;
        else
            return false;

    }

    public DayBookReportBean getDayBookReport() {
        return dayBookReport;
    }

    public void setDayBookReport(final DayBookReportBean dayBookReport) {
        this.dayBookReport = dayBookReport;
    }

    public DayBookList getDayBook() {
        return dayBook;
    }

    public void setDayBook(final DayBookList dayBook) {
        this.dayBook = dayBook;
    }

    public LinkedList getDayBookDisplayList() {
        return dayBookDisplayList;
    }

    public void setDayBookDisplayList(final LinkedList dayBookDisplayList) {
        this.dayBookDisplayList = dayBookDisplayList;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

}
