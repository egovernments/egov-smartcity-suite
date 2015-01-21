/*
 * @(#)BankBranchHibernateDAO.java 3.0, 10 Jun, 2013 7:58:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Bankbranch;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

public class BankBranchHibernateDAO extends GenericHibernateDAO {

	public BankBranchHibernateDAO() {
		super(Bankbranch.class, null);
	}

	public BankBranchHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List<Bankbranch> getAllBankBranchs() {
		return getCurrentSession().createQuery("from Bankbranch BB order by BB.bank.name").list();
	}
}
