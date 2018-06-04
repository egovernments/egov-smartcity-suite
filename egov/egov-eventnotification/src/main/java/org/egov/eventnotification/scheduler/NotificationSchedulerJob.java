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

import static org.egov.eventnotification.constants.Constants.BUSINESS_NOTIFICATION_TYPE;
import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.DEFAULTERS_LIST;
import static org.egov.eventnotification.constants.Constants.MESSAGE_ASMNTNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_BILLAMT;
import static org.egov.eventnotification.constants.Constants.MESSAGE_BILLNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_CONSNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_DISRPTDATE;
import static org.egov.eventnotification.constants.Constants.MESSAGE_DUEAMT;
import static org.egov.eventnotification.constants.Constants.MESSAGE_DUEDATE;
import static org.egov.eventnotification.constants.Constants.MESSAGE_PROPTNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_USERNAME;
import static org.egov.eventnotification.constants.Constants.PUSH_NOTIFICATION_SERVICE;
import static org.egov.eventnotification.constants.Constants.SCHEDULEID;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_COMPLETE;
import static org.egov.eventnotification.constants.Constants.USER_SERVICE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.ptis.domain.entity.property.DefaultersInfo;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.service.PushNotificationService;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author somvit
 *
 */
@Service
@DisallowConcurrentExecution
public class NotificationSchedulerJob implements Job {
    private static final Logger LOGGER = Logger.getLogger(NotificationSchedulerJob.class);

    private UserService userService;
    private PushNotificationService pushNotificationService;
    private SchedulerContext schedulerContext = null;

    @Override
    public void execute(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        LOGGER.info("Notification scheduler with job key " + jobKey + " executing at " + new Date());

        try {
            schedulerContext = context.getScheduler().getContext();
        } catch (SchedulerException e1) {
            LOGGER.error(e1.getMessage());
        }

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(
                ContextLoader.getCurrentWebApplicationContext().getServletContext());
        ScheduleService notificationscheduleService = (ScheduleService) springContext.getBean("scheduleService");
        pushNotificationService = (PushNotificationService) springContext
                .getBean(PUSH_NOTIFICATION_SERVICE);

        userService = (UserService) springContext.getBean(USER_SERVICE);

        Schedule notificationSchedule = notificationscheduleService
                .getScheduleById(Long.parseLong(String.valueOf(dataMap.get(SCHEDULEID))));

        executeBusiness(notificationSchedule, Long.parseLong(String.valueOf(dataMap.get("userid"))),
                String.valueOf(dataMap.get("username")));

        notificationSchedule.setStatus(SCHEDULE_COMPLETE);
        notificationscheduleService.updateScheduleStatus(notificationSchedule);

        LOGGER.info("Notification scheduler with job key " + jobKey + " end at " + new Date());
    }

    private void executeBusiness(Schedule notificationSchedule, Long usrid, String username) {
        DateTime calendar = new DateTime(notificationSchedule.getStartDate());
        int hours = calendar.getHourOfDay();
        int minutes = calendar.getMinuteOfHour();
        calendar.plusHours(hours);
        calendar.plusMinutes(minutes);

        DateTime calendarEnd = new DateTime(notificationSchedule.getStartDate());
        int hours1 = calendarEnd.getHourOfDay();
        int minutes1 = calendarEnd.getMinuteOfHour();
        calendarEnd.plusHours(hours1 + 1);
        calendarEnd.plusMinutes(minutes1);

        if (notificationSchedule.getDraftType().getName().equalsIgnoreCase(BUSINESS_NOTIFICATION_TYPE)) {

            List<DefaultersInfo> defaultersList = (List<DefaultersInfo>) schedulerContext.get(DEFAULTERS_LIST);
            if (!defaultersList.isEmpty()) {
                for (DefaultersInfo defaultersInfo : defaultersList) {

                    String message = buildMessage(defaultersInfo, notificationSchedule.getMessageTemplate());
                    notificationSchedule.setMessageTemplate(message);
                    List<User> userList = userService.findByMobileNumberAndType(defaultersInfo.getMobileNumber(),
                            UserType.CITIZEN);
                    List<Long> userIdList = new ArrayList<>();
                    if (userList != null) {
                        for (User userid : userList) {
                            userIdList.add(userid.getId());
                        }

                        LOGGER.info("List of User mobile Obtained : " + userIdList.size());
                    }

                    buildAndSendNotifications(notificationSchedule, calendar, calendarEnd, Boolean.FALSE, userIdList, usrid,
                            username);
                }
            }
        } else
            buildAndSendNotifications(notificationSchedule, calendar, calendarEnd, Boolean.TRUE, null, usrid, username);
    }

    private String buildMessage(DefaultersInfo defaultersInfo, String message) {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        if (message.contains(MESSAGE_USERNAME))
            message = message.replace(MESSAGE_USERNAME, defaultersInfo.getOwnerName());

        if (message.contains(MESSAGE_PROPTNO))
            message = message.replace(MESSAGE_PROPTNO, defaultersInfo.getAssessmentNo());

        if (message.contains(MESSAGE_DUEDATE))
            message = message.replace(MESSAGE_DUEDATE,
                    formatter.format(defaultersInfo.getMinDate()));

        if (message.contains(MESSAGE_ASMNTNO))
            message = message.replace(MESSAGE_ASMNTNO, defaultersInfo.getAssessmentNo());

        if (message.contains(MESSAGE_DUEAMT))
            message = message.replace(MESSAGE_DUEAMT,
                    String.valueOf(defaultersInfo.getCurrentDue().doubleValue()));

        if (message.contains(MESSAGE_CONSNO))
            message = message.replace(MESSAGE_CONSNO, defaultersInfo.getAssessmentNo());

        if (message.contains(MESSAGE_BILLNO))
            message = message.replace(MESSAGE_BILLNO, defaultersInfo.getAssessmentNo());

        if (message.contains(MESSAGE_BILLAMT))
            message = message.replace(MESSAGE_BILLAMT,
                    String.valueOf(defaultersInfo.getCurrentDue().doubleValue()));

        if (message.contains(MESSAGE_DISRPTDATE))
            message = message.replace(MESSAGE_DISRPTDATE, defaultersInfo.getAssessmentNo());
        return message;
    }

    private void buildAndSendNotifications(Schedule notificationSchedule, DateTime calendar, DateTime calendarEnd,
            Boolean seandAll, List<Long> userIdList, Long userid, String username) {
        MessageContent messageContent = new MessageContent();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageContent.setEventDateTime(calendar.getMillis());
        messageContent.setExpiryDate(calendarEnd.getMillis());
        messageContent.setMessageBody(notificationSchedule.getMessageTemplate());
        messageContent.setModuleName(notificationSchedule.getTemplateName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(notificationSchedule.getDraftType().getName());
        messageContent.setSendAll(seandAll);
        if (userIdList != null)
            messageContent.setUserIdList(userIdList);
        messageContent.setSenderId(userid);
        messageContent.setSenderName(username);

        pushNotificationService.sendNotifications(messageContent);
    }
}