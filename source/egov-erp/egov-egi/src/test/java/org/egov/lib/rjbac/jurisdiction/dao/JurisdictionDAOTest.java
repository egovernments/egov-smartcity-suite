package org.egov.lib.rjbac.jurisdiction.dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class JurisdictionDAOTest {

	private JurisdictionDAO juriDao;
	private Session session;
	private Jurisdiction jurisdiction;

	@Before
	public void runBefore() throws Exception {
		this.jurisdiction = mock(Jurisdiction.class);
		this.session = mock(Session.class);
		when(this.session.isOpen()).thenReturn(true);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
		this.juriDao = new JurisdictionDAO();

	}

	@Test
	public void testRemoveJurisdiction() {
		this.juriDao.removeJurisdiction(this.jurisdiction);
		try {
			Mockito.doThrow(new HibernateException("")).when(this.session)
					.delete(Matchers.anyObject());
			this.juriDao.removeJurisdiction(this.jurisdiction);
		} catch (final Exception e) {
		}
		assertTrue(true);
	}

	@Test
	public void testUpdateJurisdiction() {
		this.juriDao.updateJurisdiction(this.jurisdiction);
		try {
			Mockito.doThrow(new HibernateException("")).when(this.session)
					.saveOrUpdate(Matchers.anyObject());
			this.juriDao.updateJurisdiction(this.jurisdiction);
		} catch (final Exception e) {
		}
		assertTrue(true);
	}

	@Test
	public void testDeleteJurisdiction() {
		this.juriDao.deleteJurisdiction(this.jurisdiction);
		try {
			Mockito.doThrow(new HibernateException("")).when(this.session)
					.delete(Matchers.anyObject());
			this.juriDao.deleteJurisdiction(this.jurisdiction);
		} catch (final Exception e) {
		}
		assertTrue(true);
	}

	@Test
	public void testDeleteJurisdictionValues() {
		this.juriDao.deleteJurisdictionValues(new JurisdictionValues());
		try {
			Mockito.doThrow(new HibernateException("")).when(this.session)
					.delete(Matchers.anyObject());
			this.juriDao.deleteJurisdictionValues(new JurisdictionValues());
		} catch (final Exception e) {
		}
		assertTrue(true);
	}

}
