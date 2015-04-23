/*
 * PropertyIDDAO.java Created on Dec 1, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.ptis.domain.entity.property.PropertyID;

/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for PropertyID
 *
 * @author Gayathri Joshi
 * @version 2.00
 * 
 */

public interface PropertyIDDAO extends org.egov.infstr.dao.GenericDAO
{
	//public PropertyID getPropertyIDByUPICNo(String upicNo);
    public List getPropertyIDByBoundry(Integer zoneID,Integer wardID,Integer colonyID);
    public List getPropertyIDByBoundryForWardBlockStreet(Integer wardID,Integer blockID,Integer streetID);
    public PropertyID getPropertyByBoundryAndMunNo(Integer zoneID,Integer wardID,Integer colonyID, Integer munNo);


}
