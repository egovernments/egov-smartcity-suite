/*
 * BillHibDAO.java
 * Created on May 23, 2007
 *
 * Copyright 2006 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.demand.dao;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;
/**
 * @author Administrator
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class BillHibDAO extends GenericHibernateDAO implements BillDAO {

    /**
     * @param persistentClass
     * @param session
     */
    public BillHibDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
   //This method is not used now because BillPT is changed as Eg_Bill. 
   /* public BillPT getBillByBillNo(String billNo,String type)
	{
		
		Query qry=getSession().createQuery("from org.egov.demand.model.BillPT bil where  bil.billNumber =:billNo and billType=:type");
		qry.setString("billNo", billNo);
		qry.setString("type", type);
		return (BillPT)qry.uniqueResult();
	}*/

}
