package org.egov.lib.admbndry;

import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.junit.EgovHibernateTest;

public class BoundaryTypeDAOTest extends EgovHibernateTest {

	public void testGetAllchildBoundaries() throws EGOVRuntimeException {

		final BoundaryTypeDAO bndryDao = new BoundaryTypeDAO();
		final List<BoundaryType> bndry = bndryDao
				.getParentBoundaryTypeByHirarchy(null);
		assertEquals(0, bndry.size());
	}

	public void testGetSecondLevelBoundaryByPassingHeirarchy()
			throws EGOVRuntimeException {

		final BoundaryTypeDAO bndryDao = new BoundaryTypeDAO();
		final Map<String, List<Boundary>> bndry = bndryDao
				.getSecondLevelBoundaryByPassingHeirarchy(null);
		assertEquals(0, bndry.size());
	}
}
