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
package org.egov.portal.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.State;

public class PortalInboxBuilder {

    private final PortalInbox portalInbox;

    public PortalInboxBuilder(final Module module, final String serviceType, final String applicationNumber,
            final String entityRefNo, final Long entityRefId, final String headerMsg, final String detailedMessage,
            final String link, final boolean isResolved, final String status, final Date slaEndDate, final State state,
            final User user) {
        portalInbox = new PortalInbox();
        portalInbox.setModule(module);
        portalInbox.setServiceType(serviceType);
        portalInbox.setApplicationNumber(applicationNumber);
        portalInbox.setEntityRefNumber(entityRefNo);
        portalInbox.setEntityRefId(entityRefId);
        portalInbox.setHeaderMessage(headerMsg);
        portalInbox.setResolved(isResolved);
        portalInbox.setDetailedMessage(detailedMessage);
        portalInbox.setLink(link);
        portalInbox.setRead(false);
        portalInbox.setStatus(status);
        portalInbox.setSlaEndDate(slaEndDate);
        portalInbox.setState(state);
        portalInbox.setApplicationDate(DateUtils.now());
        if (user != null
                && (UserType.BUSINESS.toString().equalsIgnoreCase(user.getType().toString()) || UserType.CITIZEN
                        .toString().equalsIgnoreCase(user.getType().toString()))) {
            PortalInboxUser portalInboxUser = new PortalInboxUser();
            portalInboxUser.setUser(user);
            portalInboxUser.setPortalInbox(portalInbox);
            portalInbox.setTempPortalInboxUser(new ArrayList<PortalInboxUser>(Arrays.asList(portalInboxUser)));
        }
    }

    public PortalInbox build() throws ApplicationRuntimeException {
        validate();
        return portalInbox;
    }

    private void validate() throws ApplicationRuntimeException {
        // add validation for mandatory fields
        validateParams();
        if (portalInbox.getLink() == null || portalInbox.getLink().isEmpty())
            throw new ApplicationRuntimeException("Link is mandatory");
        if (portalInbox.getStatus() == null || portalInbox.getStatus().isEmpty())
            throw new ApplicationRuntimeException("Status is mandatory");
        if (portalInbox.getState() == null)
            throw new ApplicationRuntimeException("State is mandatory");
        if (portalInbox.getTempPortalInboxUser() != null && !portalInbox.getTempPortalInboxUser().isEmpty()) {
            User user = portalInbox.getTempPortalInboxUser().get(0).getUser();
            if (user != null
                    && !(UserType.BUSINESS.toString().equalsIgnoreCase(user.getType().toString()) || UserType.CITIZEN
                            .toString().equalsIgnoreCase(user.getType().toString())))
                throw new ApplicationRuntimeException("User must be a Citizen or Business User.");
        }

    }

    private void validateParams() {
        if (portalInbox.getModule() == null)
            throw new ApplicationRuntimeException("Module is mandatory");
        if (portalInbox.getServiceType() == null || portalInbox.getServiceType().isEmpty())
            throw new ApplicationRuntimeException("ServiceType is mandatory");
        if (portalInbox.getApplicationNumber() == null || portalInbox.getApplicationNumber().isEmpty())
            throw new ApplicationRuntimeException("ApplicationNumber is mandatory");
        if (portalInbox.getDetailedMessage() == null || portalInbox.getDetailedMessage().isEmpty())
            throw new ApplicationRuntimeException("DetailedMessage is mandatory");
    }

}
