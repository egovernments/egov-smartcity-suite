/*
 * @(#)FinancialYearDAO.java 3.0, 11 Jun, 2013 2:37:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.dao.GenericDAO;

public interface FinancialYearDAO extends GenericDAO {
	public String getCurrYearFiscalId();

	public String getCurrYearStartDate();

	public String getPrevYearFiscalId();

	public CFinancialYear getFinancialYearByFinYearRange(String finYearRange);

	public List<CFinancialYear> getAllActiveFinancialYearList();

	public List<CFinancialYear> getAllActivePostingFinancialYear();

	public CFinancialYear getFinancialYearById(Long id);

	public CFinancialYear getFinancialYearByDate(Date date);

	public CFinancialYear getFinYearByDate(Date date);
}
