package org.egov.works.services.qualityControl;

import org.egov.works.models.qualityControl.JobHeader;
import org.egov.works.services.BaseService;

public interface AllocateJobService  extends BaseService<JobHeader,Long>{
	public String generateJobNumber(JobHeader jobHeader);
	
}
