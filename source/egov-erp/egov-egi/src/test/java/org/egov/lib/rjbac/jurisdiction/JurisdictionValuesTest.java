package org.egov.lib.rjbac.jurisdiction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.egov.lib.admbndry.BoundaryImpl;
import org.junit.Before;
import org.junit.Test;

public class JurisdictionValuesTest {
	private JurisdictionValues juriValue;

	@Before
	public void runBefore() {
		this.juriValue = new JurisdictionValues();
	}

	@Test
	public void testClosures() {
		this.juriValue.setId(1);
		assertEquals(Integer.valueOf(1), this.juriValue.getId());
		this.juriValue.setBoundary(new BoundaryImpl());
		assertNotNull(this.juriValue.getBoundary());
		this.juriValue.setFromDate(new Date());
		assertNotNull(this.juriValue.getFromDate());
		this.juriValue.setIsHistory('Y');
		assertEquals(Character.valueOf('Y'), this.juriValue.getIsHistory());
		this.juriValue.setToDate(new Date());
		assertNotNull(this.juriValue.getToDate());
		this.juriValue.setUserJurLevel(new Jurisdiction());
		assertNotNull(this.juriValue.getUserJurLevel());
	}

}
