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
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.models.estimate.EstimateTemplate;
import org.egov.works.models.masters.SORRate;

import java.util.Date;
import java.util.List;

@Results({
        @Result(name = AjaxEstimateTemplateAction.SEARCH_RESULTS, location = "ajaxEstimateTemplate-searchResults.jsp"),
        @Result(name = AjaxEstimateTemplateAction.ACTIVITIES, location = "ajaxEstimateTemplate-activities.jsp")
})
public class AjaxEstimateTemplateAction extends BaseFormAction {

    private static final long serialVersionUID = 4779374304829178146L;
    private EstimateTemplate estimateTemplate = new EstimateTemplate();
    private static final String CODEUNIQUECHECK = "codeUniqueCheck";
    public static final String SEARCH_RESULTS = "searchResults";
    public static final String ACTIVITIES = "activities";
    private int status;
    private String code;
    private long workTypeId;
    private long subTypeId;
    private SORRate currentRate;
    private Date estimateDate;
    private String query;
    private List<EstimateTemplate> estimateTemplateList;

    @Override
    public Object getModel() {

        return estimateTemplate;
    }

    @Action(value = "/estimate/ajaxEstimateTemplate-searchAjax")
    public String searchAjax() {
        estimateTemplateList = getEstimateTemplates();
        return SEARCH_RESULTS;
    }

    public List<EstimateTemplate> getEstimateTemplates() {
        String strquery = "";
        if (workTypeId > 0)
            strquery = "from EstimateTemplate et where upper(et.code) like '" + query.toUpperCase()
                    + "%' and et.workType.id=" + workTypeId;
        if (subTypeId > 0)
            strquery += " and et.subType.id=" + subTypeId;
        if (status == 1)
            strquery += " and et.status=" + status;
        return getPersistenceService().findAllBy(strquery);
    }

    @Action(value = "/estimate/ajaxEstimateTemplate-findCodeAjax")
    public String findCodeAjax() {
        estimateTemplate = (EstimateTemplate) getPersistenceService().find("from EstimateTemplate where upper(code)=?",
                code.toUpperCase());

        return ACTIVITIES;
    }

    public String codeUniqueCheck() {
        return CODEUNIQUECHECK;
    }

    public boolean getCodeCheck() {
        boolean codeexistsOrNot = false;
        Long estimateTemplateId = null;
        if (code != null)
            if (getPersistenceService().findByNamedQuery("EstimateTemplateCodeUniqueCheck", code.toUpperCase()) != null)
                estimateTemplateId = (Long) getPersistenceService().findByNamedQuery("EstimateTemplateCodeUniqueCheck",
                        code.toUpperCase());
        if (estimateTemplateId != null)
            codeexistsOrNot = true;
        else
            codeexistsOrNot = false;

        return codeexistsOrNot;
    }

    public EstimateTemplate getEstimateTemplate() {
        return estimateTemplate;
    }

    public void setEstimateTemplate(final EstimateTemplate estimateTemplate) {
        this.estimateTemplate = estimateTemplate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public void setStatus(final String status) {
        if (status != null && !status.equalsIgnoreCase(""))
            this.status = Integer.parseInt(status);
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public long getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(final long workTypeId) {
        this.workTypeId = workTypeId;
    }

    public long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(final long subTypeId) {
        this.subTypeId = subTypeId;
    }

    public SORRate getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(final SORRate currentRate) {
        this.currentRate = currentRate;
    }

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<EstimateTemplate> getEstimateTemplateList() {
        return estimateTemplateList;
    }

}
