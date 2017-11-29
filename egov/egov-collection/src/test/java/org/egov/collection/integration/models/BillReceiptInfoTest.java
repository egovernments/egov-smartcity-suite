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