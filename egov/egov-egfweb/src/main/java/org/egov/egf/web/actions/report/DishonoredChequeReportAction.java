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

import com.exilant.eGov.src.reports.DishonoredChequeBean;
import com.exilant.eGov.src.reports.DishonoredChequeReport;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Fund;
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
    @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "dishonoredChequeReport-"
            + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp"),
            @Result(name = "results", location = "dishonoredChequeReport-results.jsp")
})
public class DishonoredChequeReportAction extends BaseFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;


    /**
     *
     */
    private static final long serialVersionUID = -1526444081011004380L;
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeReportAction.class);
    private DishonoredChequeBean dishonoredChequeReport = new DishonoredChequeBean();
    private DishonoredChequeReport dishonoredCheque = new DishonoredChequeReport();
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected ArrayList dishonoredChequeDisplayList = new ArrayList();
    String heading = "";
    private String showMode = "";

    public DishonoredChequeReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return dishonoredChequeReport;
    }

    public void prepareNewForm() {
        super.prepare();
        addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/dishonoredChequeReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "mode", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/dishonoredChequeReport-ajaxSearch")
    public String ajaxSearch() throws TaskFailedException {

        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("DishonoredChequeAction | Search | start");
        try {
            dishonoredChequeDisplayList = dishonoredCheque.getDishonoredChequeDetails(dishonoredChequeReport);
        } catch (final Exception e) {

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("DishonoredChequeAction | list | End");
        heading = getGLHeading();
        prepareNewForm();
        showMode = "result";
        return "results";
    }

    private String getGLHeading() {

        String heading = "";
        Fund fund = new Fund();
        heading = "Dishonored Cheque/DD Report under Mode of Payment:"
                + (dishonoredChequeReport.getMode().equalsIgnoreCase("2") ? "Cheque" : "DD") + " from "
                + dishonoredChequeReport.getStartDate();
        if (checkNullandEmpty(dishonoredChequeReport.getEndDate()))
            heading = heading + " to " + dishonoredChequeReport.getEndDate();
        if (checkNullandEmpty(dishonoredChequeReport.getFundLst())) {
            fund = (Fund) persistenceService.find("from Fund where  id = ?",
                    Integer.parseInt(dishonoredChequeReport.getFundLst()));
            heading = heading + " and Fund :" + fund.getName();
        }
        if (checkNullandEmpty(dishonoredChequeReport.getChequeNo()))
            heading = heading + " and Cheque/DD Number :" + dishonoredChequeReport.getChequeNo();
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

    public DishonoredChequeBean getDishonoredChequeReport() {
        return dishonoredChequeReport;
    }

    public void setDishonoredChequeReport(
            final DishonoredChequeBean dishonoredChequeReport) {
        this.dishonoredChequeReport = dishonoredChequeReport;
    }

    public DishonoredChequeReport getDishonoredCheque() {
        return dishonoredCheque;
    }

    public void setDishonoredCheque(final DishonoredChequeReport dishonoredCheque) {
        this.dishonoredCheque = dishonoredCheque;
    }

    public ArrayList getDishonoredChequeDisplayList() {
        return dishonoredChequeDisplayList;
    }

    public void setDishonoredChequeDisplayList(final ArrayList dishonoredChequeDisplayList) {
        this.dishonoredChequeDisplayList = dishonoredChequeDisplayList;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

}