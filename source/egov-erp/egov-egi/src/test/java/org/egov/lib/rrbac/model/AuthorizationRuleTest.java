package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AuthorizationRuleTest {

	@Test
	public void testGetId() {
		final AuthorizationRule authRule = new AuthorizationRule();
		authRule.setAction(null);
		authRule.setCreatedBy(null);
		authRule.setCreatedDate(null);
		authRule.setId(1l);
		authRule.setModifiedBy(null);
		authRule.setModifiedDate(null);
		authRule.setObjectType(null);
		authRule.setScript(null);

		authRule.getAction();
		authRule.getCreatedBy();
		authRule.getCreatedDate();
		authRule.getId();
		authRule.getModifiedBy();
		authRule.getModifiedDate();
		authRule.getObjectType();
		authRule.getScript();
		assertTrue(true);
	}

}
