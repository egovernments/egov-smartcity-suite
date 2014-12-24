package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class RuleGroupTest {

	@Test
	public void testGetId() {
		final RuleGroup ruleGroup = new RuleGroup();
		ruleGroup.setName("name");
		ruleGroup.equals(null);
		ruleGroup.equals(ruleGroup);
		ruleGroup.equals(new Object());
		final RuleGroup ruleGroup2 = new RuleGroup();
		ruleGroup.equals(ruleGroup2);
		ruleGroup2.setName("2");
		ruleGroup.equals(ruleGroup2);
		ruleGroup2.setName("name");
		ruleGroup.equals(ruleGroup2);
		ruleGroup.hashCode();

		ruleGroup.setId(1);
		ruleGroup.setUpdatedTime(null);
		ruleGroup.setRules(new HashSet());
		ruleGroup.addRules(new AmountRule());
		ruleGroup.getId();
		ruleGroup.getUpdatedTime();
		ruleGroup.setRoleId(null);
		ruleGroup.getRoleId();
		try {
			ruleGroup.compareTo(ruleGroup);
			ruleGroup.compareTo(new RuleGroup());
		} catch (final ClassCastException e) {
			assertTrue(true);
		}
	}

}
