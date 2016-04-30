package org.egov.ptis.builder.entity.property;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.junit.Ignore;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ramki
 */
@Ignore
public class BasicPropertyBuilder {
	private final BasicProperty basicProperty;

	public BasicPropertyBuilder() {
		basicProperty = new BasicPropertyImpl();
	}

	public BasicProperty build() {
		return basicProperty;
	}

	public BasicPropertyBuilder withUpicNo(final String upicNo) {
		basicProperty.setUpicNo(upicNo);
		return this;
	}

	public BasicPropertyBuilder withActive() {
		basicProperty.setActive(Boolean.TRUE);
		return this;
	}

	public BasicPropertyBuilder withInActive() {
		basicProperty.setActive(Boolean.FALSE);
		return this;
	}

	public BasicPropertyBuilder withPropertyID(final PropertyID propertyId) {
		basicProperty.setPropertyID(propertyId);
		return this;
	}

	public BasicPropertyBuilder withPropertySet(final Set<Property> propertySet) {
		basicProperty.setPropertySet(propertySet);
		for(Property property : propertySet){
			property.setBasicProperty(basicProperty);
		}
		return this;
	}

	public BasicPropertyBuilder withDefaults() {
		Set<Property> propertySet = new HashSet<Property>();
		withUpicNo("UPIC001");
		withActive();
		propertySet.add(new PropertyBuilder().withDefaults().build());
		withPropertySet(propertySet);
		withPropertyID(new PropertyIDBuilder().withDefaults().build());
		return this;
	}
}
