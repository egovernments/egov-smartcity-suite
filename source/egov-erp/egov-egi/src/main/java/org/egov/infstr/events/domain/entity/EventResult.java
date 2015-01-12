/*
 * @(#)EventResult.java 3.0, 17 Jun, 2013 12:02:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "eg_event_result")
public class EventResult {

	private Integer id;
	private String module;
	private String eventCode;
	private Date dateRaised;
	private String result;
	private Date timeOfProcessing;
	private String details;

	@SequenceGenerator(name = "Event_Result", sequenceName = "eg_event_result_seq", allocationSize = 1)
	@GeneratedValue(generator = "Event_Result", strategy=GenerationType.SEQUENCE)
	@Id
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "module")
	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "event_code")
	public String getEventCode() {
		return this.eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	@Column(name = "date_raised")
	public Date getDateRaised() {
		return dateRaised;
	}

	public void setDateRaised(Date dateRaised) {
		this.dateRaised = dateRaised;
	}
	
	@Column(name = "result")
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "timeofprocessing")
	public Date getTimeOfProcessing() {
		return this.timeOfProcessing;
	}

	public void setTimeOfProcessing(Date timeOfProcessing) {
		this.timeOfProcessing = timeOfProcessing;
	}

	@Column(name = "details")
	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventResult [id=").append(id).append(", module=")
				.append(module).append(", eventCode=").append(eventCode)
				.append(", dateRaised=").append(dateRaised).append(", result=")
				.append(result).append(", timeOfProcessing=")
				.append(timeOfProcessing).append(", details=").append(details)
				.append("]");
		return builder.toString();
	}

	
}
