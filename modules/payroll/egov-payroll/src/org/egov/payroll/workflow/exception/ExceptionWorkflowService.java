package org.egov.payroll.workflow.exception;

import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.AppConfigTagUtil;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.services.payslip.PayslipFailureException;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.pims.commons.Position;

/**
 * @author surya
 */
public class ExceptionWorkflowService extends PersistenceService<EmpException, Long>
{
	private static final Logger LOGGER = Logger.getLogger(ExceptionWorkflowService.class);
	private PayrollExternalInterface payrollExternalInterface;	
	private SimpleWorkflowService<EmpException> exceptionWfService;
	
	public void setExceptionWfService(SimpleWorkflowService<EmpException> exceptionWfService) {
		this.exceptionWfService = exceptionWfService;
	}
	public void setPayrollExternalInterface(PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}	
			
	public void updateExceptionStatus(EmpException exception,String status){
		//EgwStatus statusObj = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE, status);
		EgwStatus statusObj = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,status);
		String userId = EGOVThreadLocals.getUserId();
		User user = payrollExternalInterface.getUserByID(Integer.parseInt(userId));
		EgwSatuschange statusChanges = new EgwSatuschange();
		statusChanges.setCreatedby(user.getId());
		statusChanges.setFromstatus(exception.getStatus().getId());
		statusChanges.setTostatus(statusObj.getId());
		statusChanges.setModuleid(1);
		statusChanges.setModuletype(statusObj.getModuletype());
		payrollExternalInterface.createEgwSatuschange(statusChanges);
		exception.setStatus(statusObj);
	}
	
	//FIXME: remove this api and get directly inside python scrip
	/**
	 * Getting superior position
	 * @param position
	 * @param objType
	 * @return position
	 */
	public Position getSuperiorPosition(Position position,String objType){
		Position posTemp = null;
		posTemp = payrollExternalInterface.getSuperiorPositionByObjType(position, objType);
		return posTemp;
	}
	
	/**
	 * Getting Inferior position
	 * @param position
	 * @param objType
	 * @return position
	 */
	public Position getInferiorPosition(Position position,String objType){
		return payrollExternalInterface.getInferiorPositionByObjType(position, objType);
	}
	

	public EmpException createExceptionWorkFlow(EmpException exception,Position position){
		removeCache();
		try{
			  //Position position = payrollExternalInterface.getPositionByUserId(exception.getEmployee().getUserMaster().getId());	
			  if(position==null) {
				  throw new EGOVRuntimeException("The Employe for whom creating exception does not have position assigned");
			  }
			  else{
				  exceptionWfService.start(exception, position, "Created by "+exception.getUserid().getUserName());			 
				  LOGGER.info("paysliId======"+exception.getId()+", actionname="+"CreateCleark");
				  String actionName = "user" + "_approve";
				  exception.getCurrentState().setNextAction("Approval Pending");
				  //FIXME:created by user name:-Fixed
				  exceptionWfService.transition(actionName, exception,"Created by "+exception.getUserid().getUserName());
				  			 
				  LOGGER.info(" Test Exception Work Flow End ----> ");
			  }
			  				  
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+"-During workflow creation");
		  }
		  return exception;
	}
	 
	 public EmpException updateExceptionWorkFlow(EmpException exception,String actionName,String comment)throws PayslipFailureException{
		  try{
			  LOGGER.info("Inside payslip workflow update by this action--"+actionName);
			  exceptionWfService.transition(actionName, exception, comment);
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+"-During workflow updation");
		  } 
		  return exception;
	  }
	 
	 protected void removeCache(){
		 EgovMasterDataCaching.getInstance().removeFromCache("pay-empExceptions");
	 }
	 /**
		 * Deciding the payslip generation workflow whether manual or auto
		 * @return 'manual' for manual workflow and 'auto' for automatic workflow
		 */
		public String typeOfExceptionWF(){
			return EGovConfig.getAppConfigValue(PayrollConstants.EMP_EXCEPTION_MODILE, PayrollConstants.EMP_EXCEPTION_WFKEY, PayrollConstants.WORKFLOW_MANUAL);
		}
}
