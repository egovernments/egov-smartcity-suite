/**
 * 
 */
package org.egov.infstr.reporting.viewer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.egov.infstr.cache.LRUCache;
import org.egov.infstr.reporting.engine.ReportConstants;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.junit.Before;
import org.junit.Test;

import servletunit.ServletOutputStreamSimulator;

/**
 * JUnit test cases for the report viewer servlet
 */
public class ReportViewerTest {

	private ReportViewer reportViewer;
	private HttpSession session;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private ByteArrayOutputStream baos;

	@Before
	public void setup() throws IOException {
		this.baos = new ByteArrayOutputStream();

		this.session = EasyMock.createMock(HttpSession.class);

		this.req = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(this.req.getSession()).andReturn(this.session);
		EasyMock.expect(
				this.req.getParameter(ReportConstants.REQ_PARAM_REPORT_ID))
				.andReturn("0");

		this.resp = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(this.resp.getOutputStream()).andReturn(
				new ServletOutputStreamSimulator(this.baos));
		this.resp.setHeader(EasyMock.isA(String.class),
				EasyMock.isA(String.class));
		this.resp.setContentType(EasyMock.isA(String.class));
		this.resp.setContentLength(EasyMock.gt(0));

		EasyMock.replay(this.req);
		EasyMock.replay(this.resp);

		this.reportViewer = new ReportViewer();
	}

	@Test
	public void testDoGet() throws IOException, ServletException {
		final ReportOutput reportOutput = new ReportOutput();
		reportOutput.setReportFormat(FileFormat.PDF);
		reportOutput.setReportOutputData("testdata".getBytes());
		final LRUCache<Integer, ReportOutput> reportOutputCache = new LRUCache<Integer, ReportOutput>(
				1, 1);
		reportOutputCache.put(0, reportOutput);

		EasyMock.expect(
				this.session
						.getAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP))
				.andReturn(reportOutputCache);
		EasyMock.replay(this.session);

		this.reportViewer.doGet(this.req, this.resp);

		EasyMock.verify(this.session);
		EasyMock.verify(this.req);
		EasyMock.verify(this.resp);

		final String output = this.baos.toString();
		assertTrue(output.equals("testdata"));
		assertTrue(reportOutputCache.size() == 0);
	}

	@Test(expected = NullPointerException.class)
	public void testDoGetNoReport() throws IOException, ServletException {
		EasyMock.expect(
				this.session
						.getAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP))
				.andReturn(null);
		EasyMock.replay(this.session);

		this.reportViewer.doGet(this.req, this.resp);

		EasyMock.verify(this.session);
		EasyMock.verify(this.req);
		EasyMock.verify(this.resp);

		final String output = this.baos.toString();
		assertFalse(output.equals("testdata"));
	}

	@Test
	public void testDoGetNoFormat() throws IOException, ServletException {
		final ReportOutput reportOutput = new ReportOutput();
		reportOutput.setReportFormat(null);
		reportOutput.setReportOutputData("testdata".getBytes());
		final LRUCache<Integer, ReportOutput> reportOutputCache = new LRUCache<Integer, ReportOutput>(
				1, 1);
		reportOutputCache.put(0, reportOutput);

		EasyMock.expect(
				this.session
						.getAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP))
				.andReturn(reportOutputCache);
		EasyMock.replay(this.session);

		this.reportViewer.doGet(this.req, this.resp);

		EasyMock.verify(this.session);
		EasyMock.verify(this.req);
		EasyMock.verify(this.resp);

		final String output = this.baos.toString();
		assertFalse(output.equals("testdata"));
		assertTrue(reportOutputCache.size() == 0);
	}

	@Test
	public void testDoGetNoReportData() throws IOException, ServletException {
		final ReportOutput reportOutput = new ReportOutput();
		reportOutput.setReportFormat(FileFormat.PDF);
		reportOutput.setReportOutputData(null);
		final LRUCache<Integer, ReportOutput> reportOutputCache = new LRUCache<Integer, ReportOutput>(
				1, 1);
		reportOutputCache.put(0, reportOutput);

		EasyMock.expect(
				this.session
						.getAttribute(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP))
				.andReturn(reportOutputCache);
		EasyMock.replay(this.session);

		this.reportViewer.doGet(this.req, this.resp);

		EasyMock.verify(this.session);
		EasyMock.verify(this.req);
		EasyMock.verify(this.resp);

		final String output = this.baos.toString();
		assertFalse(output.equals("testdata"));
		assertTrue(reportOutputCache.size() == 0);
	}
}
