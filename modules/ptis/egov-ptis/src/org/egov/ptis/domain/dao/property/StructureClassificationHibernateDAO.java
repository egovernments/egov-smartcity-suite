/*
 * StructureClassificationHibernateDAO.java Created on Oct 5, 2005
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the StructureClassificationDAO for the Hibernate specific 
 * Implementation 
 * @author Neetu
 * @version 2.00
 * @author Srikanth
 * Added new method for factor retrieval
 */

public class StructureClassificationHibernateDAO extends GenericHibernateDAO implements StructureClassificationDAO {

	/**
	 * @param persistentClass
	 * @param session
	 */
	public StructureClassificationHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	/**
	 * To get all StructuralClassification data
	 * @return {@link StructureClassification} List
	 * */
	public List  getAllStructureClassification() {
		Query qry = getSession().createQuery("FROM StructureClassification SC WHERE ((SC.toDate IS NULL AND SC.fromDate <= :currDate) "+ 
		"OR (SC.fromDate <= :currDate AND SC.toDate >= :currDate))");
		qry.setDate("currDate", new Date());
		return qry.list();
	}

	/**
	 * To get the construction cost by Construction Type Code and Floor Number
	 * @param constrTypeCode
	 * @param floorNum
	 * @return {@link StructureClassification} StructureClassification
	 * */
	public StructureClassification getStructureClassification(String constrTypeCode, Integer floorNum) {
		Query qry = getSession().createQuery("FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode AND SC.floorNum =:floorNum");
		qry.setString("constrTypeCode",constrTypeCode);
		qry.setInteger("floorNum",floorNum);
		return  (StructureClassification)qry.uniqueResult();
	}

	/**
	 * To get the construction cost by Construction Type Code, From Date and Floor Number
	 * @param constrTypeCode
	 * @param fromDate
	 * @param floorNum
	 * @return {@link StructureClassification} StructureClassification
	 **/
	public StructureClassification getStructureClassification(String constrTypeCode, Date fromDate,Integer floorNum) {
		Query qry = getSession().createQuery("FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode "+
				"AND SC.floorNum =:floorNum AND ((SC.toDate IS NULL AND SC.fromDate <= :fromDate) "+ 
		"OR (SC.fromDate <= :fromDate AND SC.toDate >= :fromDate))");
		qry.setString("constrTypeCode",constrTypeCode);
		qry.setInteger("floorNum",floorNum);
		qry.setDate("fromDate",fromDate);
		return  (StructureClassification)qry.uniqueResult();
	}


	/**
	 * To get the construction cost by Construction Type
	 * @param constrTypeCode
	 * @return {@link StructureClassification}StructureClassification
	 * */
	public StructureClassification getStructureClassification(String constrTypeCode) {
		Query qry = getSession().createQuery("FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode " +
		"AND ((SC.toDate IS NULL AND SC.fromDate <= :currDate) OR (SC.fromDate <= :currDate AND SC.toDate >= :currDate))");
		qry.setString("constrTypeCode",constrTypeCode);
		qry.setDate("currDate", new Date());
		return  (StructureClassification)qry.uniqueResult();
	}


	/**
	 * To get the construction cost by Construction Type Code and From Date
	 * @param constrTypeCode
	 * @param fromDate
	 * @return {@link StructureClassification} StructureClassification
	 **/ 
	public StructureClassification getStructureClassification(String constrTypeCode, Date fromDate) {
		Query qry = getSession().createQuery("FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode " +
		"AND ((SC.toDate IS NULL AND SC.fromDate <= :fromDate) OR (SC.fromDate <= :fromDate AND SC.toDate >= :fromDate))");
		qry.setString("constrTypeCode",constrTypeCode);
		qry.setDate("fromDate",fromDate);
		return  (StructureClassification)qry.uniqueResult();
	}
}