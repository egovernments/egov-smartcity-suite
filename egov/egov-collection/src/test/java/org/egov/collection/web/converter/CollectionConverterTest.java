package org.egov.erpcollection.web.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;
import org.egov.erpcollection.integration.models.ReceiptInstrumentInfo;
import org.egov.erpcollection.integration.services.BillingIntegrationServiceStub;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.util.FinancialsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.erpcollection.web.xml.converter.BillReceiptInfoConverter;
import org.egov.erpcollection.web.xml.converter.ReceiptAccountInfoConverter;
import org.egov.erpcollection.web.xml.converter.ReceiptInstrumentInfoConverter;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.utils.FinancialConstants;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CollectionConverterTest  extends AbstractPersistenceServiceTest<ReceiptPayeeDetails, Long>{
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
}
