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

import static org.egov.council.utils.constants.CouncilConstants.ADJOURNED;
import static org.egov.council.utils.constants.CouncilConstants.APPROVED;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.council.entity.CouncilAgenda;
import org.egov.council.entity.CouncilAgendaDetails;
import org.egov.council.repository.CouncilAgendaDetailsRepository;
import org.egov.council.repository.CouncilAgendaRepository;
import org.egov.infra.utils.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouncilAgendaService {
	
	private final CouncilAgendaDetailsRepository councilAgendaDetailsRepository;
    private final CouncilAgendaRepository councilAgendaRepository;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    public CouncilAgendaService(final CouncilAgendaRepository councilAgendaRepository,final CouncilAgendaDetailsRepository councilAgendaDetailsRepository) {
        this.councilAgendaRepository = councilAgendaRepository;
        this.councilAgendaDetailsRepository= councilAgendaDetailsRepository;
    }
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public CouncilAgenda create(final CouncilAgenda councilAgenda) {
        return councilAgendaRepository.save(councilAgenda);
    }

    @Transactional
    public CouncilAgenda update(final CouncilAgenda councilAgenda) {
        return councilAgendaRepository.save(councilAgenda);
    }
    
    @Transactional
    public void deleteAllInBatch(List<CouncilAgendaDetails> existingPreambleList) {
		councilAgendaDetailsRepository.deleteInBatch(existingPreambleList);
		
	}
    
    public List<CouncilAgenda> findAll() {
        return councilAgendaRepository.findAll(new Sort(Sort.Direction.DESC, "agendaNumber"));
    }

    public CouncilAgenda findOne(Long id) {
        return councilAgendaRepository.findById(id);
    }

    @SuppressWarnings("unchecked")
    public List<CouncilAgenda> search(CouncilAgenda councilAgenda) {
        return buildSearchCriteria(councilAgenda).list();
    }
    
    @SuppressWarnings("unchecked")
    public List<CouncilAgenda> searchForAgendaToCreateMeeting(CouncilAgenda councilAgenda) {
        return buildSearchCriteria(councilAgenda).add(Restrictions.in("status.code", new String[] {APPROVED, ADJOURNED})).list();
    }
    
    public Criteria buildSearchCriteria(CouncilAgenda councilAgenda){
        final Criteria criteria = getCurrentSession().createCriteria(CouncilAgenda.class,"councilAgenda").createAlias("councilAgenda.status", "status");
        if(null != councilAgenda.getStatus())
            criteria.add(Restrictions.eq("status", councilAgenda.getStatus().getCode()));
    if(null != councilAgenda.getCommitteeType())
            criteria.add(Restrictions.eq("committeeType", councilAgenda.getCommitteeType()));
    if (councilAgenda.getFromDate() != null && councilAgenda.getToDate() != null) {
        criteria.add(Restrictions.between("councilAgenda.createdDate", councilAgenda.getFromDate(),DateUtils.addDays(councilAgenda.getToDate(),1)));
    }
    if (null != councilAgenda.getAgendaNumber())
        criteria.add(Restrictions.ilike("councilAgenda.agendaNumber", councilAgenda.getAgendaNumber(),MatchMode.ANYWHERE));

    return criteria;
    }

}