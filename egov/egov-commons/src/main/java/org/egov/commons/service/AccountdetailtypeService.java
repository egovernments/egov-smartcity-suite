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

package org.egov.commons.service;

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

import org.egov.commons.Accountdetailtype;
import org.egov.commons.repository.AccountdetailtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountdetailtypeService {

    private final AccountdetailtypeRepository accountdetailtypeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AccountdetailtypeService(final AccountdetailtypeRepository accountdetailtypeRepository) {
        this.accountdetailtypeRepository = accountdetailtypeRepository;
    }

    @Transactional
    public Accountdetailtype create(final Accountdetailtype accountdetailtype) {
        return accountdetailtypeRepository.save(accountdetailtype);
    }

    @Transactional
    public Accountdetailtype update(final Accountdetailtype accountdetailtype) {
        return accountdetailtypeRepository.save(accountdetailtype);
    }

    public List<Accountdetailtype> findAll() {
        return accountdetailtypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Accountdetailtype findByName(final String name) {
        return accountdetailtypeRepository.findByName(name);
    }

    public Accountdetailtype findOne(final Integer id) {
        return accountdetailtypeRepository.findOne(id);
    }

    public List<Accountdetailtype> findByFullQualifiedName(final String fullQualifiedName) {
        return accountdetailtypeRepository.findByFullQualifiedName(fullQualifiedName);
    }

    public List<Accountdetailtype> search(final Accountdetailtype accountdetailtype, final String mode) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Accountdetailtype> createQuery = cb.createQuery(Accountdetailtype.class);
        final Root<Accountdetailtype> accountdetailtypes = createQuery.from(Accountdetailtype.class);
        createQuery.select(accountdetailtypes);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<Accountdetailtype> tempAccountDetailType = m.entity(Accountdetailtype.class);

        final List<Predicate> predicates = new ArrayList<>();
        if (accountdetailtype.getName() != null) {
            final String name = "%" + accountdetailtype.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(accountdetailtypes.get("name")));
            predicates.add(cb.like(
                    cb.lower(accountdetailtypes.get(tempAccountDetailType.getDeclaredSingularAttribute("name", String.class))),
                    name));
        }
        if (accountdetailtype.getDescription() != null) {
            final String code = "%" + accountdetailtype.getDescription().toLowerCase() + "%";
            predicates.add(cb.isNotNull(accountdetailtypes.get("description")));
            predicates.add(cb.like(
                    cb.lower(
                            accountdetailtypes
                                    .get(tempAccountDetailType.getDeclaredSingularAttribute("description", String.class))),
                    code));
        }
        if ("edit".equalsIgnoreCase(mode))
            predicates.add(cb.equal(accountdetailtypes.get("fullQualifiedName"), "org.egov.masters.model.AccountEntity"));
        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<Accountdetailtype> query = entityManager.createQuery(createQuery);

        return query.getResultList();
    }

    public List<Accountdetailtype> findByGlcodeId(final Long glcodeId) {
        return accountdetailtypeRepository.findByGlcodeId(glcodeId);
    }

    public Accountdetailtype findByDescription(final String description) {
        return accountdetailtypeRepository.findByDescription(description);
    }
}