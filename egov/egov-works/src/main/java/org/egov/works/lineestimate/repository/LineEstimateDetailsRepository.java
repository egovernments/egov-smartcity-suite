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
package org.egov.works.lineestimate.repository;

import org.egov.infra.admin.master.entity.User;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineEstimateDetailsRepository extends JpaRepository<LineEstimateDetails, Long> {

    List<LineEstimateDetails> findByEstimateNumberContainingIgnoreCase(String name);

    LineEstimateDetails findByEstimateNumberAndLineEstimate_Status_CodeEquals(String estimateNumber, String status);

    LineEstimateDetails findByEstimateNumberAndLineEstimate_Status_CodeNot(String estimateNumber, String status);

    @Query("select distinct(led.estimateNumber) from LineEstimateDetails as led where upper(led.estimateNumber) like upper(:estimateNumber) and led.lineEstimate.status.code = :egwStatus and not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where wo.estimateNumber = led.estimateNumber and upper(wo.egwStatus.code) != :status)")
    List<String> findEstimateNumbersForLoa(@Param("estimateNumber") String estimateNumber, @Param("egwStatus") String egwStatus,
            @Param("status") String status);

    @Query("select distinct(led.estimateNumber) from LineEstimateDetails as led where not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where led.estimateNumber = wo.estimateNumber and upper(wo.egwStatus.code) != :status)")
    List<String> findEstimateNumbersToSearchLineEstimatesForLoa(@Param("status") String status);

    @Query("select distinct(led.lineEstimate.adminSanctionNumber) from LineEstimateDetails as led  where led.lineEstimate.adminSanctionNumber like :adminSanctionNumber and led.lineEstimate.status.code = :egwStatus and not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where led.estimateNumber = wo.estimateNumber and upper(wo.egwStatus.code) != :status)")
    List<String> findAdminSanctionNumbersForLoa(@Param("adminSanctionNumber") String adminSanctionNumber,
            @Param("egwStatus") String egwStatus, @Param("status") String status);

    @Query("select distinct(estimateNumber) from LineEstimateDetails as led where led.lineEstimate.executingDepartment.id = :departmentId")
    List<String> findEstimateNumbersForDepartment(@Param("departmentId") Long departmentId);

    @Query("select distinct(led.projectCode.code) from LineEstimateDetails as led  where led.projectCode.code like :code and not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where led.estimateNumber = wo.estimateNumber and upper(wo.egwStatus.code) != :status)")
    List<String> findWorkIdentificationNumbersToSearchLineEstimatesForLoa(@Param("code") String code,
            @Param("status") String status);

    @Query("select distinct(estimateNumber) from LineEstimateDetails as led where upper(led.projectCode.code) = upper(:workIdentificationNumber)")
    List<String> findEstimateNumbersForWorkIdentificationNumber(
            @Param("workIdentificationNumber") String workIdentificationNumber);

    @Query("select distinct(estimateNumber) from LineEstimateDetails as led where led.lineEstimate.spillOverFlag = :spillOverFlag")
    List<String> findEstimateNumbersForSpillOverFlag(@Param("spillOverFlag") boolean spillOverFlag);

    @Query("select distinct(led.projectCode.code) from LineEstimateDetails as led  where upper(led.projectCode.code) like upper(:code) and led.lineEstimate.status.code in (:adminSanctionstatus, :technicalSanctionstatus)")
    List<String> findWorkIdentificationNumbersToSearchWorkProgressRegister(@Param("code") String code,
            @Param("adminSanctionstatus") String adminSanctionstatus,
            @Param("technicalSanctionstatus") String technicalSanctionstatus);
    
    @Query("select distinct(led.lineEstimate.createdBy) from LineEstimateDetails as led where led.lineEstimate.executingDepartment.id = :department and led.lineEstimate.status.code = :lineEstimateStatus and not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where led.estimateNumber = wo.estimateNumber and upper(wo.egwStatus.code) = :workOrderStatus)")
    List<User> findCreatedByForCancelLineEstimateByDepartment(@Param("department") Long department,@Param("lineEstimateStatus") String lineEstimateStatus,@Param("workOrderStatus") String workOrderStatus);

    LineEstimateDetails findByProjectCode_codeAndLineEstimate_Status_CodeNotLike(String workIdentificationNumber, String status);
    
    @Query("select distinct(led.estimateNumber) from LineEstimateDetails as led where upper(led.estimateNumber) like upper(:estimateNumber) and led.lineEstimate.status.code != :lineEstimateStatus")
    List<String> findEstimateNumbersForEstimatePhotograph(@Param("estimateNumber") String estimateNumber, @Param("lineEstimateStatus") String lineEstimateStatus);
    
    @Query("select distinct(led.projectCode.code) from LineEstimateDetails as led where upper(led.projectCode.code) like upper(:workIdentificationNumber) and led.lineEstimate.status.code != :lineEstimateStatus")
    List<String> findWorkIdentificationNumberForEstimatePhotograph(@Param("workIdentificationNumber") String workIdentificationNumber, @Param("lineEstimateStatus") String lineEstimateStatus);


}
