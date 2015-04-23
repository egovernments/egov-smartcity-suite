/*
 * PropertyIDHibernateDAO.java Created on Dec 1, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyIDDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */

public class PropertyIDHibernateDAO extends GenericHibernateDAO implements PropertyIDDAO
{
	private final static Logger LOGGER = Logger.getLogger(PropertyIDHibernateDAO.class);
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertyIDHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}


	/*public PropertyID getPropertyIDByUPICNo(String upicNo)
	{
		logger.info(">>>>>>>>>>>>>>>>>> upicNo"+upicNo);
		Query qry = getCurrentSession().createQuery("from PropertyID PD where PD.id_PropertyId =:upicNo");
		qry.setString("upicNo", upicNo);
        return (PropertyID)qry.uniqueResult();
	}*/
    public List getPropertyIDByBoundry(Integer zoneID,Integer wardID,Integer colonyID)
    {
        LOGGER.info(">>>>>>>>>>>>>>>>>> colonyId"+zoneID+":::::::"+wardID+":::::::::::"+colonyID);
        Query qry = getCurrentSession().createQuery("from PropertyID PD where PD.zoneId=:zoneID And PD.wardId=:wardID And PD.colonyId =:colonyID");
        qry.setInteger("zoneID", zoneID);
        qry.setInteger("wardID", wardID);
        qry.setInteger("colonyID", colonyID);
        LOGGER.info(">>>>>>>>>>>>>>>>>> After Qry");
        return qry.list();
       // return (PropertyID)qry.uniqueResult();
    }
    public List getPropertyIDByBoundryForWardBlockStreet(Integer wardID,Integer blockID,Integer streetID)
    {
        LOGGER.info(">>>>>>>>>>>>>>>>>> streetID"+wardID+":::::::"+blockID+":::::::::::"+streetID);
        Query qry = getCurrentSession().createQuery("from PropertyID PD where PD.wardId=:wardID And PD.blockId=:blockID And PD.streetId =:streetID");
        qry.setInteger("wardID", wardID);
        qry.setInteger("blockID", blockID);
        qry.setInteger("streetID", streetID);
        LOGGER.info(">>>>>>>>>>>>>>>>>> After Qry");
        return qry.list();
       // return (PropertyID)qry.uniqueResult();
    }
    
    
    
    public PropertyID getPropertyByBoundryAndMunNo(Integer zoneID,Integer wardID,Integer colonyID, Integer munNo)
    {
        LOGGER.info(">>>>>>>>>>>>>>>>>> colonyId"+colonyID+":::::::::::::::munNo:"+munNo);
        Query qry = getCurrentSession().createQuery("from PropertyID PD where PD.zoneId=:zoneID And PD.wardId=:wardID And PD.colonyId =:colonyID and PD.doorNum =:munNo");
        qry.setInteger("zoneID", zoneID);
        qry.setInteger("wardID", wardID);
        qry.setInteger("colonyID", colonyID);
        qry.setInteger("munNo", munNo);
          return (PropertyID)qry.uniqueResult();
        
    }
}


