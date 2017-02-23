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

import org.egov.infra.admin.master.entity.User;
import org.egov.works.workorder.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterOfAcceptanceRepository extends JpaRepository<WorkOrder, Long> {

    WorkOrder findById(final Long id);

    @Query("select distinct(wo.workOrderNumber) from WorkOrder as wo where wo.parent.id is null and upper(wo.workOrderNumber) like upper(:workOrderNumber)")
    List<String> findDistinctWorkOrderNumberContainingIgnoreCase(@Param("workOrderNumber") final String workOrderNumber);

    @Query("select distinct(wo.estimateNumber) from WorkOrder as wo where wo.parent.id is null and upper(wo.estimateNumber) like upper(:estimateNumber)")
    List<String> findDistinctEstimateNumberNumberContainingIgnoreCase(@Param("estimateNumber") final String estimateNumber);

    List<WorkOrder> findByEstimateNumberAndEgwStatus_codeEquals(final String estimateNumber, final String statusCode);

    List<WorkOrder> findByWorkOrderNumberContainingIgnoreCaseAndEgwStatus_codeEqualsAndParent_idIsNull(
            final String workOrderNumber,
            final String statusCode);

    List<WorkOrder> findByEstimateNumberContainingIgnoreCaseAndEgwStatus_codeEquals(final String estimateNumber,
            final String statusCode);

    @Query("select distinct(wo.contractor.name) from WorkOrder as wo where wo.contractor.name like :name or wo.contractor.code like :name")
    List<String> findDistinctContractorByContractor_codeAndNameContainingIgnoreCase(@Param("name") final String name);

    WorkOrder findByWorkOrderNumberAndEgwStatus_codeNotLike(final String workOrderNumber, final String statusCode);

    WorkOrder findByEstimateNumberAndEgwStatus_codeNotLike(final String estimateNumber, final String statusCode);

    WorkOrder findByWorkOrderNumberAndEgwStatus_codeEquals(final String workOrderNumber, final String statusCode);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and woe.workOrder.egwStatus.code = :approvedStatus and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype) ")
    List<String> findWorkOrderNumberForContractorBill(@Param("workOrderNumber") String workOrderNumber,
            @Param("approvedStatus") String approvedStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and woe.workOrder.egwStatus.code = :approvedStatus and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype) and exists (select distinct mh.workOrderEstimate from MBHeader mh where mh.egwStatus.code =:approvedStatus )")
    List<String> findWorkOrderNumberForContractorBillWithMB(@Param("workOrderNumber") String workOrderNumber,
            @Param("approvedStatus") String approvedStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(woe.workOrder.estimateNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and upper(woe.workOrder.estimateNumber) like upper(:estimateNumber) and woe.workOrder.egwStatus.code = :approvedStatus and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype)")
    List<String> findEstimateNumberForContractorBill(@Param("estimateNumber") String estimateNumber,
            @Param("approvedStatus") String approvedStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(woe.workOrder.estimateNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and upper(woe.workOrder.estimateNumber) like upper(:estimateNumber) and woe.workOrder.egwStatus.code = :approvedStatus and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype) and exists (select distinct mh.workOrderEstimate from MBHeader mh where mh.egwStatus.code =:approvedStatus )")
    List<String> findEstimateNumberForContractorBillWithMB(@Param("estimateNumber") String estimateNumber,
            @Param("approvedStatus") String approvedStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where (upper(woe.workOrder.contractor.name) like upper(:contractorname) or upper(woe.workOrder.contractor.code) like upper(:contractorname)) and woe.workOrder.egwStatus.code = :approvedStatus and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype) ")
    List<String> findContractorForContractorBill(@Param("contractorname") String contractorname,
            @Param("approvedStatus") String approvedStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where upper(woe.workOrder.contractor.name) like upper(:contractorname) and woe.workOrder.egwStatus.code = :approvedStatus and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype) and exists (select distinct mh.workOrderEstimate from MBHeader mh where mh.egwStatus.code =:approvedStatus )")
    List<String> findContractorForContractorBillWithMB(@Param("contractorname") String contractorname,
            @Param("approvedStatus") String approvedStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(cbr.workOrderEstimate.workOrder.workOrderNumber) from ContractorBillRegister as cbr where upper(cbr.billstatus) != :status and cbr.billtype = :billtype")
    List<String> getDistinctNonCancelledWorkOrderNumbersByBillType(@Param("status") String billstatus,
            @Param("billtype") String finalBill);

    @Query("select distinct(cbr.workOrderEstimate.workOrder.workOrderNumber) from ContractorBillRegister as cbr where cbr.workOrderEstimate.workOrder.id = :workOrderId and upper(cbr.billstatus) not in (:billstatus1,:billstatus2)")
    List<String> getContractorBillInWorkflowForWorkorder(@Param("workOrderId") Long workOrderId,
            @Param("billstatus1") String billstatus1, @Param("billstatus2") String billstatus2);

    @Query("select distinct(ae.projectCode.code) from AbstractEstimate as ae  where upper(ae.projectCode.code) like upper(:code) and exists (select distinct(wo.estimateNumber) from WorkOrder as wo where ae.estimateNumber = wo.estimateNumber)")
    List<String> findWorkIdentificationNumberToCreateMilestone(@Param("code") String code);

    @Query("select sum(br.billamount) from EgBillregister as br where br.workOrderEstimate.workOrder.id = (select id from WorkOrder as wo where wo.workOrderNumber = :workOrderNumber and wo.egwStatus.code = :status) and br.billstatus != :billStatus")
    Double getGrossBillAmountOfBillsCreated(@Param("workOrderNumber") String workOrderNumber, @Param("status") String status,
            @Param("billStatus") String billstatus);

    @Query("select distinct(wo.workOrderNumber) from WorkOrder as wo where wo.egwStatus.code = :workOrderStatus and not exists (select distinct(cbr.workOrderEstimate.workOrder) from ContractorBillRegister as cbr where wo.id = cbr.workOrderEstimate.workOrder.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype)")
    List<String> findWorkOrderNumbersToModifyLoa(@Param("workOrderStatus") String workOrderStatus, @Param("status") String status,
            @Param("billtype") String billtype);

    @Query("select distinct(ae.projectCode.code) from AbstractEstimate as ae  where upper(ae.projectCode.code) like upper(:code) and exists (select distinct(wo.estimateNumber) from WorkOrder as wo where ae.estimateNumber = wo.estimateNumber and egwStatus.code = :status)")
    List<String> findWorkIdentificationNumbersToSearchLOAToCancel(@Param("code") String code,
            @Param("status") String status);

    @Query("select distinct(wo.contractor.name) from WorkOrder as wo where upper(wo.contractor.name) like upper(:code) and wo.egwStatus.code = :status")
    List<String> findContractorsToSearchLOAToCancel(@Param("code") String code, @Param("status") String status);

    @Query("select distinct(woe.workOrder.estimateNumber) from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code != :workorderstatus and not exists(select distinct(woa.workOrderEstimate.estimate.estimateNumber) from WorkOrderActivity woa where woa.workOrderEstimate = woe) and  woe.estimate.lineEstimateDetails.lineEstimate.id =:lineestimateid")
    List<String> findEstimateNumbersToCancelLineEstimate(@Param("lineestimateid") Long linEstimateId,
            @Param("workorderstatus") String workOrderStatus);

    @Query("select distinct(wo.id) from WorkOrder as wo where wo.id = (select distinct(os.objectId) from OfflineStatus as os where os.id = (select max(status.id) from OfflineStatus status where status.objectType = :objectType and os.objectId = wo.id) and os.objectId = wo.id and os.egwStatus.code = :offlineStatus and os.objectType = :objectType )")
    List<Long> findWorkOrderForLoaStatus(@Param("offlineStatus") String offlineStatus, @Param("objectType") String objectType);

    @Query("select distinct(woe.estimate.estimateNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and woe.workOrder.egwStatus.code = :workOrderStatus and upper(woe.estimate.estimateNumber) like upper(:estimateNumber) and not exists(select distinct(woa.workOrderEstimate.estimate.estimateNumber) from WorkOrderActivity woa where woa.workOrderEstimate = woe)")
    List<String> findEstimateNumbersToModifyLOA(@Param("estimateNumber") String estimateNumber,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and woe.workOrder.egwStatus.code = :workOrderStatus and upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and not exists(select distinct(woa.workOrderEstimate.workOrder.workOrderNumber) from WorkOrderActivity woa where woa.workOrderEstimate = woe)")
    List<String> findWorkOrderNumbersToModifyLOA(@Param("workOrderNumber") String workOrderNumber,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.estimate.estimateNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and woe.workOrder.egwStatus.code = :workOrderStatus and upper(woe.estimate.estimateNumber) like upper(:estimateNumber) and exists(select distinct(woa.workOrderEstimate.estimate.estimateNumber) from WorkOrderActivity woa where woa.workOrderEstimate = woe)")
    List<String> findEstimateNumbersToSetOfflineStatus(@Param("estimateNumber") String estimateNumber,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where woe.workOrder.parent.id is null and woe.workOrder.egwStatus.code = :workOrderStatus and upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and exists(select distinct(woa.workOrderEstimate.workOrder.workOrderNumber) from WorkOrderActivity woa where woa.workOrderEstimate = woe)")
    List<String> findWorkOrderNumbersToSetOfflineStatus(@Param("workOrderNumber") String workOrderNumber,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code = :workOrderStatus and (upper(woe.workOrder.contractor.name) like upper(:contractorName) or upper(woe.workOrder.contractor.code) like upper(:contractorName)) and exists (select distinct(woa.workOrderEstimate) from WorkOrderActivity woa where woa.workOrderEstimate = woe.id)")
    List<String> findContractorToSetOfflineStatus(@Param("contractorName") String contractorName,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code = :workOrderStatus and (upper(woe.workOrder.contractor.name) like upper(:contractorName) or upper(woe.workOrder.contractor.code) like upper(:contractorName)) and not exists (select distinct(woa.workOrderEstimate) from WorkOrderActivity woa where woa.workOrderEstimate = woe.id)")
    List<String> findContractorToModifyLOA(@Param("contractorName") String contractorName,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(wo.engineerIncharge) from WorkOrder as wo where wo.egwStatus.code = :workOrderStatus")
    List<User> getWorkAssignedUsers(@Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where (upper(woe.workOrder.contractor.name) like upper(:code) or upper(woe.workOrder.contractor.code) like upper(:code)) and woe.workOrder.egwStatus.code = :status")
    List<String> findContractorsToSearchLOAToCreateRE(@Param("code") String code, @Param("status") String status);

    @Query("select distinct(woe.estimate.estimateNumber) from WorkOrderEstimate as woe where upper(woe.estimate.estimateNumber) like upper(:estimateNumber) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) != :status and cbr.billtype = :billtype)")
    List<String> findDistinctEstimateNumberToModifyLOA(@Param("estimateNumber") final String estimateNumber,
            @Param("workOrderStatus") String workOrderStatus, @Param("status") String status, @Param("billtype") String billtype);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and woe.estimate.lineEstimateDetails.id in (select ep.lineEstimateDetails.id from EstimatePhotographs as ep) ")
    List<String> findworkOrderNumbersToViewEstimatePhotograph(@Param("workOrderNumber") final String workOrderNumber,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where upper(woe.workOrder.contractor.name) like upper(:contractorName) or upper(woe.workOrder.contractor.code) like upper(:contractorName) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and woe.estimate.lineEstimateDetails.id in (select ep.lineEstimateDetails.id from EstimatePhotographs as ep) ")
    List<String> findContractorsToViewEstimatePhotograph(@Param("contractorName") final String contractorName,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where (upper(woe.workOrder.contractor.name) like upper(:code) or upper(woe.workOrder.contractor.code) like upper(:code)) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null")
    List<String> findContractorsToCreateCR(@Param("code") String code, @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.estimate.estimateNumber) from WorkOrderEstimate as woe where upper(woe.estimate.estimateNumber) like upper(:estimateNumber) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and not exists (select distinct(mb.workOrder) from MBHeader as mb where woe.workOrder.id = mb.workOrder.id and mb.egwStatus.code !=:mbStatus )")
    List<String> findEstimateNumbersToCreateCR(@Param("estimateNumber") String estimateNumber,
            @Param("workOrderStatus") String workOrderStatus, @Param("mbStatus") String mbStatus);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and not exists (select distinct(mb.workOrder) from MBHeader as mb where woe.workOrder.id = mb.workOrder.id and mb.egwStatus.code !=:mbStatus ) ")
    List<String> findWorkOrderNumbersToCreateCR(@Param("workOrderNumber") String workOrderNumber,
            @Param("workOrderStatus") String workOrderStatus, @Param("mbStatus") String mbStatus);

    @Query("select distinct(woe.workOrder.contractor.name) from WorkOrderEstimate as woe where upper(woe.workOrder.contractor.name) like upper(:contractorName) or upper(woe.workOrder.contractor.code) like upper(:contractorName) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and woe.estimate.id in (select ep.abstractestimate.id from EstimatePhotographs as ep where ep.abstractestimate.parent is null) ")
    List<String> findContractorsToSearchEstimatePhotograph(@Param("contractorName") final String contractorName,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate as woe where upper(woe.workOrder.workOrderNumber) like upper(:workOrderNumber) and woe.workOrder.egwStatus.code = :workOrderStatus and woe.workOrder.parent is null and woe.estimate.id in (select ep.abstractestimate.id from EstimatePhotographs as ep where ep.abstractestimate.parent is null) ")
    List<String> findworkOrderNumbersToSearchEstimatePhotograph(@Param("workOrderNumber") final String workOrderNumber,
            @Param("workOrderStatus") String workOrderStatus);

}
