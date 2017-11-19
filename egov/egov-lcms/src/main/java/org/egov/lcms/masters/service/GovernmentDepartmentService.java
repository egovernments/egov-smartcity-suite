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
package org.egov.lcms.masters.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import org.egov.lcms.masters.entity.GovernmentDepartment;
import org.egov.lcms.masters.repository.GovernmentDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GovernmentDepartmentService {

    private final GovernmentDepartmentRepository governmentDepartmentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GovernmentDepartmentService(final GovernmentDepartmentRepository governmentDepartmentRepository) {
        this.governmentDepartmentRepository = governmentDepartmentRepository;
    }

    @Transactional
    public GovernmentDepartment persist(final GovernmentDepartment governmentDepartment) {
        return governmentDepartmentRepository.save(governmentDepartment);
    }

    public List<GovernmentDepartment> findAll() {
        return governmentDepartmentRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public GovernmentDepartment findByName(final String name) {
        return governmentDepartmentRepository.findByName(name);
    }

    public GovernmentDepartment findByCode(final String code) {
        return governmentDepartmentRepository.findByCode(code);
    }

    public GovernmentDepartment findOne(final Long id) {
        return governmentDepartmentRepository.findOne(id);
    }
    public List<GovernmentDepartment> getActiveGovernmentDepartment() {
        return governmentDepartmentRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<GovernmentDepartment> search(final GovernmentDepartment governmentDepartment) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<GovernmentDepartment> createQuery = cb.createQuery(GovernmentDepartment.class);
        final Root<GovernmentDepartment> governmentDepartmentObj = createQuery.from(GovernmentDepartment.class);
        createQuery.select(governmentDepartmentObj);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<GovernmentDepartment> GovernmentDepartment = m
                .entity(GovernmentDepartment.class);

        final List<GovernmentDepartment> resultList;
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (governmentDepartment.getName() == null && governmentDepartment.getCode() == null
                && governmentDepartment.getActive() == null)
            resultList = findAll();
        else {
            if (governmentDepartment.getName() != null) {
                final String interimOrderType = "%" + governmentDepartment.getName().toLowerCase() + "%";
                predicates.add(cb.isNotNull(governmentDepartmentObj.get("name")));
                predicates.add(cb.like(
                        cb.lower(governmentDepartmentObj
                                .get(GovernmentDepartment.getDeclaredSingularAttribute("name", String.class))),
                                interimOrderType));
            }
            if (governmentDepartment.getCode() != null) {
                final String code = "%" + governmentDepartment.getCode().toLowerCase() + "%";
                predicates.add(cb.isNotNull(governmentDepartmentObj.get("code")));
                predicates
                .add(cb.like(
                        cb.lower(governmentDepartmentObj
                                .get(GovernmentDepartment.getDeclaredSingularAttribute("code", String.class))),
                                code));
            }
            if (governmentDepartment.getActive() != null)
                if (governmentDepartment.getActive() == true)
                    predicates
                    .add(cb.equal(
                            governmentDepartmentObj.get(
                                    GovernmentDepartment.getDeclaredSingularAttribute("active", Boolean.class)),
                                    true));
                else
                    predicates.add(cb.equal(
                            governmentDepartmentObj
                            .get(GovernmentDepartment.getDeclaredSingularAttribute("active", Boolean.class)),
                            false));

            createQuery.where(predicates.toArray(new Predicate[] {}));
            final TypedQuery<GovernmentDepartment> query = entityManager.createQuery(createQuery);

            resultList = query.getResultList();
        }
        return resultList;
    }
}