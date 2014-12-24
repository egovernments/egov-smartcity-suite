package org.egov.works.models.workorder;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.WorksPackage;

public class WorkOrderNumberGenerator {
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public String getWorkOrderNumberGenerator(AbstractEstimate abstractEstimate, CFinancialYear financialYear,WorksPackage worksPackage, WorkOrder workOrder, PersistenceService persistenceService){
		List<Script> scripts=null;
		if(worksPackage==null)
			scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.workOrderNumber.generator");
		else scripts = scriptService.findAllByNamedQuery("SCRIPT", "workordernumber.for.workspackage");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("worksPackage",worksPackage,"estimate",abstractEstimate,"finYear",
				financialYear,"sequenceGenerator",sequenceGenerator,"workOrder",workOrder,"persistenceService",persistenceService));
	}

	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	
}
