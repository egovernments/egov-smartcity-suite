package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.securityDeposit.ReturnSecurityDeposit;

public class ReturnSecurityDepositWFService extends PersistenceService<ReturnSecurityDeposit,Long>{
	private EmployeeService employeeService;
	
	public ReturnSecurityDepositWFService(){
		setType(ReturnSecurityDeposit.class);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}

