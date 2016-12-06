/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.reporting.engine.jasper;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.AbstractReportService;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Report service for generating reports using the JasperReports engine. Caches the report templates based on the template file to improve performance.
 */
public class JasperReportService extends AbstractReportService<JasperReport> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);
	public static final String TEMPLATE_EXTENSION = ".jasper";
	private static final String JASPER_PROPERTIES_FILE = "config/jasperreports.properties";

	@PersistenceContext
	private EntityManager entityManager;

	static {
		// Set the system property for jasperreports properties file
		System.setProperty(DefaultJasperReportsContext.PROPERTIES_FILE, JASPER_PROPERTIES_FILE);
	}

	/**
	 * @param templateCacheMinSize Minimum size of template cache
	 * @param templateCacheMaxSize Maximum size of template cache
	 */
	public JasperReportService(final int templateCacheMinSize, final int templateCacheMaxSize) {
		super(templateCacheMinSize, templateCacheMaxSize);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infra.reporting.engine.AbstractReportService#getTemplateExtension ()
	 */
	@Override
	protected String getTemplateExtension() {
		return TEMPLATE_EXTENSION;
	}

	/**
	 * Returns exporter for given report format, jasper print object and output stream
	 * @param reportInput Report Input object
	 * @param jasperPrint Jasper print object
	 * @param outputStream Report output stream
	 * @return exporter for given report format, jasper print object and output stream
	 */
	private JRExporter getExporter(final ReportRequest reportInput, final JasperPrint jasperPrint, final OutputStream outputStream) {
		JRExporter exporter;

		switch (reportInput.getReportFormat()) {

		case PDF:
			exporter = new JRPdfExporter();
			if (reportInput.isPrintDialogOnOpenReport()) {
				exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.print()");
			}
			break;

		case XLS:
			exporter = new JExcelApiExporter();
			break;

		case RTF:
			exporter = new JRRtfExporter();
			break;

		case HTM:
			exporter = new JRHtmlExporter();
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
			exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, "/images");
			break;

		case TXT:
			exporter = new JRTextExporter();
			// TBD: from Jasper 3.6.1 - can be externalized
			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, 6);
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 12);
			// exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 800);
			// exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 500);
			exporter.setParameter(JRTextExporterParameter.LINE_SEPARATOR, "\r\n");
			break;

		case CSV:
			exporter = new JRCsvExporter();
			break;

		default:
			throw new ApplicationRuntimeException("Invalid report format [" + reportInput.getReportFormat() + "]");
		}

		// Set common exporter parameters
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, ReportConstants.CHARACTER_ENCODING_UTF8);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		return exporter;
	}

	/**
	 * Creates report for given report template, format, data source and parameters
	 * @param reportInput Report Input object
	 * @param dataSource Data source
	 * @return Report output created using given input and data source
	 */
	private ReportOutput createReport(final ReportRequest reportInput, final JRDataSource dataSource) {
		try {
			final JasperPrint jasperPrint = JasperFillManager.fillReport(getTemplate(reportInput.getReportTemplate()), reportInput.getReportParams(), dataSource);

			final byte[] data = exportReport(reportInput, jasperPrint);

			return new ReportOutput(data, reportInput);
		} catch (final Exception e) {
			final String errMessage = "Exception in report creation!";
			LOGGER.error(errMessage, e);
			throw new ApplicationRuntimeException(errMessage, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infra.reporting.engine.AbstractReportService#createReportFromSql (org.egov.infra.reporting.engine.ReportRequest, java.sql.Connection)
	 */
	@Override
	protected ReportOutput createReportFromSql(final ReportRequest reportInput, final Connection connection) {
		try {
			final JasperPrint jasperPrint = JasperFillManager.fillReport(getTemplate(reportInput.getReportTemplate()), reportInput.getReportParams(), connection);

			final byte[] data = exportReport(reportInput, jasperPrint);

			return new ReportOutput(data, reportInput);
		} catch (final Exception e) {
			final String errMessage = "Exception in report creation!";
			LOGGER.error(errMessage, e);
			throw new ApplicationRuntimeException(errMessage, e);
		}
	}

	/**
	 * Exports the given jasper print object in required format by looking at the report input
	 * @param reportInput The report input
	 * @param jasperPrint The jasper print object
	 * @return The exported report in the form of a byte array
	 */
	private byte[] exportReport(final ReportRequest reportInput, final JasperPrint jasperPrint) throws JRException, IOException {
		try {
			final ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
			final JRExporter exporter = getExporter(reportInput, jasperPrint, reportOutputStream);

			exporter.exportReport();

			final byte[] data = reportOutputStream.toByteArray();
			reportOutputStream.close();

			return data;
		} catch (final Exception e) {
			final String errMsg = "Exception in export report!";
			LOGGER.error(errMsg, e);
			throw new ApplicationRuntimeException(errMsg, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @seeorg.egov.infstr.reporting.engine.AbstractReportService# createReportFromJavaBean(org.egov.infra.reporting.engine.ReportRequest)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected ReportOutput createReportFromJavaBean(final ReportRequest reportInput) {
		final Object reportData = reportInput.getReportInputData();
		JRDataSource dataSource = null;

		if (reportData == null) {
			dataSource = new JREmptyDataSource();
		} else if (reportData.getClass().isArray()) {
			dataSource = new JRBeanArrayDataSource((Object[]) reportData, false);
		} else if (reportData instanceof Collection) {
			dataSource = new JRBeanCollectionDataSource((Collection) reportData, false);
		} else {
			// Not an array/collection. Possibly a single object. Create a
			// data source with array of one object
			dataSource = new JRBeanArrayDataSource(new Object[] { reportData }, false);
		}

		return createReport(reportInput, dataSource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infra.reporting.engine.AbstractReportService#createReportFromHql (org.egov.infra.reporting.engine.ReportRequest)
	 */
	@Override
	protected ReportOutput createReportFromHql(final ReportRequest reportInput) {
		try {
			Map<String, Object> reportParams = reportInput.getReportParams();
			if (reportParams == null) {
				reportParams = new HashMap<String, Object>();
			}
			reportParams.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, entityManager.unwrap(Session.class));
			JasperReportsContext jrc = DefaultJasperReportsContext.getInstance();
			jrc.setValue(JRHibernateQueryExecuterFactory.PROPERTY_HIBERNATE_FIELD_MAPPING_DESCRIPTIONS, false);
			final JasperPrint jasperPrint = JasperFillManager.getInstance(jrc).fill(getTemplate(reportInput.getReportTemplate()), reportParams);
			final byte[] data = exportReport(reportInput, jasperPrint);
			return new ReportOutput(data, reportInput);
		} catch (final Exception e) {
			final String errMessage = "Exception in report creation!";
			LOGGER.error(errMessage, e);
			throw new ApplicationRuntimeException(errMessage, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infra.reporting.engine.AbstractReportService#loadTemplate(java .io.InputStream)
	 */
	@Override
	protected JasperReport loadTemplate(final InputStream templateInputStream) {
		try {
			return (JasperReport) JRLoader.loadObject(templateInputStream);
		} catch (final JRException e) {
			final String errMsg = "Exception while loading jasperreport template from inpust stream!";
			LOGGER.error(errMsg, e);
			throw new ApplicationRuntimeException(errMsg, e);
		}
	}
}
