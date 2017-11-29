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

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.egf.model.ReportSearch;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;



public class ReportAction extends BaseFormAction
{
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final long serialVersionUID = 1L;
    protected ReportSearch reportSearch = new ReportSearch();
    protected List<String> headerFields = new ArrayList<String>();
    protected List<String> mandatoryFields = new ArrayList<String>();
    protected List<Object> schemeList = new ArrayList<Object>();
    protected static final String REPORT = "report";
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    protected SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    @Autowired
    private AppConfigValueService appConfigValueService;

    @Override
    public Object getModel() {
        return reportSearch;
    }

    public ReportAction()
    {
        addRelatedEntity(Constants.DEPARTMENT, Department.class);
        addRelatedEntity(Constants.FUNCTION, CFunction.class);
        addRelatedEntity(Constants.FUND, Fund.class);
        addRelatedEntity(Constants.SCHEME, Scheme.class);
        addRelatedEntity(Constants.SUBSCHEME, SubScheme.class);
        addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
        addRelatedEntity(Constants.FUNDSOURCE, Fundsource.class);
        addRelatedEntity(Constants.FIELD, Boundary.class);
    }

    @Override
    public void prepare()
    {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        getHeaderFields();
        if (headerFields.contains(Constants.DEPARTMENT))
            addDropdownData("departmentList", persistenceService.findAllBy("from Department order by name"));
        if (headerFields.contains(Constants.FUNCTION))
            addDropdownData("functionList",
                    persistenceService.findAllBy("from CFunction where isactive=true and isnotleaf=false  order by name"));
        if (headerFields.contains(Constants.FUNCTIONARY))
            addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=true order by name"));
        if (headerFields.contains(Constants.FUND))
            addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        if (headerFields.contains(Constants.FUNDSOURCE))
            addDropdownData("fundsourceList",
                    persistenceService.findAllBy(" from Fundsource where isactive=true order by name"));
        if (headerFields.contains(Constants.FIELD))
            addDropdownData("fieldList",
                    persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
        if (headerFields.contains(Constants.SCHEME))
            addDropdownData("schemeList", Collections.EMPTY_LIST);
        if (headerFields.contains(Constants.SUBSCHEME))
            addDropdownData("subschemeList", Collections.EMPTY_LIST);
    }

    protected void getHeaderFields()
    {
        final List<AppConfigValues> appConfigList = appConfigValueService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, "REPORT_SEARCH_MISATTRRIBUTES");
            for (final AppConfigValues appConfigVal : appConfigList)
            {
                final String value = appConfigVal.getValue();
                final String header = value.substring(0, value.indexOf('|'));
                headerFields.add(header);
                final String mandate = value.substring(value.indexOf('|') + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }
    }

    @Override
    public void validate() {
        checkMandatoryField(Constants.FUND, Constants.FUND, reportSearch.getFund(), "voucher.fund.mandatory");
        checkMandatoryField(Constants.DEPARTMENT, Constants.DEPARTMENT, reportSearch.getDepartment(),
                "voucher.department.mandatory");
        checkMandatoryField(Constants.SCHEME, Constants.SCHEME, reportSearch.getScheme(), "voucher.scheme.mandatory");
        checkMandatoryField(Constants.SUBSCHEME, Constants.SUBSCHEME, reportSearch.getSubScheme(), "voucher.subscheme.mandatory");
        checkMandatoryField(Constants.FUNCTIONARY, Constants.FUNCTIONARY, reportSearch.getFunctionary(),
                "voucher.functionary.mandatory");
        checkMandatoryField(Constants.FUNDSOURCE, Constants.FUNDSOURCE, reportSearch.getFundsource(),
                "voucher.fundsource.mandatory");
        checkMandatoryField(Constants.FUNCTION, Constants.FUNCTION, reportSearch.getFunction(), "voucher.function.mandatory");
        checkMandatoryField(Constants.FIELD, Constants.FIELD, reportSearch.getField(), "voucher.field.mandatory");
    }

    protected void checkMandatoryField(final String objectName, final String fieldName, final Object value, final String errorKey)
    {
        if (mandatoryFields.contains(fieldName) && value == null)
            addFieldError(objectName, getText(errorKey));
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    public String ajaxLoadSchemes()
    {
        schemeList = persistenceService.findAllBy(" from Scheme where fund=?", reportSearch.getFund());
        return "schemes";
    }

    public String ajaxLoadSubSchemes()
    {
        schemeList = persistenceService.findAllBy(" from SubScheme where scheme=?", reportSearch.getScheme());
        return "schemes";
    }

    public List<Object> getSchemeList() {
        return schemeList;
    }

    public void setReportSearch(final ReportSearch reportSearch) {
        this.reportSearch = reportSearch;
    }

    public ReportSearch getReportSearch() {
        return reportSearch;
    }
}