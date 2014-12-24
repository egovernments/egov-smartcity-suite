package com.exilant.exility.dataservice;

import java.sql.Connection;

import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;


/*
 * This class should not be called by programmers directly. call to this has to
 * come through JobService. (modifier public is provided for XML Loader)
 * 
 * A Job consists of tasks. The tasks are defined in resource/jobs.xml
 * (These are loaded and cached by JobService for performance)
 * 
 * This class executes each tasks. 
 */
public class Job{
	
	//fields loaded by XML
	public String id;
	public JobStep[] jobSteps;
	//public String accessRoles;
	//contains the permissible roles for the job
	public String roles;
	
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public Job(){
		super();
	}
	
	public void execute(DataCollection dc, Connection con) throws TaskFailedException{
		for(int i=0; i<jobSteps.length; i++){
			this.jobSteps[i].execute(dc, con);
		}
	}
	/*public boolean previliged(String roleIDs){
		String[] rolesToValidate=roleIDs.split(",");
		String[] validRoles=accessRoles.split(",");
		for(int i=0;i<rolesToValidate.length;i++){
			for(int j=0;j<validRoles.length;j++){
				if(rolesToValidate[i].equalsIgnoreCase(validRoles[j])){
					return true;
				}
			}
		}
		return false;
	}*/
	
	public boolean hasAccess(DataCollection dc, Connection con) throws TaskFailedException{
		String currentUserName;
		String allowedRoles;
		String userRole="";
		//Integer userId;
		boolean bAccess = false;
		currentUserName = dc.getValue("current_loginName");
		//LOGGER.debug("######currentUserId: " + currentUserName);
		allowedRoles = this.roles;
		//LOGGER.debug("######allowedRoles: " + allowedRoles);
		try{
		//User aUser = new User(currentUserName);
		User aUser=userService.getUserByUserName(currentUserName);
		//userRole = aUser.getRole(con);
		
		for (final Role role : aUser.getRoles()) {
			userRole=role.getRoleName();
		}
		if (((allowedRoles != null) && (allowedRoles.equals("Everyone")))
			|| ((allowedRoles != null) && (allowedRoles.indexOf(userRole)>0)))
			bAccess = true;
		}catch(Exception te)
		{
			dc.addMessage("exilRPError","Access not defined");			
		}
		return bAccess;
	}
	
	
}