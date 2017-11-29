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
package org.egov.collection.integration.services;


/**
 * JUnit Test class for testing the collections integration service
 * implementation.
 */
public class CollectionIntegrationServiceImplTest { /*extends
		AbstractPersistenceServiceTest<ReceiptHeader, Long> {
	*//**
	 * The collection integration service
	 *//*
	private CollectionIntegrationServiceImpl collectionIntegrationService;

	*//**
	 * The collection object factory
	 *//*
	private CollectionObjectFactory objectFactory;

	*//**
	 * The receipt headers created for testing
	 *//*
	private ArrayList<ReceiptHeader> receiptHeaders = new ArrayList<ReceiptHeader>();

	*//**
	 * The function code used
	 *//*
	private static final String functionName = "COLLECTION-INTEGRATION-FUNC";

	*//**
	 * The chart of accounts
	 *//*
	private static final String glCode1 = "99", glCode2 = "98", glCode3 = "97";

	*//**
	 * Instrument amount
	 *//*
	private final Double instrumentAmount = Double.valueOf("1000");

	*//**
	 * Instrument date
	 *//*
	private final Date instrumentDate = new Date();
	
	private CommonsManager commonsManager;
	private CollectionCommon collectionCommon;
	private EgovCommon egovCommon;
	private CollectionsUtil collectionsUtil;
	private FinancialsUtil finUtil; 
	private BoundaryDAO boundaryDAO;
	private ReceiptService receiptService;
	private ReceiptHeaderService receiptHeaderService;
	private InstrumentService instrumentService;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private EisCommonsManager eisCommonsManagerMock;
	//WorkflowService<ReceiptHeader> receiptWorkflowServiceMock;
	private UserManager userManager;
	private User user;

	*//**
	 * Test Application context
	 *//*
	private static ClassPathXmlApplicationContext context;

	@Before
	public void setupService() {
		
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					"erpCollectionsAppContextTest.xml");
		}
		FinancialsUtil financialsUtil = (FinancialsUtil) context
				.getBean("financialsUtil");

		// Create the collection integration service
		collectionIntegrationService = new CollectionIntegrationServiceImpl();

		objectFactory = new CollectionObjectFactory(session,genericService);
		user=objectFactory.createUser("testUser");
		ApplicationThreadLocals.setUserId(user.getId().toString());
		userManager = createMock(UserManager.class);

		// Receipt with cash as instrument
		receiptHeaders
				.add(objectFactory
						.createReceiptHeaderWithInstrument(
								"RCPT-1111",
								CollectionConstants.RECEIPT_TYPE_BILL,
								CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
								"BILL-1111",
								financialsUtil
										.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH),
								null, instrumentAmount, instrumentDate,
								"testCode", glCode1, functionName,
								"system"));

		// Receipt with cheque as instrument
		receiptHeaders
				.add(objectFactory
						.createReceiptHeaderWithInstrument(
								"RCPT-2222",
								CollectionConstants.RECEIPT_TYPE_BILL,
								CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
								"BILL-2222",
								financialsUtil
										.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE),
								"222222", instrumentAmount, instrumentDate,
								"testCode", glCode2, functionName,
								"system"));

		// Receipt with DD as instrument
		receiptHeaders
				.add(objectFactory
						.createReceiptHeaderWithInstrument(
								"RCPT-3333",
								CollectionConstants.RECEIPT_TYPE_BILL,
								CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
								"BILL-3333",
								financialsUtil
										.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD),
								"333333", instrumentAmount, instrumentDate,
								"testCode", glCode3, functionName,
								"system"));
		
		commonsManager = createMock(CommonsManager.class);
		boundaryDAO = createMock(BoundaryDAO.class);
		instrumentService = createMock(InstrumentService.class);
		egovCommon = createMock(EgovCommon.class);
		eisCommonsManagerMock = createMock(EisCommonsManager.class);
		//receiptWorkflowServiceMock = createMock(WorkflowService.class);
		
		finUtil = new FinancialsUtil(){
			
			public InstrumentType getInstrumentTypeByType(String type){
				return (InstrumentType) genericService.find("from InstrumentType  where type=? and isActive=true",type);
			}
		};
		finUtil.setInstrumentService(instrumentService);
		
		collectionsUtil=new CollectionsUtil();
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setEisCommonsManager(eisCommonsManagerMock);
		collectionsUtil.setUserManager(userManager);
		
		receiptService = new ReceiptService(){
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return true;
			}
		};
		receiptService.setType(ReceiptPayeeDetails.class);
		receiptService.setFinancialsUtil(finUtil);
		
		collectionCommon = new CollectionCommon();
		collectionCommon.setBoundaryDAO(boundaryDAO);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		collectionCommon.setCommonsManager(commonsManager);
		collectionCommon.setEgovCommon(egovCommon);
		collectionCommon.setPersistenceService(genericService);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		collectionCommon.setFinancialsUtil(finUtil);
		
		receiptHeaderService = new ReceiptHeaderService(){
			public CVoucherHeader createVoucherForReceipt(ReceiptHeader receiptHeader,Boolean receiptBulkUpload){
				CVoucherHeader voucherHeader=objectFactory.createVoucher("testReceiptVoucher");
				
				ReceiptVoucher receiptVoucher = new ReceiptVoucher();
				receiptVoucher.setVoucherheader(voucherHeader);
				receiptVoucher.setReceiptHeader(receiptHeader);
				receiptHeader.addReceiptVoucher(receiptVoucher);
				receiptVoucher.setReceiptHeader(receiptHeader);
				
				receiptHeader.addReceiptVoucher(receiptVoucher);
				
				return voucherHeader;
			}
			public void startWorkflow(Collection<ReceiptHeader> receiptHeaders,Boolean receiptBulkUpload){
				
			}
		};
		receiptHeaderService.setType(ReceiptHeader.class);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setFinancialsUtil(finUtil);
		//receiptHeaderService.setReceiptWorkflowService(receiptWorkflowServiceMock);
		
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);

		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);
		
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		receiptHeaderService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		collectionIntegrationService.setCommonsManager(commonsManager);
		collectionIntegrationService.setCollectionsUtil(collectionsUtil);
		collectionIntegrationService.setCollectionCommon(collectionCommon);
		collectionIntegrationService.setPersistenceService(genericService);
		collectionIntegrationService.setReceiptPayeeDetailsService(receiptService);
		collectionIntegrationService.setReceiptHeaderService(receiptHeaderService);
	}

	*//**
	 * Compares given receipt info with given receipt header
	 * 
	 * @param receiptInfo
	 *            Receipt info to be compared
	 * @param receiptHeader
	 *            Receipt header with which the receipt info is to be compared
	 *//*
	private void compareReceiptInfoWithReceiptHeader(
			BillReceiptInfo receiptInfo, ReceiptHeader receiptHeader) {
		assertNotNull(receiptInfo);
		assertNotNull(receiptInfo.getReceiptStatus());
		assertNotNull(receiptInfo.getBillReferenceNum());

		Set<ReceiptAccountInfo> receiptAccounts = receiptInfo
				.getAccountDetails();
		assertNotNull(receiptAccounts);

		Set<ReceiptInstrumentInfo> receiptInstruments = receiptInfo
				.getInstrumentDetails();
		assertNotNull(receiptInstruments);

		assertEquals(receiptHeader.getReferencenumber(), receiptInfo
				.getBillReferenceNum());

		assertEquals(receiptHeader.getStatus().getId(), receiptInfo
				.getReceiptStatus().getId());

		assertEquals(receiptHeader.getReceiptnumber(), receiptInfo
				.getReceiptNum());

		assertEquals(receiptHeader.getReceiptDate(), receiptInfo
				.getReceiptDate());

		assertEquals(receiptHeader.getLocation(), receiptInfo
				.getReceiptLocation());

		assertEquals(receiptHeader.getReceiptPayeeDetails().getPayeename(),
				receiptInfo.getPayeeName());

		assertEquals(receiptHeader.getReceiptPayeeDetails().getPayeeAddress(),
				receiptInfo.getPayeeAddress());

		for (ReceiptAccountInfo accountInfo : receiptAccounts) {
			BigDecimal crAmt = accountInfo.getCrAmount();
			assertNotNull(crAmt);

			BigDecimal drAmt = accountInfo.getDrAmount();
			assertNotNull(drAmt);

			assertNotNull(accountInfo.getFunction());

			String glCode = accountInfo.getGlCode();
			assertNotNull(glCode);
		}

		for (ReceiptInstrumentInfo instrumentInfo : receiptInstruments) {
			BigDecimal instrumentAmountActual = instrumentInfo
					.getInstrumentAmount();
			assertNotNull(instrumentAmountActual);
			assertEquals(instrumentAmount, instrumentAmountActual);

			assertNotNull(instrumentInfo.getInstrumentDate());
			assertNotNull(instrumentInfo.getInstrumentStatus());

			// Commenting asserts as build is failing. Need to find out
			// why these asserts are failing on continuum
			assertNotNull(instrumentInfo.getInstrumentType());

			if (instrumentInfo.getInstrumentType().equals(
					CollectionConstants.INSTRUMENTTYPE_CASH) == false) {
				assertNotNull(instrumentInfo.getInstrumentNumber());
			}
		}
	}

	@Test
	public void testGetReceiptInfoSingle() {
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		
		BillReceiptInfo receiptInfo=
			collectionIntegrationService.getReceiptInfo("testDummyServiceCode","testDummyReferenceNumber");
		assertNull(receiptInfo);

		ReceiptHeader receiptHeader = receiptHeaders.get(0);
		receiptInfo = collectionIntegrationService
				.getReceiptInfo(receiptHeader.getService().getCode(),receiptHeader.getReceiptnumber());
		compareReceiptInfoWithReceiptHeader(receiptInfo, receiptHeader);
	}
	
	@Test
	public void testGetReceiptInfoSingleReceipt() {
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		
		BillReceiptInfo receiptInfo=
			collectionIntegrationService.getReceiptInfo("testDummyServiceCode","testDummyReferenceNumber");
		assertNull(receiptInfo);

		ReceiptHeader receiptHeader = receiptHeaders.get(0);
		receiptInfo = collectionIntegrationService
				.getReceiptInfo(receiptHeader.getService().getCode(),receiptHeader.getReceiptnumber());
		compareReceiptInfoWithReceiptHeader(receiptInfo, receiptHeader);
	}

	@Test
	public void testGetReceiptInfoMultiple() {
		// Get all receipt headers present in the system
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		String serviceCode = receiptHeaders.get(0).getService().getCode();

		// Prepare set of receipt numbers
		HashSet<String> receiptNums = new HashSet<String>();
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			receiptNums.add(receiptHeader.getReceiptnumber());
		}

		// Fetch receipt info objects using method to be tested
		Map<String, BillReceiptInfo> receipts = collectionIntegrationService
				.getReceiptInfo(serviceCode,receiptNums);
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			// Compare every bill receipt info returned with its corresponding
			// receipt header object
			String receiptNum = receiptHeader.getReceiptnumber();
			BillReceiptInfo receiptInfo = receipts.get(receiptNum);

			compareReceiptInfoWithReceiptHeader(receiptInfo, receiptHeader);
		}
	}

	@Test
	public void testGetBillReceiptInfoSingle() {
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		List<BillReceiptInfo> receipts=
			collectionIntegrationService.getBillReceiptInfo("testDummyServiceCode","testDummyReferenceNumber");
		assertNull(receipts);
		ReceiptHeader receiptHeader = receiptHeaders.get(0);
		receipts = collectionIntegrationService
				.getBillReceiptInfo(receiptHeader.getService().getCode(),receiptHeader.getReferencenumber());
		assertNotNull(receipts);

		compareReceiptInfoWithReceiptHeader(receipts.get(0), receiptHeader);
	}

	@Test
	public void testGetBillReceiptInfoMultiple() {
		// Get all receipt headers present in the system
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		String serviceCode = receiptHeaders.get(0).getService().getCode();
		// Prepare set of receipt numbers
		HashSet<String> refNums = new HashSet<String>();
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			refNums.add(receiptHeader.getReferencenumber());
		}

		// Fetch receipt info objects using method to be tested
		Map<String, List<BillReceiptInfo>> receipts = collectionIntegrationService
				.getBillReceiptInfo(serviceCode,refNums);
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			// Compare every bill receipt info returned with its corresponding
			// receipt header object
			String refNum = receiptHeader.getReferencenumber();
			List<BillReceiptInfo> receiptInfos = receipts.get(refNum);
			assertNotNull(receiptInfos);

			compareReceiptInfoWithReceiptHeader(receiptInfos.get(0),
					receiptHeader);
		}
	}

	@Test
	public void testGetInstrumentReceiptInfoSingle() {
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		
		List<BillReceiptInfo> receiptInfo=
			collectionIntegrationService.getInstrumentReceiptInfo("testDummyServiceCode","testDummyReferenceNumber");
		assertNull(receiptInfo);
		
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			for (InstrumentHeader instrumentHeader : receiptHeader
					.getReceiptInstrument()) {
				String instrumentNum = instrumentHeader.getInstrumentNumber();
				if (instrumentNum != null && instrumentNum.trim().length() > 0) {
					receiptInfo = collectionIntegrationService
							.getInstrumentReceiptInfo(receiptHeader.getService().getCode(),instrumentNum);

					assertNotNull(receiptInfo);
					// TODO: Check that the original receipt header is present
					// in the list
					return;
				}
			}
		}
	}

	@Test
	public void testGetInstrumentReceiptInfoMultiple() {
		// Get all receipt headers present in the system
		if (receiptHeaders == null || receiptHeaders.size() == 0) {
			return;
		}
		String serviceCode = receiptHeaders.get(0).getService().getCode();
		// Prepare set of receipt numbers
		HashSet<String> instrumentNums = new HashSet<String>();
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			for (InstrumentHeader instrumentHeader : receiptHeader
					.getReceiptInstrument()) {
				String instrumentNum = instrumentHeader.getInstrumentNumber();
				if (instrumentNum != null && instrumentNum.trim().length() > 0) {
					instrumentNums.add(instrumentNum);
				}
			}
		}

		if (instrumentNums == null || instrumentNums.size() == 0) {
			return;
		}

		// Fetch receipt info objects using method to be tested
		Map<String, List<BillReceiptInfo>> receipts = collectionIntegrationService
				.getInstrumentReceiptInfo(serviceCode,instrumentNums);
		assertNotNull(receipts);
		// TODO: check that receipt info objects for ALL instrument numbers have
		// been returned
	}
	
	private BillInfoImpl createBillInfoImplObject() throws ParseException{
		
		CFunction function1 = objectFactory.createFunction("testFunctionName1","testFunctionCode1");
		CChartOfAccounts account1=objectFactory.createCOA("testGLCode1");
		
		BillAccountDetails billAccount = new BillAccountDetails(
				account1.getGlcode(),1,new BigDecimal("567.9"),
				new BigDecimal(0),function1.getCode(),"GL CODE DESCRIPTION1",1);
		
		BoundaryImpl boundary = objectFactory.createBoundary();
		Fund fund = objectFactory.createFund("testFundCode");
		Fundsource fundSource = objectFactory.createFundsource("testfundSourceName", "testfundSourceCode");
		DepartmentImpl dept = objectFactory.createDeptForCode("testDeptCode");
		ServiceDetails service = objectFactory.createServiceDetails();
		
		BillDetails billDetail = new BillDetails("refno1",null,null,boundary.getBoundaryNum().toString(),
				boundary.getBoundaryType().getName(),"", new BigDecimal("567.9"),null);
		BillPayeeDetails payee = new BillPayeeDetails("testPayeeName","testPayeeAddress");
		BillInfoImpl billInfo = new BillInfoImpl(
				service.getCode(),fund.getCode(),new BigDecimal("001"),fundSource.getCode(),
				dept.getDeptCode(),"Hello","testPaidBy",Boolean.TRUE,Boolean.TRUE,null,
				BillInfo.COLLECTIONTYPE.C);
		
		billDetail.addBillAccountDetails(billAccount);
		payee.addBillDetails(billDetail);
		billInfo.addPayees(payee);
		
		commonsManager.fundByCode(fund.getCode());
		expectLastCall().andReturn(fund);
		
		commonsManager.getFundSourceByCode(fundSource.getCode());
		expectLastCall().andReturn(fundSource);
		
		commonsManager.getFunctionByCode(function1.getCode());
		expectLastCall().andReturn(function1);
		
		commonsManager.getCChartOfAccountsByGlCode(account1.getGlcode());
		expectLastCall().andReturn(account1);
		
		boundaryDAO.getBoundary(boundary.getBoundaryNum().intValue(),
				boundary.getBoundaryType().getName(),
				CollectionConstants.BOUNDARY_HIER_CODE_ADMIN);
		expectLastCall().andReturn(boundary);
		replay(boundaryDAO);
		
		return billInfo;
	}
	
	@Test
	public void testCreateReceiptWithoutDept(){
		Fund fund = objectFactory.createFund("testFundCode");
		
		BillInfoImpl billColl = new BillInfoImpl(
				"testServiceCode",fund.getCode(),new BigDecimal("001"),"testFundSourceCode",
				"testDeptCode","Hello","testPaidBy",Boolean.TRUE,Boolean.TRUE,null,
				BillInfo.COLLECTIONTYPE.C);
		
		commonsManager.fundByCode(fund.getCode());
		expectLastCall().andReturn(fund);
		replay(commonsManager);
		
		List<PaymentInfo> paytInfoList = new ArrayList<PaymentInfo>();
		paytInfoList.add(new PaymentInfoCash());
		
		try{
		collectionIntegrationService.createReceipt(billColl, paytInfoList);
		}
		catch(ApplicationRuntimeException ex){
			assertEquals("Department not present for the department code [testDeptCode].",ex.getMessage());
		}
	}
	
	@Test
	public void testCreateReceiptWithoutFund(){
		BillInfoImpl billColl = new BillInfoImpl(
				"testService","testFundCode",new BigDecimal("001"),"testfundSourceCode",
				"deptCode","Hello","testPaidBy",Boolean.TRUE,Boolean.TRUE,null,
				BillInfo.COLLECTIONTYPE.C);
		
		List<PaymentInfo> paytInfoList = new ArrayList<PaymentInfo>();
		paytInfoList.add(new PaymentInfoCash());
		
		try{
		collectionIntegrationService.createReceipt(billColl, paytInfoList);
		}
		catch(ApplicationRuntimeException ex){
			assertEquals("Fund not present for the fund code [testFundCode].",ex.getMessage());
		}
		
	}
	
	@Test
	public void testCreateReceiptWithCash() throws ParseException{
		BillInfoImpl billInfo = createBillInfoImplObject();
		
		List<PaymentInfo> paytInfoList = new ArrayList<PaymentInfo>();
		PaymentInfoCash paytInfoCash = new PaymentInfoCash(
				new BigDecimal(1000));
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		InstrumentHeader instrHeaderCash = new InstrumentHeader();
		instrHeaderCash.setInstrumentType(finUtil.getInstrumentTypeByType(
				CollectionConstants.INSTRUMENTTYPE_CASH));
		instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCash.setInstrumentAmount(new BigDecimal(1000));
		instrHeaderCash.setStatusId(instrumentStatus);
		session.saveOrUpdate(instrHeaderCash);
		actualInstrList.add(instrHeaderCash);
		
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCash);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		EasyMock.expect(instrumentService.addToInstrument(EasyMock.isA(List.class))).andReturn(actualInstrList);
		EasyMock.expect(instrumentService.updateInstrumentVoucherReference(
				EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		replay(commonsManager);

		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		replay(userManager);

		
		paytInfoList.add(paytInfoCash);
		
		BillReceiptInfoImpl billReceiptInfo = (BillReceiptInfoImpl)collectionIntegrationService.createReceipt(billInfo, paytInfoList);
		ReceiptInstrumentInfo billReceiptInstrInfo = billReceiptInfo.getInstrumentDetails().iterator().next();
		
		
		BillPayeeDetails expctdBillPayee = billInfo.getPayees().get(0);
		BillDetails expctdBillDetail = expctdBillPayee.getBillDetails().get(0);
		BillAccountDetails expctdBillAccDetail = expctdBillDetail.getAccounts().get(0);
		
		assertEquals(expctdBillDetail.getRefNo(), billReceiptInfo.getBillReferenceNum());
		
		assertEquals(expctdBillDetail.getBoundaryNum(), billReceiptInfo.getReceiptMisc().getBoundary().getBoundaryNum().toString());
		assertEquals(BillingIntegrationService.EVENT_RECEIPT_CREATED, billReceiptInfo.getEvent());
		assertEquals(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED,billReceiptInfo.getReceiptStatus().getCode());
		assertEquals(CollectionConstants.COLLECTION_TYPE_FIELDCOLLECTION,billReceiptInfo.getCollectionType().charAt(0));
		assertEquals(expctdBillPayee.getPayeeName(), billReceiptInfo.getPayeeName());
		assertEquals(expctdBillPayee.getPayeeAddress(),billReceiptInfo.getPayeeAddress());
		assertEquals(paytInfoCash.getInstrumentAmount(), billReceiptInstrInfo.getInstrumentAmount());
		assertEquals(paytInfoCash.getInstrumentType().toString(), billReceiptInstrInfo.getInstrumentType());
		assertEquals(billInfo.getPaidBy(), billReceiptInfo.getPaidBy());
		assertEquals(billInfo.getCollectionType(), BillInfo.COLLECTIONTYPE.C);
	}
	
	//@Test
	public void testCreateReceiptWithCard() throws ParseException{
		BillInfoImpl billInfo = createBillInfoImplObject();
		
		List<PaymentInfo> paytInfoList = new ArrayList<PaymentInfo>();
		PaymentInfoCard paytInfoCard = new PaymentInfoCard();
		paytInfoCard = new PaymentInfoCard("12345",
				new BigDecimal(1000),"67890","12","2010","234",paytInfoCard.cardTypeValue.M);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		InstrumentHeader instrHeaderCard = new InstrumentHeader();
		instrHeaderCard.setInstrumentType(finUtil.getInstrumentTypeByType(
				CollectionConstants.INSTRUMENTTYPE_CARD));
		instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCard.setInstrumentAmount(new BigDecimal(1000));
		instrHeaderCard.setStatusId(instrumentStatus);
		session.saveOrUpdate(instrHeaderCard);
		actualInstrList.add(instrHeaderCard);
		
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCard);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		EasyMock.expect(instrumentService.addToInstrument(EasyMock.isA(List.class))).andReturn(actualInstrList);
		EasyMock.expect(instrumentService.updateInstrumentVoucherReference(
				EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		replay(commonsManager);
		
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		replay(userManager);
		
		paytInfoList.add(paytInfoCard);
		
		BillReceiptInfoImpl billReceiptInfo = (BillReceiptInfoImpl)collectionIntegrationService.createReceipt(billInfo, paytInfoList);
		ReceiptInstrumentInfo billReceiptInstrInfo = billReceiptInfo.getInstrumentDetails().iterator().next();
		
		
		BillPayeeDetails expctdBillPayee = billInfo.getPayees().get(0);
		BillDetails expctdBillDetail = expctdBillPayee.getBillDetails().get(0);
		BillAccountDetails expctdBillAccDetail = expctdBillDetail.getAccounts().get(0);
		
		assertEquals(expctdBillDetail.getRefNo(), billReceiptInfo.getBillReferenceNum());
		
		assertEquals(expctdBillDetail.getBoundaryNum(), billReceiptInfo.getReceiptMisc().getBoundary().getBoundaryNum().toString());
		assertEquals(BillingIntegrationService.EVENT_RECEIPT_CREATED, billReceiptInfo.getEvent());
		assertEquals(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED,billReceiptInfo.getReceiptStatus().getCode());
		assertEquals(CollectionConstants.COLLECTION_TYPE_FIELDCOLLECTION,billReceiptInfo.getCollectionType().charAt(0));
		assertEquals(expctdBillPayee.getPayeeName(), billReceiptInfo.getPayeeName());
		assertEquals(expctdBillPayee.getPayeeAddress(),billReceiptInfo.getPayeeAddress());
		assertEquals(paytInfoCard.getInstrumentAmount(), billReceiptInstrInfo.getInstrumentAmount());
		//assertEquals(paytInfoCard.getInstrumentNumber(), billReceiptInstrInfo.getInstrumentNumber());
		//assertEquals(paytInfoCard.getTransactionNumber(), billReceiptInstrInfo.getTransactionNumber());
		assertEquals(paytInfoCard.getInstrumentType().toString(), billReceiptInstrInfo.getInstrumentType());
		assertEquals(billInfo.getPaidBy(), billReceiptInfo.getPaidBy());
	}
	
	@Test
	public void testCreateReceiptWithChequeDD() throws ParseException{
		BillInfoImpl billInfo = createBillInfoImplObject();
		
		Bank bank = objectFactory.createBank();
		commonsManager.getBankById(Integer.valueOf(bank.getId()));
		expectLastCall().andReturn(bank).anyTimes();
		
		List<PaymentInfo> paytInfoList = new ArrayList<PaymentInfo>();
		PaymentInfoChequeDD paytInfoChequeDD1 = new PaymentInfoChequeDD(
				bank.getId().longValue(),"testBranchName",new Date(),
				"123456",TYPE.cheque,new BigDecimal(500));
		PaymentInfoChequeDD paytInfoChequeDD2 = new PaymentInfoChequeDD(
				bank.getId().longValue(),"testBranchName",new Date(),
				"789012",TYPE.dd,new BigDecimal(500));
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		Date date=new Date();
		InstrumentHeader chqInstrumentHeader=objectFactory.createInstrumentHeaderWithBankDetails(
				finUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE), 
				"12346",Double.valueOf(500),date,
				instrumentStatus,bank,"testBranchName",CollectionConstants.ZERO_INT);
		InstrumentHeader ddInstrumentHeader=objectFactory.createInstrumentHeaderWithBankDetails(
				finUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD), 
				"789012",Double.valueOf(500),date,
				instrumentStatus,bank,"testBranchName",CollectionConstants.ZERO_INT);
		
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		actualInstrList.add(chqInstrumentHeader);
		actualInstrList.add(ddInstrumentHeader);
		
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(chqInstrumentHeader);
		instrList.add(ddInstrumentHeader);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		
		EasyMock.expect(instrumentService.addToInstrument(EasyMock.isA(List.class))).andReturn(actualInstrList);
		EasyMock.expect(instrumentService.updateInstrumentVoucherReference(
				EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		CChartOfAccounts chequeInHand = objectFactory.createCOA("TestcchequeInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CHEQUEINHAND, chequeInHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(chequeInHand.getGlcode());
		expectLastCall().andReturn(chequeInHand);
		replay(commonsManager);
		
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		replay(userManager);
		
		paytInfoList.add(paytInfoChequeDD1);
		paytInfoList.add(paytInfoChequeDD2);
		
		BillReceiptInfoImpl billReceiptInfo = (BillReceiptInfoImpl)collectionIntegrationService.createReceipt(billInfo, paytInfoList);
		ReceiptInstrumentInfo billReceiptInstrInfo1 = billReceiptInfo.getInstrumentDetails().iterator().next();
		ReceiptInstrumentInfo billReceiptInstrInfo2 = billReceiptInfo.getInstrumentDetails().iterator().next();
		
		ReceiptInstrumentInfo actualBillReceiptInstrInfo1=new ReceiptInstrumentInfoImpl(chqInstrumentHeader);
		ReceiptInstrumentInfo actualBillReceiptInstrInfo2=new ReceiptInstrumentInfoImpl(ddInstrumentHeader);
		// assertTrue(billReceiptInfo.getInstrumentDetails().contains(actualBillReceiptInstrInfo1));
		assertEquals(paytInfoChequeDD2.getInstrumentAmount(), billReceiptInstrInfo1.getInstrumentAmount());
		assertEquals(paytInfoChequeDD2.getInstrumentAmount(), billReceiptInstrInfo1.getInstrumentAmount());
		assertEquals(paytInfoChequeDD2.getInstrumentType().toString(), billReceiptInstrInfo1.getInstrumentType());
		assertEquals(paytInfoChequeDD2.getPaidBy(), billReceiptInfo.getPaidBy());
		assertEquals(paytInfoChequeDD1.getInstrumentAmount(), billReceiptInstrInfo2.getInstrumentAmount());
		assertEquals(paytInfoChequeDD1.getInstrumentType().toString(), billReceiptInstrInfo2.getInstrumentType());
		assertEquals(paytInfoChequeDD1.getPaidBy(), billReceiptInfo.getPaidBy());
		
		assertEquals(paytInfoChequeDD1.getInstrumentAmount(), billReceiptInstrInfo2.getInstrumentAmount());
		assertEquals(paytInfoChequeDD1.getInstrumentType().toString(), billReceiptInstrInfo2.getInstrumentType());
		//assertEquals(paytInfoChequeDD2.getPaidBy(), billReceiptInfo.getPaidBy());
	}
	
	@Test
	public void testCreateReceiptWithBank() throws ParseException{
		BillInfoImpl billInfo = createBillInfoImplObject();
		
		List<PaymentInfo> paytInfoList = new ArrayList<PaymentInfo>();
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		
		InstrumentHeader instrHeaderBank = objectFactory.createBankInstrumentHeader();
		
		PaymentInfoBank paytInfoBank = new PaymentInfoBank(new BigDecimal(1000), 
				instrHeaderBank.getBankId().getId().longValue(),
				instrHeaderBank.getBankAccountId().getId().longValue(),
				123456,instrHeaderBank.getTransactionDate());
		
		commonsManager.getBankaccountById(paytInfoBank.getBankAccountId().intValue());
		expectLastCall().andReturn(instrHeaderBank.getBankAccountId());
		
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		actualInstrList.add(instrHeaderBank);
		replay(commonsManager);
		
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		userManager.getUserByID(user.getId());
		expectLastCall().andReturn(user);
		replay(userManager);

		
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderBank);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		
		EasyMock.expect(instrumentService.addToInstrument(EasyMock.isA(List.class))).andReturn(actualInstrList);
		EasyMock.expect(instrumentService.updateInstrumentVoucherReference(
				EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		paytInfoList.add(paytInfoBank);
		
		BillReceiptInfoImpl billReceiptInfo = (BillReceiptInfoImpl)collectionIntegrationService.createReceipt(billInfo, paytInfoList);
		
		ReceiptInstrumentInfo billReceiptInstrInfo = billReceiptInfo.getInstrumentDetails().iterator().next();
		
		BillPayeeDetails expctdBillPayee = billInfo.getPayees().get(0);
		BillDetails expctdBillDetail = expctdBillPayee.getBillDetails().get(0);
		BillAccountDetails expctdBillAccDetail = expctdBillDetail.getAccounts().get(0);
		
		assertEquals(expctdBillDetail.getRefNo(), billReceiptInfo.getBillReferenceNum());
		
		assertEquals(BillingIntegrationService.EVENT_RECEIPT_CREATED, billReceiptInfo.getEvent());
		assertEquals(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED,billReceiptInfo.getReceiptStatus().getCode());
		assertEquals(CollectionConstants.COLLECTION_TYPE_FIELDCOLLECTION,billReceiptInfo.getCollectionType().charAt(0));
		assertEquals(expctdBillPayee.getPayeeName(), billReceiptInfo.getPayeeName());
		assertEquals(expctdBillPayee.getPayeeAddress(),billReceiptInfo.getPayeeAddress());
		assertEquals(paytInfoBank.getInstrumentAmount(), billReceiptInstrInfo.getInstrumentAmount());
		assertEquals(paytInfoBank.getInstrumentType().toString(), billReceiptInstrInfo.getInstrumentType());
		assertEquals(paytInfoBank.getTransactionDate(), billReceiptInstrInfo.getTransactionDate());
		assertEquals(String.valueOf(paytInfoBank.getTransactionNumber()), billReceiptInstrInfo.getTransactionNumber());
		
		Bankaccount expectedAccount= (Bankaccount) genericService.find(
				"from Bankaccount where id=?",paytInfoBank.getBankAccountId().intValue());
		assertEquals(expectedAccount.getBankbranch().getBranchname(), billReceiptInstrInfo.getBankBranchName());
		assertEquals(expectedAccount.getBankbranch().getBank().getName(), billReceiptInstrInfo.getBankName());
		assertEquals(expectedAccount.getAccountnumber(), billReceiptInstrInfo.getBankAccountNumber());
		assertEquals(billInfo.getPaidBy(), billReceiptInfo.getPaidBy());
		
		assertEquals(paytInfoBank.getBankId(),expectedAccount.getBankbranch().getBank().getId());
	}*/
}
