package org.egov.lib.security.terminal.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserCounterMapTest {
	private final UserCounterMap userCounterMap = new UserCounterMap();

	@Test
	public void testGetCounterId() {
		this.userCounterMap.getCounterId();
		assertTrue(true);
	}

	@Test
	public void testSetCounterId() {
		this.userCounterMap.setCounterId(null);
		assertTrue(true);
	}

	@Test
	public void testGetId() {
		this.userCounterMap.getId();
		assertTrue(true);
	}

	@Test
	public void testSetId() {
		this.userCounterMap.setId(1);
		assertTrue(true);
	}

	@Test
	public void testGetUserId() {
		this.userCounterMap.getUserId();
		assertTrue(true);
	}

	@Test
	public void testSetUserId() {
		this.userCounterMap.setUserId(null);
		assertTrue(true);
	}

	@Test
	public void testGetFromDate() {
		this.userCounterMap.getFromDate();
		assertTrue(true);
	}

	@Test
	public void testSetFromDate() {
		this.userCounterMap.setFromDate(null);
		assertTrue(true);
	}

	@Test
	public void testGetModifiedDate() {
		this.userCounterMap.getModifiedDate();
		assertTrue(true);
	}

	@Test
	public void testSetModifiedDate() {
		this.userCounterMap.setModifiedDate(null);
		assertTrue(true);
	}

	@Test
	public void testGetModifiedBy() {
		this.userCounterMap.getModifiedBy();
		assertTrue(true);
	}

	@Test
	public void testSetModifiedBy() {
		this.userCounterMap.setModifiedBy(1);
		assertTrue(true);
	}

	@Test
	public void testGetToDate() {
		this.userCounterMap.getToDate();
		assertTrue(true);
	}

	@Test
	public void testSetToDate() {
		this.userCounterMap.setToDate(null);
		assertTrue(true);
	}

}
