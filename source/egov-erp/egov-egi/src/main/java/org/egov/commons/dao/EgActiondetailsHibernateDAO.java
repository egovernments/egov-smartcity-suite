/*
 * @(#)EgActiondetailsHibernateDAO.java 3.0, 11 Jun, 2013 2:23:23 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.EgActiondetails;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgActiondetailsHibernateDAO extends GenericHibernateDAO {
	
	public EgActiondetailsHibernateDAO(){
		super(EgActiondetails.class, null);
	}
	
	public EgActiondetailsHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List<EgActiondetails> getEgActiondetailsFilterBy(final String moduleId, final ArrayList<String> actionType, final String moduleType) {
		final Query qry = getCurrentSession().createQuery("from EgActiondetails ad where ad.moduleid =:moduleId and ad.actiontype in (:actionType) and ad.moduletype=:moduleType order by lastmodifieddate");
		qry.setString("moduleId", moduleId);
		qry.setParameterList("actionType", actionType);
		qry.setString("moduleType", moduleType);
		return qry.list();
	}

	public EgActiondetails getEgActiondetailsByWorksdetailId(final String moduleId, final String actionType, final String moduleType) {
		final Query qry = getCurrentSession().createQuery("from EgActiondetails ad where ad.moduleid =:moduleId and ad.actiontype =:actionType and ad.moduletype=:moduleType order by lastmodifieddate");
		qry.setString("moduleId", moduleId);
		qry.setString("actionType", actionType);
		qry.setString("moduleType", moduleType);
		return (EgActiondetails) qry.uniqueResult();
	}

	public List<EgActiondetails> getEgActiondetailsFilterBy(final ArrayList<String> actionType, final String moduleType) {
		final Query qry = getCurrentSession().createQuery("from EgActiondetails ad where ad.actiontype in (:actionType) and ad.moduletype=:moduleType order by lastmodifieddate");
		qry.setParameterList("actionType", actionType);
		qry.setString("moduleType", moduleType);
		return qry.list();
	}
}