package org.egov.works.models.estimate;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class TechnicalSanctionNumberGenerator {
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	public void setScriptService(PersistenceService<Script, Long> scriptService) {
		this.scriptService = scriptService;
	}
	
	public String getTechnicalSanctionNumber(AbstractEstimate estimate,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.techsanctionnumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}
	
	public String getTechnicalSanctionNumberForRE(AbstractEstimate estimate,CFinancialYear financialYear){ 
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.revisionEstimate.techsanctionnumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
 