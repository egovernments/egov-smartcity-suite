package org.egov.infstr.client.filter;

import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.documents.Notice;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import servletunit.ServletConfigSimulator;

import javax.jcr.RepositoryException;
import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

import static org.mockito.Mockito.*;

@Ignore
public class NoticeSaveFilterTest extends EgovHibernateTest {

	NoticeSaveFilter filter;
	XmlWebApplicationContext webApplicationContext;
	DocumentManagerService docManager;

	@Override
	@Before
	public void setUp() throws ServletException, Exception {
		super.setUp();
		this.filter = new NoticeSaveFilter();
		this.webApplicationContext = mock(XmlWebApplicationContext.class);
		this.docManager = mock(DocumentManagerService.class);
		when(this.webApplicationContext.getBean("documentManagerService"))
				.thenReturn(this.docManager);
		EGOVThreadLocals.setDomainName("localhost");
		EGOVThreadLocals.setUserId("1");
		final FilterConfig config = new MockFilterConfig();
		config.getServletContext().setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				this.webApplicationContext);
		this.filter.init(config);
	}

	@Test
	public void testWithNoticeObject() throws IOException, ServletException,
			IllegalAccessException, RepositoryException {

		final ServletConfigSimulator config = new ServletConfigSimulator();
		final HttpServletRequestSimulator request = new HttpServletRequestSimulator(
				config.getServletContext());
		request.setAttribute("noticeObject", getNoticeObj());
		final HttpServletResponseSimulator response = new HttpServletResponseSimulator();
		// response.setOutputStream(out);
		this.filter.doFilter(request, response, new FilterChain() {
			@Override
			public void doFilter(final ServletRequest arg0,
					final ServletResponse arg1) {

			}
		});
		verify(this.docManager).addDocumentObject(Matchers.any(Notice.class));
	}

	@Test
	public void testWithNoNoticeObject() throws IOException, ServletException,
			IllegalAccessException, RepositoryException {

		final ServletConfigSimulator config = new ServletConfigSimulator();
		final HttpServletRequestSimulator request = new HttpServletRequestSimulator(
				config.getServletContext());
		final HttpServletResponseSimulator response = new HttpServletResponseSimulator();

		this.filter.doFilter(request, response, new FilterChain() {
			@Override
			public void doFilter(final ServletRequest arg0,
					final ServletResponse arg1) {

			}
		});
		verify(this.webApplicationContext, never()).getBean(
				Matchers.anyString());
		verify(this.docManager, never()).addDocumentObject(
				Matchers.any(Notice.class));
	}

	private Notice getNoticeObj() {
		final Notice notice = new Notice("doc123", "EGI", "Notice6", new Date());
		return notice;
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		this.docManager = null;
		this.webApplicationContext = null;
		this.filter = null;

	}
}
