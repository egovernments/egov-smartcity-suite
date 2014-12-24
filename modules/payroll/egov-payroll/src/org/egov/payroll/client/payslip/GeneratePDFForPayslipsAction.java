package org.egov.payroll.client.payslip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.reports.MonthlyPayslipReports;


/** This Class generate PDF output for the payslips based on the selected month,year and department
 * 1. It checks whether the PDF file is already generated for the selected input parameter
 *   a)If Yes - Then the specific file will read from the disk without generating again.
 * 	 b)If No - The the PDF output is generated and saved in the disk for the selected input parameters.
 * 2.If (flag==yes)
 *   a)The generation of PDF is done respective of checking the file exists or not.
 * 3.If (flag==yes) the generated is shown.
 * 
 * @author Lokesh, Mamatha , Divya
 *
 */
public class GeneratePDFForPayslipsAction extends Action{
	private static final Logger logger = Logger.getLogger(GeneratePDFForPayslipsAction.class);	
	
	private MonthlyPayslipReports monthlyPayslipReport;
	
		
	public MonthlyPayslipReports getMonthlyPayslipReport() {
		return monthlyPayslipReport;
	}
	public void setMonthlyPayslipReport(MonthlyPayslipReports monthlyPayslipReport) {
		this.monthlyPayslipReport = monthlyPayslipReport;
	}
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse resp)
	throws Exception
	{
		try
		{	
			ReportOutput reportOutput = null;
			String cityName="";
			String cityname="";
			String citynameLocal="";
			if(req.getSession().getAttribute("cityName")!=null)
			 {
				cityName=(String)req.getSession().getAttribute("cityName");
			 }
			
			logger.info("from session cityName "+cityName);
			 cityname=(String)req.getSession().getAttribute("cityname");
			 logger.info("from session cityname"+cityname);
			 citynameLocal=(String)req.getSession().getAttribute("citynamelocal");
			 logger.info("from session citynamelocal"+citynameLocal);
			//String filePath = EGovConfig.getProperty("payroll_egov_config.xml","PDFPath","", EGOVThreadLocals.getDomainName()+".Batch");
			String filePath = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipPdfPath",new Date()).getValue();
			//	EGovConfig.getProperty("payroll_egov_config.xml",mainCity,"", "PAYROLL");
			String reportExtn = req.getParameter("reportExtn");
			String month = req.getParameter("month");
			logger.info("month------------> : " + month);
			String year = req.getParameter("fin_Year");
			logger.info("year------------> : " + year);
			String dept = req.getParameter("dept");
			logger.info("dept------------> : " + dept);
			String flag = req.getParameter("flag");
			logger.info("flag------------> : " + flag);
			String empId = req.getParameter("employeeCodeId");
			logger.info("empId---------->"+empId);
			String empCode = req.getParameter("employeeCode");
			logger.info("empCode---------->"+empCode);
			String billNum="";
			if(null!=req.getParameter("billNumberId") && !req.getParameter("billNumberId").equalsIgnoreCase(""))
			{
				if(null!=req.getParameter("billNumber") && !req.getParameter("billNumber").equalsIgnoreCase(""))
				{
					billNum=req.getParameter("billNumber");
				}	
			}
			
			/*The Name conventions for the PDF file is as follows:
		 	1. Month selected for PDf Generation.
		 	2. Financial Year ID should be predfixed with "FY"
		 	3. Department ID selected for PDf Generation.

		 	   Eg : 04-FY06-01.PDF

		 	 In some case department is null, then the department is 
		 	 considererd as "00" 

		 	 	Eg : 04-FY06-00 
			 */
			AppConfigValues appConfigValues = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll","LocalLang",new Date());
			
			String fName="";
			if(! "0".equals(dept))
			{
				fName = filePath+"0"+month+"-"+"FY"+"0"+year+"-"+"0"+dept+billNum+"."+reportExtn;
			}
			else if(!(empId==null) && !("").equals(empId))
			{
				fName = filePath+"0"+month+"-"+"FY"+"0"+year+"-"+empCode+billNum+"."+reportExtn;
			}
			else
			{
				fName = filePath+"0"+month+"-"+"FY"+"0"+year+"-00"+billNum+"."+reportExtn;
			}
			File file=new File(fName);
			boolean exists = file.exists();
			Map<String,String> paramMap=new HashMap<String,String>();
			String localLang =null;
			if(appConfigValues!=null)
			{
				localLang =appConfigValues.getValue();
				final String jasper=".jasper";
				cityname=citynameLocal;//cityname  used for pdftitle,if locallang exists in DB pdf title cityname should dispaly in regional lang
				logger.info("localLang"+localLang);
				paramMap.put(("Earnings"+jasper),("/Earnings_"+localLang.toLowerCase()+jasper));
				paramMap.put(("Deductions"+jasper),("/Deductions_"+localLang.toLowerCase()+jasper));
				paramMap.put(("Loans"+jasper),("/Loans_"+localLang.toLowerCase()+jasper));
				
				paramMap.put("employee-tl", getMessageBundle(req,"employee-tl"));
				paramMap.put("designation-tl", getMessageBundle(req,"designation-tl"));
				paramMap.put("bankACNo-tl", getMessageBundle(req,"bankACNo-tl"));
				paramMap.put("center-tl", getMessageBundle(req,"center-tl"));
				paramMap.put("earningsOrAllowances-tl", getMessageBundle(req,"earningsOrAllowances-tl"));
				paramMap.put("deductions-tl", getMessageBundle(req,"deductions-tl"));
				paramMap.put("totalEarnings-tl", getMessageBundle(req,"totalEarnings-tl"));
				paramMap.put("totalDeductions-tl", getMessageBundle(req,"totalDeductions-tl"));
				paramMap.put("netpay-tl", getMessageBundle(req,"netpay-tl"));
				paramMap.put("computerGenPayslip-tl", getMessageBundle(req,"computerGenPayslip-tl"));
				paramMap.put("department-tl", getMessageBundle(req, "department-tl"));
				paramMap.put("cashOrCheque-tl", getMessageBundle(req, "cashOrCheque-tl"));
			}
			else
			{
			paramMap.put("employee", getMessageBundle(req,"employee"));
			paramMap.put("designation", getMessageBundle(req,"designation"));
			paramMap.put("bankACNo", getMessageBundle(req,"bankACNo"));
			paramMap.put("center", getMessageBundle(req,"center"));
			paramMap.put("earningsOrAllowances", getMessageBundle(req,"earningsOrAllowances"));
			paramMap.put("deductions", getMessageBundle(req,"deductions"));
			paramMap.put("totalEarnings", getMessageBundle(req,"totalEarnings"));
			paramMap.put("totalDeductions", getMessageBundle(req,"totalDeductions"));
			paramMap.put("netpay", getMessageBundle(req,"netpay"));
			paramMap.put("computerGenPayslip", getMessageBundle(req,"computerGenPayslip"));
			paramMap.put("department", getMessageBundle(req, "department"));
			paramMap.put("cashOrCheque", getMessageBundle(req, "cashOrCheque"));
			}
			
			//It returns false if File or directory does not exist

			if (!exists|| "yes".equals(flag)) {
				logger.info("the file already existing  : " + exists);
				MonthlyPayslipReports mon = monthlyPayslipReport;
				if(!(empId==null) && !"".equals(empId))
				{
					//some Api
					if(localLang!=null)
					{
						paramMap.put("Emp_payslip.jasper",("/Emp_payslip_"+localLang.toLowerCase()+".jasper"));
						logger.info("localLang for empid "+paramMap.get("Emp_payslip.jasper"));
					}
						
					reportOutput = mon.generateMonthlyPayslipPDFofAnEmployee(new Integer(month),new Integer(year), new Integer(empId),new Integer(empCode),billNum,cityname,paramMap,reportExtn);
				}
				else
				{
					if(localLang!=null)
					{
						paramMap.put("VMC_Payslips.jasper",("/VMC_Payslips_"+localLang.toLowerCase()+".jasper"));
						logger.info("localLang for dep "+paramMap.get("VMC_Payslips.jasper"));
					}
					reportOutput = mon.generateMonthlyPayslipsPDF(new Integer(month),new Integer(year),new Integer(dept),billNum,cityname,paramMap,reportExtn);
				}
				generatePDFOutput(fName, req, resp, reportExtn);				 
			 
			 //  req.getSession().setAttribute("reportid", reportId);
			}else{
				// It returns true if File exists
				logger.info("the file already existing : " + exists);
				generatePDFOutput(fName, req, resp, reportExtn);
			}		    
			return null;
		}catch(EGOVRuntimeException egovExp)
		{
			logger.error(egovExp.getMessage());
			egovExp.printStackTrace();
			HibernateUtil.rollbackTransaction();
			return mapping.findForward("error");	
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
			HibernateUtil.rollbackTransaction();
			return mapping.findForward("error");
		}
	}
	/**
	 * This method shows the pdf output in browser by passing the file Name with absolute path.
	 * @param fName - fully qualified path of fileName
	 * @param req - HttpServletRequest  request object
	 * @param resp - HttpServletRequest  respone object
	 * @throws JRException
	 * @throws IOException
	 */
	private void generatePDFOutput(String fName,HttpServletRequest req, HttpServletResponse resp, String reportExtn)
	throws JRException, IOException {

		try{
		File f = new File(fName);
		FileInputStream istr = new FileInputStream(f);
		BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

		int size = (int) f.length(); // get the file size (in bytes)
		byte[] data = new byte[size]; // allocate byte array of right size
		bstr.read( data, 0, size ); // read into byte array

		bstr.close();
		
		FileFormat fileFormat = FileFormat.valueOf(reportExtn.toUpperCase());
		logger.info("f.getName():-------" + f.getName());
		if (data != null && data.length > 0) {
			resp.setHeader("Content-disposition", "attachment; filename=Payslip." + reportExtn);
			resp.setContentType(ReportViewerUtil.getContentType(fileFormat));			
			//resp.setHeader("pragma", "no-cache");
			ServletOutputStream ouputStream = resp.getOutputStream();
			ouputStream.write(data, 0, data.length);
			ouputStream.flush();
			ouputStream.close();
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new EGOVRuntimeException(e.getMessage());
		}
	}
	protected  String getMessageBundle(HttpServletRequest request,String key){
		MessageResources messageResources=null;
		messageResources=getResources(request);
		return messageResources.getMessage(key);
		}

}



