package org.egov.eventnotification.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.egov.eventnotification.constants.EventnotificationConstant;
import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class EventService {

	
	private final EventRepository eventRepository;
	
	@Autowired
    public EventService(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;

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
}
