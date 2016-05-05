package org.egov.ptis.client.service.calculator;

import org.egov.builder.entities.BoundaryBuilder;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.builder.entity.property.BasicPropertyBuilder;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

import static org.mockito.MockitoAnnotations.initMocks;

public class APTaxCalculatorTest {
	@Autowired
	private APTaxCalculator taxCalculator;

	private Boundary locality;
	private Property property;
	private BasicProperty basicProperty;
	private HashMap<Installment, TaxCalculationInfo> taxInfo = new HashMap<Installment, TaxCalculationInfo>();

	@Before
	public void before() {
		initMocks(this);
		initMasters();
		initProperty();
	}

	private void initMasters() {
		locality = new BoundaryBuilder().withDefaults().build();
	}

	private void initProperty() {
		basicProperty = new BasicPropertyBuilder().withDefaults().build();
		property = basicProperty.getProperty();
	}

	@Ignore
	public void calculatePropertyTax() throws TaxCalculatorExeption {
		taxInfo = taxCalculator.calculatePropertyTax(property, new Date());
	}
}
