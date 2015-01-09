package org.egov.web.converters;

import junit.framework.TestCase;

public class MoneyConverterTest extends TestCase {

	private MoneyConverter moneyConverter;

	public void testConvertFromString() {
		this.moneyConverter = new MoneyConverter();
		final String[] values = { "10" };
		Object obj = this.moneyConverter.convertFromString(null, values, null);
		assertNotNull(obj);
		obj = this.moneyConverter.convertFromString(null, null, null);
		assertNotNull(obj);
	}

	public void testConvertToString() {
		this.moneyConverter = new MoneyConverter();
		assertEquals("", this.moneyConverter.convertToString(null, ""));
	}

}
