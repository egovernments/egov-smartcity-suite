/*
 * PropertyDetailHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyDetailHibernateDAO extends GenericHibernateDAO implements PropertyDetailDAO {
	/**
	 * @param persistentClass
	 * @param session
	 */

	private static final Logger LOGGER = Logger.getLogger(PropertyDetailHibernateDAO.class);

	public PropertyDetailHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	/*
	 * public PropertyDetail getPropertyDetailByPropertyDetailsID(String
	 * PropertyDetailsID) { Query qry =getSession().createQuery(
	 * "from PropertyDetail PD where pd.PropertyDetailsID =: PropertyDetailsID "
	 * ); qry.setString("PropertyDetailsID", PropertyDetailsID); return
	 * (PropertyDetail)qry.uniqueResult(); }
	 */

	public PropertyDetail getPropertyDetailByProperty(Property property) {
		Query qry = getCurrentSession().createQuery(
				"from PropertyDetail PD where PD.property = :property ");
		qry.setEntity("property", property);
		return (PropertyDetail) qry.uniqueResult();
	}

	public PropertyDetail getPropertyDetailBySurveyNumber(String surveyNumber) {
		Query qry = getCurrentSession().createQuery(
				"from PropertyDetail PD where PD.property.surveyNumber =: SURVEY_NUM ");
		qry.setString("surveyNumber", surveyNumber);
		return (PropertyDetail) qry.uniqueResult();
	}

	public PropertyDetail getPropertyDetailByRegNum(String regNum) {
		LOGGER.info("getPropertyDetailByRegNum Invoked");

		BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory()
				.getBasicPropertyDAO();
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByRegNum(regNum);

		LOGGER.info("basicProperty : " + basicProperty);

		Query qry = getCurrentSession().createQuery(
				"from PropertyImpl P where P.basicProperty = :BasicProperty ");
		qry.setEntity("BasicProperty", basicProperty);
		Property property = (Property) qry.uniqueResult();
		LOGGER.info("property : " + property);
		Query qry1 = getCurrentSession().createQuery(
				"from PropertyDetail PD where PD.property =:Property ");
		qry1.setEntity("Property", property);

		return (PropertyDetail) qry1.uniqueResult();
	}

}
