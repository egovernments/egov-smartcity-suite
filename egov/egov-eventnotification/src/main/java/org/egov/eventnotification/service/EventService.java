package org.egov.eventnotification.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.EventDetails;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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

    private static final Logger LOGGER = Logger.getLogger(EventService.class);
    private final EventRepository eventRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;

    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Fetch all the event in descending order
     * @return List<Event>
     */
    public List<Event> findAll(Date startDate, String status) {
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        List<Event> eventList = null;
        try {
            eventList = eventRepository.getAllEventsByDate(formatter.parse(formatter.format(startDate)).getTime(), status);
            if (eventList != null)
                for (Event event : eventList) {
                    EventDetails eventDetails = new EventDetails();
                    Date sd = new Date(event.getStartDate());
                    eventDetails.setStartDt(formatter.parse(formatter.format(sd)));
                    Date ed = new Date(event.getEndDate());
                    eventDetails.setEndDt(formatter.parse(formatter.format(ed)));
                    event.setEventDetails(eventDetails);
                }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return eventList;
    }

    /**
     * This method fetch the event by id
     * @param id
     * @return Event
     */
    public Event findById(Long id) {
        return eventRepository.findOne(id);
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
    public Event persist(Event event)
            throws IOException, ParseException {
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
    public Event update(Event event)
            throws IOException, ParseException {
        Event existingEvent = findById(event.getId());
        if (existingEvent != null) {
            existingEvent.setAddress(event.getAddress());
            existingEvent.setCost(event.getCost());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setEndDate(event.getEndDate());
            existingEvent.setEndTime(event.getEndTime());
            existingEvent.setEventhost(event.getEventhost());
            existingEvent.setEventlocation(event.getEventlocation());
            existingEvent.setEventType(event.getEventType());
            existingEvent.setIspaid(event.getIspaid());
            existingEvent.setName(event.getName());
            existingEvent.setStartDate(event.getStartDate());
            existingEvent.setStartTime(event.getStartTime());
            existingEvent.setVersion(existingEvent.getVersion() + 1);
            existingEvent.setStatus(event.getStatus());
            existingEvent.setUrl(event.getUrl());
            existingEvent.setMessage(event.getMessage());
            if (event.getEventDetails().getFile() != null)
                if (existingEvent.getFilestore() != null) {
                    removeEventWallpaper(existingEvent);
                    eventUploadWallpaper(existingEvent, event);
                } else
                    eventUploadWallpaper(existingEvent, event);
        }

        return existingEvent;
    }

    /**
     * This method is used to search the event.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return List<Event>
     */
    public List<Event> searchEvent(String eventType, String eventName, String eventHost, Date startDate, String status) {
        DateFormat formatter = new SimpleDateFormat(EventnotificationConstant.DDMMYYYY);
        Criteria criteria = getCurrentSession().createCriteria(Event.class);
        if (eventType != null)
            criteria.add(Restrictions.ilike(EventnotificationConstant.EVENT_EVENTTYPE, eventType + "%"));
        if (eventName != null)
            criteria.add(Restrictions.ilike(EventnotificationConstant.EVENT_NAME, eventName + "%"));
        if (eventHost != null)
            criteria.add(Restrictions.ilike(EventnotificationConstant.EVENT_HOST, eventHost + "%"));

        try {
            criteria.add(Restrictions.ge(EventnotificationConstant.EVENT_STARTDATE,
                    formatter.parse(formatter.format(startDate)).getTime()));
            criteria.add(Restrictions.ge(EventnotificationConstant.EVENT_ENDDATE,
                    formatter.parse(formatter.format(startDate)).getTime()));
            criteria.add(Restrictions.eq(EventnotificationConstant.STATUS_COLUMN, status));
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }

        criteria.addOrder(Order.desc(EventnotificationConstant.EVENT_ID));
        return criteria.list();
    }

    public void eventUploadWallpaper(Event event) throws IOException {

        for (MultipartFile multipartFile : event.getEventDetails().getFile())
            if (!multipartFile.isEmpty())
                event.setFilestore(
                        fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(), EventnotificationConstant.MODULE_NAME));

    }

    public void eventUploadWallpaper(Event existingEvent, Event event) throws IOException {

        for (MultipartFile multipartFile : event.getEventDetails().getFile())
            if (!multipartFile.isEmpty())
                existingEvent.setFilestore(
                        fileStoreService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(), EventnotificationConstant.MODULE_NAME));

    }

    public void removeEventWallpaper(Event event) throws IOException {

        fileStoreService.delete(event.getFilestore().getFileStoreId(), EventnotificationConstant.MODULE_NAME);

    }
}
