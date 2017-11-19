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

import org.egov.lcms.masters.entity.CourtMaster;
import org.egov.lcms.masters.entity.CourtTypeMaster;
import org.egov.lcms.masters.repository.CourtMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CourtMasterService {

    private final CourtMasterRepository courtMasterRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CourtMasterService(final CourtMasterRepository courtMasterRepository) {
        this.courtMasterRepository = courtMasterRepository;
    }

    @Transactional
    public CourtMaster persist(final CourtMaster courtMaster) {
        return courtMasterRepository.save(courtMaster);
    }

    public List<CourtMaster> findAll() {
        return courtMasterRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CourtMaster findByName(final String name) {
        return courtMasterRepository.findByName(name);
    }

    public CourtMaster findOne(final Long id) {
        return courtMasterRepository.findOne(id);
    }

    public List<CourtMaster> findActiveCourtByCourtType(final CourtTypeMaster courtType) {
        return courtMasterRepository.findByActiveTrueAndCourtType(courtType);
    }

    public List<CourtMaster> getActiveCourtMaster() {
        return courtMasterRepository.findByActiveTrueOrderByOrderNumberAsc();
    }

    public List<CourtMaster> search(final CourtMaster courtMaster) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<CourtMaster> createQuery = cb.createQuery(CourtMaster.class);
        final Root<CourtMaster> courtMasterobj = createQuery.from(CourtMaster.class);
        createQuery.select(courtMasterobj);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<CourtMaster> CourtMaster = m.entity(CourtMaster.class);

        final List<CourtMaster> resultList;
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (courtMaster.getName() == null && courtMaster.getCourtType() == null && courtMaster.getActive() == null)
            resultList = findAll();
        else {
            if (courtMaster.getName() != null) {
                final String name = "%" + courtMaster.getName().toLowerCase() + "%";
                predicates.add(cb.isNotNull(courtMasterobj.get("name")));
                predicates.add(cb.like(
                        cb.lower(courtMasterobj.get(CourtMaster.getDeclaredSingularAttribute("name", String.class))),
                        name));
            }
            if (courtMaster.getCourtType() != null)
                predicates.add(cb.equal(courtMasterobj.get("courtType"), courtMaster.getCourtType()));
            if (courtMaster.getActive() != null)
                if (courtMaster.getActive())
                    predicates.add(cb.equal(
                            courtMasterobj.get(CourtMaster.getDeclaredSingularAttribute("active", Boolean.class)),
                            true));
                else
                    predicates.add(cb.equal(
                            courtMasterobj.get(CourtMaster.getDeclaredSingularAttribute("active", Boolean.class)),
                            false));

            createQuery.where(predicates.toArray(new Predicate[] {}));
            final TypedQuery<CourtMaster> query = entityManager.createQuery(createQuery);

            resultList = query.getResultList();
        }
        return resultList;
    }

}