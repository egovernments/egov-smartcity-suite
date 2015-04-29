package org.egov.works.models.tender;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;

public class WorksPackageNumberGenerator {
    @Autowired
    private SequenceGenerator sequenceGenerator;
    //private PersistenceService<Script, Long> scriptService;
    @Autowired
    private ScriptService scriptService;
	
	public String getWorksPackageNumber(WorksPackage entity,CFinancialYear finYear){
	    try{
                ScriptContext scriptContext = ScriptService.createContext("worksPackage",entity,"finYear",finYear,"sequenceGenerator",sequenceGenerator);
                return scriptService.executeScript("works.wpNumber.generator", scriptContext).toString();
            }
            catch (ValidationException sequenceException) {
                throw sequenceException;
            }
		//List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.wpNumber.generator");
		//return scripts.get(0).eval(Script.createContext("worksPackage",entity,"finYear",finYear,"sequenceGenerator",sequenceGenerator)).toString();
	}

}
