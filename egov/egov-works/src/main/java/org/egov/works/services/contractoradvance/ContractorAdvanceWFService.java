package org.egov.works.services.contractoradvance;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractorAdvanceWFService  extends PersistenceService<ContractorAdvanceRequisition,Long> {
        @Autowired
        private EmployeeService employeeService;  
        
	public ContractorAdvanceWFService(){
		setType(ContractorAdvanceRequisition.class);
	}
	

}
