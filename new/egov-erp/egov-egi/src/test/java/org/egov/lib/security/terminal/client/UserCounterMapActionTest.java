package org.egov.lib.security.terminal.client;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.utils.TestUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserCounterMap;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class UserCounterMapActionTest extends TestCase {

	private UserCounterMapAction usercounterAction;
	private ActionMapping actionMapping;
	private UserCounterMapForm usercounterForm;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Session session;
	private Query query;
	private HttpSession httpSession;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		EGOVThreadLocals.setDomainName("domainName");
		beforeTestRuns();
	}

	@Before
	public void beforeTestRuns() throws Exception {
		this.usercounterAction = new UserCounterMapAction();
		this.query = Mockito.mock(Query.class);
		this.actionMapping = Mockito.mock(ActionMapping.class);
		this.usercounterForm = new UserCounterMapForm();
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
		TestUtils.activateInitialContext();

	}

	private void setFormProperty() {
		this.usercounterForm.setCounter(new String[] { "1", "1" });
		this.usercounterForm.setCounterName(new String[] { "1", "1" });
		this.usercounterForm.setForward("forward");
		this.usercounterForm.setFromDate(new String[] { "01/01/2010",
				"01/01/2010" });
		this.usercounterForm.setLoginType("Location");
		this.usercounterForm.setSelCheck(new String[] { "yes", "no" });
		this.usercounterForm.setToDate(new String[] { "01/01/2010",
				"01/01/2010" });
		this.usercounterForm.setUserCounterId(new String[] { "1", "" });
		this.usercounterForm.setUserId(new String[] { "1", "1" });
		this.usercounterForm.setUserName(new String[] { "1", "1" });
		this.usercounterForm.getUserName();
		this.usercounterForm.getCounterName();
	}

	@Test
	public void testCreateUserControlMapping() throws Exception {
		try {
			setFormProperty();
			this.usercounterAction.createUserControlMapping(this.actionMapping,
					this.usercounterForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setFormProperty();
		this.usercounterForm.setLocationId("1");
		final Location location = new Location();
		location.setId(1);
		final UserImpl user = new UserImpl();
		user.setId(1);
		final UserCounterMap userCounterMap = new UserCounterMap();
		userCounterMap.setCounterId(location);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location, userCounterMap, location, user,
						userCounterMap);
		this.usercounterAction.createUserControlMapping(this.actionMapping,
				this.usercounterForm, this.request, this.response);
		beforeTestRuns();
		setFormProperty();
		this.usercounterForm.setLocationId("1");
		this.usercounterForm.setLoginType("Terminal");
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(location, userCounterMap, location, user,
						userCounterMap);
		this.usercounterAction.createUserControlMapping(this.actionMapping,
				this.usercounterForm, this.request, this.response);
	}

	@Test
	public void testGetUserCounterForCurrentDate() throws Exception {
		try {
			this.usercounterForm = Mockito.mock(UserCounterMapForm.class);
			Mockito.when(this.usercounterForm.getLoginType()).thenThrow(
					new EGOVRuntimeException(""));
			this.usercounterAction.getUserCounterForCurrentDate(
					this.actionMapping, this.usercounterForm, this.request,
					this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setFormProperty();
		this.usercounterForm.setLocationId("1");
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(new Location());
		Mockito.when(this.query.list()).thenReturn(new ArrayList());
		this.usercounterAction.getUserCounterForCurrentDate(this.actionMapping,
				this.usercounterForm, this.request, this.response);
		this.usercounterForm.setLoginType("Terminal");
		this.usercounterAction.getUserCounterForCurrentDate(this.actionMapping,
				this.usercounterForm, this.request, this.response);
	}

	@Test
	public void testGetAllUserCounters() throws Exception {
		try {
			this.usercounterForm = Mockito.mock(UserCounterMapForm.class);
			Mockito.when(this.usercounterForm.getLoginType()).thenThrow(
					new EGOVRuntimeException(""));
			this.usercounterAction.getAllUserCounters(this.actionMapping,
					this.usercounterForm, this.request, this.response);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		beforeTestRuns();
		setFormProperty();
		this.usercounterForm.setLocationId("1");
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(new Location());
		Mockito.when(this.query.list()).thenReturn(new ArrayList());
		this.usercounterAction.getAllUserCounters(this.actionMapping,
				this.usercounterForm, this.request, this.response);
		this.usercounterForm.setLoginType("Terminal");
		this.usercounterAction.getAllUserCounters(this.actionMapping,
				this.usercounterForm, this.request, this.response);
	}

}
