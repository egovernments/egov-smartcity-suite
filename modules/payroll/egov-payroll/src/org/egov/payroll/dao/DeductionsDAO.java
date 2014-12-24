/*
 * DeductionsDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.util.List;





/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Deductions
 *
 * @author Lokesh
 * @version 2.00
 * 
 */

public interface DeductionsDAO extends org.egov.infstr.dao.GenericDAO
{
	
	public List getAllOtherDeductionChartOfAccount()throws Exception;
		
	


}