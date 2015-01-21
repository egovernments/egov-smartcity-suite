package org.egov.lib.security.terminal.dao;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserValidateHibernateDAOTest {

	private Session session;
	private UserValidateHibernateDAO userValidateHibDao;
	private Query query;
	@Mock
	private SessionFactory sessionFactory;

	@Before
	public void beforeTestRuns() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.session = Mockito.mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		this.query = Mockito.mock(Query.class);
		when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		when(this.session.isOpen()).thenReturn(true);
		this.userValidateHibDao = new UserValidateHibernateDAO(sessionFactory);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
	}

	@Test
	public void testValidateUser() {
		final UserValidate validate = new UserValidate();
		validate.setPassword("password");
		final ArrayList l = new ArrayList();
		l.add(new Object());
		when(this.query.list()).thenReturn(l);
		boolean condition = this.userValidateHibDao.validateUser(validate);
		assertTrue(condition);
		when(this.query.list()).thenReturn(Collections.emptyList());
		condition = this.userValidateHibDao.validateUser(validate);
		assertFalse(condition);
	}

	@Test
	public void testValidateUserLocation() {
		final UserValidate validate = new UserValidate();
		validate.setPassword("password");
		final ArrayList l = new ArrayList();
		l.add(new Object());
		when(this.query.list()).thenReturn(l);
		boolean condition = this.userValidateHibDao
				.validateUserLocation(validate);
		assertTrue(condition);
		when(this.query.list()).thenReturn(Collections.emptyList());
		condition = this.userValidateHibDao.validateUserLocation(validate);
		assertFalse(condition);
	}

	@Test
	public void testValidateUserTerminal() {
		final UserValidate validate = new UserValidate();
		validate.setPassword("password");
		final ArrayList l = new ArrayList();
		l.add(new Object());
		when(this.query.list()).thenReturn(l);
		boolean condition = this.userValidateHibDao
				.validateUserTerminal(validate);
		assertTrue(condition);
		when(this.query.list()).thenReturn(Collections.emptyList());
		condition = this.userValidateHibDao.validateUserTerminal(validate);
		assertFalse(condition);
	}

	@Test
	public void testGetLocationByIP() {
		when(this.query.uniqueResult()).thenReturn(new Location());
		final Location loc = this.userValidateHibDao
				.getLocationByIP("ipAddress");
		assertNotNull(loc);
	}

	@Test
	public void testGetTerminalByIP() {
		when(this.query.uniqueResult()).thenReturn(new Location());
		final Location loc = this.userValidateHibDao
				.getTerminalByIP("ipAddress");
		assertNotNull(loc);
	}

	@Test
	public void testValidateActiveUserForPeriod() {
		when(this.query.uniqueResult()).thenReturn(new UserImpl());
		boolean condition = this.userValidateHibDao
				.validateActiveUserForPeriod("username");
		assertTrue(condition);
		when(this.query.uniqueResult()).thenReturn(null);
		condition = this.userValidateHibDao
				.validateActiveUserForPeriod("username");
		assertFalse(condition);
	}

}
