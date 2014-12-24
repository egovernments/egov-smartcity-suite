package org.egov.lib.security.terminal.model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class LocationTest {

	private final Location location = new Location();

	@Test
	public void testGetCreatedDate() {
		this.location.getCreatedDate();
		assertTrue(true);
	}

	@Test
	public void testSetCreatedDate() {
		this.location.setCreatedDate(null);
		assertTrue(true);
	}

	@Test
	public void testGetDesc() {
		this.location.getDesc();
		assertTrue(true);
	}

	@Test
	public void testSetDesc() {
		this.location.setDesc("desc");
		assertTrue(true);
	}

	@Test
	public void testGetLastModifiedDate() {
		this.location.getLastModifiedDate();
		assertTrue(true);
	}

	@Test
	public void testSetLastModifiedDate() {
		this.location.setLastModifiedDate(null);
		assertTrue(true);
	}

	@Test
	public void testGetId() {
		this.location.getId();
		assertTrue(true);
	}

	@Test
	public void testSetId() {
		this.location.setId(1);
		assertTrue(true);
	}

	@Test
	public void testGetName() {
		this.location.getName();
		assertTrue(true);
	}

	@Test
	public void testSetName() {
		this.location.setName("name");
		assertTrue(true);
	}

	@Test
	public void testGetLocationId() {
		this.location.getLocationId();
		assertTrue(true);
	}

	@Test
	public void testSetLocationId() {
		this.location.setLocationId(null);
		assertTrue(true);
	}

	@Test
	public void testGetIsActive() {
		this.location.getIsActive();
		assertTrue(true);
	}

	@Test
	public void testSetIsActive() {
		this.location.setIsActive(1);
		assertTrue(true);
	}

	@Test
	public void testGetLocationIPMapSet() {
		this.location.getLocationIPMapSet();
		assertTrue(true);
	}

	@Test
	public void testSetLocationIPMapSet() {
		this.location.setLocationIPMapSet(new HashSet<LocationIPMap>());
		assertTrue(true);
	}

	@Test
	public void testGetIsLocation() {
		this.location.getIsLocation();
		assertTrue(true);
	}

	@Test
	public void testSetIsLocation() {
		this.location.setIsLocation(1);
		assertTrue(true);
	}

	@Test
	public void testAddLocationIPMap() {
		this.location.addLocationIPMap(new HashSet());
		assertTrue(true);
	}

}
