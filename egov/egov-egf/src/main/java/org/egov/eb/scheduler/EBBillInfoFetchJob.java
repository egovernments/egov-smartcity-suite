/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.scheduler;

import org.apache.log4j.Logger;
import org.egov.eb.service.bill.EBBillInfoService;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.utils.EGovConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
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
			
			
			//EGOVThreadLocals.setUserId((Long) jobExeContext.getJobDetail().getJobDataMap().get("loggedInUserId"));
			//TODO use new quartz class from egi		
			executeJob(jobExeContext);
			
			 
			
		} catch (final Exception ex) {
			LOGGER.error("Unable to complete execution Scheduler ", ex);
			 
			throw new JobExecutionException("Unable to execute job Scheduler", ex, false);
		}
		
		if (isDebugEnabled) {
			LOGGER.debug("Exiting from BillInfoFetchJob.execute");
		}
	}
	@Transactional
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
		
	/*	if (StringUtils.isNotBlank(jndiName)) {
			EGOVThreadLocals.setJndiName(jndiName);
		}
		
		String factoryName = EGovConfig.getProperty(URL, "","HibernateFactory");
		
		if (StringUtils.isNotBlank(factoryName)) {
			EGOVThreadLocals.setHibFactName(factoryName);
		}*/
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
