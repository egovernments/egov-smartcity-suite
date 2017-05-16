/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.portal.service;

import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.portal.entity.PortalInbox;
import org.egov.portal.entity.PortalInboxUser;
import org.egov.portal.repository.PortalInboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PortalInboxService {

    private final PortalInboxRepository portalInboxRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    public PortalInboxService(final PortalInboxRepository portalInboxRepository) {
        this.portalInboxRepository = portalInboxRepository;
    }

    @Transactional
    public void pushInboxMessage(final PortalInbox portalInbox) {
        if (portalInbox.getTempPortalInboxUser().isEmpty()) {
            final User user = getLoggedInUser();
            if (user != null)
                createPortalUser(portalInbox, user);
        } else
            portalInbox.getPortalInboxUsers().addAll(portalInbox.getTempPortalInboxUser());
        portalInboxRepository.save(portalInbox);
    }

    private void createPortalUser(final PortalInbox portalInbox, final User user) {
        if (UserType.BUSINESS.toString().equalsIgnoreCase(user.getType().toString())
                || UserType.CITIZEN.toString().equalsIgnoreCase(user.getType().toString())) {
            final PortalInboxUser portalInboxUser = new PortalInboxUser();
            portalInboxUser.setUser(user);
            portalInbox.getPortalInboxUsers().add(portalInboxUser);
            portalInboxUser.setPortalInbox(portalInbox);
        } else
            throw new ApplicationRuntimeException("Logged in User must be a Citizen or Business User.");
    }

    @Transactional
    public void updateInboxMessage(final String applicationNumber, final Long moduleId, final String status,
            final Boolean isResolved, final Date slaEndDate, final State state, final User user) {
        final PortalInbox portalInbox = getPortalInboxByApplicationNo(applicationNumber, moduleId);
        if (portalInbox != null) {
            portalInbox.setStatus(status);
            portalInbox.setResolved(isResolved);
            if (portalInbox.getSlaEndDate() != null)
                portalInbox.setSlaEndDate(slaEndDate);
            portalInbox.setState(state);
            if (!containsUser(portalInbox.getPortalInboxUsers(), user.getId()))
                if (user != null)
                    createPortalUser(portalInbox, user);
            portalInboxRepository.save(portalInbox);
        }
    }

    public boolean containsUser(final List<PortalInboxUser> list, final Long userId) {
        return list.stream().anyMatch(item -> item.getUser().getId().equals(userId));
    }

    public PortalInbox getPortalInboxByApplicationNo(final String applicationNumber, final Long moduleId) {
        return portalInboxRepository.findByApplicationNumberAndModule_Id(applicationNumber, moduleId);
    }

    /**
     * This method returns the User instance associated with the logged in user
     *
     * @return the logged in user
     */
    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }
}
