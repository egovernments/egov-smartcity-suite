/**
 * 
 */
package org.egov.infstr.reporting.engine.jasper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for testing JasperReportService
 */
@SuppressWarnings("unchecked")
public class JasperReportServiceTest extends AbstractPersistenceServiceTest {
	private ReportService reportService;
	private static final String beanReportTemplateName = "testReportJavaBean";
	private static final String sqlReportTemplateName = "testReportSql";
	private static final String hqlReportTemplateName = "testReportHql";
	private static final String invalidTemplateName = "abcdefgh_invalid_template";
	private final TestReportData testReportData[] = new TestReportData[3];

	public static class TestReportData {
		private final String name;

		public TestReportData(final String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	@Before
	public void setUp() {
		this.reportService = new JasperReportService(2, 2);
		this.testReportData[0] = new TestReportData("TestReportData1");
		this.testReportData[1] = new TestReportData("TestReportData2");
		this.testReportData[2] = new TestReportData("TestReportData3");
	}

	@Test
	public void testIsValidTemplate() {
		assertTrue(this.reportService.isValidTemplate(beanReportTemplateName));
		assertTrue(this.reportService.isValidTemplate(sqlReportTemplateName));
		assertTrue(this.reportService.isValidTemplate(hqlReportTemplateName));
		assertFalse(this.reportService.isValidTemplate(invalidTemplateName));
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testCreateBeanReportWithInvalidTemplate() {
		final ReportRequest reportInput = new ReportRequest(
				invalidTemplateName, this.testReportData[0], null);
		this.reportService.createReport(reportInput);
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testCreateSqlReportWithInvalidTemplate() {
		final ReportRequest reportInput = new ReportRequest(
				invalidTemplateName, null, ReportDataSourceType.SQL);
		this.reportService.createReport(reportInput);
	}

	@Test
	public void testCreateReportWithJavaBeanNull() {
		final ReportRequest reportInput = new ReportRequest(
				beanReportTemplateName, (Object) null, null);
		createReport(reportInput);
	}

	@Test
	public void testCreateReportWithJavaBeanSingle() {
		final ReportRequest reportInput = new ReportRequest(
				beanReportTemplateName, this.testReportData[0], null);
		createReport(reportInput);
	}

	@Test
	public void testCreateReportWithJavaBeanArray() {
		final ReportRequest reportInput = new ReportRequest(
				beanReportTemplateName, this.testReportData, null);
		createReport(reportInput);
	}

	@Test
	public void testCreateReportWithJavaBeanCollection() {
		final ReportRequest reportInput = new ReportRequest(
				beanReportTemplateName, Arrays.asList(this.testReportData),
				null);
		createReport(reportInput);
	}

	/**
	 * Creates report and checks that report output is created properly
	 * 
	 * @param reportInput
	 *            the report input object
	 */
	private void createReport(final ReportRequest reportInput) {
		final ReportOutput reportOutput = this.reportService
				.createReport(reportInput);

		assertNotNull(reportOutput);
		assertTrue(reportOutput.getReportOutputData().length > 0);
		assertTrue(reportOutput.getReportFormat() == reportInput
				.getReportFormat());
	}

	@Test
	public void testCreateReportWithSql() {
		// Test PDF report
		ReportRequest reportInput = new ReportRequest(sqlReportTemplateName,
				null, ReportDataSourceType.SQL);
		createReport(reportInput);

		// Test RTF report
		reportInput = new ReportRequest(sqlReportTemplateName, null,
				ReportDataSourceType.SQL);
		reportInput.setReportFormat(FileFormat.RTF);
		createReport(reportInput);

		// Test XLS report
		reportInput = new ReportRequest(sqlReportTemplateName, null,
				ReportDataSourceType.SQL);
		reportInput.setReportFormat(FileFormat.XLS);
		createReport(reportInput);

		// Test HTM report
		reportInput = new ReportRequest(sqlReportTemplateName, null,
				ReportDataSourceType.SQL);
		reportInput.setReportFormat(FileFormat.HTM);
		createReport(reportInput);
	}

	@Test
	public void testCreateReportWithHql() {
		final ReportRequest reportInput = new ReportRequest(
				hqlReportTemplateName, null, ReportDataSourceType.HQL);
		createReport(reportInput);
	}

	@Test
	public void testCreateReportWithSqlAndPrintDialog() {
		final ReportRequest reportInput = new ReportRequest(
				sqlReportTemplateName, null, ReportDataSourceType.SQL);
		reportInput.setPrintDialogOnOpenReport(true);
		createReport(reportInput);
	}
}
