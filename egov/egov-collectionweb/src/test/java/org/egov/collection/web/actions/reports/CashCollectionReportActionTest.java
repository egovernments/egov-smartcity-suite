/**
 * 
 */
package org.egov.collection.web.actions.reports;


/**
 * JUnit test cases for cash collection report action
 */
@SuppressWarnings("unchecked")
public class CashCollectionReportActionTest {/* extends
		AbstractPersistenceServiceTest {
	private CashCollectionReportAction action;
	private CollectionsUtil collectionsUtil;
	private ReportService reportService;
	private Map actionSession = new HashMap();

	@Before
	public void setupAction() {
		action = new CashCollectionReportAction();
		action.setPersistenceService(genericService);
		action.setSession(actionSession);

		collectionsUtil = EasyMock.createMock(CollectionsUtil.class);
		action.setCollectionsUtil(collectionsUtil);

		EasyMock.expect(collectionsUtil.getAllCounters()).andReturn(
				new ArrayList<Location>());
		EasyMock.expect(collectionsUtil.getReceiptCreators()).andReturn(
				new ArrayList<User>());
		EasyMock.expect(collectionsUtil.getReceiptZoneList()).andReturn(
				new ArrayList<Boundary>());
		EasyMock.replay(collectionsUtil);
		action.prepare();
	}

	@Test
	public void testGetModel() {
		assertNull(action.getModel());
	}

	@Test
	public void testPrepare() {
		EasyMock.verify(collectionsUtil);
		assertEquals(action.getCounterId(), -1);
		assertEquals(action.getUserId(), -1);
		assertNotNull(action.getFromDate());
		assertNotNull(action.getToDate());
		assertNull(action.getInstrumentStatus());
		assertEquals(action.getBoundaryId(), -1);
	}

	@Test
	public void testSetGet() {
		Long counterId = 10L;
		Long userId = 20L;
		Date fromDate = new Date();
		Date toDate = new Date();
		Long boundaryId = 20L;
		action.setCounterId(counterId);
		action.setUserId(userId);
		action.setFromDate(fromDate);
		action.setToDate(toDate);
		action.setInstrumentStatus(CollectionConstants.INSTRUMENT_NEW_STATUS);
		action.setBoundaryId(boundaryId);
		assertEquals(counterId, action.getCounterId());
		assertEquals(userId, action.getUserId());
		assertEquals(fromDate, action.getFromDate());
		assertEquals(toDate, action.getToDate());
		assertEquals(CollectionConstants.INSTRUMENT_NEW_STATUS, action
				.getInstrumentStatus());
		assertEquals(boundaryId, action.getBoundaryId());
	}

	@Test
	public void testCriteria() {
		assertEquals(BaseFormAction.INDEX, action.criteria());
	}

	@Test
	public void testReport() {
		ReportOutput reportOutput = new ReportOutput();
		reportService = createMock(ReportService.class);
		action.setReportService(reportService);

		expect(reportService.createReport(isA(ReportRequest.class))).andReturn(
				reportOutput);

		replay(reportService);
		assertEquals(CashCollectionReportAction.REPORT, action.report());
		
		LRUCache<Integer, ReportOutput> reportOutputCache = (LRUCache<Integer, ReportOutput>)actionSession
				.get(ReportConstants.ATTRIB_EGOV_REPORT_OUTPUT_MAP);
		assertTrue(reportOutputCache.containsValue(reportOutput));
		verify(reportService);
	}
*/}
