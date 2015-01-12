/*
 * @(#)AclObjectIdentity.java 3.0, 18 Jun, 2013 3:57:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.acl.models;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

public class AclObjectIdentity extends BaseModel {
	private static final long serialVersionUID = 5770066884148808731L;

	private AclObjClass aclObjClass;
	private Long domainObjectId;// reference to domain object
	private Set<AclSid> aclSidList = new LinkedHashSet<AclSid>();

	@Required(message = "aclObjClass is required")
	public AclObjClass getAclObjClass() {
		return aclObjClass;
	}

	public void setAclObjClass(AclObjClass aclObjClass) {
		this.aclObjClass = aclObjClass;
	}

	@Required(message = "domainObjectId is required")
	public Long getDomainObjectId() {
		return domainObjectId;
	}

	public void setDomainObjectId(Long domainObjectId) {
		this.domainObjectId = domainObjectId;
	}

	@Valid
	public Set<AclSid> getAclSidList() {
		return aclSidList;
	}

	public void setAclSidList(Set<AclSid> aclSidList) {
		this.aclSidList = aclSidList;
	}

	public void addAclSid(AclSid sid) {
		this.getAclSidList().add(sid);
		sid.setAclObjectIdentity(this);
	}

	public Set<AclSidType> getDistinctSidTypes() {
		Set<AclSidType> sidTypeSet = new LinkedHashSet<AclSidType>();
		for (AclSid sid : getAclSidList()) {
			sidTypeSet.add(sid.getSidType());
		}
		return sidTypeSet;
	}

}
