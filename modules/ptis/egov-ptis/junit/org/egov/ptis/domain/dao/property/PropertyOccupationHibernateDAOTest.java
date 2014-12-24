package org.egov.ptis.domain.dao.property;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.PropertyOccupation;

public class PropertyOccupationHibernateDAOTest extends EgovHibernateTest {
	PropertyOccupationDAO propOccDao;
	PropertyOccupation propOcc;

	public void testGetPropertyOccupationByOccCodeAndUsageInputNull() {
		propOccDao = PropertyDAOFactory.getDAOFactory().getPropertyOccupationDAO();
		propOcc = propOccDao.getPropertyOccupationByOccCodeAndUsage(null, null);
		assertNull(propOcc);
	}

	public void testGetPropertyOccupationByOccCodeAndUsageInputOccAndUsg() {
		propOccDao = PropertyDAOFactory.getDAOFactory().getPropertyOccupationDAO();
		propOcc = propOccDao.getPropertyOccupationByOccCodeAndUsage("SELFOCC", 1L);
		assertNotNull(propOcc);
	}

	public void testGetPropertyOccupationByOccCodeInputNull() {
		propOccDao = PropertyDAOFactory.getDAOFactory().getPropertyOccupationDAO();
		propOcc = propOccDao.getPropertyOccupationByOccCode(null);
		assertNull(propOcc);
	}

	public void testGetPropertyOccupationByOccCodeAndDateInputNull() {
		propOccDao = PropertyDAOFactory.getDAOFactory().getPropertyOccupationDAO();
		propOcc = propOccDao.getPropertyOccupationByOccCodeAndDate(null, null);
		assertNull(propOcc);
	}

	/*
	 * These api's testGetPropertyOccupationByOccCodeInputCode and
	 * testGetPropertyOccupationByOccCodeAndDateInputCodeDate not applicable in
	 * case of COCPTIS as we are having same same occupancy with different
	 * usages in boundary category table public void
	 * testGetPropertyOccupationByOccCodeInputCode() { propOccDao =
	 * PropertyDAOFactory.getDAOFactory().getPropertyOccupationDAO(); propOcc =
	 * propOccDao.getPropertyOccupationByOccCode("SELFOCC");
	 * assertNotNull(propOcc); }
	 * 
	 * public void testGetPropertyOccupationByOccCodeAndDateInputCodeDate() {
	 * propOccDao =
	 * PropertyDAOFactory.getDAOFactory().getPropertyOccupationDAO(); propOcc =
	 * propOccDao.getPropertyOccupationByOccCodeAndDate("SELFOCC",DateUtils.
	 * getLastFinYearStartDate()); assertNotNull(propOcc); }
	 */

}
