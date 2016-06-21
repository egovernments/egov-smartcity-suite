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
package org.egov.works.offlinestatus.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.repository.LetterOfAcceptanceRepository;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.repository.OfflineStatusRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OfflineStatusService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private OfflineStatusRepository offlineStatusRepository;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public List<WorkOrder> searchLetterOfAcceptanceForOfflineStatus(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrder.class, "wo")
                .addOrder(Order.asc("workOrderDate"))
                .createAlias("wo.contractor", "woc")
                .createAlias("egwStatus", "status");

        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(
                        Restrictions.eq("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                criteria.add(Restrictions.ge("workOrderDate", searchRequestLetterOfAcceptance.getFromDate()));
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                criteria.add(Restrictions.le("workOrderDate", searchRequestLetterOfAcceptance.getToDate()));
            if (searchRequestLetterOfAcceptance.getName() != null)
                criteria.add(Restrictions.eq("woc.name", searchRequestLetterOfAcceptance.getName()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                criteria.add(
                        Restrictions.ilike("fileNumber", searchRequestLetterOfAcceptance.getFileNumber(), MatchMode.ANYWHERE));
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null) {
                // TODO Need TO handle in single query
                final List<String> estimateNumbers = lineEstimateDetailsRepository
                        .findEstimateNumbersForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
                if (estimateNumbers.isEmpty())
                    estimateNumbers.add("");
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));
            }
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null) {
                final List<Long> workOrderIds = letterOfAcceptanceRepository
                        .findWorkOrderForLoaStatus(searchRequestLetterOfAcceptance.getEgwStatus(), WorksConstants.WORKORDER);
                if (workOrderIds.isEmpty())
                    workOrderIds.add(null);
                criteria.add(Restrictions.or(
                        Restrictions.in("id", workOrderIds),
                        Restrictions.or(Restrictions.eq("status.code", searchRequestLetterOfAcceptance.getEgwStatus()))));

            }

        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Transactional
    public WorkOrder create(final WorkOrder workOrder, final Long id) {

        for (final OfflineStatus status : workOrder.getOfflineStatuses())
            if (status.getId() == null) {
                status.setObjectId(id);
                status.setObjectType(WorksConstants.WORKORDER);
                status.setEgwStatus(worksUtils.getStatusById(status.getEgwStatus().getId()));
                offlineStatusRepository.save(status);
            }

        return workOrder;
    }

    public List<OfflineStatus> getOfflineStatusByObjetIdAndType(final WorkOrder workOrder) {
        return offlineStatusRepository.findByObjectIdAndObjectType(workOrder.getId(), WorksConstants.WORKORDER);
    }

    public List<OfflineStatus> getOfflineStatusByObjetId(final Long workOrderId) {
        return offlineStatusRepository.findByObjectId(workOrderId, WorksConstants.WORKORDER);
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getStatusNameDetails(final String[] statusNames) {
        return CollectionUtils.select(Arrays.asList(statusNames), statusName -> (String) statusName != null);
    }

}
