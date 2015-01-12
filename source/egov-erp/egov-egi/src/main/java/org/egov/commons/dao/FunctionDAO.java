/*
 * @(#)FunctionDAO.java 3.0, 11 Jun, 2013 2:58:22 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.CFunction;
import org.egov.infstr.dao.GenericDAO;

public interface FunctionDAO extends GenericDAO {
	public List<CFunction> getAllActiveFunctions();

	public CFunction getFunctionByCode(String functionCode);

	public CFunction getFunctionById(Long Id);
}
