package org.egov.payroll.workflow.advance;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.SalaryARF;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.commons.Position;


/**
 * @author Jagadeesan
 *
 */
public class SalaryARFWorkflowService extends PersistenceService<SalaryARF, Long>{
	
	private static final Logger LOGGER = Logger.getLogger(SalaryARFWorkflowService.class);
	private SimpleWorkflowService<SalaryARF>  salaryARFSimpleWorkflowService;
	
	private PayrollExternalInterface payrollExternalInterface;
	private AdvanceService advanceService;
	
	public void createWorkFlow(Advance advance)throws Exception{
		  try{
			  
			  User user = PayrollManagersUtill.getUserService().getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
			  Position position = payrollExternalInterface.getPositionByUserId(user.getId());			 
			  
			  if(position==null)
			  {
				  throw new EGOVRuntimeException("No position mapped for the employee who is creating the Advance");
			  }
			  else
			  {
				  String actionName = "user_approve";	
				  salaryARFSimpleWorkflowService.start(advance.getSalaryARF(), position, "Advance Workflow Start");			 
				  LOGGER.warn("advanceId for bankloan======"+advance.getSalaryARF().getId());			 
				  salaryARFSimpleWorkflowService.transition(actionName, advance.getSalaryARF(), "Created by "+payrollExternalInterface.getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId())).getUserName());
				  advance.getSalaryARF().getCurrentState().setNextAction("Pending for Sanction/Reject");
			  }
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+"-During workflow creation");
		  }
	  }
	  
	/**
	 * Getting superior position
	 * @param position
	 * @param objType
	 * @return position
	 */
	public Position getSuperiorPosition(Position position,String objType){
		Position posTemp = null;
		posTemp =payrollExternalInterface.getSuperiorPositionByObjType(position, objType);
		return posTemp;
	}

	/**
	 * Purpose : To get the advance workflow is manual or auto
	 * @return
	 */
	public String getTypeOfAdvanceWf()
	{
		String advanceWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll",PayrollConstants.ADVANCE_WF_TYPE,new java.util.Date()).getValue();
		return advanceWfType;
	}
	
	public PayrollExternalInterface getPayrollExternalInterface() {
		return payrollExternalInterface;
	}
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	public AdvanceService getAdvanceService() {
		return advanceService;
	}

	public void setAdvanceService(AdvanceService advanceService) {
		this.advanceService = advanceService;
	}

	public SimpleWorkflowService<SalaryARF> getSalaryARFSimpleWorkflowService() {
		return salaryARFSimpleWorkflowService;
	}

	public void setSalaryARFSimpleWorkflowService(
			SimpleWorkflowService<SalaryARF> salaryARFSimpleWorkflowService) {
		this.salaryARFSimpleWorkflowService = salaryARFSimpleWorkflowService;
	}
}
