package org.egov.payroll.services.providentfund;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.services.PersistenceService;
import org.egov.payroll.model.providentfund.PFHeader;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.StatefulJob;

/**
 * @author Jagadeesan
 *
 */
public class CPFQuartzJob implements StatefulJob
{
	private static final Logger LOGGER = Logger.getLogger(CPFQuartzJob.class);
	CPFDelegate delegate;
	//PersistenceService<PFHeader, Long> cpfService; 
	
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
		LOGGER.info("JobName ["+context.getJobDetail().getDescription()+"] executing at="+new java.util.Date());
		
		try
		{
			//ApplicationContext webAppCtx = (ApplicationContext)context.getScheduler().getContext().get(PayrollConstants.APPLICATION_CONTEXT_KEY);
			//delegate = (CPFDelegate) webAppCtx.getBean("cpfDelegate");
			//cpfService = (PersistenceService<PFHeader, Long>) webAppCtx.getBean("cpfService");
			
			SchedulerContext schedCtx = context.getScheduler().getContext();
			delegate = (CPFDelegate) schedCtx.get("cpfDelegate");
			//cpfService = (PersistenceService<PFHeader, Long>) schedCtx.get("cpfService");
			
			String cityURL = (String) context.getJobDetail().getJobDataMap().get("cityURL");
			String hibFactName = (String) context.getJobDetail().getJobDataMap().get("hibFactName");
			String jndi = (String) context.getJobDetail().getJobDataMap().get("jndi");
			int userId = (Integer) context.getJobDetail().getJobDataMap().get("userId");
			String cpfHeaderId = (String)context.getJobDetail().getJobDataMap().get("cpfHeaderId");
			String cpfMonth = (String)context.getJobDetail().getJobDataMap().get("cpfMonth");
			String cpfFinancialYearId = (String)context.getJobDetail().getJobDataMap().get("cpfFinancialYearId");
			SetDomainJndiHibFactNames.setThreadLocals(cityURL,jndi,hibFactName);
			//PFHeader pfHeader = (PFHeader)cpfService.findById(Long.valueOf(cpfHeaderId),false);
			List<String> mandatoryFields = (ArrayList<String>) context.getJobDetail().getJobDataMap().get("mandatoryFields");
			delegate.CPFCalculation(userId,new Date(),cpfHeaderId,cpfMonth,cpfFinancialYearId,mandatoryFields);
		}
		catch(JobExecutionException e)
		{
			LOGGER.error("JobExecutionException in CPFQuartzJob ="+e.getMessage());
			throw e;
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in CPFQuartzJob ="+e.getMessage());
		}
	}
}