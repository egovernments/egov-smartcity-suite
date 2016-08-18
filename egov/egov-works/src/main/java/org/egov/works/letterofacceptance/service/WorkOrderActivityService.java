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
package org.egov.works.letterofacceptance.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.letterofacceptance.repository.WorkOrderActivityRepository;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkOrderActivityService {

    @PersistenceContext
    private EntityManager entityManager;

    private final WorkOrderActivityRepository workOrderActivityRepository;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public WorkOrderActivityService(final WorkOrderActivityRepository workOrderActivityRepository) {
        this.workOrderActivityRepository = workOrderActivityRepository;
    }

    public WorkOrderActivity getWorkOrderActivityById(final Long id) {
        return workOrderActivityRepository.findOne(id);
    }

    @Transactional
    public WorkOrderActivity create(final WorkOrderActivity workOrderActivity) {

        return workOrderActivityRepository.save(workOrderActivity);
    }

    public List<WorkOrderActivity> searchActivities(final Long workOrderEstimateId, final String description,
            final String itemCode, final String sorType) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrderActivity.class, "woa")
                .createAlias("woa.workOrderEstimate", "woe")
                .createAlias("activity", "act");
        if (workOrderEstimateId != null)
            criteria.add(Restrictions.eq("woe.id", workOrderEstimateId));

        if (sorType != null && sorType.equalsIgnoreCase("SOR"))
            criteria.add(Restrictions.isNotNull("act.schedule"));

        if (sorType != null && sorType.equalsIgnoreCase("Non Sor"))
            criteria.add(Restrictions.isNull("act.schedule"));

        return criteria.list();
    }
    
    public List<WorkOrderActivity> getAllWOrkOrderActivitiesWithMB(final Long workOrderEstimateId) {
        return workOrderActivityRepository.getAllWOrkOrderActivitiesWithMB(workOrderEstimateId);
    }
    
    
}
