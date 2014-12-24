/*
 * @(#)EgwSatuschangeHibernateDAO.java 3.0, 11 Jun, 2013 2:31:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.EgwSatuschange;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgwSatuschangeHibernateDAO extends GenericHibernateDAO {
	
	public EgwSatuschangeHibernateDAO() {
		super(EgwSatuschange.class,null);
	}
	
	public EgwSatuschangeHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}
}
