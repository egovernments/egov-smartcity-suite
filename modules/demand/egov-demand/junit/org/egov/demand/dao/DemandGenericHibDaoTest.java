package org.egov.demand.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.commons.service.CommonsManagerBean;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DemandGenericHibDaoTest {
	private EgovHibernateTest egovHibernateTest;
	private DemandGenericDao dmdGenericDao = null;
	private EgDemandDao demandDao = null;
	private EgDemand egDemand = null;
	private Installment installment = null;
	private ModuleDao moduleDao = null;
	private Module module = null;
	private CommonsManagerBean commonsManager = null;
	private EgDemandReasonMaster dmdRsnMaster = null;
	List list = null;
	private static String trueString = "true";

	@Before
	public void setUp() throws Exception {
		EGOVThreadLocals.setUserId(String.valueOf(1));
		egovHibernateTest = new EgovHibernateTest();
		egovHibernateTest.setUp();
		dmdGenericDao = new DemandGenericHibDao();
		demandDao = DCBHibernateDaoFactory.getDaoFactory().getEgDemandDao();
		egDemand = (EgDemand) demandDao.findById(Long.valueOf(1), true);
		moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		// This is tested With Property tax Module.
		module = (Module) moduleDao.getModuleByName("Property Tax");
		commonsManager = new CommonsManagerBean();
		installment = commonsManager.getInsatllmentByModuleForGivenDate(module, new Date());
		dmdRsnMaster = dmdGenericDao.getDemandReasonMasterByCode("PENALTY", module);
		list = new ArrayList<EgDemandDetails>();

	}

	/*@Test
	public void getDmdDetailListWithData() {
		list = dmdGenericDao.getDmdDetailList(egDemand, installment, module, dmdRsnMaster);
		assertTrue(trueString, list.size() > 0);
	}
*/
	@After
	public void tearDown() throws Exception {
		egovHibernateTest.tearDown();
		dmdGenericDao = null;
		installment = null;
		moduleDao = null;
		module = null;
		commonsManager = null;
	}

	@Test
	public void getDmdDetailListWithNull() {
		list = dmdGenericDao.getDmdDetailList(null, null, null, null);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdDetailListWithEgDemandNull() {
		list = dmdGenericDao.getDmdDetailList(null, installment, module, dmdRsnMaster);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdDetailListWithInstallmentNull() {
		list = dmdGenericDao.getDmdDetailList(egDemand, null, module, dmdRsnMaster);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdDetailListWithModuleNull() {
		list = dmdGenericDao.getDmdDetailList(egDemand, installment, null, dmdRsnMaster);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdDetailListWithDmdRsnMsterNull() {
		list = dmdGenericDao.getDmdDetailList(egDemand, installment, module, null);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdAmtAndCollAmtWithNull() {
		list = dmdGenericDao.getDmdAmtAndCollAmt(null, null);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdAmtAndCollAmtWithEgDemandNull() {
		list = dmdGenericDao.getDmdAmtAndCollAmt(null, installment);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdAmtAndCollAmtWithInstallmentNull() {
		list = dmdGenericDao.getDmdAmtAndCollAmt(egDemand, null);
		assertTrue(trueString, list.size() == 0);
	}

	@Test
	public void getDmdAmtAndCollAmtWithData() {
		list = dmdGenericDao.getDmdAmtAndCollAmt(egDemand, installment);
		assertTrue(trueString, list.size() > 0);
	}

	@Test
	public void getDCBWithAllNull() {
		list = dmdGenericDao.getDCB(null, null);
		assertTrue(trueString, list.isEmpty());
	}

	@Test
	public void getDCBWithEgDemandNull() {
		list = dmdGenericDao.getDCB(null, module);
		assertTrue(trueString, list.isEmpty());
	}

	@Test
	public void getDCBWithModuleNull() {
		list = dmdGenericDao.getDCB(null, module);
		assertTrue(trueString, list.isEmpty());
	}

	@Test
	public void getDCBWithData() {
		list = dmdGenericDao.getDCB(egDemand, module);
		assertTrue(trueString, !list.isEmpty());
	}

	@Test
	public void getEgDemandReasonMasterIdsWithDemandNull() {
		list = dmdGenericDao.getEgDemandReasonMasterIds(null);
		assertTrue(trueString, list.isEmpty());
	}

	@Test
	public void getEgDemandReasonMasterIdsWithData() {
		list = dmdGenericDao.getEgDemandReasonMasterIds(egDemand);
		assertTrue(trueString, !list.isEmpty());
	}

	@Test
	public void getReceiptDetailsWithData() {
		list = dmdGenericDao.getBillReceipts(egDemand);
		assertTrue(trueString, !list.isEmpty());
	}

}
