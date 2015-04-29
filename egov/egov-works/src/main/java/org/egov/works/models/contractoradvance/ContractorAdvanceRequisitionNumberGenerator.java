package org.egov.works.models.contractoradvance;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractorAdvanceRequisitionNumberGenerator {
	public static final String SEQUENCE_TYPE="CONTRACTOR_ARF";
	 @Autowired
	 private SequenceGenerator sequenceGenerator;
	 //private PersistenceService<Script, Long> scriptService;
	 @Autowired
	 private ScriptService scriptService;
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public String getARFNumber(ContractorAdvanceRequisition contractorAdvanceRequisition,CFinancialYear financialYear, PersistenceService persistenceService){
	    ScriptContext scriptContext = ScriptService.createContext("contractorAdvanceRequisition",contractorAdvanceRequisition,"finYear",financialYear,"sequenceGenerator",sequenceGenerator,"persistenceService",persistenceService);
            return scriptService.executeScript("works.contractor.arfnumber.generator", scriptContext).toString();
       
	    
	    /*List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.contractor.arfnumber.generator");
		try{
			return scripts.get(0).eval(Script.createContext("contractorAdvanceRequisition",contractorAdvanceRequisition,"finYear",financialYear,"sequenceGenerator",sequenceGenerator,"persistenceService",persistenceService)).toString();
		}
		catch (ValidationException sequenceException) {
			throw sequenceException;
		}*/

	}

}