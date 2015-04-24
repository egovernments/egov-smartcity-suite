package org.egov.collection.entity;

public class ReceiptPayeeDetailsTest { /*extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long> {
	private CollectionObjectFactory objectFactory;
	
	public ReceiptPayeeDetailsTest() {		
		this.type = ReceiptPayeeDetails.class;
	}
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testCreateReceiptPayeeDetails()
	{
		ReceiptPayeeDetails saved = service.create(objectFactory.createReceiptPayeeWithoutHeader());
		ReceiptPayeeDetails retrieved = service.findById(saved.getId(), false);
		assertEquals(saved,retrieved);
	}
	
	@Test
	public void testCreateReceiptPayeeDetailsWithHeader()
	{
		ReceiptPayeeDetails unsaved = service.create(objectFactory.createReceiptPayeeWithoutHeader());
		ReceiptHeader unsavedHeader = objectFactory.createUnsavedReceiptHeader();
		//unsavedHeader.setReceiptnumber("123456");
		unsaved.addReceiptHeader(unsavedHeader);
		ReceiptPayeeDetails saved = service.persist(unsaved);
		ReceiptPayeeDetails retrieved = service.findById(saved.getId(), false);
		assertEquals(saved,retrieved);
	}
	
	@Test
	public void testValidateReceiptPayeeDetails()
	{
		ReceiptPayeeDetails unsaved = service.create(objectFactory.createReceiptPayeeWithoutHeader());
		ReceiptHeader unsavedReceiptHeader = objectFactory.createUnsavedReceiptHeader();
		unsavedReceiptHeader.setReceiptnumber(null);
		unsaved.addReceiptHeader(unsavedReceiptHeader);
		
		List<ValidationError> errors = unsaved.validate();
		assertEquals(0, errors.size());
		//assertEquals("collection.receiptnum.null", errors.get(0).getMessage());
	}*/
}
