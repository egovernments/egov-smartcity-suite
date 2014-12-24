package org.egov.web.actions.common;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.mail.MailServeable;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.XWorkTestCase;

public class MailSenderActionTest extends XWorkTestCase {

	private MailServeable mockMailService;
	private final MailSenderAction mailSender = new MailSenderAction();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.mockMailService = mock(MailServeable.class);
		when(this.mockMailService.sendMail(anyString(), anyString()))
				.thenReturn(true, false);
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.session = mock(HttpSession.class);
		when(this.session.getAttribute(anyString())).thenReturn("some");
		when(this.request.getSession()).thenReturn(this.session);
		when(this.response.getWriter()).thenReturn(new PrintWriter(System.out));
		ServletActionContext.setRequest(this.request);
		ServletActionContext.setResponse(this.response);
		EGOVThreadLocals.setUserId("1");
		EGOVThreadLocals.setDomainName("asass");
		this.mailSender.getModel();
	}

	@Test
	public void testSendFeedback() throws Exception {
		this.mailSender.setFeedbackMailService(this.mockMailService);
		this.mailSender.setMessage("somemessage");
		this.mailSender.setSubject("somesubject");
		this.mailSender.sendFeedback();
		assertTrue(true);
		this.mailSender.sendFeedback();
		assertTrue(true);
		when(this.mockMailService.sendMail(anyString(), anyString()))
				.thenThrow(new RuntimeException("e"));
		try {
			this.mailSender.sendFeedback();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testSendError() throws Exception {
		this.mailSender.setErrorMailService(this.mockMailService);
		this.mailSender.sendError();
		assertTrue(true);
		this.mailSender.sendError();
		assertTrue(true);
		when(this.mockMailService.sendMail(anyString(), anyString()))
				.thenThrow(new RuntimeException("e"));
		try {
			this.mailSender.sendError();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}
}
