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

import static org.egov.eventnotification.utils.constants.Constants.ACTIVE;
import static org.egov.eventnotification.utils.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.utils.constants.Constants.EMPTY;
import static org.egov.eventnotification.utils.constants.Constants.MAX_TEN;
import static org.egov.eventnotification.utils.constants.Constants.MIN_NUMBER_OF_REQUESTS;
import static org.egov.eventnotification.utils.constants.Constants.MODULE_NAME;
import static org.egov.eventnotification.utils.constants.Constants.NO;
import static org.egov.eventnotification.utils.constants.Constants.NOTIFICATION_TYPE_EVENT;
import static org.egov.eventnotification.utils.constants.Constants.YES;
import static org.egov.eventnotification.utils.constants.Constants.ZERO;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.contracts.EventDetails;
import org.egov.eventnotification.entity.contracts.EventSearch;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.DateUtils;
import org.egov.pushbox.entity.contracts.MessageContent;
import org.egov.pushbox.entity.contracts.MessageContentDetails;
import org.egov.pushbox.service.PushNotificationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author somvit
 *
 */
@Service
@Transactional(readOnly = true)
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private UserService userService;

    public List<Event> getAllEventByStatus(String status) {
        List<Event> eventList = null;
        eventList = eventRepository.findByStatusAndEndDateGreaterThanOrderByIdDesc(status, DateUtils.today());
        if (!eventList.isEmpty())
            for (Event event : eventList)
                populateEventDetails(event);
        return eventList;
    }

    public List<Event> getAllOngoingEvent(String status) {
        List<Event> eventList = null;
        Date startDate;
        Date endDate;
        startDate = DateUtils.startOfToday().toDate();
        endDate = DateUtils.endOfToday().plusDays(6).toDate();
        eventList = eventRepository.findByStatusAndStartDateIsBetweenAndEndDateGreaterThanOrderByIdDesc(status,
                startDate, endDate, DateUtils.today());
        if (!eventList.isEmpty())
            for (Event event : eventList)
                populateEventDetails(event);
        return eventList;
    }

    private void populateEventDetails(Event event) {
        EventDetails eventDetails = new EventDetails();
        DateTime sd = new DateTime(event.getStartDate());
        eventDetails.setStartDt(DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getStartDate()), DDMMYYYY));
        if (sd.getHourOfDay() < MAX_TEN)
            eventDetails.setStartHH(ZERO + String.valueOf(sd.getHourOfDay()));
        else
            eventDetails.setStartHH(String.valueOf(sd.getHourOfDay()));
        if (sd.getMinuteOfHour() < MAX_TEN)
            eventDetails.setStartMM(ZERO + String.valueOf(sd.getMinuteOfHour()));
        else
            eventDetails.setStartMM(String.valueOf(sd.getMinuteOfHour()));

        DateTime ed = new DateTime(event.getEndDate());
        eventDetails.setEndDt(DateUtils.getDate(DateUtils.getDefaultFormattedDate(event.getEndDate()), DDMMYYYY));
        if (ed.getHourOfDay() < MAX_TEN)
            eventDetails.setEndHH(ZERO + String.valueOf(ed.getHourOfDay()));
        else
            eventDetails.setEndHH(String.valueOf(ed.getHourOfDay()));
        if (ed.getMinuteOfHour() < MAX_TEN)
            eventDetails.setEndMM(ZERO + String.valueOf(ed.getMinuteOfHour()));
        else
            eventDetails.setEndMM(String.valueOf(ed.getMinuteOfHour()));

        if (event.isPaid())
            eventDetails.setPaid(YES);
        else
            eventDetails.setPaid(NO);
        event.setEventDetails(eventDetails);
    }

    public Event getEventById(Long id) {
        Event event = eventRepository.findOne(id);
        populateEventDetails(event);
        return event;
    }

    @Transactional
    public Event saveEvent(Event event) {
        try {
            DateTime sd = DateUtils.startOfGivenDate(new DateTime(event.getEventDetails().getStartDt()))
                    .withHourOfDay(Integer.parseInt(event.getEventDetails().getStartHH()))
                    .withMinuteOfHour(Integer.parseInt(event.getEventDetails().getStartMM()));
            event.setStartDate(sd.toDate());

            DateTime ed = DateUtils.startOfGivenDate(new DateTime(event.getEventDetails().getEndDt()))
                    .withHourOfDay(Integer.parseInt(event.getEventDetails().getEndHH()))
                    .withMinuteOfHour(Integer.parseInt(event.getEventDetails().getEndMM()));
            event.setEndDate(ed.toDate());
            event.setStatus(ACTIVE.toUpperCase());

            if (event.getEventDetails().getFile() != null)
                eventUploadWallpaper(event);

            eventRepository.saveAndFlush(event);
            sendPushMessage(event);

            return event;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public Event updateEvent(Event updatedEvent) {
        try {
            DateTime sd = DateUtils.startOfGivenDate(new DateTime(updatedEvent.getEventDetails().getStartDt()))
                    .withHourOfDay(Integer.parseInt(updatedEvent.getEventDetails().getStartHH()))
                    .withMinuteOfHour(Integer.parseInt(updatedEvent.getEventDetails().getStartMM()));
            updatedEvent.setStartDate(sd.toDate());

            DateTime ed = DateUtils.startOfGivenDate(new DateTime(updatedEvent.getEventDetails().getEndDt()))
                    .withHourOfDay(Integer.parseInt(updatedEvent.getEventDetails().getEndHH()))
                    .withMinuteOfHour(Integer.parseInt(updatedEvent.getEventDetails().getEndMM()));
            updatedEvent.setEndDate(ed.toDate());

            if (updatedEvent.getEventDetails().getFile()[0].getSize() > MIN_NUMBER_OF_REQUESTS)
                eventUploadWallpaper(updatedEvent);
            return eventRepository.save(updatedEvent);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    public List<Event> searchEvent(EventSearch eventSearch) {
        return eventRepository.searchEvent(eventSearch);
    }

    public void eventUploadWallpaper(Event event) {
        try {
            for (MultipartFile multipartFile : event.getEventDetails().getFile())
                if (!multipartFile.isEmpty())
                    event.setFilestore(
                            fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                    multipartFile.getContentType(), MODULE_NAME));
        } catch (final IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }

    }

    public void eventUploadWallpaper(Event existingEvent, Event event) {
        try {
            for (MultipartFile multipartFile : event.getEventDetails().getFile())
                if (!multipartFile.isEmpty())
                    existingEvent.setFilestore(
                            fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                    multipartFile.getContentType(), MODULE_NAME));
        } catch (final IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }

    }

    public void removeEventWallpaper(Event event) {
        fileStoreService.delete(String.valueOf(event.getFilestore()), MODULE_NAME);
    }

    private void sendPushMessage(Event event) {
        User user = userService.getCurrentUser();
        DateTime calendar = new DateTime(event.getStartDate());
        DateTime calendarEnd = new DateTime(event.getEndDate());

        MessageContent messageContent = new MessageContent();
        MessageContentDetails messageDetails = new MessageContentDetails();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageDetails.setEventAddress(event.getEventAddress().getAddress());
        messageDetails.setEventDateTime(calendar.getMillis());
        messageDetails.setEventLocation(event.getEventAddress().getEventLocation());
        messageContent.setExpiryDate(calendarEnd.getMillis());
        if (event.getFilestore() == null)
            messageContent.setImageUrl(EMPTY);
        else
            messageContent.setImageUrl(String.valueOf(event.getFilestore()));
        messageContent.setMessageBody(event.getMessage());
        messageContent.setMessageId(event.getId());
        messageContent.setModuleName(event.getName());
        messageContent.setNotificationDateTime(new Date().getTime());
        messageContent.setNotificationType(NOTIFICATION_TYPE_EVENT);
        messageContent.setSenderId(user.getId());
        messageContent.setSenderName(user.getName());
        messageDetails.setSendAll(Boolean.TRUE);
        if (event.getEventAddress().getUrl() == null)
            messageContent.setUrl(EMPTY);
        else
            messageContent.setUrl(event.getEventAddress().getUrl());

        messageContent.setDetails(messageDetails);
        pushNotificationService.sendNotifications(messageContent);
    }
}