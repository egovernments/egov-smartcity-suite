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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.repository.WorkOrderEstimateRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    private LineEstimateService lineEstimateService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

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
            SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {

        final List<String> estimateNumbers = lineEstimateService
                .getEstimateNumberForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
        if (estimateNumbers.isEmpty())
            estimateNumbers.add("");
        final List<String> workOrderNumbers = workOrderEstimateRepository.getCancelledWorkOrderNumbersByBillType(
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrderEstimate.class, "woe")
                .createAlias("estimate", "e").createAlias("workOrder", "wo").createAlias("workOrder.contractor", "woc")
                .createAlias("workOrder.egwStatus", "status").createAlias("estimate.projectCode", "projectCode");

        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(Restrictions.eq("wo.workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber())
                        .ignoreCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                criteria.add(Restrictions.ge("wo.workOrderDate", searchRequestLetterOfAcceptance.getFromDate()));
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                criteria.add(Restrictions.le("wo.workOrderDate", searchRequestLetterOfAcceptance.getToDate()));
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("e.estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber())
                        .ignoreCase());
            if (searchRequestLetterOfAcceptance.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.eq("projectCode.code",
                        searchRequestLetterOfAcceptance.getWorkIdentificationNumber()));
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getContractor()))
                criteria.add(Restrictions.ge("woc.name", searchRequestLetterOfAcceptance.getContractor()));
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                criteria.add(Restrictions.in("e.estimateNumber", estimateNumbers));
            if (!workOrderNumbers.isEmpty())
                criteria.add(Restrictions.not(Restrictions.in("wo.workOrderNumber", workOrderNumbers)));
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                criteria.add(Restrictions.eq("status.code", searchRequestLetterOfAcceptance.getEgwStatus()));

        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public JsonObject validateMBInDrafts(final Long workOrderId, final JsonObject jsonObject) {
        List<MBHeader> mbHeaders = mbHeaderService
                .getMBHeadersByWorkOrder(workOrderEstimateRepository.findByWorkOrder_Id(workOrderId).getWorkOrder());
        String userName = "";
        for (MBHeader MBHeader : mbHeaders) {
            if (MBHeader.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)) {
                Assignment assignment = assignmentService
                        .getPrimaryAssignmentForPositon(MBHeader.getState().getOwnerPosition().getId());
                if (assignment != null)
                    userName = assignment.getEmployee().getName();
                jsonObject
                        .addProperty("mberror",
                                messageSource.getMessage("error.mbheader.newstatus", new String[] {
                                        MBHeader.getMbRefNo(), MBHeader.getEgwStatus().getDescription(), userName },
                                        null));
            }

        }
        return jsonObject;
    }

    public JsonObject validateMBInWorkFlow(final Long workOrderId, final JsonObject jsonObject) {
        List<MBHeader> mbHeaders = mbHeaderService
                .getMBHeadersByWorkOrder(workOrderEstimateRepository.findByWorkOrder_Id(workOrderId).getWorkOrder());
        String userName = "";
        for (MBHeader MBHeader : mbHeaders) {
            if (!(MBHeader.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.NEW)
                    || MBHeader.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED))) {
                Assignment assignment = assignmentService
                        .getPrimaryAssignmentForPositon(MBHeader.getState().getOwnerPosition().getId());
                if (assignment != null)
                    userName = assignment.getEmployee().getName();
                jsonObject
                        .addProperty("mberror",
                                messageSource.getMessage("error.mbheader.workflow", new String[] {
                                        MBHeader.getMbRefNo(), MBHeader.getEgwStatus().getDescription(), userName },
                                        null));
            }
        }
        return jsonObject;
    }

    /**
     * Method is called to while saving mb and validate final bill for workorder
     * @param workOrderId
     * @return
     */
    public JsonObject checkFinalContractorBillForWorkOrder(final Long workOrderId) {
        final JsonObject jsonObject = new JsonObject();
        WorkOrder workOrder = workOrderEstimateRepository.findByWorkOrder_Id(workOrderId).getWorkOrder();
        ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContratorBillForWorkOrder(
                workOrder, ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        if (contractorBillRegister != null) {
            jsonObject.addProperty("mberror", messageSource.getMessage("error.mbheader.create", new String[] {}, null));
        }
        return jsonObject;
    }

}
