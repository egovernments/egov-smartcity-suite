package org.egov.pims.model;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class GradeMasterTest {

	private final GradeMaster gradeMaster = new GradeMaster();

	@Test
	public void testGetId() {
		this.gradeMaster.getId();
		assertTrue(true);
	}

	@Test
	public void testSetId() {
		this.gradeMaster.setId(1);
		assertTrue(true);
	}

	@Test
	public void testGetName() {
		this.gradeMaster.getName();
		assertTrue(true);
	}

	@Test
	public void testSetName() {
		this.gradeMaster.setName("name");
		assertTrue(true);
	}

	@Test
	public void testGetFromDate() {
		this.gradeMaster.getFromDate();
		assertTrue(true);
	}

	@Test
	public void testSetFromDate() {
		this.gradeMaster.setFromDate(new Date());
		assertTrue(true);
	}

	@Test
	public void testGetToDate() {
		this.gradeMaster.getToDate();
		assertTrue(true);
	}

	@Test
	public void testSetToDate() {
		this.gradeMaster.setToDate(new Date());
		assertTrue(true);
	}

	@Test
	public void testGetAge() {
		this.gradeMaster.getAge();
		assertTrue(true);
	}

	@Test
	public void testSetAge() {
		this.gradeMaster.setAge(1);
		assertTrue(true);
	}

}
