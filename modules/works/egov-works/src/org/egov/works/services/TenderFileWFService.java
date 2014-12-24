package org.egov.works.services;

import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.tender.TenderFile;

public class TenderFileWFService  extends PersistenceService<TenderFile,Long> {
	private EmployeeService employeeService;
	private CommonsService commonsService;
	public TenderFileWFService(){
		setType(TenderFile.class);
	}
		
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
