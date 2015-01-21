/*
 * @(#)BankDAO.java 3.0, 10 Jun, 2013 10:17:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Bank;
import org.egov.exceptions.EGOVRuntimeException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BankDAO {

	private SessionFactory sessionFactory;

	public BankDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public void createBank(final Bank bank) {
		try {

			getSession().save(bank);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while creating Bank", e);
		}
	}

	public void updateBank(final Bank bank) {
		try {
			getSession().saveOrUpdate(bank);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while updating Bank", e);
		}
	}

	public void removeBank(final Bank bank) {
		try {
			getSession().delete(bank);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while deleting Bank", e);
		}
	}

	public Bank getBankById(final int bank) {
		try {
			return (Bank) getSession().get(Bank.class, Integer.valueOf(bank));
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred while getting Bank by id", e);
		}
	}

}
