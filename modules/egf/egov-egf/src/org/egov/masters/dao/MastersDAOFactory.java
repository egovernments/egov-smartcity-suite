/*
 * MastersDAOFactory.java Created on Sep 8, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.masters.dao;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public abstract class MastersDAOFactory 
{
	private static final MastersDAOFactory HIBERNATE = new MastersHibernateDAOFactory();	
	
	public static MastersDAOFactory getDAOFactory()
	{
		return HIBERNATE;
	}
	
	public abstract AccountEntityHibernateDAO getAccountEntityDAO();
	public abstract AccountdetailtypeHibernateDAO getAccountdetailtypeDAO();
}  