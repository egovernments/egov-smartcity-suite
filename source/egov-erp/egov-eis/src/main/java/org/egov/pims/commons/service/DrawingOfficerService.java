package org.egov.pims.commons.service;

import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.DrawingOfficer;

public class DrawingOfficerService extends PersistenceService<DrawingOfficer, Long> implements
		EntityTypeService {

	@Override
	public List<? extends EntityType> filterActiveEntities(String filterKey,
			int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		return findPageByNamedQuery(DrawingOfficer.QRY_DO_STARTSWITH, 0, pageSize, filterKey + "%").getList();
	      
	}

	@Override
	public List<? extends EntityType> getAllActiveEntities(
			Integer accountDetailTypeId) {
		return findAll("code");
	}

}
