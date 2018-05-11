package org.egov.eventnotification.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.infra.admin.master.entity.User;
import org.egov.pushbox.application.entity.MessageContent;
import org.egov.pushbox.application.service.PushNotificationService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

@DisallowConcurrentExecution
public class OneTimeNotificationSchedulerJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(NotificationSchedulerJob.class);

    /**
     * This calls the push box api for to send push notification.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        JobKey jobKey = context.getJobDetail().getKey();
        LOGGER.info("One time Job of Notification Scheduler with Job Key : " + jobKey + " executing at " + new Date());

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(
                ContextLoader.getCurrentWebApplicationContext().getServletContext());
        ScheduleService notificationscheduleService = (ScheduleService) springContext.getBean("scheduleService");
        PushNotificationService pushNotificationService = (PushNotificationService) springContext
                .getBean("pushNotificationService");
        User user = (User) dataMap.get(EventnotificationConstant.USER);

        NotificationSchedule notificationSchedule = notificationscheduleService
                .findOne(Long.parseLong(String.valueOf(dataMap.get(EventnotificationConstant.SCHEDULEID))));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(notificationSchedule.getStartDate()));
        int hours = Integer.parseInt(notificationSchedule.getStartTime().split(":")[0]);
        int minutes = Integer.parseInt(notificationSchedule.getStartTime().split(":")[1]);
        calendar.set(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, minutes);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(new Date(notificationSchedule.getStartDate()));
        int hours1 = Integer.parseInt(notificationSchedule.getStartTime().split(":")[0]);
        int minutes1 = Integer.parseInt(notificationSchedule.getStartTime().split(":")[1]);
        calendarEnd.set(Calendar.HOUR, hours1 + 1);
        calendarEnd.set(Calendar.MINUTE, minutes1);

        // Todo: Need Json or Sql to fetch all the user information to create notice type message

        MessageContent messageContent = new MessageContent();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageContent.setEventDateTime(calendar.getTimeInMillis());
        messageContent.setExpiryDate(calendarEnd.getTimeInMillis());
        messageContent.setMessageBody(notificationSchedule.getMessageTemplate());
        messageContent.setModuleName(notificationSchedule.getTemplatename());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(notificationSchedule.getNotificationType());
        messageContent.setSendAll(Boolean.TRUE);

        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());

        pushNotificationService.sendNotifications(messageContent);
        
        LOGGER.info("One time Job of Notification Scheduler with Job Key : " + jobKey + " finished at " + new Date());
    }
}
