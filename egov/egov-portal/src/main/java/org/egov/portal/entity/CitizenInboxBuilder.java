/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.portal.entity;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.commons.Module;
import org.egov.portal.entity.enums.MessageType;
import org.egov.portal.entity.enums.Priority;
import org.joda.time.DateTime;

/**
 * Builder class for Citizen Inbox
 * 
 * @author rishi
 *
 */

public class CitizenInboxBuilder {

	private CitizenInbox citizenInbox;

	public CitizenInboxBuilder(MessageType messageType,String headerMessage,String detailedMessage,DateTime messageDate,User assignedToCitizen,Priority priority) {
		citizenInbox = new CitizenInbox();
		citizenInbox.setMessageType(messageType);
		citizenInbox.setHeaderMessage(headerMessage);
		citizenInbox.setDetailedMessage(detailedMessage);
		citizenInbox.setMessageDate(messageDate);
		citizenInbox.setAssignedToCitizen(assignedToCitizen);
		citizenInbox.setPriority(priority);
	}

	public CitizenInboxBuilder module(Module module) {
		citizenInbox.setModule(module);
		return this;
	}
	
	public CitizenInboxBuilder identifier(String identifier) {
		citizenInbox.setIdentifier(identifier);
		return this;
	}

	public CitizenInboxBuilder link(String link) {
		citizenInbox.setLink(link);
		citizenInbox.setDetailedMessage(citizenInbox.getDetailedMessage().replace("<a>", "<a href=\""+link+"\" target=\"_blank\">"));
		return this;
	}

	public CitizenInboxBuilder state(State state) {
		citizenInbox.setState(state);
		return this;
	}

	public CitizenInboxBuilder status(String status) {
		citizenInbox.setStatus(status);
		return this;
	}

	public CitizenInbox build() throws EGOVRuntimeException {
		validate();
		citizenInbox.setRead(false);
		return citizenInbox;
	}

	private void validate() throws EGOVRuntimeException {
		if (citizenInbox.getMessageType() == null) {
			throw new EGOVRuntimeException("Message Type is mandatory");
		}
		if (citizenInbox.getHeaderMessage() == null) {
			throw new EGOVRuntimeException("Header Message is mandatory");
		}
		if (citizenInbox.getDetailedMessage() == null) {
			throw new EGOVRuntimeException("Detailed Message is mandatory");
		}
		if (citizenInbox.getMessageDate() == null) {
			throw new EGOVRuntimeException("Message Date is mandatory");
		}
		if (citizenInbox.getAssignedToCitizen() == null) {
			throw new EGOVRuntimeException("Assigned To Citizen is mandatory");
		}
		if (citizenInbox.getPriority() == null) {
			throw new EGOVRuntimeException("Priority is mandatory");
		}
	}

}