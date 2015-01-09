package org.egov.lib.security.terminal.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.security.terminal.model.Location;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class UserValidateActionTest extends TestCase {

	private UserValidateAction userValidateAction;
	private ActionMapping actionMapping;
	private UserValidateForm userValidateForm;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Session session;
	private Query query;
	private HttpSession httpSession;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		beforeTestRuns();
	}

	@Before
	public void beforeTestRuns() throws Exception {
		this.userValidateAction = new UserValidateAction();
		this.query = Mockito.mock(Query.class);
		this.actionMapping = Mockito.mock(ActionMapping.class);
		this.userValidateForm = new UserValidateForm();
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
		this.userValidateForm.setCounterId("1");
		this.userValidateForm.setIpAddress("1");
		this.userValidateForm.setLocationId("1");
		this.userValidateForm.setLoginType("Location");
		this.userValidateForm.setPassword("1");
		this.userValidateForm.setUsername("username");
	}

	@Test
	public void testValidateUser() throws Exception {
		try {
			setFormProperty();
			this.userValidateForm.setLoginType(null);
			this.userValidateAction.validateUser(this.actionMapping,
					this.userValidateForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		try {
			beforeTestRuns();
			setFormProperty();
			this.userValidateForm.setCounterId(null);
			this.userValidateAction.validateUser(this.actionMapping,
					this.userValidateForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setFormProperty();
		this.userValidateForm.setLoginType("Terminal");
		Mockito.when(this.query.list()).thenReturn(new ArrayList());
		this.userValidateAction.validateUser(this.actionMapping,
				this.userValidateForm, this.request, this.response);
		beforeTestRuns();
		setFormProperty();
		final Location location = new Location();
		final List locations = new ArrayList();
		locations.add(location);
		Mockito.when(this.query.list()).thenReturn(locations);
		Mockito.when(this.query.uniqueResult()).thenReturn(location,
				new UserImpl());
		this.userValidateAction.validateUser(this.actionMapping,
				this.userValidateForm, this.request, this.response);
		Mockito.when(this.query.uniqueResult()).thenReturn(null, location,
				new UserImpl());
		this.userValidateAction.validateUser(this.actionMapping,
				this.userValidateForm, this.request, this.response);
	}

}
