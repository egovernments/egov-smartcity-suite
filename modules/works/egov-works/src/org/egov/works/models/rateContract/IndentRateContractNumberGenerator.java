package org.egov.works.models.rateContract;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class IndentRateContractNumberGenerator {
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public String getIndentRateContractNumber(Indent entity, CFinancialYear finYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.indentRateContractNumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("indent",entity,"finYear",finYear, "sequenceGenerator",sequenceGenerator));
	}

	public String getBudgetApprNo(Indent entity,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.indent.budgetappno.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("indent",entity,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}
	
	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}