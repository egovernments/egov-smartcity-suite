package org.egov.works.models.contractorBill;

import javax.script.ScriptContext;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;

public class ContractorBillNumberGenerator {
	public static final String SEQUENCE_TYPE="CONTRACTORBILL";
	 @Autowired
         private SequenceGenerator sequenceGenerator;
         //private PersistenceService<Script, Long> scriptService;
         @Autowired
         private ScriptService scriptService;
	
	public String getBillNumber(WorkOrder workOrder, CFinancialYear financialYear,WorkOrderEstimate workOrderEstimate){
		//List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.contractorBillNumber.generator");
		//return scripts.get(0).eval(Script.createContext("workOrder",workOrder,"workOrderEstimate", workOrderEstimate, "finYear",financialYear,"sequenceGenerator",sequenceGenerator)).toString();
	    ScriptContext scriptContext = ScriptService.createContext("workOrder",workOrder,"workOrderEstimate", workOrderEstimate, "finYear",financialYear,"sequenceGenerator",sequenceGenerator);
            return scriptService.executeScript("works.contractorBillNumber.generator", scriptContext).toString();
       
	}

}
