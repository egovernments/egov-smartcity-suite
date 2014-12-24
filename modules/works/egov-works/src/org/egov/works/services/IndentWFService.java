package org.egov.works.services;

import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.rateContract.Indent;

public class IndentWFService  extends PersistenceService<Indent,Long> {
	private EmployeeService employeeService;
	private CommonsService commonsService;
	private IndentRateContractService indentRateContractService;
	public IndentWFService(){
		setType(Indent.class);
	}
		
	public EmployeeService getEmployeeService() {
		return employeeService;
	}


	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	public CommonsService getcommonsService() {
		return commonsService;
	}

	public IndentRateContractService getIndentRateContractService() {
		return indentRateContractService;
	}

	public void setIndentRateContractService(
			IndentRateContractService indentRateContractService) {
		this.indentRateContractService = indentRateContractService;
	}
}
