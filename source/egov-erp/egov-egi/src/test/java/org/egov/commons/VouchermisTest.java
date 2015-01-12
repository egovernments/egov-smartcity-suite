package org.egov.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

public class VouchermisTest {

	@Test
	public void testVouchermis() {
		// Test all closures
		final Vouchermis vouchermis = new Vouchermis();
		final Vouchermis vouchermis2 = new Vouchermis(9);
		final String expected = "";
		vouchermis2.setAccountcode(expected);
		assertEquals(expected, vouchermis2.getAccountcode());
		vouchermis.setAccounthead(expected);
		assertEquals(expected, vouchermis.getAccounthead());
		final int expectedInt = 1;
		vouchermis.setAcountDepartment(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getAcountDepartment());
		vouchermis.setAssetdesc(expected);
		assertEquals(expected, vouchermis.getAssetdesc());
		vouchermis.setBillnumber(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getBillnumber());
		vouchermis.setBillregisterid(expected);
		assertEquals(expected, vouchermis.getBillregisterid());
		vouchermis.setBudgetaryAppnumber(expected);
		assertEquals(expected, vouchermis.getBudgetaryAppnumber());
		vouchermis.setCashbook(expected);
		assertEquals(expected, vouchermis.getCashbook());
		vouchermis.setConcurrancePn(expected);
		assertEquals(expected, vouchermis.getConcurrancePn());
		final Short shortVal = 1;
		vouchermis.setConcurranceSn(shortVal);
		assertEquals(shortVal, vouchermis.getConcurranceSn());
		vouchermis.setContractamt(expected);
		assertEquals(expected, vouchermis.getContractamt());
		final Date expectedDate = new Date();
		vouchermis.setCreatetimestamp(expectedDate);
		assertEquals(expectedDate, vouchermis.getCreatetimestamp());
		vouchermis.setCurrentyear(expected);
		assertEquals(expected, vouchermis.getCurrentyear());
		vouchermis.setDemandno(expected);
		assertEquals(expected, vouchermis.getDemandno());
		vouchermis.setDepartmentid(null);
		assertNull(vouchermis.getDepartmentid());
		vouchermis.setDeptacchead(expected);
		assertEquals(expected, vouchermis.getDeptacchead());
		vouchermis.setDivisioncode(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getDivisioncode());
		vouchermis.setDivisionid(null);
		assertNull(vouchermis.getDivisionid());
		vouchermis.setEmdSecurity(expected);
		assertEquals(expected, vouchermis.getEmdSecurity());
		vouchermis.setFunctionary(null);
		assertNull(vouchermis.getFunctionary());
		vouchermis.setFundsource(null);
		assertNull(vouchermis.getFundsource());
		vouchermis.setGrossded(expected);
		assertEquals(expected, vouchermis.getGrossded());
		vouchermis.setId(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getId());
		vouchermis.setIutNumber(expected);
		assertEquals(expected, vouchermis.getIutNumber());
		vouchermis.setIutStatus(expected);
		assertEquals(expected, vouchermis.getIutStatus());
		vouchermis.setMonth(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getMonth());
		vouchermis.setNarration(expected);
		assertEquals(expected, vouchermis.getNarration());
		vouchermis.setNatureofwork(expected);
		assertEquals(expected, vouchermis.getNatureofwork());
		vouchermis.setNetamt(expected);
		assertEquals(expected, vouchermis.getNetamt());
		vouchermis.setNetdeduction(expected);
		assertEquals(expected, vouchermis.getNetdeduction());
		vouchermis.setProjectcode(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getProjectcode());
		vouchermis.setProjectfund(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getProjectfund());
		vouchermis.setSchemeid(null);
		assertNull(vouchermis.getSchemeid());
		vouchermis.setSchemename(expected);
		assertEquals(expected, vouchermis.getSchemename());
		vouchermis.setSegmentid(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getSegmentid());
		vouchermis.setSourcePath(expected);
		assertEquals(expected, vouchermis.getSourcePath());
		vouchermis.setSubaccounthead(expected);
		assertEquals(expected, vouchermis.getSubaccounthead());
		vouchermis.setSubschemeid(null);
		assertNull(vouchermis.getSubschemeid());
		vouchermis.setSubSegmentid(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getSubSegmentid());
		vouchermis.setTotexpenditure(expected);
		assertEquals(expected, vouchermis.getTotexpenditure());
		vouchermis.setUpdatedtimestamp(expectedDate);
		assertNotNull(vouchermis.getUpdatedtimestamp());
		vouchermis.setUserdept(expected);
		assertEquals(expected, vouchermis.getUserdept());
		vouchermis.setVoucherheaderid(null);
		assertNull(vouchermis.getVoucherheaderid());
		vouchermis.setWardcode(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getWardcode());
		vouchermis.setZonecode(expectedInt);
		assertEquals(Integer.valueOf(1), vouchermis.getZonecode());

	}

}
