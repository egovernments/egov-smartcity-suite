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

package org.egov.commons.service;

import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FunctionService {

    private final FunctionRepository functionRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public FunctionService(final FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    @Transactional
    public CFunction create(final CFunction function) {
        return functionRepository.save(function);
    }

    @Transactional
    public CFunction update(final CFunction function) {
        return functionRepository.save(function);
    }

    public List<CFunction> findAllActive() {
        return functionRepository.findByIsActiveAndIsNotLeaf(true, false);
    }

    public List<CFunction> findAll() {
        return functionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CFunction findByName(final String name) {
        return functionRepository.findByName(name);
    }

    public CFunction findByCode(final String code) {
        return functionRepository.findByCode(code);
    }

    public CFunction findOne(final Long id) {
        return functionRepository.findOne(id);
    }

    public List<CFunction> findAllIsNotLeafTrue() {
        return functionRepository.findByIsNotLeaf(true);
    }

    public List<CFunction> findByNameLikeOrCodeLike(final String name) {
        return functionRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(name, name);
    }

    public List<CFunction> search(final CFunction function) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<CFunction> createQuery = cb.createQuery(CFunction.class);
        final Root<CFunction> functions = createQuery.from(CFunction.class);
        createQuery.select(functions);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<CFunction> tempFunction = m.entity(CFunction.class);

        final List<Predicate> predicates = new ArrayList<>();
        if (function.getName() != null) {
            final String name = "%" + function.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(functions.get("name")));
            predicates.add(cb.like(
                    cb.lower(functions.get(tempFunction.getDeclaredSingularAttribute("name", String.class))), name));
        }
        if (function.getCode() != null) {
            final String code = "%" + function.getCode().toLowerCase() + "%";
            predicates.add(cb.isNotNull(functions.get("code")));
            predicates.add(cb.like(
                    cb.lower(functions.get(tempFunction.getDeclaredSingularAttribute("code", String.class))), code));
        }
        if (function.getIsActive())
            predicates.add(
                    cb.equal(functions.get(tempFunction.getDeclaredSingularAttribute("isActive", Boolean.class)), true));
        if (function.getParentId() != null)

            predicates.add(cb.equal(functions.get("parentId"), function.getParentId()));

        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<CFunction> query = entityManager.createQuery(createQuery);

        return query.getResultList();
    }

}