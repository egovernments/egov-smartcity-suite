package org.egov.services.deduction;

import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.RemittanceSchedulerLog;
import org.egov.utils.FinancialConstants;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.StatefulJob;

public class RemittanceJob extends AbstractQuartzJob implements StatefulJob{

	private static final Logger LOGGER    = Logger.getLogger(RemittanceJob.class);
	private String glcode;
    private	ScheduledRemittanceService scheduledRemittanceService;
	private String jobName;        
	private Long schedularLogId;
 	@Override   
	public void executeJob() {
 		LOGGER.info("Inside RemittanceJob"); 
		RemittanceSchedulerLog remittanceScheduler = new RemittanceSchedulerLog();
		remittanceScheduler = buildRemittanceScheduler(remittanceScheduler);
		remittanceScheduler.setCreatedBy( Integer.valueOf(EGOVThreadLocals.getUserId()));
		scheduledRemittanceService.getRemittanceSchedulerLogService().persist(remittanceScheduler);
		
		schedularLogId=remittanceScheduler.getId();
		scheduledRemittanceService.searchRecovery(glcode,jobName,schedularLogId,null,null);
	}
	     
	private RemittanceSchedulerLog buildRemittanceScheduler(RemittanceSchedulerLog remittanceScheduler)
	{
		remittanceScheduler.setGlcode(null);
		remittanceScheduler.setSchType(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_AUTO);
		remittanceScheduler.setSchJobName(getJobName());
		remittanceScheduler.setLastRunDate(new Date());
		remittanceScheduler.setCreatedDate(new Date());
		remittanceScheduler.setCreatedBy( Integer.valueOf(EGOVThreadLocals.getUserId()));
		remittanceScheduler.setStatus("Started");      
		return remittanceScheduler;      
	}
	public String getGlcode() {
		return glcode;
	}
	public void setGlcode(String glcode) {  
		this.glcode = glcode;
	}
	

	public void setScheduledRemittanceService(
			ScheduledRemittanceService scheduledRemittanceService) {
		this.scheduledRemittanceService = scheduledRemittanceService;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}	
	
}
