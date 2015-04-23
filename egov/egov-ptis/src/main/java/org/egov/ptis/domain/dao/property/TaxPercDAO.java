/*
 * TaxPercDAO Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.TaxPerc;


/**
 * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for Tax Perc
 * 
 * @author Neetu
 * @version 2.00 
 */

public interface TaxPercDAO extends org.egov.infstr.dao.GenericDAO
{
	public TaxPerc getTaxPerc(Category category, PropertyUsage propertyUsage,BigDecimal amount,Date date);
	public Float getTaxPerc(Integer usageId);
}
