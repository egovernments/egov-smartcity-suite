package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.workorder.WorkOrder;

public class WorkOrderWFService  extends PersistenceService<WorkOrder,Long> {
	private EmployeeService employeeService;
	public WorkOrderWFService(){
		setType(WorkOrder.class);
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
