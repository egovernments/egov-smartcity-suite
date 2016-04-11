package org.egov.works.master.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.ScheduleOfRate;

public class ScheduleOfRateService extends PersistenceService<ScheduleOfRate, Long> {

    @PersistenceContext
    private EntityManager entityManager;

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
