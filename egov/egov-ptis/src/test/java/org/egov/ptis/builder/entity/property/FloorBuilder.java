package org.egov.ptis.builder.entity.property;

import org.egov.commons.Area;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyUsage;

import java.util.Date;

/**
 * @author Ramki
 */

public class FloorBuilder {
	private final Floor floor;

	public FloorBuilder() {
		floor = new Floor();
	}

	public Floor build() {
		return floor;
	}

	public FloorBuilder withBuiltUpArea(final Float builtUpArea) {
		Area area = new Area();
		area.setArea(builtUpArea);
		floor.setBuiltUpArea(area);
		return this;
	}

	public FloorBuilder withUsage(final PropertyUsage propertyUsage) {
		floor.setPropertyUsage(propertyUsage);
		return this;
	}

	public FloorBuilder withOccupancy(final PropertyOccupation propertyOccupation) {
		floor.setPropertyOccupation(propertyOccupation);
		return this;
	}

	public FloorBuilder withOccupancyDate(final Date date) {
		floor.setCreatedDate(date);
		return this;
	}

	public FloorBuilder withDefaults() {
		withOccupancyDate(new Date());
		withBuiltUpArea(1000F);
		return this;
	}
}
