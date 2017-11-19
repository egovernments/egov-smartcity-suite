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
package org.egov.collection.services;


/**
 * JUnit Test cases for Receipt Header Service
 */
public class ReceiptHeaderServiceTest { /* extends
		AbstractPersistenceServiceTest<ReceiptHeader, Long> {
	private ReceiptHeaderService receiptHeaderService;
	//private WorkflowService<ReceiptHeader> receiptWorkflowService;
	private CollectionObjectFactory objectFactory;
	private FinancialsUtil financialsUtil;
	private CollectionsUtil collectionsUtil;
	@Autowired AppConfigValuesDAO appConfigValuesDAO;
	private EisCommonsManager eisCommonsManagerMock;
	private CreateVoucher voucherCreatorMock;
	
	private static final String functionName = "SUBMIT-COLLECTION-FUNC";
	private static final String glCode = "99";
	private final Double instrumentAmount = Double.valueOf("1000");
	private final Date instrumentDate = new Date();
	private static final String instrumentStatusCode = "testCode";
	private static final String userName = "testUser";
	private static final String counterName = "testCounter";
	private ReceiptHeader receiptToBeSubmitted;
	private ReceiptHeader receiptToBeApproved;

	private static ClassPathXmlApplicationContext context;

	*//**
	 * Creates the receipts used for testing
	 *//*
	private void createReceipts() {
		receiptToBeSubmitted = objectFactory
				.createReceiptHeaderWithInstrument(
						"RCPT-1111",
						CollectionConstants.RECEIPT_TYPE_BILL,
						CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
						"BILL-1111",
						financialsUtil
								.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH),
						null, instrumentAmount, instrumentDate,
						instrumentStatusCode, glCode, functionName, userName,
						counterName);

		receiptToBeApproved = objectFactory
				.createReceiptHeaderWithInstrument(
						"RCPT-2222",
						CollectionConstants.RECEIPT_TYPE_BILL,
						CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED,
						"BILL-2222",
						financialsUtil
								.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH),
						null, instrumentAmount, instrumentDate,
						instrumentStatusCode, glCode, functionName, userName,
						counterName);
	}

	@Before
	public void setupService() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					"erpCollectionsAppContextTest.xml");
		}

		objectFactory = new CollectionObjectFactory(session);
		collectionsUtil = (CollectionsUtil) context.getBean("collectionsUtil");
		financialsUtil = (FinancialsUtil) context.getBean("financialsUtil");

		voucherCreatorMock = org.easymock.classextension.EasyMock
				.createMock(CreateVoucher.class);
		financialsUtil.setVoucherCreator(voucherCreatorMock);
		
		CollectionsNumberGenerator numberGenerator = (CollectionsNumberGenerator) context
				.getBean("collectionsNumberGenerator");

		eisCommonsManagerMock = org.easymock.EasyMock
				.createMock(EisCommonsManager.class);
		collectionsUtil.setEisCommonsManager(eisCommonsManagerMock);
		
		genericHibDao = new GenericHibernateDaoFactory(){
			protected Session getCurrentSession(){
				return session;
			}
			public  AppDataDAO getAppDataDAO()
			{
				return new AppDataHibernateDAO(AppData.class,session);
			}
		};
		
		collectionsUtil.setGenericDao(genericHibDao);

		receiptHeaderService = new ReceiptHeaderService();
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setFinancialsUtil(financialsUtil);
		receiptHeaderService.setCollectionsNumberGenerator(numberGenerator);
		receiptWorkflowService = new SimpleWorkflowService<ReceiptHeader>(
				receiptHeaderService, ReceiptHeader.class);
		receiptHeaderService.setReceiptWorkflowService(receiptWorkflowService);

		// Create receipts
		createReceipts();
	}

	@Test
	public void testFindAllByStatusUserCounterService() {
		// Search for the "to be submitted" receipt
		List<ReceiptHeader> receiptHeadersFound = receiptHeaderService
				.findAllByStatusUserCounterService(
						CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
						receiptToBeSubmitted.getCreatedBy().getUserName(),
						receiptToBeSubmitted.getLocation().getId(),
						receiptToBeSubmitted.getService().getCode());
		assertNotNull(receiptHeadersFound);
		assertTrue(receiptHeadersFound.size() > 0);
		assertTrue(receiptHeadersFound.contains(receiptToBeSubmitted));

		// Search for all receipts
		receiptHeadersFound = receiptHeaderService
				.findAllByStatusUserCounterService("ALL", "ALL", -1, "ALL");
		assertNotNull(receiptHeadersFound);
		assertTrue(receiptHeadersFound.size() > 1);
		assertTrue(receiptHeadersFound.contains(receiptToBeSubmitted));
		assertTrue(receiptHeadersFound.contains(receiptToBeApproved));
	}

	@Test
	public void testInternalReferenceNumberForCash() {
		ReceiptHeader receiptHeader = objectFactory
				.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNo");

		receiptHeader
				.addInstrument(objectFactory
						.createUnsavedInstrumentHeader(
								objectFactory
										.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CASH),
								objectFactory
										.createUnsavedEgwStatus(
												"testStatus",
												CollectionConstants.RECEIPT_STATUS_DESC_CREATED)));
		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(
				receiptHeader.getCreatedDate());

		List<String> actualinternalRefNo = receiptHeaderService
				.generateInternalReferenceNo(receiptHeader);
		String actualSeqNo = actualinternalRefNo.get(0).substring(0,
				actualinternalRefNo.get(0).lastIndexOf('/'));

		List numbers = session.createSQLQuery(
				"SELECT VALUE FROM EG_NUMBER_GENERIC WHERE OBJECTTYPE=?")
				.setString(0, "RECEIPTREF").list();
		BigDecimal result;
		if (numbers != null && !numbers.isEmpty())
			result = (BigDecimal) numbers.get(0);
		else
			result = new BigDecimal(0);
		String cashsequence = "0000" + (result.longValue() + 1);
		cashsequence = cashsequence.substring(cashsequence.length() - 4,
				cashsequence.length());
		String expectedResult1 = cashsequence + "/"
				+ financialYear.getFinYearRange().substring(0, 5)
				+ financialYear.getFinYearRange().substring(7);
		assertEquals(2, actualinternalRefNo.size());
	}

	@Test
	public void testInternalReferenceNumberForCheque() {
		ReceiptHeader receiptHeader = objectFactory
				.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNo");

		receiptHeader
				.addInstrument(objectFactory
						.createUnsavedInstrumentHeader(
								objectFactory
										.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CHEQUE),
								objectFactory
										.createUnsavedEgwStatus(
												"testStatus",
												CollectionConstants.RECEIPT_STATUS_DESC_CREATED)));

		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(receiptHeader.getCreatedDate());

		List<String> actualinternalRefNo = receiptHeaderService
				.generateInternalReferenceNo(receiptHeader);
		String actualSeqNo = actualinternalRefNo.get(1).substring(0,
				actualinternalRefNo.get(1).lastIndexOf('/'));

		List numbers = session.createSQLQuery(
				"SELECT VALUE FROM EG_NUMBER_GENERIC WHERE OBJECTTYPE=?")
				.setString(0, "RECEIPTREF").list();
		BigDecimal result;
		if (numbers != null && !numbers.isEmpty())
			result = (BigDecimal) numbers.get(0);
		else
			result = new BigDecimal(0);
		String cashsequence = "000000" + (result.longValue() + 1);
		cashsequence = cashsequence.substring(cashsequence.length() - 6,
				cashsequence.length());
		String expectedResult1 = cashsequence + "/"
				+ financialYear.getFinYearRange().substring(0, 5)
				+ financialYear.getFinYearRange().substring(7);
		assertEquals(2, actualinternalRefNo.size());
		// assertEquals(expectedResult1,actualinternalRefNo.get(1));
	}

	//@Test
	public void testStartWorkflow() {
		Collection<ReceiptHeader> receipts = new ArrayList<ReceiptHeader>();
		receipts.add(receiptToBeSubmitted);
		// receipts.add(receiptToBeApproved);

		EasyMock.expect(
				eisCommonsManagerMock.getCurrentPositionByUser((User) EasyMock
						.isA(UserImpl.class))).andReturn(
				objectFactory.createPosition());

		EasyMock.expect(
				eisCommonsManagerMock.getCurrentPositionByUser((User) EasyMock
						.isA(UserImpl.class))).andReturn(
				objectFactory.createPosition());

		EasyMock.expect(
				eisCommonsManagerMock.getPositionByName("COLLECTIONS_MANAGER"))
				.andReturn(objectFactory.createPosition());

		EasyMock.expect(
				eisCommonsManagerMock.getCurrentPositionByUser((User) EasyMock
						.isA(UserImpl.class))).andReturn(
				objectFactory.createPosition());

		EasyMock.expect(
				eisCommonsManagerMock.getPositionByName("COLLECTIONS_MANAGER"))
				.andReturn(objectFactory.createPosition());

		EasyMock.replay(eisCommonsManagerMock);

		org.easymock.classextension.EasyMock.expect(
				voucherCreatorMock
						.createVoucher(
								org.easymock.classextension.EasyMock
										.isA(HashMap.class),
								org.easymock.classextension.EasyMock
										.isA(List.class),
								org.easymock.classextension.EasyMock
										.isA(List.class))).andReturn(
		objectFactory.createVoucher("testVoucher"));
		org.easymock.classextension.EasyMock.replay(voucherCreatorMock);

		receiptHeaderService.startWorkflow(receipts,Boolean.FALSE);

		EasyMock.verify(eisCommonsManagerMock);
		org.easymock.classextension.EasyMock.verify(voucherCreatorMock);
		assertNotNull(receiptToBeSubmitted.getState());
		assertTrue(receiptToBeSubmitted.getState().getValue().equals(CollectionConstants.WF_STATE_VOUCHER_CREATED));
	}
	
	@Test
	public void testcheckIfMapObjectExist(){
		List<HashMap<String, Object>> paramList=new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> objHashMap=new HashMap<String, Object>();
		
		objHashMap.put(CollectionConstants.BANKREMITTANCE_VOUCHERDATE, "12/12/2009");
		objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, "Test Service 1");
		objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, "TestFund");
		objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, "TestDept");
		paramList.add(objHashMap);
		
		Object[] arrayObjectInitialIndex=new Object[8];
		arrayObjectInitialIndex[0]="1000";
		arrayObjectInitialIndex[1]="12/12/2009";
		arrayObjectInitialIndex[2]="Test Service 1";
		arrayObjectInitialIndex[3]="";
		arrayObjectInitialIndex[4]="";
		arrayObjectInitialIndex[5]="";
		arrayObjectInitialIndex[6]="TestFund";
		arrayObjectInitialIndex[7]="TestDept";
		
		assertEquals(0, receiptHeaderService.checkIfMapObjectExist(paramList, arrayObjectInitialIndex));
		
	}
	
	@Test
	public void testcheckIfMapObjectNotExist(){
		List<HashMap<String, Object>> paramList=new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> objHashMap=new HashMap<String, Object>();
		
		objHashMap.put("VOUCHERDATE", "12/12/2009");
		objHashMap.put("SERVICENAME", "Test Service 1");
		paramList.add(objHashMap);
		
		Object[] arrayObjectInitialIndex=new Object[3];
		arrayObjectInitialIndex[0]="1000";
		arrayObjectInitialIndex[1]="13/12/2009";
		arrayObjectInitialIndex[2]="Test Service 2";
		
		assertEquals(-1, receiptHeaderService.checkIfMapObjectExist(paramList, arrayObjectInitialIndex));
	}
	
	@Test
	public void testReceiptDetailObjectExist(){
		List<ReceiptDetail> newReceiptDetailList=new ArrayList<ReceiptDetail>();
		ReceiptDetail receiptDetailObj= objectFactory.createReceiptDetailWithoutHeader();
		
		newReceiptDetailList.add(receiptDetailObj);
		assertEquals(0,receiptHeaderService.checkIfReceiptDetailObjectExist(newReceiptDetailList, receiptDetailObj));
	}
	
	@Test
	public void testaggregateDuplicateReceiptDetailObject(){
		List<ReceiptDetail> newReceiptDetailListDiffCOA=new ArrayList<ReceiptDetail>();
		List<ReceiptDetail> newReceiptDetailListSameCOA=new ArrayList<ReceiptDetail>();
		
		ReceiptDetail receiptDetailObj1= objectFactory.createReceiptDetailWithoutHeader();
		ReceiptDetail receiptDetailObj2= objectFactory.createReceiptDetailWithoutHeader();
		
		ReceiptDetail receiptDetailObj3= objectFactory.createReceiptDetailWithoutHeader();
		ReceiptDetail receiptDetailObj4= objectFactory.createReceiptDetailWithoutHeader();
		
		newReceiptDetailListDiffCOA.add(receiptDetailObj1);
		newReceiptDetailListDiffCOA.add(receiptDetailObj2);
		
		CChartOfAccounts account1=objectFactory.createCOA("testGLCode1");
		
		receiptDetailObj3.setAccounthead(account1);
		receiptDetailObj4.setAccounthead(account1);
		
		newReceiptDetailListSameCOA.add(receiptDetailObj3);
		newReceiptDetailListSameCOA.add(receiptDetailObj4);
		
		assertEquals(1,receiptHeaderService.aggregateDuplicateReceiptDetailObject(newReceiptDetailListSameCOA).size());
		assertEquals(2,receiptHeaderService.aggregateDuplicateReceiptDetailObject(newReceiptDetailListDiffCOA).size());
	}
	*/
}
