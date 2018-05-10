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
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This API is for daily job. Which will execute daily notification.
 * @author somvit
 *
 */
@Service
@DisallowConcurrentExecution
public class NotificationSchedulerJob implements Job {
    private static final Logger LOGGER = Logger.getLogger(NotificationSchedulerJob.class);

    /**
     * This calls the push box api for to send push notification.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        JobKey jobKey = context.getJobDetail().getKey();
        LOGGER.info("Daily Job of Notification Scheduler with Job Key : " + jobKey + " executing at " + new Date());

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
    }
}
