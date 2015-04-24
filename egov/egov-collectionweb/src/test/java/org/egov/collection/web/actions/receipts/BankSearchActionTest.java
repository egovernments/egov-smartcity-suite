package org.egov.erpcollection.web.actions.receipts;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.egov.commons.Bank;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Test;

public class BankSearchActionTest extends AbstractPersistenceServiceTest<Bank,Integer>{
	
	private BankSearchAction action;
	private CollectionObjectFactory objectFactory;
	
	public BankSearchActionTest() {
		this.type = Bank.class;
	}
	
	@Before
	public void setUp() throws Exception {
		action = new BankSearchAction();
		action.setBankService(service);
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void getBankList() throws Exception{
		objectFactory.createBank();
		action.setQuery("t");
		Collection<Bank> result = action.getBankList();
		assertEquals(false,result.isEmpty());
	}
	
	@Test
	public void testSearchAjax() throws Exception{
		assertEquals("searchResults",action.searchAjax());
	}

}
