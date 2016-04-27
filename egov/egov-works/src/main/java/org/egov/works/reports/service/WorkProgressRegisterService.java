/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.reports.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.reports.entity.WorkProgressRegisterSearchRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkProgressRegisterService {

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> findWorkIdentificationNumbersToSearchLineEstimatesForLoa(final String code) {
        final List<String> workIdNumbers = lineEstimateDetailsRepository
                .findWorkIdentificationNumbersToSearchWorkProgressRegister("%" + code + "%",
                        LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(),
                        LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
        return workIdNumbers;
    }

    @Transactional
    public List<WorkProgressRegister> searchWorkProgressRegister(
            final WorkProgressRegisterSearchRequest workProgressRegisterSearchRequest) {
        if (workProgressRegisterSearchRequest != null) {
            final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkProgressRegister.class);
            if (workProgressRegisterSearchRequest.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id",
                        workProgressRegisterSearchRequest.getDepartment()));
            if (workProgressRegisterSearchRequest.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.eq("winCode",
                        workProgressRegisterSearchRequest.getWorkIdentificationNumber()));
            if (workProgressRegisterSearchRequest.getContractor() != null) {
                criteria.createAlias("contractor", "contractor");
                criteria.add(Restrictions.or(Restrictions.ilike("contractor.code",
                        workProgressRegisterSearchRequest.getContractor()), Restrictions.ilike("contractor.name",
                                workProgressRegisterSearchRequest.getContractor())));
            }
            if (workProgressRegisterSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(Restrictions.ge("adminSanctionDate",
                        workProgressRegisterSearchRequest.getAdminSanctionFromDate()));
            if (workProgressRegisterSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate",
                        workProgressRegisterSearchRequest.getAdminSanctionToDate()));

            if (workProgressRegisterSearchRequest.isSpillOverFlag())
                criteria.add(Restrictions.eq("spillOverFlag", workProgressRegisterSearchRequest.isSpillOverFlag()));

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else
            return new ArrayList<WorkProgressRegister>();
    }
}
