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



public class ChallanUploadTest {/* extends AbstractPersistenceServiceTest<Challan,Long>{
	private ChallanAction action;
	private CollectionObjectFactory objectFactory;
	private @Autowired AppConfigValuesDAO appConfigValuesDAO;
	private CommonsManager commonsManager;
	private BoundaryDAO boundaryDAO;
	
	private ReceiptService receiptService;
	private ChallanService challanService;
	private WorkflowService<Challan> challanWorkflowService;
	//private WorkflowService<ReceiptHeader> receiptWorkflowService;
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
	private CFinancialYear financialYear1011;
	private UserImpl user;
	private EGovEJBTest egovEJBTest;
	static ServiceLocator sl = ServiceLocator.getInstance();
	static CommonsManagerHome cmh = null;
	static UserManagerHome umh=null;
	static EisManagerHome emh=null;
	
	private List<String[]> inputList=new ArrayList<String[]>();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	
	@Test
	public void testMock(){
	assertTrue(true);
	}
	//@Before
	public void setupAction() throws Exception{
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
		user =  (UserImpl) genericService.find("from UserImpl  where userName=? ","system");
		objectFactory = new CollectionObjectFactory(session);
		setupEJB();
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);

		position = (Position) genericService.find("from Position  where name=?","MEDICAL OFFICER_1");
		
		collectionsUtil=new CollectionsUtil(){
			public Position getPositionOfUser(User user) {
				return position;
			}
			public Position getPositionByName(String positionName) {
				return position;
			}
			public List<DesignationMaster> getDesignationsAllowedForChallanApproval(
					User loggedInUser,ReceiptHeader receiptHeaderObj){
				return new ArrayList<DesignationMaster>();
			}
		};
		egovCommon=new EgovCommon();
		egovCommon.setGenericDao(genericDao);
		egovCommon.setPersistenceService(genericService);
		boundaryDAO= new BoundaryDAO();
		collectionsUtil.setGenericDao(genericDao);
		collectionsUtil.setScriptService(genericService);
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setUserManager(userManager);
		collectionsUtil.setEisManager(eisManager);
		collectionsUtil.setPersistenceService(genericService);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);		
		
		instrumentService = new InstrumentService(){
			
			public InstrumentType getInstrumentTypeByType(String type){
				return (InstrumentType) genericService.find("from InstrumentType  where type=? and isActive=true",type);
			}
		};
		PersistenceService<InstrumentHeader, Long> iHeaderService= new PersistenceService<InstrumentHeader, Long>();
		iHeaderService.setType(InstrumentHeader.class);
		instrumentService.setInstrumentHeaderService(iHeaderService);
		
		PersistenceService<InstrumentOtherDetails, Long> iOtherDetailsService= new PersistenceService<InstrumentOtherDetails, Long>();
		iOtherDetailsService.setType(InstrumentOtherDetails.class);
		instrumentService.setInstrumentOtherDetailsService(iOtherDetailsService);
		instrumentService.setPersistenceService(genericService);
		
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
		
		receiptHeaderService = new ReceiptHeaderService(){
			public void startWorkflow(Collection<ReceiptHeader> receiptHeaders)
			throws ApplicationRuntimeException {
				
			}
		};
		receiptHeaderService.setType(ReceiptHeader.class);
		receiptHeaderService.setPersistenceService(genericService);
		receiptHeaderService.setCollectionsUtil(collectionsUtil);
		receiptHeaderService.setCommonsManager(commonsManager);
		receiptHeaderService.setCollectionsNumberGenerator(collectionsNumberGenerator);
		receiptHeaderService.setFinancialsUtil(financialsUtil);
		receiptWorkflowService=new SimpleWorkflowService<ReceiptHeader>(
				receiptHeaderService, ReceiptHeader.class);
		receiptHeaderService.setReceiptWorkflowService(receiptWorkflowService);
		
		collectionCommon = new CollectionCommon();
		collectionCommon.setPersistenceService(genericService);
		collectionCommon.setEgovCommon(egovCommon);
		collectionCommon.setCommonsManager(commonsManager);
		collectionCommon.setCollectionsUtil(collectionsUtil);
		collectionCommon.setReceiptHeaderService(receiptHeaderService);
		collectionCommon.setReceiptPayeeDetailsService(receiptService);
		
		financialYear1011=(CFinancialYear)genericService.find("from CFinancialYear  where finYearRange=? ","2010-11");
		
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
	public ReceiptHeader createReceiptHeader(String[] inputArray) {
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setReferenceDesc(inputArray[4]);
		receiptHeader.setReceiptMisc(createReceiptMisc(receiptHeader,inputArray));
		receiptHeader.setReceiptPayeeDetails(createReceiptPayee(inputArray));
		receiptHeader.setManualreceiptnumber(inputArray[13]);
		Date date=null;
		try {
			date = sdf.parse(inputArray[14]);
		} catch (ParseException e) {

		}
		receiptHeader.setManualreceiptdate(date);
		return receiptHeader;
	}
	public ReceiptMisc createReceiptMisc(ReceiptHeader receiptHeader,String[] inputArray) {
		ReceiptMisc receiptMisc = new ReceiptMisc();
		Fund fund = (Fund) genericService.find("from Fund  where name=? ",inputArray[5]);
		DepartmentImpl dept = (DepartmentImpl) genericService.find("from DepartmentImpl d where d.deptName=? ",inputArray[6]);
		receiptMisc.setDepartment(dept);
		receiptMisc.setFund(fund);
		receiptMisc.setReceiptHeader(receiptHeader);
		return receiptMisc;
	}
	public ReceiptPayeeDetails createReceiptPayee(String[] inputArray) {
		ReceiptPayeeDetails receiptPayee = new ReceiptPayeeDetails();
		receiptPayee.setPayeeAddress(inputArray[3]);
		receiptPayee.setPayeename(inputArray[2]);
		return receiptPayee;
	}
	public Challan createChallan(ReceiptHeader header,String[] inputArray) {
		Challan challan = new Challan();
		
		Date date=null;
		try {
			date = sdf.parse(inputArray[1]);
		} catch (ParseException e) {

		}
		challan.setChallanDate(date);
		challan.setChallanNumber(inputArray[0]);
		challan.setReceiptHeader(header);
		return challan;
	}
	public List<ReceiptDetailInfo> createCreditDetailslist(String[] inputArray) {
		List<ReceiptDetailInfo> billCreditDetailslist = new ArrayList<ReceiptDetailInfo>();
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		CChartOfAccounts account = (CChartOfAccounts)genericService.find("from CChartOfAccounts  where glcode=? ",inputArray[8]);
		vd.setAccounthead(account.getName());
		vd.setCreditAmountDetail(new BigDecimal(inputArray[9]));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		vd.setGlcodeDetail(account.getGlcode());
		vd.setGlcodeIdDetail(account.getId());
		vd.setFinancialYearId(financialYear1011.getId());
		billCreditDetailslist.add(vd);
		return billCreditDetailslist;
	}
	public List<ReceiptDetailInfo> createEmptySubLedgerlist(){
		List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(0));
		vd.setDetailCode("");
		vd.setDetailKey("");
		subLedgerlist.add(vd);
		return subLedgerlist;
	}
	
	public List<ReceiptDetailInfo> createSubLedgerlist(String[] inputArray) throws Exception{
		List<ReceiptDetailInfo> subLedgerlist = new ArrayList<ReceiptDetailInfo>();
		Accountdetailtype accountdetailtype = (Accountdetailtype)genericService.find("from Accountdetailtype  where name=? ",inputArray[10]);
		CChartOfAccounts account = (CChartOfAccounts)genericService.find("from CChartOfAccounts  where glcode=? ",inputArray[8]);
		String table=accountdetailtype.getFullQualifiedName();
		Class<?> service = Class.forName(table);
		String simpleName = service.getSimpleName();
		simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "classpath*:org/egov/infstr/beanfactory/globalApplicationContext.xml" });//To get the globalappContext
		EntityTypeService entityService = (EntityTypeService) applicationContext
				.getBean(simpleName);
		List entityList=entityService.filterActiveEntities(inputArray[11], -1, accountdetailtype.getId());
		EntityType entityType=null;
		if(entityList.size()==1){
			entityType=(EntityType)entityList.get(0);
		}
		ReceiptDetailInfo vd = new ReceiptDetailInfo();
		vd.setCreditAmountDetail(new BigDecimal(0));
		vd.setDebitAmountDetail(new BigDecimal(0));
		vd.setAmount(new BigDecimal(inputArray[12]));
		vd.setDetailCode(inputArray[11]);
		vd.setDetailKey(entityType.getName());
		vd.setDetailKeyId(entityType.getEntityId());
		vd.setDetailType(accountdetailtype);
		vd.setDetailTypeName(inputArray[10]);
		vd.setGlcode(account);
		vd.setSubledgerCode(inputArray[8]);
		subLedgerlist.add(vd);
		return subLedgerlist;
	}
	
	*//**
	 * This method will be temporary and can be used till the actual array
	 * which contains values from the excel/css is ready
	 *//*
	private void initialiseInputArray(){
		String[] inputArrayCash = new String[21];
		
		// from the input for challan receipt creation; from the created receipt for initial challan creation 
		inputArrayCash[0]="challanNo14";
		
		// from the input for challan receipt creation; from the created receipt for initial challan creation 
		inputArrayCash[1]="01/04/2010";
		
		inputArrayCash[2]="testPayeeName1";
		inputArrayCash[3]="testPayeeAddress1";
		inputArrayCash[4]="testNarration1";
		inputArrayCash[5]="Capital Projects Fund";
		inputArrayCash[6]="A-Accounts Central Cell";
		inputArrayCash[7]="Municipal Body";
		
		//accounthead code
		inputArrayCash[8]="1100101";
		
		// amount
		inputArrayCash[9]="2000.0";
		
		// subledger type
		inputArrayCash[10]="";
		
		//subledger code
		inputArrayCash[11]="";
		
		//subledger amount
		inputArrayCash[12]="";
		
		// from the input for challan receipt creation; from the created receipt for initial challan creation 
		inputArrayCash[13]="testReceiptNo12";
		
		inputArrayCash[14]="02/04/2010";
		
		//payment mode can take values CASH, CHEQUE, DD
		inputArrayCash[15]=CollectionConstants.INSTRUMENTTYPE_CASH;
		inputArrayCash[16]="2000.0";
		inputArrayCash[17]="";
		inputArrayCash[18]="";
		inputArrayCash[19]="";
		inputArrayCash[20]="";
		
		inputList.add(inputArrayCash);
		
String[] inputArrayCheque = new String[21];
		
		// from the input for challan receipt creation; from the created receipt for initial challan creation 
		inputArrayCheque[0]="challanNo4";
		
		// from the input for challan receipt creation; from the created receipt for initial challan creation 
		inputArrayCheque[1]="21/05/2010";
		
		inputArrayCheque[2]="testPayeeName2";
		inputArrayCheque[3]="testPayeeAddress2";
		inputArrayCheque[4]="testNarration2";
		inputArrayCheque[5]="Primary Education Fund";
		inputArrayCheque[6]="B-Buildings";
		inputArrayCheque[7]="Finance Accounts and Audit";
		
		//accounthead code
		inputArrayCheque[8]="3117004";
		
		// amount
		inputArrayCheque[9]="3500.0";
		
		// subledger type
		inputArrayCheque[10]="Employee";
		
		//subledger code
		inputArrayCheque[11]="102";
		
		//subledger amount
		inputArrayCheque[12]="3500.0";
		
		// from the input for challan receipt creation; from the created receipt for initial challan creation 
		inputArrayCheque[13]="testReceiptNo4";
		
		inputArrayCheque[14]="24/05/2010";
		
		//payment mode can take values CASH, CHEQUE, DD
		inputArrayCheque[15]=CollectionConstants.INSTRUMENTTYPE_CHEQUE;
		inputArrayCheque[16]="3500.0";
		inputArrayCheque[17]="123456";
		inputArrayCheque[18]="20/04/2010";
		inputArrayCheque[19]="AXIS Bank";
		inputArrayCheque[20]="Jayanagar";
		
		inputList.add(inputArrayCheque);
		
	}
	private void initialiseValuesForSaveNew(String[] inputArray){
		DepartmentImpl dept = (DepartmentImpl) genericService.find("from DepartmentImpl d where d.deptName=? ",inputArray[6]);
		action.setDeptId(String.valueOf(dept.getId()));
		action.setDept(dept);
		
		ReceiptHeader header = createReceiptHeader(inputArray);
		Challan challan = createChallan(header,inputArray);
		header.setChallan(challan);
		CFunction function=(CFunction)genericService.find("from CFunction  where name=? ",inputArray[7]);
				
		action.setReceiptHeader(header);
		
		List<ReceiptDetailInfo> billCreditDetailslist = createCreditDetailslist(inputArray);
		action.setBillDetailslist(billCreditDetailslist);
		List<ReceiptDetailInfo> subLedgerlist=null;
		try {
			if(inputArray[10]!=""){
				subLedgerlist=createSubLedgerlist(inputArray);
			}
			else{
				subLedgerlist=createEmptySubLedgerlist();
			}
		} catch (Exception e) {


		}
		action.setSubLedgerlist(subLedgerlist);
		
		action.setPositionUser(-1);
		
		
		
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("com.egov.user.LoginUserName", "system");
		action.setSession(sessionMap);
	}
	
	private void initialiseValuesForCreateReceipt(String[] inputArray) throws ParseException{
		action.getReceiptHeader().setService((ServiceDetails) genericService.findByNamedQuery(
				CollectionConstants.QUERY_SERVICE_BY_CODE, 
				CollectionConstants.SERVICE_CODE_COLLECTIONS));
		EgwStatus instrumentStatus = (EgwStatus) genericService.find(
				"from EgwStatus S where S.moduletype =? and S.description =?",
				CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
				CollectionConstants.INSTRUMENT_NEW_STATUS);
		
		if(CollectionConstants.INSTRUMENTTYPE_CASH.equals(inputArray[15])){
			action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CASH);
			
			InstrumentHeader instrHeaderCash = new InstrumentHeader();
			List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
			instrHeaderCash.setInstrumentAmount(new BigDecimal(inputArray[16]));
			instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
			instrHeaderCash.setStatusId(instrumentStatus);
			instrHeaderCash.setInstrumentType(financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));
			session.saveOrUpdate(instrHeaderCash);
			actualInstrList.add(instrHeaderCash);
			action.setInstrHeaderCash(instrHeaderCash);
			List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
			instrList.add(instrHeaderCash);
			
			String[] instrumentType = {};
			action.setInstrumentType(instrumentType);
			
		}
		
		if(CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(inputArray[15]) ||
				CollectionConstants.INSTRUMENTTYPE_DD.equals(inputArray[15])){
			
			String[] instrumentType = new String[1];
			String[] instrAmt =  new String[1];
			String[] branchName =  new String[1];
			String[] instrNum =  new String[1];
			String[] bankId =  new String[1];
			String[] instrDate = new String[1];
			Bank[] bank = new Bank[1];
			
			//payt mode is CHEQUE/DD
			instrumentType[0]=inputArray[15];
			instrAmt[0]=inputArray[16];
			branchName[0]=inputArray[20];
			instrNum[0]=inputArray[17];
			bank[0] = (Bank) genericService.find("from Bank where name=?", inputArray[19]);
			bankId[0]=bank[0].getId().toString();
			instrDate[0]=inputArray[18];
			
			action.setInstrumentAmount(instrAmt);
			action.setInstrumentType(instrumentType);
			action.setInstrumentBranchName(branchName);
			action.setInstrumentNumber(instrNum);
			action.setInstrumentBankId(bankId);
			action.setInstrumentDate(instrDate);
			
			action.setInstrumentTypeCashOrCard(CollectionConstants.INSTRUMENTTYPE_CHEQUE);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date instrumentDate = sdf.parse(inputArray[18]);
			
			InstrumentHeader instrHeaderChequeDD;
			
			if(CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(inputArray[15])){
			instrHeaderChequeDD = objectFactory.createInstrumentHeaderWithBankDetails(
					financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE), 
					inputArray[17],Double.valueOf(inputArray[16]),instrumentDate,
					instrumentStatus,bank[0],inputArray[20],"0");
			}
			else{
				instrHeaderChequeDD = objectFactory.createInstrumentHeaderWithBankDetails(
						financialsUtil.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD), 
						inputArray[17],Double.valueOf(inputArray[16]),instrumentDate,
						instrumentStatus,bank[0],inputArray[20],"0");
			}
			List<InstrumentHeader> actualInstrList = new ArrayList<InstrumentHeader>();
			actualInstrList.add(instrHeaderChequeDD);
			List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
			instrList.add(instrHeaderChequeDD);
			//----
			action.setInstrumentProxyList(actualInstrList);
		}
	}
	
	//@Test
	public void testSaveChallanReceipt() throws ApplicationException, ParseException{
		initialiseInputArray();
		
		for (String[] inputArray : inputList) 
		{
		initialiseValuesForSaveNew(inputArray);
		
		action.setActionName(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN);
		action.save();
		
		// The challan has been created and receipt is reserved for the challan.
		// Next step is to create the actual receipt for the challan.
		action.setChallanNumber(action.getReceiptHeader().getChallan().getChallanNumber());
		action.setChallanId(action.getReceiptHeader().getChallan().getId().toString());
		
		initialiseValuesForCreateReceipt(inputArray);
		
		ReportService reportService = EasyMock.createMock(ReportService.class);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(false);
		EasyMock.expect(reportService.isValidTemplate(EasyMock.isA(String.class))).andReturn(true);
		EasyMock.expect(reportService.createReport(EasyMock.isA(ReportRequest.class))).andReturn(new ReportOutput());
		EasyMock.replay(reportService);
		collectionCommon.setReportService(reportService);
		
		assertEquals(action.saveOrupdate(), CollectionConstants.REPORT);
		
		}
	}
	
	//@After
	public void tearDown(){
		if(session.isOpen() && !session.getTransaction().wasCommitted())
			session.getTransaction().rollback();
		//Uncomment to commit the data
		//session.getTransaction().commit();
	}
	private void setupEJB() throws Exception{
		
		
		egovEJBTest=new EGovEJBTest();
		egovEJBTest.setUp();
		ApplicationThreadLocals.setUserId(user.getId().toString());
		egovEJBTest.registerEJB("CommonsManagerHome", CommonsManagerHome.class, CommonsManager.class, CommonsManagerBean.class);
		egovEJBTest.registerEJB("EmpLeaveManagerHome", EmpLeaveManagerHome.class, EmpLeaveManager.class, EmpLeaveManagerBean.class);
		egovEJBTest.registerEJB("UserManagerHome", UserManagerHome.class, UserManager.class, UserManagerBean.class);
		egovEJBTest.registerEJB("EisManagerHome", EisManagerHome.class, EisManager.class, EisManagerBean.class);
		
		try
        {
        	if(cmh == null){
        		cmh = (CommonsManagerHome)sl.getLocalHome("CommonsManagerHome");
        	}
        	if(umh == null){
        		umh = (UserManagerHome)sl.getLocalHome("UserManagerHome");
        	}
        	if(emh == null){
        		emh = (EisManagerHome)sl.getLocalHome("EisManagerHome");
        	}
        	commonsManager = cmh.create();
        	userManager = umh.create();
        	eisManager = emh.create();
        }
		catch (CreateException e) {
            
			throw new ApplicationRuntimeException(e.getMessage(),e);
        }
		catch (ServiceLocatorException serviceLocatorEx) {

        	//LOGGER.error(serviceLocatorEx.getMessage());
		}
		
	}
	
*/}
