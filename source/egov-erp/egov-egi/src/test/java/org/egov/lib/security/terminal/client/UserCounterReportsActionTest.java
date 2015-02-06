package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@Ignore
public class UserCounterReportsActionTest {

	private UserCounterReportsAction userCounterReportsAction;
	private ActionMapping actionMapping;
	private UserCounterReportsForm userCounterReportsForm;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Session session;
	private Query query;
	private HttpSession httpSession;

	@Before
	public void beforeTestRuns() throws Exception {
		this.userCounterReportsAction = new UserCounterReportsAction();
		this.query = Mockito.mock(Query.class);
		this.actionMapping = Mockito.mock(ActionMapping.class);
		this.userCounterReportsForm = new UserCounterReportsForm();
		this.request = Mockito.mock(HttpServletRequest.class);
		this.httpSession = Mockito.mock(HttpSession.class);
		Mockito.when(this.request.getSession()).thenReturn(this.httpSession);
		this.response = Mockito.mock(HttpServletResponse.class);
		this.session = Mockito.mock(Session.class);
		Mockito.when(this.session.isOpen()).thenReturn(true);
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);

	}

	private void setFormProperty() {
		this.userCounterReportsForm.setFromDate("01/01/2010");
		this.userCounterReportsForm.setIsLoc("1");
		this.userCounterReportsForm.setToDate("01/02/2010");
	}

	@Test
	public void testGenerateUserCounterReports() throws Exception {
		try {
			this.userCounterReportsForm = Mockito
					.mock(UserCounterReportsForm.class);
			Mockito.when(this.userCounterReportsForm.getIsLoc()).thenThrow(
					new EGOVRuntimeException(""));
			this.userCounterReportsAction.generateUserCounterReports(
					this.actionMapping, this.userCounterReportsForm,
					this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setFormProperty();
		Mockito.when(this.query.list()).thenReturn(new ArrayList());
		this.userCounterReportsAction.generateUserCounterReports(
				this.actionMapping, this.userCounterReportsForm, this.request,
				this.response);
		this.userCounterReportsForm.setToDate("");
		this.userCounterReportsForm.setIsLoc("");
		this.userCounterReportsAction.generateUserCounterReports(
				this.actionMapping, this.userCounterReportsForm, this.request,
				this.response);
		this.userCounterReportsForm.setFromDate("");
		this.userCounterReportsForm.setIsLoc("1");
		this.userCounterReportsForm.setToDate("");
		this.userCounterReportsAction.generateUserCounterReports(
				this.actionMapping, this.userCounterReportsForm, this.request,
				this.response);
	}

}
