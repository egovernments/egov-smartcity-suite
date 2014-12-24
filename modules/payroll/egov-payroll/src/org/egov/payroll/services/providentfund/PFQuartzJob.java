package org.egov.payroll.services.providentfund;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.StatefulJob;

/**
 * @author Ilayaraja
 *
 */
public class PFQuartzJob implements StatefulJob
{
	private static final Logger LOGGER = Logger.getLogger(PFQuartzJob.class);
	PFDelegate delegate;

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
		LOGGER.info("JobName ["+context.getJobDetail().getDescription()+"] executing at="+new java.util.Date());
		
		try
		{
			//ApplicationContext webAppCtx = (ApplicationContext)context.getScheduler().getContext().get(PayrollConstants.APPLICATION_CONTEXT_KEY);
			//delegate = (PFDelegate) webAppCtx.getBean("pfDelegate");
			SchedulerContext skedCtx = context.getScheduler().getContext();
			delegate = (PFDelegate) skedCtx.get("pfDelegate");
			String cityURL = (String) context.getJobDetail().getJobDataMap().get("cityURL");
			String hibFactName = (String) context.getJobDetail().getJobDataMap().get("hibFactName");
			String jndi = (String) context.getJobDetail().getJobDataMap().get("jndi");
			int userId = (Integer) context.getJobDetail().getJobDataMap().get("userId");
			List<String> mandatoryFields = (ArrayList<String>) context.getJobDetail().getJobDataMap().get("mandatoryFields");
			SetDomainJndiHibFactNames.setThreadLocals(cityURL,jndi,hibFactName);
			delegate.PFInterestCalculation(userId,new Date(),mandatoryFields);
		}
		catch(JobExecutionException e)
		{
			LOGGER.error("JobExecutionException in PFQuartzJob ="+e.getMessage());
			throw e;
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in PFQuartzJob ="+e.getMessage());
		}
	}
}
