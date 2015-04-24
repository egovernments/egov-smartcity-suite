/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.demand.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.hibernate.ObjectNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EgBillDetailsHibDaoTest 
{
	EgBillDetailsDao billDetDao = null;
	EgBillDao billDao = null;
	EgBill egBill = null;

	@Before
	public void setUp() throws Exception 
	{
		billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		egBill = (EgBill)billDao.findById(Long.valueOf(1),true);
	}

	@After
	public void tearDown() throws Exception 
	{
		billDetDao = null;
		billDao = null;
		egBill = null;
	}

	public EgBill createBill(String billNo)
	{
		EgBill bill = new EgBill();
		bill.setBillNo(billNo);
		return bill;
	}

	@Test
	public void  getBillDetailsByBillWithInputNull()
	{
		List<EgBillDetails> billDetList = billDetDao.getBillDetailsByBill(null);
		assertNull(billDetList);
	}

	@Test(expected=ObjectNotFoundException.class)
	public void getBillDetailsByBillWithWrongBill()
	{
		EgBill bill = (EgBill)billDao.findById(Long.valueOf(0),true);

		billDetDao.getBillDetailsByBill(bill);
	}

	@Test
	public void getBillDetailsByBillWithExistingBill()
	{
		List<EgBillDetails> billDetList = billDetDao.getBillDetailsByBill(egBill);
		assertNotNull(billDetList.size());
	}
}
