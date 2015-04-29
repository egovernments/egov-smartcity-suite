package org.egov.works.models.estimate;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class BudgetNumberGenerator {
    
    @Autowired
    private SequenceGenerator sequenceGenerator;
    //private PersistenceService<Script, Long> scriptService;
    @Autowired
    private ScriptService scriptService;
		
	public String getBudgetApprNo(AbstractEstimate estimate,CFinancialYear financialYear){
	    try{
	        ScriptContext scriptContext = ScriptService.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator);
	        return scriptService.executeScript("works.budgetappno.generator", scriptContext).toString();
	    }
            catch (ValidationException sequenceException) {
               throw sequenceException;
            }
		/*List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.budgetappno.generator");
		try{
		return scripts.get(0).eval(Script.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator)).toString();
		}
		catch (ValidationException sequenceException) {
		   throw sequenceException;
		}*/
		
	}
	
}
