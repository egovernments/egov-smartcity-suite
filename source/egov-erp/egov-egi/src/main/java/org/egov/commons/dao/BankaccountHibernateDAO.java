/*
 * @(#)BankaccountHibernateDAO.java 3.0, 10 Jun, 2013 7:53:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Bankaccount;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class BankaccountHibernateDAO extends GenericHibernateDAO {
	
	public BankaccountHibernateDAO() {
		super(Bankaccount.class,null);
	}
	
	public BankaccountHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List<Bankaccount> getAllBankAccounts() {
		return getCurrentSession().createQuery("from Bankaccount BA order by BA.accountnumber").list();
	}

	/**
	 * This method will return the BankAccount object based on matching bankcode,branchcode,bankaccountanumber
	 * @return
	 */
	public Bankaccount getBankAccountByAccBranchBank(final String bankAccNum, final String bankBranchCode, final String bankCode) {
		final Query qry = getCurrentSession().createQuery("from Bankaccount bankacc where bankacc.accountnumber=:accNum " + " and bankacc.bankbranch.branchcode=:branchCode and bankacc.bankbranch.bank.code=:bankCode");
		qry.setString("accNum", bankAccNum);
		qry.setString("branchCode", bankBranchCode);
		qry.setString("bankCode", bankCode);
		Bankaccount bankAccount = null;
		if (qry.list().size() != 0) {
			bankAccount = (Bankaccount) qry.list().get(0);
		}
		return bankAccount;
	}

}
