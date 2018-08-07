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

import java.util.LinkedList;

@ParentPackage("egov")
@Results({
        @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "generalLedgerReport-"
                + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp"),
        @Result(name = "results", location = "generalLedgerReport-results.jsp"),
        @Result(name = "searchResult", location = "generalLedgerReport-searchDrilldown.jsp")
})
public class GeneralLedgerReportAction extends BaseFormAction {

    private static final long serialVersionUID = 4734431707050536319L;
    private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReportAction.class);

    @Autowired
    private transient AppConfigValueService appConfigValuesService;

    @Autowired
    @Qualifier("chartOfAccountsService")
    private transient PersistenceService<CChartOfAccounts, Long> chartOfAccountsService;

    @Autowired
    @Qualifier("generalLedgerReport")
    private transient GeneralLedgerReport generalLedgerReport;

    private transient GeneralLedgerReportBean generalLedgerReportBean = new GeneralLedgerReportBean();
    private LinkedList generalLedgerDisplayList = new LinkedList();
    private String heading = "";

    @Override
    public Object getModel() {
        return generalLedgerReportBean;
    }

    public void prepareNewForm() {
        super.prepare();

        addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=true order by name"));
        addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive=true order by name"));
        addDropdownData("fieldList", persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
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
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED)})
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/generalLedgerReport-ajaxSearch")
    @ReadOnly
    public String ajaxSearch() {

        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        try {
            generalLedgerDisplayList = generalLedgerReport.getGeneralLedgerList(generalLedgerReportBean);
        } catch (Exception e) {
            LOGGER.error("Error while getting General Ledger", e);
        }
        heading = getGLHeading();
        generalLedgerReportBean.setHeading(getGLHeading());
        prepareNewForm();
        return "results";
    }

    @Action(value = "/report/generalLedgerReport-searchDrilldown")
    public String searchDrilldown() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        try {
            generalLedgerDisplayList = generalLedgerReport.getGeneralLedgerList(generalLedgerReportBean);
        } catch (Exception e) {
            LOGGER.error("Error while getting General Ledger", e);
        }
        heading = getGLHeading();
        generalLedgerReportBean.setHeading(getGLHeading());
        prepareNewForm();
        return "searchResult";
    }

    private String getGLHeading() {

        StringBuilder glHeading = new StringBuilder(50);
        CChartOfAccounts glCode = new CChartOfAccounts();
        Fund fund = new Fund();
        if (checkNullandEmpty(generalLedgerReportBean.getGlCode1())) {
            glCode = (CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode = ?",
                    generalLedgerReportBean.getGlCode1());
            if (generalLedgerReportBean.getFund_id().isEmpty()) {
                fund = (Fund) persistenceService.find("from Fund where id = ?", 0);
            } else
                fund = (Fund) persistenceService.find("from Fund where id = ?",
                        Integer.parseInt(generalLedgerReportBean.getFund_id()));
        }

        if (fund == null) {
            glHeading.append("General Ledger Report for ").append(glCode.getGlcode())
                    .append(':').append(glCode.getName()).append(" from ").append(generalLedgerReportBean.getStartDate())
                    .append(" to ").append(generalLedgerReportBean.getEndDate()).toString();
        } else
            glHeading.append("General Ledger Report for ").append(glCode.getGlcode()).append(':')
                    .append(glCode.getName()).append(" for ").append(fund.getName()).append(" from ")
                    .append(generalLedgerReportBean.getStartDate()).append(" to ").append(generalLedgerReportBean.getEndDate()).toString();
        if (checkNullandEmpty(generalLedgerReportBean.getDepartmentId())) {
            final Department dept = (Department) persistenceService.find("from Department where id = ?",
                    Long.parseLong(generalLedgerReportBean.getDepartmentId()));
            glHeading.append(" under ").append(dept.getName()).append(" Department ");
        }
        if (checkNullandEmpty(generalLedgerReportBean.getFunctionCode())) {
            final CFunction function = (CFunction) persistenceService.find("from CFunction where id = ?",
                    Long.valueOf(generalLedgerReportBean.getFunctionCodeId()));
            glHeading.append(" in ").append(function.getName()).append(" Function ");
        }

        if (checkNullandEmpty(generalLedgerReportBean.getFunctionaryId())) {
            final Functionary functionary = (Functionary) persistenceService.find("from Functionary where id = ?",
                    Integer.parseInt(generalLedgerReportBean.getFunctionaryId()));
            glHeading.append(" in ").append(functionary.getName()).append(" Functionary ");
        }

        if (checkNullandEmpty(generalLedgerReportBean.getFieldId())) {
            final Boundary ward = (Boundary) persistenceService.find("from Boundary where id = ?",
                    Long.parseLong(generalLedgerReportBean.getFieldId()));
            glHeading.append(" in ").append(ward.getName()).append(" Field ");
        }
        return glHeading.toString();
    }

    private boolean checkNullandEmpty(final String column) {
        return column != null && !column.isEmpty();

    }

    public GeneralLedgerReportBean getGeneralLedgerReportBean() {
        return generalLedgerReportBean;
    }

    public void setGeneralLedgerReportBean(GeneralLedgerReportBean generalLedgerReportBean) {
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