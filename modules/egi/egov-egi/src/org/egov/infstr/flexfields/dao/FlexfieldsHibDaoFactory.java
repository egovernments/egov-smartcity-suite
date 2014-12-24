/*
 * @(#)FlexfieldsHibDaoFactory.java 3.0, 17 Jun, 2013 1:20:12 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import org.egov.infstr.flexfields.model.EgApplDomain;
import org.egov.infstr.flexfields.model.EgAttributetype;
import org.egov.infstr.flexfields.model.EgAttributevalues;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

public class FlexfieldsHibDaoFactory extends FlexfieldsDaoFactory {
	protected Session getCurrentSession() {
		return HibernateUtil.getCurrentSession();
	}

	@Override
	public EgApplDomainDao getEgApplDomainDao() {
		return new EgApplDomainHibDao(EgApplDomain.class, getCurrentSession());
	}

	@Override
	public EgAttributetypeDao getEgAttributetypeDao() {
		return new EgAttributetypeHibDao(EgAttributetype.class, getCurrentSession());
	}

	@Override
	public EgAttributevaluesDao getEgAttributevaluesDao() {
		return new EgAttributevaluesHibDao(EgAttributevalues.class, getCurrentSession());
	}
}
