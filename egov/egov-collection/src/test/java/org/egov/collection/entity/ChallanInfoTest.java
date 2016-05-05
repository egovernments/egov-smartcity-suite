package org.egov.collection.entity;

public class ChallanInfoTest  { /*extends AbstractPersistenceServiceTest {
	private CollectionObjectFactory objectFactory;
	private EgovCommon egovCommon;
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
		egovCommon = createMock(EgovCommon.class);
	}
	
	@Test
	public void testChallanInfoWithChildObj() throws ApplicationException
	{
		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderForChallan();
		ReceiptHeader receiptHeaderref=objectFactory.createReceiptHeaderWithChallan();
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).
			andReturn(null).anyTimes();
		replay(egovCommon);
		
		ChallanInfo challanInfo = new ChallanInfo(receiptHeader,egovCommon,receiptHeaderref);
		
		assertEquals(challanInfo.getAccountDetails().iterator().next().getGlCode().substring(0, 6),
				receiptHeader.getReceiptDetails().iterator().next().getAccounthead().getGlcode().substring(0, 6));
		assertEquals(challanInfo.getAccountPayeeDetails().iterator().next().getGlCode(),
				receiptHeader.getReceiptDetails().iterator().next().
					getAccountPayeeDetails().iterator().next().
						getReceiptDetail().getAccounthead().getGlcode());
		assertTrue(challanInfo.getInstrumentDetails().isEmpty());
		
		assertEquals(challanInfo.getBillReferenceNum(),receiptHeader.getReferencenumber());
		assertEquals(challanInfo.getDescription(),receiptHeader.getReferenceDesc());
		assertEquals(challanInfo.getFunctionName(),receiptHeader.getReceiptDetails().iterator().next().getFunction().getName());
		assertEquals(challanInfo.getPayeeAddress(),receiptHeader.getReceiptPayeeDetails().getPayeeAddress());
		assertEquals(challanInfo.getPayeeName(),receiptHeader.getReceiptPayeeDetails().getPayeename());
		assertEquals(challanInfo.getReceiptMisc(),receiptHeader.getReceiptMisc());
		assertEquals(challanInfo.getServiceName(),receiptHeader.getService().getServiceName());
		assertEquals(challanInfo.getTotalAmount(),receiptHeader.getTotalAmount());
		
		
		assertEquals(challanInfo.getChallanDate(),receiptHeaderref.getChallan().getChallanDate());
		assertEquals(challanInfo.getChallanNumber(),receiptHeaderref.getChallan().getChallanNumber());
		assertEquals(challanInfo.getChallanServiceName(),receiptHeaderref.getChallan().getService().getServiceName());
		assertEquals(challanInfo.getChallanStatus(),receiptHeaderref.getChallan().getStatus());
		assertEquals(challanInfo.getCreatedBy(),receiptHeaderref.getChallan().getCreatedBy());
		assertEquals(challanInfo.getChallanVoucherNum(),receiptHeaderref.getChallan().getVoucherHeader().getVoucherNumber());
	}
	
	@Test
	public void testChallanInfoWithChallanReceipt() throws ApplicationException
	{
		ReceiptHeader receiptHeader=objectFactory.createReceiptHeaderWithChallan();
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).
			andReturn(null).anyTimes();
		replay(egovCommon);
		
		ChallanInfo challanInfo = new ChallanInfo(receiptHeader,egovCommon,new ReceiptHeader());
		
		assertEquals(challanInfo.getChallanDate(),receiptHeader.getChallan().getChallanDate());
		assertEquals(challanInfo.getChallanNumber(),receiptHeader.getChallan().getChallanNumber());
		assertEquals(challanInfo.getChallanServiceName(),receiptHeader.getChallan().getService().getServiceName());
		assertEquals(challanInfo.getChallanStatus(),receiptHeader.getChallan().getStatus());
		assertEquals(challanInfo.getCreatedBy(),receiptHeader.getChallan().getCreatedBy());
		assertEquals(challanInfo.getChallanVoucherNum(),receiptHeader.getChallan().getVoucherHeader().getVoucherNumber());
	}*/
}
