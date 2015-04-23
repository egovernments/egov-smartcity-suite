/* TaxPercDAO Created on june 13, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;




/**
 * <p>This is an interface which would be implemented by the 
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data 
 * access operations for Tax Perc
 * 
 * @author Lokesh
 * @version 2.00 
 */

public interface TaxPercentageforDatesDAO extends org.egov.infstr.dao.GenericDAO
{
	public List getTaxPercentageforDates(Integer type, BigDecimal amount,Date installmentStartDate,Date installmentEndDate);
}
