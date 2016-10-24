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
package org.egov.works.letterofacceptance.repository;

import java.util.List;

import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderActivityRepository extends JpaRepository<WorkOrderActivity, Long> {
    WorkOrderActivity findByActivity_IdAndWorkOrderEstimate_WorkOrder_EgwStatus_Code(final Long activityId,
            final String woStatus);

    @Query("select woa from WorkOrderActivity woa where woa.workOrderEstimate.estimate.id =:revisionEstimateId and woa.workOrderEstimate.id=:revisionWorkOrderId and woa.activity.revisionType=:changedQuantityRevisionType")
    List<WorkOrderActivity> findChangedQuantityActivitiesForEstimate(@Param("revisionEstimateId") Long revisionEstimateId,
            @Param("revisionWorkOrderId") Long revisionWorkOrderEstimateId,
            @Param("changedQuantityRevisionType") RevisionType changedQuantityRevisionType);

    @Query("select sum(woa.approvedQuantity) from WorkOrderActivity woa where workOrderEstimate.workOrder.egwStatus.code =:workOrderStatus group by woa,woa.activity having activity.id =:activityId")
    Object getActivityQuantity(@Param("activityId") Long activityId, @Param("workOrderStatus") String workOrderStatus);

    @Query("select sum(woa.approvedQuantity*coalesce((CASE WHEN woa.activity.revisionType = 'REDUCED_QUANTITY' THEN -1 WHEN woa.activity.revisionType = 'ADDITIONAL_QUANTITY' THEN 1 WHEN woa.activity.revisionType = 'NON_TENDERED_ITEM' THEN 1 WHEN woa.activity.revisionType = 'LUMP_SUM_ITEM' THEN 1 END),1)) from WorkOrderActivity woa where woa.activity.abstractEstimate.egwStatus.code = 'APPROVED' and woa.activity.abstractEstimate.id !=:revisionEstimateId group by woa.activity.parent having (woa.activity.parent is not null and woa.activity.parent.id =:parentId)")
    Object getREActivityQuantity(@Param("revisionEstimateId") Long revisionEstimateId, @Param("parentId") Long parentId);

    @Query("select woa from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.id = :workOrderId or woa.workOrderEstimate.workOrder.parent.id= :workOrderId")
    List<WorkOrderActivity> getWorkOrderActivitiesForContractorPortal(@Param("workOrderId") Long workOrderId);

}
