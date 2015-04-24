package org.egov.erpcollection.web.actions.receipts;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.egov.commons.Bankaccount;
import org.egov.erpcollection.models.CollectionObjectFactory;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.models.AbstractPersistenceServiceTest;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;

public class AjaxBankRemittanceActionTest extends AbstractPersistenceServiceTest<ReceiptPayeeDetails,Long>{
	
	private AjaxBankRemittanceAction action;
	private CollectionObjectFactory objectFactory;
	
	@Before
	public void setupAction(){
		service = createMock(PersistenceService.class);
		action = new AjaxBankRemittanceAction();
		action.setPersistenceService(genericService);
		objectFactory = new CollectionObjectFactory(session);
		
	}
	
	@Test(expected = ValidationException.class)
	public void getBankBranchListWithErrors(){
		action.setFundId(0);
		action.bankBranchList();
	}
	
	@Test(expected = ValidationException.class)
	public void getAccountListWithErrors(){
		action.setFundId(0);
		action.accountList();
	}
	
	@Test
	public void getBankBranchList() throws Exception{
		ServiceDetails serviceDetails=objectFactory.createServiceDetails();
		action.setServiceName(serviceDetails.getServiceName());
		action.setFundId(serviceDetails.getServiceBankAccount().iterator().next().getFund().getId());
		Query qry=session.createSQLQuery("select bb.id as branchid,b.NAME||'-'||bb.BRANCHNAME as branchname from bank b,bankbranch bb, bankaccount ba," +
		"EG_BANKACCOUNTSERVICEMAPPING asm,eg_servicedetails sd where asm.BANKACCOUNTID=ba.ID and asm.SERVICEID=sd.ID and " +
		"ba.BRANCHID=bb.ID and bb.BANKID=b.ID and sd.SERVICENAME=:serviceName");
		qry.setString("serviceName", serviceDetails.getServiceName());
		
		String result = action.bankBranchList();
		assertNotNull(qry.list());
		assertNotNull(action.getServiceName());
		assertEquals("bankBranchList",result);
	}

	@Test
	public void getAccountList() throws Exception{
		ServiceDetails serviceDetails=objectFactory.createServiceDetails();
		action.setServiceName(serviceDetails.getServiceName());
		action.setFundId(serviceDetails.getServiceBankAccount().iterator().next().getFund().getId());
		
		Bankaccount bankaccount=objectFactory.createBankAccount("123456");
		action.setBranchId(bankaccount.getId());
		
		Query qry=session.createSQLQuery("select ba.id as accountid,ba.accountnumber as accountnumber from bankaccount ba," +
		"EG_BANKACCOUNTSERVICEMAPPING asm,eg_servicedetails sd where asm.BANKACCOUNTID=ba.ID and asm.SERVICEID=sd.ID and " +
		"ba.BRANCHID=:branchId and sd.SERVICENAME=:serviceName");
		qry.setString("serviceName", serviceDetails.getServiceName());
		qry.setInteger("branchId", bankaccount.getId());
		
		String result = action.accountList();
		assertNotNull(qry.list());
		assertNotNull(action.getServiceName());
		assertEquals("accountList",result);
	}
	
	@Test
	public void testGetModel() {
		assertNull(action.getModel());
	}
}
