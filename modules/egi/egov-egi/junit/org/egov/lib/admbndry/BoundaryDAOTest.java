package org.egov.lib.admbndry;

import java.util.List;

import org.egov.infstr.junit.EgovHibernateTest;

public class BoundaryDAOTest extends EgovHibernateTest {

	/*
	 public void testGetBoundaryNotExistByNumTypeandHierCode() {
		final BoundaryDAO bndryDao = new BoundaryDAO();
		final Boundary bndry = bndryDao.getBoundary(3, "cityzone",
				"administration");
		assertNull(bndry);
	}

	public void testGetBoundaryByNumTypeandHierCodeCaseInsensitive() {
		final BoundaryDAO bndryDao = new BoundaryDAO();
		final Boundary bndry = bndryDao.getBoundary(3, "zone", "admin");
		assertNotNull(bndry);
	}*/

	public void testGetAllchildBoundaries() {
		final BoundaryDAO bndryDao = new BoundaryDAO();
		final List<BoundaryImpl> bndry = bndryDao.getAllchildBoundaries(3);
		assertNotNull(bndry);
	}

	public void testGetAllchildBoundariesIfValueNull() {
		final BoundaryDAO bndryDao = new BoundaryDAO();
		final List<BoundaryImpl> bndry = bndryDao.getAllchildBoundaries(null);
		assertNull(bndry);
	}
}
