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
