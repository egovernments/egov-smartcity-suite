package org.egov.infstr.security.spring.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutCookieClearerHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final Cookie cookies[] = request.getCookies();
		if (cookies == null || cookies.length < 1)
			return;
		for (final Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setValue(null);
			response.addCookie(cookie);
		}
		
	}

}
