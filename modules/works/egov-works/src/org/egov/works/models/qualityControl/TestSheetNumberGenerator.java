package org.egov.works.models.qualityControl;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class TestSheetNumberGenerator {
	public static final String SEQUENCE_TYPE="TESTSHEETHEADER";
	private SequenceGenerator sequenceGenerator; 
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public String getTestSheetNumber(TestSheetHeader testSheetHeader,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.testsheetnumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("testSheetHeader",testSheetHeader,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public void setScriptService(PersistenceService<Script, Long> scriptService) {
		this.scriptService = scriptService;
	}
	
	public void discardNumber() {
		sequenceGenerator.discardNumber(SEQUENCE_TYPE);
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
