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
package org.egov.portal.entity;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.workflow.entity.State;
import org.egov.portal.entity.enums.MessageType;
import org.egov.portal.entity.enums.Priority;

import java.util.Date;

/**
 * Builder class for Citizen Inbox
 *
 * @author rishi
 *
 */

public class CitizenInboxBuilder {

    private final CitizenInbox citizenInbox;

    public CitizenInboxBuilder(final MessageType messageType, final String headerMessage, final String detailedMessage,
            final Date messageDate,
            final User assignedToCitizen, final Priority priority) {
        citizenInbox = new CitizenInbox();
        citizenInbox.setMessageType(messageType);
        citizenInbox.setHeaderMessage(headerMessage);
        citizenInbox.setDetailedMessage(detailedMessage);
        citizenInbox.setMessageDate(messageDate);
        citizenInbox.setAssignedToCitizen(assignedToCitizen);
        citizenInbox.setPriority(priority);
    }

    public CitizenInboxBuilder module(final Module module) {
        citizenInbox.setModule(module);
        return this;
    }

    public CitizenInboxBuilder identifier(final String identifier) {
        citizenInbox.setIdentifier(identifier);
        return this;
    }

    public CitizenInboxBuilder link(final String link) {
        citizenInbox.setLink(link);
        return this;
    }

    public CitizenInboxBuilder state(final State state) {
        citizenInbox.setState(state);
        return this;
    }

    public CitizenInboxBuilder status(final String status) {
        citizenInbox.setStatus(status);
        return this;
    }

    public CitizenInbox build() throws ApplicationRuntimeException {
        validate();
        citizenInbox.setRead(false);
        return citizenInbox;
    }

    private void validate() throws ApplicationRuntimeException {
        if (citizenInbox.getMessageType() == null)
            throw new ApplicationRuntimeException("Message Type is mandatory");
        if (citizenInbox.getHeaderMessage() == null)
            throw new ApplicationRuntimeException("Header Message is mandatory");
        if (citizenInbox.getDetailedMessage() == null)
            throw new ApplicationRuntimeException("Detailed Message is mandatory");
        if (citizenInbox.getMessageDate() == null)
            throw new ApplicationRuntimeException("Message Date is mandatory");
        if (citizenInbox.getAssignedToCitizen() == null)
            throw new ApplicationRuntimeException("Assigned To Citizen is mandatory");
        if (citizenInbox.getPriority() == null)
            throw new ApplicationRuntimeException("Priority is mandatory");
    }

}