package org.egov.works.services.qualityControl;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.qualityControl.TestResultHeader;

public class TestResultWFService extends PersistenceService<TestResultHeader,Long>{
	private EmployeeService employeeService;
	public TestResultWFService(){
		setType(TestResultHeader.class);
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}
