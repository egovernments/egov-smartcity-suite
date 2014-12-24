package org.egov.payroll.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class MonthlyPayslipReports {

	private static final Logger logger = Logger.getLogger(MonthlyPayslipReports.class);
	private static final String EARNINGSJASPER="Earnings.jasper";
	private static final String DEDUCTIONSJASPER="Deductions.jasper";
	private static final String LOANSJASPER="Loans.jasper";
	private static final String EMP_PAYSLIPJASPER="Emp_payslip.jasper";
	private static final String VMC_PAYSLIPSJASPER="VMC_Payslips.jasper";
	private static final String EARNINGSJASPERSLASH="/Earnings.jasper";
	private static final String DEDUCTIONSJASPERSLASH="/Deductions.jasper";
	private static final String LOANSJASPERSLASH="/Loans.jasper";
	private static final String EMP_PAYSLIPJASPERSLASH="/Emp_payslip.jasper";
	private static final String VMC_PAYSLIPSJASPERSLASH="/VMC_Payslips.jasper";
	
	private ReportService reportService;
	
	
	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	//	Class for reading the output that is generated
//	 by the Velocity engine; this ouput is sent
//	 to the Jasper XML template compiler
	public class TemplateCompiler implements Runnable {

		private PipedInputStream inStream;

		private JasperReport jasperReport;

		public TemplateCompiler(PipedInputStream pipedStream) {
			this.inStream = pipedStream;
		}

		/*
		 * This method should be called after the thread that is executing this
		 * Runnable instance has finished.
		 */
		public JasperReport getJasperReport() {
			return jasperReport;
		}

		public void run() {
			try {
				// If no input stream was provided, return
				if (inStream == null) {
					return;
				}

				jasperReport = JasperCompileManager.compileReport(inStream);
			} catch (Exception io) {
				logger.error(io.getMessage());
				//log it
			}
		}
	}


	
	/*If the department passed for the method is null , then  the payslips is generated for all the departments. 
	 * This method  reads the vm file from the server and convert it back to .jrxml and returns
	 * the jasperPrint object.
	 * 
	 */
	 //for department
	public ReportOutput generateMonthlyPayslipsPDF(Integer month,Integer year,Integer dept,String billNum,String cityName,Map paramsMap,String reportExtn)throws Exception  {
		
		logger.info("city--------"+cityName);
			
			 String filePath = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipPdfPath",new Date()).getValue();
			  /*The Name conventions for the PDF file is as follows:
			 	1. Month selected for PDf Generation.
			 	2. Financial Year ID should be predfixed with "FY"
			 	3. Department ID selected for PDf Generation.
			 	
			 	   Eg : 04-FY06-01.PDF
			 	   
			 	 In some case department is null, then the department is 
			 	 considererd as "00" 
			 	 
			 	 	Eg : 04-FY06-00.pdf 
		 	 */
			Integer yr = year;
			Integer mon=month;
			Integer depart= dept;
			Integer deptid;
			 String fNameWithoutExtn="";
			 if(!depart.toString().equals("0"))
			 {
				 fNameWithoutExtn = filePath+"0"+mon.toString()+"-"+"FY"+"0"+yr.toString()+"-"+"0"+depart.toString()+billNum;
				 deptid=depart;
			 }
			 else
			 {
				 fNameWithoutExtn = filePath+"0"+month.toString()+"-"+"FY"+"0"+year.toString()+"-00"+billNum;
				 deptid=null;
			 }
			
			// Get template
		//	Template template = Velocity.getTemplate("VMC_Payslips2.vm");
		//	JasperReport jasperReport = compileTemplate(template, context);
		
			/* InputStream reportStreamEar = this.getClass().getResourceAsStream(paramsMap.get(EARNINGSJASPER)==null?EARNINGSJASPERSLASH:(String)paramsMap.get(EARNINGSJASPER));//
			 JasperReport earningReport = (JasperReport)JRLoader.loadObject(reportStreamEar);
			 
			 
			 InputStream reportStreamDed = this.getClass().getResourceAsStream(paramsMap.get(DEDUCTIONSJASPER)==null?DEDUCTIONSJASPERSLASH:(String)paramsMap.get(DEDUCTIONSJASPER));
			 JasperReport deductionReport = (JasperReport)JRLoader.loadObject(reportStreamDed);
			 
			 
			 InputStream reportStreamLoan = this.getClass().getResourceAsStream(paramsMap.get(LOANSJASPER)==null?LOANSJASPERSLASH:(String)paramsMap.get(LOANSJASPER));
			 JasperReport loansReport = (JasperReport)JRLoader.loadObject(reportStreamLoan);
			 
			 InputStream reportStream = this.getClass().getResourceAsStream(paramsMap.get(VMC_PAYSLIPSJASPER)==null?VMC_PAYSLIPSJASPERSLASH:(String)paramsMap.get(VMC_PAYSLIPSJASPER));
			 JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportStream);*/
			 GregorianCalendar cal = new GregorianCalendar();
			PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
			 CFinancialYear cyr=payrollExternalInterface.findFinancialYearById(Long.valueOf(yr));
			 String yearInWrds = "";
			 
			 if(month.intValue()>4)
			 {				 
				 cal.setTime(cyr.getStartingDate());
				 yearInWrds=cal.get(Calendar.YEAR)+"";
			 }else
			 {
				 cal.setTime(cyr.getEndingDate());
				 yearInWrds=cal.get(Calendar.YEAR)+"";
			 }
			//String cityname=(String).getAttribute("cityname");
			 List<CFinancialYear> finYearList = (List)payrollExternalInterface.getAllFinancialYearList();
			 String finYrRange="";
			 for(Iterator<CFinancialYear> itr=finYearList.iterator(); itr.hasNext();)
			 {
				 CFinancialYear finyr=itr.next();
				 if(finyr.getId().intValue()== year.intValue())
					 finYrRange=finyr.getFinYearRange();
			 }
			 paramsMap.put("pdftitle","Payslip for the month of "+getMonthInWords(month)+" "+ finYrRange);
			 paramsMap.put("MONTH",mon );
			 String status = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus",new Date()).getValue();
			  EgwStatus egwStatus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
					  PayrollConstants.PAYSLIP_MODULE, status);
			 paramsMap.put("STATUS",egwStatus.getId());
			 paramsMap.put("MONTHINWORDS",getMonthInWords(month) );
			 paramsMap.put("yearInWrds",yearInWrds);
			 paramsMap.put("FINYR",year );
			 paramsMap.put("DEPT_ID",deptid );
			 if(!("").equalsIgnoreCase(billNum))
			 {
				 paramsMap.put("billNum","Bill Number : "+billNum);
			 }
			 else
			 {
				 paramsMap.put("billNum", "");
			 }
			 paramsMap.put("billId", billNum);
			 /*paramsMap.put("EARNING_SUB_REPORT",earningReport );
			 paramsMap.put("DEDUCTION_SUB_REPORT",deductionReport );
			 paramsMap.put("LOAN_SUB_REPORT",loansReport );*/
			 logger.info("before fill report ");
			 
			 //Generating pdf/rtf using ReportService
			 String EMP_PAYSLIP_JRXML = paramsMap.get(VMC_PAYSLIPSJASPER)==null?"VMC_Payslips":"VMC_Payslips";
			 ReportRequest reportInput = new ReportRequest(EMP_PAYSLIP_JRXML, paramsMap, ReportDataSourceType.SQL);
			 /*ReportUtil.getTemplateAsStream(paramsMap.get(LOANSJASPER)==null?"Loans.jasper":(String)paramsMap.get(LOANSJASPER));		*/	 
			 reportInput.setReportFormat(FileFormat.valueOf(reportExtn.toUpperCase()));			
			 ReportOutput ro = reportService.createReport(reportInput);
			 saveReportFile(ro, fNameWithoutExtn,reportExtn);
			 
			 //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramsMap,HibernateUtil.getCurrentSession().connection());
			  
			 logger.info("before store pdf call--------");
			
			 
			// storePDF(jasperPrint,fName);
			
			 return ro;
		
		
	}
	/**
	 * This Api Generate the pdf only for a employee based
	 * @param month
	 * @param year
	 * @param empId
	 * @throws Exception
	 */
	public ReportOutput generateMonthlyPayslipPDFofAnEmployee(Integer month,Integer year,Integer empId,Integer empCode,String billNum,String cityName,Map paramsMap,String reportExtn)throws Exception  {
		
		
		logger.info("only if employee.......................");
	
		 String filePath = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipPdfPath",new Date()).getValue();
		  /*The Name conventions for the PDF file is as follows:
		 	1. Month selected for PDf Generation.
		 	2. Financial Year ID should be predfixed with "FY"
		 	3. Employee ID selected for PDf Generation.
		 	
		 	   Eg : 04-FY06-100.PDF
		 	  
	 	 */
		Integer yr = year;
		Integer mon=month;
		Integer employee= empCode;
		
		 String fNameWithoutExtn="";
		 if(!employee.toString().equals("0"))
		 {
			 fNameWithoutExtn = filePath+"0"+mon.toString()+"-"+"FY"+"0"+yr.toString()+"-"+employee.toString()+billNum;
			 
		 }
		 InputStream reportStreamEar = this.getClass().getResourceAsStream(
				paramsMap.get(EARNINGSJASPER) == null ? EARNINGSJASPERSLASH:(String)paramsMap.get(EARNINGSJASPER));
		 JasperReport earningReport = (JasperReport)JRLoader.loadObject(reportStreamEar);
		 
		 InputStream reportStreamDed = this.getClass().getResourceAsStream(paramsMap.get(DEDUCTIONSJASPER)==null?DEDUCTIONSJASPERSLASH:(String)paramsMap.get(DEDUCTIONSJASPER));
		 JasperReport deductionReport = (JasperReport)JRLoader.loadObject(reportStreamDed);
		 
		 InputStream reportStreamLoan = this.getClass().getResourceAsStream(paramsMap.get(LOANSJASPER)==null?LOANSJASPERSLASH:(String)paramsMap.get(LOANSJASPER));
		 JasperReport loansReport = (JasperReport)JRLoader.loadObject(reportStreamLoan);
		 
		 InputStream reportStream = this.getClass().getResourceAsStream(paramsMap.get(EMP_PAYSLIPJASPER)==null?EMP_PAYSLIPJASPERSLASH:(String)paramsMap.get(EMP_PAYSLIPJASPER));
		 JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportStream);
		 GregorianCalendar cal = new GregorianCalendar();
		 PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();	
		 CFinancialYear cyr=payrollExternalInterface.findFinancialYearById(Long.valueOf(yr));
		 String yearInWrds = "";
		 if(month.intValue()>4)
		 {				 
			 cal.setTime(cyr.getStartingDate());
			 yearInWrds=cal.get(Calendar.YEAR)+"";
		 }else
		 {
			 cal.setTime(cyr.getEndingDate());
			 yearInWrds=cal.get(Calendar.YEAR)+"";
		 }
		 List<CFinancialYear> finYearList = (List)payrollExternalInterface.getAllFinancialYearList();
		 String finYrRange="";
		 for(Iterator<CFinancialYear> itr=finYearList.iterator(); itr.hasNext();)
		 {
			 CFinancialYear finyr=itr.next();
			 if(finyr.getId().intValue()== year.intValue())
				 finYrRange=finyr.getFinYearRange();
		 }
		 paramsMap.put("pdftitle","Payslip for the month of "+getMonthInWords(month)+" "+ finYrRange);
		 paramsMap.put("MONTH",mon );
		 String status = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCancelledStatus",new Date()).getValue();
		  EgwStatus egwStatus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
				  PayrollConstants.PAYSLIP_MODULE, status);
		 paramsMap.put("STATUS",egwStatus.getId());
		 paramsMap.put("MONTHINWORDS",getMonthInWords(month) );
		 paramsMap.put("yearInWrds",yearInWrds);
		 paramsMap.put("FINYR",year );
		 paramsMap.put("EMPID",empId );
		 paramsMap.put("EARNING_SUB_REPORT",earningReport );
		 paramsMap.put("DEDUCTION_SUB_REPORT",deductionReport );
		 paramsMap.put("LOAN_SUB_REPORT",loansReport );
		 if(!("").equalsIgnoreCase(billNum))
		 {
			 paramsMap.put("billNum","Bill Number : "+billNum);
		 }	 
		 else
		 {
			 paramsMap.put("billNum", "");
		 }
		 paramsMap.put("billId", billNum);
		 logger.info("before fill report ");
		 
		 
		 //Generating pdf/rtf using ReportService
		 String EMP_PAYSLIP_JRXML = "Emp_payslip";
		 ReportRequest reportInput = new ReportRequest(EMP_PAYSLIP_JRXML, paramsMap, ReportDataSourceType.SQL);
		 /*ReportUtil.getTemplateAsStream(paramsMap.get(LOANSJASPER)==null?"Loans.jasper":(String)paramsMap.get(LOANSJASPER));		*/	 
		 reportInput.setReportFormat(FileFormat.valueOf(reportExtn.toUpperCase()));			
		 ReportOutput ro = reportService.createReport(reportInput);
		 saveReportFile(ro, fNameWithoutExtn,reportExtn);
		 
		 //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramsMap,HibernateUtil.getCurrentSession().connection());
		 //storePDF(jasperPrint,fName);
		 logger.info("before store pdf call");
		 
		 return ro;	
   }

	private void saveReportFile(ReportOutput ro, String fNameWithoutExtn, String reportExtn) throws IOException {
		logger.info("Inside file save------");
		//String fileExtn = ro.getReportFormat().name().toLowerCase();
		ro.getReportOutputData();		
		//save file
		//File f = new File(fNameWithoutExtn+"."+reportExtn);
		//f.createNewFile();
	    FileOutputStream fop = new FileOutputStream(fNameWithoutExtn+"."+reportExtn, false);
        fop.write(ro.getReportOutputData());
        fop.flush();
	    fop.close();	          
	}
	
	/*
	 * This method stores the pdf in the specified path under the server
	 */
	
    public void  storePDF(JasperPrint jasperPrint,String fName) throws Exception
    {
    	try {
    		logger.info("before store");
    	List<JasperPrint> jasperPrintList= new ArrayList<JasperPrint>();
    	JRPdfExporter pdfExporter = new JRPdfExporter();
		jasperPrintList.add(jasperPrint);
		pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrintList);
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME , fName);
        pdfExporter.exportReport();
        logger.info("after store");
    	}
    	catch (JRException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			throw e; 
		}
    } 
    private String getMonthInWords(Integer month)
    {
    	switch(month)
    	{
	    	case 1:return "Jan";
	    	case 2:return "Feb";
	    	case 3:return "Mar";
	    	case 4:return "Apr";
	    	case 5:return "May";
	    	case 6:return "Jun";
	    	case 7:return "Jul";
	    	case 8:return "Aug";
	    	case 9:return "Sep";
	    	case 10:return "Oct";
	    	case 11:return "Nov";
	    	case 12:return "Dec";
	    	default: return null;
    	}
    }
	
}

	



