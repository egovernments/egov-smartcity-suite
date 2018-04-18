package org.egov.eventnotification.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.repository.EventRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

	
	private final EventRepository eventRepository;
	
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
	public List<Event> findAll() {
		return eventRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
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
		if(existingEvent != null) {
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
			existingEvent.setWallpaper(event.getWallpaper());
			existingEvent.setVersion((existingEvent.getVersion()+1));
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
	public List<Event> searchEvent(String eventType,String eventName,String eventHost){
		Criteria criteria = getCurrentSession().createCriteria(Event.class);
		if(eventType != null) {
			criteria.add(Restrictions.ilike("eventType", eventType+"%"));
		}
		if(eventName != null) {
			criteria.add(Restrictions.ilike("name", eventName+"%"));
		}
		if(eventHost != null) {
			criteria.add(Restrictions.ilike("eventhost", eventHost+"%"));
		}
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}
}
