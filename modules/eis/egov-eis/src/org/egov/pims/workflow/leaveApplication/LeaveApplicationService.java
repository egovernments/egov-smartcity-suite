package org.egov.pims.workflow.leaveApplication;

import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.utils.EisManagersUtill;

/**
 * 
 * @author DivyaShree
 *
 */
public class LeaveApplicationService extends PersistenceService<LeaveApplication,Integer> 
{
	private SimpleWorkflowService<LeaveApplication> leaveWfService;
		
	public final static Logger LOGGER = Logger.getLogger(LeaveApplicationService.class.getClass());
	public void setLeaveWfService(
			SimpleWorkflowService<LeaveApplication> leaveWfService) {
		this.leaveWfService = leaveWfService;
	}
	
	
	public LeaveApplication createLeaveApplicationWorkFlow(LeaveApplication leaveapplication)
	{
		
		try{
			  Position position = EisManagersUtill.getEisCommonsService().getPositionByUserId(leaveapplication.getEmployeeId().getUserMaster().getId());
			  LOGGER.info("POSITION ID--"+position.getId());
			  LOGGER.info("Position Name---"+position.getName());
			  if(position==null) {
				  throw new EGOVRuntimeException("The Employe for whom creating Leave does not have position assigned");
			  }
			  else{
				   leaveWfService.start(leaveapplication, position, "Created by Creator");			 
				  			 
				  /*String desigName = position.getDesigId().getDesignationName().replaceAll(" ","");
				  String actionName = desigName + "_approve";*/
				  String actionName = "user" + "_approve";
				  leaveapplication.getCurrentState().setNextAction("Approval Pending");
				  LOGGER.info("check the userCreated"+leaveapplication.getCreatedBy().getUserName());
				  leaveWfService.transition(actionName,leaveapplication,"created by clerk");
				  			 
				 
			  }
			  				  
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  LOGGER.info("INSERT--validation error---------");
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+"jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
		  }
		  return leaveapplication;
	}
	
	public LeaveApplication updateLeaveApplication(LeaveApplication leaveapplication,String actionName,String comment)
	{
		LOGGER.info("coming to update---");
		try{
			  
			leaveWfService.transition(actionName, leaveapplication, comment);
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  LOGGER.info("UPDATE--validation error---------");
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+"-During workflow updation");
		  }
		  return leaveapplication;
	}
	
	/**
	  * Based on config key api decides for Auto or Manual
	  * if strleaveAutoOrManaul-Auto(Auto WorkFlow) 
	  * else Manual
	  * @return true for Manaul
	  */
	public String typeOfLeaveWF(){
		
		String strLeaveType=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Leave Application","LeaveAutoOrManualWorkFlow",new Date()).getValue();
		return strLeaveType;
	}
	
}
