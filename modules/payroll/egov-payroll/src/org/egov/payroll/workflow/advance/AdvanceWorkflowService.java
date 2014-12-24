package org.egov.payroll.workflow.advance;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.SalaryARF;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.commons.Position;


/**
 * @author Jagadeesan
 *
 */
public class AdvanceWorkflowService extends PersistenceService<Advance, Long>{
	
	private static final Logger LOGGER = Logger.getLogger(AdvanceWorkflowService.class);
	private SimpleWorkflowService<Advance> advanceSimpleWorkflowService;
	private PayrollExternalInterface payrollExternalInterface;
	private AdvanceService advanceService;
	
	public void createWorkFlow(Advance advance)throws Exception{
		  try{
			  
			  Position position = payrollExternalInterface.getPositionforEmp(advance.getEmployee().getIdPersonalInformation());			 
			  
			  if(position==null)
			  {
				  throw new EGOVRuntimeException("No position defined for employee for whom advance created.");
			  }
			  else
			  {
				  String actionName = "user_approve";	
				  advanceSimpleWorkflowService.start(advance, position, "Advance Workflow Start");			 
				  LOGGER.warn("arf id advance======"+advance.getId());			 
				  advanceSimpleWorkflowService.transition(actionName, advance, "Created by "+payrollExternalInterface.getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId())).getUserName());
				  advance.getCurrentState().setNextAction("Pending for Sanction/Reject");
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

	public SimpleWorkflowService<Advance> getAdvanceSimpleWorkflowService() {
		return advanceSimpleWorkflowService;
	}

	public void setAdvanceSimpleWorkflowService(
			SimpleWorkflowService<Advance> advanceSimpleWorkflowService) {
		this.advanceSimpleWorkflowService = advanceSimpleWorkflowService;
	}

	public AdvanceService getAdvanceService() {
		return advanceService;
	}

	public void setAdvanceService(AdvanceService advanceService) {
		this.advanceService = advanceService;
	}

	
}
