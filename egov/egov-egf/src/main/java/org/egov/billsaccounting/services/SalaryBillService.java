/*
 * SalaryBillServiceBean.java Created on Mar 3, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.billsaccounting.services;

import java.util.Date;
import java.util.List;

import org.egov.billsaccounting.dao.BillsAccountingDAOFactory;
import org.egov.billsaccounting.dao.SalarybilldetailHibernateDAO;
import org.egov.billsaccounting.model.Salarybilldetail;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;

/**
 * @author Sathish P
 * @version 1.00
 */
public class SalaryBillService   
{
	 
		
	public List<Salarybilldetail> getSalarybilldetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, String month, String vhNoFrom, String vhNoTo,Department dept,String functionary)
	{
		SalarybilldetailHibernateDAO salBillDAO=BillsAccountingDAOFactory.getDAOFactory().getSalarybilldetailDAO();
		return salBillDAO.getSalarybilldetailFilterBy(fundId, fundSource, vhFromDate, vhToDate, month, vhNoFrom, vhNoTo,dept,functionary);
	}
	public Salarybilldetail getSalarybilldetailById(Integer id)
	{
		SalarybilldetailHibernateDAO salBillDAO=BillsAccountingDAOFactory.getDAOFactory().getSalarybilldetailDAO();
		return (Salarybilldetail)salBillDAO.findById(id, false);
	}
	/**
	 * creates salarybilldetail
	 * 	 * @param salaryBill  void
	 */
	public void createSalaryBillDetail(Salarybilldetail salaryBill)
	{
		SalarybilldetailHibernateDAO salBillDAO=BillsAccountingDAOFactory.getDAOFactory().getSalarybilldetailDAO();
		salBillDAO.create(salaryBill);
	}
}
