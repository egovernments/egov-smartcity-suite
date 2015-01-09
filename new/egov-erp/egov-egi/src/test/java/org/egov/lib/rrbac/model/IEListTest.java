package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IEListTest {

	@Test
	public void testGetId() {
		final IEList ieList = new IEList();
		ieList.setId(1);
		ieList.equals(null);
		ieList.equals(ieList);
		ieList.equals(new Object());
		final IEList ieList2 = new IEList();
		ieList.equals(ieList2);
		ieList2.setId(2);
		ieList.equals(ieList2);
		ieList2.setId(1);
		ieList.equals(ieList2);
		ieList.hashCode();

		ieList.getUpdatedTime();
		ieList.setUpdatedTime(null);
		ieList.setRuleId(null);
		ieList.getRuleId();
		ieList.setType(null);
		ieList.getType();
		ieList.setValue(null);
		ieList.getValue();

		assertTrue(true);
	}

}
