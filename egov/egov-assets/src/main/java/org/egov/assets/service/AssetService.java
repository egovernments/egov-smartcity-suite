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
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
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
	private EgwStatusHibernateDAO egwStatusHibernateDAO;

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