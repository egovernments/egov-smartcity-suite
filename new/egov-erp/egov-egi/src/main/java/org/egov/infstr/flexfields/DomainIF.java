/*
 * @(#)DomainIF.java 3.0, 17 Jun, 2013 12:56:56 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

import org.egov.exceptions.EGOVRuntimeException;

public interface DomainIF {

	public void createDomain(Domain obj) throws EGOVRuntimeException;

	public void updateDomain(Domain obj) throws EGOVRuntimeException;

	public void deleteDomain(int id) throws EGOVRuntimeException;

	public Domain getDomain(int id) throws EGOVRuntimeException;
}
