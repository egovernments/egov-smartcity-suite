/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.scheduler.quartz;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.quartz.JobDataMap;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class QuartzJobAwareBeanFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobAwareBeanFactory.class);
    private static final String SCHEDULER_NAME = "%s-scheduler";
    private static final String MODULE_NAME_KEY = "moduleName";
    private static final String JOB_BEAN_NAME_KEY = "jobBeanName";

    private String[] ignoredUnknownProperties;
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setIgnoredUnknownProperties(final String[] ignoredUnknownProperties) {
        super.setIgnoredUnknownProperties(ignoredUnknownProperties);
        this.ignoredUnknownProperties = ignoredUnknownProperties;
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) {
        try {
            JobDataMap jobDataMap = bundle.getJobDetail().getJobDataMap();
            SchedulerContext schedulerContext = SchedulerRepository.getInstance().
                    lookup(String.format(SCHEDULER_NAME, jobDataMap.getString(MODULE_NAME_KEY))).
                    getContext();
            Object job = applicationContext.getBean(jobDataMap.getString(JOB_BEAN_NAME_KEY), bundle.getJobDetail().getJobClass());
            BeanWrapper bw = new BeanWrapperImpl(job);
            if (isEligibleForPropertyPopulation(bw.getWrappedInstance())) {
                final MutablePropertyValues pvs = new MutablePropertyValues();
                if (schedulerContext != null) {
                    pvs.addPropertyValues(schedulerContext);
                }
                pvs.addPropertyValues(jobDataMap);
                pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());
                if (ignoredUnknownProperties != null) {
                    for (String property : ignoredUnknownProperties) {
                        if (pvs.contains(property) && !bw.isWritableProperty(property)) {
                            pvs.removePropertyValue(property);
                        }
                    }
                    bw.setPropertyValues(pvs);
                } else {
                    bw.setPropertyValues(pvs, true);
                }
            }
            return bw.getWrappedInstance();
        } catch (SchedulerException e) {
            LOGGER.error("Error occurred while initializing Scheduler Job Beans", e);
            throw new ApplicationRuntimeException("Error occurred while initializing Scheduler", e);
        }
    }
}
