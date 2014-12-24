package org.egov.works.services.qualityControl;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.qualityControl.SampleLetterHeader;

public class SampleLetterWFService extends PersistenceService<SampleLetterHeader,Long>{
	private EmployeeService employeeService;
	public SampleLetterWFService(){
		setType(SampleLetterHeader.class);
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
