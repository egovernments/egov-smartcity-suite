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
package org.egov.works.milestone.repository;

import org.egov.works.milestone.entity.TrackMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackMilestoneRepository extends JpaRepository<TrackMilestone, Long> {

    @Query("select distinct(led.projectCode.code) from LineEstimateDetails as led  where upper(led.projectCode.code) like upper(:code) and exists (select distinct(tm.milestone.workOrderEstimate.workOrder.estimateNumber) from TrackMilestone as tm where led.estimateNumber = tm.milestone.workOrderEstimate.workOrder.estimateNumber)")
    List<String> findWorkIdentificationNumbersTrackMilestone(@Param("code") String code);
    
    TrackMilestone findByMilestone_Id(final Long id);
    
    @Query("select distinct(tms) FROM WorkOrderEstimate as woe,Milestone as ms ,TrackMilestone as tms WHERE ms.workOrderEstimate.id =:workOrderEstimateId and woe.workOrder.egwStatus.code=:workOrderStatus and tms.status.code =:trackMilestoneStatus and ms.status.code =:milestoneStatus")
    TrackMilestone findTrackMilestoneTotalPercentage(@Param("workOrderEstimateId") Long workOrderEstimateId,@Param("workOrderStatus") String workOrderStatus,@Param("trackMilestoneStatus") String trackMilestoneStatus,@Param("milestoneStatus") String milestoneStatus);
    
    @Query("select distinct(tms) from TrackMilestone tms where tms.milestone.workOrderEstimate.id =:workOrderEstimateId and tms.milestone.workOrderEstimate.workOrder.egwStatus.code=:workOrderStatus and tms.status.code =:trackMilestoneStatus and tms.status.code =:milestoneStatus and tms.totalPercentage >= 1 ")
    TrackMilestone findMinimunPercentageForTrackMileStone(@Param("workOrderEstimateId") Long workOrderEstimateId,@Param("workOrderStatus") String workOrderStatus,@Param("trackMilestoneStatus") String trackMilestoneStatus,@Param("milestoneStatus") String milestoneStatus);
    
    @Query("select distinct(tms) from TrackMilestone tms where tms.milestone.workOrderEstimate.id =:workOrderEstimateId and tms.milestone.workOrderEstimate.workOrder.egwStatus.code=:workOrderStatus and tms.status.code =:trackMilestoneStatus and tms.status.code =:milestoneStatus and tms.totalPercentage = 100 ")
    TrackMilestone findCompletionPercentageForTrackMileStone(@Param("workOrderEstimateId") Long workOrderEstimateId,@Param("workOrderStatus") String workOrderStatus,@Param("trackMilestoneStatus") String trackMilestoneStatus,@Param("milestoneStatus") String milestoneStatus);

}
