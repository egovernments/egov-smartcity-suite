/**
 * 
 */
package org.egov.collection.integration.services;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.egov.collection.entity.CollectionObjectFactory;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test class for testing the billing integration service 
 * implementation.
 */
public class BillingIntegrationServiceStubTest  {
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
		objectFactory = null ;//= new CollectionObjectFactory(session);
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
	
	/*private Set<BillReceiptInfo> createBillReceipts(){
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
	}*/

}
