/**
 * 
 */
package org.egov.erpcollection.web.actions.reports;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easymock.classextension.EasyMock;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.infstr.cache.LRUCache;
import org.egov.infstr.reporting.engine.ReportConstants;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.lib.rjbac.dept.Department;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.web.actions.BaseFormAction;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for collection summary report action
 */
@SuppressWarnings("unchecked")
public class CollectionSummaryActionTest extends
		AbstractPersistenceServiceTest {
	private CollectionSummaryAction action;
	private ReportService reportService;
	private CollectionObjectFactory objectFactory;
	private Map actionSession = new HashMap();

	@Before
	public void setupAction() {
		objectFactory = new CollectionObjectFactory(session);
		
		action = new CollectionSummaryAction();
		action.setPersistenceService(genericService);
		action.setSession(actionSession);

		action.prepare();
	}

	@Test
	public void testGetModel() {
		assertNull(action.getModel());
	}

	@Test
	public void testSetGet() {
		Integer deptId = 1;
		Date fromDate = new Date();
		Date toDate = new Date();
		action.setDeptId(deptId);
		action.setFromDate(fromDate);
		action.setToDate(toDate);
		assertEquals(deptId, action.getDeptId());
		assertEquals(fromDate, action.getFromDate());
		assertEquals(toDate, action.getToDate());
	}

	@Test
	public void testCriteria() {
		CollectionsUtil collectionsUtilMock = EasyMock.createMock(CollectionsUtil.class);
		action.setCollectionsUtil(collectionsUtilMock);
		
		Department dept = objectFactory.createDept("test.dept");
		EasyMock.expect(collectionsUtilMock.getDepartmentOfLoggedInUser(EasyMock.isA(Map.class))).andReturn(dept);
		EasyMock.replay(collectionsUtilMock);
		
		String critResult = action.criteria();
		EasyMock.verify(collectionsUtilMock);
		
		assertEquals(critResult, BaseFormAction.INDEX);
		assertEquals(dept.getId(), action.getDeptId());
		assertNotNull(action.getFromDate());
		assertNotNull(action.getToDate());
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
}
