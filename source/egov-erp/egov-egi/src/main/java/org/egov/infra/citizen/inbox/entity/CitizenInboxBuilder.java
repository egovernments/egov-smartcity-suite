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

	public CitizenInboxBuilder() {
		citizenInbox = new CitizenInbox();
	}

	public CitizenInboxBuilder module(Module module) {
		citizenInbox.setModule(module);
		return this;
	}

	public CitizenInboxBuilder messageType(MessageType messageType) {
		citizenInbox.setMessageType(messageType);
		return this;
	}

	public CitizenInboxBuilder identifier(String identifier) {
		citizenInbox.setIdentifier(identifier);
		return this;
	}

	public CitizenInboxBuilder headerMessage(String headerMessage) {
		citizenInbox.setHeaderMessage(headerMessage);
		return this;
	}

	public CitizenInboxBuilder detailedMessage(String detailedMessage) {
		citizenInbox.setDetailedMessage(detailedMessage);
		return this;
	}

	public CitizenInboxBuilder link(String link) {
		citizenInbox.setLink(link);
		return this;
	}

	public CitizenInboxBuilder messageDate(Date messageDate) {
		citizenInbox.setMessageDate(messageDate);
		return this;
	}

	public CitizenInboxBuilder state(State state) {
		citizenInbox.setState(state);
		return this;
	}

	public CitizenInboxBuilder assignedToCitizen(User assignedToCitizen) {
		citizenInbox.setAssignedToCitizen(assignedToCitizen);
		return this;
	}

	public CitizenInboxBuilder priority(Priority priority) {
		citizenInbox.setPriority(priority);
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