/*
 * @(#)VouchermisHibernateDAO.java 3.0, 14 Jun, 2013 11:42:19 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.commons.Vouchermis;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class VouchermisHibernateDAO extends GenericHibernateDAO {
	public VouchermisHibernateDAO() {
		super(Vouchermis.class, null);
	}

	public VouchermisHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public Vouchermis getVouchermisByVhId(final Integer vhId) {
		final Query qry = getSession().createQuery("from Vouchermis where voucherheaderid =:vhId");
		qry.setInteger("vhId", vhId);
		return (Vouchermis) qry.uniqueResult();
	}

}
