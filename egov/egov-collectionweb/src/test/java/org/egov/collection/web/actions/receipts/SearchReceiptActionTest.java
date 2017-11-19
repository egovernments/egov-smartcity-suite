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
package org.egov.collection.web.actions.receipts;


public class SearchReceiptActionTest { /*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private SearchReceiptAction action;
	private CollectionObjectFactory objectFactory;
	Map<String, String[]> parameters = new HashMap<String, String[]>();
	
	@Before
	public void setupAction(){
		action = new SearchReceiptAction();
		action.setParameters(parameters);
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@After
	public void resetPrepare(){
		action.setParameters(null);
	}
	
	@Test
	public void prepareAction() {
		assertNotNull(action.getDropdownData().containsKey("serviceTypeList"));
	}
	
	@Test
	public void testPrepareAction(){
		List<Location> actualCounterList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_COUNTERS);
		List<InstrumentType> actualInstrumentTypeList = genericService.findAllBy("from InstrumentType i where i.isActive = true order by type");
		List<InstrumentType> actualUserList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS);
		
		action.prepare();
		assertEquals(actualCounterList,action.getDropdownData().get("counterList"));
		assertEquals(actualInstrumentTypeList,action.getDropdownData().get("instrumentTypeList"));
		assertEquals(actualUserList,action.getDropdownData().get("userList"));
	}
	
	@Test
	public void testSearchByUser(){
		objectFactory.createReceiptPayeeDetails();
		action.prepare();
		int userId = ((UserImpl)action.getDropdownData().get("userList").get(0)).getId();
		action.setUserId(userId);
		action.search();
		List<ReceiptHeader> actualSearchResults = genericService.findAllBy(
				"from org.egov.collection.entity.ReceiptHeader receipt where receipt.createdBy.id = ?", userId);
		assertTrue(actualSearchResults.containsAll(action.getSearchResult().getList()));
		assertEquals(action.getTarget(),"searchresult");
	}
	
	
	@Test
	public void searchWithOnlyReceiptNumber() {
		action.setReceiptNumber("RN");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithSomeParams() {
		action.setReceiptNumber("RN");
		action.setSearchStatus(659);
		action.setServiceTypeId(1);
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithAllParams() {
		action.setReceiptNumber("RN");
		action.setFromDate(new Date(2009,1,1));
		action.setToDate(new Date(2009,1,1));
		action.setSearchStatus(659);
		action.setCounterId(4);
		action.setServiceTypeId(1);
		action.setInstrumentType("cheque");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithInstrumentType() {
		action.setInstrumentType("cheque");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithNoParams() {
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void testGetReceiptStatuses() {
		List<EgwStatus> expectedStatuses=genericService.findAllBy(
				"from EgwStatus s where moduletype=? and code != ? order by description",
				ReceiptHeader.class.getSimpleName(), CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		
		List<EgwStatus> actualStatuses=action.getReceiptStatuses();
		assertTrue(actualStatuses.containsAll(expectedStatuses));
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
