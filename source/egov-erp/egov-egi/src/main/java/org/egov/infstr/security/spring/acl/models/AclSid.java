/*
 * @(#)AclSid.java 3.0, 18 Jun, 2013 3:57:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.acl.models;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;

public class AclSid extends BaseModel {

	private static final long serialVersionUID = 8716207380957130047L;
	@Required(message = "sidType is required")
	private AclSidType sidType;
	private Long ownerSid;// can be userid or groupid

	private AclObjectIdentity aclObjectIdentity;

	private int permission;

	public Long getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(Long ownerSid) {
		this.ownerSid = ownerSid;
	}

	@Valid
	public AclSidType getSidType() {
		return sidType;
	}

	public void setSidType(AclSidType sidType) {
		this.sidType = sidType;
	}

	@Required(message = "permission is required")
	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public AclObjectIdentity getAclObjectIdentity() {
		return aclObjectIdentity;
	}

	public void setAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
		this.aclObjectIdentity = aclObjectIdentity;
	}

}
