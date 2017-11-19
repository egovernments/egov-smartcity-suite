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

import org.egov.assets.model.AssetCategory;
import org.egov.assets.model.CategoryPropertyType;
import org.egov.assets.repository.AssetCategoryRepository;
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
import java.util.Iterator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssetCategoryService {

	private final AssetCategoryRepository assetCategoryRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public AssetCategoryService(
			final AssetCategoryRepository assetCategoryRepository) {
		this.assetCategoryRepository = assetCategoryRepository;
	}

	@Transactional
	public AssetCategory create(AssetCategory assetCategory) {
		assetCategory = removeEmptyCategoryproperties(assetCategory);
		return assetCategoryRepository.save(assetCategory);
	}

	@Transactional
	public AssetCategory update(AssetCategory assetCategory) {
		assetCategory = removeEmptyCategoryproperties(assetCategory);
		return assetCategoryRepository.save(assetCategory);
	}

	public List<AssetCategory> findAll() {
		return assetCategoryRepository.findAll(new Sort(Sort.Direction.ASC,
				"name"));
	}

	public AssetCategory findByName(String name) {
		return assetCategoryRepository.findByName(name);
	}

	public AssetCategory findByCode(String code) {
		return assetCategoryRepository.findByCode(code);
	}

	public AssetCategory findOne(Long id) {
		return assetCategoryRepository.findOne(id);
	}

	public List<AssetCategory> search(AssetCategory assetCategory) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssetCategory> createQuery = cb.createQuery(AssetCategory.class);
		Root<AssetCategory> assetCategories = createQuery.from(AssetCategory.class);
		createQuery.select(assetCategories);
		Metamodel model = entityManager.getMetamodel();
		EntityType<AssetCategory> AssetCategory_ = model.entity(AssetCategory.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(assetCategory.getName() != null)
		{
			String name="%"+assetCategory.getName().toLowerCase()+"%";
			predicates.add(cb.isNotNull(assetCategories.get("name")));
			predicates.add(cb.like(cb.lower(assetCategories.get(AssetCategory_.getDeclaredSingularAttribute("name",String.class))),name));
		}
		if(assetCategory.getAssetType() != null)
		{
			predicates.add(cb.equal(assetCategories.get("assetType"),assetCategory.getAssetType()));
		}
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<AssetCategory> query=entityManager.createQuery(createQuery);
		List<AssetCategory> resultList = query.getResultList();
		return resultList;
	}
	
	private AssetCategory removeEmptyCategoryproperties(AssetCategory assetCategory){
		Iterator<CategoryPropertyType> iterator = assetCategory.getCategoryProperties().iterator();
		while(iterator.hasNext()){
			CategoryPropertyType categoryProperty = iterator.next();
			if((categoryProperty.getName() == null) && (categoryProperty.getDataType() == null)
					&& (categoryProperty.getFormat() == null)  && (categoryProperty.getEnumValues() == null)
					&& (categoryProperty.getLocalText() == null))
			{
				iterator.remove();
			}
			else if(categoryProperty.getAssetCategory()==null)
			{
				categoryProperty.setAssetCategory(assetCategory);
			}
		}
		return assetCategory;
	}
}