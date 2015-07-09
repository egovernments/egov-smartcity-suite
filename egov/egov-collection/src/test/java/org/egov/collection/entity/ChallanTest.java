package org.egov.collection.entity;

public class ChallanTest  { /*extends AbstractPersistenceServiceTest<Challan,Long> {
	private CollectionObjectFactory objectFactory;
	
	public ChallanTest(){
	}
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
	}
	
	@Test
	public void testCreateChallan()
	{
		Challan saved = (Challan) service.create( objectFactory.createChallan());
		Challan retrieved = (Challan) service.findById(saved.getId(), false);
		assertEquals(saved,retrieved);
	}
	
	@Test
	public void testGetStateDetails(){
		assertEquals(new Challan().getStateDetails(),"Challan - null");
	}
	
	@Test
	public void testVoucherHeaderForVoucherNumber(){
		CVoucherHeader voucher = objectFactory.createVoucher("testVoucherHeader");
		CVoucherHeader expected = (CVoucherHeader) genericService.findByNamedQuery("getVoucherHeaderByVoucherNumber", voucher.getVoucherNumber());
		assertEquals(expected, voucher);
	}
	*/
}
