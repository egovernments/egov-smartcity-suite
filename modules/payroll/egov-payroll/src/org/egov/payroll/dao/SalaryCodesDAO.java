/*
 * SalaryCodesDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.util.List;

import org.egov.payroll.model.SalaryCodes;




/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for salarycodes
 *
 * @author Lokesh
 * @version 2.00
 *
 */

public interface SalaryCodesDAO extends org.egov.infstr.dao.GenericDAO
{


	public SalaryCodes getSalaryCodesByHead(String head);
	public List getAllSalarycodesByCategoryType(String type);
	public List getAllSalaryCodesByTypeAsSortedByOrder(String type);
	public List getSalaryCodesByCategoryId(Integer catId);
	public List getSalaryCodesByOrderId();
	public List<SalaryCodes> getSalaryCodesByCategoryName(String categoryName);
	public List<SalaryCodes> getSalaryCodesByCategoryNames(String categoryName1, String categoryName2);
	public Long getMaxOrderSalarycode();
	public List<SalaryCodes> getAllSAlaryCodesSortedByOrder();
	public List<SalaryCodes> getAllSalarycodesByCategoryId(Integer categoryId);


}