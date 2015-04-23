/*
 * PaymentheaderHibernateDAO.java Created on Mar 11, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payment.dao;

import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payment.model.Subledgerpaymentheader;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */
public class SubledgerpaymentheaderHibernateDAO extends GenericHibernateDAO
{

	public SubledgerpaymentheaderHibernateDAO(Class persistentClass, Session session)
	{
			super(persistentClass, session);
	}
	
	public List<Subledgerpaymentheader> getSubledgerpaymentheaderByVoucherHeader(CVoucherHeader voucherHeader)
	{
		Query qry =HibernateUtil.getCurrentSession().createQuery("from Subledgerpaymentheader where voucherheader =:voucherHeader");
		qry.setEntity("voucherHeader", voucherHeader);
		return qry.list();
	}
}
