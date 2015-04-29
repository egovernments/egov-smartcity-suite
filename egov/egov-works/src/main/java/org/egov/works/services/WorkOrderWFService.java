package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.workorder.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkOrderWFService  extends PersistenceService<WorkOrder,Long> {
        
        @Autowired
        private EmployeeService employeeService;  
        
	public WorkOrderWFService(){
		setType(WorkOrder.class);
	}
	
}
