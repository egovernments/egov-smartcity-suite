/*
 * PropertyTypeMasterHibernateDAO Created on 15 Dec, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyTypeMasterDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyTypeMasterHibernateDAO extends GenericHibernateDAO implements PropertyTypeMasterDAO
{
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertyTypeMasterHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	
	public  PropertyTypeMaster getPropertyTypeMasterByName(String type)
	{
		Query qry = getCurrentSession().createQuery("from PropertyTypeMaster P where P.type =:type ");
		qry.setString("type", type);
		return (PropertyTypeMaster)qry.uniqueResult();
	}
    public  PropertyTypeMaster getPropertyTypeMasterById(Integer id){
        Query qry = getCurrentSession().createQuery("from PropertyTypeMaster P where P.id =:id ");
        qry.setInteger("id", id);
        return (PropertyTypeMaster)qry.uniqueResult(); 
    }
    public PropertyTypeMaster getPropertyTypeMasterByCode(String code)
    {
    	Query qry = getCurrentSession().createQuery("from PropertyTypeMaster P where P.code =:Code ");
        qry.setString("Code", code);
        return (PropertyTypeMaster)qry.uniqueResult(); 
    }
}
  

