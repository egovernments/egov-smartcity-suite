package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.tender.WorksPackage;

public class WorkspackageWFService  extends PersistenceService<WorksPackage,Long> {
	private EmployeeService employeeService;
	public WorkspackageWFService(){
		setType(WorksPackage.class);
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
