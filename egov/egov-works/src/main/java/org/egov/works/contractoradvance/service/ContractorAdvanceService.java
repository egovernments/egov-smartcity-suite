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
package org.egov.works.contractoradvance.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.entity.SearchRequestContractorRequisition;
import org.egov.works.contractoradvance.repository.ContractorAdvanceRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContractorAdvanceService {

    @Autowired
    private ContractorAdvanceRepository contractorAdvanceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public ContractorAdvanceRequisition getContractorAdvanceRequisitionById(final Long id) {
        return contractorAdvanceRepository.findOne(id);
    }

    public List<String> getAdvanceRequisitionNumberToSearchCR(final String advanceRequisitionNumber) {
        final List<String> advanceRequisitionNumbers = contractorAdvanceRepository
                .findAdvanceRequisitionNumberToSearchCR("%" + advanceRequisitionNumber + "%");
        return advanceRequisitionNumbers;
    }

    public List<String> getWorkOrderNumberToSearchCR(final String workOrderNumber) {
        final List<String> workOrderNumbers = contractorAdvanceRepository
                .findWorkOrderNumberToSearchCR("%" + workOrderNumber + "%");
        return workOrderNumbers;
    }

    public List<String> getContractorsToSearchCR(final String contractorName) {
        final List<String> contractorNames = contractorAdvanceRepository
                .findContractorsToSearchCR("%" + contractorName + "%");
        return contractorNames;
    }

    public List<ContractorAdvanceRequisition> searchContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClause(searchRequestContractorRequisition, queryStr);
        final Query query = setParameterForSearchContractorAdvance(searchRequestContractorRequisition, queryStr);
        final List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = query.getResultList();
        return contractorAdvanceRequisitionList;
    }

    private void buildWhereClause(final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final StringBuilder queryStr) {

        queryStr.append(
                "select car from ContractorAdvanceRequisition as car where 1=1 ");

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceRequisitionNumber()))
            queryStr.append(
                    " and upper(car.advanceRequisitionNumber) = :advanceRequisitionNumber");

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getWorkOrderNumber()))
            queryStr.append(" and upper(car.workOrderEstimate.workOrder.workOrderNumber) = :workOrderNumber");

        if (StringUtils.isNotBlank(searchRequestContractorRequisition.getContractorName()))
            queryStr.append(" and upper(car.workOrderEstimate.workOrder.contractor.name) = :contractorName");

        if (searchRequestContractorRequisition.getFromDate() != null)
            queryStr.append(
                    " and car.advanceRequisitionDate >= :fromDate");

        if (searchRequestContractorRequisition.getToDate() != null)
            queryStr.append(
                    " and car.advanceRequisitionDate <= :toDate");

        if (searchRequestContractorRequisition.getEgwStatus() != null)
            queryStr.append(
                    " and car.status.code = :status)");

    }

    private Query setParameterForSearchContractorAdvance(
            final SearchRequestContractorRequisition searchRequestContractorRequisition,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        if (searchRequestContractorRequisition != null) {
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getWorkOrderNumber()))
                qry.setParameter("workOrderNumber",
                        searchRequestContractorRequisition.getWorkOrderNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getAdvanceRequisitionNumber()))
                qry.setParameter("advanceRequisitionNumber",
                        searchRequestContractorRequisition.getAdvanceRequisitionNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestContractorRequisition.getContractorName()))
                qry.setParameter("contractorName", searchRequestContractorRequisition.getContractorName().toUpperCase());
            if (searchRequestContractorRequisition.getEgwStatus() != null)
                qry.setParameter("status", searchRequestContractorRequisition.getEgwStatus());
            if (searchRequestContractorRequisition.getFromDate() != null)
                qry.setParameter("fromDate", searchRequestContractorRequisition.getFromDate());
            if (searchRequestContractorRequisition.getToDate() != null)
                qry.setParameter("toDate", searchRequestContractorRequisition.getToDate());

        }
        return qry;
    }

}
