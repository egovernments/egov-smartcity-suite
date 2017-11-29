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
package org.egov.works.milestone.service;

import org.egov.works.milestone.entity.SearchRequestMilestone;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.repository.TrackMilestoneRepository;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrackMilestoneService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TrackMilestoneRepository trackMilestoneRepository;

    @Autowired
    public TrackMilestoneService(final TrackMilestoneRepository trackMilestoneRepository) {
        this.trackMilestoneRepository = trackMilestoneRepository;
    }

    public List<TrackMilestone> searchTrackMilestone(final SearchRequestMilestone searchRequestMilestone) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(TrackMilestone.class)
                .createAlias("milestone", "milestone")
                .createAlias("milestone.workOrderEstimate", "woe")
                .createAlias("woe.estimate", "estimate")
                .createAlias("estimate.lineEstimateDetails", "led")
                .createAlias("led.lineEstimate", "le")
                .createAlias("status", "status")
                .createAlias("woe.workOrder", "wo")
                .createAlias("led.projectCode", "projectCode");

        if (searchRequestMilestone != null) {
            if (searchRequestMilestone.getDepartment() != null)
                criteria.add(Restrictions.eq("le.executingDepartment.id", searchRequestMilestone.getDepartment()));
            if (searchRequestMilestone.getTrackMilestoneFromDate() != null)
                criteria.add(Restrictions.ge("createdDate", searchRequestMilestone.getTrackMilestoneFromDate()));
            if (searchRequestMilestone.getTrackMilestoneToDate() != null) {
                final DateTime dateTime = new DateTime(searchRequestMilestone.getTrackMilestoneToDate().getTime()).plusDays(1);
                criteria.add(Restrictions.le("createdDate", dateTime.toDate()));
            }
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

    public List<String> findWorkIdentificationNumbersTrackMilestone(final String code) {
        final List<String> workIdNumbers = trackMilestoneRepository
                .findWorkIdentificationNumbersTrackMilestone("%" + code + "%");
        return workIdNumbers;
    }

    @Transactional
    public TrackMilestone save(final TrackMilestone trackMilestone) {
        return trackMilestoneRepository.save(trackMilestone);
    }

    public TrackMilestone getTrackMilestoneByMilestoneId(final Long id) {
        return trackMilestoneRepository.findByMilestone_Id(id);
    }

    public TrackMilestone getTrackMilestoneTotalPercentage(final Long workOrderEstimateId) {
        return trackMilestoneRepository.findTrackMilestoneTotalPercentage(workOrderEstimateId, WorksConstants.APPROVED,
                WorksConstants.APPROVED, WorksConstants.APPROVED);
    }

    public TrackMilestone getMinimumPercentageToCreateContractorBill(final Long workOrderEstimateId) {
        return trackMilestoneRepository.findMinimunPercentageForTrackMileStone(workOrderEstimateId, WorksConstants.APPROVED,
                WorksConstants.APPROVED, WorksConstants.APPROVED);
    }

    public TrackMilestone getCompletionPercentageToCreateContractorFinalBill(final Long workOrderEstimateId) {
        return trackMilestoneRepository.findCompletionPercentageForTrackMileStone(workOrderEstimateId, WorksConstants.APPROVED,
                WorksConstants.APPROVED, WorksConstants.APPROVED);
    }

}
