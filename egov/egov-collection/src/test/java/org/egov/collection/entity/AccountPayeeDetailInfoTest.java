package org.egov.collection.entity;


public class AccountPayeeDetailInfoTest { /* extends AbstractPersistenceServiceTest {
	private CollectionObjectFactory objectFactory;
	private EgovCommon egovCommon;
	
	@Before
	public void setUp() {
		objectFactory = new CollectionObjectFactory(session);
		
	}
	
	@Test
	public void testCreateAccountPayeeDetailInfoEx(){
		AccountPayeeDetail accountPayeeDetail = objectFactory.createAccountPayeeDetail();
		try{
		AccountPayeeDetailInfo accountPayeeDetailInfo = new AccountPayeeDetailInfo(accountPayeeDetail,egovCommon){
			public void populateEntityType(AccountPayeeDetail accountPayeeDetail,
					EgovCommon egovCommon) throws ApplicationException{
					throw new ApplicationException("Exception from test case");
			}
		};
		}
		catch(ApplicationRuntimeException ex){
			assertEquals("Could not get entity type for account detail type ["
							+ accountPayeeDetail.getAccountDetailType().getTablename()
							+ "], account detail key id ["
							+ accountPayeeDetail.getAccountDetailKey().getId()
							+ "]", ex.getMessage());
		}
	}
	
	@Test
	public void testCreateAccountPayeeDetailInfo() throws ApplicationException
	{
		AccountPayeeDetail accountPayeeDetail = objectFactory.createAccountPayeeDetail();
		egovCommon = createMock(EgovCommon.class);
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).
			andThrow(new ApplicationException("")).andReturn(null);
		AccountPayeeDetailInfo accountPayeeDetailInfo = new AccountPayeeDetailInfo(accountPayeeDetail,egovCommon);
		
		assertEquals(accountPayeeDetailInfo.getAccountDetailKey(),accountPayeeDetail.getAccountDetailKey());
		assertEquals(accountPayeeDetailInfo.getAccountDetailType(),accountPayeeDetail.getAccountDetailType());
		assertEquals(accountPayeeDetailInfo.getAccountDetailTypeName(),accountPayeeDetail.getAccountDetailType().getName());
		assertEquals(accountPayeeDetailInfo.getAmount(),accountPayeeDetail.getAmount());
		assertEquals(accountPayeeDetailInfo.getGlCode(),accountPayeeDetail.getReceiptDetail().getAccounthead().getGlcode());
		assertEquals(accountPayeeDetailInfo.getOrderNumber(),accountPayeeDetail.getReceiptDetail().getOrdernumber());
		assertNull(accountPayeeDetailInfo.getEntityType());
	}*/
}
