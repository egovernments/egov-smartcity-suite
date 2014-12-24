/*
 * PropertyOccupationHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyOccupationDAO for the Hibernate specific
 * Implementation
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyOccupationHibernateDAO extends GenericHibernateDAO implements PropertyOccupationDAO {
	/**
	 * @param persistentClass
	 * @param session
	 */
	public PropertyOccupationHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public PropertyOccupation getPropertyOccupationByOccCodeAndUsage(String occCode, Long propertyUsage) {
		Query qry = null;
		PropertyOccupation propOcc = null;
		if (occCode != null && propertyUsage != null) {
			qry = getSession()
					.createQuery(
							"from PropertyOccupation PO where PO.occupancyCode = :occCode and PO.propertyUsage.id = :propertyUsage and :date between PO.fromDate and PO.toDate ");
			qry.setString("occCode", occCode);
			qry.setLong("propertyUsage", propertyUsage);
			qry.setDate("date", new Date());
			if (qry.list().size() == 1)
				propOcc = (PropertyOccupation) qry.uniqueResult();
		}
		return propOcc;
	}

	public PropertyOccupation getPropertyOccupationByOccCode(String occCode) {
		PropertyOccupation propOcc = null;
		Query qry = null;
		if (occCode != null) {
			qry = getSession().createQuery(
					"from PropertyOccupation PO where PO.occupancyCode = :occCode AND ("
							+ "(PO.toDate IS NULL AND PO.fromDate <= :currDate) " + "OR "
							+ "(PO.fromDate <= :currDate AND PO.toDate >= :currDate)) ");
			qry.setString("occCode", occCode);
			qry.setDate("currDate", new Date());
			if (qry.list().size() == 1)
				propOcc = (PropertyOccupation) qry.uniqueResult();
		}
		return propOcc;
	}

	public PropertyOccupation getPropertyOccupationByOccCodeAndDate(String occCode, Date fromDate) {
		PropertyOccupation propOcc = null;
		Query qry = null;
		if (occCode != null) {
			qry = getSession().createQuery(
					"from PropertyOccupation PO where PO.occupancyCode = :occCode AND ("
							+ "(PO.toDate IS NULL AND PO.fromDate <= :fromDate) " + "OR "
							+ "(PO.fromDate <= :fromDate AND PO.toDate >= :fromDate)) ");
			qry.setString("occCode", occCode);
			qry.setDate("fromDate", fromDate);
			if (qry.list().size() == 1)
				propOcc = (PropertyOccupation) qry.uniqueResult();
		}
		return propOcc;
	}
}
