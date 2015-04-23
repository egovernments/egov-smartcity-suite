package org.egov.ptis.domain.dao.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicPropertyHibernateDAOTest {
	private EgovHibernateTest egovHibernateTest;
	private BasicPropertyDAO basicPropertyDAO;

	@Before
	public void setUp() throws Exception {
		egovHibernateTest = new EgovHibernateTest();
		egovHibernateTest.setUp();
		basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
		EGOVThreadLocals.setUserId(String.valueOf(1));
	}

	@After
	public void tearDown() throws Exception {
		basicPropertyDAO = null;
		egovHibernateTest.tearDown();
	}

	@Test
	public void getInActiveBasicPropertyByPropertyIDWithInputNull() {
		BasicProperty basicProperty = basicPropertyDAO.getInActiveBasicPropertyByPropertyID(null);
		assertEquals(null, basicProperty);
	}

	@Test
	public void getInActiveBasicPropertyByPropertyIDWithInputEmptyString() {
		BasicProperty basicProperty = basicPropertyDAO.getInActiveBasicPropertyByPropertyID("");
		assertEquals(null, basicProperty);
	}

	@Test
	public void getInActiveBasicPropertyByPropertyIDWithRelevantPropertyID() {
		BasicProperty basicProperty = basicPropertyDAO.getInActiveBasicPropertyByPropertyID("08-119-0000-001");
		assertNotNull(basicProperty);
	}

	@Test
	public void getBasicPropertyByPropertyIDWithUnrelevantPropertyID() {
		BasicProperty basicProperty = basicPropertyDAO.getInActiveBasicPropertyByPropertyID("08-119-0000-000");
		assertEquals(null, basicProperty);
	}

	@Test
	public void getBasicPropertyByPropertyIDWithInputNull() {
		String propertyId = null;
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
		assertEquals(null, basicProperty);
	}

	@Test
	public void getBasicPropertyByPropertyIDWithInputEmptyString() {
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID("");
		assertEquals(null, basicProperty);
	}

	@Test
	public void getBasicPropertyByPropertyIDWithRelevantPropertyID() {
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID("08-119-0000-000");
		assertNotNull(basicProperty);
	}

	@Test
	public void getInActiveBasicPropertyByPropertyIDWithUnrelevantPropertyID() {
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID("08-119-0000-001");
		assertEquals(null, basicProperty);
	}

	public void getAllBasicPropertyByPropertyIDWithInputNull() {
		BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(null);
		assertEquals(null, basicProperty);
	}

	@Test
	public void getAllBasicPropertyByPropertyIDWithInputEmptyString() {
		BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID("");
		assertEquals(null, basicProperty);
	}

	@Test
	public void getAllBasicPropertyByPropertyIDWithRelevantPropertyID() {
		BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID("08-119-0000-000");
		assertNotNull(basicProperty);
	}

	@Test
	public void getChildBasicPropsForParentWithNullBasicProp() {
		List<BasicPropertyImpl> basicPropList;
		basicPropList = basicPropertyDAO.getChildBasicPropsForParent(null);
		assertEquals(0, basicPropList.size());
	}

	@Test
	public void getChildBasicPropsForParentWithWrongBasicProp() {
		List<BasicPropertyImpl> basicPropList;
		BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID("Wrong Bill No");
		basicPropList = basicPropertyDAO.getChildBasicPropsForParent(basicProperty);
		assertEquals(0, basicPropList.size());
	}

	@Test
	public void getChildBasicPropsForParentWithRelavantBasicProp() {
		List<BasicPropertyImpl> basicPropList;
		BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID("08-119-0000-010");
		basicPropList = basicPropertyDAO.getChildBasicPropsForParent(basicProperty);
		assertEquals(1, basicPropList.size());
	}
}