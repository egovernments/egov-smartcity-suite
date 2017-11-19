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
package org.egov.works.web.actions.reports;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AjaxWorksGISReportAction extends BaseFormAction {

    private static final long serialVersionUID = -4158479843943110898L;
    private List<Boundary> wardList;
    private static final String WARDS = "wards";
    private Long zoneId;
    private String query;
    private List<String> estimateNumberSearchList = new LinkedList<String>();
    private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
    @Autowired
    private BoundaryService boundaryService;

    public String ajaxLoadWardsByZone() {
        wardList = boundaryService.getAllBoundariesByBoundaryTypeId(zoneId);
        return WARDS;
    }

    /*
     * Autocomplete of Admin sanctioned Estimate nos for Cancel Estimate screen
     */
    public String searchEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and UPPER(ae.estimateNumber) like '%'||?||'%' "
                    + " and ae.egwStatus.code != 'NEW' )";
            params.add(query.toUpperCase());

            estimateNumberSearchList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    public List<Boundary> getWardList() {
        return wardList;
    }

    public void setWardList(final List<Boundary> wardList) {
        this.wardList = wardList;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<String> getEstimateNumberSearchList() {
        return estimateNumberSearchList;
    }
}