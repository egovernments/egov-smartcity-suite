package org.egov.erpcollection.models;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReceiptDetailTest extends AbstractPersistenceServiceTest<ReceiptDetail,Long> {
	private CollectionObjectFactory factory;
	
	public ReceiptDetailTest() {		
		this.type = ReceiptDetail.class;
	}
	
	@Before
	public void setUp() {
		factory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testcreateReceiptDetail()
	{
		Assert.assertNotNull(service.create(factory.createReceiptDetail()));
	}
}
