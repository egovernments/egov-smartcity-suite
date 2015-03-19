/*
 * @(#)AccountEntity.java 3.0, 7 Jun, 2013 8:50:32 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.masters.model;

import java.util.Date;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.UserImpl;

public class AccountEntity implements java.io.Serializable, EntityType {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Accountdetailtype accountdetailtype;

	private String name;

	private String code;

	private String narration;

	private Boolean isactive;

	private Date lastmodified;

	private UserImpl modifiedby;

	private Date created;

	private Integer accountDetailKeyId;

	public AccountEntity() {
		//For hibernate to work
	}

	public AccountEntity(Accountdetailtype accountdetailtype, String name, String code, String narration, Boolean isactive, Date lastmodified, UserImpl modifiedby, Date created) {
		this.accountdetailtype = accountdetailtype;
		this.name = name;
		this.code = code;
		this.narration = narration;
		this.isactive = isactive;
		this.lastmodified = lastmodified;
		this.modifiedby = modifiedby;
		this.created = created;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Accountdetailtype getAccountdetailtype() {
		return this.accountdetailtype;
	}

	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public UserImpl getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(UserImpl modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getAccountDetailKeyId() {
		return accountDetailKeyId;
	}

	public void setAccountDetailKeyId(Integer accountDetailKeyId) {
		this.accountDetailKeyId = accountDetailKeyId;
	}

	@Override
	public String getBankaccount() {
		return null;
	}

	@Override
	public String getBankname() {
		return null;
	}

	@Override
	public String getIfsccode() {		
		return null;
	}

	@Override
	public String getModeofpay() {		
		return null;
	}

	@Override
	public String getPanno() {		
		return null;
	}

	@Override
	public String getTinno() {		
		return null;
	}

	@Override
	public Integer getEntityId() {
		return this.id;

	}

	public String getEntityDescription() {
		return this.narration;
	}

}
