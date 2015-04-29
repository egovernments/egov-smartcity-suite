package org.egov.works.models.estimate;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class EstimateNumberGenerator {
	public static final String SEQUENCE_TYPE="ABSTRACTESTIMATE";
	 @Autowired
         private SequenceGenerator sequenceGenerator;
         //private PersistenceService<Script, Long> scriptService;
         @Autowired
         private ScriptService scriptService;
	
	public String getEstimateNumber(AbstractEstimate estimate,CFinancialYear financialYear){
	    try{
	        ScriptContext scriptContext = ScriptService.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator);
                return scriptService.executeScript("works.estimatenumber.generator", scriptContext).toString();
	    }
            catch (ValidationException sequenceException) {
                throw sequenceException;
            }
	        /*List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.estimatenumber.generator");
		try{
		return scripts.get(0).eval(Script.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator)).toString();
		}
		catch (ValidationException sequenceException) {
			throw sequenceException;
		}*/

	}

}
