/*
 * EgRemittanceDetailHibernateDAO.java Created on Oct 11, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.deduction.model.EgRemittance;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.deduction.model.EgRemittanceGldtl;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00 
 */

public class EgRemittanceDetailHibernateDAO extends GenericHibernateDAO
{
	public EgRemittanceDetailHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);

	}
	
	public List<EgRemittanceDetail> getEgRemittanceDetailByEgRmt(EgRemittance egRmt)
	{
		Query qry = null;//HibernateUtil.getCurrentSession().createQuery("from EgRemittanceDetail erd where erd.egRemittance =:egRmt");
		qry.setEntity("egRmt", egRmt);///This fix is for Phoenix Migration.
		return qry.list();
	}
	
	public EgRemittanceDetail getEgRemittanceDetailFilterBy(EgRemittance egRmt, EgRemittanceGldtl egRmtGldtl)
	{
		Query qry =null;/*HibernateUtil.getCurrentSession().createQuery("from EgRemittanceDetail erd where erd.egRemittance =:egRmt and erd.egRemittanceGldtl =:egRmtGldtl");
		qry.setEntity("egRmt", egRmt);
		qry.setEntity("egRmtGldtl", egRmtGldtl);*/
		//This fix is for Phoenix Migration.
		return (EgRemittanceDetail)qry.uniqueResult();
	}
}

