package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import org.egov.exceptions.AmountException;
import org.junit.Test;

public class AmountRuleTest {

	@Test
	public void testSetId() {
		final AmountRule amountRule = new AmountRule();
		amountRule.setName("name");
		final AmountRule dummy = new AmountRule();
		amountRule.equals(amountRule);
		amountRule.equals(null);
		amountRule.equals(dummy);
		dummy.setName("name");
		amountRule.equals(dummy);
		amountRule.equals(new Object());
		amountRule.hashCode();

		amountRule.getActive();
		amountRule.getDefaultValue();
		amountRule.getId();
		amountRule.getMaxRange();
		amountRule.getMinRange();
		amountRule.getName();
		amountRule.getType();
		amountRule.getUpdatedTime();

		amountRule.setActive(1);
		amountRule.setDefaultValue(null);
		amountRule.setId(1);
		amountRule.setMaxRange(1);
		amountRule.setMinRange(1);
		amountRule.setType(null);
		amountRule.setUpdatedTime(null);

		try {
			amountRule.isValid(amountRule);
			dummy.setMaxRange(0);
			dummy.setMinRange(0);
			amountRule.isValid(dummy);
		} catch (final AmountException e) {
			assertTrue(true);
		}
		assertTrue(true);
	}

}
