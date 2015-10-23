package org.egov.works.master.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.common.entity.UOM;
import org.egov.infstr.services.PersistenceService;

public class UOMService extends PersistenceService<UOM, Long> {

	@PersistenceContext
    private EntityManager entityManager;
	
	public UOM getUOMById(Long uomId) {
		UOM uom = (UOM) entityManager.find(UOM.class, uomId);
		return uom;
	}

	public List<UOM> getAllUOMs() {
		final Query query = entityManager.createQuery("from UOM  order by upper(uom)");
		List<UOM> uomList = (List<UOM>) query.getResultList();
		return uomList;
	}
}
