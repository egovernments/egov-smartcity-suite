/*
 * PropertyStatusDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.ptis.domain.entity.property.PropertyStatus;

/**
 * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for PropertyStatus
 * 
 * @author Neetu
 * @version 2.00
 */

public interface PropertyStatusDAO extends org.egov.infstr.dao.GenericDAO
{
	public PropertyStatus getPropertyStatusByName(String status);
	public PropertyStatus getPropertyStatusByCode(String statusCode);
}
