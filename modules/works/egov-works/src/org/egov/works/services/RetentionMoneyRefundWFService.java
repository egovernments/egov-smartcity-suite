package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.retentionMoney.RetentionMoneyRefund;

public class RetentionMoneyRefundWFService extends PersistenceService<RetentionMoneyRefund,Long>{
	private EmployeeService employeeService;
	
	public RetentionMoneyRefundWFService(){
		setType(RetentionMoneyRefund.class);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
}

