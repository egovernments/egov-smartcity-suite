package org.egov.ptis.builder.entity.master;

import org.egov.ptis.domain.entity.property.Category;
import org.junit.Ignore;

/**
 * @author Ramki
 */
@Ignore
public class CategoryBuilder {
	private final Category category;

	public CategoryBuilder() {
		category = new Category();
	}

	public Category build() {
		return category;
	}
}
