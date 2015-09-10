package org.egov.collection.integration.models;


/**
 * The bill receipt information class. Provides details of a bill receipt.
 */
public class BillReceiptInfoTest {/* extends AbstractPersistenceServiceTest<ReceiptPayeeDetails, Long> {
	private CollectionObjectFactory objectFactory;
	private EgovCommon egovCommon;
	
	@Before
	public void setupService(){
		objectFactory = new CollectionObjectFactory(session,genericService);
		egovCommon = createMock(EgovCommon.class);
	}
	
	
	@Test
	public void testConstructBillReceiptInfoFromReceiptHeader(){
		
		ReceiptPayeeDetails payeeDetails = objectFactory.createReceiptPayeeDetails();
		ReceiptHeader header = payeeDetails.getReceiptHeaders().iterator().next();
		
		EgwStatus receiptCreatedStatus = (EgwStatus) genericService.find("from EgwStatus where code=?",
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED);
		
		header.setStatus(receiptCreatedStatus);
		
		
		header.addInstrument(objectFactory.createBankInstrumentHeader());
		
		header.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);
		//header.addReceiptDetail(objectFactory.createReceiptDetailWithoutHeader());
		
		session.saveOrUpdate(header);
		
		ReceiptDetail receiptDetail = header.getReceiptDetails().iterator().next();
		Iterator<InstrumentHeader> iterator = header.getReceiptInstrument().iterator();
		InstrumentHeader instrHeader = iterator.next();
		boolean actualIsRevenueAccountHead=FinancialsUtil.isRevenueAccountHead(receiptDetail.getAccounthead(),FinancialsUtil.getBankChartofAccountCodeList());
		

		BillReceiptInfo billReceiptInfo = new BillReceiptInfoImpl(header);
		ReceiptAccountInfo receiptAccountInfo = billReceiptInfo.getAccountDetails().iterator().next();
		ReceiptInstrumentInfo receiptInstrInfo = billReceiptInfo.getInstrumentDetails().iterator().next();
		
		assertEquals(billReceiptInfo.getServiceName(),header.getService().getServiceName());
		assertEquals(billReceiptInfo.getPaidBy(),header.getPaidBy());
		assertEquals(billReceiptInfo.getDescription(),header.getReferenceDesc());
		assertEquals(billReceiptInfo.getTotalAmount(),header.getAmount());
		assertEquals(billReceiptInfo.getCreatedBy(),header.getCreatedBy());
		assertEquals(billReceiptInfo.getModifiedBy(),header.getModifiedBy());
		assertEquals(billReceiptInfo.getReceiptURL(),CollectionConstants.RECEIPT_VIEW_SOURCEPATH+header.getId());
		assertEquals(billReceiptInfo.getCollectionType(),header.getCollectiontype().toString());
		
		assertEquals(receiptAccountInfo.getAccountName(),receiptDetail.getAccounthead().getName());
		assertEquals(receiptAccountInfo.getGlCode(),receiptDetail.getAccounthead().getGlcode());
		assertEquals(receiptAccountInfo.getFunction(),receiptDetail.getFunction().getCode());
		assertEquals(receiptAccountInfo.getFunctionName(),receiptDetail.getFunction().getName());
		assertEquals(receiptAccountInfo.getIsRevenueAccount(),actualIsRevenueAccountHead);
		assertEquals(receiptAccountInfo.getOrderNumber(),receiptDetail.getOrdernumber());
		assertEquals(receiptAccountInfo.getDescription(),receiptDetail.getDescription());
		//assertEquals(receiptAccountInfo.getFinancialYear(),receiptDetail.getFinancialYear().getFinYearRange());
		
		assertEquals(receiptInstrInfo.getInstrumentNumber(),instrHeader.getInstrumentNumber());
		assertEquals(receiptInstrInfo.getInstrumentDate(),instrHeader.getInstrumentDate());
		assertEquals(receiptInstrInfo.getInstrumentType(),instrHeader.getInstrumentType().getType());
		assertEquals(receiptInstrInfo.getInstrumentAmount(),instrHeader.getInstrumentAmount());
		assertEquals(receiptInstrInfo.getTransactionDate(),instrHeader.getTransactionDate());
		assertEquals(receiptInstrInfo.getTransactionNumber(),instrHeader.getTransactionNumber());
		assertEquals(receiptInstrInfo.getBankName(),instrHeader.getBankAccountId().getBankbranch().getBank().getName());
		assertEquals(receiptInstrInfo.getBankBranchName(),instrHeader.getBankBranchName());
		assertEquals(receiptInstrInfo.getBankAccountNumber(),instrHeader.getBankAccountId().getAccountnumber());
	}
	
	@Test
	public void testConstructBillReceiptInfoFromChallanReceiptHeader() throws ApplicationException{
		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderForChallan();
		ReceiptHeader receiptHeaderref=objectFactory.createReceiptHeaderWithChallan();
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).andReturn(null).anyTimes();
		replay(egovCommon);
		BillReceiptInfoImpl billReceiptInfo = new BillReceiptInfoImpl(receiptHeader,egovCommon,receiptHeaderref);
		//ChallanInfo challan = new ChallanInfo(receiptHeader,egovCommon,new ReceiptHeader());
		
		assertTrue(billReceiptInfo.getChallanDetails().contains(billReceiptInfo.getChallan()));
	}*/
}