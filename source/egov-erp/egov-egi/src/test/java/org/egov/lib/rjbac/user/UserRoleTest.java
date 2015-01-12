package org.egov.lib.rjbac.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.egov.lib.rjbac.role.RoleImpl;
import org.junit.Test;

public class UserRoleTest {

	@Test
	public void testClosureUserRole() {
		final UserRole userRole = new UserRole();
		userRole.setFromDate(new Date());
		assertNotNull(userRole.getFromDate());
		userRole.setId(1);
		assertEquals(Integer.valueOf(1), userRole.getId());
		userRole.setIsHistory('Y');
		assertEquals(Character.valueOf('Y'), userRole.getIsHistory());
		userRole.setRole(new RoleImpl());
		assertNotNull(userRole.getRole());
		userRole.setToDate(new Date());
		assertNotNull(userRole.getToDate());
		userRole.setUser(new UserImpl());
		assertNotNull(userRole.getUser());

	}

}
