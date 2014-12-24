/*
 * GeneralledgerdetailHibernateDAO.java Created on Oct 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.deduction.model.Generalledgerdetail;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00 
 */

public class GeneralledgerdetailHibernateDAO extends GenericHibernateDAO
{
	public GeneralledgerdetailHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	
	public List<Generalledgerdetail> getGeneralledgerdetailByFilterBy(Integer voucherHeaderId, Integer purposeId)
	{		
		Query qry = getSession().createQuery(" from Generalledgerdetail gld where gld.generalledger.voucherHeaderId =:voucherHeaderId  " +
				"and gld.generalledger.glcodeId in(select id from CChartOfAccounts where purposeId =:purposeId) ");
		qry.setInteger("voucherHeaderId", voucherHeaderId);
		qry.setInteger("purposeId", purposeId);
		return qry.list();
	}
	
	public List<Generalledgerdetail> getGeneralledgerdetailByGlCodeId(Integer glcodeId)
	{		
		Query qry = getSession().createQuery(" from Generalledgerdetail gld where gld.generalledger.glcodeId =:glcodeId");
		qry.setInteger("glcodeId", glcodeId);
		return qry.list();
	}
	
	public List<Generalledgerdetail> getGeneralledgerdetailByVhId(Integer vhId)
	{		
		Query qry = getSession().createQuery(" from Generalledgerdetail gld where gld.generalledger.voucherHeaderId =:vhId");
		qry.setInteger("vhId", vhId);
		return qry.list();
	}
}

