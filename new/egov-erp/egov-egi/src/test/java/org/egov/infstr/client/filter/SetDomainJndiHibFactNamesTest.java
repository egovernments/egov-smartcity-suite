package org.egov.infstr.client.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import servletunit.ServletConfigSimulator;

import static org.junit.Assert.assertEquals;

@Ignore
public class SetDomainJndiHibFactNamesTest {
	private SetDomainJndiHibFactNames domainJndiHibFactNames;
	protected HttpServletRequestSimulator request;
	protected HttpServletResponseSimulator response;
	protected ServletConfigSimulator config;

	@Before
	public void setUp() {
		this.domainJndiHibFactNames = new SetDomainJndiHibFactNames();
		this.config = new ServletConfigSimulator();
		this.request = new HttpServletRequestSimulator(
				this.config.getServletContext());
		this.response = new HttpServletResponseSimulator();
	}

	@After
	public void tearDown() {
		this.domainJndiHibFactNames = null;
	}

	@Test
	public void setThreadLocals() {
		this.request.setRequestURL("http://www.proll.org:8380/egi/eGov.jsp");
		this.request.setContextPath("/egi");
		SetDomainJndiHibFactNames.setThreadLocals(this.request);
		assertEquals(
				"http://www.proll.org:8380/egi/j_acegi_cas_security_check",
				EGOVThreadLocals.getUserService());

	}

	@Test
	public void ReqUrlWrongWithoutContextPath() {
		this.request.setRequestURL("http://www.proll.org:8380/eGov.jsp");
		SetDomainJndiHibFactNames.setThreadLocals(this.request);
		assertEquals("http://www.proll.org:8380/j_acegi_cas_security_check",
				EGOVThreadLocals.getUserService());

	}

}
