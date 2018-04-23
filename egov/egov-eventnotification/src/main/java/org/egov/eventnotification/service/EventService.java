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
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class EventService implements EventnotificationConstant {

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
    public List<Event> findAll(Date startDate) {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        List<Event> eventList = null;
        try {
        eventList = eventRepository.getAllEventsByDate(formatter.parse(formatter.format(startDate)).getTime());
        if(eventList != null) {
            for(Event event : eventList) {
                
                    Date sd=new Date(event.getStartDate());
                    event.setStartDt(formatter.parse(formatter.format(sd)));
                    Date ed=new Date(event.getEndDate());
                    event.setEndDt(formatter.parse(formatter.format(ed)));
                
            }
        }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    public Event persist(final Event event, final MultipartFile[] files)
            throws IOException, ParseException {
        if (files != null)
            eventUploadWallpaper(event, files);
        Event savedEvent = eventRepository.save(event);

        return savedEvent;
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
    public Event update(Event event, MultipartFile[] files)
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
            if (files != null)
                if (existingEvent.getFilestore() != null) {
                    removeEventWallpaper(existingEvent);
                    eventUploadWallpaper(existingEvent, files);
                } else
                    eventUploadWallpaper(existingEvent, files);
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
    public List<Event> searchEvent(String eventType, String eventName, String eventHost,Date startDate) {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        Criteria criteria = getCurrentSession().createCriteria(Event.class);
        if (eventType != null)
            criteria.add(Restrictions.ilike(EVENT_EVENTTYPE, eventType + "%"));
        if (eventName != null)
            criteria.add(Restrictions.ilike(EVENT_NAME, eventName + "%"));
        if (eventHost != null)
            criteria.add(Restrictions.ilike(EVENT_HOST, eventHost + "%"));
        
        try {
            criteria.add(Restrictions.ge(EVENT_STARTDATE, formatter.parse(formatter.format(startDate)).getTime()));
            criteria.add(Restrictions.ge(EVENT_ENDDATE, formatter.parse(formatter.format(startDate)).getTime()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        criteria.addOrder(Order.desc(EVENT_ID));
        return criteria.list();
    }

    public void eventUploadWallpaper(Event event,
            final MultipartFile[] files) throws IOException {

        for (int i = 0; i < files.length; i++)
            if (!files[i].isEmpty())
                event.setFilestore(
                        fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                                files[i].getContentType(), MODULE_NAME));

    }

    public void removeEventWallpaper(Event event) throws IOException {

        fileStoreService.delete(event.getFilestore().getFileStoreId(), MODULE_NAME);

    }
}
