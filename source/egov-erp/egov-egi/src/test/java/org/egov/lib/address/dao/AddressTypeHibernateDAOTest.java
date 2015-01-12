package org.egov.lib.address.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.address.model.Address;
import org.egov.lib.address.model.AddressTypeMaster;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

public class AddressTypeHibernateDAOTest {

	private Session session;
	private Query query;
	private AddressTypeMaster addressType;
	private AddressTypeHibernateDAO addressTypeDao;

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
		this.addressType = new AddressTypeMaster();
		this.addressTypeDao = new AddressTypeHibernateDAO(Address.class,
				this.session);
	}

	@Test
	public void testGetAddressType() throws Exception {
		when(this.query.uniqueResult()).thenReturn(this.addressType);
		assertEquals(this.addressType, this.addressTypeDao.getAddressType("1"));
		when(this.query.uniqueResult()).thenThrow(new HibernateException(""));
		try {
			this.addressTypeDao.getAddressType("1");
		} catch (final Exception e) {
			assertTrue(true);
		}
		runBefore();
		when(this.query.uniqueResult()).thenThrow(new RuntimeException(""));
		try {
			this.addressTypeDao.getAddressType("1");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

}
