package org.egov.infstr.security.acegi.client.filter;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.egov.infstr.security.spring.filter.EgovAuthenticationProcessingFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class EgovAuthenticationProcessingFilterTest {

	EgovAuthenticationProcessingFilter filter;

	@Before
	public void setup() {
		final AuthenticationManager authMgr = Mockito
				.mock(AuthenticationManager.class);
		this.filter = new EgovAuthenticationProcessingFilter();
		this.filter.setAuthenticationManager(authMgr);
	}

	@Test
	public void testAttemptAuthentication() throws Exception {
		final HttpServletRequest req = createMockRequest();
		this.filter.afterPropertiesSet();
		this.filter.attemptAuthentication(req, new MockHttpServletResponse());
		// System.out.println("UserName:" +
		// req.getSession().getAttribute(EgovAuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY));
		assertEquals(
				"egovernments",
				req.getSession()
						.getAttribute(
								UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY));
	}

	private MockHttpServletRequest createMockRequest() {
		final MockHttpServletRequest request = new MockHttpServletRequest();

		request.setServletPath("/test.action");
		request.setScheme("http");
		request.setServerName("www.egov.com");
		request.setRequestURI("/egi/test.action");
		request.setContextPath("/egi");
		request.setParameter(
				UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY,
				"egovernments");
		return request;

	}
}
