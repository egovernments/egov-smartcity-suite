package org.egov.infstr.security.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private UserService userService;
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,    ServletException {          
		
		
		//TODO we dont have a user type yet upon implementing user type update 
		//this method with appropriate logic to redirect
		/*
		 * User user = this.userService.getUserByUserName(authentication.getName());
		 * if(user.getUserType().equals("citizen")) {
			setDefaultTargetUrl("/citizen/homepage.action");
		} else {
			setDefaultTargetUrl("/common/homepage.action");
		}*/
		super.onAuthenticationSuccess(request, response, authentication);
    }

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
