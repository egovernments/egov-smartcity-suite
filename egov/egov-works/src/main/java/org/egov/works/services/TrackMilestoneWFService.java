package org.egov.works.services;

import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.milestone.TrackMilestone;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vikas
*/


public class TrackMilestoneWFService extends PersistenceService<TrackMilestone,Long> {
        
        @Autowired
        private EmployeeService employeeService;        
        @Autowired
        private CommonsService commonsService;
        
	public TrackMilestoneWFService(){
		setType(TrackMilestone.class);
	}
	
}