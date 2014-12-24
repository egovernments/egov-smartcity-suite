/*
 * @(#)ChartOfAccountsDAO.java 3.0, 10 Jun, 2013 10:25:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericDAO;

public interface ChartOfAccountsDAO extends GenericDAO {
	public CChartOfAccounts getCChartOfAccountsByGlCode(String glCode);

	public List getGlcode(String minGlcode, String maxGlcode, String majGlcode) throws Exception;

	public List<CChartOfAccounts> getActiveAccountsForType(char c) throws EGOVException;

	public List<CChartOfAccounts> getAccountCodeByPurpose(Integer purposeId) throws EGOVException;

	public List<CChartOfAccounts> getNonControlCodeList() throws EGOVException;

	public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) throws EGOVException;

	public Accountdetailtype getAccountDetailTypeIdByName(String glCode, String name) throws Exception;

	public List<CChartOfAccounts> getDetailedAccountCodeList() throws EGOVException;

	public List<CChartOfAccounts> getActiveAccountsForTypes(char[] type) throws ValidationException;

	public List<CChartOfAccounts> getAccountCodeByListOfPurposeId(Integer[] purposeId) throws ValidationException;

	public List<CChartOfAccounts> getListOfDetailCode(final String glCode) throws ValidationException;

	public List<CChartOfAccounts> getBankChartofAccountCodeList();
}
