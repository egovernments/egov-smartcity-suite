package org.egov.works.models.tender;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.works.models.estimate.AbstractEstimate;
import org.springframework.beans.factory.annotation.Autowired;

public class NegotiationNumberGenerator {
        @Autowired
        private SequenceGenerator sequenceGenerator;
        //private PersistenceService<Script, Long> scriptService;
        @Autowired
        private ScriptService scriptService;
	
	public String getTenderNegotiationNumber(AbstractEstimate abstractEstimate, CFinancialYear financialYear){
	    try{
                ScriptContext scriptContext = ScriptService.createContext("estimate",abstractEstimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator);
                return scriptService.executeScript("works.negotiationNumber.generator", scriptContext).toString();
            }
            catch (ValidationException sequenceException) {
                throw sequenceException;
            }
		//List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.negotiationNumber.generator");
		//return scripts.get(0).eval(Script.createContext("estimate",abstractEstimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator)).toString();
	}

	public String getTenderNegotiationNumber(WorksPackage worksPackage, CFinancialYear financialYear){
	    try{
                ScriptContext scriptContext = ScriptService.createContext("worksPackage",worksPackage,"finYear",financialYear,"sequenceGenerator",sequenceGenerator);
                return scriptService.executeScript("workspackage.negotiationNumber.generator", scriptContext).toString();
            }
            catch (ValidationException sequenceException) {
                throw sequenceException;
            }
		//List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "workspackage.negotiationNumber.generator"); 
		//return scripts.get(0).eval(Script.createContext("worksPackage",worksPackage,"finYear",financialYear,"sequenceGenerator",sequenceGenerator)).toString();
	}	
}
