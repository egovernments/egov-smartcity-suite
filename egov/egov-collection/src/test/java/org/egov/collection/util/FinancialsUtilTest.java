/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

/**
 * 
 */
package org.egov.collection.util;

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
	@Test(expected = ApplicationRuntimeException.class)
	public void testCreatePreApprovalVoucherWithEgovException() {
		expect(
				voucherCreator.createPreApprovedVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andThrow(
				new ApplicationRuntimeException("test exception"));
		replay(voucherCreator);

		financialsUtil.createPreApprovalVoucher(new HashMap<String, Object>(),
				new ArrayList<HashMap<String, Object>>(), new ArrayList());
		verify(voucherCreator);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetReversalVoucher() throws ApplicationRuntimeException,
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
	@Test(expected = ApplicationRuntimeException.class)
	public void testCreatePreApprovalVoucherWithParseException()
			throws ApplicationRuntimeException, ParseException {
		expect(voucherCreator.reverseVoucher(isA(List.class))).andThrow(
				new ParseException("test parse exception", 1));
		replay(voucherCreator);
		financialsUtil
				.getReversalVoucher(new ArrayList<HashMap<String, Object>>());
		verify(voucherCreator);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = ApplicationRuntimeException.class)
	public void testCreatePreApprovalVoucherWithRuntimeException()
			throws ApplicationRuntimeException, ParseException {
		expect(voucherCreator.reverseVoucher(isA(List.class))).andThrow(
				new ApplicationRuntimeException("test runtime exception"));
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
		catch(ApplicationRuntimeException egovEx){
			assertEquals(egovEx.getMessage(),"Valid Appconfig value for ISVOUCHERAPPROVED is not defined");
		}

	}*/
}
