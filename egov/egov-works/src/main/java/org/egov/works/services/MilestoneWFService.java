package org.egov.works.services;

import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.milestone.Milestone;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vikas
*/


public class MilestoneWFService extends PersistenceService<Milestone,Long> {
    
        @Autowired
        private EmployeeService employeeService;        
        @Autowired
        private CommonsService commonsService;
        
	public MilestoneWFService(){
		setType(Milestone.class);
	}
	
}