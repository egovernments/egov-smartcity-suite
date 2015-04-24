package org.egov.erpcollection.models;

import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReceiptMiscTest extends
		AbstractPersistenceServiceTest<ReceiptMisc, Long> {
	private CollectionObjectFactory factory;

	public ReceiptMiscTest() {
		this.type = ReceiptMisc.class;
	}

	@Before
	public void setUp() {
		factory = new CollectionObjectFactory(session);
	}

	@Test
	public void testcreateReceiptMisc() {
		Assert.assertNotNull(service.create(factory.createReceiptMis()));
	}
}
