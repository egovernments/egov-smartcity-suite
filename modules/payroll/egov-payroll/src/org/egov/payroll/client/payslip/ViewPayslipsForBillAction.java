package org.egov.payroll.client.payslip;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ParameterAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.payroll.model.EmpPayroll;

import com.opensymphony.xwork2.ActionSupport;

public class ViewPayslipsForBillAction extends ActionSupport implements ParameterAware{
	private static final Logger LOGGER = Logger.getLogger(ViewPayslipsForBillAction.class);
	protected Map<String, String[]> parameters;
	private PersistenceService persistenceService;
	private List<Hashtable<String, Object>> payslipSet=null;



	public List<Hashtable<String, Object>> getPayslipSet() {
		return payslipSet;
	}
	public void setPayslipSet(List<Hashtable<String, Object>> payslipSet) {
		this.payslipSet = payslipSet;
	}



	public String execute(){
		LOGGER.info("inside--------");
		Long billId = Long.parseLong(parameters.get("billId")[0]);
		List<EmpPayroll> listPayslip = persistenceService.findAllBy("from EmpPayroll where billRegister.id = ?", billId);
		payslipSet = new ArrayList<Hashtable<String, Object>>();
		for(EmpPayroll payslipObj : listPayslip){
			Hashtable<String, Object> hashTablePayslips=new Hashtable<String, Object>();			
			hashTablePayslips.put("paySlip", payslipObj);
			payslipSet.add(hashTablePayslips);
		}
		return "list";
	}



	@Override
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
		
	}
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	
}
