/*
 * DeductionDAOFactory.java Created on Oct 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00 
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it.
 * If you add a new persistence mechanism, add an additional concrete factory
 * for it to the enumeration of factories.
 * <p>
 */

public abstract class DeductionDAOFactory
{
	 private static final DeductionDAOFactory HIBERNATE = new DeductionHibernateDAOFactory();


    public static DeductionDAOFactory getDAOFactory()
    {
    	return HIBERNATE;
    }
    // Add your DAO interfaces here
    public abstract EgRemittanceGldtlHibernateDAO getEgRemittanceGldtlDAO();
    public abstract EgRemittanceHibernateDAO getEgRemittanceDAO();
    public abstract GeneralledgerdetailHibernateDAO getGeneralledgerdetailDAO();
    public abstract EgRemittanceDetailHibernateDAO getEgRemittanceDetailDAO();   
}

