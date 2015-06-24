package org.egov.ptis.builder.entity.property;

import java.util.HashSet;
import java.util.Set;

import org.egov.ptis.builder.entity.master.PropertyTypeMasterBuilder;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.junit.Ignore;

/**
 * @author Ramki
 */

public class PropertyDetailBuilder {
	private final PropertyDetail propertyDetail;

	public PropertyDetailBuilder(String type) {
		if (type == "Builtup") {
			propertyDetail = new BuiltUpProperty();
		} else {
			propertyDetail = new VacantProperty();
		}
	}

	public PropertyDetail build() {
		return propertyDetail;
	}

	public PropertyDetailBuilder withPropertyType(final PropertyTypeMaster propertyTypeMaster) {
		propertyDetail.setPropertyTypeMaster(propertyTypeMaster);
		return this;
	}

	public PropertyDetailBuilder withFloorDetails(final Set<FloorIF> floorDetails) {
		propertyDetail.setFloorDetails(floorDetails);
		return this;
	}

	public PropertyDetailBuilder withDefaults() {
		Set<FloorIF> floorSet = new HashSet<FloorIF>();
		FloorIF floor = new FloorBuilder().withDefaults().build();
		floorSet.add(floor);
		propertyDetail.setPropertyTypeMaster(new PropertyTypeMasterBuilder().withType("Residential").build());
		propertyDetail.setFloorDetails(floorSet);
		return this;
	}
}
