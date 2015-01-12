package org.egov.lib.rjbac.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class UserDetailImplTest {

	@Test
	public void testClosureUserDetail() {
		final UserDetailImpl userDImpl = new UserDetailImpl();
		userDImpl.setExtrafield1("1");
		userDImpl.setExtrafield2("2");
		userDImpl.setExtrafield3("3");
		userDImpl.setBankBranchId(1);
		userDImpl.setId(1);
		final Date currDate = new Date();
		userDImpl.setDob(currDate);
		userDImpl.setEmpId(1);
		userDImpl.setLocale("enUs");

		assertEquals("1", userDImpl.getExtrafield1());
		assertEquals("2", userDImpl.getExtrafield2());
		assertEquals("3", userDImpl.getExtrafield3());
		assertEquals(Integer.valueOf(1), userDImpl.getId());
		assertEquals(Integer.valueOf(1), userDImpl.getBankBranchId());
		assertEquals(currDate, userDImpl.getDob());
		assertEquals(Integer.valueOf(1), userDImpl.getEmpId());
		assertEquals("enUs", userDImpl.getLocale());
		assertTrue(userDImpl.equals(userDImpl));
	}

}
