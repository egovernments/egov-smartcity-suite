package org.egov.web.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egov.infstr.security.spring.filter.SSOPrincipal;
import org.egov.infstr.security.spring.filter.SimpleSSOFilter;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SimpleSSOFilterTest {

	private SimpleSSOFilter filter;
	HttpServletRequest request;
	HttpSession session;

	@Before
	public void setup() {
		this.filter = new SimpleSSOFilter();

	}

	@Test
	public void checkLocalHost() {
		assertTrue(this.filter.isLocalhostOrIp("localHOST"));
		assertTrue(this.filter.isLocalhostOrIp("LOCALHOST"));
		assertTrue(this.filter.isLocalhostOrIp("localhost"));
		assertFalse(this.filter.isLocalhostOrIp("LOCALHOST.com"));
	}

	@Ignore
	public void ssoPrincipalIsEncrypted() {
		final String cookieValue = "u=username@@t=12345677888";
		final String encrypted = CryptoHelper.encrypt(cookieValue);
		final SSOPrincipal principal = new SSOPrincipal();
		principal.setUserName("username");
		principal.setTimestamp(12345677888l);
		SSOPrincipal.valueOf(encrypted);
		assertEquals(encrypted, principal.toString());
	}

	@Test
	public void checkSSOPrincipalDecryption() {
		final String cookieValue = "u=username@@t=12345677888";
		final String encrypted = CryptoHelper.encrypt(cookieValue);
		final SSOPrincipal principal = SSOPrincipal.valueOf(encrypted);
		assertEquals("username", principal.getUserName());
		assertEquals(12345677888l, principal.getTimestamp());
	}

	@Test
	public void checkSSOPrincipalTestsEmptyCredentials() {
		final String cookieValue = "u=username@@ipAddress=@@t=12345677888";
		final String encrypted = CryptoHelper.encrypt(cookieValue);
		final SSOPrincipal principal = SSOPrincipal.valueOf(encrypted);
		assertFalse(principal.getCredentials().containsKey("ipAddress"));
	}

	@Test
	public void checkSSOPrincipalEncryptionIsConsistent() {
		final SSOPrincipal principal = new SSOPrincipal();
		principal.setUserName("user");
		principal.addCredential("ip", "127");
		final SSOPrincipal recreated = SSOPrincipal.valueOf(principal
				.toString());
		assertEquals(principal.getUserName(), recreated.getUserName());
		assertEquals(principal.getCredentials(), recreated.getCredentials());
	}

	@Test
	public void checkExtraCredentialsAddedToCredentialMap() {
		final String cookieValue = "u=username@@ipAddress=127.0.0.1@@t=12345677888";
		final String encrypted = CryptoHelper.encrypt(cookieValue);
		final SSOPrincipal principal = SSOPrincipal.valueOf(encrypted);
		assertEquals("127.0.0.1", principal.getCredentials().get("ipAddress"));
	}

	@Test
	public void checkIsIP() {
		assertTrue(this.filter.isIP("192.168.1.1"));
		assertFalse(this.filter.isIP("www.google.com"));
		assertFalse(this.filter.isIP("192.168.1"));
		assertFalse(this.filter.isIP("192.google.com"));
	}

	@Test
	public void getDomainWithoutPort() {
		assertEquals("www.google.com",
				this.filter.getDomainWithoutPort("www.google.com:8080"));
		assertEquals("192.168.1.123",
				this.filter.getDomainWithoutPort("192.168.1.123:12345"));
		assertEquals("localhost",
				this.filter.getDomainWithoutPort("localhost:8080"));
		assertEquals("localhost", this.filter.getDomainWithoutPort("localhost"));
	}

	@Test
	public void authenticationAlreadyLoggedIn() throws Exception {
		setUpRequest();
		setUpCookie(System.currentTimeMillis());
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);
		this.filter.doFilter(this.request, response, chain);
		when(this.request.getCookies()).thenReturn(null);
		setUpCookie(System.currentTimeMillis());
		final Authentication authentication = getAuthetication();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		this.filter.doFilter(this.request, response, chain);

		setUpCookie(System.currentTimeMillis());
		this.filter.setSessionTimeout(0);
		this.filter.doFilter(this.request, response, chain);
		this.filter.setSessionTimeout(10);
		when(this.request.getCookies()).thenReturn(null);
		when(this.session.getAttribute("sso_done")).thenReturn("sso_done");
		setUpCookie(System.currentTimeMillis());
		this.filter.doFilter(this.request, response, chain);

		// verify(response,never()).addCookie(any(Cookie.class));

	}

	@Test
	public void authenticationAlreadyLoggedInError() throws Exception {
		setUpRequest();
		final Cookie[] cookies = new Cookie[2];
		final SSOPrincipal principal = new SSOPrincipal();
		principal.setUserName("egovernments");
		principal.setTimestamp(1);
		principal.addCredential("ip", "127");
		cookies[0] = new Cookie("egovegov", principal.toString());
		cookies[1] = new Cookie("egov2egov2", principal.toString());
		when(this.request.getCookies()).thenReturn(cookies);
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);
		final AuthenticationProvider provider = Mockito
				.mock(AuthenticationProvider.class);
		Mockito.when(provider.authenticate(any(Authentication.class)))
				.thenThrow(new AuthenticationException(null) {

					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;
				});
		this.filter.doFilter(this.request, response, chain);
	}

	@Test
	public void authenticationExpiredSession() throws Exception {
		setUpRequest();
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final FilterChain chain = mock(FilterChain.class);
		setUpCookie(System.currentTimeMillis() + 20 * 60 * 1000);
		this.filter.doFilter(this.request, response, chain);
		// verify(response).sendRedirect(any(String.class));
	}

	private void setUpRequest() {

		this.request = mock(HttpServletRequest.class);

		this.session = mock(HttpSession.class);
		when(this.request.getSession()).thenReturn(this.session);
		when(this.request.getSession(false)).thenReturn(this.session);
		when(this.request.getSession().getId()).thenReturn("123");
		final UserService userMgr = mock(UserService.class);
		final UserImpl user = new UserImpl();
		user.setUserName("egovernments");
		user.setPwd(CryptoHelper.encrypt("egovernments"));
		when(userMgr.getUserByUserName(anyString())).thenReturn(user);
		final AuthenticationProvider provider = mock(AuthenticationProvider.class);
		final UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
		when(provider.authenticate(any(Authentication.class)))
				.thenReturn(token);
		this.filter.setSessionTimeout(10);
		this.filter.setUserService(userMgr);
		this.filter.setAuthenticationProvider(provider);

	}

	@Test
	public void testLogout() throws Exception {
		this.request = mock(HttpServletRequest.class);
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final Authentication authentication = mock(Authentication.class);
		this.filter.logout(this.request, response, authentication);
		setUpCookie(System.currentTimeMillis());
		this.filter.logout(this.request, response, authentication);
		this.filter.setLogoutUrl(null);
		this.filter.init(null);
		this.filter.destroy();
	}

	@Test
	public void getSSOPrincipalFromAuthentication() throws Exception {
		final Method method = this.filter.getClass().getDeclaredMethod(
				"getSSOPrincipalFromAuthentication");
		final Authentication authentication = getAuthetication();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		method.setAccessible(true);
		method.invoke(this.filter);
	}

	private Authentication getAuthetication() {
		final Authentication authentication = new Authentication() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Collection<GrantedAuthority> getAuthorities() {
				return null;
			}

			@Override
			public Object getCredentials() {
				final java.util.HashMap<String, String> conf = new java.util.HashMap<String, String>();
				conf.put("password", "pwd");
				conf.put("j_password", "pwd");
				return conf;
			}

			@Override
			public Object getDetails() {
				return null;
			}

			@Override
			public Object getPrincipal() {
				getAuthorities();
				getDetails();
				setAuthenticated(isAuthenticated());
				getName();
				final User user = mock(User.class);
				when(user.getUsername()).thenReturn("uname");
				return user;
			}

			@Override
			public boolean isAuthenticated() {
				return false;
			}

			@Override
			public void setAuthenticated(final boolean arg0)
					throws IllegalArgumentException {
			}

			@Override
			public String getName() {
				return null;
			}
		};
		return authentication;
	}

	@Test
	public void unTestable() throws Exception {
		Method method = this.filter.getClass().getDeclaredMethod("makeCookie",
				HttpServletRequest.class, SSOPrincipal.class);
		method.setAccessible(true);
		method.invoke(this.filter, null, null);
		method = this.filter.getClass().getDeclaredMethod("doLogout",
				HttpServletRequest.class, HttpServletResponse.class);
		method.setAccessible(true);
		method.invoke(this.filter, mock(HttpServletRequest.class),
				mock(HttpServletResponse.class));
		method = this.filter.getClass().getDeclaredMethod("getSSOCookie",
				HttpServletRequest.class);
		method.setAccessible(true);
		method.invoke(this.filter, mock(HttpServletRequest.class));
		method = this.filter.getClass().getDeclaredMethod("getSSOPrincipal",
				HttpServletRequest.class);
		method.setAccessible(true);
		method.invoke(this.filter, mock(HttpServletRequest.class));
		method = this.filter.getClass().getDeclaredMethod(
				"isSSOCompletedForThisSession", HttpSession.class);
		method.setAccessible(true);
		method.invoke(this.filter, mock(HttpSession.class));
	}

	private void setUpCookie(final long cookieTimeStamp) {
		final Cookie[] cookies = new Cookie[2];
		final SSOPrincipal principal = new SSOPrincipal();
		principal.setUserName("egovernments");
		principal.setTimestamp(cookieTimeStamp);
		principal.addCredential("ip", "127");
		principal.setSessionId("123");
		cookies[0] = new Cookie("egovegov", principal.toString());
		cookies[1] = new Cookie("egov2egov2", principal.toString());
		when(this.request.getCookies()).thenReturn(cookies);
	}

}
