package org.egov.lib.admbndry;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.Date;

import org.egov.infstr.junit.EgovHibernateTest;

public class BoundaryTypeImplTest extends EgovHibernateTest {

	public void testGetFirstChild() throws Exception {
		final BoundaryTypeImpl boundaryTypeMock = createMock(BoundaryTypeImpl.class);
		final HeirarchyTypeImpl hirarchyType = buildHierarchyTypeObj(1,
				"ADMINISTRATOR", "ADMIN");
		final BoundaryTypeImpl boundaryImpl = buildBoundaryTypeObj(1, "City",
				(short) 1, null, hirarchyType);
		final BoundaryTypeImpl nextboundaryImpl = buildBoundaryTypeObj(2,
				"Zone", (short) 2, boundaryImpl, hirarchyType);

		boundaryTypeMock.getFirstChild();
		expectLastCall().andReturn(nextboundaryImpl);
		replay(boundaryTypeMock);

		final BoundaryTypeImpl bndry = boundaryTypeMock.getFirstChild();
		assertEquals("Zone", bndry.getName());
	}

	private HeirarchyTypeImpl buildHierarchyTypeObj(final int id,
			final String name, final String code) {
		final HeirarchyTypeImpl hirarchyType = new HeirarchyTypeImpl();
		hirarchyType.setId(id);
		hirarchyType.setName(name);
		hirarchyType.setCode(code);
		hirarchyType.setUpdatedTime(new Date());
		return hirarchyType;
	}

	private BoundaryTypeImpl buildBoundaryTypeObj(final int id,
			final String name, final short heirarchy,
			final BoundaryTypeImpl parent, final HeirarchyTypeImpl hirarchyType) {
		final BoundaryTypeImpl boundaryImpl = new BoundaryTypeImpl();
		boundaryImpl.setId(id);
		boundaryImpl.setName(name);
		boundaryImpl.setHeirarchyType(hirarchyType);
		boundaryImpl.setHeirarchy(heirarchy);
		boundaryImpl.setParent(parent);
		boundaryImpl.setUpdatedTime(new Date());
		return boundaryImpl;
	}
}