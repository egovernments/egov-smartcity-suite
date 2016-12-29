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
package org.egov.egf.commons.bankbranch.service;

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

import org.egov.commons.Bankbranch;
import org.egov.egf.commons.bankbranch.repository.BankBranchRepository;
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
public class CreateBankBranchService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BankBranchRepository bankBranchRepository;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public CreateBankBranchService(final BankBranchRepository bankBranchRepository) {
        this.bankBranchRepository = bankBranchRepository;
    }

    public Bankbranch getById(final Integer id) {
        return bankBranchRepository.findOne(id);
    }

    public List<Bankbranch> getByBankId(final Integer bankId) {
        return bankBranchRepository.findByBank_Id(bankId);
    }

    public List<Bankbranch> getByIsActive(final Boolean isActive) {
        return bankBranchRepository.findByIsactive(isActive);
    }

    public List<Bankbranch> getByIsActiveTrueOrderByBranchname() {
        return bankBranchRepository.findByIsactiveTrueOrderByBranchnameAsc();
    }

    @Transactional
    public Bankbranch create(final Bankbranch bankBranch) {

        bankBranch.setCreatedDate(new Date());
        bankBranch.setCreatedBy(getCurrentSession().load(User.class, ApplicationThreadLocals.getUserId()));

        return bankBranchRepository.save(bankBranch);
    }

    @Transactional
    public Bankbranch update(final Bankbranch bankbranch) {

        bankbranch.setLastModifiedDate(new Date());
        bankbranch.setLastModifiedBy(getCurrentSession().load(User.class, ApplicationThreadLocals.getUserId()));
        return bankBranchRepository.save(bankbranch);
    }

    public List<Bankbranch> search(final Bankbranch bankBranch) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Bankbranch> createQuery = cb.createQuery(Bankbranch.class);
        final Root<Bankbranch> bankBranchs = createQuery.from(Bankbranch.class);
        createQuery.select(bankBranchs);
        final Metamodel m = entityManager.getMetamodel();
        final EntityType<Bankbranch> tempBankBranch = m.entity(Bankbranch.class);

        final List<Predicate> predicates = new ArrayList<>();
        if (bankBranch.getBranchcode() != null) {
            final String code = "%" + bankBranch.getBranchcode().toLowerCase() + "%";
            predicates.add(cb.isNotNull(bankBranchs.get("branchcode")));
            predicates.add(cb.like(
                    cb.lower(bankBranchs.get(tempBankBranch.getDeclaredSingularAttribute("branchcode", String.class))), code));
        }

        if (bankBranch.getBranchMICR() != null)
            predicates.add(cb.equal(bankBranchs.get("branchMICR"), bankBranch.getBranchMICR()));

        if (bankBranch.getBranchaddress1() != null)
            predicates.add(cb.equal(bankBranchs.get("branchaddress1"), bankBranch.getBranchaddress1()));

        if (bankBranch.getContactperson() != null)
            predicates.add(cb.equal(bankBranchs.get("contactperson"), bankBranch.getContactperson()));

        if (bankBranch.getBranchphone() != null)
            predicates.add(cb.equal(bankBranchs.get("branchphone"), bankBranch.getBranchphone()));

        if (bankBranch.getNarration() != null)
            predicates.add(cb.equal(bankBranchs.get("narration"), bankBranch.getNarration()));

        if (bankBranch.getIsactive())
            predicates.add(cb.equal(bankBranchs.get("isactive"), true));

        if (bankBranch.getBank() != null && bankBranch.getBank().getId() != null)
            predicates.add(cb.equal(bankBranchs.get("bank").get("id"), bankBranch.getBank().getId()));

        if (bankBranch.getId() != null)
            predicates.add(cb.equal(bankBranchs.get("id"), bankBranch.getId()));

        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<Bankbranch> query = entityManager.createQuery(createQuery);
        return query.getResultList();

    }

}
