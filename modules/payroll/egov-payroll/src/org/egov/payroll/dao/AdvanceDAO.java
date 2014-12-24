/*
 * AdvanceDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;


/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Advance
 *
 * @author Lokesh
 * @version 2.00
 * 
 */

public interface AdvanceDAO extends org.egov.infstr.dao.GenericDAO
{
	public List<Advance> getAdvancesByEmpId(Integer empId);
	public List<Advance> getAllSaladvances();
	public List<Advance> getAllSalaryadvancesbyStatus(EgwStatus status);
	public List<Advance> getAllAdvancesByStatus(EgwStatus status);
	public List<Advance> getAllSalAdvancesByEmpAndStatus(Integer empId,EgwStatus status);
	public List<Advance> getAllEligibleAdvancesForEmp(Integer empId);
	
	/*
	 * Return list of repaid advance amount in passing duration (Applicable for only non-interest bearing advance)
	 */
	public List<String> getListOfRepaidAdvanceAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate);
	
	/*
	 * Return list of sanctioned advance amount in passing duration (Applicable for only non-interest bearing advance)
	 */
	public List<String> getListOfSanctionedAdvanceAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate);
	
	/**
	 *get advance scheduler by month,financials year for a particular advance
	 *@param Advance
	 *@param month
	 *@param finYear
	 *@return AdvanceSchedule 
	 */
	public AdvanceSchedule getAdvSchedulerByMonthYear(Advance adv, BigDecimal month, CFinancialYear finYear);
	
	/**
	 * Get advance correct schedule which is to be recovered  
	 */
	public AdvanceSchedule getRecoverableScheduler(Advance adv);
	
	/**
	 * Get advance correct schedule which is to be recovered  
	 */
	public AdvanceSchedule getAdvSchedulerById(Integer advSchedulerId);
	
}
