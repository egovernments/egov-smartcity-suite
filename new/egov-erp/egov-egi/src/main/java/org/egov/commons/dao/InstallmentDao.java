/*
 * @(#)InstallmentDao.java 3.0, 11 Jun, 2013 3:50:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.infstr.commons.Module;
import org.egov.infstr.dao.GenericDAO;

public interface InstallmentDao extends GenericDAO {
	public List<Installment> getInsatllmentByModule(Module module);

	public List<Installment> getInsatllmentByModule(Module module, Date year);

	public Installment getInsatllmentByModule(Module module, Date year, Integer installmentNumber);

	public Installment getInsatllmentByModuleForGivenDate(Module module, Date installmentDate);

	public List<Installment> getEffectiveInstallmentsforModuleandDate(Date dateToCompare, int noOfMonths, Module mod);

}
