/*
 * PaymentDAOFactory.java Created on Mar 11, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payment.dao;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.payment.Paymentheader;
import org.egov.payment.model.Subledgerpaymentheader;
import org.hibernate.Session;
/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public class PaymentHibernateDAOFactory extends PaymentDAOFactory {

	protected Session getCurrentSession()
    {
		// returns a reference to the current Session.	        
		return HibernateUtil.getCurrentSession();
    }
	public SubledgerpaymentheaderHibernateDAO getSubledgerpaymentheaderDAO()
	{
		return new SubledgerpaymentheaderHibernateDAO(Subledgerpaymentheader.class,getCurrentSession());
	}
	public PaymentheaderHibernateDAO getPaymentheaderDAO()
	{
		return new PaymentheaderHibernateDAO(Paymentheader.class,getCurrentSession());
	}
}
 