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
package org.egov.works.contractoradvance.repository;

import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorAdvanceRepository extends JpaRepository<ContractorAdvanceRequisition, Long> {

    @Query("select distinct(car.advanceRequisitionNumber) from ContractorAdvanceRequisition as car where upper(car.advanceRequisitionNumber) like upper(:advanceRequisitionNumber)")
    List<String> findAdvanceRequisitionNumberToSearchCR(@Param("advanceRequisitionNumber") String advanceRequisitionNumber);

    @Query("select distinct(car.workOrderEstimate.workOrder.workOrderNumber) from ContractorAdvanceRequisition as car where upper(car.workOrderEstimate.workOrder.workOrderNumber) like upper(:workOrderNumber)")
    List<String> findWorkOrderNumberToSearchCR(@Param("workOrderNumber") String workOrderNumber);

    @Query("select distinct(car.workOrderEstimate.workOrder.contractor.name) from ContractorAdvanceRequisition as car where upper(car.workOrderEstimate.workOrder.contractor.name) like upper(:contractorName) or upper(car.workOrderEstimate.workOrder.contractor.code) like upper(:contractorName)")
    List<String> findContractorsToSearchCR(@Param("contractorName") String contractorName);

    ContractorAdvanceRequisition findByAdvanceRequisitionNumber(final String arfNumber);

    @Query("select COALESCE(sum(advanceRequisitionAmount),0) from ContractorAdvanceRequisition where (createdDate < (select createdDate from ContractorAdvanceRequisition where id = :contractorAdvanceId) or (select count(*) from ContractorAdvanceRequisition where id = :contractorAdvanceId) = 0) and status.code = :statusCode and workOrderEstimate.id = :workOrderEstimateId")
    Double getTotalAdvancePaid(@Param("contractorAdvanceId") final Long contractorAdvanceId,
            @Param("workOrderEstimateId") final Long workOrderEstimateId, @Param("statusCode") final String statusCode);

    ContractorAdvanceRequisition findByWorkOrderEstimate_IdAndStatus_codeEquals(final Long woeId, final String status);

    @Query("select car from ContractorAdvanceRequisition as car where car.workOrderEstimate.id =:workOrderEstimateId and status.code not in (:cancelledStatus, :approvedStatus, :newStatus)")
    ContractorAdvanceRequisition findByWorkOrderEstimateAndStatus(@Param("workOrderEstimateId") Long workOrderEstimateId,
            @Param("cancelledStatus") String cancelledStatus, @Param("approvedStatus") String approvedStatus,
            @Param("newStatus") String newStatus);

    @Query("select distinct(car.egAdvanceReqMises.egBillregister.billnumber) from ContractorAdvanceRequisition as car where upper(car.egAdvanceReqMises.egBillregister.billnumber) like upper(:advanceBillNumber)")
    List<String> findAdvanceBillNumber(@Param("advanceBillNumber") String advanceBillNumber);

    List<ContractorAdvanceRequisition> findByWorkOrderEstimate_IdAndStatus_CodeNot(final Long woeId, final String status);

    @Query("select distinct(createdBy) from ContractorAdvanceRequisition as car where car.status.code = :advanceRequisitionStatus")
    List<User> getAdvanceRequisitionCreatedByUsers(@Param("advanceRequisitionStatus") String advanceRequisitionStatus);

    @Query("select distinct(car.advanceRequisitionNumber) from ContractorAdvanceRequisition as car where upper(car.advanceRequisitionNumber) like upper(:advanceRequisitionNumber) and car.status.code = :advanceRequisitionStatus)")
    List<String> findAdvanceRequisitionNumberToCancelContractorAdvance(
            @Param("advanceRequisitionNumber") String advanceRequisitionNumber,
            @Param("advanceRequisitionStatus") String advanceRequisitionStatus);

    @Query("select distinct(car.workOrderEstimate.workOrder.contractor.name) from ContractorAdvanceRequisition as car where upper(car.workOrderEstimate.workOrder.contractor.name) like upper(:contractorName) or upper(car.workOrderEstimate.workOrder.contractor.code) like upper(:contractorName) and car.status.code = :advanceRequisitionStatus")
    List<String> findContractorsToCancelContractorAdvance(@Param("contractorName") String contractorName,
            @Param("advanceRequisitionStatus") String advanceRequisitionStatus);

    @Query("select distinct(car.workOrderEstimate.workOrder.workOrderNumber) from ContractorAdvanceRequisition as car where upper(car.workOrderEstimate.workOrder.workOrderNumber) like upper(:workOrderNumber) and car.status.code = :advanceRequisitionStatus)")
    List<String> findWorkOrderNumberToCancelContractorAdvance(@Param("workOrderNumber") String workOrderNumber,
            @Param("advanceRequisitionStatus") String advanceRequisitionStatus);

    List<ContractorAdvanceRequisition> findByWorkOrderEstimate_idAndCreatedDateAfterAndStatus_codeNotLike(
            @Param("workOrderEstimateId") final Long workOrderEstimateId,
            @Param("createdDate") final Date createdDate, @Param("status") final String status);

    @Query("select COALESCE(sum(car.advanceRequisitionAmount),0) from ContractorAdvanceRequisition as car, Miscbilldetail as misc where car.status.code = :statusCode and car.workOrderEstimate.id = :workOrderEstimateId and car.egAdvanceReqMises.egBillregister.egBillregistermis.voucherHeader.id = misc.billVoucherHeader.id and misc.payVoucherHeader.status = 0")
    Double getTotalAdvanceBillsPaid(@Param("workOrderEstimateId") final Long workOrderEstimateId,
            @Param("statusCode") final String statusCode);

}
