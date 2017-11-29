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

package org.egov.commons.service;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Bank;
import org.egov.commons.Relation;
import org.egov.commons.repository.RelationRepository;
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
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RelationJpaService {

	private final RelationRepository relationRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	AccountDetailKeyService accountDetailKeyService;
	
	@Autowired
	AccountdetailtypeService accountdetailtypeService;
	@Autowired
	public RelationJpaService(final RelationRepository relationRepository) {
		this.relationRepository = relationRepository;
	}

	@Transactional
	public Relation create(Relation relation) {
		 relation =relationRepository.save(relation);
		 
		 Accountdetailkey ac=new Accountdetailkey();
		 ac.setDetailkey(relation.getId());
		 ac.setDetailname(relation.getName());
		 ac.setGroupid(1);
		 ac.setAccountdetailtype(accountdetailtypeService.findByName("Supplier"));
		 accountDetailKeyService.create(ac);
		 
		 return relation;
	}

	@Transactional
	public Relation update(final Relation relation) {
		//this code is to handle non jpa object save since it dont have version once moved bank to jpa remove this
		if(relation.getBank()!=null && relation.getBank().getId()!=null)
		{
		Integer id = relation.getBank().getId();
		entityManager.detach(relation.getBank());
		Bank bank = entityManager.find(Bank.class, id);
		relation.setBank(bank);
		}
		else
		{
			relation.setBank(null);
		}
		return relationRepository.save(relation);
	}

	public List<Relation> findAll() {
		return relationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Relation findByName(String name) {
		return relationRepository.findByName(name);
	}

	public Relation findByCode(String code) {
		return relationRepository.findByCode(code);
	}

	public Relation findOne(Integer id) {
		return relationRepository.findOne(id);
	}

	public List<Relation> search(Relation relation) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Relation> createQuery = cb.createQuery(Relation.class);
		Root<Relation> relations = createQuery.from(Relation.class);
		createQuery.select(relations);
		Metamodel m = entityManager.getMetamodel();
		EntityType<Relation> Relation_ = m.entity(Relation.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(relation.getName()!=null)
		{
		String name="%"+relation.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(relations.get("name")));
		predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(relation.getCode()!=null)
		{
		String code="%"+relation.getCode().toLowerCase()+"%";
		predicates.add(cb.isNotNull(relations.get("code")));
		predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(relation.getMobile()!=null )
		{
			String mobile=relation.getMobile();
			predicates.add(cb.isNotNull(relations.get("mobile")));
			predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("mobile", String.class))),mobile));
		}
		
		if(relation.getPanno()!=null )
		{
			String panno=relation.getPanno();
			predicates.add(cb.isNotNull(relations.get("panno")));
			predicates.add(cb.like(cb.lower(relations.get(Relation_.getDeclaredSingularAttribute("panno", String.class))),panno));
		}
		
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<Relation> query=entityManager.createQuery(createQuery);
		List<Relation> resultList = query.getResultList();
		return resultList;
		
	} 
	
}