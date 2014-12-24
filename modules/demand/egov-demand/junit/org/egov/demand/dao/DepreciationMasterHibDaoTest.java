package org.egov.demand.dao;

import static org.junit.Assert.*;

import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DepreciationMasterHibDaoTest {
	private  EgovHibernateTest egovHibernateTest;	
	private DepreciationMasterDao dcbDao;
	
	@Before
	public void setUp() throws Exception {
		egovHibernateTest=new EgovHibernateTest();
		egovHibernateTest.setUp();
		dcbDao=DCBDaoFactory.getDaoFactory().getDepreciationMasterDao();
		}
	@After
	public void tearDown() throws Exception {
		dcbDao=null;
		egovHibernateTest.tearDown();
		
	}
	
	@Test
	public void getLeastDepreciationPercentWithNullYear()
	{
		Float depreePercent=dcbDao.getLeastDepreciationPercent(null);
		assertNull(depreePercent);
	}
	
	@Test
	public void getLeastDepreciationPercentWithYearAsZero()
	{
		Float depreePercent=dcbDao.getLeastDepreciationPercent(0);
		assertNull(depreePercent);
	}
	
	@Test
	public void getLeastDepreciationPercentWithYearFromTable()
	{
		Float depreePercent=dcbDao.getLeastDepreciationPercent(2007);
		assertNotNull(depreePercent);
	}
	
	@Test
	public void getLeastDepreciationPercentWithYearNotInTable()
	{
		Float depreePercent=dcbDao.getLeastDepreciationPercent(1007);
		assertNotNull(depreePercent);
	}
	

}
