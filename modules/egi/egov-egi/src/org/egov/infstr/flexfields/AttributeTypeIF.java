/*
 * @(#)AttributeTypeIF.java 3.0, 17 Jun, 2013 12:54:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

import org.egov.exceptions.EGOVRuntimeException;

public interface AttributeTypeIF {

	public void createAttributeType(AttributeType typeObj) throws EGOVRuntimeException;

	public void updateAttributeType(AttributeType typeObj) throws EGOVRuntimeException;

	public void deleteAttributeType(int id) throws EGOVRuntimeException;

	public AttributeType getAttributeType(int id) throws EGOVRuntimeException;
}
