package org.egov.payroll.client.providentfund;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.providentfund.PFDetails;
import org.egov.payroll.model.providentfund.PFHeader;
import org.egov.payroll.services.providentfund.PFDelegate;
import org.egov.payroll.services.providentfund.PFService;
import org.egov.payroll.services.providentfund.PFQuartzJob;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * @purpose To create and load the PF information
 * @author  Ilayaraja.p
 * @created june 04, 2008
 */
public class PFAction extends DispatchAction 
{
	private static final Logger LOGGER = Logger.getLogger(PFAction.class);
	
	PFDelegate pfDelegate = new PFDelegate();
	private PersistenceService persistenceService;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private static final String ERRORSTR ="error";
	private PFService pfService = null;
	private final PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill.getPayrollExterInterface();
	private Scheduler scheduler;
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/**
	 * To insert the PF information in  create option
	 * @param mapping
	 * @param form
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward createNew(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target="",alertMessage="";
		try
		{
			LOGGER.info("create method ");
			pfService= PayrollManagersUtill.getPfService();
			PFSetupForm setupForm = (PFSetupForm) form;
			PFHeader header = new PFHeader();

			CChartOfAccounts pfChartOfAccount = payrollExternalInterface.getCChartOfAccountsById(Long.valueOf(setupForm.getPfAccountId()));
			header.setPfAccount(pfChartOfAccount);
			if(!setupForm.getPfIntExpAccountId().equals("")){
				CChartOfAccounts expChartOfAccount = payrollExternalInterface.getCChartOfAccountsById(Long.valueOf(setupForm.getPfIntExpAccountId()));
				header.setPfIntExpAccount(expChartOfAccount);
			}
			header.setPfType("GPF");
			header.setFrequency(setupForm.getFrequency());

			pfService.createPFHeader(header);
			
			alertMessage = "Executed Successfully";
			target ="loadData";
			
			if(header.getPfIntExpAccount()!=null)
			{
				//PFHibernateDAO hibernateDetailsDAO = PFDAOFactory.getDAOFactory().getPFDetailsDAO();
				
				PFDetails details = null;
				for(int i=0;i<setupForm.getDateFrom().length;i++)
				{
					details = new PFDetails();
					details.setFromDate(sdf.parse(setupForm.getDateFrom()[i]));
					details.setToDate(null);
					if(setupForm.getDateTo()[i]!=null && !setupForm.getDateTo()[i].equals("")){
						details.setToDate(sdf.parse(setupForm.getDateTo()[i]));
					}
					
					details.setPfHeaderId(Integer.parseInt(header.getId().toString()));
					details.setAnnualRateOfInterest(new BigDecimal(setupForm.getAnnualRateOfInterest()[i]));
					pfService.createPFDetails(details);
				}  
	
				String expression="";
				
				if("Monthly".equalsIgnoreCase(setupForm.getFrequency())){
					expression="0 0 22 L * ?";
				}
				else
				{
					Date enddate = pfDelegate.getFinancialYearEndDate(new Date());
					if("Annual".equalsIgnoreCase(setupForm.getFrequency()) && enddate!=null)
					{
						Calendar cal = GregorianCalendar.getInstance();
				 		cal.setTime(enddate);
				 		int mon = cal.get(Calendar.MONTH);
						int tempMon = mon+1;
						expression="0 0 22 "+cal.get(Calendar.DATE)+" "+tempMon+" ?";	
					}
				}

				String returnValue = createSchedluer(req,expression);
				
				if(!"".equals(returnValue))
				{
					alertMessage = returnValue;
				}
			}
			
			req.setAttribute("alertMessage", alertMessage);
			
		}catch(EGOVRuntimeException ex)
	    {
	        LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
	        target = ERRORSTR;
	        HibernateUtil.rollbackTransaction();
	    }
		catch(Exception e)
		{
			target = ERRORSTR;
			LOGGER.error("Exception="+e.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		
		return mapping.findForward(target);
	}
	
	/**
	 * To update the PF information in modify option
	 * @param mapping
	 * @param form
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward modify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target="",alertMessage="";
		try
		{
			LOGGER.info("modify method ");
			pfService = PayrollManagersUtill.getPfService();
			PFSetupForm setupForm = (PFSetupForm) form;
			PFHeader header = new PFHeader();
			
			if(setupForm.getId()!=null && !setupForm.getId().equals("")){
				header.setId(Long.valueOf(setupForm.getId()));
			}
			//PFHibernateDAO hibernateDAO = PFDAOFactory.getDAOFactory().getPFHeaderDAO();
			CChartOfAccounts pfChartOfAccount = payrollExternalInterface.getCChartOfAccountsById(Long.valueOf(setupForm.getPfAccountId()));
			if(!setupForm.getPfIntExpAccountId().equals("")){
				CChartOfAccounts expChartOfAccount = payrollExternalInterface.getCChartOfAccountsById(Long.valueOf(setupForm.getPfIntExpAccountId()));
				header.setPfIntExpAccount(expChartOfAccount);
			}
			header.setPfAccount(pfChartOfAccount);
			header.setPfType("GPF");
			header.setFrequency(setupForm.getFrequency());
			pfService.updatePFHeader(header);
			
			alertMessage = "Executed Successfully";
			target ="loadData";
			
			//PFHibernateDAO hibernateDetailsDAO = PFDAOFactory.getDAOFactory().getPFDetailsDAO();
			if(header.getPfIntExpAccount()==null)
			{
				List<PFDetails> detailList = pfService.getPFDetails();
				if(!detailList.isEmpty()){
					pfService.deleteAllPFDetails(Integer.parseInt(header.getId().toString()));
				}
			}
			else
			{
				PFDetails details = null;
				// delete block
				String[] deletedRows= StringUtils.split(setupForm.getDeletedRowsId(),",");
				for(int j=0;j<deletedRows.length;j++)
				{
					details = new PFDetails();
					details.setDetailId(Integer.parseInt(deletedRows[j]));
					details.setPfHeaderId(Integer.parseInt(header.getId().toString()));
					pfService.deletePFDetails(details);	
				}
	
				// update block
				for(int i=0;i<setupForm.getDateFrom().length;i++)
				{
					details = new PFDetails();
					details.setFromDate(sdf.parse(setupForm.getDateFrom()[i]));
					details.setToDate(null);
					if(setupForm.getDateTo()[i]!=null && !setupForm.getDateTo()[i].equals("")){
						details.setToDate(sdf.parse(setupForm.getDateTo()[i]));
					}
					
					details.setPfHeaderId(Integer.parseInt(header.getId().toString()));
					details.setDetailId(null);
					if(setupForm.getDetailId()[i]!=null && !setupForm.getDetailId()[i].equals("")){
						details.setDetailId(Integer.parseInt(setupForm.getDetailId()[i]));
					}
					
					details.setAnnualRateOfInterest(new BigDecimal(setupForm.getAnnualRateOfInterest()[i]));
					pfService.updatePFDetails(details);
				}
				
				String expression="";
				if("Monthly".equalsIgnoreCase(setupForm.getFrequency())){
					expression="0 0 22 L * ?";
				}
				else if("Annual".equalsIgnoreCase(setupForm.getFrequency()))
				{
					Date enddate = pfDelegate.getFinancialYearEndDate(new Date());
					Calendar cal = GregorianCalendar.getInstance();
					cal.setTime(enddate);
					if(enddate!=null)
					{
						int mon = cal.get(Calendar.MONTH);
						int tempMon = mon+1;
						expression="0 0 22 "+cal.get(Calendar.DATE)+" "+tempMon+" ?";	
					}
				}
			
				String returnValue = createSchedluer(req,expression);
					
				if(!"".equals(returnValue))
				{
					alertMessage = returnValue;
				}
			}

			req.setAttribute("alertMessage", alertMessage);

		}catch(EGOVRuntimeException ex)
        {
            LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = ERRORSTR;
            HibernateUtil.rollbackTransaction();
        }
		catch(Exception e)
		{
			target = ERRORSTR;
			LOGGER.error("Exception="+e.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}
	/**
	 * To load the PF information
	 * @param mapping
	 * @param form
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward loadData(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	 throws IOException,ServletException
	{
		String target="";
		try
		{
			pfService = PayrollManagersUtill.getPfService();
			PFSetupForm setupForm =new PFSetupForm();
			
			List headerList = pfService.getPFHeaderInfo();

			if(!headerList.isEmpty())
			{
				PFHeader header = (PFHeader) headerList.get(0);
							
				//Setup data for pfdetails
				List detailList = pfService.getPFDetails();
				if(!detailList.isEmpty()){
					setupForm = new PFSetupForm(detailList);
				}
				
				//setup data for pfheader
				setupForm.setId(String.valueOf(header.getId()));
				setupForm.setPfAccountId(""+header.getPfAccount().getId());
				if(header.getPfIntExpAccount()==null){
					setupForm.setPfIntExpAccountId("");
				}
				else{
					setupForm.setPfIntExpAccountId(""+header.getPfIntExpAccount().getId());
				}

				setupForm.setFrequency(header.getFrequency());
				String pfAccount[] = pfDelegate.getPFAccountCodes("pfaccount");
				setupForm.setPfAccount(pfAccount[0]);
				setupForm.setPfAccountName(pfAccount[1]);
				String pfIntExpAccount[] = pfDelegate.getPFAccountCodes("pfintexpaccount");
				if(pfIntExpAccount!=null && pfIntExpAccount[0]!= null && !pfIntExpAccount[0].equals("") )
				{
					setupForm.setPfIntExpAccount(pfIntExpAccount[0]);
					setupForm.setPfIntExpAccountName(pfIntExpAccount[1]);
				}
			}
			
			req.setAttribute("PFSetupForm", setupForm);
			target ="loadData";
			
		}catch(EGOVRuntimeException ex)
        {
            LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = ERRORSTR;
            HibernateUtil.rollbackTransaction();
        }
		catch(Exception e)
		{
			target = ERRORSTR;
			LOGGER.error("Exception="+e.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}
	
	/**
	 * TO create the schedluer
	 * @param req
	 * @param expression
	 */
	public String createSchedluer(HttpServletRequest req,String expression) throws IOException,ServletException
	{
		try
		{
			String cronExpr = expression;
			AppConfigValues appConfigValues = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("PFModule","RunOnSave",new Date());
			List<String> mandatoryFields = new ArrayList<String>();
		    
			if(appConfigValues==null )
			{
		    	return "No Value found for key(RunOnSave) in AppConfigValues";
			}
		    else
		    {
		    	String devmode =appConfigValues.getValue();
		    	if(devmode.equalsIgnoreCase("Y")){
			    	GregorianCalendar currdate=new GregorianCalendar();
			    	currdate.add(Calendar.SECOND,60);
			    	int mon = currdate.get(Calendar.MONTH);
					int tempMon = mon+1;
			    	//cronExpr="0 "+currdate.get(Calendar.MINUTE)+" "+currdate.get(Calendar.HOUR_OF_DAY)+" * * ?";
			    	cronExpr=currdate.get(Calendar.SECOND)+" "+currdate.get(Calendar.MINUTE)+" "+currdate.get(Calendar.HOUR_OF_DAY)+" "+currdate.get(Calendar.DAY_OF_MONTH)+" "+tempMon+" ? "+currdate.get(Calendar.YEAR);
				}
		    }
			
		    List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'PAYROLLFINTRNATTRIBUTES'");
			for (AppConfig appConfig : appConfigList) {
				for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
					String value = appConfigVal.getValue();
					String header=value.substring(0, value.indexOf('|'));
					String mandate = value.substring(value.indexOf('|')+1);
					if(mandate.equalsIgnoreCase("M")){
						mandatoryFields.add(header);
					}
				}
			}
			if(!mandatoryFields.contains("fund")){
				return "No Attribute(fund) found for key(PAYROLLFINTRNATTRIBUTES) in AppConfigValues,Fund is mandatory";
			}

		    LOGGER.info("mandatoryFields list ="+ mandatoryFields);
			LOGGER.info("Cron expressiion for PF module=="+cronExpr);
		    
		    
		    LOGGER.info("Cron expressiion for PF module=="+cronExpr);
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.getDefault());
			String userId =EGOVThreadLocals.getUserId();
			//SchedulerFactory sf =new StdSchedulerFactory();
			Scheduler scheduler = getScheduler();
			String cityURL = EGOVThreadLocals.getDomainName();
			String hibFactName = EGOVThreadLocals.getHibFactName();
			String jndi = EGOVThreadLocals.getJndiName();
			LOGGER.info("==========================hibFactName="+hibFactName+",jndi="+jndi+",cityURL="+cityURL);
			//TODO - resolve compilation error for scheduler.getJobNames("PF Job-"+cityURL);
			/*String[] runningJobNames= scheduler.getJobNames("PF Job-"+cityURL);
			//String[] runningJobNames= scheduler.get
			if(runningJobNames!=null){
				for(int i=0;i<runningJobNames.length;i++)
				{
					scheduler.ddeleteJobs(""+runningJobNames[i],"PF Job-"+cityURL);
					LOGGER.info("[ "+runningJobNames[i]+ " ] removed from the quartz schedluer");
				}
			}*/
			JobDetail jobDetail = new JobDetailImpl(cityURL+"->PFInterestCalJob"+sdf1.format(new Date()),"PF Group Job-"+cityURL,PFQuartzJob.class);
			CronTrigger cronTrigger = new CronTriggerImpl(cityURL+"->PFInterestCalTrigger"+sdf1.format(new Date()),"PF Group Trigger-"+cityURL,cronExpr);
			cronTrigger.getMisfireInstruction();
	
			//jobDetail.getJobDataMap().put("pfDelegate", pfDelegate);
			jobDetail.getJobDataMap().put("cityURL", cityURL);
			jobDetail.getJobDataMap().put("hibFactName", hibFactName);
			jobDetail.getJobDataMap().put("jndi", jndi);
			jobDetail.getJobDataMap().put("userId", Integer.parseInt(userId));
			jobDetail.getJobDataMap().put("mandatoryFields", (List<String>)mandatoryFields);
			jobDetail.requestsRecovery();
			
			Map<String,Object> schedulerContextAsMap = new HashMap<String,Object>();
			schedulerContextAsMap.put("pfDelegate", pfDelegate);
			scheduler.getContext().putAll(schedulerContextAsMap);
			
			scheduler.scheduleJob(jobDetail,cronTrigger);
			LOGGER.info("===========PFInterestCalJob"+cityURL+" job is scheduled at "+cronTrigger.getNextFireTime());
			//scheduler.start();	
		}
		catch(SchedulerException e)
		{
			LOGGER.error("SchedulerException in createSchedluer method="+e.getMessage());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in createSchedluer method="+e.getMessage());
		}
		return "";
	}

	public void setPfDelegate(PFDelegate pfDelegate) {
		this.pfDelegate = pfDelegate;
	}
}

