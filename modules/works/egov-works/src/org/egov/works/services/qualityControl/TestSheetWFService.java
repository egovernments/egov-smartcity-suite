package org.egov.works.services.qualityControl;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.qualityControl.TestSheetHeader;

public class TestSheetWFService extends PersistenceService<TestSheetHeader,Long>{
	
	private EmployeeService employeeService;
	
	public TestSheetWFService(){
		setType(TestSheetHeader.class);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}
