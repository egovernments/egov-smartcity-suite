package org.egov.payroll.web.actions.providentfund;

import java.io.IOException;
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

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.model.providentfund.PFHeader;
import org.egov.payroll.model.providentfund.PFTriggerDetail;
import org.egov.payroll.services.providentfund.CPFDelegate;
import org.egov.payroll.services.providentfund.CPFQuartzJob;
import org.egov.web.actions.BaseFormAction;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Result(name=Action.SUCCESS, type="redirect", location = "cpfTrigger.action")
@ParentPackage("egov")
@Validation()
public class CpfTriggerAction extends BaseFormAction{
	
	private static final long serialVersionUID = 1L;
	private PFHeader pfHeader = new PFHeader();
	private PersistenceService<PFTriggerDetail, Long> pfTriggerDtlService;
	private String target="";
	public static final  String NEW="new";
	private static final Logger LOGGER = Logger.getLogger(CpfTriggerAction.class);
	private Scheduler scheduler;
	
	private CPFDelegate cpfDelegate;

	@Override
	public Object getModel() {
		return pfHeader;
	}
	
	public CpfTriggerAction() {
		addRelatedEntity("tds", Recovery.class);
		addRelatedEntity("pfIntExpAccount",org.egov.commons.CChartOfAccounts.class);
		addRelatedEntity("ruleScript",org.egov.infstr.workflow.Action.class);
		addRelatedEntity("financialYear", CFinancialYear.class);
	}
	
	public String execute()
	{
		return NEW;
	}
	
	public void prepare()
	{
		super.prepare(); 
		addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 and isActiveForPosting=1 order by finYearRange desc "));
		addDropdownData("ruleScriptList", getPersistenceService().findAllByNamedQuery("BY_TYPE", "CPFHeader"));
		addDropdownData("tdsList", getPersistenceService().findAllBy("from Recovery tds where upper(tds.egPartytype.code) ='EMPLOYEE' "));
	}
	
	@SkipValidation
	public String loadCPFTrigger()
	{
		pfHeader = (PFHeader) getPersistenceService().find(" from PFHeader where pfType=?", "CPF");
		if(pfHeader==null){
			pfHeader = new PFHeader();
			addActionMessage(getMessage("CPF.NotSetup.Msg"));
		}
		else
		{
			pfHeader.setFinancialYear(null);
			pfHeader.setMonth(null);
		}
		
		return NEW;
	}
	
	public String create()  //This to trigger the batch process of CPF
	{
		String expression="";
		
		try{
			expression="0 0 22 L * ?";
			createCPFSchedluer(expression);
		}catch (Exception e) {
			addActionError("Exception ="+e.getMessage());
			return NEW;
		}
		
		if(getActionErrors().isEmpty() && getFieldErrors().isEmpty())
		{
			addActionMessage(getMessage("CPFTrigger.Success.Msg"));
			target=Action.SUCCESS;
		}
		
		return NEW;
	}

	protected String getMessage(final String key) {
		return getText(key);
	}

	/**
	 * TO create the schedluer
	 * @param req
	 * @param expression
	 */
	public String createCPFSchedluer(String expression) throws IOException,ServletException
	{
		try
		{
			String cronExpr = expression;
			List<String> mandatoryFields = new ArrayList<String>();
			AppConfigValues appConfigValues = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("CPFModule","RunOnSave",new Date());

		    if(appConfigValues==null )
			{
		    	addActionError(getMessage("CPFTrigger.AppConfNotFound.Msg"));
		   		return "";
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
			    	LOGGER.warn("CPF Cron expression ==="+cronExpr);
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
		    	addActionError(getMessage("CPFTrigger.FundWiseAppConfNotFound.Msg"));
			}

		    LOGGER.info("mandatoryFields list ="+ mandatoryFields);
			LOGGER.info("Cron expressiion for CPF module=="+expression);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.getDefault());
			String userId =EGOVThreadLocals.getUserId();
			//SchedulerFactory sf =new StdSchedulerFactory();
			Scheduler scheduler = getScheduler();
			String cityURL = EGOVThreadLocals.getDomainName();
			String hibFactName = EGOVThreadLocals.getHibFactName();
			String jndi = EGOVThreadLocals.getJndiName();
			LOGGER.info("==========================hibFactName="+hibFactName+",jndi="+jndi+",cityURL="+cityURL);
			//TODO - resolve compilation error for scheduler.getJobNames("PF Job-"+cityURL);
			/*String[] runningJobNames = scheduler.getJobNames("CPF Group Job-"
					+ cityURL);
			if (runningJobNames != null) {
				for (int i = 0; i < runningJobNames.length; i++) {
					scheduler.deleteJob("" + runningJobNames[i],
							"CPF Group Job-" + cityURL);
					LOGGER.info("[ " + runningJobNames[i]
							+ " ] removed from the quartz schedluer");
				}
			}*/
			JobDetail jobDetail = new JobDetailImpl(cityURL+"->CPFCalJob"+sdf1.format(new Date()),"CPF Group Job-"+cityURL,CPFQuartzJob.class);
			CronTrigger cronTrigger = new CronTriggerImpl(cityURL+"->CPFCalTrigger"+sdf1.format(new Date()),"CPF Group Trigger-"+cityURL,cronExpr);
			cronTrigger.getMisfireInstruction();
				
			//jobDetail.getJobDataMap().put("delegate", cpfDelegate);
			jobDetail.getJobDataMap().put("cityURL", cityURL);
			jobDetail.getJobDataMap().put("hibFactName", hibFactName);
			jobDetail.getJobDataMap().put("jndi", jndi);
			jobDetail.getJobDataMap().put("userId", Integer.parseInt(userId));
			jobDetail.getJobDataMap().put("cpfHeaderId", pfHeader.getId().toString());
			jobDetail.getJobDataMap().put("cpfMonth", pfHeader.getMonth().toString());
			jobDetail.getJobDataMap().put("cpfFinancialYearId", pfHeader.getFinancialYear().getId().toString());
			jobDetail.getJobDataMap().put("mandatoryFields", (List<String>)mandatoryFields);
			
			Map<String,Object> schedulerContextAsMap = new HashMap<String,Object>();
			schedulerContextAsMap.put("cpfDelegate", cpfDelegate);
			//schedulerContextAsMap.put("cpfService", cpfService);
			scheduler.getContext().putAll(schedulerContextAsMap);
			
			//jobDetail.getJobDataMap().put("month", pfHeader.getMonth());
			jobDetail.requestsRecovery();
			scheduler.scheduleJob(jobDetail,cronTrigger);
			LOGGER.info("===========CPFCalJob"+cityURL+" job is scheduled at "+cronTrigger.getNextFireTime());
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
	
	public void validate()
	{
		if(pfHeader.getMonth()==null){
			addFieldError("month", getMessage("CPFTrigger.month.errMsg"));
		}
		if(pfHeader.getFinancialYear()==null){
			addFieldError("financialYear", getMessage("CPFTrigger.FinancialYear.errMsg"));
		}
		PFTriggerDetail cpfTriggerDetail = null;
		if(pfHeader.getMonth()!=null  && pfHeader.getFinancialYear()!=null){
			cpfTriggerDetail =(PFTriggerDetail) pfTriggerDtlService.find("from PFTriggerDetail where month=? and financialYear.id=? and pfType=?",pfHeader.getMonth(),pfHeader.getFinancialYear().getId(),"CPF");
			if(cpfTriggerDetail!=null){
				addActionError(getMessage("CPFTrigger.TriggerBatchProcess.Msg"));
			}
		}
	}

	public PFHeader getPfHeader() {
		return pfHeader;
	}

	public void setPfHeader(PFHeader pfHeader) {
		this.pfHeader = pfHeader;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setCpfDelegate(CPFDelegate cpfDelegate) {
		this.cpfDelegate = cpfDelegate;
	}

	public PersistenceService<PFTriggerDetail, Long> getPfTriggerDtlService() {
		return pfTriggerDtlService;
	}

	public void setPfTriggerDtlService(
			PersistenceService<PFTriggerDetail, Long> pfTriggerDtlService) {
		this.pfTriggerDtlService = pfTriggerDtlService;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}
