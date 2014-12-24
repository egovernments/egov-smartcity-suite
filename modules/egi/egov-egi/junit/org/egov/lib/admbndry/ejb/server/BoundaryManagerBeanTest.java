package org.egov.lib.admbndry.ejb.server;

import java.util.Date;
import java.util.List;


import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;

public class BoundaryManagerBeanTest extends EgovHibernateTest {
	BoundaryServiceImpl boundaryManager;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.boundaryManager = new BoundaryServiceImpl();
		this.boundaryManager.setBoundaryDAO(new BoundaryDAO());
		this.boundaryManager
				.setBoundaryTypeService(new BoundaryTypeServiceImpl());
		final HeirarchyTypeServiceImpl heirImpl = new HeirarchyTypeServiceImpl();
		heirImpl.setHeirarchyTypeDAO(new HeirarchyTypeDAO());
		this.boundaryManager.setHeirarchyTypeService(heirImpl);
	}

	public void testBoundaryCreation() throws Exception {
		final BoundaryImpl boundary = newBoundary();
		this.boundaryManager.createBoundary(boundary);
	}

	public void testGetAllLeafBoundariesForParentBndryIdAndChBType() {
		final List boundaries = this.boundaryManager
				.getAllLeafBoundariesForParentBndryIdAndChBType(1, "CITY");
		assertFalse(boundaries.isEmpty());

	}

	private BoundaryImpl newBoundary() {
		final BoundaryImpl boundary = new BoundaryImpl();
		final Date date = new Date();
		boundary.setName("name");
		boundary.setIsHistory('N');
		boundary.setBoundaryType(new BoundaryTypeDAO().getBoundaryType(1));
		boundary.setFromDate(date);
		return boundary;
	}
}
