package org.egov.infstr.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Date;

import org.junit.Test;

public class StringToDateConverterTest {

	@Test
	public void testConvert() {
		final StringToDateConverter converter = new StringToDateConverter();
		Object object = converter.convert(Object.class, null);
		assertNull(object);
		object = converter.convert(Date.class, "10/10/2001");
		assertNotNull(object);
		object = converter.convert(String.class, new Date());
		assertNotNull(object);
		object = converter.convert(String.class, "10/10/2001");
		assertNotNull(object);

	}

	@Test
	public void testConvertException() {
		final StringToDateConverter converter = new StringToDateConverter();
		Object object = null;
		try {
			object = converter.convert(Date.class, new Date());
		} catch (final Exception e) {
			assertTrue(true);
		}
		assertNull(object);
		try {
			object = converter.convert(Date.class, "ld/sd/454");
		} catch (final Exception e) {
			assertTrue(true);
		}
		assertNull(object);

		try {
			final Method m = StringToDateConverter.class.getDeclaredMethod(
					"convertToString", Class.class, Object.class);
			m.setAccessible(true);
			object = m.invoke(converter, Date.class, new Object());
		} catch (final Exception e) {
			assertTrue(true);
		}
		assertNull(object);

		try {
			final Method m = StringToDateConverter.class.getDeclaredMethod(
					"convertToDate", Class.class, Object.class);
			m.setAccessible(true);
			object = m.invoke(converter, Date.class, new Object());
		} catch (final Exception e) {
			assertTrue(true);
		}
		assertNull(object);
	}
}
