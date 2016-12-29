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
package org.egov.egf.commons.bank.service;

import java.util.ArrayList;
import java.util.Date;
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

import org.egov.commons.Bank;
import org.egov.egf.commons.bank.repository.BankRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author venki
 */

@Service
@Transactional(readOnly = true)
public class CreateBankService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BankRepository bankRepository;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public CreateBankService(final BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Bank getById(final Integer id) {
        return bankRepository.findOne(id);
    }

    public List<Bank> getByIsActive(final Boolean isActive) {
        return bankRepository.findByIsactive(isActive);
    }

    public List<Bank> getByIsActiveTrueOrderByName() {
        return bankRepository.findByIsactiveTrueOrderByNameAsc();
    }

    @Transactional
    public Bank create(final Bank bank) {

        bank.setCreatedDate(new Date());
        bank.setCreatedBy(getCurrentSession().load(User.class, ApplicationThreadLocals.getUserId()));

        return bankRepository.save(bank);
    }

    @Transactional
    public Bank update(final Bank bank) {

        bank.setLastModifiedDate(new Date());
        bank.setLastModifiedBy(getCurrentSession().load(User.class, ApplicationThreadLocals.getUserId()));
        return bankRepository.save(bank);
    }

    public List<Bank> search(final Bank bank) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Bank> createQuery = cb.createQuery(Bank.class);
        final Root<Bank> banks = createQuery.from(Bank.class);
        createQuery.select(banks);
        final Metamodel m = entityManager.getMetamodel();
        final EntityType<Bank> tempBank = m.entity(Bank.class);

        final List<Predicate> predicates = new ArrayList<>();
        if (bank.getName() != null) {
            final String name = "%" + bank.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(banks.get("name")));
            predicates.add(cb.like(cb.lower(banks.get(tempBank.getDeclaredSingularAttribute("name", String.class))), name));
        }
        if (bank.getCode() != null) {
            final String code = "%" + bank.getCode().toLowerCase() + "%";
            predicates.add(cb.isNotNull(banks.get("code")));
            predicates.add(cb.like(cb.lower(banks.get(tempBank.getDeclaredSingularAttribute("code", String.class))), code));
        }
        if (bank.getIsactive())
            predicates.add(cb.equal(banks.get("isactive"), true));
        if (bank.getNarration() != null)
            predicates.add(cb.equal(banks.get("narration"), bank.getNarration()));

        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<Bank> query = entityManager.createQuery(createQuery);
        return query.getResultList();

    }

}
