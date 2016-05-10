package org.egov.ptis.builder.entity.property;

import org.egov.builder.entities.ModuleBuilder;
import org.egov.commons.Installment;
import org.egov.commons.entity.InstallmentBuilder;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;

/**
 * @author Ramki
 */

public class PropertyBuilder {
	private final PropertyImpl property;

	public PropertyBuilder() {
		property = new PropertyImpl();
	}

	public PropertyImpl build() {
		return property;
	}

	public PropertyBuilder withInstallment(final Installment installment) {
		property.setInstallment(installment);
		return this;
	}

	public PropertyBuilder withActiveStatus() {
		property.setStatus('A');
		return this;
	}

	public PropertyBuilder withBasicProperty(final BasicProperty basicProperty) {
		property.setBasicProperty(basicProperty);
		return this;
	}

	public PropertyBuilder withPropertyDetail(final PropertyDetail propertyDetail) {
		property.setPropertyDetail(propertyDetail);
		return this;
	}

	public PropertyBuilder withDefaults() {
		property.setStatus('A');
		property.setIsDefaultProperty('Y');
		property.setInstallment(new InstallmentBuilder().withCurrentHalfPeriod(
				new ModuleBuilder().withName("Property Tax").build()).build());
		property.setPropertyDetail(new PropertyDetailBuilder("BuiltUp").withDefaults().build());
		return this;
	}
}
