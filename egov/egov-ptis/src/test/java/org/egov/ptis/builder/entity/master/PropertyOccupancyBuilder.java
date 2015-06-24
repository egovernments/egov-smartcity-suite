package org.egov.ptis.builder.entity.master;

import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.junit.Ignore;

/**
 * @author Ramki
 */
@Ignore
public class PropertyOccupancyBuilder {
	private final PropertyOccupation propertyOccupation;

	public PropertyOccupancyBuilder() {
		propertyOccupation = new PropertyOccupation();
	}

	public PropertyOccupation build() {
		return propertyOccupation;
	}
}
