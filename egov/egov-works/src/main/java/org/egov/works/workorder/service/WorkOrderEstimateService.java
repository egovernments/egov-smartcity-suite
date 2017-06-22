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
package org.egov.works.workorder.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptanceForRE;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.repository.WorkOrderEstimateRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;

@Service
@Transactional(readOnly = true)
public class WorkOrderEstimateService {

    @PersistenceContext
    private EntityManager entityManager;

    private final WorkOrderEstimateRepository workOrderEstimateRepository;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public WorkOrderEstimateService(final WorkOrderEstimateRepository workOrderEstimateRepository) {
        this.workOrderEstimateRepository = workOrderEstimateRepository;
    }

    public WorkOrderEstimate getEstimateByWorkOrderAndEstimateAndStatus(final Long workOrderId, final Long estimateId) {
        return workOrderEstimateRepository.findByWorkOrder_IdAndEstimate_IdAndWorkOrder_EgwStatus_Code(workOrderId,
                estimateId, WorksConstants.APPROVED);
    }

    public WorkOrderEstimate getWorkOrderEstimateById(final Long id) {
        return workOrderEstimateRepository.findOne(id);
    }
    
    public WorkOrderEstimate getWorkOrderEstimateByWorkOrderNumber(final String workOrderNumber) {
        return workOrderEstimateRepository.findByWorkOrder_WorkOrderNumber(workOrderNumber);
    }

    public WorkOrderEstimate getWorkOrderEstimateByAbstractEstimateId(final Long estimateId) {
        return workOrderEstimateRepository.findByEstimate_IdAndWorkOrder_EgwStatus_Code(estimateId,
                WorksConstants.APPROVED);
    }

    public WorkOrderEstimate getWorkOrderEstimateByWorkOrderId(final Long workOrderId) {
        return workOrderEstimateRepository.findByWorkOrder_Id(workOrderId);
    }

    public List<String> findWorkOrderForMBHeader(final String workOrderNo) {
        final List<String> workOrderNumbers = workOrderEstimateRepository.findWorkOrderNumbersToCreateMB(
                "%" + workOrderNo + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        return workOrderNumbers;
    }

    public List<WorkOrderEstimate> searchWorkOrderToCreateMBHeader(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {

        List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
        final StringBuilder queryStr = new StringBuilder(500);
        /*
         * This block will get LOA's where BOQ is created and final bill is not created
         */
        getWorkOrdersWhereBoqIsCreated(searchRequestLetterOfAcceptance, queryStr);
        final Query query = setParameterForLetterOfAcceptanceToCreateMB(searchRequestLetterOfAcceptance, queryStr);
        query.setParameter("offlineStatus", WorkOrder.OfflineStatuses.WORK_COMMENCED.toString().toLowerCase());
        query.setParameter("objectType", WorksConstants.WORKORDER);
        workOrderEstimateList = query.getResultList();
        return workOrderEstimateList;
    }

    private Query setParameterForLetterOfAcceptanceToCreateMB(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("billStatus", ContractorBillRegister.BillStatus.CANCELLED.toString());
        qry.setParameter("billType", BillTypes.Final_Bill.toString());
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                qry.setParameter("executingDepartment", searchRequestLetterOfAcceptance.getDepartmentName());
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                qry.setParameter("fromWorkOrderDate", searchRequestLetterOfAcceptance.getFromDate());
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                qry.setParameter("toWorkOrderDate", searchRequestLetterOfAcceptance.getToDate());
            if (searchRequestLetterOfAcceptance.getContractor() != null)
                qry.setParameter("contractorName", searchRequestLetterOfAcceptance.getContractor().toUpperCase());
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                qry.setParameter("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                qry.setParameter("woStatus", WorksConstants.APPROVED);
            if (searchRequestLetterOfAcceptance.getWorkIdentificationNumber() != null)
                qry.setParameter("workIdentificationNo", searchRequestLetterOfAcceptance.getWorkIdentificationNumber());

        }
        return qry;
    }

    private void getWorkOrdersWhereBoqIsCreated(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        queryStr.append(
                " select distinct woe from WorkOrderEstimate woe where woe.workOrder.egwStatus.code = :woStatus and  not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) !=:billStatus and cbr.billtype =:billType and cbr.workOrderEstimate.id is not null) ");
        queryStr.append(
                " and exists (select workOrderEstimate  from WorkOrderActivity where woe.id =workOrderEstimate.id ) ");
        queryStr.append(
                " and exists ( select distinct(woe1) from WorkOrderEstimate as woe1 where woe1 = woe and woe1.workOrder.id = (select distinct(os.objectId) from OfflineStatus as os where os.id = (select max(status.id) from OfflineStatus status where status.objectType = :objectType and status.objectId = woe1.workOrder.id) and os.objectId = woe1.workOrder.id and lower(os.egwStatus.code) = :offlineStatus and os.objectType = :objectType ) ) ");
        if (searchRequestLetterOfAcceptance != null)
            getSubQuery(searchRequestLetterOfAcceptance, queryStr);

    }

    private void getSubQuery(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
            queryStr.append(" and woe.estimate.executingDepartment.id =:executingDepartment ");
        if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
            queryStr.append(" and woe.workOrder.workOrderNumber =:workOrderNumber ");
        if (searchRequestLetterOfAcceptance.getFromDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate >=:fromWorkOrderDate ");
        if (searchRequestLetterOfAcceptance.getToDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate <=:toWorkOrderDate ");
        if (searchRequestLetterOfAcceptance.getContractor() != null)
            queryStr.append(" and upper(woe.workOrder.contractor.name) =:contractorName ");
        if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
            queryStr.append(" and upper(woe.estimate.estimateNumber) =:estimateNumber ");
        if (searchRequestLetterOfAcceptance.getWorkIdentificationNumber() != null)
            queryStr.append(" and upper(woe.estimate.projectCode.code) =:workIdentificationNo ");

    }

    /**
     * Method is called to while saving mb and validate final bill for workorder
     *
     * @param workOrderId
     * @return
     */
    public void getContratorBillForWorkOrderEstimateAndBillType(final Long workOrderEstimateId, final JsonObject jsonObject) {
        final WorkOrderEstimate workOrderEstimate = getWorkOrderEstimateById(workOrderEstimateId);
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService
                .getContratorBillForWorkOrder(workOrderEstimate, ContractorBillRegister.BillStatus.CANCELLED.toString(),
                        BillTypes.Final_Bill.toString());
        if (contractorBillRegister != null)
            jsonObject.addProperty("mberror", messageSource.getMessage("error.mbheader.create", new String[] {}, null));
    }

    public List<String> getApprovedAndWorkCommencedWorkOrderNumbers(final String workOrderNo) {
        return workOrderEstimateRepository.findWordOrderByStatus("%" + workOrderNo + "%", WorksConstants.APPROVED,
                OfflineStatuses.WORK_COMMENCED.toString(), WorksConstants.WORKORDER);
    }

    public List<String> getEstimateNumbersByApprovedAndWorkCommencedWorkOrders(final String EstimateNumber) {
        return workOrderEstimateRepository.findEstimatesByWorkOrderStatus("%" + EstimateNumber + "%",
                WorksConstants.APPROVED, OfflineStatuses.WORK_COMMENCED.toString(), WorksConstants.WORKORDER);
    }

    public List<String> getEstimateNumbersForApprovedLoa(final String estimateNumber) {
        final List<WorkOrderEstimate> workOrderEstimates = workOrderEstimateRepository
                .findByEstimate_EstimateNumberContainingIgnoreCaseAndWorkOrder_EgwStatus_codeEquals(estimateNumber,
                        WorksConstants.APPROVED);
        final List<String> results = new ArrayList<String>();
        for (final WorkOrderEstimate details : workOrderEstimates)
            results.add(details.getEstimate().getEstimateNumber());
        return results;
    }

    public List<Contractor> getContractorsByWorkOrderStatus(final String contractorNameCode) {
        return workOrderEstimateRepository.findContractorByWorkOrderStatus("%" + contractorNameCode + "%",
                WorksConstants.APPROVED, OfflineStatuses.WORK_COMMENCED.toString(), WorksConstants.WORKORDER);
    }

    public List<WorkOrderEstimate> getWorkOrderEstimatesToCancelEstimates(final String estimateNumber) {
        return workOrderEstimateRepository.findWorkOrderEstimatesToCancelAbstractEstimate(estimateNumber.toUpperCase(),
                WorksConstants.CANCELLED_STATUS);
    }

    public WorkOrderEstimate getWorkOrderByEstimateNumber(final String estimateNumber) {
        return workOrderEstimateRepository
                .findByEstimate_EstimateNumberAndWorkOrder_EgwStatus_codeNotLike(estimateNumber,
                        WorksConstants.CANCELLED_STATUS);
    }

    public List<String> findWorkOrderForRE(final String workOrderNo) {
        final List<String> workOrderNumbers = workOrderEstimateRepository.findWorkOrderNumbersToCreateRE(
                "%" + workOrderNo + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        return workOrderNumbers;
    }

    public List<WorkOrderEstimate> searchWorkOrderToCreateRE(
            final SearchRequestLetterOfAcceptanceForRE searchRequestLetterOfAcceptanceForRE) {

        List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                " select distinct woe from WorkOrderEstimate woe where woe.workOrder.egwStatus.code = :woStatus and  not exists (select distinct(cbr.workOrderEstimate) from ContractorBillRegister as cbr where woe.id = cbr.workOrderEstimate.id and upper(cbr.billstatus) !=:billStatus and cbr.billtype =:billType and cbr.workOrderEstimate.id is not null) ");
        queryStr.append(
                " and exists (select workOrderEstimate  from WorkOrderActivity where woe.id =workOrderEstimate.id ) ");
        if (searchRequestLetterOfAcceptanceForRE != null)
            getSubQueryForRE(searchRequestLetterOfAcceptanceForRE, queryStr);
        final Query query = setParameterForLetterOfAcceptanceToCreateRE(searchRequestLetterOfAcceptanceForRE, queryStr);
        workOrderEstimateList = query.getResultList();
        return workOrderEstimateList;
    }

    private Query setParameterForLetterOfAcceptanceToCreateRE(
            final SearchRequestLetterOfAcceptanceForRE searchRequestLetterOfAcceptanceForRE, final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("billStatus", ContractorBillRegister.BillStatus.CANCELLED.toString());
        qry.setParameter("billType", BillTypes.Final_Bill.toString());
        if (searchRequestLetterOfAcceptanceForRE != null) {
            if (searchRequestLetterOfAcceptanceForRE.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptanceForRE.getWorkOrderNumber());
            if (searchRequestLetterOfAcceptanceForRE.getFromDate() != null)
                qry.setParameter("fromWorkOrderDate", searchRequestLetterOfAcceptanceForRE.getFromDate());
            if (searchRequestLetterOfAcceptanceForRE.getToDate() != null)
                qry.setParameter("toWorkOrderDate", searchRequestLetterOfAcceptanceForRE.getToDate());
            if (searchRequestLetterOfAcceptanceForRE.getContractor() != null)
                qry.setParameter("contractorName", searchRequestLetterOfAcceptanceForRE.getContractor().toUpperCase());
            if (searchRequestLetterOfAcceptanceForRE.getEstimateNumber() != null)
                qry.setParameter("estimateNumber",
                        "%" + searchRequestLetterOfAcceptanceForRE.getEstimateNumber().toUpperCase() + "%");
            if (searchRequestLetterOfAcceptanceForRE.getEgwStatus() != null)
                qry.setParameter("woStatus", WorksConstants.APPROVED);
            if (searchRequestLetterOfAcceptanceForRE.getWorkAssignedTo() != null)
                qry.setParameter("workAssignedTo", searchRequestLetterOfAcceptanceForRE.getWorkAssignedTo());
        }
        return qry;
    }

    private void getSubQueryForRE(final SearchRequestLetterOfAcceptanceForRE searchRequestLetterOfAcceptanceForRE,
            final StringBuilder queryStr) {
        queryStr.append(" and woe.estimate.parent.id is null ");
        if (searchRequestLetterOfAcceptanceForRE.getWorkOrderNumber() != null)
            queryStr.append(" and woe.workOrder.workOrderNumber =:workOrderNumber ");
        if (searchRequestLetterOfAcceptanceForRE.getFromDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate >=:fromWorkOrderDate ");
        if (searchRequestLetterOfAcceptanceForRE.getToDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate <=:toWorkOrderDate ");
        if (searchRequestLetterOfAcceptanceForRE.getContractor() != null)
            queryStr.append(
                    " and upper(woe.workOrder.contractor.name) =:contractorName ");
        if (searchRequestLetterOfAcceptanceForRE.getEstimateNumber() != null)
            queryStr.append(" and upper(woe.estimate.estimateNumber) like :estimateNumber ");
        if (searchRequestLetterOfAcceptanceForRE.getWorkAssignedTo() != null)
            queryStr.append(" and woe.workOrder.engineerIncharge.id =:workAssignedTo ");

    }

    public List<Contractor> findContractorsByWorkOrderStatus(final String code) {
        return workOrderEstimateRepository.findContractorsByWorkOrderStatus("%" + code + "%", WorksConstants.APPROVED);
    }

    public WorkOrderEstimate findWorkOrderByRevisionEstimateNumber(final String estimateNumber) {
        return workOrderEstimateRepository.findByEstimate_EstimateNumberContainingIgnoreCaseAndWorkOrder_EgwStatus_codeEquals(
                estimateNumber, WorksConstants.APPROVED).get(0);
    }

    public WorkOrderEstimate getWorkOrderEstimateByEstimateNumber(final String estimateNumber) {
        return workOrderEstimateRepository.findWorkOrderEstimateByEstimateNumber(estimateNumber, WorksConstants.APPROVED);
    }

    public List<WorkOrderEstimate> searchWorkOrderToCreateContractorAdvance(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {

        List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                " select distinct woe from WorkOrderEstimate woe where woe.workOrder.egwStatus.code = :woStatus  and woe.workOrder.parent is null and  not exists (select distinct(mb.workOrderEstimate) from MBHeader as mb where woe.id = mb.workOrderEstimate.id and mb.egwStatus.code !=:mbStatus) ");
        if (searchRequestLetterOfAcceptance != null)
            getSubQueryForCR(searchRequestLetterOfAcceptance, queryStr);
        final Query query = setParameterForLetterOfAcceptanceToCreateCR(searchRequestLetterOfAcceptance, queryStr);
        workOrderEstimateList = query.getResultList();
        return workOrderEstimateList;
    }

    private Query setParameterForLetterOfAcceptanceToCreateCR(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("woStatus", WorksConstants.APPROVED);
        qry.setParameter("mbStatus", WorksConstants.CANCELLED_STATUS);
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                qry.setParameter("workOrderFromDate", searchRequestLetterOfAcceptance.getFromDate());
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                qry.setParameter("workOrderToDate", searchRequestLetterOfAcceptance.getToDate());
            if (searchRequestLetterOfAcceptance.getContractor() != null)
                qry.setParameter("contractorName", searchRequestLetterOfAcceptance.getContractor().toUpperCase());
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                qry.setParameter("estimateNumber",
                        searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getWorkAssignedTo() != null)
                qry.setParameter("workAssignedTo", searchRequestLetterOfAcceptance.getWorkAssignedTo());
        }
        return qry;
    }

    private void getSubQueryForCR(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
            queryStr.append(" and upper(woe.workOrder.workOrderNumber) =:workOrderNumber ");
        if (searchRequestLetterOfAcceptance.getFromDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate >=:workOrderFromDate ");
        if (searchRequestLetterOfAcceptance.getToDate() != null)
            queryStr.append(" and woe.workOrder.workOrderDate <=:workOrderToDate ");
        if (searchRequestLetterOfAcceptance.getContractor() != null)
            queryStr.append(
                    " and upper(woe.workOrder.contractor.name) =:contractorName ");
        if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
            queryStr.append(" and upper(woe.estimate.estimateNumber) =:estimateNumber ");
        if (searchRequestLetterOfAcceptance.getWorkAssignedTo() != null)
            queryStr.append(" and woe.workOrder.engineerIncharge.id =:workAssignedTo ");

    }

}
