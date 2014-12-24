/*
 * StructureClassificationDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;
import java.util.Date;
import java.util.List;

import org.egov.ptis.domain.entity.property.StructureClassification;

/**
 * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for StructureClassification
 * 
 * @author Neetu
 * @version 2.00
 * @author Srikanth
 * Changing method name and adding new function declaration to get factor
 */

public interface StructureClassificationDAO extends org.egov.infstr.dao.GenericDAO {

	List <StructureClassification> getAllStructureClassification();
 
	StructureClassification getStructureClassification(String constrTypeCode, Integer floorNum);
	
	StructureClassification getStructureClassification(String constrTypeCode, Date fromDate,Integer floorNum);
	
	StructureClassification getStructureClassification(String constrTypeCode);

	StructureClassification getStructureClassification(String constrTypeCode, Date fromDate);
}
