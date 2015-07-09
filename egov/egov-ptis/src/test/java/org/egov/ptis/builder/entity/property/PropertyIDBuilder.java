package org.egov.ptis.builder.entity.property;

import org.egov.builder.entities.BoundaryBuilder;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.domain.entity.property.PropertyID;

public class PropertyIDBuilder {
	private final PropertyID propertyId;

	public PropertyIDBuilder() {
		propertyId = new PropertyID();
	}

	public PropertyID build() {
		return propertyId;
	}

	public PropertyIDBuilder withZone(Boundary zone) {
		propertyId.setZone(zone);
		return this;
	}

	public PropertyIDBuilder withWard(Boundary ward) {
		propertyId.setWard(ward);
		return this;
	}

	public PropertyIDBuilder withLocality(Boundary locality) {
		propertyId.setLocality(locality);
		return this;
	}

	public PropertyIDBuilder withDefaults() {
		withZone(new BoundaryBuilder().withDefaults().build());
		return this;
	}
}
