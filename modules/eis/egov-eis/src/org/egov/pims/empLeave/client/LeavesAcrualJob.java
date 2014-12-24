package org.egov.pims.empLeave.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.administration.rjbac.user.BeforeUserJurisdictionAction;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.empLeave.model.LeaveOpeningBalance;
import org.egov.pims.empLeave.model.TypeOfLeaveMaster;
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * Accumulated leave for employees will be accrued on a half-yearly/yearly  based on financial/calendar year 
 * this is will trigger automatically when the server is up
 * yearly and calendar year based will trigger on 1st JAN 1AM of every year ,cronexpression for this 0 0 1 1 1 ? 
 * yearly and financial year based will trigger on 1st APR 1AM of every year ,cronexpression for this 0 0 1 1 4 ? 
 * half-yearly and calendar year based will trigger on 1st JAN and JUL 1AM of every year ,cronexpression for this 0 0 1 1 1,7 ? 
 * half-yearly and financial year based will trigger on 1st APR and OCT 1AM of every year ,cronexpression for this 0 0 1 1 4,10 ?
 * @author suhasini
 *
 */
public class LeavesAcrualJob extends QuartzJobBean implements StatefulJob {

	private static final Logger logger = LoggerFactory.getLogger(LeavesAcrualJob.class);
	private static final long serialVersionUID = 4260620269267397370L;
	String cityList;
	String userName;
	@Override
	public void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try{
		//set thread local manually,since it wont come through filters	
			if(!cityList.isEmpty())
			{
			String[] cities = cityList.split(",");//TODO read from properties file,the city value in xml
			String jndiName="";
			String hibFactName ="";
			String cityURL="";
			
			for(String city:cities)
			{

				jndiName=EGovConfig.getProperty(city, "","JNDIURL");
				hibFactName = EGovConfig.getProperty(city, "","HibernateFactory");
				cityURL="http://" + city;
				logger.debug("Setting thread locals: [" + cityURL + "][" + jndiName + "][" + hibFactName + "]");
				SetDomainJndiHibFactNames.setThreadLocals(cityURL,jndiName,hibFactName);
				HibernateUtil.beginTransaction();
				User user = null;
				
				UserService userMgr=(UserService)getApplicationContext(context).getBean("eisUserMgr");
				if(userName == null) {
					user = userMgr.getUserByUserName("egovernments");
				} else {
					user = userMgr.getUserByUserName(userName);
				}
				EGOVThreadLocals.setUserId(user.getId().toString());
				EmpLeaveService eisLeaveMgr=(EmpLeaveService)getApplicationContext(context).getBean("eisLeaveMgr");
				PersistenceService persistenceService=(PersistenceService)getApplicationContext(context).getBean("persistenceService");
				ScriptService scriptService=(ScriptService)getApplicationContext(context).getBean("scriptExecutionService");
				EmployeeService eisMgr=(EmployeeService)getApplicationContext(context).getBean("eisMgr");
				
				List<PersonalInformation> employeeList=eisMgr.getAllEmployees();
				List<TypeOfLeaveMaster> typeOfLeaveMstrList=	eisLeaveMgr.getAccumulatedLeaveTypes();
				if(!(typeOfLeaveMstrList.isEmpty() && employeeList.isEmpty()))
				{
				scriptService.executeScript("eis.LeaveAcrualAutoSchedule", ScriptService.createContext("employeeList", employeeList,"eisLeaveMgr",  eisLeaveMgr,
						"typeOfLeaveMstrList",typeOfLeaveMstrList,"persistenceService",persistenceService));
				}
				
				HibernateUtil.commitTransaction();
			}
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			HibernateUtil.rollbackTransaction();
		}
		
	}

	
	//useful to wire beans
	private ApplicationContext getApplicationContext(JobExecutionContext jobContext)
	{
		try{
			SchedulerContext schedulerContext = jobContext.getScheduler().getContext();
			ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");
			return applicationContext;
		}catch(SchedulerException ex)
		{
			logger.info("Error in getting scheduler..");
			throw new EGOVRuntimeException(ex.getMessage());
		}
	}
	public void setCityList(String cityList) {
		this.cityList = cityList;
	}

	public String getCityList() {
		return cityList;
	}

}
