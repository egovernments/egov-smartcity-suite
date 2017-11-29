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



public class ChallanActionTest {/* extends AbstractPersistenceServiceTest<Challan,Long>{
	private ChallanAction action;
	private CollectionObjectFactory objectFactory;
	private @Autowired AppConfigValuesDAO appConfigValuesDAO;
	private CommonsManager commonsManager;
	private BoundaryDAO boundaryDAO;
	
	private ReceiptService receiptService;
	private ChallanService challanService;
	private WorkflowService<Challan> challanWorkflowService;
	
	private CollectionsUtil collectionsUtil;
	private FinancialsUtil financialsUtil;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private UserManager userManager;
	private EisManager eisManager;
	private InstrumentService instrumentService;
	private EgovCommon egovCommon;
	
	private Position position;
	private CollectionCommon collectionCommon;
	private ReceiptHeaderService receiptHeaderService;
	private EisUtilService eisUtilService;
	private EisCommonsManager eisCommonsManagerMock;
	private AuditEventService auditEventService; 
	//WorkflowService<ReceiptHeader> receiptWorkflowServiceMock;
	
	@Before
	public void setupAction(){
		action = new ChallanAction(){
			@Override
			protected void setValue(String relationshipName, Object relation) {
				try {
					Ognl.setValue(relationshipName, action.getModel(), relation);
				} catch (OgnlException e) {
					throw new RuntimeException(e);
				}
			}
			 @Override
			 public String getText(String textName)
			 {
				 return textName;
			 }
			 
			 @Override
			 public String getText(String textName,String[] args)
			 {
				 return textName;
			 }
			 
			 @Override
			 public String getText(String textName,String args)
			 {
				 return args;
			 }
		};
		genericDao = new GenericHibernateDaoFactory(){
			protected Session getCurrentSession(){
				return session;
			}
			public  AppConfigValuesDAO getAppConfigValuesDAO()
			{
				return new AppConfigValuesHibernateDAO(AppConfigValues.class,session);
			}
		};
		
		objectFactory = new CollectionObjectFactory(session);
		
		commonsManager = createMock(CommonsManager.class);
		userManager = createMock(UserManager.class);
		boundaryDAO = createMock(BoundaryDAO.class);
		eisManager = createMock(EisManager.class);
		egovCommon = createMock(EgovCommon.class);
		eisCommonsManagerMock = createMock(EisCommonsManager.class);
		auditEventService =  new AuditEventService();
		ApplicationThreadLocals.setUserId("1");
		genericService.setType(AuditEvent.class);
		auditEventService.setAuditEventPersistenceService(genericService);
		
				
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);

		position = objectFactory.createPosition();
		
		collectionsUtil=new CollectionsUtil(){
			public Position getPositionOfUser(User user) {
				return position;
			}
			public Position getPositionByName(String positionName) {
				return position;
			}
			public List<Department> getDepartmentsAllowedForChallaApproval(
					User loggedInUser,ReceiptHeader receiptHeaderObj){
				return new ArrayList<Department>();
			}
		};
		collectionsUtil.setGenericDao(genericDao);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setEisManager(eisManager);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setEisCommonsManager(eisCommonsManagerMock);
		collectionsUtil.setAuditEventService(auditEventService);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);		
		
		instrumentService = createMock(InstrumentService.class);
		
		financialsUtil = new FinancialsUtil(){
			public CVoucherHeader getReversalVoucher(
					List<HashMap<String, Object>> paramList){
				return new CVoucherHeader();
			}
			public InstrumentType getInstrumentTypeByType(String type){
				return (InstrumentType) genericService.find("from InstrumentType  where type=? and isActive=true",type);
			}
			public CVoucherHeader createVoucher(
					Map<String, Object> headerdetails,
					List<HashMap<String, Object>> accountcodedetails,
					List<HashMap<String, Object>> subledgerdetails,Boolean receiptBulkUpload){
				return objectFactory.createVoucher("testVoucher");
			}
			public List<InstrumentVoucher> updateInstrument(List<Map<String, Object>> paramList) {
				return new ArrayList<InstrumentVoucher>();
			}
		};
		financialsUtil.setInstrumentService(instrumentService);
		
		receiptService = new ReceiptService();		
		receiptService.setType(ReceiptPayeeDetails.class);
		receiptService.setCollectionsUtil(collectionsUtil);
		receiptService.setFinancialsUtil(financialsUtil);
		receiptService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		eisUtilService=new EisUtilService();
		eisUtilService.setPersistenceService(genericService);
		receiptService.setEisService(eisUtilService);
		challanService = new ChallanService();
		challanService.setType(Challan.class);
		challanService.setCollectionsUtil(collectionsUtil);
		
		challanWorkflowService = new SimpleWorkflowService<Challan>(
				challanService, Challan.class);
		challanService.setChallanWorkflowService(challanWorkflowService);
		
		receiptHeaderService = new ReceiptHeaderService();
		receiptHeaderService.setType(ReceiptHeader.class);
		receiptHeaderService.setPersistenceService(genericService);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		
		receiptWorkflowServiceMock = new SimpleWorkflowService<ReceiptHeader>(
				receiptHeaderService, ReceiptHeader.class);
		receiptHeaderService.setReceiptWorkflowService(receiptWorkflowServiceMock);
		receiptHeaderService.setFinancialsUtil(financialsUtil);
		
		collectionCommon = new CollectionCommon();
		collectionCommon.setPersistenceService(genericService);
		collectionCommon.setEgovCommon(egovCommon);
		collectionCommon.setCommonsManager(commonsManager);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		collectionCommon.setReceiptHeaderService(receiptHeaderService);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		

		action.setPersistenceService(genericService);
		action.setCommonsManager(commonsManager);
		action.setBoundaryDAO(boundaryDAO);
		action.setCollectionsUtil(collectionsUtil);
		action.setFinancialsUtil(financialsUtil);
		action.setReceiptPayeeDetailsService(receiptService);
		action.setChallanService(challanService);
		action.setCollectionCommon(collectionCommon);
		action.setReceiptHeaderService(receiptHeaderService);
		action.setChallanWorkflowService(challanWorkflowService);
	
	}
	
	private void initialiseActionForSaveNew(){
		Department dept = objectFactory.createDept("testDept");
		BoundaryImpl boundary = objectFactory.createBoundary();
		
		action.setDeptId(String.valueOf(dept.getId()));
		action.setBoundaryId(boundary.getId());
		
		ReceiptHeader header = objectFactory.createUnsavedReceiptHeader();
		header.setReceiptnumber(null);
		header.setCreatedBy(objectFactory.createUser("testUser"));
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		header.setReceiptPayeeDetails(payee);
		Challan challan = objectFactory.createUnsavedChallan();
		challan.setCreatedBy(null);
		header.setChallan(challan);
		//CChartOfAccounts account=objectFactory.createCOA("1201001");
		CFunction functionObj = objectFactory.createFunction("testfunction");  
		CFinancialYear financialYear=objectFactory.getFinancialYearForDate(new Date());
		ReceiptMisc receiptMisc = header.getReceiptMisc();
		
		commonsManager.fundById(receiptMisc.getFund().getId());
		expectLastCall().andReturn(receiptMisc.getFund());
		
		commonsManager.getCChartOfAccountsByGlCode(EasyMock.isA(String.class));
		expectLastCall().andReturn(account).anyTimes();
		
		commonsManager.getFunctionByCode(EasyMock.isA(String.class));
		expectLastCall().andReturn(functionObj).anyTimes();

		commonsManager.getFinancialYearById(EasyMock.isA(Long.class));
		expectLastCall().andReturn(financialYear).anyTimes();
		
		boundaryDAO.getBoundary(boundary.getId());
		expectLastCall().andReturn(boundary);
		replay(boundaryDAO);
				
		action.setReceiptHeader(header);
	}
	
	private void createEmployeeForLoggedInUser(){
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		Department dept = objectFactory.createDeptForCode("testDeptCode");
		UserImpl user = objectFactory.createUser("system",dept);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user).anyTimes();
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,dept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(dept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,dept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);
	}
	
	//@Test
	public void testCreate() {
		ReceiptHeader header=new ReceiptHeader();
		Challan challan=new Challan();
		header.setChallan(challan);
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		
		UserImpl user = objectFactory.createUser("system",loggedinUserDept);
		userManager.getUserByUserName(EasyMock.isA(String.class));
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,loggedinUserDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(loggedinUserDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,loggedinUserDept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);
		
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME, user.getUserName());
		action.setSession(sessionMap);
		action.setReceiptHeader(header);
		
		assertEquals(action.newform(),"new");
	}
	
	@Test
	public void testPrepare() {
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		
		UserImpl user = objectFactory.createUser("system",loggedinUserDept);
		userManager.getUserByUserName(EasyMock.isA(String.class));
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,loggedinUserDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(loggedinUserDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,loggedinUserDept);
		
		eisManager.getEmpForUserId(EasyMock.isA(Integer.class));
		expectLastCall().andReturn(emp);
		
		eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class));
		expectLastCall().andReturn(assignment);
		replay(eisManager);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME, user.getUserName());
		action.setSession(sessionMap);
		
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderForChallan();
		action.setReceiptId(receiptHeader.getId());
		
		List<Location> actualFundList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUND);
		List<InstrumentType> actualDeptList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS);
		List<InstrumentType> actualFieldList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FIELD);
		List<InstrumentType> actualServiceList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_CHALLAN_SERVICES,CollectionConstants.CHALLAN_SERVICE_TYPE);
		List<InstrumentType> actualFinYrList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_ACTIVE_FINANCIAL_YEAR);
		//action.setSourcePage("dummyPage");
		action.setActionName("dummyAction");
		
		action.prepare();
		
		//assertEquals("dummyPage",action.getSourcePage());
		assertEquals("dummyAction",action.getActionName());
		
		assertEquals(actualFundList,action.getDropdownData().get("fundList"));
		assertEquals(actualDeptList,action.getDropdownData().get("departmentList"));
		assertEquals(actualFieldList,action.getDropdownData().get("fieldList"));
		assertEquals(actualServiceList,action.getDropdownData().get("serviceList"));
		assertEquals(actualFinYrList,action.getDropdownData().get("financialYearList"));
		
		List<String> mandatoryFields = action.getMandatoryFields();
		List<String> headerFields = action.getHeaderFields();
		
		for(String mandatory : mandatoryFields){
			assertTrue(action.isFieldMandatory(mandatory));
		}
		
		for(String header : headerFields){
			assertTrue(action.shouldShowHeaderField(header));
		}
		
		ReceiptHeader model = action.getReceiptHeader();
		assertEquals(receiptHeader, model);
	}
	
	@Test
	public void testModel(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptNumber");
		receiptHeader.setReceiptMisc(null);
		action.setReceiptHeader(receiptHeader);
		
		assertEquals(action.getReceiptHeader(), action.getModel());
	}
	
	//@Test
	public void testSaveNewWithInvalidVoucherNumber(){
		initialiseActionForSaveNew();
		action.setBillDetailslist(objectFactory.createCreditDetailslist());
		action.setSubLedgerlist(objectFactory.createEmptySubLedgerlist());
		
		action.setPositionUser(-1);
		
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		
		UserImpl user = objectFactory.createUser("system",loggedinUserDept);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,loggedinUserDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(loggedinUserDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,loggedinUserDept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);

		
		objectFactory.createEmployeePositionDetails("testDesignation", emp,	loggedinUserDept);
		
		action.setVoucherNumber("dummyVoucherNumber");
		
		action.setActionName(CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		ReceiptHeader receiptHeaderObj=action.getReceiptHeader();
		receiptHeaderObj.getChallan().setState(objectFactory.createState(user));
		action.setReceiptHeader(receiptHeaderObj);
		
		try{
		assertEquals(action.save(),"view");
		}
		catch(Exception e){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"challan.invalid.vouchernumber");
			assertEquals(errors.get(0).getMessage(),"Voucher not found. Please check the voucher number.");
		}
	}
	
	//@Test
	public void testSaveNew(){
		initialiseActionForSaveNew();
		List<ReceiptDetailInfo> billCreditDetailslist = objectFactory.createCreditDetailslist();
		action.setBillDetailslist(billCreditDetailslist);
		action.setSubLedgerlist(objectFactory.createEmptySubLedgerlist());
		
		String glcodeDetail = billCreditDetailslist.get(0).getGlcodeDetail();
		CChartOfAccounts account=objectFactory.createCOA(glcodeDetail);
		CFunction function=objectFactory.createFunction("testFunction");
		commonsManager.getCChartOfAccountsByGlCode(glcodeDetail);
		expectLastCall().andReturn(account);
		commonsManager.getFunctionById(billCreditDetailslist.get(0).getFunctionIdDetail());
		expectLastCall().andReturn(function);
		replay(commonsManager);
		action.setPositionUser(-1);
		
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		
		UserImpl user = objectFactory.createUser("system",loggedinUserDept);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,loggedinUserDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(loggedinUserDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,loggedinUserDept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);
		
		objectFactory.createEmployeePositionDetails("testDesignation", emp,	loggedinUserDept);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		CVoucherHeader voucher = objectFactory.createVoucher("testVoucher");
		action.setVoucherNumber(voucher.getVoucherNumber());
		
		action.setActionName(CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN);
		ReceiptHeader receiptHeaderObj=action.getReceiptHeader();
		receiptHeaderObj.getChallan().setState(objectFactory.createState(user));
		action.setReceiptHeader(receiptHeaderObj);
		assertEquals(action.save(),"view");
		
		ReceiptHeader receiptHeader = action.getReceiptHeader();
		
		assertEquals(receiptHeader.getReceiptMisc().getDepartment().getId().toString(), 
				action.getDeptId());
		assertEquals(receiptHeader.getReceiptMisc().getBoundary().getId(), 
				action.getBoundaryId());
		assertEquals(receiptHeader.getStatus().getCode(), 
				CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		assertEquals(receiptHeader.getReceiptnumber(), 
				null);
		assertTrue(receiptHeader.getIsReconciled());
		assertEquals(receiptHeader.getReceipttype(), 
				CollectionConstants.RECEIPT_TYPE_CHALLAN);
		assertEquals(receiptHeader.getService().getCode(), 
				CollectionConstants.SERVICE_CODE_COLLECTIONS);
		
		assertEquals(receiptHeader.getChallan().getStatus().getCode(),
				CollectionConstants.CHALLAN_STATUS_CODE_CREATED);
		
		assertEquals(receiptHeader.getId(), action.getReceiptId());
		assertEquals(receiptHeader.getChallan().getVoucherHeader().getVoucherNumber(), action.getVoucherNumber());
		
		assertEquals(action.errors.size(),0);
	}
	
	//@Test
	public void testChallanWorkflow(){
		
		initialiseActionForSaveNew();
		List<ReceiptDetailInfo> billCreditDetailslist = objectFactory.createCreditDetailslist();
		action.setBillDetailslist(billCreditDetailslist);
		action.setSubLedgerlist(objectFactory.createEmptySubLedgerlist());
		
		String glcodeDetail = billCreditDetailslist.get(0).getGlcodeDetail();
		CChartOfAccounts account=objectFactory.createCOA(glcodeDetail);
		CFunction function=objectFactory.createFunction("testFunction");
		commonsManager.getCChartOfAccountsByGlCode(glcodeDetail);
		expectLastCall().andReturn(account);
		commonsManager.getFunctionById(billCreditDetailslist.get(0).getFunctionIdDetail());
		expectLastCall().andReturn(function);
		replay(commonsManager);
		action.setPositionUser(-1);
		
		
		createEmployeeForLoggedInUser();
		
		action.setSubLedgerlist(objectFactory.createEmptySubLedgerlist());
		
		action.setActionName(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN);
		action.setApprovalRemarks("Test Comment - Challan Validated.");
		assertEquals(action.save(),BaseFormAction.SUCCESS);
		
		State challanState = action.getReceiptHeader().getChallan().getState();
		
		assertEquals(State.END, challanState.getValue());
		assertEquals(CollectionConstants.WF_STATE_VALIDATE_CHALLAN, challanState.getPrevious().getValue());
		assertEquals("End of challan worklow", challanState.getText1());
		assertEquals(action.getApprovalRemarks(), challanState.getPrevious().getText1());
		assertEquals(CollectionConstants.CHALLAN_STATUS_CODE_VALIDATED, action.getReceiptHeader().getChallan().getStatus().getCode());
	}
	
	@Test
	public void testInvalidBillDetails(){

		List<ReceiptDetailInfo> billDetailsList = new ArrayList<ReceiptDetailInfo>();
		List<ReceiptDetailInfo> subledgerList = objectFactory.createEmptySubLedgerlist();
		
		// test if no data is entered
		billDetailsList.add(objectFactory.createReceiptDetailInfo(BigDecimal.ZERO, BigDecimal.ZERO, ""));
		try{
			action.setBillDetailslist(billDetailsList);
			action.setSubLedgerlist(subledgerList);
			action.validateAccountDetails();//(billDetailsList, subledgerList);
		}
		catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"challan.accdetail.emptyaccrow");
			assertEquals(errors.get(0).getMessage(),"No data entered in Account Details grid row : {0}");
		}
		
		// test if neither credit and amount data are entered
		action.errors.clear();
		billDetailsList.clear();
		billDetailsList.add(objectFactory.createReceiptDetailInfo(BigDecimal.ZERO, BigDecimal.ZERO, "testGLCode"));
		
		try{
			action.setBillDetailslist(billDetailsList);
			action.setSubLedgerlist(subledgerList);
			action.validateAccountDetails();//(billDetailsList, subledgerList);
		}
		catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals("challan.accdetail.amountZero",errors.get(0).getKey());
			assertEquals("Enter debit/credit amount for the account code : {0}",errors.get(0).getMessage());
			
		}
		
		// test if both credit and amount data are entered
		action.errors.clear();
		billDetailsList.clear();
		billDetailsList.add(objectFactory.createReceiptDetailInfo(BigDecimal.TEN, BigDecimal.TEN, "testGLCode"));
		
		try{
			action.setBillDetailslist(billDetailsList);
			action.setSubLedgerlist(subledgerList);
			action.validateAccountDetails();//(billDetailsList, subledgerList);
		}
		catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"challan.accdetail.amount");
			assertEquals(errors.get(0).getMessage(),"Please enter either debit/credit amount for the account code : {0}");
		}
		
		// test if credit/amount data are valid, but glcode is missing
		action.errors.clear();
		billDetailsList.clear();
		billDetailsList.add(objectFactory.createReceiptDetailInfo(BigDecimal.TEN, BigDecimal.ZERO, ""));
		
		try{
			action.setBillDetailslist(billDetailsList);
			action.setSubLedgerlist(subledgerList);
			action.validateAccountDetails();//(billDetailsList, subledgerList);
		}
		catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"challan.accdetail.accmissing");
			assertEquals(errors.get(0).getMessage(),"Account code is missing for credit/debit supplied field in account grid row :{0}");
		}
	}
	
	@Test
	public void testInvalidSubledgerDetails(){
		CChartOfAccounts account = objectFactory.createCOAForGLCode("testGLCODE");
		Accountdetailtype accountDetailType = objectFactory.createAccountdetailtype("testAccDetailTypesghsdgh");
		CChartOfAccountDetail chartOfAccDetail = objectFactory.createCOADetail(
				account, accountDetailType);
		
		List<ReceiptDetailInfo> billDetailsList = objectFactory.createCreditDetailslist(account);
		List<ReceiptDetailInfo> subLedgerList = objectFactory.createSubLedgerlist(account,accountDetailType);
		
		
		// test 1 - no errors
		action.setBillDetailslist(billDetailsList);
		action.setSubLedgerlist(subLedgerList);
		action.validateAccountDetails();
		List<ValidationError> errors = action.errors;
		assertEquals(errors.size(),0);
		
		//test 2
		errors.clear();
		//set same combination of "glcode id + detail type id + detail key id" for both subledger records.
		subLedgerList.get(0).getGlcode().setId(subLedgerList.get(1).getGlcode().getId());// = "";//subLedgerList.get(1).getGlcode().getId();
		subLedgerList.get(0).getDetailType().setId(subLedgerList.get(1).getDetailType().getId());
		subLedgerList.get(0).setDetailKeyId(subLedgerList.get(1).getDetailKeyId());
		
		//set different amount values for account details and subledger details
		billDetailsList.get(0).setCreditAmountDetail(new BigDecimal(1000));
		
		action.setBillDetailslist(billDetailsList);
		action.setSubLedgerlist(subLedgerList);
		try{
		action.validateAccountDetails();
		}
		catch(Exception ex){
			errors = action.errors;
			assertEquals(errors.size(),2);
			assertEquals(errors.get(0).getKey(),"miscreciept.samesubledger.repeated");
			assertEquals(errors.get(0).getMessage(),"Same subledger should not allow for same account code");
			
			assertEquals(errors.get(1).getKey(),"miscreciept.samesubledger.entrymissing");
			assertEquals(errors.get(1).getMessage(),"Total subledger amount is not matching for account code : {0}");
		}
		
		//test 3
		errors.clear();
		//reset credit amount value back to initial value
		billDetailsList.get(0).setCreditAmountDetail(new BigDecimal(100));
		subLedgerList.get(0).setAmount(null);
		subLedgerList.get(1).setAmount(null);
		action.setSubLedgerlist(subLedgerList);
		try{
			action.validateAccountDetails();
		}
		catch(Exception ex){
			errors = action.errors;
			//assertEquals(errors.size(),2);
			assertEquals(errors.get(0).getKey(),"miscreciept.samesubledger.repeated");
			assertEquals(errors.get(0).getMessage(),"Same subledger should not allow for same account code");
			
			assertEquals(errors.get(1).getKey(),"miscreciept.samesubledger.entrymissing");
			assertEquals(errors.get(1).getMessage(),"Subledger detail entry is missing for account code: {0}");
		}
	}
	
	@Test
	public void testCreateReceipt(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithChallan();
		EgwStatus status = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		receiptHeader.setStatus(status);
		
		State prevstate = objectFactory.createState("Type-Challan","VALIDATED");
		State endstate = objectFactory.createState("Type-Challan","END");
		receiptHeader.getChallan().changeState(prevstate);
		receiptHeader.getChallan().changeState(endstate);
		
		session.saveOrUpdate(receiptHeader);
		
		action.setChallanNumber(receiptHeader.getChallan().getChallanNumber());
		
		createEmployeeForLoggedInUser();
		
		assertEquals("createReceipt", action.createReceipt());
		
		assertFalse(action.getCashAllowed());
		assertFalse(action.getCardAllowed());
		assertTrue(action.getChequeDDAllowed());
		
		assertEquals(action.getChallanNumber(),action.getReceiptHeader().getChallan().getChallanNumber());
	}
	
	@Test
	public void testCreateReceiptFailure(){
		
		action.setChallanNumber("testChallanNo");
		
		try{
		assertEquals("createReceipt", action.createReceipt());
		}catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"challan.notfound.message");
			assertEquals(errors.get(0).getMessage(),"No Valid Challan Found. Please check the challan number.");
		}
	}
	
	@Test
	public void testCreateExistingReceipt(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithChallan();
		
		State prevstate = objectFactory.createState("Type-Challan","VALIDATED");
		State endstate = objectFactory.createState("Type-Challan","END");
		receiptHeader.getChallan().changeState(prevstate);
		receiptHeader.getChallan().changeState(endstate);
		session.saveOrUpdate(receiptHeader);
		
		action.setChallanNumber(receiptHeader.getChallan().getChallanNumber());
		
		try{
		assertEquals("createReceipt", action.createReceipt());
		}catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"challanreceipt.created.message");
			assertEquals(errors.get(0).getMessage(),"Receipt Already Created For this Challan. Receipt Number is " + receiptHeader.getReceiptnumber());
		}
	}
	
	//@Test
	public void testSaveNewWithsubledger(){
		initialiseActionForSaveNew();
		
		// should pass cchartofaccount to this method. then glcodedetail = account.getGlcode() and glcodeiddetail=account.getid
		List<ReceiptDetailInfo> billDetailslist = objectFactory.createCreditDetailslist();
		
		String glcodeDetail = billDetailslist.get(0).getGlcodeDetail();
		CChartOfAccounts account=objectFactory.createCOAForGLCode(glcodeDetail);
		Accountdetailtype accountDetailType = objectFactory.createAccountdetailtype("testAccDetailTypesghsdgh");
		objectFactory.createCOADetail(account, accountDetailType);
		
		billDetailslist.get(0).setGlcodeIdDetail(account.getId());
		CFunction function=objectFactory.createFunction("testFunction");
		commonsManager.getFunctionById(billDetailslist.get(0).getFunctionIdDetail());
		expectLastCall().andReturn(function);
		commonsManager.getCChartOfAccountsByGlCode(glcodeDetail);
		expectLastCall().andReturn(account);
		replay(commonsManager);
		
		action.setBillDetailslist(billDetailslist);
		
		List<ReceiptDetailInfo> subLedgerlist = objectFactory.createSubLedgerlist();
		subLedgerlist.get(0).setGlcode(account);
		subLedgerlist.get(1).setGlcode(account);
		
		action.setSubLedgerlist(subLedgerlist);
		
		action.setPositionUser(-1);
		
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		
		UserImpl user = objectFactory.createUser("system",loggedinUserDept);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user).anyTimes();
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,loggedinUserDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(loggedinUserDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,loggedinUserDept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);

		//eisManager.getEmpForUserId(user.getId());
		//expectLastCall().andReturn(emp);
		//replay(eisManager);
		
		ReceiptHeader receiptHeaderObj=action.getReceiptHeader();
		receiptHeaderObj.getChallan().setState(objectFactory.createState(user));
		action.setReceiptHeader(receiptHeaderObj);
		
		objectFactory.createEmployeePositionDetails("testDesignation", emp,	loggedinUserDept);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		try {
			expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class),EasyMock.isA(Integer.class))).andReturn(null).anyTimes();
		} catch (ApplicationException e) {


		}
		//aDetail.getAccountDetailType(),aDetail.getAccountDetailKey().getDetailkey());
		
		//try{
			action.setActionName(CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN);
			assertEquals(action.save(),"view");
		//}
		catch(Exception ex){
			List<ValidationError> errors = action.errors;
			assertEquals(errors.size(),0);
			assertEquals("challan.accdetail.accmissing",errors.get(0).getKey());
			assertEquals(errors.get(0).getMessage(),
					"Account code is missing for credit/debit supplied field in account grid row :{0}");
		}
	}

	//@Test
	public void testViewChallan(){
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		Department loggedinUserDept = objectFactory.createDept("testUserDeptName");
		
		UserImpl user = objectFactory.createUser("system",loggedinUserDept);
		userManager.getUserByUserName(EasyMock.isA(String.class));
		expectLastCall().andReturn(user);
		replay(userManager);
		
		PersonalInformation emp = objectFactory.createPersonalInformation(user,loggedinUserDept);
		AssignmentPrd ap = objectFactory.createEmpAsgnmtPrd(emp);
		DesignationMaster desig = objectFactory.createDesignation(loggedinUserDept.getId(), "testDesignation"+objectFactory.getRandomNumber());
		Position position = objectFactory.createPosition(desig);
		Assignment assignment=objectFactory.createAssignment(ap, desig, position,loggedinUserDept);
		
		expect(eisManager.getEmpForUserId(isA(Integer.class))).andReturn(emp);
		expect(eisManager.getAssignmentByEmpAndDate(EasyMock.isA(Date.class),EasyMock.isA(Integer.class))).andReturn(assignment);
		replay(eisManager);

		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderWithChallan();
		receiptHeader.getChallan().setState(objectFactory.createState(user));
		ReceiptDetail receiptDetail = objectFactory.createReceiptDetailWithoutHeader();
		receiptHeader.addReceiptDetail(receiptDetail);
		session.saveOrUpdate(receiptHeader);
		action.setChallanId(receiptHeader.getChallan().getId().toString());
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_NAME, user.getUserName());
		action.setSession(sessionMap);
		
		assertEquals(action.viewChallan(),"view");
		
		assertEquals(receiptHeader.getReceiptMisc().getDepartment(), action.getDept());
		assertEquals(receiptHeader.getReceiptMisc().getBoundary(), action.getBoundary());
		assertEquals(receiptHeader.getReceiptDetails().iterator().next().getFunction(), 
				action.getFunction());
		assertEquals(action.getChallanId(), action.getReceiptHeader().getChallan().getId().toString());
	}
	
	@Test
	public void testCancel(){
		ReceiptHeader receipt = objectFactory.createReceiptHeaderWithChallan();
		action.setReceiptId(receipt.getId());
		action.setReceiptHeader(receipt);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
		
		UserImpl user = objectFactory.createUser("system");
		userManager.getUserByUserName("system");
		expectLastCall().andReturn(user);
		replay(userManager);
		
		action.setActionName(CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN);
		assertEquals(action.save(),"success");
		
	}
	
	@Test
	public void testSaveReceiptAfterIntraDayCancellation(){
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		Location location = objectFactory.createCounter("test");
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sessionMap);
		
		ReceiptHeader receiptToBeCancelled = objectFactory.createReceiptHeader("testReceiptNumber");
		
		ReceiptPayeeDetails payee = objectFactory.createPayeeForChallan();
		ReceiptHeader receiptHeader = payee.getReceiptHeaders().iterator().next();
		receiptHeader.setReceiptHeader(receiptToBeCancelled);
		session.saveOrUpdate(receiptHeader);
		
		action.setReceiptHeader(receiptHeader);
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		// replay(egovCommon);
		
		try {
			EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).andReturn(null);
		} catch (ApplicationException e) {
			//
		}
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		replay(commonsManager);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		
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
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		EasyMock.expect(instrumentService.addToInstrument(EasyMock.isA(List.class))).andReturn(actualInstrList);
		EasyMock.expect(instrumentService.updateInstrumentVoucherReference(EasyMock.isA(List.class))).andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);

		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		assertEquals(action.saveOrupdate(), CollectionConstants.REPORT);
		
		ReceiptHeader cancelledReceipt = (ReceiptHeader) genericService.find(
				"from org.egov.collection.entity.ReceiptHeader where id=?",
				action.getReceiptHeader().getReceiptHeader().getId());
		
		assertEquals(CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED, cancelledReceipt.getStatus().getCode());
	}
	
	@Test
	public void testSaveReceiptForCash(){
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		Location location = objectFactory.createCounter("test");
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sessionMap);
		
		ReceiptPayeeDetails payee = objectFactory.createPayeeForChallan();
		ReceiptHeader receiptHeader = payee.getReceiptHeaders().iterator().next();
		action.setReceiptHeader(receiptHeader);
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		// replay(egovCommon);
		
		try {
			EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).andReturn(null);
		} catch (ApplicationException e) {
			//
		}
		replay(egovCommon);
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		replay(commonsManager);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		
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
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);

		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		//expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class),EasyMock.isA(Integer.class))).andReturn(null).anyTimes();
		//expect(receiptWorkflowServiceMock.transition(EasyMock.isA(String.class), EasyMock.isA(StateAware.class), EasyMock.isA(String.class)));
		//EasyMock.replay(receiptWorkflowServiceMock);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		assertEquals(action.saveOrupdate(), CollectionConstants.REPORT);
		
		assertFalse(action.getReceiptHeader().getIsReconciled());
		assertTrue(action.getReceiptHeader().getIsModifiable());
		assertEquals(action.getReceiptHeader().getCollectiontype(),CollectionConstants.COLLECTION_TYPE_COUNTER);
		assertEquals(action.getReceiptHeader().getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
	}
	
	@Test
	public void testSaveReceiptForCard(){
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		Location location = objectFactory.createCounter("test");
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
		action.setSession(sessionMap);
		
		ReceiptPayeeDetails payee = objectFactory.createPayeeForChallan();
		ReceiptHeader receiptHeader = payee.getReceiptHeaders().iterator().next();
		action.setReceiptHeader(receiptHeader);
		action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CARD);
		
		CChartOfAccounts cashOnHand = objectFactory.createCOA("TestcashInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CASHINHAND, cashOnHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		// replay(egovCommon);
		
		try {
			EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).andReturn(null);
		} catch (ApplicationException e) {
			//
		}
		replay(egovCommon);
		
		
		commonsManager.getCChartOfAccountsByGlCode(cashOnHand.getGlcode());
		expectLastCall().andReturn(cashOnHand);
		replay(commonsManager);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		
		InstrumentHeader instrHeaderCard = new InstrumentHeader();
		List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
		instrHeaderCard.setInstrumentAmount(new BigDecimal(1000));
		instrHeaderCard.setStatusId(instrumentStatus);
		instrHeaderCard.setTransactionNumber("12345");
		instrHeaderCard.setTransactionDate(new Date());
		instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);
		instrHeaderCard.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CARD));
		session.saveOrUpdate(instrHeaderCard);
		actualInstrList.add(instrHeaderCard);
		action.setInstrHeaderCard(instrHeaderCard);
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		instrList.add(instrHeaderCard);
		
		List<Map<String, Object>> instrMapList = objectFactory.createMapForInstrumentHeader(instrList);
		List<Map<String, Object>> instrVoucherMapList = new ArrayList<Map<String, Object>>();
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(instrVoucherMapList);
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);

		String[] instrumentType = {};
		action.setInstrumentType(instrumentType);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		assertEquals(action.saveOrupdate(), CollectionConstants.REPORT);
		
		assertFalse(action.getReceiptHeader().getIsReconciled());
		assertTrue(action.getReceiptHeader().getIsModifiable());
		assertEquals(action.getReceiptHeader().getCollectiontype(),CollectionConstants.COLLECTION_TYPE_COUNTER);
		assertEquals(action.getReceiptHeader().getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
	}
	
	@Test
	public void testSaveReceiptForCheque() throws Exception{
		ReceiptPayeeDetails payee = objectFactory.createPayeeForChallan();
		ReceiptHeader receiptHeader = payee.getReceiptHeaders().iterator().next();
		action.setReceiptHeader(receiptHeader);
		
		Bank bank = objectFactory.createBank();
		commonsManager.getBankById(Integer.valueOf(bank.getId()));
		expectLastCall().andReturn(bank).anyTimes();
		
		CChartOfAccounts chequeInHand = objectFactory.createCOA("TestcchequeInHand");
		Map<String, Object> cashChequeInfoMap = new HashMap();
		cashChequeInfoMap.put(CollectionConstants.MAP_KEY_EGOVCOMMON_CHEQUEINHAND, chequeInHand.getGlcode());
		egovCommon.getCashChequeInfoForBoundary();
		expectLastCall().andReturn(cashChequeInfoMap);
		// replay(egovCommon);
		
		try {
			EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).andReturn(null);
		} catch (ApplicationException e) {
			//
		}
		replay(egovCommon);;
		
		commonsManager.getCChartOfAccountsByGlCode(chequeInHand.getGlcode());
		expectLastCall().andReturn(chequeInHand);
		
		CVoucherHeader voucherHeader = new CVoucherHeader();
		
		commonsManager.findVoucherHeaderById(null);
		expectLastCall().andReturn(voucherHeader);
		replay(commonsManager);
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		Location location = objectFactory.createCounter("test");
		sessionMap.put("com.egov.user.LoginUserName", "system");
		sessionMap.put(CollectionConstants.SESSION_VAR_LOGIN_USER_COUNTERID, location.getId().toString());
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
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		
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
		
		instrumentService.addToInstrument(instrMapList);
		expectLastCall().andReturn(actualInstrList);
		
		instrumentService.updateInstrumentVoucherReference(new ArrayList());
		expectLastCall().andReturn(new ArrayList<InstrumentVoucher>());
		replay(instrumentService);
		
		action.setInstrumentProxyList(actualInstrList);
		action.setInstrumentAmount(instrAmt);
		action.setInstrumentType(instrumentType);
		action.setInstrumentBranchName(branchName);
		action.setInstrumentNumber(instrNum);
		action.setInstrumentBankId(bankId);
		action.setInstrumentDate(instrDate);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		assertEquals(action.saveOrupdate(), CollectionConstants.REPORT);
	}
	
	@Test
	public void testPrintChallan(){
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		action.setSession(new HashMap());
		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderForChallan();
		action.setReceiptHeader(receiptHeader);
		String result =action.printChallan();
		assertEquals("report",result);
		assertEquals(0,action.getReportId());
		assertEquals("viewChallan",action.getSourcePage());
	}
	
	
	@Test
	public void testValidActions(){
		
		action.setChallanWorkflowService(challanWorkflowService);
		
		
		// TEST VALID ACTIONS WHEN CHALLAN IS TO BE CREATED - ie., 
		// not associated with any state
		action.setReceiptHeader(new ReceiptHeader());
		
		List<String> actionNames = new ArrayList<String>();
		for(Action wflowAction : action.getValidActions())
		{
			actionNames.add(wflowAction.getName());
		}
		assertEquals(actionNames.size(), 1);
		assertTrue(actionNames.contains("CHALLAN_NEW"));
		
		//TEST VALID ACTIONS WHEN CHALLAN IS CREATED
		actionNames.clear();
		State state = objectFactory.createState("Challan", "CREATED");
		Challan challan = new Challan();
		challan.changeState(state);
		action.getReceiptHeader().setChallan(challan);
		//action.getReceiptHeader().getChallan().changeState(state);
		
		for(Action wflowAction : action.getValidActions())
		{
			actionNames.add(wflowAction.getName());
		}
		assertEquals(actionNames.size(), 2);
		assertTrue(actionNames.contains("CHALLAN_CHECK"));
		assertTrue(actionNames.contains("CHALLAN_REJECT"));
		
		//TEST VALID ACTIONS WHEN CHALLAN IS CHECKED
		actionNames.clear();
		state = objectFactory.createState("Challan", "CHECKED");
		action.getReceiptHeader().getChallan().changeState(state);
		
		for(Action wflowAction : action.getValidActions())
		{
			actionNames.add(wflowAction.getName());
		}
		assertEquals(actionNames.size(), 2);
		assertTrue(actionNames.contains("CHALLAN_APPROVE"));
		assertTrue(actionNames.contains("CHALLAN_REJECT"));
		
		//TEST VALID ACTIONS WHEN CHALLAN IS APPROVED
		actionNames.clear();
		state = objectFactory.createState("Challan", "APPROVED");
		action.getReceiptHeader().getChallan().changeState(state);
		
		for(Action wflowAction : action.getValidActions())
		{
			actionNames.add(wflowAction.getName());
		}
		assertEquals(actionNames.size(), 1);
		assertTrue(actionNames.contains("CHALLAN_VALIDATE"));
		
		//TEST VALID ACTIONS WHEN CHALLAN IS REJECTED
		actionNames.clear();
		state = objectFactory.createState("Challan", "REJECTED");
		action.getReceiptHeader().getChallan().changeState(state);
		
		for(Action wflowAction : action.getValidActions())
		{
			actionNames.add(wflowAction.getName());
		}
		assertEquals(actionNames.size(), 2);
		assertTrue(actionNames.contains("CHALLAN_MODIFY"));
		assertTrue(actionNames.contains("CHALLAN_CANCEL"));
		
		//TEST VALID ACTIONS WHEN CHALLAN IS VALIDATED
		actionNames.clear();
		state = objectFactory.createState("Challan", "VALIDATED");
		action.getReceiptHeader().getChallan().changeState(state);
		
		for(Action wflowAction : action.getValidActions())
		{
			actionNames.add(wflowAction.getName());
		}
		assertEquals(actionNames.size(), 1);
		assertTrue(actionNames.contains(""));
	}
	
	@Test
	public void testCancelReceipt(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithChallan();
		action.setSelectedReceipts(new Long[1]);
		action.getSelectedReceipts()[0]=receiptHeader.getId();
		
		assertEquals(CollectionConstants.CANCELRECEIPT, action.cancelReceipt());
		assertEquals(action.getReceiptHeader(),receiptHeader);
	}
	
	@Test
	public void testCancelInvalidChallanReceipt(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithInstrument(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_CHALLAN, 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, "testReferenceNo", 
				objectFactory.createInstrumentType(CollectionConstants.INSTRUMENTTYPE_CHEQUE), 
				"testInstrNo", Double.valueOf(1000), new Date(),
				CollectionConstants.INSTRUMENT_DEPOSITED_STATUS, "testGLCode", 
				"testFunctionName", "testUserName");
		
		Challan challan = objectFactory.createChallan();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-3);
		challan.setValidUpto(cal.getTime());
		cal.add(Calendar.DATE,-3);
		challan.setChallanDate(cal.getTime());
		
		challan.setReceiptHeader(receiptHeader);
		receiptHeader.setChallan(challan);
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		
		receiptService.persist(payee);
		
		ReceiptHeader saved = receiptHeaderService.findById(
				payee.getReceiptHeaders().iterator().next().getId(), false);
		
		action.setReceiptHeader(receiptHeader);
		
		assertEquals(BaseFormAction.SUCCESS, action.saveOnCancel());
		assertEquals(action.getReceiptHeader(),saved);
	}
	
	@Test
	public void testSaveOnPostRemittanceCancellation(){
		createEmployeeForLoggedInUser();
		InstrumentType type = (InstrumentType) genericService.find(
				"from InstrumentType where type=?",CollectionConstants.INSTRUMENTTYPE_CASH);
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithInstrument(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_CHALLAN, 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, "testReferenceNo", 
				type, 
				"testInstrNo", Double.valueOf(1000), new Date(),
				CollectionConstants.INSTRUMENT_RECONCILED_STATUS, "testGLCode", 
				"testFunctionName", "testUserName");
		Challan challan = objectFactory.createChallan();
		challan.setReceiptHeader(receiptHeader);
		receiptHeader.setChallan(challan);
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		
		receiptService.persist(payee);
		
		ReceiptHeader saved = receiptHeaderService.findById(
				payee.getReceiptHeaders().iterator().next().getId(), false);
		
		action.setReceiptHeader(receiptHeader);
		
		assertEquals(action.saveOnCancel(),CollectionConstants.CREATERECEIPT);
	}
	
	*//**
	 * Instrument has not been deposited
	 *//*
	@Test
	public void testSaveOnIntraDayCancel(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithInstrument(
				"testReceiptNo", CollectionConstants.RECEIPT_TYPE_CHALLAN, 
				CollectionConstants.RECEIPT_STATUS_DESC_CREATED, "testReferenceNo", 
				objectFactory.createInstrumentType(CollectionConstants.INSTRUMENTTYPE_CASH), 
				"testInstrNo", Double.valueOf(1000), new Date(),
				CollectionConstants.INSTRUMENT_NEW_STATUS, "testGLCode", 
				"testFunctionName", "testUserName");
		
		Challan challan = objectFactory.createChallan();
		challan.setReceiptHeader(receiptHeader);
		receiptHeader.setChallan(challan);
		
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		
		receiptService.persist(payee);
		
		ReceiptHeader saved = receiptHeaderService.findById(
				payee.getReceiptHeaders().iterator().next().getId(), false);
		
		EgwStatus instrumentStatus = (EgwStatus) genericService.find("from EgwStatus S where S.moduletype =? and S.code =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED);
		commonsManager.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED);
		expectLastCall().andReturn(instrumentStatus);
		
		replay(commonsManager);
		
		action.setReceiptHeader(receiptHeader);
		
		
		assertEquals(action.saveOnCancel(),BaseFormAction.SUCCESS);
		assertEquals(action.getReceiptHeader().getStatus().getCode(),CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		
		ReceiptHeader newreceiptHeader = (ReceiptHeader) genericService.find(
				"from org.egov.collection.entity.ReceiptHeader receipt where receipt.receiptHeader.id=?",
				action.getReceiptHeader().getId());
		
		
		assertEquals(newreceiptHeader.getReceiptnumber(), null);
		assertEquals(newreceiptHeader.getStatus().getCode(), CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		
	}
*/}
