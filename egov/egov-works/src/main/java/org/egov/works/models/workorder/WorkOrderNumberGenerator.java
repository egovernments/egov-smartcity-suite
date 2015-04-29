package org.egov.works.models.workorder;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.WorksPackage;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkOrderNumberGenerator {
    @Autowired
    private SequenceGenerator sequenceGenerator;
    //private PersistenceService<Script, Long> scriptService;
    @Autowired
    private ScriptService scriptService;
	
	public String getWorkOrderNumberGenerator(AbstractEstimate abstractEstimate, CFinancialYear financialYear,WorksPackage worksPackage, WorkOrder workOrder, PersistenceService persistenceService){
		/*List<Script> scripts=null;
		if(worksPackage==null)
			scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.workOrderNumber.generator");
		else scripts = scriptService.findAllByNamedQuery("SCRIPT", "workordernumber.for.workspackage");
		return scripts.get(0).eval(Script.createContext("worksPackage",worksPackage,"estimate",abstractEstimate,"finYear",
				financialYear,"sequenceGenerator",sequenceGenerator,"workOrder",workOrder,"persistenceService",persistenceService)).toString();*/
	    try{
                ScriptContext scriptContext = ScriptService.createContext("worksPackage",worksPackage,"estimate",abstractEstimate,"finYear",
                        financialYear,"sequenceGenerator",sequenceGenerator,"workOrder",workOrder,"persistenceService",persistenceService);
                if(worksPackage==null)
                    return scriptService.executeScript("works.workOrderNumber.generator", scriptContext).toString();
                else
                    return scriptService.executeScript("workordernumber.for.workspackage", scriptContext).toString();
            }
            catch (ValidationException sequenceException) {
                throw sequenceException;
            }
	}

}
