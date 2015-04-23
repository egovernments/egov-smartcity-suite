/*
 * PropertyUsageHibernateDAO.java Created on October 5, 2005
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This Class implememets the PropertyUsageDAO for the Hibernate specific 
 * Implementation 
 * @author Neetu
 * @version 2.00
 * @author Srikanth
 * Change added new method to get Property Usage by date 
 */

public class PropertyUsageHibernateDAO extends GenericHibernateDAO implements PropertyUsageDAO {
	
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertyUsageHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
	
	/**
	 * To get Property Usage by Usage Code
	 * @param usageCode
	 * @param fromDate
	 * @return PropertyUsage
	 * */
	public PropertyUsage getPropertyUsage(String usageCode) {
		Query qry = getCurrentSession().createQuery("from PropertyUsage PU where PU.usageCode = :usageCode AND PU.usageCode IS NOT NULL AND (" +
										   "(PU.toDate IS NULL AND PU.fromDate <= :currDate) " + "OR " +
										   "(PU.fromDate <= :currDate AND PU.toDate >= :currDate)) ");
		qry.setString("usageCode", usageCode);
		qry.setDate("currDate", new Date());
		return (PropertyUsage)qry.uniqueResult();
	}
	
	/**
	 * To get Property Usage by Usage Code and From Date given
	 * @param usageCode
	 * @param fromDate
	 * @return PropertyUsage
	 * */
	public PropertyUsage getPropertyUsage(String usageCode, Date fromDate) {
		Query qry = getCurrentSession().createQuery("from PropertyUsage PU where PU.usageCode = :usageCode AND PU.usageCode IS NOT NULL AND (" +
										   "(PU.toDate IS NULL AND PU.fromDate <= :fromDate) " + "OR " +
										   "(PU.fromDate <= :fromDate AND PU.toDate >= :fromDate)) ");
		qry.setString("usageCode", usageCode);
		qry.setDate("fromDate", fromDate);
		return (PropertyUsage)qry.uniqueResult();
	}
    /**
	 * To get All active Property Usages
	 * @return List of PropertyUsages
	 * */
	public List<PropertyUsage> getAllActivePropertyUsage(){
		Query qry = getCurrentSession().createQuery("from PropertyUsage PU where PU.isEnabled = " + 1 + " AND (" +
				   "(PU.toDate IS NULL AND PU.fromDate <= :currDate) " + "OR " +
				   "(PU.fromDate <= :currDate AND PU.toDate >= :currDate))");
		qry.setDate("currDate", new Date());
		return (List)qry.list();
	}
		
    /**
	 * To get All Property Usages
	 * @return List of PropertyUsages
	 * */
	public List<PropertyUsage> getAllPropertyUsage(){
		Query qry = getCurrentSession().createQuery("from PropertyUsage PU where (" +
				   "(PU.toDate IS NULL AND PU.fromDate <= :currDate) " + "OR " +
				   "(PU.fromDate <= :currDate AND PU.toDate >= :currDate))");
		qry.setDate("currDate", new Date());
		return (List)qry.list();
	}
	
	public List getPropUsageAscOrder()
	{
		Criteria criteria=getCurrentSession().createCriteria(PropertyUsage.class)
		.addOrder(Order.asc("id"));
		return criteria.list();
	}
}