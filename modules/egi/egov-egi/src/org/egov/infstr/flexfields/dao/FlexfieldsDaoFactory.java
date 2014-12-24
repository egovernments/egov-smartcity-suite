/*
 * @(#)FlexfieldsDaoFactory.java 3.0, 17 Jun, 2013 1:19:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

public abstract class FlexfieldsDaoFactory {
	private static final FlexfieldsDaoFactory HIBERNATE = new FlexfieldsHibDaoFactory();

	public static FlexfieldsDaoFactory getDAOFactory() {
		return HIBERNATE;
	}

	// Add your DAO interfaces here

	public abstract EgApplDomainDao getEgApplDomainDao();

	public abstract EgAttributetypeDao getEgAttributetypeDao();

	public abstract EgAttributevaluesDao getEgAttributevaluesDao();
}
