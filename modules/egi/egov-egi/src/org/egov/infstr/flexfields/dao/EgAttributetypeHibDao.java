/*
 * @(#)EgAttributetypeHibDao.java 3.0, 17 Jun, 2013 1:18:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.flexfields.model.EgAttributetype;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgAttributetypeHibDao extends GenericHibernateDAO implements EgAttributetypeDao {
	
	public EgAttributetypeHibDao() {
		super(EgAttributetype.class, null);
	}
			
	public EgAttributetypeHibDao(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public EgAttributetype getAttributeTypeByDomainNameAndAttrName(final String domainName, final String attributeName) {
		final Query qry = getSession().createQuery("from EgAttributetype attrType where attrType.egApplDomain.name =:domainName and attrType.attName =:attrName ");
		qry.setString("domainName", domainName);
		qry.setString("attrName", attributeName);
		return (EgAttributetype) qry.uniqueResult();
	}
}
