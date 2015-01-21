/*
 * @(#)EgAttributevaluesHibDao.java 3.0, 17 Jun, 2013 1:19:40 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.flexfields.dao;

import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.flexfields.model.EgAttributevalues;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgAttributevaluesHibDao extends GenericHibernateDAO implements EgAttributevaluesDao {
	
	public EgAttributevaluesHibDao() {
		super(EgAttributevalues.class, null);
	}
			
	public EgAttributevaluesHibDao(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	@Override
	public List<EgAttributevalues> getAttributeValuesForDomainTxn(final String domainName, final String domainTxnId) {
		final Query qry = getCurrentSession().createQuery("from EgAttributevalues attrValues where attrValues.egApplDomain.name =:domainName and attrValues.domaintxnid =:domainTxnId ");
		qry.setString("domainName", domainName);
		qry.setString("domainTxnId", domainTxnId);
		return qry.list();
	}
}
