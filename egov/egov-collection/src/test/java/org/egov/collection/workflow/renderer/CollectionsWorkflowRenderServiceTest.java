package org.egov.erpcollection.workflow.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.EgwStatus;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.inbox.WorkflowTypeService;
import org.egov.models.AbstractPersistenceServiceTest;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

public class CollectionsWorkflowRenderServiceTest extends
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
	}
}
