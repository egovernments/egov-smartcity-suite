/*
 * @(#)AuthorizeRule.java 3.0, 18 Jun, 2013 3:59:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security;

import org.egov.lib.rjbac.user.User;

/**
 * Any object that needs to use the eGovAuthorize tag needs to implement this interface
 * @author sahinab
 */
public interface AuthorizeRule {

	public boolean isAuthorized(User user);
}
