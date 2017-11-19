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

package org.egov.collection.workflow.renderer;


public class CollectionsWorkflowRenderServiceTest  {/* extends
		AbstractPersistenceServiceTest<ReceiptHeader, Long> {

	private CollectionsWorkflowRenderService renderService;
	private ReceiptHeader mockReceiptHeader;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		PersistenceService<ReceiptHeader, Long> persistenceService = mock(PersistenceService.class);
		Session session = mock(Session.class);
		Query query = mock(Query.class);

		ArrayList<StateAware> assignedItems = new ArrayList<StateAware>();
		mockReceiptHeader = mock(ReceiptHeader.class);
		EgwStatus mockStatus = mock(EgwStatus.class);
		when(mockReceiptHeader.myLinkId()).thenReturn("testLinkId1")
				.thenReturn("testLinkId2");
		when(mockReceiptHeader.getStatus()).thenReturn(mockStatus);
		when(mockStatus.getCode()).thenReturn(
				CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED)
				.thenReturn(CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED);
		assignedItems.add(mockReceiptHeader);
		assignedItems.add(mockReceiptHeader);

		when(query.list()).thenReturn(assignedItems);
		when(query.setString(anyString(), anyString())).thenReturn(null);

		when(session.createQuery(anyString())).thenReturn(query);
		when(persistenceService.getSession()).thenReturn(session);
		renderService = new CollectionsWorkflowRenderService(
				persistenceService, ReceiptHeader.class);
	}

	@Test
	public void testDraftItems() {
		// draft must return empty list
		assertEquals(renderService.getDraftWorkflowItems(1, 1, "testOrder"),
				Collections.emptyList());
	}

	@Test
	public void testFilteredItems() {
		// filtered must return empty list
		assertEquals(renderService.getFilteredWorkflowItems(1, 1, 1,
				new Date(), new Date()), Collections.emptyList());
	}

	@Test
	public void testAssignedItems() {
		// Assigned workflow items must return the mock receipt header we
		// created in setUp
		List<ReceiptHeader> assignedItems = renderService
				.getAssignedWorkflowItems(1, 1, null);
		assertNotNull(assignedItems);
		assertTrue(assignedItems.contains(mockReceiptHeader));
	}

	@Test
	public void testGetWorkflowItemsByCriteria() {
		Map<String, Object> critMap = new HashMap<String, Object>();
		critMap.put(WorkflowTypeService.WFTYPE, "test");
		List<ReceiptHeader> workflowItems = renderService
				.getWorkflowItems(critMap);
		assertNotNull(workflowItems);
		assertTrue(workflowItems.contains(mockReceiptHeader));
	}

	@Test
	public void testGetWorkflowItemsByMyLinkId() {
		assertTrue(renderService.getWorkflowItems("Approve-test-testuser-1").isEmpty());
	}*/
}
