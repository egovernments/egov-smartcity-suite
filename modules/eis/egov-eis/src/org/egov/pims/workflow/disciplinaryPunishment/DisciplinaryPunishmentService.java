package org.egov.pims.workflow.disciplinaryPunishment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.UtilityMethods;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.utils.EisManagersUtill;

/**
 * 
 * @author Jagadeesan
 *
 */
public class DisciplinaryPunishmentService extends PersistenceService<DisciplinaryPunishment,Integer> 
{
	private SimpleWorkflowService<DisciplinaryPunishment> disciplinaryPunishmentWorkflowService;
		
	public final static Logger LOGGER = Logger.getLogger(DisciplinaryPunishmentService.class);
	private transient RbacService rbacService;
	private transient EmployeeService employeeService;
	private transient EisUtilService eisService;
	
	public void createDisciplinaryPunishmentWorkFlow(DisciplinaryPunishment disciplinaryPunishment)
	{
			User user = EisManagersUtill.getUserService().getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
			List<Position> positionList = (ArrayList<Position>)getEisService().getPositionsForUser(user.getId(),new Date());
			if(positionList.isEmpty()) {
				throw new	ValidationException("","Logged in user do not have position assigned. So this user cannot able to create disciplinary");
			}
			else{
				Position position = positionList.get(0);
				LOGGER.info("POSITION ID--"+position.getId());
				LOGGER.info("Position Name---"+position.getName());
				disciplinaryPunishmentWorkflowService.start(disciplinaryPunishment, position, "Created by "+user.getUserName());			 

				String actionName = "user" + "_approve";
				disciplinaryPunishment.getCurrentState().setNextAction("Approval Pending");
				LOGGER.info("check the userCreated"+disciplinaryPunishment.getCreatedBy().getUserName());
				disciplinaryPunishmentWorkflowService.transition(actionName,disciplinaryPunishment,"created by "+user.getUserName());
		  }
	}
	
	public void updateDisciplinaryPunishmentWorkFlow(DisciplinaryPunishment disciplinaryPunishment,String actionName,String comment)
	{
		LOGGER.info("coming to update---");
		try{
			disciplinaryPunishmentWorkflowService.transition(actionName, disciplinaryPunishment, comment);
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  LOGGER.info("UPDATE--validation error---------");
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new EGOVRuntimeException(errorMsg+" - During workflow updation");
		  }
	}
	
	/**
	  * Based on config key api decides for Auto or Manual
	  * if strDisciplinaryPunishmentAutoOrManaul-Auto(Auto WorkFlow) 
	  * else Manual
	  * @return true for Manaul
	  */
	public String typeOfDisciplinaryPunishmentWF(){
		
		String strDisciplinaryPunishmentType=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("EIS","DisciplinaryPunishmentWorkFlowType",new Date()).getValue();
		return strDisciplinaryPunishmentType;
	}
	
	public String getActionUrlByPassingActionName(String actionName,String disciplinaryId) 
	{
		String url=null;
		if(actionName!=null && !"".equals(actionName)){
			Action action= rbacService.getActionByName(actionName);
			
			if(action!=null)
			{
				url="/"+action.getContextRoot().concat(action.getUrl().concat("?").concat(action.getQueryParams()));
			}
		}
		return url;
		
	}

	public String generateApplicationNo()
	{
		String appNo="";
		
		while(true)
		{
			appNo = UtilityMethods.getRandomString();
			if(employeeService.checkDisciplinaryNo(appNo))
			{
				continue;
			}
			else
			{
				break;
			}
		}
		return appNo;
	}

	public void setDisciplinaryPunishmentWorkflowService(
			SimpleWorkflowService<DisciplinaryPunishment> disciplinaryPunishmentWorkflowService) {
		this.disciplinaryPunishmentWorkflowService = disciplinaryPunishmentWorkflowService;
	}
	
	
	public RbacService getRbacService() {
		return rbacService;
	}

	public void setRbacService(RbacService rbacService) {
		this.rbacService = rbacService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public SimpleWorkflowService<DisciplinaryPunishment> getDisciplinaryPunishmentWorkflowService() {
		return disciplinaryPunishmentWorkflowService;
	}

	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

}