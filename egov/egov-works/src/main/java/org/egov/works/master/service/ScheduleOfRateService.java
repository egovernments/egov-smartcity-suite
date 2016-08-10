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

package org.egov.works.master.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.works.master.repository.ScheduleOfRateRepository;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.SORRate;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.services.WorksService;
import org.egov.works.uploadsor.UploadScheduleOfRate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleOfRateService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScheduleOfRateRepository scheduleOfRateRepository;

    @Autowired
    private WorksService worksService;

    @Autowired
    private UserService userService;

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

    @Transactional
    public ScheduleOfRate save(final ScheduleOfRate scheduleOfRate) {
        return scheduleOfRateRepository.save(scheduleOfRate);
    }

    public ScheduleOfRate findById(final Long id, final boolean b) {
        return scheduleOfRateRepository.findOne(id);
    }

    public List<ScheduleOfRate> getScheduleOfRatesByCodeAndScheduleOfCategories(final String code, final String ids,
            Date estimateDate) {
        final List<Long> scheduleOfCategoryIds = new ArrayList<Long>();
        final String[] split = ids.split(",");
        for (final String s : split)
            scheduleOfCategoryIds.add(Long.parseLong(s));
        if (estimateDate == null)
            estimateDate = new Date();
        final List<ScheduleOfRate> scheduleOfRates = scheduleOfRateRepository
                .findByCodeContainingIgnoreCaseAndScheduleCategory_IdInOrderByCode(code.toUpperCase(),
                        scheduleOfCategoryIds, estimateDate);
        for (final ScheduleOfRate rate : scheduleOfRates)
            rate.setSorRateValue(rate.getRateOn(estimateDate).getRate().getValue());

        return scheduleOfRates;
    }

    // TODO: Need to remove this method after getting better alternate option
    public ScheduleOfRate setPrimaryDetails(final ScheduleOfRate scheduleOfRate) {
        final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
        if (scheduleOfRate.getId() == null) {
            scheduleOfRate.setCreatedBy(user);
            scheduleOfRate.setCreatedDate(new Date());
        }
        scheduleOfRate.setModifiedBy(user);
        scheduleOfRate.setModifiedDate(new Date());
        return scheduleOfRate;
    }

    // TODO: Need to remove this method after getting better alternate option
    public SORRate setPrimaryDetailsForSorRates(final SORRate sorRate) {
        final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
        sorRate.setCreatedBy(user);
        sorRate.setCreatedDate(new Date());
        sorRate.setModifiedBy(user);
        sorRate.setModifiedDate(new Date());
        return sorRate;
    }

    // TODO: Need to remove this method after getting better alternate option
    public MarketRate setPrimaryDetailsForMarketRates(final MarketRate marketRate) {
        final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
        marketRate.setCreatedBy(user);
        marketRate.setCreatedDate(new Date());
        marketRate.setModifiedBy(user);
        marketRate.setModifiedDate(new Date());
        return marketRate;
    }

    public ScheduleOfRate getByCode(final String code) {
        return scheduleOfRateRepository.findByCode(code);
    }

    public ScheduleOfRate getByCodeAndScheduleCategoryId(final String code, final Long id) {
        return scheduleOfRateRepository.findByCodeAndScheduleCategory_id(code, id);
    }

    @Transactional
    public List<UploadScheduleOfRate> createScheduleOfRate(final List<UploadScheduleOfRate> uploadSORRatesList) {
        final Date currentDate = new Date();
        for (final UploadScheduleOfRate obj : uploadSORRatesList) {

            if (obj.getCreateSor()) {
                final ScheduleOfRate scheduleOfRate = new ScheduleOfRate();
                final SORRate sorRate = new SORRate();
                final MarketRate marketRate = new MarketRate();
                scheduleOfRate.setCode(obj.getSorCode());
                scheduleOfRate.setScheduleCategory(obj.getScheduleCategory());
                scheduleOfRate.setUom(obj.getUom());
                scheduleOfRate.setDescription(obj.getSorDescription());
                sorRate.setRate(new Money(obj.getRate().doubleValue()));
                sorRate.setValidity(new Period(obj.getFromDate(), obj.getToDate() != null ? obj.getToDate() : null));
                sorRate.setScheduleOfRate(scheduleOfRate);
                sorRate.setCreatedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                sorRate.setCreatedDate(currentDate);
                sorRate.setModifiedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                sorRate.setModifiedDate(currentDate);
                scheduleOfRate.getSorRates().add(sorRate);
                if (obj.getMarketRate() != null) {

                    marketRate.setMarketRate(new Money(obj.getMarketRate().doubleValue()));
                    marketRate.setValidity(
                            new Period(obj.getMarketFromDate(), obj.getMarketToDate() != null ? obj.getMarketToDate() : null));
                    marketRate.setCreatedBy(
                            entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                    marketRate.setCreatedDate(currentDate);
                    marketRate.setModifiedBy(
                            entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                    marketRate.setModifiedDate(currentDate);
                    marketRate.setScheduleOfRate(scheduleOfRate);
                    scheduleOfRate.getMarketRates().add(marketRate);
                }

                scheduleOfRate.setCreatedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                scheduleOfRate.setCreatedDate(currentDate);
                scheduleOfRate.setModifiedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                scheduleOfRate.setModifiedDate(currentDate);
                save(scheduleOfRate);
            }
            obj.setFinalStatus("Success");
        }

        return uploadSORRatesList;
    }

    @Transactional
    public List<UploadScheduleOfRate> createSORRate(final List<UploadScheduleOfRate> uploadSORRatesList) {
        final Date currentDate = new Date();
        for (final UploadScheduleOfRate obj : uploadSORRatesList) {

            final ScheduleOfRate scheduleOfRate = obj.getScheduleOfRate();
            final SORRate sorRate = new SORRate();
            final MarketRate marketRate = new MarketRate();
            sorRate.setRate(new Money(obj.getRate().doubleValue()));
            sorRate.setValidity(new Period(obj.getFromDate(), obj.getToDate() != null ? obj.getToDate() : null));
            sorRate.setScheduleOfRate(scheduleOfRate);
            sorRate.setCreatedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            sorRate.setCreatedDate(currentDate);
            sorRate.setModifiedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            sorRate.setModifiedDate(currentDate);
            scheduleOfRate.getSorRates().add(sorRate);
            if (obj.getMarketRate() != null) {

                marketRate.setMarketRate(new Money(obj.getMarketRate().doubleValue()));
                marketRate.setValidity(
                        new Period(obj.getMarketFromDate(), obj.getMarketToDate() != null ? obj.getMarketToDate() : null));
                marketRate.setCreatedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                marketRate.setCreatedDate(currentDate);
                marketRate.setModifiedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                marketRate.setModifiedDate(currentDate);
                marketRate.setScheduleOfRate(scheduleOfRate);
                scheduleOfRate.getMarketRates().add(marketRate);
            }

            scheduleOfRate.setModifiedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            scheduleOfRate.setModifiedDate(currentDate);
            save(scheduleOfRate);
            obj.setFinalStatus("Success");
        }

        return uploadSORRatesList;
    }

}
