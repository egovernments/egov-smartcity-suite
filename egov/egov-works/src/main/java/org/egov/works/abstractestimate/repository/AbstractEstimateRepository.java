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
package org.egov.works.abstractestimate.repository;

import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbstractEstimateRepository extends JpaRepository<AbstractEstimate, Long> {
    
    @Query("select distinct(ae.estimateNumber) from AbstractEstimate as ae where upper(ae.estimateNumber) like upper(:estimateNumber)")
    List<String> findDistinctEstimateNumberContainingIgnoreCase(@Param("estimateNumber") final String estimateNumber);

    List<AbstractEstimate> findByEstimateNumberAndEgwStatus_codeEquals(final String estimateNumber, final String statusCode);

    AbstractEstimate findByEstimateNumberAndEgwStatus_codeNotLike(final String estimateNumber, final String statusCode);

    AbstractEstimate findByLineEstimateDetails_EstimateNumberAndEgwStatus_codeEquals(final String estimateNumber,
            final String statusCode);

    AbstractEstimate findByLineEstimateDetails_IdAndEgwStatus_codeEquals(final Long id, final String statusCode);

    @Query("select distinct(ae.createdBy) from AbstractEstimate as ae")
    List<User> findAbstractEstimateCreatedByUsers();

    @Query("select distinct(ae.createdBy) from AbstractEstimate as ae where ae.executingDepartment.id in (:departmentIds)")
    List<User> findAbstractEstimateCreatedByUsers(@Param("departmentIds") final List<Long> departmentIds);

    AbstractEstimate findByEstimateTechnicalSanctionsIgnoreCase_TechnicalSanctionNumberAndEgwStatus_CodeNot(
            String technicalSanctionNumber, String statusCode);

    @Query("select distinct(ae.estimateNumber) from AbstractEstimate as ae where ae.egwStatus.code != :status and exists(select distinct(activity.abstractEstimate.estimateNumber) from Activity as activity where activity.abstractEstimate = ae) and ae.lineEstimateDetails.lineEstimate.id = :lineEstimateId")
    List<String> findAbstractEstimateNumbersToCancelLineEstimate(@Param("lineEstimateId") final Long lineEstimateId,
            @Param("status") final String status);

    @Query("select distinct(ae.estimateNumber) from AbstractEstimate as ae where upper(ae.estimateNumber) like upper(:code) and ae.egwStatus.code != :status and ae.estimateNumber not in (select distinct(woe.workOrder.estimateNumber) from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code != :status)")
    List<String> findAbstractEstimateNumbersToCancelEstimate(@Param("code") final String code,
            @Param("status") final String status);
    
    @Query("select distinct(ae.estimateNumber) from AbstractEstimate as ae where upper(ae.estimateNumber) like upper(:code) and ae.egwStatus.code = :abstractEstimateStatus and not exists (select distinct(woe.estimate) from WorkOrderEstimate as woe where ae.id = woe.workOrder.id and woe.workOrder.egwStatus.code != upper(:workOrderStatus))")
    List<String> findAbstractEstimateNumbersToSetOfflineStatus(@Param("code") final String code,
            @Param("abstractEstimateStatus") final String abstractEstimateStatus,@Param("workOrderStatus") final String workOrderStatus);

    @Query("select distinct(estimate.estimateNumber) from AbstractEstimate estimate where estimate.egwStatus.code = :abstractEstimateStatus and upper(estimate.estimateNumber) like upper(:estimateNumber) and not exists (select distinct(woe.estimate) from WorkOrderEstimate as woe where estimate.id = woe.estimate.id and upper(woe.workOrder.egwStatus.code) != upper(:workOrderStatus) and upper(estimate.egwStatus.code) = upper(:abstractEstimateStatus)) and exists (select act.abstractEstimate from Activity as act where estimate.id = act.abstractEstimate.id ) and exists (select off.id from OfflineStatus as off where off.objectId = estimate.id and off.objectType = :objectType and upper(off.egwStatus.code) = upper(:offlineStatus) )")
    List<String> findEstimateNumbersToCreateLOA(@Param("estimateNumber") final String estimateNumber,
            @Param("abstractEstimateStatus") final String abstractEstimateStatus,@Param("workOrderStatus") final String workOrderStatus,@Param("objectType") final String objectType,@Param("offlineStatus") final String offlineStatus);
    
    @Query("select distinct(estimate.projectCode.code) from AbstractEstimate estimate where estimate.egwStatus.code = :abstractEstimateStatus and upper(estimate.projectCode.code) like upper(:workIdentificationNumber) and not exists (select distinct(woe.estimate) from WorkOrderEstimate as woe where estimate.id = woe.estimate.id and upper(woe.workOrder.egwStatus.code) != upper(:workOrderStatus) and upper(estimate.egwStatus.code) = upper(:abstractEstimateStatus)) and exists (select act.abstractEstimate from Activity as act where estimate.id = act.abstractEstimate.id ) and exists (select off.id from OfflineStatus as off where off.objectId = estimate.id and off.objectType = :objectType and upper(off.egwStatus.code) = upper(:offlineStatus) )")
    List<String> findWorkIdentificationNumbersToCreateLOA(@Param("workIdentificationNumber") final String workIdentificationNumber,
            @Param("abstractEstimateStatus") final String abstractEstimateStatus,@Param("workOrderStatus") final String workOrderStatus,@Param("objectType") final String objectType,@Param("offlineStatus") final String offlineStatus);
    
    @Query("select distinct(estimate.lineEstimateDetails.lineEstimate.adminSanctionNumber) from AbstractEstimate estimate where estimate.egwStatus.code = :abstractEstimateStatus and upper(estimate.lineEstimateDetails.lineEstimate.adminSanctionNumber) like upper(:adminSanctionNumber) and not exists (select distinct(woe.estimate) from WorkOrderEstimate as woe where estimate.id = woe.estimate.id and upper(woe.workOrder.egwStatus.code) != upper(:workOrderStatus) and upper(estimate.egwStatus.code) = upper(:abstractEstimateStatus)) and exists (select act.abstractEstimate from Activity as act where estimate.id = act.abstractEstimate.id ) and exists (select off.id from OfflineStatus as off where off.objectId = estimate.id and off.objectType = :objectType and upper(off.egwStatus.code) = upper(:offlineStatus) )")
    List<String> findAdminSanctionNumbersToCreateLOA(@Param("adminSanctionNumber") final String adminSanctionNumber,
            @Param("abstractEstimateStatus") final String abstractEstimateStatus,@Param("workOrderStatus") final String workOrderStatus,@Param("objectType") final String objectType,@Param("offlineStatus") final String offlineStatus);

            
}
