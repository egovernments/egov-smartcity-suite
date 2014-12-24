package org.egov.works.models.contractorBill;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class ContractCertificateNumberGenerator {
	
	public static final String SEQUENCE_TYPE="CONTRACTCERTIFICATE";
	private SequenceGenerator sequenceGenerator; 
	private PersistenceService<Script, Long> scriptService;
	private ScriptService scriptExecutionService;
	
	public String getContractCertificateNumber(CFinancialYear financialYear,WorkOrderEstimate workOrderEstimate){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.contractCertificateNumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("workOrderEstimate", workOrderEstimate, "finYear",financialYear,"sequenceGenerator",sequenceGenerator));
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
