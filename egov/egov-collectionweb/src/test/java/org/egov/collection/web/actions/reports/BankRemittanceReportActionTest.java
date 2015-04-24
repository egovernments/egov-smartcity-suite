/**
 * 
 */
package org.egov.erpcollection.web.actions.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.util.CollectionsUtil;
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
public class BankRemittanceReportActionTest extends
		AbstractPersistenceServiceTest {
	
	private BankRemittanceReportAction action;
	private CollectionObjectFactory objectFactory;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setupAction() {
		objectFactory = new CollectionObjectFactory(session);
		
		action = new BankRemittanceReportAction();
		action.setPersistenceService(genericService);
		action.setSession(new HashMap());
	}
	
	@Test
	public void testPrepare() {
		action.prepare();
		Assert.assertEquals(FileFormat.PDF, action.getReportFormat());
		Assert.assertEquals(ReportDataSourceType.HQL, action.getDataSourceType());
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
	}
	
	@Test
	public void testGetReportTemplate() {
		Assert.assertEquals("bank_remittance", action.getReportTemplateName());
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
	}
}
