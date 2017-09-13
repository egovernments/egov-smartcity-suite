package org.egov.collection.util;

public class CollectionsNumberGeneratorTest { /*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private CollectionObjectFactory objectFactory;
	private CollectionsNumberGenerator collNumberGenerator;
	private SequenceNumberGenerator sequenceGenerator;
	private ScriptService scriptExecutionService;
	
	private CollectionsUtil collectionsUtil;
	private Department department;
	public CollectionsNumberGeneratorTest() {		
		this.type = ReceiptHeader.class;
	}
	
	@Before
	public void setupService(){
		
		service = new ReceiptService();
		objectFactory = new CollectionObjectFactory(session);
		
		collectionsUtil=new CollectionsUtil(){
			public Department getDepartmentOfUser(User user) {
				department= objectFactory.createDeptForCode("testDeptCode");
				return department;
			}
		};
		
		scriptExecutionService = new ScriptService(2, 5, 10, 30);

		collectionsUtil.setPersistenceService(genericService);
		
		collNumberGenerator = new CollectionsNumberGenerator();
		collNumberGenerator.setScriptExecutionService(scriptExecutionService);
		collNumberGenerator.setSequenceGenerator(sequenceGenerator);
		collNumberGenerator.setCollectionsUtil(collectionsUtil);
	}
	
	private String formatDate(Date date,String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String formattedDate = formatter.format(date);
		return formattedDate;
	}
	
	@Test
	public void testReceiptNoGenerationStrategy() {
		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(new Date());
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		String receiptSequence = "SQ_RECEIPTHEADER_"+financialYear.getFinYearRange().substring(0, 4);
		List numbers = session.createSQLQuery("SELECT "+receiptSequence+".NEXTVAL FROM DUAL").list();
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		String receiptNo = collNumberGenerator.generateReceiptNumber(receiptHeader);
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
		
		assertEquals(formatDate(receiptHeader.getCreatedDate(),"MM")+"/"+financialYear.getFinYearRange()+"/"+sequence,
				receiptNo);
	}
	
	
	@Test
	public void testInternalRefNoForChequeGenerationStrategy() {
		
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNumber");
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(
				objectFactory.createRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CHEQUE),
				objectFactory.createEgwStatus("testStatus", CollectionConstants.MODULE_NAME_RECEIPTHEADER)));

		CFinancialYear financialYear = objectFactory.getFinancialYearForDate(
				receiptHeader.getCreatedDate());
		CFinancialYear currentFinancialYear = objectFactory.getFinancialYearForDate(
				receiptHeader.getCreatedDate());
		
		String chequeReferenceSequence = "SQ_RECEIPTREF"+financialYear.getFinYearRange();
		List numbers = session.createSQLQuery("SELECT "+chequeReferenceSequence+".NEXTVAL FROM DUAL").list();
		
		List actualInternalRefNos = collNumberGenerator.generateInternalReferenceNumber(
				receiptHeader, financialYear,currentFinancialYear);
		
		
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		
		String chequesequence= "000000"+(result.longValue()+1);
		chequesequence=chequesequence.substring(chequesequence.length()-6,chequesequence.length());
		String expectedResult = chequesequence+"/"+financialYear.getFinYearRange();
		assertEquals(2,actualInternalRefNos.size());
		assertEquals(" ",actualInternalRefNos.get(0));
		assertEquals(expectedResult,actualInternalRefNos.get(1));
	}
	
	@Test
	public void testInternalRefNoForMultipleChequesGenerationStrategy() {
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNumber");
		
		InstrumentType instrumentTypeCheque=objectFactory.createRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CHEQUE);
		EgwStatus status = objectFactory.createEgwStatus("testStatus", CollectionConstants.MODULE_NAME_RECEIPTHEADER);
		
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(instrumentTypeCheque,status));
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(instrumentTypeCheque,status));
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(instrumentTypeCheque,status));
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(instrumentTypeCheque,status));
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(instrumentTypeCheque,status));
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(instrumentTypeCheque,status));
		
		CFinancialYear financialYear = objectFactory.getFinancialYearForDate(receiptHeader.getCreatedDate());
		CFinancialYear currentFinancialYear = objectFactory.getFinancialYearForDate(new Date());
		
		String chequeReferenceSequence = "SQ_RECEIPTREF"+financialYear.getFinYearRange();
		List numbers = session.createSQLQuery("SELECT "+chequeReferenceSequence+".NEXTVAL FROM DUAL").list();
		
		List<String> actualInternalRefNos = collNumberGenerator.generateInternalReferenceNumber(
				receiptHeader, financialYear,currentFinancialYear);
		
		
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		
		String chequesequence= "000000"+(result.longValue()+1);
		chequesequence=chequesequence.substring(chequesequence.length()-6,chequesequence.length());
		String expectedResult = chequesequence+"/"+financialYear.getFinYearRange();
		assertEquals(2,actualInternalRefNos.size());
		assertEquals(" ",actualInternalRefNos.get(0));
		assertEquals(expectedResult,actualInternalRefNos.get(1));
	}
	
	@Test
	public void testInternalRefNoForCashGenerationStrategy() {
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNumber");
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(
				objectFactory.createRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CASH),
				objectFactory.createEgwStatus("testStatus", CollectionConstants.MODULE_NAME_RECEIPTHEADER)));
		
		CFinancialYear financialYear = objectFactory.getFinancialYearForDate(
				receiptHeader.getCreatedDate());
		CFinancialYear currentFinancialYear = objectFactory.getFinancialYearForDate(
				receiptHeader.getCreatedDate());
		String cashReferenceSequence = "SQ_RECEIPTREF"+financialYear.getFinYearRange();
		List numbers = session.createSQLQuery("SELECT "+cashReferenceSequence+".NEXTVAL FROM DUAL").list();
		
		List actualInternalRefNos = collNumberGenerator.generateInternalReferenceNumber(
				receiptHeader, financialYear,currentFinancialYear);
		
		
		
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		
		String cashsequence= "0000"+(result.longValue()+1);
		cashsequence=cashsequence.substring(cashsequence.length()-4,cashsequence.length());
		String expectedResult = cashsequence+"/"+financialYear.getFinYearRange();
		assertEquals(2,actualInternalRefNos.size());
		assertEquals(expectedResult,actualInternalRefNos.get(0));
		assertEquals(" ",actualInternalRefNos.get(1));
	}
	
	@Test
	public void testResetInternalRefNoForCashGenerationStrategy() {
		ReceiptHeader receiptHeader = objectFactory.createUnsavedReceiptHeader();
		receiptHeader.setReceiptnumber("ReceiptNumber");
		receiptHeader.addInstrument(objectFactory.createInstrumentHeader(
				objectFactory.createRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CASH),
				objectFactory.createEgwStatus("testStatus", CollectionConstants.MODULE_NAME_RECEIPTHEADER)));
		
		CFinancialYear financialYear = objectFactory.getFinancialYearForDate(
				receiptHeader.getCreatedDate());
		CFinancialYear currentFinancialYear = objectFactory.getFinancialYearForDate(
				receiptHeader.getCreatedDate());
		String cashReferenceSequence = "SQ_RECEIPTREF"+financialYear.getFinYearRange();
		List numbers = session.createSQLQuery("SELECT "+cashReferenceSequence+".NEXTVAL FROM DUAL").list();
		
		List actualInternalRefNos = collNumberGenerator.generateInternalReferenceNumber(
				receiptHeader, financialYear,currentFinancialYear);
		
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		
		String cashsequence= "0000"+(result.longValue()+1);
		cashsequence=cashsequence.substring(cashsequence.length()-4,cashsequence.length());
		String expectedResult = cashsequence+"/"+financialYear.getFinYearRange();
		assertEquals(2,actualInternalRefNos.size());
		assertEquals(expectedResult,actualInternalRefNos.get(0));
		assertEquals(" ",actualInternalRefNos.get(1));
	}
	
	
	@Test
	public void testChallanNoGenerationStrategy() {
		Challan challan = objectFactory.createUnsavedChallan();
		
		CFinancialYear financialYear = objectFactory.getFinancialYearForDate(
				challan.getCreatedDate());
		
		UserImpl user = objectFactory.createUser("system",department);
		challan.setCreatedBy(user);
		
		String challanSequence = "SQ_CHALLAN_"+financialYear.getFinYearRange().substring(0, 4);
		List numbers = session.createSQLQuery("SELECT "+challanSequence+".NEXTVAL FROM DUAL").list();
		BigDecimal result;
		if(numbers!=null && !numbers.isEmpty())
			result=(BigDecimal)numbers.get(0);
		else
			result=new BigDecimal(0);
		String challanNo = collNumberGenerator.generateChallanNumber(challan,financialYear);
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
		assertEquals(challanNo,department.getDeptCode()+"/"+financialYear.getFinYearRange()+"/"+sequence);
	}*/
}
