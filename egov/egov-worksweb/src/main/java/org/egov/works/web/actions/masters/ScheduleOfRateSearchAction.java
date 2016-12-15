/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.works.web.actions.masters;

import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.masters.entity.SORRate;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = ScheduleOfRateSearchAction.SEARCH_RESULTS, location = "scheduleofratesearch-searchresults.jsp"),
        @Result(name = ScheduleOfRateSearchAction.SOR, location = "scheduleofratesearch-sor.jsp") })
public class ScheduleOfRateSearchAction extends BaseFormAction {

    private static final long serialVersionUID = -3299140283276738474L;
    @Autowired
    private ScheduleOfRateService scheduleOfRateService;
    public static final String SEARCH_RESULTS = "searchResults";
    public static final String SOR = "SOR";
    private ScheduleOfRate sor = new ScheduleOfRate();
    private SORRate currentRate;
    private Long sorID;
    private Date estimateDate;
    private String query;
    private String scheduleCategoryId;
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
            final ScheduleOfRateService scheduleOfRateService) {
        this.scheduleOfRateService = scheduleOfRateService;
    }

    public List<ScheduleOfRate> getSORList() {
        if (estimateId != null && estimateDate != null)
            return scheduleOfRateService.getScheduleOfRatesByCodeAndScheduleOfCategories(query, scheduleCategoryId.toString(),
                    estimateDate);
        else if (estimateDate != null)
            return scheduleOfRateService.getScheduleOfRatesByCodeAndScheduleOfCategories(query, scheduleCategoryId.toString(),
                    estimateDate);
        else
            return scheduleOfRateService.getScheduleOfRatesByCodeAndScheduleOfCategories(query, scheduleCategoryId.toString(),
                    estimateDate);

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

    public void setScheduleCategoryId(final String scheduleCategoryId) {
        this.scheduleCategoryId = scheduleCategoryId;
    }

    public List<ScheduleOfRate> getScheduleOfRateList() {
        return scheduleOfRateList;
    }

}