/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.milestone.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.SearchRequestMilestone;
import org.egov.works.milestone.repository.MilestoneRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MilestoneService {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<Milestone> getMilestoneByWorkOrderEstimateId(final Long id) {
        return milestoneRepository.findByWorkOrderEstimate_Id(id);
    }

    public List<Milestone> searchMilestone(final SearchRequestMilestone searchRequestMilestone) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Milestone.class)
                .createAlias("workOrderEstimate", "woe")
                .createAlias("woe.estimate", "estimate")
                .createAlias("estimate.lineEstimateDetails", "led")
                .createAlias("led.lineEstimate", "le")
                .createAlias("status", "status")
                .createAlias("woe.workOrder", "wo")
                .createAlias("led.projectCode", "projectCode");

        if (searchRequestMilestone != null) {
            if (searchRequestMilestone.getDepartment() != null)
                criteria.add(Restrictions.eq("le.executingDepartment.id", searchRequestMilestone.getDepartment()));
            if (searchRequestMilestone.getMilestoneFromDate() != null)
                criteria.add(Restrictions.ge("createdDate", searchRequestMilestone.getMilestoneFromDate()));
            if (searchRequestMilestone.getMilestoneToDate() != null)
                criteria.add(Restrictions.le("createdDate", searchRequestMilestone.getMilestoneToDate()));
            if (searchRequestMilestone.getStatus() != null)
                criteria.add(Restrictions.eq("status.code", searchRequestMilestone.getStatus()));
            if (searchRequestMilestone.getSubTypeOfWork() != null)
                criteria.add(Restrictions.eq("le.subTypeOfWork.id", searchRequestMilestone.getSubTypeOfWork()));
            if (searchRequestMilestone.getTypeOfWork() != null)
                criteria.add(Restrictions.eq("le.typeOfWork.id", searchRequestMilestone.getTypeOfWork()));
            if (searchRequestMilestone.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.eq("projectCode.code", searchRequestMilestone.getWorkIdentificationNumber())
                        .ignoreCase());
            if (searchRequestMilestone.getWorkOrderNumber() != null)
                criteria.add(Restrictions.ilike("wo.workOrderNumber", searchRequestMilestone.getWorkOrderNumber(),
                        MatchMode.ANYWHERE));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
