package org.egov.works.models.estimate;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class BudgetNumberGenerator {
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public String getBudgetApprNo(AbstractEstimate estimate,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.budgetappno.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}
	
	public PersistenceService<Script, Long> getScriptService() {
		return scriptService;
	}

	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
