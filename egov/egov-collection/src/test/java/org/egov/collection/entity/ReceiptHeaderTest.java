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

package org.egov.collection.entity;

public class ReceiptHeaderTest { /*extends AbstractPersistenceServiceTest<ReceiptHeader,Long> {
	private CollectionObjectFactory objectFactory;
	
	public ReceiptHeaderTest(){
		this.type=ReceiptHeader.class;
	}
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testCreateReceiptHeader()
	{
		ReceiptHeader saved = (ReceiptHeader) service.create( objectFactory.createUnsavedReceiptHeader());
		ReceiptHeader retrieved = (ReceiptHeader) service.findById(saved.getId(), false);
		assertEquals(saved,retrieved);
	}
	
	@Test
	public void testCreateReceiptHeaderWithReceiptMisc()
	{
		ReceiptHeader unsavedreceiptHeader = objectFactory.createUnsavedReceiptHeader();
		ReceiptHeader saved = (ReceiptHeader) service.create(unsavedreceiptHeader);
		ReceiptHeader retrieved = (ReceiptHeader) service.findById(saved.getId(), false);
		assertEquals(saved,retrieved);
		
		assertEquals(saved.getReceiptMisc(), retrieved.getReceiptMisc());
	}
	
	@Test
	public void testFindReceiptsByReconStatus(){		
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptNo");
		List<ReceiptHeader> receipts = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_RECEIPTS_BY_RECONSTATUS, 
				receiptHeader.getIsReconciled());
		assertTrue("Unexpected receipt found",receipts.contains(receiptHeader));
	}
	
	 @Test
	public void testFindReceiptsForVouchersTest(){		
		ReceiptVoucher receiptVoucher = objectFactory.createReceiptVoucher();
		ReceiptHeader receiptHeader = receiptVoucher.getReceiptHeader();
		CVoucherHeader voucherHeader=receiptVoucher.getVoucherheader();
		List<ReceiptHeader> receipts = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_RECEIPTS_FOR_VOUCHERS, Arrays.asList(voucherHeader.getId()));
		assertTrue("Unexpected receipt found",receipts.contains(receiptHeader));
	}
	
	@Test
	public void testGetCreatedByUsersForReceipts(){
		ReceiptHeader receiptHeader1 = objectFactory.createReceiptHeader("testReceiptNo");
		objectFactory.createReceiptHeader("testReceiptNo2", 'A', "testRefNo", "testStatusCode2", receiptHeader1.getCreatedBy(),null);
		
		List<UserImpl> createdByList = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS);
		
		if(createdByList.size()>2){
			Set<UserImpl> createdBySet = new HashSet<UserImpl>();
			createdBySet.addAll(createdByList);
			//to ensure that only distinct elements are returned
			assertTrue(createdByList.size()==createdBySet.size());
		}
	}
	
	
	@Test
	public void testGetInstrumentsAsString(){
		Date instrDate = new Date();
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(instrDate);
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptNo");
		UserImpl testUser = objectFactory.createUser("testUser");
		InstrumentHeader cashInstrument = objectFactory.createInstrumentHeader(objectFactory.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CASH),"111",1000.0,instrDate, CollectionConstants.INSTRUMENT_NEW_STATUS,testUser);
		InstrumentHeader cardInstrument = objectFactory.createInstrumentHeader(objectFactory.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CARD),"222",1000.0,instrDate, CollectionConstants.INSTRUMENT_NEW_STATUS,testUser);
		InstrumentHeader chequeInstrument = objectFactory.createInstrumentHeader(objectFactory.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CHEQUE),"333",1000.0,instrDate, CollectionConstants.INSTRUMENT_NEW_STATUS,testUser);
		InstrumentHeader ddInstrument = objectFactory.createInstrumentHeader(objectFactory.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_DD),"444",1000.0,instrDate, CollectionConstants.INSTRUMENT_NEW_STATUS,testUser);
		InstrumentHeader bankInstrument = objectFactory.createInstrumentHeader(objectFactory.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_BANK),"444",1000.0,instrDate, CollectionConstants.INSTRUMENT_NEW_STATUS,testUser);
		bankInstrument.setTransactionNumber("testTransactionNumber");
		
		receiptHeader.addInstrument(cashInstrument);
		receiptHeader.addInstrument(cardInstrument);
		receiptHeader.addInstrument(chequeInstrument);
		receiptHeader.addInstrument(ddInstrument);
		receiptHeader.addInstrument(bankInstrument);
		
		String instrumentsAsString = receiptHeader.getInstrumentsAsString();
		assertTrue(instrumentsAsString.indexOf(cashInstrument.getInstrumentType().getType() + " - " + cashInstrument.getInstrumentAmount())!=-1);
		assertTrue(instrumentsAsString.indexOf(cardInstrument.getInstrumentType().getType() + " # " + cardInstrument.getInstrumentNumber() + " - " + cardInstrument.getInstrumentAmount())!=-1);
		assertTrue(instrumentsAsString.indexOf(chequeInstrument.getInstrumentType().getType() + " # " + chequeInstrument.getInstrumentNumber() + " - " + formattedDate + " - " + chequeInstrument.getInstrumentAmount())!=-1);
		assertTrue(instrumentsAsString.indexOf(ddInstrument.getInstrumentType().getType() + " # " + ddInstrument.getInstrumentNumber() + " - " + formattedDate + " - " + ddInstrument.getInstrumentAmount())!=-1);
		assertTrue(instrumentsAsString.indexOf(bankInstrument.getInstrumentType().getType() + " # " + bankInstrument.getTransactionNumber() + " - " +  bankInstrument.getInstrumentAmount())!=-1);
	}
	
	@Test
	public void testGetInstruments(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptNo");
		Date instrDate = new Date();
		UserImpl testUser = objectFactory.createUser("testUser");
		InstrumentHeader cashInstrument = objectFactory.createInstrumentHeader(objectFactory.createUnsavedRegularInstrumentType(CollectionConstants.INSTRUMENTTYPE_CASH),"111",1000.0,instrDate, CollectionConstants.INSTRUMENT_NEW_STATUS,testUser);

		receiptHeader.addInstrument(cashInstrument);
		
		assertTrue(receiptHeader.getInstruments(cashInstrument.getInstrumentType().getType()).size()==1);
		assertTrue(receiptHeader.getInstruments(cashInstrument.getInstrumentType().getType()).contains(cashInstrument));
		assertFalse(receiptHeader.getInstruments("dummy type").contains(cashInstrument));
	}
	
	@Test
	public void testGetStateDetails(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptNo");
		receiptHeader.setLocation(objectFactory.createCounter("testCounterName"));
		String stateDetails = receiptHeader.getService().getServiceName() + CollectionConstants.SEPARATOR_HYPHEN + receiptHeader.getCreatedBy().getUserName() + CollectionConstants.SEPARATOR_HYPHEN + receiptHeader.getLocation().getName();
		assertEquals(stateDetails,receiptHeader.getStateDetails());
	}
	
	@Test
	public void testMyLinkId(){
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeader("testReceiptNo");
		receiptHeader.setState(objectFactory.createState(receiptHeader.getCreatedBy()));
		receiptHeader.setLocation(objectFactory.createCounter("testCounterName"));
		String myLinkId = receiptHeader.getCurrentState().getNextAction()
		+ CollectionConstants.SEPARATOR_HYPHEN + receiptHeader.getService().getCode()
		+ CollectionConstants.SEPARATOR_HYPHEN
		+ receiptHeader.getCreatedBy().getUserName()
		+ CollectionConstants.SEPARATOR_HYPHEN + receiptHeader.getLocation().getId();
		assertEquals(myLinkId,receiptHeader.myLinkId());
	}
	
	@Test
	public void testFindReceiptForChallanNo(){		
		ReceiptHeader receiptHeader = objectFactory.createReceiptHeaderWithChallan();
		State prevstate = objectFactory.createState("Type-Challan","VALIDATED");
		State endstate = objectFactory.createState("Type-Challan","END");
		receiptHeader.getChallan().changeState(prevstate);
		receiptHeader.getChallan().changeState(endstate);
		session.saveOrUpdate(receiptHeader);
		
		ReceiptHeader expected = (ReceiptHeader) genericService.findByNamedQuery(
				CollectionConstants.QUERY_VALIDRECEIPT_BY_CHALLANNO,
				receiptHeader.getChallan().getChallanNumber());
		
		assertEquals(receiptHeader,expected);
	}
	
	@Test
	public void testFindChallanNoForReceipt(){		
		ReceiptHeader testReceiptHeader1 = objectFactory.createReceiptHeaderForChallan();
		ReceiptHeader receiptHeader1 = objectFactory.createReceiptHeader("testChildChallanNo");
		ReceiptHeader receiptHeader2 = objectFactory.createReceiptHeaderWithChallan();
		
		Set<ReceiptHeader> receiptHeaders1 = new LinkedHashSet<ReceiptHeader>(0);
		
		Set<ReceiptHeader> receiptHeaders2 = new LinkedHashSet<ReceiptHeader>(0);
		receiptHeaders2.add(receiptHeader2);
		
		receiptHeader1.setReceiptHeaders(receiptHeaders2);
		receiptHeaders1.add(receiptHeader1);
		
		testReceiptHeader1.setReceiptHeaders(receiptHeaders1);
		assertEquals(receiptHeader2.getChallan().getChallanNumber(),testReceiptHeader1.getReceiptChallanNumber());
		
		ReceiptHeader testReceiptHeader2 = objectFactory.createReceiptHeaderWithChallan();
		assertEquals("testChallanNo",testReceiptHeader2.getReceiptChallanNumber());
	}
*/
}
