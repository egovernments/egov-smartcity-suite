package org.egov.eventnotification.service;

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.Userevent;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.eventnotification.repository.UsereventRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsereventService {

    private final UsereventRepository usereventRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public UsereventService(final UsereventRepository usereventRepository, final EventRepository eventRepository,
            final UserRepository userRepository) {
        this.usereventRepository = usereventRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * This method is used to save the user event mapping.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return List<Event>
     */
    @Transactional
    public Userevent persistUserevent(String userid, String eventid) {
        Event event = eventRepository.findOne(Long.parseLong(eventid));
        User user = userRepository.findOne(Long.parseLong(userid));
        Userevent userevent = new Userevent();
        userevent.setUserid(user);
        userevent.setEventid(event);
        return usereventRepository.save(userevent);
    }
}
