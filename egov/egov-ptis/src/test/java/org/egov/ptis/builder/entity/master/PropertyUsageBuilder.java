package org.egov.ptis.builder.entity.master;

import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.junit.Ignore;

/**
 * @author Ramki
 */
@Ignore
public class PropertyUsageBuilder {
	private final PropertyUsage propertyUsage;

	public PropertyUsageBuilder() {
		propertyUsage = new PropertyUsage();
	}

	public PropertyUsage build() {
		return propertyUsage;
	}
}
