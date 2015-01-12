package org.egov.lib.security.terminal.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.model.Location;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class LocationHibernateDAOTest {

	private Session session;
	private LocationHibernateDAO locationHibernateDAO;
	private Query query;

	@Before
	public void beforeTestRuns() throws Exception {
		this.session = Mockito.mock(Session.class);
		this.query = Mockito.mock(Query.class);
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		Mockito.when(this.session.isOpen()).thenReturn(true);
		this.locationHibernateDAO = new LocationHibernateDAO(Location.class,
				this.session);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
	}

	@Test
	public void testGetLocationIdByLocationNameAndCounter() {
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		final Location location = this.locationHibernateDAO
				.getLocationIdByLocationNameAndCounter("locationName",
						"counterName");
		assertNotNull(location);
	}

	@Test
	public void testGetCountersByLocation() {
		Mockito.when(this.query.list()).thenReturn(new ArrayList<Location>());
		final ArrayList<Location> locations = this.locationHibernateDAO
				.getCountersByLocation(1);
		assertNotNull(locations);
		Mockito.when(this.query.list()).thenReturn(null);
		try {
			this.locationHibernateDAO.getCountersByLocation(1);
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void testCheckIPAddress() throws Exception {
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		final boolean bool = this.locationHibernateDAO.checkIPAddress("ip");
		assertTrue(bool);
		Mockito.when(this.query.uniqueResult()).thenThrow(
				new HibernateException(""));
		try {
			this.locationHibernateDAO.checkIPAddress("ip");
		} catch (final Exception e) {
			assertTrue(true);
		}
		beforeTestRuns();
		Mockito.when(
				this.query.setString(Matchers.anyString(), Matchers.anyString()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.locationHibernateDAO.checkIPAddress("ip");
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void testCheckCounter() throws Exception {
		Mockito.when(this.query.uniqueResult()).thenReturn(new Location());
		final boolean bool = this.locationHibernateDAO.checkCounter("ip");
		assertTrue(bool);
		Mockito.when(this.query.uniqueResult()).thenThrow(
				new HibernateException(""));
		try {
			this.locationHibernateDAO.checkCounter("ip");
		} catch (final Exception e) {
			assertTrue(true);
		}
		beforeTestRuns();
		Mockito.when(
				this.query.setString(Matchers.anyString(), Matchers.anyString()))
				.thenThrow(new EGOVRuntimeException(""));
		try {
			this.locationHibernateDAO.checkCounter("ip");
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

}
