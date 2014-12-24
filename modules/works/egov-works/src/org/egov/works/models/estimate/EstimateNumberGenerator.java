package org.egov.works.models.estimate;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class EstimateNumberGenerator {
	public static final String SEQUENCE_TYPE="ABSTRACTESTIMATE";
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public String getEstimateNumber(AbstractEstimate estimate,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.estimatenumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("estimate",estimate,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}

	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}

	public void discardNumber() {
		sequenceGenerator.discardNumber(SEQUENCE_TYPE);
	}

	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
