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
package org.egov.eventnotification.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.infra.admin.master.entity.User;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * this api will contains all scheduler , that will run for different-2 purpose.
 * @author somvit
 *
 */
public class NotificationSchedulerManager {

    private static final Logger LOGGER = Logger.getLogger(NotificationSchedulerManager.class);

    private NotificationSchedulerManager() {

    }

    private static Scheduler scheduler;

    /**
     * all scheduler job will be configure here.
     */
    public static void schedule(NotificationSchedule notificationschedule, User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(notificationschedule.getStartDate()));
        int hours = Integer.parseInt(notificationschedule.getStartTime().split(":")[0]);
        int minutes = Integer.parseInt(notificationschedule.getStartTime().split(":")[1]);

        // configure the scheduler time
        switch (notificationschedule.getRepeat().toLowerCase()) {
        case EventnotificationConstant.SCHEDULE_DAY:
            JobDetail daylyJob = JobBuilder.newJob(NotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            CronTrigger daylyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 " + minutes + " " + hours + " ? * * *")).build();
            JobDataMap jobDataMap = daylyJob.getJobDataMap();
            jobDataMap.put(EventnotificationConstant.USER, user);
            jobDataMap.put(EventnotificationConstant.SCHEDULEID, notificationschedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.scheduleJob(daylyJob, daylyTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        case EventnotificationConstant.SCHEDULE_MONTH:
            JobDetail monthlyJob = JobBuilder.newJob(MonthlyNotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            CronTrigger monthlyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0 " + minutes + " " + hours + " " + calendar.get(Calendar.DAY_OF_MONTH) + " * ? *"))
                    .build();
            JobDataMap jobDataMap1 = monthlyJob.getJobDataMap();
            jobDataMap1.put(EventnotificationConstant.USER, user);
            jobDataMap1.put(EventnotificationConstant.SCHEDULEID, notificationschedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.scheduleJob(monthlyJob, monthlyTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        case EventnotificationConstant.SCHEDULE_YEAR:
            JobDetail yearlyJob = JobBuilder.newJob(YearlyNotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            CronTrigger yearlyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 " + minutes + " " + hours + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " " + (calendar.get(Calendar.MONTH) + 1) + " ? *"))
                    .build();
            JobDataMap jobDataMap2 = yearlyJob.getJobDataMap();
            jobDataMap2.put(EventnotificationConstant.USER, user);
            jobDataMap2.put(EventnotificationConstant.SCHEDULEID, notificationschedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.scheduleJob(yearlyJob, yearlyTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        default:
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            JobDetail singleJob = JobBuilder.newJob(OneTimeNotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            Trigger singleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + notificationschedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .startAt(calendar.getTime())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                    .build();
            JobDataMap jobDataMap3 = singleJob.getJobDataMap();
            jobDataMap3.put(EventnotificationConstant.USER, user);
            jobDataMap3.put(EventnotificationConstant.SCHEDULEID, notificationschedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.scheduleJob(singleJob, singleTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        }
    }

    /**
     * Delete the existing job.
     */
    public static void removeJob(NotificationSchedule notificationschedule) {
        String scheduleJob = null;
        String scheduleTrigger = null;

        // configure the scheduler time
        switch (notificationschedule.getRepeat().toLowerCase()) {
        case EventnotificationConstant.SCHEDULE_DAY:
            scheduleJob = EventnotificationConstant.JOB + notificationschedule.getId();
            scheduleTrigger = EventnotificationConstant.TRIGGER + notificationschedule.getId();
            break;
        case EventnotificationConstant.SCHEDULE_MONTH:
            scheduleJob = EventnotificationConstant.JOB + notificationschedule.getId();
            scheduleTrigger = EventnotificationConstant.TRIGGER + notificationschedule.getId();
            break;
        case EventnotificationConstant.SCHEDULE_YEAR:
            scheduleJob = EventnotificationConstant.JOB + notificationschedule.getId();
            scheduleTrigger = EventnotificationConstant.TRIGGER + notificationschedule.getId();
            break;
        default:
            scheduleJob = EventnotificationConstant.JOB + notificationschedule.getId();
            scheduleTrigger = EventnotificationConstant.TRIGGER + notificationschedule.getId();
            break;
        }

        try {
            Scheduler scheduler = NotificationSchedulerManager.getScheduler();
            boolean isTrigger = scheduler
                    .unscheduleJob(new TriggerKey(scheduleTrigger, EventnotificationConstant.NOTIFICATION_JOB));
            scheduler.deleteJob(new JobKey(scheduleJob, EventnotificationConstant.NOTIFICATION_JOB));
            LOGGER.info(scheduleJob + " is deleted with trigger status " + isTrigger);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Modify the existing job.
     */
    public static void updateJob(NotificationSchedule newSchedule, User user) {
        String scheduleTrigger = null;

        Calendar newTime = Calendar.getInstance();
        newTime.setTime(new Date(newSchedule.getStartDate()));
        int newhours = Integer.parseInt(newSchedule.getStartTime().split(":")[0]);
        int newminutes = Integer.parseInt(newSchedule.getStartTime().split(":")[1]);

        // configure the scheduler time
        switch (newSchedule.getRepeat().toLowerCase()) {
        case EventnotificationConstant.SCHEDULE_DAY:
            scheduleTrigger = EventnotificationConstant.TRIGGER + newSchedule.getId();
            JobDetail daylyJob = JobBuilder.newJob(NotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + newSchedule.getId(), EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            CronTrigger daylyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + newSchedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 " + newminutes + " " + newhours + " ? * * *")).build();
            JobDataMap jobDataMap = daylyJob.getJobDataMap();
            jobDataMap.put(EventnotificationConstant.USER, user);
            jobDataMap.put(EventnotificationConstant.SCHEDULEID, newSchedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();
                scheduler.addJob(daylyJob, true, true);
                scheduler.rescheduleJob(new TriggerKey(scheduleTrigger, EventnotificationConstant.NOTIFICATION_JOB),
                        daylyTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        case EventnotificationConstant.SCHEDULE_MONTH:
            scheduleTrigger = EventnotificationConstant.TRIGGER + newSchedule.getId();
            JobDetail monthlyJob = JobBuilder.newJob(MonthlyNotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + newSchedule.getId(), EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            CronTrigger monthlyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + newSchedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .withSchedule(CronScheduleBuilder.cronSchedule(
                            "0 " + newminutes + " " + newhours + " " + newTime.get(Calendar.DAY_OF_MONTH) + " * ? *"))
                    .build();
            JobDataMap jobDataMap1 = monthlyJob.getJobDataMap();
            jobDataMap1.put(EventnotificationConstant.USER, user);
            jobDataMap1.put(EventnotificationConstant.SCHEDULEID, newSchedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.addJob(monthlyJob, true, true);
                scheduler.rescheduleJob(new TriggerKey(scheduleTrigger, EventnotificationConstant.NOTIFICATION_JOB),
                        monthlyTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        case EventnotificationConstant.SCHEDULE_YEAR:
            scheduleTrigger = EventnotificationConstant.TRIGGER + newSchedule.getId();
            JobDetail yearlyJob = JobBuilder.newJob(YearlyNotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + newSchedule.getId(), EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            CronTrigger yearlyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + newSchedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 " + newminutes + " " + newhours + " "
                            + newTime.get(Calendar.DAY_OF_MONTH) + " " + (newTime.get(Calendar.MONTH) + 1) + " ? *"))
                    .build();
            JobDataMap jobDataMap2 = yearlyJob.getJobDataMap();
            jobDataMap2.put(EventnotificationConstant.USER, user);
            jobDataMap2.put(EventnotificationConstant.SCHEDULEID, newSchedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.addJob(yearlyJob, true, true);
                scheduler.rescheduleJob(new TriggerKey(scheduleTrigger, EventnotificationConstant.NOTIFICATION_JOB),
                        yearlyTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        default:
            newTime.set(Calendar.HOUR_OF_DAY, newhours);
            newTime.set(Calendar.MINUTE, newminutes);
            scheduleTrigger = EventnotificationConstant.TRIGGER + newSchedule.getId();
            JobDetail singleJob = JobBuilder.newJob(OneTimeNotificationSchedulerJob.class)
                    .withIdentity(EventnotificationConstant.JOB + newSchedule.getId(), EventnotificationConstant.NOTIFICATION_JOB)
                    .build();
            Trigger singleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EventnotificationConstant.TRIGGER + newSchedule.getId(),
                            EventnotificationConstant.NOTIFICATION_JOB)
                    .startAt(newTime.getTime())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                    .build();
            JobDataMap jobDataMap3 = singleJob.getJobDataMap();
            jobDataMap3.put(EventnotificationConstant.USER, user);
            jobDataMap3.put(EventnotificationConstant.SCHEDULEID, newSchedule.getId());
            try {
                Scheduler scheduler = NotificationSchedulerManager.getScheduler();
                scheduler.start();

                scheduler.addJob(singleJob, true, true);
                scheduler.rescheduleJob(new TriggerKey(scheduleTrigger, EventnotificationConstant.NOTIFICATION_JOB),
                        singleTrigger);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            break;
        }
    }

    public static Scheduler getScheduler() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return scheduler;
    }
}
