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

import org.egov.lcms.masters.entity.JudgmentType;
import org.egov.lcms.masters.repository.JudgmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JudgmentTypeService {

    private final JudgmentTypeRepository judgmentTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JudgmentTypeService(final JudgmentTypeRepository judgmentTypeRepository) {
        this.judgmentTypeRepository = judgmentTypeRepository;
    }

    @Transactional
    public JudgmentType persist(final JudgmentType judgmentType) {
        return judgmentTypeRepository.save(judgmentType);
    }

    public List<JudgmentType> findAll() {
        return judgmentTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public JudgmentType findByCode(final String code) {
        return judgmentTypeRepository.findByCode(code);
    }

    public JudgmentType findOne(final Long id) {
        return judgmentTypeRepository.findOne(id);
    }

    public List<JudgmentType> getJudgmentTypeList() {
        return judgmentTypeRepository.findAll();
    }

    public List<JudgmentType> getActiveJudgementTypes() {
        return judgmentTypeRepository.findByActiveTrueOrderByOrderNumberAsc();
    }

    public List<JudgmentType> search(final JudgmentType judgmentType) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<JudgmentType> createQuery = cb.createQuery(JudgmentType.class);
        final Root<JudgmentType> judgmentTypeObj = createQuery.from(JudgmentType.class);
        createQuery.select(judgmentTypeObj);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<JudgmentType> JudgmentType = m.entity(JudgmentType.class);

        final List<JudgmentType> resultList;
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (judgmentType.getName() == null && judgmentType.getCode() == null && judgmentType.getActive() == null)
            resultList = findAll();
        else {
            if (judgmentType.getName() != null) {
                final String interimOrderType = "%" + judgmentType.getName().toLowerCase() + "%";
                predicates.add(cb.isNotNull(judgmentTypeObj.get("name")));
                predicates
                .add(cb.like(
                        cb.lower(judgmentTypeObj
                                .get(JudgmentType.getDeclaredSingularAttribute("name", String.class))),
                                interimOrderType));
            }
            if (judgmentType.getCode() != null) {
                final String code = "%" + judgmentType.getCode().toLowerCase() + "%";
                predicates.add(cb.isNotNull(judgmentTypeObj.get("code")));
                predicates.add(cb.like(
                        cb.lower(judgmentTypeObj.get(JudgmentType.getDeclaredSingularAttribute("code", String.class))),
                        code));
            }
            if (judgmentType.getActive() != null)
                if (judgmentType.getActive())
                    predicates.add(cb.equal(
                            judgmentTypeObj.get(JudgmentType.getDeclaredSingularAttribute("active", Boolean.class)),
                            true));
                else
                    predicates.add(cb.equal(
                            judgmentTypeObj.get(JudgmentType.getDeclaredSingularAttribute("active", Boolean.class)),
                            false));

            createQuery.where(predicates.toArray(new Predicate[] {}));
            final TypedQuery<JudgmentType> query = entityManager.createQuery(createQuery);

            resultList = query.getResultList();
        }
        return resultList;
    }
}