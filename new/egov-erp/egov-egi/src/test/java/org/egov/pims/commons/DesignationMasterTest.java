package org.egov.pims.commons;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class DesignationMasterTest {

	private final DesignationMaster designationMaster = new DesignationMaster();

	@Test
	public void testGetPositionSet() {
		this.designationMaster.getPositionSet();
		assertTrue(true);
	}

	@Test
	public void testSetPositionSet() {
		this.designationMaster.setPositionSet(new HashSet<Position>());
		assertTrue(true);
	}

	Position position = new Position();

	@Test
	public void testAddPosition() {
		this.designationMaster.addPosition(this.position);
		assertTrue(true);
	}

	@Test
	public void testRemovePosition() {
		this.designationMaster.removePosition(this.position);
		assertTrue(true);
	}

	@Test
	public void testGetDeptId() {
		this.designationMaster.getDeptId();
		assertTrue(true);
	}

	@Test
	public void testSetDeptId() {
		this.designationMaster.setDeptId(1);
		assertTrue(true);
	}

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
	public void testGetDesignationLocal() {
		this.designationMaster.getDesignationLocal();
		assertTrue(true);
	}

	@Test
	public void testSetDesignationLocal() {
		this.designationMaster.setDesignationLocal("designationLocal");
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

	@Test
	public void testGetOfficerLevel() {
		this.designationMaster.getOfficerLevel();
		assertTrue(true);
	}

	@Test
	public void testSetOfficerLevel() {
		this.designationMaster.setOfficerLevel(1);
		assertTrue(true);
	}

	@Test
	public void testGetOutsourcedPosts() {
		this.designationMaster.getOutsourcedPosts();
		assertTrue(true);
	}

	@Test
	public void testSetOutsourcedPosts() {
		this.designationMaster.setOutsourcedPosts(1);
		assertTrue(true);
	}

	@Test
	public void testGetSanctionedPosts() {
		this.designationMaster.getSanctionedPosts();
		assertTrue(true);
	}

	@Test
	public void testSetSanctionedPosts() {
		this.designationMaster.setSanctionedPosts(1);
		assertTrue(true);
	}
}
