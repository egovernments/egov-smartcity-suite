package org.egov.web.actions.voucher;

import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DemoupdateJob  extends AbstractQuartzJob {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DemoupdateJob.class);
	
	@Override
	public void executeJob() {
		// TODO Auto-generated method stub
		if(LOGGER.isInfoEnabled())     LOGGER.info("Started Scheduler now ");
	
		
		if(LOGGER.isInfoEnabled())     LOGGER.info("ending  Scheduler now");
	}
	
}