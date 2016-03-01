package org.egov.works.master.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.ScheduleCategory;

public class ScheduleCategoryService extends PersistenceService<ScheduleCategory, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public ScheduleCategory getScheduleCategoryById(final Long scheduleCategoryId) {
        final ScheduleCategory scheduleCategory = entityManager.find(ScheduleCategory.class,
                scheduleCategoryId);
        return scheduleCategory;
    }

    public List<ScheduleCategory> getAllScheduleCategories() {
        final Query query = entityManager.createQuery("from ScheduleCategory sc");
        final List<ScheduleCategory> scheduleCategoryList = query.getResultList();
        return scheduleCategoryList;
    }

    public boolean checkForSOR(final Long id) {
        final Query query = entityManager.createQuery(" from ScheduleOfRate rate where sor_category_id  = "
                + "(select id from ScheduleCategory  where id = :id)");
        query.setParameter("id", Long.valueOf(id));
        final List retList = query.getResultList();
        if (retList != null && !retList.isEmpty())
            return false;
        else
            return true;
    }
    
    public boolean checkForScheduleCategory(final String code) {
        final Query query = entityManager.createQuery(" from ScheduleCategory  where code = :code");
        query.setParameter("code", code);
        final List retList = query.getResultList();
        if (retList != null && !retList.isEmpty())
            return true;
        else
            return false;
    }
}
