package org.egov.erpcollection.models;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.egov.EGOVException;
import org.egov.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.egf.commons.EgovCommon;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Before;
import org.junit.Test;

public class AccountPayeeDetailInfoTest extends AbstractPersistenceServiceTest {
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
					EgovCommon egovCommon) throws EGOVException{
					throw new EGOVException("Exception from test case");
			}
		};
		}
		catch(EGOVRuntimeException ex){
			assertEquals("Could not get entity type for account detail type ["
							+ accountPayeeDetail.getAccountDetailType().getTablename()
							+ "], account detail key id ["
							+ accountPayeeDetail.getAccountDetailKey().getId()
							+ "]", ex.getMessage());
		}
	}
	
	@Test
	public void testCreateAccountPayeeDetailInfo() throws EGOVException
	{
		AccountPayeeDetail accountPayeeDetail = objectFactory.createAccountPayeeDetail();
		egovCommon = createMock(EgovCommon.class);
		EasyMock.expect(egovCommon.getEntityType(EasyMock.isA(Accountdetailtype.class), EasyMock.isA(Integer.class))).
			andThrow(new EGOVException("")).andReturn(null);
		AccountPayeeDetailInfo accountPayeeDetailInfo = new AccountPayeeDetailInfo(accountPayeeDetail,egovCommon);
		
		assertEquals(accountPayeeDetailInfo.getAccountDetailKey(),accountPayeeDetail.getAccountDetailKey());
		assertEquals(accountPayeeDetailInfo.getAccountDetailType(),accountPayeeDetail.getAccountDetailType());
		assertEquals(accountPayeeDetailInfo.getAccountDetailTypeName(),accountPayeeDetail.getAccountDetailType().getName());
		assertEquals(accountPayeeDetailInfo.getAmount(),accountPayeeDetail.getAmount());
		assertEquals(accountPayeeDetailInfo.getGlCode(),accountPayeeDetail.getReceiptDetail().getAccounthead().getGlcode());
		assertEquals(accountPayeeDetailInfo.getOrderNumber(),accountPayeeDetail.getReceiptDetail().getOrdernumber());
		assertNull(accountPayeeDetailInfo.getEntityType());
	}
}
