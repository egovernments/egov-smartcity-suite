package org.egov.works.models.tender;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class TenderFileNumberGenerator {
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public String getTenderFileNumber(TenderFile entity,CFinancialYear finYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.tenderFileNumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("tenderFile",entity,"finYear",finYear, "sequenceGenerator",sequenceGenerator, "month", Integer.toString(entity.getFileDate().getMonth()+1)));
	}

	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
