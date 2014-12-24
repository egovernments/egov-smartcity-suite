package com.exilant.exility.pagemanager;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;

import com.exilant.exility.common.DataCollection;

public class SecurityGuard {

	private static final Logger LOGGER = Logger.getLogger(SecurityGuard.class);
	public SecurityGuard() {
		super();
	}
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean clearedSecurity(DataCollection dc, HttpServletRequest request)
	{
		//request.getAttribute("UserID");
		try{
		if (request.getUserPrincipal() != null)
		{
			String principalName = request.getUserPrincipal().getName();
			int userLen = principalName.indexOf("<:1>");
			if(userLen > -1)
			{
				principalName = principalName.substring(0,userLen);
			}

			
			dc.addValue("current_loginName",principalName);
			if (request.getSession().getAttribute("current_UserID") == null)
			{
				LOGGER.debug("getting $$$UsesrID$$$ = " );
				//User aUser = new User(principalName);
				User aUser=userService.getUserByUserName(principalName);
				LOGGER.debug(" got " + aUser.getId());
				request.getSession().setAttribute("current_UserID",String.valueOf(aUser.getId()));
				request.getSession().setAttribute("current_loginName",principalName);
			}
			dc.addValue("current_UserID",request.getSession().getAttribute("current_UserID").toString());
			dc.addValue("egUser_id",request.getSession().getAttribute("current_UserID").toString());
			LOGGER.debug("$$$UsesrID$$$ = " + request.getSession().getAttribute("current_UserID"));
		}
		else
		{
			dc.addValue("current_loginName","");
		}
		}
		catch (Exception te){
			LOGGER.debug("Exp="+te.getMessage());
			//dc.addMessage();
		}
		return true;
	}

}
