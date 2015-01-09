package org.egov.lib.rjbac.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;

import org.egov.lib.rrbac.model.Action;
import org.junit.Test;

public class RoleImplTest {

	@Test
	public void testClosureRoleImpl() {
		final RoleImpl role = new RoleImpl();
		role.setId(1);
		assertEquals(Integer.valueOf(1), role.getId());
		role.setParent(role);
		assertEquals(role, role.getParent());
		role.setRoleDesc("roldesc");
		assertEquals("roldesc", role.getRoleDesc());
		role.setRoleDescLocal("locdesc");
		assertEquals("locdesc", role.getRoleDescLocal());
		role.setRoleName("rolename");
		assertEquals("rolename", role.getRoleName());
		role.setRoleNameLocal("rolenamel");
		assertEquals("rolenamel", role.getRoleNameLocal());
		final Date curDate = new Date();
		role.setUpdateTime(curDate);
		assertEquals(curDate, role.getUpdateTime());
		role.setUpdateUserId(1);
		assertEquals(Integer.valueOf(1), role.getUpdateUserId());
		role.setActions(new HashSet());
		final Action action = new Action();
		action.setName("name");
		role.addAction(action);
		role.removeAction(action);
	}

	@Test
	public void testEqual() {
		final RoleImpl role = new RoleImpl();
		role.equals(null);
		role.equals(new Object());
		role.equals(role);
		role.setRoleName("name");
		final RoleImpl role2 = new RoleImpl();
		role2.setRoleName("name2");
		role.equals(role2);
		role2.setRoleName("name");
		role.equals(role2);
		role.hashCode();
	}

	@Test
	public void testCompare() {
		final RoleImpl role = new RoleImpl();
		role.setRoleName("name");
		try {
			role.compareTo(new RoleImpl());
		} catch (final Exception e) {
			assertTrue(true);
		}
		assertEquals(0, role.compareTo(role));
	}
}
