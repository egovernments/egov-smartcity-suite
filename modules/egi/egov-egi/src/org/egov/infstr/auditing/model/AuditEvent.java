/*
 * @(#)AuditEvent.java 3.0, 21 Jun, 2013 5:58:34 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.auditing.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class AuditEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//Standard AuditEvent Action which can be reused across modules.
	public static final String MODIFIED = "Modified";
	public static final String DELETED  = "Deleted";
	public static final String CREATED  = "Created";
	
	@DocumentId
	protected Long id;						//PK id of the object	
	@NotNull
	private AuditModule auditModule;		//Auditable Module
	@NotNull
	private AuditEntity auditEntity;		//Auditable Entity Name
	@NotNull
	private String action;					// the action that was done e.g. CREATED
	private String fqcn;					//fully qualified class name
	@NotNull
	private String bizId;					//Business identifier of the Auditable entity
	private Long pkId;						//Primary Key Id for the Auditable entity
	@NotEmpty
	private String userName;				//User Name 
	@NotNull
	@Length(min=1,max=4000)
	private String details1;				//First level of detail; any important fields & info
	@Length(min=0,max=4000)
	private String details2;				//second level of detail; less important fields & info
	@NotNull
	private Date eventDate;					//Modified Date
	
	AuditEvent() {
		//Default Constructor is mandatory for Hibernate to work
	}
	
	public AuditEvent(final AuditModule auditModule, final AuditEntity auditEntity, final String action, final String bizId, final String details1) {
		this.setAuditModule(auditModule);
		this.setAuditEntity(auditEntity);
		this.setAction(action);
		this.setBizId(bizId);
		this.setDetails1(details1);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public AuditModule getAuditModule() {
		return auditModule;
	}
	
	public void setAuditModule(AuditModule auditModule) {
		this.auditModule = auditModule;
	}
	
	public AuditEntity getAuditEntity() {
		return auditEntity;
	}
	
	public void setAuditEntity(AuditEntity auditEntity) {
		this.auditEntity = auditEntity;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getFqcn() {
		return fqcn;
	}
	public void setFqcn(String fqcn) {
		this.fqcn = fqcn;
	}
	
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getDetails1() {
		return details1;
	}
	
	public void setDetails1(String details1) {
		this.details1 = details1;
	}
	
	public String getDetails2() {
		return details2;
	}
	
	public void setDetails2(String details2) {
		this.details2 = details2;
	}
	
	public Date getEventDate() {
		return eventDate;
	}
	
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	@Override
	public String toString() {
		return "AuditEvent [id=" + id + ", auditModule=" + auditModule
				+ ", auditEntity=" + auditEntity + ", action=" + action
				+ ", fqcn=" + fqcn + ", bizId=" + bizId + ", pkId=" + pkId
				+ ", userName=" + userName + ", details1=" + details1
				+ ", details2=" + details2 + ", eventDate=" + eventDate + "]";
	}
}
