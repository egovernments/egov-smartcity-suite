/*
 * @(#)FiscalPeriodDAO.java 3.0, 11 Jun, 2013 2:45:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import org.egov.infstr.dao.GenericDAO;

public interface FiscalPeriodDAO extends GenericDAO {
	public String getFiscalPeriodIds(String financialYearId);
}
