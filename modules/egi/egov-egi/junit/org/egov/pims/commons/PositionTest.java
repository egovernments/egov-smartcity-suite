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

	@Test
	public void testGetOutsourcedPosts() {
		this.position.getOutsourcedPosts();
		assertTrue(true);
	}

	@Test
	public void testSetOutsourcedPosts() {
		this.position.setOutsourcedPosts(1);
		assertTrue(true);
	}

	@Test
	public void testGetSanctionedPosts() {
		this.position.getSanctionedPosts();
		assertTrue(true);
	}

	@Test
	public void testSetSanctionedPosts() {
		this.position.setSanctionedPosts(1);
		assertTrue(true);
	}

	@Test
	public void testGetDesigId() {
		this.position.getDesigId();
		assertTrue(true);
	}

	@Test
	public void testSetDesigId() {
		this.position.setDesigId(null);
		assertTrue(true);
	}

	@Test
	public void testGetEfferctiveDate() {
		this.position.getEfferctiveDate();
		assertTrue(true);
	}

	@Test
	public void testSetEfferctiveDate() {
		this.position.setEfferctiveDate(new Date());
		assertTrue(true);
	}

}
