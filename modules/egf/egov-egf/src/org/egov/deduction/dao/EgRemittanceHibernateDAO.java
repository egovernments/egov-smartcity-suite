/*
 * EgRemittanceHibernateDAO.java Created on Oct 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.egov.deduction.model.EgRemittance;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.model.recoveries.Recovery;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00   
 */

public class EgRemittanceHibernateDAO extends GenericHibernateDAO
{
	private final static  Logger LOGGER = Logger.getLogger(EgRemittanceHibernateDAO.class);
	public EgRemittanceHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}	
	public List<EgRemittance> getEgRemittanceFilterBy(Fund fund, Recovery recovery, String month, CFinancialYear financialyear)
	{
		Query qry;
		StringBuffer qryStr = new StringBuffer();
		List<EgRemittance> egRemittanceList=null;
		qryStr.append("From EgRemittance rmt where rmt.voucherheader.type='Payment' and rmt.voucherheader.status=0");
		qry = getSession().createQuery(qryStr.toString()) ;
		if(fund!=null)
	    {
	    	qryStr.append(" and (rmt.fund = :fund)");
	    	qry = getSession().createQuery(qryStr.toString());
	    }
		if(recovery!=null)
	    {
	    	qryStr.append(" and (rmt.tds = :recovery)");
	    	qry = getSession().createQuery(qryStr.toString()) ;
	    }
	    if(month!=null)
	    {
	    	qryStr.append(" and (rmt.month = :month)");
	    	qry = getSession().createQuery(qryStr.toString()) ;
	    }
	    if(financialyear!=null)
	    {
	    	qryStr.append(" and (rmt.financialyear =:financialyear)");
	    	qry = getSession().createQuery(qryStr.toString()) ;
	    }
	    
	    qryStr.append(" order by upper(rmt.tds.type)");
	    qry = getSession().createQuery(qryStr.toString()) ;
	   
	    if(fund!=null)
	    	qry.setEntity("fund",fund);
	    if(recovery!=null)
	       	qry.setEntity("recovery",recovery);
	    if(month!=null)
	      	qry.setString("month",month);
	    if(financialyear!=null)
	      	qry.setEntity("financialyear",financialyear);
	  
	    LOGGER.debug("qryStr "+qryStr.toString());
	    egRemittanceList=qry.list();
        return egRemittanceList;
	}
}

