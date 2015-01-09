package org.egov.lib.rjbac.jurisdiction;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JurisdictionValImplTest {

	@Test
	public void testJurisdicationVal() {
		final JurisdictionValImpl jurVal = new JurisdictionValImpl();
		jurVal.myJurisdictionLevel = new JurisdictionLevelImpl();
		assertNotNull(jurVal.myJurisdictionLevel);
	}
}
