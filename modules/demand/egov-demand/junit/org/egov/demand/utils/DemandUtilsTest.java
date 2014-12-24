package org.egov.demand.utils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DemandUtilsTest {
	private  EgovHibernateTest egovHibernateTest=new EgovHibernateTest();	
	private DemandUtils dmdUtils;

	@Before
	public void setUp() throws Exception {
		egovHibernateTest=new EgovHibernateTest();
		egovHibernateTest.setUp();
		dmdUtils=new DemandUtils();
	}
	@After
	public void tearDown() throws Exception {
		dmdUtils=null;
		egovHibernateTest.tearDown();

	}
	

	@Test
	public void postBillCollectionDetailsWithInputNull()
	{
		String xmlData = dmdUtils.generateBillXML(null,null);
		assertEquals("", xmlData);
	}


	@Test
	public void generateBillXMLWithCompleteBillObject()
	{
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();  
		Module module = moduleDao.getModuleByName("Property Tax");
		Set<EgBillDetails> egBillDetails = new HashSet<EgBillDetails>();
		EgBillDetails egBillDet = new EgBillDetails();
		String dispMsg = "display Message";
		EgBill egBill = new EgBill();
		egBill.setId(Long.valueOf(1));
		egBill.setBillNo("1");
		egBill.setModule(module);
		egBill.setFundCode("fundCode");
		egBill.setFunctionaryCode(BigDecimal.valueOf(0));
		egBill.setFundSourceCode("fundSourceCode");
		egBill.setDepartmentCode("departmentCode");
		egBill.setCollModesNotAllowed("bank,cheque");
		egBill.setBoundaryNum(Integer.valueOf(1));
		egBill.setBoundaryType("Street");
		egBill.setTotalAmount(new BigDecimal(0));
		egBill.setCitizenAddress("Citizen Address");
		egBill.setCitizenName("citizen name");
		egBill.setOverrideAccountHeadsAllowed(true);
		egBill.setPartPaymentAllowed(true);
		egBill.setServiceCode("serviceCode");
		egBill.setDescription("description");
		egBill.setCreateTimeStamp(new Date());
		egBill.setTotalCollectedAmount(new BigDecimal(10));
		egBill.setConsumerId("propertyid");
		egBillDet.setFunctionCode("functionCode");
		egBillDet.setGlcode("glCode");
		egBillDet.setOrderNo(Integer.valueOf(1));
		egBillDet.setCrAmount(BigDecimal.valueOf(0));
		egBillDet.setDrAmount(BigDecimal.valueOf(0));
		
		egBillDetails.add(egBillDet);
		egBill.setEgBillDetails(egBillDetails);	
		
		String xmlData = dmdUtils.generateBillXML(egBill,dispMsg);
		assertTrue(xmlData.contains("<collectionModeNotAllowed>bank</collectionModeNotAllowed>"));
		assertTrue(xmlData.contains("<fundSourceCode>fundSourceCode</fundSourceCode>"));
	}
	
}
