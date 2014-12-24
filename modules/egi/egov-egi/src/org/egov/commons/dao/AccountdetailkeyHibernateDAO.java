/*
 * @(#)AccountdetailkeyHibernateDAO.java 3.0, 10 Jun, 2013 7:18:54 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Accountdetailkey;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class AccountdetailkeyHibernateDAO extends GenericHibernateDAO {

	public AccountdetailkeyHibernateDAO() {
		super(Accountdetailkey.class,null);
	}
	
	public AccountdetailkeyHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}
}