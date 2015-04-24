package org.egov.erpcollection.web.actions.receipts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Test;

public class ReceiptNumberSearchActionTest extends AbstractPersistenceServiceTest<ReceiptHeader,Long>{
	
	private ReceiptNumberSearchAction action;
	private CollectionObjectFactory objectFactory;
	private ReceiptHeaderService receiptService;   
	
	
	public ReceiptNumberSearchActionTest() {
		this.type = ReceiptHeader.class;
	}
	
	@Before
	public void setUp() throws Exception {
		action = new ReceiptNumberSearchAction();
		receiptService = new ReceiptHeaderService();
		receiptService.setType(ReceiptHeader.class);
		receiptService.setSessionFactory(egovSessionFactory);
		action.setReceiptHeaderService(receiptService);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void getReceiptNumberList() throws Exception{
		objectFactory.createReceiptHeader("22222");
		objectFactory.createReceiptHeader("248499");
		action.setQuery("2");
		assertNotNull(action.getQuery());
		assertEquals(false,action.getReceiptNumberList().isEmpty());
	}
	
	@Test
	public void testGetModel() {
		assertNull(action.getModel());
	}
	
	@Test
	public void testsearchAjax()
	{
		assertEquals("searchResults", action.searchAjax());
	}

}
