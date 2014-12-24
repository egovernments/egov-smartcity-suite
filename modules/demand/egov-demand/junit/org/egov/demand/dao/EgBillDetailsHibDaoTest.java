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
