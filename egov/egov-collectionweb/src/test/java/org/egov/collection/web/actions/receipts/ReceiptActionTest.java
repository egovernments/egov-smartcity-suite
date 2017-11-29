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
package org.egov.collection.web.actions.receipts;




public class ReceiptActionTest {/*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	private ReceiptAction action;
	private BillCollectXmlHandler xmlHandler;
	private UserManager userManager;
	private CollectionObjectFactory objectFactory;
	private ReceiptService receiptService;
	private ReceiptHeaderService receiptHeaderService;
	
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private CollectionsUtil collectionsUtil;
	private CommonsManager commonsManager;
	private FinancialsUtil financialsUtil;
	private InstrumentService instrumentService;
	private EisManager eisManager;
	private CollectionCommon collectionCommon;
	private BoundaryDAO boundaryDAO;
	private EgovCommon egovCommon;
	private EisCommonsManager eisCommonsManagerMock;
	//WorkflowService<ReceiptHeader> receiptWorkflowServiceMock;
	@Autowired AppConfigValuesDAO appConfigValuesDAO;
	
	private ApplicationContextBeanProvider beanProvider;
	private AuditEventService auditEventService; 
		
	private String inputXML = "<bill-collect>"+"\n"+
	  "  <serviceCode>testServCode</serviceCode>"+"\n"+
	  "  <fundCode>testFundCode</fundCode>"+"\n"+
	  "  <functionaryCode>1</functionaryCode>"+"\n"+
	  "  <fundSourceCode>testfundSourceCode</fundSourceCode>"+"\n"+
	  "  <departmentCode>testDeptCode</departmentCode>"+"\n"+
	  "  <displayMessage>HELLO USER</displayMessage>"+"\n"+
	  "  <partPaymentAllowed>1</partPaymentAllowed>"+"\n"+
	  "  <overrideAccountHeadsAllowed>1</overrideAccountHeadsAllowed>"+"\n"+
	  "  <callbackForApportioning>1</callbackForApportioning>" + "\n" +
	  "  <collectionModeNotAllowed>cheque</collectionModeNotAllowed>"+"\n"+
	  "  <collectionModeNotAllowed>dd</collectionModeNotAllowed>"+"\n"+
	  "  <payees>"+"\n"+
	  "    <payee>"+"\n"+
	  "      <payeeName>Mrs. ABC</payeeName>"+"\n"+
	  "      <payeeAddress>221/16 LMN Street, Bangalore</payeeAddress>"+"\n"+
	  "      <bills>"+"\n"+
	  "        <bill refNo=\"testReferenceNo\" billDate=\"21/09/2009\" consumerCode=\"10-10-111-20\">"+"\n"+
	  "          <boundaryNum>1</boundaryNum>"+"\n"+
	  "          <boundaryType>testZone</boundaryType>"+"\n"+
	  "          <description>Property: 221/16 LMN Street, Bangalore for period 2008-09</description>"+"\n"+
	  "          <totalAmount>1000.0</totalAmount>"+"\n"+
	  "          <minimumAmount>300.0</minimumAmount>"+"\n"+
	  "          <accounts>"+"\n"+
	  "            <account glCode=\"testGLCode1\" order=\"1\" isActualDemand=\"1\">"+"\n"+
	  "              <crAmount>567.9</crAmount>"+"\n"+
	  "              <drAmount>0.0</drAmount>"+"\n"+
	  "              <functionCode>testFunctionCode1</functionCode>"+"\n"+
	  "            </account>"+"\n"+
	  "          </accounts>"+"\n"+
	  "        </bill>"+"\n"+
	  "      </bills>"+"\n"+
	  "    </payee>"+"\n"+
	  "  </payees>"+"\n"+
	  "</bill-collect>";
	
	@Before
	public void setupAction(){
		
		instrumentService = createMock(InstrumentService.class);
		
		financialsUtil = new FinancialsUtil(){
			public CVoucherHeader getReversalVoucher(
					List<HashMap<String, Object>> paramList){
				return new CVoucherHeader();
			}
			public InstrumentType getInstrumentTypeByType(String type){
				return (InstrumentType) genericService.find("from InstrumentType  where type=? and isActive=true",type);
			}
		};
		financialsUtil.setInstrumentService(instrumentService);
		
		receiptHeaderService = new ReceiptHeaderService(){
			public void startWorkflow(Collection<ReceiptHeader> receiptHeaders,Boolean receiptBulkUpload){
				
			}
		};
		
		genericHibDao = new GenericHibernateDaoFactory(){
			protected Session getCurrentSession(){
				return session;
			}
			public  AppDataDAO getAppDataDAO()
			{
				return new AppDataHibernateDAO(AppData.class,session);
			}
		};
		
		receiptHeaderService.setType(ReceiptHeader.class);

		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);

		userManager = createMock(UserManager.class);
		boundaryDAO = createMock(BoundaryDAO.class);
		commonsManager = createMock(CommonsManager.class);
		eisManager = createMock(EisManager.class);
		egovCommon = createMock(EgovCommon.class);
		eisCommonsManagerMock = createMock(EisCommonsManager.class);
		//receiptWorkflowServiceMock = createMock(WorkflowService.class);
		beanProvider=createMock(ApplicationContextBeanProvider.class);
		auditEventService =  new AuditEventService();
		ApplicationThreadLocals.setUserId("1");
		genericService.setType(AuditEvent.class);
		auditEventService.setAuditEventPersistenceService(genericService);
		
		collectionsUtil=new CollectionsUtil();
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setEisManager(eisManager);
		collectionsUtil.setEisCommonsManager(eisCommonsManagerMock);
		collectionsUtil.setGenericDao(genericHibDao);
		collectionsUtil.setBeanProvider(beanProvider);
		collectionsUtil.setAuditEventService(auditEventService);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);
		
		receiptService = new ReceiptService(){
		public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
			return true;
		}
		};;
		receiptService.setType(ReceiptPayeeDetails.class);

		receiptService.setFinancialsUtil(financialsUtil);
		receiptService.setCollectionsUtil(collectionsUtil);
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		collectionCommon = new CollectionCommon();
		collectionCommon.setBoundaryDAO(boundaryDAO);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		collectionCommon.setCommonsManager(commonsManager);
		collectionCommon.setEgovCommon(egovCommon);
		collectionCommon.setPersistenceService(genericService);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		
		//receiptHeaderService.setReceiptWorkflowService(receiptWorkflowServiceMock);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
				
		action = new ReceiptAction(){
			 @Override
			 public String getText(String textName)
			 {
				 return textName;
			 }
			 
		};
		
		PersistenceService<ServiceCategory, Long> serviceCategoryService = new PersistenceService<ServiceCategory, Long>();
		action.setServiceCategoryService(serviceCategoryService);
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		
		action.setCollectXML(inputXML);
		action.setCollectionsUtil(collectionsUtil);
		action.setPersistenceService(genericService);
		action.setReceiptPayeeDetailsService(receiptService);
		action.setReceiptHeaderService(receiptHeaderService);
		action.setCommonsManager(commonsManager);
		action.setCollectionCommon(collectionCommon);
		
		action.setFinancialsUtil(financialsUtil);
		
		objectFactory = new CollectionObjectFactory(session,genericService);
		
		xmlHandler = new BillCollectXmlHandler();
		
	}
	
	private void createEmployeeForReceiptCounterOperator(Department dept){
		UserImpl user = objectFactory.createUser("system",dept);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,dept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(dept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,dept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);
		
		action.setReceiptCreatedByCounterOperator(user);
	}
	
		
	private List<ReceiptPayeeDetails> initialisePrepareAndModel(boolean valid, boolean runMocks){
		
		action.setXmlHandler(xmlHandler);
		BoundaryImpl boundary = objectFactory.createBoundary();
		Fund fund = null;
		DepartmentImpl dept = null;
		if(valid){
			fund = objectFactory.createFund("testFundCode");
			dept = objectFactory.createDeptForCode("testDeptCode");
		}
		ServiceDetails service = objectFactory.createServiceDetails("testServCode");
		Functionary functionary = (Functionary) genericService.findByNamedQuery(
				CollectionConstants.QUERY_FUNCTIONARY_BY_CODE, new BigDecimal(1));
		Fundsource fundSource = objectFactory.createFundsource("testfundSourceName", "testfundSourceCode");
		CFunction function1 = objectFactory.createFunction("testFunctionName1","testFunctionCode1");
		CChartOfAccounts account1=objectFactory.createCOA("testGLCode1");
		
		UserImpl user = objectFactory.createUser("system",dept);
		Location location = objectFactory.createCounter("test");
		
		if(runMocks){
			userManager.getUserByUserName("system");
			expectLastCall().andReturn(user).anyTimes();
			replay(userManager);
			
			commonsManager.fundByCode("testFundCode");
			expectLastCall().andReturn(fund);
			
			commonsManager.getFundSourceByCode("testfundSourceCode");
			expectLastCall().andReturn(fundSource);
			
			commonsManager.getFunctionByCode("testFunctionCode1");
			expectLastCall().andReturn(function1);
			
			commonsManager.getCChartOfAccountsByGlCode("testGLCode1");
			expectLastCall().andReturn(account1);
		}
		
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sessionMap);
		
		
		return createModelFromXML(service,boundary,fund,functionary,fundSource,dept,account1,function1);
	}
	
	//@Test
	public void testXMLDecode(){
		String encoded = java.net.URLEncoder.encode(inputXML);
		action.setCollectXML(encoded);
		assertEquals(action.getCollectXML(),inputXML);
	}
	
	
	@Test
	public void testPrepare(){
		
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(true,true);
		ReceiptPayeeDetails expectedPayee = expectedPayeeList.get(0);
		ReceiptHeader expectedReceiptHdr = expectedPayee.getReceiptHeaders().iterator().next();
		replay(commonsManager);
		
		action.prepare();
		
		List<ReceiptPayeeDetails> actualPayeeList = (List<ReceiptPayeeDetails>) action.getModel();
		ReceiptPayeeDetails actualPayee = actualPayeeList.get(0);
		ReceiptHeader actualReceiptHdr = actualPayee.getReceiptHeaders().iterator().next();
		
		assertEquals(actualPayeeList.size(),1);
		assertEquals(expectedPayee.getPayeename(), actualPayee.getPayeename());
		assertEquals(expectedReceiptHdr.getTotalAmountToBeCollected(),
				actualReceiptHdr.getTotalAmountToBeCollected());
		assertEquals(actualPayeeList,action.getModelPayeeList());
		assertTrue(action.getChequeAllowed());
		assertTrue(action.getCashAllowed());
		assertTrue(action.getCardAllowed());
		assertTrue(action.getOverrideAccountHeads());
		assertTrue(action.getPartPaymentAllowed());
		assertEquals(action.getServiceName(),"testServiceName");
	}
	
	//@Test
	public void testSaveNewForCash() throws Exception{
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(true,true);
		
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
		EgwStatus status = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		expectLastCall().andReturn(status);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentStatus);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		action.prepare();
		
		InstrumentHeader instrHeaderCash = new InstrumentHeader();
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		instrHeaderCash.setInstrumentAmount(action.getTotalAmountToBeCollected());
		instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCash.setStatusId(instrumentStatus);
		instrHeaderCash.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));
		session.saveOrUpdate(instrHeaderCash);
		actualInstrList.add(instrHeaderCash);
		action.setInstrHeaderCash(instrHeaderCash);
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCash);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);

		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		Set<ReceiptDetail> receiptDetailListFromModel = expectedPayeeList.get(0).getReceiptHeaders().iterator().next().getReceiptDetails();
		List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();
		for(ReceiptDetail receiptDetailChange : receiptDetailListFromModel){
			ReceiptDetail receiptDetail = new ReceiptDetail();
			receiptDetail.setCramount(receiptDetailChange.getCramountToBePaid());
			receiptDetail.setDramount(receiptDetailChange.getDramount());
			receiptDetail.setOrdernumber(receiptDetailChange.getOrdernumber());
			receiptDetail.setReceiptHeader(receiptDetailChange.getReceiptHeader());
			receiptDetailList.add(receiptDetail);
		}
		action.setReceiptDetailList(receiptDetailList);
		
		beanProvider.getBean(expectedPayeeList.get(0).getReceiptHeaders().iterator().next().getService().getCode()+CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
		expectLastCall().andReturn(new BillingIntegrationServiceStub());
		replay(beanProvider);
		
		assertEquals(action.save(),"report");
	}
	
	
	@SuppressWarnings("unchecked")
	//@Test
	public void testSaveNewForBank() throws Exception{
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(true,false);
		action.setModelPayeeList(expectedPayeeList);
		
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_BANK);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentStatus);
		
		
		InstrumentHeader instrHeaderBank = objectFactory.createBankInstrumentHeader();
		action.setBankAccountId(instrHeaderBank.getBankAccountId().getId());
		commonsManager.getBankaccountById(instrHeaderBank.getBankAccountId().getId());
		expectLastCall().andReturn(instrHeaderBank.getBankAccountId());
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		Set<ReceiptDetail> receiptDetailListFromModel = expectedPayeeList.get(0).getReceiptHeaders().iterator().next().getReceiptDetails();
		List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();
		for(ReceiptDetail receiptDetailChange : receiptDetailListFromModel){
			ReceiptDetail receiptDetail = new ReceiptDetail();
			receiptDetail.setCramount(receiptDetailChange.getCramountToBePaid());
			receiptDetail.setDramount(receiptDetailChange.getDramount());
			receiptDetail.setOrdernumber(receiptDetailChange.getOrdernumber());
			receiptDetail.setReceiptHeader(receiptDetailChange.getReceiptHeader());
			receiptDetailList.add(receiptDetail);
		}
		action.setReceiptDetailList(receiptDetailList);
		
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		actualInstrList.add(instrHeaderBank);
		action.setInstrHeaderBank(instrHeaderBank);
		action.setOverrideAccountHeads(Boolean.FALSE);
		action.setCallbackForApportioning(Boolean.FALSE);

		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderBank);
		
		EasyMock.expect(instrumentService.addToInstrument(EasyMock.isA(List.class))).andReturn(actualInstrList);
		EasyMock.expect(instrumentService.updateInstrumentVoucherReference(EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		assertEquals(action.save(),"report");
	}
	
	@Test
	public void testSaveNewForCard() throws Exception{
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(true,true);
		
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CARD);
		EgwStatus status = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		expectLastCall().andReturn(status);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentStatus);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		// action.setReportService(reportService);
		
		action.prepare();
		
		InstrumentHeader instrHeaderCard = new InstrumentHeader();
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		instrHeaderCard.setInstrumentAmount(action.getTotalAmountToBeCollected());
		instrHeaderCard.setStatusId(instrumentStatus);
		instrHeaderCard.setTransactionNumber("12345");
		instrHeaderCard.setTransactionDate(new Date());
		instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCard.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CARD));
		session.saveOrUpdate(instrHeaderCard);
		actualInstrList.add(instrHeaderCard);
		action.setInstrHeaderCard(instrHeaderCard);
		action.setManualReceiptNumber("ABC123");
		action.setManualReceiptDate(new Date());
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCard);
		
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList =  new ArrayList();
				
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		Set<ReceiptDetail> receiptDetailListFromModel = expectedPayeeList.get(0).getReceiptHeaders().iterator().next().getReceiptDetails();
		List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();
		for(ReceiptDetail receiptDetailChange : receiptDetailListFromModel){
			ReceiptDetail receiptDetail = new ReceiptDetail();
			receiptDetail.setCramount(receiptDetailChange.getCramountToBePaid());
			receiptDetail.setDramount(receiptDetailChange.getDramount());
			receiptDetail.setOrdernumber(receiptDetailChange.getOrdernumber());
			receiptDetail.setReceiptHeader(receiptDetailChange.getReceiptHeader());
			receiptDetailList.add(receiptDetail);
		}
		action.setReceiptDetailList(receiptDetailList);
		
		assertEquals(action.save(),"report");
	}
	
	
	@Test
	public void testSaveNewForChequeDD() throws Exception{
		
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		UserImpl loggedinUser = objectFactory.createUser("testUserName", loggedinUserDept);
		Location location = objectFactory.createCounter("test");
		action.setReceiptCreatedByCounterOperator(loggedinUser);
		
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_BILL, "testRefNo", 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, loggedinUser,null);
		// The receipt is yet to be created. so the status is null
		receiptHeader.setStatus(null);
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		
		//payee = receiptService.persist(payee);
		action.getModelPayeeList().add(payee);
		
		EgwStatus status = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		expectLastCall().andReturn(status);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentStatus);
		
		
		Bank bank = objectFactory.createBank();
		commonsManager.getBankById(Integer.valueOf(bank.getId()));
		expectLastCall().andReturn(bank).anyTimes();
		
		CChartOfAccounts chequeInHand = objectFactory.createCOA("TestcchequeInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CHEQUEINHAND, chequeInHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(chequeInHand.getGlcode());
		expectLastCall().andReturn(chequeInHand);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID,location.getId().toString());
		action.setSession(sessionMap);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String vdt = sdf.format(date);
		date = sdf.parse(vdt);
		
		String[] instrumentType = new String[2];
		String[] instrAmt =  new String[2];
		String[] branchName =  new String[2];
		String[] instrNum =  new String[2];
		String[] bankId =  new String[2];
		String[] instrDate = new String[2];
		
		
		instrumentType[0]=CollectionConstants.INSTRUMENTTYPE_CHEQUE;
		instrAmt[0]="1000.0";
		branchName[0]="1001";
		instrNum[0]="1001";
		bankId[0]=bank.getId().toString();
		instrDate[0]=vdt;
		
		instrumentType[1]=CollectionConstants.INSTRUMENTTYPE_DD;
		instrAmt[1]="1000.0";
		branchName[1]="1001";
		instrNum[1]="1001";
		bankId[1]=bank.getId().toString();
		instrDate[1]=vdt;
		
		InstrumentHeader chqInstrumentHeader=objectFactory.createInstrumentHeaderWithBankDetails(
				financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE), 
				instrNum[0],Double.valueOf(instrAmt[0]),date,
				instrumentStatus,bank,branchName[0],"0");
		InstrumentHeader ddInstrumentHeader=objectFactory.createInstrumentHeaderWithBankDetails(
				financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD), 
				instrNum[1],Double.valueOf(instrAmt[1]),date,
				instrumentStatus,bank,branchName[1],"0");
		
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		actualInstrList.add(chqInstrumentHeader);
		actualInstrList.add(ddInstrumentHeader);
		
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(chqInstrumentHeader);
		instrList.add(ddInstrumentHeader);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		//objectFactory.createMapForInstrumentVoucher(voucherHeaderList,instrList);
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(new ArrayList());
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		action.setInstrumentProxyList(actualInstrList);
		action.setOverrideAccountHeads(Boolean.FALSE);
		action.setCallbackForApportioning(Boolean.FALSE);
		action.setManualReceiptNumber("ABC123");
		action.setManualReceiptDate(new Date());
		action.setInstrumentAmount(instrAmt);
		action.setInstrumentType(instrumentType);
		action.setInstrumentBranchName(branchName);
		action.setInstrumentNumber(instrNum);
		action.setInstrumentBankId(bankId);
		action.setInstrumentDate(instrDate);
		
		List<ReceiptPayeeDetails> expectedPayeeList= action.getModelPayeeList();
		Set<ReceiptDetail> receiptDetailListFromModel = expectedPayeeList.get(0).getReceiptHeaders().iterator().next().getReceiptDetails();
		List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();
		for(ReceiptDetail receiptDetailChange : receiptDetailListFromModel){
			ReceiptDetail receiptDetail = new ReceiptDetail();
			receiptDetail.setCramount(receiptDetailChange.getCramountToBePaid());
			receiptDetail.setOrdernumber(receiptDetailChange.getOrdernumber());
			receiptDetail.setReceiptHeader(receiptDetailChange.getReceiptHeader());
			receiptDetail.setDramount(receiptDetailChange.getDramount());
			receiptDetail.setCramountToBePaid(receiptDetailChange.getDramount());
			receiptDetailList.add(receiptDetail);
		}
		action.setReceiptDetailList(receiptDetailList);
		
		assertEquals(action.save(),"report");
	}
	
	@Test
	public void testSaveOnCancel(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithInstrument(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_BILL, 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, "testReferenceNo", 
				objectFactory.createInstrumentType(CollectionConstants.INSTRUMENTTYPE_CARD), 
				"testInstrNo", Double.valueOf(1000), new Date(),
				CollectionConstants.INSTRUMENT_NEW_STATUS, "testGLCode", 
				"testFunctionName", "testUserName");
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		
		
		receiptService.persist(payee);
		
		ReceiptHeader saved = receiptHeaderService.findById(
				payee.getReceiptHeaders().iterator().next().getId(), false);
		
		EgwStatus receiptCancelStatus = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		expectLastCall().andReturn(receiptCancelStatus);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED);
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED);
		expectLastCall().andReturn(instrumentStatus);
		
		replay(commonsManager);
		
		
		action.setOldReceiptId(saved.getId());
		action.setReasonForCancellation("RECEIPT CANCELLATION TEST");
		
		assertEquals(action.saveOnCancel(),"index");
		ReceiptHeader cancelledReceiptFromAction = action.getReceiptHeaderValues().get(0);
		assertEquals(cancelledReceiptFromAction.getStatus().getDescription(),CollectionConstants.RECEIPT_STATUS_DESC_CANCELLED);
		assertEquals(cancelledReceiptFromAction.getReasonForCancellation(),action.getReasonForCancellation());
		assertEquals(action.getTarget(),"cancel");
	}
	
	@Test
	public void testSaveNewAfterIntradayCancellation() throws Exception{
		
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithInstrument(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_BILL, 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, "testReferenceNo", 
				objectFactory.createInstrumentType(CollectionConstants.INSTRUMENTTYPE_CARD), 
				"testInstrNo", Double.valueOf(1000), new Date(),
				CollectionConstants.INSTRUMENT_DEPOSITED_STATUS, "testGLCode", 
				"testFunctionName", "testUserName");
		
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		UserImpl loggedinUser = objectFactory.createUser("testUserName", loggedinUserDept);
		Location location = objectFactory.createCounter("test");
		action.setReceiptCreatedByCounterOperator(loggedinUser);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sessionMap);
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		receiptHeader.setService(objectFactory.createServiceDetails());
		payee.addReceiptHeader(receiptHeader);
		
		List <ReceiptPayeeDetails>payeeList = new ArrayList();
		payeeList.add(payee);
			
		action.setModelPayeeList(payeeList);
		
		receiptService.persist(payee);
		
		ReceiptHeader saved = receiptHeaderService.findById(
				payee.getReceiptHeaders().iterator().next().getId(), false);
		
		EgwStatus receiptNewStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		expectLastCall().andReturn(receiptNewStatus);
		
		EgwStatus receiptCancelStatus = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		expectLastCall().andReturn(receiptCancelStatus);
		
		
		EgwStatus instrumentNewStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentNewStatus);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
		voucherHeaderList.add(voucherHeader);
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		InstrumentHeader instrHeaderCash = new InstrumentHeader();
		instrHeaderCash.setInstrumentAmount(
				saved.getReceiptDetails().iterator().next().getCramount());
		instrHeaderCash.setStatusId(instrumentNewStatus);
		instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCash.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));
		session.saveOrUpdate(instrHeaderCash);
		actualInstrList.add(instrHeaderCash);
		action.setInstrHeaderCash(instrHeaderCash);
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCash);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
		action.setOverrideAccountHeads(Boolean.FALSE);
		action.setCallbackForApportioning(Boolean.FALSE);
		action.setManualReceiptNumber("ABC123");
		action.setManualReceiptDate(new Date());
		
		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		// action.setReportService(reportService);
		
		action.setOldReceiptId(saved.getId());
		
		beanProvider.getBean(saved.getService().getCode()+CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);
		expectLastCall().andReturn(new BillingIntegrationServiceStub()).anyTimes();
		replay(beanProvider);
		
		assertEquals(action.save(),"report");
		
		
		ReceiptHeader cancelledReceiptHeader = receiptHeaderService.findById(action.getOldReceiptId(), false);
		Iterator<ReceiptHeader> iterator = action.getModelPayeeList().get(0).getReceiptHeaders().iterator();
		
		assertEquals(iterator.next().getStatus().getDescription(), CollectionConstants.RECEIPT_STATUS_DESC_CREATED);
		assertEquals(cancelledReceiptHeader.getStatus().getDescription(),CollectionConstants.RECEIPT_STATUS_DESC_CANCELLED);
	}
	
	@Test
	public void testSaveOnIntradayCancellation(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithInstrument(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_BILL, 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, "testReferenceNo", 
				objectFactory.createInstrumentType(CollectionConstants.INSTRUMENTTYPE_CARD), 
				"testInstrNo", Double.valueOf(1000), new Date(),
				CollectionConstants.INSTRUMENT_DEPOSITED_STATUS, "testGLCode", 
				"testFunctionName", "testUserName");
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		
		receiptService.persist(payee);
		
		ReceiptHeader saved = receiptHeaderService.findById(
				payee.getReceiptHeaders().iterator().next().getId(), false);
		
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		createEmployeeForReceiptCounterOperator(loggedinUserDept);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		EgwStatus receiptCancelStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		expectLastCall().andReturn(receiptCancelStatus);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED);
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED);
		expectLastCall().andReturn(instrumentStatus);
		
		replay(commonsManager);
		
		
		action.setOldReceiptId(saved.getId());
		
		assertEquals(action.saveOnCancel(),"new");
	}
	
	public List<ReceiptPayeeDetails> createModelFromXML(ServiceDetails service, Boundary boundary,
			Fund fund,Functionary functionary,Fundsource fundSource,DepartmentImpl dept, 
			CChartOfAccounts account,CFunction function){
		BillInfoImpl bill = (BillInfoImpl) xmlHandler.toObject(inputXML);
		BigDecimal totalAmountToBeCollected = BigDecimal.valueOf(0);
		StringBuilder collModesNotAllowed = new StringBuilder();
		if(bill.getCollectionModesNotAllowed()!=null){
			for(String collModeNotAllwd : bill.getCollectionModesNotAllowed()) {
				if(collModesNotAllowed.length() > 0) {
					collModesNotAllowed.append(',');
				}
				collModesNotAllowed.append(collModeNotAllwd);
			}
		}
		
		List<ReceiptPayeeDetails> testmodelPayeeList = new ArrayList<ReceiptPayeeDetails>();
		for(BillPayeeDetails billPayee : bill.getPayees()){
			ReceiptPayeeDetails payee = new ReceiptPayeeDetails(billPayee.getPayeeName(),billPayee.getPayeeAddress());
			
			for(BillDetails billDetail : billPayee.getBillDetails()){
				ReceiptHeader receiptHeader = new ReceiptHeader(
						billDetail.getRefNo(),billDetail.getBilldate(),billDetail.getConsumerCode(),
						billDetail.getDescription(),billDetail.getTotalAmount(),
						billDetail.getMinimumAmount(),bill.getPartPaymentAllowed(),
						bill.getOverrideAccountHeadsAllowed(),bill.getCallbackForApportioning(),
						bill.getDisplayMessage(),service,collModesNotAllowed.toString());
				
				ReceiptMisc receiptMisc = new ReceiptMisc(
						boundary,fund,functionary,fundSource,dept,receiptHeader,null,null);
				
				receiptHeader.setReceiptMisc(receiptMisc);
				
				for(BillAccountDetails billAccount:billDetail.getAccounts()){
					ReceiptDetail receiptDetail = new ReceiptDetail(
							account,function,billAccount.getCrAmount(),
							billAccount.getDrAmount(),
							billAccount.getCrAmount(),
							Long.valueOf(billAccount.getOrder()),
							billAccount.getDescription(),
							Long.valueOf(billAccount.getIsActualDemand()),
							receiptHeader);
					totalAmountToBeCollected=totalAmountToBeCollected.add(
							billAccount.getCrAmount()).subtract(billAccount.getDrAmount());
					receiptHeader.addReceiptDetail(receiptDetail);
				}
				receiptHeader.setTotalAmountToBeCollected(totalAmountToBeCollected);
				receiptHeader.setReceiptPayeeDetails(payee);
				payee.addReceiptHeader(receiptHeader);
			}
			testmodelPayeeList.add(payee);
		}
		
		return testmodelPayeeList;
	}
	
	@Test
	public void testGetMinimumAmt(){
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeDetails();
		payee.getReceiptHeaders().iterator().next().setMinimumAmount(BigDecimal.TEN);
		action.getModelPayeeList().add(payee);
		
		assertEquals(action.getMinimumAmount(),BigDecimal.TEN);
	}
	
	@Test
	public void testGetTotalNoOfAccounts(){
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeForBillingSystem();
		action.getModelPayeeList().add(payee);
		
		assertEquals(action.getTotalNoOfAccounts(),1);
	}
	
	@Test
	public void testCreate() {
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		Department dept = objectFactory.createDeptForCode("testDeptCode");
		createEmployeeForReceiptCounterOperator(dept);
		
		BillInfoImpl bill = (BillInfoImpl) xmlHandler.toObject(inputXML);
		action.setCollectionModesNotAllowed(bill.getCollectionModesNotAllowed());
		
		String result = action.newform();
		assertEquals("new",result);
		
		assertFalse(action.getChequeAllowed());
		assertFalse(action.getDdAllowed());
		assertFalse(action.getCashAllowed());
		assertFalse(action.getCardAllowed());
		assertTrue(action.getBankAllowed());
	}
	
	@Test
	public void testCreateWithLoggedInUserInDeptA(){
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		Department dept = (Department)genericService.find("FROM DepartmentImpl DI WHERE DI.deptCode = 'A'");
		createEmployeeForReceiptCounterOperator(dept);
		
		String result = action.newform();
		assertEquals("new",result);
		
		assertTrue(action.getChequeAllowed());
		assertTrue(action.getCashAllowed());
		assertFalse(action.getCardAllowed());
		assertTrue(action.getDdAllowed());
	}
	
	@Test
	public void testPrepareWithInvalidXML() {
		Department dept = objectFactory.createDeptForCode("testDeptCode");
		UserImpl user = objectFactory.createUser("system",dept);

		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		action.setReceiptCreatedByCounterOperator(user);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		String xml = "<bill-collect><test></bill-collect>";
		action.setCollectXML(xml);
		action.prepare();
		
		assertTrue(action.getActionErrors().contains("billreceipt.error.improperbilldata"));
	}
	
	@Test
	public void testViewReceipts(){
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		// action.setReportService(reportService);
		action.setSession(new HashMap());

		ReceiptHeader receipt1 = objectFactory.createReceiptHeader("RN1234");

		ReceiptHeaderService receiptHeaderService = org.easymock.classextension.EasyMock.createMock(ReceiptHeaderService.class);
		action.setReceiptHeaderService(receiptHeaderService);
		org.easymock.classextension.EasyMock.expect(receiptHeaderService.findById(org.easymock.classextension.EasyMock.isA(Long.class), org.easymock.classextension.EasyMock.eq(false))).andReturn(receipt1);
		org.easymock.classextension.EasyMock.replay(receiptHeaderService);
		
		action.setSelectedReceipts(new Long[]{receipt1.getId()});
		String result =action.viewReceipts();

		EasyMock.verify(reportService);
		org.easymock.classextension.EasyMock.verify(receiptHeaderService);
		assertEquals("report",result);
		assertEquals(action.getReceipts()[0].getId(),action.getSelectedReceipts()[0]);
	}
	
	@Test(expected = ApplicationRuntimeException.class)
	public void testViewReceiptsWithNoSelection(){
		action.setSelectedReceipts(new Long[]{});
		String result =action.viewReceipts();
		assertEquals("report",result);
	}
	
	@Test
	public void testPrintReceipts(){
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		// action.setReportService(reportService);
		action.setSession(new HashMap());

		ReceiptHeader receipt1 = objectFactory.createReceiptHeader("RN1234");

		ReceiptHeaderService receiptHeaderService = org.easymock.classextension.EasyMock.createMock(ReceiptHeaderService.class);
		action.setReceiptHeaderService(receiptHeaderService);
		org.easymock.classextension.EasyMock.expect(receiptHeaderService.findById(org.easymock.classextension.EasyMock.isA(Long.class), org.easymock.classextension.EasyMock.eq(false))).andReturn(receipt1);
		org.easymock.classextension.EasyMock.replay(receiptHeaderService);
		
		action.setSelectedReceipts(new Long[]{receipt1.getId()});
		String result =action.printReceipts();

		EasyMock.verify(reportService);
		org.easymock.classextension.EasyMock.verify(receiptHeaderService);
		assertEquals("report",result);
	}
	
	@Test(expected = ApplicationRuntimeException.class)
	public void testPrintReceiptsWithNoSelection(){
		action.setSelectedReceipts(new Long[]{});
		String result =action.printReceipts();
		assertEquals("report",result);
	}
	
	@Test
	public void testCancelReceiptsWithNoSelection(){
		ReceiptHeader receipt1 = objectFactory.createReceiptHeader("RN1234");
		action.setSelectedReceipts(new Long[]{receipt1.getId()});
		String result =action.cancel();
		assertEquals("cancel",result);
	}
	
	@Test
	public void testCancel(){
		action.setSelectedReceipts(new Long[]{});
		String result =action.cancel();
		assertEquals("cancel",result);
	}
	
	
	//@Test
	public void testCreateMisc() {
		collectionsUtil=new CollectionsUtil(){
			public List<String> getCollectionModesNotAllowed(User loggedInUser,ServiceDetails serviceDetails){
				return (new ArrayList<String>());
			}
		};
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
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setEisManager(eisManager);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		action.setCollectionCommon(collectionCommon);
		action.setCollectionsUtil(collectionsUtil);
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		parameters.put("Receipt", new String[]{"Misc"});
		action.setParameters(parameters);
		
		Department dept = objectFactory.createDeptForCode("testDeptCode");
		createEmployeeForReceiptCounterOperator(dept);
		HashMap<String, Object> sess = new HashMap<String, Object>();
		sess.put("com.egov.user.LoginUserName", "system");
		action.setSession(sess);
		ApplicationThreadLocals.setDomainName("localhost");
		
		String result = action.newform();
		assertEquals("new",result);
		
	}
	
	//@Test
	public void testSaveNewMiscReceiptsForCash() throws Exception{
		ApplicationThreadLocals.setDomainName("localhost");
		collectionsUtil=new CollectionsUtil(){
			public List<String> getCollectionModesNotAllowed(User loggedInUser,ServiceDetails serviceDetails){
				return (new ArrayList<String>());
			}
		};
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
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setEisManager(eisManager);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		action.setCollectionCommon(collectionCommon);
		action.setCollectionsUtil(collectionsUtil);
		Department dept = objectFactory.createDeptForCode("testDeptCode");
		UserImpl user = objectFactory.createUser("system",dept);
		Location location = objectFactory.createCounter("test");
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,dept);
		
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(dept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,dept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);
				
		action.setReceiptCreatedByCounterOperator(user);
		HashMap<String, Object> sess = new HashMap<String, Object>();
		sess.put("com.egov.user.LoginUserName", "system");
		sess.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sess);
		action.setBillSource("misc");
		action.setDeptId(dept.getId().toString());
		action.setVoucherDate(new Date());
		ReceiptMisc receiptMisc=objectFactory.createReceiptMisForMiscReceipt();
		CChartOfAccounts account=objectFactory.createCOA("1201001");
		CFunction functionObj = objectFactory.createFunction("testfunction");  
		action.setReceiptMisc(receiptMisc);
		
		commonsManager.fundById(receiptMisc.getFund().getId());
		expectLastCall().andReturn(receiptMisc.getFund()).anyTimes();
		commonsManager.getFunctionaryById(receiptMisc.getIdFunctionary().getId());
		expectLastCall().andReturn(receiptMisc.getIdFunctionary());
		commonsManager.getSchemeById(receiptMisc.getScheme().getId());
		expectLastCall().andReturn(receiptMisc.getScheme());
		commonsManager.getSubSchemeById(receiptMisc.getSubscheme().getId());
		expectLastCall().andReturn(receiptMisc.getSubscheme());
		commonsManager.fundsourceById(receiptMisc.getFundsource().getId());
		expectLastCall().andReturn(receiptMisc.getFundsource());
		commonsManager.getCChartOfAccountsByGlCode(EasyMock.isA(String.class));
		expectLastCall().andReturn(account);
		commonsManager.getFunctionByCode(EasyMock.isA(String.class));
		expectLastCall().andReturn(functionObj);
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
		EgwStatus status = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		expectLastCall().andReturn(status);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentStatus);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
		voucherHeaderList.add(voucherHeader);
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		InstrumentHeader instrHeaderCash = new InstrumentHeader();
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		instrHeaderCash.setInstrumentAmount(new BigDecimal(1000.0));
		instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCash.setStatusId(instrumentStatus);
		instrHeaderCash.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));
		session.saveOrUpdate(instrHeaderCash);
		actualInstrList.add(instrHeaderCash);
		action.setInstrHeaderCash(instrHeaderCash);
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCash);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList =  new ArrayList();
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
			
		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		action.setReceiptCreatedByCounterOperator(user);
		action.setBillCreditDetailslist(objectFactory.createBillCreditDetailslist());
		action.setBillRebateDetailslist(objectFactory.createEmptyBillRebateDetailslist());
		action.setSubLedgerlist(objectFactory.createEmptySubLedgerlist());
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		assertEquals(action.save(),"report");
	}
	
	//@Test
	public void testSaveNewMiscReceiptsForCashWithSubledger() throws Exception{
		ApplicationThreadLocals.setDomainName("localhost");
		collectionsUtil=new CollectionsUtil(){
			public List<String> getCollectionModesNotAllowed(User loggedInUser){
				return (new ArrayList<String>());
			}
		};
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
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setEisManager(eisManager);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		action.setCollectionCommon(collectionCommon);
		action.setCollectionsUtil(collectionsUtil);
		Department dept = objectFactory.createDeptForCode("testDeptCode");
		UserImpl user = objectFactory.createUser("system",dept);
		Location location = objectFactory.createCounter("test");
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,dept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(dept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,dept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);
		
		action.setReceiptCreatedByCounterOperator(user);
		HashMap<String, Object> sess = new HashMap<String, Object>();
		sess.put("com.egov.user.LoginUserName", "system");
		sess.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sess);
		action.setBillSource("misc");
		action.setVoucherDate(new Date());
		ReceiptMisc receiptMisc=objectFactory.createReceiptMisForMiscReceipt();
		//CChartOfAccounts account=objectFactory.createCOA("1201001");
		CFunction functionObj = objectFactory.createFunction("testfunction");  
		action.setReceiptMisc(receiptMisc);
		
		commonsManager.fundById(receiptMisc.getFund().getId());
		expectLastCall().andReturn(receiptMisc.getFund()).anyTimes();
		commonsManager.getFunctionaryById(receiptMisc.getIdFunctionary().getId());
		expectLastCall().andReturn(receiptMisc.getIdFunctionary());
		commonsManager.getSchemeById(receiptMisc.getScheme().getId());
		expectLastCall().andReturn(receiptMisc.getScheme());
		commonsManager.getSubSchemeById(receiptMisc.getSubscheme().getId());
		expectLastCall().andReturn(receiptMisc.getSubscheme());
		commonsManager.fundsourceById(receiptMisc.getFundsource().getId());
		expectLastCall().andReturn(receiptMisc.getFundsource());
		commonsManager.getFunctionByCode(EasyMock.isA(String.class));
		expectLastCall().andReturn(functionObj).anyTimes();
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
		EgwStatus status = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		expectLastCall().andReturn(status);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		expectLastCall().andReturn(instrumentStatus);
		
			
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		//replay(commonsManager);
		
		InstrumentHeader instrHeaderCash = new InstrumentHeader();
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		instrHeaderCash.setInstrumentAmount(new BigDecimal(1000.0));
		instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCash.setStatusId(instrumentStatus);
		instrHeaderCash.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));
		session.saveOrUpdate(instrHeaderCash);
		actualInstrList.add(instrHeaderCash);
		action.setInstrHeaderCash(instrHeaderCash);
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCash);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList =  new ArrayList();//objectFactory.createMapForInstrumentVoucher(voucherHeaderList,instrList);
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
			
		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		action.setReceiptCreatedByCounterOperator(user);
		
		List<ReceiptDetailInfo> billDetailslist = objectFactory.createBillCreditDetailslist();
		String billGlcodeDetail = billDetailslist.get(0).getGlcodeDetail();
		CChartOfAccounts billAccount=objectFactory.createCOAForGLCode(billGlcodeDetail);
		
		commonsManager.getCChartOfAccountsByGlCode(billGlcodeDetail);
		expectLastCall().andReturn(billAccount);
		
		action.setBillCreditDetailslist(billDetailslist);
		
		List<ReceiptDetailInfo> billRebateDetailslist = objectFactory.createBillRebateDetailslist();
		String rebateGlcodeDetail = billRebateDetailslist.get(0).getGlcodeDetail();
		CFunction function=objectFactory.createFunction("testFunction");
		CChartOfAccounts rebateAccount=objectFactory.createCOAForGLCode(rebateGlcodeDetail);
		billRebateDetailslist.get(0).setGlcodeIdDetail(rebateAccount.getId());
		
		Accountdetailtype accountDetailType = objectFactory.createAccountdetailtype("testAccDetailType");
		objectFactory.createCOADetail(rebateAccount, accountDetailType);
		
		commonsManager.getCChartOfAccountsByGlCode(rebateGlcodeDetail);
		expectLastCall().andReturn(rebateAccount);
		commonsManager.getFunctionById(billRebateDetailslist.get(0).getFunctionIdDetail());
		expectLastCall().andReturn(function);
		replay(commonsManager);
		
		action.setBillRebateDetailslist(billRebateDetailslist);
		
		
		List<ReceiptDetailInfo> subLedgerlist = objectFactory.createSubLedgerlist();
		subLedgerlist.get(0).setGlcode(rebateAccount);
		subLedgerlist.get(1).setGlcode(rebateAccount);
		
		action.setSubLedgerlist(subLedgerlist);
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		

		assertEquals(action.save(),"report");
	}
	
	@Test
	public void testIncompleteXMLData(){
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(false,true);
		ReceiptPayeeDetails expectedPayee = expectedPayeeList.get(0);
		expectedPayee.getReceiptHeaders().iterator().next();
		replay(commonsManager);
		
		action.prepare();
		
		assertNotNull(action.getActionErrors());
		assertTrue(action.getActionErrors().contains("billreceipt.improperbilldata.missingfund"));
	}
*/}
