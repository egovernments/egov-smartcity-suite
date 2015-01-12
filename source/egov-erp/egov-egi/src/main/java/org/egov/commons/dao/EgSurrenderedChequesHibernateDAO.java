/*
 * @(#)EgSurrenderedChequesHibernateDAO.java 3.0, 11 Jun, 2013 2:28:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.EgSurrenderedCheques;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class EgSurrenderedChequesHibernateDAO extends GenericHibernateDAO {
	
	public EgSurrenderedChequesHibernateDAO() {
		super(EgSurrenderedCheques.class,null);
	}
	
	public EgSurrenderedChequesHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

}
