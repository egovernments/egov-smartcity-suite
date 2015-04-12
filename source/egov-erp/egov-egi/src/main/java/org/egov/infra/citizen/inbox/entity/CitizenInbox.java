package org.egov.infra.citizen.inbox.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.citizen.inbox.entity.enums.MessageType;
import org.egov.infra.citizen.inbox.entity.enums.Priority;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State;
import org.egov.infstr.commons.Module;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

/**
 * CitizenInbox class
 * 
 * @author rishi
 *
 */
@Entity
@Table(name = "eg_citizeninbox")
public class CitizenInbox extends AbstractAuditable<User, Long> {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODULE_ID")
	private Module module;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "MESSAGE_TYPE")
	private MessageType messageType;

	@Length(max = 50)
	@Column(name = "IDENTIFIER")
	private String identifier;

	@NotNull
	@Length(max = 500)
	@Column(name = "HEADER_MSG")
	private String headerMessage;

	@NotNull
	@Length(max = 2048)
	@Column(name = "DETAILED_MSG")
	private String detailedMessage;

	@Length(max = 256)
	@Column(name = "LINK")
	private String link;

	@Column(name = "READ")
	private boolean read;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Column(name = "MSG_DATE")
	private Date messageDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID")
	private State state;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNED_TO_USER")
	private User assignedToCitizen;

	@NotNull
	@Length(max = 20)
	@Column(name = "PRIORITY")
	private Priority priority;

	@Length(max = 100)
	@Column(name = "STATUS")
	private String status;

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getHeaderMessage() {
		return headerMessage;
	}

	public void setHeaderMessage(String headerMessage) {
		this.headerMessage = headerMessage;
	}

	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public DateTime getMessageDate() {
		return new DateTime(messageDate);
	}

	public void setMessageDate(DateTime messageDate) {
		this.messageDate = messageDate.toDate();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public User getAssignedToCitizen() {
		return assignedToCitizen;
	}

	public void setAssignedToCitizen(User assignedToCitizen) {
		this.assignedToCitizen = assignedToCitizen;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
