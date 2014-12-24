/*
 * DeductionHibernateDAOFactory.java Created on Oct 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

import org.egov.deduction.model.EgRemittance;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.deduction.model.EgRemittanceGldtl;
import org.egov.deduction.model.Generalledgerdetail;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author MRC
 * @version 1.00 
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>,
 */

public class DeductionHibernateDAOFactory extends DeductionDAOFactory
{
	protected Session getCurrentSession()
    {
		// returns a reference to the current Session.	       
		return HibernateUtil.getCurrentSession();
    }
	
	public EgRemittanceGldtlHibernateDAO getEgRemittanceGldtlDAO()
    {
        return new EgRemittanceGldtlHibernateDAO(EgRemittanceGldtl.class,getCurrentSession());
    }
	
	public EgRemittanceHibernateDAO getEgRemittanceDAO()
    {
        return new EgRemittanceHibernateDAO(EgRemittance.class,getCurrentSession());
    }
	
	public GeneralledgerdetailHibernateDAO getGeneralledgerdetailDAO()
    {
        return new GeneralledgerdetailHibernateDAO(Generalledgerdetail.class,getCurrentSession());
    }
	
	public EgRemittanceDetailHibernateDAO getEgRemittanceDetailDAO()
    {
        return new EgRemittanceDetailHibernateDAO(EgRemittanceDetail.class,getCurrentSession());
    }
		
}

