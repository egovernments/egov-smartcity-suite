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
public class OnlineTransactionReportActionTest extends
		AbstractPersistenceServiceTest {
	
	private OnlineTransactionReportAction action;
	private CollectionObjectFactory objectFactory;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setupAction() {
		objectFactory = new CollectionObjectFactory(session);
		
		action = new OnlineTransactionReportAction();
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
		Long onlineTransactionStataId = 1L;
		action.setStatusId(onlineTransactionStataId);
		assertEquals(onlineTransactionStataId, action.getStatusId());
		
		Integer departmentId = 1;
		action.setDepartmentId(departmentId);
		assertEquals(departmentId, action.getDepartmentId());
		
		Long billingServiceId = 1L;
		action.setBillingServiceId(billingServiceId);
		assertEquals(billingServiceId, action.getBillingServiceId());
		
		Date testDate = new Date();
		action.setFromDate(testDate);
		assertEquals(testDate, action.getFromDate());
		
		action.setToDate(testDate);
		assertEquals(testDate, action.getToDate());
	}
	
	@Test
	public void testGetReportTemplate() {
		Assert.assertEquals(CollectionConstants.REPORT_TEMPLATE_ONLINE_TRANSACTION, action.getReportTemplateName());
	}

	@Test
	public void testCriteria() {
		// Create mock object of collections util and set expectations
		CollectionsUtil collectionsUtilMock = EasyMock.createMock(CollectionsUtil.class);
		action.setCollectionsUtil(collectionsUtilMock);
		
		List<ServiceDetails> billingServiceList = new ArrayList<ServiceDetails>();
		EasyMock.expect(collectionsUtilMock.getBillingServiceList()).andReturn(billingServiceList);
		
		Department dept = objectFactory.createDept("test.dept");
		
		EasyMock.replay(collectionsUtilMock);
		
		// Test the criteria method
		String critResult = action.criteria();
		EasyMock.verify(collectionsUtilMock);
		
		assertEquals(critResult, BaseFormAction.INDEX);
		assertTrue(action.getDropdownData().get(CollectionConstants.DROPDOWN_DATA_SERVICE_LIST).equals(billingServiceList));
		assertTrue(action.getDropdownData().get("departmentList").contains(dept));
		
		Date fromDate = action.getFromDate();
	}
}
