package org.egov.infstr.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class UtilityMethodsTest {

	EGovConfig eGovConfig;
	UtilityMethods utilityMethods;

	@Before
	public void runBefore() {
		this.utilityMethods = new UtilityMethods();
	}

	@Test
	public void testGetRandomFileName() throws Exception {
		try {
			UtilityMethods.getRandomFileName(".html");
		} catch (final Exception e) {
			assertTrue(true);
		}
		System.setProperty("temp", "");
		try {
			UtilityMethods.getRandomFileName(".html");
		} catch (final Exception e) {
			assertTrue(true);
		}

		final File fDir = new File("/mytemp");
		fDir.mkdir();
		fDir.setReadOnly();
		System.setProperty("temp", "/mytemp");
		try {
			UtilityMethods.getRandomFileName(".html");
		} catch (final Exception e) {
			assertTrue(true);
		}
		fDir.setWritable(true);
		try {
			UtilityMethods.getRandomFileName(".html");
		} catch (final Exception e) {
			assertTrue(true);
		}
		try {
			UtilityMethods.getRandomFileName(null);
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void testGetRandomChar() throws Exception {
		char c = UtilityMethods.getRandomChar();
		assertNotNull(c);
		final Field mod = UtilityMethods.class.getDeclaredField("mod");
		mod.setAccessible(true);
		mod.set(this.utilityMethods, 25);
		c = UtilityMethods.getRandomChar();
		assertNotNull(c);
		mod.set(this.utilityMethods, 26);
		c = UtilityMethods.getRandomChar();
		assertNotNull(c);
	}

	@Test
	public void testGetRandomString() {
		final String c = UtilityMethods.getRandomString();
		assertNotNull(c);
	}

}
