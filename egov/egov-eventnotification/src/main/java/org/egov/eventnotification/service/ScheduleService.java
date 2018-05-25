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
package org.egov.eventnotification.service;

import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.MAX_TEN;
import static org.egov.eventnotification.constants.Constants.SCHEDULED_STATUS;
import static org.egov.eventnotification.constants.Constants.ZERO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.entity.NotificationSchedule;
import org.egov.eventnotification.repository.NotificationScheduleRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    @Autowired
    private NotificationScheduleRepository notificationscheduleRepository;

    /**
     * Fetch all the schedule by status
     * @return List<Notificationschedule>
     */
    public List<NotificationSchedule> findAllSchedule() {
        List<NotificationSchedule> notificationScheduleList = null;
        notificationScheduleList = notificationscheduleRepository.findByOrderByIdDesc();
        if (!notificationScheduleList.isEmpty())
            for (NotificationSchedule notificationSchedule : notificationScheduleList) {
                EventDetails eventDetails = new EventDetails();
                DateTime sd = new DateTime(notificationSchedule.getStartDate());
                eventDetails.setStartDt(
                        DateUtils.getDate(DateUtils.getDefaultFormattedDate(notificationSchedule.getStartDate()), DDMMYYYY));
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

    /**
     * Fetch schedule by id
     * @return Notificationschedule
     */
    public NotificationSchedule getSchedule(Long id) {
        NotificationSchedule notificationSchedule = notificationscheduleRepository.findOne(id);
        EventDetails eventDetails = new EventDetails();
        DateTime sd = new DateTime(notificationSchedule.getStartDate());
        eventDetails
                .setStartDt(DateUtils.getDate(DateUtils.getDefaultFormattedDate(notificationSchedule.getStartDate()), DDMMYYYY));
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

    /**
     * This method create the event.
     * @param event
     * @param files
     * @return Event
     * @throws IOException
     * @throws ParseException
     */
    @Transactional
    public NotificationSchedule saveSchedule(NotificationSchedule notificationSchedule, User user) {
        DateTime sd = new DateTime(notificationSchedule.getEventDetails().getStartDt());
        sd = sd.withHourOfDay(Integer.parseInt(notificationSchedule.getEventDetails().getStartHH()));
        sd = sd.withMinuteOfHour(Integer.parseInt(notificationSchedule.getEventDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        notificationSchedule.setStartDate(sd.toDate());
        notificationSchedule.setStatus(SCHEDULED_STATUS);
        return notificationscheduleRepository.save(notificationSchedule);
    }

    /**
     * Soft delete of schedule notification
     * @param id
     * @param user
     * @return Notificationschedule
     */
    @Transactional
    public NotificationSchedule updateSchedule(NotificationSchedule schedule) {
        return notificationscheduleRepository.save(schedule);
    }
}
