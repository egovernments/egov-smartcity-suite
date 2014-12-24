/**
 * PayTypeMasterDAO.java 7 May, 2008 10:24:35 PM
 * 
 */
package org.egov.payroll.dao;

import org.egov.payroll.model.PayTypeMaster;

/**
 * @author Eluri
 *
 */
public interface PayTypeMasterDAO extends org.egov.infstr.dao.GenericDAO
{
	public PayTypeMaster getPayTypeMasterByPaytype(String paytype);
}

