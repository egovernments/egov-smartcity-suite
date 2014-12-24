package org.egov.infstr.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.junit.Test;

public class DateToStringConverterTest {

	@Test
	public void convertTest() {
		final DateToStringConverter dateConverter = new DateToStringConverter();
		Object o = dateConverter.convert(Class.class, null);
		assertNull(o);
		final Date dt = new Date();
		try {
			o = dateConverter.convert(Class.class, dt);
		} catch (final Exception e) {
			assertTrue(e instanceof ConversionException);
		}

		o = dateConverter.convert(String.class, dt);
		assertNotNull(o);

		o = dateConverter.convert(String.class, "MY NAME");
		assertNotNull(o);

	}
}
