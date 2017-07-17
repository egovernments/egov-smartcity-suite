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
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.egov.commons.Fund;
import org.egov.commons.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FundService {

    private final FundRepository fundRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public FundService(final FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    @Transactional
    public Fund create(final Fund fund) {
        if (fund.getParentId() != null && fund.getParentId().getId() == null)
            fund.setParentId(null);
        return fundRepository.save(fund);
    }

    public List<Fund> getByIsActive(final Boolean isActive) {
        return fundRepository.findByIsactive(isActive);
    }

    @Transactional
    public Fund update(final Fund fund) {
        return fundRepository.save(fund);
    }

    public List<Fund> findAll() {
        return fundRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Fund findByName(final String name) {
        return fundRepository.findByName(name);
    }

    public Fund findByCode(final String code) {
        return fundRepository.findByCode(code);
    }

    public Fund findOne(final Integer id) {
        return fundRepository.findOne(id);
    }

    public List<Fund> search(final Fund fund) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Fund> createQuery = cb.createQuery(Fund.class);
        final Root<Fund> funds = createQuery.from(Fund.class);
        createQuery.select(funds);
        final Metamodel m = entityManager.getMetamodel();
        final EntityType<Fund> Fund_ = m.entity(Fund.class);

        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (fund.getName() != null) {
            final String name = "%" + fund.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(funds.get("name")));
            predicates.add(cb.like(cb.lower(funds.get(Fund_.getDeclaredSingularAttribute("name", String.class))), name));
        }
        if (fund.getCode() != null) {
            final String code = "%" + fund.getCode().toLowerCase() + "%";
            predicates.add(cb.isNotNull(funds.get("code")));
            predicates.add(cb.like(cb.lower(funds.get(Fund_.getDeclaredSingularAttribute("code", String.class))), code));
        }
        if (fund.getIsactive())
            predicates.add(cb.equal(funds.get("isactive"), true));
        if (fund.getParentId() != null)
            predicates.add(cb.equal(funds.get("parentId"), fund.getParentId()));

        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<Fund> query = entityManager.createQuery(createQuery);
        return query.getResultList();

    }

    public List<Fund> findByIsnotleaf() {
        return fundRepository.findByIsnotleaf(true);
    }

    public List<Fund> findAllActiveAndIsnotleaf() {
        return fundRepository.findByIsactiveAndIsnotleaf(true, false);
    }

}