package org.egov.ptis.builder.entity.master;

import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.junit.Ignore;

/**
 * @author Ramki
 */
@Ignore
public class BoundaryCategoryBuilder {
	private final BoundaryCategory boundaryCategory;

	public BoundaryCategoryBuilder() {
		boundaryCategory = new BoundaryCategory();
	}

	public BoundaryCategory build() {
		return boundaryCategory;
	}
}
