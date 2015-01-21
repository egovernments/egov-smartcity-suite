/*
 * @(#)VoucherHeaderHibernateDAO.java 3.0, 14 Jun, 2013 11:41:17 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class VoucherHeaderHibernateDAO extends GenericHibernateDAO implements VoucherHeaderDAO {
	public VoucherHeaderHibernateDAO() {
		super(CVoucherHeader.class, null);

	}

	public VoucherHeaderHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);

	}

	@Override
	public List<CVoucherHeader> getVoucherHeadersByStatus(final Integer status) throws Exception {
		final Query qry = getCurrentSession().createQuery("from CVoucherHeader vh where vh.status=:status");
		qry.setInteger("status", status);
		return qry.list();
	}

	@Override
	public List<CVoucherHeader> getVoucherHeadersByStatusAndType(final Integer status, final String type) throws Exception {
		final Query qry = getCurrentSession().createQuery("from CVoucherHeader vh where vh.status=:status and vh.type=:type");
		qry.setInteger("status", status);
		qry.setString("type", type);
		return qry.list();
	}

	@Override
	public CVoucherHeader getVoucherHeadersByCGN(final String cgn) {
		final Query qry = getCurrentSession().createQuery(" from CVoucherHeader where cgn =:cgn");
		qry.setString("cgn", cgn);
		return (CVoucherHeader) qry.uniqueResult();
	}
}
