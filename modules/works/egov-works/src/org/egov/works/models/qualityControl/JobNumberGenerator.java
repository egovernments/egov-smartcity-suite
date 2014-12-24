package org.egov.works.models.qualityControl;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class JobNumberGenerator {
	
	public static final String SEQUENCE_TYPE="JOBHEADER";
	private SequenceGenerator sequenceGenerator; 
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	
	public String getJobNumber(JobHeader jobHeader,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.jobNumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("jobHeader",jobHeader,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	public void setScriptService(PersistenceService<Script, Long> scriptService) {
		this.scriptService = scriptService;
	}
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

}
