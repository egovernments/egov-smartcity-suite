package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.tender.WorksPackage;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkspackageWFService  extends PersistenceService<WorksPackage,Long> {
        
        @Autowired
        private EmployeeService employeeService;
        
	public WorkspackageWFService(){
		setType(WorksPackage.class);
	}
	
}
