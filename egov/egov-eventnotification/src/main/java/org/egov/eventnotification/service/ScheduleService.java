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

import static org.egov.eventnotification.constants.ConstantsHelper.DAY_CRON;
import static org.egov.eventnotification.constants.ConstantsHelper.DDMMYYYY;
import static org.egov.eventnotification.constants.ConstantsHelper.HOURS_CRON;
import static org.egov.eventnotification.constants.ConstantsHelper.MINUTES_CRON;
import static org.egov.eventnotification.constants.ConstantsHelper.MONTH_CRON;
import static org.egov.eventnotification.constants.ConstantsHelper.SCHEDULED_STATUS;
import static org.egov.eventnotification.constants.ConstantsHelper.ZERO;

import java.util.Arrays;
import java.util.List;

import org.egov.eventnotification.config.EventNotificationConfiguration;
import org.egov.eventnotification.config.properties.EventnotificationApplicationProperties;
import org.egov.eventnotification.entity.Schedule;
import org.egov.eventnotification.entity.contracts.EventDetails;
import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.repository.ScheduleRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleService {
    private static final int MAX_TEN = 10;

    @Autowired
    private ScheduleRepository notificationscheduleRepository;

    @Autowired
    private EventnotificationApplicationProperties appProperties;

    @Autowired
    private EventNotificationConfiguration notificationConfiguration;

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
    public Schedule saveSchedule(Schedule notificationSchedule, User user) {
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
        return notificationscheduleRepository.save(schedule);
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
    public String getCronExpression(Schedule notificationschedule) {
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
}
