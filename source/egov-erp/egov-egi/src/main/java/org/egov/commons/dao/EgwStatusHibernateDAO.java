/*
 * @(#)EgwStatusHibernateDAO.java 3.0, 11 Jun, 2013 2:33:05 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgwStatusHibernateDAO extends GenericHibernateDAO {
	
	public EgwStatusHibernateDAO() {
		super(EgwStatus.class,null);
	}
	
	public EgwStatusHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List<EgwStatus> getEgwStatusFilterByStatus(final ArrayList<Integer> statusId) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus egs where egs.id in (:statusId)  order by orderId");
		qry.setParameterList("statusId", statusId);
		return qry.list();
	}

	/**
	 * @param moduleType Module type
	 * @param statusCode Status code
	 * @return EgwStatus object for given module type and status code
	 */
	public EgwStatus getStatusByModuleAndCode(final String moduleType, final String code) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType and S.code =:code");
		qry.setString("moduleType", moduleType);
		qry.setString("code", code);
		return (EgwStatus) qry.uniqueResult();
	}

	public List<EgwStatus> getStatusByModule(final String moduleType) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType  order by orderId");
		qry.setString("moduleType", moduleType);
		return qry.list();
	}

	/**
	 * @param moduleType Module type
	 * @param codeList List of status codes
	 * @return List of all EgwStatus objects filtered by given module type and list of status codes
	 */
	public List<EgwStatus> getStatusListByModuleAndCodeList(final String moduleType, final List codeList) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType and S.code in(:codeList)  order by orderId");
		qry.setString("moduleType", moduleType);
		qry.setParameterList("codeList", codeList);
		return qry.list();
	}

	public EgwStatus getEgwStatusByCode(final String code) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.code =:code ");
		qry.setString("code", code);
		return (EgwStatus) qry.uniqueResult();
	}
}
