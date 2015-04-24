/**
 * 
 */
package org.egov.erpcollection.web.actions.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.lib.rjbac.dept.Department;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.web.actions.BaseFormAction;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the bank remittance report action
 */
public class PendingChallansReportActionTest extends
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
}
