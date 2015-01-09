package org.egov.infstr.client.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import servletunit.HttpServletRequestSimulator;
import servletunit.ServletConfigSimulator;

public class HibSessionServletFilterTest extends EgovHibernateTest {

	HibSessionServletFilter filter;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.filter = new HibSessionServletFilter();
	}

	public void testTransactionRolledBackAndSessionClosedOnException()
			throws Exception {
		final Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.getTransaction();
		final Transaction originalTransaction = currentSession.getTransaction();
		try {
			final ServletConfigSimulator config = new ServletConfigSimulator();
			final HttpServletRequestSimulator request = new HttpServletRequestSimulator(
					config.getServletContext());
			this.filter.doFilter(request, null, new FilterChain() {
				@Override
				public void doFilter(final ServletRequest arg0,
						final ServletResponse arg1) throws IOException,
						ServletException {
					throw new ServletException();
				}
			});
			fail();
		} catch (final Exception e) {
			assertTrue(e!=null);
		}
		assertTrue("txn is not rolled back",
				originalTransaction.wasRolledBack());
		assertFalse("session is not closed", currentSession.isOpen());
	}

	public void testTransactionCommittedAndSessionClosedOnSuccess()
			throws Exception {
		final Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.getTransaction();

		final Transaction originalTransaction = currentSession.getTransaction();
		this.filter.doFilter(null, null, new FilterChain() {
			@Override
			public void doFilter(final ServletRequest arg0,
					final ServletResponse arg1) {

			}
		});
		assertTrue("txn is committed", originalTransaction.wasCommitted());
		assertFalse("session is not closed", currentSession.isOpen());
	}

}
