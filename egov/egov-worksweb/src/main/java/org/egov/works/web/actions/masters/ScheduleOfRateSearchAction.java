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
package org.egov.works.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.SORRate;
import org.egov.works.models.masters.ScheduleOfRate;

import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({ @Result(name = ScheduleOfRateSearchAction.SEARCH_RESULTS, location = "scheduleOfRateSearch-searchResults.jsp"),
        @Result(name = ScheduleOfRateSearchAction.SOR, location = "scheduleOfRateSearch-SOR.jsp") })
public class ScheduleOfRateSearchAction extends BaseFormAction {

    private static final long serialVersionUID = -3299140283276738474L;
    private PersistenceService<ScheduleOfRate, Long> scheduleOfRateService;
    public static final String SEARCH_RESULTS = "searchResults";
    public static final String SOR = "SOR";
    private ScheduleOfRate sor = new ScheduleOfRate();
    private SORRate currentRate;
    private Long sorID;
    private Date estimateDate;
    private String query;
    private Long scheduleCategoryId;
    private Long estimateId;
    private List<ScheduleOfRate> scheduleOfRateList;

    public void setQuery(final String query) {
        this.query = query;
    }

    @Action(value = "/masters/scheduleOfRateSearch-searchAjax")
    public String searchAjax() {
        scheduleOfRateList = getSORList();
        return SEARCH_RESULTS;
    }

    @Action(value = "/masters/scheduleOfRateSearch-findSORAjax")
    public String findSORAjax() {
        sor = scheduleOfRateService.findById(sorID, false);
        if (estimateDate != null)
            currentRate = sor.getRateOn(estimateDate);
        return SOR;
    }

    @Override
    public Object getModel() {
        return sor;
    }

    public void setScheduleOfRateService(
            final PersistenceService<ScheduleOfRate, Long> scheduleOfRateService) {
        this.scheduleOfRateService = scheduleOfRateService;
    }

    public List<ScheduleOfRate> getSORList() {
        if (estimateId != null && estimateDate != null)
            return scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_SEARCH_REVISIONESTIMATE", query, query,
                    scheduleCategoryId, estimateDate, estimateDate, estimateId, estimateId);
        else if (estimateDate != null)
            return scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_SEARCH", query, query,
                    scheduleCategoryId, estimateDate, estimateDate);
        else
            return scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_SEARCH_ESTIMATETEMPLATE", query, query,
                    scheduleCategoryId);

    }

    public void setSorID(final Long sorID) {
        this.sorID = sorID;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public ScheduleOfRate getSor() {
        return sor;
    }

    public void setSor(final ScheduleOfRate scheduleOfRateInstance) {
        sor = scheduleOfRateInstance;
    }

    public SORRate getCurrentRate() {
        return currentRate;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public void setScheduleCategoryId(final Long scheduleCategoryId) {
        this.scheduleCategoryId = scheduleCategoryId;
    }

    public List<ScheduleOfRate> getScheduleOfRateList() {
        return scheduleOfRateList;
    }

}