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
import org.egov.commons.SubScheme;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.models.masters.Contractor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AjaxWorkProgressAction extends BaseFormAction {

    private static final long serialVersionUID = -8272472599041291866L;
    private Integer schemeId;
    private List<SubScheme> subSchemes;
    private static final String SUBSCHEMES = "subschemes";
    private String query;
    private List<Contractor> contractorList = new LinkedList<Contractor>();

    @Override
    public Object getModel() {

        return null;
    }

    public AjaxWorkProgressAction() {

    }

    public String loadSubSchemes() {
        subSchemes = getPersistenceService().findAllBy("from SubScheme where scheme.id=?", schemeId);
        return SUBSCHEMES;
    }

    // TODO: check only for approved work orders
    public String searchAllContractorsForWorkOrder() {
        if (!StringUtils.isEmpty(query)) {
            final StringBuilder strquery = new StringBuilder(300);
            final ArrayList<Object> params = new ArrayList<Object>();
            strquery.append(
                    "select distinct(woe.workOrder.contractor) from WorkOrderEstimate woe where upper(woe.workOrder.contractor.name) like '%'||?||'%'");
            strquery.append(" or upper(woe.workOrder.contractor.code) like '%'||?||'%'");
            strquery.append(" and woe.workOrder.egwStatus.code='APPROVED'");
            params.add(query.toUpperCase());
            params.add(query.toUpperCase());
            contractorList = getPersistenceService().findAllBy(strquery.toString(), params.toArray());
        }
        return "contractorNameSearchResults";
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final Integer schemeId) {
        this.schemeId = schemeId;
    }

    public List<SubScheme> getSubSchemes() {
        return subSchemes;
    }

    public void setSubSchemes(final List<SubScheme> subSchemes) {
        this.subSchemes = subSchemes;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<Contractor> getContractorList() {
        return contractorList;
    }

    public void setContractorList(final List<Contractor> contractorList) {
        this.contractorList = contractorList;
    }

}
