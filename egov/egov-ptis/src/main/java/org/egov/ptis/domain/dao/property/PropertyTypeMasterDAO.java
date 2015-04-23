/*
 * PropertyTypeMaster.java Created on 15 Dec, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.ptis.domain.entity.property.PropertyTypeMaster;




/**
 * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for PropertyTypeMaster Entity
 * 
 * @author Neetu
 * @version 2.00
 */

public interface PropertyTypeMasterDAO extends org.egov.infstr.dao.GenericDAO
{
	public PropertyTypeMaster getPropertyTypeMasterByName(String type);
    public PropertyTypeMaster getPropertyTypeMasterById(Integer id);
    public PropertyTypeMaster getPropertyTypeMasterByCode(String code);
}
