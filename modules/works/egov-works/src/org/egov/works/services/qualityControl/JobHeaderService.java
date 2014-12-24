package org.egov.works.services.qualityControl;

import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.qualityControl.JobHeader;

public class JobHeaderService extends PersistenceService<JobHeader, Long> implements EntityTypeService{
	@Override
	public List<JobHeader> getAllActiveEntities(Integer accountDetailTypeId) {
		return findAllBy("from JobHeader where egwStatus.code='APPROVED'");
	}

	@Override
	public List<JobHeader> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		String param = "%" + filterKey.toUpperCase() + "%";
		String qry = "from JobHeader " +
				"where egwStatus.code='APPROVED' and upper(jobNumber) like ? ";
		return (List<JobHeader>) findPageBy(qry, 0, pageSize,param).getList();
	}
}
