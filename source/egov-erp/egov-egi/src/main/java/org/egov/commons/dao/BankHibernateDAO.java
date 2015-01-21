/*
 * @(#)BankHibernateDAO.java 3.0, 10 Jun, 2013 10:22:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Bank;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class BankHibernateDAO extends GenericHibernateDAO {

	public BankHibernateDAO() {
		super(Bank.class, null);
	}

	public BankHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public Bank getBankByCode(final String bankCode) {
		final Query qry = getCurrentSession().createQuery("from Bank where code=:bankCode");
		qry.setString("bankCode", bankCode);
		return (Bank) qry.uniqueResult();
	}
}
