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

import static org.egov.eventnotification.constants.Constants.ACTIVE;
import static org.egov.eventnotification.constants.Constants.DDMMYYYY;
import static org.egov.eventnotification.constants.Constants.EMPTY;
import static org.egov.eventnotification.constants.Constants.MAX_TEN;
import static org.egov.eventnotification.constants.Constants.MIN_NUMBER_OF_REQUESTS;
import static org.egov.eventnotification.constants.Constants.MODULE_NAME;
import static org.egov.eventnotification.constants.Constants.NOTIFICATION_TYPE_EVENT;
import static org.egov.eventnotification.constants.Constants.ZERO;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.eventnotification.repository.EventRepositoryImpl;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.DateUtils;
import org.egov.pushbox.application.entity.MessageContent;
import org.egov.pushbox.application.service.PushNotificationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is a service class. It is used for event related business logic like fetch the event using entity manager etc.
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
    private EventRepositoryImpl eventRepositoryImpl;

    /**
     * Fetch all the event in descending order
     * @return List<Event>
     */
    public List<Event> findAllEventByStatus(String status) {
        List<Event> eventList = null;
        eventList = eventRepository.findByStatusOrderByIdDesc(status);
        if (!eventList.isEmpty())
            for (Event event : eventList)
                populateEventDetails(event);
        return eventList;
    }

    /**
     * Fetch all the event in descending order
     * @return List<Event>
     */
    public List<Event> findAllOngoingEvent(String status) {
        List<Event> eventList = null;
        DateTime calendar = new DateTime();
        Date startDate;
        Date endDate;
        calendar = calendar.withHourOfDay(0);
        calendar = calendar.withMinuteOfHour(0);
        calendar = calendar.withSecondOfMinute(0);
        startDate = calendar.toDate();
        calendar = calendar.plusDays(7);
        endDate = calendar.toDate();
        eventList = eventRepository.findByStatusAndStartDateIsBetweenOrderByIdDesc(status,
                startDate, endDate);
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
        event.setEventDetails(eventDetails);
    }

    /**
     * This method fetch the event by id
     * @param id
     * @return Event
     */
    public Event findByEventId(Long id) {
        Event event = eventRepository.findOne(id);
        populateEventDetails(event);
        return event;
    }

    /**
     * This method create the event.
     * @param event
     * @return Event
     * @throws IOException
     * @throws ParseException
     */
    @Transactional
    public Event saveEvent(Event event) throws IOException {
        DateTime sd = new DateTime(event.getEventDetails().getStartDt());
        sd = sd.withHourOfDay(Integer.parseInt(event.getEventDetails().getStartHH()));
        sd = sd.withMinuteOfHour(Integer.parseInt(event.getEventDetails().getStartMM()));
        sd = sd.withSecondOfMinute(00);
        event.setStartDate(sd.toDate());

        DateTime ed = new DateTime(event.getEventDetails().getEndDt());
        ed = ed.withHourOfDay(Integer.parseInt(event.getEventDetails().getEndHH()));
        ed = ed.withMinuteOfHour(Integer.parseInt(event.getEventDetails().getEndMM()));
        ed = ed.withSecondOfMinute(00);
        event.setEndDate(ed.toDate());
        event.setStatus(ACTIVE.toUpperCase());

        if (event.getEventDetails().getFile() != null)
            eventUploadWallpaper(event);

        return eventRepository.save(event);
    }

    /**
     * This method is used to update the event
     * @param event
     * @param files
     * @return Event
     * @throws IOException
     * @throws ParseException
     */
    @Transactional
    public Event updateEvent(Event updatedEvent) throws IOException {
        if (updatedEvent.getEventDetails().getFile()[0].getSize() > MIN_NUMBER_OF_REQUESTS)
            eventUploadWallpaper(updatedEvent);
        return eventRepository.save(updatedEvent);
    }

    /**
     * This method is used to search the event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return List<Event>
     */
    public List<Event> searchEvent(Event eventObj, String eventDateType) {
        return eventRepositoryImpl.searchEvent(eventObj, eventDateType);
    }

    /**
     * This method is used to upload the file into filestore
     * @param event
     * @throws IOException
     */
    public void eventUploadWallpaper(Event event) throws IOException {

        for (MultipartFile multipartFile : event.getEventDetails().getFile())
            if (!multipartFile.isEmpty())
                event.setFilestore(
                        fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(), MODULE_NAME));

    }

    /**
     * This method is used to upload the file into filestore
     * @param event
     * @param existingEvent
     * @throws IOException
     */
    public void eventUploadWallpaper(Event existingEvent, Event event) throws IOException {

        for (MultipartFile multipartFile : event.getEventDetails().getFile())
            if (!multipartFile.isEmpty())
                existingEvent.setFilestore(
                        fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(), MODULE_NAME));

    }

    /**
     * Remove the file into filestore
     * @param event
     * @throws IOException
     */
    public void removeEventWallpaper(Event event) {

        fileStoreService.delete(String.valueOf(event.getFilestore()), MODULE_NAME);

    }

    /**
     * Create a push message and sent it to pushbox
     * @param event
     * @param user
     */
    public void sendPushMessage(Event event, User user) {

        DateTime calendar = new DateTime(event.getStartDate());
        DateTime calendarEnd = new DateTime(event.getEndDate());

        MessageContent messageContent = new MessageContent();
        messageContent.setCreatedDateTime(new Date().getTime());
        messageContent.setEventAddress(event.getAddress());
        messageContent.setEventDateTime(calendar.getMillis());
        messageContent.setEventLocation(event.getEventlocation());
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
        messageContent.setSendAll(Boolean.TRUE);
        if (event.getUrl() == null)
            messageContent.setUrl("");
        else
            messageContent.setUrl(event.getUrl());

        pushNotificationService.sendNotifications(messageContent);
    }
}
