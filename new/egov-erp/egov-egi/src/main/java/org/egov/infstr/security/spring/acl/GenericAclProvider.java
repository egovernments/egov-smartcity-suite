/*
 * @(#)GenericAclProvider.java 3.0, 18 Jun, 2013 3:46:54 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.acl;


import java.util.Collection;

import org.egov.infstr.services.PersistenceService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;

public class GenericAclProvider implements AfterInvocationProvider {

	private PersistenceService persistenceService;
	private Permission[] requirePermission = {BasePermission.READ};
	
	public GenericAclProvider(PersistenceService persistenceService, Permission[] requirePermission) {
        this.persistenceService = persistenceService;
        this.requirePermission = requirePermission;
    } 
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public Object decide(Authentication arg0, Object arg1, Collection<ConfigAttribute> arg2, Object returnedObject) throws AccessDeniedException {
		return returnedObject;
	}
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
