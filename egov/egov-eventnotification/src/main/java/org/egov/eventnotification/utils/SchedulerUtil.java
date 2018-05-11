package org.egov.eventnotification.utils;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerUtil {
    private static final Logger log = Logger.getLogger(SchedulerUtil.class);
    private static SchedulerUtil INSTANCE = new SchedulerUtil();
    private SchedulerFactory SF;
    private String name;
    private String triggerName;
    private String groupName;
    private int repeatCount;

    public static SchedulerUtil getSchedulerUtilInstance() {
        return INSTANCE;
    }

    private SchedulerUtil() {
        SF = new StdSchedulerFactory();
    }

    /**
     * Add a scheduling task
     * @param name
     * @param groupName
     * @param repeatCount
     * @param triggerName
     * @param triggerStartTime
     * @param task
     * @param cronExpression
     * @param param
     * @throws SchedulerException
     */
    public Trigger addSchedule(Date triggerStartTime, Class<? extends Job> task, JobDataMap param)
            throws SchedulerException {
        Scheduler sched = SF.getScheduler();
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

    /**
     * Add a scheduling task
     * @param name
     * @param groupName
     * @param triggerName
     * @param job
     * @param cronExpression cron
     * @throws SchedulerException
     */
    public Trigger addSchedule(Class<? extends Job> task, String cronExpression, JobDataMap param)
            throws SchedulerException {
        Scheduler sched = SF.getScheduler();
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

    /**
     * Update a scheduling task
     * @param name
     * @param groupName
     * @param triggerName
     * @param job
     * @param cronExpression cron
     * @throws SchedulerException
     */
    public Trigger updateSchedule(Class<? extends Job> task, String cronExpression, JobDataMap param, TriggerKey triggerKey)
            throws SchedulerException {
        Scheduler sched = SF.getScheduler();
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

    /**
     * Update a scheduling task
     * @param name
     * @param groupName
     * @param repeatCount
     * @param triggerName
     * @param triggerStartTime
     * @param task
     * @param cronExpression
     * @param param
     * @throws SchedulerException
     */
    public Trigger updateSchedule(Date triggerStartTime, Class<? extends Job> task, JobDataMap param, TriggerKey triggerKey)
            throws SchedulerException {
        Scheduler sched = SF.getScheduler();
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

    /**
     * Check the scheduler exists
     * @param name
     * @param groupName
     * @return
     * @throws SchedulerException
     */
    public boolean hasSchedule(String name, String groupName) throws SchedulerException {
        Scheduler scheduler = SF.getScheduler();
        if (scheduler == null)
            return false;
        return scheduler.checkExists(new JobKey(name, groupName));
    }

    /**
     * Remove the job
     * @param name
     * @param groupName
     * @throws SchedulerException
     */
    public boolean removeSchedule() throws SchedulerException {
        Scheduler scheduler = SF.getScheduler();
        boolean isTrigger = scheduler.unscheduleJob(new TriggerKey(triggerName, groupName));
        scheduler.deleteJob(new JobKey(name, groupName));
        return isTrigger;
    }

    /**
     * Shotdown all scheduler
     */
    public void shutdown() {
        try {
            Collection<Scheduler> allSchedulers = SF.getAllSchedulers();
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
