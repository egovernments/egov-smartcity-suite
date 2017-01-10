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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.masters.entity.ScheduleCategory;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.egov.works.masters.entity.SearchRequestScheduleCategory;
import org.egov.works.masters.repository.ScheduleCategoryRepository;
import org.egov.works.masters.repository.ScheduleOfRateRepository;
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
    private ScheduleOfRateRepository scheduleOfRateRepository;

    public ScheduleCategory getScheduleCategoryById(final Long scheduleCategoryId) {
        return scheduleCategoryRepository.findOne(scheduleCategoryId);
    }

    public boolean checkForSOR(final Long categoryId) {
        final List<ScheduleOfRate> rates = scheduleOfRateRepository.findByScheduleCategory_id(categoryId);
        if (rates != null && !rates.isEmpty())
            return false;
        else
            return true;
    }

    public List<ScheduleCategory> getAllScheduleCategories() {
        return scheduleCategoryRepository.findAll();
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

    public ScheduleCategory findByCode(final String code) {
        return scheduleCategoryRepository.findByCodeIgnoreCase(code);
    }

}
