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

import org.egov.lcms.masters.entity.CaseTypeMaster;
import org.egov.lcms.masters.repository.CaseTypeMasterRepository;
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
public class CaseTypeMasterService {

    private final CaseTypeMasterRepository casetypeMasterRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CaseTypeMasterService(final CaseTypeMasterRepository casetypeMasterRepository) {
        this.casetypeMasterRepository = casetypeMasterRepository;
    }

    @Transactional
    public CaseTypeMaster create(final CaseTypeMaster casetypeMaster) {
        return casetypeMasterRepository.save(casetypeMaster);
    }

    public List<CaseTypeMaster> getCaseTypeList() {
        return casetypeMasterRepository.findAll();
    }

    public List<CaseTypeMaster> getActiveCaseTypeList() {
        return casetypeMasterRepository.findByActiveTrueOrderByOrdernumberAsc();
    }

    @Transactional
    public CaseTypeMaster update(final CaseTypeMaster casetypeMaster) {
        return casetypeMasterRepository.save(casetypeMaster);
    }

    public List<CaseTypeMaster> findAll() {
        return casetypeMasterRepository.findAll(new Sort(Sort.Direction.ASC, "caseType"));
    }

    public CaseTypeMaster findByCode(final String code) {
        return casetypeMasterRepository.findByCode(code);
    }

    public CaseTypeMaster findOne(final Long id) {
        return casetypeMasterRepository.findOne(id);
    }

    public List<CaseTypeMaster> search(final CaseTypeMaster casetypeMaster) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<CaseTypeMaster> createQuery = cb.createQuery(CaseTypeMaster.class);
        final Root<CaseTypeMaster> casetypemasters = createQuery.from(CaseTypeMaster.class);
        createQuery.select(casetypemasters);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<CaseTypeMaster> CasetypeMaster = m.entity(CaseTypeMaster.class);

        final List<CaseTypeMaster> resultList;
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (casetypeMaster.getCaseType() == null && casetypeMaster.getCode() == null
                && casetypeMaster.getActive() == null)
            resultList = findAll();
        else {
            if (casetypeMaster.getCode() != null) {
                final String code = "%" + casetypeMaster.getCode().toLowerCase() + "%";
                predicates.add(cb.isNotNull(casetypemasters.get("code")));
                predicates.add(cb.like(
                        cb.lower(
                                casetypemasters.get(CasetypeMaster.getDeclaredSingularAttribute("code", String.class))),
                        code));
            }
            if (casetypeMaster.getCaseType() != null) {
                final String caseType = "%" + casetypeMaster.getCaseType().toLowerCase() + "%";
                predicates.add(cb.isNotNull(casetypemasters.get("caseType")));
                predicates.add(cb.like(
                        cb.lower(casetypemasters
                                .get(CasetypeMaster.getDeclaredSingularAttribute("caseType", String.class))),
                        caseType));
            }
            if (casetypeMaster.getActive() != null)
                if (casetypeMaster.getActive())
                    predicates.add(cb.equal(
                            casetypemasters.get(CasetypeMaster.getDeclaredSingularAttribute("active", Boolean.class)),
                            true));
                else
                    predicates.add(cb.equal(
                            casetypemasters.get(CasetypeMaster.getDeclaredSingularAttribute("active", Boolean.class)),
                            false));

            createQuery.where(predicates.toArray(new Predicate[] {}));
            final TypedQuery<CaseTypeMaster> query = entityManager.createQuery(createQuery);

            resultList = query.getResultList();
        }
        return resultList;
    }
}