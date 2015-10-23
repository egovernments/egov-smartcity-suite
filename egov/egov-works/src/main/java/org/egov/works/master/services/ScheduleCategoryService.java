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
	
	public ScheduleCategory getScheduleCategoryById(Long scheduleCategoryId) {
		ScheduleCategory scheduleCategory = (ScheduleCategory) entityManager.find(ScheduleCategory.class,
				scheduleCategoryId);
		return scheduleCategory;
	}

	public List<ScheduleCategory> getAllScheduleCategories() {
		final Query query = entityManager.createQuery("from ScheduleCategory sc");
		List<ScheduleCategory> scheduleCategoryList = (List<ScheduleCategory>) query.getResultList();
		return scheduleCategoryList;
	}
}
