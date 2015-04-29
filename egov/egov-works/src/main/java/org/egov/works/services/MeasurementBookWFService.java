package org.egov.works.services;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.measurementbook.MBHeader;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasurementBookWFService  extends PersistenceService<MBHeader,Long> {
	private static final Logger logger = Logger.getLogger(MeasurementBookWFService.class);

        @Autowired
        private EmployeeService employeeService;
        
	public MeasurementBookWFService(){
		logger.info("inside negotiationservice");
		setType(MBHeader.class);
	}
}
