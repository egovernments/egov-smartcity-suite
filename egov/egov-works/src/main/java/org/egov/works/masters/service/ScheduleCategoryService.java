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

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.works.masters.entity.ScheduleCategory;
import org.egov.works.masters.entity.SearchRequestScheduleCategory;
import org.egov.works.masters.repository.ScheduleCategoryRepository;
import org.egov.works.services.WorksService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleCategoryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScheduleCategoryRepository scheduleCategoryRepository;

    @Autowired
    private WorksService worksService;

    @Autowired
    private UserService userService;

    public ScheduleCategory getScheduleCategoryById(final Long scheduleCategoryId) {
        final ScheduleCategory scheduleCategory = entityManager.find(ScheduleCategory.class,
                scheduleCategoryId);
        return scheduleCategory;
    }

    // public List<ScheduleCategory> getAllScheduleCategories() {
    // final Query query = entityManager.createQuery("from ScheduleCategory sc");
    // final List<ScheduleCategory> scheduleCategoryList = query.getResultList();
    // return scheduleCategoryList;
    // }

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

    public List<ScheduleCategory> getAllScheduleCategories() {
        return scheduleCategoryRepository.findAll();
    }

    public ScheduleCategory findById(final Long id, final boolean b) {
        return scheduleCategoryRepository.findOne(id);
    }

    @Transactional
    public ScheduleCategory save(final ScheduleCategory scheduleCategory) {
        return scheduleCategoryRepository.save(scheduleCategory);
    }

    public List<String> getCategoryNames(final String categoryName) {
        final List<ScheduleCategory> scheduleCategory = scheduleCategoryRepository
                .findByDescriptionContainingIgnoreCase(categoryName);
        final List<String> results = new ArrayList<String>();
        for (final ScheduleCategory details : scheduleCategory)
            results.add(details.getDescription());
        return results;
    }

    public List<ScheduleCategory> searchScheduleCategory(final SearchRequestScheduleCategory searchRequestScheduleCategory) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ScheduleCategory.class);
        if (searchRequestScheduleCategory != null) {
            if (searchRequestScheduleCategory.getCategoryName() != null)
                criteria.add(Restrictions.eq("description", searchRequestScheduleCategory.getCategoryName()).ignoreCase());
            if (searchRequestScheduleCategory.getCategoryDescription() != null)
                criteria.add(
                        Restrictions.ilike("code", searchRequestScheduleCategory.getCategoryDescription(), MatchMode.ANYWHERE));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    // TODO: Need to remove this method after getting better alternate option
    public ScheduleCategory setPrimaryDetails(final ScheduleCategory scheduleCategory) {
        final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
        if (scheduleCategory.getId() == null) {
            scheduleCategory.setCreatedBy(user);
            scheduleCategory.setCreatedDate(new Date());
        }
        scheduleCategory.setModifiedBy(user);
        scheduleCategory.setModifiedDate(new Date());
        return scheduleCategory;
    }

    public ScheduleCategory findByCode(final String code) {
        return scheduleCategoryRepository.findByCodeIgnoreCase(code);
    }

}
