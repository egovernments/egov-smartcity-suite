package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.workorder.WorkCompletionDetail;

public class WorkCompletionDetailWFService extends PersistenceService<WorkCompletionDetail,Long>{
	
	private EmployeeService employeeService;
	
	public WorkCompletionDetailWFService(){
		setType(WorkCompletionDetail.class);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}
