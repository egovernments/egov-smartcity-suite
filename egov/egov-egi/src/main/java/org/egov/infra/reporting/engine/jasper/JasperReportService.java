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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleRtfExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.AbstractReportService;
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

import static org.egov.infra.reporting.engine.ReportConstants.FileFormat.CSV;
import static org.egov.infra.reporting.engine.ReportConstants.FileFormat.HTM;
import static org.egov.infra.reporting.engine.ReportConstants.FileFormat.PDF;
import static org.egov.infra.reporting.engine.ReportConstants.FileFormat.RTF;
import static org.egov.infra.reporting.engine.ReportConstants.FileFormat.TXT;
import static org.egov.infra.reporting.engine.ReportConstants.FileFormat.XLS;

public class JasperReportService extends AbstractReportService<JasperReport> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);

    private static final String TEMPLATE_EXTENSION = ".jasper";
    private static final String JASPER_PROPERTIES_FILE = "config/jasperreports.properties";
    private static final String EXCEPTION_IN_REPORT_CREATION = "Error occurred while generating report.";

    @PersistenceContext
    private EntityManager entityManager;

    public JasperReportService(int templateCacheMinSize, int templateCacheMaxSize) {
        super(templateCacheMinSize, templateCacheMaxSize);
        System.setProperty(DefaultJasperReportsContext.PROPERTIES_FILE, JASPER_PROPERTIES_FILE);
    }

    @Override
    protected String getTemplateExtension() {
        return TEMPLATE_EXTENSION;
    }

    @Override
    protected ReportOutput createReportFromSql(ReportRequest reportInput, Connection connection) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(getTemplate(reportInput.getReportTemplate()),
                    reportInput.getReportParams(), connection);
            return new ReportOutput(exportReport(reportInput, jasperPrint), reportInput);
        } catch (JRException | IOException e) {
            LOGGER.error(EXCEPTION_IN_REPORT_CREATION, e);
            throw new ApplicationRuntimeException(EXCEPTION_IN_REPORT_CREATION, e);
        }
    }

    @Override
    protected ReportOutput createReportFromJavaBean(ReportRequest reportInput) {
        try {
            Object reportData = reportInput.getReportInputData();
            JRDataSource dataSource;
            if (reportData == null) {
                dataSource = new JREmptyDataSource();
            } else if (reportData.getClass().isArray()) {
                dataSource = new JRBeanArrayDataSource((Object[]) reportData, false);
            } else if (reportData instanceof Collection) {
                dataSource = new JRBeanCollectionDataSource((Collection) reportData, false);
            } else {
                dataSource = new JRBeanArrayDataSource(new Object[]{reportData}, false);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(getTemplate(reportInput.getReportTemplate()),
                    reportInput.getReportParams(), dataSource);
            return new ReportOutput(exportReport(reportInput, jasperPrint), reportInput);
        } catch (JRException | IOException e) {
            LOGGER.error(EXCEPTION_IN_REPORT_CREATION, e);
            throw new ApplicationRuntimeException(EXCEPTION_IN_REPORT_CREATION, e);
        }

    }

    @Override
    protected ReportOutput createReportFromHql(ReportRequest reportInput) {
        try {
            Map<String, Object> reportParams = reportInput.getReportParams();
            if (reportParams == null) {
                reportParams = new HashMap<>();
            }
            reportParams.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, entityManager.unwrap(Session.class));
            JasperReportsContext jrc = DefaultJasperReportsContext.getInstance();
            jrc.setValue(JRHibernateQueryExecuterFactory.PROPERTY_HIBERNATE_FIELD_MAPPING_DESCRIPTIONS, false);
            JasperPrint jasperPrint = JasperFillManager.getInstance(jrc).fill(getTemplate(reportInput.getReportTemplate()), reportParams);
            return new ReportOutput(exportReport(reportInput, jasperPrint), reportInput);
        } catch (Exception e) {
            LOGGER.error(EXCEPTION_IN_REPORT_CREATION, e);
            throw new ApplicationRuntimeException(EXCEPTION_IN_REPORT_CREATION, e);
        }
    }

    @Override
    protected JasperReport loadTemplate(InputStream templateInputStream) {
        try {
            return (JasperReport) JRLoader.loadObject(templateInputStream);
        } catch (JRException e) {
            LOGGER.error(EXCEPTION_IN_REPORT_CREATION, e);
            throw new ApplicationRuntimeException(EXCEPTION_IN_REPORT_CREATION, e);
        }
    }

    private byte[] exportReport(ReportRequest reportInput, JasperPrint jasperPrint) throws JRException, IOException {
        try (ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream()) {
            Exporter exporter = getExporter(reportInput, jasperPrint, reportOutputStream);
            exporter.exportReport();
            return reportOutputStream.toByteArray();
        } catch (Exception e) {
            LOGGER.error(EXCEPTION_IN_REPORT_CREATION, e);
            throw new ApplicationRuntimeException(EXCEPTION_IN_REPORT_CREATION, e);
        }
    }

    private Exporter getExporter(ReportRequest reportInput, JasperPrint jasperPrint, OutputStream outputStream) {
        Exporter exporter;
        ExporterConfiguration exporterConfiguration;
        ExporterOutput exporterOutput = null;
        if (PDF.equals(reportInput.getReportFormat())) {
            SimplePdfExporterConfiguration pdfExporterConfiguration = new SimplePdfExporterConfiguration();
            if (reportInput.isPrintDialogOnOpenReport())
                pdfExporterConfiguration.setPdfJavaScript("this.print()");
            exporter = new JRPdfExporter();
            exporterConfiguration = pdfExporterConfiguration;
        } else if (XLS.equals(reportInput.getReportFormat())) {
            exporter = new JRXlsExporter();
            exporterConfiguration = new SimpleXlsExporterConfiguration();
        } else if (RTF.equals(reportInput.getReportFormat())) {
            exporter = new JRRtfExporter();
            exporterConfiguration = new SimpleRtfExporterConfiguration();
        } else if (HTM.equals(reportInput.getReportFormat())) {
            exporter = new HtmlExporter();
            exporterConfiguration = new SimpleHtmlExporterConfiguration();
            exporterOutput = new SimpleHtmlExporterOutput(outputStream);
        } else if (TXT.equals(reportInput.getReportFormat())) {
            exporter = new JRTextExporter();
            exporterConfiguration = new SimpleTextExporterConfiguration();
        } else if (CSV.equals(reportInput.getReportFormat())) {
            exporter = new JRCsvExporter();
            exporterConfiguration = new SimpleCsvExporterConfiguration();
        } else {
            throw new ApplicationRuntimeException("Invalid report format [" + reportInput.getReportFormat() + "]");
        }

        exporter.setConfiguration(exporterConfiguration);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(exporterOutput == null ? new SimpleOutputStreamExporterOutput(outputStream) : exporterOutput);
        return exporter;
    }
}
