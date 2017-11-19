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

/**
 * 
 */
package org.egov.collection.integration.services;

import org.egov.collection.entity.CollectionObjectFactory;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.lang.reflect.Method;

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

		}
	}
	
/*	@Test
	public void testUpdateReceiptDetailsExceptions() {
		billingIntgrnServiceStub = new BillingIntegrationServiceStub(){
			protected File getOutputFile(String fileName) {
			  try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {


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
