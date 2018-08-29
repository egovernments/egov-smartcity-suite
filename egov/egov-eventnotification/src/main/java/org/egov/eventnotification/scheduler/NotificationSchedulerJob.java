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

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.integration.bmi.BuildMessageAdapter;
import org.egov.eventnotification.service.ScheduleService;
import org.egov.eventnotification.utils.EventNotificationUtil;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.CityPreferences;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.entity.contracts.MessageContentDetails;
import org.egov.pushbox.service.PushNotificationService;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author somvit
 *
 */
@DisallowConcurrentExecution
public class NotificationSchedulerJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationSchedulerJob.class);

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private EventNotificationUtil eventNotificationUtil;

    @Resource(name = "cities")
    protected List<String> cities;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    private String userName;

    protected String moduleName;

    private boolean cityDataRequired;

    @Override
    protected void executeInternal(final JobExecutionContext jobCtx) throws JobExecutionException {
        JobDataMap dataMap = jobCtx.getJobDetail().getJobDataMap();
        try {
            MDC.put("appname", String.format("%s-%s", moduleName, jobCtx.getJobDetail().getKey().getName()));
            for (String tenant : cities) {
                MDC.put("ulbcode", tenant);
                try {
                    prepareThreadLocal(tenant);

                    Long scheduleId = Long.parseLong(String.valueOf(dataMap.get("scheduleId")));

                    Schedule notificationSchedule = scheduleService.getScheduleById(scheduleId);

                    executeBusiness(notificationSchedule, String.valueOf(dataMap.get("contextURL")),
                            ApplicationThreadLocals.getCityCode());

                    notificationSchedule.setStatus("Complete");
                    scheduleService.updateScheduleStatus(notificationSchedule);
                } catch (Exception ex) {
                    // Ignoring and logging exception since exception will cause multi city scheduler to fail for all remaining
                    // cities.
                    LOGGER.error("Unable to complete execution Scheduler ", ex);
                }
            }
        } finally {
            ApplicationThreadLocals.clearValues();
            MDC.clear();
        }
    }

    /**
     * This method check if it is BUSINESS type notification then one object will be created for BuildMessageAdapter. Then based
     * on the specific module call the rest api. Which will return the data. Then it will iterate and build a message and send it
     * to pushbox to send the notification.
     * @param notificationSchedule
     */
    private void executeBusiness(Schedule notificationSchedule, String contextURL, String ulbCode) {
        if (notificationSchedule.getDraftType().getName().equalsIgnoreCase("BUSINESS")) {
            List<UserTaxInformation> userTaxInfoList = null;
            BuildMessageAdapter buildMessageAdapter = getBuildMessageAdapter(notificationSchedule.getCategory().getCode());

            userTaxInfoList = scheduleService.getDefaulterUserList(contextURL.concat(notificationSchedule.getUrl()),
                    notificationSchedule.getMethod(), ulbCode);

            if (userTaxInfoList != null)
                for (UserTaxInformation userTaxInformation : userTaxInfoList) {
                    String message = buildMessageAdapter.buildMessage(userTaxInformation,
                            notificationSchedule.getMessageTemplate());
                    List<Long> userIdList = new ArrayList<>();
                    if (isNotBlank(userTaxInformation.getMobileNumber())) {
                        List<User> userList = userService.findByMobileNumberAndType(userTaxInformation.getMobileNumber(),
                                UserType.CITIZEN);
                        if (userList != null)
                            for (User userid : userList)
                                userIdList.add(userid.getId());
                    } else
                        userIdList.add(userTaxInformation.getUserId());

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
        messageContent.setCityName(ApplicationThreadLocals.getCityName());
        messageContent.setUlbCode(ApplicationThreadLocals.getCityCode());

        pushNotificationService.sendNotifications(messageContent);
    }

    protected BuildMessageAdapter getBuildMessageAdapter(final String serviceCode) {
        return (BuildMessageAdapter) eventNotificationUtil.getBean(serviceCode.concat("BuildMessageAdapter"));
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public void setUserName(String userName) {
        this.userName = defaultIfBlank(userName, "system");
    }

    public void setCityDataRequired(boolean cityDataRequired) {
        this.cityDataRequired = cityDataRequired;
    }

    private void prepareThreadLocal(String tenant) {
        ApplicationThreadLocals.setTenantID(tenant);
        ApplicationThreadLocals.setUserId(userService.getUserByUsername(userName).getId());
        if (cityDataRequired) {
            City city = cityService.findAll().get(0);
            ApplicationThreadLocals.setCityCode(city.getCode());
            ApplicationThreadLocals.setCityName(city.getName());
            CityPreferences cityPreferences = city.getPreferences();
            if (cityPreferences == null)
                LOGGER.warn("City preferences not set for {}", city.getName());
            else
                ApplicationThreadLocals.setMunicipalityName(cityPreferences.getMunicipalityName());
            ApplicationThreadLocals.setDomainName(city.getDomainURL());
        }
    }
}