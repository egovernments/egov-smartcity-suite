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
package org.egov.assets.service;


import org.egov.assets.model.CategoryPropertyType;
import org.egov.assets.repository.CategoryPropertyTypeRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service 
@Transactional(readOnly = true)
public class CategoryPropertyTypeService  {

	private final CategoryPropertyTypeRepository categoryPropertyTypeRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public CategoryPropertyTypeService(final CategoryPropertyTypeRepository categoryPropertyTypeRepository) {
		this.categoryPropertyTypeRepository = categoryPropertyTypeRepository;
	}

	@Transactional
	public CategoryPropertyType create(final CategoryPropertyType categoryPropertyType) {
		return categoryPropertyTypeRepository.save(categoryPropertyType);
	} 
	@Transactional
	public CategoryPropertyType update(final CategoryPropertyType categoryPropertyType) {
		return categoryPropertyTypeRepository.save(categoryPropertyType);
	} 
	public List<CategoryPropertyType> findAll() {
		return categoryPropertyTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public CategoryPropertyType findByName(String name){
		return categoryPropertyTypeRepository.findByName(name);
	}
	public CategoryPropertyType findOne(Long id){
		return categoryPropertyTypeRepository.findOne(id);
	}
	public List<CategoryPropertyType> search(CategoryPropertyType categoryPropertyType){
		return categoryPropertyTypeRepository.findAll();
	}
	public void delete(Long id){
		categoryPropertyTypeRepository.delete(id);
	}

	/**
	 * 
	 * @param masterName
	 * @param queryText
	 * @return
	 * Currently Implemented to fetch from entity only.
	 * Should be enhanced to get from corresponding service .
	 */
	public  List<?> getMasterData(CategoryPropertyType category,String queryText){
		Session session = entityManager.unwrap(Session.class);
		Query createQuery = session.createQuery("from "+category.getEnumValues() +" where Lower(name) like '%"+queryText.toLowerCase()+"%'");
		createQuery.setMaxResults(20);
		return createQuery.list();
	}
}