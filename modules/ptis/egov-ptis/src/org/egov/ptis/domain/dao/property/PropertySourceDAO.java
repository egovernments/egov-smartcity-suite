/*
 * PropertySourceDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import org.egov.ptis.domain.entity.property.PropertySource;

/**
   * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for PropertySource
 * 
 * @author Neetu
 * @version 2.00
 */

public interface PropertySourceDAO extends org.egov.infstr.dao.GenericDAO
{
	PropertySource getPropertySourceByCode(String propSrcCode);
}
