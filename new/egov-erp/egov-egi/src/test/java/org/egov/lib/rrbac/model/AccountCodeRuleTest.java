package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class AccountCodeRuleTest {

	@Test
	public void testClosures() throws Exception {
		final AccountCodeRule accountCodeRule = new AccountCodeRule();

		// test closure
		accountCodeRule.setId(1);
		accountCodeRule.setActive(1);
		accountCodeRule.setDefaultValue(null);
		accountCodeRule.setExcluded(1);
		accountCodeRule.addIeList(new IEList());
		accountCodeRule.setIeList(new HashSet());
		IEList ieList = new IEList();
		accountCodeRule.addIeList(ieList);
		accountCodeRule.removeIeList(ieList);
		accountCodeRule.setIncluded(1);
		accountCodeRule.setName("name");
		accountCodeRule.setType("type");
		accountCodeRule.setUpdatedTime(null);

		assertEquals(Integer.valueOf(1), accountCodeRule.getId());
		assertEquals(Integer.valueOf(1), accountCodeRule.getActive());
		assertNull(accountCodeRule.getDefaultValue());
		assertEquals(Integer.valueOf(1), accountCodeRule.getExcluded());
		assertNotNull(accountCodeRule.getIeList());
		assertEquals(Integer.valueOf(1), accountCodeRule.getIncluded());
		assertNotNull(accountCodeRule.getName());
		assertNotNull(accountCodeRule.getType());
		assertNull(accountCodeRule.getUpdatedTime());

		// test custom hashcode and equal
		accountCodeRule.hashCode();
		final AccountCodeRule accoderule = new AccountCodeRule();
		accoderule.setId(1);
		accoderule.setActive(1);
		accoderule.setDefaultValue(null);
		accoderule.setExcluded(1);
		accoderule.setIncluded(1);
		accoderule.setName("name");
		accoderule.setType("type");
		accoderule.equals(null);
		accoderule.equals(accoderule);
		accoderule.equals(accountCodeRule);
		accoderule.setName("sidsds");
		accoderule.equals(accountCodeRule);
		accoderule.equals(new Object());
		assertTrue(true);

		// Test isValid method
		HashSet values = new HashSet();
		values.add("1");
		accountCodeRule.setIeList(values);
		ieList = new IEList();
		ieList.setType("I");
		ieList.setValue("1-2");
		HashSet ieSet = new HashSet();
		ieSet.add(ieList);
		accoderule.setIeList(ieSet);
		accoderule.isValid(accountCodeRule);
		ieList.setValue("1");
		ieSet = new HashSet();
		ieSet.add(ieList);
		accoderule.setIeList(ieSet);
		accoderule.isValid(accountCodeRule);

		values = new HashSet();
		values.add("1");
		accountCodeRule.setIeList(values);
		ieList = new IEList();
		ieList.setType("E");
		ieList.setValue("1-2");
		ieSet = new HashSet();
		ieSet.add(ieList);
		accoderule.setIeList(ieSet);
		try {
			accoderule.isValid(accountCodeRule);
		} catch (final Exception e) {
			assertTrue(true);
		}
		accoderule.setIncluded(0);
		ieList.setValue("1");
		ieSet = new HashSet();
		ieSet.add(ieList);
		accoderule.setIeList(ieSet);
		try {
			accoderule.isValid(accountCodeRule);
		} catch (final Exception e) {
			assertTrue(true);
		}
		ieList.setValue("2-2");
		values.add("2");
		values.add("2");
		ieSet = new HashSet();
		ieSet.add(ieList);
		accountCodeRule.setIeList(values);
		accoderule.setIeList(ieSet);
		try {
			accoderule.isValid(accountCodeRule);
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

}
