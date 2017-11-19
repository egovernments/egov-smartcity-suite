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
package org.egov.collection.web.actions.citizen;


public class OnlineReceiptActionTest  { /*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	private OnlineReceiptAction action;
	
	private CollectionsUtil collectionsUtil;
	private FinancialsUtil financialsUtil;
	private ReceiptService receiptService;
	private ReceiptHeaderService receiptHeaderService;
	private InstrumentService instrumentService;
	private CommonsManager commonsManager;
	@Autowired AppConfigValuesDAO appConfigValuesDAO;
	
	private CollectionObjectFactory objectFactory;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private CollectionCommon collectionCommon;
	private BillCollectXmlHandler xmlHandler;
	private UserManager userManager;
	private EisManager eisManager;
	private BoundaryDAO boundaryDAO;
	private EgovCommon egovCommon;
	private CreateVoucher voucherCreator;
	
	
	private String inputXML = "<bill-collect>"+"\n"+
	  "  <serviceCode>testServCode</serviceCode>"+"\n"+
	  "  <fundCode>testFundCode</fundCode>"+"\n"+
	  "  <functionaryCode>1</functionaryCode>"+"\n"+
	  "  <fundSourceCode>testfundSourceCode</fundSourceCode>"+"\n"+
	  "  <departmentCode>testDeptCode</departmentCode>"+"\n"+
	  "  <displayMessage>HELLO USER</displayMessage>"+"\n"+
	  "  <partPaymentAllowed>1</partPaymentAllowed>"+"\n"+
	  "  <overrideAccountHeadsAllowed>1</overrideAccountHeadsAllowed>"+"\n"+
	  "  <collectionModeNotAllowed>cash</collectionModeNotAllowed>"+"\n"+
	  "  <collectionModeNotAllowed>cheque</collectionModeNotAllowed>"+"\n"+
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
		
		userManager = createMock(UserManager.class);
		eisManager = createMock(EisManager.class);
		instrumentService = createMock(InstrumentService.class);
		commonsManager=createMock(CommonsManager.class);
		boundaryDAO = createMock(BoundaryDAO.class);
		egovCommon = createMock(EgovCommon.class);
		voucherCreator = createMock(CreateVoucher.class);
		
		receiptService = new ReceiptService(){
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return true;
			}
		};
		receiptService.setType(ReceiptPayeeDetails.class);

		financialsUtil = new FinancialsUtil(){
			public InstrumentType getInstrumentTypeByType(String type){
				return (InstrumentType) genericService.find("from InstrumentType  where type=? and isActive=true",type);
			}
			public CVoucherHeader createPreApprovalVoucher(
					Map<String, Object> headerdetails,
					List<HashMap<String, Object>> accountcodedetails,
					List<HashMap<String, Object>> subledgerdetails){
				CVoucherHeader voucherHeader = objectFactory.createVoucher("testVoucher");
				return voucherHeader;
			}
		};
		financialsUtil.setInstrumentService(instrumentService);
		receiptService.setFinancialsUtil(financialsUtil);
		
		collectionsUtil=new CollectionsUtil();
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setEisManager(eisManager);
		
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
		
		financialsUtil.setVoucherCreator(voucherCreator);
		
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);

		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);
		
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		receiptHeaderService = new ReceiptHeaderService();
		receiptHeaderService.setType(ReceiptHeader.class);
		receiptHeaderService.setFinancialsUtil(financialsUtil);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		collectionCommon = new CollectionCommon(){
			 protected PaymentGatewayAdaptor getPaymentGatewayAdaptor(String serviceCode){
				 return new BillDeskAdaptor();
			 }
			 public Integer generateReport(ReceiptHeader[] receipts,
						Map<String, Object> session, boolean flag){
				 return 0;
			 }
		};
		
		collectionCommon.setBoundaryDAO(boundaryDAO);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		collectionCommon.setCommonsManager(commonsManager);
		collectionCommon.setEgovCommon(egovCommon);
		collectionCommon.setPersistenceService(genericService);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		
		action = new OnlineReceiptAction(){
			@Override
			public String getText(String textName) {
				return textName;
			}
		};
		action.setCollectXML(inputXML);
		action.setCollectionsUtil(collectionsUtil);
		action.setPersistenceService(genericService);
		action.setReceiptPayeeDetailsService(receiptService);
		action.setReceiptHeaderService(receiptHeaderService);
		action.setFinancialsUtil(financialsUtil);
		action.setCommonsManager(commonsManager);
		action.setCollectionCommon(collectionCommon);
				
		objectFactory = new CollectionObjectFactory(session);
		xmlHandler = new BillCollectXmlHandler();
		
	}
	
	//@Test
	public void testProcessMessageFromPaymentGatewayInvalidCheckSum(){
		
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_BILL, "CustomerID", 
				"testStatus", objectFactory.createUser("ONLINEUSER"),null);
		EgwStatus onlinePaytStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		OnlinePayment onlinePayt=objectFactory.createOnlinePayment(receiptHeader,String.valueOf(receiptHeader.getId()),new BigDecimal(1000.0),onlinePaytStatus);
		action.setPaymentServiceId(onlinePayt.getService().getId().intValue());
		
		String successMsg="MerchantID|testReferenceNo1|TxnReferenceNo|BankReferenceNo|789.9|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21-09-2009|0300|SettlementType|"+receiptHeader.getId()+"|AdditionalInfo2|AdditionalInfo3|AdditionalInfo4|"+
		"AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription|111111";
		
		action.setMsg(successMsg);
		action.setServiceCode(onlinePayt.getService().getCode());
		
		assertEquals(action.acceptMessageFromPaymentGateway(),"result");
		
		assertEquals(action.getActionErrors().size(), 1);
		assertTrue(action.getActionErrors().iterator().next().equals
				(onlinePayt.getService().getCode().toLowerCase() + ".pgi."+
				receiptHeader.getService().getCode().toLowerCase()+".checksum.mismatch"));
	}
	
	
	@SuppressWarnings("unchecked")
	private ReceiptHeader simulateProcessSuccessMsg(){
		EgwStatus receiptStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER, 
				CollectionConstants.RECEIPT_STATUS_DESC_PENDING);
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader(
				null, CollectionConstants.RECEIPT_TYPE_BILL, "CustomerID", "", 
				objectFactory.createUser("ONLINEUSER"),receiptStatus);
		
		
		String successMsg="MerchantID|"+receiptHeader.getReferencenumber()+"|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21-09-2009|0300|SettlementType|"+receiptHeader.getId()+"|AdditionalInfo2|AdditionalInfo3|AdditionalInfo4|"+
		"AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription";
		String checksum=PGIUtil.doDigest(successMsg,CollectionConstants.UNIQUE_CHECKSUM_KEY);
		
		successMsg+="|"+checksum;//String.valueOf(localCRC32.getValue());
		action.setMsg(successMsg);
		
		
		EgwStatus onlinePaytStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, 
				CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		OnlinePayment onlinePayt=objectFactory.createOnlinePayment(
				receiptHeader,String.valueOf(receiptHeader.getId()),
				new BigDecimal(1000.0),onlinePaytStatus);
		action.setServiceCode(onlinePayt.getService().getCode());
		
		EgwStatus instrNewStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_NEW_STATUS);
		
		InstrumentHeader onlineInstrumentHeader = new InstrumentHeader();
		onlineInstrumentHeader.setInstrumentType(financialsUtil.getInstrumentTypeByType(
				CollectionConstants.INSTRUMENTTYPE_ONLINE));
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = sdf.parse("21/09/2009");
		} catch (ParseException e) {

		}
		onlineInstrumentHeader.setTransactionDate(date);
		onlineInstrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
		onlineInstrumentHeader.setTransactionNumber("TxnReferenceNo");
		onlineInstrumentHeader.setInstrumentAmount(new BigDecimal(1000.0));
		onlineInstrumentHeader.setStatusId(instrNewStatus);
				
		session.saveOrUpdate(onlineInstrumentHeader);
		
		receiptHeader.addInstrument(onlineInstrumentHeader);
		receiptHeaderService.persist(receiptHeader);

		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		actualInstrList.add(onlineInstrumentHeader);

		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(onlineInstrumentHeader);

		expect(instrumentService.addToInstrument(isA(List.class))).andReturn(
				actualInstrList);
		replay(instrumentService);

		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS);

		expect(
				commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
						CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS)).andReturn(instrumentStatus);
		replay(commonsManager);

		CVoucherHeader voucherHeader = objectFactory
				.createVoucher("testVoucherHeader");
		expect(
				voucherCreator.createVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andReturn(
				voucherHeader);
		replay(voucherCreator);
		
		action.setMsg(successMsg);
		
		return receiptHeader;
	}
	
	@Test
	public void testCreate(){
		assertEquals(action.newform(), "new");
	}
	
	//@Test
	public void testTemporary(){
		
		ReceiptHeader receiptHeader = simulateProcessSuccessMsg();
		action.setTestReceiptId(receiptHeader.getId());
		action.setTestAuthStatusCode("0300");
		
		assertEquals(action.acceptMessageFromPaymentGateway(),"result");
		
		ReceiptHeader receiptSaved = receiptHeaderService.findById(receiptHeader.getId(), false);
		
		assertEquals(receiptSaved.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
		
		assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(), CollectionConstants.ONLINEPAYMENT_STATUS_DESC_SUCCESS);
		assertEquals(receiptSaved.getOnlinePayment().getAuthorisationStatusCode(), "0300");
		
		assertEquals(action.testOnlinePaytMsg(), "PaytGatewayTest");
	}
	
	
	@Test
	public void testProcessSuccessFromPaymentGateway(){
		ReceiptHeader receiptHeader = simulateProcessSuccessMsg();
		
		assertEquals(action.acceptMessageFromPaymentGateway(),"result");
		
		ReceiptHeader receiptSaved = receiptHeaderService.findById(receiptHeader.getId(), false);
		
		assertEquals(receiptSaved,action.getOnlinePaymentReceiptHeader());
		
		//IsReconciled flag is set to true upon updating the billing system
		receiptSaved.setIsReconciled(true);
		
		assertTrue(receiptSaved.getIsReconciled());
		assertEquals(receiptSaved.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
		
		assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(), CollectionConstants.ONLINEPAYMENT_STATUS_DESC_SUCCESS);
		assertEquals(receiptSaved.getOnlinePayment().getAuthorisationStatusCode(), "0300");
		
		assertEquals(action.getPaymentResponse().getReceiptId(),String.valueOf(receiptHeader.getId()));
		assertEquals(action.getPaymentResponse().getAuthStatus(),"0300");
	}
	
	@Test
	public void testProcessSuccessMsgBillingSystemUpdateFailure(){
		receiptService = new ReceiptService(){
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return false;
			}
		};
		receiptService.setType(ReceiptPayeeDetails.class);
		receiptService.setFinancialsUtil(financialsUtil);
		
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);

		collectionsUtil=new CollectionsUtil();
		collectionsUtil.setPersistenceService(genericService);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);
		
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		action.setReceiptPayeeDetailsService(receiptService);
		
		receiptHeaderService = new ReceiptHeaderService(){
			protected CVoucherHeader createVoucher(ReceiptHeader receiptHeader,Boolean receiptBulkUpload){
				throw new ApplicationRuntimeException("Update to financials Failed!");
			}
		};
		receiptHeaderService.setType(ReceiptHeader.class);
		receiptHeaderService.setFinancialsUtil(financialsUtil);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		action.setReceiptHeaderService(receiptHeaderService);
		
		ReceiptHeader receiptHeader = simulateProcessSuccessMsg();
		
		assertEquals(action.acceptMessageFromPaymentGateway(),"result");
		
		ReceiptHeader receiptSaved = receiptHeaderService.findById(receiptHeader.getId(), false);
		
		assertEquals(receiptSaved.getStatus().getCode(), action.getOnlinePaymentReceiptHeader().getStatus().getCode());
		assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(), 
				action.getOnlinePaymentReceiptHeader().getOnlinePayment().getStatus().getDescription());
		assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(), 
				CollectionConstants.ONLINEPAYMENT_STATUS_DESC_TO_BE_REFUNDED);
		// assertEquals(receiptSaved.getOnlinePayment().getAuthorisationStatusCode(), "0300");
		// assertEquals(action.getActionErrors().iterator().next(), "Receipt creation failed. Amount paid from your account will be refunded.");
	}
	
	@Test
	public void testView(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptnumber");
		action.setReceiptId(receiptHeader.getId());
		action.setSession(new HashMap<String, Object>());
		
		assertEquals(action.view(),CollectionConstants.REPORT);
		assertEquals(action.getReportId(),0);
		
		assertEquals(action.getReceipts()[0],receiptHeader);
	}
	
	@Test
	public void testViewReportGenError(){
		collectionCommon = new CollectionCommon(){
			 protected PaymentGatewayAdaptor getPaymentGatewayAdaptor(String serviceCode){
				 return new BillDeskAdaptor();
			 }
			 public Integer generateReport(ReceiptHeader[] receipts,
						Map<String, Object> session, boolean flag){
					throw new ApplicationRuntimeException("Report Gen Failure");
			 }
		};
		
		collectionCommon.setBoundaryDAO(boundaryDAO);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		collectionCommon.setCommonsManager(commonsManager);
		collectionCommon.setEgovCommon(egovCommon);
		collectionCommon.setPersistenceService(genericService);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		
		action.setCollectionCommon(collectionCommon);
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptnumber");
		action.setReceiptId(receiptHeader.getId());
		action.setSession(new HashMap<String, Object>());
		
		try{
		assertEquals(action.view(),CollectionConstants.REPORT);
		}
		catch(Exception e){
			assertEquals(e.getMessage(), CollectionConstants.REPORT_GENERATION_ERROR);
		}
		assertEquals(action.getReportId(),-1);
		
		assertEquals(action.getReceipts()[0],receiptHeader);
	}
	
	@Test
	public void testProcessFailureMsgFromPaymentGateway(){
		
		OnlinePayment onlinePayt=objectFactory.createOnlinePayment();
		action.setServiceCode(onlinePayt.getService().getCode());
		
		String successMsg="MerchantID|"+onlinePayt.getReceiptHeader().getReferencenumber()+"|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21-09-2009|0100|SettlementType|"+onlinePayt.getReceiptHeader().getId()+"|AdditionalInfo2|AdditionalInfo3|AdditionalInfo4|"+
		"AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription";
		CRC32 localCRC32 = new CRC32();
		localCRC32.update(successMsg.substring(0, successMsg.lastIndexOf('|')).getBytes());
		successMsg+=String.valueOf(localCRC32.getValue());
		String checksum=PGIUtil.doDigest(successMsg,CollectionConstants.UNIQUE_CHECKSUM_KEY);
		successMsg+="|"+checksum;
		action.setMsg(successMsg);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, 
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
		expectLastCall().andReturn(instrumentStatus);
		replay(commonsManager);
		
		
		ReceiptHeader receiptSaved = receiptHeaderService.findById(onlinePayt.getReceiptHeader().getId(), false);
		
		assertEquals(action.acceptMessageFromPaymentGateway(),"result");
		assertEquals(receiptSaved.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(),CollectionConstants.ONLINEPAYMENT_STATUS_DESC_FAILURE);
		assertEquals(receiptSaved.getOnlinePayment().getAuthorisationStatusCode(), "0100");
		assertTrue(action.getActionErrors().iterator().next().equals
				(onlinePayt.getService().getCode().toLowerCase() + ".pgi."+
						receiptSaved.getService().getCode().toLowerCase()+"."+onlinePayt.getAuthorisationStatusCode()));
		
	}
	
	//@Test
	public void testSaveNew() throws Exception{
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(true);
		
		EgwStatus status = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_DESC_PENDING);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		expectLastCall().andReturn(status);
		
		EgwStatus onlinePaytStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
		expectLastCall().andReturn(onlinePaytStatus);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
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
		
		UserImpl userImpl = (UserImpl) genericService.find(
				"from UserImpl U where U.userName =?",CollectionConstants.CITIZEN_USER_NAME);
		
		userManager.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);
		expectLastCall().andReturn(userImpl);
		replay(userManager);
		
		ServiceDetails onlinePaytService = objectFactory.createServiceDetails();
		
		action.setPaymentServiceId(onlinePaytService.getId().intValue());
		
		action.setPaymentAmount(BigDecimal.valueOf(1000));
		action.prepare();
		
		InstrumentHeader instrHeaderOnline = new InstrumentHeader();
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		instrHeaderOnline.setInstrumentAmount(action.getTotalAmountToBeCollected());
		instrHeaderOnline.setStatusId(instrumentStatus);
		instrHeaderOnline.setTransactionNumber("12345");
		instrHeaderOnline.setTransactionDate(new Date());
		instrHeaderOnline.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderOnline.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_ONLINE));
		instrHeaderOnline.setCreatedDate(new Date());
		instrHeaderOnline.setModifiedDate(new Date());
		instrHeaderOnline.setModifiedBy(objectFactory.createUser("testuser"));
		instrHeaderOnline.setCreatedDate(new Date());
		session.saveOrUpdate(instrHeaderOnline);
		actualInstrList.add(instrHeaderOnline);
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderOnline);
		
		
		Set<ReceiptDetail> receiptDetailListFromModel = expectedPayeeList.get(0).getReceiptHeaders().iterator().next().getReceiptDetails();
		List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();
		for(ReceiptDetail receiptDetailChange : receiptDetailListFromModel){
			ReceiptDetail receiptDetail = new ReceiptDetail();
			receiptDetail.setCramount(receiptDetailChange.getCramountToBePaid());
			receiptDetail.setDramount(BigDecimal.ZERO);
			receiptDetail.setOrdernumber(receiptDetailChange.getOrdernumber());
			receiptDetail.setReceiptHeader(receiptDetailChange.getReceiptHeader());
			receiptDetailList.add(receiptDetail);
		}
		action.setReceiptDetailList(receiptDetailList);
		
		assertEquals(action.saveNew(),"redirect");
		
		ReceiptHeader expectedReceipt = action.getModelPayeeList().get(0).getReceiptHeaders().iterator().next();
		assertTrue(expectedReceipt.getIsReconciled());
		assertEquals(expectedReceipt.getStatus().getDescription(), CollectionConstants.RECEIPT_STATUS_DESC_PENDING);
		assertEquals(expectedReceipt.getOnlinePayment().getStatus().getDescription(), CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		assertEquals(expectedReceipt.getReceiptnumber(), null);
		assertEquals(expectedReceipt.getOnlinePayment().getService(),onlinePaytService);
		assertEquals(expectedReceipt.getOnlinePayment().getService().getId().intValue(),
				action.getPaymentServiceId());
		
		PaymentRequest expectedPaymentRequest=new BillDeskAdaptor().createPaymentRequest(onlinePaytService, expectedReceipt);
		assertEquals(expectedPaymentRequest.getRequestParameters(),action.getPaymentRequest().getRequestParameters());
	}
	
	private List<ReceiptPayeeDetails> initialisePrepareAndModel(boolean valid){
		
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
		
		commonsManager.fundByCode("testFundCode");
		expectLastCall().andReturn(fund);
		
		commonsManager.getFundSourceByCode("testfundSourceCode");
		expectLastCall().andReturn(fundSource);
		
		commonsManager.getFunctionByCode("testFunctionCode1");
		expectLastCall().andReturn(function1);
		
		commonsManager.getCChartOfAccountsByGlCode("testGLCode1");
		expectLastCall().andReturn(account1);
				
		return createModelFromXML(service,boundary,fund,functionary,fundSource,dept,account1,function1);
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
							billAccount.getDrAmount(),billAccount.getCrAmount(),
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
	
	//@Test
	public void testPrepare(){
		
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(true);
		ReceiptPayeeDetails expectedPayee = expectedPayeeList.get(0);
		ReceiptHeader expectedReceiptHdr = expectedPayee.getReceiptHeaders().iterator().next();
		replay(commonsManager);
		
		UserImpl userImpl = (UserImpl) genericService.find(
				"from UserImpl U where U.userName =?",CollectionConstants.CITIZEN_USER_NAME);
		
		userManager.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);
		expectLastCall().andReturn(userImpl);
		replay(userManager);
				
		action.prepare();
		
		List<ReceiptPayeeDetails> actualPayeeList = (List<ReceiptPayeeDetails>) action.getModel();
		ReceiptPayeeDetails actualPayee = actualPayeeList.get(0);
		ReceiptHeader actualReceiptHdr = actualPayee.getReceiptHeaders().iterator().next();
		
		assertEquals(actualPayeeList.size(),1);
		assertEquals(expectedPayee.getPayeename(), actualPayee.getPayeename());
		assertEquals(expectedReceiptHdr.getTotalAmountToBeCollected(),
				actualReceiptHdr.getTotalAmountToBeCollected());
		assertEquals(actualPayeeList,action.getModelPayeeList());
		assertTrue(action.getOverrideAccountHeads());
		assertTrue(action.getPartPaymentAllowed());
		assertFalse(action.getCollectionModesNotAllowed().contains("online"));
		assertEquals(action.getServiceName(),"testServiceName");
	}
	
	@Test
	public void testGetTotalNoOfAccounts(){
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeForBillingSystem();
		action.getModelPayeeList().add(payee);
		
		assertEquals(action.getTotalNoOfAccounts(),1);
	}
	
	@Test
	public void testModel(){
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeForBillingSystem();
		List<ReceiptPayeeDetails> payeeList = new ArrayList<ReceiptPayeeDetails>();
		payeeList.add(payee);
		
		action.setModelPayeeList(payeeList);
		
		assertEquals(action.getModel(), action.getModelPayeeList());
	}
	
	//@Test
	public void testInvalidXML(){
		UserImpl userImpl = (UserImpl) genericService.find(
				"from UserImpl U where U.userName =?",CollectionConstants.CITIZEN_USER_NAME);
		
		userManager.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);
		expectLastCall().andReturn(userImpl);
		replay(userManager);
		
		String xml = "<bill-collect><test></bill-collect>";
		action.setCollectXML(xml);
		action.prepare();
		
		assertNotNull(action.getActionErrors());
		assertTrue(action.getActionErrors().contains("billreceipt.error.improperbilldata"));
	}
	
	//@Test
	public void testIncompleteXMLData(){
		List<ReceiptPayeeDetails> expectedPayeeList = initialisePrepareAndModel(false);
		ReceiptPayeeDetails expectedPayee = expectedPayeeList.get(0);
		expectedPayee.getReceiptHeaders().iterator().next();
		replay(commonsManager);
		
		UserImpl userImpl = (UserImpl) genericService.find(
				"from UserImpl U where U.userName =?",CollectionConstants.CITIZEN_USER_NAME);
		
		userManager.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);
		expectLastCall().andReturn(userImpl);
		replay(userManager);
		
		action.prepare();
		assertNotNull(action.getActionErrors());
		assertTrue(action.getActionErrors().contains("billreceipt.improperbilldata.missingfund"));
		assertTrue(action.getActionErrors().contains("billreceipt.improperbilldata.missingdepartment"));
		
	}
	
	@Test
	public void testReconcileOnlinePayment(){
		simulateReconcileOnlinePayments();
		
		assertEquals("reconresult", action.reconcileOnlinePayment());
		
		ReceiptHeader actualReceipt1 = (ReceiptHeader) genericService.find(
				"from org.egov.collection.entity.ReceiptHeader where id=?",action.getSelectedReceipts()[0]);
		ReceiptHeader actualReceipt2 = (ReceiptHeader) genericService.find(
				"from org.egov.collection.entity.ReceiptHeader where id=?",action.getSelectedReceipts()[1]);
		ReceiptHeader actualReceipt3 = (ReceiptHeader) genericService.find(
				"from org.egov.collection.entity.ReceiptHeader where id=?",action.getSelectedReceipts()[2]);
		
		assertEquals(actualReceipt1.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
		assertEquals(actualReceipt2.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_FAILED);
		assertEquals(actualReceipt3.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_FAILED);
		
		assertEquals(actualReceipt1.getOnlinePayment().getStatus().getCode(),action.getStatusCode()[0]);
		assertEquals(actualReceipt2.getOnlinePayment().getStatus().getCode(),action.getStatusCode()[1]);
		assertEquals(actualReceipt3.getOnlinePayment().getStatus().getCode(),action.getStatusCode()[2]);
		
		assertEquals(actualReceipt1.getOnlinePayment().getTransactionNumber(),action.getTransactionId()[0]);
		assertEquals(actualReceipt2.getOnlinePayment().getTransactionNumber(),action.getTransactionId()[1]);
		assertEquals(actualReceipt3.getOnlinePayment().getTransactionNumber(),action.getTransactionId()[2]);
	}
	
	@Test
	public void testReconcileOnlinePaymentSystemsUpdateFailure(){
		receiptHeaderService = new ReceiptHeaderService(){
			public CVoucherHeader createVoucherForReceipt(ReceiptHeader receiptHeader,Boolean receiptBulkUpload)
			throws ApplicationRuntimeException {
				throw new ApplicationRuntimeException("Receipt Voucher Creation Exception!");
			}
		};
		receiptHeaderService.setType(ReceiptHeader.class);
		receiptHeaderService.setFinancialsUtil(financialsUtil);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		receiptService = new ReceiptService(){
			public Boolean updateBillingSystem(String serviceCode,Set<BillReceiptInfo> billReceipts){
				return false;
			}
		};
		receiptService.setType(ReceiptPayeeDetails.class);
		receiptService.setFinancialsUtil(financialsUtil);
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		
		
		action.setReceiptHeaderService(receiptHeaderService);
		action.setReceiptPayeeDetailsService(receiptService);
		
		EgwStatus receiptStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER, 
				CollectionConstants.RECEIPT_STATUS_DESC_PENDING);

		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader(
				"testReceiptNo1", CollectionConstants.RECEIPT_TYPE_BILL, "CustomerID1", "", 
				objectFactory.createUser("ONLINEUSER"),receiptStatus);

		EgwStatus onlinePaytStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, 
				CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		
		OnlinePayment onlinePayt=objectFactory.createOnlinePayment(
				receiptHeader,"12345", new BigDecimal(1000.0),onlinePaytStatus);

		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		expect(instrumentService.addToInstrument(isA(List.class))).andReturn(
				actualInstrList).anyTimes();
		replay(instrumentService);

		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED);

		expect(
				commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
						CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS)).andReturn(instrumentStatus);

		replay(commonsManager);

		CVoucherHeader voucherHeader = objectFactory
				.createVoucher("testVoucherHeader");
		expect(voucherCreator.createVoucher(isA(HashMap.class),
				isA(List.class), isA(List.class))).andReturn(
				voucherHeader).anyTimes();
		replay(voucherCreator);
		
		action.setSelectedReceipts(new Long[]{receiptHeader.getId()});
		action.setStatusCode(new String[]{CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS});
		action.setTransactionId(new String[]{"123456"});
		action.setTransactionDate(new String[]{"22/06/2010"});
		action.setRemarks(new String[]{"Transacation success"});
		
		try{
			action.reconcileOnlinePayment();
		}
		catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getMessage(),"Manual Reconciliation Rolled back as Voucher Creation Failed For Payment Reference ID : " + receiptHeader.getId());
			assertEquals(errors.get(1).getMessage(),"Receipt creation transaction rolled back as " +
					"update to billing system failed.");
		}
	}
	
	private void simulateReconcileOnlinePayments(){
		EgwStatus receiptStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER, 
				CollectionConstants.RECEIPT_STATUS_DESC_PENDING);
		
		ReceiptHeader receiptHeader1 = objectFactory.createReceiptHeader(
				"testReceiptNo1", CollectionConstants.RECEIPT_TYPE_BILL, "CustomerID1", "", 
				objectFactory.createUser("ONLINEUSER"),receiptStatus);
		
		ReceiptHeader receiptHeader2 = objectFactory.createReceiptHeader(
				"testReceiptNo2", CollectionConstants.RECEIPT_TYPE_BILL, "CustomerID2", "", 
				objectFactory.createUser("ONLINEUSER"),receiptStatus);
		
		ReceiptHeader receiptHeader3 = objectFactory.createReceiptHeader(
				"testReceiptNo3", CollectionConstants.RECEIPT_TYPE_BILL, "CustomerID2", "", 
				objectFactory.createUser("ONLINEUSER"),receiptStatus);
		
		EgwStatus onlinePaytStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, 
				CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		
		OnlinePayment onlinePayt1=objectFactory.createOnlinePayment(
				receiptHeader1,String.valueOf(receiptHeader1.getId()),
				new BigDecimal(1000.0),onlinePaytStatus);
		OnlinePayment onlinePayt2=objectFactory.createOnlinePayment(
				receiptHeader2,"",
				new BigDecimal(1000.0),onlinePaytStatus);
		OnlinePayment onlinePayt3=objectFactory.createOnlinePayment(
				receiptHeader3,"",
				new BigDecimal(1000.0),onlinePaytStatus);
		
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		expect(instrumentService.addToInstrument(isA(List.class))).andReturn(
				actualInstrList).anyTimes();
		replay(instrumentService);
		
		EgwStatus instrumentStatus1 = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS);
		
		EgwStatus instrumentStatus2 = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED);
		EgwStatus instrumentStatus3 = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED);

		expect(
				commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
						CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS)).andReturn(instrumentStatus1);
		expect(
				commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
						CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED)).andReturn(instrumentStatus2);
		expect(
				commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
						CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED)).andReturn(instrumentStatus3);
		replay(commonsManager);

		CVoucherHeader voucherHeader = objectFactory
				.createVoucher("testVoucherHeader");
		expect(
				voucherCreator.createVoucher(isA(HashMap.class),
						isA(List.class), isA(List.class))).andReturn(
				voucherHeader).anyTimes();
		replay(voucherCreator);
		
		action.setSelectedReceipts(new Long[]{receiptHeader1.getId(),receiptHeader2.getId(),receiptHeader3.getId()});
		action.setStatusCode(new String[]{CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED,
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED});
		action.setTransactionId(new String[]{"123456","",""});
		action.setTransactionDate(new String[]{"22/06/2010","",""});
		action.setRemarks(new String[]{"Transacation success","Payment has been refunded","Payment to be refunded"});
	}
	
	@Test
	public void testProcessWaitingFromPaymentGateway(){
		OnlinePayment onlinePayt = objectFactory.createOnlinePayment();
		action.setServiceCode(onlinePayt.getService().getCode());
		
		String successMsg = "MerchantID|"+onlinePayt.getReceiptHeader().getReferencenumber()+"|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
		"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
		"21-09-2009|0002|SettlementType|"+onlinePayt.getReceiptHeader().getId()+"|AdditionalInfo2|AdditionalInfo3|AdditionalInfo4|"+
		"AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription";

		String checksum = PGIUtil.doDigest(successMsg,CollectionConstants.UNIQUE_CHECKSUM_KEY);
		successMsg+="|" + checksum;
		action.setMsg(successMsg);
		
		EgwStatus paymentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
		
		commonsManager.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_ONLINEPAYMENT, 
				CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
		expectLastCall().andReturn(paymentStatus);
		replay(commonsManager);
		
		
		ReceiptHeader receiptSaved = receiptHeaderService.findById(onlinePayt.getReceiptHeader().getId(), false);
		
		assertEquals(action.acceptMessageFromPaymentGateway(),"result");
		assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(),CollectionConstants.ONLINEPAYMENT_STATUS_DESC_PENDING);
		assertEquals(receiptSaved.getOnlinePayment().getAuthorisationStatusCode(), "0002");
		
	}
	
	@Test
		public void testProcessResponseMessageForFailure(){
			
			OnlinePayment onlinePayt=objectFactory.createOnlinePayment();
			action.setServiceCode(onlinePayt.getService().getCode());
			
			String successMsg="MerchantID|"+onlinePayt.getReceiptHeader().getReferencenumber()+"|TxnReferenceNo|BankReferenceNo|1000.0|BankID|"+
			"BankMerchantID|TxnType|CurrencyName|ItemCode|SecurityType|SecurityID|SecurityPassword|"+
			"21-09-2009|0100|SettlementType|"+onlinePayt.getReceiptHeader().getId()+"|AdditionalInfo2|AdditionalInfo3|AdditionalInfo4|"+
			"AdditionalInfo5|AdditionalInfo6|AdditionalInfo7|ErrorStatus|ErrorDescription";
			String checksum=PGIUtil.doDigest(successMsg,CollectionConstants.UNIQUE_CHECKSUM_KEY);
			successMsg+="|"+checksum;
			action.setMsg(successMsg);
			
			EgwStatus instrumentStatus = (EgwStatus) genericService.find(
					"from EgwStatus S where S.moduletype =? and S.code =?",
					CollectionConstants.MODULE_NAME_ONLINEPAYMENT, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
			
			commonsManager.getStatusByModuleAndCode(
					CollectionConstants.MODULE_NAME_ONLINEPAYMENT, 
					CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
			expectLastCall().andReturn(instrumentStatus);
			replay(commonsManager);
			
			
			ReceiptHeader receiptSaved = receiptHeaderService.findById(onlinePayt.getReceiptHeader().getId(), false);
			
			assertEquals(action.processResponseMessage().getContentLength(), new StreamResult(new ByteArrayInputStream("FAILURE|NA".getBytes())).getContentLength());
			assertEquals(receiptSaved.getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
			assertEquals(receiptSaved.getOnlinePayment().getStatus().getDescription(),CollectionConstants.ONLINEPAYMENT_STATUS_DESC_FAILURE);
			assertEquals(receiptSaved.getOnlinePayment().getAuthorisationStatusCode(), "0100");
			assertTrue(action.getActionErrors().iterator().next().equals
					(onlinePayt.getService().getCode().toLowerCase() + ".pgi."+
							receiptSaved.getService().getCode().toLowerCase()+"."+onlinePayt.getAuthorisationStatusCode()));
			
		}
*/}
