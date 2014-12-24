/*
 * PropertyDetailDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Neetu
 * @version 2.00
 */

public interface PropertyDetailDAO extends org.egov.infstr.dao.GenericDAO
{
	 
	public PropertyDetail getPropertyDetailByProperty(Property property);
	public PropertyDetail getPropertyDetailBySurveyNumber(String surveyNumber);
	public PropertyDetail getPropertyDetailByRegNum(String regNum);
}
