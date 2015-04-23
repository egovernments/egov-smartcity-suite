package org.egov.eb.scheduler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.eb.service.bill.EBBillInfoService;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EBBillInfoFetchJob implements Job {

	private static final Logger LOGGER = Logger.getLogger(EBBillInfoFetchJob.class);
	private static final String URL = "localhost";
	
	private EBBillInfoService billInfoService;
	private String billingCycle;
	
	private boolean isDebugEnabled = LOGGER.isDebugEnabled();
	
	@Override
	public void execute(JobExecutionContext jobExeContext) throws JobExecutionException {
		
		if (isDebugEnabled) {
			LOGGER.debug("Entered into BillInfoFetchJob.execute");
		}
		
		try {
			setTractionalSupport();
			
			//This fix is for Phoenix Migration.
			//.HibernateUtil.beginTransaction();*/
			
			EGOVThreadLocals.setUserId((String) jobExeContext.getJobDetail().getJobDataMap().get("loggedInUserId"));
					
			executeJob(jobExeContext);
			
			 
			
		} catch (final Exception ex) {
			LOGGER.error("Unable to complete execution Scheduler ", ex);
			 
			throw new JobExecutionException("Unable to execute job Scheduler", ex, false);
		}
		finally {
			//.HibernateUtil.closeSession();*/
		}
		
		if (isDebugEnabled) {
			LOGGER.debug("Exiting from BillInfoFetchJob.execute");
		}
	}

	private void executeJob(JobExecutionContext jobExeContext) {
		Long startTime = System.currentTimeMillis();

		billInfoService = (EBBillInfoService) new ApplicationContextBeanProvider().getBean("billInfoService",
				getAppContextFiles());

		billingCycle = (String) jobExeContext.getJobDetail().getJobDataMap().get("billingCycle");
		String ebLogIdStr=(String)jobExeContext.getJobDetail().getJobDataMap().get("ebLogId");
		Long ebLogId=null;
		if(ebLogIdStr!=null)
		{
		ebLogId=Long.parseLong(ebLogIdStr);
		}
		billInfoService.fetchEBBills(billingCycle,ebLogId );
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("EBBillInfoFetchJob - Job is completed successfully in "
					+ (System.currentTimeMillis() - startTime) / 1000 + " sec(s)");
		}
	}

	private void setTractionalSupport() {
		
		String jndiName = EGovConfig.getProperty(URL, "","JNDIURL");
		
		if (StringUtils.isNotBlank(jndiName)) {
			EGOVThreadLocals.setJndiName(jndiName);
		}
		
		String factoryName = EGovConfig.getProperty(URL, "","HibernateFactory");
		
		if (StringUtils.isNotBlank(factoryName)) {
			EGOVThreadLocals.setHibFactName(factoryName);
		}
	}

	private String[] getAppContextFiles() {
		
		if (isDebugEnabled) {
			LOGGER.debug("Entered into getAppContextFiles");
		}
		
		String [] contextFiles = new String[] {
				"classpath*:org/egov/infstr/beanfactory/globalApplicationContext.xml",
                "classpath*:org/egov/infstr/beanfactory/egiApplicationContext.xml",
                "classpath*:org/egov/infstr/beanfactory/applicationContext-pims.xml",
                "classpath*:org/egov/infstr/beanfactory/applicationContext-egf.xml",
                "classpath*:org/egov/infstr/beanfactory/applicationContext-erpcollections.xml",
                "classpath*:org/serviceconfig-Bean.xml"
		};
		
		if (isDebugEnabled) {
			LOGGER.debug("Entered into getAppContextFiles");
		}
		
		return contextFiles;
	}

}
