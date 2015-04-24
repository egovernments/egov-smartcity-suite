package org.egov.erpcollection.web.actions.citizen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.EgwStatus;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.OnlinePayment;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.web.actions.BaseFormAction;
import org.junit.Before;
import org.junit.Test;

public class SearchOnlineReceiptActionTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private SearchOnlineReceiptAction action;
	private CollectionObjectFactory objectFactory;
	
	@Before
	public void setupAction(){
		action = new SearchOnlineReceiptAction();
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testGetOnlineReceiptTransitionStatuses(){
		List<EgwStatus> expectedStatuses = action.getOnlineReceiptTransitionStatuses();
		
		List<String> statusCodes = new ArrayList<String>();
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS);
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED);
		statusCodes.add(CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED);
		List<EgwStatus> actualStatuses = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_STATUSES_FOR_MODULE_AND_CODES, 
				OnlinePayment.class.getSimpleName(),
				statusCodes);
		
		assertEquals(expectedStatuses, actualStatuses);
	}
	
	@Test
	public void testGetOnlineReceiptStatuses(){
		List<EgwStatus> expectedStatuses = action.getOnlineReceiptStatuses();
		List<EgwStatus> actualStatuses = genericService.findAllByNamedQuery(
				CollectionConstants.QUERY_ALL_STATUSES_FOR_MODULE, 
				OnlinePayment.class.getSimpleName());
		
		assertEquals(expectedStatuses, actualStatuses);
	}
	
	@Test
	public void testSearch(){
		OnlinePayment onlinePayment = objectFactory.createOnlinePayment();
		
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		action.setParameters(parameters);
		action.prepare();
		
		action.setSearchTransactionStatus(onlinePayment.getStatus().getId());
		action.setReferenceId(onlinePayment.getReceiptHeader().getId());
		action.setServiceTypeId(onlinePayment.getReceiptHeader().getService().getId());
		
		Calendar newTodate= Calendar.getInstance();
		newTodate.setTime(onlinePayment.getCreatedDate());
		newTodate.add(Calendar.DATE, -1);
		action.setFromDate(newTodate.getTime());
		
		action.setToDate(onlinePayment.getCreatedDate());
		
		assertEquals(action.search(),BaseFormAction.SUCCESS);
		
		assertTrue(action.getResults().contains(onlinePayment));
		assertEquals(action.getTarget(),"searchresult");
	}
	
	@Test
	public void testSearchWithOnlyReferenceId() {
		OnlinePayment onlinePayment = objectFactory.createOnlinePayment();
		action.setReferenceId(onlinePayment.getReceiptHeader().getId());
		
		assertEquals(action.search(),BaseFormAction.SUCCESS);
		assertTrue(action.getResults().contains(onlinePayment));
	}
	
	@Test
	public void testSearchWithOnlyServiceId() {
		OnlinePayment onlinePayment = objectFactory.createOnlinePayment();
		action.setServiceTypeId(onlinePayment.getReceiptHeader().getService().getId());
		
		assertEquals(action.search(),BaseFormAction.SUCCESS);
		assertTrue(action.getResults().contains(onlinePayment));
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
