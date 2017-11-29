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
package org.egov.collection.web.converter;


public class CollectionConverterTest {/*  extends AbstractPersistenceServiceTest<ReceiptPayeeDetails, Long>{
	private CollectionObjectFactory objectFactory;
	private BillingIntegrationServiceStub billingIntgrnServiceStub;
	
	@Before
	public void setupService(){
		objectFactory = new CollectionObjectFactory(session);
		billingIntgrnServiceStub = new BillingIntegrationServiceStub();
	}
	
	@Test
	public void testBillReceiptInfoConverter() throws SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		ReceiptPayeeDetails payeeDetails = objectFactory.createReceiptPayeeForBillingSystem();
		EgwStatus status = (EgwStatus) genericService.findByNamedQuery(
				"getEgwStatusByModuleAndCode",
				CollectionConstants.MODULE_NAME_RECEIPTHEADER,
				CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED);
		Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>();
		for(ReceiptHeader receiptHeader : payeeDetails.getReceiptHeaders()){
			receiptHeader.setStatus(status);
			BillReceiptInfoImpl billRecInfo = new BillReceiptInfoImpl(receiptHeader);
			billReceipts.add(billRecInfo);
		}
		
		XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new BillReceiptInfoConverter());
        xStream.registerConverter(new ReceiptAccountInfoConverter());
        xStream.registerConverter(new ReceiptInstrumentInfoConverter());
        xStream.alias("Bill-Receipt", BillReceiptInfoImpl.class);
        String expectedXML=xStream.toXML(billReceipts);
        
		Method method= BillingIntegrationServiceStub.class.getDeclaredMethod("convertToXML",Set.class);
		method.setAccessible(true);
		String actualXML = (String) method.invoke(billingIntgrnServiceStub, billReceipts);

		assertEquals(expectedXML,actualXML);
	}
	
	@Test
	public void testConstructBillReceiptInfoFromReceiptHeader(){
		EgwStatus receiptCancelledstatus = (EgwStatus) genericService.find("from EgwStatus where code=?",CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
		
		ReceiptPayeeDetails payeeDetails = objectFactory.createReceiptPayeeForBillingSystem();
		ReceiptHeader header = payeeDetails.getReceiptHeaders().iterator().next();
		if(receiptCancelledstatus!=null)
			header.setStatus(receiptCancelledstatus);
		
		ReceiptDetail receiptDetail = header.getReceiptDetails().iterator().next();
		InstrumentHeader instrHeader = header.getReceiptInstrument().iterator().next();
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
		
		assertEquals(receiptAccountInfo.getAccountName(),receiptDetail.getAccounthead().getName());
		assertEquals(receiptAccountInfo.getGlCode(),receiptDetail.getAccounthead().getGlcode());
		assertEquals(receiptAccountInfo.getFunction(),receiptDetail.getFunction().getCode());
		assertEquals(receiptAccountInfo.getFunctionName(),receiptDetail.getFunction().getName());
		assertEquals(receiptAccountInfo.getIsRevenueAccount(),actualIsRevenueAccountHead);
		assertEquals(receiptAccountInfo.getOrderNumber(),receiptDetail.getOrdernumber());
		assertEquals(receiptAccountInfo.getDescription(),receiptDetail.getDescription());
		
		assertEquals(receiptInstrInfo.getInstrumentNumber(),instrHeader.getInstrumentNumber());
		assertEquals(receiptInstrInfo.getInstrumentDate(),instrHeader.getInstrumentDate());
		assertEquals(receiptInstrInfo.getInstrumentType(),instrHeader.getInstrumentType().getType());
		assertEquals(receiptInstrInfo.getInstrumentAmount(),instrHeader.getInstrumentAmount());
		assertEquals(receiptInstrInfo.getBankName(),null);
		assertEquals(receiptInstrInfo.getBankBranchName(),instrHeader.getBankBranchName());
	}
	
	@Test
	public void testConstructBillReceiptInfoForBouncedInstruments(){
		EgwStatus instrumentDishonoredstatus = (EgwStatus) genericService.find("from EgwStatus status where status.moduletype=? and status.description=?", CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,FinancialConstants.INSTRUMENT_DISHONORED_STATUS);//(CollectionConstants.QUERY_STATUS_BY_MODULE_AND_CODE, );//CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,CollectionConstants.INSTRUMENT_DEPOSITED_STATUS);
		InstrumentHeader dishonoredInstrHeader = objectFactory.createUnsavedInstrumentHeaderWithBankDetails(
				objectFactory.createInstrumentType("testType"), "123456", 1000.0, new Date(), 
				instrumentDishonoredstatus, objectFactory.createBank(), "testBranchName","0");
		
		ReceiptHeader header = objectFactory.createReceiptHeader("testReceiptNumber");
		EgwStatus receiptInstrBouncedtatus = (EgwStatus) genericService.find("from EgwStatus where code=?",CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED);
		header.setStatus(receiptInstrBouncedtatus);
		header.addInstrument(dishonoredInstrHeader);
		BillReceiptInfo billReceiptInfo = new BillReceiptInfoImpl(header);
		ReceiptInstrumentInfo receiptInstrInfo = billReceiptInfo.getInstrumentDetails().iterator().next();
		
		assertEquals(receiptInstrInfo.getInstrumentNumber(),dishonoredInstrHeader.getInstrumentNumber());
		assertEquals(receiptInstrInfo.getInstrumentDate(),dishonoredInstrHeader.getInstrumentDate());
		assertEquals(receiptInstrInfo.getInstrumentType(),dishonoredInstrHeader.getInstrumentType().getType());
		assertEquals(receiptInstrInfo.getInstrumentAmount(),dishonoredInstrHeader.getInstrumentAmount());
		assertEquals(receiptInstrInfo.getBankName(),dishonoredInstrHeader.getBankId().getName());
		assertEquals(receiptInstrInfo.getBankBranchName(),dishonoredInstrHeader.getBankBranchName());
		assertTrue(billReceiptInfo.getBouncedInstruments().contains(receiptInstrInfo));
	}
*/}
