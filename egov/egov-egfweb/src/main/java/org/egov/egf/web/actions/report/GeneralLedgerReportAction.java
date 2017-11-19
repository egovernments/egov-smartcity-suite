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

import com.exilant.eGov.src.reports.GeneralLedgerReport;
import com.exilant.eGov.src.reports.GeneralLedgerReportBean;
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
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "generalLedgerReport-"
                + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp"),
        @Result(name = "results", location = "generalLedgerReport-results.jsp"),
        @Result(name = "searchResult", location = "generalLedgerReport-searchDrilldown.jsp")
})
public class GeneralLedgerReportAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 4734431707050536319L;
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReportAction.class);
    private GeneralLedgerReportBean generalLedgerReportBean = new GeneralLedgerReportBean();

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    @Qualifier("generalLedgerReport")
    private GeneralLedgerReport generalLedgerReport;

    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected LinkedList generalLedgerDisplayList = new LinkedList();
    String heading = "";
    List<CChartOfAccounts> allChartOfAccounts;
    String parentForDetailedCode = "";
    private String glCode;
    @Autowired
    AppConfigValueService appConfigValuesService;
    @Autowired
    @Qualifier("chartOfAccountsService")
    private PersistenceService<CChartOfAccounts, Long> chartOfAccountsService;

    public GeneralLedgerReportAction() {
        LOGGER.error("creating instance of GeneralLedgerReportAction ");
    }

    @Override
    public Object getModel() {
        return generalLedgerReportBean;
    }

    @SuppressWarnings("unchecked")
    public void prepareNewForm() {
        super.prepare();

        allChartOfAccounts = persistenceService
                .findAllBy(
                        "select ca from CChartOfAccounts ca where"
                                +
                                " ca.glcode not in(select glcode from CChartOfAccounts where glcode like '47%' and glcode not like '471%' and glcode !='4741')"
                                +
                                " and ca.glcode not in (select glcode from CChartOfAccounts where glcode = '471%') " +
                                " and ca.isActiveForPosting=true and ca.classification=4  and ca.glcode like ?", glCode + "%");
        addDropdownData("fundList",
                persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=true order by name"));
        addDropdownData("fundsourceList",
                persistenceService.findAllBy(" from Fundsource where isactive=true order by name"));
        addDropdownData("fieldList", persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/generalLedgerReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "glCode1", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/generalLedgerReport-ajaxSearch")
    @ReadOnly
    public String ajaxSearch() throws TaskFailedException {

        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GeneralLedgerAction | Search | start");
        try {
            generalLedgerDisplayList = generalLedgerReport.getGeneralLedgerList(generalLedgerReportBean);
        } catch (final Exception e) {

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GeneralLedgerAction | list | End");
        heading = getGLHeading();
        generalLedgerReportBean.setHeading(getGLHeading());
        prepareNewForm();
        return "results";
    }

    @Action(value = "/report/generalLedgerReport-searchDrilldown")
    public String searchDrilldown()
    {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GeneralLedgerAction | Search | start");
        try {
            generalLedgerDisplayList = generalLedgerReport.getGeneralLedgerList(generalLedgerReportBean);
        } catch (final Exception e) {

        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("GeneralLedgerAction | list | End");
        heading = getGLHeading();
        generalLedgerReportBean.setHeading(getGLHeading());
        prepareNewForm();
        return "searchResult";
    }

    private String getGLHeading() {

        String heading = "";
        CChartOfAccounts glCode = new CChartOfAccounts();
        Fund fund = new Fund();
        if (checkNullandEmpty(generalLedgerReportBean.getGlCode1()) && checkNullandEmpty(generalLedgerReportBean.getGlCode1())) {
            glCode = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = ?",
                    generalLedgerReportBean.getGlCode1());
            if (generalLedgerReportBean.getFund_id().isEmpty())
            {
                fund = (Fund) persistenceService.find("from Fund where id = ?", 0);
            }
            else
                fund = (Fund) persistenceService.find("from Fund where id = ?",
                        Integer.parseInt(generalLedgerReportBean.getFund_id()));
        }
        if (fund == null)
        {
            heading = "General Ledger Report for " + glCode.getGlcode() + ":" + glCode.getName()
                    + " from " + generalLedgerReportBean.getStartDate() + " to " + generalLedgerReportBean.getEndDate();
        }
        else
            heading = "General Ledger Report for " + glCode.getGlcode() + ":" + glCode.getName() + " for " + fund.getName()
                    + " from " + generalLedgerReportBean.getStartDate() + " to " + generalLedgerReportBean.getEndDate();
        if (checkNullandEmpty(generalLedgerReportBean.getDepartmentId()))
        {
            final Department dept = (Department) persistenceService.find("from Department where id = ?",
                    Long.parseLong(generalLedgerReportBean.getDepartmentId()));
            heading = heading + " under " + dept.getName() + " Department ";
        }
        if (checkNullandEmpty(generalLedgerReportBean.getFunctionCode()))
        {
            final CFunction function = (CFunction) persistenceService.find("from CFunction where id = ?",
                    Long.valueOf(generalLedgerReportBean.getFunctionCodeId()));
            heading = heading + " in " + function.getName() + " Function ";
        }

        if (checkNullandEmpty(generalLedgerReportBean.getFunctionaryId()))
        {
            final Functionary functionary = (Functionary) persistenceService.find("from Functionary where id = ?",
                    Integer.parseInt(generalLedgerReportBean.getFunctionaryId()));
            heading = heading + " in " + functionary.getName() + " Functionary ";
        }

        if (checkNullandEmpty(generalLedgerReportBean.getFieldId()))
        {
            final Boundary ward = (Boundary) persistenceService.find("from Boundary where id = ?",
                    Long.parseLong(generalLedgerReportBean.getFieldId()));
            heading = heading + " in " + ward.getName() + " Field ";
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

    public GeneralLedgerReportBean getGeneralLedgerReportBean() {
        return generalLedgerReportBean;
    }

    public void setGeneralLedgerReportBean(
            GeneralLedgerReportBean generalLedgerReportBean) {
        this.generalLedgerReportBean = generalLedgerReportBean;
    }

    public GeneralLedgerReport getGeneralLedgerReport() {
        return generalLedgerReport;
    }

    public void setGeneralLedgerReport(GeneralLedgerReport generalLedgerReport) {
        this.generalLedgerReport = generalLedgerReport;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public LinkedList getGeneralLedgerDisplayList() {
        return generalLedgerDisplayList;
    }

    public void setGeneralLedgerDisplayList(final LinkedList generalLedgerDisplayList) {
        this.generalLedgerDisplayList = generalLedgerDisplayList;
    }

    public AppConfigValueService getAppConfigValuesService() {
        return appConfigValuesService;
    }

    public void setAppConfigValuesService(
            AppConfigValueService appConfigValuesService) {
        this.appConfigValuesService = appConfigValuesService;
    }

    public PersistenceService<CChartOfAccounts, Long> getChartOfAccountsService() {
        return chartOfAccountsService;
    }

    public void setChartOfAccountsService(
            PersistenceService<CChartOfAccounts, Long> chartOfAccountsService) {
        this.chartOfAccountsService = chartOfAccountsService;
    }

}