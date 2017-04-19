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

package org.egov.works.masters.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.service.UOMService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.masters.entity.MarketRate;
import org.egov.works.masters.entity.SORRate;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.entity.ScheduleOfRateSearchRequest;
import org.egov.works.masters.repository.ScheduleOfRateRepository;
import org.egov.works.uploadsor.UploadScheduleOfRate;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional(readOnly = true)
public class ScheduleOfRateService {
    
    private static final String SORRATEDATESOVERLAPERRORCODE = "error.sor.rate.dates.overlap";

    @PersistenceContext
    private EntityManager entityManager;

    private final ScheduleOfRateRepository scheduleOfRateRepository;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private UOMService uomService;

    @Autowired
    public ScheduleOfRateService(final ScheduleOfRateRepository scheduleOfRateRepository) {
        this.scheduleOfRateRepository = scheduleOfRateRepository;
    }

    public ScheduleOfRate getScheduleOfRateById(final Long scheduleOfRateId) {
        return entityManager.find(ScheduleOfRate.class, scheduleOfRateId);
    }

    public List<ScheduleOfRate> getAllScheduleOfRates() {
        final Query query = entityManager.createQuery("from ScheduleOfRate sor order by code asc");
        return query.getResultList();
    }

    public List<AbstractEstimate> getAllAbstractEstimateByScheduleOrRateId(final Long scheduleOfRateId) {
        final Query query = entityManager.createQuery(
                "select ae from AbstractEstimate ae, Activity act where act.abstractEstimate = ae and act.abstractEstimate.parent is null and act.abstractEstimate.egwStatus.code <> 'CANCELLED' and act.schedule.id = :scheduleOfRateId");
        query.setParameter("scheduleOfRateId", scheduleOfRateId);
        return query.getResultList();
    }

    public List<WorkOrderEstimate> getAllWorkOrderEstimateByScheduleOfRateId(final Long scheduleOfRateId) {
        final Query query = entityManager.createQuery(
                "select distinct(woa.workOrderEstimate) from WorkOrderActivity woa where woa.workOrderEstimate.estimate.parent.id is not null and woa.workOrderEstimate.estimate.egwStatus.code<> 'CANCELLED' and exists (select sor.id from ScheduleOfRate sor where sor.id = woa.activity.schedule.id and sor.id = :scheduleOfRateId )");
        query.setParameter("scheduleOfRateId", scheduleOfRateId);
        return query.getResultList();
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

    public List<ScheduleOfRate> getScheduleOfRatesByCodeAndScheduleOfCategoriesAndEstimateId(final String code, final String ids,
            Date estimateDate, final Long estimateId) {
        final List<Long> scheduleOfCategoryIds = new ArrayList<Long>();
        final List<Long> estimateIds = new ArrayList<Long>();
        final List<AbstractEstimate> estimates = estimateService.getAbstractEstimateByParentId(estimateId);
        for (final AbstractEstimate estimate : estimates)
            estimateIds.add(estimate.getId());

        estimateIds.add(estimateId);
        final String[] split = ids.split(",");
        for (final String s : split)
            scheduleOfCategoryIds.add(Long.parseLong(s));
        if (estimateDate == null)
            estimateDate = new Date();
        final List<ScheduleOfRate> scheduleOfRates = scheduleOfRateRepository
                .findByCodeAndScheduleOfCategoriesAndEstimateId(code.toUpperCase(),
                        scheduleOfCategoryIds, estimateDate, estimateIds);
        for (final ScheduleOfRate rate : scheduleOfRates)
            rate.setSorRateValue(rate.getRateOn(estimateDate).getRate().getValue());

        return scheduleOfRates;
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

            final ScheduleOfRate scheduleOfRate = getScheduleOfRateObj();
            final SORRate sorRate = getSorRateObj();
            final MarketRate marketRate = getMarketRateObj();
            scheduleOfRate.setCode(obj.getSorCode());
            scheduleOfRate.setScheduleCategory(obj.getScheduleCategory());
            scheduleOfRate.setUom(obj.getUom());
            scheduleOfRate.setDescription(obj.getSorDescription());
            sorRate.setRate(getMoneyObj(obj.getRate().doubleValue()));
            sorRate.setValidity(getPeriodObj(obj.getFromDate(), obj.getToDate() != null ? obj.getToDate() : null));
            sorRate.setScheduleOfRate(scheduleOfRate);
            sorRate.setCreatedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            sorRate.setCreatedDate(currentDate);
            sorRate.setLastModifiedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            sorRate.setLastModifiedDate(currentDate);
            scheduleOfRate.getSorRates().add(sorRate);
            if (obj.getMarketRate() != null) {

                marketRate.setMarketRate(getMoneyObj(obj.getMarketRate().doubleValue()));
                marketRate.setValidity(getPeriodObj(obj.getMarketFromDate(),
                        obj.getMarketToDate() != null ? obj.getMarketToDate() : null));
                marketRate.setCreatedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                marketRate.setCreatedDate(currentDate);
                marketRate.setLastModifiedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                marketRate.setLastModifiedDate(currentDate);
                marketRate.setScheduleOfRate(scheduleOfRate);
                scheduleOfRate.getMarketRates().add(marketRate);
            }

            scheduleOfRate.setCreatedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            scheduleOfRate.setCreatedDate(currentDate);
            scheduleOfRate.setLastModifiedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            scheduleOfRate.setLastModifiedDate(currentDate);
            save(scheduleOfRate);
            obj.setFinalStatus("Success");
        }

        return uploadSORRatesList;
    }

    @Transactional
    public List<UploadScheduleOfRate> createSORRate(final List<UploadScheduleOfRate> uploadSORRatesList) {
        final Date currentDate = new Date();
        for (final UploadScheduleOfRate obj : uploadSORRatesList) {

            final ScheduleOfRate scheduleOfRate = obj.getScheduleOfRate();
            final SORRate sorRate = getSorRateObj();
            final MarketRate marketRate = getMarketRateObj();
            sorRate.setRate(getMoneyObj(obj.getRate().doubleValue()));
            sorRate.setValidity(getPeriodObj(obj.getFromDate(), obj.getToDate() != null ? obj.getToDate() : null));
            sorRate.setScheduleOfRate(scheduleOfRate);
            sorRate.setCreatedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            sorRate.setCreatedDate(currentDate);
            sorRate.setLastModifiedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            sorRate.setLastModifiedDate(currentDate);
            scheduleOfRate.getSorRates().add(sorRate);
            if (obj.getMarketRate() != null) {

                marketRate.setMarketRate(getMoneyObj(obj.getMarketRate().doubleValue()));
                marketRate.setValidity(getPeriodObj(obj.getMarketFromDate(),
                        obj.getMarketToDate() != null ? obj.getMarketToDate() : null));
                marketRate.setCreatedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                marketRate.setCreatedDate(currentDate);
                marketRate.setLastModifiedBy(
                        entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
                marketRate.setLastModifiedDate(currentDate);
                marketRate.setScheduleOfRate(scheduleOfRate);
                scheduleOfRate.getMarketRates().add(marketRate);
            }

            scheduleOfRate.setLastModifiedBy(
                    entityManager.unwrap(Session.class).load(User.class, ApplicationThreadLocals.getUserId()));
            scheduleOfRate.setLastModifiedDate(currentDate);
            save(scheduleOfRate);
            obj.setFinalStatus("Success");
        }

        return uploadSORRatesList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Boolean validateRates(final List<UploadScheduleOfRate> uploadSORRatesList) {
        Boolean errorInMasterData = false;
        List<AbstractEstimate> estimates;
        List<WorkOrderEstimate> woe;
        Period checkPeriod;
        String error = StringUtils.EMPTY;
        LocalDate existingStartDate;
        LocalDate existingEndDate = null;
        LocalDate checkStartDate;
        Boolean toDateUpdated;
        Boolean isLatestRate;
        for (final UploadScheduleOfRate obj : uploadSORRatesList) {
            checkPeriod = getPeriodObj(obj.getFromDate(), obj.getToDate());
            error = StringUtils.EMPTY;
            if (obj.getScheduleOfRate() != null && obj.getScheduleOfRate().getSorRatesOrderById().get(0).getValidity().getEndDate() == null)
                    obj.setIsToDateNull(true);
            isLatestRate = false;
            toDateUpdated = false;
            if (obj.getScheduleOfRate() != null)
                for (final SORRate rate : obj.getScheduleOfRate().getSorRatesOrderById()) {
                    if (!isLatestRate)
                        isLatestRate = true;
                    existingStartDate = new LocalDate(rate.getValidity().getStartDate());
                    if (rate.getValidity().getEndDate() != null)
                        existingEndDate = new LocalDate(rate.getValidity().getEndDate());
                    checkStartDate = new LocalDate(obj.getFromDate());
                    if (obj.getToDate() != null)
                        new LocalDate(obj.getToDate());

                    if (isLatestRate)
                        if (checkStartDate.compareTo(existingStartDate) <= 0) {
                            error = error + " " + messageSource.getMessage(SORRATEDATESOVERLAPERRORCODE, null, null) + ",";
                            if (obj.getErrorReason() != null)
                                error = obj.getErrorReason() + error;
                            obj.setErrorReason(error);
                            break;
                        }
                    /**
                     * If latest existing SOR Rate to date is null
                     *
                     * then
                     *
                     * Check any active estimates is exist after new SOR from date
                     *
                     * if exist
                     *
                     * throw error
                     *
                     * else
                     *
                     * update to date of existing SOR to before date of new SOR from date
                     *
                     * And check overlap issue for from dates ,to dates of existing and new SOR rates both
                     *
                     */
                    if (obj.getIsToDateNull() != null && obj.getIsToDateNull()) {

                        estimates = estimateService.getBySorIdAndEstimateDate(obj.getScheduleOfRate().getId(),
                                obj.getFromDate());
                        woe = estimateService.getBySorIdAndWorkOrderDate(obj.getScheduleOfRate().getId(),
                                obj.getFromDate());

                        if (woe != null && !woe.isEmpty())
                            error = error + " " + messageSource
                                    .getMessage("error.active.revisionestimate.exist.for.given.date.range", null, null)
                                    + ",";

                        if (estimates != null && !estimates.isEmpty())
                            error = error + " " + messageSource
                                    .getMessage("error.active.estimates.exist.for.given.date.range", null, null)
                                    + ",";

                        else if (!toDateUpdated && isLatestRate) {
                            final LocalDate rateFromDate = new LocalDate(rate.getValidity().getStartDate());
                            final LocalDate previousDay = new LocalDate(obj.getFromDate()).minusDays(1);
                            if (previousDay.compareTo(rateFromDate) > 0)
                                rate.setValidity(getPeriodObj(rate.getValidity().getStartDate(), previousDay.toDate()));
                            else {
                                error = error + " " + messageSource.getMessage(SORRATEDATESOVERLAPERRORCODE, null, null) + ",";
                                if (obj.getErrorReason() != null)
                                    error = obj.getErrorReason() + error;
                                obj.setErrorReason(error);
                                break;
                            }

                            toDateUpdated = true;
                        }

                        if (obj.getScheduleOfRate().isWithin(rate.getValidity(), obj.getFromDate())
                                || obj.getToDate() != null
                                        && obj.getScheduleOfRate().isWithin(rate.getValidity(), obj.getToDate())
                                || obj.getScheduleOfRate().isWithin(checkPeriod, rate.getValidity().getStartDate())
                                || rate.getValidity().getEndDate() != null
                                        && obj.getScheduleOfRate().isWithin(checkPeriod, rate.getValidity().getEndDate())) {
                            error = error + " " + messageSource.getMessage(SORRATEDATESOVERLAPERRORCODE, null, null) + ",";
                            if (obj.getErrorReason() != null)
                                error = obj.getErrorReason() + error;
                            obj.setErrorReason(error);
                        }

                    } else {

                        /**
                         * If latest existing SOR Rate to date is present and to date is grater then new SOR rate from date
                         *
                         * then Check any active estimates is exist after new SOR from date
                         *
                         * if exist
                         *
                         * throw error
                         *
                         * else
                         *
                         * update to date of existing SOR to before date of new SOR from date
                         *
                         * And check overlap issue for from dates ,to dates of existing and new SOR rates both
                         *
                         */

                        if (existingEndDate.compareTo(checkStartDate) > 0) {

                            estimates = estimateService.getBySorIdAndEstimateDate(obj.getScheduleOfRate().getId(),
                                    obj.getFromDate());

                            woe = estimateService.getBySorIdAndWorkOrderDate(obj.getScheduleOfRate().getId(),
                                    obj.getFromDate());

                            if (woe != null && !woe.isEmpty())
                                error = error + " " + messageSource
                                        .getMessage("error.active.revisionestimate.exist.for.given.date.range", null, null)
                                        + ",";

                            if (estimates != null && !estimates.isEmpty())
                                error = error + " " + messageSource
                                        .getMessage("error.active.estimates.exist.for.given.date.range", null, null)
                                        + ",";
                            else if (!toDateUpdated && isLatestRate) {
                                final LocalDate rateFromDate = new LocalDate(rate.getValidity().getStartDate());
                                final LocalDate previousDay = new LocalDate(obj.getFromDate()).minusDays(1);
                                if (previousDay.compareTo(rateFromDate) > 0)
                                    rate.setValidity(
                                            getPeriodObj(rate.getValidity().getStartDate(), previousDay.toDate()));
                                else {
                                    error = error + " " + messageSource.getMessage(SORRATEDATESOVERLAPERRORCODE, null, null)
                                            + ",";
                                    if (obj.getErrorReason() != null)
                                        error = obj.getErrorReason() + error;
                                    obj.setErrorReason(error);
                                    break;
                                }
                                toDateUpdated = true;
                            }
                        }

                        if (obj.getScheduleOfRate().isWithin(rate.getValidity(), obj.getFromDate())
                                || obj.getToDate() != null
                                        && obj.getScheduleOfRate().isWithin(rate.getValidity(), obj.getToDate())
                                || obj.getScheduleOfRate().isWithin(checkPeriod, rate.getValidity().getStartDate())
                                || rate.getValidity().getEndDate() != null
                                        && obj.getScheduleOfRate().isWithin(checkPeriod, rate.getValidity().getEndDate())) {
                            error = error + " " + messageSource.getMessage(SORRATEDATESOVERLAPERRORCODE, null, null) + ",";
                            if (obj.getErrorReason() != null)
                                error = obj.getErrorReason() + error;
                            obj.setErrorReason(error);
                        }
                    }
                }

        }

        if (!StringUtils.EMPTY.equalsIgnoreCase(error))
            errorInMasterData = true;

        return errorInMasterData;
    }

    public List<ScheduleOfRate> searchScheduleOfRate(final ScheduleOfRateSearchRequest scheduleOfRateSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ScheduleOfRate.class);
        if (scheduleOfRateSearchRequest != null) {
            criteria.add(Restrictions.eq("scheduleCategory.id", scheduleOfRateSearchRequest.getCategoryType()));
            if (scheduleOfRateSearchRequest.getSorDescription() != null)
                criteria.add(Restrictions.ilike("description", scheduleOfRateSearchRequest.getSorDescription(),
                        MatchMode.ANYWHERE));
            if (scheduleOfRateSearchRequest.getSorCode() != null)
                criteria.add(Restrictions.ilike("code", scheduleOfRateSearchRequest.getSorCode(), MatchMode.ANYWHERE));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public void loadModelValues(final Model model) {
        model.addAttribute("sorcategories", scheduleCategoryService.getAllScheduleCategories());
        model.addAttribute("uomlist", uomService.findAll());
    }

    public void createSORAndMarketRateDetails(final ScheduleOfRate scheduleOfRate) {
        SORRate sorRate;
        MarketRate marketRate;
        scheduleOfRate.getSorRates().clear();
        for (final SORRate sor : scheduleOfRate.getTempSorRates()) {
            sorRate = getSorRateObj();
            sorRate.setRate(sor.getRate());
            sorRate.setValidity(sor.getValidity());
            sorRate.setScheduleOfRate(scheduleOfRate);
            scheduleOfRate.getSorRates().add(sorRate);
        }
        scheduleOfRate.getMarketRates().clear();
        for (final MarketRate mr : scheduleOfRate.getTempMarketRates()) {
            marketRate = getMarketRateObj();
            marketRate.setMarketRate(mr.getMarketRate());
            marketRate.setValidity(mr.getValidity());
            marketRate.setScheduleOfRate(scheduleOfRate);
            scheduleOfRate.getMarketRates().add(marketRate);
        }
    }

    private ScheduleOfRate getScheduleOfRateObj() {
        return new ScheduleOfRate();
    }

    private MarketRate getMarketRateObj() {
        return new MarketRate();
    }

    private SORRate getSorRateObj() {
        return new SORRate();
    }

    private Money getMoneyObj(final double val) {
        return new Money(val);
    }

    private Period getPeriodObj(final Date startDate, final Date endDate) {
        return new Period(startDate, endDate);
    }
}
