/*
 * PaymentDAOFactory.java Created on Sep 8, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.masters.dao;

import org.egov.commons.Accountdetailtype;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.masters.model.AccountEntity;
import org.hibernate.Session;
/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public class MastersHibernateDAOFactory extends MastersDAOFactory {

	protected Session getCurrentSession()
    {
		// returns a reference to the current Session.	        
		return HibernateUtil.getCurrentSession();
    }
	
	public AccountEntityHibernateDAO getAccountEntityDAO()
	{
		return new AccountEntityHibernateDAO(AccountEntity.class,getCurrentSession());
	}
	
	public AccountdetailtypeHibernateDAO getAccountdetailtypeDAO()
	{
		return new AccountdetailtypeHibernateDAO(Accountdetailtype.class,getCurrentSession());
	}
}
 