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
package org.egov.eventnotification.service;

import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.EventNotificationProperties;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.ordinalIndexOf;
import static org.egov.eventnotification.utils.Constants.BEANNOTIFSCH;
import static org.egov.eventnotification.utils.Constants.DAY_CRON;
import static org.egov.eventnotification.utils.Constants.EVENT_NOTIFICATION_GROUP;
import static org.egov.eventnotification.utils.Constants.HOURS_CRON;
import static org.egov.eventnotification.utils.Constants.MINUTES_CRON;
import static org.egov.eventnotification.utils.Constants.MONTH_CRON;
import static org.egov.eventnotification.utils.Constants.TRIGGER;

@Service
public class ScheduleDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDetailsService.class);
    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private EventNotificationProperties appProperties;

    /**
     * This method is used to create a new job based on the newly created schedule.
     *
     * @param schedule
     * @param fullURL
     */
    public void executeScheduler(Schedule schedule, String fullURL) {
        String cronExpression = getCronExpression(schedule);
        final JobDetailImpl jobDetail = (JobDetailImpl) beanProvider.getBean("eventNotificationJobDetail");
        final Scheduler scheduler = (Scheduler) beanProvider.getBean(BEANNOTIFSCH);
        try {
            jobDetail.setName(ApplicationThreadLocals.getTenantID().concat("_")
                    .concat("eventNotificationJob".concat(String.valueOf(schedule.getId()))));
            jobDetail.getJobDataMap().put("scheduleId", String.valueOf(schedule.getId()));
            jobDetail.getJobDataMap().put("contextURL", fullURL.substring(0, ordinalIndexOf(fullURL, "/", 3)));

            if (cronExpression == null) {
                final SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(ApplicationThreadLocals.getTenantID().concat("_")
                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))));
                trigger.setStartTime(new Date(System.currentTimeMillis() + 100000));
                trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                final Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(ApplicationThreadLocals.getTenantID().concat("_")
                                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                                EVENT_NOTIFICATION_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
            }

        } catch (final SchedulerException e) {
            LOGGER.error("Error : Encountered an exception while scheduling", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * This method take a existing scheduler and remove it from schedule.
     *
     * @param schedule
     */
    public void removeScheduler(Schedule schedule) {
        final Scheduler scheduler = (Scheduler) beanProvider.getBean(BEANNOTIFSCH);
        try {
            scheduler.unscheduleJob(new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                    .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                    EVENT_NOTIFICATION_GROUP));
            scheduler.deleteJob(new JobKey("eventNotificationJob".concat(String.valueOf(schedule.getId()))));
        } catch (final SchedulerException e) {
            LOGGER.error("Error : Encountered an exception while deleting a schedule job", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * This method modify the existing scheduler. It's basically modifying the schedule time using the rescheduleJob method.
     *
     * @param schedule
     */
    public void modifyScheduler(Schedule schedule) {
        String cronExpression = getCronExpression(schedule);
        final Scheduler scheduler = (Scheduler) beanProvider.getBean(BEANNOTIFSCH);
        try {

            if (cronExpression == null) {
                TriggerKey triggerKey = new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                        EVENT_NOTIFICATION_GROUP);
                final SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(TRIGGER.concat(String.valueOf(schedule.getId())));
                trigger.setStartTime(new Date(System.currentTimeMillis() + 100000));
                trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                scheduler.rescheduleJob(triggerKey, trigger);
                if (!scheduler.isShutdown())
                    scheduler.start();
            } else {
                TriggerKey triggerKey = new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                        .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                        EVENT_NOTIFICATION_GROUP);
                final Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(TRIGGER.concat(String.valueOf(schedule.getId())),
                                EVENT_NOTIFICATION_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
                scheduler.rescheduleJob(triggerKey, trigger);
                if (!scheduler.isShutdown())
                    scheduler.start();
            }

        } catch (final SchedulerException e) {
            LOGGER.error("Error : Encountered an exception while update schedule", e);
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * This method take a cron expression from properties file and replace the hour,minute,day and month into the placeholder to
     * make dynamic cron expression.
     *
     * @param notificationschedule
     * @return
     */
    private String getCronExpression(Schedule notificationschedule) {
        String cronExpression = null;
        DateTime calendar = new DateTime(notificationschedule.getStartDate());
        int hours = calendar.getHourOfDay();
        int minutes = calendar.getMinuteOfHour();

        switch (notificationschedule.getScheduleRepeat().getName().toLowerCase()) {
            case "day":
                cronExpression = appProperties.getDailyCron().replace(MINUTES_CRON, String.valueOf(minutes));
                cronExpression = cronExpression.replace(HOURS_CRON, String.valueOf(hours));
                break;
            case "month":
                cronExpression = appProperties.getMonthlyCron().replace(MINUTES_CRON, String.valueOf(minutes));
                cronExpression = cronExpression.replace(HOURS_CRON, String.valueOf(hours));
                cronExpression = cronExpression.replace(DAY_CRON, String.valueOf(calendar.getDayOfMonth()));
                break;
            case "year":
                cronExpression = appProperties.getYearlyCron().replace(MINUTES_CRON, String.valueOf(minutes));
                cronExpression = cronExpression.replace(HOURS_CRON, String.valueOf(hours));
                cronExpression = cronExpression.replace(DAY_CRON, String.valueOf(calendar.getDayOfMonth()));
                cronExpression = cronExpression.replace(MONTH_CRON, String.valueOf(calendar.getMonthOfYear()));
                break;
            default:
                break;
        }
        return cronExpression;
    }
}
