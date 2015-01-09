package org.egov.lib.rjbac.jurisdiction;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JurisdictionLevelImplTest {

	@Test
	public void testJuridication() {
		final JurisdictionLevelImpl juriLevel = new JurisdictionLevelImpl();
		juriLevel.myJurisdictionVal = new JurisdictionValImpl();
		assertNotNull(juriLevel.myJurisdictionVal);
	}
}
