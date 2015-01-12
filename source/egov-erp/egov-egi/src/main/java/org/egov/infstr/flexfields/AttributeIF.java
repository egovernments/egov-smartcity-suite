/*
 * @(#)AttributeIF.java 3.0, 17 Jun, 2013 12:50:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields;

import org.egov.exceptions.EGOVRuntimeException;

import java.util.List;

public interface AttributeIF {
	
	public void insert(Attribute obj) throws EGOVRuntimeException;
	public void update(Attribute obj) throws EGOVRuntimeException;
	public void delete(Attribute obj) throws EGOVRuntimeException;
	public Attribute getAttribute(int attId) throws EGOVRuntimeException;
	public List<Attribute> getDomainAttributes(int domainId) throws EGOVRuntimeException;
	public List<Attribute> getDomainTxnAttributes(int domainId,int domainTxnId) throws EGOVRuntimeException;
	public Attribute getAttributeProperties(int domainId,int domainTxnId,int attrTypeId) throws EGOVRuntimeException;
	
}
