/*
 * AdvanceDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.util.Date;
import java.util.List;

import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCodes;



/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Advance
 *
 * @author Lokesh
 * @version 2.00
 * 
 */

public interface PayheadRuleDAO extends org.egov.infstr.dao.GenericDAO
{
	public PayheadRule getEffectivePayheadRule(SalaryCodes salarycode, Date effectiveFrom);
	/**
	 * Get all payheadrule
	 */
	public List<PayheadRule> getAllPayheadRule();
	public List<PayheadRule>getAllPayheadRulesBySalCode(Integer id);
}
