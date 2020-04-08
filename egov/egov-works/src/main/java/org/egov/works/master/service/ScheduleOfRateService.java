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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.workorder.WorkOrderEstimate;

@SuppressWarnings("deprecation")
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
        return entityManager.find(ScheduleOfRate.class, scheduleOfRateId);
    }

    public List<ScheduleOfRate> getAllScheduleOfRates() {
        return entityManager.createQuery("from ScheduleOfRate sor order by code asc", ScheduleOfRate.class).getResultList();
    }

    public List<AbstractEstimate> getAllAbstractEstimateByScheduleOrRateId(final Long scheduleOfRateId) {
        return entityManager.createQuery(new StringBuffer("select ae")
                .append(" from AbstractEstimate ae, Activity act")
                .append(" where act.abstractEstimate = ae and act.abstractEstimate.parent is null")
                .append(" and act.abstractEstimate.egwStatus.code <> 'CANCELLED' and act.schedule.id = :scheduleOfRateId")
                .toString(), AbstractEstimate.class)
                .setParameter("scheduleOfRateId", scheduleOfRateId)
                .getResultList();
    }

    public List<WorkOrderEstimate> getAllWorkOrderEstimateByScheduleOfRateId(final Long scheduleOfRateId) {
        return entityManager.createQuery(new StringBuffer("select distinct(woa.workOrderEstimate)")
                .append(" from WorkOrderActivity woa")
                .append(" where woa.workOrderEstimate.estimate.parent.id is not null")
                .append(" and woa.workOrderEstimate.estimate.egwStatus.code<> 'CANCELLED'")
                .append(" and exists (select sor.id from ScheduleOfRate sor where sor.id = woa.activity.schedule.id")
                .append(" and sor.id = :scheduleOfRateId )").toString(), WorkOrderEstimate.class)
                .setParameter("scheduleOfRateId", scheduleOfRateId)
                .getResultList();
    }

    public SearchQuery prepareSearchQuery(final Long scheduleCategoryId, final String code, final String description) {
        final StringBuffer scheduleOfRateSql = new StringBuffer(100);
        final List<Object> paramList = new ArrayList<>();
        int index = 1;
        scheduleOfRateSql.append(" from ScheduleOfRate sor where sor.scheduleCategory.id=?").append(index++);
        paramList.add(scheduleCategoryId);

        if (code != null && !code.equals("")) {
            scheduleOfRateSql.append(" and UPPER(sor.code) like ?").append(index++);
            paramList.add("%" + code.toUpperCase() + "%");
        }

        if (description != null && !description.equals("")) {
            scheduleOfRateSql.append(" and UPPER(sor.description) like ?").append(index);
            paramList.add("%" + description.toUpperCase() + "%");
        }
        final String countQuery = "select count(*) " + scheduleOfRateSql;
        return new SearchQueryHQL(scheduleOfRateSql.toString(), countQuery, paramList);
    }
}
