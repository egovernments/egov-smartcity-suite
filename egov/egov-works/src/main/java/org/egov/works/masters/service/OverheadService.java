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
package org.egov.works.masters.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.works.masters.entity.Overhead;
import org.egov.works.masters.entity.OverheadRate;
import org.egov.works.masters.entity.SearchRequestOverhead;
import org.egov.works.masters.repository.OverheadRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OverheadService {

    @Autowired
    private OverheadRepository overheadRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Overhead create(final Overhead overhead) throws IOException {
        for (final OverheadRate overheadRates : overhead.getOverheadRates())
            overheadRates.setOverhead(overhead);
        final Overhead newOverhead = overheadRepository.save(overhead);
        return newOverhead;
    }

    public Overhead getOverheadById(final Long overheadId) {
        return overheadRepository.findOne(overheadId);
    }

    public Overhead getOverheadByName(final String name) {
        return overheadRepository.findByNameIgnoreCase(name);
    }

    public List<Overhead> getOverheadsByDate(final Date date) {
        return overheadRepository.getByDate(date);
    }

    public List<Overhead> searchOverheadToModify(final SearchRequestOverhead searchRequestOverhead) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Overhead.class)
                .addOrder(Order.asc("createdDate"));
        if (searchRequestOverhead != null) {
            if (searchRequestOverhead.getOverheadName() != null)
                criteria.add(Restrictions.eq("name", searchRequestOverhead.getOverheadName()).ignoreCase());
            if (searchRequestOverhead.getOverheadDescription() != null)
                criteria.add(
                        Restrictions.ilike("description", searchRequestOverhead.getOverheadDescription(), MatchMode.ANYWHERE));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findOverheadNameToSearchOverhead(final String name) {
        final List<Overhead> overhead = overheadRepository.findByNameContainingIgnoreCase("%" + name + "%");
        final List<String> results = new ArrayList<String>();
        for (final Overhead details : overhead)
            results.add(details.getName());
        return results;
    }

    public List<Overhead> searchOverheadToView(final SearchRequestOverhead searchRequestOverhead) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Overhead.class)
                .addOrder(Order.asc("createdDate"));
        if (searchRequestOverhead != null) {
            if (searchRequestOverhead.getOverheadName() != null)
                criteria.add(Restrictions.eq("name", searchRequestOverhead.getOverheadName()).ignoreCase());
            if (searchRequestOverhead.getOverheadDescription() != null)
                criteria.add(
                        Restrictions.ilike("description", searchRequestOverhead.getOverheadDescription(), MatchMode.ANYWHERE));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public void createOverheadValues(final Overhead overhead) {
        OverheadRate overheadRates = null;
        overhead.getOverheadRates().clear();
        for (final OverheadRate overheadRate : overhead.getTempOverheadRateValues()) {
            overheadRates = new OverheadRate();
            overheadRates.setLumpsumAmount(overheadRate.getLumpsumAmount());
            overheadRates.setPercentage(overheadRate.getPercentage());
            overheadRates.setValidity(overheadRate.getValidity());
            overheadRates.setOverhead(overhead);
            overhead.getOverheadRates().add(overheadRates);
        }
    }

}
