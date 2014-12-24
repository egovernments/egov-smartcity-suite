/*
 * PropertyDAO.java Created on Oct 05 , 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.util.List;

import org.egov.pims.model.DisciplinaryPunishment;


/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Property
 *
 * @author deepak
 * @version 2.00
 */
public interface DisciplinaryPunishmentDAO extends org.egov.infstr.dao.GenericDAO
{
	public DisciplinaryPunishment getDisciplinaryPunishmentByID(Integer disciplinaryPunishmentId) ;
	

}
