/*
 * DeductionsHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 package org.egov.payroll.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.web.actions.providentfund.CpfTriggerAction;
import org.hibernate.Query;
import org.hibernate.Session;



/**
 * This Class implememets the SalaryCodesDAO for the Hibernate specific
 * Implementation
 *
 * @author Lokesh
 * @version 2.00
 */

public class DeductionsHibernateDAO extends GenericHibernateDAO implements DeductionsDAO {

    
    private static final Logger LOGGER = Logger.getLogger(DeductionsHibernateDAO.class);
	public DeductionsHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    
    public List getAllOtherDeductionChartOfAccount()throws Exception{
    	try{
	    	Query qry = getSession().createQuery("select distinct(d.chartofaccounts.name) from Deductions d, " +
	    				"CChartOfAccounts c where d.chartofaccounts.id = c.id");    	
	    	return qry.list();
    	}catch(Exception e){
    		LOGGER.error(e.getMessage());
    		throw e;
    	}
    }
    
    
}

