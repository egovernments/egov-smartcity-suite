package org.egov.payroll.services.payslip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.payroll.dao.EmpPayrollDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.BatchFailureDetails;
import org.egov.payroll.model.BatchGenDetails;
import org.egov.payroll.reports.MonthlyPayslipReports;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.pims.commons.Position;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//@author Mamatha
public class AutoGenerationPaySlipJob implements StatefulJob{
	
	private static final Logger logger = Logger.getLogger(AutoGenerationPaySlipJob.class);
	static String cityname="";
		
	public AutoGenerationPaySlipJob() throws Exception {
		
	}
	protected void beginTransaction() {
		HibernateUtil.beginTransaction();
	}
	protected void commitTransaction() {
		HibernateUtil.commitTransaction();
	}

	public void generateJob(String cityURL, String jndi, String hibFactName,String batchId,String userName,JobExecutionContext jobContx,Map paramMap) throws JobExecutionException {
		BatchGenDetails batchgenobj = null;
		PayRollService payRollService =  null;
		UserService userService =  null;
		User user = null;
		try {
			int month,finyr,deptidtemp; 
			SetDomainJndiHibFactNames.setThreadLocals(cityURL,jndi,hibFactName);	
			String userId = (String) jobContx.getJobDetail().getJobDataMap().get("userId");
			Position approverPos = (Position) jobContx.getJobDetail().getJobDataMap().get("approverPosition");
			EGOVThreadLocals.setUserId(userId);
			beginTransaction();	
			String configLocation[] = { "classpath*:org/egov/infstr/beanfactory/globalApplicationContext.xml",
                    "classpath*:org/egov/infstr/beanfactory/egiApplicationContext.xml",
                    "classpath*:org/egov/infstr/beanfactory/applicationContext-pims.xml",
                    "classpath*:org/egov/infstr/beanfactory/applicationContext-egf.xml",
                    "/WEB-INF/applicationContext.xml","classpath*:org/serviceconfig-Bean.xml","/WEB-INF/payroll_action_beans.xml"};
			ApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);	
			logger.debug("getting ApplicationContext ctx ="+ctx);
			payRollService = (PayRollService)ctx.getBean("payRollService");
			logger.debug("payRollService="+payRollService);
			userService = (UserService)ctx.getBean("userManager");
			logger.debug("userService="+userService);	
			user = userService.getUserByUserName(userName);
			batchgenobj = payRollService.getBatchGenDetailsById(Long.valueOf(batchId));			
			if(batchgenobj!=null && batchgenobj.getStatus().intValue() == PayrollConstants.BATCH_GEN_STATUS_START.intValue())
			{
				batchgenobj.setStatus(PayrollConstants.BATCH_GEN_STATUS_TRIGGERED);
				payRollService.updateBatchGenDetals(batchgenobj);
				
				logger.debug("Batch Generation Started-----");
				GregorianCalendar fromdate1 = new GregorianCalendar();
				fromdate1.setTime(batchgenobj.getFromDate());				
				GregorianCalendar todate1 = new GregorianCalendar();
				todate1.setTime(batchgenobj.getToDate());
				Integer deptid=null;
				if(batchgenobj.getDepartment()!=null)
				{
					deptid=batchgenobj.getDepartment().getId();
				}
				Integer functionaryId=null;
				if(batchgenobj.getFunctionary()!=null)
				{
					functionaryId=batchgenobj.getFunctionary().getId();
				}
				Long functionId = null;
				if(batchgenobj.getFunction() != null){
					functionId = batchgenobj.getFunction().getId();
				}
				Integer billNumberId = null;
				if(batchgenobj.getBillNumber() != null){
					billNumberId = batchgenobj.getBillNumber().getId();
				}
				//independent of the payslips are generated r not, the history has to update,bz batchgen is raised 
				CFinancialYear fy = payRollService.getFinancialYearByDate(todate1.getTime());				
				int year= fy.getId().intValue();
				//TODO: Avoid this call - return a map/ or Wrapper object
				List<Integer> normalEmpIdList = payRollService.getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate1,todate1,deptid,functionaryId,functionId,billNumberId);
				List<Integer> exceptionEmpIdList = payRollService.getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(fromdate1,todate1,deptid,functionaryId,functionId,billNumberId);
				if(null != normalEmpIdList && normalEmpIdList.size() > 0)
				{
					payRollService.updateIsHistory(todate1.get(Calendar.MONTH)+1,year,normalEmpIdList);					
				}
				if(null != exceptionEmpIdList && exceptionEmpIdList.size() > 0)
				{
					payRollService.updateIsHistory(todate1.get(Calendar.MONTH)+1,year,exceptionEmpIdList);						
				}											
				HibernateUtil.getCurrentSession().flush();
				commitTransaction();
					
				beginTransaction();			
				
				IPayslipProcess payslipProcess = (IPayslipProcess) ctx.getBean("payslipProcessImpl");
				MonthlyPayslipReports mon = (MonthlyPayslipReports)ctx.getBean("monthlyPayslipReport");
									
				SchedulerContext skedCtx = jobContx.getScheduler().getContext();				

				List<BatchFailureDetails> listBatchFailure = new ArrayList();
				if(payslipProcess!=null)
					listBatchFailure = payslipProcess.generateBatchPayslips(fromdate1, todate1, deptid, userName, functionaryId, true, approverPos,functionId,billNumberId);
				//p.generateBatchPayslips() will commit the transactions
				
				int succCount = 0;
				if(listBatchFailure != null)
					succCount = normalEmpIdList.size()+exceptionEmpIdList.size()-listBatchFailure.size();		
				logger.debug("succCount="+succCount);
				if(succCount > 0)
				{
					batchgenobj.setStatus(PayrollConstants.BATCH_GEN_STATUS_CLOSE);
				}
				else
				{
					batchgenobj.setStatus(PayrollConstants.BATCH_GEN_STATUS_FAILED);
				}
				batchgenobj.setSuccCount(succCount);
				if(listBatchFailure != null)
					batchgenobj.setFailCount(listBatchFailure.size());
				else
					batchgenobj.setFailCount(new Integer(0));				
				batchgenobj.setModifiedBy(user);
				batchgenobj.setModifiedDate(new Date());
				payRollService.updateBatchGenDetals(batchgenobj);
				
				
				month = batchgenobj.getMonth().intValue();
				finyr = batchgenobj.getFinancialyear().getId().intValue();
				String billNum="";
				if(batchgenobj.getDepartment()==null)
				{
					deptidtemp = 0;				
				}else
				{
					deptidtemp = batchgenobj.getDepartment().getId();				
				}
				commitTransaction();
				/*
				beginTransaction();			
				mon.generateMonthlyPayslipsPDF(month,finyr,deptidtemp,billNum,cityname,paramMap,"pdf");			
				commitTransaction();
				*/
				HibernateUtil.getCurrentSession().flush();
				logger.debug("Scheduler completed......");
			}
		}catch (Exception e) {			
			logger.error(e.getMessage());
			HibernateUtil.rollbackTransaction();
			throw new JobExecutionException(e.getMessage());
		}
		finally
		{			
			HibernateUtil.closeSession();
		}
	}

	
	@Override
	public void execute(JobExecutionContext jobContext)throws JobExecutionException {
	try{	
			//Object[] jdArgs = (Object[]) jobContext.getJobDetail().getJobDataMap().get("args");
			String cityURL = (String) jobContext.getJobDetail().getJobDataMap().get("cityURL");
			String jndi = (String) jobContext.getJobDetail().getJobDataMap().get("JNDI");
			String hibFactName = (String) jobContext.getJobDetail().getJobDataMap().get("HIBNAME");
			String batchId = (String) jobContext.getJobDetail().getJobDataMap().get("BATCHID");
			String userName = (String) jobContext.getJobDetail().getJobDataMap().get("USERNAME");
			
			//for pdf values
			HashMap paramMap=new HashMap();
			paramMap.put("Earnings.jasper", (String) jobContext.getJobDetail().getJobDataMap().get("Earnings.jasper"));
			paramMap.put("Deductions.jasper", (String) jobContext.getJobDetail().getJobDataMap().get("Deductions.jasper"));
			paramMap.put("Loans.jasper", (String) jobContext.getJobDetail().getJobDataMap().get("Loans.jasper"));
			paramMap.put("VMC_Payslips.jasper", (String) jobContext.getJobDetail().getJobDataMap().get("VMC_Payslips.jasper"));
			
			cityname=(String) jobContext.getJobDetail().getJobDataMap().get("cityname");
			if(!(jobContext.getJobDetail().getJobDataMap().get("employee")==null))
			{
				paramMap.put("employee", (String) jobContext.getJobDetail().getJobDataMap().get("employee"));			
				paramMap.put("designation", (String) jobContext.getJobDetail().getJobDataMap().get("designation"));
				paramMap.put("bankACNo",(String) jobContext.getJobDetail().getJobDataMap().get("bankACNo"));
				paramMap.put("center",(String) jobContext.getJobDetail().getJobDataMap().get("center"));
				paramMap.put("earningsOrAllowances",(String) jobContext.getJobDetail().getJobDataMap().get("earningsOrAllowances"));
				paramMap.put("deductions",(String) jobContext.getJobDetail().getJobDataMap().get("deductions"));
				paramMap.put("totalEarnings",(String) jobContext.getJobDetail().getJobDataMap().get("totalEarnings"));
				paramMap.put("totalDeductions",(String) jobContext.getJobDetail().getJobDataMap().get("totalDeductions"));
				paramMap.put("netpay",(String) jobContext.getJobDetail().getJobDataMap().get("netpay"));
				paramMap.put("computerGenPayslip",(String) jobContext.getJobDetail().getJobDataMap().get("computerGenPayslip"));
				paramMap.put("department",(String) jobContext.getJobDetail().getJobDataMap().get("department"));
				paramMap.put("cashOrCheque",(String) jobContext.getJobDetail().getJobDataMap().get("cashOrCheque"));
			}
			else if(jobContext.getJobDetail().getJobDataMap().get("employee-tl")!=null)
			{
				paramMap.put("employee-tl", (String) jobContext.getJobDetail().getJobDataMap().get("employee-tl"));			
				paramMap.put("designation-tl", (String) jobContext.getJobDetail().getJobDataMap().get("designation-tl"));
				paramMap.put("bankACNo-tl",(String) jobContext.getJobDetail().getJobDataMap().get("bankACNo-tl"));
				paramMap.put("center-tl",(String) jobContext.getJobDetail().getJobDataMap().get("center-tl"));
				paramMap.put("earningsOrAllowances-tl",(String) jobContext.getJobDetail().getJobDataMap().get("earningsOrAllowances-tl"));
				paramMap.put("deductions-tl",(String) jobContext.getJobDetail().getJobDataMap().get("deductions-tl"));
				paramMap.put("totalEarnings-tl",(String) jobContext.getJobDetail().getJobDataMap().get("totalEarnings-tl"));
				paramMap.put("totalDeductions-tl",(String) jobContext.getJobDetail().getJobDataMap().get("totalDeductions-tl"));
				paramMap.put("netpay-tl",(String) jobContext.getJobDetail().getJobDataMap().get("netpay-tl"));
				paramMap.put("computerGenPayslip-tl",(String) jobContext.getJobDetail().getJobDataMap().get("computerGenPayslip-tl"));
				paramMap.put("department-tl",(String) jobContext.getJobDetail().getJobDataMap().get("department-tl"));
				paramMap.put("cashOrCheque-tl",(String) jobContext.getJobDetail().getJobDataMap().get("cashOrCheque-tl"));
			}
			generateJob(cityURL,jndi,hibFactName,batchId,userName,jobContext,paramMap);
		}catch(SchedulerException e)
		{
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		
	}
	  
	  private List getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId){
		  	List empList=new ArrayList();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empList = (ArrayList) empPayrollDAO.getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate,todate,deptid,functionaryId);
			return (empList);
		  
	  }
	  public List getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId)
	  {
		    List empList=new ArrayList();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empList = (ArrayList) empPayrollDAO.getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate,todate,deptid,functionaryId);
			return (empList);
	  }




	
}

