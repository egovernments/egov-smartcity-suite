/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.DisciplinaryPunishment;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the DisciplinaryPunishmentDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class DisciplinaryPunishmentHibernateDAO extends GenericHibernateDAO implements DisciplinaryPunishmentDAO
{
    /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public DisciplinaryPunishmentHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public DisciplinaryPunishment getDisciplinaryPunishmentByID(Integer disciplinaryPunishmentId)
	{
		Query qry = getSession().createQuery("from DisciplinaryPunishment D where D.id =:disciplinaryPunishmentId ");
		qry.setInteger("disciplinaryPunishmentId", disciplinaryPunishmentId);
		return (DisciplinaryPunishment)qry.uniqueResult();
	}
	
}


