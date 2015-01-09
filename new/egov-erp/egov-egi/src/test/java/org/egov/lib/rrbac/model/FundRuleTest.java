package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.egov.exceptions.FundException;
import org.junit.Test;

public class FundRuleTest {

	@Test
	public void testGetId() {
		final FundRule fundRule = new FundRule();
		fundRule.setName("name");
		fundRule.equals(null);
		fundRule.equals(fundRule);
		fundRule.equals(new Object());
		final FundRule fundRule2 = new FundRule();
		fundRule2.setName("namdse");
		fundRule.equals(fundRule2);
		fundRule2.setName("name");
		fundRule.equals(fundRule2);
		fundRule.hashCode();

		fundRule.setActive(1);
		fundRule.setDefaultValue(null);
		fundRule.setExcluded(1);
		fundRule.setId(1);
		final IEList ieListObj = new IEList();
		fundRule.addIeList(ieListObj);
		HashSet ieList = new HashSet();
		fundRule.setIeList(ieList);
		fundRule.setIncluded(1);
		fundRule.setType(null);
		fundRule.setUpdatedTime(null);

		assertNotNull(fundRule.getActive());
		assertNull(fundRule.getDefaultValue());
		assertNotNull(fundRule.getExcluded());
		assertNotNull(fundRule.getId());
		assertNotNull(fundRule.getIeList());
		assertNotNull(fundRule.getIncluded());
		assertNotNull(fundRule.getName());
		assertNull(fundRule.getType());
		assertNull(fundRule.getUpdatedTime());

		try {
			ieList = new HashSet();
			ieList.add("1");
			fundRule.setType("I");
			fundRule2.setIeList(ieList);
			fundRule.addIeList(ieListObj);
			fundRule2.setActive(1);
			fundRule2.setIncluded(1);
			fundRule2.setExcluded(1);
			final HashSet ieList2 = new HashSet();
			ieListObj.setValue("1");
			ieListObj.setType("I");
			ieList2.add(ieListObj);
			fundRule.setIeList(ieList2);
			fundRule.isValid(fundRule2);
			fundRule.setType("E");
			fundRule.isValid(fundRule2);
			final IEList ieListObj2 = new IEList();
			ieListObj2.setValue("1");
			ieListObj2.setType("E");
			ieList2.add(ieListObj2);
			fundRule.setIeList(ieList2);
			fundRule.isValid(fundRule2);

		} catch (final FundException e) {
			assertTrue(true);
		}
		try {
			fundRule2.setType("E");
			fundRule.removeIeList(ieListObj);
			fundRule.isValid(fundRule2);
		} catch (final FundException e) {
			assertTrue(true);
		}
		assertTrue(true);
	}

}
