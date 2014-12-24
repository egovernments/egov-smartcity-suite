/*
 * PropertyUsageDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.ptis.domain.entity.property.PropertyUsage;

/**
 * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for PropertyUsage
 * 
 * @author Neetu
 * @version 2.00
 */

public interface PropertyUsageDAO extends org.egov.infstr.dao.GenericDAO {
	
	PropertyUsage getPropertyUsage(String usageCode);
	PropertyUsage getPropertyUsage(String usageCode, Date fromDate);
	List<PropertyUsage> getAllActivePropertyUsage();
	List<PropertyUsage> getAllPropertyUsage();
	public List getPropUsageAscOrder();
}
