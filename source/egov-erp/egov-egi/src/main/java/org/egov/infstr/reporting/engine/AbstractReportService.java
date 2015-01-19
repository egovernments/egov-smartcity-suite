/*
 * @(#)AbstractReportService.java 3.0, 17 Jun, 2013 2:59:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.reporting.engine;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.cache.LRUCache;
import org.egov.infstr.reporting.engine.jasper.JasperReportService;
import org.egov.infstr.reporting.util.ReportUtil;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract report service providing common eGov reporting functionality. eGov infrastructure uses JasperReports for creating reports {@link JasperReportService}. Any other third party reporting framework can be supported by implementing a class that extends from {@link AbstractReportService} and
 * then configuring that class in the global bean definitions xml.
 */
public abstract class AbstractReportService<T> implements ReportService {
	/**
	 * The report template cache. Most frequently used report templates are cached in memory to improve performance of report generation.
	 */
	private LRUCache<String, T> templateCache;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReportService.class);

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Creates a report using given report input where the report data source is java beans
	 * @param reportInput The report input
	 * @return The report output
	 */
	abstract protected ReportOutput createReportFromJavaBean(ReportRequest reportInput);

	/**
	 * Creates report for given report template, format, connection and parameters
	 * @param reportInput Report Input object
	 * @param dataSource Data source
	 * @return Report output for given report template, format, data source and parameters.
	 */
	abstract protected ReportOutput createReportFromSql(ReportRequest reportInput, Connection connection);

	/**
	 * Creates report using a template that uses HQL for fetching data
	 * @param reportInput The report input
	 * @return Report output created using given input
	 */
	abstract protected ReportOutput createReportFromHql(ReportRequest reportInput);

	/**
	 * @return Extension of the report templates supported by the report service
	 */
	abstract protected String getTemplateExtension();

	/**
	 * @param templateInputStream Input stream from which the report template is to be loaded
	 * @return The report template object
	 */
	abstract protected T loadTemplate(InputStream templateInputStream);

	/**
	 * Initializes the report service (sets up the report template cache)
	 * @param templateCacheMinSize Minimum size of template cache
	 * @param templateCacheMaxSize Maximum size of template cache
	 */
	private void initialize(final int templateCacheMinSize, final int templateCacheMaxSize) {
		this.templateCache = new LRUCache<String, T>(templateCacheMinSize, templateCacheMaxSize);
	}

	/**
	 * @param templateCacheMinSize Minimum size of template cache
	 * @param templateCacheMaxSize Maximum size of template cache
	 */
	public AbstractReportService(final int templateCachMinSize, final int templateCacheMaxSize) {
		initialize(templateCachMinSize, templateCacheMaxSize);
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.reporting.engine.ReportService#createReport(org.egov. infstr.reporting.engine.ReportInput)
	 */
	@Override
	public ReportOutput createReport(final ReportRequest reportInput) {
		switch (reportInput.getReportDataSourceType()) {
		case JAVABEAN:
			return createReportFromJavaBean(reportInput);
		case SQL:
			return createReportFromSql(reportInput);
		case HQL:
			return createReportFromHql(reportInput);
		default:
			throw new EGOVRuntimeException("Invalid report data source type [" + reportInput.getReportDataSourceType() + "]");
		}
	}

	/**
	 * Creates report using a JDBC connection
	 * @param reportInput The report input
	 * @return The report output
	 */
	protected ReportOutput createReportFromSql(final ReportRequest reportInput) {
		// Hibernate Session.connection() is deprecated. Hence using the Work
		// contract for performing discrete JDBC operation.
		final JdbcReportWork reportWork = new JdbcReportWork(reportInput);
		sessionFactory.getCurrentSession().doWork(reportWork);
		return reportWork.getReportOutput();
	}

	/**
	 * Returns the Report Template object for given template path. Fetches it from the template cache is available; else loads the template from disk.
	 * @param templateName Name of the Report template (without extension)
	 * @return Report object for given template path.
	 */
	protected T getTemplate(final String templateName) {
		String errMsg = null;
		// Check if the report template is available in the cache
		T reportTemplate = this.templateCache.get(templateName);

		if (reportTemplate == null) {
			// not found in cache. Try to load the template
			try {
				final InputStream templateInputStream = ReportUtil.getTemplateAsStream(templateName + getTemplateExtension());
				reportTemplate = loadTemplate(templateInputStream);

				// Loaded successfully. Add to cache.
				this.templateCache.put(templateName, reportTemplate);

				if (reportTemplate == null) {
					errMsg = "Report template [" + templateName + "] could not be loaded";
					LOGGER.error(errMsg);
					throw new EGOVRuntimeException(errMsg);
				}
			} catch (final Exception e) {
				errMsg = "Exception in getting report template [" + templateName + "]";
				LOGGER.error(errMsg, e);
				throw new EGOVRuntimeException(errMsg, e);
			}
		}
		return reportTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.reporting.engine.ReportService#isValidTemplate(java.lang .String)
	 */
	@Override
	public boolean isValidTemplate(final String templateName) {
		T report = null;

		try {
			report = getTemplate(templateName);
		} catch (final Exception e) {
			// Template could not be loaded, which means it is not valid.
			LOGGER.error(templateName + " is not a valid template name.", e);
		}

		return (report != null);
	}

	/**
	 * Inner class used to generate report using SQL connection
	 */
	private class JdbcReportWork implements Work {
		private final ReportRequest reportInput;
		private ReportOutput reportOutput;

		/**
		 * Constructor
		 * @param reportInput The report input
		 */
		public JdbcReportWork(final ReportRequest reportInput) {
			this.reportInput = reportInput;
		}

		/**
		 * @return the Report Output
		 */
		public ReportOutput getReportOutput() {
			return this.reportOutput;
		}

		@Override
		public void execute(final Connection connection) throws SQLException {
			this.reportOutput = createReportFromSql(this.reportInput, connection);
		}
	}
}
