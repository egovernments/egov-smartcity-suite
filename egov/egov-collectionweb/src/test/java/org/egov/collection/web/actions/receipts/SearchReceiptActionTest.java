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
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.security.terminal.model.Location;
import org.egov.model.instrument.InstrumentType;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchReceiptActionTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private SearchReceiptAction action;
	private CollectionObjectFactory objectFactory;
	Map<String, String[]> parameters = new HashMap<String, String[]>();
	
	@Before
	public void setupAction(){
		action = new SearchReceiptAction();
		action.setParameters(parameters);
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@After
	public void resetPrepare(){
		action.setParameters(null);
	}
	
	@Test
	public void prepareAction() {
		assertNotNull(action.getDropdownData().containsKey("serviceTypeList"));
	}
	
	@Test
	public void testPrepareAction(){
		List<Location> actualCounterList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_ACTIVE_COUNTERS);
		List<InstrumentType> actualInstrumentTypeList = genericService.findAllBy("from InstrumentType i where i.isActive = 1 order by type");
		List<InstrumentType> actualUserList = genericService.findAllByNamedQuery(CollectionConstants.QUERY_CREATEDBYUSERS_OF_RECEIPTS);
		
		action.prepare();
		assertEquals(actualCounterList,action.getDropdownData().get("counterList"));
		assertEquals(actualInstrumentTypeList,action.getDropdownData().get("instrumentTypeList"));
		assertEquals(actualUserList,action.getDropdownData().get("userList"));
	}
	
	@Test
	public void testSearchByUser(){
		objectFactory.createReceiptPayeeDetails();
		action.prepare();
		int userId = ((UserImpl)action.getDropdownData().get("userList").get(0)).getId();
		action.setUserId(userId);
		action.search();
		List<ReceiptHeader> actualSearchResults = genericService.findAllBy(
				"from org.egov.erpcollection.models.ReceiptHeader receipt where receipt.createdBy.id = ?", userId);
		assertTrue(actualSearchResults.containsAll(action.getSearchResult().getList()));
		assertEquals(action.getTarget(),"searchresult");
	}
	
	
	@Test
	public void searchWithOnlyReceiptNumber() {
		action.setReceiptNumber("RN");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithSomeParams() {
		action.setReceiptNumber("RN");
		action.setSearchStatus(659);
		action.setServiceTypeId(1);
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithAllParams() {
		action.setReceiptNumber("RN");
		action.setFromDate(new Date(2009,1,1));
		action.setToDate(new Date(2009,1,1));
		action.setSearchStatus(659);
		action.setCounterId(4);
		action.setServiceTypeId(1);
		action.setInstrumentType("cheque");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithInstrumentType() {
		action.setInstrumentType("cheque");
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void searchWithNoParams() {
		String retResult = action.search();
		assertEquals(retResult,"success");
		assertNotNull(action.getSearchResult());
	}
	
	@Test
	public void testGetReceiptStatuses() {
		List<EgwStatus> expectedStatuses=genericService.findAllBy(
				"from EgwStatus s where moduletype=? and code != ? order by description",
				ReceiptHeader.class.getSimpleName(), CollectionConstants.RECEIPT_STATUS_CODE_PENDING);
		
		List<EgwStatus> actualStatuses=action.getReceiptStatuses();
		assertTrue(actualStatuses.containsAll(expectedStatuses));
	}
	
	@Test 
	public void testReset()
	{
		assertEquals("success", action.reset());
	}
	
	@Test
	public void testGetModel()
	{
		assertNull(action.getModel());
	}
}
