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

import org.egov.infra.utils.DateUtils;
import org.egov.works.letterofacceptance.repository.WorkOrderActivityRepository;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
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
public class WorkOrderActivityService {

    @PersistenceContext
    private EntityManager entityManager;

    private final WorkOrderActivityRepository workOrderActivityRepository;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

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
            final String itemCode, final String sorType, final String workOrderNumber) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrderActivity.class, "woa")
                .createAlias("woa.workOrderEstimate", "woe")
                .createAlias("woe.workOrder", "wo")
                .createAlias("wo.egwStatus", "status")
                .createAlias("activity", "act");
        if (workOrderEstimateId != null)
            criteria.add(Restrictions.eq("woe.id", workOrderEstimateId));
        if (workOrderNumber != null) {
            criteria.add(Restrictions.eq("wo.workOrderNumber", workOrderNumber));
            criteria.add(Restrictions.eq("status.code", WorksConstants.APPROVED));
        }

        if (sorType != null && sorType.equalsIgnoreCase("SOR"))
            criteria.add(Restrictions.isNotNull("act.schedule"));

        if (sorType != null && sorType.equalsIgnoreCase("Non Sor"))
            criteria.add(Restrictions.isNull("act.schedule"));

        return criteria.list();
    }

    public List<WorkOrderActivity> searchREActivities(final Long workOrderEstimateId, final String description,
            final String itemCode, final String nonTenderedType, final String mbDate, final String workOrderNumber) {

        WorkOrderEstimate workOrderEstimate = null;
        if (workOrderEstimateId != null)
            workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateById(workOrderEstimateId);
        else if (workOrderNumber != null)
            workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateByWorkOrderNumber(workOrderNumber);

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrderActivity.class, "woa")
                .createAlias("woa.workOrderEstimate", "woe")
                .createAlias("woe.workOrder", "workOrder")
                .createAlias("workOrder.egwStatus", "status")
                .createAlias("activity", "act")
                .createAlias("act.abstractEstimate", "estimate")
                .createAlias("woa.activity.schedule", "schedule", CriteriaSpecification.LEFT_JOIN)
                .createAlias("woa.activity.nonSor", "nonSor", CriteriaSpecification.LEFT_JOIN);

        if (workOrderEstimateId != null || workOrderNumber != null)
            criteria.add(Restrictions.le("estimate.estimateDate", DateUtils.getDate(mbDate, "dd/MM/yyyy")));
        
        if (workOrderNumber != null)
            criteria.add(Restrictions.eq("status.code", WorksConstants.APPROVED));
        
        if (mbDate != null)
            criteria.add(Restrictions.eq("workOrder.parent.id", workOrderEstimate.getWorkOrder().getId()));

        if (nonTenderedType != null && WorksConstants.NON_TENDERED.equalsIgnoreCase(nonTenderedType))
            criteria.add(Restrictions.eq("act.revisionType", RevisionType.NON_TENDERED_ITEM));
        else if (nonTenderedType != null && WorksConstants.LUMP_SUM.equalsIgnoreCase(nonTenderedType))
            criteria.add(Restrictions.eq("act.revisionType", RevisionType.LUMP_SUM_ITEM));
        else
            criteria.add(Restrictions.or(Restrictions.eq("act.revisionType", RevisionType.NON_TENDERED_ITEM),
                    Restrictions.eq("act.revisionType", RevisionType.LUMP_SUM_ITEM)));

        if (itemCode != null && !itemCode.isEmpty())
            criteria.add(Restrictions.ilike("schedule.code", itemCode,
                    MatchMode.ANYWHERE));

        if (description != null && !description.isEmpty())
            criteria.add(Restrictions.or(Restrictions.ilike("schedule.description", description,
                    MatchMode.ANYWHERE), Restrictions.ilike("nonSor.description", description,
                            MatchMode.ANYWHERE)));
        return criteria.list();

    }

    public WorkOrderActivity getWorkOrderActivityByActivity(final Long activityId) {
        return workOrderActivityRepository.findByActivity_IdAndWorkOrderEstimate_WorkOrder_EgwStatus_Code(activityId,
                WorksConstants.APPROVED);
    }

    public List<WorkOrderActivity> getChangedQuantityActivities(final RevisionAbstractEstimate revisionEstimate,
            final WorkOrderEstimate revisionWorkOrderEstimate) {
        return workOrderActivityRepository.findChangedQuantityActivitiesForEstimate(revisionEstimate.getId(),
                revisionWorkOrderEstimate.getId(), RevisionType.ADDITIONAL_QUANTITY);
    }

    public Object getQuantityForActivity(final Long activityId) {
        return workOrderActivityRepository.getActivityQuantity(activityId, WorksConstants.APPROVED);
    }

    public Object getREActivityQuantity(final Long reId, final Long parentId) {
        return workOrderActivityRepository.getREActivityQuantity(reId, parentId);
    }

    public List<WorkOrderActivity> getWorkOrderActivitiesForContractorPortal(final Long workOrderId) {
        return workOrderActivityRepository.getWorkOrderActivitiesForContractorPortal(workOrderId);
    }

}
