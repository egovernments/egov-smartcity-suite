/**
 * 
 */
package org.egov.erpcollection.integration.services;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.BillReceiptInfoImpl;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test class for testing the billing integration service 
 * implementation.
 */
public class BillingIntegrationServiceStubTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails, Long> {
	/**
	 * The collection integration service
	 */
	private BillingIntegrationServiceStub billingIntgrnServiceStub;

	/**
	 * The collection object factory
	 */
	private CollectionObjectFactory objectFactory;

	@Before  
	public void setupService() {
		objectFactory = new CollectionObjectFactory(session);
		billingIntgrnServiceStub = new BillingIntegrationServiceStub();
	}
	
	@After
	public void tearDownService() {
		Method method;
		try {
			method = BillingIntegrationServiceStub.class.getDeclaredMethod("getOutputFile",String.class);
			method.setAccessible(true);
			File deleteFile = (File) method.invoke(billingIntgrnServiceStub, "BillReceiptOutput.xml");
			if (deleteFile.exists()){
				if(!deleteFile.delete()) {
					throw new Exception("Could not delete BillReceiptOutput.xml");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
/*	@Test
	public void testUpdateReceiptDetailsExceptions() {
		billingIntgrnServiceStub = new BillingIntegrationServiceStub(){
			protected File getOutputFile(String fileName) {
			  try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();   
			}
			return null;
			 }
		};
		
		billingIntgrnServiceStub.updateReceiptDetails(createBillReceipts());
	}*/
	
	private Set<BillReceiptInfo> createBillReceipts(){
		ReceiptPayeeDetails payeeDetails = objectFactory.createReceiptPayeeForBillingSystem();
		Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>();
		
		for(ReceiptHeader receiptHeader : payeeDetails.getReceiptHeaders()){
			billReceipts.add(new BillReceiptInfoImpl(receiptHeader));
			
		}
		
		return billReceipts;
	}
	
	@Test
	public void testUpdateReceiptDetails() throws Exception{
		
			try{
			billingIntgrnServiceStub.updateReceiptDetails(createBillReceipts());
		}catch(Exception exp){
			throw new Exception("Could not delete update billing receiptdetails");
		}	
	}

}
