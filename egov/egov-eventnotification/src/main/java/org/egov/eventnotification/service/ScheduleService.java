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

import static org.egov.eventnotification.constants.Constants.DAY_CRON;
import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.HOURS_CRON;
import static org.egov.eventnotification.constants.Constants.MINUTES_CRON;
import static org.egov.eventnotification.constants.Constants.MONTH_CRON;
import static org.egov.eventnotification.constants.Constants.SCHEDULED_STATUS;
import static org.egov.eventnotification.constants.Constants.ZERO;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.eventnotification.config.EventNotificationConfiguration;
import org.egov.eventnotification.config.properties.EventnotificationApplicationProperties;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.EventDetails;
import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.repository.ScheduleRepository;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleService {
    private static final int MAX_TEN = 10;
    private static final String EVENT_NOTIFICATION_GROUP = "EVENT_NOTIFICATION_GROUP";
    private static final String TRIGGER = "eventNotificationTrigger";
    private static final String JOB = "eventNotificationJob";
    private static final String BEANNOTIFSCH = "eventnotificationScheduler";

    @Autowired
    private ScheduleRepository notificationscheduleRepository;

    @Autowired
    private EventnotificationApplicationProperties appProperties;

    @Autowired
    private EventNotificationConfiguration notificationConfiguration;

    @Autowired
    private ApplicationContext beanProvider;

    public List<Schedule> getAllSchedule() {
        List<Schedule> notificationScheduleList = null;
        notificationScheduleList = notificationscheduleRepository.findByOrderByIdDesc();
        if (!notificationScheduleList.isEmpty())
            for (Schedule notificationSchedule : notificationScheduleList) {
                EventDetails eventDetails = new EventDetails();
                DateTime sd = new DateTime(notificationSchedule.getStartDate());
                eventDetails.setStartDt(
                        DateUtils.getDate(DateUtils.getDefaultFormattedDate(notificationSchedule.getStartDate()),
                                DDMMYYYY));
                if (sd.getHourOfDay() < MAX_TEN)
                    eventDetails.setStartHH(ZERO + String.valueOf(sd.getHourOfDay()));
                else
                    eventDetails.setStartHH(String.valueOf(sd.getHourOfDay()));
                if (sd.getMinuteOfHour() < MAX_TEN)
                    eventDetails.setStartMM(ZERO + String.valueOf(sd.getMinuteOfHour()));
                else
                    eventDetails.setStartMM(String.valueOf(sd.getMinuteOfHour()));
                notificationSchedule.setEventDetails(eventDetails);
            }
        return notificationScheduleList;
    }

    public Schedule getScheduleById(Long id) {
        Schedule notificationSchedule = notificationscheduleRepository.findOne(id);
        EventDetails eventDetails = new EventDetails();
        DateTime sd = new DateTime(notificationSchedule.getStartDate());
        eventDetails
                .setStartDt(DateUtils.getDate(DateUtils.getDefaultFormattedDate(notificationSchedule.getStartDate()),
                        DDMMYYYY));
        if (sd.getHourOfDay() < MAX_TEN)
            eventDetails.setStartHH(ZERO + String.valueOf(sd.getHourOfDay()));
        else
            eventDetails.setStartHH(String.valueOf(sd.getHourOfDay()));
        if (sd.getMinuteOfHour() < MAX_TEN)
            eventDetails.setStartMM(ZERO + String.valueOf(sd.getMinuteOfHour()));
        else
            eventDetails.setStartMM(String.valueOf(sd.getMinuteOfHour()));
        notificationSchedule.setEventDetails(eventDetails);
        return notificationSchedule;
    }

    @Transactional
    public Schedule saveSchedule(Schedule notificationSchedule) {
        DateTime sd = new DateTime(notificationSchedule.getEventDetails().getStartDt());
        sd = sd.withHourOfDay(Integer.parseInt(notificationSchedule.getEventDetails().getStartHH()));
        sd = sd.withMinuteOfHour(Integer.parseInt(notificationSchedule.getEventDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        notificationSchedule.setStartDate(sd.toDate());
        notificationSchedule.setStatus(SCHEDULED_STATUS);
        return notificationscheduleRepository.save(notificationSchedule);
    }

    @Transactional
    public Schedule updateSchedule(Schedule schedule) {
        DateTime sd = new DateTime(schedule.getEventDetails().getStartDt());
        sd = sd.withHourOfDay(Integer.parseInt(schedule.getEventDetails().getStartHH()));
        sd = sd.withMinuteOfHour(Integer.parseInt(schedule.getEventDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        schedule.setStartDate(sd.toDate());
        schedule.setStatus(SCHEDULED_STATUS);
        return notificationscheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule disableSchedule(Long id) {
        Schedule notificationSchedule = getScheduleById(id);
        notificationSchedule.setStatus("Disabled");

        return notificationscheduleRepository.save(notificationSchedule);
    }

    @Transactional
    public synchronized Schedule updateScheduleStatus(Schedule schedule) {
        return notificationscheduleRepository.saveAndFlush(schedule);
    }

    /**
     * This method take a cron expression from properties file and replace the hour,minute,day and month into the placeholder to
     * make dynamic cron expression.
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
            cronExpression = appProperties.getDailyJobCron().replace(MINUTES_CRON, String.valueOf(minutes));
            cronExpression = cronExpression.replace(HOURS_CRON, String.valueOf(hours));
            break;
        case "month":
            cronExpression = appProperties.getMonthlyJobCron().replace(MINUTES_CRON, String.valueOf(minutes));
            cronExpression = cronExpression.replace(HOURS_CRON, String.valueOf(hours));
            cronExpression = cronExpression.replace(DAY_CRON, String.valueOf(calendar.getDayOfMonth()));
            break;
        case "year":
            cronExpression = appProperties.getYearlyJobCron().replace(MINUTES_CRON, String.valueOf(minutes));
            cronExpression = cronExpression.replace(HOURS_CRON, String.valueOf(hours));
            cronExpression = cronExpression.replace(DAY_CRON, String.valueOf(calendar.getDayOfMonth()));
            cronExpression = cronExpression.replace(MONTH_CRON, String.valueOf(calendar.getMonthOfYear()));
            break;
        default:
            break;
        }
        return cronExpression;
    }

    /**
     * This method build the URL and call the rest API using the restTemplate and return an object.
     * @param contextURL
     * @param urlPath
     * @return
     */
    public List<UserTaxInformation> getDefaulterUserList(String contextURL, String urlPath) {
        final String uri = contextURL.concat(urlPath);
        ResponseEntity<UserTaxInformation[]> results = notificationConfiguration.getRestTemplate().getForEntity(uri,
                UserTaxInformation[].class);
        return Arrays.asList(results.getBody());
    }

    /**
     * This method is used to create a new job based on the newly created schedule.
     * @param schedule
     * @param fullURL
     */
    public void executeScheduler(Schedule schedule, String fullURL) {
        String cronExpression = getCronExpression(schedule);
        final JobDetailImpl jobDetail = (JobDetailImpl) beanProvider.getBean("eventnotificationJobDetail");
        final Scheduler scheduler = (Scheduler) beanProvider.getBean(BEANNOTIFSCH);
        try {
            jobDetail.setName(ApplicationThreadLocals.getTenantID().concat("_")
                    .concat(JOB.concat(String.valueOf(schedule.getId()))));
            jobDetail.getJobDataMap().put("scheduleId", String.valueOf(schedule.getId()));
            jobDetail.getJobDataMap().put("contextURL", fullURL.substring(0, StringUtils.ordinalIndexOf(fullURL, "/", 3)));

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
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * This method take a existing scheduler and remove it from schedule.
     * @param schedule
     */
    public void removeScheduler(Schedule schedule) {
        final Scheduler scheduler = (Scheduler) beanProvider.getBean(BEANNOTIFSCH);
        try {
            scheduler.unscheduleJob(new TriggerKey(ApplicationThreadLocals.getTenantID().concat("_")
                    .concat(TRIGGER.concat(String.valueOf(schedule.getId()))),
                    EVENT_NOTIFICATION_GROUP));
            scheduler.deleteJob(new JobKey(JOB.concat(String.valueOf(schedule.getId()))));
        } catch (final SchedulerException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * This method modify the existing scheduler. It's basically modifying the schedule time using the rescheduleJob method.
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
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
