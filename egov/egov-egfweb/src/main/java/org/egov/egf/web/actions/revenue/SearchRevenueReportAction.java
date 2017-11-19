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
package org.egov.egf.web.actions.revenue;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.egf.revenue.Grant;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



@Results({
    @Result(name = "view", location = "searchRevenueReport-view.jsp")
})
public class SearchRevenueReportAction extends BaseRevenueAction {
    /**
     *
     */
    private static final long serialVersionUID = 2577746024306261450L;
    private static final Logger LOGGER = Logger.getLogger(SearchRevenueReportAction.class);
    private ReportHelper reportHelper;
    private InputStream inputStream;
    private List<String> grantTypeList;
    private Long finYearId;
    private String deptId;
    private String grantTypeStr;

    @Override
    public void prepare() {
        grantTypeList = new ArrayList<String>();
        grantTypeList.add(Constants.GRANT_TYPE_CFC);
        grantTypeList.add(Constants.GRANT_TYPE_ET);
        grantTypeList.add(Constants.GRANT_TYPE_SD);
        grantTypeList.add(Constants.GRANT_TYPE_SFC);
        addDropdownData("finanYearList",
                persistenceService.findAllBy("from CFinancialYear  where isActive=true order by finYearRange desc"));
        addDropdownData("grtTypeList", grantTypeList);
        addDropdownData("deptList", persistenceService.findAllBy("from Department order by deptName "));

    }

    @SkipValidation
    @Action(value = "/revenue/searchRevenueReport-beforeSearch")
    public String beforeSearch() {
        return "view";
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/revenue/searchRevenueReport-search")
    public String search() {
        final StringBuffer query = new StringBuffer();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Search Query:-" + "Financials Year id" + finYearId);
        query.append("select distinct gr.financialYear ,gr.grantType,gr.department From Grant gr where gr.financialYear.id="
                + finYearId);
        if (!deptId.equals("-1") && !grantTypeStr.equals("-1"))
            query.append(" and gr.grantType='" + grantTypeStr + "' and gr.department.id='" + deptId + "'");
        else {
            if (!grantTypeStr.equals("-1"))
                query.append(" and gr.grantType='" + grantTypeStr + "'");
            if (!deptId.equals("-1"))
                query.append(" and gr.department.id='" + deptId + "'");
        }
        final List<Object[]> findAllBy = persistenceService.findAllBy(query.toString());
        grantsList = new ArrayList<Grant>();
        // this loop needs to be replaced by query using hibernate facilities
        for (final Object[] ob : findAllBy)
        {
            Grant grant2;
            final CFinancialYear fy = (CFinancialYear) ob[0];
            final String type = (String) ob[1];
            final Department dept = (Department) ob[2];
            grant2 = new Grant();
            grant2.setFinancialYear(fy);
            grant2.setDepartment(dept);
            grant2.setGrantType(type);
            grantsList.add(grant2);
        }
        return "view";
    }

    public Long getFinYearId() {
        return finYearId;
    }

    public void setFinYearId(final Long finYearId) {
        this.finYearId = finYearId;
    }

    public List<String> getGrantTypeList() {
        return grantTypeList;
    }

    public void setGrantTypeList(final List<String> grantTypeList) {
        this.grantTypeList = grantTypeList;
    }

    public String getGrantTypeStr() {
        return grantTypeStr;
    }

    public void setGrantTypeStr(final String grantTypeStr) {
        this.grantTypeStr = grantTypeStr;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(final String deptId) {
        this.deptId = deptId;
    }

}
