/*
 * @(#)NotificationFile.java 3.0, 15 Jul, 2013 9:35:05 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import java.util.Set;

import javax.validation.Valid;

import org.egov.infstr.workflow.NotificationGroup;


/**
 * The Class NotificationFile.
 */
public class NotificationFile extends GenericFile {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The sender. */
	@Valid
	private ExternalUser sender = new ExternalUser();
	@Valid
	private Set<NotificationGroup> notificationGroups;
	
	/**
	 * Gets the sender.
	 * @return the sender
	 */
	public ExternalUser getSender() {
		return sender;
	}
	
	/**
	 * Sets the sender.
	 * @param sender the sender to set
	 */
	public void setSender(ExternalUser sender) {
		this.sender = sender;
	}

	
	/**
	 * @return the notificationGroups
	 */
	public Set<NotificationGroup> getNotificationGroups() {
		return notificationGroups;
	}

	
	/**
	 * @param notificationGroups the notificationGroups to set
	 */
	public void setNotificationGroups(Set<NotificationGroup> notificationGroups) {
		this.notificationGroups = notificationGroups;
	}

	
	
}
