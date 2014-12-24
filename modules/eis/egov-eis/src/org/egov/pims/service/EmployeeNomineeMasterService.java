package org.egov.pims.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.EmployeeNomineeMaster;
/**
 * 
 * @author Surya
 *
 */
public class EmployeeNomineeMasterService extends PersistenceService<EmployeeNomineeMaster, Integer> implements EntityTypeService 
{

	@Override
	public List<EntityType> filterActiveEntities(String filterKey,int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		List<EntityType> entities=new ArrayList<EntityType>();
		Page pg = findPageByNamedQuery("ACTIVE_NOMINEES_STARTSWITH", 0, pageSize,filterKey + "%" ,filterKey + "%");
		entities.addAll(pg.getList());
	    return entities;
	}

	@Override
	public List<EntityType> getAllActiveEntities(Integer accountDetailTypeId) {
		 List<EntityType> entities = new ArrayList<EntityType>();
	     entities.addAll(findAllByNamedQuery("ACTIVE_NOMINEES"));
	     return entities;
	}
	

}
