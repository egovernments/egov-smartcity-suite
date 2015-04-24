package org.egov.collection.services;


public class ReceiptServiceTest { /*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails, Long> {
	private ReceiptService service;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private CollectionObjectFactory objectFactory;
	private CollectionsUtil collectionsUtil;
	private EisUtilService eisUtilService;
	private GenericHibernateDaoFactory genericDao;
	private Department department;
	
	@Before
	public void setupService(){
		
		service = new ReceiptService();
		objectFactory = new CollectionObjectFactory(session);
		
		ScriptService scriptExecutionService = new ScriptService(2, 5, 10, 30);
		scriptExecutionService.setSessionFactory(egovSessionFactory);
		
		sequenceGenerator = new SequenceNumberGenerator(egovSessionFactory);
		
		genericDao = new GenericHibernateDaoFactory(){
			protected Session getCurrentSession(){
				return session;
			}
			public  AppDataDAO getAppDataDAO()
			{
				return new AppDataHibernateDAO(AppData.class,session);
			}
		};
		
		collectionsUtil=new CollectionsUtil(){
			public Department getDepartmentOfUser(User user) {
				department= objectFactory.createDeptForCode("testDeptCode");
				return department;
			}
		};
		
		
		collectionsUtil.setPersistenceService(genericService);
		collectionsUtil.setGenericDao(genericDao);
		
		eisUtilService=new EisUtilService(){
			public Date getPriorOrAfterWorkingDate(Date givenDate,int noOfDays,DATE_ORDER enumtype) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(givenDate);
				cal.add(Calendar.DATE, noOfDays);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				return cal.getTime();
			}
		};
		eisUtilService.setPersistenceService(genericService);
		
		collectionsNumberGenerator=new CollectionsNumberGenerator();
		collectionsNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collectionsNumberGenerator.setCollectionsUtil(collectionsUtil);
		collectionsNumberGenerator.setSequenceGenerator(sequenceGenerator);
		
		service.setCollectionsNumberGenerator(collectionsNumberGenerator);
		service.setSessionFactory(egovSessionFactory);
		service.setCollectionsUtil(collectionsUtil);
		service.setEisService(eisUtilService);
	}
	
	@Test
	public void testPersistPayeeList(){
		ReceiptPayeeDetails payee1 =new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader1 = objectFactory.createUnsavedReceiptHeader();
		receiptHeader1.setReceiptPayeeDetails(payee1);
		payee1.addReceiptHeader(receiptHeader1);
		
		ReceiptPayeeDetails payee2 = new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader2 = objectFactory.createUnsavedReceiptHeader();
		receiptHeader2.setReceiptPayeeDetails(payee2);
		payee2.addReceiptHeader(receiptHeader2);
		
		Set<ReceiptPayeeDetails> payeeList=new HashSet<ReceiptPayeeDetails>();
		payeeList.add(payee1);
		payeeList.add(payee2);
		List<ReceiptPayeeDetails> saved=service.persist(payeeList);
		List<ReceiptPayeeDetails> actual=service.findAll();
		assertTrue(actual.containsAll(saved));
	}
	
	@Test
	public void testPersistPendingReceiptsPayeeList(){
		ReceiptPayeeDetails payee1 =new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader1 = objectFactory.createUnsavedPendingReceiptHeader();
		receiptHeader1.setReceiptPayeeDetails(payee1);
		payee1.addReceiptHeader(receiptHeader1);
		
		ReceiptPayeeDetails payee2 = new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader2 = objectFactory.createUnsavedPendingReceiptHeader();
		receiptHeader2.setReceiptPayeeDetails(payee2);
		payee2.addReceiptHeader(receiptHeader2);
		
		Set<ReceiptPayeeDetails> payeeListNew=new HashSet<ReceiptPayeeDetails>();
		payeeListNew.add(payee1);
		payeeListNew.add(payee2);
		List<ReceiptPayeeDetails> saved=service.persistPendingReceipts(payeeListNew);
		List<ReceiptPayeeDetails> actual=service.findAll();
		assertTrue(actual.containsAll(saved));
	}
	
	@Test
	public void testPersistSetsReceiptNumberIfNotPresent(){
		ReceiptPayeeDetails payee = new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber(null);
		payee.addReceiptHeader(receiptHeader);
		ReceiptPayeeDetails saved =service.persist(payee);
		String actualReceiptNo=saved.getReceiptHeaders().iterator().next().getReceiptnumber();
		String receiptNo=collectionsNumberGenerator.generateReceiptNumber(receiptHeader);
		assertEquals(receiptNo.substring(0, (receiptNo.lastIndexOf('/')+1)), 
				actualReceiptNo.substring(0, actualReceiptNo.lastIndexOf('/')+1));
		assertEquals(Integer.valueOf(receiptNo.substring(receiptNo.lastIndexOf('/')+1))-1,
				Integer.valueOf(actualReceiptNo.substring(actualReceiptNo.lastIndexOf('/')+1)));
		
	}
	
	@Test
	public void testPersistDoesNotSetReceiptNumberIfPresent(){
		ReceiptPayeeDetails payee = new ReceiptPayeeDetails();
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNumber");
		payee.addReceiptHeader(receiptHeader);
		
		ReceiptPayeeDetails saved=service.persist(payee);
		assertEquals(saved.getReceiptHeaders().iterator().next().getReceiptnumber(),
				"ReceiptNumber");
	}
	
	@Test
	public void testPersistChallanNumber(){
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		ReceiptHeader header = objectFactory.createUnsavedReceiptHeader();
		header.setReceiptPayeeDetails(payee);
		Challan challan = objectFactory.createUnsavedChallan();
		challan.setChallanNumber(null);
		challan.setReceiptHeader(header);
		challan.getCreatedBy().setDepartment(department);
		challan.getModifiedBy().setDepartment(department);
		header.setChallan(challan);
		payee.addReceiptHeader(header);
		
		Date date = challan.getCreatedDate();
		
		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(date);
		
		UserImpl user = (UserImpl) challan.getCreatedBy();
		
		Integer validUpto = Integer.valueOf(collectionsUtil.getAppConfigValue(
				CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
				CollectionConstants.APPCONFIG_VALUE_CHALLANVALIDUPTO));
		
		Date validuptoDate=eisUtilService.getPriorOrAfterWorkingDate(challan.getChallanDate(), validUpto, DATE_ORDER.AFTER);
		
		String challanSequence = "SQ_CHALLAN_"+financialYear.getFinYearRange().substring(0, 4);
		List numbers = session.createSQLQuery("SELECT "+challanSequence+".NEXTVAL FROM DUAL").list();
		
		ReceiptPayeeDetails savedPayee = service.persistChallan(
				payee);
		Challan savedChallan = savedPayee.getReceiptHeaders().iterator().next().getChallan();
		String actualChallanNo=savedChallan.getChallanNumber();
		
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		
		String resultStr = String.valueOf(result.longValue() + 1);
		String sequence = "";
		if(resultStr.length()==1)
		{	
			sequence = "000000"+resultStr;
		}
		else if(resultStr.length()==2)
		{
			sequence = "00000"+resultStr;
		}
		else if(resultStr.length()==3)
		{
			sequence = "0000"+resultStr;
		}
		else if(resultStr.length()==4)
		{
			sequence = "000"+resultStr;
		}
		else if(resultStr.length()==5)
		{
			sequence = "00"+resultStr;
		}
		else if(resultStr.length()==6)
		{
			sequence = "0"+resultStr;
		}
		else
		{
			sequence = resultStr;
		}	
				
		String expectedChallanNo = department.getDeptCode()+"/"+financialYear.getFinYearRange()+"/"+sequence;
		
		assertEquals(expectedChallanNo,actualChallanNo);
		assertEquals(validuptoDate,savedChallan.getValidUpto());
	}
	
	 @Test
	public void testPersistDoesNotSetChallanNumberIfPresent(){
		ReceiptPayeeDetails payee = objectFactory.createReceiptPayeeWithoutHeader();
		ReceiptHeader header = objectFactory.createUnsavedReceiptHeader();
		header.setReceiptPayeeDetails(payee);
		Challan challan = objectFactory.createUnsavedChallan();
		challan.setReceiptHeader(header);
		header.setChallan(challan);
		payee.addReceiptHeader(header);
		
		ReceiptPayeeDetails savedPayee = service.persistChallan(
				challan.getReceiptHeader().getReceiptPayeeDetails());
		
		assertEquals(challan.getChallanNumber(),
				savedPayee.getReceiptHeaders().iterator().next().
				getChallan().getChallanNumber());
	}*/
}
