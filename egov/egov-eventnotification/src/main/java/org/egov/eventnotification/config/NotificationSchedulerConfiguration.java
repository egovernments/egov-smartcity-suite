/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.eventnotification.config;

import org.egov.eventnotification.scheduler.NotificationSchedulerJob;
import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Conditional(NotificationConfigCondition.class)
public class NotificationSchedulerConfiguration extends QuartzSchedulerConfiguration {

    @Bean(name = "eventNotificationScheduler", destroyMethod = "destroy")
    public SchedulerFactoryBean eventNotificationScheduler(DataSource dataSource) {
        SchedulerFactoryBean notificationScheduler = createScheduler(dataSource);
        notificationScheduler.setSchedulerName("eventnotification-scheduler");
        notificationScheduler.setAutoStartup(true);
        notificationScheduler.setOverwriteExistingJobs(true);
        notificationScheduler.setStartupDelay(1500);
        return notificationScheduler;
    }

    @Bean(name = "eventNotificationJobDetail")
    public JobDetailFactoryBean eventNotificationJobDetail() {
        JobDetailFactoryBean notificationJobDetail = new JobDetailFactoryBean();
        notificationJobDetail.setGroup("EVENT_NOTIFICATION_JOB_GROUP");
        notificationJobDetail.setName("EVENT_NOTIFICATION_JOB");
        notificationJobDetail.setDurability(true);
        notificationJobDetail.setJobClass(NotificationSchedulerJob.class);
        notificationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new ConcurrentHashMap<>();
        jobDetailMap.put("jobBeanName", "notificationJob");
        jobDetailMap.put("userName", "system");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "eventnotification");
        notificationJobDetail.setJobDataAsMap(jobDetailMap);
        return notificationJobDetail;
    }

    @Bean("notificationJob")
    public NotificationSchedulerJob notificationJob() {
        return new NotificationSchedulerJob();
    }

}
