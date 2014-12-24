/*
 * @(#)BankaccountDAO.java 3.0, 10 Jun, 2013 7:51:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Bankaccount;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

public class BankaccountDAO {

	private Session getSession() {
		 return HibernateUtil.getCurrentSession();
	}

	public void createBankaccount(final Bankaccount bankaccount) {
		try {
			getSession().save(bankaccount);
			getSession().getSessionFactory();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while creating Bank Account",e);
		}
	}

	public void updateBankaccount(final Bankaccount bankaccount) {
		try {
			getSession().saveOrUpdate(bankaccount);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while updating Bank Account",e);
		}
	}

	public void removeBankaccount(final Bankaccount bankaccount) {
		try {
			getSession().delete(bankaccount);
			getSession().getSessionFactory();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while deleting Bank Account",e);
		}
	}

	public Bankaccount getBankaccountById(final int bankaccount) {
		try {
			return (Bankaccount) getSession().get(Bankaccount.class, bankaccount);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while getting Bank Account by Id",e);
		}
	}

}
