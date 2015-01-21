/*
 * @(#)EgwTypeOfWorkHibernateDAO.java 3.0, 11 Jun, 2013 2:36:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgwTypeOfWorkHibernateDAO extends GenericHibernateDAO {

	public EgwTypeOfWorkHibernateDAO() {
		super(EgwTypeOfWork.class, null);
	}

	public EgwTypeOfWorkHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List getAllTypeOfWork() {
		return getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork order by upper(code)").list();
	}

	public List getAllTypeOfWorkOrderByDesc() {
		return getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork order by upper(description)").list();
	}

	public EgwTypeOfWork getTypeOfWorkById(final Long id) {
		final Query qry = getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork where typeOfWork.id =:id");
		qry.setLong("id", id);
		return (EgwTypeOfWork) qry.uniqueResult();
	}

	public EgwTypeOfWork findByCode(final String code) {
		final Query qry = getCurrentSession().createQuery("from EgwTypeOfWork typeOfWork where upper(typeOfWork.code) =:code");
		qry.setString("code", code.toUpperCase().trim());
		return (EgwTypeOfWork) qry.uniqueResult();
	}

	/**
	 * @param code
	 * @param parentCode
	 * @param description
	 * @return list of EgwTypeOfWork filtered by optional conditions
	 */
	public List<EgwTypeOfWork> getTypeOfWorkDetailFilterBy(final String code, final String parentCode, final String description) {
		final StringBuffer qryStr = new StringBuffer();
		qryStr.append("select distinct typeOfWork From EgwTypeOfWork typeOfWork where typeOfWork.createdby is not null ");
		if (code != null && !code.equals("")) {
			qryStr.append(" and (upper(typeOfWork.code) like :code)");
		}
		if (parentCode != null && !parentCode.equals("")) {
			qryStr.append(" and (upper(typeOfWork.parentid.code) like :parentCode)");
		}
		if (description != null && !description.equals("")) {
			qryStr.append(" and (upper(typeOfWork.description) like :description)");
		}
		final Query qry = getCurrentSession().createQuery(qryStr.toString());
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

	/**
	 * @param code
	 * @param parentCode
	 * @param description
	 * @param partyTypeCode
	 * @return list of EgwTypeOfWork filtered by optional conditions
	 */
	public List<EgwTypeOfWork> getTypeOfWorkDetailFilterByParty(final String code, final String parentCode, final String description, final String partyTypeCode) {
		final StringBuffer qryStr = new StringBuffer();
		qryStr.append("select distinct typeOfWork From EgwTypeOfWork typeOfWork where typeOfWork.egPartytype is not null ");

		if (code != null && !code.equals("")) {
			qryStr.append(" and (upper(typeOfWork.code) like :code)");
		}
		if (parentCode != null && !parentCode.equals("")) {
			qryStr.append(" and (upper(typeOfWork.parentid.code) like :parentCode)");
		}
		if (description != null && !description.equals("")) {
			qryStr.append(" and (upper(typeOfWork.description) like :description)");
		}
		if (partyTypeCode != null && !partyTypeCode.equals("")) {
			qryStr.append(" and (upper(typeOfWork.egPartytype.code) like :partyTypeCode)");
		}
		final Query qry = getCurrentSession().createQuery(qryStr.toString());
		if (code != null && !code.equals("")) {
			qry.setString("code", "%" + code.toUpperCase().trim() + "%");
		}
		if (parentCode != null && !parentCode.equals("")) {
			qry.setString("parentCode", "%" + parentCode.toUpperCase().trim() + "%");
		}
		if (description != null && !description.equals("")) {
			qry.setString("description", "%" + description.toUpperCase().trim() + "%");
		}
		if (partyTypeCode != null && !partyTypeCode.equals("")) {
			qry.setString("partyTypeCode", "%" + partyTypeCode.toUpperCase().trim() + "%");
		}

		return qry.list();
	}

	public List<EgwTypeOfWork> getAllParentOrderByCode() {
		return getCurrentSession().createQuery("from EgwTypeOfWork etw1 where etw1.parentid is null and etw1.id in (select etw2.parentid from EgwTypeOfWork etw2 where etw2.parentid = etw1.id) order by upper(code)").list();
	}

	public List findAllParentPartyType() {
		return getCurrentSession().createQuery("from EgwTypeOfWork tw where tw.parentid is null and tw.egPartytype is not null order by upper(code)").list();
	}

	public List findAllChildPartyType() {
		return getCurrentSession().createQuery("from EgwTypeOfWork tw where tw.parentid is not null and tw.egPartytype is not null order by upper(code)").list();
	}

}
