package org.egov.ptis.domain.dao.property;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.commons.service.CommonsManagerBean;
import org.egov.demand.model.EgDemand;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyHibDAOTest

{
	private transient PropertyDAO propertyDao;
	private transient List propertyList;
	private transient Boundary boundary;
	private transient BoundaryDAO boundaryDao;
	private transient EgovHibernateTest egovHibernateTest = null;
	private transient List zoneWiseDemand = null;
	private transient List currDemand = null;
	private transient BigDecimal zeroVal = BigDecimal.ZERO;
	private static String objNull = "Object is null";
	private static String objNotNull = "Object is Not Null";
	private static String trueString = "true";
	private static String zoneWiseDemandString = "size of zoneWiseDemand List";
	private static String propListstring = "size of propertyList List";
	private static String sizeOfCurrDmdStr = "size of currDemand";
	PtDemandDao eGPTDemandDao = null;
	EgDemand currentDemand = null;
	BasicPropertyDAO basicPropertyDAO = null;
	BasicProperty basicProperty = null;
	Installment installment = null;
	ModuleDao moduleDao = null;
	Module module = null;
	CommonsManagerBean commonsManager = null;
	BigDecimal penaltyamount = null;
	BigDecimal propId = null;
	private transient List list;

	@Before
	public void setUp() throws Exception {
		egovHibernateTest = new EgovHibernateTest();
		egovHibernateTest.setUp();
		propertyDao = PropertyDAOFactory.getDAOFactory().getPropertyDAO();
		boundaryDao = new BoundaryDAO();
		boundary = boundaryDao.getBoundary(34);
		zoneWiseDemand = new ArrayList();
		basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
		basicProperty = (BasicProperty) basicPropertyDAO
				.getBasicPropertyByPropertyID("08-119-0000-000");
		eGPTDemandDao = (PtDemandDao) PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		currentDemand = (EgDemand) eGPTDemandDao.getNonHistoryDemandForProperty(basicProperty
				.getProperty());
		moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		module = (Module) moduleDao.getModuleByName("Property Tax");
		commonsManager = new CommonsManagerBean();
		installment = commonsManager.getInsatllmentByModuleForGivenDate(module, new Date());
		penaltyamount = BigDecimal.ZERO;
		propId = BigDecimal.ZERO;
		list = new ArrayList();
	}

	@After
	public void tearDown() throws Exception {

		egovHibernateTest.tearDown();
		installment = null;
		moduleDao = null;
		module = null;
		commonsManager = null;
		penaltyamount = null;
		propId = null;
		list = null;
	}

	@Test
	public void getPropsMrkdForDeactByWardInputNullBoundary() throws PropertyNotFoundException {
		propertyList = propertyDao.getPropsMrkdForDeactByWard(null);
		assertNotNull(propListstring, propertyList.size() < 1);
	}

	@Test
	public void getPropsMrkdForDeactByWardCorrectBoundary() throws PropertyNotFoundException {
		propertyList = propertyDao.getPropsMrkdForDeactByWard(boundary);
		assertNotNull(propListstring, propertyList.size() < 1);
	}

	@Test
	public void getPropMaterlizeViewListInputNull() {
		zoneWiseDemand = propertyDao.getPropMaterlizeViewList(null, null, null);
		assertNotNull(zoneWiseDemandString, zoneWiseDemand.size() < 1);
	}

	@Test
	public void getPropMaterlizeViewListInputCriterionNull() {
		Projection projection = Projections.projectionList().add(Projections.property("zoneID"))
				.add(Projections.sum("aggrArrDmd")).add(Projections.sum("aggrCurrDmd")).add(
						Projections.groupProperty("zoneID"));
		Order order = Order.asc("zoneID");
		zoneWiseDemand = propertyDao.getPropMaterlizeViewList(projection, null, order);
		assertNotNull(zoneWiseDemandString, zoneWiseDemand.size() > 1);
	}

	@Test
	public void getPropMaterlizeViewListInputProjectionNull() {
		Order order = Order.asc("zoneID");
		Criterion criterion = Restrictions.like("zoneID", 3);
		zoneWiseDemand = propertyDao.getPropMaterlizeViewList(null, criterion, order);
		assertNotNull(zoneWiseDemandString, zoneWiseDemand.size() > 1);
	}

	@Test
	public void getPropMaterlizeViewListInputOrderNull() {
		Projection projection = Projections.projectionList().add(Projections.property("zoneID"))
				.add(Projections.sum("aggrArrDmd")).add(Projections.sum("aggrCurrDmd")).add(
						Projections.groupProperty("zoneID"));
		Criterion criterion = Restrictions.like("zoneID", 3);
		zoneWiseDemand = propertyDao.getPropMaterlizeViewList(projection, criterion, null);
		assertNotNull("size of zoneWiseDemand", zoneWiseDemand.size() > 1);
	}

	@Test
	public void getDmdCollAmtInstWiseWithNull() {
		currDemand = propertyDao.getDmdCollAmtInstWise(currentDemand);
		assertNotNull(sizeOfCurrDmdStr, currDemand.size() > 1);
	}

	@Test
	public void getDmdDetIdFromInstallandEgDemandNull() {
		currDemand = propertyDao.getDmdDetIdFromInstallandEgDemand(null, null);
		assertTrue(trueString, currDemand.size() == 0);
	}

	@Test
	public void getDmdDetIdFromInstallandEgDemandForInstallmentNull() {
		currDemand = propertyDao.getDmdDetIdFromInstallandEgDemand(null, currentDemand);
		assertTrue(trueString, currDemand.size() == 0);
	}

	@Test
	public void getDmdDetIdFromInstallandEgDemandFOrEgdemandNull() {
		currDemand = propertyDao.getDmdDetIdFromInstallandEgDemand(installment, null);
		assertTrue(trueString, currDemand.size() == 0);
	}

	@Test
	public void getDmdDetIdFromInstallandEgDemandData() {
		currDemand = propertyDao.getDmdDetIdFromInstallandEgDemand(installment, currentDemand);
		assertNotNull(sizeOfCurrDmdStr, currDemand.size() > 1);
	}

	@Test
	public void getEgptPropertyFromBillIdWithNull() {
		propId = propertyDao.getEgptPropertyFromBillId(null);
		assertNull(objNull, propId);
	}

	@Test
	public void getEgptPropertyFromBillIdWithData() {
		propId = propertyDao.getEgptPropertyFromBillId(Long.valueOf(30));
		assertNull(objNotNull, propId);
	}

	@Test
	public void getAllDemandsWithNull() {
		list = propertyDao.getAllDemands(null);
		assertNull(objNull, list);
	}

	@Test
	public void getAllDemandsWithData() {
		list = propertyDao.getAllDemands(basicProperty);
		assertTrue(trueString, !list.isEmpty());
	}

}
