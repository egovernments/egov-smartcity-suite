package org.egov.eventnotification.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.criterion.Order;

import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	
	public List<Event> findAll() {
		return eventRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }
	
	public Event findById(Long id) {
		return eventRepository.findOne(id);
    }
	
	@Transactional
    public Event persist(final Event event, final MultipartFile[] files)
            throws IOException, ParseException {
        
        Event savedEvent = eventRepository.save(event);
        
        return savedEvent;
    }
	
	@Transactional
    public Event update(final Event event, final MultipartFile[] files)
            throws IOException, ParseException {
        
        Event savedEvent = (Event) getCurrentSession().merge(event);
        
        return savedEvent;
    }
	
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
