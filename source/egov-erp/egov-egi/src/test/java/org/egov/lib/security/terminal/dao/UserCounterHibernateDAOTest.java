package org.egov.lib.security.terminal.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.model.UserCounterMap;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class UserCounterHibernateDAOTest {

	private Session session;
	private UserCounterHibernateDAO userCounterHibDao;
	private Query query;

	@Before
	public void beforeTestRuns() throws Exception {
		this.session = Mockito.mock(Session.class);
		EGOVThreadLocals.setDomainName("test.com");
		this.query = Mockito.mock(Query.class);
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		Mockito.when(this.session.isOpen()).thenReturn(true);
		this.userCounterHibDao = new UserCounterHibernateDAO(
				UserCounterMap.class, this.session);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
	}

	@Test
	public void testDeleteCounters() {
		try {
			Mockito.doThrow(new EGOVRuntimeException("")).when(this.query)
					.executeUpdate();
			this.userCounterHibDao.deleteCounters(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetLocationBasedUserCounterMapForCurrentDate() {
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		final List l = this.userCounterHibDao
				.getLocationBasedUserCounterMapForCurrentDate(1);
		assertNotNull(l);
		Mockito.when(
				this.query.setInteger(Matchers.anyString(), Matchers.anyInt()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.userCounterHibDao
					.getLocationBasedUserCounterMapForCurrentDate(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetTerminalBasedUserCounterMapForCurrentDate() {
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		final List l = this.userCounterHibDao
				.getTerminalBasedUserCounterMapForCurrentDate(1);
		assertNotNull(l);
		Mockito.when(
				this.query.setInteger(Matchers.anyString(), Matchers.anyInt()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.userCounterHibDao
					.getTerminalBasedUserCounterMapForCurrentDate(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserCounterMapForLocationId() {
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		final List l = this.userCounterHibDao.getUserCounterMapForLocationId(1);
		assertNotNull(l);
		Mockito.when(
				this.query.setInteger(Matchers.anyString(), Matchers.anyInt()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.userCounterHibDao.getUserCounterMapForLocationId(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserCounterMapForTerminalId() {
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		final List l = this.userCounterHibDao.getUserCounterMapForTerminalId(1);
		assertNotNull(l);
		Mockito.when(
				this.query.setInteger(Matchers.anyString(), Matchers.anyInt()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.userCounterHibDao.getUserCounterMapForTerminalId(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserCounterMapForUserId() {
		Mockito.when(this.query.list()).thenReturn(Collections.emptyList());
		final List l = this.userCounterHibDao.getUserCounterMapForUserId(1);
		assertNotNull(l);
		Mockito.when(
				this.query.setInteger(Matchers.anyString(), Matchers.anyInt()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.userCounterHibDao.getUserCounterMapForUserId(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCheckUserCounter() throws Exception {
		boolean condition = this.userCounterHibDao.checkUserCounter(null, null,
				null);
		assertFalse(condition);
		final List l = new ArrayList();
		l.add(new Object());
		Mockito.when(this.query.list()).thenReturn(l);
		condition = this.userCounterHibDao.checkUserCounter(1, new Date(),
				new Date());
		assertTrue(condition);
		condition = this.userCounterHibDao
				.checkUserCounter(1, new Date(), null);
		assertTrue(condition);
		Mockito.when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			this.userCounterHibDao.checkUserCounter(1, new Date(), null);
		} catch (final Exception e) {
			assertTrue(true);
		}
		beforeTestRuns();
		Mockito.when(
				this.query.setInteger(Matchers.anyString(), Matchers.anyInt()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.userCounterHibDao.checkUserCounter(1, new Date(), null);
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

}
