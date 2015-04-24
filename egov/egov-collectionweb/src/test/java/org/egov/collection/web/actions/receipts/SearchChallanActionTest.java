package org.egov.erpcollection.web.actions.receipts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.EgwStatus;
import org.egov.erpcollection.models.Challan;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class SearchChallanActionTest extends AbstractPersistenceServiceTest<ReceiptHeader,Long>{
	private SearchChallanAction action;
	private CollectionObjectFactory objectFactory;
	Map<String, String[]> parameters = new HashMap<String, String[]>();
	
	@Before
	public void setupAction(){
		action = new SearchChallanAction();
		action.setParameters(parameters);
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@After
	public void resetPrepare(){
		action.setParameters(null);
	}
	
	@Test
	public void testPrepareAction(){
		List<DepartmentImpl> actualDepartmentList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS);
		List<ServiceDetails> actualServiceList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_CHALLAN_SERVICES,CollectionConstants.CHALLAN_SERVICE_TYPE);
	
		action.prepare();
		assertEquals(actualDepartmentList,action.getDropdownData().get("departmentList"));
		assertEquals(actualServiceList,action.getDropdownData().get("serviceList"));
		
	}
	
	@Test
	public void searchWithNoParams() {
		try{
			String retResult = action.search();
			assertEquals(retResult,"success");
			action.getResults();
			assertNotNull(action.getResults());
		}
		catch(ValidationException e){
			List<ValidationError> errors = e.getErrors();
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"searchchallan.changecriteria");
			assertEquals(errors.get(0).getMessage(),"More than 500 results found.Please add more search criteria");
		}
	}
	
	@Test
	public void searchWithAllParams() {
		try{
		action.setChallanNumber("CN");
		action.setFromDate(new Date(2009,1,1));
		action.setToDate(new Date(2009,1,1));
		action.setStatus(659);
		action.setServiceId(1);
		action.setDepartmentId(1);
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getResults());
		}
		catch(ValidationException e){
			List<ValidationError> errors = e.getErrors();
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"searchchallan.changecriteria");
			assertEquals(errors.get(0).getMessage(),"More than 500 results found.Please add more search criteria");
		}
	}
	
	@Test
	public void searchWithOnlyChallanNumber() {
		try{
		action.setChallanNumber("RN");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getResults());
		}
		catch(ValidationException e){
			List<ValidationError> errors = e.getErrors();
			assertEquals(errors.size(),1);
			assertEquals(errors.get(0).getKey(),"searchchallan.changecriteria");
			assertEquals(errors.get(0).getMessage(),"More than 500 results found.Please add more search criteria");
		}
	}
	
	@Test
	public void testGetChallanStatuses() {
		List<EgwStatus> actualStatuses=genericService.findAllBy(
				"from EgwStatus s where moduletype=? order by description",
				Challan.class.getSimpleName());
		
		List<EgwStatus> expectedStatuses=action.getChallanStatuses();
		assertTrue(expectedStatuses.containsAll(actualStatuses));
	}
	
	@Test
	public void testModel(){
		assertNull(action.getModel());
	}
	
	@Test
	public void testReset(){
		assertEquals(action.reset(),Action.SUCCESS);
		assertEquals(action.getServiceId(),-1);
		assertEquals(action.getStatus(),-1);
		assertEquals(action.getChallanNumber(),CollectionConstants.BLANK);
		assertNull(action.getResults());
		assertNull(action.getFromDate());
		assertNull(action.getToDate());
	}
}
