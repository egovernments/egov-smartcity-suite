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

import org.egov.eventnotification.entity.Event;
import org.egov.eventnotification.entity.UserEvent;
import org.egov.eventnotification.repository.EventRepository;
import org.egov.eventnotification.repository.UserEventRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserEventService {

    @Autowired
    private UserEventRepository usereventRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to save the user event mapping.
     * @param eventType
     * @param eventName
     * @param eventHost
     * @return List<Event>
     */
    @Transactional
    public UserEvent saveUserEvent(Long userid, Long eventid) {
        UserEvent existingUserEvent = usereventRepository.findByEventIdAndUserId(eventid, userid);
        if (existingUserEvent == null) {
            Event event = eventRepository.findOne(eventid);
            User user = userRepository.findOne(userid);
            UserEvent userevent = new UserEvent();
            userevent.setUserId(user.getId());
            userevent.setEventId(event.getId());
            return usereventRepository.save(userevent);
        } else
            return null;
    }

    /**
     * This method fetch couynt of the event by id
     * @param id
     * @return Long
     */
    public Long countUsereventByEventId(Long id) {
        return usereventRepository.countByEventId(id);
    }

    /**
     * This method fetch Userevent
     * @param eventid
     * @param userid
     * @return Userevent
     */
    public UserEvent getUsereventByEventAndUser(Long eventid, Long userid) {
        return usereventRepository.findByEventIdAndUserId(eventid, userid);
    }
}
