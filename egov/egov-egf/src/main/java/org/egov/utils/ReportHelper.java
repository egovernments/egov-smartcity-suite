package org.egov.utils;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.budget.BudgetProposalBean;
import org.egov.model.report.ReportBean;
import org.egov.web.actions.report.BudgetReAppReportBean;
import org.egov.web.actions.report.CommonReportBean;
import org.egov.web.actions.report.DepartmentwiseExpenditureReport;
import org.egov.web.actions.report.DepartmentwiseExpenditureResult;
import org.egov.web.actions.report.FunctionwiseIE;
import org.egov.web.actions.report.ReportSearch;
import org.egov.web.actions.report.Statement;
import org.jfree.ui.HorizontalAlignment;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DJDataSource;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.Subreport;
import ar.com.fdvs.dj.domain.entities.SubreportParameter;

import com.exilant.eGov.src.reports.TrialBalanceBean;

public class ReportHelper {
	private static final int MB = 1024 * 1024;
	ByteArrayOutputStream outputBytes;
	private InputStream reportStream;
	private static final Logger LOGGER = Logger.getLogger(ReportHelper.class);
	private static SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	
	
	

	public OutputStream getOutputBytes() {
		return outputBytes;
	}

	public InputStream exportXls(InputStream inputStream, String jasperPath,Map<String, Object> paramMap, List<Object> dataSource)
			throws JRException, IOException {
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,setUpAndGetJasperPrint(jasperPath, paramMap, dataSource));
		exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,outputBytes);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,	true);
		exporterXLS.exportReport();
		inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
		closeStream(reportStream);
		return inputStream;
	}

	public InputStream exportXls(InputStream inputStream,JasperPrint jasperPrint) throws JRException, IOException {
		JRXlsExporter exporterXLS = new JRXlsExporter();
		outputBytes = new ByteArrayOutputStream(1 * MB);
		exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,jasperPrint);
		exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,outputBytes);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,true);
		exporterXLS.exportReport();
		inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
		closeStream(reportStream);
		return inputStream;
	}

	protected void closeStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Error" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	protected JasperPrint setUpAndGetJasperPrint(String jasperPath,
			Map<String, Object> paramMap, List<Object> dataSource)
			throws JRException {
		reportStream = this.getClass().getResourceAsStream(jasperPath);
		outputBytes = new ByteArrayOutputStream(1 * MB);
		if (dataSource.size() > 0)
			return JasperFillManager.fillReport(reportStream, paramMap,	new JRBeanCollectionDataSource(dataSource));
		return JasperFillManager.fillReport(reportStream, paramMap,	new JREmptyDataSource());
	}

	public InputStream exportHtml(InputStream inputStream, String jasperPath,
			Map<String, Object> paramMap, List dataSource, String sizeUnitPoint) {
		try {
			exportReport(setUpAndGetJasperPrint(jasperPath, paramMap,dataSource), sizeUnitPoint);
			inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
			closeStream(reportStream);
		} catch (JRException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error" + e.getMessage());
			throw new EGOVRuntimeException("report.exception", e);
		}
		return inputStream;
	}

	public InputStream exportHtml(InputStream inputStream,
			JasperPrint jasperPrint) {
		try {
			outputBytes = new ByteArrayOutputStream(1 * MB);
			exportReport(jasperPrint, JRHtmlExporterParameter.SIZE_UNIT_POINT);
			inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
			closeStream(reportStream);
		} catch (JRException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error" + e.getMessage());
			throw new EGOVRuntimeException("report.exception", e);
		}
		return inputStream;
	}
	public InputStream exportHtml(InputStream inputStream,
			JasperPrint jasperPrint,String sizeunitpoint) {
		try {
			outputBytes = new ByteArrayOutputStream(1 * MB);
			exportReport(jasperPrint, JRHtmlExporterParameter.SIZE_UNIT_POINT);
			inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
			closeStream(reportStream);
		} catch (JRException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Error" + e.getMessage());
			throw new EGOVRuntimeException("report.exception", e);
		}
		return inputStream;
	}

	public InputStream exportPdf(InputStream inputStream, String jasperPath,
			Map<String, Object> paramMap, List<Object> dataSource)
			throws JRException, IOException {
		JasperExportManager.exportReportToPdfStream(setUpAndGetJasperPrint(jasperPath, paramMap, dataSource), outputBytes);
		inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
		closeStream(reportStream);
		return inputStream;
	}

	public InputStream exportPdf(InputStream inputStream,JasperPrint jasperPrint) throws JRException, IOException {
		outputBytes = new ByteArrayOutputStream(1 * MB);
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputBytes);
		inputStream = new ByteArrayInputStream(outputBytes.toByteArray());
		closeStream(reportStream);
		return inputStream;
	}       
	
	public JasperPrint generateDepartmentwiseExpenditureJasperPrint(DepartmentwiseExpenditureReport deReport,  String heading,String subreportheading) throws JRException, IOException,Exception {
		Style detailAmountStyle = getConcurrenceAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
		Style columnStyle = getConcurrenceColumnStyle();
		Style headerStyle =getDepartmentTitleStyle();
		//HorizontalListBuilder header;// = cmp.horizontalList(); //new column header
				//  .setPrintWhenExpression(exp.printInFirstPage()); 
		
		drb = (FastReportBuilder) drb.addColumn("Sl No", "slNo", Integer.class.getName(),8,columnStyle);
		drb
				.addColumn("Department Name","departmentNm", String.class.getName(), 70,columnStyle)   
				.addColumn("Concurrence given \\n upto "+FORMATDDMMYYYY.format(deReport.getCurrentYearConcurrenceGivenUptoDate()),"concurrenceGiven", BigDecimal.class.getName(), 35,detailAmountStyle) ;
		
		for (String dt : deReport.getConcurrenceDateSet()) {
						drb.addColumn(dt , "dayAmountMap."+dt,BigDecimal.class.getName(),22, false,"0.00", detailAmountStyle);
			}
		drb.addColumn("Total Concurrence\\n given till"+FORMATDDMMYYYY.format(deReport.getToDate()),"totalConcurrenceGivenTillDate",
						BigDecimal.class.getName(),22,detailAmountStyle) ;
		
		
		// Added Blank Space for painting subtitle left and right aligned 
		drb.setTitle(heading)
		.setSubtitle("                                                                                                                                                                                                          Amount in Lakh"
				+" \\n"+"Fund :"+deReport.getFund().getName())
		.setPrintBackgroundOnOddRows(false).setWhenNoData("No data", null)
		.setDefaultStyles(getBudgetTitleStyle(), getDepartmentwiseSubTitleStyle(),getHeaderStyle(), getDetailStyle())
		.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(10)
		.setHeaderHeight(35).setUseFullPageWidth(true).setSubtitleStyle(getDepartmentwiseSubTitleStyle())
		.setSubtitleHeight(30).setTitleHeight(40);  
		drb.setFooterHeight(1000);     
		  
		drb.setPageSizeAndOrientation(Page.Page_Legal_Landscape());
		drb.addConcatenatedReport(createSubreport(deReport, subreportheading,drb));
	   	JRDataSource ds = new JRBeanCollectionDataSource  (deReport.getCurrentyearDepartmentList());
		JRDataSource ds1 = new JRBeanCollectionDataSource  (deReport.getPreviousyearDepartmentList());
		//drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
		DynamicReport dr = drb.build();
		Map myMap=new HashMap();
		myMap.put("subreportds",ds1);
		//dr.getOptions().setPrintColumnNames(false);
		//dr.getdprintColumnNames
		//dr.setProperties(myMap);  
		//dr.getOptions().set
		dr.getOptions().setIgnorePagination(true);
		//if(LOGGER.isInfoEnabled())     LOGGER.info(dr.getColumnsGroups());
	
		     //String generateJRXML = DynamicJasperHelper.generateJRXML(dr, new ClassicLayoutManager(), myMap,"UTF-8");   
		    // if(LOGGER.isInfoEnabled())     LOGGER.info(generateJRXML);
	        return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds, myMap);
	}
	
	public Subreport createSubreport(DepartmentwiseExpenditureReport deReport, String heading,FastReportBuilder mainrep) throws JRException, IOException,Exception{
		Style detailAmountStyle = getConcurrenceAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
		Style columnStyle = getConcurrenceColumnStyle();
		Style headerStyle =getDepartmentTitleStyle();
		drb = (FastReportBuilder) drb.addColumn("Sl No", "slNo", Integer.class.getName(),8,columnStyle);
		drb
				.addColumn("Department Name","departmentNm", String.class.getName(), 70,columnStyle) 
				.addColumn("Concurrence given \\n upto "+FORMATDDMMYYYY.format(deReport.getPreviousYearConcurrenceGivenUptoDate()),"concurrenceGiven",
						BigDecimal.class.getName(), 35,detailAmountStyle) ;
		
		for (String dt : deReport.getPreviousConcurrenceDateSet()) {
				drb.addColumn(dt , "dayAmountMap."+dt,BigDecimal.class.getName(),22, false,"0.00", detailAmountStyle);
		}
		drb.addColumn("Total Concurrence Given\\n till"+FORMATDDMMYYYY.format(deReport.getPreviousYearConcurrenceGivenTillDate()),
				"totalConcurrenceGivenTillDate",BigDecimal.class.getName(),22,detailAmountStyle) ;
		
		// Added Blank Space for painting subtitle left and right aligned 
		drb.setTitle(heading).setPrintBackgroundOnOddRows(false).setWhenNoData("No data", null)
		.setDefaultStyles(getBudgetTitleStyle(), getDepartmentwiseSubTitleStyle(),getHeaderStyle(), getDetailStyle())
		.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(10)
		.setHeaderHeight(35).setUseFullPageWidth(true)
		.setSubtitleHeight(30).setTitleHeight(40);  
		drb.setPageSizeAndOrientation(Page.Page_Legal_Landscape());
	
		 JRDataSource ds = new JRBeanCollectionDataSource  (deReport.getPreviousyearDepartmentList());		 
		 DJDataSource djds=new DJDataSource("subreportds",DJConstants.DATA_SOURCE_ORIGIN_PARAMETER , DJConstants.DATA_SOURCE_TYPE_JRDATASOURCE);
	
		 Subreport subRep = new Subreport ();
		 subRep.setLayoutManager (new ClassicLayoutManager ()); 
		 subRep.setDynamicReport (drb.build ());
		 subRep.setDatasource(djds);
		 subRep.setUseParentReportParameters(true);
	   
	    return subRep;
	}
  
	public JasperPrint generateIncomeExpenditureReportJasperPrint(
			Statement balanceSheet, String heading,
			String fromDate, String toDate,String subtitle, boolean showScheduleColumn)
			throws Exception {
		Style detailAmountStyle = getDetailAmountStyle();
		Style columnStyle = getColumnStyle();
		FastReportBuilder drb = new FastReportBuilder();
		if(LOGGER.isInfoEnabled())     LOGGER.info("Generating Income Expenditure pdf/excel ");
		//drb.addsubre
		drb = (FastReportBuilder) drb
		.addColumn("Account Code", "glCode",
				String.class.getName(), 55, columnStyle).addColumn(
				"Head of Account", "accountName", String.class.getName(), 100,
				columnStyle);
		if (showScheduleColumn) {
			drb.addColumn("Schedule No", "scheduleNo", String.class.getName(),
					60, columnStyle);
		}
		drb.addColumn("Revised Estimate"+balanceSheet.getFinancialYear().getFinYearRange(), "budgetAmount", BigDecimal.class
				.getName(), 70, false, "0.00", detailAmountStyle);
		drb.setTitle(heading)
		.setSubtitle(subtitle+"                                                                                                                                                                                             Amount in "+ balanceSheet.getCurrency())

		.setPrintBackgroundOnOddRows(true).setWhenNoData("No data", null)
				.setDefaultStyles(getTitleStyle(), getAmountSubTitleStyle(),getHeaderStyle(), getDetailStyle())
				.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(20)
				.setHeaderHeight(35).setUseFullPageWidth(true)
				.setSubtitleHeight(30).setTitleHeight(40);                

		drb.setPageSizeAndOrientation(new Page(612, 792, false));
		if(balanceSheet.getFunds().size()==1)
		{
			for (Fund fund : balanceSheet.getFunds()) {
			drb.addColumn(toDate + "(Rs)",
					"netAmount." + fund.getName(), BigDecimal.class.getName(),
					70, false, "0.00", detailAmountStyle);
			drb.addColumn(fromDate + "(Rs)",
					"previousYearAmount." + fund.getName(), BigDecimal.class
						.getName(), 70, false, "0.00", detailAmountStyle);
			}	
		}else
		{
		for (Fund fund : balanceSheet.getFunds()) {
			drb.addColumn(fund.getName() + " Totals As On:" + toDate + "(Rs)",
					"netAmount." + fund.getName(), BigDecimal.class.getName(),
					70, false, "0.00", detailAmountStyle);
			drb.addColumn(
					fund.getName() + " Totals As On:" + fromDate + "(Rs)",
					"previousYearAmount." + fund.getName(), BigDecimal.class
							.getName(), 70, false, "0.00", detailAmountStyle);
		}
		}

		DynamicReport dr = drb.build();
		JRDataSource ds = new JRBeanCollectionDataSource(balanceSheet	.getIeEntries());
		return DynamicJasperHelper.generateJasperPrint(dr,
				new ClassicLayoutManager(), ds);
	}      
	
	public JasperPrint generateReceiptPaymentReportJasperPrint(	Statement receiptPaymentObj, String heading,	String subtitle, String currentYearDate,String PreviousYearDate,boolean showScheduleColumn)
			throws Exception {
		Style detailAmountStyle = getDetailAmountStyle();
		Style columnStyle = getColumnStyle();
		FastReportBuilder drb = new FastReportBuilder();
		if(LOGGER.isInfoEnabled())     LOGGER.info("Generating generateReceiptPaymentReportJasperPrint pdf/excel ");
		//drb.addsubre
		drb = (FastReportBuilder)drb;   
		if (!showScheduleColumn)
		{
			drb.addColumn("Schedule Number", "accountName", String.class.getName(), 100,columnStyle);
			drb.addColumn("Account Code", "glCode",String.class.getName(), 55, columnStyle);
		}
		
		if (showScheduleColumn) {
			drb.addColumn("Head of Account", "accountName", String.class.getName(), 100,columnStyle);
			drb.addColumn("Schedule No", "scheduleNo", String.class.getName(),60, columnStyle);
		}
		
		drb.setTitle(heading)
		.setSubtitle(subtitle+"                                                                                                                                                                                             Amount in "+ receiptPaymentObj.getCurrency())

		.setPrintBackgroundOnOddRows(true).setWhenNoData("No data", null)
				.setDefaultStyles(getTitleStyle(), getAmountSubTitleStyle(),getHeaderStyle(), getDetailStyle())
				.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(20)
				.setHeaderHeight(35).setUseFullPageWidth(true)
				.setSubtitleHeight(30).setTitleHeight(40);                

		drb.setPageSizeAndOrientation(new Page(612, 792, false));
		if(receiptPaymentObj.getFunds().size()>1)
		{
						
		for (Fund fund : receiptPaymentObj.getFunds()) {
				drb.addColumn(fund.getName() + " (Rs)", "fundWiseAmount."+ fund.getCode(), BigDecimal.class.getName(), 55, false,
						"0.00", detailAmountStyle);
			}
		}
		drb.addColumn(currentYearDate+"(Rs)", "currentYearTotal",BigDecimal.class.getName(), 55, false, "0.00",detailAmountStyle);
		drb.addColumn(PreviousYearDate+"(Rs)", "previousYearTotal",BigDecimal.class.getName(), 55, false, "0.00",detailAmountStyle);
		
		DynamicReport dr = drb.build();
		JRDataSource ds = new JRBeanCollectionDataSource(receiptPaymentObj.getEntries());
		return DynamicJasperHelper.generateJasperPrint(dr,new ClassicLayoutManager(), ds);
	} 
	

	public JasperPrint generateBudgetAppropriationJasperPrint(List<BudgetReAppReportBean> BudgetAppDisplay, String heading,String subtitle,String budName ,String showFundColumn, String showFunctionColumn, String showDepartmentColumn)
			throws Exception {
		Style detailAmountStyle = getDetailAmountStyle();
		Style columnStyle = getColumnStyle();
		FastReportBuilder drb = new FastReportBuilder();
		if(LOGGER.isInfoEnabled())     LOGGER.info("  Generating Budget Approprition Report pdf/excel ");
		drb = (FastReportBuilder) drb.addColumn("Sl No", "slNo", Integer.class.getName(), 20,columnStyle);                
				         					
		if ( showFundColumn=="false") {
			drb.addColumn("Fund", "fund", String.class.getName(),60, columnStyle);
		}            
		if (  showFunctionColumn=="false") {
			drb.addColumn("Function Center", "function", String.class.getName(),75, columnStyle);
		}
		if ( showDepartmentColumn=="false") {
			drb.addColumn("Department", "department", String.class.getName(),60, columnStyle);
		}
		drb
		.addColumn("Budget Head", "budgetHead", String.class.getName(), 130,	columnStyle)
			.addColumn("Appropriation No", "budgetAppropriationNo", String.class.getName(), 50,	columnStyle)
			.addColumn("Appropriation Date","appDate",String.class.getName(), 30,columnStyle)
			.addColumn(budName, "actualAmount",BigDecimal.class.getName(), 35, false, "0.00", detailAmountStyle)
			.addColumn("Addition Amount", "additionAmount", BigDecimal.class.getName(), 30, false, "0.00", detailAmountStyle)
			.addColumn("Dedudction Amount", "deductionAmount", BigDecimal.class.getName(), 30, false, "0.00", detailAmountStyle);
  
		drb.setTitle(heading)
		.setSubtitle("                                                                                                                                                               "+subtitle)
		.setPrintBackgroundOnOddRows(true).setWhenNoData("No data", null)        
				.setDefaultStyles(getTitleStyle(),getBudgetSubTitleStyle(),getHeaderStyle(), getDetailStyle())
				.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(20)
				.setHeaderHeight(35).setUseFullPageWidth(true)   
				.setSubtitleHeight(30).setTitleHeight(40).setSubtitleStyle(getBudgetSubTitleStyle());

		drb.setPageSizeAndOrientation(new Page(612, 792, false));
		DynamicReport dr = drb.build();
		JRDataSource ds = new JRBeanCollectionDataSource(BudgetAppDisplay);
		return DynamicJasperHelper.generateJasperPrint(dr,new ClassicLayoutManager(), ds);
	}

	public JasperPrint generateBudgetReportForHOD(List<BudgetProposalBean> bpBeanList, String heading,String subtitle,
			String beforePreviousYearRange,String previousYearRange,String currentYearRange,String nextYearRange,boolean isConsolidated)
			throws Exception {
		Style detailAmountStyle = getDetailAmountStyle();
		Style columnStyle = getBudgetColumnStyle();
		String conSubtitle=isConsolidated?subtitle +"                                                                                                                                                                                                                                                                                                                                                  "+
		"Amount in Thousand":subtitle +"                                                                                                                                                                                                                                                                "+"Amount in Rupees";
		FastReportBuilder drb = new FastReportBuilder();
		if(LOGGER.isInfoEnabled())     LOGGER.info("  Generating Budget Report for HOD  pdf/excel ");
		drb = (FastReportBuilder) drb   
		.addColumn("Budget\\nDepartment", "executingDepartment", String.class.getName(), 40,	columnStyle)
		.addColumn("Fund", "fund", String.class.getName(), 20,	columnStyle)
		.addColumn("Function", "function", String.class.getName(),30,	columnStyle)
		.addColumn("Budget Group", "budgetGroup", String.class.getName(), 120,	columnStyle)
		.addColumn("Reference", "reference", String.class.getName(), 35,	columnStyle)
		.addColumn("Actuals\\n"+beforePreviousYearRange, "twoPreviousYearActuals", String.class.getName(), 30,	columnStyle)
		.addColumn("Actuals\\n"+previousYearRange, "previousYearActuals", String.class.getName(), 30,	columnStyle)
		.addColumn("BE\\n"+currentYearRange+" (A)", "currentYearBE", String.class.getName(), 20,	columnStyle)
		.addColumn("Addition/\\nReduction\\n(B)", "reappropriation", String.class.getName(), 30,	columnStyle)
		.addColumn("Total\\n(A+B)", "total", String.class.getName(), 25,	columnStyle)
		.addColumn("Actuals upto\\n"+currentYearRange, "currentYearActuals", String.class.getName(), 25,	columnStyle)
		.addColumn("Anticipatory\\nAmount till\\n31 March\\n"+currentYearRange, "anticipatory", String.class.getName(), 40,	columnStyle);
		if(isConsolidated){
			drb.addColumn("RE\\n"+currentYearRange+"\\nProposed", "proposedRE",BigDecimal.class.getName(), 25,	columnStyle)
			.addColumn("RE\\n"+currentYearRange+"\\nFixed", "approvedRE",BigDecimal.class.getName(), 25,	columnStyle)
			.addColumn("BE\\n"+nextYearRange+"\\nProposed", "proposedBE", BigDecimal.class.getName(), 25,	columnStyle)
			.addColumn("BE\\n"+nextYearRange+"\\nFixed", "approvedBE", BigDecimal.class.getName(), 20,	columnStyle);
		}else{
			drb.addColumn("RE\\n"+currentYearRange+"\\nProposed", "proposedRE",BigDecimal.class.getName(), 25,	columnStyle)
			.addColumn("BE\\n"+nextYearRange+"\\nProposed", "proposedBE", BigDecimal.class.getName(), 20,	columnStyle);
		}
		//.addColumn("Remarks", "budgetGroup", String.class.getName(), 30,	columnStyle);
			
		drb.setTitle(heading)
		.setSubtitle(conSubtitle)  
		.setPrintBackgroundOnOddRows(true).setWhenNoData("No data", null) 
				.setDefaultStyles(getBudgetTitleStyle(),getBudgetSubTitleTwoStyle(),getHeaderBudgetStyle(), getDetailBudgetStyle())
				.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(20)
				.setHeaderHeight(35).setUseFullPageWidth(true)   
				.setSubtitleHeight(40).setTitleHeight(70);
   
		drb.setPageSizeAndOrientation(new Page(612, 1500, false));
		DynamicReport dr = drb.build();
		JRDataSource ds = new JRBeanCollectionDataSource(bpBeanList);  
		if(LOGGER.isInfoEnabled())     LOGGER.info(" -------------- populated Budget Report for HOD  pdf/excel -----------");
		return DynamicJasperHelper.generateJasperPrint(dr,	new ClassicLayoutManager(), ds);
		
	}

	public JasperPrint generateFinancialStatementReportJasperPrint(
			Statement balanceSheet, String heading, String addheading,String fromDate,
			String toDate, boolean showScheduleColumn) throws Exception {
		Style detailAmountStyle = getDetailAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
		Style columnStyle = getColumnStyle();
		drb = (FastReportBuilder) drb.addColumn("Account Code", "glCode",
				String.class.getName(), 50,columnStyle).addColumn("Head of Account",
				"accountName", String.class.getName(), 100,columnStyle);
		if (showScheduleColumn) {
			drb.addColumn("Schedule No", "scheduleNo", String.class.getName(),
					60,columnStyle);
		}
		// Added Blank Space for painting subtitle left and right aligned 
		drb.setTitle(heading+" "+balanceSheet.getFinancialYear().getFinYearRange()+" "+addheading).setSubtitle("Report Run Date:"+FORMATDDMMYYYY.format(new Date())+"                                                                                                                                   Amount in " + balanceSheet.getCurrency())
				.setPrintBackgroundOnOddRows(true).setWhenNoData("No data",
						null).setDefaultStyles(getTitleStyle(),
						getSubTitleStyle(), getHeaderStyle(), getDetailStyle())
				.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(20)
				.setHeaderHeight(50).setUseFullPageWidth(true);
				//.setSubtitleHeight(20);

		drb.setPageSizeAndOrientation(new Page(612, 792, false));
	if(balanceSheet.getFunds().size()>1)
	{
		for (Fund fund : balanceSheet.getFunds()) {
			drb.addColumn(fund.getName() + " (Rs)", "fundWiseAmount."+ fund.getName(), BigDecimal.class.getName(), 55, false,
					"0.00", detailAmountStyle);
		}
	}
		drb.addColumn(fromDate + "(Rs)", "currentYearTotal",BigDecimal.class.getName(), 55, false, "0.00",detailAmountStyle)
		.addColumn( toDate+ "(Rs)", "previousYearTotal",BigDecimal.class.getName(), 55, false, "0.00",
				detailAmountStyle);
		DynamicReport dr = drb.build();
		JRDataSource ds = new JRBeanCollectionDataSource(balanceSheet
				.getEntries());
		return DynamicJasperHelper.generateJasperPrint(dr,
				new ClassicLayoutManager(), ds);
	}

	public JasperPrint generateBudgetReportJasperPrint(List inputData,
			String heading, boolean enableBeApproved, boolean enableReApproved,
			String lastYearRange, String currentYearRange, String nextYearRange)
			throws Exception {
		Style detailAmountStyle = getBudgetReportDetailAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
		drb = (FastReportBuilder) drb.addColumn("Department Code",
				"departmentCode", String.class.getName(), 50).addColumn(
				"Function Code", "functionCode", String.class.getName(), 50)
				.addColumn("Account Head", "budgetGroupName",
						String.class.getName(), 90).addColumn(
						"Actuals " + lastYearRange + "(Rs)", "actualsLastYear",
						BigDecimal.class.getName(), 100, false, "0.00",
						detailAmountStyle).addColumn(
						"BE " + currentYearRange + "(Rs)",
						"beCurrentYearApproved", BigDecimal.class.getName(),
						100, false, "0.00", detailAmountStyle).addColumn(
						"RE Proposed " + currentYearRange + "(Rs)",
						"reCurrentYearOriginal", BigDecimal.class.getName(),
						100, false, "0.00", detailAmountStyle);
		if (enableReApproved) {
			drb.addColumn("RE Approved " + currentYearRange + "(Rs)",
					"reCurrentYearApproved", BigDecimal.class.getName(), 100,
					false, "0.00", detailAmountStyle);
		}
		drb.addColumn("BE Proposed " + nextYearRange + "(Rs)",
				"beNextYearOriginal", BigDecimal.class.getName(), 100, false,
				"0.00", detailAmountStyle);
		if (enableBeApproved) {
			drb.addColumn("BE Approved " + nextYearRange + "(Rs)",
					"beNextYearApproved", BigDecimal.class.getName(), 100,
					false, "0.00", detailAmountStyle);
		}
		drb.setTitle(heading).setWhenNoData("No data", null).setDefaultStyles(
				getTitleStyle(), getSubTitleStyle(), getHeaderStyle(),
				getBudgetReportDetailStyle()).setDetailHeight(20)
				.setHeaderHeight(30).setUseFullPageWidth(true);
		drb.setPageSizeAndOrientation(new Page(612, 792, false));
		return DynamicJasperHelper.generateJasperPrint(drb.build(),
				new ClassicLayoutManager(), new JRBeanCollectionDataSource(
						inputData));
	}

	public JasperPrint generateFunctionwiseIEJasperPrint(
			FunctionwiseIE functionwiseIE, String cityName, String type)
			throws Exception {
		Style detailAmountStyle = getDetailAmountStyle();
		FastReportBuilder drb = new FastReportBuilder();
		drb = (FastReportBuilder) drb.addColumn("Sl.No.", "slNo",
				String.class.getName(), 10).addColumn("Function Code",
				"functionCode", String.class.getName(), 20).addColumn(
				"Function Head", "functionName", String.class.getName(), 50)
				.addColumn("Total " + type + " (Rs.)", "totalIncome",
						BigDecimal.class.getName(), 50, false, "0.00",
						detailAmountStyle).setTitle(cityName).setSubtitle(
						"FUNCTIONWISE " + type.toUpperCase()
								+ " SUBSIDARY REGISTER")
				.setPrintBackgroundOnOddRows(true).setWhenNoData("No Data",
						null).setDefaultStyles(getTitleStyle(),
						getSubTitleStyle(), getHeaderStyle(), getDetailStyle())
				.setOddRowBackgroundStyle(getOddRowStyle()).setDetailHeight(20)
				.setUseFullPageWidth(true).setTitleHeight(50).setSubtitleHeight(35);

		for (String s : functionwiseIE.getMajorCodeList()) {
			drb.addColumn(s, "majorcodeWiseAmount." + s, BigDecimal.class
					.getName(), 35, false, "0.00", detailAmountStyle);
		}
		DynamicReport dr = drb.build();
		JRDataSource ds = new JRBeanCollectionDataSource(functionwiseIE
				.getEntries());
		return DynamicJasperHelper.generateJasperPrint(dr,
				new ClassicLayoutManager(), ds);
	}

	private Style getOddRowStyle() {
		Style oddRowStyle = new Style();
		oddRowStyle.setBackgroundColor(new Color(247, 247, 247));
		oddRowStyle.setTransparency(Transparency.OPAQUE);
		return oddRowStyle;
	}

	private Style getSubTitleStyle() {
		Style subTitleStyle = new Style("titleStyle");
		subTitleStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

		return subTitleStyle;
	}
	
	private Style getBudgetSubTitleStyle() {  
		Style subTitleStyle = new Style("titleStyle");
		subTitleStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		subTitleStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		subTitleStyle.setStretchWithOverflow(true);
		return subTitleStyle;
	}
	
	private Style getBudgetSubTitleTwoStyle() {  
		Style subTitleStyle = new Style("titleStyle");
		subTitleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
		subTitleStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		subTitleStyle.setStretchWithOverflow(true);
		return subTitleStyle;
	}
	
	private Style getAmountSubTitleStyle() {
		Style subTitleStyle = new Style("SubAmountStyle");
		subTitleStyle.setFont(new Font(8, Font._FONT_VERDANA, true));
		subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		subTitleStyle.setStretchWithOverflow(true);
		return subTitleStyle;
	}
	private Style getTitleStyle() {
		Style titleStyle = new Style("titleStyle");
		titleStyle.setFont(new Font(9, Font._FONT_VERDANA, true));
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return titleStyle;
	}
	
	private Style getBudgetTitleStyle() {
		Style titleStyle = new Style("titleStyle");
		titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return titleStyle;
	}

	private Style getHeaderStyle() {
		Style headerStyle = new Style("header");
		headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerStyle.setBorder(Border.THIN);
		headerStyle.setBackgroundColor(new Color(204, 204, 204));
		headerStyle.setTextColor(Color.blue);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setFont(new Font(8, Font._FONT_VERDANA, true));
		headerStyle.setStretchWithOverflow(true);
		return headerStyle;
	}
	
	private Style getHeaderBudgetStyle() {
		Style headerStyle = new Style("header");
		headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerStyle.setBorder(Border.THIN);
		headerStyle.setBackgroundColor(new Color(204, 204, 204));
		headerStyle.setTextColor(Color.black);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
		headerStyle.setStretchWithOverflow(true);
		return headerStyle;
	}

	private Style getDetailAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setTextColor(Color.blue);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		detailAmountStyle.setPaddingRight(2);
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		detailAmountStyle.setBorderBottom(Border.THIN);
		return detailAmountStyle;
	}
	
	private Style getConcurrenceAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setTextColor(Color.BLACK);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(6, Font._FONT_ARIAL, true));
		detailAmountStyle.setPaddingRight(2);
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		detailAmountStyle.setBorderBottom(Border.THIN);
		return detailAmountStyle;
	}

	private Style getColumnStyle() {
		Style columnStyle = new Style("ColumnCss");
		columnStyle.setBorderLeft(Border.THIN);
		columnStyle.setBorderRight(Border.THIN);
		
		columnStyle.setTextColor(Color.blue);
		columnStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		columnStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		columnStyle.setTransparency(Transparency.OPAQUE);
		columnStyle.setBorderBottom(Border.THIN);
		// detailAmountStyle.s
		return columnStyle;
	}
	
	private Style getDepartmentTitleStyle() {
		Style titleStyle = new Style("titleStyle");
		titleStyle.setFont(new Font(6, Font._FONT_ARIAL, true));
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		return titleStyle;
	}
	private Style getDepartmentwiseSubTitleStyle() {
		Style titleStyle = new Style("titleStyle");
		titleStyle.setFont(new Font(6, Font._FONT_ARIAL, true));
		titleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		//titleStyle
		//titleStyle.setVerticalAlign(verticalAlign)
		return titleStyle;
	}
	
	private Style getBudgetColumnStyle() {
		Style columnStyle = new Style("ColumnCss");
		columnStyle.setBorderLeft(Border.THIN);
		columnStyle.setBorderRight(Border.THIN);
		columnStyle.setTextColor(Color.black);
		columnStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		columnStyle.setFont(new Font(11, Font._FONT_ARIAL, false));
		columnStyle.setTransparency(Transparency.OPAQUE);
		columnStyle.setBorderBottom(Border.THIN);
		// detailAmountStyle.s
		return columnStyle;
	}
	private Style getConcurrenceColumnStyle() {
		Style columnStyle = new Style("ColumnCss");
		columnStyle.setBorderLeft(Border.THIN);
		columnStyle.setBorderRight(Border.THIN);
		columnStyle.setTextColor(Color.black);
		//columnStyle.
		columnStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		columnStyle.setFont(new Font(5, Font._FONT_ARIAL, false));
		columnStyle.setTransparency(Transparency.OPAQUE);
		columnStyle.setBorderBottom(Border.THIN);
		// detailAmountStyle.s
		return columnStyle;
	}

	private Style getBudgetReportDetailAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setBorderTop(Border.THIN);
		detailAmountStyle.setBorderBottom(Border.THIN);
		detailAmountStyle.setTextColor(Color.black);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(8, Font._FONT_VERDANA, true));
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}

	private Style getFIEHeaderStyle() {
		Style headerStyle = new Style("header");
		headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
		headerStyle.setBorder(Border.THIN);
		headerStyle.setBackgroundColor(new Color(204, 204, 204));
		headerStyle.setTextColor(Color.blue);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		headerStyle.setFont(new Font(7, Font._FONT_VERDANA, true));
		return headerStyle;
	}

	private Style getFIEAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setBorderTop(Border.THIN);
		detailAmountStyle.setBorderBottom(Border.THIN);
		detailAmountStyle.setTextColor(Color.black);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		detailAmountStyle.setBlankWhenNull(true);
		detailAmountStyle.setPaddingRight(2);
		detailAmountStyle.setPattern("0.00");
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}

	private Style getFIECOAStyle() {
		Style detailAmountStyle = new Style("detailCOA");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setBorderTop(Border.THIN);
		detailAmountStyle.setBorderBottom(Border.THIN);
		detailAmountStyle.setTextColor(Color.black);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		detailAmountStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		detailAmountStyle.setBlankWhenNull(true);
		detailAmountStyle.setPaddingLeft(2);
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}
	private Style getTBAmountStyle() {
		Style detailAmountStyle = new Style("detailAmount");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setBorderTop(Border.THIN);
		detailAmountStyle.setBorderBottom(Border.THIN);
		detailAmountStyle.setTextColor(Color.black);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		detailAmountStyle.setFont(new Font(6, Font._FONT_VERDANA, true));
		detailAmountStyle.setPaddingRight(2);
		detailAmountStyle.setPattern("0.00");
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}
	
	private Style getCOAStyle() {
		Style detailAmountStyle = new Style("detailCOA");
		detailAmountStyle.setBorderLeft(Border.THIN);
		detailAmountStyle.setBorderRight(Border.THIN);
		detailAmountStyle.setBorderTop(Border.THIN);
		detailAmountStyle.setBorderBottom(Border.THIN);
		detailAmountStyle.setTextColor(Color.black);
		detailAmountStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		detailAmountStyle.setFont(new Font(6, Font._FONT_VERDANA, false));
		detailAmountStyle.setBlankWhenNull(true);
		detailAmountStyle.setPaddingLeft(2);
		detailAmountStyle.setTransparency(Transparency.OPAQUE);
		return detailAmountStyle;
	}

	private Style getDetailStyle() {
		Style detailStyle = new Style("detail");
		detailStyle.setBorderLeft(Border.THIN);
		detailStyle.setBorderRight(Border.THIN);
		detailStyle.setTextColor(Color.blue);
		detailStyle.setFont(new Font(8, Font._FONT_VERDANA, true));
		detailStyle.setTransparency(Transparency.OPAQUE);
		return detailStyle;
	}
	
	private Style getDetailBudgetStyle() {
		Style detailStyle = new Style("detail");
		detailStyle.setBorderLeft(Border.THIN);
		detailStyle.setBorderRight(Border.THIN);
		detailStyle.setTextColor(Color.black);
		detailStyle.setFont(new Font(11, Font._FONT_ARIAL, false));
		detailStyle.setTransparency(Transparency.OPAQUE);
		return detailStyle;
	}

	private Style getBudgetReportDetailStyle() {
		Style detailStyle = new Style("detail");
		detailStyle.setBorderLeft(Border.THIN);
		detailStyle.setBorderRight(Border.THIN);
		detailStyle.setBorderTop(Border.THIN);
		detailStyle.setBorderBottom(Border.THIN);
		detailStyle.setTextColor(Color.black);
		detailStyle.setFont(new Font(8, Font._FONT_VERDANA, true));
		detailStyle.setTransparency(Transparency.OPAQUE);
		return detailStyle;
	}

	private void exportReport(JasperPrint jasperPrint, String sizeUnitPoint)
			throws JRException {
		JRHtmlExporter exporter = new JRHtmlExporter();
		exporter
				.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,
				outputBytes);
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		exporter.setParameter(
				JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
				false);
		exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, sizeUnitPoint);
		exporter.exportReport();
	}

	public JasperPrint exportMajorAndMinorCodewise(
			List<CommonReportBean> ieWithBudgetList, String cityName,
			ReportSearch reportSearch, String heading) {
		DynamicReport dr;
		JRDataSource ds;
		Style amountStyle = getFIEAmountStyle();
		Style textStyle = getFIECOAStyle();
		String header=cityName+"\\n"+heading;
		try {
			Style detailAmountStyle = getDetailAmountStyle();
			FastReportBuilder drb = new FastReportBuilder();
			if (reportSearch.getIncExp().equalsIgnoreCase("I")) {
				drb = (FastReportBuilder) drb.addColumn("Sl.No", "slNo",Integer.class.getName(), 18, textStyle)
						.addColumn("COA", "accCode", String.class.getName(),30, textStyle).addColumn("Account Head","name", String.class.getName(), 80, textStyle)
						.addColumn("Schedule", "schedule",String.class.getName(), 25, textStyle)
						.addColumn("BE (Rs)", "beAmount",BigDecimal.class.getName(), 70, amountStyle)
						.addColumn("RE (Rs)", "reAmount",BigDecimal.class.getName(), 70, amountStyle)
						.addColumn("Receipt(Current Year) (Rs)", "amount",BigDecimal.class.getName(), 70, amountStyle)
						.addColumn("Receipt(Previous Year) (Rs)", "pyAmount",BigDecimal.class.getName(), 70, amountStyle);
			} else {
				drb = (FastReportBuilder) drb.addColumn("Sl.No", "slNo",
						Integer.class.getName(), 18, textStyle)
						.addColumn("COA", "accCode", String.class.getName(),
								20, textStyle).addColumn("Account Head",
								"name", String.class.getName(), 80, textStyle)
						.addColumn("Schedule", "schedule",
								String.class.getName(), 30, textStyle)
						.addColumn("BE (Rs)", "beAmount",
								BigDecimal.class.getName(), 65, amountStyle)
						.addColumn("BE-Appropriation (Rs)", "beAppAmount",
								BigDecimal.class.getName(), 65, amountStyle)
						.addColumn("RE (Rs)", "reAmount",
								BigDecimal.class.getName(), 65, amountStyle)
						.addColumn("RE-Appropriation (Rs)", "reAppAmount",
								BigDecimal.class.getName(), 65, amountStyle)
						.addColumn("Expenditure As On(Previous Year) (Rs)", "pyAmount",
								BigDecimal.class.getName(), 65, amountStyle)
						.addColumn("Expenditure As On(Current Year) (Rs)", "amount",
								BigDecimal.class.getName(), 65, amountStyle)
						.addColumn("Balance (Rs)", "computedBalance",
								BigDecimal.class.getName(), 70, amountStyle);

			}
			drb.setTitle(header).setSubtitle("Report Run Date:"+FORMATDDMMYYYY.format(new Date())+"                                                                    Amount in Rupees")
					.setPrintBackgroundOnOddRows(true).setWhenNoData("No Data",
							detailAmountStyle).setDefaultStyles(
							getTitleStyle(), getAmountSubTitleStyle(),
							getFIEHeaderStyle(),
							getBudgetReportDetailAmountStyle())
					.setOddRowBackgroundStyle(getOddRowStyle())
					.setDetailHeight(20)
					.setTitleHeight(50)
					.setSubtitleStyle(getAmountSubTitleStyle())
					.setSubtitleHeight(30).setUseFullPageWidth(true);
			dr = drb.build();
			ds = new JRBeanCollectionDataSource(ieWithBudgetList);
			return DynamicJasperHelper.generateJasperPrint(dr,
					new ClassicLayoutManager(), ds);
		} catch (ColumnBuilderException e) {
			LOGGER.error(e, e);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e, e);
		} catch (JRException e) {
			LOGGER.error(e, e);
		}
		return null;

	}
	
	
	
	public JasperPrint exportTBDateRange(
			List<TrialBalanceBean> al, String cityName,
			ReportBean rb, String heading,List<Fund> fundList,String expType) {
		DynamicReport dr;
		JRDataSource ds;
		Style amountStyle = getTBAmountStyle();
		Style textStyle = getCOAStyle();
		String header=cityName+"\\n"+heading;
		try {
			Style detailAmountStyle = getDetailAmountStyle();
			FastReportBuilder drb = new FastReportBuilder();
			DynamicReportBuilder db = new DynamicReportBuilder();
			if (rb.getReportType().equalsIgnoreCase("daterange")) {
				drb = (FastReportBuilder)
						 drb.addColumn("Account Code", "accCode", String.class.getName(),50, textStyle)
						.addColumn("Account Head","accName", String.class.getName(), 325, textStyle)
						.addColumn("Opening Balance (Rs)", "openingBal",	String.class.getName(), 70, amountStyle)
						.addColumn("Debit (Rs)", "debit",String.class.getName(), 70, amountStyle)
						.addColumn("Credit (Rs)", "credit",String.class.getName(), 70, amountStyle)
						.addColumn("Closing Balance (Rs)", "closingBal",String.class.getName(), 70, amountStyle);
			} else {
				drb = (FastReportBuilder)drb.addColumn("Account Code", "accCode", String.class.getName(),50, textStyle)
				.addColumn("Account Head","accName", String.class.getName(), 325, textStyle);
				for(Fund f:fundList)
				{
				 drb.addColumn(f.getName()+" (Rs)","fundWiseMap."+f.getId()+"_amount",String.class.getName(), 70,false,"0.00", amountStyle); 
				}
				drb.addColumn("Total (Rs)", "amount1",	String.class.getName(),70 , false,"0.00", amountStyle);
			}
			drb.setTitle(header).setSubtitle("Report Run Date:"+FORMATDDMMYYYY.format(new Date())+"                                                                    Amount in Rupees")
					.setPrintBackgroundOnOddRows(true).setWhenNoData("No Data",
							detailAmountStyle).setDefaultStyles(
							getTitleStyle(), getAmountSubTitleStyle(),
							getFIEHeaderStyle(),
							getBudgetReportDetailAmountStyle())
					.setOddRowBackgroundStyle(getOddRowStyle())
					.setDetailHeight(20)
					.setTitleHeight(50)
					.setSubtitleStyle(getAmountSubTitleStyle())
					.setPageSizeAndOrientation(Page.Page_A4_Landscape())
					.setSubtitleHeight(30).setUseFullPageWidth(true);
			//if(expType!=null && expType.equals("xls"))  
				//drb.setIgnorePagination(true);
			dr = drb.build();
			ds = new JRBeanCollectionDataSource(al);
			return DynamicJasperHelper.generateJasperPrint(dr,
					new ClassicLayoutManager(), ds);
		} catch (ColumnBuilderException e) {
			LOGGER.error(e, e);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e, e);
		} catch (JRException e) {
			LOGGER.error(e, e);
		}
		return null;

	}

	public JasperPrint exportDeptwise(List<CommonReportBean> ieWithBudgetList,
			String cityName, ReportSearch reportSearch, String heading) {
		DynamicReport dr;
		JRDataSource ds;
		String header=cityName+"\\n"+heading;
		Style amountStyle = getFIEAmountStyle();
		Style textStyle = getFIECOAStyle();
		try {
			Style detailAmountStyle = getDetailAmountStyle();
			FastReportBuilder drb = new FastReportBuilder();
			if (reportSearch.getIncExp().equalsIgnoreCase("I")) {
				drb = (FastReportBuilder) drb.addColumn("Sl.No", "slNo",
						Integer.class.getName(), 18, textStyle).addColumn(
						"Department", "deptName", String.class.getName(), 40,textStyle)
						.addColumn("COA", "accCode",String.class.getName(), 20, textStyle)
						.addColumn("BE (Rs)", "beAmount",BigDecimal.class.getName(), 70, amountStyle)
						.addColumn("RE (Rs)", "reAmount",BigDecimal.class.getName(), 70, amountStyle)
						.addColumn("Account Head", "name", String.class.getName(), 80,textStyle)
						.addColumn("Receipt(Current Year) (Rs)","amount", BigDecimal.class.getName(), 50, amountStyle)
						.addColumn("Receipt(Previous Year) (Rs)", "pyAmount",BigDecimal.class.getName(), 50, amountStyle);
			} else {
				drb = (FastReportBuilder) drb.addColumn("Sl.No", "slNo",
						Integer.class.getName(), 18, textStyle).addColumn(
						"Department", "deptName", String.class.getName(), 50,
						textStyle).addColumn("COA", "accCode",
						String.class.getName(), 20, textStyle).addColumn(
						"Account Head", "name", String.class.getName(), 80,
						textStyle).addColumn("BE (Rs)", "beAmount",
						BigDecimal.class.getName(), 60, amountStyle).addColumn(
						"BE-Appropriation (Rs)", "beAppAmount",
						BigDecimal.class.getName(), 60, amountStyle).addColumn(
						"RE (Rs)", "reAmount", BigDecimal.class.getName(), 60,
						amountStyle).addColumn("RE-Appropriation (Rs)",
						"reAppAmount", BigDecimal.class.getName(), 60,
						amountStyle).addColumn("Expenditure As On(Previous Year) (Rs)",
						"pyAmount", BigDecimal.class.getName(),60, 
						amountStyle).addColumn("Expenditure As On(Current Year) (Rs)",
						"amount", BigDecimal.class.getName(),60, amountStyle)
						.addColumn("Balance (Rs)", "computedBalance",
								BigDecimal.class.getName(), 60, amountStyle);

			}
			// Added Blank Space for painting subtitle left and right aligned 
			drb.setTitle(header).setSubtitle("Report Run Date:"+FORMATDDMMYYYY.format(new Date())+" Amount in Rupees")
					.setPrintBackgroundOnOddRows(true).setWhenNoData("No Data",
							detailAmountStyle).setDefaultStyles(
							getTitleStyle(), getAmountSubTitleStyle(),
							getFIEHeaderStyle(),
							getBudgetReportDetailAmountStyle())
					.setOddRowBackgroundStyle(getOddRowStyle()).setTitleHeight(50).setSubtitleHeight(20).setSubtitleStyle(getAmountSubTitleStyle())
					.setDetailHeight(20).setUseFullPageWidth(true);
			dr = drb.build();
			ds = new JRBeanCollectionDataSource(ieWithBudgetList);
			return DynamicJasperHelper.generateJasperPrint(dr,
					new ClassicLayoutManager(), ds);
		} catch (ColumnBuilderException e) {
			LOGGER.error(e, e);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e, e);
		} catch (JRException e) {
			LOGGER.error(e, e);
		}

		return null;
	}

	public JasperPrint exportDetailwise(
			List<CommonReportBean> ieWithBudgetList, String cityName,
			ReportSearch reportSearch, String heading) {
		DynamicReport dr;
		JRDataSource ds;
		Style amountStyle = getFIEAmountStyle();
		Style textStyle = getFIECOAStyle();
		String header=cityName+"\\n"+heading;
		try {
			Style detailAmountStyle = getDetailAmountStyle();
			FastReportBuilder drb = new FastReportBuilder();
			if (reportSearch.getIncExp().equalsIgnoreCase("I")) {
				drb = (FastReportBuilder) drb.addColumn("Sl.No", "slNo",
						Integer.class.getName(), 15, textStyle)
						.addColumn("COA", "accCode", String.class.getName(),
								50, textStyle).addColumn("BE (Rs)", "beAmount",BigDecimal.class.getName(), 70, amountStyle)
								.addColumn("RE (Rs)", "reAmount",BigDecimal.class.getName(), 70, amountStyle)
								.addColumn("Account Head",
								"name", String.class.getName(), 80, textStyle)
						.addColumn("Receipt(Current Year) (Rs)", "amount",
								BigDecimal.class.getName(), 50, amountStyle)
						.addColumn("Receipt(Previous Year) (Rs)", "beAmount",
								BigDecimal.class.getName(), 50, amountStyle);
			} else {
				drb = (FastReportBuilder) drb.addColumn("Sl.No", "slNo",
						Integer.class.getName(), 15, textStyle)
						.addColumn("COA", "accCode", String.class.getName(),
								50, textStyle).addColumn("Account Head",
								"name", String.class.getName(), 80, textStyle)
						.addColumn("BE (Rs)", "beAmount",
								BigDecimal.class.getName(), 40, amountStyle)
						.addColumn("BE-Appropriation (Rs)", "beAppAmount",
								BigDecimal.class.getName(), 40, amountStyle)
						.addColumn("RE (Rs)", "reAmount",
								BigDecimal.class.getName(), 40, amountStyle)
						.addColumn("RE-Appropriation (Rs)", "reAppAmount",
								BigDecimal.class.getName(), 40, amountStyle)
						.addColumn("Expenditure As On(Previous Year) (Rs)", "pyAmount",
								BigDecimal.class.getName(), 40, amountStyle)
						.addColumn("Expenditure As On(Current Year) (Rs)", "amount",
								BigDecimal.class.getName(), 40, amountStyle)
						.addColumn("Balance (Rs)", "computedBalance",
								BigDecimal.class.getName(), 40, amountStyle);

			}
			// Added Blank Space for painting subtitle left and right aligned  
			drb.setTitle(header).setSubtitle("Report Run Date:"+FORMATDDMMYYYY.format(new Date())+"                                       Amount in Rupees")
					.setPrintBackgroundOnOddRows(true).setWhenNoData("No Data",
							detailAmountStyle).setDefaultStyles(
							getTitleStyle(), getAmountSubTitleStyle(),
							getFIEHeaderStyle(), amountStyle)
					.setOddRowBackgroundStyle(getOddRowStyle()).setTitleHeight(60).setSubtitleHeight(20)
					.setDetailHeight(20).setUseFullPageWidth(true);
			dr = drb.build();
			ds = new JRBeanCollectionDataSource(ieWithBudgetList);
			return DynamicJasperHelper.generateJasperPrint(dr,
					new ClassicLayoutManager(), ds);
		} catch (ColumnBuilderException e) {
			LOGGER.error(e, e);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e, e);
		} catch (JRException e) {
			LOGGER.error(e, e);
		}

		return null;
	}
}