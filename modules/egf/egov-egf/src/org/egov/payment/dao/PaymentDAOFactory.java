/*
 * PaymentDAOFactory.java Created on Mar 11, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payment.dao;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public abstract class PaymentDAOFactory 
{
	private static final PaymentDAOFactory EJB3_PERSISTENCE = null;
	private static final PaymentDAOFactory HIBERNATE = new PaymentHibernateDAOFactory();
	
	public static PaymentDAOFactory getDAOFactory()
	{
		return HIBERNATE;
	}
	
	public abstract SubledgerpaymentheaderHibernateDAO getSubledgerpaymentheaderDAO();
    public abstract PaymentheaderHibernateDAO getPaymentheaderDAO();    
}  