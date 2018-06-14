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
package org.egov.eventnotification.scheduler;

import static org.egov.eventnotification.constants.Constants.BUSINESS_NOTIFICATION_TYPE;
import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.MESSAGE_ASMNTNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_BILLAMT;
import static org.egov.eventnotification.constants.Constants.MESSAGE_BILLNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_CONSNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_DUEAMT;
import static org.egov.eventnotification.constants.Constants.MESSAGE_DUEDATE;
import static org.egov.eventnotification.constants.Constants.MESSAGE_PROPTNO;
import static org.egov.eventnotification.constants.Constants.MESSAGE_USERNAME;
import static org.egov.eventnotification.constants.Constants.PROPERTY_MODULE;
import static org.egov.eventnotification.constants.Constants.SCHEDULEID;
import static org.egov.eventnotification.constants.Constants.SCHEDULE_COMPLETE;
import static org.egov.eventnotification.constants.Constants.WATER_CHARGES_MODULE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eventnotification.config.properties.EventnotificationApplicationProperties;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.service.PushNotificationService;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author somvit
 *
 */
@DisallowConcurrentExecution
public class NotificationSchedulerJob extends AbstractQuartzJob {

    /**
     *
     */
    private static final long serialVersionUID = -7038270977924187739L;

    private static final Logger LOGGER = Logger.getLogger(NotificationSchedulerJob.class);

    @Autowired
    private transient UserService userService;

    @Autowired
    private transient PushNotificationService pushNotificationService;

    @Autowired
    private transient ScheduleService scheduleService;

    @Autowired
    private transient EventnotificationApplicationProperties appProperties;

    private Long scheduleId = null;
    private String contextURL = null;

    @Override
    protected void executeInternal(final JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        scheduleId = Long.parseLong(String.valueOf(dataMap.get(SCHEDULEID)));
        contextURL = String.valueOf(dataMap.get("contextURL"));
        try {
            super.executeInternal(context);
        } catch (JobExecutionException e) {
            LOGGER.error("Unable to complete execution Scheduler ", e);
        }
    }

    @Override
    public void executeJob() {
        try {
            Schedule notificationSchedule = scheduleService.getScheduleById(scheduleId);

            executeBusiness(notificationSchedule);

            notificationSchedule.setStatus(SCHEDULE_COMPLETE);
            scheduleService.updateScheduleStatus(notificationSchedule);
        } catch (RuntimeException e) {
            // Ignoring exception to avoid quartz to continue with other cities
            LOGGER.error("Error occurred while executing event notification schedule job.", e);
        }
    }

    private void executeBusiness(Schedule notificationSchedule) {

        if (notificationSchedule.getDraftType().getName().equalsIgnoreCase(BUSINESS_NOTIFICATION_TYPE)) {
            List<UserTaxInformation> userTaxInfoList = null;
            if (notificationSchedule.getModule().getName().equalsIgnoreCase(PROPERTY_MODULE))
                userTaxInfoList = scheduleService.getDefaulterUserList(contextURL, appProperties.getPropertytaxRestApi().concat("Water Tax Management"));
            else if (notificationSchedule.getModule().getName().equalsIgnoreCase(WATER_CHARGES_MODULE))
                userTaxInfoList = scheduleService.getDefaulterUserList(contextURL,
                        appProperties.getWatertaxRestApi().concat("Water Tax Management"));
            if (userTaxInfoList != null)
                for (UserTaxInformation userTaxInformation : userTaxInfoList) {
                    String message = buildMessage(userTaxInformation, notificationSchedule.getMessageTemplate());

                    List<Long> userIdList = new ArrayList<>();
                    userIdList.add(Long.parseLong(userTaxInformation.getUserId()));

                    buildAndSendNotifications(notificationSchedule, message, Boolean.FALSE, userIdList);
                }
        } else
            buildAndSendNotifications(notificationSchedule, null, Boolean.TRUE, null);
    }

    private String buildMessage(UserTaxInformation userTaxInformation, String message) {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        User user = userService.getUserById(Long.parseLong(userTaxInformation.getUserId()));
        if (message.contains(MESSAGE_USERNAME))
            message = message.replace(MESSAGE_USERNAME, user.getName());

        if (message.contains(MESSAGE_PROPTNO))
            message = message.replace(MESSAGE_PROPTNO, userTaxInformation.getConsumerNumber());

        if (message.contains(MESSAGE_DUEDATE) && userTaxInformation.getDueDate()!= null)
            message = message.replace(MESSAGE_DUEDATE, formatter.format(userTaxInformation.getDueDate()));

        if (message.contains(MESSAGE_ASMNTNO))
            message = message.replace(MESSAGE_ASMNTNO, userTaxInformation.getConsumerNumber());

        if (message.contains(MESSAGE_DUEAMT))
            message = message.replace(MESSAGE_DUEAMT, userTaxInformation.getDueAmount());

        if (message.contains(MESSAGE_CONSNO))
            message = message.replace(MESSAGE_CONSNO, userTaxInformation.getConsumerNumber());

        if (message.contains(MESSAGE_BILLNO))
            message = message.replace(MESSAGE_BILLNO, userTaxInformation.getBillNo());

        if (message.contains(MESSAGE_BILLAMT))
            message = message.replace(MESSAGE_BILLAMT, userTaxInformation.getDueAmount());

        return message;
    }

    private void buildAndSendNotifications(Schedule notificationSchedule, String messageBody, Boolean seandAll,
            List<Long> userIdList) {
        User user = userService.getCurrentUser();
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

        MessageContent messageContent = new MessageContent();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageContent.setEventDateTime(calendar.getMillis());
        messageContent.setExpiryDate(calendarEnd.getMillis());
        if (messageBody == null)
            messageContent.setMessageBody(notificationSchedule.getMessageTemplate());
        else
            messageContent.setMessageBody(messageBody);
        messageContent.setModuleName(notificationSchedule.getTemplateName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(notificationSchedule.getDraftType().getName());
        messageContent.setSendAll(seandAll);
        if (userIdList != null)
            messageContent.setUserIdList(userIdList);
        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());

        pushNotificationService.sendNotifications(messageContent);
    }
}