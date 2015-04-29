package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractorBillWFService  extends PersistenceService<ContractorBillRegister,Long> {
	
        @Autowired
        private EmployeeService employeeService;
	
	public ContractorBillWFService(){
		setType(ContractorBillRegister.class);
	}

}
