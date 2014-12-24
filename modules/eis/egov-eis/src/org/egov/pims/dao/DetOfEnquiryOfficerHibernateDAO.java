/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.DetOfEnquiryOfficer;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the DetOfEnquiryOfficerDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class DetOfEnquiryOfficerHibernateDAO extends GenericHibernateDAO implements DetOfEnquiryOfficerDAO
{
    
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public DetOfEnquiryOfficerHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public DetOfEnquiryOfficer getDetOfEnquiryOfficerByID(Integer enquiryOfficerId)
	{
		Query qry = getSession().createQuery("from DetOfEnquiryOfficer B where B.enquiryOfficerId =:enquiryOfficerId ");
		qry.setInteger("enquiryOfficerId", enquiryOfficerId);
		return (DetOfEnquiryOfficer)qry.uniqueResult();
	}
	
}


