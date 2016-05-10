package org.egov.ptis.builder.entity.property;

import org.egov.ptis.builder.entity.master.PropertyTypeMasterBuilder;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacantProperty;

import java.util.ArrayList;
import java.util.List;

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

	public PropertyDetailBuilder withFloorDetails(final List<Floor> floorDetails) {
		propertyDetail.setFloorDetails(floorDetails);
		return this;
	}

	public PropertyDetailBuilder withDefaults() {
		List<Floor> floorSet = new ArrayList<Floor>();
		Floor floor = new FloorBuilder().withDefaults().build();
		floorSet.add(floor);
		propertyDetail.setPropertyTypeMaster(new PropertyTypeMasterBuilder().withType("Residential").build());
		propertyDetail.setFloorDetails(floorSet);
		return this;
	}
}
