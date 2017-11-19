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
package org.egov.works.contractorbill.repository;

import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.workorder.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ContractorBillRegisterRepository extends JpaRepository<ContractorBillRegister, Long> {

    ContractorBillRegister findByBillnumber(final String billNumber);

    List<ContractorBillRegister> findByBillnumberContainingIgnoreCase(final String billNumber);

    List<ContractorBillRegister> findByWorkOrder(final WorkOrder workOrder);

    @Query("select max(billSequenceNumber) from ContractorBillRegister cbr where cbr.workOrderEstimate.estimate.lineEstimateDetails.projectCode.code = :workIdentificationNumber")
    Integer findMaxBillSequenceNumberByWorkOrder(@Param("workIdentificationNumber") String workIdentificationNumber);

    @Query("select distinct(led.projectCode.code) from LineEstimateDetails as led  where upper(led.projectCode.code) like upper(:code) and exists (select distinct(cbr.workOrder.estimateNumber) from ContractorBillRegister as cbr where led.estimateNumber = cbr.workOrder.estimateNumber)")
    List<String> findWorkIdentificationNumberToSearchContractorBill(@Param("code") String code);

    @Query("select distinct(cbr.workOrder.contractor.name) from ContractorBillRegister as cbr where upper(cbr.workOrder.contractor.name) like upper(:contractorname) or upper(cbr.workOrder.contractor.code) like upper(:contractorname)  and cbr.workOrder.egwStatus.code = :workOrderStatus ")
    List<String> findContractorForContractorBill(@Param("contractorname") String contractorname,
            @Param("workOrderStatus") String workOrderStatus);

    @Query("select sum(cbr.billamount) from ContractorBillRegister as cbr where cbr.workOrder = :workOrder and upper(cbr.billstatus) not in (:billStatus)")
    BigDecimal findSumOfBillAmountByWorkOrderAndStatus(@Param("workOrder") final WorkOrder workOrder,
            @Param("billStatus") final String billStatus);

    @Query("select sum(cbr.billamount) from ContractorBillRegister as cbr where cbr.workOrder = :workOrder and cbr.id != :id and upper(cbr.billstatus) not in (:billStatus)")
    BigDecimal findSumOfBillAmountByWorkOrderAndStatusAndNotContractorBillRegister(@Param("workOrder") final WorkOrder workOrder,
            @Param("billStatus") final String billStatus, @Param("id") Long id);

    @Query("select distinct(cbr.workOrder.workOrderNumber) from ContractorBillRegister as cbr where cbr.workOrder.workOrderNumber like :workOrderNumber and upper(cbr.billstatus) != :status")
    List<String> findWorkOrderNumbersToCancel(@Param("workOrderNumber") String workOrderNumber, @Param("status") String status);

    @Query("select distinct(led.projectCode.code) from LineEstimateDetails as led  where upper(led.projectCode.code) like upper(:code) and exists (select distinct(cbr.workOrder.estimateNumber) from ContractorBillRegister as cbr where led.estimateNumber = cbr.workOrder.estimateNumber and billstatus = :status)")
    List<String> findWorkIdentificationNumberToSearchContractorBillToCancel(@Param("code") String code,
            @Param("status") String status);

    @Query("select distinct(cbr.billnumber) from ContractorBillRegister as cbr where upper(cbr.billnumber) like upper(:billnumber) and billstatus = :status")
    List<String> findBillNumberToSearchContractorBillToCancel(@Param("billnumber") String billNumber,
            @Param("status") String status);
    
    List<ContractorBillRegister> findByWorkOrderAndBillstatusNot(final WorkOrder workOrder, final String billstatus);
    
    @Query("select COALESCE(sum(billdetail.creditamount),0) as creditAmount,COALESCE(sum(billdetail.debitamount),0) as debitAmount from EgBilldetails billdetail where billdetail.egBillregister.billstatus =:status and  billdetail.glcodeid =:glCodeId and exists (select cbr from ContractorBillRegister cbr where billdetail.egBillregister.id = cbr.id and cbr.workOrderEstimate.id =:workOrderEstimateId and cbr.billstatus = :status) and (billdetail.egBillregister.createdDate < (select createdDate from ContractorBillRegister where id = :contractorBillId) or (select count(*) from ContractorBillRegister where id = :contractorBillId) = 0 )")
    String findSumOfDebitByAccountCodeForWorkOrder(@Param("workOrderEstimateId") final Long workOrderEstimateId,@Param("glCodeId") final BigDecimal glCodeId,@Param("status") final String status,@Param("contractorBillId") final Long contractorBillId);
}
