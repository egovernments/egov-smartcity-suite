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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CommitteeMembers;
import org.egov.council.entity.CommitteeType;
import org.egov.council.entity.CouncilMemberStatus;
import org.egov.council.repository.CouncilCommitteeMemberRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilCommitteeMemberService {

    private final CouncilCommitteeMemberRepository councilCommitteeMemberRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CouncilCommitteeMemberService(final CouncilCommitteeMemberRepository councilCommitteeMemberRepository) {
        this.councilCommitteeMemberRepository = councilCommitteeMemberRepository;
    }
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public CommitteeMembers create(final CommitteeMembers committeeMember) {
        return councilCommitteeMemberRepository.save(committeeMember);
    }

    @Transactional
    public CommitteeMembers update(final CommitteeMembers committeeMember) {
        return councilCommitteeMemberRepository.save(committeeMember);
    }

    public List<CommitteeMembers> findAll() {
        return councilCommitteeMemberRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
    
    @SuppressWarnings("unchecked")
    public List<CommitteeMembers> findAllByCommitteTypeMemberIsActive(CommitteeType committeeType) {
        final Criteria criteria = getCurrentSession().createCriteria(CommitteeMembers.class, "committeeMembers")
                .createAlias("committeeMembers.councilMember", "member");
        criteria.add(Restrictions.eq("committeeType", committeeType));
        criteria.add(Restrictions.eq("member.status", CouncilMemberStatus.ACTIVE));
        return criteria.list();
    }
    
    public List<CommitteeMembers> findAllByCommitteType(CommitteeType committeeType) {
        return councilCommitteeMemberRepository.findByCommitteeType(committeeType);
    }
    public CommitteeMembers findOne(Long id) {
        return councilCommitteeMemberRepository.findOne(id);
    }
    
    @Transactional
    public void delete(final List<CommitteeMembers> committeeMembers) {
        councilCommitteeMemberRepository.deleteInBatch(committeeMembers);
    }
    

   

}
