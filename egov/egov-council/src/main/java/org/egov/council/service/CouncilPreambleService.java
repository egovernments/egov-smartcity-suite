/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.council.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilPreamble;
import org.egov.council.repository.CouncilPreambleRepository;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilPreambleService {

    private final CouncilPreambleRepository CouncilPreambleRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected AutonumberServiceBeanResolver autonumberServiceBeanResolver;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public CouncilPreambleService(final CouncilPreambleRepository CouncilPreambleRepository) {
        this.CouncilPreambleRepository = CouncilPreambleRepository;
    }

    @Transactional
    public CouncilPreamble create(final CouncilPreamble councilPreamble) {
        return CouncilPreambleRepository.save(councilPreamble);
    }

    @Transactional
    public CouncilPreamble update(final CouncilPreamble CouncilPreamble) {
        return CouncilPreambleRepository.save(CouncilPreamble);
    }

    public CouncilPreamble findOne(Long id) {
        return CouncilPreambleRepository.findById(id);
    }

    public List<CouncilPreamble> search(CouncilPreamble councilPreamble) {
        final Criteria criteria = getCurrentSession().createCriteria(CouncilPreamble.class);
        if (null != councilPreamble.getDepartment())
            criteria.add(Restrictions.eq("department", councilPreamble.getDepartment()));

        if (null != councilPreamble.getPreambleNumber())
            criteria.add(Restrictions.eq("preambleNumber", councilPreamble.getPreambleNumber()));
        if (null != councilPreamble.getCreatedDate())
            criteria.add(Restrictions.eq("createdDate", councilPreamble.getCreatedDate()));
        if (null != councilPreamble.getCreatedDate())
            criteria.add(Restrictions.eq("createdDate", councilPreamble.getCreatedDate()));
        if (null != councilPreamble.getGistOfPreamble())
            criteria.add(Restrictions.ilike("gistOfPreamble", councilPreamble.getGistOfPreamble(), MatchMode.ANYWHERE));

        return criteria.list();
    }

}