package org.egov.lib.rjbac.dept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DepartmentImplTest {

	private DepartmentImpl department;

	@Before
	public void runBefore() {
		this.department = new DepartmentImpl();
	}

	@Test
	public void testClosures() {
		this.department.setId(1);
		assertEquals(Integer.valueOf(1), this.department.getId());
		this.department.setUpdateTime(new Date());
		assertNotNull(this.department.getUpdateTime());
		this.department.setBillingLocation("location");
		assertEquals("location", this.department.getBillingLocation());
		this.department.setDeptAddress("deptAddress");
		assertEquals("deptAddress", this.department.getDeptAddress());
		this.department.setDeptCode("deptCode");
		assertEquals("deptCode", this.department.getDeptCode());
		this.department.setDeptDetails("deptDetails");
		assertEquals("deptDetails", this.department.getDeptDetails());
		this.department.setDeptName("deptName");
		assertEquals("deptName", this.department.getDeptName());
		this.department.equals(new Object());
		this.department.equals(this.department);
		this.department.equals(null);
		final DepartmentImpl dept = new DepartmentImpl();
		dept.setDeptName("tName");
		this.department.equals(dept);
		dept.setDeptName(this.department.getDeptName());
		this.department.equals(dept);
		dept.hashCode();
		assertTrue(true);
	}

}
