package org.egov.erpcollection.models;

import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccountPayeeDetailTest extends AbstractPersistenceServiceTest<AccountPayeeDetail,Long> {
	private CollectionObjectFactory factory;
	public AccountPayeeDetailTest() {
		this.type =AccountPayeeDetail.class;
	}
	@Before
	public void setUp() {
		factory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testcreateReceiptDetail()
	{
		Assert.assertNotNull(service.create(factory.createAccountPayeeDetail()));
	}
}
