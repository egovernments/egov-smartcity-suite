package org.egov.tradelicense.domain.citizen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.tradelicense.utils.Constants;

import com.opensymphony.xwork2.Action;


public class CitizenIndexAction implements Action, ServletRequestAware {
	
	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;
	
	public String execute() {
		session = request.getSession();
		User user = new UserDAO().getUserByUserName(Constants.CITIZENUSER);
		session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
		userId = user.getId();
		EGOVThreadLocals.setUserId(userId.toString());
		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
	    this.request = arg0;
	}

}
