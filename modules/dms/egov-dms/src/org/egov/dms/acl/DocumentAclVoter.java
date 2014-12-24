/*
 * @(#)DocumentAclVoter.java 3.0, 16 Jul, 2013 11:33:09 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.acl;

import org.aopalliance.intercept.MethodInvocation;
import org.egov.dms.models.GenericFile;
import org.egov.infstr.security.spring.acl.GenericAclVoter;
import org.egov.infstr.services.PersistenceService;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.acls.model.Permission;
import org.springframework.util.Assert;

public class DocumentAclVoter extends GenericAclVoter {


	private String processConfigAttribute;
	private GenericFile domainObject;
	private Long genericFileId;



	public DocumentAclVoter(PersistenceService persistenceService,
			String processConfigAttribute, Permission[] requirePermission) {
		Assert.notNull(processConfigAttribute,
				"A processConfigAttribute is mandatory");
		Assert
		.notNull(persistenceService,
				"An persistenceService is mandatory");

		if ((requirePermission == null) || (requirePermission.length == 0)) {
			throw new IllegalArgumentException(
			"One or more requirePermission entries is mandatory");
		}
		this.persistenceService = persistenceService;
		this.processConfigAttribute = processConfigAttribute;
		this.requirePermission = requirePermission;
	}



	@Override
	protected GenericFile getDomainObjectInstance(Object secureObject) {
		Object[] args;

		if (secureObject instanceof MethodInvocation) {
			MethodInvocation invocation = (MethodInvocation) secureObject;
			args = invocation.getArguments();
			GenericFile file=(GenericFile)persistenceService.findByNamedQuery((String)args[0],(String) args[1]);
			this.domainObject=file;
			super.domainObject=this.domainObject;
			this.permissionGranted=false;
			return  file;
		}

		throw new AuthorizationServiceException("Secure object: "
				+ secureObject + " did not provide any returntype of type: GenericFile ");
	}
	
	public Long getGenericFileId() {
		return genericFileId;
	}
	public void setGenericFileId(Long genericFileId) {
		this.genericFileId = genericFileId;
	}



}