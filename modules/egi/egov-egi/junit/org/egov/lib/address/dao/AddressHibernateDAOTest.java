package org.egov.lib.address.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.address.model.Address;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

public class AddressHibernateDAOTest {
	private AddressHibernateDAO addressDao;
	private Session session;
	private Query query;
	private Address address;

	@Before
	public void runBefore() throws Exception {
		this.session = mock(Session.class);
		this.query = mock(Query.class);
		when(this.session.createQuery(Matchers.anyString())).thenReturn(
				this.query);
		when(this.session.isOpen()).thenReturn(true);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
		this.address = new Address();
		this.addressDao = new AddressHibernateDAO(Address.class, this.session);
	}

	@Test
	public void testGetAddress() throws Exception {
		when(this.query.uniqueResult()).thenReturn(this.address);
		assertEquals(this.address, this.addressDao.getAddress(1));
		when(this.query.uniqueResult()).thenThrow(new HibernateException(""));
		try {
			this.addressDao.getAddress(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
		runBefore();
		when(this.query.uniqueResult()).thenThrow(new RuntimeException(""));
		try {
			this.addressDao.getAddress(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

}
