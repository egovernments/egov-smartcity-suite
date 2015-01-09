package org.egov.infstr.utils;

import org.apache.commons.configuration.ConversionException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@Ignore
public class EgovConfigTest extends AbstractPersistenceServiceTest {
	@Test
	public void egovConfigTest() throws IOException {

		String[] actualValueArray = EGovConfig.getArray("egov_config.xml",
				"MODULE_NAME", "ME#U".split("#"), "EGI");
		assertEquals(1, actualValueArray.length);

		actualValueArray = EGovConfig.getArray("MODULE_NAME",
				"ME#U".split("#"), "EGI");
		assertEquals(1, actualValueArray.length);

		actualValueArray = EGovConfig.getArray("egov_config.xml",
				"MODULE_NAMES", "ME#U".split("#"), "EGI");
		assertEquals(2, actualValueArray.length);

		actualValueArray = EGovConfig.getArray("MODULE_NAMEs",
				"ME#U".split("#"), "EGI");
		assertEquals(2, actualValueArray.length);

		actualValueArray = EGovConfig
				.getArray("MODULE_NAME", "ME#U".split("#"));
		assertEquals(2, actualValueArray.length);

		actualValueArray = EGovConfig
				.getArray("egov_config.xml", "MODULE_NAME");
		assertEquals(0, actualValueArray.length);

		actualValueArray = EGovConfig.getArray("MODULE_NAME");
		assertEquals(0, actualValueArray.length);

		long actualValueLong = 0;
		actualValueLong = EGovConfig.getLongProperty("port", 465, "mailSender");
		assertEquals(465, actualValueLong);

		try {
			actualValueLong = EGovConfig.getLongProperty("port", 1);
		} catch (final Exception e) {
			assertTrue(e instanceof NoSuchElementException);
		}

		try {
			EGovConfig.getIntProperty("MODULE_NAME", 1, "EGI");
		} catch (final Exception e) {
			assertTrue(e instanceof ConversionException);
		}

		try {
			EGovConfig.getIntProperty("MODULE_NAME", 1);
		} catch (final Exception e) {
			assertTrue(e instanceof NoSuchElementException);
		}

		try {
			EGovConfig.getDoubleProperty("MODULE_NAME", 1);
		} catch (final Exception e) {
			assertTrue(e instanceof NoSuchElementException);
		}

		try {
			EGovConfig.getDoubleProperty("egov_config.xml", "MODULE_NAME", 1);
		} catch (final Exception e) {
			assertTrue(e instanceof NoSuchElementException);
		}

		try {
			EGovConfig.getDoubleProperty("MODULE_NAME", 1, "EGI");
		} catch (final Exception e) {
			assertTrue(e instanceof ConversionException);
		}

		boolean actualValBool = false;

		try {
			actualValBool = EGovConfig.getBooleanProperty("UNIQUEIP", false,
					"IP-BASED-LOGIN");
			assertTrue(actualValBool);
			actualValBool = EGovConfig.getBooleanProperty("egov_config.xml",
					"MODULE_NAME", true, "EGI");
		} catch (final Exception e) {
			assertTrue(e instanceof ConversionException);
		}

		try {
			actualValBool = EGovConfig.getBooleanProperty("MODULE_NAME", true,
					"EGI");
		} catch (final Exception e) {
			assertTrue(e instanceof ConversionException);
		}

		try {
			actualValBool = EGovConfig.getBooleanProperty("MODULE_NAME", true);
		} catch (final Exception e) {
			assertTrue(e instanceof NoSuchElementException);
		}

		final String val = EGovConfig.getProperty(null, null, null);
		assertNull(val);

		final File f = new File("wrongXml.xml");
		if (!f.exists()) {
			final boolean created = f.createNewFile();
			System.out
					.println("Created File for Egov Config Test ? " + created);
		}
		final File f2 = new File("testsegovConf.xml");
		if (!f2.exists()) {
			final boolean created = f2.createNewFile();
			System.out
					.println("Created File for Egov Config Test ? " + created);
		}
		final FileWriter fw = new FileWriter(f);
		final FileWriter fw2 = new FileWriter(f2);
		final String xml = "<?xml version='1.0' ?><properties><value>2</value><booleans><bool>true</bool></booleans></properties>";
		final String wrongXml = "<some><some/>";
		fw.write(wrongXml);
		fw2.write(xml);
		fw2.flush();
		try {
			EGovConfig.getLongProperty(f.getName(), "some", 1);
		} catch (final Exception e) {
			assertTrue(e instanceof EGOVRuntimeException);
		}
		try {
			actualValBool = EGovConfig.getBooleanProperty("testsegovConf.xml",
					"booleans", true, "bool");
		} catch (final Exception e) {
			assertTrue(e instanceof EGOVRuntimeException);
		}

		try {
			EGovConfig.getProperty(f.getName(), "key", "defaultValue",
					"categoryName");

		} catch (final Exception e) {
			assertTrue(e instanceof EGOVRuntimeException);
		}

		try {
			EGovConfig.getProperty(null, null, null, null);
		} catch (final Exception e) {
			assertTrue(e instanceof EGOVRuntimeException);
		}

		finally {
			if (fw != null) {
				fw.close();
			}
			if (fw2 != null) {
				fw2.close();
			}
			if (f != null && f.exists()) {
				final boolean deleted = f.delete();
				System.out.println("Deleted File for Egov Config Test ? "
						+ deleted);
			}
			if (f2 != null && f2.exists()) {
				final boolean deleted = f2.delete();
				System.out.println("Deleted File for Egov Config Test ? "
						+ deleted);
			}
		}

	}

	/*
	 * @Test public void testgetMessage() {
	 * assertEquals("CORPORATION OF CHENNAI",
	 * EGovConfig.getMessage("custom.properties",
	 * "reports.title.corporation_name")); }
	 */

	private AppConfig createAppConfig(final String module, final String key,
			final String desc, final String value) {
		final AppConfig config = new AppConfig();
		config.setKeyName(key);
		config.setModule(module);
		config.setDescription(desc);

		final AppConfigValues val = new AppConfigValues();
		val.setEffectiveFrom(new Date());
		val.setKey(config);
		val.setValue(value);

		this.session.saveOrUpdate(val);
		this.session.saveOrUpdate(config);

		return config;
	}

	@Test
	public void testGetAppConfigValue() {
		final String testModule = "test.module";
		final String testKey = "test.key";
		final String testDefaultValue = "test.default.value";
		final String testDesc = "test.description";
		final String testValue = "test.value";

		String value = EGovConfig.getAppConfigValue(testModule, testKey,
				testDefaultValue);
		Assert.assertEquals(testDefaultValue, value);

		createAppConfig(testModule, testKey, testDesc, testValue);

		value = EGovConfig.getAppConfigValue(testModule, testKey,
				testDefaultValue);
		Assert.assertEquals(testValue, value);
	}
}
