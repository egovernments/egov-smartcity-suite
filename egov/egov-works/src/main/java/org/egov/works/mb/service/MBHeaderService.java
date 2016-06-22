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
package org.egov.works.mb.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.User;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.SearchRequestMBHeader;
import org.egov.works.mb.repository.MBHeaderRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
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
public class MBHeaderService {

    @PersistenceContext
    private EntityManager entityManager;

    private final MBHeaderRepository mbHeaderRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private WorksUtils worksUtils;
    
    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;
    
    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public MBHeaderService(final MBHeaderRepository mbHeaderRepository) {
        this.mbHeaderRepository = mbHeaderRepository;
    }

    public MBHeader getMBHeaderById(final Long id) {
        return mbHeaderRepository.findOne(id);
    }

    public List<MBHeader> getMBHeadersByWorkOrderId(final Long workOrderId) {
        return mbHeaderRepository.findByWorkOrder_id(workOrderId);
    }

    public List<MBHeader> getMBHeadersByWorkOrder(final WorkOrder workOrder) {
        return mbHeaderRepository.findByWorkOrder(workOrder);
    }

    public List<MBHeader> getMBHeadersByWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        return mbHeaderRepository.findByWorkOrderEstimate(workOrderEstimate);
    }

    public List<MBHeader> getApprovedMBHeadersByWorkOrder(final WorkOrder workOrder) {
        return mbHeaderRepository.findByWorkOrderAndEgwStatus_codeEquals(workOrder,
                MBHeader.MeasurementBookStatus.APPROVED.toString());
    }

    public List<MBHeader> getApprovedMBHeadersByContractorBill(final ContractorBillRegister contractorBillRegister) {
        return mbHeaderRepository.findByEgBillregisterAndEgwStatus_codeEquals(contractorBillRegister,
                MBHeader.MeasurementBookStatus.APPROVED.toString());
    }

    public List<MBHeader> getMBHeadersByContractorBill(final ContractorBillRegister contractorBillRegister) {
        return mbHeaderRepository.findByEgBillregister(contractorBillRegister);
    }

    @Transactional
    public MBHeader create(final MBHeader mbHeader) {
        final MBHeader savedMBHeader = mbHeaderRepository.save(mbHeader);
        return savedMBHeader;
    }

    @Transactional
    public MBHeader update(final MBHeader mbHeader) {
        mbHeader.setWorkOrder(letterOfAcceptanceService.getWorkOrderById(mbHeader.getWorkOrder().getId()));
        mbHeader.setWorkOrderEstimate(workOrderEstimateService.getWorkOrderEstimateById(mbHeader.getWorkOrderEstimate().getId()));
        final MBHeader savedMBHeader = mbHeaderRepository.save(mbHeader);
        return savedMBHeader;
    }

    @Transactional
    public MBHeader cancel(final MBHeader mbHeader) {
        mbHeader.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MBHEADER,
                MBHeader.MeasurementBookStatus.CANCELLED.toString()));
        final MBHeader savedMBHeader = mbHeaderRepository.save(mbHeader);
        return savedMBHeader;
    }

    public List<User> getMBHeaderCreatedByUsers() {
        return mbHeaderRepository.findMBHeaderCreatedByUsers();
    }

    public List<MBHeader> searchMBHeader(final SearchRequestMBHeader searchRequestMBHeader) {

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(MBHeader.class, "mbh")
                .createAlias("workOrderEstimate", "woe").createAlias("workOrderEstimate.workOrder", "wo")
                .createAlias("workOrderEstimate.estimate", "e")
                .createAlias("workOrderEstimate.estimate.executingDepartment", "department")
                .createAlias("workOrderEstimate.workOrder.contractor", "woc").createAlias("mbh.egwStatus", "status");

        if (searchRequestMBHeader != null) {
            if (searchRequestMBHeader.getMbReferenceNumber() != null)
                criteria.add(
                        Restrictions.ilike("mbh.mbRefNo", "%" + searchRequestMBHeader.getMbReferenceNumber() + "%"));
            if (searchRequestMBHeader.getWorkOrderNumber() != null)
                criteria.add(
                        Restrictions.eq("wo.workOrderNumber", searchRequestMBHeader.getWorkOrderNumber()).ignoreCase());
            if (searchRequestMBHeader.getFromDate() != null)
                criteria.add(Restrictions.ge("mbh.mbDate", searchRequestMBHeader.getFromDate()));
            if (searchRequestMBHeader.getToDate() != null)
                criteria.add(Restrictions.le("mbh.mbDate", searchRequestMBHeader.getToDate()));
            if (searchRequestMBHeader.getEstimateNumber() != null)
                criteria.add(
                        Restrictions.eq("e.estimateNumber", searchRequestMBHeader.getEstimateNumber()).ignoreCase());
            if (StringUtils.isNotBlank(searchRequestMBHeader.getContractorName()))
                criteria.add(Restrictions.ge("woc.name", searchRequestMBHeader.getContractorName()));
            if (searchRequestMBHeader.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id", searchRequestMBHeader.getDepartment()));
            if (searchRequestMBHeader.getMbStatus() != null)
                criteria.add(
                        Restrictions.eq("status.id", Integer.valueOf(searchRequestMBHeader.getMbStatus().toString())));
            if (searchRequestMBHeader.getCreatedBy() != null)
                criteria.add(Restrictions.eq("mbh.createdBy.id", searchRequestMBHeader.getCreatedBy()));

        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<EgwStatus> getMBHeaderStatus() {
        final List<EgwStatus> statusList = egwStatusHibernateDAO.getStatusByModule(WorksConstants.MBHEADER);
        final List<EgwStatus> latestStatusList = new ArrayList<EgwStatus>();
        if (!statusList.isEmpty())
            for (final EgwStatus egwStatus : statusList)
                if (!egwStatus.getCode().equals(WorksConstants.NEW))
                    latestStatusList.add(egwStatus);
        return latestStatusList;
    }

    public JsonObject validateMBInDrafts(final Long workOrderEstimateId, final JsonObject jsonObject) {
        final MBHeader mbHeader = mbHeaderRepository
                .findByWorkOrderEstimate_IdAndEgwStatus_codeEquals(workOrderEstimateId, WorksConstants.NEW);
        String userName = "";
        if (mbHeader != null) {
            userName = worksUtils.getApproverName(mbHeader.getState().getOwnerPosition().getId());
            jsonObject.addProperty("mberror", messageSource.getMessage("error.mbheader.newstatus",
                    new String[] { mbHeader.getMbRefNo(), mbHeader.getEgwStatus().getDescription(), userName }, null));
        }
        return jsonObject;
    }

    public JsonObject validateMBInWorkFlow(final Long workOrderEstimateId, final JsonObject jsonObject) {
        final MBHeader mBHeader = mbHeaderRepository.findByWorkOrderEstimateAndStatus(workOrderEstimateId,
                WorksConstants.CANCELLED_STATUS, WorksConstants.APPROVED, WorksConstants.NEW);
        String userName = "";
        if (mBHeader != null) {
            userName = worksUtils.getApproverName(mBHeader.getState().getOwnerPosition().getId());
            jsonObject.addProperty("mberror", messageSource.getMessage("error.mbheader.workflow",
                    new String[] { mBHeader.getMbRefNo(), mBHeader.getEgwStatus().getDescription(), userName }, null));
        }
        return jsonObject;
    }

}