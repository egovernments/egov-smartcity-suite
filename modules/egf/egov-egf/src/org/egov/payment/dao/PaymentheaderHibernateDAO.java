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
import org.egov.model.payment.Paymentheader;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public class PaymentheaderHibernateDAO extends GenericHibernateDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PaymentheaderHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
		// TODO Auto-generated constructor stub
	}		
	public List<Paymentheader> getPaymentheaderByVoucherHeader(CVoucherHeader voucherHeader)
	{
		Query qry = getSession().createQuery("from Paymentheader where voucherheader =:voucherHeader");
		qry.setEntity("voucherHeader", voucherHeader);
		return qry.list();
	}
}

