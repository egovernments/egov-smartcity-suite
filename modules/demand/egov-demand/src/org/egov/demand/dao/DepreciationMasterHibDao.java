/*
 * DepreciationMasterHibDao.java Created on Nov 24, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.demand.dao;

import java.util.List;

import org.egov.commons.Installment;
import org.egov.demand.model.DepreciationMaster;
import org.egov.infstr.commons.Module;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the puprpose of the class/interface
 * 
 * @author Administrator
 * @version 1.00 
 * @see	    
 * @see	    
 * @since   1.00
 */
public class DepreciationMasterHibDao extends GenericHibernateDAO implements
		DepreciationMasterDao
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public DepreciationMasterHibDao(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.DCB.dao.DepreciationMasterDao#getDepreciationMaster(java.lang.Integer)
	 */
	public DepreciationMaster getDepreciationMaster(Module mod,Integer year)
	{
		Query qry = getSession().createQuery("from DepreciationMaster DM where DM.module=:mod and DM.year=:year ");//and DM.IsHistory='N' ");
		qry.setEntity("mod", mod);
		qry.setInteger("year",year);
		return (DepreciationMaster)qry.uniqueResult();
	}

	/**
	 * Added By Rajalakshmi D.N. on 07/05/2007
	 * Description : Returns the Non-History Depreciation for the Given Module, for the given Year and for the given Installment 
	 * @param Module,Year and Installment 
	 * @return DepreciationMaster record
	 */
	
	public DepreciationMaster getNonHistDepMasterByModuleInsYr(Module mod,Integer year,Installment insYear)
	{
		Query qry = getSession().createQuery("from DepreciationMaster DM where DM.module=:mod and DM.year=:year and DM.IsHistory='N' and DM.startInstallment=:insYear ");
		qry.setEntity("mod", mod);
		qry.setInteger("year",year);
		qry.setEntity("insYear",insYear);
		return (DepreciationMaster)qry.uniqueResult();
	}
	
	
	public List getDepreciationsForModule(Module mod) {
		// TODO Auto-generated method stub
		Query qry = getSession().createQuery("from DepreciationMaster DM where DM.module=:module");
		qry.setEntity("module", mod);
		return qry.list();
	}

	public List getDepreciationsForModulebyHistory(Module mod) {
		
		Query qry = getSession().createQuery("from DepreciationMaster DM where DM.module=:module and DM.IsHistory='N'");
		qry.setEntity("module", mod);
		return qry.list();
	}
	
	public Float getDepreciationPercent(Integer year)
	{
		Query qry = getSession().createQuery("select DM.depreciationPct from DepreciationMaster DM where DM.year=:year");
		qry.setInteger("year",year);
		return (Float)qry.uniqueResult();	
	}
	
    
    public List getAllNonHistoryDepreciationRates()
    {
        Query qry = getSession().createQuery("from DepreciationMaster DM where  and DM.IsHistory='N'");
        return qry.list();
    }
    
    public Float getLeastDepreciationPercent(Integer year)
	{
		Float leastDeprePercent=null;
		if(year!=null && year!=0)
		{
		Float deprePercent=getDepreciationPercent(year);
		if(deprePercent==null || deprePercent==0)
		{
			Query qry = getSession().createQuery("select min(DM.year) from DepreciationMaster DM ");
			Integer leastDepreYear=(Integer)qry.uniqueResult();	
			leastDeprePercent=getDepreciationPercent(leastDepreYear);
		}
		else
			leastDeprePercent=deprePercent;
			
		}
		return leastDeprePercent;
	}
    
}
