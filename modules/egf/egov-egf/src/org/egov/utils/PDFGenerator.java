package org.egov.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *  To create the report in pdf format
 */
public class PDFGenerator 
{
	public final static Logger LOGGER = Logger.getLogger(PDFGenerator.class); 
	public void generateReport(HttpServletRequest req, HttpServletResponse resp,String jasperName,ArrayList al, HashMap paramMap) throws Exception
	{
		LOGGER.debug("generateReport method jasperName============="+jasperName);
		String jasperpath = paramMap.get("jasperpath").toString();
		LOGGER.debug("generateReport method jasperpath============="+jasperpath);
		InputStream reportStreamEar = this.getClass().getResourceAsStream("/"+jasperpath+jasperName+".jasper");
		LOGGER.debug("reportStreamEar==="+reportStreamEar);
		JasperReport bankAdviceReport = (JasperReport)JRLoader.loadObject(reportStreamEar);
		LOGGER.debug("bankAdviceReport=="+bankAdviceReport);
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(bankAdviceReport, paramMap, new JRBeanCollectionDataSource(al));
		LOGGER.debug("jasperPrint===="+jasperPrint);
		JRPdfExporter pdfExporter = new JRPdfExporter();
		List<JasperPrint> jasperPrintList= new ArrayList<JasperPrint>(); 
		jasperPrintList.add(jasperPrint);
		pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrintList);
	    pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME ,req.getRealPath("")+"/temp/"+jasperName+".pdf");
	    pdfExporter.exportReport();
	}
	public void generateReport(String jasperName,ArrayList al, HashMap paramMap) throws Exception
	{
		LOGGER.debug("generateReport method jasperName============="+jasperName);
		String jasperpath = paramMap.get("jasperpath").toString();
		LOGGER.debug("generateReport method jasperpath============="+jasperpath);
		InputStream reportStreamEar = this.getClass().getResourceAsStream("/"+jasperpath+jasperName+".jasper");
		LOGGER.debug("reportStreamEar==="+reportStreamEar);
		JasperReport bankAdviceReport = (JasperReport)JRLoader.loadObject(reportStreamEar);
		LOGGER.debug("bankAdviceReport=="+bankAdviceReport);
				
		JasperPrint jasperPrint = JasperFillManager.fillReport(bankAdviceReport, paramMap, new JRBeanCollectionDataSource(al));
		LOGGER.debug("jasperPrint===="+jasperPrint);
		JRPdfExporter pdfExporter = new JRPdfExporter();
		List<JasperPrint> jasperPrintList= new ArrayList<JasperPrint>(); 
		jasperPrintList.add(jasperPrint);
		pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrintList);
	   // pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME ,req.getRealPath("")+"/temp/"+jasperName+".pdf");
	    pdfExporter.exportReport();
	}
}
