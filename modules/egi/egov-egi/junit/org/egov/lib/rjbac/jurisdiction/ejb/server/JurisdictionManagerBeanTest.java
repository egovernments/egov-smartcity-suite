package org.egov.lib.rjbac.jurisdiction.ejb.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.jurisdiction.ejb.api.JurisdictionService;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

public class JurisdictionManagerBeanTest {
	private JurisdictionService juriManager;
	private Session session;
	private Jurisdiction jurisdiction;

	@Before
	public void runBefore() throws Exception {
		this.juriManager = new JurisdictionServiceImpl();
		this.jurisdiction = mock(Jurisdiction.class);
		this.session = mock(Session.class);
		when(this.session.isOpen()).thenReturn(true);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);

	}

	@Test
	public void testRemoveJurisdiction() {
		this.juriManager.removeJurisdiction(this.jurisdiction);
		assertTrue(true);
	}

	@Test
	public void testUpdateJurisdiction() {
		this.juriManager.updateJurisdiction(this.jurisdiction);
		assertTrue(true);
	}

	@Test
	public void testDeleteJurisdiction() {
		this.juriManager.deleteJurisdiction(this.jurisdiction);
		assertTrue(true);
	}

	@Test
	public void testDeleteJurisdictionValues() {
		this.juriManager.deleteJurisdictionValues(new JurisdictionValues());
		assertTrue(true);
	}

}
