package org.egov.infra.citizen.inbox.entity;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.citizen.inbox.entity.enums.MessageType;
import org.egov.infra.citizen.inbox.entity.enums.Priority;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.commons.Module;

/**
 * Builder class for Citizen Inbox
 * 
 * @author rishi
 *
 */

public class CitizenInboxBuilder {

	private CitizenInbox citizenInbox;

	public CitizenInboxBuilder(MessageType messageType,String headerMessage,String detailedMessage,Date messageDate,User assignedToCitizen,Priority priority) {
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