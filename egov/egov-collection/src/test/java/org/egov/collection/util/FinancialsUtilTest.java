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
