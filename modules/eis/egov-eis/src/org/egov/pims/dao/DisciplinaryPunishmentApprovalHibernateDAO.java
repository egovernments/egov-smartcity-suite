/*
 * PropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.model.DisciplinaryPunishmentApproval;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the DisciplinaryPunishmentDAO for the Hibernate specific
 * Implementation
 *
 * @author Neetu
 * @version 2.00
 */
public class DisciplinaryPunishmentApprovalHibernateDAO extends GenericHibernateDAO implements DisciplinaryPunishmentApprovalDAO
{
   /*
     *
	 * @param persistentClass
	 * @param session
	 */
	public DisciplinaryPunishmentApprovalHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}
	public DisciplinaryPunishmentApproval getDisciplinaryPunishmentApprovalByID(Integer id)
	{
		Query qry = getSession().createQuery("from DisciplinaryPunishmentApproval D where D.id =:id ");
		qry.setInteger("id", id);
		return (DisciplinaryPunishmentApproval)qry.uniqueResult();
	}
}


