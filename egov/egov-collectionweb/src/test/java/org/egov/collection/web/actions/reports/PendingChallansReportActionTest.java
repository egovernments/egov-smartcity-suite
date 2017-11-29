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

package org.egov.collection.web.actions.reports;


/**
 * JUnit test cases for the bank remittance report action
 */
public class PendingChallansReportActionTest{/* extends
		AbstractPersistenceServiceTest {
	
	private PendingChallansReportAction action;
	private CollectionObjectFactory objectFactory;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setupAction() {
		objectFactory = new CollectionObjectFactory(session);
		
		action = new PendingChallansReportAction();
		action.setPersistenceService(genericService);
		action.setSession(new HashMap());
	}
	
	@Test
	public void testPrepare() {
		action.prepare();
		assertEquals(FileFormat.PDF, action.getReportFormat());
		assertEquals(ReportDataSourceType.HQL, action.getDataSourceType());
	}
	
	@Test
	public void testGetModel() {
		assertNull(action.getModel());
	}

	@Test
	public void testSetGet() {
		Integer deptId = 1;
		action.setDeptId(deptId);
		assertEquals(deptId, action.getDeptId());
		
		Long challanServiceId = 1L;
		action.setChallanServiceId(challanServiceId);
		assertEquals(challanServiceId, action.getChallanServiceId());
		
		Date testDate = new Date();
		action.setFromDate(testDate);
		assertEquals(testDate, action.getFromDate());		
		action.setToDate(testDate);
		assertEquals(testDate, action.getToDate());
	}
	
	@Test
	public void testGetReportTemplate() {
		Assert.assertEquals(CollectionConstants.REPORT_TEMPLATE_PENDING_CHALLANS, action.getReportTemplateName());
	}

	@Test
	public void testCriteria() {
		// Create mock object of collections util and set expectations
		CollectionsUtil collectionsUtilMock = EasyMock.createMock(CollectionsUtil.class);
		action.setCollectionsUtil(collectionsUtilMock);
		
		List<ServiceDetails> challanServiceList = new ArrayList<ServiceDetails>();
		EasyMock.expect(collectionsUtilMock.getChallanServiceList()).andReturn(challanServiceList);
		
		Department dept = objectFactory.createDept("test.dept");
		EasyMock.expect(collectionsUtilMock.getDepartmentOfLoggedInUser(EasyMock.isA(Map.class))).andReturn(dept);
		EasyMock.replay(collectionsUtilMock);
		
		// Test the criteria method
		String critResult = action.criteria();
		EasyMock.verify(collectionsUtilMock);
		
		assertEquals(critResult, BaseFormAction.INDEX);
		assertEquals(dept.getId(), action.getDeptId());
		assertTrue(action.getDropdownData().get(CollectionConstants.DROPDOWN_DATA_SERVICE_LIST).equals(challanServiceList));
		assertTrue(action.getDropdownData().get("departmentList").contains(dept));
		
		Date fromDate = action.getFromDate();
	}
*/}
