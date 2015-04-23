package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.deduction.RemittanceBean;
import org.egov.model.payment.ChequeAssignment;
import org.egov.model.recoveries.Recovery;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.services.deduction.RemitRecoveryService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.payment.BankAccountRemittanceCOA;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;




@Results(value={
		@Result(name="PDF",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=AutoRemittanceReport.pdf"}),
		@Result(name="XLS",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=AutoRemittanceReport.xls"}),
		@Result(name="summary-PDF",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=AutoRemittanceCOCLevel.pdf"}),
		@Result(name="summary-XLS",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=AutoRemittanceReportCOCLevel.xls"})
	})
     
@ParentPackage("egov")  
public class AutoRemittanceReportAction extends BaseFormAction{
	String deptLevelJasperpath = "AutoRemittanceReport";   
	String cocLevelJasperpath = "AutoRemittanceCOCLevelReport";   
	private InputStream inputStream;    
	private ReportService reportService;
	private Date paymentVoucherFromDate;
	private Date paymentVoucherToDate;
	private Date rtgsAssignedFromDate;
	private Date rtgsAssignedToDate;
	private Recovery recovery = new Recovery();
	private Fund fund = new Fund();
	private Department department = new Department();
	private EgovCommon egovCommon;
	private List<EntityType> entitiesList = new ArrayList<EntityType>();
	private RemitRecoveryService remitRecoveryService;
	private FinancialYearHibernateDAO financialYearDAO;	
	private Bank bank;
	private Bankbranch bankbranch;
	private Bankaccount bankaccount;   
	private String instrumentNumber;
	private String level;
	private DrawingOfficer drawingOfficer;
	private String supplierCode;
	private String contractorCode;
	private String accountNumber;
	private BigDecimal remittedAmountTotal = new BigDecimal("0");
	private List<AutoRemittanceBeanReport> autoRemittance = new ArrayList<AutoRemittanceBeanReport>();
	private Map<String,Object> map = new HashMap<String,Object> ();
	Map<AutoRemittanceBeanReport,List<AutoRemittanceBeanReport>> autoremittanceMap = new HashMap<AutoRemittanceBeanReport,List<AutoRemittanceBeanReport>>();
	private List<AutoRemittanceCOCLevelBeanReport> coaAbstract =new  ArrayList<AutoRemittanceCOCLevelBeanReport>(0);
	private List<AutoRemittanceCOCLevelBeanReport> remittanceList =new  ArrayList<AutoRemittanceCOCLevelBeanReport>(0);
	
	private static Logger LOGGER=Logger.getLogger(AutoRemittanceReportAction.class);
	
	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;  
	}  

	public void setRemitRecoveryService(RemitRecoveryService remitRecoveryService) {
		this.remitRecoveryService = remitRecoveryService;
	}

	@Override
	public String execute() throws Exception {
		return "reportForm";
	}

@Action(value="/report/autoRemittanceReport-newform")
	public String newform() throws Exception {
		return "reportForm";
	}
	   
	@Override
	public void prepare() {
		/HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
		/HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		addDropdownData("recoveryList", persistenceService.findAllBy(" from Recovery where isactive=1 order by chartofaccounts.glcode"));
		addDropdownData("bankList", Collections.EMPTY_LIST);
		addDropdownData("bankBranchList", Collections.EMPTY_LIST);
		addDropdownData("bankAccountList", Collections.EMPTY_LIST);
		addDropdownData("accNumList", Collections.EMPTY_LIST);
		addDropdownData("drawingList", Collections.EMPTY_LIST);
		
	}    
	

@Action(value="/report/autoRemittanceReport-ajaxLoadData")
	public String ajaxLoadData(){
		
		populateData();
		boolean addList=false;
		List<AutoRemittanceBeanReport> autoremitEntry =new ArrayList<AutoRemittanceBeanReport>();
		AutoRemittanceBeanReport selAutoRemit = new AutoRemittanceBeanReport();
		if(level.equals("atcoc"))
		{     
			populateCOCLevelSummaryData(); 
				for(AutoRemittanceBeanReport  autoRemit:autoRemittance){
					AutoRemittanceBeanReport autoremitKey = new AutoRemittanceBeanReport();
						if(autoremittanceMap.isEmpty()){
							       autoremitEntry=new ArrayList<AutoRemittanceBeanReport>();
							       autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
							       autoremitKey.setDepartment(autoRemit.getDepartment());
							       autoremitKey.setDrawingOfficer(autoRemit.getDrawingOfficer());
							       autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
							       selAutoRemit=autoremitKey;              
							       autoremitEntry.add(autoRemit); 
							       autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
							       autoremittanceMap.put(autoremitKey,autoremitEntry);   
						}else{
									Set<AutoRemittanceBeanReport> autoRemitKeySet=autoremittanceMap.keySet();  
									java.util.Iterator keySetitr =autoRemitKeySet.iterator();
									  while (keySetitr.hasNext()) {   
										  AutoRemittanceBeanReport autormt = (AutoRemittanceBeanReport) keySetitr.next();
										 addList=false;
										 if(autormt.getRemittanceCOA().equals(autoRemit.getRemittanceCOA()) &&
												 autormt.getDepartment().equals(autoRemit.getDepartment()) &&
												 autormt.getDrawingOfficer().equals(autoRemit.getDrawingOfficer()) &&
												 autormt.getBankbranchAccount().equals(autoRemit.getBankbranchAccount())){
											 autormt.setRemittedAmountSubtotal(autormt.getRemittedAmountSubtotal().add(autoRemit.getRemittedAmount()));
											 autoremitKey=autormt;addList=false;
												break;
									     }else{   
									    	 	addList=true;
									     }  
									 }              
									if(!addList){          
										autoremittanceMap.get(autoremitKey).add(autoRemit);
										selAutoRemit=null;
										}           
									else{    
										autoremitEntry=new ArrayList<AutoRemittanceBeanReport>();
										  autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
									       autoremitKey.setDrawingOfficer(autoRemit.getDrawingOfficer());
									       autoremitKey.setDepartment(autoRemit.getDepartment());
									       autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
									       autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
									       selAutoRemit=autoremitKey;              
									       autoremitEntry.add(autoRemit); 
									       autoremittanceMap.put(autoremitKey,autoremitEntry);  
									}   
						}   
					     remittedAmountTotal =remittedAmountTotal.add(autoRemit.getRemittedAmount());
				} 
		}
		else
		{ 
			for(AutoRemittanceBeanReport  autoRemit:autoRemittance){
				AutoRemittanceBeanReport autoremitKey = new AutoRemittanceBeanReport();
					if(autoremittanceMap.isEmpty()){
						       autoremitEntry=new ArrayList<AutoRemittanceBeanReport>();
						       autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
						       autoremitKey.setFundName(autoRemit.getFundName());
						       autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
						       selAutoRemit=autoremitKey;              
						       autoremitEntry.add(autoRemit); 
						       autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
						       autoremittanceMap.put(autoremitKey,autoremitEntry);   
					}else{
								Set<AutoRemittanceBeanReport> autoRemitKeySet=autoremittanceMap.keySet();  
								java.util.Iterator keySetitr =autoRemitKeySet.iterator();
								  while (keySetitr.hasNext()) {   
									  AutoRemittanceBeanReport autormt = (AutoRemittanceBeanReport) keySetitr.next();
									 addList=false;
									 if(autormt.getRemittanceCOA().equals(autoRemit.getRemittanceCOA()) &&
											 autormt.getFundName().equals(autoRemit.getFundName()) &&
											 autormt.getBankbranchAccount().equals(autoRemit.getBankbranchAccount())){
										 autormt.setRemittedAmountSubtotal(autormt.getRemittedAmountSubtotal().add(autoRemit.getRemittedAmount()));
										 autoremitKey=autormt;addList=false;
											break;
								     }else{   
								    	 	addList=true;
								     }  
								 }              
								if(!addList){          
									autoremittanceMap.get(autoremitKey).add(autoRemit);
									selAutoRemit=null;
									}           
								else{    
									autoremitEntry=new ArrayList<AutoRemittanceBeanReport>();
									  autoremitKey.setRemittanceCOA(autoRemit.getRemittanceCOA());
								       autoremitKey.setFundName(autoRemit.getFundName());
								       autoremitKey.setBankbranchAccount(autoRemit.getBankbranchAccount());
								       autoremitKey.setRemittedAmountSubtotal(autoRemit.getRemittedAmount());
								       selAutoRemit=autoremitKey;              
								       autoremitEntry.add(autoRemit); 
								       autoremittanceMap.put(autoremitKey,autoremitEntry); 
								}   
					}   
				     remittedAmountTotal =remittedAmountTotal.add(autoRemit.getRemittedAmount());
			} 
		}
	HibernateUtil.getCurrentSession().put("autoremittanceMap", autoremittanceMap);
	    return "results";    
 	}                 

@Action(value="/report/autoRemittanceReport-exportXls")
	public String exportXls() throws JRException, IOException{
		populateData();    
		if(level.equals("atcoc"))
		{
			  StringBuffer finyearQuery = new StringBuffer();
			  Date currentDate = new Date();
			  finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
			  CFinancialYear financialyear =(CFinancialYear) persistenceService.find(finyearQuery.toString());
			if(null == paymentVoucherFromDate)
			{
				paymentVoucherFromDate= financialyear.getStartingDate();
			}
			if(null == paymentVoucherToDate)
			{
				 paymentVoucherToDate = financialyear.getEndingDate();
			}  
			map.put("autoremittanceList",autoRemittance);
			populateCOCLevelSummaryData();       
			ReportRequest reportInput = new ReportRequest(cocLevelJasperpath, map, getParamMap());    
			reportInput.setReportFormat(FileFormat.XLS);   
			ReportOutput reportOutput = reportService.createReport(reportInput);
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
			return "summary-XLS";
		}
		else
		{
			ReportRequest reportInput = new ReportRequest(deptLevelJasperpath, autoRemittance, getParamMap());
			reportInput.setReportFormat(FileFormat.XLS);
			ReportOutput reportOutput = reportService.createReport(reportInput);
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		    return "XLS";
		}
	}
	  
	      
@Action(value="/report/autoRemittanceReport-exportPdf")
	public String exportPdf()
	{
		populateData();
		if(level.equals("atcoc"))
		{
			  StringBuffer finyearQuery = new StringBuffer();
			  Date currentDate = new Date();
			  finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
			  CFinancialYear financialyear =(CFinancialYear) persistenceService.find(finyearQuery.toString());
			if(null == paymentVoucherFromDate)
			{
				paymentVoucherFromDate= financialyear.getStartingDate();
			}
			if(null == paymentVoucherToDate)
			{
				 paymentVoucherToDate = financialyear.getEndingDate();
			}
			map.put("autoremittanceList",autoRemittance);
			populateCOCLevelSummaryData();       
			ReportRequest reportInput = new ReportRequest(cocLevelJasperpath, map, getParamMap());
			ReportOutput reportOutput = reportService.createReport(reportInput);
			inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
			return "summary-PDF";
		}
		else{
			  ReportRequest reportInput = new ReportRequest(deptLevelJasperpath, autoRemittance, getParamMap());
			  ReportOutput reportOutput = reportService.createReport(reportInput);
			  inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());  
			  return "PDF";
		}
		
	}        
	
	Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		StringBuffer header = new StringBuffer("");
		if(level.equals("atcoc"))
		{
			header.append("Summary of remittance for the date range ");
			header.append(Constants.DDMMYYYYFORMAT2.format(paymentVoucherFromDate) + "  to  "+Constants.DDMMYYYYFORMAT2.format(paymentVoucherToDate));
			StringBuffer detailheader = new StringBuffer("Auto remittance payment report for ");
			recovery =(Recovery) persistenceService.find("from Recovery  where id =?", recovery.getId());
			detailheader.append(recovery.getType() +" - " + recovery.getRecoveryName());
			paramMap.put("detailheader", detailheader.toString());   
		}  
		else       
		{
			header.append(" Auto remittance payment report for ");
			department = (Department) persistenceService.find("from Department where id=?",department.getId());
			header.append(department.getDeptName()+ " department ");
		}   
		  
		
		
		paramMap.put("header", header.toString());   
		if(null != recovery && null != recovery.getId() && recovery.getId() != -1)
		{
		  recovery =(Recovery) persistenceService.find("from Recovery  where id =?", recovery.getId());
		  paramMap.put("remittanceCOA", recovery.getType());
		}         
		if(null != paymentVoucherFromDate)
		{
			String formatedDate = Constants.DDMMYYYYFORMAT2.format(paymentVoucherFromDate);
			paramMap.put("payVoucherFromDate", formatedDate);
		}
		if(null != paymentVoucherToDate)
		{
			String formatedDate = Constants.DDMMYYYYFORMAT2.format(paymentVoucherToDate);
			  paramMap.put("payVoucherToDate", formatedDate);  
		}
		if(null != fund && null != fund.getId() && fund.getId() != -1)
		{
			fund = (Fund) persistenceService.find("from Fund where id=?",fund.getId());
			paramMap.put("fund", fund.getName());
		}
		if(null != drawingOfficer && null != drawingOfficer.getId() &&  drawingOfficer.getId() != -1)
		{
			drawingOfficer = (DrawingOfficer) persistenceService.find("from DrawingOfficer where id=?",drawingOfficer.getId() );
			paramMap.put("drawingOfficer",drawingOfficer.getName());
		}
		if(null != rtgsAssignedFromDate)
		{
			String formatedDate = Constants.DDMMYYYYFORMAT2.format(rtgsAssignedFromDate);
			paramMap.put("rtgsFromDate", formatedDate);
		}
		if(null != rtgsAssignedToDate)
		{
			String formatedDate = Constants.DDMMYYYYFORMAT2.format(rtgsAssignedToDate);
			paramMap.put("rtgsToDate",formatedDate);
		}	   
		if(null != instrumentNumber)
			paramMap.put("rtgsNum",instrumentNumber);
		if(null != bank && null != bank.getId() && bank.getId() != -1)
		{
			 bank = (Bank) persistenceService.find("from Bank where id = ?", bank.getId());
			 paramMap.put("bank", bank.getName());
		}
		if(null != supplierCode && !supplierCode.isEmpty())
		   paramMap.put("supplierName",supplierCode);
		if(null != contractorCode && !contractorCode.isEmpty())
			paramMap.put("contractorName",contractorCode);
		if(null != bankbranch && null != bankbranch.getId() && bankbranch.getId() != -1)
		{   
		    bankbranch = (Bankbranch) persistenceService.find("from Bankbranch where id =?", bankbranch.getId());
		    paramMap.put("bankBranch", bankbranch.getBranchname());
		   
		}
		if(null != bankaccount && null != bankaccount.getId()  && bankaccount.getId() != -1)
		{
			bankaccount = (Bankaccount) persistenceService.find("from Bankaccount where id =?", bankbranch.getId());
			 paramMap.put("bankAccountNum", bankaccount.getAccountnumber());
		}
	Date  currentDate = new Date();
	String reportRunDate = Constants.DDMMYYYYFORMAT2.format(currentDate);
	paramMap.put("reportRunDate", reportRunDate);
		return paramMap;
	}         
	
	private void populateData()
	{
		StringBuffer query = new StringBuffer("");  
		Date currentDate = new Date();
		StringBuffer finyearQuery = new StringBuffer();
		  
		finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
		CFinancialYear financialyear =(CFinancialYear) persistenceService.find(finyearQuery.toString());
		
		if(level.equals("atcoc"))
		{      
			query.append("SELECT CONCAT(CONCAT(coa.GLCODE ,' - ') ,coa.NAME) AS remittanceCOA," +
					"      dept.DEPT_NAME  AS department,CONCAT(	CONCAT(DO.NAME,'/') , DO.TAN) AS drawingOfficer , " +
					" CONCAT(CONCAT( CONCAT(CONCAT(bank.NAME, '  '),bnkbranch.BRANCHNAME), ' - '), bnkacc.ACCOUNTNUMBER) AS bankbranchAccount," +
					"  vh.VOUCHERNUMBER AS remittancePaymentNo, CONCAT(CONCAT(ih.INSTRUMENTNUMBER ,'/'),ih.INSTRUMENTDATE ) rtgsNoDate," +
					" ih.INSTRUMENTAMOUNT AS rtgsamount, remdt.ID AS remittanceDTId ,vh.id as paymentVoucherId "+ 
					" FROM EG_REMITTANCE rem, EG_REMITTANCE_DETAIL remdt,EG_REMITTANCE_GLDTL remgltl," +
					" EGF_INSTRUMENTHEADER ih,EGF_INSTRUMENTVOUCHER iv, VOUCHERHEADER vh,TDS TDS,PAYMENTHEADER ph,BANKACCOUNT bnkacc," +
					" GENERALLEDGER gl, GENERALLEDGERDETAIL gld, chartofaccounts coa, fund fund,bank bank,bankbranch bnkbranch,EG_DEPARTMENT dept,EG_DRAWINGOFFICER DO " +
					" WHERE rem.id = remdt.REMITTANCEID AND remdt.REMITTANCEGLDTLID =remgltl.ID" +
					" AND   rem.paymentvhid = iv.voucherheaderid AND  iv.instrumentheaderid = ih.ID AND  iv.voucherheaderid= vh.id " +
					" AND rem.tdsid = TDS.id AND 	 fund.id= vh.fundid " +
					" AND TDS.REMITTANCE_MODE='A' AND vh.status=0 " +      
					" AND ph.VOUCHERHEADERID = vh.id  " +
					" AND ph.BANKACCOUNTNUMBERID = bnkacc.ID AND gl.VOUCHERHEADERID= vh.id AND gld.GENERALLEDGERID=gl.id AND  dept.ID_DEPT = vh.departmentid " +
					" AND DO.ID =ph.DRAWINGOFFICER_ID	 AND ph.DRAWINGOFFICER_ID IS NOT NULL AND rem.paymentvhid IS  NOT  NULL " +
					" AND ih.ID_STATUS= (SELECT  id  FROM EGW_STATUS WHERE moduletype='Instrument' AND code='New') " +
					" AND bnkacc.BRANCHID=bnkbranch.ID  AND bank.id =bnkbranch.BANKID   AND coa.id= tds.GLCODEID ");
		}
		else
		{
		  query.append("SELECT CONCAT(CONCAT(coa.GLCODE ,' - ') ,coa.NAME) AS remittanceCOA," +
				" fund.NAME AS fundName, CONCAT(CONCAT( CONCAT(CONCAT(bank.NAME, '  '),bnkbranch.BRANCHNAME), ' - '), bnkacc.ACCOUNTNUMBER) AS bankbranchAccount," +
				"  vh.VOUCHERNUMBER AS remittancePaymentNo, CONCAT(CONCAT(ih.INSTRUMENTNUMBER ,'/'),ih.INSTRUMENTDATE ) rtgsNoDate," +
				" ih.INSTRUMENTAMOUNT AS rtgsamount, remdt.ID AS remittanceDTId ,vh.id as paymentVoucherId "+ 
				" FROM EG_REMITTANCE rem, EG_REMITTANCE_DETAIL remdt,EG_REMITTANCE_GLDTL remgltl," +
				" EGF_INSTRUMENTHEADER ih,EGF_INSTRUMENTVOUCHER iv, VOUCHERHEADER vh,TDS TDS,PAYMENTHEADER ph,BANKACCOUNT bnkacc," +
				" GENERALLEDGER gl, GENERALLEDGERDETAIL gld, chartofaccounts coa, fund fund,bank bank,bankbranch bnkbranch " +
				" WHERE rem.id = remdt.REMITTANCEID AND remdt.REMITTANCEGLDTLID =remgltl.ID" +
				" AND   rem.paymentvhid = iv.voucherheaderid AND  iv.instrumentheaderid = ih.ID AND  iv.voucherheaderid= vh.id " +
				" AND rem.tdsid = TDS.id AND 	 fund.id= vh.fundid " +
				" AND TDS.REMITTANCE_MODE='A' AND vh.status=0 " +      
				" AND ph.VOUCHERHEADERID = vh.id  " +
				" AND ph.BANKACCOUNTNUMBERID = bnkacc.ID AND gl.VOUCHERHEADERID= vh.id AND gld.GENERALLEDGERID=gl.id AND  rem.paymentvhid IS  NOT  NULL " +
				" AND ih.ID_STATUS= (SELECT  id  FROM EGW_STATUS WHERE moduletype='Instrument' AND code='New') " +
				" AND bnkacc.BRANCHID=bnkbranch.ID  AND bank.id =bnkbranch.BANKID   AND coa.id= tds.GLCODEID ");    
		}
		if(null != department &&  null != department.getId() &&   department.getId() != -1)    
		{
			query.append(" AND vh.DEPARTMENTID = "+ department.getId() );
		}	
		if(null != recovery && null != recovery.getId() && recovery.getId() != -1)
		{
		   query.append(" AND  TDS.id = "+recovery.getId());   	    
		}   
		
		if(level.equals("atcoc"))
		{
				if(null != paymentVoucherFromDate){
					query.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate)+"'" );
				}
				else{
					query.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(financialyear.getStartingDate())+"'" );
				}
				if(null != paymentVoucherToDate){ 
					query.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate)+"'" );
				}
				else
				{
					query.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(financialyear.getEndingDate())+"'" );
				}	
		}
		else
		{
			if(null != paymentVoucherFromDate){
				query.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate)+"'" );
			}
			if(null != paymentVoucherToDate){ 
				query.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate)+"'" );
			}
		}
		if(null != fund && null != fund.getId() && fund.getId() != -1)
		{
			query.append(" AND vh.fundid= "+fund.getId());
		}
		if(null != drawingOfficer && null != drawingOfficer.getId() &&  drawingOfficer.getId() != -1)
		{
			query.append(" AND ph.DRAWINGOFFICER_ID ="+ drawingOfficer.getId());
		}
		if(null != rtgsAssignedFromDate)
		{
			query.append(" AND ih.INSTRUMENTDATE >= '"+Constants.DDMMYYYYFORMAT1.format(rtgsAssignedFromDate)+"'");
		}
		if(null != rtgsAssignedToDate)
		{
			query.append(" AND ih.INSTRUMENTDATE <= '"+Constants.DDMMYYYYFORMAT1.format(rtgsAssignedToDate)+"'");
			query.append(rtgsAssignedToDate + "'");
		}
		if(null != instrumentNumber)
		{    
			query.append(" AND ih.INSTRUMENTNUMBER = '"+instrumentNumber+"'");
		}
		if(null != bank && null != bank.getId() && bank.getId() != -1)
		{
			query.append("AND bank.id = "+bank.getId());
		}
		if(null != supplierCode && !supplierCode.isEmpty())
		{
			query.append(" AND ( gld.DETAILKEYID = " + new Integer(supplierCode) + " AND gld.DETAILTYPEID=(SELECT id FROM accountdetailtype WHERE name='Creditor'))");
		}
		if(null != contractorCode && !contractorCode.isEmpty())
		{
			query.append(" AND ( gld.DETAILKEYID = " + new Integer(contractorCode) + " AND gld.DETAILTYPEID=(SELECT id FROM accountdetailtype WHERE name='contractor'))");
		}
		if(null != bankbranch && null != bankbranch.getId() && bankbranch.getId() != -1)
		{   
			query.append("AND bnkacc.BRANCHID = " + bankbranch.getId());  
		}
		if(null != bankaccount && null != bankaccount.getId()  && bankaccount.getId() != -1)
		{
			query.append(" AND bnkacc.id = "+bankaccount.getId());
		}
		if(level.equals("atcoc"))
			query.append("  GROUP BY coa.GLCODE ,coa.NAME,dept.DEPT_NAME, DO.NAME, DO.TAN,");
		else
			query.append("  GROUP BY coa.GLCODE ,coa.NAME, fund.NAME ,");     
		
		query.append(" bank.NAME,bnkbranch.BRANCHNAME, bnkacc.ACCOUNTNUMBER, vh.VOUCHERNUMBER ,ih.INSTRUMENTNUMBER ,ih.INSTRUMENTDATE," +
				" ih.INSTRUMENTAMOUNT,remdt.ID,vh.id ");
		
		if(level.equals("atcoc"))
			query.append(" order by  coa.GLCODE ,coa.NAME,dept.DEPT_NAME, DO.NAME, DO.TAN,");
		else
			query.append(" order by  coa.GLCODE ,coa.NAME, fund.NAME ,");
		
		query.append(" bank.NAME,bnkbranch.BRANCHNAME, bnkacc.ACCOUNTNUMBER, vh.VOUCHERNUMBER ,ih.INSTRUMENTNUMBER ,ih.INSTRUMENTDATE," +
					 " ih.INSTRUMENTAMOUNT,remdt.ID ");

		Session session =HibernateUtil.getCurrentSession();
		Query sqlQuery  = null ;
		if(level.equals("atcoc"))         
		{
				sqlQuery = session.createSQLQuery(query.toString())         
				.addScalar("remittanceCOA").addScalar("department") .addScalar("drawingOfficer").addScalar("bankbranchAccount")
				.addScalar("remittancePaymentNo").addScalar("rtgsNoDate")         
				.addScalar("rtgsAmount").addScalar("remittanceDTId") .addScalar("paymentVoucherId")
				.setResultTransformer(Transformers.aliasToBean(AutoRemittanceBeanReport.class));
		}
		else
		{
			sqlQuery = session.createSQLQuery(query.toString())   
					.addScalar("remittanceCOA").addScalar("fundName").addScalar("bankbranchAccount")
					.addScalar("remittancePaymentNo").addScalar("rtgsNoDate")         
					.addScalar("rtgsAmount").addScalar("remittanceDTId") .addScalar("paymentVoucherId")
					.setResultTransformer(Transformers.aliasToBean(AutoRemittanceBeanReport.class));
		}
	 autoRemittance = remitRecoveryService.populateAutoRemittanceDetailbySQL(sqlQuery);
	 
	 
	}

	 public void populateCOCLevelSummaryData()
	 {
		  StringBuffer queryString1= new StringBuffer("SELECT (SUM(DECODE( glcode,"+ FinancialConstants.INCOMETAX_CAPITAL + 
				  ",rmtAmt,DECODE(GLCODE,"+ FinancialConstants.INCOMETAX_REVENUE + " ,RMTAMT, NULL)))) AS  incomeTaxRemittedAmt," +
				  " (SUM(DECODE( glcode, " + FinancialConstants.SALESTAX_CAPITAL + " ,rmtAmt," +
				  "  DECODE(GLCODE, "+ FinancialConstants.SALESTAX_REVENUE +  " ,RMTAMT, NULL) ))) AS  salesTaxRemittedAmt," +
				  " (SUM(DECODE( glcode, "+ FinancialConstants.MWGWF_MAINTENANCE +  ",rmtAmt," +
				  "  DECODE(GLCODE, "+ FinancialConstants.MWGWF_CAPITAL +  " ,RMTAMT, NULL)))) AS  mwgwfRemittedAmt," +
				  " (SUM(DECODE(GLCODE,"+ FinancialConstants.SERVICETAX_REVENUE +  " ,RMTAMT, NULL)  ))AS serviceTaxRemittedAmt," +
				  " SUM(rmtamt) AS grandTotal FROM( SELECT * FROM (" +
				  " SELECT remdt.REMITTEDAMT AS rmtAmt,tds.TYPE  AS glcode" +
				  " FROM tds tds, eg_remittance rem, eg_remittance_detail remdt,eg_remittance_gldtl remgltl, voucherheader vh " +
				  " WHERE rem.id=remdt.REMITTANCEID" +
				  " AND remdt.REMITTANCEGLDTLID = remgltl.id" +
				  " AND tds.id=rem.TDSID" +      
				  " AND vh.status=0 " +
				  " AND tds.REMITTANCE_MODE ='A'" +
				  " AND rem.PAYMENTVHID =vh.id " +
				  " AND tds.TYPE IN (" 
				  + FinancialConstants.INCOMETAX_CAPITAL + ","
				  + FinancialConstants.INCOMETAX_REVENUE + ","
				  + FinancialConstants.SALESTAX_CAPITAL + ","
				  + FinancialConstants.SALESTAX_REVENUE +  ","
				  + FinancialConstants.MWGWF_MAINTENANCE +  ","
				  + FinancialConstants.MWGWF_CAPITAL + ","
				  + FinancialConstants.SERVICETAX_REVENUE+  ")" ) ;
		Date currentDate = new Date();
		StringBuffer finyearQuery = new StringBuffer();
		  
		finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(currentDate)).append("'");
		CFinancialYear financialyear =(CFinancialYear) persistenceService.find(finyearQuery.toString());
		
		if(null != paymentVoucherFromDate){
			  queryString1.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate)+"'" );
		}
		else{
			  queryString1.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(financialyear.getStartingDate())+"'" );
		}
		if(null != paymentVoucherToDate){ 
			queryString1.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate)+"'" );
		}
		else
		{
			queryString1.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(financialyear.getEndingDate())+"'" );
		}		
		  
		queryString1.append(" )) ");
		  Session session =HibernateUtil.getCurrentSession();
			Query sqlQuery = session.createSQLQuery(queryString1.toString())   
					.addScalar("incomeTaxRemittedAmt").addScalar("salesTaxRemittedAmt").addScalar("mwgwfRemittedAmt")
					.addScalar("serviceTaxRemittedAmt").addScalar("grandTotal")         
					.setResultTransformer(Transformers.aliasToBean(AutoRemittanceCOCLevelBeanReport.class));
			coaAbstract =sqlQuery.list();     
			map.put("coaAbstratct",coaAbstract);
		
		StringBuffer queryString2 = new StringBuffer(" SELECT departmentCode," +
				" (SUM(DECODE( glcode,"+ FinancialConstants.INCOMETAX_CAPITAL + ",rmtAmt," +
						"   DECODE(GLCODE,"+ FinancialConstants.INCOMETAX_REVENUE + " ,RMTAMT, NULL)))) AS  incomeTaxRemittedAmt," +
						" (SUM(DECODE( glcode," + FinancialConstants.SALESTAX_CAPITAL + ",rmtAmt," +
						"  DECODE(GLCODE," + FinancialConstants.SALESTAX_REVENUE + " ,RMTAMT, NULL) ))) AS  salesTaxRemittedAmt," +
						" (SUM(DECODE( glcode, "+ FinancialConstants.MWGWF_MAINTENANCE +  ",rmtAmt," +
						" DECODE(GLCODE, "+ FinancialConstants.MWGWF_CAPITAL +  " ,RMTAMT, NULL)))) AS  mwgwfRemittedAmt," +
						" (SUM(DECODE(GLCODE,"+ FinancialConstants.SERVICETAX_REVENUE +  " ,RMTAMT, NULL)  ))AS serviceTaxRemittedAmt, " +
						" SUM(rmtamt) AS departmentTotal FROM(" +
						"  SELECT * FROM (" +
						" SELECT dept.DEPT_code  departmentcode, remdt.REMITTEDAMT AS rmtAmt, tds.TYPE  AS glcode" +
						" FROM tds tds, eg_remittance rem, eg_remittance_detail remdt,eg_remittance_gldtl remgltl, voucherheader vh," +
						" eg_department dept" +
						" WHERE rem.id=remdt.REMITTANCEID" +
						" AND remdt.REMITTANCEGLDTLID = remgltl.id" +
						" AND tds.id=rem.TDSID" +
						" AND dept.ID_DEPT = vh.DEPARTMENTID" +
						" AND tds.REMITTANCE_MODE ='A'" +
						" AND vh.status=0" +
						"  AND rem.PAYMENTVHID =vh.id" +  
						" AND tds.TYPE IN (" 
						+ FinancialConstants.INCOMETAX_CAPITAL + ","
						  + FinancialConstants.INCOMETAX_REVENUE + ","
						  + FinancialConstants.SALESTAX_CAPITAL + ","
						  + FinancialConstants.SALESTAX_REVENUE +  ","
						  + FinancialConstants.MWGWF_MAINTENANCE +  ","
						  + FinancialConstants.MWGWF_CAPITAL + ","
						  + FinancialConstants.SERVICETAX_REVENUE+ 
						"  )" );
						  
				if(null != paymentVoucherFromDate){
					  queryString2.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherFromDate)+"'" );
				}
				else{
					  queryString2.append(" AND vh.voucherdate >= '"+Constants.DDMMYYYYFORMAT1.format(financialyear.getStartingDate())+"'" );
				}
				if(null != paymentVoucherToDate){ 
					queryString2.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(paymentVoucherToDate)+"'" );
				}
				else
				{
					queryString2.append(" AND vh.voucherdate <= '"+Constants.DDMMYYYYFORMAT1.format(financialyear.getEndingDate())+"'" );
				}		
				queryString2.append(" ))GROUP BY departmentcode  ORDER BY departmentcode ");
				
				
					Query sqlQuery2 = session.createSQLQuery(queryString2.toString())   
							.addScalar("departmentCode")
							.addScalar("incomeTaxRemittedAmt").addScalar("salesTaxRemittedAmt").addScalar("mwgwfRemittedAmt")
							.addScalar("serviceTaxRemittedAmt").addScalar("departmentTotal")         
							.setResultTransformer(Transformers.aliasToBean(AutoRemittanceCOCLevelBeanReport.class));
					remittanceList =sqlQuery2.list();
					map.put("summarryList", remittanceList);
					
					 
	 }      
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}


	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}

	public Recovery getRecovery() {
		return recovery;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Fund getFund() {
		return fund;
	}
	
	public List<EntityType> getEntitiesList() {
		return entitiesList;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Department getDepartment() {
		return department;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Bankbranch getBankbranch() {
		return bankbranch;
	}

	public void setBankbranch(Bankbranch bankbranch) {
		this.bankbranch = bankbranch;
	}

	public Bankaccount getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}
	public Date getPaymentVoucherFromDate() {
		return paymentVoucherFromDate;
	}

	public void setPaymentVoucherFromDate(Date paymentVoucherFromDate) {
		this.paymentVoucherFromDate = paymentVoucherFromDate;
	}

	public Date getPaymentVoucherToDate() {
		return paymentVoucherToDate;
	}

	public void setPaymentVoucherToDate(Date paymentVoucherToDate) {
		this.paymentVoucherToDate = paymentVoucherToDate;
	}

	public Date getRtgsAssignedFromDate() {
		return rtgsAssignedFromDate;
	}

	public void setRtgsAssignedFromDate(Date rtgsAssignedFromDate) {
		this.rtgsAssignedFromDate = rtgsAssignedFromDate;
	}

	public Date getRtgsAssignedToDate() {
		return rtgsAssignedToDate;
	}

	public void setRtgsAssignedToDate(Date rtgsAssignedToDate) {
		this.rtgsAssignedToDate = rtgsAssignedToDate;
	}

	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	public List<AutoRemittanceBeanReport> getAutoRemittance() {
		return autoRemittance;
	}

	public void setAutoRemittance(List<AutoRemittanceBeanReport> autoRemittance) {
		this.autoRemittance = autoRemittance;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public DrawingOfficer getDrawingOfficer() {
		return drawingOfficer;
	}

	public void setDrawingOfficer(DrawingOfficer drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getContractorCode() {
		return contractorCode;
	}

	public void setContractorCode(String contractorCode) {
		this.contractorCode = contractorCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Map<AutoRemittanceBeanReport, List<AutoRemittanceBeanReport>> getAutoremittanceMap() {
		return autoremittanceMap;
	}

	public void setAutoremittanceMap(
			Map<AutoRemittanceBeanReport, List<AutoRemittanceBeanReport>> autoremittanceMap) {
		this.autoremittanceMap = autoremittanceMap;
	}

	public BigDecimal getRemittedAmountTotal() {
		return remittedAmountTotal;
	}

	public void setRemittedAmountTotal(BigDecimal remittedAmountTotal) {
		this.remittedAmountTotal = remittedAmountTotal;
	}

	public List<AutoRemittanceCOCLevelBeanReport> getCoaAbstract() {
		return coaAbstract;
	}

	public void setCoaAbstract(List<AutoRemittanceCOCLevelBeanReport> coaAbstract) {
		this.coaAbstract = coaAbstract;
	}

	public List<AutoRemittanceCOCLevelBeanReport> getRemittanceList() {
		return remittanceList;
	}

	public void setRemittanceList(
			List<AutoRemittanceCOCLevelBeanReport> remittanceList) {
		this.remittanceList = remittanceList;
	}
    

}
