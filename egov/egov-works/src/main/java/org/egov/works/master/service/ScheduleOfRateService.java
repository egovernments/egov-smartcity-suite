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

package org.egov.works.master.service;

import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.ScheduleOfRate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ScheduleOfRateService extends PersistenceService<ScheduleOfRate, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public ScheduleOfRateService() {
        super(ScheduleOfRate.class);
    }

    public ScheduleOfRateService(Class<ScheduleOfRate> type) {
        super(type);
    }

    public ScheduleOfRate getScheduleOfRateById(final Long scheduleOfRateId) {
        final ScheduleOfRate scheduleOfRate = entityManager.find(ScheduleOfRate.class,
                scheduleOfRateId);
        return scheduleOfRate;
    }

    public List<ScheduleOfRate> getAllScheduleOfRates() {
        final Query query = entityManager.createQuery("from ScheduleOfRate sor order by code asc");
        final List<ScheduleOfRate> scheduleOfRateList = query.getResultList();
        return scheduleOfRateList;
    }

    public List getAllAbstractEstimateByScheduleOrRateId(final Long scheduleOfRateId) {
        final Query query = entityManager.createQuery(
                "select ae from AbstractEstimate ae, Activity act where act.abstractEstimate = ae and act.abstractEstimate.parent is null and act.abstractEstimate.egwStatus.code <> 'CANCELLED' and act.schedule.id = :scheduleOfRateId");
        query.setParameter("scheduleOfRateId", scheduleOfRateId);
        final List list = query.getResultList();
        return list;
    }

    public List getAllWorkOrderEstimateByScheduleOfRateId(final Long scheduleOfRateId) {
        final Query query = entityManager.createQuery(
                "select distinct(woa.workOrderEstimate) from WorkOrderActivity woa where woa.workOrderEstimate.estimate.parent.id is not null and woa.workOrderEstimate.estimate.egwStatus.code<> 'CANCELLED' and exists (select sor.id from ScheduleOfRate sor where sor.id = woa.activity.schedule.id and sor.id = :scheduleOfRateId )");
        query.setParameter("scheduleOfRateId", scheduleOfRateId);
        final List list = query.getResultList();
        return list;
    }

    public SearchQuery prepareSearchQuery(final Long scheduleCategoryId, final String code, final String description) {
        final StringBuffer scheduleOfRateSql = new StringBuffer(100);
        String scheduleOfRateStr = "";
        final List<Object> paramList = new ArrayList<Object>();
        scheduleOfRateSql.append(" from ScheduleOfRate sor where sor.scheduleCategory.id=?");
        paramList.add(scheduleCategoryId);

        if (code != null && !code.equals("")) {
            scheduleOfRateSql.append(" and UPPER(sor.code) like ?");
            paramList.add("%" + code.toUpperCase() + "%");
        }

        if (description != null && !description.equals("")) {
            scheduleOfRateSql.append(" and UPPER(sor.description) like ?");
            paramList.add("%" + description.toUpperCase() + "%");
        }
        scheduleOfRateStr = scheduleOfRateSql.toString();
        final String countQuery = "select count(*) " + scheduleOfRateStr;
        return new SearchQueryHQL(scheduleOfRateStr, countQuery, paramList);
    }
}
