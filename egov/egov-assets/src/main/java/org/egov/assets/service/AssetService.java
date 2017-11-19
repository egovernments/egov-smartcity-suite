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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.egov.assets.model.Asset;
import org.egov.assets.repository.AssetRepository;
import org.egov.assets.util.AssetCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AssetService {

	private final AssetRepository assetRepository;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AssetCommonUtil assetCommonUtil;

	@Autowired
	public AssetService(final AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}

	@Transactional
	public Asset create(final Asset asset) {
		if(asset.getCategoryProperties() != null)
		{
			String serialize = assetCommonUtil.serialize(asset.getCategoryProperties());
			asset.setProperties(serialize);
		}
		return assetRepository.save(asset);
	}

	@Transactional
	public Asset update(final Asset asset) {
		if(asset.getCategoryProperties() != null)
		{
			String serialize = assetCommonUtil.serialize(asset.getCategoryProperties());
			asset.setProperties(serialize);
		}
		return assetRepository.save(asset);
	}

	public List<Asset> findAll() {
		return assetRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Asset findByName(String name) {
		return assetRepository.findByName(name);
	}

	public Asset findByCode(String code) {
		return assetRepository.findByCode(code);
	}

	public Asset findOne(Long id) {
		return assetRepository.findOne(id);
	}

	public List<Asset> search(Asset asset) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Asset> createQuery = cb.createQuery(Asset.class);
		Root<Asset> assets = createQuery.from(Asset.class);
		createQuery.select(assets);
		Metamodel model = entityManager.getMetamodel();
		EntityType<Asset> Asset_ = model.entity(Asset.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(asset.getName()!=null)
		{
			String name="%"+asset.getName().toLowerCase()+"%";
			predicates.add(cb.isNotNull(assets.get("name")));
			predicates.add(cb.like(cb.lower(assets.get(Asset_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(asset.getCode()!=null)
		{
			String code="%"+asset.getCode().toLowerCase()+"%";
			predicates.add(cb.isNotNull(assets.get("code")));
			predicates.add(cb.like(cb.lower(assets.get(Asset_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(asset.getAssetCategory() != null)
		{
			predicates.add(cb.equal(assets.get("assetCategory"), asset.getAssetCategory()));
		}
		if(asset.getDepartment() != null)
		{
			predicates.add(cb.equal(assets.get("department"),asset.getDepartment()));
		}
		if(asset.getSearchStatus() != null)
		{
			Expression<Long> status = assets.get("status");
			predicates.add(status.in(asset.getSearchStatus()));
		}
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<Asset> query=entityManager.createQuery(createQuery);
		List<Asset> resultList = query.getResultList();
		
		return resultList;
	}
}