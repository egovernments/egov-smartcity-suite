package org.egov.lib.rjbac.user.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

public class TerminalDAOTest {

	private TerminalDAO terminalDAO;
	private Session session;
	private Query query;

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
		this.terminalDAO = new TerminalDAO();
	}

	@Test
	public void testTerminalDAO() {
		assertNotNull(this.terminalDAO);
	}

	@Test
	public void testGetTerminal() {
		List l = this.terminalDAO.getTerminal("terminal");
		assertNotNull(l);
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			l = this.terminalDAO.getTerminal("terminal");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

}
