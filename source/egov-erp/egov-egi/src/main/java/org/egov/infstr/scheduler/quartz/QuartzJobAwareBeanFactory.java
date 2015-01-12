/*
 * @(#)QuartzJobAwareBeanFactory.java 3.0, 18 Jun, 2013 2:57:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.scheduler.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.quartz.JobDataMap;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Wrapper around {@link SpringBeanJobFactory} which is responsible for initialising
 * Job Class. This wrapper will get the already available Job Class bean from the respective
 * Application Context and wire it altogether and returns the Job class instance. 
 * This will work only if 
 * 1) scheduler application context file name is applicationQuartzContext-xyz (xyz=module name) 
 * 2) The below have to be set in JobDetailBean definition's jobDataAsMap
 * 	a) jobBeanName (name or id of the job class bean)
 * 	b) moduleName
 *   
 **/
public class QuartzJobAwareBeanFactory extends SpringBeanJobFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobAwareBeanFactory.class);
	/**This value will be prefixed with moduleName value to get invoking application context file*/
	private static final String APP_CTX_NAME_PREFIX = "applicationQuartzContext-";
	/**This value will be suffixed with moduleName value to get target Scheduler*/
	private static final String SCHEDULER_NAME_SUFFIX = "-scheduler";
	
	private String[] ignoredUnknownProperties;
	
	@Override
	public void setIgnoredUnknownProperties(final String[] ignoredUnknownProperties) {
		super.setIgnoredUnknownProperties(ignoredUnknownProperties);
		this.ignoredUnknownProperties = ignoredUnknownProperties;
	}

	/**
	 * An implementation of SpringBeanJobFactory that retrieves the bean from
	 * the Spring application context for a particular module and wrap it up
	 * with other values from jobDataAsMap. 
	 */
	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) {
		try {
			final JobDataMap jobDataMap = bundle.getJobDetail().getJobDataMap();
			final SchedulerContext schedulerContext = SchedulerRepository.getInstance().lookup(jobDataMap.getString("moduleName")+SCHEDULER_NAME_SUFFIX).getContext();
			final XmlWebApplicationContext ctx = (XmlWebApplicationContext) schedulerContext.get(APP_CTX_NAME_PREFIX+ jobDataMap.getString("moduleName"));
			final Object job = ctx.getBean(jobDataMap.getString("jobBeanName"),	bundle.getJobDetail().getJobClass());
			final BeanWrapper bw = new BeanWrapperImpl(job);
			if (isEligibleForPropertyPopulation(bw.getWrappedInstance())) {
				final MutablePropertyValues pvs = new MutablePropertyValues();
				if (schedulerContext != null) {
					pvs.addPropertyValues(schedulerContext);
				}
				pvs.addPropertyValues(jobDataMap);
				pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());
				if (ignoredUnknownProperties != null) {
					for (final String ignoredUnknownPropertie : ignoredUnknownProperties) {
						final String propName = ignoredUnknownPropertie;
						if (pvs.contains(propName) && !bw.isWritableProperty(propName)) {
							pvs.removePropertyValue(propName);
						}
					}
					bw.setPropertyValues(pvs);
				} else {
					bw.setPropertyValues(pvs, true);
				}
			}
			return bw.getWrappedInstance();
		} catch (BeansException e) {
			LOGGER.error("Error occurred while initializing Scheduler Job Beans, cause : ",e);
			throw new  EGOVRuntimeException("Error occurred while initializing Scheduler Job Beans, cause : ",e);
		} catch (SchedulerException e) {
			LOGGER.error("Error occurred while initializing Scheduler Job Beans, cause : ",e);
			throw new  EGOVRuntimeException("Error occurred while initializing Scheduler, cause : ",e);
		}
	}
}
