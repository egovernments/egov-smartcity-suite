package org.egov.lib.security.terminal.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserValidateTest {

	private final UserValidate userValidate = new UserValidate();

	@Test
	public void testGetIpAddress() {
		this.userValidate.getIpAddress();
		assertTrue(true);
	}

	@Test
	public void testSetIpAddress() {
		this.userValidate.setIpAddress("ipAddress");
		assertTrue(true);
	}

	@Test
	public void testGetPassword() {
		this.userValidate.getPassword();
		assertTrue(true);
	}

	@Test
	public void testSetPassword() {
		this.userValidate.setPassword("password");
		assertTrue(true);
	}

	@Test
	public void testGetUsername() {
		this.userValidate.getUsername();
		assertTrue(true);
	}

	@Test
	public void testSetUsername() {
		this.userValidate.setUsername("username");
		assertTrue(true);
	}

	@Test
	public void testGetCounterId() {
		this.userValidate.getCounterId();
		assertTrue(true);
	}

	@Test
	public void testSetCounterId() {
		this.userValidate.setCounterId(1);
		assertTrue(true);
	}

	@Test
	public void testGetLocationId() {
		this.userValidate.getLocationId();
		assertTrue(true);
	}

	@Test
	public void testSetLocationId() {
		this.userValidate.setLocationId(1);
		assertTrue(true);
	}

}
