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

package org.egov.council.service;

import org.egov.council.entity.CouncilMember;
import org.egov.council.entity.CouncilMemberStatus;
import org.egov.council.repository.CouncilMemberRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CouncilMemberService {

    private final CouncilMemberRepository councilMemberRepository;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    public CouncilMemberService(final CouncilMemberRepository councilMemberRepository) {
        this.councilMemberRepository = councilMemberRepository;
    }
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public CouncilMember create(final CouncilMember councilMember) {
        return councilMemberRepository.save(councilMember);
    }

    @Transactional
    public CouncilMember update(final CouncilMember councilMember) {
        return councilMemberRepository.save(councilMember);
    }

    public List<CouncilMember> findAll() {
        return councilMemberRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
    
    

    public CouncilMember findOne(Long id) {
        return councilMemberRepository.findById(id);
    }
    
	@SuppressWarnings("unchecked")
    public List<CouncilMember> search(CouncilMember councilMember) {
		final Criteria criteria = getCurrentSession().createCriteria(
				CouncilMember.class);
		if (null != councilMember.getElectionWard())
			criteria.add(Restrictions.eq("electionWard",
					councilMember.getElectionWard()));
		if (null != councilMember.getDesignation())
			criteria.add(Restrictions.eq("designation",
					councilMember.getDesignation()));
		if (null != councilMember.getPartyAffiliation())
			criteria.add(Restrictions.eq("partyAffiliation",
					councilMember.getPartyAffiliation()));
		if (null != councilMember.getName())
			criteria.add(Restrictions.ilike("name", councilMember.getName(), MatchMode.ANYWHERE));
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
    public List<CouncilMember> findAllByActive() {
            final Criteria criteria = getCurrentSession().createCriteria(
                            CouncilMember.class);
            criteria.add(Restrictions.eq("status",CouncilMemberStatus.ACTIVE));
            criteria.addOrder(Order.asc("name"));
            return criteria.list();
	}
}