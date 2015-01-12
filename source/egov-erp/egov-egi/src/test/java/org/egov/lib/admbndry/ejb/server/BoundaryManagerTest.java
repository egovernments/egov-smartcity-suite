package org.egov.lib.admbndry.ejb.server;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.junit.Ignore;

import java.util.HashSet;
import java.util.Set;


@Ignore
public class BoundaryManagerTest extends EgovHibernateTest {

	public final Logger logger = Logger.getLogger(getClass());
	BoundaryServiceImpl boundaryManagerBean;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.boundaryManagerBean = new BoundaryServiceImpl();
		this.boundaryManagerBean.setBoundaryDAO(new BoundaryDAO());
		this.boundaryManagerBean
				.setBoundaryTypeService(new BoundaryTypeServiceImpl());
		final HeirarchyTypeServiceImpl heirImpl = new HeirarchyTypeServiceImpl();
		heirImpl.setHeirarchyTypeDAO(new HeirarchyTypeDAO());
		this.boundaryManagerBean.setHeirarchyTypeService(heirImpl);
	}

	public void testGetCrossHeirarchyParent() {
		Set parentBndryList = new HashSet();
		final Boundary childBndry = this.boundaryManagerBean.getBoundary(5);
		parentBndryList = this.boundaryManagerBean
				.getCrossHeirarchyParent(childBndry);
		assert (parentBndryList.size() > 0);
	}

	public void testGetCrossHeirarchyParentWhereNoChildFound() {
		Set parentBndryList = new HashSet();
		final Boundary childBndry = this.boundaryManagerBean.getBoundary(1);
		parentBndryList = this.boundaryManagerBean
				.getCrossHeirarchyParent(childBndry);
		assert (parentBndryList.size() == 0);
	}

	public void testGetCrossHeirarchyChild() {
		final BoundaryTypeService boundarTypeManagerBean = new BoundaryTypeServiceImpl();
		Set childBndryList = new HashSet();
		final Boundary parentBndry = this.boundaryManagerBean.getBoundary(2);
		final BoundaryType childBoundaryType = boundarTypeManagerBean
				.getBoundaryType(7);
		childBndryList = this.boundaryManagerBean.getCrossHeirarchyChildren(
				parentBndry, childBoundaryType);
		assert (childBndryList.size() > 0);
	}

	public void testGetCrossHeirarchyByPassingBndryObjAsNull() {
		try {
			this.boundaryManagerBean.getCrossHeirarchyParent(null);
			assert (false);
		} catch (final EGOVRuntimeException e) {
			assert (true);
		}
	}

}
