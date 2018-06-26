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

import static org.egov.eventnotification.constants.ConstantsHelper.BMA_INTERFACE_SUFFIX;
import static org.egov.eventnotification.constants.ConstantsHelper.BUSINESS_NOTIFICATION_TYPE;
import static org.egov.eventnotification.constants.ConstantsHelper.PROPERTY_MODULE;
import static org.egov.eventnotification.constants.ConstantsHelper.SCHEDULEID;
import static org.egov.eventnotification.constants.ConstantsHelper.SCHEDULE_COMPLETE;
import static org.egov.eventnotification.constants.ConstantsHelper.WATER_CHARGES_MODULE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.eventnotification.config.properties.EventnotificationApplicationProperties;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.integration.bmi.BuildMessageAdapter;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.eventnotification.utils.EventnotificationUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.scheduler.quartz.AbstractQuartzJob;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.entity.contracts.MessageContentDetails;
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

    @Autowired
    private transient EventnotificationUtil eventnotificationUtil;

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

    /**
     * This method check if it is BUSINESS type notification then one object will be created for BuildMessageAdapter. Then based
     * on the specific module call the rest api. Which will return the user information. Then it will iterate and build a message
     * and send it to pushbox to send the notification.
     * @param notificationSchedule
     */
    private void executeBusiness(Schedule notificationSchedule) {

        if (notificationSchedule.getDraftType().getName().equalsIgnoreCase(BUSINESS_NOTIFICATION_TYPE)) {
            List<UserTaxInformation> userTaxInfoList = null;
            BuildMessageAdapter buildMessageAdapter = getBuildMessageAdapter(notificationSchedule.getModule().getCode());

            if (notificationSchedule.getModule().getName().equalsIgnoreCase(PROPERTY_MODULE))
                userTaxInfoList = scheduleService.getDefaulterUserList(contextURL,
                        appProperties.getPropertytaxRestApi());
            else if (notificationSchedule.getModule().getName().equalsIgnoreCase(WATER_CHARGES_MODULE))
                userTaxInfoList = scheduleService.getDefaulterUserList(contextURL,
                        appProperties.getWatertaxRestApi());
            if (userTaxInfoList != null)
                for (UserTaxInformation userTaxInformation : userTaxInfoList) {
                    String message = buildMessageAdapter.buildMessage(userTaxInformation,
                            notificationSchedule.getMessageTemplate());
                    List<Long> userIdList = new ArrayList<>();
                    userIdList.add(Long.parseLong(userTaxInformation.getUserId()));

                    buildAndSendNotifications(notificationSchedule, message, Boolean.FALSE, userIdList);
                }
        } else
            buildAndSendNotifications(notificationSchedule, null, Boolean.TRUE, null);
    }

    /**
     * This method build the messageContent object and send it to pushbox to send the notification.
     * @param notificationSchedule
     * @param messageBody
     * @param seandAll
     * @param userIdList
     */
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
        MessageContentDetails messageDetails = new MessageContentDetails();

        messageContent.setCreatedDateTime(new Date().getTime());
        messageDetails.setEventDateTime(calendar.getMillis());
        messageContent.setExpiryDate(calendarEnd.getMillis());
        if (messageBody == null)
            messageContent.setMessageBody(notificationSchedule.getMessageTemplate());
        else
            messageContent.setMessageBody(messageBody);
        messageContent.setModuleName(notificationSchedule.getTemplateName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(notificationSchedule.getDraftType().getName());
        messageDetails.setSendAll(seandAll);
        if (userIdList != null)
            messageDetails.setUserIdList(userIdList);
        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());
        messageContent.setDetails(messageDetails);

        pushNotificationService.sendNotifications(messageContent);
    }

    protected BuildMessageAdapter getBuildMessageAdapter(final String serviceCode) {
        return (BuildMessageAdapter) eventnotificationUtil.getBean(serviceCode
                + BMA_INTERFACE_SUFFIX);
    }
}