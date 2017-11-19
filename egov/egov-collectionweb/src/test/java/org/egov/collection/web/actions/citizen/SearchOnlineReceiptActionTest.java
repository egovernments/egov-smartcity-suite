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
package org.egov.collection.web.actions.citizen;


public class SearchOnlineReceiptActionTest { /*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private SearchOnlineReceiptAction action;
	private CollectionObjectFactory objectFactory;
	
	@Before
	public void setupAction(){
		action = new SearchOnlineReceiptAction();
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testGetOnlineReceiptTransitionStatuses(){
		List<EgwStatus> expectedStatuses = action.getOnlineReceiptTransitionStatuses();
		
		List<String> statusCodes = new ArrayList<String>();
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS);
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED);
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED);
		List<EgwStatus> actualStatuses = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_STATUSES_FOR_MODULE_AND_CODES, 
				OnlinePayment.class.getSimpleName(),
				statusCodes);
		
		assertEquals(expectedStatuses, actualStatuses);
	}
	
	@Test
	public void testGetOnlineReceiptStatuses(){
		List<EgwStatus> expectedStatuses = action.getOnlineReceiptStatuses();
		List<EgwStatus> actualStatuses = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_ALL_STATUSES_FOR_MODULE, 
				OnlinePayment.class.getSimpleName());
		
		assertEquals(expectedStatuses, actualStatuses);
	}
	
	@Test
	public void testSearch(){
		OnlinePayment onlinePayment = objectFactory.createOnlinePayment();
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		action.prepare();
		
		action.setSearchTransactionStatus(onlinePayment.getStatus().getId());
		action.setReferenceId(onlinePayment.getReceiptHeader().getId());
		action.setServiceTypeId(onlinePayment.getReceiptHeader().getService().getId());
		
		Calendar newTodate= Calendar.getInstance();
		newTodate.setTime(onlinePayment.getCreatedDate());
		newTodate.add(Calendar.DATE, -1);
		action.setFromDate(newTodate.getTime());
		
		action.setToDate(onlinePayment.getCreatedDate());
		
		assertEquals(action.search(),BaseFormAction.SUCCESS);
		
		assertTrue(action.getResults().contains(onlinePayment));
		assertEquals(action.getTarget(),"searchresult");
	}
	
	@Test
	public void testSearchWithOnlyReferenceId() {
		OnlinePayment onlinePayment = objectFactory.createOnlinePayment();
		action.setReferenceId(onlinePayment.getReceiptHeader().getId());
		
		assertEquals(action.search(),BaseFormAction.SUCCESS);
		assertTrue(action.getResults().contains(onlinePayment));
	}
	
	@Test
	public void testSearchWithOnlyServiceId() {
		OnlinePayment onlinePayment = objectFactory.createOnlinePayment();
		action.setServiceTypeId(onlinePayment.getReceiptHeader().getService().getId());
		
		assertEquals(action.search(),BaseFormAction.SUCCESS);
		assertTrue(action.getResults().contains(onlinePayment));
	}
	
	@Test 
	public void testReset()
	{
		assertEquals("success", action.reset());
	}
	
	@Test
	public void testGetModel()
	{
		assertNull(action.getModel());
	}
*/}
