/*
 * PayScaleHeaderDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.util.Date;
import java.util.List;

import org.egov.payroll.model.PayScaleHeader;

/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for exception
 *
 * @author Lokesh
 * @version 2.00
 * 
 */

public interface PayScaleHeaderDAO extends org.egov.infstr.dao.GenericDAO {
	public PayScaleHeader getPayScaleHeaderByName(String name);
	public PayScaleHeader getPayScaleHeaderForEmp(Integer Id);
	public PayScaleHeader getPayScaleHeaderById(Integer Id);
	/*
	 * Return last payscale for employee
	 */
	public PayScaleHeader getLastPayscaleByEmp(Integer empId)throws Exception;
	/*
	 * Return payscale based on grade and effective date
	 */
	@Deprecated
	public List getPayScaleByGradeAndEffectiveDate(Integer gradeId,Date effectiveDate)throws Exception;
	
	public List getPayScaleByEffectiveDate(Date effectiveDate)throws Exception;
}