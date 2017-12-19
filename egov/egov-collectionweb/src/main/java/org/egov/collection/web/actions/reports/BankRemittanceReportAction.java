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

package org.egov.collection.web.actions.reports;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.springframework.beans.factory.annotation.Autowired;

@Results({ @Result(name = BankRemittanceReportAction.INDEX, location = "bankRemittanceReport-index.jsp"),
        @Result(name = BankRemittanceReportAction.REPORT, location = "bankRemittanceReport-report.jsp") })
public class BankRemittanceReportAction extends ReportFormAction {

    private static final long serialVersionUID = 1L;

    private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
    private static final String BANK_REMITTANCE_REPORT_TEMPLATE = "bank_remittance";

    private CollectionsUtil collectionsUtil;
    @Autowired
    private CityService cityService;

    /*
     * (non-Javadoc)
     * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
     */
    @Override
    public void prepare() {
        setReportFormat(ReportFormat.PDF);
        setDataSourceType(ReportDataSourceType.SQL);
    }

    /**
     * @param collectionsUtil the collections utility object to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @return the department id
     */
    public Integer getDeptId() {
        return (Integer) getReportParam(EGOV_DEPT_ID);
    }

    /**
     * @param deptId the department id to set
     */
    public void setDeptId(final Integer deptId) {
        setReportParam(EGOV_DEPT_ID, deptId);
    }

    @Override
    @Action(value = "/reports/bankRemittanceReport-criteria")
    public String criteria() {
        // Setup drop down data for department list
        addRelatedEntity("department", Department.class, "name");
        setupDropdownDataExcluding();

        // Set default values of criteria fields
        final Department dept = collectionsUtil.getDepartmentOfLoggedInUser();
        if (dept != null)
            setReportParam(EGOV_DEPT_ID, dept.getId());

        return INDEX;
    }

    @Override
    @Action(value = "/reports/bankRemittanceReport-report")
    public String report() {
        setReportParam(CollectionConstants.LOGO_PATH, cityService.getCityLogoAsStream());
        return super.report();
    }

    @Override
    protected String getReportTemplateName() {
        return BANK_REMITTANCE_REPORT_TEMPLATE;
    }
}
