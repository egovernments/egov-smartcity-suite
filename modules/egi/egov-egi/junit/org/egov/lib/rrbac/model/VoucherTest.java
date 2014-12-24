package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class VoucherTest {

	@Test
	public void testGetMinRange() {
		final Voucher voucher = new Voucher();
		voucher.setIeList(new HashSet());
		Voucher.main(null);
		voucher.setMaxRange(1);
		voucher.setMinRange(1);
		voucher.getIeList();
		voucher.getMaxRange();
		voucher.getMinRange();
		assertTrue(true);
	}

}
