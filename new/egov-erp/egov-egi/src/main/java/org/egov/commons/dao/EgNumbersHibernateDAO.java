/*
 * @(#)EgNumbersHibernateDAO.java 3.0, 11 Jun, 2013 2:24:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.EgNumbers;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgNumbersHibernateDAO extends GenericHibernateDAO {
	
	public EgNumbersHibernateDAO() {
		super(EgNumbers.class,null);
	}
	
	public EgNumbersHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public EgNumbers getEgNumberByFiscalPeriodAndVouchertype(final String fiscialperiodid, final String vouchertype) {
		final Query qry = getSession().createQuery("from EgNumbers egnum where egnum.fiscialperiodid=:fiscialperiodid and vouchertype=:vouchertype");
		qry.setString("fiscialperiodid", fiscialperiodid);
		qry.setString("vouchertype", vouchertype);
		return (EgNumbers) qry.uniqueResult();
	}
}