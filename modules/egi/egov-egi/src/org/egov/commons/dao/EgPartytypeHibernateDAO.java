/*
 * @(#)EgPartytypeHibernateDAO.java 3.0, 11 Jun, 2013 2:26:24 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.EgPartytype;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgPartytypeHibernateDAO extends GenericHibernateDAO {

	public EgPartytypeHibernateDAO() {
		super(EgPartytype.class, null);
	}

	public EgPartytypeHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List findAllPartyTypeChild() {
		return getSession().createQuery("from EgPartytype pt where pt.egPartytype is not null order by upper(code)").list();
	}

	/**
	 * @param code
	 * @param parentCode
	 * @param description
	 * @return list of EgPartytype filtered by optional conditions
	 */
	public List<EgPartytype> getPartyTypeDetailFilterBy(final String code, final String parentCode, final String description) {
		final StringBuffer qryStr = new StringBuffer();
		qryStr.append("select distinct ptype From EgPartytype ptype where ptype.createdby is not null ");
		Query qry = getSession().createQuery(qryStr.toString());

		if (code != null && !code.equals("")) {
			qryStr.append(" and (upper(ptype.code) like :code)");
			qry = getSession().createQuery(qryStr.toString());
		}
		if (parentCode != null && !parentCode.equals("")) {
			qryStr.append(" and (upper(ptype.egPartytype.code) like :parentCode)");
			qry = getSession().createQuery(qryStr.toString());
		}
		if (description != null && !description.equals("")) {
			qryStr.append(" and (upper(ptype.description) like :description)");
			qry = getSession().createQuery(qryStr.toString());
		}

		if (code != null && !code.equals("")) {
			qry.setString("code", "%" + code.toUpperCase().trim() + "%");
		}
		if (parentCode != null && !parentCode.equals("")) {
			qry.setString("parentCode", "%" + parentCode.toUpperCase().trim() + "%");
		}
		if (description != null && !description.equals("")) {
			qry.setString("description", "%" + description.toUpperCase().trim() + "%");
		}

		return qry.list();
	}

	public EgPartytype getPartytypeByCode(final String code) {
		final Query qry = getSession().createQuery("from EgPartytype pt where code=:code");
		qry.setString("code", code);
		return (EgPartytype) qry.uniqueResult();
	}

	public List<EgPartytype> getSubPartyTypesForCode(final String code) {
		final Query qry = getSession().createQuery("from EgPartytype pt where pt.egPartytype in (select pt1.id from EgPartytype pt1 where pt1.code=:code) and pt.egPartytype is not null");
		qry.setString("code", code);
		return qry.list();
	}

}
