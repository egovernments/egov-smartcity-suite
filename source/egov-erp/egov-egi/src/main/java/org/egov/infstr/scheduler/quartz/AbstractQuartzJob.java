/*
 * @(#)AbstractQuartzJob.java 3.0, 18 Jun, 2013 2:57:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.scheduler.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.scheduler.GenericJob;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * An abstract base class wrapper for {@link QuartzJobBean} and implements 
 * {@link GenericJob}. A class which extends this will be eligible for doing
 * Quartz Jobs. Those classes required Statefulness (Threadsafety) need to implement 
 * {@link StatefulJob} apart from this class. This class also wrap up wiring of some of the 
 * common settings and beans. 
 **/
public abstract class AbstractQuartzJob extends QuartzJobBean implements GenericJob {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQuartzJob.class);
	private UserService userService;
	private volatile boolean isTransactional;
	private volatile String userName;
	protected List<String> cities;
	
	/**
	 * This method will wrap up the Transaction (if isTransactional set to true) and call the executeJob
	 * implementation on individual job class. 
	 **/
	@Override
	protected void executeInternal(final JobExecutionContext jobCtx) throws JobExecutionException {
		try {
			if (this.isTransactional) {
				for (final String city : cities) {
					setTractionalSupport(city);
					//HibernateUtil.beginTransaction();
					setUserInThreadLocal();
					executeJob();
					//HibernateUtil.commitTransaction();
				} 
			} else {
				executeJob();
			}
			
		} catch (final Exception ex) {
			LOGGER.error("Unable to complete execution Scheduler ", ex);
			if (isTransactional) {
				//HibernateUtil.rollbackTransaction();
			}
			throw new JobExecutionException("Unable to execute batch job Scheduler", ex, false);
		}
		finally {
			//HibernateUtil.closeSession();
		}
	}

	public void setUserName(final String userName) {
		if (StringUtils.isBlank(userName)) {
			this.userName = "egovernments";
		} else {
			this.userName = userName;
		}
	}

	public void setTransactional(final boolean isTransactional) {
		this.isTransactional = isTransactional;
	}

	public void setCities(final List<String> cities) {
		this.cities = cities;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	protected void setTractionalSupport(final String city) {
		final String hibFactName = EGovConfig.getProperty(city, "","HibernateFactory");		
		final String jndi = EGovConfig.getProperty(city, "", "JNDIURL");
		SetDomainJndiHibFactNames.setThreadLocals(city, jndi, hibFactName);
	}

	protected void setUserInThreadLocal() {
		EGOVThreadLocals.setUserId(String.valueOf(userService.getUserByUserName(this.userName).getId()));
	}
}
