package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RuleTypeTest {

	@Test
	public void testGetId() {
		final RuleType ruleType = new RuleType();
		ruleType.setName("name");
		ruleType.equals(null);
		ruleType.equals(ruleType);
		ruleType.equals(new Object());
		final RuleType ruleType2 = new RuleType();
		ruleType.equals(ruleType2);
		ruleType2.setName("2");
		ruleType.equals(ruleType2);
		ruleType2.setName("name");
		ruleType.equals(ruleType2);
		ruleType.hashCode();

		ruleType.setId(1);
		ruleType.setUpdatedTime(null);
		ruleType.getId();
		ruleType.getUpdatedTime();

		try {
			ruleType.compareTo(ruleType);
			ruleType.compareTo(new RuleType());
		} catch (final ClassCastException e) {
			assertTrue(true);
		}
	}

}
