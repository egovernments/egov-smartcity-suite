package org.egov.lib.security.terminal.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class UserValidateHibernateDAOTest {

	private Session session;
	private UserValidateHibernateDAO userValidateHibDao;
	private Query query;

	@Before
	public void beforeTestRuns() throws Exception {
		this.session = Mockito.mock(Session.class);
		this.query = Mockito.mock(Query.class);
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		Mockito.when(this.session.isOpen()).thenReturn(true);
		this.userValidateHibDao = new UserValidateHibernateDAO();
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
		Mockito.when(this.query.list()).thenReturn(l);
		boolean condition = this.userValidateHibDao.validateUser(validate);
		assertTrue(condition);
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		condition = this.userValidateHibDao.validateUser(validate);
		assertFalse(condition);
	}

	@Test
	public void testValidateUserLocation() {
		final UserValidate validate = new UserValidate();
		validate.setPassword("password");
		final ArrayList l = new ArrayList();
		l.add(new Object());
		Mockito.when(this.query.list()).thenReturn(l);
		boolean condition = this.userValidateHibDao
				.validateUserLocation(validate);
		assertTrue(condition);
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		condition = this.userValidateHibDao.validateUserLocation(validate);
		assertFalse(condition);
	}

	@Test
	public void testValidateUserTerminal() {
		final UserValidate validate = new UserValidate();
		validate.setPassword("password");
		final ArrayList l = new ArrayList();
		l.add(new Object());
		Mockito.when(this.query.list()).thenReturn(l);
		boolean condition = this.userValidateHibDao
				.validateUserTerminal(validate);
		assertTrue(condition);
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		condition = this.userValidateHibDao.validateUserTerminal(validate);
		assertFalse(condition);
	}

	@Test
	public void testGetLocationByIP() {
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		final Location loc = this.userValidateHibDao
				.getLocationByIP("ipAddress");
		assertNotNull(loc);
	}

	@Test
	public void testGetTerminalByIP() {
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		final Location loc = this.userValidateHibDao
				.getTerminalByIP("ipAddress");
		assertNotNull(loc);
	}

	@Test
	public void testValidateActiveUserForPeriod() {
		Mockito.when(this.query.uniqueResult()).thenReturn(new UserImpl());
		boolean condition = this.userValidateHibDao
				.validateActiveUserForPeriod("username");
		assertTrue(condition);
		Mockito.when(this.query.uniqueResult()).thenReturn(null);
		condition = this.userValidateHibDao
				.validateActiveUserForPeriod("username");
		assertFalse(condition);
	}

}
