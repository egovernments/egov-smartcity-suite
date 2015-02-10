package org.egov.pims.commons;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DesignationMasterTest {

	private final DesignationMaster designationMaster = new DesignationMaster();

	@Test
	public void testGetDesignationDescription() {
		this.designationMaster.getDesignationDescription();
		assertTrue(true);
	}

	@Test
	public void testSetDesignationDescription() {
		this.designationMaster
				.setDesignationDescription("designationDescription");
		assertTrue(true);
	}

	@Test
	public void testGetDesignationId() {
		this.designationMaster.getDesignationId();
		assertTrue(true);
	}

	@Test
	public void testSetDesignationId() {
		this.designationMaster.setDesignationId(1);
		assertTrue(true);
	}

	@Test
	public void testGetDesignationName() {
		this.designationMaster.getDesignationName();
		assertTrue(true);
	}

	@Test
	public void testSetDesignationName() {
		this.designationMaster.setDesignationName("designationName");
		assertTrue(true);
	}

}
