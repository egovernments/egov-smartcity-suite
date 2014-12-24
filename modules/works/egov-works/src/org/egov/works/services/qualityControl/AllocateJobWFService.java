package org.egov.works.services.qualityControl;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.qualityControl.JobHeader;

public class AllocateJobWFService extends PersistenceService<JobHeader,Long>{
	private EmployeeService employeeService;
	public AllocateJobWFService(){
		setType(JobHeader.class);
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}
