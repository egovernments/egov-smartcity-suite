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

import org.egov.lcms.masters.entity.CourtTypeMaster;
import org.egov.lcms.masters.entity.PetitionTypeMaster;
import org.egov.lcms.masters.repository.PetitionTypeMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PetitionTypeMasterService {

    private final PetitionTypeMasterRepository petitionTypeMasterRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PetitionTypeMasterService(final PetitionTypeMasterRepository petitionTypeMasterRepository) {
        this.petitionTypeMasterRepository = petitionTypeMasterRepository;
    }

    @Transactional
    public PetitionTypeMaster create(final PetitionTypeMaster petitionTypeMaster) {
        return petitionTypeMasterRepository.save(petitionTypeMaster);
    }

    @Transactional
    public PetitionTypeMaster update(final PetitionTypeMaster petitionTypeMaster) {
        return petitionTypeMasterRepository.save(petitionTypeMaster);
    }

    public List<PetitionTypeMaster> findAll() {
        return petitionTypeMasterRepository.findAll(new Sort(Sort.Direction.ASC, "petitionType"));
    }

    public PetitionTypeMaster findByCode(final String code) {
        return petitionTypeMasterRepository.findByCode(code);
    }

    public PetitionTypeMaster findOne(final Long id) {
        return petitionTypeMasterRepository.findOne(id);
    }

    public List<PetitionTypeMaster> findActivePetitionByCourtType(final CourtTypeMaster courtType) {
        return petitionTypeMasterRepository.findByActiveTrueAndCourtType(courtType);
    }

    public List<PetitionTypeMaster> search(final PetitionTypeMaster petitionTypeMaster) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<PetitionTypeMaster> createQuery = cb.createQuery(PetitionTypeMaster.class);
        final Root<PetitionTypeMaster> petitionTypeMasterobj = createQuery.from(PetitionTypeMaster.class);
        createQuery.select(petitionTypeMasterobj);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<PetitionTypeMaster> PetitionTypeMaster = m
                .entity(PetitionTypeMaster.class);

        final List<PetitionTypeMaster> resultList;
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (petitionTypeMaster.getCode() == null && petitionTypeMaster.getCourtType() == null
                && petitionTypeMaster.getPetitionType() == null && petitionTypeMaster.getActive() == null)
            resultList = findAll();
        else {
            if (petitionTypeMaster.getCode() != null) {
                final String code = "%" + petitionTypeMaster.getCode().toLowerCase() + "%";
                predicates.add(cb.isNotNull(petitionTypeMasterobj.get("code")));
                predicates
                        .add(cb.like(
                                cb.lower(petitionTypeMasterobj
                                        .get(PetitionTypeMaster.getDeclaredSingularAttribute("code", String.class))),
                                code));
            }
            if (petitionTypeMaster.getPetitionType() != null) {
                final String petitionType = "%" + petitionTypeMaster.getPetitionType().toLowerCase() + "%";
                predicates.add(cb.isNotNull(petitionTypeMasterobj.get("petitionType")));
                predicates.add(cb.like(
                        cb.lower(petitionTypeMasterobj
                                .get(PetitionTypeMaster.getDeclaredSingularAttribute("petitionType", String.class))),
                        petitionType));
            }
            if (petitionTypeMaster.getCourtType() != null)
                predicates.add(cb.equal(petitionTypeMasterobj.get("courtType"), petitionTypeMaster.getCourtType()));
            if (petitionTypeMaster.getActive() != null)
                if (petitionTypeMaster.getActive())
                    predicates
                            .add(cb.equal(
                                    petitionTypeMasterobj.get(
                                            PetitionTypeMaster.getDeclaredSingularAttribute("active", Boolean.class)),
                                    true));
                else
                    predicates
                            .add(cb.equal(
                                    petitionTypeMasterobj.get(
                                            PetitionTypeMaster.getDeclaredSingularAttribute("active", Boolean.class)),
                                    false));

            createQuery.where(predicates.toArray(new Predicate[] {}));
            final TypedQuery<PetitionTypeMaster> query = entityManager.createQuery(createQuery);

            resultList = query.getResultList();
        }
        return resultList;
    }

    public List<PetitionTypeMaster> getPetitiontypeList() {
        return petitionTypeMasterRepository.findAll();
    }

    public List<PetitionTypeMaster> getActivePetitionTypes() {
        return petitionTypeMasterRepository.findByActiveTrueOrderByOrderNumberAsc();
    }

}