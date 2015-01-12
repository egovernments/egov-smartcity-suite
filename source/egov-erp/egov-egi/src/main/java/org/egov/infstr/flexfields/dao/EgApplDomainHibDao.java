/*
 * @(#)EgApplDomainHibDao.java 3.0, 17 Jun, 2013 1:18:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.flexfields.model.EgApplDomain;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgApplDomainHibDao extends GenericHibernateDAO implements EgApplDomainDao {

	public EgApplDomainHibDao() {
		super(EgApplDomain.class, null);
	}
	
	public EgApplDomainHibDao(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public EgApplDomain getApplDomainForDomainNameAndAttrName(final String domainName, final String attrName) {
		final Query qry = getSession().createQuery("select attrType.egApplDomain from EgAttributetype attrType where attrType.egApplDomain.name =:domainName and attrType.attName =:attrName ");
		qry.setString("domainName", domainName);
		qry.setString("attrName", attrName);
		return (EgApplDomain) qry.uniqueResult();
	}
}
