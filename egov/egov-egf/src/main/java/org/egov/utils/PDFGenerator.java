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
package org.egov.utils;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * To create the report in pdf format
 */
public class PDFGenerator
{
    public final static Logger LOGGER = Logger.getLogger(PDFGenerator.class);

    public void generateReport(final HttpServletRequest req, final HttpServletResponse resp, final String jasperName,
            final ArrayList al, final HashMap paramMap)
            throws Exception
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("generateReport method jasperName=============" + jasperName);
        final String jasperpath = paramMap.get("jasperpath").toString();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("generateReport method jasperpath=============" + jasperpath);
        final InputStream reportStreamEar = this.getClass().getResourceAsStream("/" + jasperpath + jasperName + ".jasper");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("reportStreamEar===" + reportStreamEar);
        final JasperReport bankAdviceReport = (JasperReport) JRLoader.loadObject(reportStreamEar);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("bankAdviceReport==" + bankAdviceReport);

        final JasperPrint jasperPrint = JasperFillManager.fillReport(bankAdviceReport, paramMap, new JRBeanCollectionDataSource(
                al));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("jasperPrint====" + jasperPrint);
        final JRPdfExporter pdfExporter = new JRPdfExporter();
        final List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(jasperPrint);
        pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, req.getRealPath("") + "/temp/" + jasperName + ".pdf");
        pdfExporter.exportReport();
    }

    public void generateReport(final String jasperName, final ArrayList al, final HashMap paramMap) throws Exception
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("generateReport method jasperName=============" + jasperName);
        final String jasperpath = paramMap.get("jasperpath").toString();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("generateReport method jasperpath=============" + jasperpath);
        final InputStream reportStreamEar = this.getClass().getResourceAsStream("/" + jasperpath + jasperName + ".jasper");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("reportStreamEar===" + reportStreamEar);
        final JasperReport bankAdviceReport = (JasperReport) JRLoader.loadObject(reportStreamEar);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("bankAdviceReport==" + bankAdviceReport);

        final JasperPrint jasperPrint = JasperFillManager.fillReport(bankAdviceReport, paramMap, new JRBeanCollectionDataSource(
                al));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("jasperPrint====" + jasperPrint);
        final JRPdfExporter pdfExporter = new JRPdfExporter();
        final List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(jasperPrint);
        pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        // pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME ,req.getRealPath("")+"/temp/"+jasperName+".pdf");
        pdfExporter.exportReport();
    }
}
