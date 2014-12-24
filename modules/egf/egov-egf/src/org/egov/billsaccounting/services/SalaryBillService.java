/*
 * PaymentForm.java Created on Mar 3, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.billsaccounting.services;

import java.util.Date;
import java.util.List;

import org.egov.commons.Fundsource;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.billsaccounting.model.Salarybilldetail;
  
/**
 * @author Sathish P
 * @version 1.00
 */
public interface SalaryBillService
{	
	/**
    * @param fundId
    * @param fundSource
    * @param vhFromDate
    * @param vhToDate
    * @param month
    * @param vhNoFrom
    * @param vhNoTo
    * @return list of Salarybilldetail filtered by optional conditions
    */
	public List<Salarybilldetail> getSalarybilldetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, String month, String vhNoFrom, String vhNoTo,DepartmentImpl dept,String functionary);
	
	/** 
	 * @param id
	 * @return Salarybilldetail
	 */
	public Salarybilldetail getSalarybilldetailById(Integer id);

	public void createSalaryBillDetail(Salarybilldetail salaryBill);
}


