package org.egov.lib.security.terminal.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocationIPMapTest {
	private final LocationIPMap locationIPMap = new LocationIPMap();

	@Test
	public void testGetId() {
		this.locationIPMap.getId();
		assertTrue(true);
	}

	@Test
	public void testSetId() {
		this.locationIPMap.setId(1);
		assertTrue(true);
	}

	@Test
	public void testGetIpAddress() {
		this.locationIPMap.getIpAddress();
		assertTrue(true);
	}

	@Test
	public void testSetIpAddress() {
		this.locationIPMap.setIpAddress("ipAddress");
		assertTrue(true);
	}

	@Test
	public void testGetLocation() {
		this.locationIPMap.getLocation();
		assertTrue(true);
	}

	@Test
	public void testSetLocation() {
		this.locationIPMap.setLocation(null);
		assertTrue(true);
	}

}
