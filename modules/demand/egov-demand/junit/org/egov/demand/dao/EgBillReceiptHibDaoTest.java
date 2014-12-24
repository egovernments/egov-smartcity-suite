package org.egov.demand.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EgBillReceiptHibDaoTest {
    private  EgovHibernateTest egovHibernateTest;   
    private DemandGenericDao dmdGenericDao=null;
    EgBillReceiptDao egBillReceiptDao=null;
    BillReceipt billRct=null;
    private ModuleDao moduleDao=null;
    private Module module=null;
    private static String objNull="Object is null";  
    List<EgBill> billList=null;
    @Before
    public void setUp() throws Exception {
        egovHibernateTest = new EgovHibernateTest();
        egovHibernateTest.setUp();
        moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
        //This is tested With Property tax Module.
        module = (Module)moduleDao.getModuleByName("Property Tax"); 
        dmdGenericDao=new DemandGenericHibDao();
        egBillReceiptDao=DCBHibernateDaoFactory.getDaoFactory().getEgBillReceiptDao();
        billList =new ArrayList<EgBill>();
        billList=dmdGenericDao.getBillsByBillNumber("billNo", module);
    }
    @After
    public void tearDown() throws Exception {
        egovHibernateTest.tearDown();
        dmdGenericDao=null;
    }
    
    @Test
    public void getDmdDetailListWithNull()
    {
    	billRct= egBillReceiptDao.getBillReceiptByEgBill(null);
    	assertNull(objNull,billRct);
    }
    
    
    /*
    @Test
    public void getDmdDetailListWithData()
    {
    	System.out.println(billList);
    	billRct= egBillReceiptDao.getBillReceiptByEgBill(billList.get(0));
    	assertNotNull(objNull,billRct);
    }*/
}
