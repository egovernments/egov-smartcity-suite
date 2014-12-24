package org.egov.utils;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Fund;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.FunctionwiseIE;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

public class ReportHelper {
	private static final int MB = 1024*1024;
	ByteArrayOutputStream outputBytes;
	private InputStream reportStream;
	
	public OutputStream getOutputBytes(){
		return outputBytes;
	}
	
	public InputStream exportXls(InputStream inputStream,String jasperPath,Map<String,Object> paramMap,List<Object> dataSource) throws JRException, IOException{
	    JRXlsExporter exporterXLS = new JRXlsExporter();
	    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,setUpAndGetJasperPrint(jasperPath,paramMap,dataSource));
	    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,outputBytes);
	    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,true);
	    exporterXLS.exportReport();
	    inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
	    closeStream(reportStream);
	    return inputStream;
	}
	
	public InputStream exportXls(InputStream inputStream,JasperPrint jasperPrint) throws JRException, IOException{
	    JRXlsExporter exporterXLS = new JRXlsExporter();
	    outputBytes = new ByteArrayOutputStream(1*MB);
	    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
	    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,outputBytes);
	    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,true);
	    exporterXLS.exportReport();
	    inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
	    closeStream(reportStream);
	    return inputStream;
	}
	
	protected void closeStream(InputStream stream){
		if(stream != null){
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected JasperPrint setUpAndGetJasperPrint(String jasperPath,Map<String,Object> paramMap,List<Object> dataSource) throws JRException{
		reportStream = this.getClass().getResourceAsStream(jasperPath);
	    outputBytes = new ByteArrayOutputStream(1*MB);
	    if(dataSource.size()>0)
	    	return JasperFillManager.fillReport(reportStream, paramMap,new JRBeanCollectionDataSource(dataSource));
	    return JasperFillManager.fillReport(reportStream, paramMap,new JREmptyDataSource());
	}
	
	public InputStream exportHtml(InputStream inputStream,String jasperPath,Map<String,Object> paramMap,List dataSource,String sizeUnitPoint) {
	    try {
			exportReport(setUpAndGetJasperPrint(jasperPath, paramMap, dataSource),sizeUnitPoint);
			inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
			closeStream(reportStream);
		} catch (JRException e) {
			throw new EGOVRuntimeException("report.exception",e);
		}
	    return inputStream;
	}

	public InputStream exportHtml(InputStream inputStream,JasperPrint jasperPrint) {
	    try {
	    	outputBytes = new ByteArrayOutputStream(1*MB);
			exportReport(jasperPrint, JRHtmlExporterParameter.SIZE_UNIT_POINT);
			inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
			closeStream(reportStream);
		} catch (JRException e) {
			throw new EGOVRuntimeException("report.exception",e);
		}
	    return inputStream;
	}
	
	public InputStream exportPdf(InputStream inputStream,String jasperPath,Map<String,Object> paramMap,List<Object> dataSource) throws JRException, IOException{
	    JasperExportManager.exportReportToPdfStream(setUpAndGetJasperPrint(jasperPath,paramMap,dataSource), outputBytes);
	    inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
	    closeStream(reportStream);
	    return inputStream;
	}
	
	public InputStream exportPdf(InputStream inputStream,JasperPrint jasperPrint) throws JRException, IOException{
		outputBytes = new ByteArrayOutputStream(1*MB);
	    JasperExportManager.exportReportToPdfStream(jasperPrint, outputBytes);
	    inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
	    closeStream(reportStream);
	    return inputStream;
	}
	
	public JasperPrint generateFinancialStatementReportJasperPrint(Statement balanceSheet,String heading,String fromDate,String toDate,boolean showScheduleColumn) throws Exception{
		Style detailAmountStyle = getDetailAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
	    drb = (FastReportBuilder) drb.addColumn("Account Code", "glCode", String.class.getName(),50)
	                        		 .addColumn("Head of Account", "accountName", String.class.getName(),100);
	    if(showScheduleColumn){
	    	drb.addColumn("Schedule No", "scheduleNo", String.class.getName(),60);
	    }
        drb.setTitle(heading+" "+balanceSheet.getFinancialYear().getFinYearRange())
	        .setSubtitle("Amount in " + balanceSheet.getCurrency())
	        .setPrintBackgroundOnOddRows(true) 
	        .setWhenNoData("No data", null)
	        .setDefaultStyles(getTitleStyle(), getSubTitleStyle(), getHeaderStyle(), getDetailStyle())
	        .setOddRowBackgroundStyle(getOddRowStyle())
	        .setDetailHeight(20)
	        .setHeaderHeight(30)
	        .setUseFullPageWidth(true);
	    drb.setPageSizeAndOrientation(new Page(612, 792, false));
	    for (Fund fund : balanceSheet.getFunds()) {
	    	drb.addColumn(fund.getName()+" (Rs)","fundWiseAmount."+fund.getName(),BigDecimal.class.getName(),55,false,"0.00",detailAmountStyle);
		}
        drb.addColumn("Totals As On: "+toDate+"(Rs)","currentYearTotal",BigDecimal.class.getName(),55,false,"0.00",detailAmountStyle)
        .addColumn("Totals As On: "+fromDate+"(Rs)","previousYearTotal",BigDecimal.class.getName(),55,false,"0.00",detailAmountStyle);
        DynamicReport dr = drb.build();
        JRDataSource ds = new JRBeanCollectionDataSource(balanceSheet.getEntries()); 
        return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
	}
	
	public JasperPrint generateBudgetReportJasperPrint(List inputData,String heading,boolean enableBeApproved,boolean enableReApproved,
			String lastYearRange,String currentYearRange,String nextYearRange) throws Exception{
		Style detailAmountStyle = getBudgetReportDetailAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
	    drb = (FastReportBuilder) drb.addColumn("Department Code", "departmentCode", String.class.getName(),50)
	    							 .addColumn("Function Code", "functionCode", String.class.getName(),50)
	    							 .addColumn("Account Head", "budgetGroupName", String.class.getName(),90)
	                        		 .addColumn("Actuals "+lastYearRange+"(Rs)", "actualsLastYear", BigDecimal.class.getName(),100,false,"0.00",detailAmountStyle)
	                        		 .addColumn("BE "+currentYearRange+"(Rs)", "beCurrentYearApproved", BigDecimal.class.getName(),100,false,"0.00",detailAmountStyle)
	                        		 .addColumn("RE Proposed "+currentYearRange+"(Rs)", "reCurrentYearOriginal", BigDecimal.class.getName(),100,false,"0.00",detailAmountStyle);
	    if(enableReApproved){
	    	drb.addColumn("RE Approved "+currentYearRange+"(Rs)", "reCurrentYearApproved", BigDecimal.class.getName(),100,false,"0.00",detailAmountStyle);
	    }
	    drb.addColumn("BE Proposed "+nextYearRange+"(Rs)", "beNextYearOriginal", BigDecimal.class.getName(),100,false,"0.00",detailAmountStyle);
	    if(enableBeApproved){
	    	drb.addColumn("BE Approved "+nextYearRange+"(Rs)", "beNextYearApproved", BigDecimal.class.getName(),100,false,"0.00",detailAmountStyle);
	    }
        drb.setTitle(heading)
	        .setWhenNoData("No data", null)
	        .setDefaultStyles(getTitleStyle(), getSubTitleStyle(), getHeaderStyle(), getBudgetReportDetailStyle())
	        .setDetailHeight(20)
	        .setHeaderHeight(30)
	        .setUseFullPageWidth(true);
	    drb.setPageSizeAndOrientation(new Page(612, 792, false));
        return DynamicJasperHelper.generateJasperPrint(drb.build(), new ClassicLayoutManager(), new JRBeanCollectionDataSource(inputData));
	}
	
	public JasperPrint generateFunctionwiseIEJasperPrint(FunctionwiseIE functionwiseIE,String cityName,String type) throws Exception
	{
		Style detailAmountStyle = getDetailAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
	    drb =  (FastReportBuilder) drb.addColumn("Sl.No.", "slNo", String.class.getName(),10)
	                        .addColumn("Function Code", "functionCode", String.class.getName(),20)
	                        .addColumn("Function Head", "functionName", String.class.getName(),50)
	                        .addColumn("Total "+type+" (Rs.)", "totalIncome",BigDecimal.class.getName(),50,false,"0.00",detailAmountStyle)
	                        .setTitle(cityName)
	                        .setSubtitle("FUNCTIONWISE "+type.toUpperCase()+" SUBSIDARY REGISTER")
	                        .setPrintBackgroundOnOddRows(true) 
	                        .setWhenNoData("No Data", null)
	                        .setDefaultStyles(getTitleStyle(), getSubTitleStyle(), getHeaderStyle(), getDetailStyle())
	                        .setOddRowBackgroundStyle(getOddRowStyle())
	                        .setDetailHeight(20)
	                        .setUseFullPageWidth(true);

	    for (String s : functionwiseIE.getMajorCodeList()) {
	    	drb.addColumn(s,"majorcodeWiseAmount."+s,BigDecimal.class.getName(),35,false,"0.00",detailAmountStyle);
		}
        DynamicReport dr = drb.build();
        JRDataSource ds = new JRBeanCollectionDataSource(functionwiseIE.getEntries()); 
        return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
	}

	private Style getOddRowStyle() {
		Style oddRowStyle = new Style();
		oddRowStyle.setBackgroundColor(new Color(247,247,247));
		oddRowStyle.setTransparency(Transparency.OPAQUE);
		return oddRowStyle;
	}

	private Style getSubTitleStyle() {
		Style subTitleStyle = new Style("titleStyle");
		subTitleStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
		subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return subTitleStyle;
	}

	private Style getTitleStyle() {
		Style titleStyle = new Style("titleStyle");
		titleStyle.setFont(new Font(14, Font._FONT_ARIAL, true));
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return titleStyle;
	}

	private Style getHeaderStyle() {
		Style headerStyle = new Style("header");
		headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerStyle.setBorder(Border.THIN());
		headerStyle.setBackgroundColor(new Color(204,204,204));
		headerStyle.setTextColor(Color.blue);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setFont(new Font(9, Font._FONT_ARIAL, true));
		return headerStyle;
	}

	private Style getDetailAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN());      
		detailAmountStyle.setBorderRight(Border.THIN());
		detailAmountStyle.setTextColor(Color.blue);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}

	private Style getBudgetReportDetailAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN());
		detailAmountStyle.setBorderRight(Border.THIN());
		detailAmountStyle.setBorderTop(Border.THIN());
		detailAmountStyle.setBorderBottom(Border.THIN());
		detailAmountStyle.setTextColor(Color.black);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}

	private Style getDetailStyle() {
		Style detailStyle = new Style("detail");
		detailStyle.setBorderLeft(Border.THIN());
		detailStyle.setBorderRight(Border.THIN());
		detailStyle.setTextColor(Color.blue);
		detailStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
		detailStyle.setTransparency(Transparency.OPAQUE);
		return detailStyle;
	}

	private Style getBudgetReportDetailStyle() {
		Style detailStyle = new Style("detail");
		detailStyle.setBorderLeft(Border.THIN());
		detailStyle.setBorderRight(Border.THIN());          
		detailStyle.setBorderTop(Border.THIN());
		detailStyle.setBorderBottom(Border.THIN());
		detailStyle.setTextColor(Color.black);
		detailStyle.setFont(new Font(8, Font._FONT_ARIAL, true));
		detailStyle.setTransparency(Transparency.OPAQUE);
		return detailStyle;
	}
	
	private void exportReport(JasperPrint jasperPrint,String sizeUnitPoint)throws JRException {
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT,jasperPrint);
		exporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,outputBytes);
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML,"");
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
		exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT,sizeUnitPoint);
		exporter.exportReport();
	}
}
