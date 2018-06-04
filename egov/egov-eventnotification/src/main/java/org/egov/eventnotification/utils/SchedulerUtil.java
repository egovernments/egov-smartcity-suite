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
package org.egov.eventnotification.utils;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.eventnotification.config.NotificationSchedulerConfiguration;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerUtil {
    private static final Logger log = Logger.getLogger(SchedulerUtil.class);

    @Autowired
    private NotificationSchedulerConfiguration notificationSchedulerConfiguration;
    private String name;
    private String triggerName;
    private String groupName;
    private int repeatCount;

    public Scheduler getScheduler() {
        Scheduler sched = null;
        try {
            sched = notificationSchedulerConfiguration.springBeanSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return sched;
    }

    public Trigger addSchedule(Date triggerStartTime, Class<? extends Job> task, JobDataMap param)
            throws SchedulerException {
        Scheduler sched = getScheduler();
        log.info("IS clusterd" + sched.getMetaData().isJobStoreClustered());
        JobBuilder builder = JobBuilder.newJob(task);
        builder.withIdentity(name, groupName);
        if (param != null)
            builder.usingJobData(param);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName).startAt(triggerStartTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)).build();
        sched.scheduleJob(builder.build(), trigger);
        if (!sched.isShutdown())
            sched.start();
        return trigger;
    }

    public Trigger addSchedule(Class<? extends Job> task, String cronExpression, JobDataMap param)
            throws SchedulerException {
        Scheduler sched = getScheduler();
        JobBuilder builder = JobBuilder.newJob(task);
        builder.withIdentity(name, groupName);
        if (param != null)
            builder.usingJobData(param);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
        sched.scheduleJob(builder.build(), trigger);
        if (!sched.isShutdown())
            sched.start();
        return trigger;
    }

    public Trigger updateSchedule(Class<? extends Job> task, String cronExpression, JobDataMap param, TriggerKey triggerKey)
            throws SchedulerException {
        Scheduler sched = getScheduler();
        JobBuilder builder = JobBuilder.newJob(task);
        builder.withIdentity(name, groupName);
        if (param != null)
            builder.usingJobData(param);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
        sched.addJob(builder.build(), true, true);
        sched.rescheduleJob(triggerKey, trigger);
        if (!sched.isShutdown())
            sched.start();
        return trigger;
    }

    public Trigger updateSchedule(Date triggerStartTime, Class<? extends Job> task, JobDataMap param, TriggerKey triggerKey)
            throws SchedulerException {
        Scheduler sched = getScheduler();
        JobBuilder builder = JobBuilder.newJob(task);
        builder.withIdentity(name, groupName);
        if (param != null)
            builder.usingJobData(param);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName).startAt(triggerStartTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(repeatCount)).build();
        sched.addJob(builder.build(), true, true);
        sched.rescheduleJob(triggerKey, trigger);
        if (!sched.isShutdown())
            sched.start();
        return trigger;
    }

    public boolean hasSchedule(String name, String groupName) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (scheduler == null)
            return false;
        return scheduler.checkExists(new JobKey(name, groupName));
    }

    public boolean removeSchedule() throws SchedulerException {
        Scheduler scheduler = getScheduler();
        boolean isTrigger = scheduler.unscheduleJob(new TriggerKey(triggerName, groupName));
        scheduler.deleteJob(new JobKey(name, groupName));
        return isTrigger;
    }

    public void shutdown() {
        try {
            Collection<Scheduler> allSchedulers = notificationSchedulerConfiguration.springBeanSchedulerFactory()
                    .getAllSchedulers();
            for (Scheduler s : allSchedulers)
                s.shutdown();
        } catch (SchedulerException e) {
            log.error("shut down error!", e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }
}