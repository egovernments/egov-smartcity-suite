package org.egov.works.services;

import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.masters.MilestoneTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vikas
*/


public class MilestoneTemplateWFService extends PersistenceService<MilestoneTemplate,Long> {

        @Autowired
        private EmployeeService employeeService;        
        @Autowired
        private CommonsService commonsService;
        
	public MilestoneTemplateWFService(){
		setType(MilestoneTemplate.class);
	}
	
}