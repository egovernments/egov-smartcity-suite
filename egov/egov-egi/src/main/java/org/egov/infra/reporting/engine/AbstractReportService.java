/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.reporting.engine;

import org.egov.infra.cache.impl.LRUCache;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.jasper.JasperReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract report service providing common eGov reporting functionality.
 * eGov infrastructure uses JasperReports for creating reports {@link JasperReportService}.
 * Any other third party reporting framework can be supported by implementing a class that
 * extends from {@link AbstractReportService} and then configuring that class in the global bean definitions xml.
 */
public abstract class AbstractReportService<T> implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReportService.class);

    /**
     * The report template cache. Most frequently used report
     * templates are cached in memory to improve performance of report generation.
     */
    private LRUCache<String, T> templateCache;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${dev.mode}")
    private boolean devMode;

    /**
     * @param templateCacheMinSize Minimum size of template cache
     * @param templateCacheMaxSize Maximum size of template cache
     */
    public AbstractReportService(int templateCacheMinSize, int templateCacheMaxSize) {
        this.templateCache = new LRUCache<>(templateCacheMinSize, templateCacheMaxSize);
    }

    /**
     * Creates a report using given report input where the report data source is java beans
     *
     * @param reportInput The report input
     * @return The report output
     */
    protected abstract ReportOutput createReportFromJavaBean(ReportRequest reportInput);

    /**
     * Creates report for given report template, format, connection and parameters
     *
     * @param reportInput Report Input object
     * @param connection  JDBC connection
     * @return Report output for given report template, format, data source and parameters.
     */
    protected abstract ReportOutput createReportFromSql(ReportRequest reportInput, Connection connection);

    /**
     * Creates report using a template that uses HQL for fetching data
     *
     * @param reportInput The report input
     * @return Report output created using given input
     */
    protected abstract ReportOutput createReportFromHql(ReportRequest reportInput);

    /**
     * @return Extension of the report templates supported by the report service
     */
    protected abstract String getTemplateExtension();

    /**
     * @param templateInputStream Input stream from which the report template is to be loaded
     * @return The report template object
     */
    protected abstract T loadTemplate(InputStream templateInputStream);

    @Override
    public ReportOutput createReport(ReportRequest reportInput) {
        switch (reportInput.getReportDataSourceType()) {
            case JAVABEAN:
                return createReportFromJavaBean(reportInput);
            case SQL:
                return createReportFromSql(reportInput);
            case HQL:
                return createReportFromHql(reportInput);
            default:
                throw new ApplicationRuntimeException("Invalid report data source type [" + reportInput.getReportDataSourceType() + "]");
        }
    }

    /**
     * Creates report using a JDBC connection
     *
     * @param reportInput The report input
     * @return The report output
     */
    protected ReportOutput createReportFromSql(ReportRequest reportInput) {
        JdbcReportWork reportWork = new JdbcReportWork(reportInput);
        entityManager.unwrap(Session.class).doWork(reportWork);
        return reportWork.getReportOutput();
    }

    /**
     * Returns the Report Template object for given template path. Fetches it from the template cache is available;
     * else loads the template from disk.
     *
     * @param templateName Name of the Report template (without extension)
     * @return Report object for given template path.
     */
    protected T getTemplate(String templateName) {

        T reportTemplate = this.templateCache.get(ApplicationThreadLocals.getTenantID() + templateName);

        if (devMode || reportTemplate == null) {
            try {
                reportTemplate = loadTemplate(ReportUtil.getTemplateAsStream(templateName + getTemplateExtension()));
                this.templateCache.put(ApplicationThreadLocals.getTenantID() + templateName, reportTemplate);
                if (reportTemplate == null) {
                    String errMsg = "Report template [" + templateName + "] could not be loaded";
                    LOGGER.error(errMsg);
                    throw new ApplicationRuntimeException(errMsg);
                }
            } catch (ApplicationRuntimeException e) {
                String errMsg = "Exception in getting report template [" + templateName + "]";
                LOGGER.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }
        }
        return reportTemplate;
    }

    @Override
    public boolean isValidTemplate(String templateName) {
        T report = null;
        try {
            report = getTemplate(templateName);
        } catch (Exception e) {
            LOGGER.error(templateName + " is not a valid template name.", e);
        }

        return report != null;
    }

    private class JdbcReportWork implements Work {
        private ReportRequest reportInput;
        private ReportOutput reportOutput;

        JdbcReportWork(ReportRequest reportInput) {
            this.reportInput = reportInput;
        }

        public ReportOutput getReportOutput() {
            return this.reportOutput;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
            this.reportOutput = createReportFromSql(this.reportInput, connection);
        }
    }
}
