package org.egov.works.master.services;

import org.egov.common.entity.UOM;
import org.egov.infstr.services.PersistenceService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class UOMService extends PersistenceService<UOM, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    public UOM getUOMById(final Long uomId) {
        final UOM uom = entityManager.find(UOM.class, uomId);
        return uom;
    }

    public List<UOM> getAllUOMs() {
        final Query query = entityManager.createQuery("from UOM  order by upper(uom)");
        final List<UOM> uomList = query.getResultList();
        return uomList;
    }
}
