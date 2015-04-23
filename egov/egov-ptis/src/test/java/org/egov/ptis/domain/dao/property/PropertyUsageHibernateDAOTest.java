package org.egov.ptis.domain.dao.property;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Property Usage DAO Test
 * 
 * @author Srikanth
 **/
public class PropertyUsageHibernateDAOTest {

	private transient PropertyUsageDAO propertyUsageDAO;
	private transient PropertyUsage propertyUsage;
	private transient EgovHibernateTest egovHibernateTest = null;
	private transient Calendar cal = null;

	@Before
	public void setUp() throws Exception {
		egovHibernateTest = new EgovHibernateTest();
		egovHibernateTest.setUp();
		propertyUsageDAO = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		cal = GregorianCalendar.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		egovHibernateTest.tearDown();
	}

	@Test
	public final void testGetPropUsageByUsageCode() {
		propertyUsage = propertyUsageDAO.getPropertyUsage("RESD");
		assertNotNull("residential property Usgae", propertyUsage);
	}

	@Test
	public final void testGetPropUsageByUsageCodeAndDate() {
		propertyUsage = propertyUsageDAO.getPropertyUsage("RESD", new Date());
		assertNotNull("residential property Usgae with Date ", propertyUsage);
	}

	@Test
	public final void testGetPropUsageByWrongUsageCode() {
		propertyUsage = propertyUsageDAO.getPropertyUsage("WRONGUSAGE");
		assertNull("wrong property Usgae", propertyUsage);
	}

	@Test
	public final void testGetPropUsageByNullUsageCode() {
		propertyUsageDAO = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
		propertyUsage = propertyUsageDAO.getPropertyUsage(null);
		assertNull("null property Usgae", propertyUsage);
	}

	@Test
	public final void testGetPropUsageByUsageCodeAndInvalidDate() {

		cal.set(Calendar.YEAR - 10, Calendar.MONTH, Calendar.DATE);
		propertyUsage = propertyUsageDAO.getPropertyUsage("NONRESD",
				new Date(cal.getTimeInMillis()));
		assertNull("non residential property Usgae", propertyUsage);
	}

	@Test
	public final void testGetAllActivePropertyUsage() {
		List<PropertyUsage> propUsage;
		propUsage = propertyUsageDAO.getAllActivePropertyUsage();
		assertNotNull("size of property Usage", propUsage.size() == 2);
	}

	@Test
	public final void testGetAllPropertyUsage() {
		List<PropertyUsage> propUsage;
		propUsage = propertyUsageDAO.getAllPropertyUsage();
		assertNotNull("size of property Usage", propUsage.size() == 3);
	}

	@Test
	public final void getPropUsageAscOrder() {
		List<PropertyUsage> propUsage;
		propUsage = propertyUsageDAO.getPropUsageAscOrder();
		assertNotNull("size of property Usage", propUsage.size() > 1);
	}
}
