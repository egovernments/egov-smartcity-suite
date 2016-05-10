package org.egov.ptis.builder.entity.master;

import org.egov.ptis.domain.entity.property.PropertyTypeMaster;

/**
 * @author Ramki
 */
 
public class PropertyTypeMasterBuilder {
	private final PropertyTypeMaster propertyTypeMaster;

	public PropertyTypeMasterBuilder() {
		propertyTypeMaster = new PropertyTypeMaster();
	}

	public PropertyTypeMaster build() {
		return propertyTypeMaster;
	}

	public PropertyTypeMasterBuilder withType(final String type) {
		propertyTypeMaster.setType(type);
		return this;
	}
}
