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
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.repository.WorkOrderEstimateRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkOrderEstimateService {

    @PersistenceContext
    private EntityManager entityManager;

    private final WorkOrderEstimateRepository workOrderEstimateRepository;

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

    public List<String> findWorkOrderForMBHeader(final String workOrderNo) {
        final List<String> workOrderNumbers = workOrderEstimateRepository.findWorkOrderNumbersToCreateMB(
                "%" + workOrderNo + "%", MBHeader.MeasurementBookStatus.APPROVED.toString(),
                MBHeader.MeasurementBookStatus.WORK_COMMENCED.toString(), BillTypes.Final_Bill.toString());
        return workOrderNumbers;
    }

    public List<WorkOrderEstimate> searchWorkOrderToCreateMBHeader(
            SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrderEstimate.class, "woe")
                .createAlias("estimate", "e").createAlias("workOrder", "wo").createAlias("workOrder.contractor", "woc")
                .createAlias("workOrder.egwStatus", "status");

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
                criteria.add(Restrictions.ge("e.projectCode.code",
                        searchRequestLetterOfAcceptance.getWorkIdentificationNumber()));
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getContractor()))
                criteria.add(Restrictions.ge("woc.name", searchRequestLetterOfAcceptance.getContractor()));

        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
