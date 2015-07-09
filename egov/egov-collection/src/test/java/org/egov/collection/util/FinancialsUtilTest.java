/**
 * 
 */
package org.egov.collection.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.CVoucherHeader;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.contra.ContraService;
import org.egov.services.instrument.InstrumentService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for financials utilities
 */
public class FinancialsUtilTest {
	/*private FinancialsUtil financialsUtil;
	ContraService contraService;
	CreateVoucher voucherCreator;
	InstrumentService instrumentService;

	@Before
	public void prepare() {
		financialsUtil = new FinancialsUtil();
		contraService = createMock(ContraService.class);
		voucherCreator = createMock(CreateVoucher.class);
		instrumentService = createMock(InstrumentService.class);

		financialsUtil.setVoucherCreator(voucherCreator);
		financialsUtil.setContraService(contraService);
		financialsUtil.setInstrumentService(instrumentService);
	}

	@Test
	public void testSetGetContraService() {
		financialsUtil.setContraService(contraService);
		assertEquals(contraService, financialsUtil.getContraService());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreatePreApprovalVoucher() {
		CVoucherHeader voucherHeaderExpected = createMock(CVoucherHeader.class);

		expect(
				voucherCreator.createPreApprovedVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andReturn(
				voucherHeaderExpected);
		replay(voucherCreator);

		CVoucherHeader voucherHeaderActual = financialsUtil
				.createPreApprovalVoucher(new HashMap<String, Object>(),
						new ArrayList<HashMap<String, Object>>(),
						new ArrayList());

		verify(voucherCreator);
		assertEquals(voucherHeaderExpected, voucherHeaderActual);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = ValidationException.class)
	public void testCreatePreApprovalVoucherWithValException() {
		List<ValidationError> validationErrors = (List<ValidationError>) new ArrayList<ValidationError>();

		expect(
				voucherCreator.createPreApprovedVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andThrow(
				new ValidationException(validationErrors));
		replay(voucherCreator);

		financialsUtil.createPreApprovalVoucher(new HashMap<String, Object>(),
				new ArrayList<HashMap<String, Object>>(), new ArrayList());
		verify(voucherCreator);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = EGOVRuntimeException.class)
	public void testCreatePreApprovalVoucherWithEgovException() {
		expect(
				voucherCreator.createPreApprovedVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andThrow(
				new EGOVRuntimeException("test exception"));
		replay(voucherCreator);

		financialsUtil.createPreApprovalVoucher(new HashMap<String, Object>(),
				new ArrayList<HashMap<String, Object>>(), new ArrayList());
		verify(voucherCreator);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetReversalVoucher() throws EGOVRuntimeException,
			ParseException {
		CVoucherHeader voucherHeaderExpected = createMock(CVoucherHeader.class);

		expect(voucherCreator.reverseVoucher(isA(List.class))).andReturn(
				voucherHeaderExpected);
		replay(voucherCreator);

		CVoucherHeader voucherHeaderActual = financialsUtil
				.getReversalVoucher(new ArrayList<HashMap<String, Object>>());

		verify(voucherCreator);
		assertEquals(voucherHeaderExpected, voucherHeaderActual);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = EGOVRuntimeException.class)
	public void testCreatePreApprovalVoucherWithParseException()
			throws EGOVRuntimeException, ParseException {
		expect(voucherCreator.reverseVoucher(isA(List.class))).andThrow(
				new ParseException("test parse exception", 1));
		replay(voucherCreator);
		financialsUtil
				.getReversalVoucher(new ArrayList<HashMap<String, Object>>());
		verify(voucherCreator);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = EGOVRuntimeException.class)
	public void testCreatePreApprovalVoucherWithRuntimeException()
			throws EGOVRuntimeException, ParseException {
		expect(voucherCreator.reverseVoucher(isA(List.class))).andThrow(
				new EGOVRuntimeException("test runtime exception"));
		replay(voucherCreator);
		financialsUtil
				.getReversalVoucher(new ArrayList<HashMap<String, Object>>());
		verify(voucherCreator);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateInstrument() {
		List<InstrumentHeader> instrumentsExpected = new ArrayList<InstrumentHeader>();
		expect(instrumentService.addToInstrument(isA(List.class))).andReturn(
				instrumentsExpected);
		replay(instrumentService);
		List<InstrumentHeader> instrumentsActual =null;financialsUtil
				.createInstrument(new ArrayList<Map<String, Object>>());
		verify(instrumentService);
		assertEquals(instrumentsExpected, instrumentsActual);
	}

	@Test
	public void testUpdateCheque_DD_Card_Deposit() {
		contraService.updateCheque_DD_Card_Deposit(isA(Long.class), isA(String.class), isA(InstrumentHeader.class));
		replay(contraService);
		financialsUtil.updateCheque_DD_Card_Deposit(1L, "testGlCode", createMock(InstrumentHeader.class));
		verify(contraService);
	}
	
	@Test
	public void testUpdateCashDeposit() {
		contraService.updateCashDeposit(isA(Long.class), isA(String.class), isA(InstrumentHeader.class));
		replay(contraService);
		financialsUtil.updateCashDeposit(1L, "testGlCode", createMock(InstrumentHeader.class));
		verify(contraService);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreatePreApprovalVoucherBasedOnConfig() {
		
		CVoucherHeader voucherHeaderExpected = createMock(CVoucherHeader.class);

		expect(
				voucherCreator.createPreApprovedVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andReturn(
				voucherHeaderExpected);
		replay(voucherCreator);

		CVoucherHeader voucherHeaderActual = financialsUtil
				.createVoucher(new HashMap<String, Object>(),
						new ArrayList<HashMap<String, Object>>(),
						new ArrayList(),Boolean.FALSE, Boolean.FALSE);

		verify(voucherCreator);
		assertEquals(voucherHeaderExpected, voucherHeaderActual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateApprovalVoucherBasedOnConfig() {
		CVoucherHeader voucherHeaderExpected = createMock(CVoucherHeader.class);

		expect(
				voucherCreator.createVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andReturn(
				voucherHeaderExpected);
		replay(voucherCreator);

		CVoucherHeader voucherHeaderActual = financialsUtil
				.createVoucher(new HashMap<String, Object>(),
						new ArrayList<HashMap<String, Object>>(),
						new ArrayList(),Boolean.FALSE, Boolean.TRUE);

		verify(voucherCreator);
		assertEquals(voucherHeaderExpected, voucherHeaderActual);
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void testCreateVoucherBasedOnInvalidConfig() {
		
		expect(
				voucherCreator.createPreApprovedVoucher(isA(HashMap.class), isA(List.class), isA(List.class))).andReturn(new CVoucherHeader());
		replay(voucherCreator);

		try{
			financialsUtil.createVoucher(new HashMap<String, Object>(), new ArrayList<HashMap<String, Object>>(), new ArrayList(), Boolean.FALSE,
					Boolean.FALSE);
		}
		catch(EGOVRuntimeException egovEx){
			assertEquals(egovEx.getMessage(),"Valid Appconfig value for ISVOUCHERAPPROVED is not defined");
		}

	}*/
}
