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

import org.egov.works.lineestimate.service.LineEstimateAppropriationService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.MilestoneActivity;
import org.egov.works.milestone.entity.SearchRequestMilestone;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.entity.TrackMilestoneActivity;
import org.egov.works.milestone.repository.MilestoneRepository;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MilestoneService {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TrackMilestoneService trackMilestoneService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private LineEstimateAppropriationService lineEstimateAppropriationService;

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
                .createAlias("led.projectCode", "projectCode")
                .createAlias("trackMilestone", "tm", JoinType.LEFT_OUTER_JOIN)
                .createAlias("tm.status", "trackStatus", JoinType.LEFT_OUTER_JOIN);

        if (searchRequestMilestone != null) {
            if (searchRequestMilestone.getDepartment() != null)
                criteria.add(Restrictions.eq("le.executingDepartment.id", searchRequestMilestone.getDepartment()));
            if (searchRequestMilestone.getMilestoneFromDate() != null)
                criteria.add(Restrictions.ge("createdDate", searchRequestMilestone.getMilestoneFromDate()));
            if (searchRequestMilestone.getMilestoneToDate() != null) {
                final DateTime dateTime = new DateTime(searchRequestMilestone.getMilestoneToDate().getTime()).plusDays(1);
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

        criteria.add(Restrictions.or(
                Restrictions.isEmpty("trackMilestone"),
                Restrictions.or(Restrictions.eq("tm.projectCompleted", false),
                        Restrictions.eq("trackStatus.code", WorksConstants.CANCELLED_STATUS))));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    public List<Milestone> searchMilestoneForView(final SearchRequestMilestone searchRequestMilestone) {
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
            if (searchRequestMilestone.getMilestoneToDate() != null) {
                final DateTime dateTime = new DateTime(searchRequestMilestone.getMilestoneToDate().getTime()).plusDays(1);
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

    public Milestone getMilestoneById(final Long id) {
        return milestoneRepository.findOne(id);
    }

    @Transactional
    public Milestone save(final Milestone milestone) {
        final Milestone newMilestone = milestoneRepository.save(milestone);
        return newMilestone;
    }

    @Transactional
    public Milestone create(final Milestone milestone) throws IOException {
        if (milestone.getState() == null)
            milestone.setStatus(lineEstimateAppropriationService.getStatusByModuleAndCode(WorksConstants.MILESTONE_MODULE_KEY,
                    Milestone.MilestoneStatus.APPROVED.toString()));
        for (final MilestoneActivity activity : milestone.getActivities())
            activity.setMilestone(milestone);
        final Milestone newMilestone = milestoneRepository.save(milestone);
        return newMilestone;
    }

    @Transactional
    public Milestone update(final Milestone milestone) {
        for (final TrackMilestone tm : milestone.getTrackMilestone()) {
            tm.setMilestone(milestone);
            tm.setStatus(lineEstimateAppropriationService.getStatusByModuleAndCode(WorksConstants.TRACK_MILESTONE_MODULE_KEY,
                    Milestone.MilestoneStatus.APPROVED.toString()));
            if (tm.getApprovedDate() == null)
                tm.setApprovedDate(new Date());
            Integer count = 0;
            double totalPercentage = 0;
            for (final TrackMilestoneActivity tma : tm.getActivities()) {
                tma.setMilestoneActivity(milestone.getActivities().get(count));
                tma.setTrackMilestone(tm);
                totalPercentage += milestone.getActivities().get(count).getPercentage() / 100 * tma.getCompletedPercentage();
                count++;
            }
            tm.setTotalPercentage(BigDecimal.valueOf(totalPercentage));
            if (totalPercentage == 100)
                tm.setProjectCompleted(true);
            else
                tm.setProjectCompleted(false);
            trackMilestoneService.save(tm);
        }
        return milestoneRepository.save(milestone);
    }

    public List<String> findLoaNumbersToCancelMilestone(final String code) {
        final List<String> loaNumbers = milestoneRepository
                .findLoaNumbersToCancelMilestone("%" + code + "%",
                        WorksConstants.APPROVED.toString());
        return loaNumbers;
    }

    @Transactional
    public Milestone cancel(final Milestone milestone) {
        milestone.setStatus(lineEstimateAppropriationService.getStatusByModuleAndCode(WorksConstants.MILESTONE_MODULE_KEY,
                WorksConstants.CANCELLED_STATUS));
        for (final TrackMilestone tm : milestone.getTrackMilestone())
            tm.setStatus(lineEstimateAppropriationService.getStatusByModuleAndCode(WorksConstants.TRACK_MILESTONE_MODULE_KEY,
                    WorksConstants.CANCELLED_STATUS));
        return milestoneRepository.save(milestone);
    }

    public List<Milestone> searchMilestonesToCancel(final SearchRequestMilestone searchRequestMilestone) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Milestone.class)
                .createAlias("workOrderEstimate", "woe")
                .createAlias("woe.estimate", "estimate")
                .createAlias("estimate.lineEstimateDetails", "led")
                .createAlias("status", "status")
                .createAlias("woe.workOrder", "wo")
                .createAlias("wo.contractor", "contractor")
                .createAlias("led.projectCode", "projectCode");

        if (searchRequestMilestone != null) {
            if (searchRequestMilestone.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.ilike("projectCode.code", searchRequestMilestone.getWorkIdentificationNumber(),
                        MatchMode.ANYWHERE));
            if (searchRequestMilestone.getStatus() != null)
                criteria.add(Restrictions.eq("status.code", searchRequestMilestone.getStatus()));
            if (searchRequestMilestone.getWorkOrderNumber() != null)
                criteria.add(Restrictions.eq("wo.workOrderNumber", searchRequestMilestone.getWorkOrderNumber()));
            if (searchRequestMilestone.getContractor() != null)
                criteria.add(Restrictions.eq("contractor.name", searchRequestMilestone.getContractor()).ignoreCase());
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findContractorsToCancelMilestone(final String code) {
        final List<String> loaNumbers = milestoneRepository
                .findContractorsToSearchMilestoneToCancel("%" + code + "%",
                        WorksConstants.APPROVED.toString());
        return loaNumbers;
    }

    public boolean checkMilestoneCreated(final Long workOrderId) {
        final Long milestoneId = milestoneRepository.findWorkOrderToCreateMilestone(workOrderId, WorksConstants.CANCELLED_STATUS,
                WorksConstants.CANCELLED_STATUS);
        boolean flag = false;
        if (milestoneId != null)
            flag = true;
        return flag;
    }
}
