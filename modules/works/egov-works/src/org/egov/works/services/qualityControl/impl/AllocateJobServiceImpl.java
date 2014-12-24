package org.egov.works.services.qualityControl.impl;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.qualityControl.JobHeader;
import org.egov.works.models.qualityControl.JobNumberGenerator;
import org.egov.works.services.impl.BaseServiceImpl;
import org.egov.works.services.qualityControl.AllocateJobService;

public class AllocateJobServiceImpl extends BaseServiceImpl<JobHeader, Long> implements AllocateJobService{
	
	private CommonsService commonsService;
	private JobNumberGenerator jobNumberGenerator;

	public AllocateJobServiceImpl(
			PersistenceService<JobHeader, Long> persistenceService) {
		super(persistenceService);
		// TODO Auto-generated constructor stub
	}
	
	public String generateJobNumber(JobHeader jobHeader){
		CFinancialYear financialYear = commonsService.getFinancialYearByDate(jobHeader.getJobDate());
		return jobNumberGenerator.getJobNumber(jobHeader, financialYear);
	}
	

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setJobNumberGenerator(JobNumberGenerator jobNumberGenerator) {
		this.jobNumberGenerator = jobNumberGenerator;
	}

	


}
