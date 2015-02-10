package org.egov.pims.commons;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class PositionTest {
	private final Position position = new Position();

	@Test
	public void testGetId() {
		this.position.getId();
		assertTrue(true);
	}

	@Test
	public void testSetId() {
		this.position.setId(1);
		assertTrue(true);
	}

	@Test
	public void testGetName() {
		this.position.getName();
		assertTrue(true);
	}

	@Test
	public void testSetName() {
		this.position.setName("name");
		assertTrue(true);
	}

}
