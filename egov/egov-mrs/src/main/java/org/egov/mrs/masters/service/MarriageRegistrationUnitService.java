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
package org.egov.mrs.masters.service;

import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.egov.mrs.masters.repository.MrRegistrationUnitRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MarriageRegistrationUnitService {

    @Autowired
    private MrRegistrationUnitRepository mrregistrationUnitRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public void createMrRegistrationUnit(
            final MarriageRegistrationUnit marriageRegistrationUnit) {
        mrregistrationUnitRepository.save(marriageRegistrationUnit);
    }

    @Transactional
    public MarriageRegistrationUnit updateMrRegistrationUnit(
            final MarriageRegistrationUnit marriageRegistrationUnit) {
        return mrregistrationUnitRepository
                .saveAndFlush(marriageRegistrationUnit);
    }

    public List<MarriageRegistrationUnit> getActiveRegistrationunit() {
        return mrregistrationUnitRepository.findByisActiveTrueOrderByNameAsc();
    }

    public MarriageRegistrationUnit findById(final Long id) {
        return mrregistrationUnitRepository.findById(id);
    }

    public MarriageRegistrationUnit getMarriageRegistrationUnit(
            final String name) {
        return mrregistrationUnitRepository.findByName(name);
    }

    public List<MarriageRegistrationUnit> getMarriageRegistrationUnits() {
        return mrregistrationUnitRepository.findAll();
    }

    public MarriageRegistrationUnit getbyId(final Long id) {
        return mrregistrationUnitRepository.getOne(id);
    }

    public List<MarriageRegistrationUnit> findAll() {
        return mrregistrationUnitRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    public List<MarriageRegistrationUnit> searchMarriageRegistrationUnit(
            final MarriageRegistrationUnit marriageRegistrationUnit) {
        final Criteria criteria = getCurrentSession().createCriteria(
                MarriageRegistrationUnit.class);
        if (null != marriageRegistrationUnit.getName())
            criteria.add(Restrictions.ilike("name",
                    marriageRegistrationUnit.getName(), MatchMode.ANYWHERE));
        if (null != marriageRegistrationUnit.getAddress())
            criteria.add(Restrictions.ilike("address",
                    marriageRegistrationUnit.getAddress(), MatchMode.ANYWHERE));
        if (null != marriageRegistrationUnit.getIsActive() && marriageRegistrationUnit.getIsActive())
            criteria.add(Restrictions.eq("isActive",
                    marriageRegistrationUnit.getIsActive()));
        if (null != marriageRegistrationUnit.getIsMainRegistrationUnit()
                && marriageRegistrationUnit.getIsMainRegistrationUnit())
            criteria.add(Restrictions.eq("isMainRegistrationUnit",
                    marriageRegistrationUnit.getIsMainRegistrationUnit()));
        if (marriageRegistrationUnit.getZone().getId() != null)
            criteria.add(Restrictions.eq("zone.id", marriageRegistrationUnit
                    .getZone().getId()));

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

}
