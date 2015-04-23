package com.exilant.exility.pagemanager;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.User;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class SecurityGuard {

	private static final Logger LOGGER = Logger.getLogger(SecurityGuard.class);
	public SecurityGuard() {
		super();
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

			//This fix is for Phoenix Migration.
			/*dc.addValue("current_loginName",principalName);
			if (requestHibernateUtil.getCurrentSession().getAttribute("current_UserID") == null)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("getting $$$UsesrID$$$ = " );
				User aUser = new User(principalName);
				if(LOGGER.isDebugEnabled())     LOGGER.debug(" got " + aUser.getId());
				requestHibernateUtil.getCurrentSession().setAttribute("current_UserID",String.valueOf(aUser.getId()));
				requestHibernateUtil.getCurrentSession().setAttribute("current_loginName",principalName);
			}
			dc.addValue("current_UserID",requestHibernateUtil.getCurrentSession().getAttribute("current_UserID").toString());
			dc.addValue("egUser_id",requestHibernateUtil.getCurrentSession().getAttribute("current_UserID").toString());
			if(LOGGER.isDebugEnabled())     LOGGER.debug("$$$UsesrID$$$ = " + requestHibernateUtil.getCurrentSession().getAttribute("current_UserID"));*/
		}
		else
		{
			dc.addValue("current_loginName","");
		}
		}
		catch (Exception te){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+te.getMessage());
			//dc.addMessage();
		}
		return true;
	}

}
