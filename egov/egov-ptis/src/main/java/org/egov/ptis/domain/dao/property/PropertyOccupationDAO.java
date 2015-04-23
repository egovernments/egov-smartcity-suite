/*
 * PropertyOccupationDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;

import org.egov.ptis.domain.entity.property.PropertyOccupation;

/**
 * <p>
 * This is an interface which would be implemented by the Individual Frameworks
 * for all the CRUD (create, read, update, delete) basic data access operations
 * for PropertyOccupation
 * 
 * @author Neetu
 * @version 2.00
 */

public interface PropertyOccupationDAO extends org.egov.infstr.dao.GenericDAO {
	public PropertyOccupation getPropertyOccupationByOccCodeAndUsage(String occCode, Long propertyUsage);

	public PropertyOccupation getPropertyOccupationByOccCode(String occCode);

	public PropertyOccupation getPropertyOccupationByOccCodeAndDate(String occCode, Date frmDate);
}
