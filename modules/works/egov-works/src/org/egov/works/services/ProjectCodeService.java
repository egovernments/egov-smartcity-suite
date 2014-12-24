package org.egov.works.services;

import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.estimate.ProjectCode;

public class ProjectCodeService extends PersistenceService<ProjectCode, Long> implements EntityTypeService{
	public List<ProjectCode> getAllActiveEntities(Integer accountDetailTypeId) {
		return findAllBy("from ProjectCode where isActive=1");
	}
	
	public List<ProjectCode> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		String param = "%" + filterKey.toUpperCase() + "%";
		String qry = "select distinct pc from ProjectCode pc " +
				"where isActive=1 and upper(pc.code) like ? " +
				"order by code";
		return (List<ProjectCode>) findPageBy(qry, 0, pageSize,param).getList();
	}
}
