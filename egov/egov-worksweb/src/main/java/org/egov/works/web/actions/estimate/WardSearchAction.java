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
package org.egov.works.web.actions.estimate;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.actions.BaseFormAction;

import java.util.List;

@Result(name = WardSearchAction.SEARCH_RESULTS, location = "wardSearch-searchResults.jsp")
@ParentPackage("egov")
public class WardSearchAction extends BaseFormAction {

    private static final long serialVersionUID = -1549853362997914848L;
    public static final String SEARCH_RESULTS = "searchResults";
    private String query;
    private Boolean isBoundaryHistory;
    private List<Boundary> boundaryList;

    public void setQuery(final String query) {
        this.query = query;
    }

    @Action(value = "/estimate/wardSearch-searchAjax")
    public String searchAjax() {
        boundaryList = getJurisdictionList();
        return SEARCH_RESULTS;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public List<Boundary> getJurisdictionList() {
        final StringBuilder boundaryListQuery = new StringBuilder(1000);
        boundaryListQuery.append(" from Boundary where upper(boundaryType.name) in ('CITY','ZONE','WARD') "
                + " and upper(boundaryType.hierarchyType.name)='ADMINISTRATION' and upper(name) like '%' || ? || '%' ");
        if (!isBoundaryHistory)
            boundaryListQuery.append(" and active = true ");
        boundaryListQuery.append(" order by name ");
        return getPersistenceService().findAllBy(boundaryListQuery.toString(), query.toUpperCase());
    }

    public Boolean getIsBoundaryHistory() {
        return isBoundaryHistory;
    }

    public void setIsBoundaryHistory(final Boolean isBoundaryHistory) {
        this.isBoundaryHistory = isBoundaryHistory;
    }

    public List<Boundary> getBoundaryList() {
        return boundaryList;
    }

}